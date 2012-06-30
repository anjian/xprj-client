package india.xxoo.dao;

import java.util.Vector;

public class ChartList {
	
	public final static byte CHART_TYPE_IN_TEXT = 1;
	public final static byte CHART_TYPE_OUT_TEXT = -1;
	
	public final static byte CHART_TYPE_IN_SOUND = 2;
	public final static byte CHART_TYPE_OUT_SOUND = -2;
	
	public final static byte CHART_TYPE_IN_IMAGE = 3;
	public final static byte CHART_TYPE_OUT_IMAGE = -3;
	
	public static final int TIME_INTERVAL = 10000;
	
	//某段聊天记录的时间
	private Vector iTime;
	//聊天记录的了内容
	private Vector iDataContainer;
	
	public int getChartCount(){
		return iDataContainer.size();
	}
	
	private Object getChartDataByID(int aId){
		if(aId >= 0 && aId < iDataContainer.size()){
			return iDataContainer.elementAt(aId);
		}
		return null;
	}
	
	int addChart(long aTime,byte aType,Object aData){
		if(iTime == null){
			iTime = new Vector();
		}
		if(iDataContainer == null){
			iDataContainer = new Vector();
		}
		int sId = iTime.size();
		//超越了一个时间段
		if(sId > 1 && aTime - ((long[])iTime.elementAt(sId - 1))[0] > TIME_INTERVAL){
			((long[])iTime.elementAt(sId - 1))[1] = iDataContainer.size() - 1;
		}
		iTime.addElement(new long[]{aTime,0});
		Object[] sData = {new Byte(aType),aData};
		sId = iDataContainer.size();
		iDataContainer.addElement(sData);
		return sId;
	}
	
	public int getCharTypeById(int aId){
		Object sData = getChartDataByID(aId);
		if(!(sData instanceof Object[])){
			return -1;
		}
		return ((Byte)((Object[])sData)[0]).byteValue();
	}
	
	public Object getCharDataById(int aId){
		Object sData = getChartDataByID(aId);
		if(!(sData instanceof Object[])){
			return null;
		}
		return ((Object[])sData)[1];
	}
	
	public String getCharTextById(int aId){
		return (String) getCharDataById(aId);
	}
	
	public byte[] getCharImageById(int aId){
		return ( byte[]) getCharDataById(aId);
	}
	
	public byte[] getCharVoiceById(int aId){
		return ( byte[]) getCharDataById(aId);
	}
	
	public long getChartTimeById(int aId){
		int sStart = 0;
		int sEnd = 0;
		if(iTime == null || iTime.size() <= 0){
			return -1;
		}
		sStart = (int) ((long[])iTime.elementAt(0))[1];
		for(int i = 1 ; i < iTime.size() ; i++){
			sEnd = (int) ((long[])iTime.elementAt(i))[1];
			if(aId >= sStart && aId <= sEnd){
				return ((long[])iTime.elementAt(i))[0];
			}
		}
		return -1;
	}
}
