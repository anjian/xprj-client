package india.xxoo.adapter;

import india.xxoo.util.Common;

public class Keybord {

	/**
	 * ���Ƽ�
	 */
	public static final int CMD_LEFT = -6; //�����
	public static final int CMD_RIGHT = -7;//�����
	public static final int CMD_OK = 8;//ȷ����
	public static final int CMD_DELETE = -8;//ɾ����


	/**
	 * ������
	 */
	public static final int NAV_LEFT = 2; //��
	public static final int NAV_RIGHT = 5;//��
	public static final int NAV_UP = 1;//��
	public static final int NAV_DOWN = 6;//��


	/**
	 * ��ϼ� #+?
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
	 * ���ܼ�
	 */
	public static final int FUN_CODE_MAIN_PAGE = -2001;//�ص���ҳ
	public static final int FUN_CODE_REFRESH = -2002;//ˢ��   
	public static final int FUN_CODE_OPEN_RIGHT_MENU = -2003;//�Ҽ��˵�
	public static final int FUN_CODE_FOREWARD = -2004;//ǰ��
	public static final int FUN_CODE_BACKWARD = -2005;//����
	public static final int FUN_CODE_ENTER_URL = -2006;//������ַ
	public static final int FUN_CODE_OPEN_BACKGROUND = -2007;//��̨��
	public static final int FUN_CODE_BOOKMARK_MANAGER = -2008;//��ǩ����
	public static final int FUN_CODE_LEFT_WINDOW = -2009;//�����л�����
	public static final int FUN_CODE_RIGHT_WINDOW = -2010;//�����л�����
	public static final int FUN_CODE_CLOSE_WINDOW= -2011;//�رմ���
	public static final int FUN_CODE_ADD_BOOKMARK = -2012;//�����ǩ
	public static final int FUN_CODE_SEARCH = -2013;    //����
	public static final int FUN_CODE_FILE_BROWSER = -2014;//�ļ�����
	public static final int FUN_CODE_COPY= -2015;//����
	public static final int FUN_CODE_PAGE_HEAD_TAIL = -20106;//ҳ��ҳβ
	public static final int FUN_CODE_NO_IMAGE = -2017;//��ͼģʽ
	public static final int FUN_CODE_FULL_SCREEN = -2018;//ȫ���л�
	public static final int FUN_CODE_SYSTEM_SETTINGS = -2019;//ϵͳ����
	public static final int FUN_CODE_SHOW_MENU = -2020;//�����˵�
	public static final int FUN_CODE_PAGE_ATTRIBUTE = -2021;//ҳ������
	public static final int FUN_CODE_CLEAR_RECORD = -2022;//�����¼
	public static final int FUN_CODE_DOWNLOAD_MANAGER = -2023;//���ع���
	public static final int FUN_CODE_SWITCH_THEME = -2024;//�л�����
	public static final int FUN_CODE_OPEN_SHORTCUT_MENU = -2025;//�򿪿�ݲ˵�
	public static final int FUN_CODE_SAVE_IMAGE = -2026;//����ͼƬ
	public static final int FUN_CODE_PRE_READ = -2027;//��Ԥ��
	public static final int FUN_CODE_SHARE = -2028;//�����
	public static final int FUN_CODE_FACEBOOK = -2029;//��FaceBook
	public static final int FUN_CODE_TWITTER = -2030;//��Twitter

	public static final int FUN_CODE_DIR_UP = -2031;//����
	public static final int FUN_CODE_DIR_DOWN = -2032;//����
	public static final int FUN_CODE_DIR_LEFT = -2033;//����
	public static final int FUN_CODE_DIR_RIGHT = -2034;//����
	public static final int FUN_CODE_CTR_OK = -2035;//ȷ��

	public static final int FUN_CODE_ADD_NEW_WINDOW = -2036;//�½�����



	/**
	 * ��Ч��
	 */
	public static final int KEY_INVALID = -99999;


	/**
	 * �Ƿ�Ϊ���ּ�����İ����ļ�ֵ
	 * 0-9 * # ����
	 * @param aKeyCode int ��ֵ
	 * @return boolean �Ƿ�����
	 */
	public static boolean isDigitalAreaKey(int aKeyCode) {
		return isDigitalKey(aKeyCode)||aKeyCode == 42 || aKeyCode == 35;
	}

	/**
	 * �Ƿ�Ϊ���ּ�
	 * 0-9
	 * @param aKeyCode int ��ֵ
	 * @return boolean �Ƿ�����
	 */
	public static boolean isDigitalKey(int aKeyCode) {
		return (aKeyCode >= 48 && aKeyCode <= 57);
	}

	/**
	 * 
	 * <p>���������isLetterKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-1-17��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isLetterKey(int aKeyCode){
		return (aKeyCode >= 97 && aKeyCode <= 122)||( aKeyCode >= 65 && aKeyCode <= 90);
	}

	/**
	 * 
	 * <p>isCapitalLetterKey:�ж��Ƿ�Ϊ��д��ĸ</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-9��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isCapitalLetterKey(int aKeyCode){
		return ( aKeyCode >= 65 && aKeyCode <= 90);
	}

	/**
	 * 
	 * <p>isSmallLetterKey:�ж��Ƿ�ΪСд��ĸ</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-9��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isSmallLetterKey(int aKeyCode){
		return (aKeyCode >= 97 && aKeyCode <= 122);
	}

	/**
	 * <p>isPoundKey:�ж��Ƿ�Ϊ"#"��</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-1��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isPoundKey(int aKeyCode){
		return ((char) aKeyCode == '#');
	}

	/**
	 * 
	 * <p>���������isNoGameActionKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-1-17��</li>
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
	 * <p>isLeftControlKey:�ж��Ƿ������</p>
	 *��ͬƷ�Ƶ��ֻ�������ļ�ֵ�п��ܲ�ͬ�����¼�ֵ�����Ͽ������������ֻ�
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-1��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isLeftSoftKey(int aKeyCode){
		return (aKeyCode == CMD_LEFT);
	}

	/**
	 * 
	 * <p>isRightControlKey:�ж��Ƿ������</p>
	 * ��ͬƷ�Ƶ��ֻ�������ļ�ֵ�п��ܲ�ͬ�����¼�ֵ�����Ͽ������������ֻ�
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-1��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isRightSoftKey(int aKeyCode){
		return (aKeyCode == CMD_RIGHT);
	}

	/**
	 * 
	 * <p>isSoftKey:�ж��Ƿ�Ϊ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-2��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isSoftKey(int aKeyCode){
		return (aKeyCode == CMD_LEFT || aKeyCode == CMD_RIGHT);
	}

	/**
	 * 
	 * <p>���������isControlKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-7��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isControlKey(int aKeyCode){
		return (isSoftKey(aKeyCode) || isDeleteKey(aKeyCode) || isNavgationKey(aKeyCode) || aKeyCode == CMD_OK) ;
	}

	/**
	 * 
	 * <p>���������isDeleteKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-1��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isDeleteKey(int aKeyCode){
		return aKeyCode == CMD_DELETE || aKeyCode == 127;
	}

	/**
	 * 
	 * <p>���������isNavgationKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-7��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isNavgationKey(int aKeyCode){
		return (aKeyCode == NAV_UP || aKeyCode == NAV_DOWN || aKeyCode == NAV_LEFT || aKeyCode == NAV_RIGHT);
	}


	/**
	 * 
	 * <p>���������isNavgateFunctionKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-7��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isNavgateFunctionKey(int aKeyCode){
		return (aKeyCode == FUN_CODE_DIR_UP || aKeyCode == FUN_CODE_DIR_DOWN || aKeyCode == FUN_CODE_DIR_LEFT || aKeyCode == FUN_CODE_DIR_RIGHT);
	}

	/**
	 * 
	 * <p>isCombinedKey:�ж��Ƿ�Ϊ��ϼ�</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-2��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isCombinedKey(int aKeyCode){
		return (aKeyCode  < -1000 && aKeyCode > -2000);
	}

	/**
	 * 
	 * <p>���������isValidKey�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-6��</li>
	 * </ol>
	 * @param aKeyCode
	 * @return
	 */
	public static boolean isValidKey(int aKeyCode){
		return (aKeyCode != KEY_INVALID);
	}

	/**
	 * 
	 * <p>getKeyName:��ȡ����</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-2��</li>
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
	 * <p>���������getKeyDescription�����ṩ�Ĺ���</p>
	 *
	 * <b>�޸���ʷ</b>
	 * <ol>
	 * <li>������Added by Roy on 2012-2-9��</li>
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
	 * @return int �����ļ�ֵ
	 */
	/**
	 * ͨ����������������
	 * @param aKeyName String ������
	 * @param aOriginalKeyCode int ԭ�ȵİ�ֵ
	 * @return int  �����ļ�ֵ
	 */
	public static int adaptByKeyName(String aKeyName, int aOriginalKeyCode) {
		//�ݴ���
		if (Common.isNull(aKeyName)){
			return KEY_INVALID;
		}

		aKeyName = aKeyName.toLowerCase();
		//���������
		if (aKeyName.equals("sk2(left)")) {
			return CMD_LEFT;
		}
		//���������
		if (aKeyName.equals("sk1(right)")) {
			return CMD_RIGHT;
		}

		if (aKeyName.indexOf("soft") != -1) {
			//���������
			if (aKeyName.charAt(aKeyName.length() - 1) == '1' || aKeyName.startsWith("left")) {
				//"����������ֵ���淶�����"
				return (aOriginalKeyCode != -7 ) ? CMD_LEFT : CMD_RIGHT;
			}
			//���������
			if (aKeyName.charAt(aKeyName.length() - 1) == '2' || aKeyName.startsWith("right") ||
					aKeyName.charAt(aKeyName.length() - 1) == '4') {
				return (aOriginalKeyCode != -6 ) ? CMD_RIGHT : CMD_LEFT;
			}
		}
		//����ɾ����
		if (aKeyName.equals("clear")){
			return CMD_DELETE;
		}
		//����ѡ���
		if (aKeyName.equals("select") || aKeyName.equals("ok") || aKeyName.equals("send") ||
				aKeyName.equals("fire") || aKeyName.equals("navi-center") || aKeyName.equals("start") ||
				aKeyName.equals("enter")) {
			return CMD_OK;
		}
		//�������ϵ�����
		if (aKeyName.equals("up") || aKeyName.equals("navi-up") || aKeyName.equals("up arrow") ||
				aKeyName.equals("��")) {
			return NAV_UP;
		}
		//�������µ�����
		if (aKeyName.equals("down") || aKeyName.equals("navi-down") || aKeyName.equals("down arrow") ||
				aKeyName.equals("��")) {
			return NAV_DOWN;
		}
		//�������󵼺���
		if (aKeyName.equals("left") || aKeyName.equals("navi-left") || aKeyName.equals("left arrow") ||
				aKeyName.equals("sideup") || aKeyName.equals("��")) {
			return NAV_LEFT;
		}
		//�������ҵ�����
		if (aKeyName.equals("right") || aKeyName.equals("navi-right") ||aKeyName.equals("right arrow") ||
				aKeyName.equals("sidedown") || aKeyName.equals("��")) {
			return NAV_RIGHT;
		}

		return KEY_INVALID;
	}


	/**
	 * ͨ����ֵ����
	 * @param aKeyName int ����ֵ
	 * @return int �����İ���ֵ
	 */
	public static int adaptByKeyCode(int aKeyCode) {
		//������ĸa-z A-Z
		if ((aKeyCode >= 97 && aKeyCode <= 122)||( aKeyCode >= 65 && aKeyCode <= 90)) {
			return aKeyCode;
		}

		if (aKeyCode == -11 && Device.deviceVenderIsNokia()){ //Common.phone == 0) {
			return 0;
		}

		//�������ؼ�
		if (aKeyCode == -11 && Device.deviceVenderIsSonyEricsson()) {//Common.phone == 3) {
			return -11;
		}

		if (aKeyCode == -22 && Device.deviceVenderIsSiemens()){ //Common.phone == 2) {
			return 0;
		}
		//���������
		if (aKeyCode == -6 || aKeyCode == -21 || aKeyCode == 21 ||  aKeyCode == -202 || aKeyCode == 57345 ||
				aKeyCode == 113 || aKeyCode == 65 || aKeyCode == 66 
		) {
			return CMD_LEFT;
		}
		//���������            //�����������ּ�����������
		if (aKeyCode == -7 || (aKeyCode == -22 && !Device.deviceVenderIsSonyEricsson()/*Common.phone != 3*/) || aKeyCode == 22  || aKeyCode == -203 || aKeyCode == 57346 ||
				aKeyCode == 68 || aKeyCode == 67 || aKeyCode == 112 || aKeyCode == 106 
		){
			return CMD_RIGHT;
		}
		//����ȷ����                             //��������������������⴦��
		if (aKeyCode == -5 || aKeyCode == -10 || (aKeyCode == -20 && !Device.deviceVenderIsSonyEricsson()/*Common.phone != 3*/ ) || aKeyCode == 20 ||
				aKeyCode == 23 || aKeyCode == -14 || aKeyCode == -26 ||aKeyCode == -200 ||
				aKeyCode == 13) {
			return CMD_OK;
		}
		//����ɾ����
		if (aKeyCode == -8 || aKeyCode == 8){
			return CMD_DELETE;
		}

		if( aKeyCode == -11 || aKeyCode == -16 || aKeyCode == -19 || aKeyCode == -204) {
			return -10000;
		}

		return KEY_INVALID;
	}

}
