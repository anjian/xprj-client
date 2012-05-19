/**
 * Tiny.cn.uc.ui.Window.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import java.util.Vector;

import javax.microedition.lcdui.Image;

import cn.uc.tiny.Menu.MenuType;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.BitUtils;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class Window extends Component {

	public static final String TAG = "Win";
	public static final boolean DEBUG = Component.DEBUG;

	public static final int SHOW_UPON_PREVIOUS = 0;
	public static final int SHOW_AND_CLOSE_PREVIOUS = 1;

	public static final int HIDE_MENUBAR = 1;
	public static final int MINIMIZE_TITLEBAR = 2;
	public static final int HIDE_TITLEBAR = 4;

	public static final int NORMAL_SCREEN = 0;
	public static final int FULL_SCREEN = MINIMIZE_TITLEBAR | HIDE_MENUBAR;

	private static final int MINIMIZE_TITLEBAR_HEIGHT = 8;

	private static final boolean SWITCH_NEXT = true;
	private static final boolean SWITCH_PREVIOUS = false;

	private static final String VIEW_PANE = "ViewPane";
	private static final String MODAL_LAYER = "ModalLayer";
	private static final String MENU_LAYER = "MenuLayer";
	private static final String FLOAT_LAYER = "FloatLayer";

	// splash relative
	private static int gProgressSegments = 5;
	private static int gProgress = 0;
	private static String gProgressMessage = StringUtils.EMPTY;

	private static int gShowWindowHint = SHOW_UPON_PREVIOUS;

	private final TitleBar titleBar;
	private final MenuBar menuBar;
	private MenuSource menuSrc;

	private final Component viewPane;
	private final Component modalLayer;
	private final Component menuLayer;
	private final Component floatLayer;

	private Component currView;
	private Component dragged;
	private Component pressed;
	private Component focused;
	private Component oldFocused;

	/**
	 * Contains a list of components that would like to animate their state
	 */
	private final Vector animatables = new Vector();

	private int screen = NORMAL_SCREEN;

	private Rectangle centralBounds = Rectangle.EMPTY;
	private Rectangle viewBounds = Rectangle.EMPTY;
	private Rectangle popupBounds = Rectangle.EMPTY;

	private Image[] logos;

	/**
	 * Create a splash window for startup display.
	 * 
	 * @return splash window
	 */
	public static Window createSplash(Image aStartupLogo, Image aVersionLogo,
		Image aProgressOnIcon, Image aProgressOffIcon) {

		Window splash = new Window("Splash", new TitleBar(), new MenuBar());

		splash.setAttr(Attr.SPLASH, true);
		splash.logos = new Image[] { aStartupLogo, aVersionLogo,
			aProgressOnIcon, aProgressOffIcon };

		splash.titleBar.setAttr(Attr.HAS_BACKGROUND, false);
		splash.menuBar.setAttr(Attr.HAS_BACKGROUND, false);

		return splash;
	}

	private static Component createPopupLayer(String aId) {

		Component c = new Component();

		c.setId(aId);
		c.setAttr(Attr.HAS_BACKGROUND, false);
		c.setAttr(Attr.OPAQUE, false);
		c.setAttr(Attr.CHILDREN_OVERLAP, true);
		c.setAttr(Attr.POPUP_LAYER, true);

		if (aId == FLOAT_LAYER) {
			c.setAttr(Attr.TRANSPARENT_FOR_POINTER_EVENT, true);
		}

		return c;
	}

	public Window(String aId, TitleBar aTitleBar, MenuBar aMenuBar) {

		setId(aId);
		setState(State.VISIBLE, false);

		titleBar = aTitleBar;
		menuBar = aMenuBar;

		viewPane = new Component();
		viewPane.setId(VIEW_PANE);
		viewPane.setAttr(Attr.HAS_BACKGROUND, false);
		viewPane.setAttr(Attr.OPAQUE, false);

		modalLayer = createPopupLayer(MODAL_LAYER);
		menuLayer = createPopupLayer(MENU_LAYER);
		floatLayer = createPopupLayer(FLOAT_LAYER);

		this.addComponent(titleBar);
		this.addComponent(menuBar);
		this.addComponent(viewPane);
		this.addComponent(modalLayer);
		this.addComponent(menuLayer);
		this.addComponent(floatLayer);

		// layout
		layout();
	}

	/** {@inheritDoc} */
	public String getClazz() {

		return hasAttr(Attr.SPLASH) ? "Splash" : "Window";
	}

	/**
	 * {@inheritDoc}
	 */
	public final Window getWindow() {

		return this;
	}

	/** {@inheritDoc} */
	protected void initializeComponent() {

		// make visible and initialize focused
		setVisible(true);
		initFocused();
		// reset window menu when is not splash
		if (menuBar.getMenu() == null) {
			resetWindowMenu();
		}
	}

	/** {@inheritDoc} */
	protected void deinitializeComponent() {

		setVisible(false);

		if (hasAttr(Attr.SPLASH)) {

			Log.d(TAG, "Clear data of splash.");

			gProgressMessage = StringUtils.EMPTY;
			gProgress = 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isChildrenOverlap() {

		return modalLayer.isVisible() || menuLayer.isVisible()
			|| floatLayer.isVisible();
	}

	/** {@inheritDoc} */
	public void setTitle(String aTitle) {

		titleBar.setTitle(aTitle);
	}

	/** {@inheritDoc} */
	public String getTitle() {

		return titleBar.getTitle();
	}

	public final void show() {

		this.show(SHOW_UPON_PREVIOUS);
	}

	public final void show(int aShowHint) {

		gShowWindowHint = aShowHint;

		Window oldWin = CanvasEx.getCurrWindow();

		// make the switch from old to new
		if (oldWin != null) {

			// create a special container to contains two windows to show
			// windows transition animation
			Component winContainer = new Component();
			winContainer.initialize();
			winContainer.setBounds(CanvasEx.getDisplayBounds());

			winContainer.addComponent(oldWin);
			winContainer.addComponent(this);

			int disHeight = CanvasEx.getDisplayHeight();
			winContainer.replaceWithTransition(oldWin, this, 0, -disHeight, 0,
				disHeight, 0, 0);
		}

		switchWindow(oldWin, this, gShowWindowHint == SHOW_AND_CLOSE_PREVIOUS);
	}

	public final void close() {

		Window prevWin = CanvasEx.getPrevWindow();

		// make the switch from current to previous
		if (prevWin != null) {

			// create a special container to contains two windows to show
			// windows transition animation
			Component winContainer = new Component();
			winContainer.initialize();
			winContainer.setBounds(CanvasEx.getDisplayBounds());

			winContainer.addComponent(prevWin);
			winContainer.addComponent(this);

			int disHeight = CanvasEx.getDisplayHeight();
			winContainer.replaceWithTransition(this, prevWin, 0, disHeight, 0,
				-disHeight, 0, 0);

			switchWindow(this, prevWin, true);
		} else {
			closeImpl();
		}
	}

	void switchWindow(Window aOldWin, Window aNewWin, boolean aCloseOld) {

		// remove the special windows container when transition animation over
		if (getParent() != null) {

			Component winContainer = getParent();
			winContainer.setVisible(false);
			winContainer.removeAll();
		}

		if (CanvasEx.compareOrder(aNewWin, aOldWin) > 0) {
			// show new then close old
			if (aNewWin != null) {
				aNewWin.showImpl();
			}

			if (aOldWin != null && aCloseOld) {
				aOldWin.closeImpl();
			}
		} else {
			// close old then show new
			if (aOldWin != null && aCloseOld) {
				aOldWin.closeImpl();
			}

			if (aNewWin != null) {
				aNewWin.showImpl();
			}
		}
	}

	private final void showImpl() {

		Log.d(TAG, "Show window with hint : ", Log.toString(gShowWindowHint),
			", ", this);
		initialize();
		CanvasEx.showWindow(this);
	}

	private final void closeImpl() {

		Log.d(TAG, "Close window : ", this);
		deinitialize();
		CanvasEx.closeWindow(this);
	}

	public Brush getBackground(Component aCmp, int aBackgroundId) {

		return Brush.WHITE_BRUSH;
	}

	public int getForeground(Component aCmp, int aForegroundId) {

		return Color.BLACK;
	}

	public FontEx getFont(Component aCmp) {

		return FontEx.getDefaultFont();
	}

	public Menu getWindowMenu() {

		Menu menu = null;

		if (hasModal()) {
			menu = getTopModal().getWindowMenu();
		}

		if (menu == null && currView != null) {
			menu = currView.getWindowMenu();
		}

		if (menu == null && menuSrc != null) {
			menu = menuSrc.getWindowMenu();
		}

		return menu;
	}

	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {

		if (menuSrc != null) {
			return menuSrc.getContextMenu(aContextCmp, aX, aY);
		} else {
			return null;
		}
	}

	public Menu getSubMenu(CommandEx aGroup) {

		if (menuSrc != null) {
			return menuSrc.getSubMenu(aGroup);
		} else {
			return null;
		}
	}

	public void layout() {

		Rectangle bounds = CanvasEx.getDisplayBounds();
		setBounds(bounds);

		FontEx font = this.getFont();

		int tbHeight = BitUtils.and(screen, MINIMIZE_TITLEBAR) ? MINIMIZE_TITLEBAR_HEIGHT
			: BitUtils.and(screen, HIDE_TITLEBAR) ? 0 : font.getHeight();
		int mbHeight = BitUtils.and(screen, HIDE_MENUBAR) ? 0
			: font.getHeight();

		// titlebar and menubar
		titleBar.setBounds(0, 0, bounds.width, tbHeight);
		menuBar.setBounds(0, bounds.height - mbHeight, bounds.width, mbHeight);

		// view pane
		centralBounds = new Rectangle(0, tbHeight, bounds.width, bounds.height
			- tbHeight - mbHeight);

		viewPane.setBounds(centralBounds);
		viewBounds = new Rectangle(0, 0, bounds.width, centralBounds.height);

		for (int i = getViewCount(); i-- != 0;) {
			getView(i).setBounds(viewBounds);
		}

		// relayout all views
		viewPane.layout();

		// popup layers
		popupBounds = new Rectangle(0, 0, bounds.width, bounds.height
			- mbHeight);

		modalLayer.setBounds(popupBounds);
		menuLayer.setBounds(popupBounds);
		floatLayer.setBounds(popupBounds);
	}

	public final void switchFullScreen() {

		screen = isFullScreen() ? NORMAL_SCREEN : FULL_SCREEN;
		layout();
	}

	public final void setFullScreen(int aScreenHint) {

		screen = aScreenHint;
		layout();
	}

	public final int getFullScreen() {

		return screen;
	}

	public final boolean isFullScreen() {

		return screen == FULL_SCREEN;
	}

	public final Rectangle getCentralArea() {

		return centralBounds;
	}

	public final Rectangle getPopupArea() {

		return popupBounds;
	}

	public final boolean isCurrentView(Component aView) {

		return currView == aView;
	}

	public final Component getCurrentView() {

		return currView;
	}

	public final int getCurrentViewIndex() {

		return getViewIndex(currView);
	}

	public final int getViewCount() {

		return viewPane.getComponentCount();
	}

	public final int getViewIndex(Component aView) {

		return viewPane.getComponentIndex(aView);
	}

	public final Component getView(int aViewIdx) {

		if (isViewIndexValid(aViewIdx)) {
			return viewPane.getComponentAt(aViewIdx);
		}

		return currView;
	}

	public final boolean isViewIndexValid(int aViewIdx) {

		return viewPane.isComponentIndexValid(aViewIdx);
	}

	public final void switchNextView(boolean aCircle) {

		switchNextOrPreviousView(SWITCH_NEXT, aCircle);
	}

	public final void switchPreviousView(boolean aCircle) {

		switchNextOrPreviousView(SWITCH_PREVIOUS, aCircle);
	}

	public final void switchToView(int aViewIdx, boolean aNext) {

		if (!isViewIndexValid(aViewIdx)) {
			return;
		}

		Component switchView = viewPane.getComponentAt(aViewIdx);
		if (currView != switchView) {
			this.switchView(currView, switchView, aNext);
		}
	}

	public final void addView(Component aView) {

		this.addView(getViewCount(), aView);
	}

	public final void addView(int aViewIdx, Component aView) {

		// add
		viewPane.addComponent(aViewIdx, aView);

		// set view's bounds and layout it
		aView.setBounds(viewBounds);
		aView.layout();
		aView.setVisible(false);

		// no visible current view?
		if (currView == null) {
			this.switchView(null, aView, SWITCH_NEXT);
		}

		titleBar.repaint(Reason.UPDATE);
	}

	public final void closeView(Component aView) {

		int idx = getViewIndex(aView);

		// remove
		viewPane.removeComponent(aView);

		// need switch current?
		if (currView == aView) {

			if (getViewCount() > 0) {

				switchToView(idx, SWITCH_NEXT);
			} else {

				this.switchView(currView, null, SWITCH_NEXT);
			}

			return;
		}

		titleBar.repaint(Reason.UPDATE);
		viewPane.repaint(Reason.CLOSE_VIEW);
	}

	public final void closeAllViews() {

		for (int i = viewPane.getComponentCount(); i-- > 0;) {

			closeView(viewPane.getComponentAt(i));
		}
	}

	private final void switchNextOrPreviousView(boolean aNext, boolean aCircle) {

		int idx = aNext ? getCurrentViewIndex() + 1 : getCurrentViewIndex() - 1;

		if (isViewIndexValid(idx)) {

			switchToView(idx, aNext);
		} else if (aCircle) {

			idx = aNext ? 0 : getViewCount() - 1;
			switchToView(idx, aNext);
		}
	}

	private final void switchView(Component aOldView, Component aNewView,
		boolean aNext) {

		if (aOldView != null && aNewView != null && isVisible()) {

			int aOutDstX = aNext ? -aOldView.width : aOldView.width;
			int aInSrcX = aNext ? aNewView.width : -aNewView.width;

			viewPane.replaceWithTransition(aOldView, aNewView, aOutDstX, 0,
				aInSrcX, 0, 0, 0);
		}

		this.switchView(aOldView, aNewView);
	}

	final void switchView(Component aOldView, Component aNewView) {

		if (aOldView != null) {

			aOldView.setVisible(false);
		}

		currView = aNewView;// switch

		if (aNewView != null) {

			currView.setVisible(true);
		}

		if (isVisible()) {

			titleBar.repaint(Reason.UPDATE);
			viewPane.repaint(Reason.SWITCH_VIEW);

			resetWindowMenu();
		}
	}

	public boolean isModalPopup(Component aCmp) {

		return modalLayer.contains(aCmp) || menuLayer.contains(aCmp);
	}

	void appendPopup(Popup aPopup) {

		Assert.assertNotNull(aPopup);

		if (aPopup.hasAttr(Attr.MODAL)) {
			appendModal(aPopup);
		} else if (aPopup.hasAttr(Attr.MENU) && aPopup instanceof Menu) {
			appendMenu((Menu) aPopup);
		} else if (aPopup.hasAttr(Attr.FLOAT)) {
			appendFloat(aPopup);
		}
	}

	void removePopup(Popup aPopup) {

		if (aPopup == null) {
			return;
		}

		if (aPopup.hasAttr(Attr.MODAL)) {
			removeModal(aPopup);
		} else if (aPopup.hasAttr(Attr.MENU) && aPopup instanceof Menu) {
			removeMenu((Menu) aPopup);
		} else if (aPopup.hasAttr(Attr.FLOAT)) {
			removeFloat(aPopup);
		}
	}

	void appendModal(Popup aModal) {

		Assert.assertNotNull(aModal);

		if (!modalLayer.contains(aModal)) {

			modalLayer.addComponent(aModal);
			this.setCurrentFocused(aModal.findFirstFocusable(), true);
			closeMenus();
			resetWindowMenu();
		}
	}

	void removeModal(Popup aModal) {

		Assert.assertNotNull(aModal);

		if (modalLayer.contains(aModal)) {

			modalLayer.removeComponent(aModal);
			initFocused();
			resetWindowMenu();
		}
	}

	public void closeModals() {

		closePopups(modalLayer, null, true);
	}

	public void popoutModals() {

		closePopups(modalLayer, null, false);
	}

	protected Popup getTopModal() {

		return (Popup) modalLayer.getLastComponent();
	}

	protected boolean hasModal() {

		return modalLayer.getComponentCount() > 0;
	}

	public void setMenuSource(MenuSource aMenuSource) {

		menuSrc = aMenuSource;
	}

	public void setSplashMenu(Menu aWinMenu) {

		Assert.assertNotNull(aWinMenu);
		Assert.assertEquals(aWinMenu.type, MenuType.WINDOW_MENU, Assert.ARG);

		menuBar.setMenu(aWinMenu);
	}

	public void resetWindowMenu() {

		menuBar.setMenu(getWindowMenu());
	}

	public void showContextMenu(Menu aContextMenu) {

		Assert.assertNotNull(aContextMenu);
		Assert.assertTrue(aContextMenu.hasAttr(Attr.POPABLE), Assert.ARG);
		Assert.assertEquals(aContextMenu.type, MenuType.CONTEXT_MENU,
			Assert.ARG);

		aContextMenu.show();
		menuBar.setMenu(aContextMenu);
	}

	public void showShortcutMenu(int a1stKey) {

		int[] shortcuts = CommandEx.getShortcutsBy1stKey(a1stKey);
		CommandEx[] scCmds = new CommandEx[shortcuts.length];

		for (int i = 0, scIdx; i < shortcuts.length; ++i) {

			scIdx = shortcuts[i];

			scCmds[i] = CommandEx.create(CommandEx.getShortcutCommandId(scIdx),
				CommandEx.getShortcutText(scIdx));

			scCmds[i].setEnabled(CommandEx.isShortcutEnabled(scIdx));
		}

		if (scCmds.length > 0) {

			Menu scMenu = Menu.buildShortcutMenu(getId(), this, scCmds);

			scMenu.show();

			menuBar.setMenu(scMenu);
		}
	}

	public void closeMenus() {

		Menu menu = getRootMenu();
		if (menu != null) {

			menu.close();

			if (menu.type != MenuType.WINDOW_MENU) {
				resetWindowMenu();
			}
		}
	}

	public void popoutMenus() {

		Menu menu = getRootMenu();
		if (menu != null) {

			menu.popout();

			if (menu.type != MenuType.WINDOW_MENU) {

				resetWindowMenu();
			}
		}
	}

	protected void popoutMenusInFrontOf(Menu aMenu) {

		closePopups(menuLayer, aMenu, false);
	}

	protected Menu getTopMenu() {

		return (Menu) menuLayer.getLastComponent();
	}

	protected Menu getRootMenu() {

		return (Menu) menuLayer.getFirstComponent();
	}

	protected boolean hasMenu() {

		return menuLayer.getComponentCount() > 0;
	}

	void appendMenu(Menu aMenu) {

		if (!menuLayer.contains(aMenu)) {

			menuLayer.addComponent(aMenu);

			this.setCurrentFocused(aMenu.findFirstFocusable(), true);

			menuBar.repaint(Reason.UPDATE);
		}
	}

	void removeMenu(Menu aMenu) {

		Assert.assertNotNull(aMenu);

		menuLayer.removeComponent(aMenu);
		initFocused();

		menuBar.repaint(Reason.UPDATE);
	}

	void appendFloat(Popup aFloat) {

		if (!floatLayer.contains(aFloat)) {
			floatLayer.addComponent(aFloat);
		}
	}

	void removeFloat(Popup aFloat) {

		floatLayer.removeComponent(aFloat);
	}

	public void closeFloats() {

		closePopups(floatLayer, null, true);
	}

	public void popoutFloats() {

		closePopups(floatLayer, null, false);
	}

	private void closePopups(Component aPopupLayer, Popup aStop, boolean aClose) {

		Popup popup;

		for (int i = aPopupLayer.getComponentCount(); i-- != 0;) {

			popup = (Popup) aPopupLayer.getComponentAt(i);
			if (popup == aStop) {
				return;
			}

			if (aClose) {
				popup.close();
			} else {
				popup.popout();
			}
		}

		// aPopupLayer.setVisible(false);
	}

	/**
	 * The given component is interested in animating its appearance and will
	 * start receiving callbacks when it is visible in the window allowing it to
	 * animate its appearance. This method would not register a component
	 * instance more than once
	 * 
	 * @param aCmp component that would be animated
	 */
	public final void registerAnimated(Animation aCmp) {

		if (!animatables.contains(aCmp)) {

			animatables.addElement(aCmp);
		}
	}

	/**
	 * Indicate that component would no longer like to receive animation events
	 * 
	 * @param aCmp component that would no longer receive animation events
	 */
	public final void deregisterAnimated(Animation aCmp) {

		animatables.removeElement(aCmp);
	}

	/**
	 * Has registered animations.
	 * 
	 * @return true if has registered animations
	 */
	public final boolean hasAnimations() {

		return animatables.size() > 0;
	}

	/**
	 * Makes sure all animations are repainted so they would be rendered in
	 * every frame
	 */
	public final boolean repaintAnimations() {

		// we don't save size() in a variable since the animate method may
		// deregister the animation thus invalidating the size
		boolean hasAnimation = false;
		for (int iter = 0; iter < animatables.size(); iter++) {

			Animation ani = (Animation) animatables.elementAt(iter);
			if (ani.animate()) {

				Log.d(Animation.TAG, ani);
				if (ani instanceof Component) {
					// ask repaint
					Component cmp = (Component) ani;
					cmp.repaint(Reason.ANIMATING);
				} else {
					// directly post to paint
					CanvasEx.postRepaint(ani);
				}
				hasAnimation = true;
			}
		}
		
		return hasAnimation;
	}

	/**
	 * Makes sure the component is visible in the scroll if this container is
	 * scrollable
	 * 
	 * @param aCmp the component to be visible
	 */
	public final void scrollComponentToVisible(Component aCmp) {

		Component scrollableParent = aCmp.getScrollableParent();

		if (scrollableParent != null) {

			scrollableParent.scrollComponentToVisible(aCmp);
		}
	}

	public final void initFocused() {

		if (hasMenu()) {
			this.setCurrentFocused(getTopMenu().findFirstFocusable(), true);
		} else if (hasModal()) {
			this.setCurrentFocused(getTopModal().findFirstFocusable(), true);
		} else {
			if (oldFocused != null) {
				this.setCurrentFocused(oldFocused);
				oldFocused = null;
			} else if (focused == null) {
				this.setCurrentFocused(findFirstFocusable());
			}
		}
	}

	/**
	 * Sets the current focused component and fires the appropriate focus
	 * changed event
	 * 
	 * @param aFocused the newly focused component or null for no focus
	 */
	public final void setCurrentFocused(Component aFocused) {

		this.setCurrentFocused(aFocused, isModalPopup(aFocused), null);
	}

	private final void setCurrentFocused(Component aFocused, Event aCauseEv) {

		this.setCurrentFocused(aFocused, isModalPopup(aFocused), aCauseEv);
	}

	private final void setCurrentFocused(Component aFocused, boolean aFromModal) {

		this.setCurrentFocused(aFocused, aFromModal, null);
	}

	private final void setCurrentFocused(Component aFocused,
		boolean aFromModal, Event aCauseEv) {

		if (focused == aFocused) {
			if (focused != null) {
				focused.repaint(Reason.FOCUS_GAINED);
			}
			return;
		}

		Component oldFocus = focused;
		focused = aFocused;// change

		if (oldFocus != null && !aFromModal) {
			changeFocusState(oldFocus, false, aCauseEv);
		}

		if (focused != null) {
			changeFocusState(focused, true, aCauseEv);
			if (aCauseEv != null) {
				scrollComponentToVisible(focused);
			}
		}

		// new focused is inside a modal popup?
		if (oldFocused == null && aFromModal) {
			oldFocused = oldFocus;
		}
	}

	/**
	 * Sets the current pressed component, set to <code>null</code> means no
	 * component are on pressed state.
	 */
	private final void setCurrentPressed(Component aPressed) {

		if (pressed != null && pressed.isPressed()) {

			pressed.setPressed(false);
			pressed.trigger();
		}

		pressed = aPressed;// change
		if (pressed != null) {
			pressed.setPressed(true);
		}
	}

	/**
	 * Clear the current pressed component.
	 */
	private final void clearCurrentPressed() {

		if (pressed != null) {

			if (pressed.isPressed()) {
				pressed.setPressed(false);
			}

			pressed = null;
		}
	}

	/**
	 * Request focus for a window child component
	 * 
	 * @param aCmp the window child component
	 */
	protected final void requestFocus(Component aCmp) {

		if (contains(aCmp) && aCmp.isFocusable()) {
			this.setCurrentFocused(aCmp);
		}
	}

	/**
	 * Returns the current focus component for this window
	 * 
	 * @return the current focus component for this window
	 */
	public Component getFocused() {

		return focused;
	}

	/**
	 * This method changes the component state to be focused/unfocused and fires
	 * the focus gained/lost events.
	 * 
	 * @param aCmp the Component to change the focus state
	 * @param aGained if true this Component needs to gain focus if false it
	 *            needs to lose focus
	 */
	private void changeFocusState(Component aCmp, boolean aGained,
		Event aCauseEv) {

		if (aGained) {

			aCmp.setFocus(true);
			aCmp.focusGained(aCauseEv);
			aCmp.repaint(Reason.FOCUS_GAINED);
		} else {

			aCmp.setFocus(false);
			aCmp.focusLost(aCauseEv);
			aCmp.repaint(Reason.FOCUS_LOST);
		}
	}

	/**
	 * Sets the current dragged component, set to <code>null</code> means no
	 * component are on dragged state.
	 */
	protected void setCurrentDragged(Component aDragged) {

		dragged = aDragged;
		if (dragged != null) {

			dragged.repaint(Reason.DRAGGING);
		}

		// clear current pressed if it is dragging
		clearCurrentPressed();
	}

	/**
	 * Event route entry in Window.
	 */
	public final boolean event(Event aEv) {

		boolean isPt = aEv.isPointerEvent();
		boolean isKey = aEv.isKeyEvent();

		// 1, forward none pointer events to menubar first
		if (!isPt && menuBar.event(aEv)) {
			return true;
		}

		// 2, then forward key events to menu
		if (hasMenu()) {

			if (isKey && getTopMenu().event(aEv)) {
				return true;
			}

			// pressed outside menus, hit the menu layer will close all menus
			if (isPt && aEv.type == EventType.POINTER_PRESSED) {
				if (menuLayer.hitComponent(aEv.getX(), aEv.getY()) == menuLayer) {
					closeMenus();
				}
			}
		}

		// 3, then forward key events to modal
		if (isKey && hasModal() && getTopModal().event(aEv)) {
			return true;
		}

		// 4, then filter shortcut key
		if (isKey && filterShortcut(aEv)) {
			return true;
		}

		// 5, then normal routing
		if (super.event(aEv)) {
			return true;
		}

		return false;
	}

	public void keyPressed(Event aKeyEv) {

		// forward to focused
		if (focused != null) {

			boolean hasModal = hasModal() || hasMenu();
			boolean hasModalFocused = hasModal && isModalPopup(focused);

			if (!hasModal || hasModalFocused) {

				// forward to focused first
				if (focused.event(aKeyEv)) {

					if (focused == null) {
						initFocused();
					}

					return;
				}

				// handled by window itself, set pressed or focus navigation
				if (aKeyEv.isSelectKey() && focused.hasAttr(Attr.ACTIONABLE)) {
					setCurrentPressed(focused);
				} else if (aKeyEv.isNavigationKey()) {

					Component next = focused.findNextFocusable(
						aKeyEv.getGameAction(), false);
					// if has modal, the focused need to be kept inside the
					// modal
					if (next != null && (!hasModal || isModalPopup(next))) {
						this.setCurrentFocused(next, aKeyEv);
					}
				}
			}
		} else if (aKeyEv.isNavigationKey()) {
			initFocused();
		}
	}

	public void keyReleased(Event aKeyEv) {

		// release pressed first
		setCurrentPressed(null);

		// forward to focused
		if (focused != null) {

			// focused has accepted long click?
			if (focused.hasState(State.TRIGGERED_LONG_CLICK)) {
				focused.setState(State.TRIGGERED_LONG_CLICK, false);
			} else {
				focused.event(aKeyEv);
			}
		}
	}

	public void keyRepeated(Event aKeyEv) {

		// forward to focused
		if (focused != null) {
			focused.event(aKeyEv);
		}

		// event not accepted by focused component or no focused at current,
		// by default will produces two fake key events (released then pressed)
		// to simulate a repeated behavior
		if (!aKeyEv.isAccepted() && !aKeyEv.isSelectKey()) {

			CanvasEx.postKeyEvent(EventType.KEY_RELEASED, Event.NO_TARGET,
				aKeyEv.getKeyCode(), Event.INTERNAL);

			CanvasEx.postKeyEvent(EventType.KEY_PRESSED, Event.NO_TARGET,
				aKeyEv.getKeyCode(), Event.INTERNAL);

			aKeyEv.accept();
		}
	}

	public void keyLongPressed(Event aKeyEv) {

		// forward to focused
		if (focused != null && focused.event(aKeyEv)) {
			focused.setState(State.TRIGGERED_LONG_CLICK, true);
			clearCurrentPressed();
		}
	}

	public void pointerPressed(Event aPtEv) {

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if (cmp == null || cmp == this || !cmp.isEnabled()) {
			return;
		}

		if (clearScrollingAndDragging(cmp)) {
			return;
		}

		// set as focused
		if (cmp.isFocusable()) {
			this.setCurrentFocused(cmp, aPtEv);
		}

		// forward event, if not accepted and the clicked component
		// is actionable will be set as current pressed
		if (!cmp.event(aPtEv) && cmp.hasAttr(Attr.ACTIONABLE)) {
			setCurrentPressed(cmp);
		}
	}

	public void pointerReleased(Event aPtEv) {

		// release pressed first
		setCurrentPressed(null);

		if (dragged != null) {
			dragged.event(aPtEv);
			setCurrentDragged(null);
			return;
		}

		// focused has accepted long click?
		if (focused != null && focused.hasState(State.TRIGGERED_LONG_CLICK)) {
			focused.setState(State.TRIGGERED_LONG_CLICK, false);
			return;
		}

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if (cmp == null || cmp == this || !cmp.isEnabled()) {
			return;
		}

		// set as focused
		if (cmp.isFocusable()) {
			this.setCurrentFocused(cmp, aPtEv);
		}

		// forward event
		cmp.event(aPtEv);
	}

	public void pointerDragged(Event aPtEv) {

		if (dragged != null) {
			dragged.event(aPtEv);
			return;
		}

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if (cmp == null || cmp == this || cmp == menuBar || cmp == titleBar
			|| !cmp.isEnabled()) {
			return;
		}

		// set as focused
		if (cmp.isFocusable() && !cmp.hasFocus()) {
			this.setCurrentFocused(cmp, aPtEv);
		}

		// forward event
		cmp.event(aPtEv);
	}

	public void pointerLongPressed(Event aPtEv) {

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if (cmp == null || cmp == this || !cmp.isEnabled()) {
			return;
		}

		// forward to focused
		if (focused != null && focused.event(aPtEv)) {
			focused.setState(State.TRIGGERED_LONG_CLICK, true);
			clearCurrentPressed();
		}
	}

	public void progressEvent(Event aProgressEv) {

		Log.d(TAG, "Startup progress : ", aProgressEv);

		// step forward
		gProgress = (gProgress + 1) % gProgressSegments;
		gProgressMessage = aProgressEv.getProgressInfo().toString();

		this.repaint(Reason.PROGRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sizeChanged(int aNewWidth, int aNewHeight) {

		layout();// relayout
		this.repaint(Reason.RELAYOUT);
	}

	public void onEventError(Throwable aErr) {

		super.onEventError(aErr);

		// recover
		recoverError();

		// show error dialog
		Popup.buildSimpleDialog(Popup.ERR_DIALOG, "Error",
			aErr.getMessage() != null ? aErr.getMessage() : aErr.toString(),
			null, CommandEx.CMD_BACKWARD).show();
	}

	private boolean filterShortcut(Event aEv) {

		int keycode = aEv.getKeyCode();
		int scIdx = CommandEx.findShortcut(keycode, 0);

		if (scIdx >= 0) {

			int cmdId = CommandEx.getShortcutCommandId(scIdx);

			if (aEv.type == EventType.KEY_RELEASED) {

				// post shortcut event directly
				CanvasEx.postShortcutActionEvent(Event.NO_TARGET, keycode, 0,
					cmdId);
			}
			return true;
		}

		if (CommandEx.getShortcutCountBy1stKey(keycode) > 0) {

			if (aEv.type == EventType.KEY_RELEASED) {

				// show shortcut menu directly
				showShortcutMenu(keycode);
			}

			return true;
		}

		return false;
	}

	private final boolean clearScrollingAndDragging(Component aCmp) {

		for (Component c = aCmp; c != null; c = c.getParent()) {

			// scrolling or dragging
			if (c.isScrolling() || c.isDragging()) {

				if (c.isScrolling()) {

					// stop first
					c.stopSmoothScroll();
					// guard scroll, scroll back when out of range
					c.guardScroll();
				}

				if (c.isDragging()) {

					// clear drag
					c.clearDrag();
					// clear current dragged
					setCurrentDragged(null);
				}

				return true;
			}
		}

		return false;
	}

	protected void recoverError() {

		closeModals();
		closeMenus();
		closeFloats();

		setCurrentDragged(null);
		setCurrentFocused(null);
		setCurrentPressed(null);

		initFocused();

		CanvasEx.repaintCanvas();
	}

	/**
	 * Post the repaint component to CanvasEx's paint queue.
	 * 
	 * @inheritDoc
	 */
	final void repaint(Component aCmp) {

		if (isVisible()) {
			CanvasEx.postRepaint(aCmp);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void paintContent(GraphicsEx aG) {

		if (hasAttr(Attr.SPLASH)) {
			drawSplash(aG);
		}

		super.paintContent(aG);
	}

	/** {@inheritDoc} */
	protected void paintBackground(GraphicsEx aG) {

		if (hasAttr(Attr.SPLASH)) {
			aG.setBrush(Brush.createColorBrush(0xFF041D34));
			aG.fillRectEx(x, y, width, height);
		} else {
			super.paintBackground(aG);
		}
	}

	private void drawSplash(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aG.translate(x, y);

		int upHeight = height * 2 / 3;
		int btHeight = height - upHeight;

		Image startup = logos[0];
		Image verlogo = logos[1];
		Image progresson = logos[2];
		Image progressoff = logos[3];

		// draw startup logo
		int x = GraphicsEx.getBoxX(startup.getWidth(), 0, width, 0,
			GraphicsEx.IN_BOX | GraphicsEx.HCENTER);
		int y = GraphicsEx.getBoxY(startup.getHeight(), 0, upHeight, 0,
			GraphicsEx.IN_BOX | GraphicsEx.VCENTER);

		aG.drawImage(startup, x, y, GraphicsEx.LEFT_TOP);

		// draw version logo
		x = GraphicsEx.getBoxX(verlogo.getWidth(), 0, width, 0,
			GraphicsEx.IN_BOX | GraphicsEx.HCENTER);
		y += startup.getHeight() + verlogo.getHeight() / 4;

		aG.drawImage(verlogo, x, y, GraphicsEx.LEFT_TOP);

		// draw progress message
		y = GraphicsEx.getBoxY(aG.getFont().getHeight(), upHeight,
			btHeight / 2, 0, GraphicsEx.IN_BOX | GraphicsEx.VCENTER);

		aG.setColor(Color.ALICEBLUE);
		aG.drawBoxedString(gProgressMessage, 0, 0, 0, y, width, btHeight / 2,
			GraphicsEx.HCENTER | GraphicsEx.VCENTER | GraphicsEx.BASELINE);

		// draw progress
		x = GraphicsEx.getBoxX(progresson.getWidth() * gProgressSegments, 0,
			width, 0, GraphicsEx.IN_BOX | GraphicsEx.HCENTER);
		y = GraphicsEx.getBoxY(progresson.getHeight(), upHeight, btHeight, 0,
			GraphicsEx.IN_BOX | GraphicsEx.VCENTER);

		for (int i = 0; i < gProgressSegments; ++i, x += progresson.getWidth()) {

			Image pgicon = i == gProgress ? progresson : progressoff;
			aG.drawImage(pgicon, x, y, GraphicsEx.LEFT_TOP);
		}

		aG.restore();
	}
}
