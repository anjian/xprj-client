package india.xxoo.ui;

import india.xxoo.main.Main;

public class ControCenter {
	//�򿪲˵�
	public static final int OPEN_MENU = 0;
	//��ҳ
	public static final int OPEN_HOME = 1;
	//������Ϣ
	public static final int SEED_MESSAGE = 2;
	//��ʾ������ʷ
	public static final int SHOW_HISTORY = 3;
	//��ʾ��ϵ��
	public static final int SHOW_CONTACT = 4;
	//������ҳ��
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
