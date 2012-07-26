/**
 * NetWorklistener.java
 * 
 * Copyright (c) 2004-2010 UC Mobile Ltd.
 * F/4,Building B,Guangzhou Xinxigang,No.16 Keyun Rd,Guangzhou,China
 * All right reserved.
 * 
 * Created on 2012-7-24
 */
package india.xxoo.net;

/**
 * //TODO Administrator自动生成javadoc
 * <b>NetWorklistener简介:</b>
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
public interface NetWorkListener {
	//连接成功
	public void onConnected();
	//连接关闭
	public void onClosed();
	//收到消息
	public void onReceivedMessage(int aSize);
	//消息发送成功
	public void onSentMessage();
	//连接错误
	public void onError(String aError);
}
