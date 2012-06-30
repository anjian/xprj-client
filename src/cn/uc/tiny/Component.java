/**
 * Tiny.cn.uc.ui.Component.java, 2010-11-30
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import java.util.Vector;

import cn.uc.build.Config;
import cn.uc.tiny.animations.Motion;
import cn.uc.tiny.ex.BasicEventHandler;
import cn.uc.tiny.ex.Brush;
import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.CanvasEx.Axis;
import cn.uc.tiny.ex.CanvasEx.Direction;
import cn.uc.tiny.ex.Color;
import cn.uc.tiny.ex.CommandEx;
import cn.uc.tiny.ex.Event;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.ex.EventHandler;
import cn.uc.tiny.ex.FontEx;
import cn.uc.tiny.ex.GraphicsEx;
import cn.uc.tiny.ex.GraphicsEx.DrawHints;
import cn.uc.tiny.ex.ImageEx;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.BitUtils;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * Root class for all widgets in the toolkit.
 * 
 * <p>
 * Component already support container and scroll logic on its own, and all
 * components are potentially animated (need to be registered in {@link Window}
 * ).
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class Component extends BasicEventHandler implements UIElement,
	MenuSource {

	/** The debug flag. */
	public static final boolean DEBUG = Config.COMPONENT_DEBUG;

	public static final String P_TAG = "PntCmp";
	public static final String S_TAG = "PntScl";

	public static final boolean PAINT_PARENTS_BACKGROUND = true;
	public static final boolean DONT_PAINT_PARENTS_BACKGROUND = false;

	public static final boolean PAINT_INTERSECTING_SIBLINGS = true;
	public static final boolean DONT_PAINT_INTERSECTING_SIBLINGS = false;

	public static final boolean PAINT_ABOVE_INTERSECTING_SIBLINGS = true;
	public static final boolean PAINT_BELOW_INTERSECTING_SIBLINGS = false;

	public static final boolean FIND_FIRST = true;
	public static final boolean FIND_LAST = false;

	private static final int DEFAULT_SCROLLBAR_CLICKABLE_LEN = 10;

	private static final int SCROLL_MOTION_FRICTION = 4;
	private static final int SCROLL_MOTION_DURATION = 500;
	private static final int GUARD_TIMER_DELAY = 500;

	/**
	 * {BitSet} Attribute of {@link Component}
	 */
	protected static final class Attr {

		// type relative flags, occupy bit 0 ~ 9
		/** The component is a Splash Window. */
		public static final int SPLASH = 0;
		/** The component is a overlay layer upon Window to contains Popup. */
		public static final int POPUP_LAYER = 1;
		/** The component is a modal type popup, such as Dialog. */
		public static final int MODAL = 2;
		/** The component is a menu type popup, such as Menu. */
		public static final int MENU = 3;
		/** The component is a float type popup, such as Toast. */
		public static final int FLOAT = 4;
		/** The component is exactly a Dialog. */
		public static final int DIALOG = 5;
		/** The component is exactly Toast. */
		public static final int TOAST = 6;

		// painting relative flags occupy bit 10 ~ 15
		/** The component has background. */
		public static final int HAS_BACKGROUND = 10;
		/** The component's background is opaque. */
		public static final int OPAQUE = 11;
		/** The component's scrollbar is visible. */
		public static final int SCROLLBAR_VISIBLE = 12;
		/** The component is a container and support children overlap. */
		public static final int CHILDREN_OVERLAP = 13;

		// ability or characteristic flags, occupy bit 16 ~ 31
		public static final int FOCUSABLE = 16;
		public static final int ACTIONABLE = 17;
		public static final int INPUTABLE = 18;
		public static final int POPABLE = 19;
		/** The component is allowed to scroll on x axis */
		public static final int SCROLL_X_ENABLE = 20;
		/** The component is allowed to scroll on y axis */
		public static final int SCROLL_Y_ENABLE = 21;
		/** The scrollbar can be clicked to scroll. */
		public static final int SCROLLBAR_CLICKABLE = 22;
		/**
		 * Indicates whether tensile drag (dragging beyond the boundary of the
		 * component and snapping back) is enabled for this component.
		 */
		public static final int TENSILE_DRAG_ENABLE = 23;
		/**
		 * Indicates whether the component should "trigger" tactile touch when
		 * pressed by the user in a touch screen UI.
		 */
		public static final int TACTILE_TOUCH_ENABLE = 24;
		/**
		 * When enabled, this attribute disables the delivery of mouse events to
		 * the widget and its children. Mouse events are delivered to other
		 * widgets as if the widget and its children were not present in the
		 * widget hierarchy; mouse clicks and other events effectively
		 * "pass through" them. This attribute is disabled by default.
		 */
		public static final int TRANSPARENT_FOR_POINTER_EVENT = 25;
		/**
		 * Indicates the component is allow to scroll out of its normal range
		 * and do not need to scroll back.
		 */
		public static final int ALLOW_SCROLL_OUT_OF_RANGE = 26;
		public static final int CIRCLE_FOCUS = 27;
	}

	/**
	 * {BitSet} State of {@link Component}
	 */
	protected static final class State {

		/** Indicates whether component is initialized or not. */
		public static final int INITIALIZED = 0;
		/** Indicates whether component is visible or hidden. */
		public static final int VISIBLE = 1;
		/** Indicates whether component is enabled or disabled. */
		public static final int ENABLED = 2;
		/** Indicates whether component has focus or not. */
		public static final int FOCUSED = 3;
		/**
		 * Indicates whether component is pressed or not, usually this state
		 * used by actionable component clicked by select key or pointer
		 */
		public static final int PRESSED = 4;
		public static final int ACTIVE = 5;
		public static final int SELECTED = 6;
		public static final int INPUTTING = 7;
		/** Indicates the component is on scrolling. */
		public static final int SCROLLING = 8;
		/** Indicates the component is under dragging. */
		public static final int DRAGGING = 9;
		public static final int ANIMATING = 10;
		/**
		 * Indicates the values within the component have changed and preferred
		 * size should be recalculated.
		 */
		public static final int SHOULD_CALC_PREFERRED_SIZE = 11;
		/**
		 * The long click action has been triggered, usually means the component
		 * should ignore the click action.
		 */
		public static final int TRIGGERED_LONG_CLICK = 12;
		public static final int WAITING_TO_POPOUT = 13;
	}

	/**
	 * {Enum} Reason of repaint, used for debug only
	 */
	public static final class Reason {

		public static final int NONE = 0;

		// canvas repaint reason
		public static final int REPAINT_CANVAS = 1;

		// component repaint reason
		public static final int UPDATE = 10;
		public static final int ENABLE = 11;
		public static final int DISABLE = 12;
		public static final int FOCUS_GAINED = 13;
		public static final int FOCUS_LOST = 14;
		public static final int PRESS_ON = 15;
		public static final int PRESS_OFF = 16;
		public static final int ACTIVE_ON = 17;
		public static final int ACTIVE_OFF = 18;
		public static final int SELECT = 19;
		public static final int UNSELECT = 20;
		public static final int DRAGGING = 21;
		public static final int SCROLLED = 22;
		public static final int SCROLLING = 23;
		public static final int ANIMATING = 24;
		public static final int PROGRESS = 25;
		public static final int OPAQUE_CHANGED = 26;
		public static final int STYLE_CHANGED = 27;

		// container repaint reason
		public static final int RELAYOUT = 30;
		public static final int CHILD_ADD = 31;
		public static final int CHILD_REMOVE = 32;
		public static final int CHILD_REPLACE = 33;
		public static final int CHILD_TRANSITION = 34;
		public static final int CHILD_BOUNDS_CHANGED = 35;

		// window repaint reason
		public static final int SWITCH_VIEW = 41;
		public static final int CLOSE_VIEW = 42;
	}

	static final int[] gTempPoint = new int[2];
	static final int[] gTempRect = new int[4];

	private static int gGuardDragTimer = -1;

	private static Component gTransInCmp = null;
	private static Component gTransOutCmp = null;

	private static Motion gOutMotion = Motion.createFriction(6000, 12, true);
	private static Motion gInMotion = gOutMotion.clone();

	/** Class of component, similar with element class. */
	// private String clazz = StringUtils.EMPTY;

	/** Id of component, similar with element id. */
	protected String id = StringUtils.EMPTY;

	/** The Window component belong to. */
	protected Window window;
	/** Parent of component. */
	protected Component parent;
	/** Children of component */
	protected Vector children;

	/** Attributes of component. */
	private int attributes;
	/** States of component. */
	private int states;

	/** Bounds of component, i.e. position and size. */
	protected int x, y, width, height;
	/** Scroll position of component. */
	private int scrollX, scrollY;
	/** Preferred size of component. */
	private int preferredW, preferredH;

	/** Scroll motion */
	private Motion scrollMotion;

	/** The dirty region of component for the upcoming repaint. */
	private Rectangle dirtyRegion = Rectangle.EMPTY;

	/** Construct a new component. */
	public Component() {

		setAttr(Attr.HAS_BACKGROUND, true);
		setAttr(Attr.OPAQUE, true);

		setState(State.ENABLED, true);
		setState(State.VISIBLE, true);
	}

	public String getName() {

		return "C";
	}

	public String getClazz() {

		return "C";
	}

	/**
	 * Unique identifier for a component. This id is used to retrieve a suitable
	 * Style.
	 * 
	 * @return unique string identifying this component for the style sheet
	 */
	public final String getId() {

		return id;
	}

	/**
	 * This method sets the Component the Unique identifier. This method should
	 * be used before a component has been initialized
	 * 
	 * @param aId
	 */
	public final void setId(String aId) {

		Assert.assertNotNull(aId);
		id = aId;
	}

	/**
	 * Find a component, which id equals with the given one.
	 * 
	 * @param aId the id used to find the matched component
	 * @return found component or null when no matched
	 */
	public final Component findComponentById(String aId) {

		if (StringUtils.isEmpty(aId)) {
			return null;
		}

		if (aId.equals(id)) {
			return this;
		}

		Component found = null;
		for (int i = getComponentCount(); i-- > 0;) {

			found = getComponentAt(i).findComponentById(aId);
			if (found != null) {
				break;
			}
		}

		return found;
	}

	/**
	 * Sets the Component Parent.
	 * 
	 * <p>
	 * <strong>This method should not be called by the user.</strong>
	 * </p>
	 * 
	 * @param parent the parent container
	 */
	final void setParent(Component aParent) {

		parent = aParent;
	}

	/**
	 * Gets the parent of component.
	 * 
	 * @return the parent of component.
	 */
	public final Component getParent() {

		return parent;
	}

	/**
	 * Returns a parent container that is scrollable or null if no parent is
	 * scrollable.
	 * 
	 * @return a parent container that is scrollable or null if no parent is
	 *         scrollable.
	 */
	public final Component getScrollableParent() {

		for (Component p = parent; p != null; p = p.parent) {
			if (p.isScrollable()) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Returns the Component window or null if this Component is not added yet
	 * to a window.
	 * 
	 * @return the Component's belong top level window
	 */
	public Window getWindow() {

		if (parent != null) {
			if (window == null) {
				window = parent.getWindow();
			}
			return window;
		}

		return null;
	}

	/**
	 * Returns true if this component is a parent or grandparent and so on to
	 * any level of the given child, and both components are within the same
	 * window; otherwise returns false.
	 * 
	 * @param aCmp given child
	 * @return true if this component is ancestor of given child
	 */
	public final boolean isAncestorOf(Component aCmp) {

		if (aCmp == null) {
			return false;
		}

		for (Component p = aCmp.parent; p != null; p = p.parent) {
			if (p == this) {
				return true;
			}
		}

		return false;
	}

	protected final void setAttr(int aAttrFlag, boolean aVal) {

		attributes = BitUtils.set(attributes, aAttrFlag, aVal);
	}

	protected final boolean hasAttr(int aAttrFlag) {

		return BitUtils.get(attributes, aAttrFlag);
	}

	protected final void setState(int aStateFlag, boolean aVal) {

		states = BitUtils.set(states, aStateFlag, aVal);
	}

	protected final boolean hasState(int aStateFlag) {

		return BitUtils.get(states, aStateFlag);
	}

	/**
	 * Set the component has background or not.
	 * 
	 * @param aHasBackground true for has background, false for opposite
	 */
	public final void setHasBackground(boolean aHasBackground) {

		setAttr(Attr.HAS_BACKGROUND, aHasBackground);
	}

	/**
	 * The component has background or not.
	 * 
	 * @return true when the component has background
	 */
	public boolean hasBackground() {

		return hasAttr(Attr.HAS_BACKGROUND);
	}

	/**
	 * Whether or not the background of component is opaque.
	 * 
	 * <p>
	 * <strong>Subclass may need to re-implement this method when its opacity
	 * changed according its states.</strong>
	 * </p>
	 * 
	 * @return true if the component's background is opaque
	 */
	public boolean isOpaque() {

		return hasBackground()
			&& (hasAttr(Attr.OPAQUE) || isScrolling() || isDragging());
	}

	/**
	 * Whether the component is a container and support its children overlap
	 * 
	 * @return true if the component is a container and support overlap
	 */
	public boolean isChildrenOverlap() {

		return getComponentCount() > 1 && hasAttr(Attr.CHILDREN_OVERLAP);
	}

	/**
	 * Returns true if this component can receive focus and is enabled
	 * 
	 * @return true if this component can receive focus; otherwise false
	 */
	public final boolean isFocusable() {

		return hasAttr(Attr.FOCUSABLE) && isEnabled() && isVisible();
	}

	/**
	 * Set the target of component's action event, if the component is
	 * actionable and clicked by select key or pointer.
	 * 
	 * @param aActionTarget the target for the action event of this component
	 */
	public void setActionTarget(EventHandler aActionTarget) {

	}

	/**
	 * Get the target of component's action event, if the component is
	 * actionable and clicked by select key or pointer.
	 * 
	 * @return the source of the action
	 */
	public EventHandler getActionTarget() {

		return Event.NO_TARGET;
	}

	/**
	 * Trigger an action and post the event to its action target.
	 */
	public void trigger() {

		CanvasEx.postComponentActionEvent(getActionTarget(), this);
	}

	/**
	 * Initialize the component and its children, it will actually call
	 * {@link #initializeComponent()} to do the job, and set the flag
	 * {@link State#INITIALIZED}, subclass should override
	 * {@link #initializeComponent()} to implement its own initialization logic.
	 * 
	 * <p>
	 * This method will guarantee called by the toolkit before the component
	 * show on the screen.
	 * </p>
	 */
	protected final void initialize() {

		if (!isInitialized()) {

			if (parent != null) {
				window = parent.window;
			}

			initializeComponent();
			setState(State.INITIALIZED, true);
		}

		for (int i = getComponentCount(); i-- > 0;) {
			getComponentAt(i).initialize();
		}
	}

	/**
	 * The method should be overridden by subclass to implement its own
	 * initialization logic.
	 */
	protected void initializeComponent() {

	}

	/**
	 * Deinitialize the component and its children, cleans up the initialization
	 * flags in the hierarchy, notice that paint calls might still occur after
	 * deinitialization mostly cause by perform a transitions etc.
	 * 
	 * <p>
	 * However interactivity, animation and event tracking code can and probably
	 * should be removed by this method.
	 */
	protected final void deinitialize() {

		if (isInitialized()) {

			window = null;
			setState(State.INITIALIZED, false);
			setDirtyRegion(Rectangle.EMPTY);
			deinitializeComponent();
		}

		for (int i = getComponentCount(); i-- > 0;) {
			getComponentAt(i).deinitialize();
		}
	}

	/**
	 * Invoked to indicate that the component initialization is being reversed
	 * since the component was detached from the container hierarchy. This
	 * allows the component to deregister animators and cleanup after itself.
	 * 
	 * <p>
	 * This method is the opposite of the {@link #initializeComponent()} method.
	 * 
	 * <p>
	 * The method should be overridden by subclass to implement its own
	 * deinitialization logic.
	 */
	protected void deinitializeComponent() {

	}

	/**
	 * Indicated the component is initialized or not.
	 * 
	 * @return true when the component is initialized.
	 */
	public final boolean isInitialized() {

		return hasState(State.INITIALIZED);
	}

	/**
	 * Toggles visibility of the component
	 * 
	 * @param aVisible true if component is visible; otherwise false
	 */
	void setVisible(boolean aVisible) {

		setState(State.VISIBLE, aVisible);
	}

	/**
	 * Returns whether the component is visible or not
	 * 
	 * @return true if component is visible; otherwise false
	 */
	public boolean isVisible() {

		// popup layer's visibility will be treated specially
		// it is invisible when it contains no children

		return width > 0
			&& height > 0
			&& (hasAttr(Attr.POPUP_LAYER) ? getComponentCount() > 0
				: hasState(State.VISIBLE));
	}

	/**
	 * Indicates whether component is enabled or disabled thus allowing us to
	 * prevent a component from receiving input events and indicate so visually
	 * 
	 * @param aEnabled true to enable, false to disable
	 */
	void setEnabled(boolean aEnabled) {

		setState(State.ENABLED, aEnabled);

		this.repaint(aEnabled ? Reason.ENABLE : Reason.DISABLE);
	}

	/**
	 * Indicates whether component is enabled or disabled thus allowing us to
	 * prevent a component from receiving input events and indicate so visually
	 * 
	 * @return true if enabled
	 */
	public final boolean isEnabled() {

		return hasState(State.ENABLED);
	}

	/**
	 * Set the component at pressed state, i.e. clicked.
	 * 
	 * @param aPressed true if component is pressed; otherwise false
	 */
	void setPressed(boolean aPressed) {

		setState(State.PRESSED, aPressed);

		this.repaint(aPressed ? Reason.PRESS_ON : Reason.PRESS_OFF);
	}

	/**
	 * Indicates whether component is pressed or not thus allowing us to
	 * indicate the state visually
	 * 
	 * @return true if enabled
	 */
	public final boolean isPressed() {

		return hasState(State.PRESSED);
	}

	void setActive(boolean aActive) {

		setState(State.ACTIVE, aActive);

		this.repaint(aActive ? Reason.ACTIVE_ON : Reason.ACTIVE_OFF);
	}

	public final boolean isActive() {

		return hasState(State.ACTIVE);
	}

	/**
	 * Set the component at selected state, i.e. as current choose item.
	 * 
	 * @param aSelected true if component is selected; otherwise false
	 */
	void setSelected(boolean aSelected) {

		setState(State.SELECTED, aSelected);

		this.repaint(aSelected ? Reason.SELECT : Reason.UNSELECT);
	}

	/**
	 * Indicates whether component is selected or not thus allowing us to
	 * indicate the state visually
	 * 
	 * @return true if enabled
	 */
	public final boolean isSelected() {

		return hasState(State.SELECTED);
	}

	/**
	 * This flag doesn't really give focus, its a state that determines what
	 * colors from the Style should be used when painting the component. Actual
	 * focus is determined by the parent form
	 * 
	 * @param aFocused sets the state that determines what colors from the Style
	 *            should be used when painting a focused component
	 * 
	 * @see #requestFocus
	 */
	void setFocus(boolean aFocused) {

		setState(State.FOCUSED, aFocused);
	}

	/**
	 * Returns true if the component has focus
	 * 
	 * @return true if the component has focus; otherwise false
	 */
	public final boolean hasFocus() {

		return hasState(State.FOCUSED);
	}

	/**
	 * Changes the current component to the focused component, will work only
	 * for a component that belongs to a parent window.
	 */
	public final void requestFocus() {

		Window win = getWindow();

		if (win != null) {

			win.requestFocus(this);
		}
	}

	/**
	 * Indicates the component is on inputting.
	 * 
	 * @return true when the component is inputting
	 */
	public final boolean isInputting() {

		return hasState(State.INPUTTING);
	}

	/**
	 * Indicates the component is on scrolling.
	 * 
	 * @return true when the component is scrolling
	 */
	public final boolean isScrolling() {

		return hasState(State.SCROLLING);
	}

	/**
	 * Indicates the component is under dragging.
	 * 
	 * @return true when the component is dragging
	 */
	public final boolean isDragging() {

		return hasState(State.DRAGGING);
	}

	/**
	 * Indicates the component is under animating.
	 * 
	 * @return true when the component is animating
	 */
	public final boolean isAnimating() {

		return hasState(State.ANIMATING);
	}

	/**
	 * Indicates the values within the component have changed and preferred size
	 * should be recalculated
	 * 
	 * @param aShould indicate whether this component need to recalculate his
	 *            preferred size
	 */
	public final void setShouldCalcPreferredSize(boolean aShould) {

		setState(State.SHOULD_CALC_PREFERRED_SIZE, aShould);
	}

	/**
	 * Indicates the component's preferred size is out of date.
	 * 
	 * @return true if the component's preferred size is out of date
	 */
	public final boolean shouldCalcPreferredSize() {

		return hasState(State.SHOULD_CALC_PREFERRED_SIZE);
	}

	public void setText(String aText) {

	}

	public void setTitle(String aTitle) {

	}

	public String getText() {

		return StringUtils.EMPTY;
	}

	public String getTitle() {

		return StringUtils.EMPTY;
	}

	public FontEx getFont() {

		Window win = getWindow();
		return win != null ? win.getFont(this) : FontEx.getDefaultFont();
	}

	public Menu getWindowMenu() {

		return null;
	}

	public Menu getContextMenu() {

		return this.getContextMenu(this, CanvasEx.getCurrPointerPressedX(),
			CanvasEx.getCurrPointerPressedY());
	}

	public Menu getContextMenu(int aX, int aY) {

		return this.getContextMenu(this, aX, aY);
	}

	public Menu getContextMenu(Component aContextCmp, int aX, int aY) {

		return parent != null ? parent.getContextMenu(aContextCmp, aX, aY)
			: null;
	}

	public Menu getSubMenu(CommandEx aGroup) {

		return null;
	}

	public HitTestResult hitTest(int aX, int aY) {

		Component found = hitComponent(aX, aY);

		if (found == null) {
			return HitTestResult.MISS_HIT;
		} else if (found == this) {
			return new HitTestResult(HitTestResult.HIT_MYSELF, found);
		} else {
			return new HitTestResult(HitTestResult.HIT_MY_CHILD, found);
		}
	}

	/**
	 * Whether or not the given x, y coordinates is upon the component.
	 * 
	 * @param aX absolute screen location
	 * @param aY absolute screen location
	 * @return true when the given x, y coordinates is upon this component
	 * @see #hitComponent(int, int)
	 * @see #hitTest(int, int)
	 */
	public final boolean hit(int aX, int aY) {

		getAbsolutePos(gTempPoint);
		return Rectangle.contains(gTempPoint[0], gTempPoint[1], width, height,
			aX, aY);
	}

	final boolean hitByLocalPoint(int aX, int aY) {

		return Rectangle.contains(scrollX, scrollY, width, height, aX, aY);
	}

	final boolean hitByParentPoint(int aX, int aY) {

		return Rectangle.contains(x, y, width, height, aX, aY);
	}

	/**
	 * Returns a component that exists in the given x, y coordinates by
	 * traversing component objects and invoking {@link #hit(int, int)}
	 * 
	 * @param aX absolute screen location
	 * @param aY absolute screen location
	 * @return a component if found, null otherwise
	 * @see #hit(int, int)
	 * @see #hitTest(int, int)
	 */
	public final Component hitComponent(int aX, int aY) {

		mapFromAbsolute(aX, aY, gTempPoint);
		int localX = gTempPoint[0];
		int localY = gTempPoint[1];

		if (hitByLocalPoint(localX, localY)) {
			return hitChildComponent(localX, localY);
		}
		// nothing at all
		return null;
	}

	private final Component hitChildComponent(int localX, int localY) {

		Component cmp;
		for (int i = getComponentCount(); i-- > 0;) {

			cmp = getComponentAt(i);
			if (cmp.isVisible() && cmp.hitByParentPoint(localX, localY)) {

				// find deeper?
				if (cmp.getComponentCount() > 0) {
					cmp = cmp.hitChildComponent(localX - cmp.x + cmp.scrollX,
						localY - cmp.y + cmp.scrollY);
				}

				if (!cmp.hasAttr(Attr.TRANSPARENT_FOR_POINTER_EVENT)) {
					return cmp;
				}
			}
		}

		return this;
	}

	/**
	 * Set the component's new bounds, maybe caused by move or resize the
	 * component.
	 * 
	 * @param aBounds the boundary to set
	 * @see #setBounds(int, int, int, int)
	 * @see #setPosition(int, int)
	 * @see #setSize(int, int)
	 */
	public final void setBounds(Rectangle aBounds) {

		Assert.assertNotNull(aBounds);

		setBounds(aBounds.x, aBounds.y, aBounds.width, aBounds.height);
	}

	/**
	 * Set the component's new bounds, maybe caused by move or resize the
	 * component.
	 * 
	 * @see #setPosition(int, int)
	 * @see #setSize(int, int)
	 */
	public final void setBounds(int aX, int aY, int aWidth, int aHeight) {

		boolean resized = false;
		if (width > 0 && height > 0) {

			// reset bounds, may cause parent's repaint
			if (isVisible() && parent != null) {

				Rectangle.union(x, y, width, height, aX, aY, aWidth, aHeight,
					gTempRect);

				parent.getAbsolutePos(gTempPoint);
				parent.repaint(gTempPoint[0] + gTempRect[0], gTempPoint[1]
					+ gTempRect[1], gTempRect[2], gTempRect[3],
					Reason.CHILD_BOUNDS_CHANGED);
			}
			resized = width != aWidth || height != aHeight;
		}

		setBoundsInternal(aX, aY, aWidth, aHeight);// set bounds
		if (resized) {
			guardScroll();
		}
	}

	/**
	 * Set the bounds directly without repaint, guard scroll, etc...
	 */
	protected void setBoundsInternal(int aX, int aY, int aWidth, int aHeight) {

		x = aX;
		y = aY;
		width = aWidth;
		height = aHeight;
	}

	/**
	 * Sets the component's new location relative to the parent container.
	 * 
	 * @param aX the current x coordinate of the components origin
	 * @param aY the current y coordinate of the components origin
	 */
	public final void setPosition(int aX, int aY) {

		setBounds(aX, aY, width, height);
	}

	/**
	 * Sets the component's new size.
	 * 
	 * @param aWidth new width
	 * @param aHeight new height
	 */
	public final void setSize(int aWidth, int aHeight) {

		setBounds(x, y, aWidth, aHeight);
	}

	/**
	 * Returns the component x location relatively to its parent container
	 * 
	 * @return the current x coordinate of the components origin
	 */
	public final int getX() {

		return x;
	}

	/**
	 * Returns the component y location relatively to its parent container
	 * 
	 * @return the current y coordinate of the components origin
	 */
	public final int getY() {

		return y;
	}

	/**
	 * Returns the component width
	 * 
	 * @return the component width
	 */
	public final int getWidth() {

		return width;
	}

	/**
	 * Returns the component height
	 * 
	 * @return the component height
	 */
	public final int getHeight() {

		return height;
	}

	/**
	 * Returns the absolute X location based on the component hierarchy, this
	 * method calculates a location on the screen for the component rather than
	 * a relative location as returned by getX()
	 * 
	 * @return the absolute x location of the component
	 * @see #getX()
	 */
	public final int getAbsoluteX() {

		int absX = x;
		for (Component p = parent; p != null; p = p.parent) {

			absX += p.x - p.scrollX;
		}

		return absX;
	}

	/**
	 * Returns the absolute Y location based on the component hierarchy, this
	 * method calculates a location on the screen for the component rather than
	 * a relative location as returned by getX()
	 * 
	 * @return the absolute y location of the component
	 * @see #getY
	 */
	public final int getAbsoluteY() {

		int absY = y;
		for (Component p = parent; p != null; p = p.parent) {

			absY += p.y - p.scrollY;
		}

		return absY;
	}

	/**
	 * Get absolute location based on the component hierarchy, more faster than
	 * call both {@link #getAbsoluteX()} and {@link #getAbsoluteY()}.
	 * 
	 * @param aOutputAbsPos the output absolute position
	 * @see #getAbsoluteX()
	 * @see #getAbsoluteY()
	 */
	public final void getAbsolutePos(int[] aOutputAbsPos) {

		int absX = x;
		int absY = y;
		for (Component p = parent; p != null; p = p.parent) {

			absX += p.x - p.scrollX;
			absY += p.y - p.scrollY;
		}

		aOutputAbsPos[0] = absX;
		aOutputAbsPos[1] = absY;
	}

	public final void getAbsoluteBounds(int[] aOutputAbsBounds) {

		getAbsolutePos(aOutputAbsBounds);

		aOutputAbsBounds[2] = width;
		aOutputAbsBounds[3] = height;
	}

	public final void mapFromAbsolute(int aAbsX, int aAbsY,
		int[] aOutputLocalPos) {

		for (Component p = this; p != null; p = p.parent) {

			aAbsX += p.scrollX - p.x;
			aAbsY += p.scrollY - p.y;
		}

		aOutputLocalPos[0] = aAbsX;
		aOutputLocalPos[1] = aAbsY;
	}

	public final int mapFromAbsoluteX(int aAbsX) {

		for (Component p = this; p != null; p = p.parent) {

			aAbsX += p.scrollX - p.x;
		}

		return aAbsX;
	}

	public final int mapFromAbsoluteY(int aAbsY) {

		for (Component p = this; p != null; p = p.parent) {

			aAbsY += p.scrollY - p.y;
		}

		return aAbsY;
	}

	public final void mapToAbsolute(int aLocalX, int aLocalY,
		int[] aOutputAbsPos) {

		for (Component cmp = this; cmp != null; cmp = cmp.parent) {

			aLocalX += cmp.x - cmp.scrollX;
			aLocalY += cmp.y - cmp.scrollY;
		}

		aOutputAbsPos[0] = aLocalX;
		aOutputAbsPos[1] = aLocalY;
	}

	public final int mapToAbsoluteX(int aLocalX) {

		for (Component cmp = this; cmp != null; cmp = cmp.parent) {

			aLocalX += cmp.x - cmp.scrollX;
		}

		return aLocalX;
	}

	public final int mapToAbsoluteY(int aLocalY) {

		for (Component cmp = this; cmp != null; cmp = cmp.parent) {

			aLocalY += cmp.y - cmp.scrollY;
		}

		return aLocalY;
	}

	public final void mapFrom(Component aCmp, int aCmpX, int aCmpY,
		int[] aOutputLocalPos) {

		Component begin = isAncestorOf(aCmp) ? aCmp : this;
		Component end = begin == this ? aCmp : this;

		for (Component c = begin; c != end && c != null; c = c.parent) {

			aCmpX += c.x;
			aCmpY += c.y;
		}

		aOutputLocalPos[0] = aCmpX;
		aOutputLocalPos[1] = aCmpY;
	}

	public final int mapFromX(Component aCmp, int aCmpX) {

		Component begin = isAncestorOf(aCmp) ? aCmp : this;
		Component end = begin == this ? aCmp : this;

		for (Component c = begin; c != end && c != null; c = c.parent) {

			aCmpX += c.x;
		}

		return aCmpX;
	}

	public final int mapFromY(Component aCmp, int aCmpY) {

		Component begin = isAncestorOf(aCmp) ? aCmp : this;
		Component end = begin == this ? aCmp : this;

		for (Component c = begin; c != end && c != null; c = c.parent) {

			aCmpY += c.y;
		}

		return aCmpY;
	}

	/****************************************************************/
	/*                                                              */
	/* Attributes/States/Geometry Relative APIs End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* Scroll Relative APIs Begin */
	/*                                                              */
	/****************************************************************/

	/**
	 * Indicates whether the component should/could scroll, by default a
	 * component is not scrollable.
	 * 
	 * @return whether the component is scrollable
	 */
	public final boolean isScrollable() {

		return isScrollableX() || isScrollableY();
	}

	/**
	 * Indicates whether the component should/could scroll on the X axis
	 * 
	 * @return whether the component is scrollable on the X axis
	 */
	public final boolean isScrollableX() {

		return hasAttr(Attr.SCROLL_X_ENABLE) && getPreferredWidth() > width;
	}

	/**
	 * Indicates whether the component should/could scroll on the Y axis
	 * 
	 * @return whether the component is scrollable on the X axis
	 */
	public final boolean isScrollableY() {

		return hasAttr(Attr.SCROLL_Y_ENABLE) && getPreferredHeight() > height;
	}

	/**
	 * Set the preferred size by client directly.
	 * 
	 * @param aPreferredWidth preferred width
	 * @param aPreferredHeight preferred height {@link #calcPreferredSize()}
	 */
	public final void setPreferredSize(int aPreferredWidth, int aPreferredHeight) {

		if (preferredW == aPreferredWidth && preferredH == aPreferredHeight) {
			return;
		}

		// make it scroll longer when the preferred width/height changed
		if (scrollMotion != null) {

			int dstX = scrollMotion.getDestinationX();
			int dstY = scrollMotion.getDestinationY();

			if (aPreferredWidth > preferredW) {
				dstX += aPreferredWidth - preferredW;
			}

			if (aPreferredHeight > preferredH) {
				dstY += aPreferredHeight - preferredH;
			}

			scrollMotion.setDestination(dstX, dstY);
		}

		// set values
		boolean smaller = preferredW > aPreferredWidth
			|| preferredH > aPreferredHeight;
		setPreferredSizeInternal(aPreferredWidth, aPreferredHeight);
		// it the size less than original, will need guard scroll
		if (smaller) {
			guardScroll();
		}
	}

	protected void setPreferredSizeInternal(int aPreferredWidth,
		int aPreferredHeight) {

		preferredW = aPreferredWidth;
		preferredH = aPreferredHeight;
	}

	/**
	 * Calculates the preferred size based on component content. This method is
	 * invoked lazily by getPreferredWidth and getPreferredHeight.
	 * 
	 * <p>
	 * By default, it will calculate a bounding rectangle for all children.
	 * 
	 * @see #getPreferredWidth()
	 * @see #getPreferredHeight()
	 */
	protected void calcPreferredSize() {

		int maxX = 0, maxY = 0;

		Component child;
		for (int i = getComponentCount(); i-- > 0;) {

			child = getComponentAt(i);

			maxX = Math.max(maxX, child.x + child.width);
			maxY = Math.max(maxY, child.y + child.height);
		}

		setPreferredSizeInternal(maxX, maxY);
	}

	/**
	 * Returns the Component Preferred Size, there is no guarantee the Component
	 * will be sized at its Preferred Size. The final size of the component may
	 * be smaller than its preferred size or even larger than the size.
	 * 
	 * <p>
	 * When the component's actually size smaller than its preferred size, it
	 * may turn to scrollable.
	 * 
	 * @return the component preferred width
	 * @see #getPreferredHeight()
	 */
	public final int getPreferredWidth() {

		if (shouldCalcPreferredSize()) {

			calcPreferredSize();
			setShouldCalcPreferredSize(false);
		}

		return preferredW > 0 ? preferredW : width;
	}

	/**
	 * Returns the Component Preferred Size, there is no guarantee the Component
	 * will be sized at its Preferred Size. The final size of the component may
	 * be smaller than its preferred size or even larger than the size.
	 * 
	 * <p>
	 * When the component's actually size smaller than its preferred size, it
	 * may turn to scrollable.
	 * 
	 * @return the component preferred size
	 * @see #getPreferredWidth()
	 */
	public final int getPreferredHeight() {

		if (shouldCalcPreferredSize()) {

			calcPreferredSize();
			setShouldCalcPreferredSize(false);
		}

		return preferredH > 0 ? preferredH : height;
	}

	/**
	 * Indicates the X position of the scrolling, this number is relative to the
	 * component position and so a position of 0 would indicate the x position
	 * of the component.
	 * 
	 * @param aScrollX the X position of the scrolling
	 */
	protected final void setScrollX(int aScrollX) {

		Assert.assertTrue(isScrollableX(), Assert.STATE);

		int tensile = getTensileLength();
		aScrollX = Math.max(Math.min(aScrollX, getScrollXLimit() + tensile),
			-tensile);

		if (aScrollX != scrollX) {

			scrollX = aScrollX;
			this.repaint(Reason.SCROLLED);
		}
	}

	/**
	 * Indicates the Y position of the scrolling, this number is relative to the
	 * component position and so a position of 0 would indicate the y position
	 * of the component.
	 * 
	 * @param aScrollY the Y position of the scrolling
	 */
	protected final void setScrollY(int aScrollY) {

		Assert.assertTrue(isScrollableY(), Assert.STATE);

		int tensile = getTensileLength();
		aScrollY = Math.max(Math.min(aScrollY, getScrollYLimit() + tensile),
			-tensile);

		if (aScrollY != scrollY) {

			scrollY = aScrollY;
			this.repaint(Reason.SCROLLED);
		}
	}

	public final void frictionScroll(int aSpeed, int aFriction, int aAxis) {

		int srcX = scrollX;
		int maxX = srcX;
		int srcY = scrollY;
		int maxY = srcY;

		int tensile = getTensileLength();
		if (aAxis == Axis.X_AXIS) {
			maxX = aSpeed < 0 ? -tensile : getScrollXLimit() + tensile;
		} else {
			maxY = aSpeed < 0 ? -tensile : getScrollYLimit() + tensile;
		}

		scrollMotion = Motion.createFriction(aSpeed, aFriction, false);
		smoothScroll(srcX, srcY, maxX, maxY);
	}

	public final void tensileScroll(int aInitScroll, int aDestScroll, int aAxis) {

		int srcX, srcY, dstX, dstY;

		if (aAxis == Axis.X_AXIS) {

			srcX = aInitScroll;
			dstX = Math.min(Math.max(aDestScroll, 0), getScrollXLimit());
			srcY = scrollY;
			dstY = srcY;
		} else {

			srcX = scrollX;
			dstX = srcX;
			srcY = aInitScroll;
			dstY = Math.min(Math.max(aDestScroll, 0), getScrollYLimit());
		}

		scrollMotion = Motion.createSpline();
		scrollMotion.setDuration(SCROLL_MOTION_DURATION);
		smoothScroll(srcX, srcY, dstX, dstY);
	}

	public final void smoothScroll(int aInitScrollX, int aInitScrollY,
		int aDestScrollX, int aDestScrollY) {

		// check can scroll or not
		if (aInitScrollX == aDestScrollX && aInitScrollY == aDestScrollY
			|| !isScrollable()) {
			scrollMotion = null;
			return;
		}

		if (scrollMotion == null) {
			scrollMotion = Motion.createLinear();
			scrollMotion.setDuration(SCROLL_MOTION_DURATION);
		}

		scrollMotion.setSource(aInitScrollX, aInitScrollY);
		scrollMotion.setDestination(aDestScrollX, aDestScrollY);
		scrollMotion.start();
		setState(State.SCROLLING, true);
		startAnimation();
		Log.d(S_TAG, "Start ", scrollMotion);
	}

	final void guardScroll() {

		if (hasAttr(Attr.ALLOW_SCROLL_OUT_OF_RANGE)) {
			return;
		}

		int oldScrollX = scrollX;
		int oldScrollY = scrollY;

		if (!isScrollableX() || scrollX < 0) {

			scrollX = 0;
		} else if (scrollX > getScrollXLimit()) {

			scrollX = getScrollXLimit();
		}

		if (isScrollableY() && isVisible()) {

			if (scrollY < 0) {

				tensileScroll(scrollY, 0, Axis.Y_AXIS);
			} else if (scrollY > getScrollYLimit()) {

				tensileScroll(scrollY, getScrollYLimit(), Axis.Y_AXIS);
			}
		} else {

			if (!isScrollableY() || scrollY < 0) {

				scrollY = 0;
			} else if (scrollY > getScrollYLimit()) {

				scrollY = getScrollYLimit();
			}
		}

		if (oldScrollX != scrollX || oldScrollY != scrollY) {

			repaint(Reason.SCROLLED);
		}
	}

	/**
	 * Clear the scrolling relative states and the scrolling motion
	 */
	public final void stopSmoothScroll() {

		Log.d(S_TAG, "Stop ", scrollMotion);

		// clear states dragging and scrolling
		setState(State.SCROLLING, false);
		stopAnimation();
		scrollMotion = null;
	}

	/**
	 * Makes sure the component is visible in the scroll if this container is
	 * scrollable
	 * 
	 * @param aCmp the component that will be scrolling for visibility
	 */
	public void scrollComponentToVisible(Component aCmp) {

		if (aCmp != null && isScrollable()) {

			int x = 0;
			int y = 0;
			int width = aCmp.width;
			int height = aCmp.height;
			Component coordinate = aCmp;

			if (findFirstFocusable() == aCmp) {

				// special case for the first component to allow the user to
				// scroll all the way to the top
				width = aCmp.x + Math.min(aCmp.width, width);
				height = aCmp.y + Math.min(aCmp.height, height);

				coordinate = this;
			} else if (findLastFocusable() == aCmp) {

				// special case for the last component to allow the user to
				// scroll all the way to the bottom
				mapFrom(aCmp, 0, 0, gTempPoint);
				x = gTempPoint[0];
				y = gTempPoint[1];

				width = getPreferredWidth() - x;
				height = getPreferredHeight() - y;

				coordinate = this;
			}

			this.scrollRectToVisible(x, y, width, height, coordinate);
		}
	}

	/**
	 * Makes sure the component is visible in the scroll if this container is
	 * scrollable
	 * 
	 * @param aRect the rectangle that need to be visible
	 * @param aCoordinateCmp the component according to whose coordinates
	 *            rectangle is defined. Rectangle's x/y are relative to that
	 *            component (they are not absolute).
	 */
	public final void scrollRectToVisible(Rectangle aRect,
		Component aCoordinateCmp) {

		this.scrollRectToVisible(aRect.x, aRect.y, aRect.width, aRect.height,
			aCoordinateCmp);
	}

	/**
	 * Makes sure the component is visible in the scroll if this container is
	 * scrollable
	 * 
	 * @param aX x of the rectangle that need to be visible
	 * @param aY y of the rectangle that need to be visible
	 * @param aWidth width of the rectangle that need to be visible
	 * @param aHeight height of the rectangle that need to be visible
	 * @param aCoordinateCmp the component according to whose coordinates
	 *            rectangle is defined. Rectangle's x/y are relative to that
	 *            component (they are not absolute).
	 */
	public final void scrollRectToVisible(int aX, int aY, int aWidth,
		int aHeight, Component aCoordinateCmp) {

		if (isScrollable()) {

			mapFrom(aCoordinateCmp, aX, aY, gTempPoint);
			int relativeX = gTempPoint[0];
			int relativeY = gTempPoint[1];

			// the specified rectangle already inside the current viewport?
			if (Rectangle.contains(scrollX, scrollY, width, height, relativeX,
				relativeY, aWidth, aHeight)) {
				return;
			}

			int destScrollX = scrollX;
			int destScrollY = scrollY;

			// scroll x axis?
			if (isScrollableX()) {

				if (relativeX < scrollX) {

					destScrollX = relativeX;
				} else if (relativeX + aWidth > scrollX + width) {

					destScrollX = relativeX + aWidth - width;
				}
			}

			// scroll y axis?
			if (isScrollableY()) {

				if (relativeY < scrollY) {

					destScrollY = relativeY;
				} else if (relativeY + aHeight > scrollY + height) {

					destScrollY = relativeY + aHeight - height;
				}
			}

			smoothScroll(scrollX, scrollY, destScrollX, destScrollY);
		} else if (parent != null) {

			// try to move parent scroll if you are not scrollable
			getAbsolutePos(gTempPoint);
			int x = gTempPoint[0] + aX;
			int y = gTempPoint[1] + aY;

			parent.getAbsolutePos(gTempPoint);
			x -= gTempPoint[0];
			y -= gTempPoint[1];

			parent.scrollRectToVisible(x, y, aWidth, aHeight, aCoordinateCmp);
		}
	}

	/**
	 * Indicates the X position of the scrolling, this number is relative to the
	 * component position and so a position of 0 would indicate the x position
	 * of the component.
	 * 
	 * @return the X position of the scrolling
	 */
	public final int getScrollX() {

		return scrollX;
	}

	/**
	 * Indicates the Y position of the scrolling, this number is relative to the
	 * component position and so a position of 0 would indicate the x position
	 * of the component.
	 * 
	 * @return the Y position of the scrolling
	 */
	public final int getScrollY() {

		return scrollY;
	}

	public final int getAbsoluteScrollAreaX() {

		return getAbsoluteX() - scrollX;
	}

	public final int getAbsoluteScrollAreaY() {

		return getAbsoluteY() - scrollY;
	}

	public final int getScrollXLimit() {

		return getPreferredWidth() - width;
	}

	public final int getScrollYLimit() {

		return getPreferredHeight() - height;
	}

	public void setScrollbarClickableLength(int aSbClickableLen) {

	}

	public int getScrollbarClickableLength() {

		return hasAttr(Attr.SCROLLBAR_CLICKABLE) ? DEFAULT_SCROLLBAR_CLICKABLE_LEN
			: 0;
	}

	private int getScrollAxis(int aX, int aY) {

		boolean scrollableX = isScrollableX();
		boolean scrollableY = isScrollableY();

		if (scrollableX && scrollableY) {

			int deltaX = Math.abs(CanvasEx.getCurrPointerPressedX() - aX);
			int deltaY = Math.abs(CanvasEx.getCurrPointerPressedY() - aY);
			return deltaX > deltaY ? Axis.X_AXIS : Axis.Y_AXIS;
		} else if (scrollableX) {
			return Axis.X_AXIS;
		} else {
			return Axis.Y_AXIS;
		}
	}

	private int getTensileLength() {

		return hasAttr(Attr.TENSILE_DRAG_ENABLE) ? height / 3 : 0;
	}

	/****************************************************************/
	/*                                                              */
	/* Scroll Relative APIs End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* Container Relative APIs Begin */
	/*                                                              */
	/****************************************************************/
	/**
	 * Returns the count of children components.
	 * 
	 * @return the count children components
	 */
	public int getComponentCount() {

		return children != null ? children.size() : 0;
	}

	/**
	 * Is the index of child component valid.
	 * 
	 * @param aIndex index of child component
	 * @return true when the index is valid
	 */
	public boolean isComponentIndexValid(int aIndex) {

		return aIndex >= 0 && aIndex < getComponentCount();
	}

	/**
	 * Returns the child component at given index.
	 * 
	 * @param aIndex the index of the child component you wish to get
	 * @return child component at given index
	 * @throws ArrayIndexOutOfBoundsException if an invalid index was given.
	 */
	public Component getComponentAt(int aIndex) {

		return children != null ? (Component) children.elementAt(aIndex) : null;
	}

	/**
	 * Returns the first child component.
	 * 
	 * @return first child component
	 */
	public Component getFirstComponent() {

		return getComponentCount() > 0 ? getComponentAt(0) : null;
	}

	/**
	 * Returns the last child component.
	 * 
	 * @return the last child component
	 */
	public Component getLastComponent() {

		return getComponentCount() > 0 ? getComponentAt(getComponentCount() - 1)
			: null;
	}

	/**
	 * Returns the index of the given child component in the container.
	 * 
	 * @param aCmp the component to search for
	 * @return the child component index in the container or -1 if not found
	 */
	public int getComponentIndex(Component aCmp) {

		for (int i = getComponentCount(); i-- > 0;) {
			if (getComponentAt(i) == aCmp) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns true if the given component is within the hierarchy of this
	 * container.
	 * 
	 * @param aCmp a component to check
	 * @return true if this component contained in this container
	 */
	public boolean contains(Component aCmp) {

		for (Component p = aCmp; p != null; p = p.parent) {
			if (p == this) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds a component to the container.
	 * 
	 * @param aCmp the component to be added
	 */
	public void addComponent(Component aCmp) {

		this.addComponent(getComponentCount(), aCmp);
	}

	/**
	 * This method adds a component at a specific index location in the
	 * container children array.
	 * 
	 * @param aIndex location to insert the component
	 * @param aCmp the component to add
	 * @throws ArrayIndexOutOfBoundsException if index is out of bounds
	 * @throws IllegalArgumentException if component is already contained
	 */
	public void addComponent(int aIndex, Component aCmp) {

		Assert.assertNotNull(aCmp);
		Assert.assertNull(aCmp.parent, Assert.ARG);

		if (children == null) {

			children = new Vector();
		}

		// insert
		aCmp.setParent(this);
		children.insertElementAt(aCmp, aIndex);

		// initialize child when itself is initialized
		if (isInitialized()) {

			aCmp.initialize();
		}

		// recalculate preferred size
		setShouldCalcPreferredSize(true);

		// repaint
		repaintRegionUnderChild(aCmp, Reason.CHILD_ADD);
	}

	/**
	 * Remove all components from container.
	 */
	public void removeAll() {

		for (int i = getComponentCount(); i-- > 0;) {

			removeComponent(getComponentAt(i));
		}
	}

	/**
	 * Remove a component from the container.
	 * 
	 * @param aCmp the removed component
	 */
	public void removeComponent(Component aCmp) {

		if (aCmp == null || children == null) {
			return;
		}

		// remove
		aCmp.deinitialize();
		aCmp.setParent(null);
		children.removeElement(aCmp);

		// recalculate preferred size
		setShouldCalcPreferredSize(true);
		aCmp.setShouldCalcPreferredSize(true);

		Window win = getWindow();
		if (win != null) {

			// is the removed component contains the focused one?
			if (aCmp.contains(win.getFocused())) {

				win.setCurrentFocused(null);
				win.initFocused();
			}

			// stop animation
			if (aCmp.isScrolling()) {

				win.deregisterAnimated(aCmp);
			}
		}

		// repaint
		if (isVisible()) {

			repaintRegionUnderChild(aCmp, Reason.CHILD_REMOVE);
		} else if (win != null) {

			win.repaintRegionUnderChild(this, Reason.CHILD_REMOVE);
		}
	}

	/**
	 * Performs the layout of the container if a layout is necessary.
	 */
	public void layout() {

		for (int i = getComponentCount(); i-- > 0;) {
			getComponentAt(i).layout();
		}
	}

	/**
	 * Finds the first focusable component on this container, traversal in
	 * pre-order.
	 * 
	 * @return a focusable Component or null if not exists;
	 */
	public Component findFirstFocusable() {

		return findFocusable(0, getComponentCount(), 1, FIND_FIRST);
	}

	/**
	 * Finds the last focusable component on this container, traversal in
	 * pre-order.
	 * 
	 * @return a focusable Component or null if not exists;
	 */
	public Component findLastFocusable() {

		return findFocusable(getComponentCount() - 1, -1, -1, FIND_LAST);
	}

	protected Component findNextFocusable(int aDirection, boolean aCircle) {

		boolean forward = aDirection == Direction.RIGHT
			|| aDirection == Direction.DOWN;

		boolean first = FIND_FIRST;

		Component child = this;
		Component parent = this.parent;
		Component focusable = null;
		Component circleFocusable = null;

		int step = forward ? 1 : -1;
		int idx = 0;
		int count = 0;

		for (; parent != null; child = parent, parent = parent.parent, first = forward) {

			idx = parent.getComponentIndex(child);
			count = parent.getComponentCount();

			// standard find
			focusable = forward ? parent.findFocusable(idx + 1, count, step,
				first) : parent.findFocusable(idx - 1, -1, step, first);

			if (focusable != null) {
				return focusable;
			}

			// circle find
			if (aCircle) {

				focusable = forward ? parent.findFocusable(0, idx, step, first)
					: parent.findFocusable(count - 1, idx, step, first);

				if (focusable != null) {
					circleFocusable = focusable;
				} else if (child.isFocusable()) {
					circleFocusable = child;
				}
			}
		}

		return circleFocusable;
	}

	private Component findFocusable(int aStart, int aEnd, int aStep,
		boolean aFirst) {

		Component cmp = null;
		Component focusable = null;

		for (int i = aStart; i != aEnd; i += aStep) {

			cmp = getComponentAt(i);
			if (cmp.isFocusable()) {
				return cmp;
			} else if (cmp.isVisible() && cmp.isEnabled()) {
				focusable = aFirst ? cmp.findFirstFocusable()
					: cmp.findLastFocusable();
				if (focusable != null) {
					return focusable;
				}
			}
		}

		return null;
	}

	/****************************************************************/
	/*                                                              */
	/* Container Relative APIs End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* Repaint/Paint/Animation Relative APIs Begin */
	/*                                                              */
	/****************************************************************/

	/**
	 * Clip the component's dirty region on given GraphicsEx.
	 * 
	 * @param aG given GraphicsEx
	 */
	public final void clipDirtyRegion(GraphicsEx aG) {

		aG.setClip(dirtyRegion);

		// reset dirty region
		dirtyRegion = Rectangle.EMPTY;
	}

	/**
	 * Unite the new dirty region with the component's current dirty region, if
	 * the component has no dirty region yet, will use this as current dirty
	 * region.
	 * 
	 * <p>
	 * The area outside the dirty region will be clipped in next repaint.
	 */
	private synchronized final void uniteDirtyRegion(int aDirtyX, int aDirtyY,
		int aDirtyW, int aDirtyH) {

		if (aDirtyW <= 0 || aDirtyH <= 0) {
			return;
		}

		if (dirtyRegion.isValid()) {

			// united two difference rectangle to get a new dirtyRegion
			dirtyRegion = dirtyRegion.united(aDirtyX, aDirtyY, aDirtyW, aDirtyH);
		} else {

			setDirtyRegion(new Rectangle(aDirtyX, aDirtyY, aDirtyW, aDirtyH));
		}
	}

	/**
	 * Set a new dirty region.
	 * 
	 * <p>
	 * The area outside the dirty region will be clipped in next repaint.
	 */
	private synchronized final void setDirtyRegion(Rectangle aDirty) {

		dirtyRegion = aDirty != null ? aDirty : Rectangle.EMPTY;
	}

	/**
	 * Set the repaint reason flag.
	 * 
	 * <p>
	 * <strong>This method only used for debug only.</strong>
	 * 
	 * @param aReason the repaint reason in {@link Reason}
	 */
	private final void setRepaintReason(int aReason) {

		states = states & 0x00FFFFFF | aReason << 24;
	}

	/**
	 * Get the repaint reason flag.
	 * 
	 * <p>
	 * <strong>This method only used for debug only.</strong>
	 * 
	 * @return the repaint reason in {@link Reason}
	 */
	private final int getRepaintReason() {

		return states >>> 24;
	}

	/**
	 * Get the repaint reason description.
	 * 
	 * <p>
	 * <strong>This method only used for debug only.</strong>
	 * 
	 * @return repaint reason description
	 */
	private final String repaintReason() {

		switch (getRepaintReason()) {

		case Reason.NONE:
			return "NONE";

		case Reason.REPAINT_CANVAS:
			return "REPAINT_CANVAS";

		case Reason.UPDATE:
			return "UPDATE";
		case Reason.ENABLE:
			return "ENABLE";
		case Reason.DISABLE:
			return "DISABLE";
		case Reason.FOCUS_GAINED:
			return "FOCUS_GAINED";
		case Reason.FOCUS_LOST:
			return "FOCUS_LOST";
		case Reason.PRESS_ON:
			return "PRESS_ON";
		case Reason.PRESS_OFF:
			return "PRESS_OFF";
		case Reason.ACTIVE_ON:
			return "ACTIVE_ON";
		case Reason.ACTIVE_OFF:
			return "ACTIVE_OFF";
		case Reason.SELECT:
			return "SELECT";
		case Reason.UNSELECT:
			return "UNSELECT";
		case Reason.DRAGGING:
			return "DRAGGING";
		case Reason.SCROLLED:
			return "SCROLLED";
		case Reason.SCROLLING:
			return "SCROLLING";
		case Reason.ANIMATING:
			return "ANIMATING";
		case Reason.PROGRESS:
			return "PROGRESS";
		case Reason.OPAQUE_CHANGED:
			return "OPAQUE_CHANGED";
		case Reason.STYLE_CHANGED:
			return "STYLE_CHANGED";

		case Reason.RELAYOUT:
			return "RELAYOUT";
		case Reason.CHILD_ADD:
			return "CHILD_ADD";
		case Reason.CHILD_REMOVE:
			return "CHILD_REMOVE";
		case Reason.CHILD_REPLACE:
			return "CHILD_REPLACE";
		case Reason.CHILD_TRANSITION:
			return "CHILD_TRANSITION";
		case Reason.CHILD_BOUNDS_CHANGED:
			return "CHILD_BOUNDS_CHANGED";

		case Reason.SWITCH_VIEW:
			return "SWITCH_VIEW";
		case Reason.CLOSE_VIEW:
			return "CLOSE_VIEW";

		default:
			return "UNKNOWN";
		}
	}

	/**
	 * Repaint this Component, the repaint call causes a callback of the paint
	 * method on the paint thread.
	 * 
	 * @param aReason the repaint reason of {@link Reason}
	 */
	public void repaint(int aReason) {

		dirtyRegion = Rectangle.EMPTY;// reset dirty region

		this.repaint(this, aReason);
	}

	/**
	 * Repaints a specific region within the component
	 * 
	 * @param aDirtyX boundary of the region to repaint
	 * @param aDirtyY boundary of the region to repaint
	 * @param aDirtyW boundary of the region to repaint
	 * @param aDirtyH boundary of the region to repaint
	 * @param aReason the repaint reason of {@link Reason}
	 */
	public final void repaint(int aDirtyX, int aDirtyY, int aDirtyW,
		int aDirtyH, int aReason) {

		// if already request repaint, and it need full region repaint,
		// do not unite the dirty region
		if (getRepaintReason() == Reason.NONE || dirtyRegion.isValid()) {
			uniteDirtyRegion(aDirtyX, aDirtyY, aDirtyW, aDirtyH);
		}

		this.repaint(this, aReason);
	}

	/**
	 * Repaints a specific region within the component
	 * 
	 * @param aDirty bounds of the region to repaint
	 * @param aReason the repaint reason of {@link Reason}
	 */
	public final void repaint(Rectangle aDirty, int aReason) {

		if (aDirty != null && aDirty.isValid()) {

			uniteDirtyRegion(aDirty.x, aDirty.y, aDirty.width, aDirty.height);
		}

		this.repaint(this, aReason);
	}

	/**
	 * Repaint the region occupy by child component.
	 * 
	 * @param aChild child component
	 * @param aReason the repaint reason of {@link Reason}
	 */
	final void repaintRegionUnderChild(Component aChild, int aReason) {

		aChild.getAbsoluteBounds(gTempRect);

		this.repaint(gTempRect[0], gTempRect[1], gTempRect[2], gTempRect[3],
			aReason);
	}

	/**
	 * Repaint the given component to the screen
	 * 
	 * @param aCmp the given component on the screen
	 * @param aReason the repaint reason of {@link Reason}
	 */
	final void repaint(Component aCmp, int aReason) {

		Assert.assertNotNull(aCmp);
		Assert.assertNotEquals(aReason, Reason.NONE, Assert.ARG);

		Window win = getWindow();
		if (win == null) {
			return;
		}

		// check component and its parent's visibility
		for (Component c = aCmp; c != win; c = c.parent) {

			if (c == null || !c.isVisible()) {
				return;
			}
		}

		// reach to window
		aCmp.setRepaintReason(aReason);
		win.repaint(aCmp);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean animate() {

		boolean animating = false;
		// perform the scroll motion if exists
		if (scrollMotion != null) {
			animating |= scrollAnimate();
		}

		// perform two components transition animation
		if (gTransInCmp != null && gTransOutCmp != null) {
			animating |= transitionAnimate();
		}

		setState(State.ANIMATING, animating);
		return animating;
	}

	private boolean transitionAnimate() {

		// children transition animation
		boolean finished = gOutMotion.isFinished() && gInMotion.isFinished();

		if (!finished) {

			int outX = gOutMotion.getValueX();
			int outY = gOutMotion.getValueY();
			int inX = gInMotion.getValueX();
			int inY = gInMotion.getValueY();

			Log.d(Motion.TAG, "outX : ", Log.toString(outX), ", outY : ",
				Log.toString(outY), ", inX : ", Log.toString(inX), ", inY : ",
				Log.toString(inY));

			gTransOutCmp.setBoundsInternal(outX, outY, gTransOutCmp.width,
				gTransOutCmp.height);
			gTransInCmp.setBoundsInternal(inX, inY, gTransInCmp.width,
				gTransInCmp.height);
			gTransInCmp.setVisible(true);

			setRepaintReason(Reason.ANIMATING);
		} else {

			// finished
			gTransOutCmp.setBoundsInternal(gOutMotion.getSourceX(),
				gOutMotion.getSourceY(), gTransOutCmp.width,
				gTransOutCmp.height);
			gTransInCmp.setBoundsInternal(gInMotion.getDestinationX(),
				gInMotion.getDestinationY(), gTransInCmp.width,
				gTransInCmp.height);
			gTransOutCmp.setVisible(false);

			repaint(Reason.CHILD_TRANSITION);
			// clear
			gTransInCmp = gTransOutCmp = null;
		}

		return !finished;
	}

	private boolean scrollAnimate() {

		// change the variable directly for efficiency both in removing
		// redundant
		// repaints and scroll checks
		int dragValX = scrollMotion.getValueX();
		int dragValY = scrollMotion.getValueY();
		boolean scrollableX = isScrollableX();
		boolean scrollableY = isScrollableY();

		if (!(scrollableX || scrollableY)) {
			stopSmoothScroll();
			return false;
		}

		if (scrollableX) {
			scrollX = dragValX;
		}

		if (scrollableY) {
			scrollY = dragValY;
		}

		if (scrollMotion.isFinished()) {
			// stop first
			stopSmoothScroll();
			// guard scroll, scroll back when out of range
			guardScroll();
		}

		return true;
	}

	protected final void replaceWithTransition(Component aTransOutCmp,
		Component aTransInCmp, int aOutDstX, int aOutDstY, int aInSrcX,
		int aInSrcY, int aInDstX, int aInDstY) {

		Assert.assertNotNull(aTransOutCmp);
		Assert.assertNotNull(aTransInCmp);

		gTransInCmp = aTransInCmp;
		gTransOutCmp = aTransOutCmp;

		gOutMotion.setSource(aTransOutCmp.x, aTransOutCmp.y);
		gOutMotion.setDestination(aOutDstX, aOutDstY);
		gInMotion.setSource(aInSrcX, aInSrcY);
		gInMotion.setDestination(aInDstX, aInDstY);

		gOutMotion.start();
		gInMotion.start();

		CanvasEx.executeTransition(this);
	}

	public void startAnimation() {

		Window win = getWindow();

		if (win != null) {
			win.registerAnimated(this);
		}
	}

	public void stopAnimation() {

		Window win = getWindow();

		if (win == null) {
			win = CanvasEx.getCurrWindow();
		}

		if (win != null) {
			win.deregisterAnimated(this);
		}
	}

	public ImageEx getSnapshot() {

		ImageEx snapshot = ImageEx.createImage(width, height);
		GraphicsEx g = snapshot.getGraphics();

		paintComponent(g, DONT_PAINT_PARENTS_BACKGROUND);

		return snapshot;
	}

	/**
	 * Paint the whole component on the screen, it will rendering the affected
	 * branches of the components tree.
	 * 
	 * @see #paintComponent(GraphicsEx, boolean)
	 */
	public void paint(GraphicsEx aG) {

		clipDirtyRegion(aG);
		if (isInitialized()) {
			paintComponent(aG, PAINT_PARENTS_BACKGROUND);
		} else {
			Log.w("Paint not initialized component ", this);
		}
		setRepaintReason(Reason.NONE);
	}

	/**
	 * Paints this component as a root by going to all the parent components and
	 * setting the absolute translation based on coordinates and scroll status.
	 * Restores translation when the painting is finished.
	 * 
	 * @param aG the graphics to paint this Component on
	 * @param aPaintBg if true paints all parents background
	 */
	public final void paintComponent(GraphicsEx aG, boolean aPaintBg) {

		// save clip, translation and draw hints
		aG.save(GraphicsEx.SAVE_CLIP | GraphicsEx.SAVE_DRAW_HINTS);
		// use low quality when scrolling and dragging
		boolean highQuality = !(hasBackground() && (isScrolling() || isDragging()));
		aG.setDrawHint(DrawHints.HIGHT_QUALITY, highQuality);

		// since scrollability can translate everything... we should clip
		// based on the current scroll
		Component parent = this.parent;
		for (; parent != null; parent = parent.parent) {

			Log.d(P_TAG, "Before clip : ", aG.getClipRect(), ", by ", parent);
			parent.getAbsoluteBounds(gTempRect);
			aG.clipRect(gTempRect[0], gTempRect[1], gTempRect[2], gTempRect[3]);
			Log.d(P_TAG, "After clip : ", aG.getClipRect(), ", by ", parent);
		}

		getAbsoluteBounds(gTempRect);
		int absX = gTempRect[0];
		int absY = gTempRect[1];
		aG.clipRect(gTempRect[0], gTempRect[1], gTempRect[2], gTempRect[3]);
		Log.d(P_TAG, "After clip : ", aG.getClipRect(), ", by ", this);

		if (aPaintBg && !isOpaque()) {
			paintParentBackgrounds(aG);
		}

		aG.translate(absX - x, absY - y);
		// paint itself, the graphics' translation is its parent's absolute x, y
		paintComponentInternal(aG, PAINT_INTERSECTING_SIBLINGS);
		// restore
		aG.restore();
	}

	/**
	 * This method performs the paint of the component internally including
	 * drawing the scrollbars and scrolling the component. This functionality is
	 * hidden from developers to prevent errors
	 * 
	 * @param aG the component graphics
	 * @param aPaintIntersects paint intersecting siblings or not
	 */

	final void paintComponentInternal(GraphicsEx aG, boolean aPaintIntersects) {

		if (!isVisible()) {
			return;
		}

		boolean intersects = Rectangle.intersects(x, y, width, height,
			aG.getClipX(), aG.getClipY(), aG.getClipWidth(), aG.getClipHeight());

		if (intersects) {

			aG.save(GraphicsEx.SAVE_CLIP);
			aG.clipRect(x, y, width, height);

			if (hasBackground()) {
				paintBackground(aG);// paint background!!!
			}

			if (isScrollable()) {

				aG.save(GraphicsEx.SAVE_TRANSLATION);
				aG.translate(-scrollX, -scrollY);
				paintContent(aG);// paint itself!!!
				aG.restore();

				if (hasAttr(Attr.SCROLLBAR_VISIBLE)) {
					paintScrollbars(aG);
				}
			} else {
				paintContent(aG);// paint itself!!!
			}

			// paint all the intersecting components above the Component
			if (aPaintIntersects && parent != null) {
				paintIntersectingComponentsAbove(aG);
			}

			aG.restore();
		}
	}

	/**
	 * Paints the UI for the scrollbars on the component, this will be invoked
	 * only for scrollable components. This method invokes the appropriate X/Y
	 * versions to do all the work.
	 * 
	 * @param aG the component graphics
	 */
	protected void paintScrollbars(GraphicsEx aG) {

		if (isScrollableX()) {
			paintScrollbarX(aG);
		}

		if (isScrollableY()) {
			paintScrollbarY(aG);
		}
	}

	/**
	 * Paints the UI for the scrollbar on the X axis, this method allows
	 * component subclasses to customize the look of a scrollbar
	 * 
	 * @param aG the component graphics
	 */
	protected void paintScrollbarX(GraphicsEx aG) {

		int scrollW = getPreferredWidth();
		int block = width * width / scrollW;
		int offset;

		if (scrollX + width == scrollW) {
			// normalize the offset to avoid rounding errors to
			// the bottom of the screen
			offset = width - block;
		} else {
			offset = (scrollX + width) * width / scrollW - block;
		}

		aG.save(GraphicsEx.SAVE_TRANSLATION | GraphicsEx.SAVE_COLOR);

		aG.translate(x, y);
		aG.setColor(Color.GREENYELLOW);

		aG.drawLine(height - 4, offset, height - 4, offset + block);
		aG.drawLine(height - 3, offset, height - 3, offset + block);
		aG.drawLine(height - 2, offset, height - 2, offset + block);
		aG.drawLine(height - 1, offset, height - 1, offset + block);

		aG.restore();
	}

	/**
	 * Paints the UI for the scrollbar on the Y axis, this method allows
	 * component subclasses to customize the look of a scrollbar
	 * 
	 * @param aG the component graphics
	 */
	protected void paintScrollbarY(GraphicsEx aG) {

		int scrollH = getPreferredHeight();
		int block = height * height / scrollH;
		int offset;

		if (scrollY + height == scrollH) {
			// normalize the offset to avoid rounding errors to
			// the bottom of the screen
			offset = height - block;
		} else {
			offset = (scrollY + height) * height / scrollH - block;
		}

		aG.save(GraphicsEx.SAVE_TRANSLATION | GraphicsEx.SAVE_COLOR);

		aG.translate(x, y);
		aG.setColor(Color.GREENYELLOW);

		aG.drawLine(width - 4, offset, width - 4, offset + block);
		aG.drawLine(width - 3, offset, width - 3, offset + block);
		aG.drawLine(width - 2, offset, width - 2, offset + block);
		aG.drawLine(width - 1, offset, width - 1, offset + block);

		aG.restore();
	}

	/**
	 * Paints content of the component, invoked with the clipping region and
	 * appropriate scroll translation.
	 * 
	 * @param aG the component graphics
	 */
	protected void paintContent(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aG.translate(x, y);
		for (int i = 0, count = getComponentCount(); i < count; ++i) {
			getComponentAt(i).paintComponentInternal(aG,
				DONT_PAINT_INTERSECTING_SIBLINGS);
		}
		aG.restore();
	}

	/**
	 * Paints the background of the component, invoked with the clipping region
	 * and appropriate scroll translation.
	 * 
	 * <p>
	 * Also draws the component border if such a border exists. The border
	 * unlike the content of the component will not be affected by scrolling for
	 * a scrollable component.
	 * 
	 * @param aG the component graphics
	 */
	protected void paintBackground(GraphicsEx aG) {

		Log.d(P_TAG, "Background clip : ", aG.getClipRect());

		aG.setBrush(Brush.WHITE_BRUSH);
		aG.fillRectEx(x, y, width, height);
	}

	/**
	 * This method paints all the parents Components Background.
	 * 
	 * @param aG the graphics object
	 */
	private void paintParentBackgrounds(GraphicsEx aG) {

		getAbsoluteBounds(gTempRect);
		paintParentBackground(aG, parent, this, gTempRect[0], gTempRect[1],
			gTempRect[2], gTempRect[3]);
	}

	private void paintParentBackground(GraphicsEx aG, Component aParent,
		Component aChild, int aAbsX, int aAbsY, int aAbsW, int aAbsH) {

		if (aParent == null) {
			return;
		}

		// call recursive for parent's parent when its background is not opaque
		if (!aParent.isOpaque()) {
			paintParentBackground(aG, aParent.parent, aParent, aAbsX, aAbsY,
				aAbsW, aAbsH);
		}

		if (!aParent.isVisible()) {
			return;
		}

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aParent.getAbsolutePos(gTempPoint);
		aG.translate(gTempPoint[0], gTempPoint[1]);

		// paint parent background
		if (aParent.hasAttr(Attr.HAS_BACKGROUND)) {

			aG.save(GraphicsEx.SAVE_TRANSLATION);
			aG.translate(-aParent.x, -aParent.y);
			aParent.paintBackground(aG);
			aG.restore();
		}

		// paint intersecting siblings
		aParent.paintIntersectingComponents(aG, aChild, aAbsX, aAbsY, aAbsW,
			aAbsH, PAINT_BELOW_INTERSECTING_SIBLINGS);
		aG.restore();
	}

	private void paintIntersectingComponentsAbove(GraphicsEx aG) {

		aG.save(GraphicsEx.SAVE_TRANSLATION);
		aG.resetTranslation();
		for (Component p = parent, c = this; p != null; c = p, p = p.parent) {

			aG.save(GraphicsEx.SAVE_TRANSLATION);

			p.getAbsolutePos(gTempPoint);
			aG.translate(gTempPoint[0], gTempPoint[1]);

			getAbsoluteBounds(gTempRect);
			p.paintIntersectingComponents(aG, c, gTempRect[0], gTempRect[1],
				gTempRect[2], gTempRect[3], PAINT_ABOVE_INTERSECTING_SIBLINGS);

			aG.restore();// restore translation
		}

		aG.restore();// restore translation
	}

	private void paintIntersectingComponents(GraphicsEx aG, Component aChild,
		int aAbsX, int aAbsY, int aAbsW, int aAbsH, boolean aAbove) {

		if (isChildrenOverlap() && contains(aChild)) {

			int indexOfComponent = getComponentIndex(aChild);
			int startIndex = 0, endIndex = 0;

			if (aAbove) {
				startIndex = indexOfComponent + 1;
				endIndex = getComponentCount();
			} else {
				startIndex = 0;
				endIndex = indexOfComponent;
			}

			Component sibling = null;
			for (int i = startIndex; i < endIndex; ++i) {

				sibling = getComponentAt(i);
				sibling.getAbsoluteBounds(gTempRect);
				boolean intersects = Rectangle.intersects(aAbsX, aAbsY, aAbsW,
					aAbsH, gTempRect[0], gTempRect[1], gTempRect[2],
					gTempRect[3]);

				if (intersects) {
					sibling.paintComponentInternal(aG,
						DONT_PAINT_INTERSECTING_SIBLINGS);
				}
			}// for
		}
	}

	/****************************************************************/
	/*                                                              */
	/* Repaint/Paint/Animation Relative APIs End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* Event Handler Relative APIs Begin */
	/*                                                              */
	/****************************************************************/

	public void orentationChanged(int aNewOrentation) {

		for (int i = getComponentCount(); i-- > 0;) {
			getComponentAt(i).orentationChanged(aNewOrentation);
		}
	}

	/**
	 * When Component do not handle the given event, it will try to forward to
	 * its parent.
	 */
	public boolean event(Event aEv) {

		// internal handling first
		switch (aEv.type) {

		case EventType.POINTER_DRAGGED:
			pointerDraggedInternal(aEv);
			break;
		case EventType.POINTER_RELEASED:
			pointerReleasedInternal(aEv);
			break;
		case EventType.TIMER:
			timerEventInternal(aEv);
			break;
		}

		// then forward to its subclass or parent
		if (super.event(aEv)) {
			return true;
		} 
		
		if (parent != null && !(parent instanceof Window)) {
			return parent.event(aEv);
		} 
		
		return false;
	}

	private void pointerDraggedInternal(Event aPtEv) {

		if (isScrollable()) {

			Window win = getWindow();
			Event lastDrag = null;

			if (!isDragging()) {

				// set in dragging state
				setState(State.DRAGGING, true);
				// need to set as current dragged component to accept release
				// event
				win.setCurrentDragged(this);
				lastDrag = aPtEv;
			} else {
				lastDrag = CanvasEx.getLastPointerEvent();
			}

			// use a guard timer to prevent the release event maybe missed
			// and the scroll position is not in range
			Rectangle detect = win.getCentralArea();
			if (!detect.containsExcludeBoundary(aPtEv.getX(), aPtEv.getY())) {
				if (gGuardDragTimer < 0) {
					gGuardDragTimer = CanvasEx.findAvailableTimer();
				}
				CanvasEx.startTimer(gGuardDragTimer, this, GUARD_TIMER_DELAY);
			} else {
				CanvasEx.closeTimer(gGuardDragTimer);
			}

			// we drag inversely to get a feel of grabbing a physical screen
			// and pulling it in the reverse direction of the drag
			if (isScrollableY()) {
				setScrollY(scrollY + lastDrag.getY() - aPtEv.getY());
			}

			if (isScrollableX()) {
				setScrollX(scrollX + lastDrag.getX() - aPtEv.getX());
			}

			aPtEv.accept();
		}
	}

	private void pointerReleasedInternal(Event aPtEv) {

		if (isDragging()) {

			clearDrag();

			int axis = getScrollAxis(aPtEv.getX(), aPtEv.getY());
			int scroll = axis == Axis.X_AXIS ? scrollX : scrollY;
			int limit = axis == Axis.X_AXIS ? getScrollXLimit()
				: getScrollYLimit();

			if (scroll < 0) {
				tensileScroll(scroll, 0, axis);
			} else if (scroll > limit) {
				tensileScroll(scroll, limit, axis);
			} else {
				int speed = CanvasEx.getDragSpeed(axis);
				if (Math.abs(speed) > 1000) {
					frictionScroll(speed, SCROLL_MOTION_FRICTION, axis);
				}
			}

			aPtEv.accept();
		} else {// just click, no dragging...

			mapFromAbsolute(aPtEv.getX(), aPtEv.getY(), gTempPoint);
			int x = gTempPoint[0] - scrollX;
			int y = gTempPoint[1] - scrollY;

			// scroll x axis to the click point directly?
			if (isScrollableX() && height - y < getScrollbarClickableLength()) {

				int preferred = getPreferredWidth();
				int scrollLimit = getScrollXLimit();
				int block = width * width / preferred;

				int dst = x * scrollLimit / width;
				dst = x < block ? 0 : width - x < block ? scrollLimit : dst;

				this.scrollRectToVisible(dst, y, width, height, this);
				aPtEv.accept();
			}

			// scroll y axis to the click point directly?
			if (isScrollableY() && width - x < getScrollbarClickableLength()) {

				int preferred = getPreferredHeight();
				int scrollLimit = getScrollYLimit();
				int block = height * height / preferred;

				int dst = y * scrollLimit / height;
				dst = y < block ? 0 : height - y < block ? scrollLimit : dst;

				this.scrollRectToVisible(x, dst, width, height, this);
				aPtEv.accept();
			}
		}
	}

	private void timerEventInternal(Event aTimerEv) {

		if (aTimerEv.getTimerId() == gGuardDragTimer) {

			guardScroll();
			if (isDragging()) {
				clearDrag();
			}
			aTimerEv.accept();
		}
	}

	protected void clearDrag() {

		setState(State.DRAGGING, false);
		this.repaint(Reason.UPDATE);

		CanvasEx.closeTimer(gGuardDragTimer);
		gGuardDragTimer = CanvasEx.INVALID_TIMER_INDEX;
	}

	public void dump(String aPrefix) {

		if (!DEBUG) {
			return;
		}

		System.out.println(aPrefix + this);
		for (int i = 0, count = getComponentCount(); i < count; ++i) {
			getComponentAt(i).dump(aPrefix + "#" + (i + 1) + "\t");
		}
	}

	public String toString() {

		if (!DEBUG) {
			return super.toString();
		}

		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append('.').append(getClazz());
		sb.append('#').append(getId());
		sb.append(" [");
		if (getRepaintReason() != Reason.NONE) {
			sb.append("reason=");
			sb.append(repaintReason());
		}
		sb.append(", bounds=");
		Rectangle bounds = new Rectangle(x, y, width, height);
		sb.append(bounds);
		if (dirtyRegion.isValid()) {
			sb.append(", dirty=");
			sb.append(Log.toString(dirtyRegion));
		}
		sb.append(", attributes=");
		sb.append(Log.toBinaryString(attributes));
		sb.append(", states=");
		sb.append(Log.toBinaryString(states));
		sb.append("]");

		return sb.toString();
	}
}
