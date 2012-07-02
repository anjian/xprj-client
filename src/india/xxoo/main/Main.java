package india.xxoo.main;
import india.xxoo.ui.MainCanvas;
import india.xxoo.ui.UIManager;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;



public class Main extends MIDlet {
	
	public static MIDlet iMain;
	public static MainCanvas iMainCanvas;
	
	public Main() {
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		 UIManager c = new UIManager(this);
		 c.displayChartList();
	}

}
