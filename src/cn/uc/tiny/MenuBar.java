/**
 * Tiny.cn.uc.ui.MenuBar.java, 2010-12-17
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Log;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class MenuBar extends Component {

	public static final int LEFT_RIGHT_COMMAND_WIDTH = 60;

	private final String text = StringUtils.EMPTY;
	private Menu menu;

	public MenuBar() {

	}

	/** {@inheritDoc} */
	public String getClazz() {

		return "MenuBar";
	}

	public void setMenu(Menu aMenu) {

		if (menu != aMenu) {
			menu = aMenu;
		}
		this.repaint(Reason.UPDATE);
	}

	public Menu getMenu() {

		return menu;
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {

		if (aKeyEv.isSoftKey()) {
			if (aKeyEv.type == EventType.KEY_RELEASED) {
				triggerCommand(aKeyEv.isLeftSK());
			}
			aKeyEv.accept();
		}
	}

	/** {@inheritDoc} */
	public void pointerReleased(Event aPtEv) {

		if (aPtEv.getX() > 0 && aPtEv.getX() < LEFT_RIGHT_COMMAND_WIDTH) {
			triggerCommand(true);
		} else if (aPtEv.getX() > width - LEFT_RIGHT_COMMAND_WIDTH
			&& aPtEv.getX() < width) {
			triggerCommand(false);
		}
	}

	private void triggerCommand(boolean aLeft) {

		if (menu == null) {
			return;
		}

		CommandEx cmd = menu.getCommand(aLeft);
		if (cmd == null) {
			return;
		}

		if (menu.isShown()) {

			Window win = getWindow();
			if (cmd == CommandEx.CMD_SELECT) {
				win.getTopMenu().select();
			} else {
				win.popoutMenus();
			}
		} else {

			if (cmd.isGroup()) {
				if (menu.hasAttr(Attr.POPABLE)) {
					menu.show();
				} else {
					Log.w(menu);
				}
			} else {
				// trigger command action
				CanvasEx.postCommandActionEvent(menu.getOwner(), cmd.getId());
			}
		}
	}

	/** {@inheritDoc} */
	protected void paintContent(GraphicsEx aG) {

		int color = Color.WHITE;

		aG.setFont(getFont());

		if (menu != null) {

			CommandEx left = menu.getLeftCommand();
			CommandEx right = menu.getRightCommand();

			String str = left != null ? left.getLabel() : StringUtils.EMPTY;
			aG.setColor(color);
			aG.drawBoxedString(str, 10, 0, x, y, width, height, GraphicsEx.LEFT
				| GraphicsEx.VCENTER);

			str = right != null ? right.getLabel() : StringUtils.EMPTY;
			aG.drawBoxedString(str, 10, 0, x, y, width, height,
				GraphicsEx.RIGHT | GraphicsEx.VCENTER);
		}

		aG.setColor(color);
		aG.drawBoxedString(text, 0, 0, x, y, width, height, GraphicsEx.HCENTER
			| GraphicsEx.VCENTER);
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		aG.setBrush(Brush.createColorBrush(Color.SKYBLUE));
		aG.fillRectEx(x, y, width, height);
	}
}
