/**
 * Tiny.cn.uc.demo.SimpleWindowDemo.java, 2011-4-10
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.debug.Log;

/**
 * Simple Window Demo.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class SimpleWindowDemo extends MIDlet implements EventHandler,
	MenuSource {

	static String txtAbout = "This is a Simple Window Demo.\n\n"
		+ "Click 'Exit' to exit the application.";

	private boolean startup = false;

	/**
	 * 
	 */
	public SimpleWindowDemo() {
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

		SimpleWindow win = new SimpleWindow();
		win.setMenuSource(this);
		win.setTitle("SimpleWin");
		win.show();
		return;
	}

	/** {@inheritDoc} */
	protected void pauseApp() {
	}

	/** {@inheritDoc} */
	protected void destroyApp(boolean aParamBoolean)
		throws MIDletStateChangeException {
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {

		CommandEx cmdAbout = CommandEx.create(T.MENU_ABOUT);
		CommandEx cmdExit = CommandEx.create(T.MENU_EXIT);
		Menu menu = Menu.buildWindowMenu(cmdAbout, cmdExit);
		menu.setOwner(this);
		return menu;
	}

	/** {@inheritDoc} */
	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {
		return null;
	}

	/** {@inheritDoc} */
	public Menu getSubMenu(CommandEx aGroup) {
		return null;
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {

		if (aEv.isCommandAction()) {
			if (aEv.getCommandId() == T.MENU_EXIT) {
				CanvasEx.exit();
			} else if (aEv.getCommandId() == T.MENU_ABOUT) {
				Popup dlgAbout = Popup.buildSimpleDialog(
					Resource.getText(T.MENU_ABOUT),
					Resource.getText(T.MENU_ABOUT), txtAbout,
					CommandEx.CMD_CONFIRM, CommandEx.CMD_CANCEL);
				dlgAbout.show();
			}
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
}
