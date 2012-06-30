/**
 * Tiny.cn.uc.ui.text.TextLayout.java, 2010-12-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import cn.uc.build.Config;
import cn.uc.util.ArrayUtils;
import cn.uc.util.BitUtils;
import cn.uc.util.NumberUtils;
import cn.uc.util.TextUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class TextLayout {

	/**
	 * {enum} State
	 * 
	 * <ul>
	 * <li> {@link #STATE_NONE} : Just created
	 * <li> {@link #STATE_INITIALIZED} : Font and Text are setup
	 * <li> {@link #STATE_LAYOUT_BEGIN} : Layout begin, can call
	 * {@link #createLine(int, int, int)}
	 * <li> {@link #STATE_LAYOUT_END} : Layout end, can call
	 * {@link #draw(GraphicsEx, int, int)}
	 * </ul>
	 */
	public static final int STATE_NONE = 0;
	public static final int STATE_INITIALIZED = 1;
	public static final int STATE_LAYOUT_BEGIN = 2;
	public static final int STATE_LAYOUT_END = 3;

	/**
	 * {Bitset} {@link TextOption}s of {@link TextLayout}, contains :
	 * 
	 * <p>
	 * <b>Wrap Mode</b>
	 * <p>
	 * 
	 * <ul>
	 * <li> {@link #NO_WRAP}
	 * <li> {@link #WORD_WRAP}
	 * <li> {@link #WRAP_ANYWHERE}
	 * <li> {@link #WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE}
	 * </ul>
	 * 
	 * <p>
	 * <b>Metrics</b>
	 * <p>
	 * 
	 * <ul>
	 * <li> {@link #USE_FONT_DEFAULT_HEIGHT}
	 * <li> {@link #USE_FONT_EXACT_HEIGHT}
	 * 
	 * <li> {@link #TAB_STOP_AT_TWO_SPACES}
	 * <li> {@link #TAB_STOP_AT_FOUR_SPACES}
	 * <li> {@link #TAB_STOP_AT_EIGHT_SPACES}
	 * 
	 * <li> {@link #LINE_HEIGHT_1_EM}
	 * <li> {@link #LINE_HEIGHT_1_2_EM}
	 * <li> {@link #LINE_HEIGHT_1_5_EM}
	 * <li> {@link #LINE_HEIGHT_1_8_EM}
	 * <li> {@link #LINE_HEIGHT_2_EM}
	 * </ul>
	 * 
	 * <p>
	 * <b>Alignment</b>
	 * <p>
	 * 
	 * <ul>
	 * <li> {@link #ALIGN_LEFT}
	 * <li> {@link #ALIGN_RIGHT}
	 * <li> {@link #ALIGN_HCENTER}
	 * <li> {@link #ALIGN_JUSTIFY}
	 * <li> {@link #ALIGN_TOP}
	 * <li> {@link #ALIGN_BOTTOM}
	 * <li> {@link #ALIGN_VCENTER}
	 * <li> {@link #ALIGN_CENTER}
	 * </ul>
	 * 
	 * <p>
	 * <b>Display</b>
	 * </p>
	 * 
	 * <ul>
	 * <li> {@link #END_ELLIPSIS}
	 * <li> {@link #BEGIN_ELLIPSIS}
	 * <li> {@link #NO_CLIP}
	 * <li> {@link #LINE_CLIP}
	 * <li> {@link #BOX_CLIP}
	 * <li> {@link #DRAW_LAYOUT_BOUNDS}
	 * <li> {@link #DRAW_LINE_BOUNDS}
	 * </ul>
	 * 
	 */
	public static final class TextOption {

		/**
		 * Text is not wrapped at all. Displays text on a single line only.
		 * Carriage returns and line feeds do not break the line.
		 */
		public static final int NO_WRAP = 0x0000;
		/** Text is wrapped at word boundaries. */
		public static final int WORD_WRAP = 0x0001;
		/**
		 * Text can be wrapped at any point on a line, even if it occurs in the
		 * middle of a word.
		 */
		public static final int WRAP_ANYWHERE = 0x0002;
		/**
		 * If possible, wrapping occurs at a word boundary; otherwise it will
		 * occur at the appropriate point on the line, even in the middle of a
		 * word. (Word will be broke when its length larger than half length of
		 * line.)
		 */
		public static final int WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE = 0x0004;

		/**
		 * Use the default height of font as the font height.
		 * 
		 * @see {@link FontEx#getHeight()}
		 */
		public static final int USE_FONT_DEFAULT_HEIGHT = 0x0008;
		/**
		 * Use the exact height of font as the font height.
		 * 
		 * @see {@link FontEx#getExactHeight()}
		 */
		public static final int USE_FONT_EXACT_HEIGHT = 0x0010;

		/** No tab stop, just treat tab as a space. */
		public static final int NO_TAB_STOP = 0x0020;
		/** A tab stop will be two space width. */
		public static final int TAB_STOP_AT_TWO_SPACES = 0x0040;
		/** A tab stop will be four spaces width. */
		public static final int TAB_STOP_AT_FOUR_SPACES = 0x0080;
		/** A tab stop will be eight spaces width. */
		public static final int TAB_STOP_AT_EIGHT_SPACES = 0x0100;

		/** Line height constants. */
		public static final int LINE_HEIGHT_1_EM = 0x0000;
		public static final int LINE_HEIGHT_1_2_EM = 0x0200;
		public static final int LINE_HEIGHT_1_5_EM = 0x0400;
		public static final int LINE_HEIGHT_1_8_EM = 0x0800;
		public static final int LINE_HEIGHT_2_EM = 0x1000;

		/** Aligns with the left edge. */
		public static final int ALIGN_LEFT = 0x10000;
		/** Aligns with the right edge. */
		public static final int ALIGN_RIGHT = 0x20000;
		/** Centers horizontally in the available space. */
		public static final int ALIGN_HCENTER = 0x40000;
		/** Justifies the text in the available space. */
		public static final int ALIGN_JUSTIFY = 0x80000;
		/** Aligns with the top. */
		public static final int ALIGN_TOP = 0x100000;
		/** Aligns with the bottom. */
		public static final int ALIGN_BOTTOM = 0x200000;
		/** Centers vertically in the available space. */
		public static final int ALIGN_VCENTER = 0x400000;
		/** Centers in both dimensions. */
		public static final int ALIGN_CENTER = ALIGN_HCENTER | ALIGN_VCENTER;

		/**
		 * For displayed text, if the end of a string does not fit in the box
		 * rectangle,it is truncated and ellipses are added.
		 */
		public static final int END_ELLIPSIS = 0x1000000;
		/**
		 * For displayed text, if the begin of a string does not fit in the box
		 * rectangle,it is truncated and ellipses are added.
		 */
		public static final int BEGIN_ELLIPSIS = 0x2000000;

		/**
		 * When the box size for draw is smaller than TextLayout's bounds, do
		 * not clip.
		 */
		public static final int NO_CLIP = 0x0000000;
		/**
		 * When the box size for draw is smaller than TextLayout's bounds, clip
		 * at the boundary of lines inside the box.
		 */
		public static final int LINE_CLIP = 0x4000000;
		/**
		 * When the box size for draw is smaller than TextLayout's bounds, just
		 * clip at the bounds of box.
		 */
		public static final int BOX_CLIP = 0x8000000;

		public static final int DRAW_LAYOUT_BOUNDS = 0x10000000;
		public static final int DRAW_LINE_BOUNDS = 0x20000000;
	}

	/** Predefined text options. */
	public static final int PREDEFINED_TEXT_OPTIONS_ALIGN_CENTER = TextOption.WORD_WRAP
		| TextOption.USE_FONT_EXACT_HEIGHT
		| TextOption.TAB_STOP_AT_FOUR_SPACES
		| TextOption.LINE_HEIGHT_1_2_EM
		| TextOption.ALIGN_CENTER
		| TextOption.END_ELLIPSIS
		| TextOption.BEGIN_ELLIPSIS
		| TextOption.LINE_CLIP /*
							    * | TextOption.DRAW_LAYOUT_BOUNDS |
							    * TextOption.DRAW_LINE_BOUNDS
							    */;

	public static final int PREDEFINED_TEXT_OPTIONS_ALIGN_LEFT_TOP = TextOption.WORD_WRAP
		| TextOption.USE_FONT_EXACT_HEIGHT
		| TextOption.TAB_STOP_AT_FOUR_SPACES
		| TextOption.LINE_HEIGHT_1_2_EM
		| TextOption.ALIGN_LEFT
		| TextOption.ALIGN_TOP
		| TextOption.END_ELLIPSIS
		| TextOption.BEGIN_ELLIPSIS | TextOption.LINE_CLIP /*
														    * | TextOption.
														    * DRAW_LAYOUT_BOUNDS
														    * | TextOption.
														    * DRAW_LINE_BOUNDS
														    */;

	/** "..." */
	public static final String ELLIPSIS = "...";

	/** Invalid line index, = -1 */
	public static final int INVALID_LINE_INDEX = -1;

	public static int gLayoutBoundsColor = Color.DARKGRAY;
	public static int gTextBoundsColor = Color.LIGHTBLUE;
	public static int gLineBoundsColor = Color.LIGHTGRAY;

	/**
	 * The offset of each metric elements of a line
	 */
	private static final int LINE_X = 0;
	private static final int LINE_Y = 1;
	private static final int LINE_WIDTH = 2;
	private static final int LINE_STR_OFFSET = 3;
	private static final int LINE_STR_LEN = 4;
	private static final int LINE_SPACE_NUM = 5;
	private static final int LINE_ELEMENTS_NUM = 6;

	// private constants
	private static final int LINE_DATA_INCREASE_LEN = LINE_ELEMENTS_NUM * 4;

	private FontEx font;
	private String text;

	private int options;
	private int state;

	private int count;
	private int nextStartIdx;

	private int lineWidth;
	private int lineHeight;

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	private short[] data;

	public TextLayout() {

		setState(STATE_NONE);
	}

	public TextLayout(FontEx aFont, String aText) {

		setFont(aFont);
		setText(aText);
	}

	public void setFont(FontEx font) {

		Assert.assertNotNull(font);

		this.font = font;
		checkState();
	}

	public FontEx getFont() {

		return font;
	}

	public void setText(String aText) {

		Assert.assertNotNull(aText);

		text = aText;

		checkState();
	}

	public String getText() {

		return text;
	}

	public void setTextOptions(int aTextOptions) {

		Assert.assertNotEquals(state, STATE_LAYOUT_BEGIN, Assert.STATE);

		options = aTextOptions;
	}

	public int getTextOptions() {

		return options;
	}

	public boolean hasOption(int aTextOption) {

		return BitUtils.and(options, aTextOption);
	}

	private void setState(int state) {

		this.state = state;
	}

	public int getState() {

		return state;
	}

	public int getLineCount() {

		return count;
	}

	public boolean isLineIndexValid(int aLineIdx) {

		return NumberUtils.inRange(0, aLineIdx, count);
	}

	public int getLineX(int aLineIdx) {

		Assert.assertTrue(isLineIndexValid(aLineIdx), Assert.ARG);

		return data[aLineIdx * LINE_ELEMENTS_NUM + LINE_X];
	}

	public int getLineY(int aLineIdx) {

		Assert.assertTrue(isLineIndexValid(aLineIdx), Assert.ARG);

		return data[aLineIdx * LINE_ELEMENTS_NUM + LINE_Y];
	}

	public int getLineWidth(int aLineIdx) {

		Assert.assertTrue(isLineIndexValid(aLineIdx), Assert.ARG);

		return data[aLineIdx * LINE_ELEMENTS_NUM + LINE_WIDTH];
	}

	public String getLineText(int aLineIdx) {

		Assert.assertTrue(isLineIndexValid(aLineIdx), Assert.ARG);

		int startIdx = data[aLineIdx * LINE_ELEMENTS_NUM + LINE_STR_OFFSET];

		int endIdx = startIdx
			+ data[aLineIdx * LINE_ELEMENTS_NUM + LINE_STR_LEN];

		return text.substring(startIdx, endIdx);
	}

	public int getLineIndex(int aY) {

		for (int i = 0, lineY = 0; i < count; ++i) {

			lineY = getLineY(i);

			if (aY == lineY)
				return i;
			else if (aY < lineY)
				return i - 1;
		}

		return INVALID_LINE_INDEX;
	}

	public int getUnderLineIndex(int aY) {

		for (int i = 0, lineY = 0; i < count; ++i) {

			lineY = getLineY(i);

			if (aY <= lineY)
				return i;
		}

		return INVALID_LINE_INDEX;
	}

	public int getAboveLineIndex(int aY) {

		for (int i = 0, lineY = 0; i < count; ++i) {

			lineY = getLineY(i);

			if (aY == lineY)
				return i - 1;
			else if (aY < lineY)
				return i - 2;
		}

		return INVALID_LINE_INDEX;
	}

	public int getBoundsWidth() {

		return lineWidth;
	}

	public int getBoundsHeight() {

		return lineHeight * count;
	}

	public int getExactBoundsX() {

		return minX;
	}

	public int getExactBoundsY() {

		return minY;
	}

	public int getExactBoundsWidth() {

		return maxX - minX;
	}

	public int getExactBoundsHeight() {

		return maxY - minY;
	}

	public void beginLayout() {

		Assert.assertNotEquals(state, STATE_NONE, Assert.STATE);

		// reset data
		setState(STATE_LAYOUT_BEGIN);

		count = 0;
		nextStartIdx = 0;

		lineWidth = lineHeight = 0;

		minX = minY = Integer.MAX_VALUE;
		maxX = maxY = 0;
	}

	public boolean createLine(int aX, int aY, int aLineMaxWidth) {

		Assert.assertLargerThan(aLineMaxWidth, 0, Assert.ARG);
		Assert.assertEquals(state, STATE_LAYOUT_BEGIN, Assert.STATE);

		if (createLineImpl(aX, aY, aLineMaxWidth))
			return true;

		// reach end of text...
		nextStartIdx = text.length();
		return false;
	}

	private boolean createLineImpl(int aX, int aY, int aLineMaxWidth) {

		int lnStartIdx = nextStartIdx;
		int lnIdx = lnStartIdx;
		int lnLen = 0;
		int lnAccW = 0;// current accumulate width
		int lnLastSpaceW = 0;// width of last space position

		boolean wrap = isWarp();

		int spaceIdx = 0;
		int spaceNum = 0;
		int spaceWidth = font.spaceWidth();
		int tabStop = getTapStop();

		char ch = 0, chNext = 0;
		int chWidth = 0;

		int fnHeight = getFontHeight();
		int anchor = getAnchor();

		for (int txtLen = text.length(); lnIdx < txtLen; ++lnIdx) {

			ch = text.charAt(lnIdx);
			
			// line break?
			if (wrap && TextUtils.isLineBreakChar(ch)) {

				lnLen = lnIdx - lnStartIdx;
				addLine(aX, aY, lnLen, lnAccW, spaceNum, fnHeight, anchor);

				// skip the current char
				nextStartIdx = lnIdx + 1;

				// reach end or break line?
				if (lnIdx < txtLen - 1 && ch != TextUtils.ASCII_FORM_FEED) {

					chNext = text.charAt(lnIdx + 1);
					if (TextUtils.canCombinedAsOneLineBreak(ch, chNext)) {
						// need to skip one more char
						++nextStartIdx;
					}
				}
				return nextStartIdx != txtLen;
			}

			// whitespace?
			if (TextUtils.isWhitespace(ch)) {

				spaceIdx = lnIdx;
				lnLastSpaceW = lnAccW;
				
				// tab or space?
				if (ch == TextUtils.ASCII_HORIZONTAL_TAB) {
					chWidth = tabStop - lnAccW % tabStop;
					spaceNum += chWidth / spaceWidth;
				} else {
					chWidth = spaceWidth;
					++spaceNum;
				}
			} else {
				chWidth = font.charWidth(ch);
			}

			// the accumulated line width larger than the max line width?
			if (wrap && (lnAccW + chWidth > aLineMaxWidth)) {

				// has space and current char maybe inside an English word?
				boolean wrapLastWord = false;
				if (spaceIdx > 0 && TextUtils.isEnglishWordChar(ch)) {

					if (BitUtils.and(options, TextOption.WORD_WRAP)) {
						wrapLastWord = true;
					} else if (BitUtils.and(options,
						TextOption.WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE)) {
						// current word's length less than half of line length
						if (lnIdx - spaceIdx < (lnIdx - lnStartIdx) / 2) {
							wrapLastWord = true;
						}
					}
				}

				lnLen = wrapLastWord ? spaceIdx - lnStartIdx + 1 : lnIdx
					- lnStartIdx;

				// at least larger than 0 to avoid never end loop
				if (lnLen == 0) {
					lnLen = 1;
					lnAccW = chWidth;
				}

				addLine(aX, aY, lnLen, wrapLastWord ? lnLastSpaceW : lnAccW,
					spaceNum, fnHeight, anchor);
				nextStartIdx += lnLen;
				return true;
			}

			// accumulate line width
			lnAccW += chWidth;

			// reach end of text?
			if (lnIdx == txtLen - 1) {
				lnLen = lnIdx - lnStartIdx + 1;
				addLine(aX, aY, lnLen, lnAccW, spaceNum, fnHeight, anchor);
				return false;
			}
		}// for

		return false;
	}

	private void addLine(int aX, int aY, int aLen, int aLineRealWidth,
		int aSpaceNum, int aFontHeight, int aAnchor) {

		int offset = LINE_ELEMENTS_NUM * count;

		// need expand data array?
		if (data == null || offset + LINE_ELEMENTS_NUM > data.length) {
			data = ArrayUtils.copyArrayGrow(data, LINE_DATA_INCREASE_LEN);
		}

		int xOff = GraphicsEx.getBoxX(aLineRealWidth, 0, lineWidth, 0, aAnchor);
		int yOff = GraphicsEx.getBoxY(aFontHeight, 0, lineHeight, 0, aAnchor);
		aX += xOff;
		aY += yOff;

		// record line metrics
		data[offset + LINE_X] = (short) aX;
		data[offset + LINE_Y] = (short) aY;
		data[offset + LINE_WIDTH] = (short) aLineRealWidth;
		data[offset + LINE_STR_OFFSET] = (short) nextStartIdx;
		data[offset + LINE_STR_LEN] = (short) aLen;
		data[offset + LINE_SPACE_NUM] = (short) aSpaceNum;

		// calculate bounding rectangle's bottom-right corner's position
		minX = Math.min(minX, aX);
		minY = Math.min(minY, aY);
		maxX = Math.max(maxX, aX + aLineRealWidth);
		maxY = Math.max(maxY, aY + aFontHeight);

		// increase line count
		++count;
	}

	public void endLayout() {

		Assert.assertEquals(state, STATE_LAYOUT_BEGIN, Assert.STATE);

		setState(STATE_LAYOUT_END);
	}

	public void layout(int aMaxWidth, int aMaxHeight) {

		this.layout(aMaxWidth, aMaxHeight, getLineHeight());
	}

	public void layout(int aMaxWidth, int aMaxHeight, int aLineHeight) {

		Assert.assertLargerThan(aMaxWidth, 0, Assert.ARG);

		beginLayout();
		lineWidth = aMaxWidth;// keep it
		lineHeight = aLineHeight;
		int x = 0, y = 0;

		if (aMaxHeight <= 0) {
			// no predefined max layout height, the final height will depend on
			// the text itself
			aMaxHeight = Integer.MAX_VALUE;
		}

		for (; createLine(x, y, lineWidth); y += lineHeight) {
			if (y + lineHeight > aMaxHeight) {
				break;
			}
		}
		endLayout();
	}

	public void draw(GraphicsEx aG, int aBoxWidth, int aBoxHeight) {

		aG.save(GraphicsEx.SAVE_FONT | GraphicsEx.SAVE_CLIP);
		int anchor = getAnchor();
		int xOff = getDrawOffsetX(aBoxWidth, anchor);
		int yOff = getDrawOffsetY(aBoxHeight, anchor);
		int lnStart = 0;
		int lnEnd = count;

		// clip at line or box or no?
		if (aBoxHeight < getExactBoundsHeight()) {

			if (BitUtils.and(options, TextOption.LINE_CLIP)) {

				lnStart = getUnderLineIndex(Math.abs(yOff));
				lnEnd = getAboveLineIndex(Math.abs(yOff) + aBoxHeight) + 1;
			} else if (BitUtils.and(options, TextOption.BOX_CLIP)) {

				lnStart = getLineIndex(Math.abs(yOff));
				lnEnd = getLineIndex(Math.abs(yOff) + aBoxHeight) + 1;
				aG.clipRect(0, 0, aBoxWidth, aBoxHeight);
			}
		}

		// translate
		yOff -= getDrawFontOffsetY();
		aG.translate(xOff, yOff);
		aG.setFont(font);
		for (int ln = lnStart, off = lnStart * LINE_ELEMENTS_NUM; ln < lnEnd; ++ln) {

			drawLine(aG, ln, lnStart, lnEnd, off);
			off += LINE_ELEMENTS_NUM;// next line data offset
		}

		// draw layout bounding rectangle
		if (BitUtils.and(options, TextOption.DRAW_LAYOUT_BOUNDS)) {
			drawLayoutBounds(aG);
		}

		aG.restore();
	}

	private void drawLine(GraphicsEx aG, int aLineIdx, int aStartLine,
		int aEndLine, int aLineOffset) {

		int strOff = data[aLineOffset + LINE_STR_OFFSET];
		int strLen = data[aLineOffset + LINE_STR_LEN];
		int lnX = data[aLineOffset + LINE_X];
		int lnY = data[aLineOffset + LINE_Y];
		int lnW = data[aLineOffset + LINE_WIDTH];

		// draw sub-string
		if (aLineIdx == aStartLine && aStartLine != 0
			&& BitUtils.and(options, TextOption.BEGIN_ELLIPSIS)) {

			StringBuffer lineText = new StringBuffer(ELLIPSIS).append(text.substring(
				strOff, strOff + strLen));

			int realW = font.stringWidth(lineText.toString());
			for (int i = 0; i < ELLIPSIS.length(); ++i) {

				if (realW > lnW) {

					int chIdx = ELLIPSIS.length();
					char ch = lineText.charAt(chIdx);

					lineText.deleteCharAt(chIdx);
					realW -= font.charWidth(ch);
				} else {
					break;
				}
			}

			aG.drawString(lineText.toString(), lnX, lnY, GraphicsEx.LEFT_TOP);
		} else if (aLineIdx == aEndLine - 1 && aEndLine != count
			&& BitUtils.and(options, TextOption.END_ELLIPSIS)) {

			StringBuffer lineText = new StringBuffer(text.substring(strOff,
				strOff + strLen)).append(ELLIPSIS);
			int lineW = font.stringWidth(lineText.toString());
			int maxLineW = data[aLineOffset + LINE_WIDTH];

			for (int i = 0; i < ELLIPSIS.length(); ++i) {

				if (lineW > maxLineW) {
					int chIdx = lineText.length() - ELLIPSIS.length() - 1;
					char ch = lineText.charAt(chIdx);
					lineText.deleteCharAt(chIdx);
					lineW -= font.charWidth(ch);
				} else {
					break;
				}
			}

			aG.drawString(lineText.toString(), lnX, lnY, GraphicsEx.LEFT_TOP);
		} else {
			aG.drawSubstring(text, strOff, strLen, lnX, lnY,
				GraphicsEx.LEFT_TOP);
		}

		// draw line bounding rectangle
		if (BitUtils.and(options, TextOption.DRAW_LINE_BOUNDS)) {
			drawLineBounds(aG, aLineOffset, lnX, lnY);
		}
	}

	private void drawLayoutBounds(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_COLOR);
		aG.setColor(gTextBoundsColor);
		aG.drawRect(minX, minY + getDrawFontOffsetY(),
			getExactBoundsWidth() - 1, getExactBoundsHeight() - 1);
		aG.setColor(gLayoutBoundsColor);
		aG.drawRect(0, getDrawFontOffsetY(), getBoundsWidth() - 1,
			getBoundsHeight() - 1);
		aG.restore();
	}

	private void drawLineBounds(GraphicsEx aG, int aOffset, int aX, int aY) {

		aG.save(GraphicsEx.SAVE_COLOR);
		aG.setColor(gLineBoundsColor);
		aG.drawRect(aX, aY + getDrawFontOffsetY(),
			data[aOffset + LINE_WIDTH] - 1, getFontHeight() - 1);
		aG.restore();
	}

	private boolean isWarp() {

		return hasOption(TextOption.WORD_WRAP)
			|| hasOption(TextOption.WRAP_ANYWHERE)
			|| hasOption(TextOption.WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE);
	}

	private int getTapStop() {

		if (hasOption(TextOption.TAB_STOP_AT_TWO_SPACES))
			return font.spaceWidth() * 2;
		if (hasOption(TextOption.TAB_STOP_AT_FOUR_SPACES))
			return font.spaceWidth() * 4;
		else if (hasOption(TextOption.TAB_STOP_AT_EIGHT_SPACES))
			return font.spaceWidth() * 8;

		return font.spaceWidth();
	}

	private int getLineHeight() {

		if (lineHeight == 0) {

			lineHeight = getFontHeight();

			if (hasOption(TextOption.LINE_HEIGHT_1_2_EM))
				lineHeight = lineHeight * 120 / 100;
			else if (hasOption(TextOption.LINE_HEIGHT_1_5_EM))
				lineHeight = lineHeight * 150 / 100;
			else if (hasOption(TextOption.LINE_HEIGHT_1_8_EM))
				lineHeight = lineHeight * 180 / 100;
			else if (hasOption(TextOption.LINE_HEIGHT_2_EM))
				lineHeight = lineHeight * 200 / 100;
		}

		return lineHeight;
	}

	private int getFontHeight() {

		return hasOption(TextOption.USE_FONT_EXACT_HEIGHT) ? font.getExactHeight()
			: font.getHeight();
	}

	private int getDrawFontOffsetY() {

		return hasOption(TextOption.USE_FONT_EXACT_HEIGHT) ? font.getAverageYMin()
			: 0;
	}

	private int getAnchor() {

		int anchor = GraphicsEx.IN_BOX;

		if (BitUtils.and(options, TextOption.ALIGN_LEFT)) {
			anchor |= GraphicsEx.LEFT;
		} else if (BitUtils.and(options, TextOption.ALIGN_RIGHT)) {
			anchor |= GraphicsEx.RIGHT;
		} else {
			anchor |= GraphicsEx.HCENTER;
		}

		if (BitUtils.and(options, TextOption.ALIGN_TOP)) {
			anchor |= GraphicsEx.TOP;
		} else if (BitUtils.and(options, TextOption.ALIGN_BOTTOM)) {
			anchor |= GraphicsEx.BOTTOM;
		} else {
			anchor |= GraphicsEx.VCENTER;
		}

		return anchor;
	}

	private int getDrawOffsetX(int aBoxWidth, int aAnchor) {

		return GraphicsEx.getBoxX(getExactBoundsWidth(), 0, aBoxWidth, 0,
			aAnchor) - getExactBoundsX();
	}

	private int getDrawOffsetY(int aBoxHeight, int aAnchor) {

		return GraphicsEx.getBoxY(getExactBoundsHeight(), 0, aBoxHeight, 0,
			aAnchor) - getExactBoundsY();
	}

	private void checkState() {

		if (font == null || text == null) {
			setState(STATE_NONE);
		} else {
			setState(STATE_INITIALIZED);
		}
	}

	/** {@inheritDoc} */
	public String toString() {

		if (!Config.DEBUG)
			return super.toString();

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("TextLayout [options=");
		sBuffer.append(options);
		sBuffer.append(", state=");
		sBuffer.append(state);
		sBuffer.append(", count=");
		sBuffer.append(count);
		sBuffer.append(", minX=");
		sBuffer.append(minX);
		sBuffer.append(", minY=");
		sBuffer.append(minY);
		sBuffer.append(", maxX=");
		sBuffer.append(maxX);
		sBuffer.append(", maxY=");
		sBuffer.append(maxY);
		sBuffer.append(", text=");
		sBuffer.append(text);
		sBuffer.append(", font=");
		sBuffer.append(Log.toString(font));
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
