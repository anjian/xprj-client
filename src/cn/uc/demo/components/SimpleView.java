/**
 * Tiny.cn.uc.demo.components.SimpleView.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import cn.uc.tiny.Component;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.GraphicsEx;

/**
 * Simple View.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class SimpleView extends Component {

	private Brush bgBrush = Brush.createColorBrush(Color.LIGHTPINK);
	
	/**
	 * 
	 */
	public SimpleView() {
		
		super();
		
		setAttr(Attr.SCROLL_Y_ENABLE, true);
		setAttr(Attr.SCROLLBAR_VISIBLE, true);
		setAttr(Attr.TENSILE_DRAG_ENABLE, true);
	}
	
	/** {@inheritDoc} */
	public String getClazz() {

		return "SimpleView";
	}

	public void setBackground(Brush aBgBrush) {

		bgBrush = aBgBrush;
		setAttr(Attr.OPAQUE, bgBrush.isOpaque());
	}
	
	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		aG.setBrush(bgBrush);
		aG.fillRectEx(x, y, width, height);
	}
}
