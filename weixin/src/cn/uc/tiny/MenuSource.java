/**
 * Tiny.cn.uc.tiny.MenuOwner.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.tiny.ex.CommandEx;

/**
 * Source of menu, can set a external menu for a Window, component itself also a
 * menu source.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 * @see {@link Window#setMenuSource(MenuSource)}
 */
public interface MenuSource {

	public Menu getWindowMenu();

	public Menu getContextMenu(Component aContextCmp, int aX, int aY);

	public Menu getSubMenu(CommandEx aGroup);
}
