package india.xxoo.ui;

import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;


/**
 * 
 * <b>UMenu���:</b>
 * <p>��Ҫ���������������</p>
 *
 * <b>��������:</b>
 * <p>���幦�ܵ�������������¹���ʱ��Ҫ����´�����</p>
 *
 * <b>�޸���ʷ</b>
 * <p>
 *  <ol>
 *   <li>������Added by Roy on 2012-3-19��</li>
 *  </ol> 
 * </p>
 * @author Roy
 * @version 1.0
 */
public class UMenu {
    private int id = -1;
    private UMenu iParent = null;
    private Vector items = new Vector();
    private Vector iSubMenus;
    private Vector iSeparators = null;
    
    private boolean isVisibale = false;   
    private boolean isActivate = false;
    private int iSelectedIndex = -1;
    private int iStartIndex = 0;
    private int iDisplayItemNum = 0;
    
    private int iPosX = 0;
    private int iPosY = 0;
    private int iPosXBackup = 0;
    private int iPosYBackup = 0;
    private int iWidth;
    private int iHeight;
    private int itemWidth;
    private int itemHeight ;
    private int iShortcutDescWidth = 0;
    
    private static final int HPADDINGS = 2;
    private static final int VPADDINGS = 2;
    private static final int TOUCH_HPADDINGS = 6;
    private static final int TOUCH_VPADDINGS = 6;
    private static final int ITEM_SPACE = 8;
    private static final int SUB_MENU_ICON_WIDTH = 8;
    private static final int SHORTCUT_RECT_PADDINGS = 1;
    
    private int iHPaddings = HPADDINGS;
    private int iVPaddings = VPADDINGS;
    
    private int[] iUpItem = null;//���ϲ˵���
    private int[] iDownItem = null;//�����²˵���
    private boolean iHasUpDownItem = false;
    private static final int UP_ITEM_ACTIION_ID = -65530;
    private static final int DOWN_ITEM_ACTIION_ID = -65531;
    private static final int TOUCH_CONTROL_ITEM_HEIGHT = 20;
    private static final int CONTROL_ITEM_HEIGHT = 10;
    private static final int CONTROL_ICON_WIDTH = 8;
    private int iControlItemHeight = CONTROL_ITEM_HEIGHT;
    
    //�˵���ʾê��
    public static final int ANCHOR_TOP_LEFT = 0;//����
    public static final int ANCHOR_TOP_RIGHT = 1;//����
    public static final int ANCHOR_BOTTOM_LEFT = 2;//����
    public static final int ANCHOR_BOTTOM_RIGHT = 3;//����
    private int iAnchor = ANCHOR_TOP_LEFT;   
    
    /**
     * �ɻ�����:
     * gPaintableRect[0]:�������
     * gPaintableRect[1]:�ϱ�����
     * gPaintableRect[2]:����
     * gPaintableRect[3]:����
     */
    public static int[] gPaintableRect = new int [4];    
    
    public static boolean gIsTouchMode = false;//�Ƿ���
    
    public UMenu(){   
    }
    
    public UMenu(int aId){
        id = aId;
    }
    
    public UMenu(UMenu aParent, int aIndex){       
        if(aParent != null){
            aParent.addMenu(this, aIndex);
        }
    }   
    
    /**
     * 
     * <p>���������getId�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     * 
     * <p>��Ӳ˵���</p>
     *  ÿ���˵�����һ��Object[3]�����ʾ��Object[0]Ϊһ��int[5]���洢һЩ������Ϣ���ֱ�Ϊaction Id, posX, posY, enable, hasSubMenu����
     *  Object[1]��һ��String��Ϊ�˵�������֣� Object[2]��һ��String��Ϊ�˵���Ŀ�ݼ�
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aText �˵���
     * @param actionId  ִ��ID
     */
    public void addItem(String aText, int actionId){
        addItem(aText, actionId, null);
    }
    
    /**
     * 
     * <p>��Ӳ˵���</p>
     *  ÿ���˵�����һ��Object[3]�����ʾ��Object[0]Ϊһ��int[5]���洢һЩ������Ϣ���ֱ�Ϊaction Id, posX, posY, enable, hasSubMenu����
     *  Object[1]��һ��String��Ϊ�˵�������֣� Object[2]��һ��String��Ϊ�˵���Ŀ�ݼ�
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aText �˵���
     * @param actionId ִ��ID
     * @param aShortcut �������
     */
    public void addItem(String aText, int actionId, String aShortcut){
        int [] sControls = {actionId, 0, 0, 1, 0};//action Id, posX, posY, enable, hasSubMenu
        Object[] sItem = {sControls, aText, aShortcut};
        items.addElement(sItem);
    }
    
    /**
     * 
     * <p>��ӷָ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-28��</li>
     * </ol>
     */
    public void addSeparator(){
        if(iSeparators == null){
            iSeparators = new Vector();
        }
        
        iSeparators.addElement(new Integer(items.size() - 1));
    }
    
    /**
     * 
     * <p>���������addMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aMenu �Ӳ˵�
     * @param aIndex �󶨵�Item Index
     */
    public void addMenu(UMenu aMenu, int aIndex){
        if(aIndex < 0 || aIndex > items.size() - 1){
            return;
        }
        Object[] sItem = (Object[])items.elementAt(aIndex);
        int[] sControls = (int[])sItem[0];
        sControls[4] = 1;
        aMenu.setParent(this);
        putMenu(aMenu, aIndex);
    }
    
    /**
     * 
     * <p>���������addMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aText
     * @param actionId
     * @return
     */
    public UMenu addMenu(String aText, int actionId){
        this.addItem(aText, actionId);
        
        UMenu sMenu = new UMenu(this.getId());
        this.addMenu(sMenu, items.size() - 1);        
        return sMenu;
    }
    
    /**
     * 
     * <p>���������clear�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     */
    public void clear(){
        items.removeAllElements();
        
        if(iSubMenus != null){
            iSubMenus.removeAllElements();
        }
    }
    
    private void putMenu(UMenu aMenu, int aIndex){
        if(iSubMenus == null){
            iSubMenus = new Vector();
        }
        
        Object[] sObject = new Object[2];
        int[] sIndex = new int[1];
        sIndex[0] =  aIndex;
        sObject[0] = sIndex;
        sObject[1] = aMenu;
        
        iSubMenus.addElement(sObject);
    }
    
    /**
     * 
     * <p>���������getSubMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aIndex
     * @return
     */
    public UMenu getSubMenu(int aIndex){
        if(iSubMenus != null){
            for(int i = 0; i < iSubMenus.size(); i++){
                Object[] sObject = (Object[])(iSubMenus.elementAt(i));
                int[] sIndex = (int[])sObject[0];
                
                if(sIndex != null && sIndex.length > 0 && sIndex[0] == aIndex){
                    return (UMenu)sObject[1];
                }
            }
        }
        
        return null;
    }
    
    /**
     * 
     * <p>���������getSubMenus�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-26��</li>
     * </ol>
     * @return
     */
    public Vector getSubMenus(){
        Vector subMenus = new Vector();
        if(iSubMenus != null){
            for(int i = 0; i < iSubMenus.size(); i++){
                Object[] sObject = (Object[])(iSubMenus.elementAt(i));      
                subMenus.addElement(sObject[1]);
            }
        }
        
        return subMenus;
    }
    
    /**
     * 
     * <p>���������setParent�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aMenu
     */
    public void setParent(UMenu aMenu){
        iParent = aMenu;
    }
    
    /**
     * 
     * <p>���������getParentMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @return
     */
    public UMenu getParentMenu(){
        return iParent;
    }
    
    /**
     * 
     * <p>���������getAncestorMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @return
     */
    public UMenu getAncestorMenu(){
        UMenu sAncestor = this;
        while(sAncestor.getParentMenu() != null){
            sAncestor = sAncestor.getParentMenu();
        }
        
        return sAncestor;
    }
    
    /**
     * 
     * <p>���������isSubMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @return
     */
    public boolean isSubMenu(){
        return (iParent != null);
    }
    
    /**
     * 
     * <p>���������isVisible�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @return
     */
    public boolean isVisible(){
        return isVisibale;
    }
    
    /**
     * 
     * <p>���������setVisible�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aIsVisible
     */
    public void setVisible(boolean aIsVisible){
        isVisibale = aIsVisible;
    }
    
    /**
     * 
     * <p>���������show�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-26��</li>
     * </ol>
     */
    public void show(){
        isVisibale = true;
        
        setActivate(true);
    }
    
    /**
     * 
     * <p>���������show�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aX
     * @param aY
     */
    public void show(int aX, int aY){
        show();
        setPos(aX, aY, ANCHOR_TOP_LEFT);
    }
    
    /**
     * 
     * <p>���������show�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aX
     * @param aY
     * @param anchor
     */
    public void show(int aX, int aY, int anchor){
        show();
        setPos(aX, aY, anchor);
    }
    
    /**
     * 
     * <p>���������close�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aIsCloseSubMenus
     */
    public void close(boolean aIsCloseSubMenus){
        isVisibale = false;
        isActivate = false;
        iSelectedIndex = -1;
        iStartIndex = 0;
        if(aIsCloseSubMenus){
            closeSubMenus();
        }
    }
    
    /**
     * 
     * <p>���������closeSubMenus�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-26��</li>
     * </ol>
     */
    public void closeSubMenus(){
        if(iSubMenus != null){
            for(int i = 0; i < iSubMenus.size(); i++){
                Object[] sObject = (Object[])(iSubMenus.elementAt(i));    
                
                if(sObject != null && sObject.length > 1){
                     ((UMenu)sObject[1]).close(true);
                }
            }
        }
    }
   
    /**
     * 
     * <p>���������setSelectedItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aItem
     */
    public void setSelectedItem(Object aItem){
        Object[] sItem = (Object[])aItem;
        if(sItem == null || sItem.length < 1){
            return;
        }
        int[] sControls = (int[])sItem[0];
        if(sControls == null || sControls.length < 1){
            return;
        }
        
        for(int i = 0; i < items.size(); i++){
            Object[] sItem1 = (Object[])items.elementAt(i);
            int[] sControls1 = (int[])sItem1[0];
            if(sControls1[0] == sControls[0] && sControls1[3] > 0){
                iSelectedIndex = i;
                break;
            }
        }
    }
    
    public void setPos(int aX, int aY, int anchor){
        iPosXBackup = aX;
        iPosYBackup = aY;
        iAnchor = anchor;
    }
    
    public int getLeft(){
        return iPosX;
    }
    
    public int getTop(){
       return iPosY; 
    }
    
    public int getWidth(){
        return iWidth;
    }
    
    public int getHeight(){
        return iHeight;
    }
    
    public int getRight(){
        return iPosX + iWidth;
    }
    
    public int getBottom(){
        return iPosY + iHeight;
    }
      
    /**
     * 
     * <p>���������setActivate�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aIsActivate
     */
    public void setActivate(boolean aIsActivate){
        isActivate = aIsActivate;
        
        if(isActivate){
            if(iParent != null){
                iParent.setActivate(false);
            }
            
            if(iSubMenus != null){
                for(int i = 0; i < iSubMenus.size(); i++){
                    Object[] sObject = (Object[])(iSubMenus.elementAt(i));    
                    
                    if(sObject != null && sObject.length > 1){
                         ((UMenu)sObject[1]).setActivate(false);
                    }
                }
            }
        }
    }
    
    /**
     * 
     * <p>���������getActivateMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @return
     */
    public UMenu getActivateMenu(){
        if(!isActivate){
            if(iSubMenus != null){
                for(int i = 0; i < iSubMenus.size(); i++){
                    Object[] sObject = (Object[])(iSubMenus.elementAt(i));    
                    
                    if(sObject != null && sObject.length > 1){
                         UMenu sMenu = (UMenu)sObject[1];
                         UMenu sActMenu = sMenu.getActivateMenu();
                         if(sActMenu != null && sActMenu.isVisible()){
                             return sActMenu;
                         }                        
                    }
                }
            }
        }
        
        return this;
    }
    
    public void setSelected(int aIndex){
        if(aIndex == iSelectedIndex){
            return;
        }
        
        if(aIndex < 0){
            iSelectedIndex = items.size() -1;
            iStartIndex = iSelectedIndex - iDisplayItemNum + 1;
        }else if(aIndex > items.size() - 1){
            iSelectedIndex = 0;
            iStartIndex = 0;
        }else{
            iSelectedIndex = aIndex;
        }
    }
    
    /**
     * 
     * <p>���������moveUp�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     */
    public void moveUp(){
        if(iSelectedIndex == iStartIndex && iStartIndex > 0){
            iStartIndex--;
            iSelectedIndex --;
        }else{
            setSelected(iSelectedIndex - 1);  
        }
                
        Object sItem = getSelectedItem();
        if(!UMenu.isItemEnabled(sItem)){
            moveUp();
        }
    }
    
    /**
     * 
     * <p>���������moveDown�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     */
    public void moveDown(){
        if(iSelectedIndex  - iStartIndex == iDisplayItemNum -1 && iStartIndex + iDisplayItemNum < items.size()){
            iStartIndex ++;
            iSelectedIndex++;
        }else{
            setSelected(iSelectedIndex + 1);
        }
        
        
        Object sItem = getSelectedItem();
        if(!UMenu.isItemEnabled(sItem)){
            moveDown();
        }
    }    
    
    /**
     * 
     * <p>���������getSelectedIndex�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @return
     */
    public int getSelectedIndex(){
        return iSelectedIndex;
    }
    
    /**
     * 
     * <p>���������getSelectedItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @return
     */
    public Object getSelectedItem(){
        if(iSelectedIndex > -1 && iSelectedIndex < items.size()){
            return items.elementAt(iSelectedIndex);
        }
        
        return null;
    }
    
    /**
     * 
     * <p>���������getItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aIndex
     * @return
     */
    public Object getItem(int aIndex){
        if(aIndex > -1 && aIndex < items.size()){
            return items.elementAt(aIndex);
        }
        
        return null;
    }
    
    /**
     * 
     * <p>���������getItems�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @return
     */
    public Vector getItems(){
        return items;
    }
    
    /**
     * 
     * <p>���������getItemTop�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-26��</li>
     * </ol>
     * @param aIndex
     * @return
     */
    public int getItemTop(int aIndex){
        int sTop = 0;
        if(aIndex >= 0 && aIndex < items.size()){
            Object[] sItem = (Object[])items.elementAt(aIndex);
            int [] sControls = (int[])sItem[0];
            sTop = sControls[2];
        }
        
        return sTop;
    }
    
    
    public int getItemTop(Object aItem){
        int sTop = 0;
        if(aItem != null && aItem instanceof Object[]){
            Object[] sItem = (Object[])aItem;
            int [] sControls = (int[])sItem[0];
            sTop = sControls[2];
        }
        
        return sTop;
    }
    
    /**
     * 
     * <p>���������containsPoint�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aX
     * @param aY
     * @return
     */
    public boolean containsPoint(int aX, int aY){
        return (aX > iPosX && aX < iPosX + iWidth && aY > iPosY && aY < iPosY + iHeight);
    }
    
    /**
     * 
     * <p>���������getSelectedItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-22��</li>
     * </ol>
     * @param aX
     * @param aY
     * @return
     */
    public Object getItem(int aX, int aY){ 
        if(iHasUpDownItem){
            if(aX > iUpItem[1] && aX < iUpItem[1] + itemWidth && aY > iUpItem[2] && aY < iUpItem[2] + iControlItemHeight){
                return iUpItem;
            }            
            if(aX > iDownItem[1] && aX < iDownItem[1] + itemWidth && aY > iDownItem[2] && aY < iDownItem[2] + iControlItemHeight){
                return iDownItem;
            }
        }
             
        for(int i = iStartIndex; i < iStartIndex + iDisplayItemNum; i++){
            Object[] sItem = (Object[]) items.elementAt(i);
            int[] sControls = (int[]) sItem[0];
            
            if(aX > sControls[1] && aX < sControls[1] + itemWidth && aY > sControls[2] && aY < sControls[2] + itemHeight){
               return sItem;
            }
        }
        
        return null;
    }   
    
    /**
     * 
     * <p>���������draw�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-19��</li>
     * </ol>
     * @param aGraphics
     */
    public void draw(MyGraphics aGraphics){              
        if(!isVisibale)
            return;  
        
        drawItems(aGraphics);        
        drawSubMenus(aGraphics);
    }
    
    private void drawSubMenus(MyGraphics aGraphics){
        if(iSubMenus != null){
            for(int i = 0; i < iSubMenus.size(); i++){
                Object[] sObject = (Object[])(iSubMenus.elementAt(i));                    
                if(sObject != null && sObject.length > 1){
                     ((UMenu)sObject[1]).draw(aGraphics);
                }
            }
        }
    }
    
    private void drawItems(MyGraphics aGraphics){
        calculate(MyFont.getMyFont());          
        aGraphics.setClip(iPosX, iPosY, iWidth, iHeight);
        int[] sColors = {};
        
        //draw background
        aGraphics.setColor(isActivate ? sColors[26] : sColors[30]);
        aGraphics.fillRect(iPosX + 1, iPosY + 1, iWidth - 2, iHeight - 2);        
        
        //�����ڱ߿�
        aGraphics.setColor(isActivate ? sColors[27] : sColors[37]);
        aGraphics.drawRect(iPosX + 1, iPosY + 1, iWidth - 3, iHeight - 3);

        //������߿�
        aGraphics.setColor(isActivate ? sColors[28] : sColors[38]);
        aGraphics.drawShortCornerFrame(iPosX , iPosY, iWidth, iHeight, 0);
        
        int sPosY = iPosY;        
        if(iHasUpDownItem){//�������ʾȫ�������°�ť
            if(iUpItem == null){
                iUpItem = new int[]{UP_ITEM_ACTIION_ID, 0 , 0};
            }            
            if(iDownItem == null){
                iDownItem = new int[]{DOWN_ITEM_ACTIION_ID, 0, 0};
            }          
            
            aGraphics.setColor(isActivate ? sColors[33] : sColors[34]);
            aGraphics.fillTriangle(iPosX + iWidth / 2, sPosY + (iControlItemHeight - CONTROL_ICON_WIDTH) / 2,
                    iPosX + (iWidth - CONTROL_ICON_WIDTH) / 2, sPosY + (iControlItemHeight + CONTROL_ICON_WIDTH) / 2, 
                    iPosX + (iWidth + CONTROL_ICON_WIDTH) / 2, sPosY + (iControlItemHeight + CONTROL_ICON_WIDTH) / 2);            
            iUpItem[1] = iPosX;
            iUpItem[2] = sPosY;
            sPosY += iControlItemHeight;
        }
        
        //���˵���      
        for(int i = iStartIndex; i < iStartIndex + iDisplayItemNum; i++){
            Object[] sItem = (Object[])items.elementAt(i);
            int[] sControls = (int[])sItem[0];            
            sControls[1] = iPosX;
            sControls[2]= sPosY;
            
            boolean sIsFocus = (iSelectedIndex == i);
            if(sIsFocus){   //���㱳��
                aGraphics.fillGradualColorRoundCornerRect(iPosX + 1, sPosY + 1, itemWidth -2, itemHeight - 2,
                        new int[]{sColors[29],sColors[53],sColors[54],sColors[55],sColors[56]}, 0, 5, 1);
            }                      
                       
            //�����ı�
            int sTextColor = isActivate? sColors[33]:sColors[34];
            if (sControls[3] == 0) { //��ɫ����,������
                sTextColor = isActivate? sColors[31]:sColors[39];
            } 
            if (sIsFocus) { //��ý���
                sTextColor = isActivate? sColors[32]:sColors[52];
            }
            aGraphics.setColor(sTextColor);
            int sTextRectWidth = itemWidth - 2 * iHPaddings;
            if(sControls[4] > 0){
                sTextRectWidth -= SUB_MENU_ICON_WIDTH;
            }
            if(sItem.length > 2 && sItem[2] != null){
                sTextRectWidth -= iShortcutDescWidth;
            }
            aGraphics.drawStringInARow((String)sItem[1], iPosX + iHPaddings, sPosY, sTextRectWidth, itemHeight, 1, 2, "...", false, 0);
           
            //���Ӳ˵���ʾ����
            if(sControls[4] > 0){
                int sX = iPosX + itemWidth - iHPaddings - SUB_MENU_ICON_WIDTH;
                aGraphics.fillTriangle(sX, sPosY + itemHeight / 2 - SUB_MENU_ICON_WIDTH / 2, sX, sPosY + itemHeight / 2 + SUB_MENU_ICON_WIDTH / 2, 
                        iPosX + iWidth - iHPaddings, sPosY + itemHeight / 2);
            }
            
            //����ݷ�ʽ
            if(sItem.length > 2 && sItem[2] != null){
                int[] sColor = new int[3];//0 ���� ,1 ������ ,2 ����
                if (sControls[3] == 0) {    //��ɫ����,������
                    sColor[0]=sColors[49];
                    sColor[1]=sColors[50];
                    sColor[2]=sColors[51];
                } else if (sIsFocus) { //��ý���
                    sColor[0] = sColors[46];
                    sColor[1]=sColors[47];
                    sColor[2]=sColors[48];
                } else {  //��ͨ
                    sColor[0] = sColors[43];
                    sColor[1]=sColors[44];
                    sColor[2]=sColors[45];
                }
                
                int sRectWidth = iShortcutDescWidth;
                int sRectHeight = MyFont.getMyFont().getHeight();
                int sRectLeft = iPosX + itemWidth - iHPaddings - sRectWidth;
                int sRectTop = sPosY + iVPaddings;
                String sText = (String)sItem[2];
                
                aGraphics.setColor(sColor[0]); 
                aGraphics.fillRect(sRectLeft + 1, sRectTop + 1, sRectWidth - 2, sRectHeight - 2);
                aGraphics.drawShortCornerFrame(sRectLeft, sRectTop, sRectWidth, sRectHeight, 0);
                //���Ƹ�����
                aGraphics.setColor(sColor[1]); 
                aGraphics.drawLine(sRectLeft + 1, sRectTop+1, sRectLeft+sRectWidth-2, sRectTop+1);
                //��������
                aGraphics.setColor(sColor[2]);             
                aGraphics.drawString(sText, sRectLeft + (sRectWidth - MyFont.getMyFont().stringWidth(sText)) / 2, sPosY + iVPaddings, Graphics.TOP | Graphics.LEFT);
            }
            
            sPosY += itemHeight;
            
            if(hasSeparatorAtInedex(i)){//���ָ���
                aGraphics.setColor(isActivate? sColors[31]:sColors[39]);
                aGraphics.drawLine(iPosX + 2, sPosY, iPosX + itemWidth - 4, sPosY);
                sPosY += 1;
            }
        }
        
        if(iHasUpDownItem){  //�������ʾȫ�����°�ť          
            aGraphics.setColor(isActivate ? sColors[33] : sColors[34]);
            aGraphics.fillTriangle(iPosX + iWidth / 2, sPosY + (iControlItemHeight + CONTROL_ICON_WIDTH) / 2,
                    iPosX + (iWidth - CONTROL_ICON_WIDTH) / 2, sPosY + (iControlItemHeight - CONTROL_ICON_WIDTH) / 2, 
                    iPosX + (iWidth + CONTROL_ICON_WIDTH) / 2, sPosY + (iControlItemHeight - CONTROL_ICON_WIDTH) / 2);
            
            iDownItem[1] = iPosX;
            iDownItem[2] = sPosY;
        }
    }
    
    private void calculate(MyFont aFont){
        if(gIsTouchMode){
            iVPaddings = TOUCH_VPADDINGS;
            iHPaddings = TOUCH_HPADDINGS;
            iControlItemHeight = TOUCH_CONTROL_ITEM_HEIGHT;
        }
   
        for(int i = 0; i < items.size(); i++){        
            Object[] sItem = (Object[])items.elementAt(i);
            int sItemWidth = aFont.stringWidth((String)sItem[1]) + iHPaddings * 2;
            if(sItem.length > 2 && sItem[2] != null){
                int sShortcutWidth = aFont.stringWidth((String)sItem[2]) + SHORTCUT_RECT_PADDINGS * 2;
                iShortcutDescWidth = Math.max(iShortcutDescWidth, sShortcutWidth );
                sItemWidth += sShortcutWidth + ITEM_SPACE;
            }
           
            int[] sControls = (int[])sItem[0];
            if(sControls[4] > 0){
                sItemWidth += SUB_MENU_ICON_WIDTH + ITEM_SPACE;
            }
            
            if(iSelectedIndex == -1 && sControls[3] > 0){
                iSelectedIndex = i;
            }
            
            itemWidth = Math.max(itemWidth, sItemWidth);
        }
        
        if(itemWidth > gPaintableRect[2]){
            itemWidth = gPaintableRect[2];
        }
        
        itemHeight = aFont.getHeight() + 2 * iVPaddings;        
        iWidth = itemWidth;
        iHeight = itemHeight * items.size();
        if(iSeparators != null){
            iHeight += iSeparators.size();
        }
        
        if(iHeight > gPaintableRect[3]){
            iDisplayItemNum = (gPaintableRect[3] - 2 * iControlItemHeight) / itemHeight;
            iHeight = itemHeight * iDisplayItemNum + 2 * iControlItemHeight ;
            iHasUpDownItem = true;
        }else{
            iDisplayItemNum = items.size();
            iHasUpDownItem = false;
        }

        switch (iAnchor) {
        case ANCHOR_TOP_LEFT:
            iPosX = iPosXBackup;
            iPosY = iPosYBackup;
            break;            
        case ANCHOR_TOP_RIGHT:
            iPosX = iPosXBackup - iWidth;
            iPosY = iPosYBackup;
            break;
        case ANCHOR_BOTTOM_LEFT:
            iPosX = iPosXBackup;
            iPosY = iPosYBackup - iHeight;
            break;
        case ANCHOR_BOTTOM_RIGHT:
            iPosX = iPosXBackup - iWidth;
            iPosY = iPosYBackup - iHeight;
            break;
        default:
            break;
        }
        
        if(iPosX < gPaintableRect[0]){
            iPosX = gPaintableRect[0];
        }
        
        if(iPosX > gPaintableRect[0] + gPaintableRect[2] - iWidth){
            iPosX = gPaintableRect[0] + gPaintableRect[2] - iWidth;
        }
        
        if(iPosY < gPaintableRect[1]){
            iPosY = gPaintableRect[1];
        }
        
        if(iPosY > gPaintableRect[1] + gPaintableRect[3] - iHeight){
            iPosY = gPaintableRect[1] + gPaintableRect[3] - iHeight;
        }
    }   
    
    private boolean hasSeparatorAtInedex(int aIndex){
        if(iSeparators != null && iSeparators.size() > 0){
            for(int i = 0; i < iSeparators.size(); i++){
                Integer sIdx = (Integer)(iSeparators.elementAt(i));
                if(sIdx.intValue() == aIndex){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 
     * <p>���������setPaintableRect�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param ax
     * @param ay
     * @param aw
     * @param ah
     */
    public static void setPaintableRect(int ax, int ay, int aw, int ah){
        gPaintableRect[0] = ax;
        gPaintableRect[1] = ay;
        gPaintableRect[2] = aw;
        gPaintableRect[3] = ah;
    }
    
    /**
     * 
     * <p>���������setItemEnabled�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @param aIsEnabled
     */
    public static void setItemEnabled(Object aItem, boolean aIsEnabled){
        if(aItem instanceof Object[]){
            Object [] sItem = (Object[])aItem;
            if(sItem != null && sItem.length > 1){
                int [] sControls = (int[]) sItem[0];
                if(sControls != null && sControls.length > 4){
                    sControls[3] = aIsEnabled? 1 : 0;
                }
            }
        }
    }
    
    /**
     * 
     * <p>���������isItemEnabled�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @return
     */
    public static boolean isItemEnabled(Object aItem){
        if(aItem instanceof Object[]){
            Object [] sItem = (Object[])aItem;
            if(sItem != null && sItem.length > 1){
                int [] sControls = (int[]) sItem[0];
                if(sControls != null && sControls.length > 4){
                    return (sControls[3] > 0);
                }
            }
        }
    
        return false;
    }
    
    /**
     * 
     * <p>���������isItemHasSubMenu�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @return
     */
    public static boolean isItemHasSubMenu(Object aItem){
        if(aItem instanceof Object[]){
            Object [] sItem = (Object[])aItem;
            if(sItem != null && sItem.length > 1){
                int [] sControls = (int[]) sItem[0];
                if(sControls != null && sControls.length > 4){
                    return (sControls[4] > 0);
                }
            }
        }

        return false;
    }
    
    /**
     * 
     * <p>���������getItemActionId�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @return
     */
    public static int getItemActionId(Object aItem){
        if(aItem instanceof Object[]){
            Object [] sItem = (Object[])aItem;
            if(sItem != null && sItem.length > 1){
                int [] sControls = (int[]) sItem[0];
                if(sControls != null && sControls.length > 4){
                    return sControls[0];
                }
            }
        }
      
        return -1;
    }
    
    /**
     * 
     * <p>���������isUpItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @return
     */
    public static boolean isUpItem(Object aItem){
        if(aItem instanceof int[]){
            int[] sItem = (int[])aItem;
            if(sItem != null && sItem.length > 0){
               return sItem[0] == UP_ITEM_ACTIION_ID; 
            }
        }
      
        return false;
    }
    
    /**
     * 
     * <p>���������isDownItem�����ṩ�Ĺ���</p>
     *
     * <b>�޸���ʷ</b>
     * <ol>
     * <li>������Added by Roy on 2012-3-29��</li>
     * </ol>
     * @param aItem
     * @return
     */
    public static boolean isDownItem(Object aItem){
        if(aItem instanceof int[]){
            int[] sItem = (int[])aItem;
            if(sItem != null && sItem.length > 0){
               return sItem[0] == DOWN_ITEM_ACTIION_ID; 
            }
        }
        return false;
    }
}
