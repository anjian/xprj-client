/**
 * Tiny.cn.uc.demo.EventDemo.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.Button;
import cn.uc.demo.components.SimpleView;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
import cn.uc.tiny.Component.Reason;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.Platform;
import cn.uc.util.debug.Log;

/**
 * Event Demo.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class EventDemo extends MIDlet implements EventHandler, MenuSource,
	Runnable {

	private boolean startup = false;

	private int viewcolor = Color.ALICEBLUE;
	private int viewalpha = Color.OPAQUE;

	private int color = Color.SNOW;
	private int bgcolor = Color.LIGHTPINK;
	private int alpha = Color.OPAQUE;

	private int timerid = CanvasEx.INVALID_TIMER_INDEX;

	static String POST_BUTTON_ID = "post_btn";
	static String SEND_BUTTON_ID = "send_btn";
	static String TIMER_BUTTON_ID = "timer_btn";

	static final int USER_POST_EVENT = EventType.USER_DEFINED + 1;
	static final int USER_SEND_EVENT = EventType.USER_DEFINED + 2;

	/**
	 * 
	 */
	public EventDemo() {

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

		SimpleView view = new SimpleView();
		view.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		Button button = new Button();
		button.setId(POST_BUTTON_ID);
		button.setText("Post Event");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);
		button.setBounds(50, 50, 120, 50);
		view.addComponent(button);

		button = new Button();
		button.setId(SEND_BUTTON_ID);
		button.setText("Send Event");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_DOWN_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);
		button.setBounds(50, 110, 120, 50);
		view.addComponent(button);

		button = new Button();
		button.setId(TIMER_BUTTON_ID);
		button.setText("Start Timer");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_TIMER, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);
		button.setBounds(50, 170, 120, 50);
		view.addComponent(button);

		SimpleWindow win = new SimpleWindow();
		win.setMenuSource(this);
		win.setTitle("Event");
		win.addView(view);
		win.show();
		return;
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {

		Menu menu = Menu.buildWindowMenu(null, CommandEx.create(T.MENU_EXIT));
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
			}
			return true;
		} else if (aEv.isComponentAction()) {

			Popup.gToastEndAnchor = GraphicsEx.CENTER | GraphicsEx.IN_BOX;
			Button button = (Button) aEv.getActionComponent();
			String id = button.getId();

			if (POST_BUTTON_ID.equals(id)) {
				Popup toast = Popup.buildToast("Post user event after 2s.");
				toast.show();
				Platform.startThread(this, "post thread", Thread.NORM_PRIORITY);
			} else if (SEND_BUTTON_ID.equals(id)) {
				Popup toast = Popup.buildToast("Send user event after 2s " +
						"and delay 3s to the sender.");
				toast.show();
				Platform.startThread(this, "send thread", Thread.NORM_PRIORITY);
			} else if (TIMER_BUTTON_ID.equals(id)) {
				if (timerid == CanvasEx.INVALID_TIMER_INDEX) {

					timerid = CanvasEx.findAvailableTimer();
					CanvasEx.startTimer(timerid, this, 2000);

					button.setText("Stop Timer");
					button.repaint(Reason.UPDATE);

					Popup toast = Popup.buildToast("Start timer with 2s delay.");
					toast.show();
				} else {

					CanvasEx.stopTimer(timerid);
					timerid = CanvasEx.INVALID_TIMER_INDEX;

					button.setText("Start Timer");
					button.repaint(Reason.UPDATE);

					Popup toast = Popup.buildToast("Stop timer.");
					toast.show();
				}
			}
			return true;
		} else if (aEv.type == USER_POST_EVENT) {
			Popup.gToastEndAnchor = GraphicsEx.LEFT_TOP | GraphicsEx.IN_BOX;
			Popup toast = Popup.buildToast(aEv.param.toString());
			toast.show();
			return true;
		} else if (aEv.type == USER_SEND_EVENT) {
			Popup.gToastEndAnchor = GraphicsEx.LEFT_TOP | GraphicsEx.IN_BOX;
			Popup toast = Popup.buildToast(aEv.param.toString());
			toast.show();
			//  wait 3s and force to process events and repaint canvas
			for (int i = 0; i < 30; i++) {
				// force to process events immediately
				CanvasEx.processEvents();
				// force to repaint canvas immediately
				CanvasEx.forceRepaintCanvas();
				Platform.sleepThread(100);
			}
			return true;
		} else if (aEv.type == EventType.TIMER && aEv.getTimerId() == timerid) {

			Popup.gToastEndAnchor = GraphicsEx.LEFT_TOP | GraphicsEx.IN_BOX;
			Popup toast = Popup.buildToast("Timer has been trigger!");
			toast.show();

			Component component = CanvasEx.getCurrWindow().findComponentById(
				TIMER_BUTTON_ID);
			if (component != null && component instanceof Button) {

				//  wait 5s and force to process events and repaint canvas
				for (int i = 0; i < 50; i++) {
					// force to process events immediately
					CanvasEx.processEvents();
					// force to repaint canvas immediately
					CanvasEx.forceRepaintCanvas();
					Platform.sleepThread(100);
				}

				// simulate a click to stop the timer
				Button button = (Button) component;
				if (button.getText().indexOf("Stop") >= 0) {
					CanvasEx.postPointerEvent(EventType.POINTER_PRESSED, button);
					CanvasEx.postPointerEvent(EventType.POINTER_RELEASED,
						button);
				} else {
					return true;
				}

				//  wait 5s and force to process events and repaint canvas
				for (int i = 0; i < 50; i++) {
					// force to process events immediately
					CanvasEx.processEvents();
					// force to repaint canvas immediately
					CanvasEx.forceRepaintCanvas();
					Platform.sleepThread(100);
				}

				// simulate more click to start the timer again
				if (button.getText().indexOf("Start") >= 0) {
					CanvasEx.postPointerEvent(EventType.POINTER_PRESSED, button);
					CanvasEx.postPointerEvent(EventType.POINTER_RELEASED,
						button);
				}
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

	/** {@inheritDoc} */
	public void run() {

		Platform.sleepThread(2000);

		String name = Thread.currentThread().toString();
		if (name.indexOf("post") >= 0) {

			Event userEv = new Event(USER_POST_EVENT, this, 0, 0,
				"This a post user event by " + toString(), true);
			CanvasEx.postEvent(userEv);
			Popup.gToastEndAnchor = GraphicsEx.RIGHT_BOTTOM | GraphicsEx.IN_BOX;
			Popup toast = Popup.buildToast("User event posted.");
			toast.show();
		} else if (name.indexOf("send") >= 0) {

			Event userEv = new Event(USER_SEND_EVENT, this, 0, 0,
				"This a send user event by " + toString(), true);
			CanvasEx.sendEvent(userEv);
			Popup.gToastEndAnchor = GraphicsEx.RIGHT_BOTTOM | GraphicsEx.IN_BOX;
			Popup toast = Popup.buildToast("User event send.");
			toast.show();
		}
	}
}
