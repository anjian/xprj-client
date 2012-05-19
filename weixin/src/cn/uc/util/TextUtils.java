/**
 * Tiny.cn.uc.util.TextUtils.java, 2011-1-24
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

/**
 * TextUtils is an utility class that help to handle text (characters, words,
 * strings, etc...), part of its APIs are source from JavaSE SDK (Apache
 * Harmony) like {@link Character}, and from Apache Commons Lang like
 * {@link CharUtils}, {@link WordUtils} and {@link StringUtils}.
 * 
 * <p>
 * <b>About Unicode Encoding</b>, please refer to :
 * <ul>
 * <li>http://467411.blog.163.com/blog/static/3353960920104122221346/
 * <li>http://www.fmddlmyy.cn/mytext.html
 * <li>http://www.unicode.org/
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class TextUtils {

	//--------------------------------------------------------------------------
	//(char)32 = ' '; (char)126 = '~'
	public static final char ASCII_PRINTABLE_FIRST = 0x20;
	public static final char ASCII_PRINTABLE_LAST = 0x7E;

	public static final char ASCII_HORIZONTAL_TAB = '\t';
	public static final char ASCII_LINE_FEED = '\n';
	public static final char ASCII_FORM_FEED = '\f';
	public static final char ASCII_CARRIAGE_RETURN = '\r';

	public static final char CJK_UNIFIED_IDEOGRAPHS_FIRST = 0x4E00;
	public static final char CJK_UNIFIED_IDEOGRAPHS_LAST = 0x9FBF;

	public static final char CJK_UNIFIED_IDEOGRAPHS_EX_A_FIRST = 0x3400;
	public static final char CJK_UNIFIED_IDEOGRAPHS_EX_A_LAST = 0x4DBF;

	/**
	 * Checks whether the character is CJK character.
	 * 
	 * @param ch the character to check
	 * @return true if the character is CJK
	 */
	public static final boolean isCJK(char ch) {

		return ch >= CJK_UNIFIED_IDEOGRAPHS_FIRST &&
			ch <= CJK_UNIFIED_IDEOGRAPHS_LAST ||
			ch >= CJK_UNIFIED_IDEOGRAPHS_EX_A_FIRST &&
			ch <= CJK_UNIFIED_IDEOGRAPHS_EX_A_LAST;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAscii('a')  = true
	 *   CharUtils.isAscii('A')  = true
	 *   CharUtils.isAscii('3')  = true
	 *   CharUtils.isAscii('-')  = true
	 *   CharUtils.isAscii('\n') = true
	 *   CharUtils.isAscii('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if less than 128
	 */
	public static boolean isAscii(char ch) {

		return ch < 128;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit printable.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiPrintable('a')  = true
	 *   CharUtils.isAsciiPrintable('A')  = true
	 *   CharUtils.isAsciiPrintable('3')  = true
	 *   CharUtils.isAsciiPrintable('-')  = true
	 *   CharUtils.isAsciiPrintable('\n') = false
	 *   CharUtils.isAsciiPrintable('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 32 and 126 inclusive
	 */
	public static boolean isAsciiPrintable(char ch) {

		return ch >= 32 && ch < 127;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit control.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiControl('a')  = false
	 *   CharUtils.isAsciiControl('A')  = false
	 *   CharUtils.isAsciiControl('3')  = false
	 *   CharUtils.isAsciiControl('-')  = false
	 *   CharUtils.isAsciiControl('\n') = true
	 *   CharUtils.isAsciiControl('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if less than 32 or equals 127
	 */
	public static boolean isAsciiControl(char ch) {

		return ch < 32 || ch == 127;
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiAlpha('a')  = true
	 *   CharUtils.isAsciiAlpha('A')  = true
	 *   CharUtils.isAsciiAlpha('3')  = false
	 *   CharUtils.isAsciiAlpha('-')  = false
	 *   CharUtils.isAsciiAlpha('\n') = false
	 *   CharUtils.isAsciiAlpha('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAsciiAlpha(char ch) {

		return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic upper case.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiAlphaUpper('a')  = false
	 *   CharUtils.isAsciiAlphaUpper('A')  = true
	 *   CharUtils.isAsciiAlphaUpper('3')  = false
	 *   CharUtils.isAsciiAlphaUpper('-')  = false
	 *   CharUtils.isAsciiAlphaUpper('\n') = false
	 *   CharUtils.isAsciiAlphaUpper('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 65 and 90 inclusive
	 */
	public static boolean isAsciiAlphaUpper(char ch) {

		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit alphabetic lower case.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiAlphaLower('a')  = true
	 *   CharUtils.isAsciiAlphaLower('A')  = false
	 *   CharUtils.isAsciiAlphaLower('3')  = false
	 *   CharUtils.isAsciiAlphaLower('-')  = false
	 *   CharUtils.isAsciiAlphaLower('\n') = false
	 *   CharUtils.isAsciiAlphaLower('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 97 and 122 inclusive
	 */
	public static boolean isAsciiAlphaLower(char ch) {

		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit numeric.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiNumeric('a')  = false
	 *   CharUtils.isAsciiNumeric('A')  = false
	 *   CharUtils.isAsciiNumeric('3')  = true
	 *   CharUtils.isAsciiNumeric('-')  = false
	 *   CharUtils.isAsciiNumeric('\n') = false
	 *   CharUtils.isAsciiNumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 48 and 57 inclusive
	 */
	public static boolean isAsciiNumeric(char ch) {

		return ch >= '0' && ch <= '9';
	}

	/**
	 * <p>
	 * Checks whether the character is ASCII 7 bit numeric.
	 * </p>
	 * 
	 * <pre>
	 *   CharUtils.isAsciiAlphanumeric('a')  = true
	 *   CharUtils.isAsciiAlphanumeric('A')  = true
	 *   CharUtils.isAsciiAlphanumeric('3')  = true
	 *   CharUtils.isAsciiAlphanumeric('-')  = false
	 *   CharUtils.isAsciiAlphanumeric('\n') = false
	 *   CharUtils.isAsciiAlphanumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch the character to check
	 * @return true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAsciiAlphanumeric(char ch) {

		return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' &&
			ch <= '9';
	}

	/**
	 * Returns the lower case equivalent for the specified character if the
	 * character is an upper case letter. Otherwise, the specified character is
	 * returned unchanged.
	 * 
	 * @param c the character
	 * @return if {@code c} is an upper case character then its lower case
	 *         counterpart, otherwise just {@code c}.
	 */
	public static char toAsciiLowerCase(char c) {

		if ('A' <= c && c <= 'Z')
			return (char) (c + ('a' - 'A'));

		return c;
	}

	/**
	 * Returns the upper case equivalent for the specified character if the
	 * character is a lower case letter. Otherwise, the specified character is
	 * returned unchanged.
	 * 
	 * @param c the character to convert.
	 * @return if {@code c} is a lower case character then its upper case
	 *         counterpart, otherwise just {@code c}.
	 */
	public static char toAsciiUpperCase(char c) {

		// Optimized case for ASCII
		if ('a' <= c && c <= 'z')
			return (char) (c - ('a' - 'A'));

		return c;
	}

	public static boolean isEnglishWordChar(char ch) {

		return isAsciiAlphanumeric(ch) || ch == '_';
	}

	/**
	 * Check whether the character is a line break character.
	 * 
	 * @param ch the character to check
	 * @return true if equals to '\r' or '\n' or '\f'
	 */
	public static boolean isLineBreakChar(char ch) {

		return ch == ASCII_LINE_FEED || ch == ASCII_CARRIAGE_RETURN ||
			ch == ASCII_FORM_FEED;
	}

	/**
	 * Check whether the two characters can combined as one line break.
	 * 
	 * @param ch the character to check
	 * @param chNext the character next to ch to check
	 * @return true if ch equals to '\r' and chNext equals to '\n', or the
	 *         opposite
	 */
	public static boolean canCombinedAsOneLineBreak(char ch, char chNext) {

		return ch == TextUtils.ASCII_LINE_FEED &&
			chNext == TextUtils.ASCII_CARRIAGE_RETURN ||
			ch == TextUtils.ASCII_CARRIAGE_RETURN &&
			chNext == TextUtils.ASCII_LINE_FEED;
	}

	/**
	 * Indicates whether the specified character is a Unicode space character.
	 * That is, if it is a member of one of the Unicode categories Space
	 * Separator, Line Separator, or Paragraph Separator.
	 * 
	 * @param c the character to check.
	 * @return {@code true} if {@code c} is a Unicode space character, {@code
	 *         false} otherwise.
	 */
	public static boolean isSpaceChar(char c) {

		if (c == 0x20 || c == 0xa0 || c == 0x1680)
			return true;
		if (c < 0x2000)
			return false;
		return c <= 0x200b || c == 0x2028 || c == 0x2029 || c == 0x202f ||
			c == 0x3000;
	}

	/**
	 * Indicates whether the specified character is a whitespace character in
	 * Java.
	 * 
	 * @param c the character to check.
	 * @return {@code true} if the supplied {@code c} is a whitespace character
	 *         in Java; {@code false} otherwise.
	 */
	public static boolean isWhitespace(char c) {

		// Optimized case for ASCII
		if (c >= 0x1c && c <= 0x20 || c >= 0x9 && c <= 0xd)
			return true;
		if (c == 0x1680)
			return true;
		if (c < 0x2000 || c == 0x2007)
			return false;
		return c <= 0x200b || c == 0x2028 || c == 0x2029 || c == 0x3000;
	}
}
