/**
 * Tiny.cn.uc.util.Map.java, 2011-2-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util;

import java.util.Enumeration;


/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public interface Map {

	public int size();

	public boolean isEmpty();

	public Enumeration keys();

	public Enumeration elements();

	public boolean contains(Object value);

	public boolean containsKey(Object key);

	public Object get(Object key);

	public Object put(Object key, Object value);

	public Object remove(Object key);

	public void clear();

	public Object findEqualsValue(Object value);

	public Object putWithValueEqualityCheck(Object key, Object value);
}
