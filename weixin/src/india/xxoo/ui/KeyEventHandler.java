package india.xxoo.ui;

public class KeyEventHandler extends EventHandler{

	public static final int KEYCODE_UP = 0;
	public static final int KEYCODE_DOWN = 1;
	public static final int KEYCODE_LEFT = 2;
	public static final int KEYCODE_RIGHT = 3;
	
	public static final int KEYTYPE_PRESSED = 0;
	public static final int KEYTYPE_REPEATED = 1;
	public static final int KEYTYPE_RELEASED = 2;

	public int handle(int aKeyType, int aKeyCode) {
		return 0;
	}
	
	private KeyEventHandler() {
	}
	
	public synchronized EventHandler getEventHandler(){
		if(iEventHandler == null){
			iEventHandler = new KeyEventHandler();
		}
		return iEventHandler;
	}
}
