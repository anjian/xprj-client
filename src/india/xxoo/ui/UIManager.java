package india.xxoo.ui;

import india.xxoo.ui.listAdapt.ChartListAdapt;
import india.xxoo.ui.listAdapt.ContactListAdapt;
import india.xxoo.ui.listAdapt.ListAdapter;

import javax.microedition.midlet.MIDlet;

import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
import cn.uc.demo.components.MenuWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.ListView;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;

public class UIManager implements EventHandler, MenuSource {
	public MIDlet iMid;
	
	public static final String CONTACT = "co";
	public static final String CHART = "ch";
	public static final String HOME = "ho";
	public static final String SEND = "s";
	public static final String ADD_FRIEND = "af";
	
	public static final String MARTCH = "m";
	public static final String SEARCH = "se";
	public static final String INVITE_FRIEND = "if";
	
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
		contact.setId(HOME);
		contact.setText("Home");
		contact.setActionTarget(this);
		grid.addComponent(contact);

		Button chart = new Button();
		chart.setId(CONTACT);
		chart.setText("Contact");
		chart.setActionTarget(this);
		grid.addComponent(chart);

		Button home = new Button();
		home.setId(ADD_FRIEND);
		home.setText("+Friend");
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
		send.setText("Send");
		input.addComponent(this.textBox);
		input.addComponent(send);

		int sInputHeight = FontEx.getDefaultFont().getHeight() + 10;
		list.setBounds(0, 0, 239, 276 - sInputHeight);
		input.setBounds(0, 276 - sInputHeight, 239, sInputHeight);

		this.textBox.setBounds(0, 0, 199, sInputHeight);
		send.setBounds(199, 0, 40, sInputHeight);
		

		view.addComponent(list);
		view.addComponent(input);

		win.setMenuSource(this);
		win.setTitle(title);
		win.addView(view);
		win.show();
	}
	
	public void displayAddFriend(){
		CanvasEx.initCanvas(iMid);
		FontEx.initializeUsedFonts(185);
		FontEx.setDefaultFont(FontEx.getFont(0, 0));

		Component title = createNormalTitleBar();
		MenuWindow win = new MenuWindow();

		GridContainer aGride = new GridContainer();
		aGride.setCol(3);
//		aGride.setPosition(, aY);
//		aGride.setSpacing();
		//Æ¥Åä
		Button sMatch = new Button();
		sMatch.setId(MARTCH);
		sMatch.setText("Match");
		sMatch.setActionTarget(this);
		//Ñ°ÕÒ
		Button sSearch = new Button();
		sSearch.setId(SEARCH);
		sSearch.setText("Search");
		sSearch.setActionTarget(this);
		//ÍÆ¼ö
		Button sInvite = new Button();
		sInvite.setId(INVITE_FRIEND);
		sInvite.setText("Invite Friend");
		sInvite.setActionTarget(this);
		
		aGride.addComponent(sMatch);
		aGride.addComponent(sSearch);
		aGride.addComponent(sInvite);
		
		win.setMenuSource(this);
		win.setTitle(title);
		win.addView(aGride);
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
			if (CHART.equals(id))
				displayChartList();
			else if (CONTACT.equals(id))
				displayContactList();
			else if (HOME.equals(id)){
				if (SEND.equals(id) && ((this.listAdapter instanceof ChartListAdapt))){
					((ChartListAdapt) this.listAdapter).addChart(1, 0, 2, this.textBox.getText());
					this.textBox.clear();
				}
			}else if (ADD_FRIEND.equals(id)){
				displayAddFriend();
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