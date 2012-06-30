package cn.uc.demo;

import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
import cn.uc.demo.components.SimpleView;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.Popup;
import cn.uc.tiny.Resource;
import cn.uc.tiny.Window;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.util.debug.Log;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class PopupDemo extends MIDlet
  implements EventHandler, MenuSource
{
  static final int CMD_ID_COLOR = 1000;
  static final int CMD_ID_ALPHA = 1001;
  static final int CMD_ID_T_COLOR = 1002;
  static final int CMD_ID_V_ALPHA = 1003;
  static final int CMD_ID_V_COLOR = 1004;
  static final int CMD_ID_RED = 1010;
  static final int CMD_ID_GREEN = 1011;
  static final int CMD_ID_BLUE = 1012;
  static final int CMD_ID_YELLOW = 1013;
  static final int CMD_ID_ORANGE = 1014;
  static final int CMD_ID_BLACK = 1015;
  static final int CMD_ID_THREE_QUARTER_TRANSPARENT = 1020;
  static final int CMD_ID_HALF_TRANSPARENT = 1021;
  static final int CMD_ID_QUARTER_TRANSPARENT = 1022;
  static final int CMD_ID_EIGHTN_TRANSPARENT = 1023;
  static final int CMD_ID_LITTLE_TRANSPARENT = 1024;
  static final int CMD_ID_OPAQUE = 1025;
  static final int CMD_ID_V_RED = 1050;
  static final int CMD_ID_V_GREEN = 1051;
  static final int CMD_ID_V_BLUE = 1052;
  static final int CMD_ID_V_YELLOW = 1053;
  static final int CMD_ID_V_ORANGE = 1054;
  static final int CMD_ID_V_BLACK = 1055;
  static final int CMD_ID_V_THREE_QUARTER_TRANSPARENT = 1040;
  static final int CMD_ID_V_HALF_TRANSPARENT = 1041;
  static final int CMD_ID_V_QUARTER_TRANSPARENT = 1042;
  static final int CMD_ID_V_EIGHTN_TRANSPARENT = 1043;
  static final int CMD_ID_V_LITTLE_TRANSPARENT = 1044;
  static final int CMD_ID_V_OPAQUE = 1045;
  static final int CMD_ID_T_RED = 1030;
  static final int CMD_ID_T_GREEN = 1031;
  static final int CMD_ID_T_BLUE = 1032;
  static final int CMD_ID_T_YELLOW = 1033;
  static final int CMD_ID_T_ORANGE = 1034;
  static final int CMD_ID_T_BLACK = 1035;
  SimpleView view;
  Button button;
  private boolean startup = false;

  private int viewcolor = -984833;
  private int viewalpha = 255;

  private int color = -1286;
  private int bgcolor = -8355712;
  private int alpha = 128;

  protected void destroyApp(boolean aArg0)
    throws MIDletStateChangeException
  {
  }

  protected void pauseApp()
  {
  }

  protected void startApp()
    throws MIDletStateChangeException
  {
    if (this.startup) {
      CanvasEx.restoreCanvas();
      return;
    }

    this.startup = true;

    Log.addTagFilter("Paint");
    Log.addTagFilter("Event");
    Log.addTagFilter("Timer");
    Log.addTagFilter("PntCmp");
    Log.addTagFilter("PntScl");
    Log.addTagFilter("DRAW");
    Log.addTagFilter("ANIMATION");
    Log.addTagFilter("MOTION");

    CanvasEx.initCanvas(this);
    FontEx.initializeUsedFonts(185);
    FontEx.setDefaultFont(FontEx.getFont(0, 
      0));

    this.view = new SimpleView();
    this.view.setBackground(Brush.createColorBrush(this.viewalpha, this.viewcolor));

    this.button = new Button();
    this.button.setId("toast_btn");
    this.button.setText("Pop Toast");
    this.button.setTextColor(this.color);
    this.button.setIcon(Resource.getImage(6, false));
    this.button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
    this.button.setActionTarget(this);

    this.view.addComponent(this.button);
    this.button.setBounds(50, 50, 120, 50);

    this.button = new Button();
    this.button.setId("dialog_btn");
    this.button.setText("Pop Dialog");
    this.button.setTextColor(this.color);
    this.button.setIcon(Resource.getImage(5, false));
    this.button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
    this.button.setActionTarget(this);

    this.view.addComponent(this.button);
    this.button.setBounds(50, 110, 120, 50);

    this.button = new Button();
    this.button.setId("complex_dialog_btn");
    this.button.setText("Pop Complex Dlg");
    this.button.setTextColor(this.color);
    this.button.setIcon(Resource.getImage(5, false));
    this.button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
    this.button.setActionTarget(this);

    this.view.addComponent(this.button);
    this.button.setBounds(50, 170, 150, 50);

    SimpleWindow win = new SimpleWindow();
    win.setMenuSource(this);
    win.setTitle("Popup");
    win.addView(this.view);
    win.show();
  }

  public Menu getWindowMenu()
  {
    Menu menu = Menu.buildWindowMenu(null, CommandEx.create(332));
    menu.setOwner(this);
    return menu;
  }

  public Menu getContextMenu(Component aContextCmp, int aX, int aY)
  {
    return null;
  }

  public Menu getSubMenu(CommandEx aGroup)
  {
    CommandEx[] commands = (CommandEx[])null;

    switch (aGroup.getId())
    {
    case 1000:
      commands = new CommandEx[] { CommandEx.create(1010, "红色"), 
        CommandEx.create(1011, "绿色"), 
        CommandEx.create(1012, "蓝色"), 
        CommandEx.create(1013, "黄色"), 
        CommandEx.create(1014, "橙色"), 
        CommandEx.create(1015, "黑色") };
      break;
    case 1001:
      commands = new CommandEx[] { 
        CommandEx.create(1020, "3/4透明"), 
        CommandEx.create(1021, "1/2透明"), 
        CommandEx.create(1022, "1/4透明"), 
        CommandEx.create(1023, "1/8透明"), 
        CommandEx.create(1024, "一点点透明"), 
        CommandEx.create(1025, "不透明") };
      break;
    case 1002:
      commands = new CommandEx[] { CommandEx.create(1030, "红色"), 
        CommandEx.create(1031, "绿色"), 
        CommandEx.create(1032, "蓝色"), 
        CommandEx.create(1033, "黄色"), 
        CommandEx.create(1034, "橙色"), 
        CommandEx.create(1035, "黑色") };
      break;
    case 1004:
      commands = new CommandEx[] { CommandEx.create(1050, "红色"), 
        CommandEx.create(1051, "绿色"), 
        CommandEx.create(1052, "蓝色"), 
        CommandEx.create(1053, "黄色"), 
        CommandEx.create(1054, "橙色"), 
        CommandEx.create(1055, "黑色") };
      break;
    case 1003:
      commands = new CommandEx[] { 
        CommandEx.create(1040, "3/4透明"), 
        CommandEx.create(1041, "1/2透明"), 
        CommandEx.create(1042, "1/4透明"), 
        CommandEx.create(1043, "1/8透明"), 
        CommandEx.create(1044, "一点点透明"), 
        CommandEx.create(1045, "不透明") };
    }

    return Menu.buildSubMenu("SubMenu-" + aGroup.getLabel(), commands);
  }

  public boolean event(Event aEv)
  {
    if (aEv.isCommandAction()) {
      switch (aEv.getCommandId())
      {
      case 332:
        CanvasEx.exit();
        break;
      case -4:
        CanvasEx.getCurrWindow().closeModals();
        break;
      case 1010:
        this.bgcolor = -65536;
        break;
      case 1011:
        this.bgcolor = -16744448;
        break;
      case 1012:
        this.bgcolor = -16776961;
        break;
      case 1013:
        this.bgcolor = -256;
        break;
      case 1014:
        this.bgcolor = -23296;
        break;
      case 1015:
        this.bgcolor = -16777216;
        break;
      case 1030:
        this.color = -65536;
        break;
      case 1031:
        this.color = -16744448;
        break;
      case 1032:
        this.color = -16776961;
        break;
      case 1033:
        this.color = -256;
        break;
      case 1034:
        this.color = -23296;
        break;
      case 1035:
        this.color = -16777216;
        break;
      case 1020:
        this.alpha = 64;
        break;
      case 1021:
        this.alpha = 128;
        break;
      case 1022:
        this.alpha = 192;
        break;
      case 1023:
        this.alpha = 224;
        break;
      case 1024:
        this.alpha = 240;
        break;
      case 1025:
        this.alpha = 255;
        break;
      case 1050:
        this.viewcolor = -65536;
        break;
      case 1051:
        this.viewcolor = -16744448;
        break;
      case 1052:
        this.viewcolor = -16776961;
        break;
      case 1053:
        this.viewcolor = -256;
        break;
      case 1054:
        this.viewcolor = -23296;
        break;
      case 1055:
        this.viewcolor = -16777216;
        break;
      case 1040:
        this.viewalpha = 64;
        break;
      case 1041:
        this.viewalpha = 128;
        break;
      case 1042:
        this.viewalpha = 192;
        break;
      case 1043:
        this.viewalpha = 224;
        break;
      case 1044:
        this.viewalpha = 240;
        break;
      case 1045:
        this.viewalpha = 255;
      }

      this.button.setTextColor(this.color);
      this.button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));

      this.view.setBackground(Brush.createColorBrush(this.viewalpha, this.viewcolor));
      this.view.repaint(10);

      Popup toast = Popup.buildToast("Color or background changed.");
      toast.show();
      return true;
    }if (aEv.isComponentAction())
    {
      Button button = (Button)aEv.getActionComponent();

      if (button.getId().equals("toast_btn"))
      {
        Popup.gToastEndAnchor = 148;
        Popup toast = Popup.buildToast("I am a Toast, and I have been pop to left top corner, aHHH!!!");

        toast.show();

        Popup.gToastEndAnchor = 152;
        toast = Popup.buildToast("I am a Toast, and I have been pop to right top corner, aHHH!!!");

        toast.show();

        Popup.gToastEndAnchor = 164;
        toast = Popup.buildToast("I am a Toast, and I have been pop to left bottom corner, aHHH!!!");

        toast.show();

        Popup.gToastEndAnchor = 168;
        toast = Popup.buildToast("I am a Toast, and I have been pop to right bottom corner, aHHH!!!");

        toast.show();

        Popup.gToastEndAnchor = 131;
        toast = Popup.buildToast("I am a Toast, and I have been pop to center, aHHH!!!");

        toast.show();
      } else if (button.getId().equals("dialog_btn"))
      {
        CommandEx[] commands = { 
          CommandEx.createGroup(1000, "选择按钮背景颜色"), 
          CommandEx.createGroup(1001, "选择按钮背景透明度"), 
          CommandEx.createGroup(1002, "选择按钮文本颜色"), 
          CommandEx.createGroup(1004, "选择视图背景颜色"), 
          CommandEx.createGroup(1003, "选择视图背景透明度") };

        Menu menu = Menu.buildWindowMenu("DemoMenu", 
          CommandEx.CMD_GROUP_MENU, CommandEx.CMD_CANCEL, commands);
        menu.setOwner(this);
        Popup dialog = Popup.buildSimpleDialog("dialog", "颜色选择", 
          "选择按钮和视图的颜色", menu);
        dialog.show();
      } else if (button.getId().equals("complex_dialog_btn"))
      {
        GridContainer grid = new GridContainer();
        grid.setHasBackground(false);

        for (int i = 0; i < 12; i++)
        {
          int row = i / 2 + 1;
          int col = i % 2 + 1;

          Button btn = new Button();
          btn.setText("(" + row + ", " + col + ")");
          btn.setTextColor(this.color);
          btn.setIcon(Resource.getImage(6, 
            false));
          btn.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
          btn.setActionTarget(this);
          grid.addComponent(btn);
        }

        Popup dialog = Popup.buildComplexDialog("complex_dialog", 
          "带组件的对话框", grid, Menu.buildWindowMenu(
          CommandEx.CMD_CONFIRM, CommandEx.CMD_CANCEL));
        dialog.show();
      }
      return true;
    }

    return false;
  }

  public void keyEvent(Event aKeyEv)
  {
  }

  public void pointerEvent(Event aPtEv)
  {
  }

  public void actionEvent(Event aActEv)
  {
  }

  public void timerEvent(Event aTimerEv)
  {
  }

  public void progressEvent(Event aProgressEv)
  {
  }

  public void errorEvent(Event aErrorEv)
  {
  }

  public void sizeChanged(int aNewWidth, int aNewHeight)
  {
  }

  public void orentationChanged(int aNewOrentation)
  {
  }

  public void showNotify()
  {
  }

  public void hideNotify()
  {
  }

  public void focusGained(Event aCauseEv)
  {
  }

  public void focusLost(Event aCauseEv)
  {
  }

  public void onEventError(Throwable aErr)
  {
  }
}