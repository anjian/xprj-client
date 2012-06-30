package india.xxoo.ui;

import india.xxoo.adapter.MyGraphics;
import india.xxoo.util.Common;

public abstract class ScrollAble extends Component {
	
	int iShowOffset;
	int iContentHeigh ;
	static final int LENGTH = 3;
	
	public ScrollAble(int aX, int aY, int aWidth, int aHeigh) {
		super(aX, aY, aWidth, aHeigh);
		iContentHeigh = aHeigh;
	}
	
	public boolean eventChangeView(int aIncrement){
		if(aIncrement < 0){
			aIncrement = Math.max(-iShowOffset,aIncrement);
		}else{
			aIncrement = Math.max(iContentHeigh - iYHeight[1] - iShowOffset,aIncrement);
		}
		if(aIncrement == 0){
			return false;
		}else{
			iShowOffset += aIncrement;
			return true;
		}
	}
	//绘制滚动条的部分
	public void DrawScrollBar(MyGraphics g){
		g.saveOffset(iXWidth[0], iYHeight[0]);
		try{
			//滚动条长度
			int scrollBarLength = iYHeight[1] * iYHeight[1] / iContentHeigh;
			//滚动条的偏移
			int scrollBarOffset = iYHeight[1] * iShowOffset / iContentHeigh;
			g.setColor(0x000000);
			g.fillRect(iXWidth[1] - LENGTH - 1, scrollBarOffset, LENGTH, scrollBarLength);
		}catch (Exception e) {
			e.printStackTrace();
		}
		g.resetOffset();
	}
	
	public boolean isRectInScreen(short[] aRect){
		return Common.isTwoRectCollision(aRect[0], aRect[1], aRect[2], aRect[3], iXWidth[0], iShowOffset, iXWidth[1], iYHeight[1]);
	}
}
