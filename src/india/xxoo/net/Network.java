package india.xxoo.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import cn.uc.util.io.BufferOutputStream;

public class Network implements Runnable{
	private SocketConnection iConnection;
	private InputStream iInputStream;
	private OutputStream iOutputStream;
	private boolean exit = false;
	private BufferOutputStream iBuffOut;
	private final static int OUT_BUFFER_SIZE = 1024;
	private NetWorkListener iListener;
	
	public void setListener(NetWorkListener aListener){
		iListener = aListener;
	}
	
	public void connect(String aUrl,String aPort){
		try{
			iConnection = (SocketConnection)Connector.open("socket://127.0.0.1:6060");
			iConnection.setSocketOption(SocketConnection.LINGER, 0);
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		new Thread(this).start();
	}
	
	public InputStream getInputStream(){
		if(iInputStream == null){
			try{
				iInputStream = iConnection.openInputStream();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return iInputStream;
	}
	
	private OutputStream getOutputStream(){
		if(iOutputStream == null){
			try{
				iOutputStream = iConnection.openOutputStream();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return iOutputStream;
	}
	
	public void sendData(byte[] aData){
		try{
			iBuffOut.write(aData);
		}catch (IOException e){
			iListener.onError(e.toString());
			return;
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//连接
		try{
			getInputStream();
			iBuffOut = new BufferOutputStream(getOutputStream(),OUT_BUFFER_SIZE);
		}catch(Exception e){
			iListener.onError(e.toString());
			return;
		}
		iListener.onConnected();
		long sTime = System.currentTimeMillis();
		//开始监听
		while(!exit){
			try{
				int sSize = iInputStream.available();
				if(sSize > 0){
					iListener.onReceivedMessage(sSize);
				}
				if(iBuffOut.isFull() || System.currentTimeMillis() - sTime > 2000){
					sTime = System.currentTimeMillis();
					iBuffOut.flush();
					iListener.onSentMessage();
				}
				Thread.sleep(500);
			}catch(Exception e){
				iListener.onError(e.toString());
			}
		}
		onClosed();
	}
	//关闭网络连接
	public void close(){
		exit = true;
	}
	//关闭网络连接时的回调方法
	private void onClosed(){
		//清空缓存
		try{
			iBuffOut.close();
			iInputStream.close();
			iOutputStream.close();
			iConnection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			iBuffOut = null;
			iInputStream = null;
			iOutputStream = null;
			iConnection = null;
		}
		iListener.onClosed();
	}
}	

