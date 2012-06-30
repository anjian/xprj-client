/**
 * Tiny.cn.uc.util.ArrayMap.java, 2010-12-29
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * ArrayMap, Map implementation with array inside.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class ArrayMap implements Map {

	private static final int DEFAULT_CAPACITY = 4;
	private static final int DEFAULT_INCREASE_PERCENTAGE_FACTOR = 50;

	class ArrayMapEnumerator implements Enumeration {

		private int cursor;

		public ArrayMapEnumerator(boolean enumKey) {

			cursor = enumKey ? 0 : 1;
		}

		/** {@inheritDoc} */
		public boolean hasMoreElements() {

			for (int i = cursor, len = getSearchLength(); i < len; i += 2) {
				if (entries[i] != null)
					return true;
			}
			return false;
		}

		/** {@inheritDoc} */
		public Object nextElement() {

			if (hasMoreElements()) {

				for (int len = getSearchLength(); cursor < len; cursor += 2) {

					Object element = entries[cursor];
					if (element != null) {
						cursor += 2;// point to next element
						return element;
					}
				}
			}

			throw new NoSuchElementException();
		}
	}

	private int size;
	private int holeNum;
	private Object[] entries;
	private int[] hashs;

	public ArrayMap(int initialCapacity, boolean cacheHash) {

		if (initialCapacity < 0)
			throw new IllegalArgumentException();

		if (initialCapacity == 0) {
			initialCapacity = 1;
		}

		entries = new Object[initialCapacity * 2];

		if (cacheHash) {
			hashs = new int[initialCapacity];
		}
	}

	public ArrayMap() {

		this(DEFAULT_CAPACITY, true);
	}

	public int size() {

		return size;
	}

	public boolean isEmpty() {

		return size == 0;
	}

	public Enumeration keys() {

		return new ArrayMapEnumerator(true);
	}

	public Enumeration elements() {

		return new ArrayMapEnumerator(false);
	}

	public boolean contains(Object value) {

		return findEqualsValue(value) != null;
	}

	public boolean containsKey(Object key) {

		return get(key) != null;
	}

	public Object get(Object key) {

		if (key == null)
			throw new NullPointerException();

		int hashCode = key.hashCode();
		for (int i = 0, len = getSearchLength(); i < len; i += 2) {

			if (hashs != null && hashs[i / 2] != hashCode)
				continue;

			if (key.equals(entries[i])) {
				return entries[i + 1];
			}
		}

		return null;
	}

	public Object put(Object key, Object value) {

		if (key == null || value == null)
			throw new NullPointerException();

		Object oldValue = null;
		int hashCode = key.hashCode();
		for (int i = 0, len = getSearchLength(); i < len; i += 2) {

			if (hashs != null && hashs[i / 2] != hashCode)
				continue;
			
			// key already exists, just replace the old value
			if (key.equals(entries[i])) {

				oldValue = entries[i + 1];
				entries[i] = key;
				entries[i + 1] = value;
				if (hashs != null) {
					hashs[i / 2] = key.hashCode();
				}

				return oldValue;
			}
		}

		// put into a hole or append at the last position
		if (holeNum > 0) {

			for (int i = 0, len = entries.length; i < len; i += 2) {

				if (entries[i] == null) {

					entries[i] = key;
					entries[i + 1] = value;
					if (hashs != null) {
						hashs[i / 2] = key.hashCode();
					}
					break;
				}
			}

			// decrease the number of holes
			--holeNum;
		} else {

			// new key, may need expand entries first when full
			if (entries.length == size * 2) {
				expandEntries();
			}

			entries[size * 2] = key;
			entries[size * 2 + 1] = value;
			if (hashs != null) {
				hashs[size] = key.hashCode();
			}
		}

		// increase size
		++size;
		return oldValue;
	}

	public Object remove(Object key) {

		if (key == null)
			throw new NullPointerException();

		Object oldValue = null;
		int hashCode = key.hashCode();
		for (int i = 0, len = getSearchLength(); i < len; i += 2) {

			if (hashs != null && hashs[i / 2] != hashCode)
				continue;
			
			if (key.equals(entries[i])) {

				oldValue = entries[i + 1];
				entries[i] = null;
				entries[i + 1] = null;
				if (hashs != null) {
					hashs[i / 2] = 0;
				}

				// increase the number of holes
				++holeNum;
				// decrease the size
				--size;
				break;
			}
		}

		return oldValue;
	}

	public Object findEqualsValue(Object value) {

		if (value == null)
			throw new NullPointerException();

		for (int i = 1, len = getSearchLength(); i < len; i += 2) {

			if (value.equals(entries[i]))
				return entries[i];
		}

		return null;
	}

	public Object putWithValueEqualityCheck(Object key, Object value) {

		Object realPutValue = findEqualsValue(value);
		if (realPutValue == null) {
			realPutValue = value;
		}

		put(key, realPutValue);
		return realPutValue;
	}

	public void clear() {

		for (int i = 0; i < entries.length; ++i) {
			entries[i] = null;
		}
		
		if (hashs != null) {
			for (int i = 0; i < hashs.length; ++i) {
				hashs[i] = 0;
			}
		}

		size = 0;
		holeNum = 0;
	}

	private void expandEntries() {

		int oldCapacity = entries.length;
		int newCapacity = oldCapacity
			* (100 + DEFAULT_INCREASE_PERCENTAGE_FACTOR) / 100;
		newCapacity = ((newCapacity + 3) / 4) * 4;// align to 4

		Object[] newEntries = new Object[newCapacity];
		System.arraycopy(entries, 0, newEntries, 0, oldCapacity);
		entries = newEntries;

		if (hashs != null) {

			int[] newHashs = new int[newCapacity / 2];
			System.arraycopy(hashs, 0, newHashs, 0, oldCapacity / 2);
			hashs = newHashs;
		}
	}

	private int getSearchLength() {

		if (isEmpty())
			return 0;
		else if (holeNum > 0)
			return entries.length;
		else
			return size * 2;
	}

	public String toString() {
	
		int max = size() - 1;
		StringBuffer buf = new StringBuffer();
		Enumeration k = keys();
		Enumeration e = elements();
		buf.append("{");
	
		for (int i = 0; i <= max; ++i) {
			String s1 = k.nextElement().toString();
			String s2 = e.nextElement().toString();
			buf.append(s1 + "=" + s2);
			if (i < max) {
				buf.append(", ");
			}
		}
		buf.append("}");
		return buf.toString();
	}
}
