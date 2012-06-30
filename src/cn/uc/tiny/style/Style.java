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

import java.util.Hashtable;

/**
 * Class to representing a set of CSS properties (e.g. a CSS style attribute).
 * 
 * <p>
 * About CSS2.1 style properties, please refer to <a
 * href="http://www.w3.org/TR/2010/WD-CSS2-20101207/propidx.html" >CSS Full
 * property table</a>, Style do not support properties of categories Paged
 * media, User interface and Text direction, see the below table for more
 * information :
 * </p>
 * 
 * <table>
 * <tr>
 * <td>PROPERTY</td>
 * <td>SUPPORT</td>
 * <td>CATEGORY</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>'margin' & TRBL</td>
 * <td>yes</td>
 * <td>MARGIN</td>
 * </tr>
 * <tr>
 * <td>'padding' & TRBL</td>
 * <td>yes</td>
 * <td>PADDING</td>
 * </tr>
 * <tr>
 * <td>'border-width' & TRBL</td>
 * <td>yes</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'border-color' & TRBL</td>
 * <td>yes</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'border-style' & TRBL</td>
 * <td>yes</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'border' & TRBL</td>
 * <td>yes</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'outline-width' & TRBL</td>
 * <td>no</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'outline-color' & TRBL</td>
 * <td>no</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'outline-style' & TRBL</td>
 * <td>no</td>
 * <td>BORDER</td>
 * </tr>
 * <tr>
 * <td>'outline' & TRBL</td>
 * <td>no</td>
 * <td>BORDER</td>
 * </tr>
 * 
 * <tr>
 * <td>'display'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'position'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'top'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'right'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'bottom'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'left'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'float'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'clear'</td>
 * <td>yes</td>
 * <td>BOX POSITIONING</td>
 * </tr>
 * <tr>
 * <td>'z-index'</td>
 * <td>yes</td>
 * <td>BOX LAYERED</td>
 * </tr>
 * 
 * <tr>
 * <td>'width'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * </tr>
 * <tr>
 * <td>'height'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * </tr>
 * <tr>
 * <td>'min-width'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * </tr>
 * <tr>
 * <td>'max-width'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * </tr>
 * <tr>
 * <td>'min-height'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * </tr>
 * <tr>
 * <td>'max-height'</td>
 * <td>yes</td>
 * <td>BOX DIMENSION</td>
 * <tr>
 * <td>'line-height'</td>
 * <td>yes</td>
 * <td>LINE HEIGHT</td>
 * </tr>
 * </tr>
 * <td>'vertical-align'</td>
 * <td>yes</td>
 * <td>LINE HEIGHT</td>
 * </tr>
 * 
 * <tr>
 * <td>'overflow'</td>
 * <td>yes</td>
 * <td>VISUAL EFFECTS</td>
 * </tr>
 * <tr>
 * <td>'clip'</td>
 * <td>part, no 'rect' function</td>
 * <td>VISUAL EFFECTS</td>
 * </tr>
 * <tr>
 * <td>'visibility'</td>
 * <td>yes</td>
 * <td>VISUAL EFFECTS</td>
 * </tr>
 * 
 * <tr>
 * <td>'content'</td>
 * <td>no</td>
 * <td>GENERATED CONTENT</td>
 * </tr>
 * <tr>
 * <td>'quote'</td>
 * <td>no</td>
 * <td>GENERATED CONTENT</td>
 * </tr>
 * <tr>
 * <td>'counter-reset'</td>
 * <td>no</td>
 * <td>AUTOMATIC COUNTERS</td>
 * </tr>
 * <tr>
 * <td>'counter-increment'</td>
 * <td>no</td>
 * <td>AUTOMATIC COUNTERS</td>
 * </tr>
 * <tr>
 * <td>'list-style-type'</td>
 * <td>yes</td>
 * <td>LISTS</td>
 * </tr>
 * <tr>
 * <td>'list-style-image'</td>
 * <td>yes</td>
 * <td>LISTS</td>
 * </tr>
 * <td>'list-style-position'</td>
 * <td>yes</td>
 * <td>LISTS</td>
 * </tr>
 * <td>'list-style'</td>
 * <td>yes</td>
 * <td>LISTS</td>
 * </tr>
 * 
 * <tr>
 * <td>'color'</td>
 * <td>yes</td>
 * <td>COLOR</td>
 * </tr>
 * <tr>
 * <td>'background-color'</td>
 * <td>yes</td>
 * <td>BACKGROUND</td>
 * </tr>
 * <tr>
 * <td>'background-image'</td>
 * <td>yes</td>
 * <td>BACKGROUND</td>
 * </tr>
 * <tr>
 * <td>'background-repeat'</td>
 * <td>yes</td>
 * <td>BACKGROUND</td>
 * </tr>
 * <tr>
 * <td>'background-attachment'</td>
 * <td>no</td>
 * <td>BACKGROUND</td>
 * </tr>
 * <tr>
 * <td>'background-position'</td>
 * <td>yes</td>
 * <td>BACKGROUND</td>
 * </tr>
 * <tr>
 * <td>'background'</td>
 * <td>yes</td>
 * <td>BACKGROUND</td>
 * </tr>
 * 
 * <tr>
 * <td>'font-family'</td>
 * <td>yes</td>
 * <td>FONT</td>
 * </tr>
 * <tr>
 * <td>'font-style'</td>
 * <td>yes</td>
 * <td>FONT</td>
 * </tr>
 * <tr>
 * <td>'font-variant'</td>
 * <td>yes</td>
 * <td>FONT</td>
 * </tr>
 * <tr>
 * <td>'font-weight'</td>
 * <td>yes</td>
 * <td>FONT</td>
 * </tr>
 * <tr>
 * <td>'font-size'</td>
 * <td>yes</td>
 * <td>FONT</td>
 * </tr>
 * <tr>
 * <td>'font'</td>
 * <td>part, no system font</td>
 * <td>FONT</td>
 * </tr>
 * 
 * <tr>
 * <td>'text-indent'</td>
 * <td>yes</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'text-align'</td>
 * <td>yes</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'text-decoration'</td>
 * <td>yes</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'text-transform'</td>
 * <td>yes</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'letter-spacing'</td>
 * <td>no</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'word-spacing'</td>
 * <td>no</td>
 * <td>TEXT</td>
 * </tr>
 * <tr>
 * <td>'white-space'</td>
 * <td>yes</td>
 * <td>TEXT</td>
 * </tr>
 * 
 * <tr>
 * <td>'caption-side'</td>
 * <td>yes</td>
 * <td>TABLE</td>
 * </tr>
 * <tr>
 * <td>'table-layout'</td>
 * <td>yes</td>
 * <td>TABLE</td>
 * </tr>
 * <tr>
 * <td>'border-collapse'</td>
 * <td>yes</td>
 * <td>TABLE</td>
 * </tr>
 * <tr>
 * <td>'border-spacing'</td>
 * <td>yes</td>
 * <td>TABLE</td>
 * </tr>
 * <tr>
 * <td>'empty-cells'</td>
 * <td>yes</td>
 * <td>TABLE</td>
 * </tr>
 * </table>
 * 
 * 
 * <p>
 * <b>Modifications From Original</b>
 * </p>
 * 
 * <ul>
 * <li>Remove HtmlWidget, SystemRequestHandler, etc...
 * <li>Remove the internal {@linkplain Font} dependency, the default pixel
 * conversion will use a {@link #DEFAULT_FONT_HEIGHT} constant, client may need
 * to define the value conversion algorithm by itself to adapt its real
 * environment
 * <li>Add DEBUG flag to control debug output
 * 
 * <li>Fixed the bug in handle 'border-spacing' property, CSS do not have
 * 'border-top-spacing', 'border-right-spacing'...
 * <li>Fixed the bug in 'border' property handling, add 'inherit' support
 * <li>Fixed the misspell in 'list-style-position' property name string
 * <li>Fixed the bugs of handling initial value
 * <li>Fixed the bugs of handling inherit value
 * <li>Fixed the bugs of handling background-position property
 * 
 * <li>Change the way to handle 'background-image', it has an index, and when it
 * is {@link #URI} type, the url will be kept in {@link #backgroundImage}
 * member, client need to call {@link #getBackgroundImageUrl()} to get the url
 * string , and when it is {@link #ENUM} type, the {@link #getValue(int)} may
 * return {@link Value#NONE} or {@link Value#INHERIT}
 * <li>Add {@link #URI} unit type
 * <li>Add 'font-style' property support
 * <li>Add 'font-variant' property support
 * <li>Add 'font-family' support
 * <li>Change the way to handle 'font-weight' property, separate handling enum
 * and number unit
 * <li>Add 'line-height' property support
 * <li>Add 'font' property support, not support system font hint
 * <li>Add RGB function support
 * <li>Add 'min-width', 'max-width', 'min-height' and 'max-height' properties
 * support
 * <li>Add 'list-style-image' support
 * <li>Add full 'list-style-type' support
 * <li>Add 'uc-border-style' property and 'ucpop' value support
 * </ul>
 * 
 * @author Stefan Haustein
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 */
public class Style {

	/** DEBUG flag, do not commit the code with TRUE flag!!! */
	public static final boolean DEBUG = CssTokenizer.DEBUG;

	/** Device dpi, default will be 100. */
	public static int DEVICE_DPI = 100;

	/** Default font height in px. */
	public static int DEFAULT_FONT_HEIGHT = 16;

	/** Default indent width in px. */
	public static int DEFAULT_INDENT = 30;

	// inherited properties and background color
	public static final int BACKGROUND_COLOR = 0; // only non-inherited
	public static final int BORDER_COLLAPSE = 1;
	public static final int BORDER_SPACING = 2;
	public static final int CAPTION_SIDE = 3;
	public static final int COLOR = 4;
	public static final int DISPLAY = 5;
	public static final int EMPTY_CELLS = 6;
	public static final int FONT_FAMILY = 7;
	public static final int FONT_STYLE = 8;
	public static final int FONT_VARIANT = 9;
	public static final int FONT_WEIGHT = 10;
	public static final int FONT_SIZE = 11;
	public static final int LINE_HEIGHT = 12;
	public static final int LIST_STYLE_IMAGE = 13;
	public static final int LIST_STYLE_POSITION = 14;
	public static final int LIST_STYLE_TYPE = 15;
	public static final int TEXT_ALIGN = 16;
	public static final int TEXT_DECORATION = 17;
	public static final int TEXT_INDENT = 18;
	public static final int TEXT_TRANSFORM = 19;
	public static final int VISIBILITY = 20;
	public static final int WHITE_SPACE = 21;

	// Note: TOP/RIGHT/BOTTOM/LEFT must be adjacent constants in ascending order
	public static final int MARGIN_TOP = 22;
	public static final int MARGIN_RIGHT = 23;
	public static final int MARGIN_BOTTOM = 24;
	public static final int MARGIN_LEFT = 25;

	public static final int BORDER_TOP_COLOR = 26;
	public static final int BORDER_RIGHT_COLOR = 27;
	public static final int BORDER_BOTTOM_COLOR = 28;
	public static final int BORDER_LEFT_COLOR = 29;
	public static final int BORDER_TOP_WIDTH = 30;
	public static final int BORDER_RIGHT_WIDTH = 31;
	public static final int BORDER_BOTTOM_WIDTH = 32;
	public static final int BORDER_LEFT_WIDTH = 33;
	public static final int BORDER_TOP_STYLE = 34;
	public static final int BORDER_RIGHT_STYLE = 35;
	public static final int BORDER_BOTTOM_STYLE = 36;
	public static final int BORDER_LEFT_STYLE = 37;

	public static final int PADDING_TOP = 38;
	public static final int PADDING_RIGHT = 39;
	public static final int PADDING_BOTTOM = 40;
	public static final int PADDING_LEFT = 41;

	public static final int WIDTH = 42;
	public static final int HEIGHT = 43;
	public static final int MIN_WIDTH = 44;
	public static final int MAX_WIDTH = 45;
	public static final int MIN_HEIGHT = 46;
	public static final int MAX_HEIGHT = 47;

	public static final int BACKGROUND_POSITION_X = 48;
	public static final int BACKGROUND_POSITION_Y = 49;
	public static final int BACKGROUND_REPEAT = 50;
	public static final int BACKGROUND_IMAGE = 51;

	public static final int CLEAR = 52;
	public static final int FLOAT = 53;
	public static final int OVERFLOW = 54;
	public static final int POSITION = 55;
	public static final int TOP = 56;
	public static final int RIGHT = 57;
	public static final int BOTTOM = 58;
	public static final int LEFT = 59;
	public static final int VERTICAL_ALIGN = 60;

	public static final int Z_INDEX = 61;
	public static final int CLIP = 62;
	public static final int TABLE_LAYOUT = 63;

	// all inherited properties and the background color
	public static final int TEXT_PROPERTY_COUNT = 22;
	public static final int PROPERTY_COUNT = 64;

	/**
	 * Flag for -top/-right/-bottom/-left abbreviation multiple value property
	 * handling
	 */
	public static final int MULTIVALUE_TRBL = 0x10000;
	private static final int MULTIVALUE_BORDER = 0x20000;

	private static final int MULTIVALUE_BACKGROUND = 0x1000;
	private static final int MULTIVALUE_BACKGROUND_POSITION = 0x1001;
	private static final int MULTIVALUE_FONT = 0x1002;
	private static final int MULTIVALUE_LIST_STYLE = 0x1003;

	static final int UNRECOGNIZED_PROPERTY_ID = 0x1234;
	static final Integer UNRECOGNIZED_PROPERTY = new Integer(
		UNRECOGNIZED_PROPERTY_ID);

	/**
	 * {Enum} Value of Enum unit.
	 */
	public static final class Value {

		//invalid value 
		public static final int INVALID = -1;

		//general common values
		public static final int NONE = 0;
		public static final int AUTO = 1;
		public static final int INHERIT = 2;
		public static final int SCROLL = 3;
		public static final int FIXED = 4;
		public static final int COLLAPSE = 5;
		public static final int VISIBLE = 6;
		public static final int HIDDEN = 7;
		public static final int CENTER = 8;
		public static final int NORMAL = 9;
		public static final int MEDIUM = 10;
		public static final int TOP = 56;
		public static final int RIGHT = 57;
		public static final int BOTTOM = 58;
		public static final int LEFT = 59;

		//background-attachment - scroll | fixed | inherit

		//background-position - [ [ <percentage> | <length> | left | center | right ]
		//[ <percentage> | <length> | top | center | bottom ]? ] | [ [ left | center | right ] 
		//|| [ top | center | bottom ] ] | inherit

		//background-repeat - repeat | repeat-x | repeat-y | no-repeat | inherit
		public static final int REPEAT = 20;
		public static final int REPEAT_X = 21;
		public static final int REPEAT_Y = 22;
		public static final int NO_REPEAT = 23;

		//border-collapse - collapse | separate | inherit
		public static final int SEPARATE = 24;

		//border-width - thin | medium | thick
		public static final int THIN = 25;
		public static final int THICK = 26;

		//border-style - hidden
		public static final int DOTTED = 27;
		public static final int DASHED = 28;
		public static final int SOLID = 29;
		public static final int DOUBLE = 30;
		public static final int GROOVE = 31;
		public static final int RIDGE = 32;
		public static final int INSET = 33;
		public static final int OUTSET = 34;
		public static final int UCPOP = 35;

		//caption-side - top | bottom | inherit

		//clear - none | left | right | both | inherit
		public static final int BOTH = 36;

		//display - inline | block | list-item | run-in | inline-block | table 
		//| inline-table | table-row-group | table-header-group | table-footer-group
		//| table-row | table-column-group | table-column | table-cell 
		//| table-caption | none | inherit
		public static final int INLINE = 37;
		public static final int BLOCK = 38;
		public static final int LIST_ITEM = 39;
		public static final int RUN_IN = 40;
		public static final int INLINE_BLOCK = 41;
		public static final int TABLE = 42;
		public static final int INLINE_TABLE = 43;
		public static final int TABLE_ROW_GROUP = 44;
		public static final int TABLE_HEADER_GROUP = 45;
		public static final int TABLE_FOOTER_GROUP = 46;
		public static final int TABLE_ROW = 47;
		public static final int TABLE_COLUMN_GROUP = 48;
		public static final int TABLE_COLUMN = 49;
		public static final int TABLE_CELL = 50;
		public static final int TABLE_CAPTION = 51;

		//empty-cells - show | hide | inherit
		public static final int SHOW = 52;
		public static final int HIDE = 53;

		//float - left | right | none | inherit

		//font-size
		public static final int XX_SMALL = 60;
		public static final int X_SMALL = 61;
		public static final int SMALL = 62;
		public static final int LARGE = 63;
		public static final int X_LARGE = 64;
		public static final int XX_LARGE = 65;
		public static final int LARGER = 66;
		public static final int SMALLER = 67;

		//font-style - normal
		public static final int ITALIC = 68;
		public static final int OBLIQUE = 69;

		// font-variant - normal
		public static final int SMALL_CAPS = 70;

		// font-weight - normal
		public static final int BOLD = 71;
		public static final int BOLDER = 72;
		public static final int LIGHTER = 73;

		//list-style-position
		public static final int INSIDE = 80;
		public static final int OUTSIDE = 81;

		// list-style-type
		public static final int DISC = 82;
		public static final int CIRCLE = 83;
		public static final int SQUARE = 84;
		public static final int DECIMAL = 85;
		public static final int DECIMAL_LEADING_ZERO = 86;
		public static final int LOWER_ROMAN = 87;
		public static final int UPPER_ROMAN = 88;
		public static final int LOWER_GREEK = 89;
		public static final int LOWER_LATIN = 90;
		public static final int UPPER_LATIN = 91;
		public static final int ARMENIAM = 92;
		public static final int GEORGIAN = 93;
		public static final int LOWER_ALPHA = 94;
		public static final int UPPER_ALPHA = 95;

		//overflow - visible, hidden, scroll 

		//position 
		public static final int STATIC = 100;
		public static final int RELATIVE = 101;
		public static final int ABSOLUTE = 102;

		//text-align - left, right, center, justify
		public static final int JUSTIFY = 103;

		//text-decoration
		public static final int UNDERLINE = 104;
		public static final int OVERLINE = 105;
		public static final int LINE_THROUGH = 106;
		public static final int BLINK = 107;

		//text-transform
		public static final int CAPITALIZE = 108;
		public static final int UPPERCASE = 109;
		public static final int LOWERCASE = 110;

		//vertical-align - baseline | sub | super | top | text-top | middle | bottom | text-bottom | <percentage> | <length> | inherit
		public static final int BASELINE = 111;
		public static final int SUB = 112;
		public static final int SUPER = 113;
		public static final int TEXT_TOP = 114;
		public static final int MIDDLE = 115;
		public static final int TEXT_BOTTOM = 116;

		//visibility - visible | hidden | collapse | inherit

		//whitespace - normal | pre | nowrap | pre-wrap | pre-line | inherit
		public static final int PRE = 117;
		public static final int NOWRAP = 118;
		public static final int PRE_WRAP = 119;
		public static final int PRE_LINE = 120;
	}

	/** Unknown unit. */
	public static final byte UNIT_UNKNOWN = -1;

	/**
	 * Unit constant for numbers without unit.
	 */
	public static final byte NUMBER = 0;

	/**
	 * Unit constant for percent values.
	 */
	public static final byte PERCENT = 1;

	/**
	 * Unit constant for centimeters (cm).
	 */
	public static final byte CM = 2;

	/**
	 * Unit constant for the m-width typographic unit.
	 */
	public static final byte EM = 3;

	/**
	 * Unit constant for the lowercase letter height typographic unit (ex).
	 */
	public static final byte EX = 4;

	/**
	 * Unit constant for inches (in).
	 */
	public static final byte IN = 5;

	/**
	 * Unit constant for millimeters (cm).
	 */
	public static final byte MM = 6;

	/**
	 * Unit constant for pica (pc, 12pt).
	 */
	public static final byte PC = 7;

	/**
	 * Unit constant for point (pt, 1/72 in).
	 */
	public static final byte PT = 8;

	/**
	 * Unit constant for pixels.
	 */
	public static final byte PX = 9;

	/**
	 * Unit constant to mark enumerated values (e.g. LEFT/RIGHT/CENTER/JUSTIFY
	 * etc.).
	 */
	public static final byte ENUM = 16;

	/**
	 * Unit constant for color values (Note: The COLOR constant denotes the CSS
	 * property name "color").
	 */
	public static final byte ARGB = 17;

	/**
	 * Unit constant for uri string such as 'background-image' and
	 * 'list-style-image'.
	 */
	public static final byte URI = 18;

	/**
	 * Unit constant for index to refer to an value object inside a list.
	 */
	public static final byte INDEX = 19;

	/** Empty Style object for defensive programming */
	public static final Style EMPTY_STYLE = new Style();

	/**
	 * Names of the units, the order must correspond to the unit constants
	 * (NUMBER, PERCENT, CM, ...)
	 */
	private static final String[] UNIT_NAMES = { "", "%", "cm", "em", "ex",
	"in", "mm", "pc", "pt", "px" };

	static final int[] TOP_LEVEL = new int[0];

	public static final Hashtable NAME_TO_ID_MAP;
	public static final Hashtable VALUE_TO_ID_MAP;

	//debug only hashtable
	static Hashtable ID_TO_NAME_MAP;
	static Hashtable ID_TO_VALUE_MAP;

	private static final String[] TRBL = { "-top", "-right", "-bottom", "-left" };

	private static final int POS_AFTER_FONT_SIZE = 100;

	/**
	 * Bit field, keeping track of which fields have explicitly been set for
	 * this Style object.
	 */
	long valuesSet;
	int firstSet = PROPERTY_COUNT;
	int lastSet;

	int[] values = new int[TEXT_PROPERTY_COUNT];
	byte[] units = new byte[TEXT_PROPERTY_COUNT];

	/** List style image url. */
	private String listStyleImage;

	/** Background image url. */
	private String backgroundImage;

	/** Font family string. */
	private StringBuffer fontFamily;

	/**
	 * Specificity of this style, set by the corresponding selector in the
	 * stylesheet parse method.
	 */
	int specificity;

	/**
	 * Position of this style declaration inside the style sheet
	 */
	int position;

	/**
	 * Nested import positions
	 */
	int[] nesting = TOP_LEVEL;

	static {

		NAME_TO_ID_MAP = new Hashtable();
		VALUE_TO_ID_MAP = new Hashtable();

		if (DEBUG) {

			ID_TO_NAME_MAP = new Hashtable();
			ID_TO_VALUE_MAP = new Hashtable();
		}

		addName("background", MULTIVALUE_BACKGROUND);
		addName("background-color", BACKGROUND_COLOR);
		addName("background-position", MULTIVALUE_BACKGROUND_POSITION);
		addName("background-position-x", BACKGROUND_POSITION_X);
		addName("background-position-y", BACKGROUND_POSITION_Y);
		addName("background-repeat", BACKGROUND_REPEAT);
		addName("background-image", BACKGROUND_IMAGE);
		addName("border", MULTIVALUE_BORDER | MULTIVALUE_TRBL);
		addName("border-collapse", BORDER_COLLAPSE);
		addName("border-color", MULTIVALUE_TRBL | BORDER_TOP_COLOR);
		addName("border-style", MULTIVALUE_TRBL | BORDER_TOP_STYLE);
		addName("uc-border-style", MULTIVALUE_TRBL | BORDER_TOP_STYLE);
		addName("border-width", MULTIVALUE_TRBL | BORDER_TOP_WIDTH);
		addName("border-spacing", BORDER_SPACING);
		//addName("border-spacing", MULTIVALUE_TRBL | BORDER_TOP_SPACING);
		addName("bottom", BOTTOM);
		addName("caption-side", CAPTION_SIDE);
		addName("clear", CLEAR);
		addName("clip", CLIP);
		addName("color", COLOR);
		addName("display", DISPLAY);
		addName("empty-cells", EMPTY_CELLS);
		addName("float", FLOAT);
		addName("font", MULTIVALUE_FONT);
		addName("font-family", FONT_FAMILY);
		addName("font-style", FONT_STYLE);
		addName("font-variant", FONT_VARIANT);
		addName("font-weight", FONT_WEIGHT);
		addName("font-size", FONT_SIZE);
		addName("line-height", LINE_HEIGHT);

		addName("height", HEIGHT);
		addName("left", LEFT);
		addName("list-style", MULTIVALUE_LIST_STYLE);
		addName("list-style-image", LIST_STYLE_IMAGE);
		addName("list-style-position", LIST_STYLE_POSITION);
		addName("list-style-type", LIST_STYLE_TYPE);
		addName("margin", MULTIVALUE_TRBL | MARGIN_TOP);
		addName("overflow", OVERFLOW);
		addName("padding", MULTIVALUE_TRBL | PADDING_TOP);
		addName("position", POSITION);
		addName("right", RIGHT);
		addName("table-layout", TABLE_LAYOUT);
		addName("text-align", TEXT_ALIGN);
		addName("text-decoration", TEXT_DECORATION);
		addName("text-indent", TEXT_INDENT);
		addName("text-transform", TEXT_TRANSFORM);
		addName("top", TOP);
		addName("vertical-align", VERTICAL_ALIGN);
		addName("visibility", VISIBILITY);
		addName("white-space", WHITE_SPACE);
		addName("width", WIDTH);
		addName("min-width", MIN_WIDTH);
		addName("max-width", MAX_WIDTH);
		addName("min-height", MIN_HEIGHT);
		addName("max-height", MAX_HEIGHT);
		addName("z-index", Z_INDEX);

		for (int i = 0; i < 4; i++) {

			addName("border" + TRBL[i] + "-color", BORDER_TOP_COLOR + i);
			addName("border" + TRBL[i] + "-style", BORDER_TOP_STYLE + i);
			addName("border" + TRBL[i] + "-width", BORDER_TOP_WIDTH + i);
			//addName("border" + TRBL[i] + "-spacing", BORDER_TOP_SPACING + i);
			addName("border" + TRBL[i], MULTIVALUE_BORDER | i);
			addName("margin" + TRBL[i], MARGIN_TOP + i);
			addName("padding" + TRBL[i], PADDING_TOP + i);
		}

		addValue("auto", Value.AUTO, ENUM);
		addValue("none", Value.NONE, ENUM);
		addValue("visible", Value.VISIBLE, ENUM);
		addValue("hidden", Value.HIDDEN, ENUM);
		addValue("collapse", Value.COLLAPSE, ENUM);
		addValue("inherit", Value.INHERIT, ENUM);

		addValue("italic", Value.ITALIC, ENUM);
		addValue("oblique", Value.OBLIQUE, ENUM);

		addValue("small-caps", Value.SMALL_CAPS, ENUM);

		addValue("lighter", Value.LIGHTER, ENUM);
		addValue("normal", Value.NORMAL, ENUM);
		addValue("bold", Value.BOLD, ENUM);
		addValue("bolder", Value.BOLDER, ENUM);

		addValue("xx-small", Value.XX_SMALL, ENUM);
		addValue("x-small", Value.X_SMALL, ENUM);
		addValue("small", Value.SMALL, ENUM);
		addValue("medium", Value.MEDIUM, ENUM);
		addValue("large", Value.LARGE, ENUM);
		addValue("x-large", Value.X_LARGE, ENUM);
		addValue("xx-large", Value.XX_LARGE, ENUM);
		addValue("larger", Value.LARGER, ENUM);
		addValue("smaller", Value.SMALLER, ENUM);

		addValue("inline", Value.INLINE, ENUM);
		addValue("inline-block", Value.INLINE_BLOCK, ENUM);
		addValue("block", Value.BLOCK, ENUM);
		addValue("list-item", Value.LIST_ITEM, ENUM);
		addValue("run-in", Value.RUN_IN, ENUM);
		addValue("table", Value.TABLE, ENUM);
		addValue("inline-table", Value.INLINE_TABLE, ENUM);
		addValue("table-row", Value.TABLE_ROW, ENUM);
		addValue("table-tow-group", Value.TABLE_ROW_GROUP, ENUM);
		addValue("table-header-group", Value.TABLE_HEADER_GROUP, ENUM);
		addValue("table-footer-group", Value.TABLE_FOOTER_GROUP, ENUM);
		addValue("table-column", Value.TABLE_COLUMN, ENUM);
		addValue("table-column-group", Value.TABLE_COLUMN_GROUP, ENUM);
		addValue("table-cell", Value.TABLE_CELL, ENUM);
		addValue("table-caption", Value.TABLE_CAPTION, ENUM);

		addValue("separate", Value.SEPARATE, ENUM);

		// various
		// note: top/left/right/bottom already defined as property names
		addValue("absolute", Value.ABSOLUTE, ENUM);
		addValue("baseline", Value.BASELINE, ENUM);
		addValue("both", Value.BOTH, ENUM);
		addValue("bottom", Value.BOTTOM, ENUM);
		addValue("center", Value.CENTER, ENUM);
		addValue("capitalize", Value.CAPITALIZE, ENUM);
		addValue("fixed", Value.FIXED, ENUM);
		addValue("hide", Value.HIDE, ENUM);
		addValue("inside", Value.INSIDE, ENUM);
		addValue("justify", Value.JUSTIFY, ENUM);
		addValue("medium", Value.MEDIUM, ENUM);
		addValue("middle", Value.MIDDLE, ENUM);
		addValue("no-repeat", Value.NO_REPEAT, ENUM);
		addValue("nowrap", Value.NOWRAP, ENUM);
		addValue("outside", Value.OUTSIDE, ENUM);
		addValue("pre", Value.PRE, ENUM);
		addValue("pre-wrap", Value.PRE_WRAP, ENUM);
		addValue("pre-line", Value.PRE_LINE, ENUM);
		addValue("relative", Value.RELATIVE, ENUM);
		addValue("scroll", Value.SCROLL, ENUM);
		addValue("sub", Value.SUB, ENUM);
		addValue("super", Value.SUPER, ENUM);
		addValue("show", Value.SHOW, ENUM);
		addValue("static", Value.STATIC, ENUM);
		addValue("top", Value.TOP, ENUM);
		addValue("text-top", Value.TEXT_TOP, ENUM);
		addValue("text-bottom", Value.TEXT_BOTTOM, ENUM);
		addValue("thick", Value.THICK, ENUM);
		addValue("thin", Value.THIN, ENUM);
		addValue("underline", Value.UNDERLINE, ENUM);
		addValue("overline", Value.OVERLINE, ENUM);
		addValue("line-through", Value.LINE_THROUGH, ENUM);
		addValue("blink", Value.BLINK, ENUM);
		addValue("uppercase", Value.UPPERCASE, ENUM);
		addValue("lowercase", Value.LOWERCASE, ENUM);
		addValue("repeat", Value.REPEAT, ENUM);
		addValue("repeat-x", Value.REPEAT_X, ENUM);
		addValue("repeat-y", Value.REPEAT_Y, ENUM);

		addValue("square", Value.SQUARE, ENUM);
		addValue("circle", Value.CIRCLE, ENUM);
		addValue("disc", Value.DISC, ENUM);
		addValue("decimal", Value.DECIMAL, ENUM);
		addValue("decimal-leading-zero", Value.DECIMAL_LEADING_ZERO, ENUM);
		addValue("lower-roman", Value.LOWER_ROMAN, ENUM);
		addValue("upper-roman", Value.UPPER_ROMAN, ENUM);
		addValue("lower-greek", Value.LOWER_GREEK, ENUM);
		addValue("lower-latin", Value.LOWER_LATIN, ENUM);
		addValue("upper-latin", Value.UPPER_LATIN, ENUM);
		addValue("armenian", Value.ARMENIAM, ENUM);
		addValue("georgian", Value.GEORGIAN, ENUM);
		addValue("lower-alpha", Value.LOWER_ALPHA, ENUM);
		addValue("upper-alpha", Value.UPPER_ALPHA, ENUM);

		addValue("dotted", Value.DOTTED, ENUM);
		addValue("dashed", Value.DASHED, ENUM);
		addValue("solid", Value.SOLID, ENUM);
		addValue("double", Value.DOUBLE, ENUM);
		addValue("groove", Value.GROOVE, ENUM);
		addValue("rdige", Value.RIDGE, ENUM);
		addValue("inset", Value.INSET, ENUM);
		addValue("outset", Value.OUTSET, ENUM);
		addValue("ucpop", Value.UCPOP, ENUM);

		addValue("left", Value.LEFT, ENUM);
		addValue("right", Value.RIGHT, ENUM);

		addValue("transparent", 0, ARGB);
		addValue("maroon", 0xff800000, ARGB);
		addValue("red", 0x0ffff0000, ARGB);
		addValue("orange", 0xffffA500, ARGB);
		addValue("yellow", 0xffffff00, ARGB);
		addValue("olive", 0xff808000, ARGB);
		addValue("purple", 0xff800080, ARGB);
		addValue("fuchsia", 0xffff00ff, ARGB);
		addValue("white", 0xffffffff, ARGB);
		addValue("lime", 0xff00ff00, ARGB);
		addValue("green", 0xff008000, ARGB);
		addValue("navy", 0xff000080, ARGB);
		addValue("blue", 0xff0000ff, ARGB);
		addValue("aqua", 0xff00ffff, ARGB);
		addValue("teal", 0xff008080, ARGB);
		addValue("black", 0xff000000, ARGB);
		addValue("silver", 0xffc0c0c0, ARGB);
		addValue("gray", 0xff808080, ARGB);
	}

	private static void addValue(String value, int id, byte unit) {

		VALUE_TO_ID_MAP.put(value, new Long((((long) unit) << 32) |
			(id & 0x0ffffffffL)));

		if (unit == ENUM && DEBUG)
			ID_TO_VALUE_MAP.put(new Integer(id), value);
	}

	private static void addName(String name, int id) {

		Integer i = new Integer(id);

		NAME_TO_ID_MAP.put(name, i);

		if (DEBUG)
			ID_TO_NAME_MAP.put(i, name);
	}

	/**
	 * Creates a style object with the color property black, normal font weight
	 * and display type "inline"; all other values are defined initial value of
	 * the property.
	 */
	public Style() {

	}

	/**
	 * Reads a group of style declarations from a string.
	 */
	public void read(String url, String def) {

		CssTokenizer ct = new CssTokenizer(url, def);
		read(ct);
	}

	/**
	 * Reads a style declaration from a CSS tokenizer.
	 */
	void read(CssTokenizer tokenizer) {

		String name = null;

		Integer idObj = null;
		int id = 0;

		while (tokenizer.ttype != CssTokenizer.TT_EOF && tokenizer.ttype != '}') {

			if (tokenizer.ttype == CssTokenizer.TT_IDENT) {

				name = tokenizer.sval;
				idObj = (Integer) NAME_TO_ID_MAP.get(name);

				//get the id of identifier
				if (idObj == null) {
					tokenizer.debug("unrecognized property");
					id = UNRECOGNIZED_PROPERTY_ID;
				}
				else {
					id = idObj.intValue();
				}

				tokenizer.nextToken(false);
				if (tokenizer.ttype != ':') {
					continue;
				}
				tokenizer.nextToken(false);

				readPropertyValue(tokenizer, name, id);
			}

			// handle !important
			if (tokenizer.ttype == '!') {
				tokenizer.nextToken(false);
				if (tokenizer.ttype == CssTokenizer.TT_IDENT &&
					"important".equals(tokenizer.sval)) {
					specificity = StyleSheet.SPECIFICITY_IMPORTANT;
					tokenizer.nextToken(false);
				}
			}

			// skip trailing trash
			while (tokenizer.ttype != CssTokenizer.TT_EOF &&
				tokenizer.ttype != ';' && tokenizer.ttype != '}') {
				tokenizer.debug("skipping");
				tokenizer.nextToken(false);
			}
			while (tokenizer.ttype == ';') {
				tokenizer.nextToken(false);
			}
		}
	}

	/**
	 * Read the value of property in a declaration
	 */
	private void readPropertyValue(CssTokenizer tokenizer, String name, int id) {

		int pos = 0;
		loop: while (true) {

			switch (tokenizer.ttype) {

			case CssTokenizer.TT_HASH:
				setColor(id, '#' + tokenizer.sval, pos);
				break;

			case CssTokenizer.TT_DIMENSION:
				byte unit = (byte) indexOf(UNIT_NAMES,
					tokenizer.sval.toLowerCase());

				set(id, tokenizer.nval, unit, pos);
				break;

			case CssTokenizer.TT_NUMBER:
				set(id, tokenizer.nval, NUMBER, pos);
				break;

			case CssTokenizer.TT_PERCENTAGE:
				set(id, tokenizer.nval, PERCENT, pos);
				break;

			case CssTokenizer.TT_URI:
				if (id == MULTIVALUE_BACKGROUND || id == BACKGROUND_IMAGE) {

					backgroundImage = StyleSheet.getAbsoluteUrl(tokenizer.url,
						tokenizer.sval);

					set(BACKGROUND_IMAGE, URI, URI);
				}
				else if (id == LIST_STYLE_IMAGE) {

					listStyleImage = StyleSheet.getAbsoluteUrl(tokenizer.url,
						tokenizer.sval);

					set(LIST_STYLE_IMAGE, URI, URI);
				}
				break;

			case CssTokenizer.TT_FUNCTION:
				String func = tokenizer.sval.toLowerCase();

				//handle rgb function
				if ("rgb".equals(func)) {

					int p1 = tokenizer.nextToken(false);

					int r = (p1 == CssTokenizer.TT_PERCENTAGE
						? tokenizer.nval * 255 / 100 : tokenizer.nval) / 1000;

					if (tokenizer.nextToken(false) != ',')
						tokenizer.debug("rgb() require green channel");

					int p2 = tokenizer.nextToken(false);
					int g = (p2 == CssTokenizer.TT_PERCENTAGE
						? tokenizer.nval * 255 / 100 : tokenizer.nval) / 1000;

					if (tokenizer.nextToken(false) != ',')
						tokenizer.debug("rgb() require blue channel");

					int p3 = tokenizer.nextToken(false);
					int b = (p3 == CssTokenizer.TT_PERCENTAGE
						? tokenizer.nval * 255 / 100 : tokenizer.nval) / 1000;

					int val = 0x0ff000000 | (r << 16) | (g << 8) | b;
					set(id, val, ARGB, pos);

					tokenizer.nextToken(false);
				}
				else {

					tokenizer.debug("unknown function:" + func);
					break loop;
				}
				break;

			case '/':
				//make the setFont method know it already pass 
				//the font-size property
				if (id == MULTIVALUE_FONT) {

					pos += POS_AFTER_FONT_SIZE;
				}
				break;

			case ',':
				// ignore for now...
				break;

			case CssTokenizer.TT_IDENT:

				Long valObj = (Long) VALUE_TO_ID_MAP.get(tokenizer.sval.toLowerCase());

				if (valObj != null) {

					set(id, (int) valObj.longValue(),
						(byte) (valObj.longValue() >>> 32), pos);

					break;
				}
				else if (id != UNRECOGNIZED_PROPERTY_ID &&
					id != MULTIVALUE_FONT) {

					tokenizer.debug("Unrecognized value '" + valObj +
						"' for property " + name);
				}

			case CssTokenizer.TT_STRING:

				if (id == FONT_FAMILY || id == MULTIVALUE_FONT) {

					if (fontFamily == null)
						fontFamily = new StringBuffer();
					else
						fontFamily.append(',');

					fontFamily.append(tokenizer.sval);

					set(FONT_FAMILY, URI, URI);
				}
				break;

			default:
				break loop;
			}
			pos++;
			tokenizer.nextToken(false);
		}
	}

	/**
	 * Style do not contains any properties?
	 */
	public boolean isEmpty() {

		return valuesSet == 0;
	}

	/**
	 * Compares the specificity of this style to s2 and returns the difference.
	 */
	public int compare(Style s2) {

		if (specificity > s2.specificity) {
			return 1;
		}
		else if (specificity < s2.specificity) {
			return -1;
		}
		else {
			int min = Math.min(nesting.length, s2.nesting.length);
			for (int i = 0; i < min; i++) {
				if (nesting[i] > s2.nesting[i]) {
					return 1;
				}
				else if (nesting[i] < s2.nesting[i]) {
					return -1;
				}
			}

			int n1 = min + 1 < nesting.length ? nesting[min + 1] : position;
			int n2 = min + 1 < s2.nesting.length ? s2.nesting[min + 1]
				: s2.position;

			return n1 - n2;
		}
	}

	/**
	 * Inherit values from the given style. Needs to be called before a style
	 * can be used in order to get rid of INHERIT values.
	 * 
	 * @param from the style to inherit from
	 */
	public void inherit(Style from) {

		if (from == null) {

			//inherit initial value
			from = EMPTY_STYLE;
		}

		int max = Math.max(lastSet, from.lastSet);

		for (int id = Math.min(firstSet, from.firstSet); id <= max; id++) {

			boolean set = isSet(id);
			boolean formSet = from.isSet(id);

			//check whether the property is with value 'inherit' 
			//or by default is inherit
			if ((set && values[id] == Value.INHERIT && units[id] == ENUM) ||
				(!set && id < TEXT_PROPERTY_COUNT && id != BACKGROUND_COLOR && id != DISPLAY)) {

				if (formSet) {

					set(id, from.values[id], from.units[id]);
				}
				else {

					//use the initial value and its unit
					set(id, from.getValue(id), (byte) from.getUnit(id));
				}
			}
		}
	}

	/**
	 * Copies all style information set in from to this
	 * 
	 * @param from the style to copy from
	 */
	public void set(Style from) {

		if (from == null)
			return;

		for (int id = from.firstSet; id <= from.lastSet; id++) {

			if (from.isSet(id)) {

				set(id, from.values[id], from.units[id]);
			}
		}

		if (from.backgroundImage != null)
			backgroundImage = from.backgroundImage;

		if (from.listStyleImage != null)
			listStyleImage = from.listStyleImage;

		if (from.fontFamily != null)
			fontFamily = from.fontFamily;
	}

	/**
	 * Returns true if the given property is set to a value
	 */
	public boolean isSet(int id) {

		return (valuesSet & (1L << id)) != 0;
	}

	/**
	 * Set the given property to the given value and unit.
	 * 
	 * @param id CSS property id constant
	 * @param value the value to set (multiply lengths with 1000)
	 * @param unit the unit (NUMBER, PERCENT, CM, EM, EX, IN, MM, PC, PT, PX,
	 *            ARGB)
	 * @return this style
	 */
	public Style set(int id, int value, byte unit) {

		if (id != UNRECOGNIZED_PROPERTY_ID) {

			if (id >= values.length) {

				int[] newValues = new int[PROPERTY_COUNT];
				byte[] newUnits = new byte[PROPERTY_COUNT];
				System.arraycopy(values, 0, newValues, 0, values.length);
				System.arraycopy(units, 0, newUnits, 0, units.length);
				values = newValues;
				units = newUnits;
			}

			values[id] = value;
			units[id] = unit;
			valuesSet |= 1L << id;
			firstSet = Math.min(firstSet, id);
			lastSet = Math.max(lastSet, id);
		}

		return this;
	}

	/**
	 * Internal version of set that takes the value position into account
	 * for multi value properties (border, font, margin etc.)
	 * 
	 * @param id property id
	 * @param value property value
	 * @param unit unit constant
	 * @param pos position of this value
	 * @return this
	 */
	public Style set(int id, int value, byte unit, int pos) {

		if ((id & MULTIVALUE_BORDER) != 0) {
			id -= MULTIVALUE_BORDER;
			switch (unit) {
			case ARGB:
				id += BORDER_TOP_COLOR;
				break;

			case ENUM:
				switch (value) {
				case Value.MEDIUM:
				case Value.THIN:
				case Value.THICK:
					id += BORDER_TOP_WIDTH;
					break;

				case Value.INHERIT://inherit!!!
					if (pos == 0) {

						set(MULTIVALUE_TRBL | BORDER_TOP_WIDTH, value, unit, 0);
						set(MULTIVALUE_TRBL | BORDER_TOP_STYLE, value, unit, 0);
						set(MULTIVALUE_TRBL | BORDER_TOP_COLOR, value, unit, 0);
					}
				default:
					id += BORDER_TOP_STYLE;
				}
				break;

			default:
				id += BORDER_TOP_WIDTH;
			}
			set(id, value, unit, 0);
		}
		else if ((id & MULTIVALUE_TRBL) != 0) {
			id -= MULTIVALUE_TRBL;
			switch (pos) {
			case 0:
				set(id, value, unit);
				set(id + 1, value, unit);
				set(id + 2, value, unit);
				set(id + 3, value, unit);
				break;
			case 1:
				set(id + 1, value, unit);
				set(id + 3, value, unit);
				break;
			case 2:
				set(id + 2, value, unit);
				break;
			case 3:
				set(id + 3, value, unit);
				break;
			}
		}
		else {
			switch (id) {

			case MULTIVALUE_FONT:

				if (pos == 0) {

					//initialize all font properties 
					set(FONT_FAMILY, Value.NONE, ENUM);
					set(FONT_STYLE, Value.NORMAL, ENUM);
					set(FONT_VARIANT, Value.NORMAL, ENUM);
					set(FONT_WEIGHT, Value.NORMAL, ENUM);
					set(FONT_SIZE, Value.MEDIUM, ENUM);
					set(LINE_HEIGHT, Value.NORMAL, ENUM);
				}

				if (unit == ENUM) {//enum

					switch (value) {

					case Value.ITALIC:
					case Value.OBLIQUE:
						set(FONT_STYLE, value, ENUM);
						break;

					case Value.SMALL_CAPS:
						set(FONT_VARIANT, value, ENUM);
						break;

					case Value.BOLD:
					case Value.BOLDER:
					case Value.LIGHTER:
						set(FONT_WEIGHT, value, ENUM);
						break;

					case Value.XX_SMALL:
					case Value.X_SMALL:
					case Value.SMALL:
					case Value.SMALLER:
					case Value.MEDIUM:
					case Value.LARGE:
					case Value.LARGER:
					case Value.X_LARGE:
					case Value.XX_LARGE:
						set(FONT_SIZE, value, ENUM);
						break;

					case Value.INHERIT:

						if (pos == 0) {

							//all inherit
							set(FONT_FAMILY, Value.INHERIT, ENUM);
							set(FONT_STYLE, Value.INHERIT, ENUM);
							set(FONT_VARIANT, Value.INHERIT, ENUM);
							set(FONT_WEIGHT, Value.INHERIT, ENUM);
							set(FONT_SIZE, Value.INHERIT, ENUM);
							set(LINE_HEIGHT, Value.INHERIT, ENUM);
						}
						break;
					}
				}
				else if (unit == NUMBER) {//number without unit

					if (value % 100 == 0 && value >= 100 && value <= 900) {

						set(FONT_WEIGHT, value, unit);
					}
				}
				else {//length and percentage

					if (pos < POS_AFTER_FONT_SIZE) {

						set(FONT_SIZE, value, unit);
					}
					else {

						set(LINE_HEIGHT, value, unit);
					}
				}
				break;

			case MULTIVALUE_BACKGROUND:
				if (unit == ENUM && value == Value.INHERIT && pos == 0) {
					set(BACKGROUND_COLOR, Value.INHERIT, ENUM);
					set(BACKGROUND_REPEAT, Value.INHERIT, ENUM);
					set(BACKGROUND_POSITION_X, Value.INHERIT, ENUM);
					set(BACKGROUND_POSITION_Y, Value.INHERIT, ENUM);
					set(BACKGROUND_IMAGE, Value.INHERIT, ENUM);
				}
				else if (unit == ARGB) {
					set(BACKGROUND_COLOR, value, ARGB);
				}
				else if (unit == ENUM &&
					(value == Value.NO_REPEAT || value == Value.REPEAT ||
						value == Value.REPEAT_X || value == Value.REPEAT_Y)) {
					set(BACKGROUND_REPEAT, value, ENUM);
				}
				else if (unit == ENUM &&
					(value == Value.SCROLL || value == Value.FIXED)) {
					// ignore attachment
				}
				else if (!isSet(BACKGROUND_POSITION_X) &&
					!isSet(BACKGROUND_POSITION_Y)) {
					set(MULTIVALUE_BACKGROUND_POSITION, value, unit, 0);
				}
				else {
					set(MULTIVALUE_BACKGROUND_POSITION, value, unit, 1);
				}
				break;
			case MULTIVALUE_BACKGROUND_POSITION:
				//its very tricky to handle the top/right/bottom/left/center...
				//see the fucking spec for the detail!!!
				if (unit == ENUM) {

					switch (value) {
					case Value.TOP:
					case Value.BOTTOM:
						set(Style.BACKGROUND_POSITION_Y, value, Style.ENUM);
						break;
					case Value.RIGHT:
					case Value.LEFT:
						set(Style.BACKGROUND_POSITION_X, value, Style.ENUM);
						break;
					default:
						if (!isSet(Style.BACKGROUND_POSITION_X))
							set(BACKGROUND_POSITION_X, value, unit);
						else if (!isSet(Style.BACKGROUND_POSITION_Y))
							set(BACKGROUND_POSITION_Y, value, unit);
						break;
					}
				}
				else {

					if (pos == 0)
						set(BACKGROUND_POSITION_X, value, unit);
					else
						set(BACKGROUND_POSITION_Y, value, unit);
				}

				// to make the not set one as center or 50% 
				// depend on the already set type
				if (!isSet(Style.BACKGROUND_POSITION_X)) {

					if (unit == ENUM) {
						
						set(Style.BACKGROUND_POSITION_X, Value.CENTER,
							Style.ENUM);
					}
					else {
						
						set(Style.BACKGROUND_POSITION_X, 50 * 1000,
							Style.PERCENT);
					}
				}

				if (!isSet(Style.BACKGROUND_POSITION_Y)) {

					if (unit == ENUM) {
						
						set(Style.BACKGROUND_POSITION_Y, Value.CENTER,
							Style.ENUM);
					}
					else {
						
						set(Style.BACKGROUND_POSITION_Y, 50 * 1000,
							Style.PERCENT);
					}
				}
				break;
			case MULTIVALUE_LIST_STYLE:
				if (pos == 0 && unit == ENUM && value == Value.INHERIT) {
					set(LIST_STYLE_POSITION, Value.INHERIT, ENUM);
					set(LIST_STYLE_TYPE, Value.INHERIT, ENUM);
				}
				else if (unit == ENUM &&
					(value == Value.INSIDE || value == Value.OUTSIDE)) {
					set(LIST_STYLE_POSITION, value, ENUM);
				}
				else {
					set(LIST_STYLE_TYPE, value, unit);
				}
				break;

			default:
				set(id, value, unit);
			}
		}
		return this;
	}

	/**
	 * Set a CSS color value.
	 * 
	 * @param id field id (COLOR, ...)
	 * @param color the color in the form #RGB or #RRGGBB or one of the 16 CSS
	 *            color identifiers
	 * @param pos index of the border-color value (0..3); 0 otherwise
	 */
	public void setColor(int id, String color, int pos) {

		if (color.length() > 0 && color.charAt(0) == '#') {
			try {
				// #RGB or #RRGGBB hexadecimal color value
				int value = Integer.parseInt(color.substring(1), 16);
				if (color.length() == 4) {
					value = (value & 0x00f) | ((value & 0x0ff) << 4) |
						((value & 0xff0) << 8) | ((value & 0xf00) << 12);
				}
				// set with transparency opaque
				set(id, 0x0ff000000 | value, ARGB, pos);
			}
			catch (NumberFormatException e) {
				// ignore invalid colors
			}
		}
		else {
			Long v = (Long) VALUE_TO_ID_MAP.get(color.toLowerCase());
			if (v != null) {
				long l = v.longValue();
				if ((l >>> 32) == ARGB) {
					set(id, (int) l, ARGB, pos);
				}
			}
		}
	}

	/**
	 * Get the specificity of the Style.
	 */
	public int getSpecificity() {

		return this.specificity;
	}

	/**
	 * Returns the raw integer value for the given property value. For lengths,
	 * please use getPx() to obtain a pixel integer value. Lengths are stored
	 * internally multiplied by 1000. This method does all the default handling
	 * (that is not handled in the default style sheet).
	 * 
	 * @param id property id
	 * @return raw property value
	 */
	public int getValue(int id) {

		//    if (id >= units.length || (units[id] == PERCENT && id == HEIGHT)) {
		//      return id == WIDTH || id == HEIGHT ? AUTO : 0;
		//    }
		if (isSet(id)) {

			if (id == HEIGHT && units[id] == PERCENT)
				return Value.AUTO;

			return values[id];
		}

		//property not set, return initial value
		switch (id) {
		case BORDER_TOP_WIDTH:
		case BORDER_BOTTOM_WIDTH:
		case BORDER_LEFT_WIDTH:
		case BORDER_RIGHT_WIDTH:
		case FONT_SIZE:
			return Value.MEDIUM;
		case BORDER_TOP_COLOR:
		case BORDER_BOTTOM_COLOR:
		case BORDER_LEFT_COLOR:
		case BORDER_RIGHT_COLOR:
			//border-color's initial value is the value of 'color'
			return getValue(COLOR);
		case BACKGROUND_COLOR:
			return 0x00000000;
		case FONT_STYLE:
		case FONT_VARIANT:
		case FONT_WEIGHT:
		case LINE_HEIGHT:
			return Value.NORMAL;
		case BOTTOM:
		case HEIGHT:
		case LEFT:
		case RIGHT:
		case TABLE_LAYOUT:
		case TOP:
		case WIDTH:
		case CLIP:
		case Z_INDEX:
			return Value.AUTO;
		case VERTICAL_ALIGN:
			return Value.BASELINE;
		case COLOR:
			return 0xff000000;
		case DISPLAY:
			return Value.INLINE;
		case OVERFLOW:
		case VISIBILITY:
			return Value.VISIBLE;
		case LIST_STYLE_TYPE:
			return Value.DISC;
		case LIST_STYLE_POSITION:
			return Value.OUTSIDE;
		case POSITION:
			return Value.STATIC;
		case BACKGROUND_REPEAT:
			return Value.REPEAT;
		case TEXT_ALIGN:
			return Value.LEFT;
		case WHITE_SPACE:
			return Value.NORMAL;
		case CAPTION_SIDE:
			return Value.TOP;
		case BORDER_COLLAPSE:
			return Value.SEPARATE;
		case EMPTY_CELLS:
			return Value.SHOW;
		}
		return 0;
	}

	/**
	 * Returns the unit for the given property.
	 * 
	 * @param id id of property
	 * @return One of the length unit constants (PX etc), PERCENT, NUMBER or
	 *         ENUM.
	 */
	public int getUnit(int id) {

		if (isSet(id)) {
			return units[id];
		}

		//property not set, return the unit of initial value
		switch (id) {
		case BACKGROUND_POSITION_X:
		case BACKGROUND_POSITION_Y:
			return PERCENT;
		case BORDER_TOP_COLOR:
		case BORDER_BOTTOM_COLOR:
		case BORDER_LEFT_COLOR:
		case BORDER_RIGHT_COLOR:
		case BACKGROUND_COLOR:
		case COLOR:
			return ARGB;
		case MIN_WIDTH:
		case MIN_HEIGHT:
		case PADDING_TOP:
		case PADDING_RIGHT:
		case PADDING_BOTTOM:
		case PADDING_LEFT:
		case TEXT_INDENT:
			return NUMBER;
		}

		// Note: ENUM is fine for number types since 0 is returned for getPx()
		return ENUM;
	}

	/**
	 * Returns the url of background image.
	 * 
	 * @return the url of background image, return null string means 'none'.
	 */
	public String getBackgroundImageUrl() {

		return backgroundImage;
	}

	/**
	 * Returns the url of list style image.
	 * 
	 * @return the url of list style image, return null string means 'none'.
	 */
	public String getListStyleImageUrl() {

		return listStyleImage;
	}

	/**
	 * Returns the font-family string, separated by ','
	 * 
	 * @return string of font-family
	 */
	public String getFontFamily() {

		return fontFamily != null ? fontFamily.toString() : null;
	}

	/**
	 * Returns true if this is a (inline) block style.
	 * 
	 * @param full if false, true is returned for inline blocks, too.
	 */
	public boolean isBlock(boolean full) {

		int display = getEnum(DISPLAY);
		if (display == Value.BLOCK || display == Value.TABLE ||
			display == Value.LIST_ITEM) {
			return true;
		}
		if (full) {
			return false;
		}
		return backgroundImage != null || getEnum(DISPLAY) != Value.INLINE ||
			getPx(BORDER_BOTTOM_WIDTH) != 0 || getPx(BORDER_LEFT_WIDTH) != 0 ||
			getPx(BORDER_RIGHT_WIDTH) != 0 || getPx(BORDER_TOP_WIDTH) != 0 ||
			getEnum(POSITION) == Value.ABSOLUTE ||
			lengthIsFixed(Style.HEIGHT, true) ||
			lengthIsFixed(Style.WIDTH, true);
	}

	/**
	 * Determines whether a length should be calculated automatically.
	 * 
	 * @param id property id
	 * @param baseAvailable set to true if the corresponding container dimension
	 *            is available, false otherwise
	 * @return true if the property should be calculated instead of calling
	 *         getPx().
	 */
	public boolean lengthIsFixed(int id, boolean baseAvailable) {

		switch (getUnit(id)) {
		case PERCENT:
			return baseAvailable;
		case NUMBER:
		case CM:
		case EM:
		case EX:
		case IN:
		case MM:
		case PC:
		case PT:
		case PX:
			return true;
		}
		return false;
	}

	/**
	 * Returns the pixel value as a rounded integer, i.e. not in the internal
	 * fixpoint format. Other units are converted to pixel automatically.
	 * <p>
	 * Percent values are converted to 0. For properties permitting percent
	 * values, please use getPx(int id, int base).
	 */
	public int getPx(int id) {

		int unit = getUnit(id);
		int v = getValue(id);

		if (id >= BORDER_TOP_WIDTH && id <= BORDER_LEFT_WIDTH) {

			//when border donot have style, its width will always return zero
			if (getValue(id - BORDER_TOP_WIDTH + BORDER_TOP_STYLE) == 0)
				return 0;

			//handle enum unit border-width, change to px unit
			//thin = 1, medium = 2, thick = 3
			if (unit == ENUM) {
				switch (v) {
				case Value.THIN:
					return 1;
				case Value.THICK:
					return 3;
				default://medium or inherit
					return 2;
				}
			}

			if (v < 0)
				return 0;
		}

		if (v == 0)
			return 0;

		if (id == FONT_SIZE && unit == ENUM) {

			//handle enum unit font-size, change to px unit
			//start with 9px and multiple 1.2 scale factor each time
			//according CSS2.1 suggestion - 
			//9, 10.8, 12.96, 15.55, 18.66, 22.34, 26.87
			//after round - 9, 11, 13, 16, 19, 22, 27
			switch (v) {
			case Value.XX_SMALL:
				return 9;
			case Value.X_SMALL:
				return 11;
			case Value.SMALL:
			case Value.SMALLER:
				return 13;
			case Value.LARGE:
			case Value.LARGER:
				return 19;
			case Value.X_LARGE:
				return 22;
			case Value.XX_LARGE:
				return 27;
			default://medium or inherit
				return 16;
			}
		}

		switch (unit) {
		case PX:
			return v / 1000;
		case EM:
			return v * DEFAULT_FONT_HEIGHT / 1000;
		case EX:
			return v * DEFAULT_FONT_HEIGHT / 2000;
		case IN:
			return (v * DEVICE_DPI) / 1000;
		case CM:
			return (v * DEVICE_DPI * 100 / 254) / 1000;
		case MM:
			return (v * DEVICE_DPI * 10 / 254) / 1000;
		case PT:
			return (v * DEVICE_DPI / 72) / 1000;
		case PC:
			return (v * DEVICE_DPI / 6) / 1000;
		default:
			return 0;
		}
	}

	/**
	 * Returns the pixel value as a rounded integer, i.e. not in the internal
	 * fixpoint format. Other units are converted to pixel automatically.
	 * <p>
	 * Percent values are multiplied with the base value. Note: Automatic
	 * margins are implemented by shifting the child component to the right in
	 * order to keep the implementation of getPx() and BlockWidget.preDraw()
	 * simple.
	 */
	public int getPx(int id, int base) {

		return isSet(id) && units[id] == PERCENT ? base * values[id] / 100000
			: getPx(id);
	}

	/**
	 * Returns the value of the property if it is an ENUM, INVALID otherwise.
	 * 
	 * @param id the property id
	 * @return the value if getUnit() returns ENUM, INVALID otherwise
	 */
	public int getEnum(int id) {

		return getUnit(id) == ENUM ? getValue(id) : Value.INVALID;
	}

	/**
	 * 
	 * 
	 * @param id
	 * @param containerLength
	 * @param imageLength
	 * @return
	 */
	public int getBackgroundReferencePoint(int id, int containerLength,
		int imageLength) {

		int percent = 0;
		switch (getUnit(id)) {
		case ENUM:
			switch (getValue(id)) {
			case Value.TOP:
			case Value.LEFT:
				return 0;
			case Value.CENTER:
				percent = 50;
				break;
			case Value.RIGHT:
			case Value.BOTTOM:
				percent = 100;
				break;
			default:
				return 0;
			}
			break;
		case PERCENT:
			percent = getValue(id) / 1000;
			break;
		default:
			return getPx(id);
		}
		return (containerLength - imageLength) * percent / 100;
	}

	public boolean isBoldFont() {

		int unit = getUnit(FONT_WEIGHT);
		int val = getValue(FONT_WEIGHT);

		if (unit == ENUM) {

			return val == Value.BOLD || val == Value.BOLDER;
		}
		else {

			return val >= 700000;
		}
	}

	public boolean isItalicFont() {

		int val = getEnum(FONT_STYLE);

		return val == Value.ITALIC || val == Value.OBLIQUE;
	}

	public boolean isUnderlineText() {

		return getEnum(TEXT_DECORATION) == Value.UNDERLINE;
	}

	/**
	 * Dumps this style sheet to stdout for debugging purposes.
	 */
	public void dump(String indent) {

		if (!DEBUG)
			return;

		//#if (debug == true)

		for (int id = 0; id < PROPERTY_COUNT; id++) {

			if (isSet(id)) {

				printProperty(indent, id, values[id], units[id],
					getBackgroundImageUrl(), getListStyleImageUrl(),
					getFontFamily());
			}
		}

		System.out.println("/* specificity: " + specificity + ", position: " +
			position + " */");

		//#endif
	}

	static void printProperty(String indent, int id, int v, int unit,
		String backgroundImage, String listStyleImage, String fontFamily) {

		System.out.print(indent + ID_TO_NAME_MAP.get(new Integer(id)) + ": ");

		switch (unit) {
		case ARGB:
			System.out.print("#" +
				Long.toString((v & 0x0ffffffffL) | 0x100000000L, 16).substring(
					1));
			break;
		case URI:
		case INDEX:
			if (id == BACKGROUND_IMAGE)
				System.out.print(backgroundImage);
			else if (id == LIST_STYLE_IMAGE)
				System.out.print(listStyleImage);
			else if (id == FONT_FAMILY)
				System.out.print(fontFamily);
			break;
		case ENUM:
			System.out.print("" + ID_TO_VALUE_MAP.get(new Integer(v)));
			break;
		default:
			StringBuffer buf;
			if (v % 1000 == 0) {
				System.out.print(v / 1000);
			}
			else {
				buf = new StringBuffer(Integer.toString(v));
				while (buf.length() < 4) {
					buf.insert(0, '0');
				}
				buf.insert(buf.length() - 3, '.');
				System.out.print(buf.toString());
			}
			if (unit >= 0 && unit < UNIT_NAMES.length) {
				System.out.print(UNIT_NAMES[unit]);
			}
		}
		System.out.println("; ");
	}

	public static int indexOf(Object[] array, Object s) {

		for (int i = 0; i < array.length; i++) {
			if (s == null) {
				if (array[i] == null) {
					return i;
				}
			}
			else {
				if (s.equals(array[i])) {
					return i;
				}
			}
		}
		return -1;
	}
}
