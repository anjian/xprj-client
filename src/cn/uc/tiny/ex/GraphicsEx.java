/**
 * Tiny.cn.uc.ui.ex.GraphicsEx.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import java.util.Stack;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.uc.build.Config;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.ArrayUtils;
import cn.uc.util.BitUtils;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * GraphicsEx is a wrapper of MIDP's {@link Graphics}, and provides additional
 * rendering abilities.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 * @see {@link Brush}
 */
public final class GraphicsEx {

	public static final String TAG = "DRAW";

	/** @see {@link Graphics#HCENTER} */
	public static final int HCENTER = 1;
	/** @see {@link Graphics#VCENTER} */
	public static final int VCENTER = 2;
	/** @see {@link Graphics#LEFT} */
	public static final int LEFT = 4;
	/** @see {@link Graphics#RIGHT} */
	public static final int RIGHT = 8;
	/** @see {@link Graphics#TOP} */
	public static final int TOP = 16;
	/** @see {@link Graphics#BOTTOM} */
	public static final int BOTTOM = 32;
	/** @see {@link Graphics#BASELINE} */
	public static final int BASELINE = 64;
	/** Inside the box bounds. */
	public static final int IN_BOX = 128;
	/** Outside the box bounds. */
	public static final int OUT_BOX = 256;
	/** @see {@link Graphics#SOLID} */
	public static final int SOLID = 0;
	/** @see {@link Graphics#DOTTED} */
	public static final int DOTTED = 1;

	public static final int LEFT_TOP = LEFT | TOP;
	public static final int RIGHT_TOP = RIGHT | TOP;
	public static final int LEFT_BOTTOM = LEFT | BOTTOM;
	public static final int RIGHT_BOTTOM = RIGHT | BOTTOM;
	public static final int CENTER = HCENTER | VCENTER;

	public static final int LEFT_TOP_IN = LEFT | TOP | IN_BOX;
	public static final int RIGHT_TOP_IN = RIGHT | TOP | IN_BOX;
	public static final int LEFT_BOTTOM_IN = LEFT | BOTTOM | IN_BOX;
	public static final int RIGHT_BOTTOM_IN = RIGHT | BOTTOM | IN_BOX;
	public static final int CENTER_IN = HCENTER | VCENTER | IN_BOX;
	
	public static final int LEFT_TOP_OUT = LEFT | TOP | OUT_BOX;
	public static final int RIGHT_TOP_OUT = RIGHT | TOP | OUT_BOX;
	public static final int LEFT_BOTTOM_OUT = LEFT | BOTTOM | OUT_BOX;
	public static final int RIGHT_BOTTOM_OUT = RIGHT | BOTTOM | OUT_BOX;
	public static final int CENTER_OUT = HCENTER | VCENTER | OUT_BOX;
	
	public static final class DrawHints {

		public static final int HIGHT_QUALITY = 0;
	}

	private static final int SAVE_CLIP_FLAG = 0x00000001;

	public static final int SAVE_TRANSLATION = 0x00000002;
	public static final int SAVE_CLIP = SAVE_TRANSLATION | SAVE_CLIP_FLAG;
	public static final int SAVE_COLOR = 0x00000004;
	public static final int SAVE_STROKE = 0x00000008;
	public static final int SAVE_DRAW_HINTS = 0x0000010;
	public static final int SAVE_BRUSH = 0x0000020;
	public static final int SAVE_FONT = 0x00000040;

	public static final int SAVE_COLOR_AND_STROKE = SAVE_COLOR | SAVE_STROKE;
	public static final int SAVE_ALL = SAVE_CLIP | SAVE_COLOR_AND_STROKE
		| SAVE_DRAW_HINTS | SAVE_BRUSH | SAVE_FONT;

	private static final int MAX_ARGB_STRIP_LINE_NUM = 10;
	private static final int STACK_INCREASE_MAX_STEP = 10;
	private static final int STACK_INCREASE_FACTOR = STACK_INCREASE_MAX_STEP * 4;

	// temp buffer
	static final int[] gTempRect = new int[4];
	static final char[] gTempChars = new char[32];

	/** Use the count the total drawn graphics primitives. */
	public static int gDrawnPrimitives = 0;

	public static int getBoxX(int aObjW, int aBoxX, int aBoxW, int aOffsetX,
		int aAnchor) {

		boolean inside = BitUtils.and(aAnchor, IN_BOX);
		boolean outside = BitUtils.and(aAnchor, OUT_BOX);

		Assert.assertTrue(inside != outside, Assert.ARG);

		if (inside) {

			if (BitUtils.and(aAnchor, LEFT))
				return aOffsetX + aBoxX;
			else if (BitUtils.and(aAnchor, RIGHT))
				return aBoxW - aObjW - aOffsetX + aBoxX;
			else if (BitUtils.and(aAnchor, HCENTER))
				return (aBoxW - aObjW) / 2 - aOffsetX + aBoxX;
		}

		if (outside) {

			if (BitUtils.and(aAnchor, LEFT))
				return -(aObjW + aOffsetX) + aBoxX;
			else if (BitUtils.and(aAnchor, RIGHT))
				return aBoxW + aOffsetX + aBoxX;
			else if (BitUtils.and(aAnchor, HCENTER))
				return (aBoxW - aObjW) / 2 - aOffsetX + aBoxX;
		}

		return aOffsetX + aBoxX;
	}

	public static int getBoxY(int aObjH, int aBoxY, int aBoxH, int aOffsetY,
		int aAnchor) {

		boolean inside = BitUtils.and(aAnchor, IN_BOX);
		boolean outside = BitUtils.and(aAnchor, OUT_BOX);

		Assert.assertTrue(inside != outside, Assert.ARG);

		if (inside) {

			if (BitUtils.and(aAnchor, TOP))
				return aOffsetY + aBoxY;
			else if (BitUtils.and(aAnchor, BOTTOM))
				return aBoxH - aObjH - aOffsetY + aBoxY;
			else if (BitUtils.and(aAnchor, VCENTER))
				return (aBoxH - aObjH) / 2 - aOffsetY + aBoxY;
		}

		if (outside) {

			if (BitUtils.and(aAnchor, TOP))
				return -(aObjH + aOffsetY) + aBoxY;
			else if (BitUtils.and(aAnchor, BOTTOM))
				return aBoxH + aOffsetY + aBoxY;
			else if (BitUtils.and(aAnchor, VCENTER))
				return (aBoxH - aObjH) / 2 - aOffsetY + aBoxY;
		}

		return aOffsetY + aBoxY;
	}

	final Graphics g;

	// states save/restore stack
	private int[] stack;
	private int stackTop;
	private Stack objStack;

	private int xTrans;
	private int yTrans;

	private FontEx font;
	private Brush brush = Brush.NULL_BRUSH;

	private int drawHints;

	/**
	 * Create a GraphicsEx from a MIDP Graphics.
	 * 
	 * @param aG
	 */
	public GraphicsEx(Graphics aG) {

		g = aG;
		reset();
	}

	public void reset() {

		setDrawHint(DrawHints.HIGHT_QUALITY, true);

		xTrans = yTrans = 0;
		brush = Brush.NULL_BRUSH;

		if (font != null) {

			g.setFont(font.font);
		}

		if (stack != null) {
			stackTop = 0;
		}

		if (objStack != null) {
			objStack.removeAllElements();
		}
	}

	public void setDrawHint(int aHint, boolean aOn) {

		drawHints = BitUtils.set(drawHints, aHint, aOn);
	}

	public boolean getDrawHint(int aHint) {

		return BitUtils.get(drawHints, aHint);
	}

	public void save(int aFlags) {

		// allocate and grow when needed
		if (stack == null) {

			stack = new int[STACK_INCREASE_FACTOR];
			stackTop = 0;
			objStack = new Stack();
		}

		if (stackTop + STACK_INCREASE_MAX_STEP >= stack.length) {
			stack = ArrayUtils.copyArrayGrow(stack, STACK_INCREASE_FACTOR);
		}

		// (clipX, clipY, clipW, clipH, xTrans, yTrans, color, stroke,
		// drawHints, flags)

		// clip
		if (BitUtils.and(aFlags, SAVE_CLIP_FLAG)) {

			stack[stackTop++] = getClipX();
			stack[stackTop++] = getClipY();
			stack[stackTop++] = getClipWidth();
			stack[stackTop++] = getClipHeight();
		}

		// translation
		if (BitUtils.and(aFlags, SAVE_TRANSLATION)) {

			stack[stackTop++] = xTrans;
			stack[stackTop++] = yTrans;
		}

		// color
		if (BitUtils.and(aFlags, SAVE_COLOR)) {
			stack[stackTop++] = getColor();
		}

		// stroke
		if (BitUtils.and(aFlags, SAVE_STROKE)) {
			stack[stackTop++] = getStrokeStyle();
		}

		// draw hints
		if (BitUtils.and(aFlags, SAVE_DRAW_HINTS)) {
			stack[stackTop++] = drawHints;
		}

		// flags
		stack[stackTop++] = aFlags;

		// (brush, font)

		// brush
		if (BitUtils.and(aFlags, SAVE_BRUSH)) {
			objStack.push(getBrush());
		}

		// font
		if (BitUtils.and(aFlags, SAVE_FONT)) {
			objStack.push(getFont());
		}

		Log.d(TAG, "Save flags : ", Log.toBinaryString(aFlags),
			", stack top : ", Log.toString(stackTop));
	}

	public void restore() {

		if (ArrayUtils.isEmpty(stack))
			return;

		// (clipX, clipY, clipW, clipH, xTrans, yTrans, color, stroke,
		// drawHints, flags)

		// flags
		int flags = stack[--stackTop];

		// draw hints
		if (BitUtils.and(flags, SAVE_DRAW_HINTS)) {
			drawHints = stack[--stackTop];
		}

		// stroke
		if (BitUtils.and(flags, SAVE_STROKE)) {
			setStrokeStyle(stack[--stackTop]);
		}

		// color
		if (BitUtils.and(flags, SAVE_COLOR)) {
			this.setColor(stack[--stackTop]);
		}

		// translation
		if (BitUtils.and(flags, SAVE_TRANSLATION)) {

			yTrans = stack[--stackTop];
			xTrans = stack[--stackTop];
		}

		// clip
		if (BitUtils.and(flags, SAVE_CLIP_FLAG)) {

			int clipH = stack[--stackTop];
			int clipW = stack[--stackTop];
			int clipY = stack[--stackTop];
			int clipX = stack[--stackTop];

			this.setClip(clipX, clipY, clipW, clipH);
		}

		// (brush, font)

		// font
		if (BitUtils.and(flags, SAVE_FONT) && !objStack.isEmpty()) {
			setFont((FontEx) objStack.pop());
		}

		// brush
		if (BitUtils.and(flags, SAVE_BRUSH) && !objStack.isEmpty()) {
			setBrush((Brush) objStack.pop());
		}

		Log.d(TAG, "Restore flags : ", Log.toBinaryString(flags),
			", stack top : ", Log.toString(stackTop));
	}

	public void translate(int aX, int aY) {

		xTrans += aX;
		yTrans += aY;
	}

	public void resetTranslation() {

		xTrans = 0;
		yTrans = 0;
	}

	public int getTranslateX() {

		return xTrans;
	}

	public int getTranslateY() {

		return yTrans;
	}

	public void clipRect(Rectangle aClip) {

		Assert.assertNotNull(aClip);

		if (aClip.isValid()) {
			this.clipRect(aClip.x, aClip.y, aClip.width, aClip.height);
		}
	}

	public void clipRect(int aX, int aY, int aWidth, int aHeight) {

		g.clipRect(xTrans + aX, yTrans + aY, aWidth, aHeight);
	}

	public void setClip(Rectangle aClip) {

		Assert.assertNotNull(aClip);

		if (aClip.isValid()) {
			this.setClip(aClip.x, aClip.y, aClip.width, aClip.height);
		}
	}

	public void setClip(int aX, int aY, int aWidth, int aHeight) {

		g.setClip(xTrans + aX, yTrans + aY, aWidth, aHeight);
	}

	public Rectangle getClipRect() {

		return new Rectangle(getClipX(), getClipY(), getClipWidth(),
			getClipHeight());
	}

	public int getClipX() {

		return g.getClipX() - xTrans;
	}

	public int getClipY() {

		return g.getClipY() - yTrans;
	}

	public int getClipHeight() {

		return g.getClipHeight();
	}

	public int getClipWidth() {

		return g.getClipWidth();
	}

	public void copyArea(int aX_src, int aY_src, int aWidth, int aHeight,
		int aX_dest, int aY_dest, int aAnchor) {

		++gDrawnPrimitives;

		g.copyArea(xTrans + aX_src, yTrans + aY_src, aWidth, aHeight, xTrans
			+ aX_dest, yTrans + aY_dest, aAnchor);
	}

	public void drawArc(int aX, int aY, int aWidth, int aHeight,
		int aStartAngle, int aArcAngle) {

		++gDrawnPrimitives;

		g.drawArc(xTrans + aX, yTrans + aY, aWidth, aHeight, aStartAngle,
			aArcAngle);
	}

	public void drawChar(char aCharacter, int aX, int aY, int aAnchor) {

		++gDrawnPrimitives;

		g.drawChar(aCharacter, xTrans + aX, yTrans + aY, aAnchor);
	}

	public void drawChars(char[] aData, int aOffset, int aLength, int aX,
		int aY, int aAnchor) {

		++gDrawnPrimitives;

		g.drawChars(aData, aOffset, aLength, xTrans + aX, yTrans + aY, aAnchor);
	}

	public void drawCharsInBox(char[] aData, int aOffset, int aLength, int aX,
		int aY, Rectangle aBox, int aArchor) {

		Assert.assertNotNull(aBox);

		if (aBox.isValid()) {

			this.drawCharsInBox(aData, aOffset, aLength, aX, aY, aBox.x,
				aBox.y, aBox.width, aBox.height, aArchor);
		}
	}

	public void drawCharsInBox(char[] aData, int aOffset, int aLength, int aX,
		int aY, int aBoxX, int aBoxY, int aBoxW, int aBoxH, int aAnchor) {

		if (ArrayUtils.isEmpty(aData))
			return;

		int x = BitUtils.and(aAnchor, LEFT) ? aX : getBoxX(
			font.charsWidth(aData, aOffset, aLength), aBoxX, aBoxW, aX, aAnchor
				| GraphicsEx.IN_BOX);

		int y = getBoxY(font.getHeight(), aBoxY, aBoxH, aY, aAnchor
			| GraphicsEx.IN_BOX);

		if (BitUtils.and(aAnchor, BASELINE)) {
			y -= font.getBaselinePosition();
		}

		drawChars(aData, aOffset, aLength, x, y, GraphicsEx.LEFT_TOP);
	}

	public void drawImage(Image aImg, int aX, int aY, int aAnchor) {

		++gDrawnPrimitives;

		g.drawImage(aImg, xTrans + aX, yTrans + aY, aAnchor);
	}

	public void drawLine(int aX1, int aY1, int aX2, int aY2) {

		++gDrawnPrimitives;

		g.drawLine(xTrans + aX1, yTrans + aY1, xTrans + aX2, yTrans + aY2);
	}

	public void drawRGB(int[] aRgbData, int aOffset, int aScanlength, int aX,
		int aY, int aWidth, int aHeight, boolean aProcessAlpha) {

		++gDrawnPrimitives;

		g.drawRGB(aRgbData, aOffset, aScanlength, xTrans + aX, yTrans + aY,
			aWidth, aHeight, aProcessAlpha);
	}

	public void drawRect(int aX, int aY, int aWidth, int aHeight) {

		++gDrawnPrimitives;

		g.drawRect(xTrans + aX, yTrans + aY, aWidth, aHeight);
	}

	public void drawRoundRect(int aX, int aY, int aWidth, int aHeight,
		int aArcWidth, int aArcHeight) {

		++gDrawnPrimitives;

		g.drawRoundRect(xTrans + aX, yTrans + aY, aWidth, aHeight, aArcWidth,
			aArcHeight);
	}

	public void drawRegion(Image aSrc, int aX_src, int aY_src, int aWidth,
		int aHeight, int aTransform, int aX_dest, int aY_dest, int aAnchor) {

		++gDrawnPrimitives;

		g.drawRegion(aSrc, aX_src, aY_src, aWidth, aHeight, aTransform, xTrans
			+ aX_dest, yTrans + aY_dest, aAnchor);
	}

	public void drawString(String aStr, int aX, int aY, int aAnchor) {

		++gDrawnPrimitives;
		g.drawString(aStr, xTrans + aX, yTrans + aY, aAnchor);
	}

	public void drawBoxedString(String aStr, int aX, int aY, Rectangle aBox,
		int aAnchor) {

		if (StringUtils.isEmpty(aStr)) {
			return;
		}

		drawBoxedSubstring(aStr, 0, aStr.length(), aX, aY, aBox, aAnchor);
	}

	public void drawBoxedString(String aStr, int aX, int aY, int aBoxX,
		int aBoxY, int aBoxW, int aBoxH, int aAnchor) {

		if (StringUtils.isEmpty(aStr))
			return;

		drawBoxedSubstring(aStr, 0, aStr.length(), aX, aY, aBoxX, aBoxY, aBoxW,
			aBoxH, aAnchor);
	}

	public void drawSubstring(String aStr, int aOffset, int aLen, int aX,
		int aY, int aAnchor) {

		++gDrawnPrimitives;
		g.drawSubstring(aStr, aOffset, aLen, xTrans + aX, yTrans + aY, aAnchor);
	}

	public void drawBoxedSubstring(String aStr, int aOffset, int aLen, int aX,
		int aY, Rectangle aBox, int aAnchor) {

		Assert.assertNotNull(aBox);

		if (aBox.isValid()) {
			this.drawBoxedSubstring(aStr, aOffset, aLen, aX, aY, aBox.x,
				aBox.y, aBox.width, aBox.height, aAnchor);
		}
	}

	public void drawBoxedSubstring(String aStr, int aOffset, int aLen, int aX,
		int aY, int aBoxX, int aBoxY, int aBoxW, int aBoxH, int aAnchor) {

		if (StringUtils.isEmpty(aStr))
			return;

		int anchor = aAnchor | GraphicsEx.IN_BOX;
		int x = BitUtils.and(aAnchor, LEFT) ? aX : getBoxX(
			font.substringWidth(aStr, aOffset, aLen), aBoxX, aBoxW, aX, anchor);
		int y = getBoxY(font.getHeight(), aBoxY, aBoxH, aY, anchor);

		if (BitUtils.and(aAnchor, BASELINE)) {
			y -= font.getBaselinePosition();
		}

		drawSubstring(aStr, aOffset, aLen, x, y, GraphicsEx.LEFT_TOP);
	}

	public void fillArc(int aX, int aY, int aWidth, int aHeight,
		int aStartAngle, int aArcAngle) {

		++gDrawnPrimitives;

		g.fillArc(xTrans + aX, yTrans + aY, aWidth, aHeight, aStartAngle,
			aArcAngle);
	}

	public void fillRect(int aX, int aY, int aWidth, int aHeight) {

		++gDrawnPrimitives;

		g.fillRect(xTrans + aX, yTrans + aY, aWidth, aHeight);
	}

	public void fillRectEx(Rectangle aRect) {

		Assert.assertNotNull(aRect);

		if (aRect.isValid()) {
			this.fillRectEx(aRect.x, aRect.y, aRect.width, aRect.height);
		}
	}

	public void fillRectEx(int aX, int aY, int aWidth, int aHeight) {

		// check call normal fillRect or not
		if (brush.isNull()) {

			fillRect(aX, aY, aWidth, aHeight);
			return;
		}

		if (brush.isOpaqueColor() || !getDrawHint(DrawHints.HIGHT_QUALITY)) {

			int oldColor = g.getColor();
			g.setColor(brush.getColor());
			fillRect(aX, aY, aWidth, aHeight);// fill
			g.setColor(oldColor);
			return;
		}

		// check if the clip rectangle contains the fill rectangle,
		// otherwise get the intersection of both to fill
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipHeight();

		aX += xTrans;
		aY += yTrans;

		if (Rectangle.contains(clipX, clipY, clipW, clipH, aX, aY, aWidth,
			aHeight)) {

			fillRectExImpl(aX, aY, aWidth, aHeight);
		} else if (Rectangle.intersection(aX, aY, aWidth, aHeight, clipX,
			clipY, clipW, clipH, gTempRect)) {

			fillRectExImpl(gTempRect[0], gTempRect[1], gTempRect[2],
				gTempRect[3]);
		}
	}

	private void fillRectExImpl(int aX, int aY, int aWidth, int aHeight) {

		// advance fill use Brush
		int num = Math.max(aHeight, MAX_ARGB_STRIP_LINE_NUM);
		int[] strip = brush.getArgbStrip(aWidth, num);

		for (int h = 0, r = aHeight; h < aHeight; h += num, r -= num) {
			drawHorzArgbStrip(strip, aX, aY + h, aWidth, Math.min(num, r));
		}
	}

	public void fillRoundRect(int aX, int aY, int aWidth, int aHeight,
		int aArcWidth, int aArcHeight) {

		++gDrawnPrimitives;

		g.fillRoundRect(xTrans + aX, yTrans + aY, aWidth, aHeight, aArcWidth,
			aArcHeight);
	}

	public void fillTriangle(int aX1, int aY1, int aX2, int aY2, int aX3,
		int aY3) {

		++gDrawnPrimitives;

		g.fillTriangle(xTrans + aX1, yTrans + aY1, xTrans + aX2, yTrans + aY2,
			xTrans + aX3, yTrans + aY3);
	}

	public void setGrayScale(int aValue) {

		g.setGrayScale(aValue);
	}

	public int getGrayScale() {

		return g.getGrayScale();
	}

	public int getDisplayColor(int aColor) {

		return g.getDisplayColor(aColor);
	}

	public void setBrush(Brush aBrush) {

		brush = aBrush != null ? aBrush : Brush.NULL_BRUSH;
	}

	public Brush getBrush() {

		return brush;
	}

	public void setColor(int aRed, int aGreen, int aBlue) {

		g.setColor(aRed, aGreen, aBlue);
	}

	public void setColor(int RGB) {

		g.setColor(RGB);
	}

	public int getColor() {

		return g.getColor();
	}

	public int getRedComponent() {

		return g.getRedComponent();
	}

	public int getBlueComponent() {

		return g.getBlueComponent();
	}

	public int getGreenComponent() {

		return g.getGreenComponent();
	}

	public void setStrokeStyle(int aStyle) {

		g.setStrokeStyle(aStyle);
	}

	public int getStrokeStyle() {

		return g.getStrokeStyle();
	}

	public void setFont(FontEx aFont) {

		if (font != aFont) {

			font = aFont;
			g.setFont(aFont.font);
		}
	}

	public FontEx getFont() {

		return font;
	}

	private void drawHorzArgbStrip(int[] aArgbStrip, int aX, int aY,
		int aLineWidth, int aLineNums) {

		++gDrawnPrimitives;

		g.drawRGB(aArgbStrip, 0, aLineWidth, aX, aY, aLineWidth, aLineNums,
			true);
	}

	private void drawVerticalArgbStrip(int[] aArgbStrip, int aX, int aY,
		int aLineLen) {

		++gDrawnPrimitives;
		for (int i = 0; i < aLineLen; i += aArgbStrip.length) {
			g.drawRGB(aArgbStrip, 0, 1, aX, aY + i, 1, aArgbStrip.length, true);
		}
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("GraphicsEx [g=");
		sBuffer.append(g);
		sBuffer.append(", xTrans=");
		sBuffer.append(xTrans);
		sBuffer.append(", yTrans=");
		sBuffer.append(yTrans);
		sBuffer.append(", drawHints=");
		sBuffer.append(drawHints);
		sBuffer.append(", font=");
		sBuffer.append(font);
		sBuffer.append(", brush=");
		sBuffer.append(brush);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
