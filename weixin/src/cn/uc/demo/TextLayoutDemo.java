/**
 * Tiny.cn.uc.demo.TextLayoutDemo.java, 2011-4-13
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.demo;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import cn.uc.T;
import cn.uc.demo.components.SimpleView;
import cn.uc.demo.components.SimpleWindow;
import cn.uc.demo.components.TextFragment;
import cn.uc.tiny.Component;
import cn.uc.tiny.Menu;
import cn.uc.tiny.MenuSource;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.TextLayout;
import cn.uc.tiny.ex.TextLayout.TextOption;
import cn.uc.util.debug.Log;

/**
 * TextLayout Demo.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class TextLayoutDemo extends MIDlet implements EventHandler, MenuSource {

	private final String P1 = "我是一段话，按如下方式显示：按词断行，使用默认字体高度，行高1em，左对齐显示。"
		+ "I am paragraph, display with: wrap by word, use default font height,"
		+ " use 1em line height, and text align left.";

	private final int P1_OPTIONS = TextOption.WORD_WRAP
		| TextOption.USE_FONT_DEFAULT_HEIGHT
		| TextOption.TAB_STOP_AT_FOUR_SPACES | TextOption.LINE_HEIGHT_1_EM
		| TextOption.ALIGN_LEFT | TextOption.END_ELLIPSIS
		| TextOption.BEGIN_ELLIPSIS | TextOption.LINE_CLIP;

	private final String P2 = "我是一段话，按如下方式显示：任意断行，使用精确字体高度，行高1.5em，左对齐显示。"
		+ "I am paragraph, display with: wrap anywhere, use exact font height,"
		+ " use 1.5em line height, and text align left.";

	private final int P2_OPTIONS = TextOption.WRAP_ANYWHERE
		| TextOption.USE_FONT_EXACT_HEIGHT | TextOption.TAB_STOP_AT_FOUR_SPACES
		| TextOption.LINE_HEIGHT_1_5_EM | TextOption.ALIGN_LEFT
		| TextOption.END_ELLIPSIS | TextOption.BEGIN_ELLIPSIS
		| TextOption.LINE_CLIP;

	private final String P3 = "我是一段话，按如下方式显示：尽量按词断行，使用精确字体高度，行高2em，右对齐显示。"
		+ "I am paragraph, display with: try to wrap by word, use exact font height,"
		+ " use 2em line height, and text align right.";

	private final int P3_OPTIONS = TextOption.WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE
		| TextOption.USE_FONT_EXACT_HEIGHT
		| TextOption.TAB_STOP_AT_FOUR_SPACES
		| TextOption.LINE_HEIGHT_2_EM
		| TextOption.ALIGN_RIGHT
		| TextOption.END_ELLIPSIS
		| TextOption.BEGIN_ELLIPSIS
		| TextOption.LINE_CLIP;

	private final String P4 = "我是一段话，按如下方式显示：尽量按词断行，使用默认字体高度，行高1.5em，居中对齐显示。"
		+ "I am paragraph, display with: try to wrap by word, use exact font height,"
		+ " use 1.5em line height, and text align center.";

	private final int P4_OPTIONS = TextOption.WARP_AT_WORDS_BOUNDARY_OR_ANYWHERE
		| TextOption.USE_FONT_DEFAULT_HEIGHT
		| TextOption.TAB_STOP_AT_FOUR_SPACES
		| TextOption.LINE_HEIGHT_1_5_EM
		| TextOption.ALIGN_CENTER
		| TextOption.END_ELLIPSIS
		| TextOption.BEGIN_ELLIPSIS
		| TextOption.LINE_CLIP;
	
	private static final int H_SPACING = 5;
	private static final int V_SPACING = 10;

	private static final int CMD_ID_SHOW_BOUNDS = 1000;
	private static final int CMD_ID_HIDE_BOUNDS = 1001;

	SimpleView view;

	private boolean startup = false;

	private int viewcolor = Color.ALICEBLUE;
	private int viewalpha = Color.OPAQUE;

//	private int color = Color.SNOW;
//	private int bgcolor = Color.GRAY;
//	private int alpha = Color.HALF_TRANSPARENT;

	boolean showBounds = true;

	/**
	 * 
	 */
	public TextLayoutDemo() {

	}

	/** {@inheritDoc} */
	protected void destroyApp(boolean aArg0) throws MIDletStateChangeException {
	}

	/** {@inheritDoc} */
	protected void pauseApp() {
	}

	/** {@inheritDoc} */
	protected void startApp() throws MIDletStateChangeException {

		if (startup) {
			CanvasEx.restoreCanvas();
			return;
		}

		startup = true;

		Log.addTagFilter(CanvasEx.P_TAG);
		Log.addTagFilter(CanvasEx.E_TAG);
		Log.addTagFilter(CanvasEx.T_TAG);
		Log.addTagFilter(Component.P_TAG);
		Log.addTagFilter(Component.S_TAG);
		Log.addTagFilter(GraphicsEx.TAG);
		Log.addTagFilter(Animation.TAG);
		Log.addTagFilter(Motion.TAG);

		CanvasEx.initCanvas(this);
		FontEx.initializeUsedFonts(FontEx.SUPPORT_ALL_FONTS);
		FontEx.setDefaultFont(FontEx.getFont(FontEx.STYLE_PLAIN,
			FontEx.SIZE_MEDIUM));

		view = new SimpleView();
		view.setBackground(Brush.createColorBrush(viewalpha, viewcolor));

		SimpleWindow win = new SimpleWindow();
		win.setMenuSource(this);
		win.setTitle("Text");
		win.addView(view);
		win.show();

		layoutText();
		return;
	}

	private void layoutText() {

		// view.setPreferredSize(view.getWidth(), 1024);
		view.removeAll();

		int yoffset = V_SPACING;
		for (int i = 0; i < FontEx.FONT_ID_TABLES.length; ++i) {

			int options = P1_OPTIONS;
			if (showBounds) {
				options |= TextOption.DRAW_LINE_BOUNDS;
				options |= TextOption.DRAW_LAYOUT_BOUNDS;
			}
			TextLayout layout = new TextLayout(FontEx.getFont(i), P1);
			layout.setTextOptions(options);
			layout.layout(view.getWidth() - 2 * H_SPACING, view.getHeight());

			TextFragment tfrag = new TextFragment();
			view.addComponent(tfrag);

			tfrag.setPosition(H_SPACING, yoffset);
			tfrag.setTextLayout(layout);
			tfrag.layout();

			yoffset += (tfrag.getHeight() + V_SPACING);

			options = P2_OPTIONS;
			if (showBounds) {
				options |= TextOption.DRAW_LINE_BOUNDS;
				options |= TextOption.DRAW_LAYOUT_BOUNDS;
			}

			layout = new TextLayout(FontEx.getFont(i), P2);
			layout.setTextOptions(options);
			layout.layout(view.getWidth() - 2 * H_SPACING, view.getHeight());

			tfrag = new TextFragment();
			view.addComponent(tfrag);

			tfrag.setPosition(H_SPACING, yoffset);
			tfrag.setTextLayout(layout);
			tfrag.layout();

			yoffset += (tfrag.getHeight() + V_SPACING);
			
			options = P3_OPTIONS;
			if (showBounds) {
				options |= TextOption.DRAW_LINE_BOUNDS;
				options |= TextOption.DRAW_LAYOUT_BOUNDS;
			}

			layout = new TextLayout(FontEx.getFont(i), P3);
			layout.setTextOptions(options);
			layout.layout(view.getWidth() - 2 * H_SPACING, view.getHeight());

			tfrag = new TextFragment();
			view.addComponent(tfrag);

			tfrag.setPosition(H_SPACING, yoffset);
			tfrag.setTextLayout(layout);
			tfrag.layout();

			yoffset += (tfrag.getHeight() + V_SPACING);
			
			options = P4_OPTIONS;
			if (showBounds) {
				options |= TextOption.DRAW_LINE_BOUNDS;
				options |= TextOption.DRAW_LAYOUT_BOUNDS;
			}

			layout = new TextLayout(FontEx.getFont(i), P4);
			layout.setTextOptions(options);
			layout.layout(view.getWidth() - 2 * H_SPACING, view.getHeight());

			tfrag = new TextFragment();
			view.addComponent(tfrag);

			tfrag.setPosition(H_SPACING, yoffset);
			tfrag.setTextLayout(layout);
			tfrag.layout();

			yoffset += (tfrag.getHeight() + V_SPACING);
		}
	}

	/** {@inheritDoc} */
	public Menu getWindowMenu() {
		
		CommandEx[] commands = new CommandEx[] {
			CommandEx.create(CMD_ID_SHOW_BOUNDS, "显示边界"),
			CommandEx.create(CMD_ID_HIDE_BOUNDS, "不显示边界"), };

		Menu menu = Menu.buildWindowMenu("DemoMenu", CommandEx.CMD_GROUP_MENU,
			CommandEx.create(T.MENU_EXIT), commands);
		menu.setOwner(this);

		return menu;
	}

	/** {@inheritDoc} */
	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {
		return null;
	}

	/** {@inheritDoc} */
	public Menu getSubMenu(CommandEx aGroup) {
		return null;
	}

	/** {@inheritDoc} */
	public boolean event(Event aEv) {
		
		if (aEv.isCommandAction()) {
			switch (aEv.getCommandId()) {

			case T.MENU_EXIT:
				CanvasEx.exit();
				break;

			case CMD_ID_SHOW_BOUNDS:
				showBounds = true;
				break;

			case CMD_ID_HIDE_BOUNDS:
				showBounds = false;
				break;
			}

			layoutText();
			CanvasEx.repaintCanvas();
			return true;
		}

		return false;
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {
	}

	/** {@inheritDoc} */
	public void pointerEvent(Event aPtEv) {
	}

	/** {@inheritDoc} */
	public void actionEvent(Event aActEv) {
	}

	/** {@inheritDoc} */
	public void timerEvent(Event aTimerEv) {
	}

	/** {@inheritDoc} */
	public void progressEvent(Event aProgressEv) {
	}

	/** {@inheritDoc} */
	public void errorEvent(Event aErrorEv) {
	}

	/** {@inheritDoc} */
	public void sizeChanged(int aNewWidth, int aNewHeight) {
	}

	/** {@inheritDoc} */
	public void orentationChanged(int aNewOrentation) {
	}

	/** {@inheritDoc} */
	public void showNotify() {
	}

	/** {@inheritDoc} */
	public void hideNotify() {
	}

	/** {@inheritDoc} */
	public void focusGained(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void focusLost(Event aCauseEv) {
	}

	/** {@inheritDoc} */
	public void onEventError(Throwable aErr) {
	}

}
