/**
 * StyleCollection.java, 2011-3-3
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.style;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * StyleCollection contains all styles for every element inside a web page.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 7.7
 * @version 1.0
 */
public final class StyleCollection {

	/** DEBUG flag, do not commit the code with TRUE flag!!! */
	public static final boolean DEBUG = Style.DEBUG;

	public static final int INVALID_STYLE_INDEX = -1;
	public static final int INVALID_ENUM_INDEX_VALUE = -1;

	/** 4 bytes for color type value. */
	public static final int VALUE_TYPE_COLOR = 4;
	/**
	 * 2 bytes for numeric or enum type value, the first 3 bits represent the
	 * unit, 0 for {@link Style#NUMBER}, 1 for {@link Style#PERCENT}, 2 for
	 * {@link Style#EM}, 3 for {@link Style#EX}, 4 for {@link Style#PX}, 5 for
	 * negative {@link Style#PX}, 6 for {@link Style#ENUM}, and CM/IN/MM/PC/PT
	 * will be convert to PX.
	 */
	public static final int VALUE_TYPE_NUMERIC_OR_ENUM = 2;
	/** 1 byte for enum or index type value. */
	public static final int VALUE_TYPE_ENUM_OR_INDEX = 1;
	/** 0 byte for unsupport property. */
	public static final int VALUE_TYPE_UNSUPPORT = 0;

	private static final int SERIAL_VERSION_UID = 0x13535230;

	private static final int MAX_NUMERIC_VALUE = 0x0000FFFF >> 3;
	private static final int MAX_NUMERIC_BITS = 16 - 3;

	/** The offset table use to locate a property in a packed byte array. */
	private static final byte[] PROPERTY_OFFSET = {

	VALUE_TYPE_COLOR, //BACKGROUND_COLOR = 0;
	VALUE_TYPE_UNSUPPORT, //BORDER_COLLAPSE = 1;
	VALUE_TYPE_UNSUPPORT, //BORDER_SPACING = 2;
	VALUE_TYPE_UNSUPPORT, //CAPTION_SIDE = 3;
	VALUE_TYPE_COLOR, //COLOR = 4;
	VALUE_TYPE_ENUM_OR_INDEX, //DISPLAY = 5;
	VALUE_TYPE_UNSUPPORT, //EMPTY_CELLS = 6;
	VALUE_TYPE_ENUM_OR_INDEX, //FONT_FAMILY = 7;
	VALUE_TYPE_UNSUPPORT, //FONT_STYLE = 8;
	VALUE_TYPE_UNSUPPORT, //FONT_VARIANT = 9;
	VALUE_TYPE_UNSUPPORT, //FONT_WEIGHT = 10;
	VALUE_TYPE_UNSUPPORT, //FONT_SIZE = 11;
	VALUE_TYPE_NUMERIC_OR_ENUM, //LINE_HEIGHT = 12;
	VALUE_TYPE_ENUM_OR_INDEX, //LIST_STYLE_IMAGE = 13;
	VALUE_TYPE_UNSUPPORT, //LIST_STYLE_POSITION = 14;
	VALUE_TYPE_ENUM_OR_INDEX, //LIST_STYLE_TYPE = 15;
	VALUE_TYPE_ENUM_OR_INDEX, //TEXT_ALIGN = 16;
	VALUE_TYPE_ENUM_OR_INDEX, //TEXT_DECORATION = 17;
	VALUE_TYPE_NUMERIC_OR_ENUM, //TEXT_INDENT = 18;
	VALUE_TYPE_UNSUPPORT, //TEXT_TRANSFORM = 19;
	VALUE_TYPE_ENUM_OR_INDEX, //VISIBILITY = 20;
	VALUE_TYPE_UNSUPPORT, //WHITE_SPACE = 21;

	VALUE_TYPE_NUMERIC_OR_ENUM, //MARGIN_TOP = 22;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MARGIN_RIGHT = 23;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MARGIN_BOTTOM = 24;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MARGIN_LEFT = 25;

	VALUE_TYPE_COLOR, //BORDER_TOP_COLOR = 26;
	VALUE_TYPE_COLOR, //BORDER_RIGHT_COLOR = 27;
	VALUE_TYPE_COLOR, //BORDER_BOTTOM_COLOR = 28;
	VALUE_TYPE_COLOR, //BORDER_LEFT_COLOR = 29;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BORDER_TOP_WIDTH = 30;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BORDER_RIGHT_WIDTH = 31;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BORDER_BOTTOM_WIDTH = 32;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BORDER_LEFT_WIDTH = 33;
	VALUE_TYPE_ENUM_OR_INDEX, //BORDER_TOP_STYLE = 34;
	VALUE_TYPE_ENUM_OR_INDEX, //BORDER_RIGHT_STYLE = 35;
	VALUE_TYPE_ENUM_OR_INDEX, //BORDER_BOTTOM_STYLE = 36;
	VALUE_TYPE_ENUM_OR_INDEX, //BORDER_LEFT_STYLE = 37;

	VALUE_TYPE_NUMERIC_OR_ENUM, //PADDING_TOP = 38;
	VALUE_TYPE_NUMERIC_OR_ENUM, //PADDING_RIGHT = 39;
	VALUE_TYPE_NUMERIC_OR_ENUM, //PADDING_BOTTOM = 40;
	VALUE_TYPE_NUMERIC_OR_ENUM, //PADDING_LEFT = 41;

	VALUE_TYPE_NUMERIC_OR_ENUM, //WIDTH = 42;
	VALUE_TYPE_NUMERIC_OR_ENUM, //HEIGHT = 43;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MIN_WIDTH = 44;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MAX_WIDTH = 45;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MIN_HEIGHT = 46;
	VALUE_TYPE_NUMERIC_OR_ENUM, //MAX_HEIGHT = 47;

	VALUE_TYPE_NUMERIC_OR_ENUM, //BACKGROUND_POSITION_X = 48;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BACKGROUND_POSITION_Y = 49;
	VALUE_TYPE_ENUM_OR_INDEX, //BACKGROUND_REPEAT = 50;
	VALUE_TYPE_ENUM_OR_INDEX, //BACKGROUND_IMAGE = 51;

	VALUE_TYPE_ENUM_OR_INDEX, //CLEAR = 52;
	VALUE_TYPE_ENUM_OR_INDEX, //FLOAT = 53;
	VALUE_TYPE_ENUM_OR_INDEX, //OVERFLOW = 54;
	VALUE_TYPE_ENUM_OR_INDEX, //POSITION = 55;
	VALUE_TYPE_NUMERIC_OR_ENUM, //TOP = 56;
	VALUE_TYPE_NUMERIC_OR_ENUM, //RIGHT = 57;
	VALUE_TYPE_NUMERIC_OR_ENUM, //BOTTOM = 58;
	VALUE_TYPE_NUMERIC_OR_ENUM, //LEFT = 59;
	VALUE_TYPE_ENUM_OR_INDEX, //VERTICAL_ALIGN = 60;

	VALUE_TYPE_UNSUPPORT, //Z_INDEX = 61;
	VALUE_TYPE_UNSUPPORT, //CLIP = 62;
	VALUE_TYPE_UNSUPPORT, //TABLE_LAYOUT = 63;

	0, // 64, for the whole length
	};

	/** The initial value of each property. */
	private final static byte[] INITIAL_STYLE_DATA;

	private final static byte[] VALUE_TYPE_NUMERIC_OR_ENUM_UNITS = new byte[] {
	Style.NUMBER, Style.PERCENT, Style.EM, Style.EX, Style.PX, Style.PX,
	Style.ENUM };

	static {

		//convert the value type to offset of each property
		int offset = 0, count = PROPERTY_OFFSET.length;
		for (int i = 0, bytes = 0; i < count; ++i, offset += bytes) {

			bytes = PROPERTY_OFFSET[i];
			PROPERTY_OFFSET[i] = (byte) offset;
		}

		//the last offset is the total length of full style data
		INITIAL_STYLE_DATA = new byte[offset];

		//create the initial style data
		StyleCollection collection = new StyleCollection();
		collection.pack(Style.EMPTY_STYLE, Style.PROPERTY_COUNT - 1,
			INITIAL_STYLE_DATA);
	}

	/** The property offset table used in this styles collection. */
	private final byte[] offsets = PROPERTY_OFFSET;

	/** Storage of all styles. */
	private Vector styles = new Vector();

	/**
	 * Pack a new style into the collection and return the corresponding index.
	 * 
	 * @param aStyle a given style object
	 * @return the index to present the given style in the collection
	 */
	public int pack(Style aStyle) {

		if (aStyle == null || aStyle.isEmpty())
			return add(INITIAL_STYLE_DATA);

		byte[] data = new byte[offsets[aStyle.lastSet + 1]];

		pack(aStyle, aStyle.lastSet, data);

		return add(data);
	}

	/**
	 * Add a already packed style data into collection.
	 * 
	 * @param aStyleData packed style data
	 * @return the index of the data inside collection
	 */
	public int add(byte[] aStyleData) {

		//find same style data already exists?
		byte[] data;

		loop: for (int i = styles.size(); i-- > 0;) {

			data = getStyleData(i);

			if (data == aStyleData)
				return i;

			if (data.length == aStyleData.length) {

				for (int j = data.length; j-- > 0;) {

					if (data[j] != aStyleData[j])
						continue loop;
				}

				//found same
				return i;
			}
		}

		//append new style data
		styles.addElement(aStyleData);
		return styles.size() - 1;
	}

	/**
	 * Clear the collection, remove all style data.
	 */
	public void clear() {

		styles.removeAllElements();
	}

	/**
	 * Get the total length of all styles data.
	 */
	public int getTotalDataLength() {

		int total = 0;
		for (int i = getStyleCount(); i-- > 0;) {

			total += getStyleData(i).length;
		}

		return total;
	}

	/**
	 * Get the count of styles inside the collection.
	 * 
	 * @return count of styles
	 */
	public int getStyleCount() {

		return styles.size();
	}

	/**
	 * Get the pack data of specified style.
	 * 
	 * @param aStyleIdx the index of specified style
	 * @return pack style data
	 */
	public byte[] getStyleData(int aStyleIdx) {

		return (byte[]) styles.elementAt(aStyleIdx);
	}

	/**
	 * Get the value of specified property of specified style.
	 * 
	 * @param aStyleIdx the index of specified style
	 * @param aPropertyId the id of specified property
	 * @return the property value
	 * @throws IllegalArgumentException when the property not support
	 */
	public int getValue(int aStyleIdx, int aPropertyId) {

		int type = getValueType(aPropertyId);

		if (type == VALUE_TYPE_UNSUPPORT)
			throw new IllegalArgumentException();

		byte[] data = getStyleData(aStyleIdx);
		int offset = offsets[aPropertyId];

		//use initial style data when the property not contained
		if (offset >= data.length)
			data = INITIAL_STYLE_DATA;

		switch (type) {

		case VALUE_TYPE_COLOR:
			return data[offset] << 24 | (data[offset + 1] & 0xFF) << 16 |
				(data[offset + 2] & 0xFF) << 8 | data[offset + 3] & 0xFF;

		case VALUE_TYPE_NUMERIC_OR_ENUM:
			int u = (data[offset] & 0xFF) >> 5;
			int v = (data[offset] & 0x1F) << 8 | data[offset + 1] & 0xFF;

			if (u == 6)
				return data[offset + 1]; //enum value
			else if (u == 5)
				return -v; //negative px
			else
				return v; //others - px, em, ex, percent, number

		case VALUE_TYPE_ENUM_OR_INDEX:
			return data[offset];

		default:
			return INVALID_ENUM_INDEX_VALUE;
		}
	}

	/**
	 * Get the value type of specified property.
	 * 
	 * @param aPropertyId the id of specified property
	 * @return the value type of property, one of {@link #VALUE_TYPE_COLOR},
	 *         {@link #VALUE_TYPE_NUMERIC_OR_ENUM},
	 *         {@link #VALUE_TYPE_ENUM_OR_INDEX}, {@link #VALUE_TYPE_UNSUPPORT}
	 */
	public int getValueType(int aPropertyId) {

		return offsets[aPropertyId + 1] - offsets[aPropertyId];
	}

	/**
	 * Is collection support to store the specified property.
	 * 
	 * @param aPropertyId the id of specified property
	 * @return true the the property is supported
	 */
	public boolean isPropertySupport(int aPropertyId) {

		return getValueType(aPropertyId) != VALUE_TYPE_UNSUPPORT;
	}

	/**
	 * Get the unit of specified property of specified style.
	 * 
	 * <p>
	 * CM/IN/MM/PC/PT will be convert to PX in advance. When the unit is NUMBER,
	 * EM or EX, the value is multiple by 100 for it properly contains
	 * fractional part.
	 * 
	 * @param aStyleIdx the index of specified style
	 * @param aPropertyId the id of specified property
	 * @return the property unit, will be one of {@link Style#ARGB},
	 *         {@link Style#NUMBER}, {@link Style#PERCENT}, {@link Style#EM},
	 *         {@link Style#EX}, {@link Style#PX}, {@link Style#INDEX},
	 *         {@link Style#ENUM} or {@link Style#UNIT_UNKNOWN} for not support
	 *         property
	 */
	public int getUnit(int aStyleIdx, int aPropertyId) {

		switch (getValueType(aPropertyId)) {

		case VALUE_TYPE_COLOR:
			return Style.ARGB;

		case VALUE_TYPE_NUMERIC_OR_ENUM:
			//the first 3 bits represent the unit of this type
			byte[] data = getStyleData(aStyleIdx);
			int offset = offsets[aPropertyId];

			//use initial style data when the property not contained
			if (offset >= data.length)
				data = INITIAL_STYLE_DATA;

			int unit = (data[offset] & 0xFF) >> 5;

			return VALUE_TYPE_NUMERIC_OR_ENUM_UNITS[unit];

		case VALUE_TYPE_ENUM_OR_INDEX:
			if (aPropertyId == Style.BACKGROUND_IMAGE ||
				aPropertyId == Style.LIST_STYLE_IMAGE ||
				aPropertyId == Style.FONT_FAMILY)
				return Style.INDEX;
			else
				return Style.ENUM;
		}

		warn("Unknown unit!!!");
		return Style.UNIT_UNKNOWN;
	}

	/**
	 * Get pixel value of property.
	 * 
	 * <p>
	 * Use {@link #getPx(int, int, int)} for unit {@link Style#PERCENT} or
	 * {@link Style#NUMBER}.
	 * 
	 * @param aStyleIdx the index of specified style
	 * @param aPropertyId the id of specified property
	 * @return pixel value of property, when the property value's unit can not
	 *         convert to pixel directly, will return 0
	 * @see #getPx(int, int, int)
	 */
	public int getPx(int aStyleIdx, int aPropertyId) {

		int u = getUnit(aStyleIdx, aPropertyId);
		int v = getValue(aStyleIdx, aPropertyId);

		switch (u) {
		case Style.PX:
			return v;

		case Style.EM:
			return em2Px(aPropertyId, getValue(aStyleIdx, Style.FONT_FAMILY), v);

		case Style.EX:
			return ex2Px(aPropertyId, getValue(aStyleIdx, Style.FONT_FAMILY), v);

		default:
			warn("The unit can not convert to pixel directly!!!");
			break;
		}

		return 0;
	}

	/**
	 * Get pixel value of property.
	 * 
	 * @param aStyleIdx the index of specified style
	 * @param aPropertyId the id of specified property
	 * @param aBase the base value for property value with unit
	 *            {@link Style#PERCENT} or {@link Style#NUMBER}
	 * @return pixel value of property
	 * @see #getPx(int, int)
	 */
	public int getPx(int aStyleIdx, int aPropertyId, int aBase) {

		int u = getUnit(aStyleIdx, aPropertyId);
		int v = getValue(aStyleIdx, aPropertyId);

		if (u == Style.PERCENT)
			return aBase * v / 100;
		else if (u == Style.NUMBER)
			return aBase * v;
		else
			return getPx(aStyleIdx, aPropertyId);
	}

	private void pack(Style aStyle, int aLastProperty, byte[] aData) {

		for (int id = 0, u, v; id <= aLastProperty; ++id) {

			//need to skip the not support property?
			if (!isPropertySupport(id))
				continue;

			switch (id) {
			case Style.FONT_FAMILY:
				//font id
				setValue(aData, Style.FONT_FAMILY, getFontFamilyId(aStyle),
					Style.INDEX);
				break;

			case Style.BACKGROUND_IMAGE:
				//background image index
				setValue(aData, Style.BACKGROUND_IMAGE,
					getBackgroundImageIndex(aStyle.getBackgroundImageUrl()),
					Style.INDEX);
				break;

			case Style.LIST_STYLE_IMAGE:
				//list style image index
				setValue(aData, Style.LIST_STYLE_IMAGE,
					getListStyleImageIndex(aStyle.getListStyleImageUrl()),
					Style.INDEX);
				break;

			default:
				//other properties 
				u = aStyle.getUnit(id);
				v = aStyle.getValue(id);

				switch (u) {
				case Style.ARGB:
					setValue(aData, id, v, Style.ARGB);
					break;

				case Style.NUMBER:
				case Style.EX:
				case Style.EM:
					//the value divided by 10 in advance to avoid overflow
					setValue(aData, id, v / 10, u);
					break;

				case Style.PERCENT:
					//the percentage value divided by 1000 in advance to avoid overflow, 
					//we just keep the none-fractional part
					setValue(aData, id, v / 1000, Style.PERCENT);
					break;

				case Style.PX:
				case Style.IN:
				case Style.CM:
				case Style.MM:
				case Style.PT:
				case Style.PC:
					setValue(aData, id, toPx(u, v), Style.PX);
					break;

				case Style.ENUM:
					setValue(aData, id, v, Style.ENUM);
					break;

				default:
					setValue(aData, id, INVALID_ENUM_INDEX_VALUE, Style.ENUM);
				}//switch(u)
				break;
			}//switch(id)
		}//for
	}

	private void setValue(byte[] aData, int aId, int aValue, int aUnit) {

		int type = getValueType(aId);
		int offset = offsets[aId];

		switch (type) {

		case VALUE_TYPE_COLOR:
			aData[offset + 0] = (byte) (aValue >> 24);
			aData[offset + 1] = (byte) (aValue >> 16);
			aData[offset + 2] = (byte) (aValue >> 8);
			aData[offset + 3] = (byte) aValue;
			break;

		case VALUE_TYPE_NUMERIC_OR_ENUM:
			if (aUnit == Style.ENUM) {

				aData[offset + 0] = (byte) (6 << 5);
				aData[offset + 1] = (byte) aValue;
			}
			else {

				if (aValue > MAX_NUMERIC_VALUE) {

					warn(aValue + " large than max value!!!");
					aValue = MAX_NUMERIC_VALUE;
				}
				else if (aValue < -MAX_NUMERIC_VALUE) {

					warn(aValue + " less than max negative value!!!");
					aValue = -MAX_NUMERIC_VALUE;
				}

				if (aValue < 0 && aUnit != Style.PX) {

					warn(aValue + " less than 0, and its unit not support!!!");
					aValue = 0;
				}

				switch (aUnit) {
				case Style.PERCENT:
					aValue |= 1 << MAX_NUMERIC_BITS;
					break;

				case Style.EM:
					aValue |= 2 << MAX_NUMERIC_BITS;
					break;

				case Style.EX:
					aValue |= 3 << MAX_NUMERIC_BITS;
					break;

				case Style.PX:
					if (aValue >= 0) {

						aValue |= 4 << MAX_NUMERIC_BITS;
					}
					else {

						aValue = -aValue;
						aValue |= 5 << MAX_NUMERIC_BITS;
					}
					break;
				}

				aData[offset + 0] = (byte) (aValue >> 8);
				aData[offset + 1] = (byte) aValue;
			}
			break;

		case VALUE_TYPE_ENUM_OR_INDEX:
			aData[offset] = (byte) aValue;
			break;
		}
	}

	/**
	 * Write the whole collection into an output stream for serialization.
	 * 
	 * <p>
	 * This method do not close the given output stream, the caller need to
	 * handle this by its own.
	 * 
	 * @param aOut output stream
	 * @throws IOException
	 */
	public void write(OutputStream aOut) throws IOException {

		DataOutputStream dos = new DataOutputStream(aOut);

		dos.writeInt(SERIAL_VERSION_UID);//serial uid
		dos.writeShort(getStyleCount());
		dos.writeShort(0);//reserved

		byte[] data;
		for (int idx = 0, count = getStyleCount(); idx < count; ++idx) {

			data = getStyleData(idx);
			dos.writeShort(data.length);
			dos.write(data, 0, data.length);
		}

		dos.flush();//just flush, not close
	}

	/**
	 * Read the whole collection from an input stream for deserialization.
	 * 
	 * <p>
	 * This method do not close the given input stream, the caller need to
	 * handle this by its own.
	 * 
	 * @param aIn input stream
	 * @throws IOException
	 * @throws IllegalArgumentException when the input stream do not contains
	 *             correct data
	 */
	public void read(InputStream aIn) throws IOException,
		IllegalArgumentException {

		DataInputStream dis = new DataInputStream(aIn);

		if (dis.readInt() != SERIAL_VERSION_UID)
			throw new IllegalArgumentException();

		int count = dis.readShort();
		int reserved = dis.readShort();

		byte[] data;
		Vector v = new Vector(count);

		for (int idx = 0, len = 0; idx < count; ++idx) {

			len = dis.readShort();
			data = new byte[len];
			dis.read(data);

			v.addElement(data);
		}

		//read success
		styles = v;
	}

	public void dump(String aPrefix) {

		//#if (debug == true)
		if (!DEBUG)
			return;

		StringBuffer sb = new StringBuffer(aPrefix);
		sb.append(" Style collection (style count:").append(getStyleCount()).append(
			", total length:").append(getTotalDataLength()).append(
			", avg length:").append(getTotalDataLength() / getStyleCount()).append(
			"):");

		System.out.println(sb);

		for (int idx = 0; idx < getStyleCount(); ++idx) {

			System.out.println();
			System.out.println("[Style No." + (idx + 1) + "]");

			for (int id = 0; id < Style.PROPERTY_COUNT; id++) {

				if (isPropertySupport(id)) {

					int v = getValue(idx, id);
					int u = getUnit(idx, id);

					switch (u) {
					case Style.PX:
					case Style.PERCENT:
						v *= 1000;
						break;
					case Style.EM:
					case Style.EX:
					case Style.NUMBER:
						v *= 10;
						break;
					}

					Style.printProperty(
						"",
						id,
						v,
						u,
						Integer.toString(getValue(idx, Style.BACKGROUND_IMAGE)),
						Integer.toString(getValue(idx, Style.LIST_STYLE_IMAGE)),
						Integer.toString(getValue(idx, Style.FONT_FAMILY)));
				}
				else {

					System.out.println(Style.ID_TO_NAME_MAP.get(new Integer(id)) +
						": <NOT SUPPORT>;");
				}
			}
		}
		//#endif
	}

	private static void warn(String aWarnMsg) {

		//#if (debug == true)
		if (DEBUG)
			System.err.println(aWarnMsg);
		//#endif
	}

	private static int toPx(int aUnit, int aValue) {

		switch (aUnit) {
		case Style.PX:
			return aValue / 1000;
		case Style.IN:
			return aValue * Style.DEVICE_DPI / 1000;
		case Style.CM:
			return aValue * Style.DEVICE_DPI * 100 / 254 / 1000;
		case Style.MM:
			return aValue * Style.DEVICE_DPI * 10 / 254 / 1000;
		case Style.PT:
			return aValue * Style.DEVICE_DPI / 72 / 1000;
		case Style.PC:
			return aValue * Style.DEVICE_DPI / 6 / 1000;
		default:
			return 0;
		}
	}

	public static int getFontFamilyId(Style aStyle) {

		//TODO need to convert the font style, weight and size info to a font id 
		return INVALID_ENUM_INDEX_VALUE;
	}

	public static int em2Px(int aId, int aFontId, int aEm) {

		//TODO need to convert the em unit to px unit
		//When the unit is NUMBER, EM or EX, the value is multiple by 100 
		//for it properly contains fraction 
		return aEm;
	}

	public static int ex2Px(int aId, int aFontId, int aEx) {

		//TODO need to convert the ex unit to px unit
		//When the unit is NUMBER, EM or EX, the value is multiple by 100 for 
		//it properly contains fraction
		return aEx;
	}

	private int getBackgroundImageIndex(String aBackgroundImageUrl) {

		//TODO 
		return INVALID_ENUM_INDEX_VALUE;
	}

	private int getListStyleImageIndex(String aListStyleImageUrl) {

		//TODO
		return INVALID_ENUM_INDEX_VALUE;
	}
}
