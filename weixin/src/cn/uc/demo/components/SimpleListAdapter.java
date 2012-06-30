/**
 * Tiny.cn.uc.demo.DemoListAdapter.java, 2011-1-6
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo.components;

import cn.uc.tiny.ListAdapter;
import cn.uc.tiny.ListView;
import cn.uc.tiny.Popup;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;

/**
 * A Simple List Adapter.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class SimpleListAdapter extends BasicEventHandler implements
	ListAdapter {

	public static final String[] LIST = new String[] {

	"AbsListView.LayoutParams", "AbsoluteLayout",
		"AbsoluteLayout.LayoutParams", "AbsSeekBar", "AbsSpinner",
		"AdapterView<T extends Adapter>", "AdapterView.AdapterContextMenuInfo",
		"AlphabetIndexer", "AnalogClock", "AbsListView.LayoutParams",
		"AbsoluteLayout", "AbsoluteLayout.LayoutParams", "AbsSeekBar",
		"AbsSpinner", "AdapterView<T extends Adapter>",
		"AdapterView.AdapterContextMenuInfo", "AlphabetIndexer", "AnalogClock",
		"AbsListView.LayoutParams", "AbsoluteLayout",
		"AbsoluteLayout.LayoutParams", "AbsSeekBar", "AbsSpinner",
		"AdapterView<T extends Adapter>", "AdapterView.AdapterContextMenuInfo",
		"AlphabetIndexer", "AnalogClock", "AbsListView.LayoutParams",
		"AbsoluteLayout", "AbsoluteLayout.LayoutParams", "AbsSeekBar",
		"AbsSpinner", "AdapterView<T extends Adapter>",
		"AdapterView.AdapterContextMenuInfo", "AlphabetIndexer", "AnalogClock", };

	private ListView view;

	/** {@inheritDoc} */
	public String getClazz() {

		return "SimpleList";
	}

	/** {@inheritDoc} */
	public void setListView(ListView aListView) {

		view = aListView;
		view.setScrollbarClickableLength(10);
	}

	/** {@inheritDoc} */
	public int getCount() {

		return LIST.length;
	}

	/** {@inheritDoc} */
	public int getItemCommonHeight(int aIdx) {

		return FontEx.getDefaultFont().getHeight() + 10;
	}

	/** {@inheritDoc} */
	public Object getItem(int aIdx) {

		return LIST[aIdx];
	}

	/** {@inheritDoc} */
	public String getItemText(int aIdx) {

		return LIST[aIdx];
	}

	/** {@inheritDoc} */
	public int paintItem(GraphicsEx aG, int aIdx, int aItemCurrHeight,
		int aItemCurrWidth) {

		// aG.drawLine(0, 0, aItemWidth, 0);
		aG.drawString(LIST[aIdx], 0, 0, GraphicsEx.LEFT | GraphicsEx.TOP);
		// aG.drawLine(0, this.getItemHeight() * 2 - 1, aItemWidth,
		// this.getItemHeight() * 2 - 1);

		return getItemCommonHeight(aIdx) * 2;
	}

	/** {@inheritDoc} */
	public void onItemClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY) {

		Popup.gToastStartAnchor = GraphicsEx.TOP | GraphicsEx.OUT_BOX;
		Popup.gToastEndAnchor = GraphicsEx.VCENTER | GraphicsEx.RIGHT
			| GraphicsEx.IN_BOX;
		Popup.buildToast(
			"ListView click : " + aIdx + ", " + LIST[aIdx] + ", at " + aItemX
				+ ", " + aItemY).show();
	}

	/** {@inheritDoc} */
	public void onItemLongClicked(int aIdx, Event aCauseEv, int aItemX,
		int aItemY) {

		Popup.gToastStartAnchor = GraphicsEx.TOP | GraphicsEx.OUT_BOX;
		Popup.gToastEndAnchor = GraphicsEx.VCENTER | GraphicsEx.LEFT
			| GraphicsEx.IN_BOX;
		Popup.buildToast(
			"ListView long click : " + aIdx + ", " + LIST[aIdx] + ", at "
				+ aItemX + ", " + aItemY).show();
	}

	/** {@inheritDoc} */
	public void onItemSelected(int aIdx) {

		Popup.gToastStartAnchor = GraphicsEx.TOP | GraphicsEx.OUT_BOX;
		Popup.gToastEndAnchor = GraphicsEx.VCENTER | GraphicsEx.LEFT
			| GraphicsEx.IN_BOX;
		// Popup.buildToast("ListView select : " + aIdx + ", " +
		// LIST[aIdx]).show();
	}

	/** {@inheritDoc} */
	public void onItemUnSelected(int aIdx) {

		Popup.gToastStartAnchor = GraphicsEx.TOP | GraphicsEx.OUT_BOX;
		Popup.gToastEndAnchor = GraphicsEx.VCENTER | GraphicsEx.RIGHT
			| GraphicsEx.IN_BOX;
		// Popup.buildToast("ListView unselect : " + aIdx).show();
	}

	public String toString() {

		return "DemoListAdapter";
	}
}
