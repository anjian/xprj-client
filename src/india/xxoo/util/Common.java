package india.xxoo.util;

import india.xxoo.adapter.MyFont;

import java.util.Vector;


public class Common {
	public static boolean isNull(String aString){
		return aString == null || aString.length() == 0;
	}
	public static Object extendArray(Object aArray,int aAddSize){
		Object sTemp = null;
		int length = 0;
		if(aArray == null){
			return null;
		}
		//int����
		if(aArray instanceof int[]){
			length = ((int[]) aArray).length;
			sTemp = new int[length + aAddSize];
		}
		//byte[]����
		
		//short����
		if(aArray instanceof short[]){
			length = ((short[]) aArray).length;
			sTemp = new short[length + aAddSize];
		}
		System.arraycopy(aArray, 0, sTemp, 0, length);
		return sTemp;
	}
	
	 /**
     * <p>��chars�����۶ϣ�֧�ִַ�</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by yangxing on 2010-10-20��</li>
     * </ol>
     * @param aChars		��Ҫ������ַ�����
     * @param aStartIdxs	��ʼλ��
     * @param aLayoutWidth		�������и���
     * @param aFont			���������壬���Ϊ�ձ�ʾ������aFontIndex��ȷ��
     * @param aFontIndex	����������Ļ���λ��
     * @param aLineWidth	�����͵ķ���ֵ���������Ű����������ȣ�����ȥ����ʵ�����������ֵĿ��
     * @return	��Ҫ���д�����ַ��±�
     */
    public static final int calculateCharsWithPixel(String aString ,int aStartIndex, int aLayoutWidth,MyFont aFont,int[] aLineWidth){
    	//aMaxWidth��aLineWidth[2]�����岻һ����ǰ���ǵ�ǰ���������ֵ����򣬺����ǵ�ǰ�еĿ��,��һ�����ʵĿ�ȱȵ�һ�����Ҫ���ʱ�򣬻�Ҫ��ڶ����Ƚ�һ�£�
    	//�������������Ҫ����ô��Ӧ��ǿ�Ƶ���һ��ʼ�ͽ��л���
    	if(aString == null || aLayoutWidth <= 0 || aStartIndex >= aString.length() || aLineWidth == null || aLineWidth[0] <= 0){
    		return -1;
    	}
    	//��һ���ַ��Ŀ��
    	int sNextCharWidth = 0;
    	//��ǰ������ַ��Ŀ�Ⱥ�
    	int sCurrentCharsWidth = 0;
    	//�ִʱ��
    	int sSpaceOffset = -1;
    	//��¼�ִʱ��λ�õ���ʼλ�õľ���
    	int sTempWidthBeforeSpace = 0;
    	//��¼�Ű�����������
    	int sLineWidth = -1;
    
    	if(aLineWidth != null){
    		sLineWidth = aLineWidth[0];
    		aLineWidth[0] = 0;
    	}
		while(aStartIndex < aString.length()){
			//����Ӣ�ĵ��ַ����Ͱѷִʱ�Ǵ���
			if(isCannotSplitWord(aString.charAt(aStartIndex)) && sSpaceOffset == -1){
				sSpaceOffset = aStartIndex;
				sTempWidthBeforeSpace = sCurrentCharsWidth;
			}
			//��Ӣ�ı�����ĵ�ʱ�򣬾Ͱѱ��ȡ��
			else if(sSpaceOffset != -1 && !isCannotSplitWord(aString.charAt(aStartIndex)) && isCannotSplitWord(aString.charAt(aStartIndex - 1))){
				//��spaceOffset��tempWidthBeforeSpace���и�ֵ������ǰ�������ַ��������жϣ��Ƿ�Ҫ���
				sSpaceOffset = -1;
			}
			sNextCharWidth = aFont.charWidth(aString.charAt(aStartIndex));
			sCurrentCharsWidth += sNextCharWidth;
			
			if(sCurrentCharsWidth > aLayoutWidth){
				//�ִʱ����Ч����ô��ֱ�ӷ��ر���д����λ��
				//sLineWidthΪ�����е�����ȣ������ǰ�Ű������ȵ����п����ҷִʵı�����е��ʼ
				//��һ�������ر�ĳ����������п��ˣ���ô���ò��ضϴ���
				if(sSpaceOffset != -1 && (sTempWidthBeforeSpace != 0 || sLineWidth != -1 && sLineWidth != aLayoutWidth)){
					if(aLineWidth != null){
						aLineWidth[0] = sTempWidthBeforeSpace;
					}
					return sSpaceOffset;
				}
				break;
			}
			aStartIndex++;
		}
		if(aLineWidth != null){
			aLineWidth[0] = sCurrentCharsWidth - (aStartIndex < aString.length() ? sNextCharWidth : 0);
		}
    	return aStartIndex;
    }
    
    public static boolean isCannotSplitWord(char aChar){
        return ((aChar >= 0x41 && aChar <= 0x7A)||(aChar >= 0xC0 && aChar <= 0x2AF)||//������ĸ
                (aChar >= 0x0300 && aChar <= 0x0303) || (aChar == 0x0323)||//Խ���������
                (aChar >= 0x0410 && aChar <= 0x044F)|| (aChar == 0x0401) || (aChar == 0x0451)||//����
                (aChar >= 0x1D00 && aChar <= 0x1EFF)||(aChar >= 0x2C60 && aChar <= 0x2C7F)||//������ĸ
                (aChar >= 0xA720 && aChar <= 0xA7FF)||(aChar >= 0xFB00 && aChar <= 0xFB06)||//������ĸ
                (aChar >= '0' && aChar <= '9'));//����
    }
    
    
    public static void copyInto(Vector aVector , short[] aArray){
    	for(int i = 0 ; i < aVector.size() ; i++){
    		aArray[i] = (short) ((Integer)aVector.elementAt(i)).intValue();
    	}
    }
    
    public static final int calculateStringWithPixelInALine(String aString,int aMaxWidth, MyFont aFont){
   	 	int resIdx = aString.length();
        int stringWidth = aFont.stringWidth(aString);
        int cnCharWidth = aFont.chineseWordWidth();
        int startIdx = aMaxWidth / cnCharWidth;
        
        if (stringWidth <= aMaxWidth)
            startIdx = resIdx;
        if(startIdx >= resIdx) return resIdx ;
        int currentCharWidth = aFont.stringWidth(aString.substring(0, startIdx));
        int nextCharWidth = aFont.charWidth(aString.charAt(startIdx));

        while ((currentCharWidth + nextCharWidth) < aMaxWidth) {
            currentCharWidth+=nextCharWidth;
            startIdx++;
            if(startIdx > resIdx) break;
            nextCharWidth = aFont.charWidth(aString.charAt(startIdx));
        }
        return startIdx;
   }

    public static boolean isTwoRectCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
    	return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
    
    public static int layoutText(String aText,int aWidth, int aLeading, MyFont aFont){
    	int sIndex = 0;
    	int sHeight = 0;
    	do{
    		sIndex = calculateCharsWithPixel(aText,sIndex,aWidth,aFont,new int[]{aWidth});
    		sHeight += aFont.getHeight() + aLeading;
    	}while(sIndex < aText.length() && sIndex != -1);
    	return sHeight;
    }
    
    public static String formatTime(long aTime){
    	return "8:00";
    }
    
}
