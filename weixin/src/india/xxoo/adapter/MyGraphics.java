package india.xxoo.adapter;

import india.xxoo.util.Common;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class MyGraphics{
	private static MyGraphics iMyGraphics;
	private static Graphics  iG;
	private static MyFont iFont;
	//ƫ�ƣ�����Ҫʹ�ö�ջʵ��!~
	private short iXOffset;
	private int iYOffset;
	private short iTempXOffset;
	private int iTempYOffset;

	public void saveOffset(int aXOffset, int aYOffset){
		iTempXOffset = iXOffset;
		iTempYOffset = iYOffset;

		iXOffset = (short) aXOffset;
		iYOffset = aYOffset;
	}
	public void resetOffset(){
		//Ҫʹ�ö�ջʵ��
		iXOffset = iTempXOffset;
		iYOffset = iTempYOffset;
	}
	
	//������������Ҫʹ�ö�սʵ��
	private short iclipX;
	private short iclipY;
	private short iclipW;
	private short iclipH;
	private short iTempClipX;
	private short iTempClipY;
	private short iTempClipW;
	private short iTempClipH;
	
	public void saveClipe(int aClipX,int aClipY,int aClipW,int aClipH){
		iTempClipX = iclipX;
		iTempClipY = iclipY;
		iTempClipW = iclipW;
		iTempClipH = iclipH;
		
		iclipX = (short) aClipX;
		iclipX = (short) aClipY;
		iclipX = (short) aClipW;
		iclipX = (short) aClipH;
	}
	
	public void resetClipe(){
		iclipX = iTempClipX;
		iclipY = iTempClipY;
		iclipW = iTempClipW;
		iclipH = iTempClipH;
	}
	
	private MyGraphics(Graphics aG){
		iFont = MyFont.getMyFont();
		iG = aG;
	}

	public synchronized  static MyGraphics getGraphics(Graphics aG){
		if(iMyGraphics == null){
			iMyGraphics = new MyGraphics(aG);
		}else{
			iG = aG;
		}
		return iMyGraphics;
	}

	public void setColor(int aColor){
		iG.setColor(aColor);
	}

	public void fillRect(int aX, int aY, int aWidth,int aHeigh){
		iG.fillRect(aX + iXOffset, aY + iYOffset, aWidth, aHeigh);
	}


	public void drawRect(int aX, int aY, int aWidth,int aHeigh){
		iG.drawRect(aX + iXOffset, aY + iYOffset, aWidth, aHeigh);
	}

	/**
	 * ָ����ͼ���򲢿������ö��뷽ʽ�����ַ���
	 * @param aStr String ���Ƶ��ַ���
	 * @param aX int �����Ͻ�x����
	 * @param aY int �������Ͻ�y����
	 * @param aWidth int ������
	 * @param aHeight int ����߶�
	 * @param aHAlignFlag int ˮƽ�����־ 1 -> �� 2 -> �� 3 -> ��
	 * @param aVAlignFlag int ��ֱ�����־ 1 -> �� 2 -> �� 3 -> ��
	 * @param drawFalg boolean �Ƿ�ʹ����ҳ����Ļ�ͼ�ӿ�
	 * @param aSetClipFlag int ���ü��������־ 0 -> ����
	 */
	public void drawStringEx(String aStr, int aX, int aY, int aWidth, int aHeight, int aHAlignFlag, int aVAlignFlag) {
		int sStrWidth = iFont.stringWidth(aStr);
		int sStrHeight = iFont.getHeight();

		if (sStrWidth >= aWidth) {
			aHAlignFlag = 1;
		}
		if (sStrHeight >= aHeight) {
			aVAlignFlag = 3;
		}

		int sFinalOffsetX = aX;
		int sFinalOffsetY = aY;
		int sOffsetX = 0;
		//ˮƽ����
		if (aHAlignFlag == 2) {
			sOffsetX = (((aWidth - sStrWidth) >> 1));
		}
		//ˮƽ����
		if (aHAlignFlag == 3) {
			sOffsetX = ((aWidth - sStrWidth));
		}

		sFinalOffsetX += sOffsetX > 0 ? sOffsetX : 0;

		int sInterval = 0;
		int sOffsetY = 0;
		sInterval = aHeight - sStrHeight;

		if (sInterval >= 0) {
			//��ֱ����
			if (aVAlignFlag == 2) {
				sOffsetY=(aHeight - sStrHeight+1) >> 1;//(((sInterval = ((aHeight - sStrHeight) >> 1)) == 0 ? 1 :sInterval));
			}
			//��ֱ����
			if (aVAlignFlag == 3) {
				sOffsetY = aHeight - sStrHeight;
			}
		} else {
			sInterval *= -1;
			sOffsetY = ((sInterval >> 1) == 0 ? 1 : (sInterval >> 1));
			sOffsetY *= -1;
		}
		sFinalOffsetY += sOffsetY;
		drawString(aStr, sFinalOffsetX, sFinalOffsetY, Graphics.TOP|Graphics.LEFT);
	}

	public void drawString(String aString , int aX , int aY , int aAnchor) {
		iG.drawString(aString, aX + iXOffset, aY + iYOffset,aAnchor);
	}
	public void drawLine(int i, int sPosY, int j, int sPosY2) {
		iG.drawLine(i, sPosY, j, sPosY2);
	}
	public void setClip(int iPosX, int iPosY, int iWidth, int iHeight) {
		iG.setClip( iPosX, iPosY,iWidth, iHeight);
	}
	public void drawShortCornerFrame(int aX, int aY , int aWith, int aHeight,int aType) {
		int sArcSize = 1;
		if(aType == 1){
			sArcSize = 2;
		}
		// mod by lihq 2010-9-27 : m-44654  
		//��
		drawLine(aX + sArcSize, aY, aX + aWith -sArcSize - 1, aY);
		//��
		drawLine(aX + sArcSize, aY + aHeight -1, aX + aWith -sArcSize - 1, aY + aHeight -1);
		//��
		drawLine(aX, aY + sArcSize, aX, aY + aHeight - 1 - sArcSize);
		//��
		drawLine(aX + aWith -1, aY + sArcSize,aX + aWith -1, aY + aHeight - 1 - sArcSize);

		if(aType == 1){
			drawDot(aX + 1, aY + 1);
			drawDot(aX + 1, aY + aHeight - 2);
			drawDot(aX + aWith - 2, aY + 1);
			drawDot(aX + aWith - 2, aY + aHeight - 2);
		}
	}
	private void drawDot(int aX, int aY) {
		drawLine(aX, aY, aX, aY);
	}

	public void fillTriangle(int aX1, int aY1, int aX2, int aY2, int aX3, int aY3) {
		iG.fillTriangle(aX1 + iXOffset, aY1 + iYOffset, aX2 + iXOffset, aY2 + iYOffset, aX3 + iXOffset,	aY3 + iYOffset);
	}

	public void drawStringInARow(String aDrawText, int aX, int aY, int aRowWidth, int aRowHeight, int aHAlignFlag, int aVAlignFlag,
			String aPostfixString , boolean aDrawFlag , int aSetClipFlag) {
		if(Common.isNull(aDrawText) || aRowWidth <= 0){
			return;
		}
		MyFont sFont = MyFont.getMyFont();
		if(aPostfixString == null)
			aPostfixString = "";
		L1:    	if(sFont.stringWidth(aDrawText) > aRowWidth){
			int sPostfixWidth = sFont.stringWidth(aPostfixString);
			if(sPostfixWidth >= aRowWidth){
				break L1;
			}
			int drawIdx = Common.calculateStringWithPixelInALine(aDrawText, aRowWidth - sPostfixWidth,sFont );
			if(drawIdx <= 0){
				break L1;
			}
			int sDrawStringWidth = sFont.stringWidth(aDrawText.substring(0, drawIdx));
			if(aHAlignFlag == 2){
				aX += (aRowWidth - sPostfixWidth - sDrawStringWidth)>>1;
			}
			drawStringEx(aDrawText.substring(0, drawIdx), aX, aY, sDrawStringWidth, aRowHeight, aHAlignFlag, aVAlignFlag);
			drawStringEx(aPostfixString, aX + sDrawStringWidth, aY, sPostfixWidth, aRowHeight, aHAlignFlag, aVAlignFlag);
			return;
		}

		drawStringEx(aDrawText, aX, aY, aRowWidth, aRowHeight, aHAlignFlag, aVAlignFlag);
	}
	public final void fillGradualColorRoundCornerRect(int aX , int aY, int aWidth, int aHeight,
    		int[] aColors, int aColorOffset, int aColorLength, int aOrientation){
    	int sCurrentColor = iG.getColor();
    	if(aOrientation == 1){
    		int sChunkH = aHeight / aColorLength ;
    		int sChunkOffsetH = aHeight % aColorLength ;
    		int sCurrentY = aY;
    		for(int i = 0 ; i < aColorLength ; i++){
    			int sH = sChunkH + (sChunkOffsetH-- > 0 ? 1: 0);
    			this.setColor(aColors[i + aColorOffset]);
    			if(i==0){
    				drawLine(aX+1, sCurrentY, aX+aWidth-2, sCurrentY);
    				fillRect(aX,sCurrentY + 1,aWidth,sH - 1);
    			}
    			if(i == aColorLength - 1){
    				this.fillRect(aX,sCurrentY,aWidth,sH - 1);
    				drawLine(aX+1, sCurrentY + sH - 1, aX+aWidth-2, 

    						sCurrentY + sH - 1);
    			}else{
    				this.fillRect(aX,sCurrentY,aWidth,sH);
    			}
    			sCurrentY += sH;
    		}
    	}else if(aOrientation == -1){
    		int sChunkW = aWidth / aColorLength ;
    		int sChunkOffsetW = aWidth % aColorLength ;
    		int sCurrentX = aX;
    		for(int i = 0 ; i < aColorLength ; i++){
    			int sW = sChunkW + (sChunkOffsetW-- > 0 ? 1: 0);
    			this.setColor(aColors[i + aColorOffset]);
    			fillRect(sCurrentX,aY,sW,aHeight);
    			sCurrentX += sW;
    		}
    	}
    	setColor(sCurrentColor);
    }
	
	public void drawImage(Image aImage, int aX , int aY){
		iG.drawImage(aImage, aX + iXOffset, aY + iYOffset, 20);
	}
}
