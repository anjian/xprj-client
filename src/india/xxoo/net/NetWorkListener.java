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
 * //TODO Administrator�Զ�����javadoc
 * <b>NetWorklistener���:</b>
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
public interface NetWorkListener {
	//���ӳɹ�
	public void onConnected();
	//���ӹر�
	public void onClosed();
	//�յ���Ϣ
	public void onReceivedMessage(int aSize);
	//��Ϣ���ͳɹ�
	public void onSentMessage();
	//���Ӵ���
	public void onError(String aError);
}
