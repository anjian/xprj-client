package india.xxoo.adapter;

import india.xxoo.util.Common;

public class Device {

	private String iDeviceUserAgent;
	private int iDeviceVender;
	private static Device gInstance = null;

	// 设备厂家
	public static final int DEVICE_VENDER_UNKNOW           = -1;
	public static final int DEVICE_VENDER_NOKIA            = 0;
	public static final int DEVICE_VENDER_MOTOLOLA         = 1;
	public static final int DEVICE_VENDER_SIEMENS          = 2;
	public static final int DEVICE_VENDER_SONYERICSSON     = 3;
	public static final int DEVICE_VENDER_SAMSUNG          = 4;
	public static final int DEVICE_VENDER_5                = 5;
	public static final int DEVICE_VENDER_PALM             = 6;
	public static final int DEVICE_VENDER_BLACKBERRY       = 7;
	public static final int DEVICE_VENDER_VS               = 8;
	public static final int DEVICE_VENDER_LG               = 9;
	public static final int DEVICE_VENDER_K_TOUCH          = 10;
	public static final int DEVICE_VENDER_LENOVO           = 11;

	public final static Device getInstance(){
		if(gInstance == null){ 
			gInstance = new Device();
			gInstance.initDevice();
		}
		return gInstance;
	}

	public void initDevice(){
		initDeviceVenderAndPlatform();
	}

	public final static String deviceSystemProperty(String aKey){
		try {
			return System.getProperty(aKey);
		} catch (Throwable e) {
		}
		return null;
	}

	public void initDeviceVenderAndPlatform() {  
		/* User-Agent */
		iDeviceUserAgent = deviceSystemProperty("microedition.platform");
		iDeviceUserAgent = (iDeviceUserAgent != null ? iDeviceUserAgent.toLowerCase() : "");


		if (iDeviceUserAgent.indexOf("nokia") != -1 || iDeviceUserAgent.equals("siemens_sx1") ||
				iDeviceUserAgent.equals("sendo x") || iDeviceUserAgent.equals("panasonic x700")){
			setDeviceVender(DEVICE_VENDER_NOKIA);    
		}else if (iDeviceUserAgent.indexOf("palm") != -1){
			// phone = 6;
			setDeviceVender(DEVICE_VENDER_VS);
		}else if (checkClassExist("com.siemens.mp.lcdui.Image")){
			// phone = 2;
			setDeviceVender(DEVICE_VENDER_SIEMENS);
		}else if (checkClassExist("com.motorola.phonebook.PhoneBookRecord") ||
				checkClassExist("com.motorola.Dialer") ||
				checkClassExist("com.motorola.phone.Dialer") ||
				checkClassExist("com.motorola.graphics.j3d.Light") ||
				checkClassExist("com.motorola.lwt.ComponentScreen") ||
				checkClassExist("com.motorola.game.GameScreen") ||
				checkClassExist("com.motorola.funlight.FunLight") ||
				checkClassExist("com.motorola.multimedia.Lighting") ||
				checkClassExist("com.motorola.io.ConnectorEvent") ||
				checkClassExist("com.motorola.extensions.ScalableJPGImage") ||
				deviceSystemProperty("batterylevel") != null ||
				deviceSystemProperty("BatteryLevel") != null){
			//phone = 1;
			setDeviceVender(DEVICE_VENDER_MOTOLOLA);
		}else if (iDeviceUserAgent.indexOf("sonyericsson") != -1 || iDeviceUserAgent.equals("symbian os") ||
				deviceSystemProperty("com.sonyericsson.IMEI") != null ||
				deviceSystemProperty("com.sonyericsson.imei") != null){
			//phone = 3;
			setDeviceVender(DEVICE_VENDER_SONYERICSSON);

		}else if (checkClassExist("com.samsung.util.AudioClip") ||
				checkClassExist("com.samsung.util.LCDLight") ||
				checkClassExist("com.samsung.util.SM") ||
				checkClassExist("com.samsung.util.SMS") ||
				checkClassExist("com.samsung.util.Vibration")){
			//phone = 4;
			setDeviceVender(DEVICE_VENDER_SAMSUNG);
		}else if (iDeviceUserAgent.startsWith("lg-")){
			//phone = 9;
			setDeviceVender(DEVICE_VENDER_LG);
		}else if (iDeviceUserAgent.indexOf("lenovo") != -1){ 
			setDeviceVender(DEVICE_VENDER_LENOVO);
		}else if (iDeviceUserAgent.indexOf("k-touch") != -1){ 
			setDeviceVender(DEVICE_VENDER_K_TOUCH);
		}


	}

	public static boolean checkClassExist(String aClassName) {
		try {
			Class.forName(aClassName);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public void setDeviceVender(int aDeviceVender){
		iDeviceVender = aDeviceVender;
	}
	
	public boolean deviceVenderIs(int aDeviceVender ){
        return iDeviceVender == aDeviceVender;
    }

	public static final boolean deviceVenderIsUnknow(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_UNKNOW);
	}

	public static final boolean deviceVenderIsNokia(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_NOKIA);
	}
	public static final boolean deviceVenderIsSonyEricsson(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_SONYERICSSON);
	}
	public static final boolean deviceVenderIsSamsung(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_SAMSUNG);
	}
	public static final boolean deviceVenderIsMotolola(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_MOTOLOLA);
	}
	public static final boolean deviceVenderIsSiemens(){
		return gInstance.deviceVenderIs(DEVICE_VENDER_SIEMENS);
	}

}
