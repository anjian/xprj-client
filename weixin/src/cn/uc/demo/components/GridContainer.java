/**
 * Tiny.cn.uc.demo.components.GridContainer.java, 2011-4-12
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import cn.uc.tiny.Component;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.debug.Log;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class GridContainer extends Component {

	public static final String TAG = "GridContainer";
	
	public static final int COL = 2;
	public static final int ROW_H = 100;
	public static final int SPACING = 10;

	private Brush bgBrush = Brush.createColorBrush(Color.LIGHTPINK);

	/**
	 * 
	 */
	public GridContainer() {

		super();
		
		setAttr(Attr.SCROLL_Y_ENABLE, true);
		setAttr(Attr.SCROLLBAR_VISIBLE, true);
		setAttr(Attr.TENSILE_DRAG_ENABLE, true);
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return "Grid";
	}
	
	/** {@inheritDoc} */
	public int getScrollbarClickableLength() {

		return 10;
	}
	
	public void setBackground(Brush aBgBrush) {

		bgBrush = aBgBrush;
		setAttr(Attr.OPAQUE, bgBrush.isOpaque());
	}

	/** {@inheritDoc} */
	public void layout() {

		Log.d(TAG, "Grid container layout...");
		
		int colW = (width - (COL + 1) * SPACING) / COL;
		int rowH = ROW_H;

		int xoff, yoff = 0;
		int row = 0, col = 0;
		int count = getComponentCount();
		for (int i = 0; i < count; ++i, row = i / COL, col = i % COL) {

			xoff = colW * col + (col + 1) * SPACING;
			yoff = rowH * row + (row + 1) * SPACING;
			Component c = getComponentAt(i);
			c.setBounds(xoff, yoff, colW, rowH);
		}

		super.layout();
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		aG.setBrush(bgBrush);
		aG.fillRectEx(x, y, width, height);
	}
}
