/**
 * Tiny.cn.uc.ui.animations.Animation.java, 2010-11-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.animations;

import cn.uc.tiny.ex.GraphicsEx;

/**
 * Allows any object to react to events and draw an animation at a fixed
 * interval. All animation methods are executed on the GUI thread. For
 * simplicities sake all components are animatable, however no animation
 * will appear unless it is explicitly registered into the parent Window. In
 * order to stop animation callbacks the animation must be explicitly removed
 * from the Window (notice that this differs from removing the component from
 * the Window!).
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public interface Animation {

	public static final String TAG = "ANIMATION";

	/**
	 * Allows the animation to reduce "repaint" calls when it returns false. It
	 * is called once for every repaint loop.
	 * 
	 * @return true if a repaint is desired or false if no repaint is necessary
	 */
	public boolean animate();

	/**
	 * Draws the animation, within a component the standard paint method would
	 * be invoked since it bares the exact same signature.
	 * 
	 * @param aGEx graphics context
	 */
	public void paint(GraphicsEx aGEx);
}
