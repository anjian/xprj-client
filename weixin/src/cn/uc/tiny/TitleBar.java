/**
 * Tiny.cn.uc.ui.TitleBar.java, 2010-12-17
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import javax.microedition.lcdui.Canvas;

import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;
import cn.uc.util.StringUtils;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class TitleBar extends Component {

	private String title = StringUtils.EMPTY;
	private String showTitle = StringUtils.EMPTY;
	private int timer;

	public TitleBar() {

		setAttr(Attr.FOCUSABLE, true);
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return "TitleBar";
	}

	protected void initializeComponent() {

		timer = CanvasEx.findAvailableTimer();
		CanvasEx.startTimer(timer, this, 10000);
	}

	protected void deinitializeComponent() {

		CanvasEx.closeTimer(timer);
	}

	public void setTitle(String aText) {

		title = aText;
		this.repaint(Reason.UPDATE);
	}

	public void repaint(int aReason) {

		if (aReason == Reason.UPDATE) {

			Window win = getWindow();
			if (win != null) {

				StringBuffer sb = new StringBuffer(title);

				sb.append(" - [");
				sb.append(win.getCurrentViewIndex() + 1);
				sb.append("/");
				sb.append(win.getViewCount());
				sb.append("] [");
				sb.append(CanvasEx.getOverallFps());
				sb.append("/");
				sb.append(CanvasEx.getPaintFps());
				sb.append("fps, ");
				sb.append(CanvasEx.getTotalFrames());
				sb.append("]");

				showTitle = sb.toString();
			}
		}

		super.repaint(aReason);
	}

	protected void paintContent(GraphicsEx aG) {

		aG.setFont(getFont());
		aG.setColor(/* this.getForeground() */Color.ALICEBLUE);
		aG.drawString(showTitle, x + 10, y + height / 2
			+ aG.getFont().getHeight() / 2, GraphicsEx.LEFT | GraphicsEx.BOTTOM);
	}

	protected void paintBackground(GraphicsEx aG) {

		if (hasFocus()) {
			aG.setBrush(Brush.createColorBrush(Color.LIGHTBLUE));
		} else {
			aG.setBrush(Brush.createColorBrush(Color.SKYBLUE));	
		}
		aG.fillRectEx(x, y, width, height);
	}

	public void keyPressed(Event aKeyEv) {

		if (aKeyEv.isNavigationKey() && aKeyEv.getGameAction() != Canvas.DOWN) {

			Window win = getWindow();
			if (aKeyEv.getGameAction() == Canvas.LEFT) {
				win.switchPreviousView(true);
			} else if (aKeyEv.getGameAction() == Canvas.RIGHT) {
				win.switchNextView(true);
			}
			
			aKeyEv.accept();
		}
	}

	public void keyReleased(Event aKeyEv) {

		if (aKeyEv.isNavigationKey() && aKeyEv.getGameAction() != Canvas.DOWN) {
			aKeyEv.accept();
		}
	}

	public void pointerLongPressed(Event aPtEv) {

		super.pointerLongPressed(aPtEv);
	}

	public void pointerPressed(Event aPtEv) {

		super.pointerPressed(aPtEv);
	}

	public void pointerReleased(Event aPtEv) {

		super.pointerReleased(aPtEv);
	}

	public void timerEvent(Event aTimerEv) {

		this.repaint(Reason.UPDATE);
		CanvasEx.startTimer(timer, this, 10000);
	}
}
