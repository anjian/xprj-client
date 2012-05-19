/**
 * Tiny.cn.uc.util.CollectionUtils.java, 2010-11-19
 * 
 * Copyright (c) 2010 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

/**
 * CollectionUtils is an utility class, it help to perform some
 * Vector/Stack/Hashmap relative operations.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class CollectionUtils {

	/**
	 * Pop the stack until meet the given object.
	 * 
	 * @param aStack stack
	 * @param aObj object
	 * @return true if the stack contains the object, otherwise return false
	 */
	public static boolean popUntil(Stack aStack, Object aObj) {

		if (aStack == null) {
			return false;
		}

		if (aStack.contains(aObj)) {
			for (Object o = aStack.peek(); !aStack.empty(); o = aStack.pop()) {
				if (o == aObj) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Dump objects to vector from an Enumeration.
	 * 
	 * @param aEnum given Enumeration
	 * @return vector
	 */
	public static Vector toVector(Enumeration aEnum) {

		Vector vec = new Vector();
		for (; aEnum.hasMoreElements();) {
			vec.addElement(aEnum.nextElement());
		}
		return vec;
	}

	/**
	 * Dump string objects to vector from an Enumeration.
	 * 
	 * @param aEnum given Enumeration
	 * @return string vector
	 */
	public static Vector toStringVector(Enumeration aEnum) {

		Vector vec = new Vector();
		for (; aEnum.hasMoreElements();) {
			vec.addElement(aEnum.nextElement().toString());
		}
		return vec;
	}

	public static Vector toStringVector(Vector aVector) {

		return toStringVector(aVector.elements());
	}

	public static String[] toStringArray(Enumeration aEnum) {

		Vector v = toStringVector(aEnum);
		String[] s = new String[v.size()];
		v.copyInto(s);
		return s;
	}
}
