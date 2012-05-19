package india.xxoo.ui;

import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;
import india.xxoo.util.Common;

public class BottomBar extends Component{
	
	public static final int PADDING = 2;
	public static final int CHILD_PADDING = 2;
	
	//����
	short[][] iRect;
	//��ʾ������
	String[] iText;
	//��ʾ��ͼƬ
	String[] iImgUrl[];
	//���Ƴ���
	int[] iControlFlag;
	//ѡ�еĺ��ӵ��±�
	byte iSelectIndex;
	//���ӵ�����
	byte iChildCount;
	
	public BottomBar(int aX , int aY , int aWidth , int aHeigh) {
		super(aX, aY, aWidth, aHeigh);
		iText = new String[]{"�˵�","��ҳ"};
		iControlFlag = new int[]{ControCenter.OPEN_MENU, ControCenter.OPEN_HOME};
		iChildCount = 2;
		iRect = new short[iChildCount][4];
		layout();
	}
	
	public void layout(){
		int sCurrentX = PADDING;
		int sCurrentY = PADDING;
		
		int sRemaind = iXWidth[1] - PADDING * 2;
		int sInterval;
		//����ÿ��Ԫ�صĴ�С
		for(int i = 0 ; i < iChildCount ; i++){
			iRect[i][2] = (short) MyFont.getMyFont().stringWidth(iText[i]);
			sRemaind -= iRect[i][2] + CHILD_PADDING * 2;
		}
		sInterval = sRemaind / (iChildCount - 1);
		
		for(int i = 0 ; i < iChildCount ; i++){
			//x,y
			iRect[i][0] = (short) sCurrentX;
			iRect[i][1] = (short) sCurrentY;
			//h
			iRect[i][3] = (short) iYHeight[1];
			
			sCurrentX += iRect[i][2] + CHILD_PADDING * 2 + sInterval;
		}
	}
	
	public void addCommand(String aText,String aImgUrl,int aContrlFlag){
		
	}
	
	public void draw(MyGraphics g) {
		g.setColor(0x231231);
		g.fillRect(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);
		
		g.saveOffset(iXWidth[0], iYHeight[0]);
		try{
			for(int i = 0 ; i < iChildCount ; i++){
				//������
				if(!Common.isNull(iText[i])){
					g.setColor(0xffffff);
					g.drawStringEx(iText[i], iRect[i][0], iRect[i][1], iRect[i][2], iRect[i][3], 2, 2);
				}
				//��ͼƬ
				//!~
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			g.resetOffset();
		}
	}

	public boolean handleEvent(int aKeyType, int aKeyCode) {
		boolean sResult = false;
		return sResult;
	}

	public boolean isFocusAble() {
		return false;
	}

}
