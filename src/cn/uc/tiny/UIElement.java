/**
 * Tiny.cn.uc.tiny.UIElement.java, 2011-4-15
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.ex.EventHandler;

/**
 * The most abstract concept of an UI Element.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public interface UIElement extends EventHandler, Animation {
	

	/**
	 * Returns the ui element's x location relatively to its parent container.
	 * 
	 * @return the current x coordinate of the ui element's origin
	 */
	public int getX();

	/**
	 * Returns the ui element's y location relatively to its parent container.
	 * 
	 * @return the current y coordinate of the ui element's origin
	 */
	public int getY();

	/**
	 * Returns the ui element's width.
	 * 
	 * @return the ui element's width
	 */
	public int getWidth();

	/**
	 * Returns the ui element's height.
	 * 
	 * @return the ui element's height
	 */
	public int getHeight();
}
