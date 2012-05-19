/**
 * Tiny.cn.uc.ui.Theme.java, 2011-1-7
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.style;

import cn.uc.tiny.ex.Brush;

/**
 * 
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Theme {

	public static final Brush[] BACKGROUNDS = new Brush[BackgroundId.TOTAL];
	public static final int[] FOREGROUNDS = new int[ForegroundId.TOTAL];

	public static final class BackgroundId {

		public static final int UNDEFINED = 0;

		public static final int WINDOW = 1;
		public static final int SPLASH = 2;

		public static final int TITLE_BAR = 3;
		public static final int TITLE_BAR_FOCUSED = 4;
		public static final int TITLE_BAR_TAB_ACTIVE = 5;
		public static final int TITLE_BAR_TAB_INACTIVE = 6;

		public static final int MENU_BAR = 7;
		public static final int MENU_BAR_COMMAND_NORMAL = 8;
		public static final int MENU_BAR_COMMAND_PRESSED = 9;

		public static final int PROGRESS_BAR = 10;
		public static final int PROGRESS_BAR_PROGRESS = 11;

		public static final int SCROLL_BAR_VERTICAL = 12;
		public static final int SCROLL_BAR_HORIZONTAL = 13;

		public static final int MENU_ACTIVE = 14;
		public static final int MENU_INACTIVE = 15;
		public static final int MENU_ITEM_SELECTED = 16;
		public static final int MENU_ITEM_UNSELECTED = 17;

		public static final int TOAST = 18;

		public static final int DIALOG = 19;
		public static final int DIALOG_TITLE_BAR = 20;

		public static final int LIST_VIEW = 21;
		public static final int LIST_VIEW_ODD_ITEM = 22;
		public static final int LIST_VIEW_EVEN_ITEM = 23;
		public static final int LIST_VIEW_ITEM_SELECTED = 24;

		public static final int TOTAL = 25;
	}

	public static final class ForegroundId {

		public static final int UNDEFINED = 0;

		public static final int NORMAL_TEXT = 1;
		public static final int SELECTED_TEXT = 2;
		public static final int SELECTED_ITEM_TEXT = 3;

		public static final int SPLASH_TEXT = 4;

		public static final int TOTAL = 5;
	}
}
