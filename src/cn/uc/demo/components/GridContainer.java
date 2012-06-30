package cn.uc.demo.components;

import cn.uc.tiny.Component;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.debug.Log;

public class GridContainer extends Component
{
  public static final String TAG = "GridContainer";
  public int iColum = 2;
  public int iRowH = 100;
  public int iSpacing = 10;
  public static final int DEFOULT_COL = 2;
  public static final int DEFOULT_ROW_H = 100;
  public static final int SPACING = 10;
  private Brush bgBrush = Brush.createColorBrush(-18751);

  public void setCol(int aCol)
  {
    this.iColum = aCol;
  }

  public void setRowH(int aRowH) {
    this.iRowH = aRowH;
  }

  public void setSpacing(int aSpacing) {
    this.iSpacing = aSpacing;
  }

  public GridContainer(int aCol, int aRowH, int aSpacing)
  {
    setCol(aCol);
    setRowH(aRowH);
    setSpacing(aSpacing);
    setAttr(21, true);
    setAttr(12, true);
    setAttr(23, true);
  }

  public GridContainer() {
    this(2, 100, 10);
  }

  public String getClazz()
  {
    return "Grid";
  }

  public int getScrollbarClickableLength()
  {
    return 10;
  }

  public void setBackground(Brush aBgBrush)
  {
    this.bgBrush = aBgBrush;
    setAttr(11, this.bgBrush.isOpaque());
  }

  public void layout()
  {
    Log.d("GridContainer", "Grid container layout...");

    int colW = (this.width - (this.iColum + 1) * this.iSpacing) / this.iColum;
    int rowH = this.iRowH;

    int yoff = 0;
    int row = 0; int col = 0;
    int count = getComponentCount();
    for (int i = 0; i < count; col = i % this.iColum)
    {
      int xoff = colW * col + (col + 1) * this.iSpacing;
      yoff = rowH * row + (row + 1) * this.iSpacing;
      Component c = getComponentAt(i);
      c.setBounds(xoff, yoff, colW, rowH);

      i++; row = i / this.iColum;
    }

    super.layout();
  }

  protected void paintBackground(GraphicsEx aG)
  {
    aG.setBrush(this.bgBrush);
    aG.fillRectEx(this.x, this.y, this.width, this.height);
  }
}