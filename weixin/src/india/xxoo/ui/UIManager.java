package india.xxoo.ui;

import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
import cn.uc.demo.components.MenuWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.ListAdapter;
import cn.uc.tiny.ListView;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.Window;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import india.xxoo.ui.chartList.ChartListAdapt;
import india.xxoo.ui.contactList.ContactListAdapt;
import java.io.PrintStream;
import javax.microedition.midlet.MIDlet;

public class UIManager implements EventHandler, MenuSource {
	public MIDlet iMid;
	public static final String CONTACT = "co";
	public static final String CHART = "ch";
	public static final String HOME = "ho";
	public static final String SEND = "s";
	ListAdapter listAdapter;
	InputBar textBox;
	//sadsa

	public UIManager(MIDlet aMidlet) {
		this.iMid = aMidlet;
	}

	private Component createNormalTitleBar() {
		GridContainer grid = new GridContainer(3, FontEx.getDefaultFont()
				.getHeight(), 0);

		Button contact = new Button();
		contact.setId("co");
		contact.setText("联系人");
		contact.setActionTarget(this);
		grid.addComponent(contact);

		Button chart = new Button();
		chart.setId("ch");
		chart.setText("聊天记录");
		chart.setActionTarget(this);
		grid.addComponent(chart);

		Button home = new Button();
		home.setId("ho");
		home.setText("个人主页");
		home.setActionTarget(this);
		grid.addComponent(home);

		return grid;
	}

	public void displayContactList() {
		CanvasEx.initCanvas(this.iMid);
		FontEx.initializeUsedFonts(185);
		FontEx.setDefaultFont(FontEx.getFont(0, 0));

		ListView list = new ListView();
		this.listAdapter = new ContactListAdapt();
		list.setAdapter(this.listAdapter);

		Component title = createNormalTitleBar();
		MenuWindow win = new MenuWindow();
		win.setMenuSource(this);
		win.setTitle(title);
		win.addView(list);
		win.show();
	}

	public void displayChartList() {
		CanvasEx.initCanvas(this.iMid);
		FontEx.initializeUsedFonts(185);
		FontEx.setDefaultFont(FontEx.getFont(0, 0));

		Component title = createNormalTitleBar();
		MenuWindow win = new MenuWindow();

		Component view = new Component();

		ListView list = new ListView();
		this.listAdapter = new ChartListAdapt();
		list.setAdapter(this.listAdapter);

		Component input = new Component();

		this.textBox = new InputBar();
		input.setActionTarget(this.textBox);

		Button send = new Button();
		send.setActionTarget(this);
		send.setId("s");
		input.addComponent(this.textBox);
		input.addComponent(send);

		int sInputHeight = FontEx.getDefaultFont().getHeight() + 10;
		list.setBounds(0, 0, 239, 276 - sInputHeight);
		input.setBounds(0, 276 - sInputHeight, 239, sInputHeight);

		this.textBox.setBounds(0, 0, 199, sInputHeight);
		send.setBounds(199, 0, 40, sInputHeight);
		send.setText("发送");

		view.addComponent(list);
		view.addComponent(input);

		win.setMenuSource(this);
		win.setTitle(title);
		win.addView(view);
		win.show();
	}

	public Menu getWindowMenu() {
		return null;
	}

	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {
		return null;
	}

	public Menu getSubMenu(CommandEx aGroup) {
		return null;
	}

	public boolean event(Event aEv) {
		if (aEv.isComponentAction()) {
			Button button = (Button) aEv.getActionComponent();
			String id = button.getId();
			if ("ch".equals(id))
				displayChartList();
			else if ("co".equals(id))
				displayContactList();
			else if (!"ho".equals(id)) {
				if (("s".equals(id))
						&& ((this.listAdapter instanceof ChartListAdapt))) {
					((ChartListAdapt) this.listAdapter).addChart(1, 0, 2,
							this.textBox.getText());
					this.textBox.clear();
				}
			}
		}
		return false;
	}

	public void keyEvent(Event aKeyEv) {
		System.out.println();
	}

	public void pointerEvent(Event aPtEv) {
	}

	public void actionEvent(Event aActEv) {
	}

	public void timerEvent(Event aTimerEv) {
	}

	public void progressEvent(Event aProgressEv) {
	}

	public void errorEvent(Event aErrorEv) {
	}

	public void sizeChanged(int aNewWidth, int aNewHeight) {
	}

	public void orentationChanged(int aNewOrentation) {
	}

	public void showNotify() {
	}

	public void hideNotify() {
	}

	public void focusGained(Event aCauseEv) {
	}

	public void focusLost(Event aCauseEv) {
	}

	public void onEventError(Throwable aErr) {
	}
}