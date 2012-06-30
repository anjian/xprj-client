package india.xxoo.dao;

import java.util.Vector;

public class Manager {
	//ͷ��ߴ�
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
	
	//װ������
	public void load(){
		iCurrentFriend = addFriend("����", "0", null);
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "���");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "��");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "��������");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "���ð����������");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "��Է�����");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "����");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "��ʲô�ô");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_IN_TEXT, "�ҳԷ�ȥ");
		iCurrentFriend.addChart(ChartList.CHART_TYPE_OUT_TEXT, "Ŷ��������");
		
		
		addFriend("����", "1", null);
		addFriend("����", "2", null);
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
	//���濼��ɾ�����ѵ����??
	public int getFiendCount(){
		return iFriendList.size();
	}
	
}
