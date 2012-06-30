/**
 * Tiny.cn.uc.demo.MenuDemo.java, 2011-4-12
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.lcdui.Canvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.Button;
import cn.uc.demo.components.MenuWindow;
import cn.uc.demo.components.SimpleView;
import cn.uc.tiny.Component;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.debug.Log;

/**
 * Menu demo.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class MenuDemo extends MIDlet implements EventHandler {

	private boolean startup = false;

	private int viewcolor = Color.ALICEBLUE;
	private int viewalpha = Color.OPAQUE;

	private int color = Color.SNOW;
	private int bgcolor = Color.GRAY;
	private int alpha = Color.HALF_TRANSPARENT;

	/**
	 * 
	 */
	public MenuDemo() {

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
		
		initShortcuts();

		SimpleView view = new SimpleView();
		view.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		Button button = new Button();
		button.setText("Please press me and hold!!!");
		button.setTextColor(color);
		//button.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);

		view.addComponent(button);
		button.setBounds(10, 50, 200, 50);

		MenuWindow win = new MenuWindow();
		win.setTitle("Menu");
		win.addView(view);
		win.show();
		return;
	}
	
	public void initShortcuts() {

		addShortcut(Canvas.KEY_NUM0, 0, T.SHORTCUT_RETURN_HOME);
		addShortcut(Canvas.KEY_NUM1, 0, T.SHORTCUT_MENU);
		addShortcut(Canvas.KEY_NUM2, 0, T.SHORTCUT_MOVE_UP);
		addShortcut(Canvas.KEY_NUM3, 0, T.SHORTCUT_MULTI_WIN_SWITCH);
		addShortcut(Canvas.KEY_NUM4, 0, T.SHORTCUT_SCROLL_UP);
		addShortcut(Canvas.KEY_NUM5, 0, T.SHORTCUT_CONFIRM);
		addShortcut(Canvas.KEY_NUM6, 0, T.SHORTCUT_SCROLL_DOWN);
		addShortcut(Canvas.KEY_NUM7, 0, T.SHORTCUT_BACKWARD);
		addShortcut(Canvas.KEY_NUM8, 0, T.SHORTCUT_MOVE_DOWN);
		addShortcut(Canvas.KEY_NUM9, 0, T.SHORTCUT_FORWARD);
		addShortcut(Canvas.KEY_STAR, 0, T.SHORTCUT_REFRESH);

		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM0, T.SHORTCUT_INPUT);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM1, T.SHORTCUT_NEW_BG_WIN);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM2, T.SHORTCUT_BOOKMARK);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM3, T.SHORTCUT_PRE_WIN);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM4, T.SHORTCUT_ADD_BOOKMARK);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM5, T.SHORTCUT_QUICK_SEARCH);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM6, T.SHORTCUT_CLOSE_WIN);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM7, T.SHORTCUT_FILE_MANAGE);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM8,
			T.SHORTCUT_FREESTYLE_COPY);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_NUM9,
			T.SHORTCUT_PAGE_BEGIN_END);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_STAR,
			T.SHORTCUT_NO_PHOTO_SWITCH);
		addShortcut(Canvas.KEY_POUND, Canvas.KEY_POUND,
			T.SHORTCUT_FULLSCREEN_SWITCH);

		addShortcut(0, 0, T.SHORTCUT_SYSTEM_SETTINGS);
		addShortcut(0, 0, T.SHORTCUT_POPUP_MENU);
		addShortcut(0, 0, T.SHORTCUT_PAGE_ATTRIBUTES);
		addShortcut(0, 0, T.SHORTCUT_CLEAR_RECORD);
		addShortcut(0, 0, T.SHORTCUT_DOWNLOAD_MANAGE);
		addShortcut(0, 0, T.SHORTCUT_THEME_SWITCH);
		addShortcut(0, 0, T.SHORTCUT_SWITCH_ORIENTATION);
		addShortcut(0, 0, T.SHORTCUT_SAVE_PHOTO);
		addShortcut(0, 0, T.SHORTCUT_ENABLE_PREREAD);
		addShortcut(0, 0, T.MENU_SHARE);
	}

	public void addShortcut(int a1stKey, int a2ndKey, int aCmdId) {

		CommandEx.addShortcut(a1stKey, a2ndKey, aCmdId, aCmdId);
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {
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
