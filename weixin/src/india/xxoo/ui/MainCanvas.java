package india.xxoo.ui;

import india.xxoo.adapter.Keybord;
import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;
import india.xxoo.dao.ChartList;
import india.xxoo.dao.Friend;
import india.xxoo.dao.Manager;
import india.xxoo.main.Main;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;





public class MainCanvas extends Canvas implements Runnable{

	public static final int PADDING = 5;

	//�ĸ���Ҫ�Ŀؼ�
	public Component iTitleBar;
	//���Ĳ���
	public Component iPageBar;
	//�������벿��
	public Component iInputBar;
	//�ײ��˵���
	public Component iBottomBar;
	//��ǰ�����bar
	public Component iFocusBar;
	//���ݹ�����
	public Manager iDataManager;

	public int iKeyCode;
	public int iKeyType;

	private boolean isExit;

	public MainCanvas() {
		setFullScreenMode(true);
		iDataManager = new Manager();
		new Thread(this).start();
	}
	
	public void init(){
		showHistoryState();
	}
	
	/**
	 * ��ǰ״̬Ϊ������ʷ
	 */
	public void showHistoryState(){
		int sBarHeight = MyFont.getMyFont().getHeight() + PADDING * 2;
		if(iTitleBar == null){
			iTitleBar = new TitleBar(0,0,getWidth(),sBarHeight);
		}
		iPageBar = createHistoryPageBar(0,sBarHeight,getWidth(),getHeight() - sBarHeight * 2);
		iInputBar = null;
		if(iBottomBar == null){
			iBottomBar = new BottomBar(0,getHeight() - sBarHeight,getWidth(),sBarHeight);
		}
		iFocusBar = iPageBar;
	}
	
	/**
	 * ��ǰ״̬Ϊ����
	 */
	public void showChartState(){
		int sBarHeight = MyFont.getMyFont().getHeight() + PADDING * 2;
		if(iTitleBar == null){
			iTitleBar = new TitleBar(0,0,getWidth(),sBarHeight);
		}
		iPageBar = createChartPageBar(0,sBarHeight,getWidth(),getHeight() - sBarHeight * 3);
		if(iInputBar == null){
			iInputBar = new InputBar(0,getHeight() - sBarHeight * 2,getWidth(),sBarHeight);
		}
		if(iBottomBar == null){
			iBottomBar = new BottomBar(0,getHeight() - sBarHeight,getWidth(),sBarHeight);
		}
		iFocusBar = iInputBar;
	}
	
	/**
	 * ��ǰ״̬Ϊ��ϵ��
	 */
	public void showContactState(){
		int sBarHeight = MyFont.getMyFont().getHeight() + PADDING * 2;
		if(iTitleBar == null){
			iTitleBar = new TitleBar(0,0,getWidth(),sBarHeight);
		}
		iPageBar = createContactPageBar(0,sBarHeight,getWidth(),getHeight() - sBarHeight * 2);
		iInputBar = null;
		if(iBottomBar == null){
			iBottomBar = new BottomBar(0,getHeight() - sBarHeight,getWidth(),sBarHeight);
		}
		iFocusBar = iPageBar;
	}
	

	//��������ҳ��
	public PageBar createChartPageBar(int aX , int aY, int aWidth , int aHeight){
		PageBar sPageBar = new PageBar(aX,aY,aWidth,aHeight,PageBar.CHART_PAGE_BAR);
		ChartList sChartList = Main.iMainCanvas.iDataManager.getiCurrentFriend().getiChartContent();
		int sFriendCount = sChartList.getChartCount();
		for(int i = 0 ; i < sFriendCount ;i++){
			Object sData = sChartList.getCharDataById(i);
			if(sData != null){
				sPageBar.addChild(i);
			}
		}
		sPageBar.layout();
		return sPageBar;
	}
	//������ϵ��ҳ��
	public PageBar createContactPageBar(int aX , int aY, int aWidth , int aHeight){
		PageBar sPageBar = new PageBar(aX,aY,aWidth,aHeight,PageBar.CONTACT_PAGE_BAR);
		int sFriendCount = Main.iMainCanvas.iDataManager.getFiendCount();
		for(int i = 0 ; i < sFriendCount ;i++){
			Friend sFriend = Main.iMainCanvas.iDataManager.getFiendById(i);
			if(sFriend != null){
				sPageBar.addChild(i);
			}
		}
		sPageBar.layout();
		return sPageBar;
	}
	//���������¼ҳ��
	public PageBar createHistoryPageBar(int aX , int aY, int aWidth , int aHeight){
		PageBar sPageBar = new PageBar(aX,aY,aWidth,aHeight,PageBar.HISTOTY_PAGE_BAR);
		int sFriendCount = Main.iMainCanvas.iDataManager.getFiendCount();
		for(int i = 0 ; i < sFriendCount ;i++){
			Friend sFriend = Main.iMainCanvas.iDataManager.getFiendById(i);
			if(sFriend.getiChartContent() != null){
				sPageBar.addChild(i);
			}
		}
		sPageBar.layout();
		return sPageBar;
	}
	
	
	
	protected void paint(Graphics arg0) {
		MyGraphics iG = MyGraphics.getGraphics(arg0);
		if(iTitleBar != null)iTitleBar.draw(iG);
		if(iPageBar != null)iPageBar.draw(iG);
		if(iInputBar != null)iInputBar.draw(iG);
		if(iBottomBar != null)iBottomBar.draw(iG);
	}

	public void run() {
		while(!isExit){
			try {
				//��������
				boolean sNeedRepaint = input();
				//����һЩ����
				check();
				//�����Ҫ�ػ�
				if(sNeedRepaint){
					repaint();
				}
				recover();
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * ����һЩ�����ı仯
	 */
	private void check(){

	}

	/**
	 * ��������
	 * @return �Ƿ���Ҫ���»���
	 */
	private boolean input(){
		try {
			if(iKeyType == 1){
				if(iInputBar != null && iFocusBar == iInputBar && iInputBar.handleEvent(iKeyType, iKeyCode)){
					return true;
				}
				if(iFocusBar == iPageBar && iPageBar.handleEvent(iKeyType, iKeyCode)) {
					return true;
				}
				//�ƶ�����
				if(iKeyCode == Keybord.NAV_UP && iFocusBar == iInputBar){
					iFocusBar = iPageBar;
					return true;
				}
				if(iKeyCode == Keybord.NAV_DOWN && iFocusBar == iPageBar && iInputBar != null){
					iFocusBar = iInputBar;
					return true;
				}
				//Ĭ���¼��Ĵ���
				if(iTitleBar.handleEvent(iKeyType, iKeyCode)){
					return true;
				}
				iBottomBar.handleEvent(iKeyType, iKeyCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private void recover(){
		iKeyCode = -1;
		iKeyType = -1;
	}

	protected void keyPressed(int keyCode) {
		iKeyCode = adaptKeyCode(keyCode);
		iKeyType = 1;
	}

	protected void keyReleased(int keyCode) {
		iKeyCode = adaptKeyCode(keyCode);
		iKeyType = 0;
	}

	private int adaptKeyCode(int aKeyCode) {
		if(!Keybord.isDigitalAreaKey(aKeyCode)){
			int sKeyCode = Keybord.KEY_INVALID;
			//ͨ����������
			String sKeyName = null;
			try {
				sKeyName = getKeyName(aKeyCode);
			}catch (Exception e) {
			}
			if (sKeyName != null) {
				sKeyCode = Keybord.adaptByKeyName(sKeyName, aKeyCode);
				if (Keybord.isValidKey(sKeyCode)) {
					return sKeyCode;
				}
			}

			//ͨ����ֵ����
			sKeyCode = Keybord.adaptByKeyCode(aKeyCode);
			if (Keybord.isValidKey(sKeyCode)) {
				return sKeyCode;
			}

			//ͨ����Ϸ����������
			if(Keybord.isNoGameActionKey(aKeyCode)){
				return aKeyCode;
			}


			int sGameAction = 0;
			try {
				sGameAction = getGameAction(aKeyCode);
			} catch(Exception e) {
			}

			if (Keybord.isValidKey(sGameAction) && sGameAction != 0) {     
				return sGameAction;
			}
		}
		return aKeyCode;
	}
}

