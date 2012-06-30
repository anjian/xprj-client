/**
 * Tiny.cn.uc.demo.SimpleViewDemo.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.SimpleView;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Component.Reason;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;
import cn.uc.util.debug.Log;

/**
 * Simple View Demo.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class SimpleViewDemo extends MIDlet implements EventHandler, MenuSource {

	SimpleView view;
	private boolean startup = false;

	private int color = Color.YELLOW;
	private int alpha = Color.OPAQUE;

	static final int CMD_ID_COLOR = 1000;
	static final int CMD_ID_ALPHA = 1001;

	static final int CMD_ID_RED = 1010;
	static final int CMD_ID_GREEN = 1011;
	static final int CMD_ID_BLUE = 1012;
	static final int CMD_ID_YELLOW = 1013;
	static final int CMD_ID_ORANGE = 1014;
	static final int CMD_ID_BLACK = 1015;

	static final int CMD_ID_THREE_QUARTER_TRANSPARENT = 1020;
	static final int CMD_ID_HALF_TRANSPARENT = 1021;
	static final int CMD_ID_QUARTER_TRANSPARENT = 1022;
	static final int CMD_ID_EIGHTN_TRANSPARENT = 1023;
	static final int CMD_ID_LITTLE_TRANSPARENT = 1024;
	static final int CMD_ID_OPAQUE = 1025;

	/**
	 * 
	 */
	public SimpleViewDemo() {

	}

	/** {@inheritDoc} */
	protected void destroyApp(boolean aArg0) throws MIDletStateChangeException {
	}

	/** {@inheritDoc} */
	protected void pauseApp() {
	}

	/** {@inheritDoc} */
	protected void startApp() throws MIDletStateChangeException {

		if (startup) {
			CanvasEx.restoreCanvas();
			return;
		}

		startup = true;

		Log.addTagFilter(CanvasEx.P_TAG);
		Log.addTagFilter(CanvasEx.E_TAG);
		Log.addTagFilter(CanvasEx.T_TAG);
		Log.addTagFilter(Component.P_TAG);
		Log.addTagFilter(Component.S_TAG);
		Log.addTagFilter(GraphicsEx.TAG);
		Log.addTagFilter(Animation.TAG);
		Log.addTagFilter(Motion.TAG);

		CanvasEx.initCanvas(this);
		FontEx.initializeUsedFonts(FontEx.SUPPORT_NORMAL_FONTS);
		FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
			FontEx.SIZE_MEDIUM));

		view = new SimpleView();
		view.setBackground(Brush.createColorBrush(alpha, color));

		SimpleWindow win = new SimpleWindow();
		win.setMenuSource(this);
		win.setTitle("SimpleView");
		win.addView(view);
		win.show();
		return;
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {

		if (aEv.isCommandAction()) {

			switch (aEv.getCommandId()) {
			
			case T.MENU_EXIT:
				CanvasEx.exit();
				break;
				
			case CMD_ID_RED:
				color = Color.RED;
				break;
				
			case CMD_ID_GREEN:
				color = Color.GREEN;
				break;
				
			case CMD_ID_BLUE:
				color = Color.BLUE;
				break;
				
			case CMD_ID_YELLOW:
				color = Color.YELLOW;
				break;
				
			case CMD_ID_ORANGE:
				color = Color.ORANGE;
				break;
				
			case CMD_ID_BLACK:
				color = Color.BLACK;
				break;
				
			case CMD_ID_THREE_QUARTER_TRANSPARENT:
				alpha = Color.THREE_QUARTER_TRANSPARENT;
				break;
				
			case CMD_ID_HALF_TRANSPARENT:
				alpha = Color.HALF_TRANSPARENT;
				break;
				
			case CMD_ID_QUARTER_TRANSPARENT:
				alpha = Color.QUARTER_TRANSPARENT;
				break;
				
			case CMD_ID_EIGHTN_TRANSPARENT:
				alpha = Color.EIGHTN_TRANSPARENT;
				break;
				
			case CMD_ID_LITTLE_TRANSPARENT:
				alpha = Color.LITTLE_TRANSPARENT;
				break;
				
			case CMD_ID_OPAQUE:
				alpha = Color.OPAQUE;
				break;
			}
			
			view.setBackground(Brush.createColorBrush(alpha, color));
			view.repaint(Reason.UPDATE);
			return true;
		}

		return false;
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {
	}

	/** {@inheritDoc} */
	public void pointerEvent(Event aPtEv) {
	}

	/** {@inheritDoc} */
	public void actionEvent(Event aActEv) {
	}

	/** {@inheritDoc} */
	public void timerEvent(Event aTimerEv) {
	}

	/** {@inheritDoc} */
	public void progressEvent(Event aProgressEv) {
	}

	/** {@inheritDoc} */
	public void errorEvent(Event aErrorEv) {
	}

	/** {@inheritDoc} */
	public void sizeChanged(int aNewWidth, int aNewHeight) {
	}

	/** {@inheritDoc} */
	public void orentationChanged(int aNewOrentation) {
	}

	/** {@inheritDoc} */
	public void showNotify() {
	}

	/** {@inheritDoc} */
	public void hideNotify() {
	}

	/** {@inheritDoc} */
	public void focusGained(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void focusLost(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void onEventError(Throwable aErr) {
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {

		CommandEx[] commands = new CommandEx[] {
			CommandEx.createGroup(CMD_ID_COLOR, "选择颜色"),
			CommandEx.createGroup(CMD_ID_ALPHA, "选择透明度"), };

		Menu menu = Menu.buildWindowMenu("DemoMenu", CommandEx.CMD_GROUP_MENU,
			CommandEx.create(T.MENU_EXIT), commands).setOwner(this);

		return menu;
	}

	/** {@inheritDoc} */
	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {

		return null;
	}

	/** {@inheritDoc} */
	public Menu getSubMenu(CommandEx aGroup) {

		CommandEx[] commands = null;

		switch (aGroup.getId()) {

		case CMD_ID_COLOR:
			commands = new CommandEx[] { CommandEx.create(CMD_ID_RED, "红色"),
				CommandEx.create(CMD_ID_GREEN, "绿色"),
				CommandEx.create(CMD_ID_BLUE, "蓝色"),
				CommandEx.create(CMD_ID_YELLOW, "黄色"),
				CommandEx.create(CMD_ID_ORANGE, "橙色"),
				CommandEx.create(CMD_ID_BLACK, "黑色"), };
			break;

		case CMD_ID_ALPHA:
			commands = new CommandEx[] {
				CommandEx.create(CMD_ID_THREE_QUARTER_TRANSPARENT, "3/4透明"),
				CommandEx.create(CMD_ID_HALF_TRANSPARENT, "1/2透明"),
				CommandEx.create(CMD_ID_QUARTER_TRANSPARENT, "1/4透明"),
				CommandEx.create(CMD_ID_EIGHTN_TRANSPARENT, "1/8透明"),
				CommandEx.create(CMD_ID_LITTLE_TRANSPARENT, "一点点透明"),
				CommandEx.create(CMD_ID_OPAQUE, "不透明"), };
			break;
		}

		return Menu.buildSubMenu("SubMenu-" + aGroup.getLabel(), commands);
	}
}
