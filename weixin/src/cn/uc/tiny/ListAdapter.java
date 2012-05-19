/**
 * Tiny.cn.uc.ui.ListAdaptor.java, 2011-1-6
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.GraphicsEx;

/**
 * Item data, paint and event handle adapter for {@link ListView}.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public interface ListAdapter extends EventHandler {

	public String getClazz();

	public void setListView(ListView aListView);

	public int getCount();

	public int getItemCommonHeight();

	public Object getItem(int aIdx);

	public String getItemText(int aIdx);

	public int paintItem(GraphicsEx aG, int aIdx, int aItemCurrHeight,
		int aItemCurrWidth);

	public void onItemClicked(int aIdx, Event aCauseEv, int aItemX, int aItemY);

	public void onItemLongClicked(int aIdx, Event aCauseEv, int aItemX,
		int aItemY);

	public void onItemSelected(int aIdx);

	public void onItemUnSelected(int aIdx);
}
