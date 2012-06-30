package india.xxoo.adapter;

import javax.microedition.lcdui.Font;

public class MyFont {
	private Font iFont;
	private static MyFont iMyFont;
	
	private MyFont() {
		iFont = Font.getDefaultFont();
	}
	public synchronized  static MyFont getMyFont(){
		if(iMyFont == null){
			iMyFont = new MyFont();
		}
		return iMyFont;
	}
	
	public int stringWidth(String aString){
		return iFont.stringWidth(aString);//!~
	}
	
	public int getHeight(){
		return iFont.getHeight();//!~
	}
	
	public int charWidth(char aChar){
		return iFont.charWidth(aChar);
	}
	
	public int chineseWordWidth(){
		return iFont.charWidth('һ');
	}
	
}
