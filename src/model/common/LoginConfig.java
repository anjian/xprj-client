package model.common;

/**
 * 
 * //TODO Administrator�Զ�����javadoc
 * <b>LoginConfig���:</b>
 * <p>��Ҫ���������������</p>
 *
 * <b>��������:</b>
 * <p>���幦�ܵ�������������¹���ʱ��Ҫ����´�����</p>
 *
 * <b>�޸���ʷ</b>
 * <p>
 *  <ol>
 *   <li>������Added by Administrator on 2012-7-22��</li>
 *  </ol> 
 * </p>
 * @author Administrator
 * @version 1.0
 */
public class LoginConfig {

	private String iUserName = null;
	private String iPassword = null;
	
    private static LoginConfig gInstance = new LoginConfig();

    private LoginConfig() {
        //TODO load config
    }

    public static LoginConfig instance() {
        return gInstance;
    }
	
	// set method set
	public void setUserName(String aUserName) {
		this.iUserName = aUserName;
	}
	
	public void setPassword(String aPassword) {
		this.iPassword = aPassword;
	}
	
	// get method set
	public String getUserName() {
		return iUserName;
	}
	
	public String getPassword() {
		return iPassword;
	}
}
