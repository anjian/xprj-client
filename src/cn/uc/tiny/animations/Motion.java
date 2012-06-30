/**
 * Tiny.cn.uc.ui.animations.Motion.java, 2010-12-24
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.animations;

import cn.uc.build.Config;
import cn.uc.util.BitUtils;
import cn.uc.util.Platform;
import cn.uc.util.debug.Log;

/**
 * Abstracts the notion of physical motion over time from a numeric location to
 * another. This class can be subclassed to implement any motion equation for
 * appropriate physics effects.
 * <p>
 * This class relies on the System.currentTimeMillis() method to provide
 * transitions between coordinates. The motion can be subclassed to provide
 * every type of motion feel from parabolic motion to spline and linear motion.
 * The default implementation provides a simple algorithm giving the feel of
 * acceleration and deceleration.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Motion {

	public static final String TAG = "MOTION";

	/** State of a motion Popup, IN, OUT, STAY and END */
	public static final int IN = 0;
	public static final int OUT = 1;
	public static final int STAY = 2;
	public static final int END = 3;

	/** Type of Motion */
	public static final int LINEAR = 0;
	public static final int SPLINE = 1;
	public static final int FRICTION = 2;

	private static final int IS_NORMALIZATION = 0;//default is normalization
	private static final int IS_FINISHED = 1;//default is not finished

	public final int type;
	private int flags;

	private int srcX;
	private int dstX;
	private int srcY;
	private int dstY;

	private int duration = 500;
	private int startTime;

	private int velocity;
	private int friction;

	/**
	 * Creates a linear motion starting from source value all the way to
	 * destination value
	 * 
	 * @return new motion object
	 */
	public static Motion createLinear() {

		return new Motion(LINEAR);
	}

	/**
	 * Creates a spline motion starting from source value all the way to
	 * destination value
	 * 
	 * @return new motion object
	 */
	public static Motion createSpline() {

		return new Motion(SPLINE);
	}

	/**
	 * Creates a friction motion starting from source with initial speed and the
	 * friction.
	 * 
	 * @param aInitVelocity the starting velocity, must in the range of 1000
	 *            ~ 10000 or -1000 ~ -10000
	 * @param aFriction the motion friction, must in the range of 1 ~ 10
	 * @return new motion object
	 */
	public static Motion createFriction(int aInitVelocity, int aFriction,
		boolean aNormalize) {

		Motion friction = new Motion(FRICTION);
		friction.velocity = aInitVelocity;
		friction.friction = aFriction;
		friction.duration = Math.abs(aInitVelocity) / aFriction;
		friction.flags = BitUtils.set(friction.flags, IS_NORMALIZATION,
			aNormalize);

		return friction;
	}

	private Motion(int aType) {

		type = aType;

		//default is normalize
		flags = BitUtils.set(flags, IS_NORMALIZATION);
	}

	private Motion(Motion aOther) {

		type = aOther.type;
		flags = aOther.flags;
		duration = aOther.duration;
		velocity = aOther.velocity;
		friction = aOther.friction;
		srcX = aOther.srcX;
		dstX = aOther.dstX;
		srcY = aOther.srcY;
		dstY = aOther.dstY;
	}

	public Motion clone() {

		return new Motion(this);
	}

	public void start() {

		startTime = Platform.currentTimeAfterStart();

		flags = BitUtils.clear(flags, IS_FINISHED);
	}

	public void setDuration(int aDuration) {

		if (type != FRICTION) {

			duration = aDuration;
		}
	}

	public int getDuration() {

		return duration;
	}

	public void setSource(int aSrc) {

		srcX = aSrc;
	}

	public void setSource(int aX, int aY) {

		srcX = aX;
		srcY = aY;
	}

	public int getSource() {

		return getSourceX();
	}

	public int getSourceX() {

		return srcX;
	}

	public int getSourceY() {

		return srcY;
	}

	public void setDestination(int aDst) {

		dstX = aDst;
	}

	public void setDestination(int aX, int aY) {

		dstX = aX;
		dstY = aY;
	}

	public int getDestination() {

		return getDestinationX();
	}

	public int getDestinationX() {

		return dstX;
	}

	public int getDestinationY() {

		return dstY;
	}

	public boolean isFinished() {

		return this.isFinished(Platform.currentTimeMillis());
	}

	public boolean isFinished(long aCurrTime) {

		return aCurrTime - getStartTime() >= duration ||
			BitUtils.get(flags, IS_FINISHED);
	}

	public int getValue() {

		return this.getValueX();
	}

	public int getValue(int aState) {

		return this.getValueX(aState);
	}

	public int getValue(long aCurrTime) {

		return this.getValueX(aCurrTime);
	}

	public int getValue(int aState, long aCurrTime) {

		return this.getValueX(aState, aCurrTime);
	}

	public int getValueX() {

		return this.getValueX(Platform.currentTimeMillis());
	}

	public int getValueX(int aState) {

		return this.getValueX(aState, Platform.currentTimeMillis());
	}

	public int getValueX(long aCurrTime) {

		return this.getValue(srcX, dstX, getStartTime(), aCurrTime);
	}

	public int getValueX(int aState, long aCurrTime) {

		int src = aState == IN ? srcX : dstX;
		int dst = aState == IN ? dstX : srcX;

		return this.getValue(src, dst, getStartTime(), aCurrTime);
	}

	public int getValueY() {

		return this.getValueY(Platform.currentTimeMillis());
	}

	public int getValueY(int aState) {

		return this.getValueX(aState, Platform.currentTimeMillis());
	}

	public int getValueY(long aCurrTime) {

		return this.getValue(srcY, dstY, getStartTime(), aCurrTime);
	}

	public int getValueY(int aState, long aCurrTime) {

		int src = aState == IN ? srcY : dstY;
		int dst = aState == IN ? dstY : srcY;

		return this.getValue(src, dst, getStartTime(), aCurrTime);
	}

	public long getStartTime() {

		return Platform.realTimeOfDelta(startTime);
	}

	private int getValue(int src, int dst, long startTime, long currTime) {

		//check
		if (BitUtils.get(flags, IS_NORMALIZATION)) {

			if (currTime <= startTime || dst == src)
				return src;

			if (currTime - startTime >= duration || duration == 0)
				return dst;
		}

		if (type == FRICTION) {

			if (velocity == 0 || dst == src)
				return src;
		}

		int eclipse = (int) (currTime - startTime);
		int val = dst;

		switch (type) {

		case LINEAR:
			val = getLinearValue(src, dst, eclipse);
			break;

		case SPLINE:
			val = getSplineValue(src, dst, eclipse);
			break;

		case FRICTION:
			val = getFrictionValue(src, dst, eclipse);
			break;
		}

		//prevent the not normalization motion too long to finished
		if (src < dst && val >= dst || src > dst && val <= dst) {

			Log.d(TAG, "Finished src:", Log.toString(src), ", dst:",
				Log.toString(dst), ", val:", Log.toString(val));

			flags = BitUtils.set(flags, IS_FINISHED);
		}

		return val;
	}

	private int getLinearValue(int src, int dst, int eclipse) {

		int val = src + eclipse * (dst - src) / duration;

		Log.d(TAG, "Linear src:", Log.toString(src), ", dst:",
			Log.toString(dst), ", val:", Log.toString(val));

		return val;
	}

	private int getSplineValue(int src, int dst, int eclipse) {

		int center = duration / 2;
		int val = 0;

		if (dst < src) {

			eclipse = duration - eclipse;
		}

		if (eclipse > center) {

			val = -center * center + 2 * center * eclipse - eclipse * eclipse /
				2;
		}
		else {

			val = eclipse * eclipse / 2;
		}

		val *= Math.abs(dst - src);
		val /= center * center;
		val += src < dst ? src : dst;

		Log.d(TAG, "Spline src:", Log.toString(src), ", dst:",
			Log.toString(dst), ", val:", Log.toString(val));

		return val;
	}

	private int getFrictionValue(int src, int dst, int eclipse) {

		int val = Math.abs(velocity) * eclipse - friction * eclipse * eclipse /
			2;

		if (BitUtils.get(flags, IS_NORMALIZATION)) {

			val *= (dst - src) * 2;
			val /= duration * velocity;

			val += src;
		}
		else {

			val /= dst > src ? 10000 : -10000;

			val += src;
			val = dst > src ? Math.min(val, dst) : Math.max(val, dst);
		}

		Log.d(TAG, "Friction src:", Log.toString(src), ", dst:",
			Log.toString(dst), ", val:", Log.toString(val));

		return val;
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Motion [type=");
		sBuffer.append(type);
		sBuffer.append(", flag=");
		sBuffer.append(flags);
		sBuffer.append(", srcX=");
		sBuffer.append(srcX);
		sBuffer.append(", dstX=");
		sBuffer.append(dstX);
		sBuffer.append(", srcY=");
		sBuffer.append(srcY);
		sBuffer.append(", dstY=");
		sBuffer.append(dstY);
		sBuffer.append(", duration=");
		sBuffer.append(duration);
		sBuffer.append(", startTime=");
		sBuffer.append(startTime);
		sBuffer.append(", velocity=");
		sBuffer.append(velocity);
		sBuffer.append(", friction=");
		sBuffer.append(friction);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
