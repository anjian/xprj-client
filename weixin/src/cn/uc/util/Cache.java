/**
 * Tiny.cn.uc.util.Cache.java, 2010-12-29
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import cn.uc.build.Config;
import cn.uc.util.debug.Assert;

/**
 * Used to cache Objects.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Cache {

	public static Cache createMapCache(int aCapacity) {

		return new Cache(aCapacity, true);
	}

	public static Cache createListCache(int aCapacity) {

		return new Cache(aCapacity, false);
	}

	private final Object[] cache;
	private final boolean asMap;

	private int cursor = -1;
	private int hit = 0;
	private int unhit = 0;

	private Cache(int aCapacity, boolean aAsMap) {

		asMap = aAsMap;
		cache = aAsMap ? new Object[aCapacity * 2] : new Object[aCapacity];
	}

	public boolean isEmpty() {

		return cursor < 0;
	}

	public boolean isMap() {

		return asMap;
	}

	public boolean isList() {

		return !asMap;
	}

	public void clear() {

		for (int i = cache.length; i-- != 0;) {
			cache[i] = null;
		}

		cursor = -1;
	}

	public Object get(Object aKey) {

		for (int i = 0, step = isMap() ? 2 : 1; i < cache.length &&
			cache[i] != null; i += step) {

			if (cache[i].equals(aKey))
				return hit(i + step - 1);
		}

		return unhit();
	}

	public Object get(int aHashCode) {

		for (int i = 0, step = isMap() ? 2 : 1; i < cache.length &&
			cache[i] != null; i += step) {

			if (cache[i].hashCode() == aHashCode)
				return hit(i + step - 1);
		}

		return unhit();
	}

	public Object get(String aId) {

		for (int i = 0, step = isMap() ? 2 : 1; i < cache.length &&
			cache[i] != null; i += step) {

			if (cache[i].toString().equals(aId))
				return hit(i + step - 1);
		}

		return unhit();
	}

	public void put(Object aKey, Object aCacheObj) {

		Assert.assertTrue(isMap(), Assert.ARG);

		stepCursor();

		cache[cursor++] = aKey;
		cache[cursor] = aCacheObj;
	}

	public void put(Object aCacheObj) {

		Assert.assertTrue(isList(), Assert.ARG);

		stepCursor();

		cache[cursor] = aCacheObj;
	}

	private Object hit(int aIdx) {

		++hit;
		return cache[aIdx];
	}

	private Object unhit() {

		++unhit;
		return null;
	}

	private void stepCursor() {

		cursor = cursor < cache.length ? cursor + 1 : 0;
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Cache [asMap=");
		sBuffer.append(asMap);
		sBuffer.append(", cursor=");
		sBuffer.append(cursor);
		sBuffer.append(", hit=");
		sBuffer.append(hit);
		sBuffer.append(", unhit=");
		sBuffer.append(unhit);
		sBuffer.append(", cache=");
		sBuffer.append(cache);
		sBuffer.append("]");
		return sBuffer.toString();
	}
}
