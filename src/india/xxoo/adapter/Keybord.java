package india.xxoo.adapter;

import india.xxoo.util.Common;

public class Keybord {

	/**
	 * 控制键
	 */
	public static final int CMD_LEFT = -6; //左软键
	public static final int CMD_RIGHT = -7;//右软键
	public static final int CMD_OK = 8;//确定键
	public static final int CMD_DELETE = -8;//删除键


	/**
	 * 导航键
	 */
	public static final int NAV_LEFT = 2; //左
	public static final int NAV_RIGHT = 5;//右
	public static final int NAV_UP = 1;//上
	public static final int NAV_DOWN = 6;//下


	/**
	 * 组合键 #+?
	 */
	public static final int POUND_KEY0 = -1048;//#0
	public static final int POUND_KEY1 = -1049;
	public static final int POUND_KEY2 = -1050;
	public static final int POUND_KEY3 = -1051;
	public static final int POUND_KEY4 = -1052;
	public static final int POUND_KEY5 = -1053;
	public static final int POUND_KEY6 = -1054;
	public static final int POUND_KEY7 = -1055;
	public static final int POUND_KEY8 = -1056;
	public static final int POUND_KEY9 = -1057;//#9
	public static final int POUND_STAR = -1042;//#*
	public static final int POUND_POUND = -1035; //##


	/**
	 * 功能键
	 */
	public static final int FUN_CODE_MAIN_PAGE = -2001;//回到首页
	public static final int FUN_CODE_REFRESH = -2002;//刷新   
	public static final int FUN_CODE_OPEN_RIGHT_MENU = -2003;//右键菜单
	public static final int FUN_CODE_FOREWARD = -2004;//前进
	public static final int FUN_CODE_BACKWARD = -2005;//后退
	public static final int FUN_CODE_ENTER_URL = -2006;//输入网址
	public static final int FUN_CODE_OPEN_BACKGROUND = -2007;//后台打开
	public static final int FUN_CODE_BOOKMARK_MANAGER = -2008;//书签管理
	public static final int FUN_CODE_LEFT_WINDOW = -2009;//向左切换窗口
	public static final int FUN_CODE_RIGHT_WINDOW = -2010;//向右切换窗口
	public static final int FUN_CODE_CLOSE_WINDOW= -2011;//关闭窗口
	public static final int FUN_CODE_ADD_BOOKMARK = -2012;//添加书签
	public static final int FUN_CODE_SEARCH = -2013;    //搜索
	public static final int FUN_CODE_FILE_BROWSER = -2014;//文件管理
	public static final int FUN_CODE_COPY= -2015;//复制
	public static final int FUN_CODE_PAGE_HEAD_TAIL = -20106;//页首页尾
	public static final int FUN_CODE_NO_IMAGE = -2017;//无图模式
	public static final int FUN_CODE_FULL_SCREEN = -2018;//全屏切换
	public static final int FUN_CODE_SYSTEM_SETTINGS = -2019;//系统设置
	public static final int FUN_CODE_SHOW_MENU = -2020;//弹出菜单
	public static final int FUN_CODE_PAGE_ATTRIBUTE = -2021;//页面属性
	public static final int FUN_CODE_CLEAR_RECORD = -2022;//清除记录
	public static final int FUN_CODE_DOWNLOAD_MANAGER = -2023;//下载管理
	public static final int FUN_CODE_SWITCH_THEME = -2024;//切换主题
	public static final int FUN_CODE_OPEN_SHORTCUT_MENU = -2025;//打开快捷菜单
	public static final int FUN_CODE_SAVE_IMAGE = -2026;//保存图片
	public static final int FUN_CODE_PRE_READ = -2027;//打开预读
	public static final int FUN_CODE_SHARE = -2028;//打分享
	public static final int FUN_CODE_FACEBOOK = -2029;//打开FaceBook
	public static final int FUN_CODE_TWITTER = -2030;//打开Twitter

	public static final int FUN_CODE_DIR_UP = -2031;//向上
	public static final int FUN_CODE_DIR_DOWN = -2032;//向下
	public static final int FUN_CODE_DIR_LEFT = -2033;//向左
	public static final int FUN_CODE_DIR_RIGHT = -2034;//向右
	public static final int FUN_CODE_CTR_OK = -2035;//确定

	public static final int FUN_CODE_ADD_NEW_WINDOW = -2036;//新建窗口



	/**
	 * 无效键
	 */
	public static final int KEY_INVALID = -99999;


	/**
	 * 是否为数字键区域的按键的键值
	 * 0-9 * # 属于
	 * @param aKeyCode int 键值
	 * @return boolean 是否属于
	 */
	public static boolean isDigitalAreaKey(int aKeyCode) {
		return isDigitalKey(aKeyCode)||aKeyCode == 42 || aKeyCode == 35;
	}

	/**
	 * 是否为数字键
	 * 0-9
	 * @param aKeyCode int 键值
	 * @return boolean 是否属于
	 */
	public static boolean isDigitalKey(int aKeyCode) {
		return (aKeyCode >= 48 && aKeyCode <= 57);
	}

	/**
	 * 
	 * <p>请具体描述isLetterKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-1-17）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isLetterKey(int aKeyCode){
		return (aKeyCode >= 97 && aKeyCode <= 122)||( aKeyCode >= 65 && aKeyCode <= 90);
	}

	/**
	 * 
	 * <p>isCapitalLetterKey:判断是否为大写字母</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-9）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isCapitalLetterKey(int aKeyCode){
		return ( aKeyCode >= 65 && aKeyCode <= 90);
	}

	/**
	 * 
	 * <p>isSmallLetterKey:判断是否为小写字母</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-9）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isSmallLetterKey(int aKeyCode){
		return (aKeyCode >= 97 && aKeyCode <= 122);
	}

	/**
	 * <p>isPoundKey:判断是否为"#"键</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-1）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isPoundKey(int aKeyCode){
		return ((char) aKeyCode == '#');
	}

	/**
	 * 
	 * <p>请具体描述isNoGameActionKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-1-17）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isNoGameActionKey(int aKeyCode){
		return isLetterKey(aKeyCode) || aKeyCode == -8 || aKeyCode == -11 || aKeyCode == 33 || aKeyCode == 63
		|| (aKeyCode >= 43 && aKeyCode <= 45 ) || aKeyCode == 47 || aKeyCode == 39;
	}

	/**
	 * 
	 * <p>isLeftControlKey:判断是否左软键</p>
	 *不同品牌的手机左软键的键值有可能不同，以下键值基本上可以适配所有手机
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-1）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isLeftSoftKey(int aKeyCode){
		return (aKeyCode == CMD_LEFT);
	}

	/**
	 * 
	 * <p>isRightControlKey:判断是否右软键</p>
	 * 不同品牌的手机右软键的键值有可能不同，以下键值基本上可以适配所有手机
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-1）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isRightSoftKey(int aKeyCode){
		return (aKeyCode == CMD_RIGHT);
	}

	/**
	 * 
	 * <p>isSoftKey:判断是否为软键</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-2）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isSoftKey(int aKeyCode){
		return (aKeyCode == CMD_LEFT || aKeyCode == CMD_RIGHT);
	}

	/**
	 * 
	 * <p>请具体描述isControlKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-7）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isControlKey(int aKeyCode){
		return (isSoftKey(aKeyCode) || isDeleteKey(aKeyCode) || isNavgationKey(aKeyCode) || aKeyCode == CMD_OK) ;
	}

	/**
	 * 
	 * <p>请具体描述isDeleteKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-1）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isDeleteKey(int aKeyCode){
		return aKeyCode == CMD_DELETE || aKeyCode == 127;
	}

	/**
	 * 
	 * <p>请具体描述isNavgationKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-7）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isNavgationKey(int aKeyCode){
		return (aKeyCode == NAV_UP || aKeyCode == NAV_DOWN || aKeyCode == NAV_LEFT || aKeyCode == NAV_RIGHT);
	}


	/**
	 * 
	 * <p>请具体描述isNavgateFunctionKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-7）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isNavgateFunctionKey(int aKeyCode){
		return (aKeyCode == FUN_CODE_DIR_UP || aKeyCode == FUN_CODE_DIR_DOWN || aKeyCode == FUN_CODE_DIR_LEFT || aKeyCode == FUN_CODE_DIR_RIGHT);
	}

	/**
	 * 
	 * <p>isCombinedKey:判断是否为组合键</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-2）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isCombinedKey(int aKeyCode){
		return (aKeyCode  < -1000 && aKeyCode > -2000);
	}

	/**
	 * 
	 * <p>请具体描述isValidKey方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-6）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isValidKey(int aKeyCode){
		return (aKeyCode != KEY_INVALID);
	}

	/**
	 * 
	 * <p>getKeyName:获取键名</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-2）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static char[] getKeyName(int aKeyCode){
		char[] sName = null;
		if(isCombinedKey(aKeyCode)){
			sName = new char[]{(char)35, (char)(-aKeyCode - 1000)};
		}else if(isSmallLetterKey(aKeyCode)){
			sName = new char[]{(char)(aKeyCode - 32)};
		}else if(isDeleteKey(aKeyCode)){
			sName = new char[]{(char)127};
		}else{        
			sName = new char[]{(char)aKeyCode};
		}
		return sName;
	}

	/**
	 * 
	 * <p>请具体描述getKeyDescription方法提供的功能</p>
	 *
	 * <b>修改历史</b>
	 * <ol>
	 * <li>创建（Added by Roy on 2012-2-9）</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static String getKeyDescription(int aKeyCode){
		String sName = null;
		if(isCombinedKey(aKeyCode)){
			sName = "#+" + new String( new char[]{ (char)(-aKeyCode - 1000)});
		}else if(isSmallLetterKey(aKeyCode)){
			sName = new String(new char[]{(char)(aKeyCode - 32)});
		}else if(isDeleteKey(aKeyCode)){
			sName = "DEL";
		}else{        
			sName = new String( new char[]{(char)aKeyCode});
		}
		return sName;
	}

	/**
	 *
	 * @param aKeyName String
	 * @return int 适配后的键值
	 */
	/**
	 * 通过按键名进行适配
	 * @param aKeyName String 按键名
	 * @param aOriginalKeyCode int 原先的按值
	 * @return int  适配后的键值
	 */
	public static int adaptByKeyName(String aKeyName, int aOriginalKeyCode) {
		//容错处理
		if (Common.isNull(aKeyName)){
			return KEY_INVALID;
		}

		aKeyName = aKeyName.toLowerCase();
		//适配左软键
		if (aKeyName.equals("sk2(left)")) {
			return CMD_LEFT;
		}
		//适配右软键
		if (aKeyName.equals("sk1(right)")) {
			return CMD_RIGHT;
		}

		if (aKeyName.indexOf("soft") != -1) {
			//适配左软键
			if (aKeyName.charAt(aKeyName.length() - 1) == '1' || aKeyName.startsWith("left")) {
				//"处理键名与键值不规范的情况"
				return (aOriginalKeyCode != -7 ) ? CMD_LEFT : CMD_RIGHT;
			}
			//适配右软键
			if (aKeyName.charAt(aKeyName.length() - 1) == '2' || aKeyName.startsWith("right") ||
					aKeyName.charAt(aKeyName.length() - 1) == '4') {
				return (aOriginalKeyCode != -6 ) ? CMD_RIGHT : CMD_LEFT;
			}
		}
		//适配删除键
		if (aKeyName.equals("clear")){
			return CMD_DELETE;
		}
		//适配选择键
		if (aKeyName.equals("select") || aKeyName.equals("ok") || aKeyName.equals("send") ||
				aKeyName.equals("fire") || aKeyName.equals("navi-center") || aKeyName.equals("start") ||
				aKeyName.equals("enter")) {
			return CMD_OK;
		}
		//适配向上导航键
		if (aKeyName.equals("up") || aKeyName.equals("navi-up") || aKeyName.equals("up arrow") ||
				aKeyName.equals("↑")) {
			return NAV_UP;
		}
		//适配向下导航键
		if (aKeyName.equals("down") || aKeyName.equals("navi-down") || aKeyName.equals("down arrow") ||
				aKeyName.equals("↓")) {
			return NAV_DOWN;
		}
		//适配向左导航键
		if (aKeyName.equals("left") || aKeyName.equals("navi-left") || aKeyName.equals("left arrow") ||
				aKeyName.equals("sideup") || aKeyName.equals("←")) {
			return NAV_LEFT;
		}
		//适配向右导航键
		if (aKeyName.equals("right") || aKeyName.equals("navi-right") ||aKeyName.equals("right arrow") ||
				aKeyName.equals("sidedown") || aKeyName.equals("→")) {
			return NAV_RIGHT;
		}

		return KEY_INVALID;
	}


	/**
	 * 通过键值适配
	 * @param aKeyName int 按键值
	 * @return int 适配后的按键值
	 */
	public static int adaptByKeyCode(int aKeyCode) {
		//过滤字母a-z A-Z
		if ((aKeyCode >= 97 && aKeyCode <= 122)||( aKeyCode >= 65 && aKeyCode <= 90)) {
			return aKeyCode;
		}

		if (aKeyCode == -11 && Device.deviceVenderIsNokia()){ //Common.phone == 0) {
			return 0;
		}

		//索爱返回键
		if (aKeyCode == -11 && Device.deviceVenderIsSonyEricsson()) {//Common.phone == 3) {
			return -11;
		}

		if (aKeyCode == -22 && Device.deviceVenderIsSiemens()){ //Common.phone == 2) {
			return 0;
		}
		//适配左软件
		if (aKeyCode == -6 || aKeyCode == -21 || aKeyCode == 21 ||  aKeyCode == -202 || aKeyCode == 57345 ||
				aKeyCode == 113 || aKeyCode == 65 || aKeyCode == 66 
		) {
			return CMD_LEFT;
		}
		//适配右软件            //索爱机型音乐键的特殊外理
		if (aKeyCode == -7 || (aKeyCode == -22 && !Device.deviceVenderIsSonyEricsson()/*Common.phone != 3*/) || aKeyCode == 22  || aKeyCode == -203 || aKeyCode == 57346 ||
				aKeyCode == 68 || aKeyCode == 67 || aKeyCode == 112 || aKeyCode == 106 
		){
			return CMD_RIGHT;
		}
		//适配确定键                             //索爱机型浏览器键的特殊处理
		if (aKeyCode == -5 || aKeyCode == -10 || (aKeyCode == -20 && !Device.deviceVenderIsSonyEricsson()/*Common.phone != 3*/ ) || aKeyCode == 20 ||
				aKeyCode == 23 || aKeyCode == -14 || aKeyCode == -26 ||aKeyCode == -200 ||
				aKeyCode == 13) {
			return CMD_OK;
		}
		//适配删除键
		if (aKeyCode == -8 || aKeyCode == 8){
			return CMD_DELETE;
		}

		if( aKeyCode == -11 || aKeyCode == -16 || aKeyCode == -19 || aKeyCode == -204) {
			return -10000;
		}

		return KEY_INVALID;
	}

}
