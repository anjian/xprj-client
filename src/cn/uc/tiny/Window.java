package cn.uc.tiny;

import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.BitUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;
import java.util.Vector;
import javax.microedition.lcdui.Image;

public class Window extends Component {
	public static final String TAG = "Win";
	public static final boolean DEBUG = false;
	public static final int SHOW_UPON_PREVIOUS = 0;
	public static final int SHOW_AND_CLOSE_PREVIOUS = 1;
	public static final int HIDE_MENUBAR = 1;
	public static final int MINIMIZE_TITLEBAR = 2;
	public static final int HIDE_TITLEBAR = 4;
	public static final int NORMAL_SCREEN = 0;
	public static final int FULL_SCREEN = 3;
	private static final int MINIMIZE_TITLEBAR_HEIGHT = 8;
	private static final boolean SWITCH_NEXT = true;
	private static final boolean SWITCH_PREVIOUS = false;
	private static final String VIEW_PANE = "ViewPane";
	private static final String MODAL_LAYER = "ModalLayer";
	private static final String MENU_LAYER = "MenuLayer";
	private static final String FLOAT_LAYER = "FloatLayer";
	private static int gProgressSegments = 5;
	private static int gProgress = 0;
	private static String gProgressMessage = "";

	private static int gShowWindowHint = 0;
	public final TitleBar titleBar;
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
	private final Vector animatables = new Vector();

	private int screen = 0;

	private Rectangle centralBounds = Rectangle.EMPTY;
	private Rectangle viewBounds = Rectangle.EMPTY;
	private Rectangle popupBounds = Rectangle.EMPTY;
	private Image[] logos;

	public static Window createSplash(Image aStartupLogo, Image aVersionLogo,
			Image aProgressOnIcon, Image aProgressOffIcon) {
		Window splash = new Window("Splash", new TitleBar(), new MenuBar());

		splash.setAttr(0, true);
		splash.logos = new Image[] { aStartupLogo, aVersionLogo,
				aProgressOnIcon, aProgressOffIcon };

		splash.titleBar.setAttr(10, false);
		splash.menuBar.setAttr(10, false);

		return splash;
	}

	private static Component createPopupLayer(String aId) {
		Component c = new Component();

		c.setId(aId);
		c.setAttr(10, false);
		c.setAttr(11, false);
		c.setAttr(13, true);
		c.setAttr(1, true);

		if (aId == "FloatLayer") {
			c.setAttr(25, true);
		}

		return c;
	}

	public Window(String aId, TitleBar aTitleBar, MenuBar aMenuBar) {
		setId(aId);
		setState(1, false);

		this.titleBar = aTitleBar;
		this.menuBar = aMenuBar;

		this.viewPane = new Component();
		this.viewPane.setId("ViewPane");
		this.viewPane.setAttr(10, false);
		this.viewPane.setAttr(11, false);

		this.modalLayer = createPopupLayer("ModalLayer");
		this.menuLayer = createPopupLayer("MenuLayer");
		this.floatLayer = createPopupLayer("FloatLayer");

		addComponent(this.titleBar);
		addComponent(this.menuBar);
		addComponent(this.viewPane);
		addComponent(this.modalLayer);
		addComponent(this.menuLayer);
		addComponent(this.floatLayer);

		layout();
	}

	public String getClazz() {
		return hasAttr(0) ? "Splash" : "Window";
	}

	public final Window getWindow() {
		return this;
	}

	protected void initializeComponent() {
		setVisible(true);
		initFocused();

		if (this.menuBar.getMenu() == null)
			resetWindowMenu();
	}

	protected void deinitializeComponent() {
		setVisible(false);

		if (hasAttr(0)) {
			Log.d("Win", "Clear data of splash.");

			gProgressMessage = "";
			gProgress = 0;
		}
	}

	public boolean isChildrenOverlap() {
		return (this.modalLayer.isVisible()) || (this.menuLayer.isVisible())
				|| (this.floatLayer.isVisible());
	}

	public final void show() {
		show(0);
	}

	public final void show(int aShowHint) {
		gShowWindowHint = aShowHint;

		Window oldWin = CanvasEx.getCurrWindow();

		if (oldWin != null) {
			Component winContainer = new Component();
			winContainer.initialize();
			winContainer.setBounds(CanvasEx.getDisplayBounds());

			winContainer.addComponent(oldWin);
			winContainer.addComponent(this);

			int disHeight = CanvasEx.getDisplayHeight();
			winContainer.replaceWithTransition(oldWin, this, 0, -disHeight, 0,
					disHeight, 0, 0);
		}

		switchWindow(oldWin, this, gShowWindowHint == 1);
	}

	public final void close() {
		Window prevWin = CanvasEx.getPrevWindow();

		if (prevWin != null) {
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
		if (getParent() != null) {
			Component winContainer = getParent();
			winContainer.setVisible(false);
			winContainer.removeAll();
		}

		if (CanvasEx.compareOrder(aNewWin, aOldWin) > 0) {
			if (aNewWin != null) {
				aNewWin.showImpl();
			}

			if ((aOldWin != null) && (aCloseOld))
				aOldWin.closeImpl();
		} else {
			if ((aOldWin != null) && (aCloseOld)) {
				aOldWin.closeImpl();
			}

			if (aNewWin != null)
				aNewWin.showImpl();
		}
	}

	private final void showImpl() {
		Log.d("Win", "Show window with hint : ", Log.toString(gShowWindowHint),
				", ", this);
		initialize();
		CanvasEx.showWindow(this);
	}

	private final void closeImpl() {
		Log.d("Win", "Close window : ", this);
		deinitialize();
		CanvasEx.closeWindow(this);
	}

	public Brush getBackground(Component aCmp, int aBackgroundId) {
		return Brush.WHITE_BRUSH;
	}

	public int getForeground(Component aCmp, int aForegroundId) {
		return -16777216;
	}

	public FontEx getFont(Component aCmp) {
		return FontEx.getDefaultFont();
	}

	public Menu getWindowMenu() {
		Menu menu = null;

		if (hasModal()) {
			menu = getTopModal().getWindowMenu();
		}

		if ((menu == null) && (this.currView != null)) {
			menu = this.currView.getWindowMenu();
		}

		if ((menu == null) && (this.menuSrc != null)) {
			menu = this.menuSrc.getWindowMenu();
		}

		return menu;
	}

	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {
		if (this.menuSrc != null) {
			return this.menuSrc.getContextMenu(aContextCmp, aX, aY);
		}
		return null;
	}

	public Menu getSubMenu(CommandEx aGroup) {
		if (this.menuSrc != null) {
			return this.menuSrc.getSubMenu(aGroup);
		}
		return null;
	}

	public void layout() {
		Rectangle bounds = CanvasEx.getDisplayBounds();
		setBounds(bounds);

		FontEx font = getFont();

		int tbHeight = BitUtils.and(this.screen, 4) ? 0 : BitUtils.and(
				this.screen, 2) ? 8 : font.getHeight();
		int mbHeight = BitUtils.and(this.screen, 1) ? 0 : font.getHeight();

		this.titleBar.setBounds(0, 0, bounds.width, tbHeight);
		this.menuBar.setBounds(0, bounds.height - mbHeight, bounds.width,
				mbHeight);

		this.centralBounds = new Rectangle(0, tbHeight, bounds.width,
				bounds.height - tbHeight - mbHeight);

		this.viewPane.setBounds(this.centralBounds);
		this.viewBounds = new Rectangle(0, 0, bounds.width,
				this.centralBounds.height);

		for (int i = getViewCount(); i-- != 0;) {
			getView(i).setBounds(this.viewBounds);
		}

		this.viewPane.layout();

		this.popupBounds = new Rectangle(0, 0, bounds.width, bounds.height
				- mbHeight);

		this.modalLayer.setBounds(this.popupBounds);
		this.menuLayer.setBounds(this.popupBounds);
		this.floatLayer.setBounds(this.popupBounds);
	}

	public final void switchFullScreen() {
		this.screen = (isFullScreen() ? 0 : 3);
		layout();
	}

	public final void setFullScreen(int aScreenHint) {
		this.screen = aScreenHint;
		layout();
	}

	public final int getFullScreen() {
		return this.screen;
	}

	public final boolean isFullScreen() {
		return this.screen == 3;
	}

	public final Rectangle getCentralArea() {
		return this.centralBounds;
	}

	public final Rectangle getPopupArea() {
		return this.popupBounds;
	}

	public final boolean isCurrentView(Component aView) {
		return this.currView == aView;
	}

	public final Component getCurrentView() {
		return this.currView;
	}

	public final int getCurrentViewIndex() {
		return getViewIndex(this.currView);
	}

	public final int getViewCount() {
		return this.viewPane.getComponentCount();
	}

	public final int getViewIndex(Component aView) {
		return this.viewPane.getComponentIndex(aView);
	}

	public final Component getView(int aViewIdx) {
		if (isViewIndexValid(aViewIdx)) {
			return this.viewPane.getComponentAt(aViewIdx);
		}

		return this.currView;
	}

	public final boolean isViewIndexValid(int aViewIdx) {
		return this.viewPane.isComponentIndexValid(aViewIdx);
	}

	public final void switchNextView(boolean aCircle) {
		switchNextOrPreviousView(true, aCircle);
	}

	public final void switchPreviousView(boolean aCircle) {
		switchNextOrPreviousView(false, aCircle);
	}

	public final void switchToView(int aViewIdx, boolean aNext) {
		if (!isViewIndexValid(aViewIdx)) {
			return;
		}

		Component switchView = this.viewPane.getComponentAt(aViewIdx);
		if (this.currView != switchView)
			switchView(this.currView, switchView, aNext);
	}

	public final void addView(Component aView) {
		addView(getViewCount(), aView);
	}

	public final void addView(int aViewIdx, Component aView) {
		this.viewPane.addComponent(aViewIdx, aView);

		aView.setBounds(this.viewBounds);
		aView.layout();
		aView.setVisible(false);

		if (this.currView == null) {
			switchView(null, aView, true);
		}

		this.titleBar.repaint(10);
	}

	public final void closeView(Component aView) {
		int idx = getViewIndex(aView);

		this.viewPane.removeComponent(aView);

		if (this.currView == aView) {
			if (getViewCount() > 0) {
				switchToView(idx, true);
			} else {
				switchView(this.currView, null, true);
			}

			return;
		}

		this.titleBar.repaint(10);
		this.viewPane.repaint(42);
	}

	public final void closeAllViews() {
		for (int i = this.viewPane.getComponentCount(); i-- > 0;) {
			closeView(this.viewPane.getComponentAt(i));
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
		if ((aOldView != null) && (aNewView != null) && (isVisible())) {
			int aOutDstX = aNext ? -aOldView.width : aOldView.width;
			int aInSrcX = aNext ? aNewView.width : -aNewView.width;

			this.viewPane.replaceWithTransition(aOldView, aNewView, aOutDstX,
					0, aInSrcX, 0, 0, 0);
		}

		switchView(aOldView, aNewView);
	}

	final void switchView(Component aOldView, Component aNewView) {
		if (aOldView != null) {
			aOldView.setVisible(false);
		}

		this.currView = aNewView;

		if (aNewView != null) {
			this.currView.setVisible(true);
		}

		if (isVisible()) {
			this.titleBar.repaint(10);
			this.viewPane.repaint(41);

			resetWindowMenu();
		}
	}

	public boolean isModalPopup(Component aCmp) {
		return (this.modalLayer.contains(aCmp))
				|| (this.menuLayer.contains(aCmp));
	}

	void appendPopup(Popup aPopup) {
		Assert.assertNotNull(aPopup);

		if (aPopup.hasAttr(2))
			appendModal(aPopup);
		else if ((aPopup.hasAttr(3)) && ((aPopup instanceof Menu)))
			appendMenu((Menu) aPopup);
		else if (aPopup.hasAttr(4))
			appendFloat(aPopup);
	}

	void removePopup(Popup aPopup) {
		if (aPopup == null) {
			return;
		}

		if (aPopup.hasAttr(2))
			removeModal(aPopup);
		else if ((aPopup.hasAttr(3)) && ((aPopup instanceof Menu)))
			removeMenu((Menu) aPopup);
		else if (aPopup.hasAttr(4))
			removeFloat(aPopup);
	}

	void appendModal(Popup aModal) {
		Assert.assertNotNull(aModal);

		if (!this.modalLayer.contains(aModal)) {
			this.modalLayer.addComponent(aModal);
			setCurrentFocused(aModal.findFirstFocusable(), true);
			closeMenus();
			resetWindowMenu();
		}
	}

	void removeModal(Popup aModal) {
		Assert.assertNotNull(aModal);

		if (this.modalLayer.contains(aModal)) {
			this.modalLayer.removeComponent(aModal);
			initFocused();
			resetWindowMenu();
		}
	}

	public void closeModals() {
		closePopups(this.modalLayer, null, true);
	}

	public void popoutModals() {
		closePopups(this.modalLayer, null, false);
	}

	protected Popup getTopModal() {
		return (Popup) this.modalLayer.getLastComponent();
	}

	protected boolean hasModal() {
		return this.modalLayer.getComponentCount() > 0;
	}

	public void setMenuSource(MenuSource aMenuSource) {
		this.menuSrc = aMenuSource;
	}

	public void setSplashMenu(Menu aWinMenu) {
		Assert.assertNotNull(aWinMenu);
		Assert.assertEquals(aWinMenu.type, 0, 0);

		this.menuBar.setMenu(aWinMenu);
	}

	public void resetWindowMenu() {
		this.menuBar.setMenu(getWindowMenu());
	}

	public void showContextMenu(Menu aContextMenu) {
		Assert.assertNotNull(aContextMenu);
		Assert.assertTrue(aContextMenu.hasAttr(19), 0);
		Assert.assertEquals(aContextMenu.type, 1, 0);

		aContextMenu.show();
		this.menuBar.setMenu(aContextMenu);
	}

	public void showShortcutMenu(int a1stKey) {
		int[] shortcuts = CommandEx.getShortcutsBy1stKey(a1stKey);
		CommandEx[] scCmds = new CommandEx[shortcuts.length];

		for (int i = 0; i < shortcuts.length; i++) {
			int scIdx = shortcuts[i];

			scCmds[i] = CommandEx.create(CommandEx.getShortcutCommandId(scIdx),
					CommandEx.getShortcutText(scIdx));

			scCmds[i].setEnabled(CommandEx.isShortcutEnabled(scIdx));
		}

		if (scCmds.length > 0) {
			Menu scMenu = Menu.buildShortcutMenu(getId(), this, scCmds);

			scMenu.show();

			this.menuBar.setMenu(scMenu);
		}
	}

	public void closeMenus() {
		Menu menu = getRootMenu();
		if (menu != null) {
			menu.close();

			if (menu.type != 0)
				resetWindowMenu();
		}
	}

	public void popoutMenus() {
		Menu menu = getRootMenu();
		if (menu != null) {
			menu.popout();

			if (menu.type != 0) {
				resetWindowMenu();
			}
		}
	}

	protected void popoutMenusInFrontOf(Menu aMenu) {
		closePopups(this.menuLayer, aMenu, false);
	}

	protected Menu getTopMenu() {
		return (Menu) this.menuLayer.getLastComponent();
	}

	protected Menu getRootMenu() {
		return (Menu) this.menuLayer.getFirstComponent();
	}

	protected boolean hasMenu() {
		return this.menuLayer.getComponentCount() > 0;
	}

	void appendMenu(Menu aMenu) {
		if (!this.menuLayer.contains(aMenu)) {
			this.menuLayer.addComponent(aMenu);

			setCurrentFocused(aMenu.findFirstFocusable(), true);

			this.menuBar.repaint(10);
		}
	}

	void removeMenu(Menu aMenu) {
		Assert.assertNotNull(aMenu);

		this.menuLayer.removeComponent(aMenu);
		initFocused();

		this.menuBar.repaint(10);
	}

	void appendFloat(Popup aFloat) {
		if (!this.floatLayer.contains(aFloat))
			this.floatLayer.addComponent(aFloat);
	}

	void removeFloat(Popup aFloat) {
		this.floatLayer.removeComponent(aFloat);
	}

	public void closeFloats() {
		closePopups(this.floatLayer, null, true);
	}

	public void popoutFloats() {
		closePopups(this.floatLayer, null, false);
	}

	private void closePopups(Component aPopupLayer, Popup aStop, boolean aClose) {
		for (int i = aPopupLayer.getComponentCount(); i-- != 0;) {
			Popup popup = (Popup) aPopupLayer.getComponentAt(i);
			if (popup == aStop) {
				return;
			}

			if (aClose)
				popup.close();
			else
				popup.popout();
		}
	}

	public final void registerAnimated(Animation aCmp) {
		if (!this.animatables.contains(aCmp)) {
			this.animatables.addElement(aCmp);
		}
	}

	public final void deregisterAnimated(Animation aCmp) {
		this.animatables.removeElement(aCmp);
	}

	public final boolean hasAnimations() {
		return this.animatables.size() > 0;
	}

	public final boolean repaintAnimations() {
		boolean hasAnimation = false;
		for (int iter = 0; iter < this.animatables.size(); iter++) {
			Animation ani = (Animation) this.animatables.elementAt(iter);
			if (!ani.animate())
				continue;
			Log.d("ANIMATION", ani);
			if ((ani instanceof Component)) {
				Component cmp = (Component) ani;
				cmp.repaint(24);
			} else {
				CanvasEx.postRepaint(ani);
			}
			hasAnimation = true;
		}

		return hasAnimation;
	}

	public final void scrollComponentToVisible(Component aCmp) {
		Component scrollableParent = aCmp.getScrollableParent();

		if (scrollableParent != null) {
			scrollableParent.scrollComponentToVisible(aCmp);
		}
	}

	public final void initFocused() {
		if (hasMenu()) {
			setCurrentFocused(getTopMenu().findFirstFocusable(), true);
		} else if (hasModal()) {
			setCurrentFocused(getTopModal().findFirstFocusable(), true);
		} else if (this.oldFocused != null) {
			setCurrentFocused(this.oldFocused);
			this.oldFocused = null;
		} else if (this.focused == null) {
			setCurrentFocused(findFirstFocusable());
		}
	}

	public final void setCurrentFocused(Component aFocused) {
		setCurrentFocused(aFocused, isModalPopup(aFocused), null);
	}

	private final void setCurrentFocused(Component aFocused, Event aCauseEv) {
		setCurrentFocused(aFocused, isModalPopup(aFocused), aCauseEv);
	}

	private final void setCurrentFocused(Component aFocused, boolean aFromModal) {
		setCurrentFocused(aFocused, aFromModal, null);
	}

	private final void setCurrentFocused(Component aFocused,
			boolean aFromModal, Event aCauseEv) {
		if (this.focused == aFocused) {
			if (this.focused != null) {
				this.focused.repaint(13);
			}
			return;
		}

		Component oldFocus = this.focused;
		this.focused = aFocused;

		if ((oldFocus != null) && (!aFromModal)) {
			changeFocusState(oldFocus, false, aCauseEv);
		}

		if (this.focused != null) {
			changeFocusState(this.focused, true, aCauseEv);
			if (aCauseEv != null) {
				scrollComponentToVisible(this.focused);
			}

		}

		if ((this.oldFocused == null) && (aFromModal))
			this.oldFocused = oldFocus;
	}

	private final void setCurrentPressed(Component aPressed) {
		if ((this.pressed != null) && (this.pressed.isPressed())) {
			this.pressed.setPressed(false);
			this.pressed.trigger();
		}

		this.pressed = aPressed;
		if (this.pressed != null)
			this.pressed.setPressed(true);
	}

	private final void clearCurrentPressed() {
		if (this.pressed != null) {
			if (this.pressed.isPressed()) {
				this.pressed.setPressed(false);
			}

			this.pressed = null;
		}
	}

	protected final void requestFocus(Component aCmp) {
		if ((contains(aCmp)) && (aCmp.isFocusable()))
			setCurrentFocused(aCmp);
	}

	public Component getFocused() {
		return this.focused;
	}

	private void changeFocusState(Component aCmp, boolean aGained,
			Event aCauseEv) {
		if (aGained) {
			aCmp.setFocus(true);
			aCmp.focusGained(aCauseEv);
			aCmp.repaint(13);
		} else {
			aCmp.setFocus(false);
			aCmp.focusLost(aCauseEv);
			aCmp.repaint(14);
		}
	}

	protected void setCurrentDragged(Component aDragged) {
		this.dragged = aDragged;
		if (this.dragged != null) {
			this.dragged.repaint(21);
		}

		clearCurrentPressed();
	}

	public final boolean event(Event aEv) {
		boolean isPt = aEv.isPointerEvent();
		boolean isKey = aEv.isKeyEvent();

		if ((!isPt) && (this.menuBar.event(aEv))) {
			return true;
		}

		if (hasMenu()) {
			if ((isKey) && (getTopMenu().event(aEv))) {
				return true;
			}

			if ((isPt)
					&& (aEv.type == 4)
					&& (this.menuLayer.hitComponent(aEv.getX(), aEv.getY()) == this.menuLayer)) {
				closeMenus();
			}

		}

		if ((isKey) && (hasModal()) && (getTopModal().event(aEv))) {
			return true;
		}

		if ((isKey) && (filterShortcut(aEv))) {
			return true;
		}

		return super.event(aEv);
	}

	public void keyPressed(Event aKeyEv) {
		if (this.focused != null) {
			boolean hasModal = (hasModal()) || (hasMenu());
			boolean hasModalFocused = (hasModal)
					&& (isModalPopup(this.focused));

			if ((!hasModal) || (hasModalFocused)) {
				if (this.focused.event(aKeyEv)) {
					if (this.focused == null) {
						initFocused();
					}

					return;
				}

				if ((aKeyEv.isSelectKey()) && (this.focused.hasAttr(17))) {
					setCurrentPressed(this.focused);
				} else if (aKeyEv.isNavigationKey()) {
					Component next = this.focused.findNextFocusable(
							aKeyEv.getGameAction(), false);

					if ((next != null) && ((!hasModal) || (isModalPopup(next))))
						setCurrentFocused(next, aKeyEv);
				}
			}
		} else if (aKeyEv.isNavigationKey()) {
			initFocused();
		}
	}

	public void keyReleased(Event aKeyEv) {
		setCurrentPressed(null);

		if (this.focused != null) {
			if (this.focused.hasState(12))
				this.focused.setState(12, false);
			else
				this.focused.event(aKeyEv);
		}
	}

	public void keyRepeated(Event aKeyEv) {
		if (this.focused != null) {
			this.focused.event(aKeyEv);
		}

		if ((!aKeyEv.isAccepted()) && (!aKeyEv.isSelectKey())) {
			CanvasEx.postKeyEvent(1, Event.NO_TARGET, aKeyEv.getKeyCode(),
					false);

			CanvasEx.postKeyEvent(0, Event.NO_TARGET, aKeyEv.getKeyCode(),
					false);

			aKeyEv.accept();
		}
	}

	public void keyLongPressed(Event aKeyEv) {
		if ((this.focused != null) && (this.focused.event(aKeyEv))) {
			this.focused.setState(12, true);
			clearCurrentPressed();
		}
	}

	public void pointerPressed(Event aPtEv) {
		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if ((cmp == null) || (cmp == this) || (!cmp.isEnabled())) {
			return;
		}

		if (clearScrollingAndDragging(cmp)) {
			return;
		}

		if (cmp.isFocusable()) {
			setCurrentFocused(cmp, aPtEv);
		}

		if ((!cmp.event(aPtEv)) && (cmp.hasAttr(17)))
			setCurrentPressed(cmp);
	}

	public void pointerReleased(Event aPtEv) {
		setCurrentPressed(null);

		if (this.dragged != null) {
			this.dragged.event(aPtEv);
			setCurrentDragged(null);
			return;
		}

		if ((this.focused != null) && (this.focused.hasState(12))) {
			this.focused.setState(12, false);
			return;
		}

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if ((cmp == null) || (cmp == this) || (!cmp.isEnabled())) {
			return;
		}

		if (cmp.isFocusable()) {
			setCurrentFocused(cmp, aPtEv);
		}

		cmp.event(aPtEv);
	}

	public void pointerDragged(Event aPtEv) {
		if (this.dragged != null) {
			this.dragged.event(aPtEv);
			return;
		}

		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if ((cmp == null) || (cmp == this) || (cmp == this.menuBar)
				|| (cmp == this.titleBar) || (!cmp.isEnabled())) {
			return;
		}

		if ((cmp.isFocusable()) && (!cmp.hasFocus())) {
			setCurrentFocused(cmp, aPtEv);
		}

		cmp.event(aPtEv);
	}

	public void pointerLongPressed(Event aPtEv) {
		Component cmp = hitComponent(aPtEv.getX(), aPtEv.getY());
		if ((cmp == null) || (cmp == this) || (!cmp.isEnabled())) {
			return;
		}

		if ((this.focused != null) && (this.focused.event(aPtEv))) {
			this.focused.setState(12, true);
			clearCurrentPressed();
		}
	}

	public void progressEvent(Event aProgressEv) {
		Log.d("Win", "Startup progress : ", aProgressEv);

		gProgress = (gProgress + 1) % gProgressSegments;
		gProgressMessage = aProgressEv.getProgressInfo().toString();

		repaint(25);
	}

	public void sizeChanged(int aNewWidth, int aNewHeight) {
		layout();
		repaint(30);
	}

	public void onEventError(Throwable aErr) {
		super.onEventError(aErr);

		recoverError();

		Popup.buildSimpleDialog(
				"ErrorDlg",
				"Error",
				aErr.getMessage() != null ? aErr.getMessage() : aErr.toString(),
				null, CommandEx.CMD_BACKWARD).show();
	}

	private boolean filterShortcut(Event aEv) {
		int keycode = aEv.getKeyCode();
		int scIdx = CommandEx.findShortcut(keycode, 0);

		if (scIdx >= 0) {
			int cmdId = CommandEx.getShortcutCommandId(scIdx);

			if (aEv.type == 1) {
				CanvasEx.postShortcutActionEvent(Event.NO_TARGET, keycode, 0,
						cmdId);
			}
			return true;
		}

		if (CommandEx.getShortcutCountBy1stKey(keycode) > 0) {
			if (aEv.type == 1) {
				showShortcutMenu(keycode);
			}

			return true;
		}

		return false;
	}

	private final boolean clearScrollingAndDragging(Component aCmp) {
		for (Component c = aCmp; c != null; c = c.getParent()) {
			if ((!c.isScrolling()) && (!c.isDragging()))
				continue;
			if (c.isScrolling()) {
				c.stopSmoothScroll();

				c.guardScroll();
			}

			if (c.isDragging()) {
				c.clearDrag();

				setCurrentDragged(null);
			}

			return true;
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

	final void repaint(Component aCmp) {
		if (isVisible())
			CanvasEx.postRepaint(aCmp);
	}

	protected void paintContent(GraphicsEx aG) {
		if (hasAttr(0)) {
			drawSplash(aG);
		}

		super.paintContent(aG);
	}

	protected void paintBackground(GraphicsEx aG) {
		if (hasAttr(0)) {
			aG.setBrush(Brush.createColorBrush(-16507596));
			aG.fillRectEx(this.x, this.y, this.width, this.height);
		} else {
			super.paintBackground(aG);
		}
	}

	private void drawSplash(GraphicsEx aG) {
		aG.save(2);
		aG.translate(this.x, this.y);

		int upHeight = this.height * 2 / 3;
		int btHeight = this.height - upHeight;

		Image startup = this.logos[0];
		Image verlogo = this.logos[1];
		Image progresson = this.logos[2];
		Image progressoff = this.logos[3];

		int x = GraphicsEx.getBoxX(startup.getWidth(), 0, this.width, 0, 129);
		int y = GraphicsEx.getBoxY(startup.getHeight(), 0, upHeight, 0, 130);

		aG.drawImage(startup, x, y, 20);

		x = GraphicsEx.getBoxX(verlogo.getWidth(), 0, this.width, 0, 129);
		y += startup.getHeight() + verlogo.getHeight() / 4;

		aG.drawImage(verlogo, x, y, 20);

		y = GraphicsEx.getBoxY(aG.getFont().getHeight(), upHeight,
				btHeight / 2, 0, 130);

		aG.setColor(-984833);
		aG.drawBoxedString(gProgressMessage, 0, 0, 0, y, this.width,
				btHeight / 2, 67);

		x = GraphicsEx.getBoxX(progresson.getWidth() * gProgressSegments, 0,
				this.width, 0, 129);
		y = GraphicsEx.getBoxY(progresson.getHeight(), upHeight, btHeight, 0,
				130);

		for (int i = 0; i < gProgressSegments; x += progresson.getWidth()) {
			Image pgicon = i == gProgress ? progresson : progressoff;
			aG.drawImage(pgicon, x, y, 20);

			i++;
		}

		aG.restore();
	}

	public void setTitle(Component aTitle) {
		this.titleBar.setTitle(aTitle);
	}
}