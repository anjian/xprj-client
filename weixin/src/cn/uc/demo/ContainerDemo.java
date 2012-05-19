/**
 * Tiny.cn.uc.demo.ContainerDemo.java, 2011-4-12
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
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
 * Demo of Container.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class ContainerDemo extends MIDlet implements EventHandler, MenuSource {

	private boolean startup = false;

	private int viewcolor = Color.ALICEBLUE;
	private int viewalpha = Color.HALF_TRANSPARENT;

	private int color = Color.SNOW;
	private int bgcolor = Color.PINK;
	private int alpha = Color.OPAQUE;

	/**
	 * 
	 */
	public ContainerDemo() {

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
		// Log.addTagFilter(CanvasEx.E_TAG);
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
		GridContainer grid = new GridContainer();
		grid.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		for (int i = 0; i < 24; ++i) {

			int row = i / GridContainer.COL + 1;
			int col = i % GridContainer.COL + 1;

			Button button = new Button();
			button.setText("Button (" + row + ", " + col + ")");
			button.setTextColor(color);
			button.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW, false));
			button.setBackground(Brush.createColorBrush(alpha, bgcolor));
			button.setActionTarget(this);
			grid.addComponent(button);
		}
		win.addView(grid);
		
		grid = new GridContainer();
		grid.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		for (int i = 0; i < 12; ++i) {

			int row = i / GridContainer.COL + 1;
			int col = i % GridContainer.COL + 1;

			GridContainer g = new GridContainer();
			g.setHasBackground(false);

			for (int j = 0; j < 12; ++j) {

				row = j / GridContainer.COL + 1;
				col = j % GridContainer.COL + 1;

				Button button = new Button();
				button.setText("" + row + "," + col);
				button.setTextColor(color);
				button.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW,
					false));
				button.setBackground(Brush.createColorBrush(alpha, bgcolor));
				button.setActionTarget(this);

				g.addComponent(button);
			}
			grid.addComponent(g);
		}
		win.addView(grid);
		
		win.setMenuSource(this);
		win.setTitle("Container");
		win.show();
		return;
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {

		Menu menu = Menu.buildWindowMenu(CommandEx.create(T.MENU_SWITCH_RIGHT),
			CommandEx.create(T.MENU_EXIT));
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
			switch (aEv.getCommandId()) {

			case T.MENU_EXIT:
				CanvasEx.exit();
				break;
				
			case T.MENU_SWITCH_RIGHT:
				CanvasEx.getCurrWindow().switchNextView(true);
				break;
			}
			return true;
		} else if (aEv.isComponentAction()) {

			Popup.gToastEndAnchor = GraphicsEx.CENTER | GraphicsEx.IN_BOX;
			Button button = (Button) aEv.getActionComponent();
			Popup toast = Popup.buildToast(button.getText()
				+ " has been clicked.");
			toast.show();
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
