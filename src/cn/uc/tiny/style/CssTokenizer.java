// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package cn.uc.tiny.style;

import cn.uc.build.Config;

/**
 * CSS tokenizer, used internally by Style and StyleSheet. The design of this
 * class is kept as close to java.io.StreamTokenizer as possible.
 * 
 * <p>
 * About CSS tokenization, please refer to : <a href="http://www.w3.org/TR/2010
 * /WD-CSS2-20101207/syndata.html#tokenization">CSS Tokenization</a>
 * </p>
 * 
 * <p>
 * <b>Modifications From Original</b>
 * </p>
 * 
 * <ul>
 * <li>Remove HtmlWidget member
 * <li>Add DEBUG flag to control debug output
 * <li>Fixed the bug that we will need to check EOF again when skip space char
 * <li>Fixed the bug when read url, use {@link #assertChar(int)} to replace
 * {@link #assertTokenType(int)}
 * <li>Add CDO/CDC tokens type support
 * <li>Add property name start with '-' prefix support
 * <li>Add two values separated by '/' support, like 'font: x-large/110%'
 * </ul>
 * 
 * @author Stefan Haustein
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 */
public class CssTokenizer {
	
	/** DEBUG flag, do not commit the code with TRUE flag!!!*/
	public static final boolean DEBUG = Config.STYLE_DEBUG;

	/**
	 * ttype (token type) value when the end of input has been reached.
	 */
	public static final int TT_EOF = -1;

	/**
	 * ttype value when the current token is an identifier.
	 */
	public static final int TT_IDENT = -2;

	/**
	 * ttype value when the current token is of the form @{ident} .
	 */
	public static final int TT_ATKEYWORD = -3;

	/**
	 * ttype value when the current token is a STRING token, as defined in the
	 * CSS specification (a text sting enclosed in single or double quotes).
	 */
	public static final int TT_STRING = -4;

	/**
	 * ttype value when the current token is a HASH token, as defined in the CSS
	 * specification (#{name}).
	 */
	public static final int TT_HASH = -6;

	/**
	 * ttype value when the current token is a number
	 */
	public static final int TT_NUMBER = -7;

	/**
	 * ttype value when the current token is a dimension.
	 */
	public static final int TT_PERCENTAGE = -8;

	/**
	 * ttype value when the current token is a dimension.
	 */
	public static final int TT_DIMENSION = -9;

	/**
	 * ttype value when the current token is a dimension.
	 */
	public static final int TT_URI = -10;

	/**
	 * ttype value if the current token is whitespace.
	 */
	public static final int TT_S = -14;

	/**
	 * ttype value when the current token is an identifier followed by an
	 * opening bracket.
	 */
	public static final int TT_FUNCTION = -16;

	/**
	 * ttype value when the current token is '~='.
	 */
	public static final int TT_INCLUDES = -17;

	/**
	 * ttype value when the current token is '|='.
	 */
	public static final int TT_DASHMATCH = -18;

	/**
	 * ttype value when the current token is '<!--'.
	 */
	public static final int TT_CDO = -19;

	/**
	 * ttype value when the current token is '-->'.
	 */
	public static final int TT_CDC = -20;

	private static final int UNINITIALIZED = -99;

	/**
	 * Characters interpreted on its own or terminating an identifier
	 */
	private static final String TOKENS = "~|<>+*()[]{}.,;*:%=!@#";

	/**
	 * Current token type (One of the TT_XXX constants)
	 */
	public int ttype;

	/**
	 * Current string value. Valid for TT_IDENT, TT_ATKEYWORD, TT_STRING,
	 * TT_HASH, TT_DIMENSION, TT_URI, TT_PERCENT and TT_FUNCTION. Never contains
	 * the constant part of a token (string quotes, @-prefix, "url(" etc.)
	 * except for TT_PERCENT, where sval contains the percent character '%'.
	 */
	public String sval;

	/**
	 * Current numeric value, multiplied with 1000. Valid for TT_NUMBER,
	 * TT_PERCENTAGE and TT_DIMENSION.
	 */
	public int nval;
	
	/** Current parsing CSS stylesheet's url. */
	public String url;

	private String cssString;
	private int pos;
	private int nextChar = UNINITIALIZED;
	private int lineNumber;

	/**
	 * Constructs the tokenizer for the given text.
	 * @param url the url of CSS stylesheet for parsing
	 */
	public CssTokenizer(String url, String cssString) {

		this.url = url;
		this.cssString = cssString;

		nextToken(false);
	}

	/**
	 * Moves to the next token and returns the token type (one of the TT
	 * constants).
	 * 
	 * @param reportWhitespace if true, white space is reported (ignored
	 *            otherwise)
	 */
	public int nextToken(boolean reportWhitespace) {

		sval = "";
		nval = 0;

		if (nextChar == UNINITIALIZED) {
			nextChar = read();
		}

		if (nextChar == -1) {
			ttype = TT_EOF;
			return TT_EOF;
		}

		// handle white space
		if (nextChar <= ' ') {

			skipWhitespace();
			if (reportWhitespace) {
				ttype = TT_S;
				return ttype;
			}

			//check EOF again
			if (nextChar == -1) {
				ttype = TT_EOF;
				return TT_EOF;
			}
		}

		switch (nextChar) {

		case '.':
			nextChar = read();
			if (nextChar < '0' || nextChar > '9') {
				ttype = '.';
			}
			else {
				readNumeric(true);
			}
			break;

		case '@':
			nextChar = read();
			sval = readIdentifier();
			ttype = TT_ATKEYWORD;
			break;

		case '#':
			StringBuffer hash = new StringBuffer();
			nextChar = read();
			do {
				hash.append((char) nextChar);
				nextChar = read();
			} while ((nextChar >= '0' && nextChar <= '9') ||
				(nextChar >= 'a' && nextChar <= 'z') ||
				(nextChar >= 'A' && nextChar <= 'Z') || nextChar == '-' ||
				nextChar == '_');
			ttype = TT_HASH;
			sval = hash.toString();
			break;

		case '/':
			nextChar = read();
			switch (nextChar) {
			case '/':
				do {
					nextChar = read();
				} while (nextChar != '\n' && nextChar != '\r' && nextChar != -1);
				return nextToken(reportWhitespace);

			case '*':
				int prev;
				nextChar = read();
				do {
					prev = nextChar;
					nextChar = read();
					if (nextChar == '\n') {
						lineNumber++;
					}
				} while (nextChar != -1 && (prev != '*' || nextChar != '/'));

				nextChar = UNINITIALIZED;
				return nextToken(reportWhitespace);

			default:
				ttype = '/';
			}
			break;

		case '\'':
			nextChar = read();
			sval = readString('\'');
			ttype = TT_STRING;
			break;

		case '"':
			nextChar = read();
			sval = readString('\"');
			ttype = TT_STRING;
			break;

		case '<':// handle CDO '<!--'
			if ((nextChar = read()) == '!' && (nextChar = read()) == '-' &&
				(nextChar = read()) == '-') {

				nextChar = read();
				sval = "<!--";
				ttype = TT_CDO;
			}
			else {

				if (nextChar != '<') {
					nextChar = UNINITIALIZED;
				}

				return nextToken(reportWhitespace);
			}
			break;

		case '-':
			if ((nextChar = read()) == '-' && (nextChar = read()) == '>') {

				nextChar = read();
				sval = "-->";
				ttype = TT_CDC;
			}
			else if (nextChar >= '0' && nextChar <= '9') { //number or identifier

				readNumeric(false);
				nval = -nval;
			}
			else if (nextChar == '-') {

				return nextToken(reportWhitespace);
			}
			else {

				sval = '-' + this.readIdentifier();
				ttype = TT_IDENT;
			}
			break;

		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			readNumeric(false);
			break;

		case '~':
			nextChar = read();
			if (nextChar == '=') {
				ttype = TT_INCLUDES;
				nextChar = UNINITIALIZED;
			}
			else {
				ttype = '~';
			}
			break;

		case '|':
			nextChar = read();
			if (nextChar == '=') {
				ttype = TT_DASHMATCH;
				nextChar = UNINITIALIZED;
			}
			else {
				ttype = '~';
			}
			break;

		default:
			if (TOKENS.indexOf((char) nextChar) != -1) {

				ttype = nextChar;
				nextChar = UNINITIALIZED;
			}
			else {

				sval = readIdentifier();

				if (nextChar == '(') {
					if (sval.equals("url")) {
						nextChar = read();
						skipWhitespace();
						switch (nextChar) {
						case '\'':
							nextChar = read();
							sval = readString('\'');
							skipWhitespace();
							assertChar(')');
							nextChar = UNINITIALIZED;
							break;
						case '\"':
							nextChar = read();
							sval = readString('\"');
							skipWhitespace();
							assertChar(')');
							nextChar = UNINITIALIZED;
							break;
						default:
							sval = readString(')');
							break;
						}
						ttype = TT_URI;
					}
					else {
						ttype = TT_FUNCTION;
						nextChar = UNINITIALIZED;
					}
				}
				else {
					ttype = TT_IDENT;
				}
			}
		}

		//debug output
		if (DEBUG) {
			debug(sval.length() > 0 ? sval : ("" + (char) ttype));
		}

		return ttype;
	}

	/**
	 * Reads and returns an identifier. Must be on first char.
	 */
	private String readIdentifier() {

		StringBuffer sb = new StringBuffer();
		do {
			if (nextChar == '\\') {
				sb.append(readEscape());
			}
			else {
				sb.append((char) nextChar);
				nextChar = read();
			}
		} while (TOKENS.indexOf(nextChar) == -1 && nextChar > ' ' &&
			nextChar != '/');

		return sb.toString();
	}

	/**
	 * Skips whitespace, if present.
	 */
	private void skipWhitespace() {

		while (nextChar <= ' ' && nextChar != -1) {
			if (nextChar == '\n') {
				lineNumber++;
			}
			nextChar = read();
		}
	}

	/**
	 * Reads a string, starting with the current nextChar value, until reaching
	 * the endquote char. The endquote char is consumed but not appended to the
	 * string. The string is returned.
	 */
	private String readString(char quoteChar) {

		StringBuffer sb = new StringBuffer();
		while (nextChar != quoteChar && nextChar != -1 && nextChar != 10 &&
			nextChar != 13) {
			if (nextChar == '\\') {
				sb.append(readEscape());
			}
			else {
				sb.append((char) nextChar);
				nextChar = read();
			}
		}
		nextChar = UNINITIALIZED;
		return sb.toString();
	}

	/**
	 * Reads a character escape (up to six hex digits)
	 */
	private char readEscape() {

		int digitCount = 0;
		int result = 0;
		nextChar = read();

		while (digitCount < 6 &&
			((nextChar >= '0' && nextChar <= '9') ||
				(nextChar >= 'a' && nextChar <= 'z') || (nextChar >= 'A' && nextChar <= 'Z'))) {
			digitCount++;
			result = result * 16 + Character.digit((char) nextChar, 16);
			nextChar = read();
		}
		if (digitCount == 0) {
			result = nextChar;
			nextChar = read();
		}
		else if (nextChar <= ' ') {
			nextChar = read();
		}
		return (char) result;
	}

	/**
	 * Reads a numeric value (e.g. 0.3, .3, 999) and writes the value * 1000 to
	 * nval. If then number is followed by % or an identifier, the unit is read
	 * to sval. The token type is set to TT_NUMBER, TT_PERCENT or TT_DIMENSION
	 * accordingly.
	 * 
	 * @param start prefix for the number to be parsed.
	 */
	private void readNumeric(boolean fraction) {

		long val = 0;
		long div = fraction ? 1 : 0;
		do {
			if (nextChar == '.') {
				div = 1;
			}
			else {
				// add the next digit 
				val = val * 10 + nextChar - '0';
				div *= 10;
			}
			nextChar = read();
		} while ((nextChar >= '0' && nextChar <= '9') || nextChar == '.');

		val = val * 1000;
		if (div != 0) {
			val /= div;
		}
		nval = (int) val;

		if (nextChar == '%') {

			ttype = TT_PERCENTAGE;
			nextChar = UNINITIALIZED;
			sval = "%";
		}
		else if ((nextChar >= 'a' && nextChar <= 'z') ||
			(nextChar >= 'A' && nextChar <= 'Z')) {

			StringBuffer sb = new StringBuffer();
			do {
				sb.append((char) nextChar);
				nextChar = read();
			} while ((nextChar >= 'a' && nextChar <= 'z') ||
				(nextChar >= 'A' && nextChar <= 'Z'));

			ttype = TT_DIMENSION;
			sval = sb.toString();
		}
		else {

			ttype = TT_NUMBER;
			sval = "";
		}
	}

	private int read() {

		return pos >= cssString.length() ? -1 : cssString.charAt(pos++);
	}

	/**
	 * Asserts the given token type.
	 */
	public void assertTokenType(int c) {

		if (DEBUG && ttype != c) {
			debug("Expected: " + (char) c);
		}
	}

	/**
	 * Assert the given char equals to {@link #nextChar}
	 */
	private void assertChar(int c) {

		if (DEBUG && nextChar != c) {
			debug("Expected char: " + (char) c);
		}
	}

	/**
	 * Prints a message and tokenizer state information to STDERR.
	 * 
	 * @param msg the message to print
	 */
	public void debug(String msg) {

		if (!DEBUG)
			return;
		
	    //#if (debug == true)
		
		System.out.print("[" + msg + "] line: " + lineNumber + "; token: {");
		switch (ttype) {
		case TT_IDENT:
			System.out.print("IDENT} '" + sval + "'");
			break;
		case TT_STRING:
			System.out.print("STRING} '" + sval + "'");
			break;
		case TT_URI:
			System.out.print("URI} '" + sval + "'");
			break;
		case TT_HASH:
			System.out.print("HASH CLASS/COLOR} '" + sval + "'");
			break;
		case TT_FUNCTION:
			System.out.print("FUNC} '" + sval + "'");
			break;
		case TT_CDO:
			System.out.print("CDO} '" + sval + "'");
			break;
		case TT_CDC:
			System.out.print("CDC} '" + sval + "'");
			break;
		case TT_NUMBER:
		case TT_DIMENSION:
		case TT_PERCENTAGE:
			System.out.print("NUMERIC} " + nval + " unit: " + sval);
			break;
		default:
			if (ttype <= ' ') {
				System.out.print(ttype);
			}
			else {
				System.out.print("P-MARK} '" + (char) ttype + "'");
			}
		}
		System.out.println(";");
		
		//#endif
	}
}
