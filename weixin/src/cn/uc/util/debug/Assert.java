/**
 * Tiny.cn.uc.util.Assert.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util.debug;

import cn.uc.util.StringUtils;

/**
 * Assert the condition, pre-conditions/post-conditions of method,
 * invariants of object is valid.
 * 
 * <p>
 * It can be by each method to check its pre-conditions, post-conditions and
 * object invariants during its execution. If the defined conditions are not
 * fulfill, sub classes of {@link RuntimeException}, such as
 * {@link NullPointerException}, {@link IllegalArgumentException} or
 * {@link IllegalStateException} will be thrown.
 * </p>
 * 
 * <p>
 * If the method accept a 'type' argument, please pass one of the value between
 * Assert.ARG or Assert.STATE, it will be used to choose to throw
 * {@link IllegalArgumentException} or {@link IllegalStateException} when
 * failure.
 * </p>
 * 
 * <p>
 * <strong> This class can be removed totally along with its methods' invoking
 * statements by ProGuard when the final release of software do not need any
 * assertions, you can put the following script in ProGuard's configuration
 * file: </strong>
 * 
 * <pre>
 * -assumenosideeffects class cn.uc.util.debug.Assert {
 *   &ltmethods&gt;
 * }
 * #&ltmethods&gt -> < methods >
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Assert {

	public static final int ARG = 0;
	public static final int STATE = 1;

	public static void assertNotNull(Object aObj) {

		if (aObj == null)
			throw new NullPointerException(
				"The object is 'null', and 'not null' is expected.");
	}

	public static void assertNotEmpty(Object aObj, int aType) {

		if (aObj == null)
			throw new NullPointerException(
				"The object is 'null', and 'not null' is expected.");
		else if (aObj instanceof String) {

			if (StringUtils.isEmpty((String) aObj)) {

				throwByType(aType,
					"The object is 'empty', and 'not empty' is expected.");
			}
		}
	}

	public static void assertTrue(boolean aCond, int aType) {

		if (!aCond) {
			throwByType(aType,
				"The condition is 'false', and 'true' is expected.");
		}
	}

	public static void assertFalse(boolean aCond, int aType) {

		if (aCond) {
			throwByType(aType,
				"The condition is 'true', and 'false' is expected.");
		}
	}

	public static void assertNull(Object aObj, int aType) {

		if (aObj != null) {

			String err = getTypeString(aType) + " " + aObj +
				" is 'not null', and 'null' is expected.";

			throwByType(aType, err);
		}
	}

	public static void assertSame(Object aObj1, Object aObj2, int aType) {

		if (aObj1 != aObj2) {

			String err = getTypeString(aType) + " " + aObj1 +
				" not same with " + aObj2;

			throwByType(aType, err);
		}
	}

	public static void assertEquals(Object aObj1, Object aObj2, int aType) {

		if (aObj1 != aObj2 && aObj1 != null && !aObj1.equals(aObj2)) {

			String err = getTypeString(aType) + " " + aObj1 +
				" not equals to " + aObj2;

			throwByType(aType, err);
		}
	}

	public static void assertNotEquals(Object aObj1, Object aObj2, int aType) {

		if (aObj1 == aObj2 || aObj1 != null && aObj1.equals(aObj2)) {

			String err = getTypeString(aType) + " " + aObj1 + " equals to " +
				aObj2;

			throwByType(aType, err);
		}
	}

	public static void assertEquals(int aVal1, int aVal2, int aType) {

		if (aVal1 != aVal2) {

			String err = getTypeString(aType) + " " + aVal1 +
				" not equals to " + aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertNotEquals(int aVal1, int aVal2, int aType) {

		if (aVal1 == aVal2) {

			String err = getTypeString(aType) + " " + aVal1 + " equals to " +
				aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertLargerThan(int aVal1, int aVal2, int aType) {

		if (aVal1 <= aVal2) {

			String err = getTypeString(aType) + " " + aVal1 +
				" not larger than " + aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertLargerOrEquals(int aVal1, int aVal2, int aType) {

		if (aVal1 < aVal2) {

			String err = getTypeString(aType) + " " + aVal1 + " less than " +
				aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertLessThan(int aVal1, int aVal2, int aType) {

		if (aVal1 >= aVal2) {

			String err = getTypeString(aType) + " " + aVal1 +
				" not less than " + aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertLessOrEquals(int aVal1, int aVal2, int aType) {

		if (aVal1 > aVal2) {

			String err = getTypeString(aType) + " " + aVal1 + " larger than " +
				aVal2;

			throwByType(aType, err);
		}
	}

	public static void assertInRangeV1(int aMinInclusive, int aVal,
		int aMaxExclusive, int aType) {

		if (aMinInclusive >= aMaxExclusive) {

			throwByType(aType, "Range [min ~ max) min >= max.");
		}

		if (aVal < aMinInclusive || aVal >= aMaxExclusive) {

			String err = getTypeString(aType) + " (" + aVal +
				") out of range [" + aMinInclusive + " ~ " + aMaxExclusive +
				").";

			throwByType(aType, err);
		}
	}

	public static void assertInRangeV2(int aMinInclusive, int aVal,
		int aMaxInclusive, int aType) {

		if (aMinInclusive >= aMaxInclusive) {

			throwByType(aType, "Range [min ~ max] min >= max.");
		}

		if (aVal < aMinInclusive || aVal > aMaxInclusive) {

			String err = getTypeString(aType) + " (" + aVal +
				") out of range [" + aMinInclusive + " ~ " + aMaxInclusive +
				"].";

			throwByType(aType, err);
		}
	}

	private static String getTypeString(int aType) {

		return aType == ARG ? "ARG" : "STATE";
	}

	private static void throwByType(int aType, String aErrMsg) {

		if (aType == ARG)
			throw new IllegalArgumentException(aErrMsg);
		else
			throw new IllegalStateException(aErrMsg);
	}
}
