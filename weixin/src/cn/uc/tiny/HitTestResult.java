/**
 * Tiny.cn.uc.ui.HitTestResult.java, 2010-12-1
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.build.Config;
import cn.uc.util.debug.Log;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class HitTestResult {

	public static final int HIT_NONE = -1;
	public static final int HIT_MYSELF = 0;
	public static final int HIT_MY_CHILD = 1;

	public static final HitTestResult MISS_HIT = new HitTestResult(HIT_NONE,
		null, null);

	public final int result;
	public final Component hit;
	public final Object data;

	/**
	 * @param aResult
	 * @param aHit
	 */
	public HitTestResult(int aResult, Component aHit) {

		this(aResult, aHit, null);
	}

	/**
	 * @param aResult
	 * @param aHit
	 * @param aData
	 */
	public HitTestResult(int aResult, Component aHit, Object aData) {

		result = aResult;
		hit = aHit;
		data = aData;
	}

	public boolean isMiss() {

		return MISS_HIT == this || result == HIT_NONE;
	}

	public boolean isHit() {

		return result == HIT_MYSELF || result == HIT_MY_CHILD;
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("HitTestResult [result=");
		sBuffer.append(result);
		sBuffer.append(", hit=");
		sBuffer.append(Log.toString(hit));
		sBuffer.append(", data=");
		sBuffer.append(data);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
