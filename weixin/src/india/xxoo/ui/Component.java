package india.xxoo.ui;


import india.xxoo.adapter.MyGraphics;
import india.xxoo.main.Main;

public abstract class Component {
	/**
	 * x����Ϳ��
	 */
	short[] iXWidth;
	/**
	 * y����͸߶�
	 */
	int[] iYHeight;
	
	//���ܾ۽�
	public static final int FOCUSE_TYPE_UNABLE = -1;
	//û�о۽�
	public static final int FOCUSE_TYPE_UNFOCOUS = 0;
	//�۽�
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
	
	//��ǰ������ѡ��ʱ�Ľ������ɫ
	public static final int CURRENT_FOCUSE_COLOR = 0x0000ff;
	//��ǰ��������ѡ��ʱ�Ľ������ɫ
	public static final int NOT_CURRENT_FOCUSE_COLOR = 0x00ff00;
}
