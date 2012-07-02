package india.xxoo.ui.listAdapt;

import java.util.Vector;

import javax.microedition.lcdui.Image;

import cn.uc.tiny.ListView;
import cn.uc.tiny.Popup;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;

public class ChartListAdapt extends BasicEventHandler
  implements ListAdapter
{
  public static final byte SENDING = 0;
  public static final byte ERROR = 1;
  public static final byte COMPLETE = 2;
  public static final byte TEXT = 0;
  public static final byte SOUND = 1;
  public static final byte IMAGE = 2;
  public static final byte IN = 0;
  public static final byte OUT = 1;
  public static final byte SYSTEM = 2;
  public static final int INTERVAL = 5;
  Image sHead1;
  Image sHead2;
  public Vector LIST = new Vector();
  private ListView view;

  public ChartListAdapt()
  {
    testAddChart();
    try {
      this.sHead1 = Image.createImage("/1.png");
      this.sHead2 = Image.createImage("/2.png");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testAddChart() {
    addChart(2, 0, 2, "6月2日10:45:23");
    addChart(1, 0, 2, "在啊");
    addChart(0, 0, 2, "恩");
    addChart(1, 0, 2, "怎么样最近");
    addChart(2, 0, 2, "6月2日10:55:00");
    addChart(0, 0, 2, "就那样吧，没啥特别的");
    addChart(1, 0, 1, "爸妈身体都好吧");
    addChart(0, 0, 0, "恩，谢谢关心");
  }

  public void addChart(int aSource, int aType, int aState, Object aContent)
  {
    Object[] sData = new Object[2];
    sData[0] = new byte[]{(byte)aSource, (byte)aType, (byte)aState };
    sData[1] = aContent;
    this.LIST.addElement(sData);
    if (this.view != null)
      this.view.layout();
  }

  public String getClazz()
  {
    return "AdaptList";
  }

  public void setListView(ListView aListView)
  {
    this.view = aListView;
    this.view.setScrollbarClickableLength(10);
  }

  public int getCount()
  {
    return this.LIST.size();
  }

  public int getItemCommonHeight(int aIdx)
  {
    return FontEx.getDefaultFont().getHeight() + 10;
  }

  public Object getItem(int aIdx)
  {
    return this.LIST.elementAt(aIdx);
  }

  public int getItemSource(int aIdx) {
    return ((byte[])((Object[])this.LIST.elementAt(aIdx))[0])[0];
  }

  public int getItemType(int aIdx) {
    return ((byte[])((Object[])this.LIST.elementAt(aIdx))[0])[1];
  }

  public int getItemState(int aIdx) {
    return ((byte[])((Object[])this.LIST.elementAt(aIdx))[0])[2];
  }

  public String getItemText(int aIdx)
  {
    if (getItemType(aIdx) == 0) {
      return (String)((Object[])this.LIST.elementAt(aIdx))[1];
    }
    return null;
  }

  public int paintItem(GraphicsEx aG, int aIdx, int aItemCurrHeight, int aItemCurrWidth)
  {
    int aSource = getItemSource(aIdx);
    String sText = getItemText(aIdx);
    int sTextWidth = FontEx.getDefaultFont().stringWidth(sText);
    int sBoxX = 0;
    int sImageX = 0;
    int aMaxWidth = 0;
    Image sHead = null;
    try
    {
      if (aSource == 2) {
        aG.drawString(sText, (aItemCurrWidth - sTextWidth) / 2, 0, 20);
      }
      else {
        if (aSource == 1) {
          sHead = this.sHead1;
          int sImageWidth = sHead.getWidth();
          aMaxWidth = aItemCurrWidth - sImageWidth * 2;
          sBoxX = sImageWidth;
        } else if (aSource == 0) {
          sHead = this.sHead2;
          int sImageWidth = sHead.getWidth();
          aMaxWidth = aItemCurrWidth - sImageWidth * 2;
          if (sTextWidth > aMaxWidth)
            sBoxX = sImageWidth;
          else {
            sBoxX = sImageWidth + aMaxWidth - sTextWidth - 15;
          }
          sImageX = aItemCurrWidth - sImageWidth;
        }

        if (sHead != null) {
          aG.drawImage(sHead, sImageX, 0, 20);
        }

        aG.drawRect(sBoxX, 0, Math.min(aMaxWidth, sTextWidth + 15), getItemCommonHeight(aIdx));

        aG.drawString(getItemText(aIdx), sBoxX + 10, 5, 20);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return getItemCommonHeight(aIdx) * 2;
  }

  public void onItemClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 138;

    Popup.buildToast(
      "ListView click : " + aIdx + ", " + getItemText(aIdx) + ", at " + aItemX + 
      ", " + aItemY).show();
  }

  public void onItemLongClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 134;

    Popup.buildToast(
      "ListView long click : " + aIdx + ", " + getItemText(aIdx) + ", at " + 
      aItemX + ", " + aItemY).show();
  }

  public void onItemSelected(int aIdx)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 134;
  }

  public void onItemUnSelected(int aIdx)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 138;
  }

  public String toString()
  {
    return "DemoListAdapter";
  }
}