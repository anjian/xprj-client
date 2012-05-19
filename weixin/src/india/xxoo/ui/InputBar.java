package india.xxoo.ui;

import india.xxoo.adapter.Keybord;
import india.xxoo.adapter.MyGraphics;
import india.xxoo.main.Main;
import india.xxoo.util.Common;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;



public class InputBar extends Component{
	String iInputText;
	
	public InputBar(int aX , int aY , int aWidth , int aHeigh) {
		super(aX, aY, aWidth, aHeigh);
		
	}
	
	public void draw(MyGraphics g) {
		//背景
		int sColor = isFocus() ? CURRENT_FOCUSE_COLOR : 0xffffff;
				
		g.setColor(sColor);
		g.fillRect(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);
		//边框
		g.setColor(0x000000);
		g.drawRect(iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1]);
		//文字
		if(!Common.isNull(iInputText)){
			g.drawStringEx(iInputText, iXWidth[0], iYHeight[0], iXWidth[1], iYHeight[1], 1, 2);
		}
	}

	public boolean handleEvent(int aKeyType, int aKeyCode) {
		boolean sResult = false;
		if(isFocus()){
			switch(aKeyCode){
			case Keybord.CMD_OK:
				TextBox sTextBox = new TextBox("你想说", iInputText, 150, TextField.ANY);
				final Command sOK = new Command("确定", Command.OK, 1);
				final Command sCancel = new Command("取消", Command.CANCEL, 1);
				
				sTextBox.addCommand(sOK);
				sTextBox.addCommand(sCancel);
				sTextBox.setCommandListener(new CommandListener() {
					public void commandAction(Command arg0, Displayable arg1) {
						if(arg0 == sOK){
							TextBox sTextBox = (TextBox) arg1;
							iInputText = sTextBox.getString();
							Display.getDisplay(Main.iMain).setCurrent(Main.iMainCanvas);
						}else if(arg0 == sCancel){
							Display.getDisplay(Main.iMain).setCurrent(Main.iMainCanvas);
						}
					}
				});
				
				Display.getDisplay(Main.iMain).setCurrent(sTextBox);
				sResult = true;
				break;
			case Keybord.CMD_LEFT:
				ControCenter.act(ControCenter.SEED_MESSAGE,iInputText);
				sResult = true;
				break;
			}
		}
		return sResult;
	}

	public boolean isFocusAble() {
		return true;
	}
}
