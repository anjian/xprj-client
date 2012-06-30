/**
 * Tiny.cn.uc.util.HashtableMap.java, 2011-2-11
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class HashtableMap extends Hashtable implements Map {

	/**
	 * 
	 */
	public HashtableMap() {

	}

	/**
	 * @param initialCapacity
	 */
	public HashtableMap(int initialCapacity) {

		super(initialCapacity);
	}

	/** {@inheritDoc} */
	public Object findEqualsValue(Object value) {

		if (value == null)
			throw new NullPointerException();
		
		Object element = null;
		for (Enumeration e = this.elements(); e.hasMoreElements();) {

			element = e.nextElement();
			if (value.equals(element))
				return element;

		}
		
		return null;
	}

	/** {@inheritDoc} */
	public Object putWithValueEqualityCheck(Object key, Object value) {

		Object realPutValue = findEqualsValue(value);
		if (realPutValue == null) {

			realPutValue = value;
		}

		put(key, realPutValue);
		
		return realPutValue;
	}

}
