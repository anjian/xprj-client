/**
 * Tiny.cn.uc.demo.DemoWindow.java, 2010-11-25
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import java.io.IOException;
import java.util.Calendar;

import cn.uc.T;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuBar;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
import cn.uc.tiny.TitleBar;
import cn.uc.tiny.Window;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CanvasEx.Rotation;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.ImageEx;
import cn.uc.util.Platform;
import cn.uc.util.StringUtils;
import cn.uc.util.io.File;

/**
 * Window to contains lots of menus.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class MenuWindow extends Window {

	public static final String ID = "DemoWindow";

	public static final int CMD_ID_SNAPSHOT = 10000;

	static final Brush ORANGE = Brush.createColorBrush(Color.ORANGE);
	static final Brush TRANS_ORANGE = Brush.createColorBrush(
		Color.HALF_TRANSPARENT, Color.ORANGE);
	static final Brush DARKORANGE = Brush.createColorBrush(Color.DARKORANGE);
	static final Brush TRANS_DARKORANGE = Brush.createColorBrush(
		Color.HALF_TRANSPARENT, Color.DARKORANGE);
	static final Brush GOLD = Brush.createColorBrush(Color.GOLD);
	static final Brush PALEGOLDENROD = Brush.createColorBrush(Color.PALEGOLDENROD);
	static final Brush ANTIQUEWHITE = Brush.createColorBrush(Color.ANTIQUEWHITE);
	static final Brush TRANS_ANTIQUEWHITE = Brush.createColorBrush(
		Color.HALF_TRANSPARENT, Color.ANTIQUEWHITE);
	static final Brush TRANS_LIGHTSKYBLUE = Brush.createColorBrush(
		Color.HALF_TRANSPARENT, Color.LIGHTSKYBLUE);

	public static CommandEx createCommand(int aTxtId) {

		return CommandEx.create(aTxtId, Resource.getText(aTxtId));
	}

	public static CommandEx createGroup(int aTxtId) {

		return CommandEx.createGroup(aTxtId, Resource.getText(aTxtId));
	}

	public MenuWindow() {

		super(ID, new TitleBar(), new MenuBar());
	}

	public MenuWindow(TitleBar aTitleBar, MenuBar aMenuBar) {

		super(ID, aTitleBar, aMenuBar);
	}

	public Menu getWindowMenu() {

		Menu menu = super.getWindowMenu();

		if (menu != null)
			return menu;

		CommandEx[] commands = new CommandEx[] { createGroup(T.MENU_OPEN),
			createGroup(T.MENU_WINDOW), createGroup(T.MENU_NAVIGATION),
			createGroup(T.MENU_TOOLS), createGroup(T.MENU_FILE),
			createGroup(T.MENU_SETTINGS), createGroup(T.MENU_HELP),
			CommandEx.create(CMD_ID_SNAPSHOT, "ÆÁÄ»½ØÍ¼"),
			createCommand(T.MENU_EXIT) };

		menu = Menu.buildWindowMenu("DemoMenu", CommandEx.CMD_GROUP_MENU,
			createCommand(T.MENU_EXIT), commands).setOwner(this);

		return menu;
	}

	public Menu getSubMenu(CommandEx aGroup) {

		CommandEx[] commands = null;

		switch (aGroup.getId()) {

		case T.MENU_OPEN:

			commands = new CommandEx[] { createCommand(T.MENU_HOME_PAGE),
				createCommand(T.MENU_BOOKMARK), createCommand(T.MENU_WEBSITE),
				createCommand(T.MENU_SEARCH), createCommand(T.MENU_HISTORY),
				createCommand(T.FILE_SYS_SAVED_PAGE) };

			break;

		case T.MENU_WINDOW:

			commands = new CommandEx[] {
				createCommand(T.MENU_CREATE_NEW_WINDOW),
				createCommand(T.MENU_OPEN_IN_BACKGROUND),
				createCommand(T.MENU_SWITCH_RIGHT),
				createCommand(T.MENU_SWITCH_LEFT),
				createCommand(T.MENU_CLOSE_CURRENT),
				createCommand(T.MENU_CLOSE_OTHERS) };

			break;

		case T.MENU_NAVIGATION:

			commands = new CommandEx[] { createCommand(T.MENU_ADD_BOOKMARK),
				createCommand(T.MENU_FORWARD), createCommand(T.MENU_BACKWARD),
				createCommand(T.MENU_REFRESH),
				createCommand(T.MENU_SHORTCUT_MENU) };

			break;

		case T.MENU_TOOLS:

			commands = new CommandEx[] { createGroup(T.MENU_COPY),
				createGroup(T.MENU_SHARE), createGroup(T.MENU_CELLPHONE),
				createCommand(T.MENU_CLEAR_RECORD),
				createCommand(T.COMMON_CLIBORD),
				createCommand(T.MENU_FIND_IN_PAGE),
				createCommand(T.COMMON_PAGE_PROPERTY) };

			break;

		case T.MENU_COPY:

			commands = new CommandEx[] { createCommand(T.MENU_FREESTYLE_COPY),
				createCommand(T.MENU_SELECTED_TEXT),
				createCommand(T.MENU_SELECTED_LINK),
				createCommand(T.MENU_CURRENT_SCREEN),
				createCommand(T.MENU_WHOLE_PAGE) };

			break;

		case T.MENU_SHARE:

			commands = new CommandEx[] {
				createCommand(T.MENU_SHARED_BY_WEIBLOG),
				createCommand(T.MENU_SHARED_BY_SHORT_MESSAGE) };

			break;

		case T.MENU_CELLPHONE:

			commands = new CommandEx[] {
				createCommand(T.MENU_SEND_SHORT_MESSAGE),
				createCommand(T.MENU_DIALUP) };

			break;

		case T.MENU_FILE:

			commands = new CommandEx[] { createCommand(T.MENU_DOWNLOAD_MANAGE),
				createCommand(T.MENU_FILE_MANAGE),
				createCommand(T.MENU_SAVE_AS),
				createCommand(T.MENU_SHOW_PHOTOS),
				createCommand(T.MENU_SAVE_PHOTO),
				createCommand(T.FILE_SYS_SAVE_PAGE),
				createCommand(T.MENU_SAVE_TO_NETDISK) };

			break;

		case T.MENU_SETTINGS:

			commands = new CommandEx[] { createCommand(T.MENU_SYSTEM_SETTINGS),
				createCommand(T.MENU_SHORTCUT_KEYS),
				createCommand(T.MENU_AUTO_REFRESH),
				createGroup(T.MENU_SWITCH_SKIN),
				createGroup(T.MENU_OPERATION_MODE),
				createGroup(T.MENU_FONT_SIZE),
				createCommand(T.MENU_RESTORE_DEFAULT),
				createCommand(T.MENU_SWITCH_ORIENTATION) };

			break;

		case T.MENU_SWITCH_SKIN:

			commands = new CommandEx[] { createCommand(T.SKIN_DEFAULT),
				createCommand(T.SKIN_NIGHT),
				createCommand(T.COMMON_SKIN_MANAGE),
				createCommand(T.COMMON_DOWNLOAD_THEME) };

			break;

		case T.MENU_OPERATION_MODE:

			commands = new CommandEx[] { createCommand(T.MENU_COMMON_MODE),
				createCommand(T.MENU_MOUSE_MODE) };

			break;

		case T.MENU_FONT_SIZE:

			commands = new CommandEx[] { createCommand(T.MENU_LARGE_FONT),
				createCommand(T.MENU_MIDDLE_FONT),
				createCommand(T.MENU_SMALL_FONT) };

			break;

		case T.MENU_HELP:

			commands = new CommandEx[] {
				createCommand(T.UPDATE_CHECK_CHECK_UPDATES),
				createCommand(T.MENU_CHECK_NETWORK),
				createCommand(T.MENU_HELP_DISCRIPTION),
				createCommand(T.COMMON_RECOMMEND_FRIEND),
				createCommand(T.MENU_REPORT_WEBSITE),
				createCommand(T.MENU_FEEDBACK), createCommand(T.MENU_ABOUT) };

			break;
		}

		return Menu.buildSubMenu("SubMenu-" + aGroup.getLabel(), commands);
	}

	public void actionEvent(Event aActEv) {

		if (!aActEv.isCommandAction())
			return;

		int cmdId = aActEv.getCommandId();

		EventHandler target = aActEv.target;
		switch (cmdId) {

		case T.MENU_EXIT: {

			Popup.gDialogStayDuration = 0;
			Popup.buildSimpleDialog(Popup.EXIT_DIALOG, "Exit",
				"Do you want to exit the Demo?", CommandEx.CMD_CONFIRM,
				CommandEx.CMD_CANCEL).show();
			break;
		}

		case CommandEx.CMD_ID_CONFIRM: {
			CanvasEx.exit();
			break;
		}

		case CMD_ID_SNAPSHOT: {
			snapshot();
			break;
		}

		case CommandEx.CMD_ID_SWITCH:
		case T.MENU_SWITCH_LEFT: {
			switchPreviousView(true);
			break;
		}

		case T.MENU_SWITCH_RIGHT: {
			switchNextView(true);
			break;
		}

		case T.SHORTCUT_FULLSCREEN_SWITCH: {
			switchFullScreen();
			break;
		}

		case T.MENU_SWITCH_ORIENTATION: {
			if (CanvasEx.isDisplayRotated()) {
				CanvasEx.setDisplayRotation(Rotation.ROTATE_0);
			} else {
				CanvasEx.setDisplayRotation(Rotation.ROTATE_90);
			}
			break;
		}

		case T.MENU_LARGE_FONT: {
			FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
				FontEx.SIZE_LARGE));
			layout();
			break;
		}

		case T.MENU_MIDDLE_FONT: {
			FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
				FontEx.SIZE_MEDIUM));
			layout();
			break;
		}

		case T.MENU_SMALL_FONT: {
			FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
				FontEx.SIZE_SMALL));
			layout();
			break;
		}

		}// switch

		aActEv.accept();
	}

	public void keyLongPressed(Event aKeyEv) {

		if (aKeyEv.isStarKey()) {

			snapshot();
			aKeyEv.accept();
			return;
		}

		super.keyLongPressed(aKeyEv);
	}

	protected void snapshot() {

		File file = null;
		try {

			Calendar now = Calendar.getInstance();
			String root = File.getRootPath();
			StringBuffer name = new StringBuffer();
			name.append("file:///").append(root).append("/").append(
				now.get(Calendar.YEAR)).append("-").append(
				now.get(Calendar.MONTH) + 1).append("-").append(
				now.get(Calendar.DAY_OF_MONTH)).append("-").append(
				now.get(Calendar.HOUR_OF_DAY)).append("-").append(
				now.get(Calendar.MINUTE)).append("-").append(
				now.get(Calendar.SECOND)).append(".bmp");

			ImageEx snapshot = getSnapshot();
			snapshot.save(name.toString());

			Popup.buildToast("½ØÍ¼³É¹¦£¬" + name.toString()).show();
		} catch (IOException e) {
			CanvasEx.postErrorEvent(Event.UNDEFINED_ERROR_ID, e);
		} catch (Throwable t) {
			CanvasEx.postErrorEvent(Event.UNDEFINED_ERROR_ID, t);
		} finally {
			Platform.closeFile(file);
		}
	}
}
