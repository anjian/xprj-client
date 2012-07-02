/**
 * Tiny.cn.uc.ui.ListView.java, 2011-1-6
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import india.xxoo.ui.listAdapt.ListAdapter;

import javax.microedition.lcdui.Canvas;

import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.util.NumberUtils;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class ListView extends Component {

	public static final String TAG = "LIST";
	public static final boolean DEBUG = Component.DEBUG;

	private static final int[] NULL_OFFSETS = new int[] { Integer.MAX_VALUE };

	private static final int INVALID_ITEM_IDX = -1;

	private static final int ITEM_VISIBLE_SCROLL_DELAY = 800;
	private static final int SCROLL_ITEM_TO_VISIBLE = 0;
	private static final int SCROLL_ITEM_TO_TOP_VISIBLE = 1;

	private static int gScrollItemToVisibleTimer = -1;
	private static int gScrollItemToVisibleIdx;
	private static int gScrollItemToVisibleMethod;

	/** Margin left, right, top, bottom. */
	private int mgLeft;
	private int mgRight;
	private int mgTop;
	private int mgBottom;
	/** Scrollbar's clickable length. */
	private int sbClickableLen;

	/** List adapter. */
	private ListAdapter adapter;

	/** Count of items. */
	private int count;
	/** Current selected item index. */
	private int selected = INVALID_ITEM_IDX;
	/** Y offset table of items. */
	private int[] yoffsets;

	public ListView() {

		setAttr(Attr.SCROLL_Y_ENABLE, true);
		setAttr(Attr.SCROLLBAR_VISIBLE, true);
		setAttr(Attr.TENSILE_DRAG_ENABLE, true);
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return adapter != null ? adapter.getClazz() : "ListView";
	}

	/** {@inheritDoc} */
	protected void initializeComponent() {

		if (adapter != null && adapter.getCount() > 0) {

			setAttr(Attr.FOCUSABLE, true);

			count = adapter.getCount();
			yoffsets = new int[count + 1];

			if (!isItemIndexValid(selected)) {
				this.selectItem(INVALID_ITEM_IDX);
			}
		} else {

			setAttr(Attr.FOCUSABLE, false);

			Window win = getWindow();
			if (win != null && win.getFocused() == this) {
				win.setCurrentFocused(null);
				win.initFocused();
			}

			count = 0;
			yoffsets = NULL_OFFSETS;
			this.selectItem(INVALID_ITEM_IDX);
		}

		calcPreferredSize();
		guardScroll();
		this.repaint(Reason.UPDATE);
	}

	public void setMargins(int aLeft, int aTop, int aRight, int aBottom) {

		int newPreferredHeight = getPreferredHeight() - mgTop - mgBottom + aTop
			+ aBottom;
		setPreferredSize(width, newPreferredHeight);

		mgLeft = aLeft;
		mgTop = aTop;
		mgRight = aRight;
		mgBottom = aBottom;

		this.repaint(Reason.UPDATE);
	}

	public void setAdapter(ListAdapter aAdapter) {

		if (aAdapter != null) {
			aAdapter.setListView(this);
		}
		adapter = aAdapter;
	}

	public ListAdapter getAdapter() {

		return adapter;
	}

	public int getCount() {

		return count;
	}

	public void layout() {

		setState(State.INITIALIZED, false);
		initialize();
	}

	public void updateItem(int aIdx) {

		if (isItemIndexValid(aIdx)) {

			getAbsolutePos(gTempPoint);
			this.repaint(gTempPoint[0], gTempPoint[1] + getItemTopYOffset(aIdx)
				- getScrollY(), width, getItemHeight(aIdx), Reason.UPDATE);
		}
	}

	public void updateItemHeight(int aIdx, int aNewHeight) {

		if (isItemIndexValid(aIdx) && aNewHeight > 0) {

			int delta = aNewHeight - getItemHeight(aIdx);
			for (int idx = aIdx + 1; idx <= count; ++idx) {
				yoffsets[idx] += delta;
			}

			setPreferredSize(width, getPreferredHeight() + delta);
			this.repaint(Reason.UPDATE);
		}
	}

	public void selectItem(int aIdx) {

		this.selectItem(aIdx, true);
	}

	private void selectItem(int aIdx, boolean aDelayScrollItemToVisible) {

		if (aIdx != selected) {

			int old = selected;
			selected = aIdx;

			if (adapter != null && isItemIndexValid(old)) {
				updateItem(old);
				adapter.onItemUnSelected(old);
			}

			if (adapter != null && isItemIndexValid(selected)) {
				updateItem(selected);
				adapter.onItemSelected(selected);
			} else {
				selected = INVALID_ITEM_IDX;
			}
		}

		this.scrollItemToVisible(selected, aDelayScrollItemToVisible);
	}

	public boolean isItemIndexValid(int aIdx) {

		return aIdx >= 0 && aIdx < count;
	}

	public int getSelectedItem() {

		return selected;
	}

	public int getItemTopAbsoluteYOffset(int aIdx) {

		int y = 0;

		if (isItemIndexValid(aIdx)) {

			y = getItemTopYOffset(aIdx);
			y -= getScrollY();
			y += getAbsoluteY();
		}

		return y;
	}

	public int getItemBottomAbsoluteYOffset(int aIdx) {

		int y = 0;

		if (isItemIndexValid(aIdx)) {

			y = getItemTopYOffset(aIdx);
			y += getItemHeight(aIdx);
			y -= getScrollY();
			y += getAbsoluteY();
		}

		return y;
	}

	/**
	 * Get the height of the specified item.
	 * 
	 * @param aIdx specified item index
	 * @return height of the specified item
	 */
	private int getItemHeight(int aIdx) {

		return yoffsets[aIdx + 1] - yoffsets[aIdx];
	}

	/**
	 * Get the top Y offset of the specified item.
	 * 
	 * @param aIdx specified item index
	 * @return top Y offset of the specified item
	 * @see #getItemBottomYOffset(int)
	 */
	private int getItemTopYOffset(int aIdx) {

		return mgTop + yoffsets[aIdx];
	}

	/**
	 * Get the index of item by specified Y coordinate.
	 * 
	 * @param aY specified Y coordinate
	 * @return index of item
	 */
	private int getItemIndex(int aY) {

		aY -= mgTop;

		int begin = 0;
		int end = count;
		int center = 0;

		while (end - begin > 10) {

			center = begin + (end - begin) / 2;
			if (aY > yoffsets[center]) {
				begin = center;
			} else if (aY < yoffsets[center]) {
				end = center;
			} else {
				return center;
			}
		}

		for (int idx = begin; idx < end; ++idx) {
			if (aY >= yoffsets[idx] && aY < yoffsets[idx + 1]) {
				return idx;
			}
		}

		return INVALID_ITEM_IDX;
	}

	/**
	 * Get the index of item by specified Y coordinate, and round the index
	 * value when it is out of the valid range.
	 * 
	 * @param aY specified Y coordinate
	 * @return index of item
	 * @see #getItemIndex(int)
	 */
	private int getRoundItemIndex(int aY) {

		int idx = getItemIndex(aY);
		if (idx == INVALID_ITEM_IDX) {
			idx = aY < mgTop ? 0 : count - 1;
		}
		return idx;
	}

	private void triggerItemClick(Event aCauseEv, int aItemX, int aItemY) {

		adapter.onItemClicked(selected, aCauseEv, aItemX, aItemY);
	}

	private void triggerItemLongClick(Event aCauseEv, int aItemX, int aItemY) {

		adapter.onItemLongClicked(selected, aCauseEv, aItemX, aItemY);
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {

		if (adapter == null || adapter.event(aKeyEv))
			return;

		switch (aKeyEv.type) {

		case EventType.KEY_PRESSED:
		case EventType.KEY_REPEATED:
			if (aKeyEv.isNavigationKey()) {

				int newIdx = 0;
				switch (aKeyEv.getGameAction()) {

				case Canvas.UP:
					if (selected > 0) {
						this.selectItem(selected - 1);
						aKeyEv.accept();
					}
					break;

				case Canvas.DOWN:
					this.selectItem(Math.min(selected + 1, count - 1));
					aKeyEv.accept();
					break;

				case Canvas.LEFT:
				case Canvas.RIGHT:
					if (aKeyEv.getGameAction() == Canvas.LEFT) {
						newIdx = getRoundItemIndex(getItemTopYOffset(selected)
							- height + 1);
					} else {
						newIdx = getRoundItemIndex(getItemTopYOffset(selected)
							+ height - 1);
					}

					this.selectItem(newIdx, false);
					this.scrollItemToTopVisible(newIdx);
					aKeyEv.accept();
					break;
				}
			} else if (aKeyEv.isSelectKey()) {
				aKeyEv.accept();
			}
			break;

		case EventType.KEY_RELEASED:
			if (aKeyEv.isSelectKey()) {
				triggerItemClick(aKeyEv, 0, 0);
				aKeyEv.accept();
			}
			break;

		case EventType.KEY_LONG_PRESSED:
			if (aKeyEv.isSelectKey()) {
				triggerItemLongClick(aKeyEv, 0, 0);
				aKeyEv.accept();
			}
			break;
		}// switch
	}

	/** {@inheritDoc} */
	public void pointerEvent(Event aPtEv) {

		if (adapter == null)
			return;

		boolean dragging = isDragging();

		super.pointerEvent(aPtEv);
		if (aPtEv.isAccepted())
			return;

		int idx = getItemIndex(mapFromAbsoluteY(aPtEv.getY()));
		if (!isItemIndexValid(idx))
			return;

		switch (aPtEv.type) {

		case EventType.POINTER_PRESSED:
			this.selectItem(idx, false);
			aPtEv.accept();
			break;

		case EventType.POINTER_DRAGGED:
			stopSmoothScroll();
			aPtEv.accept();
			break;

		case EventType.POINTER_RELEASED:
		case EventType.POINTER_LONG_PRESSED:
			if (idx == selected && !dragging) {

				mapFromAbsolute(aPtEv.getX(), aPtEv.getY(), gTempPoint);
				int x = gTempPoint[0];
				int y = gTempPoint[1];
				x -= mgLeft;
				y -= getItemTopYOffset(idx);

				if (aPtEv.type == EventType.POINTER_RELEASED) {
					triggerItemClick(aPtEv, x, y);
				} else {
					triggerItemLongClick(aPtEv, x, y);
				}
				aPtEv.accept();
			}
			break;
		}
	}

	/** {@inheritDoc} */
	public void timerEvent(Event aTimerEv) {

		if (aTimerEv.getTimerId() == gScrollItemToVisibleTimer) {

			if (gScrollItemToVisibleMethod == SCROLL_ITEM_TO_VISIBLE) {
				this.scrollItemToVisible(gScrollItemToVisibleIdx, false);
			} else {
				this.scrollItemToTopVisible(gScrollItemToVisibleIdx, false);
			}

			CanvasEx.closeTimer(gScrollItemToVisibleTimer);
			gScrollItemToVisibleTimer = CanvasEx.INVALID_TIMER_INDEX;
			aTimerEv.accept();
		}
	}

	/** {@inheritDoc} */
	public void focusGained(Event aCauseEv) {

		// default make the first item selected when get focused by key
		if ((aCauseEv == null || aCauseEv.isKeyEvent())
			&& !isItemIndexValid(selected) && isItemIndexValid(0)) {
			this.selectItem(0);
		}
	}

	/** {@inheritDoc} */
	protected void calcPreferredSize() {

		int idx = 0;
		int yoffset = 0;
		int height = 0;
		for (; idx < count; ++idx) {
			height = adapter != null ? adapter.getItemCommonHeight(idx) : 0;
			yoffsets[idx] = yoffset;
			yoffset += height;
		}
		yoffsets[idx] = yoffset;

		// set preferred size
		setPreferredSizeInternal(width, yoffset + mgTop + mgBottom);
	}

	/** {@inheritDoc} */
	public void setScrollbarClickableLength(int aSbClickableLen) {

		sbClickableLen = aSbClickableLen;
	}

	/** {@inheritDoc} */
	public int getScrollbarClickableLength() {

		return sbClickableLen;
	}

	public void scrollItemToVisible(int aIdx) {

		this.scrollItemToVisible(aIdx, true);
	}

	public void scrollItemToTopVisible(int aIdx) {

		this.scrollItemToTopVisible(aIdx, true);
	}

	private void scrollItemToVisible(int aIdx, boolean aDelay) {

		if (isItemIndexValid(aIdx)) {

			int y = aIdx > 0 ? getItemTopYOffset(aIdx) : 0;
			int h = getItemHeight(aIdx) + (aIdx == count - 1 ? mgBottom : 0);
			this.scrollRectToVisible(0, y, width, h, this);
			if (aDelay) {
				delayVisibleScroll(aIdx, SCROLL_ITEM_TO_VISIBLE);
			}
		}
	}

	private void scrollItemToTopVisible(int aIdx, boolean aDelay) {

		if (isItemIndexValid(aIdx)) {

			int y = aIdx > 0 ? getItemTopYOffset(aIdx) : 0;
			int h = y < getScrollYLimit() ? height : (getPreferredHeight() - y);
			this.scrollRectToVisible(0, y, width, h, this);
			if (aDelay) {
				delayVisibleScroll(aIdx, SCROLL_ITEM_TO_TOP_VISIBLE);
			}
		}
	}

	private void delayVisibleScroll(int aIdx, int aMethod) {

		if (gScrollItemToVisibleTimer < 0) {
			gScrollItemToVisibleTimer = CanvasEx.findAvailableTimer();
		}

		gScrollItemToVisibleIdx = aIdx;
		gScrollItemToVisibleMethod = aMethod;

		CanvasEx.startTimer(gScrollItemToVisibleTimer, this,
			ITEM_VISIBLE_SCROLL_DELAY);
	}

	protected void paintContent(GraphicsEx aG) {

		if (adapter == null || count == 0)
			return;

		int clipY = aG.getClipY();
		int clipHeight = aG.getClipHeight();

		int beginY = NumberUtils.max(getScrollY(), clipY, 0);
		int endY = NumberUtils.min(getScrollY() + height, clipY + clipHeight,
			getPreferredHeight());

		int begin = getRoundItemIndex(beginY);
		int end = getRoundItemIndex(endY);

		// Log.d("LIST", aG.getClipRect());
		// System.out.println("TX : " + aG.getTranslateX() + ", TY : " +
		// aG.getTranslateY() + ", beginY : " + beginY + ", endY : " + endY +
		// ", begin : " + begin + ", end : " + end);

		int colorNormal = Color.SNOW;
		int colorSelected = Color.GOLD;

		aG.save(GraphicsEx.SAVE_TRANSLATION | GraphicsEx.SAVE_COLOR);
		aG.translate(x + mgLeft, y + getItemTopYOffset(begin));

		int itemWidth = width - mgLeft - mgRight;
		int itemHeight = 0;
		int itemNewHeight = 0;
		for (int i = begin; i <= end; ++i) {

			aG.setColor(i != selected ? colorNormal : colorSelected);
			// item's height maybe changed during its painting
			itemHeight = getItemHeight(i);
			itemNewHeight = adapter.paintItem(aG, i, itemHeight, itemWidth);
			aG.translate(0, itemNewHeight);

			// if item's height is changed, need to update and repaint
			if (itemHeight != itemNewHeight) {
				updateItemHeight(i, itemNewHeight);
			}
		}
		aG.restore();
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		if (hasFocus()) {
			aG.setBrush(Brush.createColorBrush(Color.DARKBLUE));
		} else {
			aG.setBrush(Brush.createColorBrush(Color.DARKGOLDENROD));
		}
		aG.fillRectEx(x, y, width, height);
	}
}
