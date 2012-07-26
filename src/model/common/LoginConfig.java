package model.common;

/**
 * 
 * //TODO Administrator自动生成javadoc
 * <b>LoginConfig简介:</b>
 * <p>简要的描述此类的作用</p>
 *
 * <b>功能描述:</b>
 * <p>具体功能点描述，当添加新功能时，要求更新此描述</p>
 *
 * <b>修改历史</b>
 * <p>
 *  <ol>
 *   <li>创建（Added by Administrator on 2012-7-22）</li>
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
