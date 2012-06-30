package cn.uc.tiny;

import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.GraphicsEx;

public class TitleBar extends Component {
	private String title = "";
	private String showTitle = "";
	private int timer;

	public TitleBar() {
		setAttr(16, true);
	}

	public String getClazz() {
		return "TitleBar";
	}

	protected void initializeComponent() {
		this.timer = CanvasEx.findAvailableTimer();
		CanvasEx.startTimer(this.timer, this, 10000);
	}

	protected void deinitializeComponent() {
		CanvasEx.closeTimer(this.timer);
	}

	public void setTitle(Component aTitle) {
		removeAll();
		addComponent(aTitle);
		aTitle.setBounds(this.x, this.y, this.width, this.height);
		aTitle.layout();
		aTitle.setVisible(true);
		repaint(10);
	}

	public void repaint(int aReason) {
		if (aReason == 10) {
			Window win = getWindow();
			if (win != null) {
				StringBuffer sb = new StringBuffer(this.title);

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

				this.showTitle = sb.toString();
			}
		}

		super.repaint(aReason);
	}

	protected void paintContent(GraphicsEx aG) {
		super.paintContent(aG);
	}

	protected void paintBackground(GraphicsEx aG) {
		if (hasFocus())
			aG.setBrush(Brush.createColorBrush(-5383962));
		else {
			aG.setBrush(Brush.createColorBrush(-7876885));
		}
		aG.fillRectEx(this.x, this.y, this.width, this.height);
	}

	public void keyPressed(Event aKeyEv) {
		if ((aKeyEv.isNavigationKey()) && (aKeyEv.getGameAction() != 6)) {
			Window win = getWindow();
			if (aKeyEv.getGameAction() == 2)
				win.switchPreviousView(true);
			else if (aKeyEv.getGameAction() == 5) {
				win.switchNextView(true);
			}

			aKeyEv.accept();
		}
	}

	public void keyReleased(Event aKeyEv) {
		if ((aKeyEv.isNavigationKey()) && (aKeyEv.getGameAction() != 6))
			aKeyEv.accept();
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
		repaint(10);
		CanvasEx.startTimer(this.timer, this, 10000);
	}
}