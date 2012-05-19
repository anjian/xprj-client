package india.xxoo.ui;

import india.xxoo.main.Main;

public class ControCenter {
	//打开菜单
	public static final int OPEN_MENU = 0;
	//主页
	public static final int OPEN_HOME = 1;
	//发送消息
	public static final int SEED_MESSAGE = 2;
	//显示聊天历史
	public static final int SHOW_HISTORY = 3;
	//显示联系人
	public static final int SHOW_CONTACT = 4;
	//打开聊天页面
	public static final int SHOW_CHART = 5;
	
	
	public static void act(int aType , Object aParameter){
		switch(aType){
		case OPEN_MENU:
			break;
		case OPEN_HOME:
			break;
		case SEED_MESSAGE:
			break;
		case SHOW_HISTORY:
			Main.iMainCanvas.showHistoryState();
			break;
		case SHOW_CONTACT:
			Main.iMainCanvas.showContactState();
			break;
		case SHOW_CHART:
			Main.iMainCanvas.showChartState();
			break;
		}
	}
}
