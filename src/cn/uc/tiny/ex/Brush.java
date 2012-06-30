/**
 * Tiny.cn.uc.ui.ex.Brush.java, 2010-11-25
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import cn.uc.build.Config;
import cn.uc.util.ArrayUtils;
import cn.uc.util.NumberUtils;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Brush {

	/** Brush type */
	public static final int NULL = -1;
	public static final int COLOR = 0;
	public static final int IMAGE = 1;
	public static final int GRADIENT = 2;
	
	public static final Brush NULL_BRUSH = new Brush(Brush.NULL, 0, 0, null);
	public static final Brush WHITE_BRUSH = Brush.createColorBrush(Color.WHITE);
	public static final Brush BLACK_BRUSH = Brush.createColorBrush(Color.BLACK);

	public static Brush createColorBrush(int aColor) {

		return new Brush(Brush.COLOR, aColor, 0, null);
	}

	public static Brush createColorBrush(int aAlpha, int aRGB) {

		return createColorBrush(getTransparentColor(aAlpha, aRGB));
	}

	public static boolean isOpaqueColor(int aColor) {

		return aColor >>> 24 == Color.OPAQUE;
	}

	public static int getColor(int aA, int aR, int aG, int aB) {

		return aA << 24 & 0xFF000000 | aR << 16 & 0x00FF0000 | aG << 8 &
			0x0000FF00 | aB & 0x000000FF;
	}

	public static int getOpaqueColor(int aARGB) {

		return 0xFF000000 | aARGB;
	}

	public static int getTransparentColor(int aAlpha, int aRGB) {

		return aAlpha << 24 | 0x00FFFFFF & aRGB;
	}

	public static int getMoreTransparentColor(int aARGB, int aAlphaDelta) {

		int a = aARGB >>> 24;
		a += aAlphaDelta;

		return a << 24 | aARGB & 0x00FFFFFF;
	}

	public static int getDarkerColor(int aARGB, int aFactor) {

		if ((aARGB & 0x00FFFFFF) == 0)
			return aARGB;
		else if (aFactor == 0)
			return Color.WHITE;
		else if (aFactor < 100)
			return getLighterColor(aARGB, 10000 / aFactor);

		int r = aARGB >>> 16 & 0x000000FF;
		int g = aARGB >>> 8 & 0x000000FF;
		int b = aARGB & 0x000000FF;

		int max = NumberUtils.max(r, g, b);
		int delta = aFactor - 100;
		aFactor = delta * 1000000 / (max * aFactor);

		r = r - r * r * aFactor / 1000000 - delta;
		g = g - g * g * aFactor / 1000000 - delta;
		b = b - b * b * aFactor / 1000000 - delta;

		if (r < 0) {
			r = 0;
		}

		if (g < 0) {
			g = 0;
		}

		if (b < 0) {
			b = 0;
		}

		return aARGB & 0xFF000000 | r << 16 | g << 8 | b;
	}

	public static int getLighterColor(int aARGB, int aFactor) {

		if (aFactor == 0)
			return Color.BLACK;
		else if ((aARGB & 0x00FFFFFF) == 0x00FFFFFF)
			return aARGB;
		else if ((aARGB & 0x00FFFFFF) == 0)
			return aARGB & 0xFF000000 | 0x00101010;

		int r = aARGB >>> 16 & 0x000000FF;
		int g = aARGB >>> 8 & 0x000000FF;
		int b = aARGB & 0x000000FF;

		int max = NumberUtils.max(r, g, b);
		int delta = aFactor - 100;
		aFactor = delta * 10000 / max;

		r = r + r * r * aFactor / 1000000 + delta;
		g = g + g * g * aFactor / 1000000 + delta;
		b = b + b * b * aFactor / 1000000 + delta;

		if (r > 255) {
			r = 255;
		}

		if (g > 255) {
			g = 255;
		}

		if (b > 255) {
			b = 255;
		}

		return aARGB & 0xFF000000 | r << 16 | g << 8 | b;
	}

	public static int getGray(int aR, int aG, int aB) {

		//Y = 0.299R + 0.587G + 0.114B
		return (299 * aR + 587 * aG + 114 * aB) / 1000;
	}

	public static int getGrayColor(int aARGB) {

		int r = aARGB >>> 16 & 0x000000FF;
		int g = aARGB >>> 8 & 0x000000FF;
		int b = aARGB & 0x000000FF;

		int gray = getGray(r, g, b);

		return aARGB & 0xFF000000 | gray << 16 | gray << 8 | gray;
	}

	public static int getGrayColor(int aARGB, int aFactor) {

		if (aFactor == 0)
			return Color.BLACK;
		else if ((aARGB & 0x00FFFFFF) == 0)
			return aARGB;

		int r = aARGB >>> 16 & 0x000000FF;
		int g = aARGB >>> 8 & 0x000000FF;
		int b = aARGB & 0x000000FF;

		int max = NumberUtils.max(r, g, b);
		int gray = getGray(r, g, b);

		aFactor = (aFactor - 100) * 10000 / max;

		r = gray + r * aFactor / 10000;
		g = gray + g * aFactor / 10000;
		b = gray + b * aFactor / 10000;

		return aARGB & 0xFF000000 | r << 16 | g << 8 | b;
	}

	public static int getRevertColor(int aARGB) {

		int a = aARGB >>> 24;
		int r = aARGB >>> 16 & 0x000000FF;
		int g = aARGB >>> 8 & 0x000000FF;
		int b = aARGB & 0x000000FF;

		a = 255 - a;
		r = 255 - r;
		g = 255 - g;
		b = 255 - b;

		return a << 24 | r << 16 | g << 8 | b;
	}

	public final int type;
	public final int arg1;
	public final int arg2;

	private final Object obj;

	private Brush(int aType, int aArg1, int aArg2, Object aObj) {

		type = aType;
		arg1 = aArg1;
		arg2 = aArg2;
		obj = aObj;
	}

	public boolean isNull() {

		return NULL_BRUSH.equals(this);
	}

	public boolean isColor() {

		return type == Brush.COLOR;
	}

	public boolean isOpaque() {

		if (isColor())
			return isOpaqueColor(arg1);
		else
			return true;
	}

	public boolean isOpaqueColor() {

		return isColor() && isOpaqueColor(arg1);
	}

	public int getColor() {

		return arg1;
	}

	public Brush lighter(int aFactor) {

		return getColorBrush(getLighterColor(arg1, aFactor));
	}

	public Brush darker(int aFactor) {

		return getColorBrush(getDarkerColor(arg1, aFactor));
	}

	public Brush gray() {

		return getColorBrush(getGrayColor(arg1));
	}

	public Brush grayer(int aFactor) {

		return getColorBrush(getGrayColor(arg1, aFactor));
	}

	public Brush opaque() {

		return getColorBrush(getOpaqueColor(arg1));
	}

	public Brush transparent(int aAlpha) {

		return getColorBrush(getTransparentColor(aAlpha, arg1));
	}

	public Brush moreTransparent(int aAlphaDelta) {

		return getColorBrush(getMoreTransparentColor(arg1, aAlphaDelta));
	}

	public Brush revert() {

		return getColorBrush(getRevertColor(arg1));
	}

	private Brush getColorBrush(int aNewColor) {

		if (isColor())
			return aNewColor != arg1 ? Brush.createColorBrush(aNewColor) : this;
		else
			return NULL_BRUSH;
	}

	public int[] getArgbStrip(int aWidth, int aHeight) {

		//generate a ARGB strip used to fill
		int[] strip = new int[aWidth * aHeight];
		ArrayUtils.fill(strip, this.getColor());

		return strip;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Brush))
			return false;

		Brush other = (Brush) obj;

		if (type == Brush.COLOR && type == other.type)
			return arg1 == other.arg1;

		if (type == Brush.NULL && type == other.type)
			return true;

		return false;
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Brush [type=");
		sBuffer.append(type);
		sBuffer.append(", arg1=");
		sBuffer.append(arg1);
		sBuffer.append(", arg2=");
		sBuffer.append(arg2);
		sBuffer.append(", obj=");
		sBuffer.append(obj);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
