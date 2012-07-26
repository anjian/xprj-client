package india.xxoo.main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import india.xxoo.net.NetWorkListener;
import india.xxoo.net.Network;
import india.xxoo.ui.MainCanvas;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;



public class Main extends MIDlet{
	
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
	Network n ;
	protected void startApp() throws MIDletStateChangeException {
//		 UIManager c = new UIManager(this);
//		 c.displayAddFriend();

	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onConnected()
	 */
	public void onConnected() {
		System.out.println("服务器连接成功");
		try{
			n.sendData("我日".getBytes("utf-8"));
		}catch (UnsupportedEncodingException e){
		}
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onClosed()
	 */
	public void onClosed() {
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onReceivedMessage()
	 */
	public void onReceivedMessage(int aSize) {
		InputStream i = n.getInputStream();
		byte[] sData = new byte[aSize];
		try{
			i.read(sData);
		}catch (IOException e){
		}
		try{
			System.out.println(new String(sData,"utf-8"));
		}catch (UnsupportedEncodingException e){
		}
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onSentMessage()
	 */
	public void onSentMessage() {
		
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onError(java.lang.String)
	 */
	public void onError(String aError) {
	}
}
