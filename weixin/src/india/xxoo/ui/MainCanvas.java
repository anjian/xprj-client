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

	//四个主要的控件
	public Component iTitleBar;
	//正文部分
	public Component iPageBar;
	//文字输入部分
	public Component iInputBar;
	//底部菜单栏
	public Component iBottomBar;
	//当前焦点的bar
	public Component iFocusBar;
	//数据管理器
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
	 * 当前状态为聊天历史
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
	 * 当前状态为聊天
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
	 * 当前状态为联系人
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
	

	//创建聊天页面
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
	//创建联系人页面
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
	//创建聊天记录页面
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
				//接受输入
				boolean sNeedRepaint = input();
				//更新一些变量
				check();
				//如果需要重绘
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
	 * 更改一些环境的变化
	 */
	private void check(){

	}

	/**
	 * 接受输入
	 * @return 是否需要重新绘制
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
				//移动焦点
				if(iKeyCode == Keybord.NAV_UP && iFocusBar == iInputBar){
					iFocusBar = iPageBar;
					return true;
				}
				if(iKeyCode == Keybord.NAV_DOWN && iFocusBar == iPageBar && iInputBar != null){
					iFocusBar = iInputBar;
					return true;
				}
				//默认事件的处理
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
			//通过键名适配
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

			//通过键值适配
			sKeyCode = Keybord.adaptByKeyCode(aKeyCode);
			if (Keybord.isValidKey(sKeyCode)) {
				return sKeyCode;
			}

			//通过游戏动作健适配
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

