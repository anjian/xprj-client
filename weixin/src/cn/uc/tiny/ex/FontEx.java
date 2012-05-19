/**
 * Tiny.cn.uc.tiny.ex.FontEx.java, 2010-12-17
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import java.util.Vector;

import javax.microedition.lcdui.Font;

import cn.uc.build.Config;
import cn.uc.util.BitUtils;
import cn.uc.util.NumberUtils;
import cn.uc.util.StringUtils;
import cn.uc.util.TextUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * Encapsulate MIDP's {@link Font}
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class FontEx {

	public static final String TAG = "Font";

	/** @see {@link Font#STYLE_PLAIN} */
	public static final int STYLE_PLAIN = 0;
	/** @see {@link Font#STYLE_BOLD} */
	public static final int STYLE_BOLD = 1;
	/** @see {@link Font#STYLE_ITALIC} */
	public static final int STYLE_ITALIC = 2;
	/** @see {@link Font#STYLE_UNDERLINED} */
	public static final int STYLE_UNDERLINED = 4;
	/** @see {@link Font#SIZE_SMALL} */
	public static final int SIZE_SMALL = 8;
	/** @see {@link Font#SIZE_MEDIUM} */
	public static final int SIZE_MEDIUM = 0;
	/** @see {@link Font#SIZE_LARGE} */
	public static final int SIZE_LARGE = 16;
	/** @see {@link Font#FACE_SYSTEM} */
	public static final int FACE_SYSTEM = 0;
	/** @see {@link Font#FACE_MONOSPACE} */
	public static final int FACE_MONOSPACE = 32;
	/** @see {@link Font#FACE_PROPORTIONAL} */
	public static final int FACE_PROPORTIONAL = 64;
	/** @see {@link Font#FONT_STATIC_TEXT} */
	public static final int FONT_STATIC_TEXT = 0;
	/** @see {@link Font#FONT_INPUT_TEXT} */
	public static final int FONT_INPUT_TEXT = 1;

	// Mask
	public static final int FONT_STYLE_MASK = STYLE_PLAIN | STYLE_BOLD
		| STYLE_ITALIC | STYLE_UNDERLINED;
	public static final int FONT_SIZE_MASK = SIZE_SMALL | SIZE_MEDIUM
		| SIZE_LARGE;
	public static final int FONT_FACE_MASK = FACE_SYSTEM | FACE_MONOSPACE
		| FACE_PROPORTIONAL;

	public static final int FONT_UNSUPPORT_ID = -1;
	public static final int[] FONT_ID_TABLES = {
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_PLAIN,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_BOLD,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_PLAIN,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_BOLD,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_PLAIN,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_BOLD,

		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_PLAIN | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_PLAIN | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_PLAIN | STYLE_ITALIC
			| STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_BOLD | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_BOLD | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_SMALL | STYLE_BOLD | STYLE_ITALIC
			| STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_PLAIN | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_PLAIN | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_PLAIN | STYLE_ITALIC
			| STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_BOLD | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_BOLD | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_MEDIUM | STYLE_BOLD | STYLE_ITALIC
			| STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_PLAIN | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_PLAIN | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_PLAIN | STYLE_ITALIC
			| STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_BOLD | STYLE_ITALIC,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_BOLD | STYLE_UNDERLINED,
		FACE_PROPORTIONAL | SIZE_LARGE | STYLE_BOLD | STYLE_ITALIC
			| STYLE_UNDERLINED, };

	public static final int SUPPORT_STYLE_BOLD = 1;
	public static final int SUPPORT_STYLE_ITALIC = 2;
	public static final int SUPPORT_STYLE_UNDERLINED = 4;
	public static final int SUPPORT_SIZE_SMALL = 8;
	public static final int SUPPORT_SIZE_MEDIUM = 16;
	public static final int SUPPORT_SIZE_LARGE = 32;
	public static final int SUPPORT_FACE_MONOSPACE = 64;
	public static final int SUPPORT_FACE_PROPORTIONAL = 128;

	public static final int SUPPORT_ONLY_DEFAULT_FONT = 0;
	public static final int SUPPORT_NORMAL_FONTS = SUPPORT_STYLE_BOLD
		| SUPPORT_SIZE_SMALL | SUPPORT_SIZE_MEDIUM | SUPPORT_SIZE_LARGE
		| SUPPORT_FACE_PROPORTIONAL;
	public static final int SUPPORT_ALL_FONTS = SUPPORT_STYLE_BOLD
		| SUPPORT_STYLE_ITALIC | SUPPORT_STYLE_UNDERLINED | SUPPORT_SIZE_SMALL
		| SUPPORT_SIZE_MEDIUM | SUPPORT_SIZE_LARGE | SUPPORT_FACE_MONOSPACE
		| SUPPORT_FACE_PROPORTIONAL;

	// Ò»(4E00) Ó®(8D62) à÷(571C)
	private static final char CHINESE_CHAR_1_FOR_WIDTH = 0x4E00;
	private static final char CHINESE_CHAR_2_FOR_WIDTH = 0x8D62;
	private static final char CHINESE_CHAR_3_FOR_WIDTH = 0x571C;
	private static final String CHINESE_STR = "Ó®£º×òÒ¹Î÷·çµò±ÌÊ÷£¬¶ÀÉÏÎ÷Â¥£¬Íû¾¡ÌìÑÄÂ·¡£Ó®";

	private static final int FONT_METRICS_NUM = 101;
	private static final int FM_CJK_CHAR_AVG_X_MIN_IDX = 0;
	private static final int FM_CJK_CHAR_AVG_X_MAX_IDX = 1;
	private static final int FM_AVG_Y_MIN_IDX = 2;
	private static final int FM_AVG_Y_MAX_IDX = 3;
	private static final int FM_AVG_CHAR_WIDTH_IDX = 4;
	private static final int FM_WIDTH_OFFSET = 5;

	private static final int FM_ASCII_PRINTABLE_CHAR_WIDTH_OFFSET = FM_WIDTH_OFFSET
		- TextUtils.ASCII_PRINTABLE_FIRST;

	private static final int FM_ASCII_NUMBERIC_CHAR_WIDTH_OFFSET = FM_WIDTH_OFFSET
		+ 48 - TextUtils.ASCII_PRINTABLE_FIRST;

	private static final int FM_CJK_CHAR_WIDTH_IDX = FM_WIDTH_OFFSET
		+ TextUtils.ASCII_PRINTABLE_LAST - TextUtils.ASCII_PRINTABLE_FIRST + 1;

	/** Capability of the platform's font system. */
	private static int gCapability = SUPPORT_NORMAL_FONTS;

	/** The default system font. */
	private static final Font gDefaultSysFont = Font.getDefaultFont();

	/** Store create fonts. */
	private static final Vector gFonts = new Vector();

	private static FontEx gDefaultFont;

	/**
	 * Initialize used fonts, if some types of fonts are not supported by the
	 * platform, client App need to provide different capability.
	 * 
	 * <p>
	 * Client App can turn off some types of fonts when it think they are not
	 * supported by the platform, and FontEx will auto map the unsupported font
	 * to the most close supported font.
	 * 
	 * @param aCapability the font capability of the platform
	 * @see #SUPPORT_ONLY_DEFAULT_FONT
	 * @see #SUPPORT_NORMAL_FONTS
	 * @see #SUPPORT_ALL_FONTS
	 * @see #getFont(int)
	 */
	public static void initializeUsedFonts(int aCapability) {

		gCapability = aCapability;
		for (int i = 0; i < FONT_ID_TABLES.length; ++i) {
			getFont(i);
		}
	}

	/**
	 * Set the font capability of the platform, by default FontEx will enable
	 * all fonts, but the client App can turn off some types of fonts when it
	 * think they are not supported by the platform, and FontEx will auto map
	 * the unsupported font to the most close supported font.
	 * 
	 * @param aCapability
	 */
	public static void setCapability(int aCapability) {

		gCapability = aCapability;
	}

	/**
	 * Whether the specified capability is supported?
	 * 
	 * @param aCapability specified capability
	 * @return true when the specified capability is supported
	 */
	public static boolean hasCapability(int aCapability) {

		return BitUtils.and(gCapability, aCapability);
	}

	/**
	 * Set default font.
	 * 
	 * @param aFont default font
	 */
	public static void setDefaultFont(FontEx aFont) {

		gDefaultFont = aFont;
	}

	/**
	 * Get default font.
	 * 
	 * @return default font
	 */
	public static FontEx getDefaultFont() {

		if (gDefaultFont == null) {
			gDefaultFont = getFont(gDefaultSysFont);
		}

		return gDefaultFont;
	}

	/**
	 * Get font by face, style and size.
	 * 
	 * @return font
	 * @see {@link Font#getFont(int, int, int)}
	 */
	public static FontEx getFont(int aFace, int aStyle, int aSize) {

		int face = mapToSupportedFace(aFace);
		int style = mapToSupportedStyle(aStyle);
		int size = mapToSupportedSize(aSize);

		// find in cache first
		FontEx font = null;
		for (int i = 0, count = gFonts.size(); i < count; ++i) {

			font = (FontEx) gFonts.elementAt(i);
			if (font.getFace() == face && font.getStyle() == style
				&& font.getSize() == size) {
				return font;
			}
		}

		Log.d(TAG, "Face ", Log.toString(aFace), " -> ", Log.toString(face));
		Log.d(TAG, "Style ", Log.toString(aStyle), " -> ", Log.toString(style));
		Log.d(TAG, "Size ", Log.toString(aSize), " -> ", Log.toString(size));

		// create new font
		return createFont(face, style, size);
	}

	/**
	 * Get font by the id, the meaning of id are :
	 * 
	 * <ul>
	 * <li>0 : PROPORTIONAL, PLAIN, SMALL,
	 * <li>1 : PROPORTIONAL, BOLD, SMALL
	 * <li>2 : PROPORTIONAL, PLAIN, MEDIUM
	 * <li>3 : PROPORTIONAL, BOLD, MEDIUM
	 * <li>4 : PROPORTIONAL, PLAIN, LARGE
	 * <li>5 : PROPORTIONAL, BOLD, LARGE
	 * </ul>
	 * 
	 * @param aId the id of font
	 * @return the font most close to the id
	 */
	public static FontEx getFont(int aId) {

		if (aId >= 0 && aId < FONT_ID_TABLES.length) {

			int face = FONT_ID_TABLES[aId] & FONT_FACE_MASK;
			int style = FONT_ID_TABLES[aId] & FONT_STYLE_MASK;
			int size = FONT_ID_TABLES[aId] & FONT_SIZE_MASK;
			return getFont(face, style, size);
		}

		return gDefaultFont;
	}

	/**
	 * Get font by style and size, the face will be FACE_PROPORTIONAL.
	 * 
	 * @return the font most close to client's request
	 */
	public static FontEx getFont(int aStyle, int aSize) {

		return getFont(FACE_PROPORTIONAL, aStyle, aSize);
	}

	/**
	 * Get the corresponding FontEx by system font.
	 */
	static FontEx getFont(Font aFont) {

		return getFont(aFont.getFace(), aFont.getStyle(), aFont.getSize());
	}

	private static int mapToSupportedFace(int aFace) {

		if (aFace == FACE_PROPORTIONAL
			&& hasCapability(SUPPORT_FACE_PROPORTIONAL)) {
			return FACE_PROPORTIONAL;
		}

		if (aFace == FACE_MONOSPACE && hasCapability(SUPPORT_FACE_MONOSPACE)) {
			return FACE_MONOSPACE;
		}

		return FACE_SYSTEM;
	}

	private static int mapToSupportedStyle(int aStyle) {

		int style = STYLE_PLAIN;

		if (((aStyle & STYLE_BOLD) != 0) && hasCapability(SUPPORT_STYLE_BOLD)) {
			style |= STYLE_BOLD;
		}

		if (((aStyle & STYLE_ITALIC) != 0)
			&& hasCapability(SUPPORT_STYLE_ITALIC)) {
			style |= STYLE_ITALIC;
		}

		if (((aStyle & STYLE_UNDERLINED) != 0)
			&& hasCapability(SUPPORT_STYLE_UNDERLINED)) {
			style |= STYLE_UNDERLINED;
		}

		return style;
	}

	private static int mapToSupportedSize(int aSize) {

		int size = gDefaultSysFont.getSize();

		if (aSize == SIZE_SMALL && hasCapability(SUPPORT_SIZE_SMALL)
			|| aSize == SIZE_MEDIUM && hasCapability(SUPPORT_SIZE_MEDIUM)
			|| aSize == SIZE_LARGE && hasCapability(SUPPORT_SIZE_LARGE)) {
			size = aSize;
		}

		return size;
	}

	private static FontEx createFont(int aFace, int aStyle, int aSize) {

		Font font = Font.getFont(aFace, aStyle, aSize);

		FontEx fontEx = new FontEx(font);
		gFonts.addElement(fontEx);

		if (aStyle == gDefaultSysFont.getStyle()
			&& aSize == gDefaultSysFont.getSize()) {

			gDefaultFont = fontEx;
		}

		return fontEx;
	}

	public static ImageEx getCharImage(FontEx aFont, char aDrawChar,
		int aBgColor, int aCharColor, int aLeftMargin, int aTopMargin,
		int aRightMagin, int aBottomMargin) {

		GraphicsEx.gTempChars[0] = aDrawChar;

		return getCharsOverlayImage(aFont, GraphicsEx.gTempChars, 0, 1,
			aBgColor, aCharColor, aLeftMargin, aTopMargin, aRightMagin,
			aBottomMargin);
	}

	public static ImageEx getCharsOverlayImage(FontEx aFont, char[] aDrawChars,
		int aOffset, int aLen, int aBgColor, int aCharColor, int aLeftMargin,
		int aTopMargin, int aRightMagin, int aBottomMargin) {

		Assert.assertNotNull(aDrawChars);
		Assert.assertLargerThan(aDrawChars.length, 0, Assert.ARG);

		int charImgW = aFont.font.charWidth(aDrawChars[0]) + aLeftMargin
			+ aRightMagin;
		int charImgH = aFont.getHeight() + aTopMargin + aBottomMargin;

		ImageEx charImg = ImageEx.createImage(charImgW, charImgH);
		GraphicsEx g = charImg.getGraphics();

		g.setFont(aFont);

		// fill background
		g.setColor(aBgColor);
		g.fillRect(0, 0, charImgW, charImgH);

		// draw char
		g.setColor(aCharColor);
		for (int i = aOffset; i < aDrawChars.length && i < aOffset + aLen; ++i) {

			g.drawChar(aDrawChars[aOffset + i], aLeftMargin, aTopMargin,
				GraphicsEx.LEFT_TOP);
		}

		return charImg;
	}

	public static ImageEx getStringImage(FontEx aFont, String aStr,
		int aBgColor, int aCharColor, int aLeftMargin, int aTopMargin,
		int aRightMagin, int aBottomMargin) {

		Assert.assertFalse(StringUtils.isEmpty(aStr), Assert.ARG);

		int charImgW = aFont.font.stringWidth(aStr) + aTopMargin
			+ aBottomMargin;
		int charImgH = aFont.getHeight() + aLeftMargin + aRightMagin;

		ImageEx charImg = ImageEx.createImage(charImgW, charImgH);
		GraphicsEx g = charImg.getGraphics();

		g.setFont(aFont);

		// fill background
		g.setColor(aBgColor);
		g.fillRect(0, 0, charImgW, charImgH);

		// draw char
		g.setColor(aCharColor);
		g.drawString(aStr, aLeftMargin, aTopMargin, GraphicsEx.LEFT_TOP);

		return charImg;
	}

	/** {@link Font} of MIDP. */
	final Font font;

	/** Metrics cache of font. */
	private byte[] fm;

	private FontEx(Font aFont) {

		font = aFont;
	}

	public int getId() {

		for (int i = 0; i < FONT_ID_TABLES.length; ++i) {
			if (FONT_ID_TABLES[i] == (getFace() | getStyle() | getSize())) {
				return i;
			}
		}

		return FONT_UNSUPPORT_ID;
	}

	public FontEx toBold() {

		return isBold() ? this : getFont(getFace(), getStyle() | STYLE_BOLD,
			getSize());
	}

	public FontEx toPlain() {

		return isPlain() ? this : getFont(getFace(), getStyle() & ~STYLE_BOLD,
			getSize());
	}

	public FontEx toItalic() {

		return isItalic() ? this : getFont(getFace(),
			getStyle() | STYLE_ITALIC, getSize());
	}

	public FontEx toUnderlined() {

		return isUnderlined() ? this : getFont(getFace(), getStyle()
			| STYLE_UNDERLINED, getSize());
	}

	public FontEx toSmaller() {

		switch (getSize()) {

		case SIZE_LARGE:
			return getFont(getFace(), getStyle(), SIZE_MEDIUM);

		case SIZE_MEDIUM:
			return getFont(getFace(), getStyle(), SIZE_SMALL);

		default:
			return this;
		}
	}

	public FontEx toSmallest() {

		return getSize() == SIZE_SMALL ? this : getFont(getFace(), getStyle(),
			SIZE_SMALL);
	}

	public FontEx toLarger() {

		switch (getSize()) {

		case SIZE_MEDIUM:
			return getFont(getFace(), getStyle(), SIZE_LARGE);

		case SIZE_SMALL:
			return getFont(getFace(), getStyle(), SIZE_MEDIUM);

		default:
			return this;
		}
	}

	public FontEx toLargest() {

		return getSize() == SIZE_LARGE ? this : getFont(getFace(), getStyle(),
			SIZE_LARGE);
	}

	/** @see {@link Font#getStyle()} */
	public int getStyle() {

		return font.getStyle();
	}

	/** @see {@link Font#getSize()} */
	public int getSize() {

		return font.getSize();
	}

	/** @see {@link Font#getFace()} */
	public int getFace() {

		return font.getFace();
	}

	/** @see {@link Font#isPlain()} */
	public boolean isPlain() {

		return font.isPlain();
	}

	/** @see {@link Font#isBold()} */
	public boolean isBold() {

		return font.isBold();
	}

	/** @see {@link Font#isItalic()} */
	public boolean isItalic() {

		return font.isItalic();
	}

	/** @see {@link Font#isUnderlined()} */
	public boolean isUnderlined() {

		return font.isUnderlined();
	}

	/** @see {@link Font#getHeight()} */
	public int getHeight() {

		return font.getHeight();
	}

	/**
	 * Get the average exact height of font's all characters.
	 * 
	 * <p>
	 * <b>Exact Height</b> : usually the default height of font remain margins,
	 * we can calculate the yMin and yMax value through a character image to
	 * exclude the top/bottom margins, and the <b>exact height</b> will equals
	 * to (yMax - yMin).
	 * <p>
	 * 
	 * <pre>
	 * 
	 * --------------- 
	 *   +   |       |---- yMin ------
	 * Height|   A   | Exact Height
	 *   +   |       |---- yMax ------ 
	 * ---------------
	 * 
	 * </pre>
	 * 
	 * @return the exact height of font
	 * @see #getAverageYMin()
	 * @see #getAverageYMax()
	 */
	public int getExactHeight() {

		// ensure initialized metrics first
		calculateFontMetrics();

		return fm[FM_AVG_Y_MAX_IDX] - fm[FM_AVG_Y_MIN_IDX];
	}

	/**
	 * The yMin value to exclude the top margins.
	 * 
	 * @return average yMin value
	 * @see getExactHeight
	 */
	public int getAverageYMin() {

		// ensure initialized metrics first
		calculateFontMetrics();

		return fm[FM_AVG_Y_MIN_IDX];
	}

	/**
	 * The yMax value to exclude the bottom margins.
	 * 
	 * @return average yMax value
	 * @see getExactHeight
	 */
	public int getAverageYMax() {

		// ensure initialized metrics first
		calculateFontMetrics();

		return fm[FM_AVG_Y_MAX_IDX];
	}

	/** @see {@link Font#getBaselinePosition()} */
	public int getBaselinePosition() {

		return font.getBaselinePosition();
	}

	/** @see {@link Font#charWidth(char)} */
	public int charWidth(char aCh) {

		// ensure initialized metrics first
		calculateFontMetrics();

		if (TextUtils.isAsciiPrintable(aCh))
			// Printable ASCII chars
			return fm[aCh + FM_ASCII_PRINTABLE_CHAR_WIDTH_OFFSET];
		else if (TextUtils.isCJK(aCh))
			// CJK chars
			return fm[FM_CJK_CHAR_WIDTH_IDX];
		else
			return font.charWidth(aCh);
	}

	/**
	 * Get the width of Chinese character, in theory, they all have the same
	 * width.
	 * 
	 * @return width of Chinese character
	 */
	public int chineseCharWidth() {

		return charWidth(CHINESE_CHAR_1_FOR_WIDTH);
	}

	/**
	 * Get the width of ' ' character.
	 * 
	 * @return width of ' ' character
	 */
	public int spaceWidth() {

		return charWidth(' ');
	}

	/**
	 * Get the average width of characters.
	 * 
	 * @return the average width of characters
	 */
	public int averageCharWidth() {

		// ensure initialized metrics first
		calculateFontMetrics();

		return fm[FM_AVG_CHAR_WIDTH_IDX];
	}

	/** @see {@link Font#charsWidth(char[], int, int)} */
	public int charsWidth(char[] aCh, int aOffset, int aLength) {

		int width = 0;
		for (int i = 0; i < aCh.length; ++i) {
			width += charWidth(aCh[i]);
		}

		return width;
	}

	/** @see {@link Font#stringWidth(String)} */
	public int stringWidth(String aStr) {

		return substringWidth(aStr, 0, aStr.length());
	}

	/** @see {@link Font#substringWidth(String, int, int)} */
	public int substringWidth(String str, int offset, int len) {

		int width = 0;
		for (int i = offset; i < len; ++i) {
			width += charWidth(str.charAt(i));
		}

		return width;
	}

	/**
	 * Width of a given number when it as a string.
	 * 
	 * @param aNumber given number
	 * @return given number's string width
	 */
	public int numberWidth(int aNumber) {

		// ensure initialized metrics first
		calculateFontMetrics();

		int width = aNumber < 0 ? charWidth('-') : 0;
		for (int quot = aNumber, res = 0, highest = 0; quot != 0;) {

			res = quot / 10;
			highest = quot - res * 10;
			quot = res;
			width += fm[highest + FM_ASCII_NUMBERIC_CHAR_WIDTH_OFFSET];
		}

		return width;
	}

	/**
	 * Reset the already calculated font metrics, need to recalculate again.
	 */
	public void resetFontMetrics() {

		fm = null;
	}

	private void calculateFontMetrics() {

		if (fm == null) {
			fm = new byte[FONT_METRICS_NUM];
		} else {
			// already initialized...
			return;
		}

		int widthSys1 = font.charWidth(CHINESE_CHAR_1_FOR_WIDTH);
		int widthSys2 = font.charWidth(CHINESE_CHAR_2_FOR_WIDTH);
		int widthSys3 = font.charWidth(CHINESE_CHAR_3_FOR_WIDTH);

		// calculate white margins in a char image
		GraphicsEx.gTempChars[0] = CHINESE_CHAR_1_FOR_WIDTH;
		GraphicsEx.gTempChars[1] = CHINESE_CHAR_2_FOR_WIDTH;
		GraphicsEx.gTempChars[2] = CHINESE_CHAR_3_FOR_WIDTH;
		GraphicsEx.gTempChars[3] = 'h';
		GraphicsEx.gTempChars[4] = 'y';
		GraphicsEx.gTempChars[5] = 'g';
		GraphicsEx.gTempChars[6] = '_';

		ImageEx charImg = getCharsOverlayImage(this, GraphicsEx.gTempChars, 0,
			7, Color.WHITE, Color.BLACK, 5, 5, 5, 5);
		charImg.getMargins(Color.WHITE, Color.BLACK, GraphicsEx.gTempRect);

		int mgLeft = GraphicsEx.gTempRect[0];
		int mgTop = GraphicsEx.gTempRect[1];
		int mgRight = GraphicsEx.gTempRect[2];
		int mgBottom = GraphicsEx.gTempRect[3];

		int charImgW = charImg.getWidth();
		int charImgH = charImg.getHeight();
		int widthScan = charImgW - mgLeft - mgRight;

		if (widthSys1 != widthSys2 || widthSys2 != widthSys3
			|| widthSys2 != widthScan) {

			Log.w("The width of Chinese characters are not all same.");
			Log.w("Need use different method to calculate again.");
			Log.s(TAG, charImg, new String(GraphicsEx.gTempChars, 0, 7));

			charImg = getStringImage(this, CHINESE_STR, Color.WHITE,
				Color.BLACK, 5, 5, 5, 5);
			charImg.getMargins(Color.WHITE, Color.BLACK, GraphicsEx.gTempRect);

			widthScan = charImg.getWidth() - GraphicsEx.gTempRect[0]
				- GraphicsEx.gTempRect[2];
			widthScan /= CHINESE_STR.length();

			if (widthSys1 != widthSys2 || widthSys2 != widthSys3
				|| widthSys2 != widthScan) {
				Log.w("The width of Chinese characters are not all same.");
				Log.s(TAG, charImg, CHINESE_STR.substring(2, 7));
			}
		}

		// calculate the metrics of Chinese chars
		fm[FM_CJK_CHAR_WIDTH_IDX] = (byte) NumberUtils.max(widthSys1,
			widthSys2, widthSys3, widthScan);

		fm[FM_CJK_CHAR_AVG_X_MIN_IDX] = (byte) (mgLeft - 5);
		fm[FM_CJK_CHAR_AVG_X_MAX_IDX] = (byte) (charImgW - mgRight - 5);
		fm[FM_AVG_Y_MIN_IDX] = (byte) (mgTop - 5);
		fm[FM_AVG_Y_MAX_IDX] = (byte) (charImgH - mgBottom - 5);

		// calculate the metrics of visible ASCII chars
		char ch = TextUtils.ASCII_PRINTABLE_FIRST;
		int chIdx = FM_WIDTH_OFFSET;

		int totalWidth = 0;
		int chWidth = 0;
		for (; ch <= TextUtils.ASCII_PRINTABLE_LAST; ++ch, ++chIdx) {

			chWidth = font.charWidth(ch);
			totalWidth += chWidth;
			fm[chIdx] = (byte) chWidth;
		}

		totalWidth += fm[FM_CJK_CHAR_WIDTH_IDX] * 105;
		fm[FM_AVG_CHAR_WIDTH_IDX] = (byte) (totalWidth / 200);

		Log.d(TAG, "Char ", Log.toString(CHINESE_CHAR_1_FOR_WIDTH),
			" sys width : ", Log.toString(widthSys1));
		Log.d(TAG, "Char ", Log.toString(CHINESE_CHAR_2_FOR_WIDTH),
			" sys width : ", Log.toString(widthSys2));
		Log.d(TAG, "Char ", Log.toString(CHINESE_CHAR_3_FOR_WIDTH),
			" sys width : ", Log.toString(widthSys3));
		Log.d(TAG, "Scan char ", Log.toString(CHINESE_CHAR_2_FOR_WIDTH),
			" width : ", Log.toString(widthScan));

		Log.d(TAG, "Width : ", Log.toString(fm[FM_CJK_CHAR_WIDTH_IDX]));
		Log.d(TAG, "X Min : ", Log.toString(fm[FM_CJK_CHAR_AVG_X_MIN_IDX]));
		Log.d(TAG, "X Max : ", Log.toString(fm[FM_CJK_CHAR_AVG_X_MAX_IDX]));
		Log.d(TAG, "Y Min : ", Log.toString(fm[FM_AVG_Y_MIN_IDX]));
		Log.d(TAG, "Y Max : ", Log.toString(fm[FM_AVG_Y_MAX_IDX]));

		Log.d(TAG, this);
	}

	/** {@inheritDoc} */
	public String toString() {

		if (!Config.DEBUG)
			return super.toString();

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("FontEx [face=");
		sBuffer.append(getFace());
		sBuffer.append(", style=");
		sBuffer.append(getStyle());
		sBuffer.append(", size=");
		sBuffer.append(getSize());
		sBuffer.append(", isPlain=");
		sBuffer.append(isPlain());
		sBuffer.append(", isBold=");
		sBuffer.append(isBold());
		sBuffer.append(", isItalic=");
		sBuffer.append(isItalic());
		sBuffer.append(", isUnderlined=");
		sBuffer.append(isUnderlined());
		sBuffer.append(", height=");
		sBuffer.append(getHeight());
		sBuffer.append(", exactHeight=");
		sBuffer.append(getExactHeight());
		sBuffer.append(", avgYMin=");
		sBuffer.append(getAverageYMin());
		sBuffer.append(", avgYMax=");
		sBuffer.append(getAverageYMax());
		sBuffer.append(", chineseCharWidth=");
		sBuffer.append(chineseCharWidth());
		sBuffer.append(", baselinePosition=");
		sBuffer.append(getBaselinePosition());
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
