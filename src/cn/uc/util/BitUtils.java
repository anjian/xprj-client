/**
 * Tiny.cn.uc.util.BitUtils.java, 2010-11-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import cn.uc.util.debug.Assert;

/**
 * BitUtils is an utility class, it help to handle Bits/Bytes/Words in an
 * Integer.
 * 
 * <p>
 * <b>BitSet</b>
 * </p>
 * 
 * <p>
 * Each bit in a BitSet is a flag, usually identify a feature/attribute/state,
 * or as a switch. The following APIs can be used to handle a bitset. The
 * meaning of each method, can refer to class {@link java.util.BitSet}.
 * </p>
 * 
 * <ul>
 * <li>{@link #isEmpty(int)}
 * <li>{@link #get(int, int)}
 * <li>{@link #cardinality(int)}
 * <li>{@link #clear(int, int)}
 * <li>{@link #set(int, int)}
 * <li>{@link #set(int, int, boolean)}
 * <li>{@link #flip(int, int)}
 * <li>{@link #and(int, int)}
 * <li>{@link #andNot(int, int)}
 * <li>{@link #or(int, int)}
 * <li>{@link #xor(int, int)}
 * </ul>
 * 
 * 
 * <p>
 * <b>Words and Bytes</b>
 * </p>
 * 
 * <p>
 * Part of {@link BitUtils}'s APIs can be used to handle Words (
 * <code>short</code>) and Bytes (<code>byte</code>) inside an Integer (
 * <code>int</code>).
 * </p>
 * 
 * <p>
 * Words:
 * </p>
 * 
 * <ul>
 * <li>{@link #toInt(int, int)}
 * <li>{@link #setLow(int, int)}
 * <li>{@link #setHigh(int, int)}
 * <li>{@link #low(int)}
 * <li>{@link #high(int)}
 * <li>{@link #toShorts(int, short[], int)}
 * <li>{@link #toInt(short[], int)}
 * </ul>
 * 
 * <p>
 * Bytes:
 * </p>
 * 
 * <ul>
 * <li>{@link #toInt(int, int, int, int)}
 * <li>{@link #toShort(int, int)}
 * <li>{@link #setByte1(int, int)}
 * <li>{@link #setByte2(int, int)}
 * <li>{@link #setByte3(int, int)}
 * <li>{@link #setByte4(int, int)}
 * <li>{@link #byte1(int)}
 * <li>{@link #byte2(int)}
 * <li>{@link #byte3(int)}
 * <li>{@link #byte4(int)}
 * <li>{@link #byteN(int, int)}
 * <li>{@link #toBytes(int, byte[], int)}
 * <li>{@link #toBytes(short, byte[], int)}
 * <li>{@link #toInt(byte[], int)}
 * <li>{@link #toShort(byte[], int)}
 * <li>{@link #reverseBytes(int)}
 * <li>{@link #reverseBytes(short)}
 * <li>{@link #reverseBytes(char)}
 * <li>{@link #reverseToBytes(int, byte[], int)}
 * <li>{@link #fromReverseBytes(byte[], int)}
 * <li>{@link #reverseTo3Bytes(int, byte[], int)}
 * <li>{@link #fromReverse3Bytes(byte[], int, int)}
 * </ul>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class BitUtils {

	private static int bitmask(int aBitIdx) {
		
		return 1 << aBitIdx; 
	}
	
	public static boolean isEmpty(int aBitSet) {

		return aBitSet == 0;
	}

	public static boolean get(int aBitSet, int aBitIdx) {

		return (aBitSet & bitmask(aBitIdx)) != 0;
	}

	public static int cardinality(int aBitSet) {

		int mask = 1;
		int card = 0;

		for (int i = 0; i < 32; ++i) {

			card += aBitSet >>> i & mask;
		}

		return card;
	}

	public static int clear(int aBitSet, int aBitIdx) {

		return aBitSet & ~(1 << aBitIdx);
	}

	public static int set(int aBitSet, int aBitIdx) {

		return aBitSet | 1 << aBitIdx;
	}

	public static int set(int aBitSet, int aBitIdx, boolean aYes) {

		return aYes ? aBitSet | 1 << aBitIdx : aBitSet & ~(1 << aBitIdx);
	}

	public static int flip(int aBitSet, int aBitIdx) {

		int bitset2 = 1 << aBitIdx;

		return (aBitSet & bitset2) != 0 ? aBitSet & ~bitset2 : aBitSet &
			bitset2;
	}

	public static boolean and(int aBitSet, int aBitSet2) {

		return (aBitSet & aBitSet2) != 0;
	}

	public static boolean andNot(int aBitSet, int aBitSet2) {

		return (aBitSet & ~aBitSet2) != 0;
	}

	public static boolean or(int aBitSet, int aBitSet2) {

		return (aBitSet | aBitSet2) != 0;
	}

	public static boolean xor(int aBitSet, int aBitSet2) {

		return (aBitSet ^ aBitSet2) != 0;
	}

	public static int toInt(int aLowWord, int aHighWord) {

		Assert.assertInRangeV2(Short.MIN_VALUE, aLowWord, Short.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV2(Short.MIN_VALUE, aHighWord, Short.MAX_VALUE,
			Assert.ARG);

		return aHighWord << 16 | aLowWord & 0xFFFF;
	}

	public static int setLow(int aVal, int aLowWord) {

		return toInt(aLowWord, high(aVal));
	}

	public static int setHigh(int aVal, int aHighWord) {

		return toInt(low(aVal), aHighWord);
	}

	public static short low(int aVal) {

		return (short) aVal;
	}

	public static short high(int aVal) {

		return (short) (aVal >> 16);
	}

	public static void toShorts(int aVal, short[] aOutWords, int aOffset) {

		aOutWords[aOffset + 0] = high(aVal);
		aOutWords[aOffset + 1] = low(aVal);
	}

	public static int toInt(short[] aOutWords, int aOffset) {

		return toInt(aOutWords[aOffset + 1], aOutWords[aOffset]);
	}

	public static int toInt(int aByte1, int aByte2, int aByte3, int aByte4) {

		Assert.assertInRangeV2(Byte.MIN_VALUE, aByte1, Byte.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV2(Byte.MIN_VALUE, aByte2, Byte.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV2(Byte.MIN_VALUE, aByte3, Byte.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV2(Byte.MIN_VALUE, aByte4, Byte.MAX_VALUE,
			Assert.ARG);

		return aByte4 << 24 | (aByte3 & 0xFF) << 16 | (aByte2 & 0xFF) << 8 |
			aByte1 & 0xFF;
	}

	public static int toShort(int aLowByte, int aHighByte) {

		Assert.assertInRangeV2(Byte.MIN_VALUE, aLowByte, Byte.MAX_VALUE,
			Assert.ARG);
		Assert.assertInRangeV2(Byte.MIN_VALUE, aHighByte, Byte.MAX_VALUE,
			Assert.ARG);

		return aHighByte << 8 | aLowByte & 0xFF;
	}

	public static int setByte1(int aVal, int aByte1) {

		return toInt(aByte1, byte2(aVal), byte3(aVal), byte4(aVal));
	}

	public static int setByte2(int aVal, int aByte2) {

		return toInt(byte1(aVal), aByte2, byte3(aVal), byte4(aVal));
	}

	public static int setByte3(int aVal, int aByte3) {

		return toInt(byte1(aVal), byte2(aVal), aByte3, byte4(aVal));
	}

	public static int setByte4(int aVal, int aByte4) {

		return toInt(byte1(aVal), byte2(aVal), byte3(aVal), aByte4);
	}

	public static byte byte1(int aVal) {

		return (byte) aVal;
	}

	public static byte byte2(int aVal) {

		return (byte) (aVal >> 8);
	}

	public static byte byte3(int aVal) {

		return (byte) (aVal >> 16);
	}

	public static byte byte4(int aVal) {

		return (byte) (aVal >> 24);
	}

	public static byte byteN(int aVal, int aIdx) {

		Assert.assertInRangeV1(0, aIdx, 4, Assert.ARG);

		switch (aIdx) {
		case 0:
			return byte1(aVal);
		case 1:
			return byte2(aVal);
		case 2:
			return byte3(aVal);
		case 3:
			return byte4(aVal);
		}

		return 0;
	}

	public static void toBytes(int aVal, byte[] aOutBytes, int aOffset) {

		aOutBytes[aOffset + 0] = byte4(aVal);
		aOutBytes[aOffset + 1] = byte3(aVal);
		aOutBytes[aOffset + 2] = byte2(aVal);
		aOutBytes[aOffset + 3] = byte1(aVal);
	}

	public static void toBytes(short aVal, byte[] aOutBytes, int aOffset) {

		aOutBytes[0 + aOffset] = byte2(aVal);
		aOutBytes[1 + aOffset] = byte1(aVal);
	}

	public static int toInt(byte[] aInBytes, int aOffset) {

		return aInBytes[aOffset + 0] << 24 |
			(aInBytes[aOffset + 1] & 0xFF) << 16 |
			(aInBytes[aOffset + 2] & 0xFF) << 8 | aInBytes[aOffset + 3] & 0xFF;
	}

	public static int toShort(byte[] aInBytes, int aOffset) {

		return aInBytes[aOffset + 0] << 8 | aInBytes[aOffset + 1] & 0xFF;
	}

	/**
	 * Reverses the order of the bytes of the specified integer.
	 * 
	 * @param aVal the integer value for which to reverse the byte order.
	 * @return the reversed value.
	 */
	public static int reverseBytes(int aVal) {

		int b3 = aVal >>> 24;
		int b2 = aVal >>> 8 & 0xFF00;
		int b1 = (aVal & 0xFF00) << 8;
		int b0 = aVal << 24;
		return b0 | b1 | b2 | b3;
	}

	/**
	 * Reverses the bytes of the specified short.
	 * 
	 * @param aVal the short value for which to reverse bytes.
	 * @return the reversed value.
	 */
	public static short reverseBytes(short aVal) {

		int high = aVal >> 8 & 0xFF;
		int low = (aVal & 0xFF) << 8;
		return (short) (low | high);
	}

	/**
	 * Reverses the order of the first and second byte in the specified
	 * character.
	 * 
	 * @param c the character to reverse.
	 * @return the character with reordered bytes.
	 */
	public static char reverseBytes(char c) {

		return (char) (c << 8 | c >> 8);
	}

	public static void reverseToBytes(int aVal, byte[] aOut, int aOffset) {

		aOut[aOffset] = (byte) (aVal & 0xFF);
		aOut[aOffset + 1] = (byte) (aVal >>> 8 & 0xFF);
		aOut[aOffset + 2] = (byte) (aVal >>> 16 & 0xFF);
		aOut[aOffset + 3] = (byte) (aVal >>> 24);
	}

	public static int fromReverseBytes(byte[] aInBytes, int aOffset) {

		return toInt(aInBytes[aOffset], aInBytes[aOffset + 1],
			aInBytes[aOffset + 2], aInBytes[aOffset + 3]);
	}

	public static void reverseTo3Bytes(int aVal, byte[] aOut, int aOffset) {

		aOut[aOffset] = (byte) (aVal & 0x000000FF);
		aOut[aOffset + 1] = (byte) (aVal >>> 8 & 0x000000FF);
		aOut[aOffset + 2] = (byte) (aVal >>> 16 & 0x000000FF);
	}

	public static int fromReverse3Bytes(byte[] aInBytes, int aOffset, int aByte4) {

		return toInt(aInBytes[aOffset], aInBytes[aOffset + 1],
			aInBytes[aOffset + 2], aByte4);
	}
}
