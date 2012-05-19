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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import cn.uc.tiny.style.Style.Value;

/**
 * This class represents a CSS style sheet. It is also used to represent parts
 * of a style sheet in a tree structure, where the depth of the tree equals the
 * length of the longest selector.
 * <p>
 * The properties field contains the style that is valid for the corresponding
 * selector path denoted by the position in the tree. Accordingly, the
 * properties field of the root style sheet contains the Style that is applied
 * for the "*" selector (matching anything).
 * <p>
 * To apply the style sheet, the tree is visited in a depth first search for
 * matching selector parts. Each visited node with a non-empty properties field
 * is inserted in a queue. The queue is ordered by the specificity of the node.
 * <p>
 * After all matching selectors were visited, the StyleSheet objects are fetched
 * from the queue in the order of ascending specificity and their properties are
 * applied. Thus, values denoted by more specific selectors overwrite values
 * from less specific rules.
 * <p>
 * Note that child and descendant selectors are inverted, so the evaluation of a
 * style sheet can always start at the element it is applied to.
 * 
 * <p>
 * About the Selectors of CSS2.1, please refer to : <a
 * href="http://www.w3.org/TR/CSS21/selector.html#id-selectors" >Selectors</a>,
 * StyleSheet do not support Adjacent sibling selectors and only support part of
 * Attribute selectors. Also, StyleSheet can not handle the wildcard character
 * '*' to represent any element, it will be ignored. For example, 'div * p'
 * selector, the '*' will be ignored and it will be treated as 'div p'.
 * </p>
 * 
 * <table>
 * <tr>
 * <td>SELECTOR</td>
 * <td>SUPPORT</td>
 * <td>NOTE</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>Universal</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Type</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Descendant</td>
 * <td>yes</td>
 * <td></td>
 * <tr>
 * <td>Child</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Adjacent sibling</td>
 * <td>no</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Attribute</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Id</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Pseudo class</td>
 * <td>part</td>
 * <td>not support 'lang' function</td>
 * </tr>
 * <tr>
 * <td>Pseudo element</td>
 * <td>yes</td>
 * <td></td>
 * </tr>
 * 
 * </table>
 * 
 * <p>
 * <b>Modifications From Original</b>
 * </p>
 * 
 * <ul>
 * <li>Remove HtmlWidget, SystemRequestHandler, etc...
 * <li>Remove the internal {@linkplain Font} dependency
 * <li>Remove the handle of @import rule
 * <li>Remove the Element dependency,we only need element name, element pseudo
 * state and element attributes as arguments
 * <li>Add DEBUG flag to control debug output
 * 
 * <li>Extra User Agent Style initialization to member
 * {@link #initUserAgentStylesheet()}
 * <li>Make {@link #put(String, Style)} public
 * <li>Add the handle of CDO/CDC tokens, just ignore it
 * <li>Fixed the bug in handle pseudo class and pseudo element specificity
 * <li>Fixed the bug in the DASHMATCH '|=' attribute match algorithm
 * <li>Simplify the way to calculate the nesting position of Style, because we
 * donot need to support @import rule
 * </ul>
 * 
 * @author Stefan Haustein
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 */
public class StyleSheet {

	/** DEBUG flag, do not commit the code with TRUE flag!!! */
	public static final boolean DEBUG = Style.DEBUG;

	public static final String PSEUDO_CLASS_FIRST_CHILD = "first-child";
	public static final String PSEUDO_CLASS_LINK = "link";
	public static final String PSEUDO_CLASS_VISITED = "visited";
	public static final String PSEUDO_CLASS_ACTIVE = "active";
	public static final String PSEUDO_CLASS_HOVER = "hover";
	public static final String PSEUDO_CLASS_FOCUS = "focus";
	public static final String PSEUDO_ELEMENT_FIRST_LINE = "first-line";
	public static final String PSEUDO_ELEMENT_FIRST_LETTER = "first-letter";
	public static final String PSEUDO_ELEMENT_BEFORE = "before";
	public static final String PSEUDO_ELEMENT_AFTER = "after";

	/*
	 * SELECT_XXX constants are used to specify the selector type in
	 * selectAttributeOperation and in parseSelector().
	 */
	private static final char SELECT_ID = 1;
	private static final char SELECT_CLASS = 2;
	private static final char SELECT_PSEUDOCLASS = 3;
	private static final char SELECT_NAME = 4;
	private static final char SELECT_DESCENDANT = 6;
	private static final char SELECT_ATTRIBUTE_NAME = 7;
	private static final char SELECT_ATTRIBUTE_VALUE = 8;
	private static final char SELECT_ATTRIBUTE_INCLUDES = 9;
	private static final char SELECT_ATTRIBUTE_DASHMATCH = 10;
	private static final char SELECT_CHILD = 11;

	/**
	 * Specificity weight for element name, pseudo-element selectors.
	 */
	private static final int SPECIFICITY_D = 1;

	/**
	 * Specificity weight for element class, pseudo-class and other normal
	 * attributes selectors.
	 */
	private static final int SPECIFICITY_C = 100 * SPECIFICITY_D;

	/**
	 * Specificity weight for id selectors.
	 */
	private static final int SPECIFICITY_B = 100 * SPECIFICITY_C;

	/**
	 * Specificity weight for !important selectors
	 */
	static final int SPECIFICITY_IMPORTANT = 100 * SPECIFICITY_B;

	/**
	 * A table mapping element names to sub-style sheets for the corresponding
	 * selection path.
	 */
	private Hashtable selectElementName; //<String, StyleSheet>

	/**
	 * A table mapping pseudoclass names to sub-style sheets for the
	 * corresponding selection path.
	 */
	private Hashtable selectPseudoclass; //<String, StyleSheet>

	/**
	 * A list of attribute names for selectors. Forms attribute selectors
	 * together with selectAttributeOperation and selectAttributeValue.
	 */
	private Vector selectAttributeName; //<String>

	/**
	 * A list of attribute operations for selectors (one of the
	 * SELECT_ATTRIBUTE_XX constants). Forms attribute selectors together with
	 * selectAttributeName and selectAttributeValue.
	 */
	private StringBuffer selectAttributeOperation; //<SELECT_ATTRIBUTE_XX>

	/**
	 * A list of Hashtables, mapping attribute values to sub-style sheets for
	 * the corresponding selection path. Forms attribute selectors together with
	 * selectAttributeName and selectAttributeOperation.
	 */
	private Vector selectAttributeValue; //<StyleSheet/Hashtable>

	/**
	 * Reference to child selector selector sub-style sheet.
	 */
	private StyleSheet selectChild;

	/**
	 * Reference to descendant selector sub-style sheet.
	 */
	private StyleSheet selectDescendants;

	/**
	 * Properties for * rules
	 */
	private Vector properties;

	/**
	 * Creates a empty style sheet for HTML.
	 */
	public StyleSheet() {

	}

	/**
	 * Reads a style sheet from the given css string.
	 * 
	 * @param url URL of this style sheet (or the containing document)
	 * @param css the CSS string to load the style sheet from
	 * @param nesting the nesting position of the appearance of CSS text
	 *            , when client do not support @import rule, it can simple pass
	 *            the current line number when meet the specified CSS text,
	 *            it may inside a style element or read from an external CSS
	 *            resource
	 * @return this
	 * @throws IOException if there is an underlying stream exception
	 */
	public StyleSheet read(String url, String css, int nesting) {

		CssTokenizer ct = new CssTokenizer(url, css);

		//Simplify the nesting position calculation, 
		//relative code has been removed

		int position = 0;
		boolean inMedia = false;

		while (ct.ttype != CssTokenizer.TT_EOF) {

			if (ct.ttype == CssTokenizer.TT_ATKEYWORD) { //handle @ rule

				if ("media".equals(ct.sval)) { //handle 'media'

					ct.nextToken(false);
					inMedia = false;
					do {
						if (ct.ttype != ',') {
							inMedia |= checkMediaType(ct.sval);
						}
						ct.nextToken(false);
					} while (ct.ttype != '{' && ct.ttype != CssTokenizer.TT_EOF);

					ct.nextToken(false);
					if (!inMedia) {
						int level = 1;
						do {
							switch (ct.ttype) {
							case '}':
								level--;
								break;
							case '{':
								level++;
								break;
							case CssTokenizer.TT_EOF:
								return this;
							}
							ct.nextToken(false);
						} while (level > 0);
					}
				}
				else if ("import".equals(ct.sval)) { //handle 'import'

					ct.nextToken(false);
					//String importUrl = ct.sval;
					ct.nextToken(false);
					StringBuffer media = new StringBuffer();
					while (ct.ttype != ';' && ct.ttype != CssTokenizer.TT_EOF) {

						media.append(ct.sval);
						ct.nextToken(false);
					}

					//just ignore the @import rule, 
					//relative code has been removed
					ct.nextToken(false);
					position++;
				}
				else {

					ct.debug("unsupported @" + ct.sval);
					ct.nextToken(false);
				}
			}
			else if (ct.ttype == '}') {

				if (!inMedia)
					ct.debug("unexpected }");

				inMedia = false;
				ct.nextToken(false);
			}
			else if (ct.ttype == CssTokenizer.TT_CDO ||
				ct.ttype == CssTokenizer.TT_CDC) {
				//ignore
			}
			else {

				// no @keyword or } -> regular ruleset
				//read selectors
				Vector targets = new Vector();
				targets.addElement(parseSelector(ct));
				while (ct.ttype == ',') {

					ct.nextToken(false);
					targets.addElement(parseSelector(ct));
				}

				//read styles block
				Style style = new Style();
				if (ct.ttype == '{') {

					ct.nextToken(false);
					style.read(ct);
					ct.assertTokenType('}');
				}
				else {

					ct.debug("'{' expected");
				}

				//copy the read block to every selector
				for (int i = 0; i < targets.size(); i++) {

					Style target = (Style) targets.elementAt(i);
					if (target == null)
						continue;

					target.position = nesting * 1000 + position;
					//target.nesting = nesting;
					target.set(style);
				}
				ct.nextToken(false);
				position++;
			}
		}
		return this;
	}

	/**
	 * Parse a selector. The tokenizer must be at the first token of the
	 * selector. When returning, the current token will be ',' or '{'.
	 * 
	 * <p>
	 * This method brings selector paths into the tree form described in the
	 * class documentation.
	 * 
	 * @param ct the css tokenizer
	 * @return the node at the end of the tree path denoted by this selector,
	 *         where the corresponding CSS properties will be stored
	 * @throws IOException
	 */
	private Style parseSelector(CssTokenizer ct) {

		boolean error = false;

		int specificity = 0;
		StyleSheet result = this;

		loop: while (true) {

			switch (ct.ttype) {
			case CssTokenizer.TT_IDENT: {
				if (result.selectElementName == null) {
					result.selectElementName = new Hashtable();
				}
				result = descend(result.selectElementName,
					ct.sval.toLowerCase());
				specificity += SPECIFICITY_D;
				ct.nextToken(true);
				break;
			}
			case '*': {
				// no need to do anything...
				ct.nextToken(true);
				continue;
			}
			case '[': {
				ct.nextToken(false);
				String name = ct.sval.toLowerCase();
				ct.nextToken(false);
				char type;
				String value = null;

				if (ct.ttype == ']') {
					type = SELECT_ATTRIBUTE_NAME;
				}
				else {
					switch (ct.ttype) {
					case CssTokenizer.TT_INCLUDES:
						type = SELECT_ATTRIBUTE_INCLUDES;
						break;
					case '=':
						type = SELECT_ATTRIBUTE_VALUE;
						break;
					case CssTokenizer.TT_DASHMATCH:
						type = SELECT_ATTRIBUTE_DASHMATCH;
						break;
					default:
						error = true;
						break loop;
					}
					ct.nextToken(false);
					if (ct.ttype != CssTokenizer.TT_STRING) {
						error = true;
						break loop;
					}
					value = ct.sval;
					ct.nextToken(false);
					ct.assertTokenType(']');
					specificity += SPECIFICITY_C;
				}
				result = result.createAttributeSelector(type, name, value);
				ct.nextToken(true);
				break;
			}
			case '.':
				ct.nextToken(false);
				error = ct.ttype != CssTokenizer.TT_IDENT;
				result = result.createAttributeSelector(
					SELECT_ATTRIBUTE_INCLUDES, "class", ct.sval);
				specificity += SPECIFICITY_C;
				ct.nextToken(true);
				break;

			case CssTokenizer.TT_HASH:
				result = result.createAttributeSelector(SELECT_ATTRIBUTE_VALUE,
					"id", ct.sval);
				specificity += SPECIFICITY_B;
				ct.nextToken(true);
				break;

			case ':':
				ct.nextToken(false);
				error = ct.ttype != CssTokenizer.TT_IDENT;
				if (result.selectPseudoclass == null) {
					result.selectPseudoclass = new Hashtable();
				}
				result = descend(result.selectPseudoclass, ct.sval);

				if (PSEUDO_ELEMENT_FIRST_LINE.equals(ct.sval) ||
					PSEUDO_ELEMENT_FIRST_LETTER.equals(ct.sval) ||
					PSEUDO_ELEMENT_BEFORE.equals(ct.sval) ||
					PSEUDO_ELEMENT_AFTER.equals(ct.sval)) {

					//pseudo element
					specificity += SPECIFICITY_D;
				}
				else {

					//pseudo class
					specificity += SPECIFICITY_C;
				}

				ct.nextToken(true);
				break;

			case CssTokenizer.TT_S:
				ct.nextToken(false);
				if (ct.ttype == '{' || ct.ttype == ',' || ct.ttype == -1) {
					break loop;
				}
				if (ct.ttype == '>') {
					if (result.selectChild == null) {

						result.selectChild = new StyleSheet();
					}
					result = result.selectChild;
					ct.nextToken(false);
				}
				else {
					if (result.selectDescendants == null) {

						result.selectDescendants = new StyleSheet();
					}
					result = result.selectDescendants;
				}
				break;

			case '>':
				if (result.selectChild == null) {

					result.selectChild = new StyleSheet();
				}
				result = result.selectChild;
				ct.nextToken(false);
				break;

			default: // unknown
				break loop;
			}
		}

		// state: behind all recognized tokens -- check for unexpected stuff
		if (error || (ct.ttype != ',' && ct.ttype != '{')) {

			ct.debug("Unrecognized selector");
			// parse to '{', ',' or TT_EOF to get to a well-defined state
			while (ct.ttype != ',' && ct.ttype != CssTokenizer.TT_EOF &&
				ct.ttype != '{') {
				ct.nextToken(false);
			}
			return null;
		}

		Style style = new Style();
		style.specificity = specificity;
		if (result.properties == null) {
			result.properties = new Vector();
		}
		result.properties.addElement(style);

		return style;
	}

	private StyleSheet createAttributeSelector(char type, String name,
		String value) {

		//treat id and class as normal attributes
		boolean hasValue = value != null && value.length() > 0;
		if (type == SELECT_ID || ("id".equals(name) && hasValue)) {

			type = SELECT_ATTRIBUTE_VALUE;
		}
		else if (type == SELECT_CLASS || ("class".equals(name) && hasValue)) {

			type = SELECT_ATTRIBUTE_INCLUDES;
		}

		int index = -1;
		if (selectAttributeOperation == null) {

			selectAttributeOperation = new StringBuffer();
			selectAttributeName = new Vector();
			selectAttributeValue = new Vector();
		}
		else {

			for (int j = 0; j < selectAttributeOperation.length(); j++) {

				if (selectAttributeOperation.charAt(j) == type &&
					selectAttributeName.elementAt(j).equals(name)) {

					index = j;
				}
			}
		}

		if (type == SELECT_ATTRIBUTE_NAME) {

			if (index == -1) {

				index = selectAttributeOperation.length();
				selectAttributeOperation.append(type);
				selectAttributeName.addElement(name);
				selectAttributeValue.addElement(new StyleSheet());
			}

			return (StyleSheet) selectAttributeValue.elementAt(index);
		}

		if (index == -1) {

			index = selectAttributeOperation.length();
			selectAttributeOperation.append(type);
			selectAttributeName.addElement(name);
			selectAttributeValue.addElement(new Hashtable());
		}

		return descend((Hashtable) selectAttributeValue.elementAt(index), value);
	}

	/**
	 * Returns the style sheet denoted by the given key from the hashtable. If
	 * not yet existing, a corresponding entry is created with the given
	 * specificity.
	 */
	private static StyleSheet descend(Hashtable h, String key) {

		StyleSheet s = (StyleSheet) h.get(key);

		if (s == null) {

			s = new StyleSheet();
			h.put(key, s);
		}
		return s;
	}

	/**
	 * Helper method for collectStyles(). Determines whether the given key is
	 * in the given map. If so, the style search continues in the corresponding
	 * style sheet.
	 * 
	 * @param elementName name of the element
	 * @param elementPseudo pseudo-class or pseudo-element of element, such as
	 *            'link'
	 * @param elementAttributes attributes table of the element
	 * @param map corresponding sub style sheet map
	 * @param key element name or attribute value
	 * @param queue the queue to store collected Styles
	 * @param children the queue to store matched children StyleSheets of
	 *            specified element
	 * @param descendants the queue to store matched descendants StyleSheets of
	 *            specified element
	 */
	private void collectStyles(String elementName, String elementPseudo,
		Hashtable elementAttributes, Hashtable map, String key, Vector queue,
		Vector children, Vector descendants) {

		if (key == null || map == null)
			return;

		StyleSheet sh = (StyleSheet) map.get(key);
		if (sh != null) {

			sh.collectStyles(elementName, elementPseudo, elementAttributes,
				queue, children, descendants);
		}
	}

	/**
	 * Performs a depth first search of all matching selectors and enqueues the
	 * corresponding style information.
	 * 
	 * @param elementName name of the element
	 * @param elementPseudo pseudo-class or pseudo-element of element, such as
	 *            'link'
	 * @param elementAttributes attributes table of the element
	 * @param queue the queue to store collected Styles
	 * @param children the queue to store matched children StyleSheets of
	 *            specified element
	 * @param descendants the queue to store matched descendants StyleSheets of
	 *            specified element
	 * @see {@link #PSEUDO_CLASS_LINK}, {@link #PSEUDO_ELEMENT_AFTER}, etc..
	 */
	public void collectStyles(String elementName, String elementPseudo,
		Hashtable elementAttributes, Vector queue, Vector children,
		Vector descendants) {

		if (properties != null && queue != null) {

			// enqueue the style at the current node according to its specificity
			Style p = null;
			for (int i = 0, index = 0, size = properties.size(); i < size; i++) {

				p = (Style) properties.elementAt(i);
				index = queue.size();
				while (index > 0) {

					Style s = (Style) queue.elementAt(index - 1);
					if (s.compare(p) <= 0)
						break;

					if (s == p) {
						index = -1;
						break;
					}
					index--;
				}

				if (index != -1) {

					queue.insertElementAt(p, index);
				}
			}
		}

		if (selectAttributeOperation != null) {

			for (int i = 0; i < selectAttributeOperation.length(); i++) {

				int attrType = selectAttributeOperation.charAt(i);
				String attrName = (String) selectAttributeName.elementAt(i);
				String elemAttrValue = null;

				if (elementAttributes != null)
					elemAttrValue = (String) elementAttributes.get(attrName);

				if (elemAttrValue == null || elemAttrValue.length() == 0)
					continue;//skip empty attribute

				Hashtable valueMap = null;
				StyleSheet sheet = null;
				String val = null;

				switch (attrType) {

				case SELECT_ATTRIBUTE_NAME:
					sheet = (StyleSheet) selectAttributeValue.elementAt(i);
					sheet.collectStyles(elementName, elementPseudo,
						elementAttributes, queue, children, descendants);
					break;

				case SELECT_ATTRIBUTE_VALUE:
					valueMap = (Hashtable) selectAttributeValue.elementAt(i);
					collectStyles(elementName, elementPseudo,
						elementAttributes, valueMap, elemAttrValue, queue,
						children, descendants);
					break;

				case SELECT_ATTRIBUTE_DASHMATCH:
					valueMap = (Hashtable) selectAttributeValue.elementAt(i);

					for (Enumeration e = valueMap.keys(); e.hasMoreElements();) {

						val = (String) e.nextElement();
						if (elemAttrValue.equals(val) ||
							elemAttrValue.startsWith(val + '-')) {

							sheet = (StyleSheet) valueMap.get(val);

							sheet.collectStyles(elementName, elementPseudo,
								elementAttributes, queue, children, descendants);
						}
					}
					break;

				case SELECT_ATTRIBUTE_INCLUDES:
					valueMap = (Hashtable) selectAttributeValue.elementAt(i);
					String[] values = split(elemAttrValue, ' ');

					for (int j = 0; j < values.length; j++) {

						collectStyles(elementName, elementPseudo,
							elementAttributes, valueMap, values[j], queue,
							children, descendants);
					}
					break;
				}
			}
		}

		if (selectElementName != null) {

			collectStyles(elementName, elementPseudo, elementAttributes,
				selectElementName, elementName, queue, children, descendants);
		}

		if (selectChild != null && children != null) {

			children.addElement(selectChild);
		}

		if (selectPseudoclass != null && elementPseudo != null &&
			elementPseudo.length() > 0) {

			collectStyles(elementName, elementPseudo, elementAttributes,
				selectPseudoclass, elementPseudo, queue, children, descendants);
		}

		if (selectDescendants != null && descendants != null) {

			descendants.addElement(selectDescendants);
		}
	}

	/**
	 * Performs a depth first search of all matching selectors and enqueues the
	 * corresponding style information.
	 * 
	 * @param elementName name of the element
	 * @param elementPseudo pseudo-class or pseudo-element of element, such as
	 *            'link'
	 * @param elementClass class attribute of the element
	 * @param elementId id of the element
	 * @param queue the queue to store collected Styles
	 * @param children the queue to store matched children StyleSheets of
	 *            specified element
	 * @param descendants the queue to store matched descendants StyleSheets of
	 *            specified element
	 * @see {@link #PSEUDO_CLASS_LINK}, {@link #PSEUDO_ELEMENT_AFTER}, etc..
	 */
	public void collectStyles(String elementName, String elementPseudo,
		String elementClass, String elementId, Vector queue, Vector children,
		Vector descendants) {

		Hashtable elementAttributes = new Hashtable();

		if (elementClass != null)
			elementAttributes.put("class", elementClass);

		if (elementId != null)
			elementAttributes.put("id", elementId);

		collectStyles(elementName, elementPseudo, elementAttributes, queue,
			children, descendants);
	}

	/**
	 * Initialize the styles of User Agent, client can call this method on root
	 * StyleSheet object to setup the User Agent styles or setup by itself.
	 * 
	 * <table>
	 * <tr>
	 * <td>a</td>
	 * <td>color: #0000ff; decoration: underline</td>
	 * </tr>
	 * <tr>
	 * <td>b</td>
	 * <td>font-weight: 700;</td>
	 * </tr>
	 * <tr>
	 * <td>body</td>
	 * <td>display: block; padding: 5px;</td>
	 * </tr>
	 * <tr>
	 * <td>dd</td>
	 * <td>display: block;</td>
	 * </tr>
	 * <tr>
	 * <td>dir</td>
	 * <td>display: block; margin-top: 2px; margin-bottom: 2px; margin-left:
	 * 10px;</td>
	 * </tr>
	 * <tr>
	 * <td>div</td>
	 * <td>display: block;</td>
	 * </tr>
	 * <tr>
	 * <td>dl</td>
	 * <td>display: block;</td>
	 * </tr>
	 * <tr>
	 * <td>dt</td>
	 * <td>display: block;</td>
	 * </tr>
	 * <tr>
	 * <td>h1 .. h6</td>
	 * <td>display: block; font-weight: 700; margin-top: 2px; margin-bottom:
	 * 2px;</td>
	 * </tr>
	 * <tr>
	 * <td>hr</td>
	 * <td>border-top-color: #888888; border-top-width: 1px; border-top-style:
	 * solid; display: block; margin-top: 2px; margin-bottom: 2px;</td>
	 * </tr>
	 * <tr>
	 * <td>li</td>
	 * <td>display: list-item; margin-top: 2px; margin-bottom: 2px;</td>
	 * </tr>
	 * </td></tr>
	 * <tr>
	 * <td>ol</td>
	 * <td>display: block; list-style-type: decimal; margin-left: 10px;</td>
	 * </tr>
	 * <tr>
	 * <td>p</td>
	 * <td>display: block; margin-top: 2px; margin-bottom: 2px;</td>
	 * </tr>
	 * <tr>
	 * <td>th</td>
	 * <td>display: table-cell; font-weight: 700; padding: 1px;</td>
	 * </tr>
	 * <tr>
	 * <td>tr</td>
	 * <td>display: table-row;</td>
	 * </tr>
	 * <tr>
	 * <td>td</td>
	 * <td>display: table-cell; padding: 1px;</td>
	 * </tr>
	 * <tr>
	 * <td>ul</td>
	 * <td>display: block; margin-left: 10px;</td>
	 * </tr>
	 * <tr>
	 * <td>img</td>
	 * <td>display: inline-block;</td>
	 * </tr>
	 * </table>
	 */
	public void initUserAgentStylesheet() {

		// Set default indent with to sufficient space for ordered lists with
		// two digits and the default paragraph spacing to 50% of the font height
		// (so top and bottom spacing adds up to a full line)
		int defaultIndent = Style.DEFAULT_INDENT;
		int defaultParagraphSpace = Math.max(1, Style.DEFAULT_FONT_HEIGHT / 2) * 1000;

		put(":link",
			new Style().set(Style.COLOR, 0x0ff0000ff, Style.ARGB).set(
				Style.TEXT_DECORATION, Value.UNDERLINE, Style.ENUM));
		put("address", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		put("b", new Style().set(Style.FONT_WEIGHT, 700000, Style.NUMBER));
		put("blockquote",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_RIGHT, defaultIndent, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX));
		put("body",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.PADDING_TOP | Style.MULTIVALUE_TRBL,
				defaultParagraphSpace / 2, Style.PX, 0));
		put("button",
			new Style().set(Style.DISPLAY, Value.INLINE_BLOCK, Style.ENUM).set(
				Style.PADDING_TOP | Style.MULTIVALUE_TRBL, 3000, Style.PX, 0));
		put("center",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX).set(
				Style.TEXT_ALIGN, Value.CENTER, Style.ENUM));
		put("dd",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX));
		put("dir",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX).set(
				Style.LIST_STYLE_TYPE, Value.SQUARE, Style.ENUM));
		put("div", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		put("dl", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		put("dt", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		put("form", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		for (int i = 1; i <= 6; i++) {
			put("h" + i,
				new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
					Style.FONT_WEIGHT, 700000, Style.NUMBER).set(
					Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
					Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX));
		}
		put("hr",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.BORDER_TOP_WIDTH, 1000, Style.PX).set(
				Style.BORDER_TOP_STYLE, Value.SOLID, Style.ENUM).set(
				Style.BORDER_TOP_COLOR, 0x0ff888888, Style.ARGB).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX));
		put("img",
			new Style().set(Style.DISPLAY, Value.INLINE_BLOCK, Style.ENUM));
		put("input",
			new Style().set(Style.DISPLAY, Value.INLINE_BLOCK, Style.ENUM));
		put("li",
			new Style().set(Style.DISPLAY, Value.LIST_ITEM, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX));
		put("marquee", new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM));
		put("menu",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX).set(
				Style.LIST_STYLE_TYPE, Value.SQUARE, Style.ENUM));
		put("ol",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX).set(
				Style.LIST_STYLE_TYPE, Value.DECIMAL, Style.ENUM));
		put("p",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_TOP, defaultParagraphSpace, Style.PX).set(
				Style.MARGIN_BOTTOM, defaultParagraphSpace, Style.PX));
		put("pre",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.WHITE_SPACE, Value.PRE, Style.ENUM).set(Style.MARGIN_TOP,
				defaultParagraphSpace, Style.PX).set(Style.MARGIN_BOTTOM,
				defaultParagraphSpace, Style.PX));
		put("select[multiple]",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).
			// set(Style.BACKGROUND_COLOR, 0x0ffff0000, Style.ARGB).
			set(Style.PADDING_TOP | Style.MULTIVALUE_TRBL, 1000, Style.PX, 0).set(
				Style.BORDER_TOP_WIDTH | Style.MULTIVALUE_TRBL, 1000, Style.PX,
				0).set(Style.BORDER_TOP_COLOR | Style.MULTIVALUE_TRBL,
				0x0ff888888, Style.ARGB, 0).set(
				Style.BORDER_TOP_STYLE | Style.MULTIVALUE_TRBL, Value.SOLID,
				Style.ENUM, 0));
		put("script", new Style().set(Style.DISPLAY, Value.NONE, Style.ENUM));
		put("strong",
			new Style().set(Style.FONT_WEIGHT, Value.BOLD, Style.ENUM));
		put("style", new Style().set(Style.DISPLAY, Value.NONE, Style.ENUM));

		put("table",
			new Style().set(Style.DISPLAY, Value.TABLE, Style.ENUM).set(
				Style.CLEAR, Value.BOTH, Style.ENUM));
		put("td",
			new Style().set(Style.DISPLAY, Value.TABLE_CELL, Style.ENUM).set(
				Style.PADDING_TOP | Style.MULTIVALUE_TRBL, 1000, Style.PX, 0).set(
				Style.TEXT_ALIGN, Value.LEFT, Style.ENUM));
		put("th",
			new Style().set(Style.DISPLAY, Value.TABLE_CELL, Style.ENUM).set(
				Style.FONT_WEIGHT, 700000, Style.NUMBER).set(
				Style.PADDING_TOP | Style.MULTIVALUE_TRBL, 1000, Style.PX, 0).set(
				Style.TEXT_ALIGN, Value.CENTER, Style.ENUM));
		put("tr", new Style().set(Style.DISPLAY, Value.TABLE_ROW, Style.ENUM));
		put("ul",
			new Style().set(Style.DISPLAY, Value.BLOCK, Style.ENUM).set(
				Style.MARGIN_LEFT, defaultIndent, Style.PX).set(
				Style.LIST_STYLE_TYPE, Value.SQUARE, Style.ENUM));
		put("ul ul",
			new Style().set(Style.LIST_STYLE_TYPE, Value.CIRCLE, Style.ENUM));
		put("ul ul ul",
			new Style().set(Style.LIST_STYLE_TYPE, Value.DISC, Style.ENUM));
	}

	/**
	 * Internal method used to simplify building the default style sheet.
	 * 
	 * @param selector element name
	 * @param style default style for the element
	 */
	public void put(String selector, Style style) {

		if (selectElementName == null) {

			selectElementName = new Hashtable();
		}

		boolean simple = true;
		for (int i = 0; i < selector.length(); i++) {

			char c = selector.charAt(i);
			if (c < 'a' || c > 'z') {
				simple = false;
				break;
			}
		}

		if (simple) {

			StyleSheet s = new StyleSheet();
			s.properties = new Vector();
			s.properties.addElement(style);
			style.specificity = SPECIFICITY_D - SPECIFICITY_IMPORTANT;
			selectElementName.put(selector, s);
		}
		else {

			CssTokenizer ct = new CssTokenizer(null, selector + "{");
			Style target = parseSelector(ct);
			target.set(style);
			// copy important
			target.specificity += style.specificity - SPECIFICITY_IMPORTANT;
		}

	}

	/**
	 * Print the style sheet to stdout for debugging purposes.
	 * 
	 * @param current the current selector
	 */
	public void dump(String current) {

		if (!DEBUG)
			return;

		//#if (debug == true)

		if (properties != null) {
			System.out.print(current.length() == 0 ? "*" : current);
			System.out.print(" {");
			for (int i = 0; i < properties.size(); i++) {
				((Style) properties.elementAt(i)).dump("");
			}
			System.out.println("}");
		}

		if (selectElementName != null) {
			for (Enumeration e = selectElementName.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				((StyleSheet) selectElementName.get(key)).dump(current + key);
			}
		}

		if (selectAttributeOperation != null) {

			for (int i = 0; i < selectAttributeOperation.length(); i++) {

				int type = selectAttributeOperation.charAt(i);
				StringBuffer p = new StringBuffer(current);
				p.append('[');
				p.append((String) selectAttributeName.elementAt(i));

				if (type == SELECT_ATTRIBUTE_NAME) {
					p.append(']');
					((StyleSheet) selectAttributeValue.elementAt(i)).dump(p.toString());
				}
				else {
					switch (type) {
					case SELECT_ATTRIBUTE_VALUE:
						p.append('=');
						break;
					case SELECT_ATTRIBUTE_INCLUDES:
						p.append("~=");
						break;
					case SELECT_ATTRIBUTE_DASHMATCH:
						p.append("|=");
						break;
					}
					Hashtable valueMap = (Hashtable) selectAttributeValue.elementAt(i);

					for (Enumeration e = valueMap.keys(); e.hasMoreElements();) {

						String key = (String) e.nextElement();
						((StyleSheet) valueMap.get(key)).dump(p.toString() +
							'"' + key + "\"]");
					}
				}
			}
		}

		if (selectDescendants != null) {
			selectDescendants.dump(current + " ");
		}

		if (selectChild != null) {
			selectChild.dump(current + " > ");
		}

		if (selectPseudoclass != null) {

			for (Enumeration e = selectPseudoclass.keys(); e.hasMoreElements();) {

				String key = (String) e.nextElement();
				((StyleSheet) selectPseudoclass.get(key)).dump(current + ":" +
					key);
			}
		}

		//#endif
	}

	/**
	 * Returns a string array created by splitting the given string at the given
	 * separator.
	 */
	public static String[] split(String target, char separator) {

		int separatorInstances = 0;
		int targetLength = target.length();
		for (int index = target.indexOf(separator, 0); index != -1 &&
			index < targetLength; index = target.indexOf(separator, index)) {
			separatorInstances++;
			// Skip over separators
			if (index >= 0) {
				index++;
			}
		}
		String[] results = new String[separatorInstances + 1];
		int beginIndex = 0;
		for (int i = 0; i < separatorInstances; i++) {
			int endIndex = target.indexOf(separator, beginIndex);
			results[i] = target.substring(beginIndex, endIndex);
			beginIndex = endIndex + 1;
		}
		// Last piece (or full string if there were no separators).
		results[separatorInstances] = target.substring(beginIndex);
		return results;
	}

	/**
	 * returns true if the media string is null or contains "all" or "screen";
	 * "handheld" is accepted, too, if not in desktop rendering mode.
	 */
	public static boolean checkMediaType(String media) {

		if (media == null || media.trim().length() == 0) {
			return true;
		}
		media = media.toLowerCase();
		return media.indexOf("all") != -1 || media.indexOf("screen") != -1 ||
			media.indexOf("handheld") != -1;
	}

	/**
	 * Resolves a base URL and a relative URL to an absolute URL, e.g.
	 * "http://acme.com/prod/index.html" and "foo.html" to
	 * "http://acme.com/prod/foo.html". If the relative URL parameter contains
	 * an absolute URL, it is returned unchanged.
	 * 
	 * @param baseUrl the absolute base URL used for resolving the URL. If null,
	 *            the relative URL is returned
	 * @param relative URL, must not be null
	 * @return the resulting absolute URL
	 */
	public static String getAbsoluteUrl(String baseUrl, String relative) {

		if (baseUrl == null || baseUrl.length() == 0) {
			return relative;
		}

		// If there is a colon that is not separating a drive letter (c:) and that
		// is not or beyond the longest known protocol (6th char in "https://"),
		// we assume that the second parameter is an absolute address and return it
		// unchanged.
		int colPos = relative.indexOf(':');
		if (colPos > 1 && colPos < 7) {
			return relative;
		}

		if (relative.startsWith("//")) {
			int cut = baseUrl.indexOf("//");
			return (cut == -1 ? baseUrl : baseUrl.substring(0, cut)) + relative;
		}

		// cut off query and label
		int cutH = baseUrl.indexOf('#');
		int cutQ = baseUrl.indexOf('?');
		if (cutH != -1 || cutQ != -1) {
			int cut;
			if (cutH != -1 && cutQ != -1) {
				cut = Math.min(cutH, cutQ);
			}
			else if (cutH != -1) {
				cut = cutH;
			}
			else {
				cut = cutQ;
			}
			baseUrl = baseUrl.substring(0, cut);
		}

		colPos = baseUrl.indexOf(':');
		if (relative.startsWith("/")) {
			int cut = baseUrl.indexOf('/', colPos + 3);
			return (cut == -1 ? baseUrl : baseUrl.substring(0, cut)) + relative;
		}

		int cut = baseUrl.lastIndexOf('/');

		if (cut > colPos + 2) {
			return baseUrl.substring(0, cut + 1) + relative;
		}

		return baseUrl + '/' + relative;
	}
}
