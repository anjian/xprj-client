package india.xxoo.ui;

import india.xxoo.adapter.Keybord;
import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;
import india.xxoo.dao.Manager;
import india.xxoo.main.Main;
import india.xxoo.util.Common;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Image;




public class PageBar extends ScrollAble{
	
	public static final int PADDING = 5;
	public static final int CHILD_PADDING = 5;
	public static final int LEDDING = 2;
	//������ʾ���
	private static int gTextWidth;
	
	//ҳ��bar������
	private byte iType;
	public static final int CHART_PAGE_BAR = 0;
	public static final int HISTOTY_PAGE_BAR = 1;
	public static final int CONTACT_PAGE_BAR = 2;
	
	short[][] iRect; 
	int iChildCount;
	int iSelectId = -1;
	int[] iID;
	
	public PageBar(int aX , int aY , int aWidth , int aHeigh ,int aType) {
		super(aX, aY, aWidth, aHeigh);
		//���ֵ���ʾ����,�ݶ�s
		gTextWidth = aWidth - PADDING * 2 -  Manager.iIconSize[0] * 2;
		iType = (byte) aType;
		iRect = new short[16][4];
		iID = new int[16];
	}
	
	public void layout(){
		int sLayoutWidth = iXWidth[1] - 2 * PADDING;
		int sCurrentX = PADDING;
		int sCurrentY = PADDING;
		int aChildHeight = 0;
		
		for(int i = 0 ; i < iChildCount ; i++){
			//��ʼ�����ӵĸ߿�
			//��Ҫ�ع�??
			switch(iType){
			case CHART_PAGE_BAR:
				//������������
				String sText = Main.iMainCanvas.iDataManager.getiCurrentFriend().getiChartContent().getCharTextById(iID[i]);
				aChildHeight = Common.layoutText(sText,gTextWidth,LEDDING,MyFont.getMyFont());
				break;
			case HISTOTY_PAGE_BAR:
				//����ͷ��ĸ߶�
				aChildHeight = Manager.iIconSize[1];
				break;
			case CONTACT_PAGE_BAR:
				aChildHeight = Manager.iIconSize[1];
				break;
			}
			//ȷ������
			
			iRect[i][0] = (short) sCurrentX;
			iRect[i][1] = (short) sCurrentY;
			iRect[i][2] = (short) sLayoutWidth;
			iRect[i][3] = (short) aChildHeight;			
			sCurrentY += aChildHeight;
		}
	}
	
	public void drawChild(MyGraphics g,int aChildIndex){
		switch(iType){
		case CHART_PAGE_BAR://��Ҫ�ع�
			int sColor = 0;
			if(iSelectId == aChildIndex){
				sColor = isFocus()? CURRENT_FOCUSE_COLOR : NOT_CURRENT_FOCUSE_COLOR;
			}else{
				sColor = 0xF5F5F5;
			}
			g.setColor(sColor);
			g.fillRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			
			g.setColor(0x0000ff);
			g.drawRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			
			//������
			g.setColor(0x000000);
			
			break;
		case HISTOTY_PAGE_BAR:
			if(iSelectId == aChildIndex){
				sColor = isFocus()? CURRENT_FOCUSE_COLOR : NOT_CURRENT_FOCUSE_COLOR;
				g.setColor(sColor);
				g.fillRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			}
			
			g.setColor(0xff0000);
			g.drawRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			//ͷ��
			Image sHead = null;
			int sX = iRect[aChildIndex][0];
			try {
				sHead = Image.createImage("/1.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(sHead != null){
				g.drawImage(sHead,sX,iRect[aChildIndex][1]);
			}
			String sTime = Common.formatTime(12332);
			int sTimeWidth = MyFont.getMyFont().stringWidth(sTime);
			
			sX += Manager.iIconSize[0] + CHILD_PADDING;
			
			g.setColor(0x000000);
			//����
			String sText = Main.iMainCanvas.iDataManager.getFiendById(iID[aChildIndex]).getiName();
			g.drawStringEx(sText, sX, iRect[aChildIndex][1], iRect[aChildIndex][2] - sX - sTimeWidth -CHILD_PADDING, iRect[aChildIndex][3], 1, 1);
			
			//ʱ��
			g.drawStringEx(sTime, iRect[aChildIndex][0] + iRect[aChildIndex][2] - sTimeWidth, iRect[aChildIndex][1] , sTimeWidth, iRect[aChildIndex][3], 1, 1);
			//����
			sText = "����";
			g.drawStringEx(sText, sX, iRect[aChildIndex][1], iRect[aChildIndex][2] - sX , iRect[aChildIndex][3], 1, 3);
			
			break;
		case CONTACT_PAGE_BAR:
			if(iSelectId == aChildIndex){
				sColor = isFocus()? CURRENT_FOCUSE_COLOR : NOT_CURRENT_FOCUSE_COLOR;
				g.setColor(sColor);
				g.fillRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			}
			
			g.setColor(0xff0000);
			g.drawRect(iRect[aChildIndex][0], iRect[aChildIndex][1], iRect[aChildIndex][2], iRect[aChildIndex][3]);
			//ͷ��
			sHead = null;
			sX = iRect[aChildIndex][0];
			try {
				sHead = Image.createImage("/1.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(sHead != null){
				g.drawImage(sHead,sX,iRect[aChildIndex][1]);
			}
			sX += Manager.iIconSize[0] + CHILD_PADDING;
			
			g.setColor(0x000000);
			//����
			sText = Main.iMainCanvas.iDataManager.getFiendById(iID[aChildIndex]).getiName();
			g.drawStringEx(sText, sX, iRect[aChildIndex][1], iRect[aChildIndex][2] - sX, iRect[aChildIndex][3], 1, 2);
			//����ǩ��
			
			
			break;
		}
	}
	
	public void draw(MyGraphics g) {
		g.saveClipe(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);
		g.setColor(0xeeeeee);
		g.fillRect(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);

		g.saveOffset(iXWidth[0], iYHeight[0] - iShowOffset);
		try{
			for(int i = 0 ; i < iChildCount ; i ++){
				if(isRectInScreen(iRect[i])){
					drawChild(g,i);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			g.resetOffset();
			g.resetClipe();
		}
		DrawScrollBar(g);
	}

	public boolean handleEvent(int aKeyType, int aKeyCode) {
		boolean sResult = false;
		switch(aKeyCode){
			case Keybord.NAV_UP:
				sResult = moveFocuse(-1);
				break;
			case Keybord.NAV_DOWN:
				sResult = moveFocuse(1);
				break;
			case Keybord.CMD_OK:
				break;
		}
		return sResult;
	}
	
	private boolean moveFocuse(int aDirect){
		//�����ǰû�н��㣬��ôѡ��һ��
		boolean sRes = false;
		int sSelectId = iSelectId;
		if(sSelectId == -1){
			sSelectId = searchIndex(aDirect);
		}else{
			sSelectId += aDirect;
		}
		if(sSelectId < 0 || sSelectId >= iChildCount){
			sRes = false;
		}else if(isRectInScreen(iRect[sSelectId])){
			//�����ڵ�ǰ��Ļ
			iSelectId = sSelectId;
			sRes = true;
		}else{
			sRes = eventChangeView(iYHeight[1] / 4 * aDirect);
		}
		return sRes;
	}
	
	private int searchIndex(int aDirect){
		return aDirect == 1 ? 0 : (iChildCount - 1);//�������Ҫ�жϸý����Ƿ���Ծ۽�
	}

	public boolean isChildFocusAble(int aChildIndex) {
		return true;
	}
	
	public void addChild(int aId){
		if(iChildCount >= iID.length){
			//��������
			iID = (int[]) Common.extendArray(iID, iID.length);
		}
		iID[iChildCount] = aId;
		iChildCount ++;
	}

	public boolean isFocusAble() {
		return false;
	}
	

//	public void layout(){
//		int sXOffset = (iXWidth[1] -  PADDING * 2) / 10;
//		int sTextElementWidth = sXOffset * 6;
//		int sSoundElemtnWidth = sXOffset * 4;
//		
//		int sCurrentX = sXOffset;
//		int sCurrentY = PADDING;
//		Vector sFlodVector = new Vector(); 
//		for(int i = 0 ; i < iChildCount ; i++){
//			//w
//			iRect[i][2] = (short) sTextElementWidth;
//			
//			if(iTypes[i] == 0){
//				//x
//				iRect[i][0] = (short) sCurrentX;
//			}else if(iTypes[i] == 1){
//				iRect[i][0] = (short) (iXWidth[1] -  PADDING - iRect[i][2] - sXOffset);
//			}
//			iRect[i][1] = (short) sCurrentY;
//			int sCurrentTextIndex = 0;
//			int sLastTextIndex = 0;
//			int sLineWidth = iRect[i][2] - CHILD_PADDING * 2;
//			int sRemaind = sLineWidth;
//			int[] aMaxWidth = {sLineWidth};
//			
//			int sHeight = 0;
//			do{
//				//������,������ܳ���ͼ�Ļ���
//				sCurrentTextIndex = Common.calculateCharsWithPixel(iChartString[i], sCurrentTextIndex, sRemaind,MyFont.getMyFont(), aMaxWidth);
//				if(sCurrentTextIndex ==  sLastTextIndex || sCurrentTextIndex == -1){
//					//��Ҫǿ�ƻ���
//					break;
//				}else if(sCurrentTextIndex >= iChartString[i].length()){
//					//�Ѿ�������
//					if(sFlodVector.size() > 1){
//						short[] sFlod = new short[sFlodVector.size()];
//						Common.copyInto(sFlodVector,sFlod);
//						iFoldIndex[i] = sFlod;
//					}else{
//						
//					}
//					sFlodVector.removeAllElements();
//					
//					sHeight += MyFont.getMyFont().getHeight();
//					break;
//				}else{
//					sFlodVector.addElement(new Integer(sCurrentTextIndex));
//					sLastTextIndex = sCurrentTextIndex;
//					sHeight += MyFont.getMyFont().getHeight() + PADDING;
//				}
//			}while(true);
//			
//			//h
//			iRect[i][3] = (short) sHeight;
//			sCurrentY += sHeight + PADDING;
//		}
//		iContentHeigh = sCurrentY;
//	}
}
