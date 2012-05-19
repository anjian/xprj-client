package india.xxoo.ui;


import india.xxoo.adapter.MyGraphics;
import india.xxoo.main.Main;

public abstract class Component {
	/**
	 * x坐标和宽度
	 */
	short[] iXWidth;
	/**
	 * y坐标和高度
	 */
	int[] iYHeight;
	
	//不能聚焦
	public static final int FOCUSE_TYPE_UNABLE = -1;
	//没有聚焦
	public static final int FOCUSE_TYPE_UNFOCOUS = 0;
	//聚焦
	public static final int FOCUSE_TYPE_FOCOUS = 1; 
	
	public abstract boolean isFocusAble();
	
	public boolean isFocus(){
		return Main.iMainCanvas.iFocusBar == this;
	}
	
	public Component(int aX , int aY , int aWidth , int aHeigh) {
		iXWidth = new short[]{(short) aX,(short) aWidth};
		iYHeight = new int[]{aY,aHeigh};
	}
	public abstract void draw(MyGraphics g);
	public abstract boolean handleEvent(int aKeyType,int aKeyCode);
	
	//当前容器被选中时的焦点的颜色
	public static final int CURRENT_FOCUSE_COLOR = 0x0000ff;
	//当前容器不被选中时的焦点的颜色
	public static final int NOT_CURRENT_FOCUSE_COLOR = 0x00ff00;
}
