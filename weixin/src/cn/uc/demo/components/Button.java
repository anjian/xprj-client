/**
 * Tiny.cn.uc.demo.components.Button.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import javax.microedition.lcdui.Image;

import cn.uc.T;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
import cn.uc.tiny.Window;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.GraphicsEx;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class Button extends Component {

	private String text;
	private Image icon;
	private EventHandler actionTarget = Event.NO_TARGET;

	private int textColor = Color.WHITESMOKE;
	private Brush bgBrush = Brush.createColorBrush(Color.ORANGE);

	public Button() {

		super();

		setAttr(Attr.FOCUSABLE, true);
		setAttr(Attr.ACTIONABLE, true);
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return "Button";
	}

	/** {@inheritDoc} */
	public void setText(String aText) {

		text = aText;
	}
	
	public String getText() {
		
		return text;
	}

	public void setIcon(Image aIcon) {

		icon = aIcon;
	}

	/** {@inheritDoc} */
	public void setActionTarget(EventHandler aActionTarget) {

		actionTarget = aActionTarget;
	}

	/** {@inheritDoc} */
	public EventHandler getActionTarget() {

		return actionTarget;
	}

	public void setTextColor(int aTextColor) {

		textColor = aTextColor;
	}

	public void setBackground(Brush aBgBrush) {

		bgBrush = aBgBrush;
		setAttr(Attr.OPAQUE, bgBrush.isOpaque());
	}

	public void pointerLongPressed(Event aPtEv) {

		Window win = getWindow();
		if (win != null) {
			Menu context = this.getContextMenu(aPtEv.getX(), aPtEv.getY());
			if (context != null) {
				win.showContextMenu(context);	
			}
		}

		aPtEv.accept();
	}
	
	/** {@inheritDoc} */
	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {

		CommandEx[] commands = new CommandEx[] {

		MenuWindow.createCommand(T.MENU_OPEN_IN_BACKGROUND),
			MenuWindow.createCommand(T.MENU_FIX),
			MenuWindow.createCommand(T.MENU_EDIT),
			MenuWindow.createCommand(T.MENU_DELETE) };

		return Menu.buildContextMenu(getId(), this, aX, aY, commands);
	}
	
	/** {@inheritDoc} */
	public void actionEvent(Event aActEv) {

		if (!aActEv.isCommandAction())
			return;

		int cmdId = aActEv.getCommandId();

		switch (cmdId) {

		case T.MENU_OPEN_IN_BACKGROUND:
		case T.MENU_FIX:
		case T.MENU_EDIT:
		case T.MENU_DELETE: {

			Popup.buildToast(
				Resource.getText(cmdId) + " by " +
					aActEv.target.toString()).show();
			break;
		}
		}
		aActEv.accept();
	}
	
	/** {@inheritDoc} */
	protected void paintContent(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aG.translate(x, y);
		int xoff = 0;
		int yoff = 0;
		if (icon != null) {

			xoff += 5;
			yoff += (height - icon.getHeight()) / 2;
			aG.drawImage(icon, xoff, yoff, GraphicsEx.LEFT_TOP);
			xoff += icon.getWidth() + 5;
		}
		aG.setFont(getFont());
		aG.setColor(textColor);
		aG.drawBoxedString(text, 0, 0, xoff, 0, width - xoff, height,
			GraphicsEx.CENTER);
		aG.restore();
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		Brush brush = bgBrush;
		if (isPressed()) {
			brush = bgBrush.darker(110);
		} else if (hasFocus()) {
			brush = bgBrush.lighter(110);
		}
		aG.setBrush(brush);
		aG.fillRectEx(x, y, width, height);

		aG.setColor(Brush.getDarkerColor(bgBrush.getColor(), 110));
		aG.drawRect(x, y, width - 2, height - 2);
		aG.setColor(Brush.getDarkerColor(bgBrush.getColor(), 120));
		aG.drawRect(x + 1, y + 1, width - 2, height - 2);
	}
}
