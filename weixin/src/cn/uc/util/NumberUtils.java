/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.uc.util;

/**
 * <p>
 * Provides extra functionality for Java Number classes.
 * </p>
 * 
 * <p>
 * This version is modified by <a href="mailto:yixx@ucweb.com">Roger Yi</a>, add
 * range check and range bound of number.
 * </p>
 * 
 * @author Apache Software Foundation
 * @author <a href="mailto:rand_mcneely@yahoo.com">Rand McNeely</a>
 * @author <a href="mailto:steve.downey@netfolio.com">Steve Downey</a>
 * @author Eric Pugh
 * @author Phil Steitz
 * @author Matthew Hawthorne
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author <a href="mailto:fredrik@westermarck.com">Fredrik Westermarck</a>
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 2.0
 * @version $Id: NumberUtils.java 905707 2010-02-02 16:59:59Z niallp $
 */
public class NumberUtils {

	/**
	 * <p>
	 * <code>NumberUtils</code> instances should NOT be constructed in standard
	 * programming. Instead, the class should be used as
	 * <code>NumberUtils.toInt("6");</code>.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public NumberUtils() {

		super();
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning
	 * <code>zero</code> if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, <code>zero</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toInt(null) = 0
	 *   NumberUtils.toInt("")   = 0
	 *   NumberUtils.toInt("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the int represented by the string, or <code>zero</code> if
	 *         conversion fails
	 * @since 2.1
	 */
	public static int toInt(String str) {

		return toInt(str, 0);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning a default
	 * value if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toInt(null, 1) = 1
	 *   NumberUtils.toInt("", 1)   = 1
	 *   NumberUtils.toInt("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the int represented by the string, or the default if conversion
	 *         fails
	 * @since 2.1
	 */
	public static int toInt(String str, int defaultValue) {

		if (str == null)
			return defaultValue;
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>long</code>, returning
	 * <code>zero</code> if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, <code>zero</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toLong(null) = 0L
	 *   NumberUtils.toLong("")   = 0L
	 *   NumberUtils.toLong("1")  = 1L
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the long represented by the string, or <code>0</code> if
	 *         conversion fails
	 * @since 2.1
	 */
	public static long toLong(String str) {

		return toLong(str, 0L);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>long</code>, returning a default
	 * value if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toLong(null, 1L) = 1L
	 *   NumberUtils.toLong("", 1L)   = 1L
	 *   NumberUtils.toLong("1", 0L)  = 1L
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the long represented by the string, or the default if conversion
	 *         fails
	 * @since 2.1
	 */
	public static long toLong(String str, long defaultValue) {

		if (str == null)
			return defaultValue;
		try {
			return Long.parseLong(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>byte</code>, returning
	 * <code>zero</code> if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, <code>zero</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toByte(null) = 0
	 *   NumberUtils.toByte("")   = 0
	 *   NumberUtils.toByte("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the byte represented by the string, or <code>zero</code> if
	 *         conversion fails
	 * @since 2.5
	 */
	public static byte toByte(String str) {

		return toByte(str, (byte) 0);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>byte</code>, returning a default
	 * value if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toByte(null, 1) = 1
	 *   NumberUtils.toByte("", 1)   = 1
	 *   NumberUtils.toByte("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the byte represented by the string, or the default if conversion
	 *         fails
	 * @since 2.5
	 */
	public static byte toByte(String str, byte defaultValue) {

		if (str == null)
			return defaultValue;
		try {
			return Byte.parseByte(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>short</code>, returning
	 * <code>zero</code> if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, <code>zero</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toShort(null) = 0
	 *   NumberUtils.toShort("")   = 0
	 *   NumberUtils.toShort("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the short represented by the string, or <code>zero</code> if
	 *         conversion fails
	 * @since 2.5
	 */
	public static short toShort(String str) {

		return toShort(str, (short) 0);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>short</code>, returning a
	 * default value if the conversion fails.
	 * </p>
	 * 
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   NumberUtils.toShort(null, 1) = 1
	 *   NumberUtils.toShort("", 1)   = 1
	 *   NumberUtils.toShort("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the short represented by the string, or the default if conversion
	 *         fails
	 * @since 2.5
	 */
	public static short toShort(String str, short defaultValue) {

		if (str == null)
			return defaultValue;
		try {
			return Short.parseShort(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	// Min in array
	//--------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static long min(long[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns min
		long min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static int min(int[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns min
		int min = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] < min) {
				min = array[j];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static short min(short[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns min
		short min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte min(byte[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns min
		byte min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	// Max in array
	//--------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static long max(long[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns max
		long max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static int max(int[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns max
		int max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static short max(short[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns max
		short max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte max(byte[] array) {

		// Validates input
		if (array == null)
			throw new IllegalArgumentException("The Array must not be null");
		else if (array.length == 0)
			throw new IllegalArgumentException("Array cannot be empty.");

		// Finds and returns max
		byte max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	// 3 param min
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the minimum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static long min(long a, long b, long c) {

		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static int min(int a, int b, int c) {

		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static short min(short a, short b, short c) {

		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static byte min(byte a, byte b, byte c) {

		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	// 3 param max
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the maximum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static long max(long a, long b, long c) {

		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static int max(int a, int b, int c) {

		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of four <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @param d value 4
	 * @return the largest of the values
	 */
	public static int max(int a, int b, int c, int d) {

		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		if (d > a) {
			a = d;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static short max(short a, short b, short c) {

		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static byte max(byte a, byte b, byte c) {

		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks whether the <code>String</code> contains only digit characters.
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str the <code>String</code> to check
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {

		if (StringUtils.isEmpty(str))
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether the String a valid Java number.
	 * </p>
	 * 
	 * <p>
	 * Valid numbers include hexadecimal marked with the <code>0x</code> qualifier,
	 * scientific notation and numbers marked with a type qualifier (e.g. 123L).
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumber(String str) {

		if (StringUtils.isEmpty(str))
			return false;
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = chars[0] == '-' ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz)
					return false; // str == "0x"
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') &&
						(chars[i] < 'a' || chars[i] > 'f') &&
						(chars[i] < 'A' || chars[i] > 'F'))
						return false;
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			}
			else if (chars[i] == '.') {
				if (hasDecPoint || hasExp)
					// two decimal points or dec in exponent   
					return false;
				hasDecPoint = true;
			}
			else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp)
					// two E's
					return false;
				if (!foundDigit)
					return false;
				hasExp = true;
				allowSigns = true;
			}
			else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns)
					return false;
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			}
			else
				return false;
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9')
				// no type qualifier, OK
				return true;
			if (chars[i] == 'e' || chars[i] == 'E')
				// can't have an E at the last byte
				return false;
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp)
					// two decimal points or dec in exponent
					return false;
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns &&
				(chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
				return foundDigit;
			if (chars[i] == 'l' || chars[i] == 'L')
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}

	//-----------------------------------------------------------------------
	/**
	 * Check value is inside the range [min, max).
	 * 
	 * @param aMinInclusive min value
	 * @param aVal check value
	 * @param aMaxExclusive max value
	 * @return true when the value is inside the range
	 */
	public static boolean inRange(int aMinInclusive, int aVal, int aMaxExclusive) {

		return aVal >= aMinInclusive && aVal < aMaxExclusive;
	}

	/**
	 * Check value is inside the range [min, max].
	 * 
	 * @param aMinInclusive min value
	 * @param aVal check value
	 * @param aMaxInclusive max value
	 * @return true when the value is inside the range
	 */
	public static boolean inRangeInclusive(int aMinInclusive, int aVal,
		int aMaxInclusive) {

		return aVal >= aMinInclusive && aVal <= aMaxInclusive;
	}

	//-----------------------------------------------------------------------
	/**
	 * Bound the value in the range of [min, max].
	 * 
	 * @param aMin min value
	 * @param aVal value
	 * @param aMax max value
	 * @return the bounded value
	 */
	public static int bound(int aMin, int aVal, int aMax) {

		if (aVal <= aMin)
			return aMin;

		if (aVal >= aMax)
			return aMax;

		return aVal;
	}
}
