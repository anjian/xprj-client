/**
 * Tiny.cn.uc.ui.geom.Rectangle.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.geom;

import cn.uc.build.Config;

/**
 * Rectangle region or shape, it contains a point of top-left corner (x, y) and
 * the width and height value.
 * 
 * Rectangle is an immutable class, all member variables are <code>final</code>,
 * so you can not change its value after creation, but Rectangle provides
 * methods to help client to create other derived Rectangles.
 * 
 * <p>
 * <strong>This class also used as Point or Dimension, or other type just for
 * holding four Integers, to save the cost of additional classes. Please be
 * careful when Rectangle used as Point, Dimension or other type, don't call any
 * of its methods under such circumstance, otherwise you will get the undefined
 * result.</strong>
 * <p>
 * When it used as Point, only the members x and y have meaning, the members
 * width and height are meaningless.
 * <p>
 * When is used as Dimension, only the members width and height have meaning,
 * the members x and y are meaningless.
 * </p>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Rectangle {

	public static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);

	public final int x;
	public final int y;
	public final int width;
	public final int height;
	
	/**
	 * Create Rectangle from arbitrary two corner points.
	 * 
	 * @param aX1
	 * @param aY1
	 * @param aX2
	 * @param aY2
	 * @return new rectangle
	 */
	public static Rectangle createFromArbitraryTwoPoints(int aX1, int aY1,
		int aX2, int aY2) {

		int x = Math.min(aX1, aX2);
		int y = Math.min(aY1, aY2);
		int width = Math.max(aX1, aX2) - x;
		int height = Math.max(aY1, aY2) - y;

		return new Rectangle(x, y, width, height);
	}

	public static boolean contains(int aRectX, int aRectY, int aRectW,
		int aRectH, int aX, int aY) {

		return aRectW > 0 && aRectH > 0 && aX >= aRectX &&
			aX < aRectX + aRectW && aY >= aRectY && aY < aRectY + aRectH;
	}

	public static boolean containsExcludeBoundary(int aRectX, int aRectY,
		int aRectW, int aRectH, int aX, int aY) {

		return aRectW > 0 && aRectH > 0 && aX > aRectX &&
			aX < aRectX + aRectW - 1 && aY > aRectY && aY < aRectY + aRectH - 1;
	}

	public static boolean contains(int aRectX, int aRectY, int aRectW,
		int aRectH, int aRect2X, int aRect2Y, int aRect2W, int aRect2H) {

		return aRectW > 0 && aRectH > 0 && aRect2W > 0 && aRect2H > 0 &&
			aRect2X >= aRectX && aRect2X + aRect2W <= aRectX + aRectW &&
			aRect2Y >= aRectY && aRect2Y + aRect2H <= aRectY + aRectH;
	}

	public static boolean containsExcludeBoundary(int aRectX, int aRectY,
		int aRectW, int aRectH, int aRect2X, int aRect2Y, int aRect2W,
		int aRect2H) {

		return aRectW > 0 && aRectH > 0 && aRect2W > 0 && aRect2H > 0 &&
			aRect2X > aRectX && aRect2X + aRect2W < aRectX + aRectW &&
			aRect2Y > aRectY && aRect2Y + aRect2H < aRectY + aRectH;
	}

	public static boolean intersects(int aRectX, int aRectY, int aRectW,
		int aRectH, int aRect2X, int aRect2Y, int aRect2W, int aRect2H) {

		if (aRectW <= 0 || aRectH <= 0 || aRect2W <= 0 || aRect2H <= 0)
			return false;//not intersected

		int x1 = Math.max(aRectX, aRect2X);
		int x2 = Math.min(aRectX + aRectW, aRect2X + aRect2W);
		if (x1 > x2)
			return false;//not intersected

		int y1 = Math.max(aRectY, aRect2Y);
		int y2 = Math.min(aRectY + aRectH, aRect2Y + aRect2H);
		if (y1 > y2)
			return false;//not intersected

		return true;
	}

	public static Rectangle intersection(int aRectX, int aRectY, int aRectW,
		int aRectH, int aRect2X, int aRect2Y, int aRect2W, int aRect2H) {

		if (aRectW <= 0 || aRectH <= 0 || aRect2W <= 0 || aRect2H <= 0)
			return EMPTY;//not intersected

		int x1 = Math.max(aRectX, aRect2X);
		int x2 = Math.min(aRectX + aRectW, aRect2X + aRect2W);
		if (x1 > x2)
			return EMPTY;//not intersected

		int y1 = Math.max(aRectY, aRect2Y);
		int y2 = Math.min(aRectY + aRectH, aRect2Y + aRect2H);

		if (y1 > y2)
			return EMPTY;//not intersected

		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	public static boolean intersection(int aRectX, int aRectY, int aRectW,
		int aRectH, int aRect2X, int aRect2Y, int aRect2W, int aRect2H,
		int[] aInterscectdRect) {

		if (aRectW <= 0 || aRectH <= 0 || aRect2W <= 0 || aRect2H <= 0)
			return false;//not intersected

		int x1 = Math.max(aRectX, aRect2X);
		int x2 = Math.min(aRectX + aRectW, aRect2X + aRect2W);
		if (x1 > x2)
			return false;//not intersected

		int y1 = Math.max(aRectY, aRect2Y);
		int y2 = Math.min(aRectY + aRectH, aRect2Y + aRect2H);

		if (y1 > y2)
			return false;//not intersected

		aInterscectdRect[0] = x1;
		aInterscectdRect[1] = y1;
		aInterscectdRect[2] = x2 - x1;
		aInterscectdRect[3] = y2 - y1;

		return true;
	}

	public static boolean intersection(Rectangle aRect, int aRect2X,
		int aRect2Y, int aRect2W, int aRect2H, int[] aInterscectdRect) {

		return intersection(aRect.x, aRect.y, aRect.width, aRect.height,
			aRect2X, aRect2Y, aRect2W, aRect2H, aInterscectdRect);
	}

	public static boolean intersection(Rectangle aRect, Rectangle aRect2,
		int[] aInterscectdRect) {

		return intersection(aRect.x, aRect.y, aRect.width, aRect.height,
			aRect2.x, aRect2.y, aRect2.width, aRect2.height, aInterscectdRect);
	}

	public static Rectangle union(int aRectX, int aRectY, int aRectW,
		int aRectH, int aRect2X, int aRect2Y, int aRect2W, int aRect2H) {

		if (aRectW <= 0 || aRectH <= 0 || aRect2W <= 0 || aRect2H <= 0)
			return EMPTY;

		int x1 = Math.min(aRectX, aRect2X);
		int x2 = Math.max(aRectX + aRectW, aRect2X + aRect2W);

		int y1 = Math.min(aRectY, aRect2Y);
		int y2 = Math.max(aRectY + aRectH, aRect2Y + aRect2H);

		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	public static boolean union(int aRectX, int aRectY, int aRectW, int aRectH,
		int aRect2X, int aRect2Y, int aRect2W, int aRect2H, int[] aUnionRect) {

		if (aRectW <= 0 || aRectH <= 0 || aRect2W <= 0 || aRect2H <= 0)
			return false;

		int x1 = Math.min(aRectX, aRect2X);
		int x2 = Math.max(aRectX + aRectW, aRect2X + aRect2W);

		int y1 = Math.min(aRectY, aRect2Y);
		int y2 = Math.max(aRectY + aRectH, aRect2Y + aRect2H);

		aUnionRect[0] = x1;
		aUnionRect[1] = y1;
		aUnionRect[2] = x2 - x1;
		aUnionRect[3] = y2 - y1;

		return true;
	}

	public static boolean union(Rectangle aRect, int aRect2X, int aRect2Y,
		int aRect2W, int aRect2H, int[] aUnionRect) {

		return union(aRect.x, aRect.y, aRect.width, aRect.height, aRect2X,
			aRect2Y, aRect2W, aRect2H, aUnionRect);
	}

	public static boolean union(Rectangle aRect, Rectangle aRect2,
		int[] aUnionRect) {

		return union(aRect.x, aRect.y, aRect.width, aRect.height, aRect2.x,
			aRect2.y, aRect2.width, aRect2.height, aUnionRect);
	}

	/**
	 * Create a rectangle with top-left corner point, width and height.
	 * 
	 * <p>
	 * The given width and height must larger than or equal to 0, and when one
	 * of them equals to zero means create an empty rectangle.
	 * </p>
	 * 
	 * @param aX top-left corner point's x coordination
	 * @param aY top-left corner point's y coordination
	 * @param aWidth width of rectangle
	 * @param aHeight height of rectangle
	 */
	public Rectangle(int aX, int aY, int aWidth, int aHeight) {

		x = aX;
		y = aY;
		width = aWidth;
		height = aHeight;
	}

	/**
	 * Check whether or not this Rectangle is valid, i.e. width and height
	 * larger than zero.
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {

		return width > 0 && height > 0;
	}

	public int getRight() {

		return x + width;
	}

	public int getBottom() {

		return y + height;
	}

	public int getCenterX() {

		return x + width / 2;
	}

	public int getCenterY() {

		return y + height / 2;
	}

	/**
	 * Checks whether or not this Rectangle entirely contains the specified
	 * Point, include boundary.
	 * 
	 * @param aX given Point's x coordination
	 * @param aY given Point's y coordination
	 * @return true if the given Point is contained entirely inside this
	 *         Rectangle; false otherwise
	 * @see #containsExcludeBoundary(int, int)
	 */
	public boolean contains(int aX, int aY) {

		return width > 0 && height > 0 && aX >= x && aX < getRight() &&
			aY >= y && aY < getBottom();
	}

	/**
	 * Checks whether or not this Rectangle entirely contains the specified
	 * Point, exclude boundary.
	 * 
	 * @param aX given Point's x coordination
	 * @param aY given Point's y coordination
	 * @return true if the given Point is contained entirely inside this
	 *         Rectangle; false otherwise
	 * @see #contains(int, int)
	 */
	public boolean containsExcludeBoundary(int aX, int aY) {

		return width > 0 && height > 0 && aX > x && aX < getRight() - 1 &&
			aY > y && aY < getBottom() - 1;
	}

	/**
	 * Checks whether or not this Rectangle entirely contains the specified
	 * Rectangle, include boundary.
	 * 
	 * @param aRect the specified Rectangle
	 * @return true if the Rectangle is contained entirely inside this
	 *         Rectangle; false otherwise
	 * @see #containsExcludeBoundary(Rectangle)
	 */
	public boolean contains(Rectangle aRect) {

		return this.contains(aRect.x, aRect.y, aRect.width, aRect.height);
	}

	/**
	 * Checks whether or not this Rectangle entirely contains the specified
	 * Rectangle, exclude boundary.
	 * 
	 * @param aRect the specified Rectangle
	 * @return true if the Rectangle is contained entirely inside this
	 *         Rectangle; false otherwise
	 * @see #contains(Rectangle)
	 */
	public boolean containsExcludeBoundary(Rectangle aRect) {

		return this.containsExcludeBoundary(aRect.x, aRect.y, aRect.width,
			aRect.height);
	}

	/**
	 * Checks whether this Rectangle entirely contains the Rectangle at the
	 * specified location (aX, aY) with the specified dimensions (aWidth,
	 * aHeight), include boundary.
	 * 
	 * @param aX the specified x coordinate
	 * @param aY the specified y coordinate
	 * @param aWidth the width of the Rectangle
	 * @param aHeight the height of the Rectangle
	 * @return true if the Rectangle specified by (aX, aY, aWidth, aHeight) is
	 *         entirely enclosed inside this Rectangle; false otherwise.
	 * @see #containsExcludeBoundary(int, int, int, int)
	 */
	public boolean contains(int aX, int aY, int aWidth, int aHeight) {

		return width > 0 && height > 0 && aWidth > 0 && aHeight > 0 &&
			aX >= x && aX + aWidth <= getRight() && aY >= y &&
			aY + aHeight <= getBottom();
	}

	/**
	 * Checks whether this Rectangle entirely contains the Rectangle at the
	 * specified location (aX, aY) with the specified dimensions (aWidth,
	 * aHeight), exclude boundary.
	 * 
	 * @param aX the specified x coordinate
	 * @param aY the specified y coordinate
	 * @param aWidth the width of the Rectangle
	 * @param aHeight the height of the Rectangle
	 * @return true if the Rectangle specified by (aX, aY, aWidth, aHeight) is
	 *         entirely enclosed inside this Rectangle; false otherwise.
	 * @see #contains(int, int, int, int)
	 */
	public boolean containsExcludeBoundary(int aX, int aY, int aWidth,
		int aHeight) {

		return width > 0 && height > 0 && aWidth > 0 && aHeight > 0 && aX > x &&
			aX + aWidth < getRight() && aY > y && aY + aHeight < getBottom();
	}

	/**
	 * Checks whether or not this Rectangle is intersect with the specified
	 * Rectangle.
	 * 
	 * @param aX the specified x coordinate
	 * @param aY the specified y coordinate
	 * @param aWidth the width of the Rectangle
	 * @param aHeight the height of the Rectangle
	 * @return true if intersect
	 */
	public boolean intersects(Rectangle aRect) {

		return this.intersects(aRect.x, aRect.y, aRect.width, aRect.height);
	}

	/**
	 * Checks whether or not this Rectangle is intersect with the specified
	 * Rectangle.
	 * 
	 * @param aRect specified Rectangle
	 * @return true if intersect
	 */
	public boolean intersects(int aX, int aY, int aWidth, int aHeight) {

		return intersects(x, y, width, height, aX, aY, aWidth, aHeight);
	}

	/**
	 * Create a new Rectangle by translating this Rectangle with specified delta
	 * X and delta Y.
	 * 
	 * @param aDx delta X
	 * @param aDy delta Y
	 * @return translated Rectangle
	 */
	public Rectangle translated(int aDx, int aDy) {

		if (aDx == 0 && aDy == 0)
			return this;
		else
			return new Rectangle(x + aDx, y + aDy, width, height);
	}

	/**
	 * Create a new Rectangle by move this Rectangle to a new Location.
	 * 
	 * @param aX new location's x coordination
	 * @param aDy new location's Y coordination
	 * @return moved Rectangle
	 */
	public Rectangle setPosition(int aX, int aY) {

		if (x == aX && y == aY)
			return this;
		else
			return new Rectangle(aX, aY, width, height);
	}

	/**
	 * Create a new Rectangle with a new size.
	 * 
	 * @param aWidth width
	 * @param aHeight height
	 * @return resized Rectangle
	 */
	public Rectangle setSize(int aWidth, int aHeight) {

		if (width == aWidth && height == aHeight)
			return this;
		else
			return new Rectangle(x, y, aWidth, aHeight);
	}

	/**
	 * Returns the bounding Rectangle of this Rectangle and the given Rectangle.
	 * 
	 * @param aRect given Rectangle
	 * @return bounding Rectangle
	 * @see #intersected(Rectangle)
	 */
	public Rectangle united(Rectangle aRect) {

		return this.united(aRect.x, aRect.y, aRect.width, aRect.height);
	}

	/**
	 * Returns the bounding Rectangle of this Rectangle and the given Rectangle.
	 * 
	 * @param aX the x coordinate of the Rectangle
	 * @param aY the y coordinate of the Rectangle
	 * @param aWidth the width of the Rectangle
	 * @param aHeight the height of the Rectangle
	 * @return bounding Rectangle
	 * @see #intersected(Rectangle)
	 */
	public Rectangle united(int aX, int aY, int aWidth, int aHeight) {

		if (this.equals(aX, aY, aWidth, aHeight))
			return this;
		else
			return union(x, y, width, height, aX, aY, aWidth, aHeight);
	}

	/**
	 * Returns the intersection of this Rectangle and the given Rectangle.
	 * 
	 * @param aRect given Rectangle
	 * @return intersection Rectangle, return {@link #EMPTY} when not
	 *         intersection
	 * @see #united(Rectangle)
	 */
	public Rectangle intersected(Rectangle aRect) {

		return this.intersected(aRect.x, aRect.y, aRect.width, aRect.height);
	}

	/**
	 * Returns the intersection of this Rectangle and the given Rectangle.
	 * 
	 * @param aX the x coordinate of the Rectangle
	 * @param aY the y coordinate of the Rectangle
	 * @param aWidth the width of the Rectangle
	 * @param aHeight the height of the Rectangle
	 * @return intersection Rectangle, return {@link #EMPTY} when not
	 *         intersection
	 * @see #united(Rectangle)
	 */
	public Rectangle intersected(int aX, int aY, int aWidth, int aHeight) {

		if (this.equals(aX, aY, aWidth, aHeight))
			return this;
		else
			return intersection(x, y, width, height, aX, aY, aWidth, aHeight);
	}

	public boolean equals(Object aObj) {

		if (this == aObj)
			return true;
		if (aObj == null)
			return false;
		if (!(aObj instanceof Rectangle))
			return false;

		Rectangle other = (Rectangle) aObj;

		return this.equals(other.x, other.y, other.width, other.height);
	}

	public boolean equals(int aX, int aY, int aWidth, int aHeight) {

		return x == aX && y == aY && width == aWidth && height == aHeight;
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("[x=");
		sBuffer.append(x);
		sBuffer.append(", y=");
		sBuffer.append(y);
		sBuffer.append(", width=");
		sBuffer.append(width);
		sBuffer.append(", height=");
		sBuffer.append(height);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
