/**
 * Tiny.cn.uc.ui.Menu.java, 2010-12-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import india.xxoo.ui.listAdapt.ListAdapter;

import javax.microedition.lcdui.Canvas;

import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Assert;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Menu extends Popup implements ListAdapter {

	private static final int GROUP_TRIANGLE_LEFT_MARGIN = 5;
	private static final int GROUP_TRIANGLE_RIGHT_MARGIN = 1;
	private static final int GROUP_TRIANGLE_WIDTH = 10;

	/****************************************************************/
	/*                                                              */
	/* Menu Relative Build/Show Parameters Begin */
	/*                                                              */
	/****************************************************************/
	// layout relative parameters
	public static int gMenuItemSpacing = 1;
	public static int gMenuLeftMargin = 5;
	public static int gMenuTopMargin = 3;
	public static int gMenuRightMargin = 5;
	public static int gMenuBottomMargin = 3;

	// animation relative parameters
	public static Motion gMenuMotion = gDialogMotion;
	public static int gMenuInOutDuration = 300;

	public static int gWinMenuStartAnchor = GraphicsEx.BOTTOM
		| GraphicsEx.OUT_BOX;;
	public static int gWinMenuStartX = 1;
	public static int gWinMenuStartY = 0;
	public static int gWinMenuEndAnchor = GraphicsEx.BOTTOM | GraphicsEx.IN_BOX;
	public static int gWinMenuEndX = 1;
	public static int gWinMenuEndY = 0;

	public static int gScMenuStartAnchor = GraphicsEx.BOTTOM
		| GraphicsEx.HCENTER | GraphicsEx.OUT_BOX;;
	public static int gScMenuStartX = 0;
	public static int gScMenuStartY = 0;
	public static int gScMenuEndAnchor = GraphicsEx.VCENTER
		| GraphicsEx.HCENTER | GraphicsEx.IN_BOX;
	public static int gScMenuEndX = 0;
	public static int gScMenuEndY = 0;

	/****************************************************************/
	/*                                                              */
	/* Menu Relative Build/Show Parameters End */
	/*                                                              */
	/****************************************************************/

	public static final class MenuType {

		public static final int WINDOW_MENU = 0;
		public static final int CONTEXT_MENU = 1;
		public static final int SHORTCUT_MENU = 2;
		public static final int SUB_MENU = 3;
	}

	public static Menu buildWindowMenu(CommandEx aLeft, CommandEx aRight) {

		return buildWindowMenu(MENU, aLeft, aRight, CommandEx.EMPTY_GROUP);
	}

	public static Menu buildWindowMenu(String aId, CommandEx aLeft,
		CommandEx aRight, CommandEx[] aCommands) {

		Menu menu = new Menu(aId, MenuType.WINDOW_MENU, aLeft, aRight,
			CommandEx.CMD_SELECT, CommandEx.CMD_CANCEL, aCommands, gMenuMotion,
			0, gMenuInOutDuration, gWinMenuStartAnchor, gWinMenuEndAnchor,
			gWinMenuStartX, gWinMenuStartY, gWinMenuEndX, gWinMenuEndY,
			gMenuLeftMargin, gMenuTopMargin, gMenuRightMargin,
			gMenuBottomMargin, gMenuItemSpacing, 0);

		return menu;
	}

	public static Menu buildWindowMenu(String aId, CommandEx aLeft,
		CommandEx aRight, CommandEx aLeftOnShown, CommandEx aRightOnShown,
		CommandEx[] aCommands) {

		Menu menu = new Menu(aId, MenuType.WINDOW_MENU, aLeft, aRight,
			aLeftOnShown, aRightOnShown, aCommands, gMenuMotion, 0,
			gMenuInOutDuration, gWinMenuStartAnchor, gWinMenuEndAnchor,
			gWinMenuStartX, gWinMenuStartY, gWinMenuEndX, gWinMenuEndY,
			gMenuLeftMargin, gMenuTopMargin, gMenuRightMargin,
			gMenuBottomMargin, gMenuItemSpacing, 0);

		return menu;
	}

	public static Menu buildContextMenu(String aId, EventHandler aOwner,
		int aAbsX, int aAbsY, CommandEx[] aCommands) {

		int anchor = GraphicsEx.LEFT | GraphicsEx.TOP | GraphicsEx.IN_BOX;

		Menu menu = new Menu(aId, MenuType.CONTEXT_MENU, null, null,
			CommandEx.CMD_SELECT, CommandEx.CMD_CANCEL, aCommands, null, 0, 0,
			anchor, anchor, 0, 0, aAbsX, aAbsY, gMenuLeftMargin,
			gMenuTopMargin, gMenuRightMargin, gMenuBottomMargin,
			gMenuItemSpacing, 0);

		menu.setOwner(aOwner);

		return menu;
	}

	public static Menu buildShortcutMenu(String aId, EventHandler aOwner,
		CommandEx[] aCommands) {

		Menu menu = new Menu(aId, MenuType.SHORTCUT_MENU, null, null,
			CommandEx.CMD_SELECT, CommandEx.CMD_CANCEL, aCommands, gMenuMotion,
			0, gMenuInOutDuration, gScMenuStartAnchor, gScMenuEndAnchor,
			gScMenuStartX, gScMenuStartY, gScMenuEndX, gScMenuEndY,
			gMenuLeftMargin, gMenuTopMargin, gMenuRightMargin,
			gMenuBottomMargin, gMenuItemSpacing, 0);

		menu.setOwner(aOwner);

		return menu;
	}

	public static Menu buildSubMenu(String aId, CommandEx[] aCommands) {

		Menu menu = new Menu(aId, MenuType.SUB_MENU, null, null, null, null,
			aCommands, gMenuMotion, 0, gMenuInOutDuration, GraphicsEx.IN_BOX
				| GraphicsEx.BOTTOM, GraphicsEx.IN_BOX | GraphicsEx.BOTTOM, 0,
			0, 0, 0, gMenuLeftMargin, gMenuTopMargin, gMenuRightMargin,
			gMenuBottomMargin, gMenuItemSpacing, 0);

		return menu;
	}

	private final char[] gPrefixChar = new char[] { '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', '*', '#' };

	/** The type of menu. */
	public final int type;

	/**
	 * The command will display on the left side of menubar when menu is not
	 * popup yet.
	 */
	private final CommandEx cmdLeft;
	/**
	 * The command will display on the right side of menubar when menu is not
	 * popup yet.
	 */
	private final CommandEx cmdRight;
	/**
	 * The command will display on the left side of menubar when menu is already
	 * popup.
	 */
	private final CommandEx cmdLeftOnShown;
	/**
	 * The command will display on the right side of menubar when menu is
	 * already popup.
	 */
	private final CommandEx cmdRightOnShown;
	/**
	 * The commands will display on the popup menu.
	 */
	private final CommandEx[] commands;

	/** The list view embedded inside the menu. */
	private final ListView list;

	/** The owner of the menu, usually is the component create this menu. */
	private EventHandler owner;

	private Menu(String aId, int aType, CommandEx aLeft, CommandEx aRight,
		CommandEx aLeftOnShown, CommandEx aRightOnShown, CommandEx[] aCommands,
		Motion aMotion, int aStayDuration, int aInOutDuration,
		int aStartAnchor, int aEndAnchor, int aStartX, int aStartY, int aEndX,
		int aEndY, int aLeftMagin, int aTopMargin, int aRightMargin,
		int aBottomMargin, int aLayoutSpacing, int aLayoutHints) {

		super(aId, aMotion, aStayDuration, aInOutDuration, aStartAnchor,
			aEndAnchor, aStartX, aStartY, aEndX, aEndY, aLeftMagin, aTopMargin,
			aRightMargin, aBottomMargin, aLayoutSpacing, aLayoutHints);

		type = aType;
		
		cmdLeft = aLeft;
		cmdRight = aRight;
		cmdLeftOnShown = aLeftOnShown;
		cmdRightOnShown = aRightOnShown;

		commands = aCommands != null ? aCommands : CommandEx.EMPTY_GROUP;

		if (commands.length > 0) {

			Assert.assertLessOrEquals(commands.length, gPrefixChar.length,
				Assert.ARG);

			list = new ListView();
			list.setAttr(Attr.HAS_BACKGROUND, false);
			list.setAttr(Attr.OPAQUE, false);
			list.setMargins(aLeftMagin, aTopMargin, aRightMargin, aBottomMargin);
			addComponent(list);
			list.setAdapter(this);
		} else {
			list = null;
		}

		setAttr(Attr.MENU, true);
		setAttr(Attr.POPABLE, hasAttr(Attr.POPABLE) && commands.length > 0);
	}

	public boolean show() {

		// check window
		Window win = CanvasEx.getCurrWindow();
		if (win == null)
			return false;

		// close other menus when it is not sub menu
		if (type != MenuType.SUB_MENU)
			win.closeMenus();

		setActive(true);
		list.selectItem(0);

		return super.show();
	}

	public void close() {

		closeSubMenu();

		Menu upper = getUpperMenu();

		super.close();

		if (upper != null) {

			if (upper.hasState(State.WAITING_TO_POPOUT)) {

				upper.popout();
			} else {

				upper.setActive(true);
			}

			upper.menu = null;
		}
	}

	public void popout() {

		if (getMotionState() == Motion.STAY) {

			// popout
			if (menu != null && menu.isShown()) {

				popoutSubMenu();
				setState(State.WAITING_TO_POPOUT, true);
			} else {

				setState(State.WAITING_TO_POPOUT, false);
				super.popout();
			}
		} else {

			// close directly
			close();
		}
	}

	public void stop() {

		Menu upper = getUpperMenu();
		if (upper != null) {

			upper.setActive(false);
		}

		super.stop();
	}

	public void select() {

		clickMenuItem(list.getSelectedItem());
	}

	public Menu setOwner(EventHandler aOwner) {

		owner = aOwner;
		return this;
	}

	public boolean hasOwner() {

		return owner != null;
	}

	public EventHandler getOwner() {

		return owner;
	}

	public CommandEx getCommand(boolean aLeft) {

		return aLeft ? getLeftCommand() : getRightCommand();
	}

	public CommandEx getLeftCommand() {

		return isShown() ? cmdLeftOnShown : cmdLeft;
	}

	public CommandEx getRightCommand() {

		return isShown() ? cmdRightOnShown : cmdRight;
	}

	private Menu getUpperMenu() {

		Component parent = getParent();
		if (parent != null) {

			int idx = parent.getComponentIndex(this);
			if (idx > 0)
				return (Menu) parent.getComponentAt(idx - 1);
		}

		return null;
	}

	private void closeSubMenu() {

		if (menu != null) {

			menu.close();
			menu = null;
		}
	}

	private void popoutSubMenu() {

		if (menu != null) {

			menu.popout();
		}
	}

	private void showSubMenu(int aIdx, CommandEx aGroup) {

		// show sub-menu
		if (owner instanceof MenuSource) {
			menu = ((MenuSource) owner).getSubMenu(aGroup);
		} else {
			menu = null;
		}

		if (menu == null) {
			return;
		}

		Rectangle box = getBoxRegion();

		int absRightX = getAbsoluteX() + width;
		int itemAbsY = list.getItemTopAbsoluteYOffset(aIdx);
		int preferredW = menu.getPreferredWidth();
		int preferredH = menu.getPreferredHeight();

		int endX = absRightX - getLeftMargin();
		if (endX + preferredW > box.width) {
			endX = Math.max(box.width - preferredW, 0);
		}

		int endY = box.getBottom() - itemAbsY - preferredH + getTopMargin();
		if (endY < getBottomMargin()) {
			endY = getBottomMargin();
		}

		int startX = endX - preferredW / 2;
		int startY = endY;

		menu.setStartEndAnchor(GraphicsEx.IN_BOX | GraphicsEx.BOTTOM,
			GraphicsEx.IN_BOX | GraphicsEx.BOTTOM);
		menu.setStartOffset(startX, startY);
		menu.setEndOffset(endX, endY);
		menu.setOwner(owner);
		menu.show();
	}

	private void clickMenuItem(int aIdx) {

		if (aIdx >= 0 && aIdx < commands.length) {

			CommandEx clicked = commands[aIdx];
			if (clicked.isGroup()) {

				closeSubMenu();
				showSubMenu(aIdx, clicked);
			} else {

				// close all menus
				getWindow().closeMenus();
				int cmdId = clicked.getId();
				int scIdx = CommandEx.findShortcut(cmdId);
				if (scIdx != CommandEx.INVALID_SHORTCUT_INDEX) {
					// post shortcut event to system
					CanvasEx.postShortcutActionEvent(owner,
						CommandEx.getShortcut1stKeycode(scIdx),
						CommandEx.getShortcut2ndKeycode(scIdx), cmdId);
				} else {
					// post action event to its owner
					CanvasEx.postCommandActionEvent(owner, cmdId);
				}
			}
		}
	}

	public void setListView(ListView aListView) {

	}

	public int getCount() {

		return commands.length;
	}

	/** {@inheritDoc} */
	public Object getItem(int aIdx) {

		if (aIdx >= 0 && aIdx < getCount()) {
			return commands[aIdx];
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	public String getItemText(int aIdx) {

		if (aIdx >= 0 && aIdx < getCount()) {
			return commands[aIdx].getLabel();
		} else {
			return StringUtils.EMPTY;
		}
	}

	public int getItemCommonHeight(int aIdx) {

		return font.getHeight() + getLineSpacing();
	}

	public int paintItem(GraphicsEx aG, int aIdx, int aItemCurrHeight,
		int aItemCurrWidth) {

		CommandEx cmd = commands[aIdx];
		int offset = getCount() <= 9 ? 1 : 0;
		String label = "" + gPrefixChar[aIdx + offset] + '.' + cmd.getLabel();

		int arrowW = cmd.isGroup() ? GROUP_TRIANGLE_WIDTH : 0;
		int width = aItemCurrWidth - arrowW;
		int height = aItemCurrHeight - gMenuItemSpacing;
		int anchor = GraphicsEx.LEFT | GraphicsEx.HCENTER;

		aG.setFont(font);
		aG.drawBoxedString(label, 0, 0, 0, 0, height, width, anchor);

		if (cmd.isGroup()) {
			drawGroupDecoration(aG, aItemCurrWidth, height, width);
		}

		return aItemCurrHeight;
	}

	private void drawGroupDecoration(GraphicsEx aG, int aItemWidth,
		int aHeight, int aWidth) {

		aHeight = aHeight / 2 * 2 + 1;
		int pad = aHeight * 3 / 8;
		int center = aHeight / 2 + 1;

		aG.setColor(Color.SNOW);
		aG.fillTriangle(aWidth + GROUP_TRIANGLE_LEFT_MARGIN, pad, aItemWidth
			- GROUP_TRIANGLE_RIGHT_MARGIN, center, aWidth
			+ GROUP_TRIANGLE_LEFT_MARGIN, center + center - pad);
	}

	public void keyEvent(Event aKeyEv) {

		if (list == null)
			return;

		switch (aKeyEv.type) {

		case EventType.KEY_PRESSED:
			if (aKeyEv.isNavigationKey()) {

				int selected = list.getSelectedItem();
				CommandEx command = commands[selected];

				switch (aKeyEv.getGameAction()) {

				case Canvas.LEFT:

					popout();
					aKeyEv.accept();
					break;

				case Canvas.RIGHT:

					if (command.isGroup()) {

						showSubMenu(selected, command);
					} else {

						list.selectItem(0);
					}

					aKeyEv.accept();
					break;
				}
			}
			break;

		case EventType.KEY_RELEASED:
			if (aKeyEv.isStdTelephoneKey()) {

				int start = getCount() <= 9 ? 1 : 0;
				int end = getCount() <= 9 ? 10 : gPrefixChar.length;

				for (int i = start; i < end; ++i) {

					if (aKeyEv.getKeyCode() == gPrefixChar[i]) {

						clickMenuItem(i - start);

						aKeyEv.accept();
						return;
					}
				}
			}
			break;
		}// switch
	}

	public void onItemClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY) {

		Assert.assertNotNull(owner);

		clickMenuItem(aIdx);
	}

	public void onItemLongClicked(int aIdx, Event aCauseEv, int aItemX,
		int aItemY) {

	}

	public void onItemSelected(int aIdx) {

	}

	public void onItemUnSelected(int aIdx) {

	}

	/** {@inheritDoc} */
	protected void calcPreferredSize() {

		int dotW = font.charWidth('.');

		int preferredW = 0;
		int preferredH = 0;

		for (int i = 0, w = 0; i < commands.length; ++i) {

			w = font.numberWidth(i + 1) + dotW;
			w += font.stringWidth(commands[i].getLabel());

			if (commands[i].isGroup()) {

				w += GROUP_TRIANGLE_WIDTH;
			}

			preferredW = Math.max(preferredW, w);
			preferredH += getItemCommonHeight(i);
		}

		preferredW += getLeftMargin() + getRightMargin();
		preferredH += + getTopMargin() + getBottomMargin();

		Rectangle box = getBoxRegion();
		preferredH = Math.min(preferredH, box.height);

		// handle context menu's position problems
		if (type == MenuType.CONTEXT_MENU) {

			int endX = getEndX() - box.x;
			int endY = getEndY() - box.y;

			if (endX + preferredW > box.width) {

				endX = box.width - preferredW;
			}

			if (endY + preferredH > box.height) {

				endY = box.height - preferredH;
			}

			setEndOffset(endX, endY);

			preferredH = Math.min(preferredH, box.height - endY);
		}

		setPreferredSizeInternal(preferredW, preferredH);
	}

	/** {@inheritDoc} */
	protected Rectangle getBoxRegion() {

		Window win = CanvasEx.getCurrWindow();
		if (win != null)
			return win.getCentralArea();

		return super.getBoxRegion();
	}

	protected void paintBackground(GraphicsEx aG) {

		Brush brush = Brush.createColorBrush(Color.GREEN);
		if (isActive()) {
			brush = brush.lighter(110);
		}
		aG.setBrush(brush);
		aG.fillRectEx(x, y, width, height);
	}
}
