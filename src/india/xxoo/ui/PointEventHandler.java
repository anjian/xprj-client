package india.xxoo.ui;

public class PointEventHandler extends EventHandler{
	private PointEventHandler() {
	}
	
	public synchronized EventHandler getEventHandler(){
		if(iEventHandler == null){
			iEventHandler = new PointEventHandler();
		}
		return iEventHandler;
	}
	
	public int handle(int aKeyType, int aKeyCode) {
		return 0;
	}
}
