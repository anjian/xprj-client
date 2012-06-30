package cn.uc.demo;

import cn.uc.demo.components.Button;
import cn.uc.demo.components.GridContainer;
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

public class ContainerDemo extends MIDlet
  implements EventHandler, MenuSource
{
  private boolean startup = false;

  private int viewcolor = -984833;
  private int viewalpha = 128;

  private int color = -1286;
  private int bgcolor = -16181;
  private int alpha = 255;

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

    SimpleWindow win = new SimpleWindow();
    GridContainer grid = new GridContainer();
    grid.setBackground(Brush.createColorBrush(this.viewalpha, this.viewcolor));

    for (int i = 0; i < 24; i++)
    {
      int row = i / 2 + 1;
      int col = i % 2 + 1;

      Button button = new Button();
      button.setText("Button (" + row + ", " + col + ")");
      button.setTextColor(this.color);
      button.setIcon(Resource.getImage(6, false));
      button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
      button.setActionTarget(this);
      grid.addComponent(button);
    }
    win.addView(grid);

    grid = new GridContainer();
    grid.setBackground(Brush.createColorBrush(this.viewalpha, this.viewcolor));

    for (int i = 0; i < 12; i++)
    {
      int row = i / 2 + 1;
      int col = i % 2 + 1;

      GridContainer g = new GridContainer();
      g.setHasBackground(false);

      for (int j = 0; j < 12; j++)
      {
        row = j / 2 + 1;
        col = j % 2 + 1;

        Button button = new Button();
        button.setText(row + "," + col);
        button.setTextColor(this.color);
        button.setIcon(Resource.getImage(6, 
          false));
        button.setBackground(Brush.createColorBrush(this.alpha, this.bgcolor));
        button.setActionTarget(this);

        g.addComponent(button);
      }
      grid.addComponent(g);
    }
    win.addView(grid);

    win.setMenuSource(this);
    win.setTitle("Container");
    win.show();
  }

  public Menu getWindowMenu()
  {
    Menu menu = Menu.buildWindowMenu(CommandEx.create(345), 
      CommandEx.create(332));
    menu.setOwner(this);
    return menu;
  }

  public Menu getContextMenu(Component aContextCmp, int aX, int aY)
  {
    return null;
  }

  public Menu getSubMenu(CommandEx aGroup)
  {
    return null;
  }

  public boolean event(Event aEv)
  {
    if (aEv.isCommandAction()) {
      switch (aEv.getCommandId())
      {
      case 332:
        CanvasEx.exit();
        break;
      case 345:
        CanvasEx.getCurrWindow().switchNextView(true);
      }

      return true;
    }if (aEv.isComponentAction())
    {
      Popup.gToastEndAnchor = 131;
      Button button = (Button)aEv.getActionComponent();
      Popup toast = Popup.buildToast(button.getText() + 
        " has been clicked.");
      toast.show();
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