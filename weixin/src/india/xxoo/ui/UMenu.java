package india.xxoo.ui;

import india.xxoo.adapter.MyFont;
import india.xxoo.adapter.MyGraphics;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;


/**
 * 
 * <b>UMenu简介:</b>
 * <p>简要的描述此类的作用</p>
 *
 * <b>功能描述:</b>
 * <p>具体功能点描述，当添加新功能时，要求更新此描述</p>
 *
 * <b>修改历史</b>
 * <p>
 *  <ol>
 *   <li>创建（Added by Roy on 2012-3-19）</li>
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
    
    private int[] iUpItem = null;//向上菜单项
    private int[] iDownItem = null;//向上下菜单项
    private boolean iHasUpDownItem = false;
    private static final int UP_ITEM_ACTIION_ID = -65530;
    private static final int DOWN_ITEM_ACTIION_ID = -65531;
    private static final int TOUCH_CONTROL_ITEM_HEIGHT = 20;
    private static final int CONTROL_ITEM_HEIGHT = 10;
    private static final int CONTROL_ICON_WIDTH = 8;
    private int iControlItemHeight = CONTROL_ITEM_HEIGHT;
    
    //菜单显示锚点
    public static final int ANCHOR_TOP_LEFT = 0;//左上
    public static final int ANCHOR_TOP_RIGHT = 1;//右上
    public static final int ANCHOR_BOTTOM_LEFT = 2;//左下
    public static final int ANCHOR_BOTTOM_RIGHT = 3;//右下
    private int iAnchor = ANCHOR_TOP_LEFT;   
    
    /**
     * 可画区域:
     * gPaintableRect[0]:左边坐标
     * gPaintableRect[1]:上边坐标
     * gPaintableRect[2]:长度
     * gPaintableRect[3]:左宽度
     */
    public static int[] gPaintableRect = new int [4];    
    
    public static boolean gIsTouchMode = false;//是否触屏
    
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
     * <p>请具体描述getId方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
     * </ol>
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     * 
     * <p>添加菜单项</p>
     *  每个菜单项由一个Object[3]数组表示，Object[0]为一个int[5]，存储一些控制信息（分别为action Id, posX, posY, enable, hasSubMenu）；
     *  Object[1]是一个String，为菜单项的文字； Object[2]是一个String，为菜单项的快捷键
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @param aText 菜单项
     * @param actionId  执行ID
     */
    public void addItem(String aText, int actionId){
        addItem(aText, actionId, null);
    }
    
    /**
     * 
     * <p>添加菜单项</p>
     *  每个菜单项由一个Object[3]数组表示，Object[0]为一个int[5]，存储一些控制信息（分别为action Id, posX, posY, enable, hasSubMenu）；
     *  Object[1]是一个String，为菜单项的文字； Object[2]是一个String，为菜单项的快捷键
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @param aText 菜单项
     * @param actionId 执行ID
     * @param aShortcut 快捷描述
     */
    public void addItem(String aText, int actionId, String aShortcut){
        int [] sControls = {actionId, 0, 0, 1, 0};//action Id, posX, posY, enable, hasSubMenu
        Object[] sItem = {sControls, aText, aShortcut};
        items.addElement(sItem);
    }
    
    /**
     * 
     * <p>添加分隔线</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-28）</li>
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
     * <p>请具体描述addMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @param aMenu 子菜单
     * @param aIndex 绑定的Item Index
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
     * <p>请具体描述addMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述clear方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述getSubMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述getSubMenus方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-26）</li>
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
     * <p>请具体描述setParent方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @param aMenu
     */
    public void setParent(UMenu aMenu){
        iParent = aMenu;
    }
    
    /**
     * 
     * <p>请具体描述getParentMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @return
     */
    public UMenu getParentMenu(){
        return iParent;
    }
    
    /**
     * 
     * <p>请具体描述getAncestorMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述isSubMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @return
     */
    public boolean isSubMenu(){
        return (iParent != null);
    }
    
    /**
     * 
     * <p>请具体描述isVisible方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
     * </ol>
     * @return
     */
    public boolean isVisible(){
        return isVisibale;
    }
    
    /**
     * 
     * <p>请具体描述setVisible方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
     * </ol>
     * @param aIsVisible
     */
    public void setVisible(boolean aIsVisible){
        isVisibale = aIsVisible;
    }
    
    /**
     * 
     * <p>请具体描述show方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-26）</li>
     * </ol>
     */
    public void show(){
        isVisibale = true;
        
        setActivate(true);
    }
    
    /**
     * 
     * <p>请具体描述show方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述show方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述close方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
     * <p>请具体描述closeSubMenus方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-26）</li>
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
     * <p>请具体描述setSelectedItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述setActivate方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述getActivateMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述moveUp方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述moveDown方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述getSelectedIndex方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
     * </ol>
     * @return
     */
    public int getSelectedIndex(){
        return iSelectedIndex;
    }
    
    /**
     * 
     * <p>请具体描述getSelectedItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述getItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述getItems方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
     * </ol>
     * @return
     */
    public Vector getItems(){
        return items;
    }
    
    /**
     * 
     * <p>请具体描述getItemTop方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-26）</li>
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
     * <p>请具体描述containsPoint方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述getSelectedItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-22）</li>
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
     * <p>请具体描述draw方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-19）</li>
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
        
        //绘制内边框
        aGraphics.setColor(isActivate ? sColors[27] : sColors[37]);
        aGraphics.drawRect(iPosX + 1, iPosY + 1, iWidth - 3, iHeight - 3);

        //绘制外边框
        aGraphics.setColor(isActivate ? sColors[28] : sColors[38]);
        aGraphics.drawShortCornerFrame(iPosX , iPosY, iWidth, iHeight, 0);
        
        int sPosY = iPosY;        
        if(iHasUpDownItem){//如果不显示全部则画上下按钮
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
        
        //画菜单项      
        for(int i = iStartIndex; i < iStartIndex + iDisplayItemNum; i++){
            Object[] sItem = (Object[])items.elementAt(i);
            int[] sControls = (int[])sItem[0];            
            sControls[1] = iPosX;
            sControls[2]= sPosY;
            
            boolean sIsFocus = (iSelectedIndex == i);
            if(sIsFocus){   //焦点背景
                aGraphics.fillGradualColorRoundCornerRect(iPosX + 1, sPosY + 1, itemWidth -2, itemHeight - 2,
                        new int[]{sColors[29],sColors[53],sColors[54],sColors[55],sColors[56]}, 0, 5, 1);
            }                      
                       
            //绘制文本
            int sTextColor = isActivate? sColors[33]:sColors[34];
            if (sControls[3] == 0) { //灰色字体,不可用
                sTextColor = isActivate? sColors[31]:sColors[39];
            } 
            if (sIsFocus) { //获得焦点
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
           
            //画子菜单提示三角
            if(sControls[4] > 0){
                int sX = iPosX + itemWidth - iHPaddings - SUB_MENU_ICON_WIDTH;
                aGraphics.fillTriangle(sX, sPosY + itemHeight / 2 - SUB_MENU_ICON_WIDTH / 2, sX, sPosY + itemHeight / 2 + SUB_MENU_ICON_WIDTH / 2, 
                        iPosX + iWidth - iHPaddings, sPosY + itemHeight / 2);
            }
            
            //画快捷方式
            if(sItem.length > 2 && sItem[2] != null){
                int[] sColor = new int[3];//0 背景 ,1 辅助线 ,2 字体
                if (sControls[3] == 0) {    //灰色字体,不可用
                    sColor[0]=sColors[49];
                    sColor[1]=sColors[50];
                    sColor[2]=sColors[51];
                } else if (sIsFocus) { //获得焦点
                    sColor[0] = sColors[46];
                    sColor[1]=sColors[47];
                    sColor[2]=sColors[48];
                } else {  //普通
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
                //绘制辅助线
                aGraphics.setColor(sColor[1]); 
                aGraphics.drawLine(sRectLeft + 1, sRectTop+1, sRectLeft+sRectWidth-2, sRectTop+1);
                //绘制文字
                aGraphics.setColor(sColor[2]);             
                aGraphics.drawString(sText, sRectLeft + (sRectWidth - MyFont.getMyFont().stringWidth(sText)) / 2, sPosY + iVPaddings, Graphics.TOP | Graphics.LEFT);
            }
            
            sPosY += itemHeight;
            
            if(hasSeparatorAtInedex(i)){//画分隔线
                aGraphics.setColor(isActivate? sColors[31]:sColors[39]);
                aGraphics.drawLine(iPosX + 2, sPosY, iPosX + itemWidth - 4, sPosY);
                sPosY += 1;
            }
        }
        
        if(iHasUpDownItem){  //如果不显示全部则画下按钮          
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
     * <p>请具体描述setPaintableRect方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述setItemEnabled方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述isItemEnabled方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述isItemHasSubMenu方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述getItemActionId方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述isUpItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
     * <p>请具体描述isDownItem方法提供的功能</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by Roy on 2012-3-29）</li>
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
