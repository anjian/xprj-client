/**
 * Tiny.cn.uc.demo.PopupDemo.java, 2011-4-12
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
import cn.uc.demo.components.SimpleView;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Component.Reason;
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
 * Demo to show Popups, i.e. Toast, Dialog...
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class PopupDemo extends MIDlet implements EventHandler, MenuSource {

	static final int CMD_ID_COLOR = 1000;
	static final int CMD_ID_ALPHA = 1001;
	static final int CMD_ID_T_COLOR = 1002;
	static final int CMD_ID_V_ALPHA = 1003;
	static final int CMD_ID_V_COLOR = 1004;

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

	static final int CMD_ID_V_RED = 1050;
	static final int CMD_ID_V_GREEN = 1051;
	static final int CMD_ID_V_BLUE = 1052;
	static final int CMD_ID_V_YELLOW = 1053;
	static final int CMD_ID_V_ORANGE = 1054;
	static final int CMD_ID_V_BLACK = 1055;

	static final int CMD_ID_V_THREE_QUARTER_TRANSPARENT = 1040;
	static final int CMD_ID_V_HALF_TRANSPARENT = 1041;
	static final int CMD_ID_V_QUARTER_TRANSPARENT = 1042;
	static final int CMD_ID_V_EIGHTN_TRANSPARENT = 1043;
	static final int CMD_ID_V_LITTLE_TRANSPARENT = 1044;
	static final int CMD_ID_V_OPAQUE = 1045;

	static final int CMD_ID_T_RED = 1030;
	static final int CMD_ID_T_GREEN = 1031;
	static final int CMD_ID_T_BLUE = 1032;
	static final int CMD_ID_T_YELLOW = 1033;
	static final int CMD_ID_T_ORANGE = 1034;
	static final int CMD_ID_T_BLACK = 1035;

	SimpleView view;
	Button button;

	private boolean startup = false;

	private int viewcolor = Color.ALICEBLUE;
	private int viewalpha = Color.OPAQUE;

	private int color = Color.SNOW;
	private int bgcolor = Color.GRAY;
	private int alpha = Color.HALF_TRANSPARENT;

	/**
	 * 
	 */
	public PopupDemo() {

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
		view.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		button = new Button();
		button.setId("toast_btn");
		button.setText("Pop Toast");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);

		view.addComponent(button);
		button.setBounds(50, 50, 120, 50);

		button = new Button();
		button.setId("dialog_btn");
		button.setText("Pop Dialog");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_DOWN_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);

		view.addComponent(button);
		button.setBounds(50, 110, 120, 50);

		button = new Button();
		button.setId("complex_dialog_btn");
		button.setText("Pop Complex Dlg");
		button.setTextColor(color);
		button.setIcon(Resource.getImage(Resource.ICON_DOWN_ARROW, false));
		button.setBackground(Brush.createColorBrush(alpha, bgcolor));
		button.setActionTarget(this);

		view.addComponent(button);
		button.setBounds(50, 170, 150, 50);

		SimpleWindow win = new SimpleWindow();
		win.setMenuSource(this);
		win.setTitle("Popup");
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

		CommandEx[] commands = null;

		switch (aGroup.getId()) {

		case CMD_ID_COLOR:
			commands = new CommandEx[] { CommandEx.create(CMD_ID_RED, "��ɫ"),
				CommandEx.create(CMD_ID_GREEN, "��ɫ"),
				CommandEx.create(CMD_ID_BLUE, "��ɫ"),
				CommandEx.create(CMD_ID_YELLOW, "��ɫ"),
				CommandEx.create(CMD_ID_ORANGE, "��ɫ"),
				CommandEx.create(CMD_ID_BLACK, "��ɫ"), };
			break;

		case CMD_ID_ALPHA:
			commands = new CommandEx[] {
				CommandEx.create(CMD_ID_THREE_QUARTER_TRANSPARENT, "3/4͸��"),
				CommandEx.create(CMD_ID_HALF_TRANSPARENT, "1/2͸��"),
				CommandEx.create(CMD_ID_QUARTER_TRANSPARENT, "1/4͸��"),
				CommandEx.create(CMD_ID_EIGHTN_TRANSPARENT, "1/8͸��"),
				CommandEx.create(CMD_ID_LITTLE_TRANSPARENT, "һ���͸��"),
				CommandEx.create(CMD_ID_OPAQUE, "��͸��"), };
			break;

		case CMD_ID_T_COLOR:
			commands = new CommandEx[] { CommandEx.create(CMD_ID_T_RED, "��ɫ"),
				CommandEx.create(CMD_ID_T_GREEN, "��ɫ"),
				CommandEx.create(CMD_ID_T_BLUE, "��ɫ"),
				CommandEx.create(CMD_ID_T_YELLOW, "��ɫ"),
				CommandEx.create(CMD_ID_T_ORANGE, "��ɫ"),
				CommandEx.create(CMD_ID_T_BLACK, "��ɫ"), };
			break;

		case CMD_ID_V_COLOR:
			commands = new CommandEx[] { CommandEx.create(CMD_ID_V_RED, "��ɫ"),
				CommandEx.create(CMD_ID_V_GREEN, "��ɫ"),
				CommandEx.create(CMD_ID_V_BLUE, "��ɫ"),
				CommandEx.create(CMD_ID_V_YELLOW, "��ɫ"),
				CommandEx.create(CMD_ID_V_ORANGE, "��ɫ"),
				CommandEx.create(CMD_ID_V_BLACK, "��ɫ"), };
			break;

		case CMD_ID_V_ALPHA:
			commands = new CommandEx[] {
				CommandEx.create(CMD_ID_V_THREE_QUARTER_TRANSPARENT, "3/4͸��"),
				CommandEx.create(CMD_ID_V_HALF_TRANSPARENT, "1/2͸��"),
				CommandEx.create(CMD_ID_V_QUARTER_TRANSPARENT, "1/4͸��"),
				CommandEx.create(CMD_ID_V_EIGHTN_TRANSPARENT, "1/8͸��"),
				CommandEx.create(CMD_ID_V_LITTLE_TRANSPARENT, "һ���͸��"),
				CommandEx.create(CMD_ID_V_OPAQUE, "��͸��"), };
			break;
		}

		return Menu.buildSubMenu("SubMenu-" + aGroup.getLabel(), commands);
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {

		if (aEv.isCommandAction()) {
			switch (aEv.getCommandId()) {

			case T.MENU_EXIT:
				CanvasEx.exit();
				break;

			case CommandEx.CMD_ID_CANCEL:
				CanvasEx.getCurrWindow().closeModals();
				break;

			case CMD_ID_RED:
				bgcolor = Color.RED;
				break;

			case CMD_ID_GREEN:
				bgcolor = Color.GREEN;
				break;

			case CMD_ID_BLUE:
				bgcolor = Color.BLUE;
				break;

			case CMD_ID_YELLOW:
				bgcolor = Color.YELLOW;
				break;

			case CMD_ID_ORANGE:
				bgcolor = Color.ORANGE;
				break;

			case CMD_ID_BLACK:
				bgcolor = Color.BLACK;
				break;

			case CMD_ID_T_RED:
				color = Color.RED;
				break;

			case CMD_ID_T_GREEN:
				color = Color.GREEN;
				break;

			case CMD_ID_T_BLUE:
				color = Color.BLUE;
				break;

			case CMD_ID_T_YELLOW:
				color = Color.YELLOW;
				break;

			case CMD_ID_T_ORANGE:
				color = Color.ORANGE;
				break;

			case CMD_ID_T_BLACK:
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

			case CMD_ID_V_RED:
				viewcolor = Color.RED;
				break;

			case CMD_ID_V_GREEN:
				viewcolor = Color.GREEN;
				break;

			case CMD_ID_V_BLUE:
				viewcolor = Color.BLUE;
				break;

			case CMD_ID_V_YELLOW:
				viewcolor = Color.YELLOW;
				break;

			case CMD_ID_V_ORANGE:
				viewcolor = Color.ORANGE;
				break;

			case CMD_ID_V_BLACK:
				viewcolor = Color.BLACK;
				break;

			case CMD_ID_V_THREE_QUARTER_TRANSPARENT:
				viewalpha = Color.THREE_QUARTER_TRANSPARENT;
				break;

			case CMD_ID_V_HALF_TRANSPARENT:
				viewalpha = Color.HALF_TRANSPARENT;
				break;

			case CMD_ID_V_QUARTER_TRANSPARENT:
				viewalpha = Color.QUARTER_TRANSPARENT;
				break;

			case CMD_ID_V_EIGHTN_TRANSPARENT:
				viewalpha = Color.EIGHTN_TRANSPARENT;
				break;

			case CMD_ID_V_LITTLE_TRANSPARENT:
				viewalpha = Color.LITTLE_TRANSPARENT;
				break;

			case CMD_ID_V_OPAQUE:
				viewalpha = Color.OPAQUE;
				break;
			}

			button.setTextColor(color);
			button.setBackground(Brush.createColorBrush(alpha, bgcolor));

			view.setBackground(Brush.createColorBrush(viewalpha, viewcolor));
			view.repaint(Reason.UPDATE);
			
			Popup toast = Popup.buildToast("Color or background changed.");
			toast.show();
			return true;
		} else if (aEv.isComponentAction()) {

			Button button = (Button) aEv.getActionComponent();

			if (button.getId().equals("toast_btn")) {

				Popup.gToastEndAnchor = GraphicsEx.LEFT_TOP_IN;
				Popup toast = Popup.buildToast("I am a Toast, "
					+ "and I have been pop to left top corner, aHHH!!!");
				toast.show();

				Popup.gToastEndAnchor = GraphicsEx.RIGHT_TOP_IN;
				toast = Popup.buildToast("I am a Toast, "
					+ "and I have been pop to right top corner, aHHH!!!");
				toast.show();

				Popup.gToastEndAnchor = GraphicsEx.LEFT_BOTTOM_IN;
				toast = Popup.buildToast("I am a Toast, "
					+ "and I have been pop to left bottom corner, aHHH!!!");
				toast.show();

				Popup.gToastEndAnchor = GraphicsEx.RIGHT_BOTTOM_IN;
				toast = Popup.buildToast("I am a Toast, "
					+ "and I have been pop to right bottom corner, aHHH!!!");
				toast.show();

				Popup.gToastEndAnchor = GraphicsEx.CENTER_IN;
				toast = Popup.buildToast("I am a Toast, "
					+ "and I have been pop to center, aHHH!!!");
				toast.show();
			} else if (button.getId().equals("dialog_btn")) {

				CommandEx[] commands = new CommandEx[] {
					CommandEx.createGroup(CMD_ID_COLOR, "ѡ��ť������ɫ"),
					CommandEx.createGroup(CMD_ID_ALPHA, "ѡ��ť����͸����"),
					CommandEx.createGroup(CMD_ID_T_COLOR, "ѡ��ť�ı���ɫ"),
					CommandEx.createGroup(CMD_ID_V_COLOR, "ѡ����ͼ������ɫ"),
					CommandEx.createGroup(CMD_ID_V_ALPHA, "ѡ����ͼ����͸����"), };

				Menu menu = Menu.buildWindowMenu("DemoMenu",
					CommandEx.CMD_GROUP_MENU, CommandEx.CMD_CANCEL, commands);
				menu.setOwner(this);
				Popup dialog = Popup.buildSimpleDialog("dialog", "��ɫѡ��",
					"ѡ��ť����ͼ����ɫ", menu);
				dialog.show();
			} else if (button.getId().equals("complex_dialog_btn")) {

				GridContainer grid = new GridContainer();
				grid.setHasBackground(false);

				for (int i = 0; i < 12; ++i) {

					int row = i / GridContainer.COL + 1;
					int col = i % GridContainer.COL + 1;

					Button btn = new Button();
					btn.setText("(" + row + ", " + col + ")");
					btn.setTextColor(color);
					btn.setIcon(Resource.getImage(Resource.ICON_RIGHT_ARROW,
						false));
					btn.setBackground(Brush.createColorBrush(alpha, bgcolor));
					btn.setActionTarget(this);
					grid.addComponent(btn);
				}

				Popup dialog = Popup.buildComplexDialog("complex_dialog",
					"������ĶԻ���", grid, Menu.buildWindowMenu(
						CommandEx.CMD_CONFIRM, CommandEx.CMD_CANCEL));
				dialog.show();
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