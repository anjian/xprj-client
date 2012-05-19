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
 * Operations on arrays, primitive arrays (like <code>int[]</code>) and
 * primitive wrapper arrays (like <code>Integer[]</code>).
 * </p>
 * 
 * <p>
 * This class tries to handle <code>null</code> input gracefully. An exception
 * will not be thrown for a <code>null</code> array input. However, an Object
 * array that contains a <code>null</code> element may throw an exception. Each
 * method documents its behaviour.
 * </p>
 * 
 * <p>
 * This version is modified by <a href="mailto:yixx@ucweb.com">Roger Yi</a>,
 * remove the part can not compiled with JavaMe SDK and add some functions :
 * 
 * <pre>
 * {@link #toString(int[])}
 * {@link #fill(int[], int)}
 * 
 * clear(Object[])
 * clear(Object[], int, int)
 * shiftLeft(Object[], int)
 * shiftRight(Object[], int)
 * lastOne(Object[])
 * lastNotNull(Object[])
 * pushBack(Object[], Object)
 * pushFront(Object[], Object)
 * </pre>
 * 
 * </p>
 * 
 * @author Apache Software Foundation
 * @author Moritz Petersen
 * @author <a href="mailto:fredrik@westermarck.com">Fredrik Westermarck</a>
 * @author Nikolay Metchev
 * @author Matthew Hawthorne
 * @author Tim O'Brien
 * @author Pete Gieser
 * @author Gary Gregory
 * @author <a href="mailto:equinus100@hotmail.com">Ashwin S</a>
 * @author Maarten Coene
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 2.0
 * @version $Id: ArrayUtils.java 905988 2010-02-03 10:52:37Z niallp $
 */
public final class ArrayUtils {

	/**
	 * An empty immutable <code>Object</code> array.
	 */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	/**
	 * An empty immutable <code>Class</code> array.
	 */
	public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
	/**
	 * An empty immutable <code>String</code> array.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	/**
	 * An empty immutable <code>long</code> array.
	 */
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	/**
	 * An empty immutable <code>Long</code> array.
	 */
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
	/**
	 * An empty immutable <code>int</code> array.
	 */
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	/**
	 * An empty immutable <code>Integer</code> array.
	 */
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
	/**
	 * An empty immutable <code>short</code> array.
	 */
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];
	/**
	 * An empty immutable <code>Short</code> array.
	 */
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
	/**
	 * An empty immutable <code>byte</code> array.
	 */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	/**
	 * An empty immutable <code>Byte</code> array.
	 */
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
	/**
	 * An empty immutable <code>boolean</code> array.
	 */
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
	/**
	 * An empty immutable <code>Boolean</code> array.
	 */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
	/**
	 * An empty immutable <code>char</code> array.
	 */
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];
	/**
	 * An empty immutable <code>Character</code> array.
	 */
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	/**
	 * The index value when an element is not found in a list or array:
	 * <code>-1</code>.
	 * This value is returned by methods in this class and can also be used in
	 * comparisons with values returned by
	 * various method from {@link java.util.List}.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <p>
	 * ArrayUtils instances should NOT be constructed in standard programming.
	 * Instead, the class should be used as
	 * <code>ArrayUtils.clone(new int[] {2})</code>.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public ArrayUtils() {

		super();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static long[] clone(long[] array) {

		if (array == null)
			return null;
		long[] cloneArray = new long[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static int[] clone(int[] array) {

		if (array == null)
			return null;
		int[] cloneArray = new int[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static short[] clone(short[] array) {

		if (array == null)
			return null;
		short[] cloneArray = new short[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static char[] clone(char[] array) {

		if (array == null)
			return null;
		char[] cloneArray = new char[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static byte[] clone(byte[] array) {

		if (array == null)
			return null;
		byte[] cloneArray = new byte[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static boolean[] clone(boolean[] array) {

		if (array == null)
			return null;
		boolean[] cloneArray = new boolean[array.length];
		System.arraycopy(array, 0, cloneArray, 0, array.length);
		return cloneArray;
	}

	// nullToEmpty
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Object[] nullToEmpty(Object[] array) {

		if (array == null || array.length == 0)
			return EMPTY_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static String[] nullToEmpty(String[] array) {

		if (array == null || array.length == 0)
			return EMPTY_STRING_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static long[] nullToEmpty(long[] array) {

		if (array == null || array.length == 0)
			return EMPTY_LONG_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static int[] nullToEmpty(int[] array) {

		if (array == null || array.length == 0)
			return EMPTY_INT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static short[] nullToEmpty(short[] array) {

		if (array == null || array.length == 0)
			return EMPTY_SHORT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static char[] nullToEmpty(char[] array) {

		if (array == null || array.length == 0)
			return EMPTY_CHAR_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static byte[] nullToEmpty(byte[] array) {

		if (array == null || array.length == 0)
			return EMPTY_BYTE_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static boolean[] nullToEmpty(boolean[] array) {

		if (array == null || array.length == 0)
			return EMPTY_BOOLEAN_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Long[] nullToEmpty(Long[] array) {

		if (array == null || array.length == 0)
			return EMPTY_LONG_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Integer[] nullToEmpty(Integer[] array) {

		if (array == null || array.length == 0)
			return EMPTY_INTEGER_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Short[] nullToEmpty(Short[] array) {

		if (array == null || array.length == 0)
			return EMPTY_SHORT_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Character[] nullToEmpty(Character[] array) {

		if (array == null || array.length == 0)
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Byte[] nullToEmpty(Byte[] array) {

		if (array == null || array.length == 0)
			return EMPTY_BYTE_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a <code>null</code> reference
	 * to an empty one.
	 * </p>
	 * 
	 * <p>
	 * This method returns an empty array for a <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be
	 * overridden with the empty <code>public static</code> references in this
	 * class.
	 * </p>
	 * 
	 * @param array the array to check for <code>null</code> or empty
	 * @return the same array, <code>public static</code> empty array if
	 *         <code>null</code> or empty input
	 * @since 2.5
	 */
	public static Boolean[] nullToEmpty(Boolean[] array) {

		if (array == null || array.length == 0)
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		return array;
	}

	/**
	 * <p>
	 * Produces a new <code>long</code> array containing the elements between
	 * the start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static long[] subarray(long[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_LONG_ARRAY;

		long[] subarray = new long[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new <code>int</code> array containing the elements between the
	 * start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static int[] subarray(int[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_INT_ARRAY;

		int[] subarray = new int[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new <code>short</code> array containing the elements between
	 * the start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static short[] subarray(short[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_SHORT_ARRAY;

		short[] subarray = new short[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new <code>char</code> array containing the elements between
	 * the start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static char[] subarray(char[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_CHAR_ARRAY;

		char[] subarray = new char[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new <code>byte</code> array containing the elements between
	 * the start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static byte[] subarray(byte[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_BYTE_ARRAY;

		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new <code>boolean</code> array containing the elements between
	 * the start and end indices.
	 * </p>
	 * 
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input
	 * produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0)
	 *            is promoted to 0, overvalue (&gt;array.length) results
	 *            in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the
	 *            returned subarray. Undervalue (&lt; startIndex) produces
	 *            empty array, overvalue (&gt;array.length) is demoted to
	 *            array length.
	 * @return a new array containing the elements between
	 *         the start and end indices.
	 * @since 2.1
	 */
	public static boolean[] subarray(boolean[] array, int startIndexInclusive,
		int endIndexExclusive) {

		if (array == null)
			return null;
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0)
			return EMPTY_BOOLEAN_ARRAY;

		boolean[] subarray = new boolean[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	// Is same length
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * 
	 * <p>
	 * Any multi-dimensional aspects of the arrays are ignored.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(Object[] array1, Object[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(long[] array1, long[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(int[] array1, int[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(short[] array1, short[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(char[] array1, char[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(byte[] array1, byte[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating <code>null</code>
	 * arrays as length <code>0</code>.
	 * </p>
	 * 
	 * @param array1 the first array, may be <code>null</code>
	 * @param array2 the second array, may be <code>null</code>
	 * @return <code>true</code> if length of arrays matches, treating
	 *         <code>null</code> as an empty array
	 */
	public static boolean isSameLength(boolean[] array1, boolean[] array2) {

		if (array1 == null && array2 != null && array2.length > 0 ||
			array2 == null && array1 != null && array1.length > 0 ||
			array1 != null && array2 != null && array1.length != array2.length)
			return false;
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same type taking into account
	 * multi-dimensional arrays.
	 * </p>
	 * 
	 * @param array1 the first array, must not be <code>null</code>
	 * @param array2 the second array, must not be <code>null</code>
	 * @return <code>true</code> if type of arrays matches
	 * @throws IllegalArgumentException if either array is <code>null</code>
	 */
	public static boolean isSameType(Object array1, Object array2) {

		if (array1 == null || array2 == null)
			throw new IllegalArgumentException("The Array must not be null");
		return array1.getClass().getName().equals(array2.getClass().getName());
	}

	// Reverse
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * There is no special handling for multi-dimensional arrays.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(Object[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		Object tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(long[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(int[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(short[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(char[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		char tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(byte[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * 
	 * <p>
	 * This method does nothing for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be <code>null</code>
	 */
	public static void reverse(boolean[] array) {

		if (array == null)
			return;
		int i = 0;
		int j = array.length - 1;
		boolean tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	// IndexOf search
	// ----------------------------------------------------------------------

	// Object IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given object in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param objectToFind the object to find, may be <code>null</code>
	 * @return the index of the object within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int indexOf(Object[] array, Object objectToFind) {

		return indexOf(array, objectToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given object in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param objectToFind the object to find, may be <code>null</code>
	 * @param startIndex the index to start searching at
	 * @return the index of the object within the array starting at the index,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int indexOf(Object[] array, Object objectToFind,
		int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++)
				if (array[i] == null)
					return i;
		}
		else {
			for (int i = startIndex; i < array.length; i++)
				if (objectToFind.equals(array[i]))
					return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given object within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param objectToFind the object to find, may be <code>null</code>
	 * @return the last index of the object within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind) {

		return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given object in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param objectToFind the object to find, may be <code>null</code>
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the object within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind,
		int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i >= 0; i--)
				if (array[i] == null)
					return i;
		}
		else {
			for (int i = startIndex; i >= 0; i--)
				if (objectToFind.equals(array[i]))
					return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the object is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param objectToFind the object to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(Object[] array, Object objectToFind) {

		return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
	}

	// long IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(long[] array, long valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(long[] array, long valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(long[] array, long valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// int IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(int[] array, int valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(int[] array, int valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(int[] array, int valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// short IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(short[] array, short valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(short[] array, short valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind,
		int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(short[] array, short valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// char IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 * @since 2.1
	 */
	public static boolean contains(char[] array, char valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// byte IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(byte[] array, byte valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(byte[] array, byte valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {

		if (array == null)
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(byte[] array, byte valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// boolean IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind) {

		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given
	 * index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the
	 * array length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND}
	 *         (<code>-1</code>) if not found or <code>null</code> array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind,
		int startIndex) {

		if (ArrayUtils.isEmpty(array))
			return INDEX_NOT_FOUND;
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) if
	 * <code>null</code> array input.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may
	 *            be <code>null</code>
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind) {

		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the
	 * given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a
	 * <code>null</code> input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} (
	 * <code>-1</code>). A startIndex larger than the array length will search
	 * from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be
	 *            <code>null</code>
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array,
	 *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or
	 *         <code>null</code> array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind,
		int startIndex) {

		if (ArrayUtils.isEmpty(array))
			return INDEX_NOT_FOUND;
		if (startIndex < 0)
			return INDEX_NOT_FOUND;
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--)
			if (valueToFind == array[i])
				return i;
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns <code>false</code> if a <code>null</code> array is
	 * passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return <code>true</code> if the array contains the object
	 */
	public static boolean contains(boolean[] array, boolean valueToFind) {

		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// Primitive/Object array converters
	// ----------------------------------------------------------------------

	// Character array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Characters to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Character</code> array, may be <code>null</code>
	 * @return a <code>char</code> array, <code>null</code> if null array input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static char[] toPrimitive(Character[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_CHAR_ARRAY;
		final char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].charValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Character to primitives handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Character</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return a <code>char</code> array, <code>null</code> if null array input
	 */
	public static char[] toPrimitive(Character[] array, char valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_CHAR_ARRAY;
		final char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			Character b = array[i];
			result[i] = b == null ? valueForNull : b.charValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive chars to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>char</code> array
	 * @return a <code>Character</code> array, <code>null</code> if null array
	 *         input
	 */
	public static Character[] toObject(char[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		final Character[] result = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Character(array[i]);
		}
		return result;
	}

	// Long array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Longs to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Long</code> array, may be <code>null</code>
	 * @return a <code>long</code> array, <code>null</code> if null array input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static long[] toPrimitive(Long[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_LONG_ARRAY;
		final long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].longValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Long to primitives handling <code>null</code>
	 * .
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Long</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return a <code>long</code> array, <code>null</code> if null array input
	 */
	public static long[] toPrimitive(Long[] array, long valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_LONG_ARRAY;
		final long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			Long b = array[i];
			result[i] = b == null ? valueForNull : b.longValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive longs to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>long</code> array
	 * @return a <code>Long</code> array, <code>null</code> if null array input
	 */
	public static Long[] toObject(long[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_LONG_OBJECT_ARRAY;
		final Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Long(array[i]);
		}
		return result;
	}

	// Int array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Integers to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Integer</code> array, may be <code>null</code>
	 * @return an <code>int</code> array, <code>null</code> if null array input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static int[] toPrimitive(Integer[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_INT_ARRAY;
		final int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].intValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Integer to primitives handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Integer</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return an <code>int</code> array, <code>null</code> if null array input
	 */
	public static int[] toPrimitive(Integer[] array, int valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_INT_ARRAY;
		final int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			Integer b = array[i];
			result[i] = b == null ? valueForNull : b.intValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive ints to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array an <code>int</code> array
	 * @return an <code>Integer</code> array, <code>null</code> if null array
	 *         input
	 */
	public static Integer[] toObject(int[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_INTEGER_OBJECT_ARRAY;
		final Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Integer(array[i]);
		}
		return result;
	}

	// Short array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Shorts to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Short</code> array, may be <code>null</code>
	 * @return a <code>byte</code> array, <code>null</code> if null array input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static short[] toPrimitive(Short[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_SHORT_ARRAY;
		final short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].shortValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Short to primitives handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Short</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return a <code>byte</code> array, <code>null</code> if null array input
	 */
	public static short[] toPrimitive(Short[] array, short valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_SHORT_ARRAY;
		final short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			Short b = array[i];
			result[i] = b == null ? valueForNull : b.shortValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive shorts to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>short</code> array
	 * @return a <code>Short</code> array, <code>null</code> if null array input
	 */
	public static Short[] toObject(short[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_SHORT_OBJECT_ARRAY;
		final Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Short(array[i]);
		}
		return result;
	}

	// Byte array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Bytes to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Byte</code> array, may be <code>null</code>
	 * @return a <code>byte</code> array, <code>null</code> if null array input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static byte[] toPrimitive(Byte[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BYTE_ARRAY;
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].byteValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Bytes to primitives handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Byte</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return a <code>byte</code> array, <code>null</code> if null array input
	 */
	public static byte[] toPrimitive(Byte[] array, byte valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BYTE_ARRAY;
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			Byte b = array[i];
			result[i] = b == null ? valueForNull : b.byteValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive bytes to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>byte</code> array
	 * @return a <code>Byte</code> array, <code>null</code> if null array input
	 */
	public static Byte[] toObject(byte[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BYTE_OBJECT_ARRAY;
		final Byte[] result = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Byte(array[i]);
		}
		return result;
	}

	// Boolean array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Booleans to primitives.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Boolean</code> array, may be <code>null</code>
	 * @return a <code>boolean</code> array, <code>null</code> if null array
	 *         input
	 * @throws NullPointerException if array content is <code>null</code>
	 */
	public static boolean[] toPrimitive(Boolean[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BOOLEAN_ARRAY;
		final boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].booleanValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Booleans to primitives handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>Boolean</code> array, may be <code>null</code>
	 * @param valueForNull the value to insert if <code>null</code> found
	 * @return a <code>boolean</code> array, <code>null</code> if null array
	 *         input
	 */
	public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BOOLEAN_ARRAY;
		final boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			Boolean b = array[i];
			result[i] = b == null ? valueForNull : b.booleanValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive booleans to objects.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input
	 * array.
	 * </p>
	 * 
	 * @param array a <code>boolean</code> array
	 * @return a <code>Boolean</code> array, <code>null</code> if null array
	 *         input
	 */
	public static Boolean[] toObject(boolean[] array) {

		if (array == null)
			return null;
		else if (array.length == 0)
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		final Boolean[] result = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
		}
		return result;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(Object[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(long[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(int[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(short[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(char[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(byte[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 * @since 2.1
	 */
	public static boolean isEmpty(boolean[] array) {

		if (array == null || array.length == 0)
			return true;
		return false;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is not empty or not <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(Object[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(long[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(int[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(short[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(char[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(byte[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is not empty or not
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return <code>true</code> if the array is not empty or not
	 *         <code>null</code>
	 * @since 2.5
	 */
	public static boolean isNotEmpty(boolean[] array) {

		return array != null && array.length != 0;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new boolean[] array.
	 * @since 2.1
	 */
	public static boolean[] addAll(boolean[] array1, boolean[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		boolean[] joinedArray = new boolean[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new char[] array.
	 * @since 2.1
	 */
	public static char[] addAll(char[] array1, char[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		char[] joinedArray = new char[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new byte[] array.
	 * @since 2.1
	 */
	public static byte[] addAll(byte[] array1, byte[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new short[] array.
	 * @since 2.1
	 */
	public static short[] addAll(short[] array1, short[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		short[] joinedArray = new short[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new int[] array.
	 * @since 2.1
	 */
	public static int[] addAll(int[] array1, int[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		int[] joinedArray = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of <code>array1</code> followed
	 * by all of the elements <code>array2</code>. When an array is returned, it
	 * is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new long[] array.
	 * @since 2.1
	 */
	public static long[] addAll(long[] array1, long[] array2) {

		if (array1 == null)
			return clone(array2);
		else if (array2 == null)
			return clone(array1);
		long[] joinedArray = new long[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, true)          = [true]
	 * ArrayUtils.add([true], false)       = [true, false]
	 * ArrayUtils.add([true, false], true) = [true, false, true]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static boolean[] add(boolean[] array, boolean element) {

		boolean[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static byte[] add(byte[] array, byte element) {

		byte[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, '0')       = ['0']
	 * ArrayUtils.add(['1'], '0')      = ['1', '0']
	 * ArrayUtils.add(['1', '0'], '1') = ['1', '0', '1']
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static char[] add(char[] array, char element) {

		char[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static int[] add(int[] array, int element) {

		int[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static long[] add(long[] array, long element) {

		long[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new
	 * array.
	 * </p>
	 * 
	 * <p>
	 * The new array contains the same elements of the input array plus the
	 * given element in the last position. The component type of the new array
	 * is the same as that of the input array.
	 * </p>
	 * 
	 * <p>
	 * If the input array is <code>null</code>, a new one element array is
	 * returned whose component type is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * ArrayUtils.add(null, 0)   = [0]
	 * ArrayUtils.add([1], 0)    = [1, 0]
	 * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be
	 *            <code>null</code>
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 * @since 2.1
	 */
	public static short[] add(short[] array, short element) {

		short[] newArray = copyArrayGrow(array, 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static boolean[] copyArrayGrow(boolean[] array, int grow) {

		if (array != null) {
			boolean[] newArray = new boolean[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new boolean[grow];
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static byte[] copyArrayGrow(byte[] array, int grow) {

		if (array != null) {
			byte[] newArray = new byte[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new byte[grow];
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static char[] copyArrayGrow(char[] array, int grow) {

		if (array != null) {
			char[] newArray = new char[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new char[grow];
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static int[] copyArrayGrow(int[] array, int grow) {

		if (array != null) {
			int[] newArray = new int[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new int[grow];
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static long[] copyArrayGrow(long[] array, int grow) {

		if (array != null) {
			long[] newArray = new long[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new long[grow];
	}

	/**
	 * Returns a copy of the given array of size N greater than the argument.
	 * The last value of the array is left to the default value.
	 * 
	 * @param array The array to copy, must not be <code>null</code>.
	 * @param grow The size to grow
	 * @return A new copy of the array of size N greater than the input.
	 */
	public static short[] copyArrayGrow(short[] array, int grow) {

		if (array != null) {
			short[] newArray = new short[array.length + grow];
			System.arraycopy(array, 0, newArray, 0, array.length);
			return newArray;
		}
		return new short[grow];
	}

	/**
	 * Creates a {@code String} representation of the {@code int[]} passed. The
	 * result is surrounded by brackets ({@code &quot;[]&quot;}), each element
	 * is converted to a {@code String} via the {@link String#valueOf(int)} and
	 * separated by {@code &quot;, &quot;}. If the array is {@code null}, then
	 * {@code &quot;null&quot;} is returned.
	 * 
	 * @param array
	 *            the {@code int} array to convert.
	 * @return the {@code String} representation of {@code array}.
	 * @since 1.5
	 */
	public static String toString(int[] array) {

		if (array == null)
			return "null"; //$NON-NLS-1$
		if (array.length == 0)
			return "[]"; //$NON-NLS-1$
		StringBuffer sb = new StringBuffer(2 + array.length * 4);
		sb.append('[');
		sb.append(array[0]);
		for (int i = 1; i < array.length; i++) {
			sb.append(", "); //$NON-NLS-1$
			sb.append(array[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public static void fill(int[] array, int val) {

		for (int i = 0; i < array.length; ++i) {
			array[i] = val;
		}
	}

	public static void clear(Object[] array) {

		if (isEmpty(array))
			return;

		clear(array, 0, array.length);
	}

	public static void clear(Object[] array, int startIndex, int length) {

		if (isEmpty(array))
			return;

		for (int idx = startIndex, last = startIndex + length; idx < array.length &&
			idx < last; ++idx) {
			array[idx] = null;
		}
	}

	public static void shiftLeft(Object[] array, int shift) {

		if (isEmpty(array))
			return;

		for (int src = shift, dst = 0; src < array.length; ++src, ++dst) {
			array[dst] = array[src];
		}

		clear(array, array.length - shift, shift);
	}

	public static void shiftRight(Object[] array, int shift) {

		if (isEmpty(array))
			return;

		for (int src = array.length - shift - 1, dst = array.length - 1; src > 0; --src, --dst) {
			array[dst] = array[src];
		}

		clear(array, 0, shift);
	}

	public static Object lastOne(Object[] array) {

		if (isEmpty(array))
			return null;

		return array[array.length - 1];
	}

	public static Object lastNotNull(Object[] array) {

		Object last = lastOne(array);

		if (last == null) {

			//the last one is null, find the first not null
			int idx = indexOf(array, null);

			if (idx > 0) {
				last = array[idx - 1];
			}
		}

		return last;
	}

	public static void pushBack(Object[] array, Object obj) {

		if (isEmpty(array) || obj == null)
			return;

		int pos = array.length - 1;

		if (array[pos] == null) {
			//not full, find the first null position
			pos = indexOf(array, null);
		}
		else {
			//full, shift left
			shiftLeft(array, 1);
		}

		array[pos] = obj;
	}

	public static void pushFront(Object[] array, Object obj) {

		if (isEmpty(array) || obj == null)
			return;

		if (array[0] != null) {
			shiftRight(array, 1);
		}

		array[0] = obj;
	}
}
