/**
 * TestNetWork.java
 * 
 * Copyright (c) 2004-2010 UC Mobile Ltd.
 * F/4,Building B,Guangzhou Xinxigang,No.16 Keyun Rd,Guangzhou,China
 * All right reserved.
 * 
 * Created on 2012-7-24
 */
package india.xxoo.net;

/**
 * //TODO Administrator�Զ�����javadoc
 * <b>TestNetWork���:</b>
 * <p>��Ҫ���������������</p>
 *
 * <b>��������:</b>
 * <p>���幦�ܵ�������������¹���ʱ��Ҫ����´�����</p>
 *
 * <b>�޸���ʷ</b>
 * <p>
 *  <ol>
 *   <li>������Added by Administrator on 2012-7-24��</li>
 *  </ol> 
 * </p>
 * @author Administrator
 * @version 1.0
 */
public class TestNetWork implements NetWorkListener  {

	/**
	 * <p>
	 * ���췽��
	 * </p>
	 */
	public TestNetWork() {
		Network n = new Network();
		n.setListener(this);
		n.connect("", "");
	}
	/**
	 * @see india.xxoo.net.NetWorkListener#onConnected()
	 */
	public void onConnected() {
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onClosed()
	 */
	public void onClosed() {
	}

	/**
	 * @see india.xxoo.net.NetWorkListener#onReceivedMessage(int)
	 */
	public void onReceivedMessage(int aSize) {
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
