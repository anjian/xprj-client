/**
 * Tiny.cn.uc.demo.components.SimpleWindow.java, 2011-4-10
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import javax.microedition.lcdui.Image;

import cn.uc.tiny.MenuBar;
import cn.uc.tiny.Resource;
import cn.uc.tiny.TitleBar;
import cn.uc.tiny.Window;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;

/**
 * Simple Window.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class SimpleWindow extends Window {

	/**
	 * @param aId
	 * @param aTitleBar
	 * @param aMenuBar
	 */
	public SimpleWindow() {

		super("SimpleWin", new TitleBar(), new MenuBar());
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aG.translate(x, y);

		aG.setBrush(Brush.createColorBrush(Color.LIGHTYELLOW));
		aG.fillRectEx(0, 0, width, height);

		Image startup = Resource.getImage(Resource.LOGO_UCWEB_STARTUP, true);

		// draw startup logo
		int x = GraphicsEx.getBoxX(startup.getWidth(), 0, width, 0,
			GraphicsEx.IN_BOX | GraphicsEx.HCENTER);
		int y = GraphicsEx.getBoxY(startup.getHeight(), 0, height, 0,
			GraphicsEx.IN_BOX | GraphicsEx.VCENTER);

		aG.drawImage(startup, x, y, GraphicsEx.LEFT_TOP);
		aG.restore();
	}
}
