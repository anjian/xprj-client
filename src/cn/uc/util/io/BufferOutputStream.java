/**
 * BufferOutputStream.java
 * 
 * Copyright (c) 2004-2010 UC Mobile Ltd.
 * F/4,Building B,Guangzhou Xinxigang,No.16 Keyun Rd,Guangzhou,China
 * All right reserved.
 * 
 * Created on 2012-7-24
 */
package cn.uc.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * //TODO Administrator自动生成javadoc
 * <b>BufferOutputStream简介:</b>
 * <p>简要的描述此类的作用</p>
 *
 * <b>功能描述:</b>
 * <p>具体功能点描述，当添加新功能时，要求更新此描述</p>
 *
 * <b>修改历史</b>
 * <p>
 *  <ol>
 *   <li>创建（Added by Administrator on 2012-7-24）</li>
 *  </ol> 
 * </p>
 * @author Administrator
 * @version 1.0
 */
public class BufferOutputStream {
	private byte[] buff;
	private int pos;
	private OutputStream iOut;
	private boolean isFull;
	
	/**
	 * <p>
	 * 构造方法
	 * </p>
	 */
	public BufferOutputStream(OutputStream aOut,int aBufferSize) {
		iOut = aOut;
		buff = new byte[aBufferSize];
	}
	
	/**
	 * <p>
	 * 获取pos值
	 * </p>
	 * @return pos
	 */
	public boolean isFull() {
		return isFull;
	}
	
	public void write(byte[] aData) throws IOException{
		int sPos = 0;
		int sLen = 0;
		do{
			sLen = Math.min(buff.length - pos, aData.length - sPos);
			System.arraycopy(aData, sPos, buff, pos, sLen);
			sPos += sLen;
			pos += sLen;
			if(sPos < aData.length){
				synchronized (this){
					try{
						wait();
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}else{
				break;
			}
		}while(true);
	}
	
	public void flush() throws IOException{
		iOut.write(buff,0,pos);
		pos = 0;
		synchronized (this){
			notify();
		}
	}
	
	public void close() throws IOException{
		try{
			if(pos != 0){
				flush();
			}
		}catch (IOException e) {
			throw e;
		}finally{
			buff = null;
			iOut.close();
		}
	}
}
