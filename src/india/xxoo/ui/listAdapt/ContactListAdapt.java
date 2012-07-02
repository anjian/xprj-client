package india.xxoo.ui.listAdapt;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import cn.uc.tiny.ListView;
import cn.uc.tiny.Popup;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;

public class ContactListAdapt extends BasicEventHandler
  implements ListAdapter
{
  public static final String[] LIST = { 
    "张三", "李四", "王五" };

  public static final String[] signature = { 
    "我的生活我做主", "拒绝低俗", "我来自火星" };
  private ListView view;

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
    return LIST.length;
  }

  public int getItemCommonHeight(int aIdx)
  {
    return FontEx.getDefaultFont().getHeight() + 10;
  }

  public Object getItem(int aIdx)
  {
    return LIST[aIdx];
  }

  public String getItemText(int aIdx)
  {
    return LIST[aIdx];
  }

  public int paintItem(GraphicsEx aG, int aIdx, int aItemCurrHeight, int aItemCurrWidth)
  {
    Image sHead = null;
    try {
      sHead = Image.createImage("/1.png");
    } catch (IOException e) {
      e.printStackTrace();
    }
    int ImageWidth = 0;
    if (sHead != null) {
      ImageWidth = sHead.getWidth();
      aG.drawImage(sHead, 0, 0, 20);
    }

    aG.drawString(LIST[aIdx], ImageWidth + 5, 0, 20);

    aG.drawString(signature[aIdx], ImageWidth + 5, getItemCommonHeight(aIdx), 20);
    return getItemCommonHeight(aIdx) * 2;
  }

  public void onItemClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 138;

    Popup.buildToast(
      "ListView click : " + aIdx + ", " + LIST[aIdx] + ", at " + aItemX + 
      ", " + aItemY).show();
  }

  public void onItemLongClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY)
  {
    Popup.gToastStartAnchor = 272;
    Popup.gToastEndAnchor = 134;

    Popup.buildToast(
      "ListView long click : " + aIdx + ", " + LIST[aIdx] + ", at " + 
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