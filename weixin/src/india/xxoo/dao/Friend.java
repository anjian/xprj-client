package india.xxoo.dao;

public class Friend {
	
	private String iName;
	private String iUserID;
	private byte[] iHeadIcon;
	private ChartList iChartContent;
	
	public ChartList getiChartContent() {
		return iChartContent;
	}

	public void setiChartContent(ChartList iChartContent) {
		this.iChartContent = iChartContent;
	}

	public Friend(String aName,String aUserId,byte[] aHeadIcon){
		iName = aName;
		iUserID = aUserId;
		iHeadIcon = aHeadIcon;
		iChartContent = new ChartList();
	}
	
	public String getiName() {
		return iName;
	}


	public void setiName(String iName) {
		this.iName = iName;
	}


	public String getiUserID() {
		return iUserID;
	}


	public void setiUserID(String iUserID) {
		this.iUserID = iUserID;
	}


	public byte[] getiHeadIcon() {
		return iHeadIcon;
	}


	public void setiHeadIcon(byte[] iHeadIcon) {
		this.iHeadIcon = iHeadIcon;
	}
	

	
	
	//序列化
	public void serialize(){
		
	}
	//反序列话
	public void deserialize(){
		
	}
	
	public void addChart(int aType,Object aData){
		iChartContent.addChart(System.currentTimeMillis(),(byte) aType, aData);
	}
}
