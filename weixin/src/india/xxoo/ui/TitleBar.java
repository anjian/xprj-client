package india.xxoo.ui;

import india.xxoo.adapter.Keybord;
import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;

public class TitleBar extends Component{

	public static final int PADDING = 2;
	public static final int CHIRLD_PADDING = 2;
	
	
	String[] iTitleNames;
	short[][] iTitleRect;
	//��title����
	byte iTitleCount;
	//�����id
	byte iFocusId ;
	
	public TitleBar(int aX , int aY , int aWidth , int aHeigh) {
		super(aX, aY, aWidth, aHeigh);
		//��ʱĬ������
		iTitleNames = new String[]{"�����¼","��ϵ��","�����Ƽ�"};
		iTitleCount = (byte) iTitleNames.length;
		iTitleRect = new short[iTitleCount][4];
		layout();
	}
	
	private void layout(){
		int sCurrentX = PADDING;
		int sCurrentY = PADDING;
		
		int sAverage = (iXWidth[1] - PADDING * (iTitleCount + 1))/ iTitleCount;
		
		for(int i = 0 ; i < iTitleCount ; i++){
			iTitleRect[i][0] = (short) sCurrentX;
			iTitleRect[i][1] = (short) sCurrentY;
			//�����ƽ����߶�ҪС
			if(MyFont.getMyFont().stringWidth(iTitleNames[i]) < sAverage){
				iTitleRect[i][2] = (short) sAverage;
			}else{
				//�����ƽ���߶�Ҫ��	
				//!~
			}
			iTitleRect[i][3] = (short)(iYHeight[1] - 2 * PADDING);
			
			sCurrentX += iTitleRect[i][2] + CHIRLD_PADDING;
		}
	}
	
	public void draw(MyGraphics g) {
		//����
		g.setColor(0xffffff);
		g.fillRect(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);
		//����
		for(int i = 0 ; i < iTitleCount; i++){
			//����
			if(iFocusId == i){
				g.setColor(0x0000ff);
			}else{
				g.setColor(0x00ff00);	
			}
			g.fillRect(iTitleRect[i][0], iTitleRect[i][1], iTitleRect[i][2], iTitleRect[i][3]);
			
			
			//��ǰѡ��
			if(iFocusId == i){
				g.setColor(NOT_CURRENT_FOCUSE_COLOR);
				g.fillRect(iTitleRect[i][0], iTitleRect[i][1], iTitleRect[i][2],3);
			}
			
			g.setColor(0x00ffff);
			g.drawRect(iTitleRect[i][0], iTitleRect[i][1], iTitleRect[i][2], iTitleRect[i][3]);
			//����
			g.setColor(0xffffff);
			g.drawStringEx(iTitleNames[i], iTitleRect[i][0], iTitleRect[i][1], iTitleRect[i][2], iTitleRect[i][3], 2, 2);
		}
	}

	public boolean handleEvent(int aKeyType, int aKeyCode) {
		boolean sResult = false;
		int sTempFocus = iFocusId;
		switch(aKeyCode){
		case  Keybord.NAV_LEFT:
			iFocusId --;
			if(iFocusId < 0){
				iFocusId = 0;
			}
			sResult = true;
			break;
		case  Keybord.NAV_RIGHT:
			iFocusId ++;
			if(iFocusId >= iTitleCount){
				iFocusId = (byte) (iTitleCount - 1);
			}
			sResult = true;
			break;
		default:
			break;
		}
		if(sTempFocus != iFocusId){
			upDataFocus();
		}
		return sResult;
	}

	private void upDataFocus() {
		switch(iFocusId){
		case 0:
			ControCenter.act(ControCenter.SHOW_HISTORY, null);
			break;
		case 1:
			ControCenter.act(ControCenter.SHOW_CONTACT, null);
			break;
		case 2:
			break;
		}
	}

	public boolean isFocusAble() {
		return false;
	}
	
	
}
