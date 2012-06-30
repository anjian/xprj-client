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
		//int数组
		if(aArray instanceof int[]){
			length = ((int[]) aArray).length;
			sTemp = new int[length + aAddSize];
		}
		//byte[]数组
		
		//short数组
		if(aArray instanceof short[]){
			length = ((short[]) aArray).length;
			sTemp = new short[length + aAddSize];
		}
		System.arraycopy(aArray, 0, sTemp, 0, length);
		return sTemp;
	}
	
	 /**
     * <p>对chars进行折断，支持分词</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by yangxing on 2010-10-20）</li>
     * </ol>
     * @param aChars		需要处理的字符数组
     * @param aStartIdxs	开始位置
     * @param aLayoutWidth		给定的切割宽度
     * @param aFont			给定的字体，如果为空表示字体由aFontIndex来确定
     * @param aFontIndex	给定的字体的缓存位置
     * @param aLineWidth	参数型的返回值，传进来排版区域的最大宽度，传出去的是实际用来排文字的宽度
     * @return	需要换行处理的字符下标
     */
    public static final int calculateCharsWithPixel(String aString ,int aStartIndex, int aLayoutWidth,MyFont aFont,int[] aLineWidth){
    	//aMaxWidth与aLineWidth[2]的意义不一样，前者是当前可以排文字的区域，后者是当前行的宽度,当一个单词的宽度比第一个宽度要大的时候，还要与第二个比较一下，
    	//如果比这两个都要宽，那么就应该强制的在一开始就进行换行
    	if(aString == null || aLayoutWidth <= 0 || aStartIndex >= aString.length() || aLineWidth == null || aLineWidth[0] <= 0){
    		return -1;
    	}
    	//下一个字符的宽度
    	int sNextCharWidth = 0;
    	//当前计算的字符的宽度和
    	int sCurrentCharsWidth = 0;
    	//分词标记
    	int sSpaceOffset = -1;
    	//记录分词标记位置到开始位置的距离
    	int sTempWidthBeforeSpace = 0;
    	//记录排版区域的最大宽度
    	int sLineWidth = -1;
    
    	if(aLineWidth != null){
    		sLineWidth = aLineWidth[0];
    		aLineWidth[0] = 0;
    	}
		while(aStartIndex < aString.length()){
			//来了英文的字符，就把分词标记打上
			if(isCannotSplitWord(aString.charAt(aStartIndex)) && sSpaceOffset == -1){
				sSpaceOffset = aStartIndex;
				sTempWidthBeforeSpace = sCurrentCharsWidth;
			}
			//由英文变成中文的时候，就把标记取消
			else if(sSpaceOffset != -1 && !isCannotSplitWord(aString.charAt(aStartIndex)) && isCannotSplitWord(aString.charAt(aStartIndex - 1))){
				//对spaceOffset和tempWidthBeforeSpace进行赋值，根据前后两个字符的类型判断，是否要标记
				sSpaceOffset = -1;
			}
			sNextCharWidth = aFont.charWidth(aString.charAt(aStartIndex));
			sCurrentCharsWidth += sNextCharWidth;
			
			if(sCurrentCharsWidth > aLayoutWidth){
				//分词标记有效，那么就直接返回标记中代表的位置
				//sLineWidth为所在行的最大宽度，如果当前排版的最大宽度等于行宽，而且分词的标记在行的最开始
				//即一个单词特别的长，都超过行宽了，那么不得不截断处理
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
        return ((aChar >= 0x41 && aChar <= 0x7A)||(aChar >= 0xC0 && aChar <= 0x2AF)||//拉丁字母
                (aChar >= 0x0300 && aChar <= 0x0303) || (aChar == 0x0323)||//越南语的音标
                (aChar >= 0x0410 && aChar <= 0x044F)|| (aChar == 0x0401) || (aChar == 0x0451)||//俄文
                (aChar >= 0x1D00 && aChar <= 0x1EFF)||(aChar >= 0x2C60 && aChar <= 0x2C7F)||//拉丁字母
                (aChar >= 0xA720 && aChar <= 0xA7FF)||(aChar >= 0xFB00 && aChar <= 0xFB06)||//拉丁字母
                (aChar >= '0' && aChar <= '9'));//数字
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
