package india.xxoo.dao;

import java.util.Vector;

public class Manager {
	//头像尺寸
	public static int[] iIconSize = {40,40};
	
	private Vector iFriendList = new Vector();
	private Friend iCurrentFriend;

	public Friend getiCurrentFriend() {
		return iCurrentFriend;
	}
	public void setiCurrentFriend(Friend iCurrentFriend) {
		this.iCurrentFriend = iCurrentFriend;
	}
	public Manager() {
		load();
	}
	
	//装载数据
	public void load(){
		iCurrentFriend = addFriend("张三", "0", null);
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "你好");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "滚");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "别这样嘛");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "你妹啊，别恶心我");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "你吃饭了麽");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "吃了");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "有什么活动么");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "我吃饭去");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "哦，这样的");
		
		
		addFriend("李四", "1", null);
		addFriend("王五", "2", null);
	}
	
	public Friend getFiendById(int aID){
		if(aID >= 0 && aID < iFriendList.size()){
			return (Friend) iFriendList.elementAt(aID);
		}
		return null;
	}
	
	public Friend addFriend(String aName,String aID,byte[] aHeadIcon){
		Friend sFriend = new Friend(aName,aID,aHeadIcon);
		iFriendList.addElement(sFriend);
		return sFriend;
	}
	//后面考虑删除好友的情况??
	public int getFiendCount(){
		return iFriendList.size();
	}
	
}
