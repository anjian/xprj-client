/**
 * Tiny.cn.uc.ui.ex.CanvasEx.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

import cn.uc.tiny.Component;
import cn.uc.tiny.Component.Reason;
import cn.uc.tiny.Window;
import cn.uc.tiny.animations.Animation;
import cn.uc.tiny.ex.Event.EventType;
import cn.uc.tiny.geom.Rectangle;
import cn.uc.util.BitUtils;
import cn.uc.util.CollectionUtils;
import cn.uc.util.Platform;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * Extension of {@link Canvas}, manage events queue, dispatch events and manage
 * windows.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class CanvasEx extends Canvas implements Runnable {

	// log TAG
	public static final String C_TAG = "Canvas";
	public static final String E_TAG = "Event";
	public static final String T_TAG = "Timer";
	public static final String P_TAG = "Paint";
	public static final String W_TAG = "WinMgr";

	public static final int LEFT_SOFT_KEY = -6;
	public static final int RIGHT_SOFT_KEY = -7;
	public static final int SEND_KEY = -10;
	public static final int CLEAR_KEY = -8;
	public static final int SELECT_KEY = -5;

	public static final int INVALID_TIMER_INDEX = -1;

	private static final int KEY_MAY_GEN_BY_OS = 0;
	private static final int KEY_MAY_GEN_BY_SELF = 1;
	private static final int KEY_GEN_BY_OS = 2;
	private static final int KEY_GEN_BY_SELF = 3;

	private static final int KEY_REPEATED_FIRST_INTERVAL = 2000;
	private static final int KEY_REPEATED_CONTINUE_INTERVAL = 100;
	private static final int KEY_REPEATED_GUARD_INTERVAL = 4000;

	private static final int KEY_RELEASED_NORMAL_INTERVAL = 200;
	private static final int KEY_RELEASED_GUARD_INTERVAL = 2000;

	private static final int LONG_PRESSED_INTERVAL = 1000;
	private static final int LONG_PRESSED_GUARD_INTERVAL = 2000;

	private static final int MAX_EVENT_LOOP_IDLE_TIME = 30000;

	/**
	 * number representing a minimum number of motion events to start a drag
	 * operation
	 */
	private static final int DRAG_AUTO_ACTIVATION_THRESHOLD = 7;

	/** Max drag speed. */
	private static final int MAX_DRAG_SPEED = 20000;

	/**
	 * {Enum} Direction, contains four directions.
	 * 
	 * <p>
	 * UP/LEFT/RIGHT/DOWN and NORTH/WEST/EAST/SOUTH are just the different names
	 * for the same meaning.
	 * </p>
	 * 
	 * <p>
	 * <strong>The value of UP/LEFT/RIGHT/DOWN are guarantee to be same with the
	 * ones in {@link Canvas}.</strong>
	 * </p>
	 */
	public static class Direction {

		public static final int UP = Canvas.UP;
		public static final int LEFT = Canvas.LEFT;
		public static final int RIGHT = Canvas.RIGHT;
		public static final int DOWN = Canvas.DOWN;

		public static final int NORTH = UP;
		public static final int WEST = LEFT;
		public static final int EAST = RIGHT;
		public static final int SOUTH = DOWN;
	}

	/**
	 * {Enum} Area, contains edges, corners and center area.
	 */
	public static class Area {

		public static final int TOP_EDGE = 0;
		public static final int LEFT_EDGE = 1;
		public static final int RIGHT_EDGE = 2;
		public static final int BOTTOM_EDGE = 3;

		public static final int TOP_LEFT_CORNER = 4;
		public static final int TOP_RIGHT_CORNER = 5;
		public static final int BOTTOM_LEFT_CORNER = 6;
		public static final int BOTTOM_RIGHT_CORNER = 7;

		public static final int CENTER = 8;
	}

	public static class Orientation {

		public static final int HORIZONTAL = 0;
		public static final int VERTICAL = 1;
	}

	public static class Axis {

		public static final int X_AXIS = 0;
		public static final int Y_AXIS = 1;
		public static final int Z_AXIS = 2;
	}

	public static class Rotation {

		public static final int ROTATE_0 = Sprite.TRANS_NONE;
		public static final int ROTATE_90 = Sprite.TRANS_ROT90;
		public static final int ROTATE_180 = Sprite.TRANS_ROT180;
		public static final int ROTATE_270 = Sprite.TRANS_ROT270;
	}

	/**
	 * Global {@link CanvasEx} instance
	 */
	public static final CanvasEx gCanvas = new CanvasEx();

	/**
	 * wrapper of Graphics from paint()
	 */
	private static GraphicsEx gGraphics;

	/** OffScreen image buffer */
	private static Image gOffScreen;

	private static int gOrgLeftSoftKeyCode = 0;
	private static int gOrgRightSoftKeyCode = 0;
	private static int gOrgSelectKeyCode = 0;
	private static int gOrgSendKeyCode = 0;
	private static int gOrgClearKeyCode = 0;

	private static int gDragActivationCounter = 0;
	private static int gDragThreshold = 8;
	private static int gDragStartPercentage = 3;
	private static int gDragActivationX = 0;
	private static int gDragActivationY = 0;

	private static int gPointerPressedX = 0;
	private static int gPointerPressedY = 0;
	private static int gPointerPressedTime = 0;
	private static int gPointerLongPressedTime = 0;

	/**
	 * Is the key repeated event generated by under OS or need to simulate by
	 * itself. Assume the value is <code>KEY_MAY_GEN_BY_OS</code> or
	 * <code>KEY_MAY_GEN_BY_SELF</code> first, but when get the key repeated
	 * event from OS the first time, this value will be set as
	 * <code>KEY_GEN_BY_OS</code> once for all.
	 */
	private static int gKeyRepeatedBy = gCanvas.hasRepeatEvents() ? KEY_MAY_GEN_BY_OS
		: KEY_MAY_GEN_BY_SELF;

	/**
	 * Is the key released event generated by under OS or need to simulate by
	 * itself. Assume the value is <code>KEY_MAY_GEN_BY_OS</code> first, but if
	 * never get the key released event from OS, this value will be set as
	 * <code>KEY_GEN_BY_OS</code> once for all.
	 * 
	 * <p>
	 * <strong>When the key released event need to simulate by itself, the side
	 * effect is the toolkit can not generate key repeated event and key long
	 * pressed event any more, because it is impossible to know whether the key
	 * is still pressed or released by user.</strong>
	 * </p>
	 */
	private static int gKeyReleasedBy = KEY_MAY_GEN_BY_OS;

	private static int gKeyPressedKeyCode = 0;
	private static int gKeyPressedTime = 0;
	private static int gKeyRepeatedTime = 0;
	private static int gKeyLongPressedTime = 0;

	private static Object gKeyPostAndSimulateMutex = new Object();

	/** Canvas's bounds */
	private Rectangle bounds = Rectangle.EMPTY;

	/** Canvas's rotation angle */
	private int rotation;

	/**
	 * Initialize canvas with MIDlet, must call it first!
	 * 
	 * @param aMidlet
	 */
	public static void initCanvas(MIDlet aMidlet) {

		Assert.assertNotNull(aMidlet);

		gMidlet = aMidlet;
		gDisplay = Display.getDisplay(aMidlet);

		// ensure the already on shown displayable is in the bottom of the
		// stack
		gDisplayables.removeAllElements();
		if (gDisplay.getCurrent() != null) {
			gDisplayables.push(gDisplay.getCurrent());
		}

		// then the canvas itself will be pushed into the stack and show
		gDisplayables.push(gCanvas);
		gDisplay.setCurrent(gCanvas);
		// make sure is full screen
		gCanvas.setFullScreenMode(true);
	}

	/**
	 * Restore the canvas, ensure it will be shown on the display, will pop out
	 * other displayables in front of it inside the stack.
	 */
	public static void restoreCanvas() {

		if (!gCanvas.isShown()) {
			showDisplayable(gCanvas);
		}

		repaintCanvas();
	}

	/**
	 * Repaint canvas, i.e. repaint the current window upon canvas.
	 */
	public static void repaintCanvas() {

		Window win = getCurrWindow();
		if (win != null) {
			win.repaint(Reason.REPAINT_CANVAS);
			gCanvas.repaint();
		}
	}

	/**
	 * Force to repaint the canvas even from event thread, the event thread will
	 * release the mutex to make the paint thread to go through, and block
	 * itself until the repaint finished.
	 * 
	 * <p>
	 * Use this method carefully, its dangerous and may cause potential thread
	 * synchronize issues..., only when you want to do a heavy job in event
	 * thread and want to update the ui to make user feel the App still alive...
	 */
	public static void forceRepaintCanvas() {

		repaintCanvas();
		if (isEventThread()) {
			// in event thread, need to release the mutex to event thread
			Platform.waitObject(gPaintingAndEventHandlingMutex, 0);
		}
	}

	/**
	 * Whether the canvas is current showing displayable.
	 * 
	 * @return true means the canvas is current showing displayable
	 */
	public static boolean isCanvasShown() {

		return gCanvas.isShown();
	}

	public static int getDisplayWidth() {

		return gCanvas.getWidth();
	}

	public static int getDisplayHeight() {

		return gCanvas.getHeight();
	}

	public static Rectangle getDisplayBounds() {

		return gCanvas.bounds;
	}

	public static int getDisplayOrientation() {

		return gCanvas.getOrientation();
	}

	public static void setDisplayRotation(int aRotation) {

		gCanvas.setRotation(aRotation);
	}

	public static int getDisplayRotation() {

		return gCanvas.getRotation();
	}

	public static boolean isDisplayRotated() {

		return gCanvas.isRotated();
	}

	/**
	 * This method allows us to manipulate the drag started detection logic.
	 * <p>
	 * If the pointer was dragged for more than this percentage of the display
	 * size it is safe to assume that a drag is in progress.
	 * 
	 * @param aDragStartPercentage percentage of the screen required to initiate
	 *            drag
	 */
	public static void setDragStartPercentage(int aDragStartPercentage) {

		gDragStartPercentage = aDragStartPercentage;
	}

	/**
	 * This method allows us to manipulate the drag started detection logic.
	 * <p>
	 * If the pointer was dragged for more than this percentage of the display
	 * size it is safe to assume that a drag is in progress.
	 * 
	 * @return motion percentage
	 */
	public static int getDragStartPercentage() {

		return gDragStartPercentage;
	}

	/**
	 * This method can indicate whether a drag event has started or whether the
	 * device is just sending out "noise".
	 * <p>
	 * This method is invoked by pointer dragged to determine whether to
	 * propagate the actual pointer drag event to event handler.
	 * 
	 * @param aX the position of the current drag event
	 * @param aY the position of the current drag event
	 * @return true if the drag should propagate into event handler
	 */
	private static boolean hasDragStarted(final int aX, final int aY) {

		if (gDragActivationCounter == 0) {
			gDragActivationX = aX;
			gDragActivationY = aY;
			++gDragActivationCounter;
			return false;
		}

		// send the drag events to the form only after latency of 7 drag events,
		// most touch devices are too sensitive and send too many drag events.
		// 7 is just a latency const number that is pretty good for most devices
		// this may be tuned for specific devices.
		if (gDragActivationCounter > DRAG_AUTO_ACTIVATION_THRESHOLD) {
			return true;
		}

		int dragX = Math.abs(gDragActivationX - aX);
		int dragY = Math.abs(gDragActivationY - aY);
		if (dragX > gDragThreshold || dragY > gDragThreshold) {
			++gDragActivationCounter;
		}

		// have we passed the motion threshold on the X axis?
		int dragStartThreshodX = getDisplayWidth() * getDragStartPercentage();
		if (dragX * 100 > dragStartThreshodX) {
			gDragActivationCounter = DRAG_AUTO_ACTIVATION_THRESHOLD + 1;
			return true;
		}

		// have we passed the motion threshold on the Y axis?
		int dragStartThreshodY = getDisplayHeight() * getDragStartPercentage();
		if (dragY * 100 > dragStartThreshodY) {
			gDragActivationCounter = DRAG_AUTO_ACTIVATION_THRESHOLD + 1;
			return true;
		}

		return false;
	}

	private static void simulateKeyPointerEvents() {

		simulatePointerLongPressed();

		// check first to avoid enter synchronized block for most situations
		if (gKeyPressedTime == 0) {
			return;
		}

		synchronized (gKeyPostAndSimulateMutex) {
			simulateKeyEvents();
			gKeyPostAndSimulateMutex.notify();
		}
	}

	private static void simulatePointerLongPressed() {

		// simulate pointer long pressed event
		if (gDragActivationCounter < DRAG_AUTO_ACTIVATION_THRESHOLD
			&& gPointerPressedTime != 0 && gPointerLongPressedTime == 0
			&& Platform.eclipse(gPointerPressedTime) > LONG_PRESSED_INTERVAL) {

			gPointerLongPressedTime = Platform.currentTimeAfterStart();
			postPointerEvent(EventType.POINTER_LONG_PRESSED, gPointerPressedX,
				gPointerPressedY, Event.INTERNAL);
		}
	}

	/**
	 * Simulate PointerDragged event for some broken devices which can not
	 * generate it.
	 */
	private static void simulatePointerDragged(int aX, int aY) {

		// this is a special case designed to detect a "flick" event on some
		// Samsung devices that send a pointerPressed/Released with widely
		// differing X/Y values but don't send the pointerDrag events in between
		if (gDragActivationCounter == 0 && aX != gPointerPressedX
			&& aY != gPointerPressedY) {

			// set start drag point
			hasDragStarted(gPointerPressedX, gPointerPressedY);

			if (hasDragStarted(aX, aY)) {

				postPointerEvent(EventType.POINTER_DRAGGED, gPointerPressedX,
					gPointerPressedY, Event.INTERNAL);

				postPointerEvent(EventType.POINTER_DRAGGED, aX, aY,
					Event.INTERNAL);

			}
		}
	}

	private static void simulateKeyEvents() {

		long current = Platform.currentTimeMillis();

		int interval = gKeyReleasedBy == KEY_GEN_BY_OS ? LONG_PRESSED_INTERVAL
			: LONG_PRESSED_GUARD_INTERVAL;

		// simulate key long pressed event
		if ((gKeyReleasedBy == KEY_GEN_BY_OS || gKeyReleasedBy == KEY_MAY_GEN_BY_OS)
			&& gKeyPressedTime > 0
			&& gKeyLongPressedTime == 0
			&& Platform.eclipse(current, gKeyPressedTime) > interval) {

			gKeyLongPressedTime = Platform.currentTimeAfterStart(current);

			postKeyEvent(EventType.KEY_LONG_PRESSED, Event.NO_TARGET,
				gKeyPressedKeyCode, Event.INTERNAL);
		}

		// simulate key repeated event for some broken devices which can not
		// generate it.
		interval = gKeyRepeatedTime == 0 ? KEY_REPEATED_FIRST_INTERVAL
			: KEY_REPEATED_CONTINUE_INTERVAL;

		interval = gKeyRepeatedBy != KEY_GEN_BY_SELF ? KEY_REPEATED_GUARD_INTERVAL
			: interval;

		if (gKeyRepeatedBy != KEY_GEN_BY_OS
			&& gKeyPressedTime > 0
			&& Platform.eclipse(current,
				Math.max(gKeyPressedTime, gKeyRepeatedTime)) > interval) {

			// generated by itself, not OS
			gKeyRepeatedBy = KEY_GEN_BY_SELF;

			gKeyRepeatedTime = Platform.currentTimeAfterStart(current);

			postKeyEvent(EventType.KEY_REPEATED, Event.NO_TARGET,
				gKeyPressedKeyCode, Event.INTERNAL);
		}

		// simulate key released event for some broken devices which can not
		// generate it.
		interval = gKeyReleasedBy == KEY_GEN_BY_SELF ? KEY_RELEASED_NORMAL_INTERVAL
			: KEY_RELEASED_GUARD_INTERVAL;

		if (gKeyReleasedBy != KEY_GEN_BY_OS
			&& gKeyPressedTime > 0
			&& Platform.eclipse(current,
				Math.max(gKeyPressedTime, gKeyRepeatedTime)) > interval) {

			// generated by itself, not OS
			gKeyReleasedBy = KEY_GEN_BY_SELF;

			// clear
			gKeyPressedTime = 0;
			gKeyRepeatedTime = 0;
			gKeyLongPressedTime = 0;

			postKeyEvent(EventType.KEY_RELEASED, Event.NO_TARGET,
				gKeyPressedKeyCode, Event.INTERNAL);
		}
	}

	public static boolean isNumberKey(int aKeyCode) {

		return aKeyCode >= KEY_NUM0 && aKeyCode <= KEY_NUM9;
	}

	public static boolean isStarKey(int aKeyCode) {

		return aKeyCode == KEY_STAR;
	}

	public static boolean isPoundKey(int aKeyCode) {

		return aKeyCode == KEY_POUND;
	}

	public static boolean isStdTelephoneKey(int aKeyCode) {

		return aKeyCode >= KEY_NUM0 && aKeyCode <= KEY_NUM9
			|| aKeyCode == KEY_STAR || aKeyCode == KEY_POUND;
	}

	public static boolean isCharacterKey(int aKeyCode) {

		// KeyCode >= ' ' && KeyCode <= '~'
		return aKeyCode >= 32 && aKeyCode <= 126;
	}

	public static boolean isNavigationKey(int aKeyCode, int aGameAction) {

		return (aGameAction == LEFT || aGameAction == UP
			|| aGameAction == RIGHT || aGameAction == DOWN)
			&& !isCharacterKey(aKeyCode);
	}

	public static boolean isNavigationKey(int aKeyCode) {

		return isNavigationKey(aKeyCode, gCanvas.getGameAction(aKeyCode));
	}

	public static boolean isSelectKey(int aKeyCode, int aGameAction) {

		return aKeyCode == SELECT_KEY || aGameAction == FIRE
			&& !isCharacterKey(aKeyCode);
	}

	public static boolean isSendKey(int aKeyCode) {

		return aKeyCode == SEND_KEY;
	}

	public static boolean isClearKey(int aKeyCode) {

		return aKeyCode == CLEAR_KEY;
	}

	public static boolean isSoftKey(int aKeyCode) {

		return aKeyCode == LEFT_SOFT_KEY || aKeyCode == RIGHT_SOFT_KEY;
	}

	public static boolean isLeftSK(int aKeyCode) {

		return aKeyCode == LEFT_SOFT_KEY;
	}

	public static boolean isRightSK(int aKeyCode) {

		return aKeyCode == RIGHT_SOFT_KEY;
	}

	private CanvasEx() {

		super.setFullScreenMode(true);

		rotation = Rotation.ROTATE_0;
		bounds = new Rectangle(0, 0, super.getWidth(), super.getHeight());
	}

	public int getWidth() {

		return bounds != null ? bounds.width : super.getWidth();
	}

	public int getHeight() {

		return bounds != null ? bounds.height : super.getHeight();
	}

	public int getGameAction(int aKeyCode) {

		int orgKeyCode = gCanvas.getOriginalKeyCode(aKeyCode);
		int gameAction = 0;

		try {
			gameAction = super.getGameAction(orgKeyCode);
		} catch (Throwable t) {
			Log.e(t);
			return 0;
		}

		if (gameAction != 0 && isRotated()) {

			boolean rotateRight = rotation == Rotation.ROTATE_90;
			switch (gameAction) {

			case UP:
				return rotateRight ? LEFT : RIGHT;
			case DOWN:
				return rotateRight ? RIGHT : LEFT;
			case LEFT:
				return rotateRight ? DOWN : UP;
			case RIGHT:
				return rotateRight ? UP : DOWN;
			}
		}

		return gameAction;
	}

	public int getKeyCode(int aGameAction) {

		if (isRotated()) {

			boolean r = rotation == Rotation.ROTATE_90;
			switch (aGameAction) {

			case UP:
				return r ? super.getKeyCode(LEFT) : super.getKeyCode(RIGHT);
			case DOWN:
				return r ? super.getKeyCode(RIGHT) : super.getKeyCode(LEFT);
			case LEFT:
				return r ? super.getKeyCode(DOWN) : super.getKeyCode(UP);
			case RIGHT:
				return r ? super.getKeyCode(UP) : super.getKeyCode(DOWN);
			}
		}

		try {
			return super.getKeyCode(aGameAction);
		} catch (Throwable t) {
			Log.e(t);
			return 0;
		}
	}

	public String getKeyName(int aKeyCode) {

		if (isCharacterKey(aKeyCode)) {
			return super.getKeyName(aKeyCode);
		}

		if (isRotated()) {

			int gameAction = getGameAction(aKeyCode);
			if (gameAction != 0) {
				return super.getKeyName(super.getKeyCode(gameAction));
			}
		}

		try {

			int orgKeyCode = getOriginalKeyCode(aKeyCode);
			return super.getKeyName(orgKeyCode);
		} catch (Throwable t) {
			Log.e(t);
			return StringUtils.EMPTY;
		}
	}

	public int getOrientation() {

		return getWidth() > getHeight() ? Orientation.HORIZONTAL
			: Orientation.VERTICAL;
	}

	public void setRotation(int aRotation) {

		// can set rotation in paint thread or during transition!!!
		if (isPaintThread() || (gThreadState & IS_IN_TRANSITION) != 0) {
			Log.w("Try to set rotation in paint thread or during transition!!!");
			return;
		}

		if (rotation == aRotation || aRotation == Rotation.ROTATE_180
			|| !isDoubleBuffered()) {
			return;
		}

		Log.d(C_TAG, "Rotate from ", Log.toString(rotation), " to ",
			Log.toString(aRotation));

		boolean rotate90Go = rotation == Rotation.ROTATE_0;
		boolean rotate90Back = aRotation == Rotation.ROTATE_0;
		boolean rotate90 = rotate90Go || rotate90Back;

		int width = bounds.width;
		int height = bounds.height;

		synchronized (gPaintingAndEventHandlingMutex) {

			if (rotate90) {

				// create or destroy offscreen image
				if (rotate90Back) {
					gOffScreen = null;
					gGraphics = null;
				} else {
					gOffScreen = Image.createImage(height, width);
					gGraphics = new GraphicsEx(gOffScreen.getGraphics());
				}// rotate screen dimension
				bounds = new Rectangle(0, 0, height, width);
			}// if (rotate90)

			rotation = aRotation;
			gPaintingAndEventHandlingMutex.notify();
		}// synchronized

		if (rotate90) {
			getEventHandler().sizeChanged(height, width);
			getEventHandler().orentationChanged(getOrientation());
		}
		repaintCanvas();
	}

	public boolean isRotated() {

		return rotation != Rotation.ROTATE_0;
	}

	public int getRotation() {

		return rotation;
	}

	protected void paint(Graphics aG) {

		Window win = getCurrWindow();
		if (win == null || !hasPendingPaints()) {
			return;
		}

		// mark the current thread as paint thread
		if (gPaintThread == null) {
			gPaintThread = Thread.currentThread();
			gPaintThread.setPriority(Thread.NORM_PRIORITY + 1);
		}

		long current = Platform.currentTimeMillis();
		try {

			if ((gThreadState & IS_IN_TRANSITION) != 0) {
				// execute the transition animation queue directly
				synchronized (gPaintQueueLock) {
					paintEventLoop(aG, gPaintQueue, gPaintQueueFill, win);
					gPaintQueueFill = 0;
					gPaintQueueLock.notify();
				}
			} else {
				int count = 0;
				synchronized (gPaintQueueLock) {
					// switch the temp queue and paint queue
					count = gPaintQueueFill;
					Animation[] array = gPaintQueue;
					gPaintQueue = gPaintQueueTemp;
					gPaintQueueTemp = array;
					gPaintQueueFill = 0;
				}

				synchronized (gPaintingAndEventHandlingMutex) {
					gThreadState |= IS_IN_PAINTING;
					paintEventLoop(aG, gPaintQueueTemp, count, win);
					gThreadState &= ~IS_IN_PAINTING;
					gPaintingAndEventHandlingMutex.notify();
				}
			}
		} catch (Throwable t) {
			postErrorEvent(Event.UNDEFINED_ERROR_ID, t);
		} finally {
			gThreadState &= ~IS_IN_PAINTING;
		}

		// FPS tracker
		gFpsPaintTimeConsume += Platform.eclipse(current);
		++gFrames;
		if (Platform.eclipse(current, gFpsOverallTimer) > 2500) {

			gFpsOverall = gFrames * 1000 / 2500;
			gFpsPaint = gFrames * 1000 / gFpsPaintTimeConsume;
			gTotalFrames += gFrames;

			gFrames = 0;
			gFpsPaintTimeConsume = 1;
			gFpsOverallTimer = Platform.currentTimeAfterStart();

			// adjust the animation frame interval according to the frame rate
			gAnimateFrameInterval = 1000 / Math.max(30,
				Math.min(100, gFpsPaint * 2));

			Log.d(P_TAG, "Overall ", Log.toString(gFpsOverall), "fps, paint ",
				Log.toString(gFpsPaint), "fps, total frames ",
				Log.toString(gTotalFrames));
		}
	}

	protected void hideNotify() {

		super.setFullScreenMode(false);

		processEvents();// force to process remain events
		stopEventLoop();// stop event loop
		getEventHandler().hideNotify();// hide notify
	}

	protected void showNotify() {

		super.setFullScreenMode(true);

		restoreCanvas();// restore canvas
		startEventLoop();// start event loop
		getEventHandler().showNotify();// show notify
	}

	protected void sizeChanged(int aNewW, int aNewH) {

		if (aNewW != getWidth() || aNewH != getHeight()) {

			bounds = bounds.setSize(aNewW, aNewH);
			// post size changed
			getEventHandler().sizeChanged(aNewW, aNewH);
		}
	}

	protected void keyPressed(int aKeyCode) {

		postKeyEventInternal(EventType.KEY_PRESSED, Event.NO_TARGET, aKeyCode,
			Event.SPONTANEOUS);
	}

	protected void keyReleased(int aKeyCode) {

		postKeyEventInternal(EventType.KEY_RELEASED, Event.NO_TARGET, aKeyCode,
			Event.SPONTANEOUS);
	}

	protected void keyRepeated(int aKeyCode) {

		postKeyEventInternal(EventType.KEY_REPEATED, Event.NO_TARGET, aKeyCode,
			Event.SPONTANEOUS);
	}

	protected void pointerPressed(int aX, int aY) {

		postPointerEventInternal(EventType.POINTER_PRESSED, aX, aY,
			Event.SPONTANEOUS);
	}

	protected void pointerReleased(int aX, int aY) {

		postPointerEventInternal(EventType.POINTER_RELEASED, aX, aY,
			Event.SPONTANEOUS);
	}

	protected void pointerDragged(int aX, int aY) {

		postPointerEventInternal(EventType.POINTER_DRAGGED, aX, aY,
			Event.SPONTANEOUS);
	}

	private int translateX(int aX, int aY) {

		switch (getRotation()) {

		case Rotation.ROTATE_90:
			return aY;

		case Rotation.ROTATE_270:
			return getWidth() - aY;

		default:
			return aX;
		}
	}

	private int translateY(int aX, int aY) {

		switch (getRotation()) {

		case Rotation.ROTATE_90:
			return getHeight() - aX;

		case Rotation.ROTATE_270:
			return aX;

		default:
			return aY;
		}
	}

	private boolean isKeyNeedAdapt(int aOrgKey) {

		return !isCharacterKey(aOrgKey) && super.getGameAction(aOrgKey) == 0;
	}

	private int getOriginalKeyCode(int aAdaptedKey) {

		int orgKey = 0;

		switch (aAdaptedKey) {

		case LEFT_SOFT_KEY:
			orgKey = gOrgLeftSoftKeyCode;
			break;
		case RIGHT_SOFT_KEY:
			orgKey = gOrgRightSoftKeyCode;
			break;
		case SELECT_KEY:
			orgKey = gOrgSelectKeyCode;
			break;
		case SEND_KEY:
			orgKey = gOrgSendKeyCode;
			break;
		case CLEAR_KEY:
			orgKey = gOrgClearKeyCode;
			break;
		}

		return orgKey != 0 ? orgKey : aAdaptedKey;
	}

	private int getAdaptedKeyCode(int aOrgKey) {

		if (aOrgKey == gOrgLeftSoftKeyCode) {
			return LEFT_SOFT_KEY;
		} else if (aOrgKey == gOrgRightSoftKeyCode) {
			return RIGHT_SOFT_KEY;
		} else if (aOrgKey == gOrgSelectKeyCode) {
			return SELECT_KEY;
		} else if (aOrgKey == gOrgSendKeyCode) {
			return SEND_KEY;
		} else if (aOrgKey == gOrgClearKeyCode) {
			return CLEAR_KEY;
		}

		int newKey = aOrgKey;

		try {
			newKey = getAdaptedKeyCodeImpl(aOrgKey);
		} catch (Throwable t) {
			getEventHandler().onEventError(t);
		}

		// record the original keycode to create a map between original ->
		// adapted
		switch (newKey) {

		case LEFT_SOFT_KEY:
			gOrgLeftSoftKeyCode = aOrgKey;
			break;
		case RIGHT_SOFT_KEY:
			gOrgRightSoftKeyCode = aOrgKey;
			break;
		case SELECT_KEY:
			gOrgSelectKeyCode = aOrgKey;
			break;
		case SEND_KEY:
			gOrgSendKeyCode = aOrgKey;
			break;
		case CLEAR_KEY:
			gOrgClearKeyCode = aOrgKey;
			break;
		}

		return newKey;
	}

	private int getAdaptedKeyCodeImpl(int aOrgKey) {

		String keyName = super.getKeyName(aOrgKey);
		int adaptedKey = adaptByKeyName(keyName.toLowerCase(), aOrgKey);

		if (adaptedKey == 0) {
			adaptedKey = adaptByKeyCode(aOrgKey);
		}

		return adaptedKey != 0 ? adaptedKey : aOrgKey;
	}

	/**
	 * Try to guess the key code by its name.
	 * <p>
	 * 通过按键名进行适配
	 * 
	 * @param aKeyName 按键名
	 * @param aOriginalKeyCode 原先的按值
	 * @return 适配后的键值
	 */
	private int adaptByKeyName(String aName, int aOriginalKeyCode) {

		// 适配左软键
		if (aName.equals("sk2(left)")) {
			return LEFT_SOFT_KEY;
		}

		// 适配右软键
		if (aName.equals("sk1(right)")) {
			return RIGHT_SOFT_KEY;
		}

		if (aName.indexOf("soft") != INVALID_TIMER_INDEX) {
			// "处理键名与键值不规范的情况"
			int pos = aName.length() - 1;
			char numChar = aName.charAt(pos);
			// 适配左软键
			if (numChar == '1' || aName.startsWith("left")) {
				return aOriginalKeyCode != RIGHT_SOFT_KEY ? LEFT_SOFT_KEY
					: RIGHT_SOFT_KEY;
			}

			// 适配右软键
			if (numChar == '2' || aName.startsWith("right") || numChar == '4') {
				return aOriginalKeyCode != LEFT_SOFT_KEY ? RIGHT_SOFT_KEY
					: LEFT_SOFT_KEY;
			}
		}

		// 适配删除键
		if (aName.equals("clear")) {
			return CLEAR_KEY;
		}

		// 适配发送键
		if (aName.equals("send")) {
			return SEND_KEY;
		}

		// 适配选择键
		if (aName.equals("select") || aName.equals("ok")
			|| aName.equals("fire") || aName.equals("navi-center")
			|| aName.equals("start") || aName.equals("enter")) {
			return SELECT_KEY;
		}

		return 0;
	}

	/**
	 * 通过键值适配
	 * 
	 * @param aKeyName int 按键值
	 * @return int 适配后的按键值
	 */
	private int adaptByKeyCode(int aKeyCode) {

		// 适配左软件
		if (aKeyCode == -6 || aKeyCode == -21 || aKeyCode == 21
			|| aKeyCode == -202 || aKeyCode == 57345 || aKeyCode == 113
			|| aKeyCode == 65 || aKeyCode == 66) {
			return LEFT_SOFT_KEY;
		}

		// 适配右软件 //索爱机型音乐键的特殊外理
		if (aKeyCode == -7 || aKeyCode == -22 || aKeyCode == 22
			|| aKeyCode == -203 || aKeyCode == 57346 || aKeyCode == 68
			|| aKeyCode == 67 || aKeyCode == 112 || aKeyCode == 106) {
			return RIGHT_SOFT_KEY;
		}

		// 适配确定键 //索爱机型浏览器键的特殊处理
		if (aKeyCode == -5 || aKeyCode == -10 || aKeyCode == -20
			|| aKeyCode == 20 || aKeyCode == 23 || aKeyCode == -14
			|| aKeyCode == -26 || aKeyCode == -200 || aKeyCode == 13) {
			return SELECT_KEY;
		}

		// 适配删除键
		if (aKeyCode == -8 || aKeyCode == 8) {
			return CLEAR_KEY;
		}

		return 0;
	}

	/**
	 * Run the event loop.
	 * 
	 * <p>
	 * <strong>Client code will never call this method directly!!! </strong>
	 * </p>
	 */
	public void run() {

		while (gEnableLoop) {
			eventLoopIdleCheck();
			eventLoopEntry();
		}
	}

	/****************************************************************/
	/*                                                              */
	/* EventDispatcher Begin */
	/*                                                              */
	/****************************************************************/
	private static final int PAINT_QUEUE_LEN = 16;
	private static final int EVENTS_KEEP_NUM = 16;
	private static final int MAX_TIMER_NUM = 16;

	private static final Event EMPTY_KEY_EVENT = new Event(
		EventType.KEY_RELEASED, BasicEventHandler.EMPTY_HANDLER, KEY_NUM1, 9,
		Event.NO_PARAM, Event.INTERNAL);

	private static final Event EMPTY_POINTER_EVENT = new Event(
		EventType.POINTER_RELEASED, BasicEventHandler.EMPTY_HANDLER, 0, 0,
		Event.NO_PARAM, Event.INTERNAL);

	/** Switch to enable/disable the event loop */
	private static volatile boolean gEnableLoop;

	// last key events and pointer events
	private static final Event[] gKeyEvents = new Event[EVENTS_KEEP_NUM];
	private static final Event[] gPointerEvents = new Event[EVENTS_KEEP_NUM];
	private static int gKeyEventsFill = 0;
	private static int gPointerEventsFill = 0;

	// event loop idle time
	private static int gEventIdleTime;

	// event queue to store events waiting for dispatch
	private static final Vector gEventQueue = new Vector();

	// event filters
	private static final Vector gEventFilters = new Vector();

	// paint queue to store components waiting for repaint
	private static Animation[] gPaintQueue = new Animation[PAINT_QUEUE_LEN];
	private static Animation[] gPaintQueueTemp = new Animation[PAINT_QUEUE_LEN];
	private static int gPaintQueueFill = 0;

	// paint queue lock
	private static final Object gPaintQueueLock = gKeyEvents;

	// the lock used by event thread to wait awhile
	// private static final Object gEventThreadWaitLock = gPointerEvents;

	// the state and mutex to synchronize the paint thread and event thread
	private static final int IS_IN_TRANSITION = 1;
	private static final int IS_IN_EVENT_HANDLING = 2;
	private static final int IS_IN_PAINTING = 4;
	private static final int IS_IN_ANIMATING = 8;

	private static volatile int gThreadState = 0;
	private static final Object gPaintingAndEventHandlingMutex = gPointerEvents;

	// fps tracker
	private static int gTotalFrames;
	private static int gFrames;
	private static int gFpsOverallTimer = Platform.currentTimeAfterStart();
	private static int gFpsOverall;
	private static int gFpsPaintTimeConsume = 1;
	private static int gFpsPaint;

	// timer queue
	private static final int[] gTimers = new int[MAX_TIMER_NUM];
	private static final EventHandler[] gTimerTargets = new EventHandler[MAX_TIMER_NUM];
	private static int gActiveTimerCount = 0;
	private static long gAllocatedTimerBitSet = 0;

	// animation
	private static int gAnimateFrameInterval = 1000 / 60;
	private static int glastAnimateTime = 0;

	// refer to Event thread
	private static Thread gEventThread;

	// refer to Paint Thread (The event thread of MIDP Canvas itself)
	private static Thread gPaintThread;

	private static void startEventLoop() {

		if (gEventThread == null || !gEventThread.isAlive()) {

			// enable and restart event thread
			gEnableLoop = true;
			gEventThread = Platform.startThread(gCanvas, "EVT",
				Thread.NORM_PRIORITY);
			Log.d(E_TAG, "The event loop is started.");
		}
	}

	private static void stopEventLoop() {

		if (gEventThread != null && gEventThread.isAlive()) {
			gEnableLoop = false;// stop event loop
			Log.d(E_TAG, "The event loop is stopped.");
		}
	}

	/**
	 * The current thread is Event thread or not.
	 * 
	 * @return true if the current thread is Event thread, otherwise false
	 */
	public static boolean isEventThread() {

		return gEventThread == Thread.currentThread();
	}

	/**
	 * The current thread is Paint thread or not.
	 * 
	 * @return true if the current thread is Event thread, otherwise false
	 */
	public static boolean isPaintThread() {

		return gPaintThread == Thread.currentThread();
	}

	/**
	 * Has pending events in the queue waiting for dispatching.
	 * 
	 * <p>
	 * <strong>This method is thread-safe, it is designed can be called from
	 * non-GUI thread.</strong>
	 * </p>
	 * 
	 * @return has pending events or not
	 */
	public static boolean hasPendingEvents() {

		return !gEventQueue.isEmpty();
	}

	/**
	 * Returns true if the implementation still has elements to paint.
	 * 
	 * @return false by default
	 */
	public static boolean hasPendingPaints() {

		return gPaintQueueFill != 0;
	}

	public static boolean hasActiveTimers() {

		return gActiveTimerCount > 0;
	}

	/**
	 * Processes pending events until there are no more events to process, if
	 * the call thread is not GUI thread, it will just post a request to process
	 * and return immediately.
	 */
	public static void processEvents() {

		if (isEventThread()) {
			eventLoopEntry();
		}
	}

	/**
	 * Install event filter.
	 * 
	 * @param aFilter
	 */
	public static void installEventFilter(EventHandler aFilter) {

		Assert.assertNotNull(aFilter);

		gEventFilters.addElement(aFilter);
	}

	/**
	 * Remove installed event filter.
	 * 
	 * @param aFilter
	 */
	public static void removeEventFilter(EventHandler aFilter) {

		gEventFilters.removeElement(aFilter);
	}

	public static synchronized int findAvailableTimer() {

		// all allocated?
		if (gAllocatedTimerBitSet != -1L) {

			long mask = 1;
			for (int id = 0; id < gTimers.length && id < 64; ++id, mask <<= 1) {

				if ((gAllocatedTimerBitSet & mask) == 0) {
					gAllocatedTimerBitSet |= mask;
					return id;
				}
			}
		}

		return INVALID_TIMER_INDEX;
	}

	public static synchronized void closeTimer(int aTimerId) {

		if (aTimerId < 0 || aTimerId >= gTimers.length) {
			return;
		}

		if (gTimers[aTimerId] != 0) {
			stopTimer(aTimerId);
		}
		gAllocatedTimerBitSet &= ~(1 << aTimerId);
	}

	public static boolean startTimer(int aTimerId, EventHandler aTimerTarget,
		int aDelayMills) {

		if (aTimerId < 0 || aTimerId >= gTimers.length) {
			return false;
		}

		int fireTime = Platform.currentTimeAfterStart() + aDelayMills;
		synchronized (gTimers) {

			if (gTimers[aTimerId] == 0) {
				++gActiveTimerCount;
			}
			gTimers[aTimerId] = fireTime;
			gTimerTargets[aTimerId] = aTimerTarget;
		}

		Log.d(T_TAG, "Start timer - ", Log.toString(aTimerId),
			", fire time - ", Log.toString(fireTime));
		return true;
	}

	public static void stopTimer(int aTimerId) {

		if (!hasActiveTimers() || aTimerId < 0 || aTimerId >= gTimers.length) {
			return;
		}

		synchronized (gTimers) {

			if (gTimers[aTimerId] != 0) {
				--gActiveTimerCount;
			}
			gTimers[aTimerId] = 0;
			gTimerTargets[aTimerId] = null;
		}
		Log.d(T_TAG, "Stop timer - ", Log.toString(aTimerId));
	}

	private static void fireTimers() {

		if (!hasActiveTimers()) {
			return;
		}

		long currTime = Platform.currentTimeMillis();

		synchronized (gTimers) {

			for (int i = 0; i < gTimers.length; ++i) {

				if (gTimers[i] != 0
					&& currTime > Platform.realTimeOfDelta(gTimers[i])) {

					postTimerEvent(gTimerTargets[i], i, gTimers[i]);

					gTimers[i] = 0;
					gTimerTargets[i] = null;

					--gActiveTimerCount;
				}// if
			}// for
		}// synchronized
	}

	/**
	 * Send the event immediately, the send thread will be blocked until the
	 * event is sent by event thread.
	 * 
	 * <p>
	 * Use this method carefully in paint thread, its dangerous and may cause
	 * potential thread synchronize issues when it is called in paint thread...
	 * 
	 * @param aEv event object
	 */
	public static void sendEvent(Event aEv) {

		Assert.assertNotNull(aEv);

		if (isPaintThread()) {
			// in paint thread, post and release the mutex to event thread
			aEv.setWaitToSend();
			postEvent(aEv);
			Platform.waitObject(gPaintingAndEventHandlingMutex, 0);
		} else if (isEventThread()) {
			// dispatch it directly
			dispatchEvent(aEv);
		} else {
			Assert.assertFalse(aEv.isWaitToSend(), Assert.ARG);

			// post the event and wait until it sent
			synchronized (aEv) {
				aEv.setWaitToSend();
				postEvent(aEv);
				Platform.waitObject(aEv, 0);
			}
		}
	}

	/**
	 * Post event into event queue.
	 * 
	 * <p>
	 * <strong>This method is thread-safe, it is designed can be called from
	 * non-GUI thread.</strong>
	 * </p>
	 * 
	 * @param aEv event object
	 */
	public static void postEvent(Event aEv) {

		Assert.assertNotNull(aEv);

		gEventQueue.addElement(aEv);
		startEventLoop();
	}

	/**
	 * Helper method to post key event, will call {@link postEvent}.
	 */
	public static void postKeyEvent(int aType, EventHandler aTarget,
		int aKeyCode, boolean aSpontaneous) {

		postEvent(new Event(aType, aTarget, aKeyCode,
			gCanvas.getGameAction(aKeyCode), Event.NO_PARAM, aSpontaneous));
	}

	private static void postKeyEventInternal(int aType, EventHandler aTarget,
		int aKeyCode, boolean aSpontaneous) {

		// adapt key code
		if (gCanvas.isKeyNeedAdapt(aKeyCode)) {

			aKeyCode = gCanvas.getAdaptedKeyCode(aKeyCode);
		}

		synchronized (gKeyPostAndSimulateMutex) {

			// bookkeeping and filter
			switch (aType) {

			case EventType.KEY_PRESSED:
				// if the last key is not released, ignore this pressed,
				// this is usually caused by the key release event can not
				// generated by OS and need to simulate by itself
				if (gKeyPressedTime != 0) {
					return;
				}

				// record pressed state
				gKeyPressedKeyCode = aKeyCode;
				gKeyPressedTime = Platform.currentTimeAfterStart();
				gKeyRepeatedTime = 0;
				gKeyLongPressedTime = 0;
				break;

			case EventType.KEY_REPEATED:
				// key repeated event generated by OS
				gKeyRepeatedBy = KEY_GEN_BY_OS;

				// when already released, ignore any following repeated
				if (gKeyPressedTime == 0) {
					return;
				}
				break;

			case EventType.KEY_RELEASED:
				// key released event generated by OS
				gKeyReleasedBy = KEY_GEN_BY_OS;

				// clear
				gKeyPressedTime = 0;
				gKeyRepeatedTime = 0;
				gKeyLongPressedTime = 0;
				break;
			}
			// switch
		}
		// synchronized (gKeyPostAndSimulateMutex)

		postKeyEvent(aType, aTarget, aKeyCode, aSpontaneous);
	}

	/**
	 * Helper method to post pointer event, will call {@link postEvent}.
	 */
	public static void postPointerEvent(int aType, Component aCmp) {

		if (aCmp == null) {
			return;
		}

		postEvent(new Event(aType, Event.NO_TARGET, aCmp.getAbsoluteX() + 1,
			aCmp.getAbsoluteY() + 1, Event.NO_PARAM, Event.SPONTANEOUS));
	}

	/**
	 * Helper method to post pointer event, will call {@link postEvent}.
	 */
	public static void postPointerEvent(int aType, int aX, int aY,
		boolean aSpontaneous) {

		postEvent(new Event(aType, Event.NO_TARGET, aX, aY, Event.NO_PARAM,
			aSpontaneous));
	}

	private static void postPointerEventInternal(int aType, int aX, int aY,
		boolean aSpontaneous) {

		// translate coordinate
		if (gCanvas.isRotated()) {

			int x = aX;
			aX = gCanvas.translateX(aX, aY);
			aY = gCanvas.translateY(x, aY);
		}

		// bookkeeping and filter
		switch (aType) {

		case EventType.POINTER_PRESSED:
			// record pressed state
			gPointerPressedX = aX;
			gPointerPressedY = aY;
			gPointerPressedTime = Platform.currentTimeAfterStart();
			gPointerLongPressedTime = 0;
			break;

		case EventType.POINTER_DRAGGED:
			// drag start check
			if (!hasDragStarted(aX, aY)) {
				return;
			}
			break;

		case EventType.POINTER_RELEASED:
			// simulate drag event for broken device
			simulatePointerDragged(aX, aY);
			// clear
			gDragActivationCounter = 0;
			gPointerPressedTime = 0;
			gPointerLongPressedTime = 0;
			break;
		}

		postPointerEvent(aType, aX, aY, aSpontaneous);
	}

	/**
	 * Helper method to post component action event, will call {@link postEvent}
	 * .
	 */
	public static void postComponentActionEvent(EventHandler aTarget,
		Component aSource) {

		postEvent(new Event(EventType.ACTION, aTarget, Event.NO_ARG,
			Event.NO_ARG, aSource, Event.INTERNAL));
	}

	/**
	 * Helper method to post command action event, will call {@link postEvent} .
	 */
	public static void postCommandActionEvent(EventHandler aTarget, int aCmdId) {

		postEvent(new Event(EventType.ACTION, aTarget, aCmdId, Event.NO_ARG,
			Event.NO_PARAM, Event.INTERNAL));
	}

	/**
	 * Helper method to post shortcut action event, will call {@link postEvent}.
	 */
	public static void postShortcutActionEvent(EventHandler aTarget,
		int a1stKey, int a2ndKey, int aCmdId) {

		postEvent(new Event(EventType.ACTION, aTarget, aCmdId, BitUtils.toInt(
			a1stKey, a2ndKey), Event.NO_PARAM, Event.INTERNAL));
	}

	/**
	 * Helper method to post timer event, will call {@link postEvent}.
	 */
	private static void postTimerEvent(EventHandler aTarget, int aTimerId,
		int aTime) {

		postEvent(new Event(EventType.TIMER, aTarget, aTimerId, aTime,
			Event.NO_PARAM, Event.INTERNAL));
	}

	/**
	 * Helper method to post progress event, will call {@link postEvent}.
	 */
	public static void postProgressEvent(EventHandler aTarget, int aProgress,
		String aProgressInfo) {

		postEvent(new Event(EventType.PROGRESS, aTarget, aProgress,
			Event.NO_ARG, aProgressInfo, Event.INTERNAL));
	}

	/**
	 * 
	 * Helper method to post error event, will call {@link postEvent}.
	 */
	public static void postErrorEvent(int aErrorId, Throwable aException) {

		postEvent(new Event(EventType.ERROR, Event.NO_TARGET, aErrorId,
			Event.NO_ARG, aException, Event.INTERNAL));
	}

	/**
	 * Helper method to post error event, will call {@link postEvent}.
	 */
	public static void postErrorEvent(int aErrorId, String aErrMsg) {

		postEvent(new Event(EventType.ERROR, Event.NO_TARGET, aErrorId,
			Event.NO_ARG, aErrMsg, Event.INTERNAL));
	}

	/**
	 * Invoked to add an element to the paintQueue
	 * 
	 * @param cmp component or animation to push into the paint queue
	 */
	public static void postRepaint(Animation cmp) {

		Assert.assertNotNull(cmp);

		synchronized (gPaintQueueLock) {

			// if already in the paint queue, will be ignored
			for (int iter = 0; iter < gPaintQueueFill; iter++) {
				if (gPaintQueue[iter] == cmp) {
					return;
				}
			}

			// overcrowding the queue don't try to grow the array!
			if (gPaintQueueFill >= gPaintQueue.length) {
				Log.d(E_TAG, "Warning paint queue size exceeded, "
					+ "please watch the amount of repaint calls");
			} else {
				gPaintQueue[gPaintQueueFill++] = cmp;// add into paint queue
				gCanvas.repaint();// ask repaint
			}
			gPaintQueueLock.notify();
		}
	}

	public static int getCurrPressedKeyCode() {

		return gKeyPressedKeyCode;
	}

	public static int getCurrPointerPressedX() {

		return gPointerPressedX;
	}

	public static int getCurrPointerPressedY() {

		return gPointerPressedY;
	}

	/**
	 * Get last happened key event.
	 * 
	 * @return last happened key event, if no key event happen yet, an empty
	 *         meaningless key event will be return
	 */
	public static Event getLastKeyEvent() {

		int idx = gKeyEventsFill != 0 ? gKeyEventsFill - 1
			: gKeyEvents.length - 1;

		return gKeyEvents[idx] != null ? gKeyEvents[idx] : EMPTY_KEY_EVENT;
	}

	/**
	 * Get last happened key events.
	 * 
	 * @return last happened key events, arrange in time order, from older to
	 *         newer.
	 */
	public static Event[] getLastKeyEvents() {

		int count = gKeyEvents[gKeyEventsFill] == null ? gKeyEventsFill
			: gKeyEvents.length;

		Event[] copy = new Event[count];

		for (int i = gKeyEventsFill; i < count; ++i) {

			copy[i] = gKeyEvents[i];
		}

		for (int i = 0; i < gKeyEventsFill; ++i) {

			copy[i] = gKeyEvents[i];
		}

		return copy;
	}

	/**
	 * Get last happened pointer event.
	 * 
	 * @return last happened pointer event, if no pointer event happen yet, an
	 *         empty meaningless pointer event will be return
	 */
	public static Event getLastPointerEvent() {

		int idx = gPointerEventsFill != 0 ? gPointerEventsFill - 1
			: gPointerEvents.length - 1;

		return gPointerEvents[idx] != null ? gPointerEvents[idx]
			: EMPTY_POINTER_EVENT;
	}

	/**
	 * Get last happened pointer events.
	 * 
	 * @return last happened pointer events, arrange in time order, from older
	 *         to newer.
	 */
	public static Event[] getLastPointerEvents() {

		int count = gPointerEvents[gPointerEventsFill] == null ? gPointerEventsFill
			: gPointerEvents.length;

		Event[] copy = new Event[count];

		for (int i = gPointerEventsFill; i < count; ++i) {

			copy[i] = gPointerEvents[i];
		}

		for (int i = 0; i < gPointerEventsFill; ++i) {

			copy[i] = gPointerEvents[i];
		}

		return copy;
	}

	/**
	 * This method returns the current dragging speed based on the latest
	 * dragged events.
	 * 
	 * @return the dragging speed when pointer dragged
	 */
	public static int getDragSpeed(int aAxis) {

		long current = Platform.currentTimeMillis();

		int timediff = 0;
		int diff = 0;
		int velocity = 0;
		int speed = 0;

		int draggedPtNum = 0;
		int ratio = 30000;

		Event ptOld = null, ptEv = null;
		int idx = gPointerEventsFill, end = gPointerEvents.length;
		for (; idx != end; ++idx) {

			ptEv = gPointerEvents[idx];

			// step to end then circle once
			if (idx == gPointerEvents.length - 1) {
				idx = INVALID_TIMER_INDEX;
				end = gPointerEventsFill;
			}

			// ensure not null
			if (ptEv == null) {
				continue;
			}

			switch (ptEv.type) {

			case EventType.POINTER_PRESSED:
				// reset
				ptOld = null;
				speed = 0;
				draggedPtNum = 0;
				break;

			case EventType.POINTER_DRAGGED:
				// calculate each step
				if (Platform.eclipse(current, ptEv.time) < 400) {

					if (ptOld == null) {
						ptOld = ptEv;
						timediff = Platform.eclipse(current, ptOld.time);
					}

					diff = aAxis == Axis.X_AXIS ? ptEv.getX() - ptOld.getX()
						: ptEv.getY() - ptOld.getY();

					if (timediff > 0) {
						velocity = diff * ratio / timediff;
						speed += velocity;
					}
				}
				++draggedPtNum;
				break;
			}
		}

		draggedPtNum = Math.max(draggedPtNum, 1);
		speed = -speed / draggedPtNum;

		Log.d(C_TAG, "Current drag speed : ", Log.toString(speed),
			", dragged points : ", Log.toString(draggedPtNum), ", timediff : ",
			Log.toString(timediff));

		if (speed > MAX_DRAG_SPEED)
			return MAX_DRAG_SPEED;
		else if (speed < -MAX_DRAG_SPEED)
			return -MAX_DRAG_SPEED;
		else
			return speed;
	}

	public static void setMaxAnimationFrameRate(int aAnimationFrameRate) {

		gAnimateFrameInterval = 1000 / aAnimationFrameRate;
	}

	public static void executeTransition(Animation aTransition) {

		// can not execute transition in paint thread!!!
		if (isPaintThread() || (gThreadState & IS_IN_TRANSITION) != 0) {
			Log.w("Try to execute transition in paint thread or during transition!!!");
			return;
		}

		try {
			synchronized (gPaintingAndEventHandlingMutex) {
				// paint previous repaint components first
				gCanvas.repaint();
				Platform.waitObject(gPaintingAndEventHandlingMutex,
					gAnimateFrameInterval);
				// transitions
				gThreadState |= IS_IN_TRANSITION;
				executeTransitionImpl(aTransition);
				gThreadState &= ~IS_IN_TRANSITION;
				gPaintingAndEventHandlingMutex.notify();
			}
		} catch (Throwable t) {
			Log.e(t);
		} finally {
			gThreadState &= ~IS_IN_TRANSITION;
		}
	}

	private static void executeTransitionImpl(Animation aTransition) {

		Platform.sleepThread(gAnimateFrameInterval);

		while (aTransition.animate()) {
			postRepaint(aTransition);
			Platform.sleepThread(gAnimateFrameInterval);
		}
	}

	public static int getOverallFps() {

		return gFpsOverall;
	}

	public static int getPaintFps() {

		return gFpsPaint;
	}

	public static int getTotalFrames() {

		return gTotalFrames;
	}

	/**
	 * Event loop entry, {@link #eventLoop()} will be invoked first, then
	 * {@link #fireTimers()}, then {@link #animationLoop(Window)}, then
	 * {@link #simulateKeyPointerEvents()}.
	 */
	private static void eventLoopEntry() {

		try {

			synchronized (gPaintingAndEventHandlingMutex) {

				gThreadState |= IS_IN_EVENT_HANDLING;
				// event
				eventLoop();
				// animation
				if (animationLoop(getCurrWindow())) {
					gThreadState |= IS_IN_ANIMATING;
				} else {
					gThreadState &= ~IS_IN_ANIMATING;
				}
				gThreadState &= ~IS_IN_EVENT_HANDLING;
				// wake up the paint thread
				gPaintingAndEventHandlingMutex.notify();
			}

			fireTimers();
			simulateKeyPointerEvents();
		} catch (Throwable t) {
			// forward event error to default event handler
			getEventHandler().onEventError(t);
		} finally {
			gThreadState &= ~IS_IN_EVENT_HANDLING;
		}
	}

	/**
	 * Handle normal events, i.e. events except paint event.
	 * 
	 * <p>
	 * <strong>Event Dispatch Procedure</strong>
	 * <li>If event has target, then dispatch it to its target directly.
	 * <li>If event do not has target, then use installed filters to filter it.
	 * <li>If event is not filter out, then dispatch it to default initiate
	 * handler, usually it is current window.
	 * </p>
	 */
	private static void eventLoop() {

		// filter and handle events
		EventHandler handler = CanvasEx.getEventHandler();
		Event ev = null;
		while (gEventQueue.size() > 0) {

			ev = (Event) gEventQueue.elementAt(0);
			gEventQueue.removeElementAt(0);
			dispatchEvent(ev, handler);
		}
	}

	/**
	 * Dispatch event to event's target or the current default event handler.
	 * 
	 * @param aEv event object
	 */
	private static void dispatchEvent(Event aEv) {

		dispatchEvent(aEv, CanvasEx.getEventHandler());
	}

	/**
	 * Dispatch event to event's target or the specified event handler.
	 * 
	 * @param aEv event
	 * @param aHandler specified event handler
	 */
	private static void dispatchEvent(Event aEv, EventHandler aHandler) {

		if (aEv.hasTarget()) {
			aEv.target.event(aEv);
		} else if (!filterEvent(aEv)) {
			aHandler.event(aEv);
		}

		recordEvent(aEv);
		if (aEv.isWaitToSend()) {
			aEv.setSent();
			synchronized (aEv) {
				aEv.notifyAll();
			}
		}

		Log.d(aEv.type == EventType.TIMER ? T_TAG : E_TAG, aEv, " -> ",
			aHandler);
	}

	/**
	 * Filter the event with installed event filters.
	 * 
	 * @param aEv event
	 * @return true if the event is filtered out, which means the event will not
	 *         be dispatch subsequently
	 * 
	 * @see {@link dispatchEvent}
	 */
	private static boolean filterEvent(Event aEv) {

		EventHandler filter = null;
		boolean filtered = false;
		for (Enumeration e = gEventFilters.elements(); e.hasMoreElements();) {

			filter = (EventHandler) e.nextElement();
			filtered |= filter.event(aEv);
			Log.d(E_TAG, aEv, " filtered by ", filter);
		}

		return filtered;
	}

	/**
	 * Invoked by the GUI Thread to repaint every dirty component in the paint
	 * queue.
	 */
	private static void paintEventLoop(Graphics aG, Animation[] aPaintQueue,
		int aCount, Window aWin) {

		// setup painting environment
		if (gOffScreen == null && (gGraphics == null || gGraphics.g != aG)) {
			gGraphics = new GraphicsEx(aG);
		} else {
			gGraphics.reset();
		}

		// set current window's font
		gGraphics.setFont(aWin.getFont());

		// paint loop
		Animation ani = null;
		for (int i = 0; i < aCount; ++i) {

			ani = aPaintQueue[i];
			aPaintQueue[i] = null;

			gGraphics.resetTranslation();
			gGraphics.setClip(gCanvas.bounds);

			Log.d(P_TAG, "PAINT >> ", ani);
			int drawn = GraphicsEx.gDrawnPrimitives;
			ani.paint(gGraphics);// paint!!!
			Log.d(P_TAG, "PAINT END >> ", ani, " drawn graphics primitives : ",
				Log.toString(GraphicsEx.gDrawnPrimitives - drawn));
		}

		// use offscreen, need flush to real screen
		if (gOffScreen != null) {

			Sprite sp = new Sprite(gOffScreen);
			sp.setTransform(getDisplayRotation());
			sp.defineReferencePixel(0, 0);
			sp.setPosition(0, 0);
			sp.paint(aG);
		}

		// animation
		// animationLoop(aWin);
	}

	private static void recordEvent(Event aEv) {

		if (aEv.isKeyEvent()) {

			gKeyEvents[gKeyEventsFill++] = aEv;

			if (gKeyEventsFill == gKeyEvents.length) {

				gKeyEventsFill = 0;
			}
		} else if (aEv.isPointerEvent()) {

			gPointerEvents[gPointerEventsFill++] = aEv;

			if (gPointerEventsFill == gPointerEvents.length) {

				gPointerEventsFill = 0;
			}
		}
	}

	/**
	 * Get the initiate event handler for event dispatch, when windows stack is
	 * not empty, return the current showing window.
	 * 
	 * @return the initiate event handler
	 */
	private static EventHandler getEventHandler() {

		synchronized (gWindows) {

			return gWindows.empty() ? BasicEventHandler.EMPTY_HANDLER
				: (EventHandler) gWindows.peek();
		}
	}

	private static boolean animationLoop(Window win) {

		if (win != null
			&& Platform.eclipse(glastAnimateTime) > gAnimateFrameInterval) {

			glastAnimateTime = Platform.currentTimeAfterStart();
			return win.repaintAnimations();
		}

		return false;
	}

	private static boolean eventLoopIdleCheck() {

		// stop the idle event loop?
		if (!hasPendingEvents()) {

			// start idle
			if (gEventIdleTime == 0) {
				gEventIdleTime = Platform.currentTimeAfterStart();
			}

			int idleTime = Platform.eclipse(gEventIdleTime);
			if (idleTime > MAX_EVENT_LOOP_IDLE_TIME) {
				stopEventLoop();
				return true;
			} else {
				Platform.sleepThread(gAnimateFrameInterval / 3);
			}
		} else {
			gEventIdleTime = 0;
		}

		return false;
	}

	/****************************************************************/
	/*                                                              */
	/* EventDispatcher End */
	/*                                                              */
	/****************************************************************/

	/****************************************************************/
	/*                                                              */
	/* WindowManager Begin */
	/*                                                              */
	/****************************************************************/

	/** The global midlet instance. */
	public static MIDlet gMidlet;

	/** The global display instance */
	public static Display gDisplay;

	// stack of displayables, the top of it is the current displayable
	private static final Stack gDisplayables = new Stack();

	// stack of windows, the top of it is the current showing window
	private static final Stack gWindows = new Stack();

	/**
	 * Ask the Midlet to exit, call the {@link MIDlet#notifyDestroyed()} method.
	 */
	public static void exit() {

		if (gMidlet != null) {

			restorePrevWindow();
			restorePrevDisplayable();
			gMidlet.notifyDestroyed();
		}
	}

	/**
	 * Get the kept display, which will be kept after initCanvas().
	 * 
	 * @return kept display
	 * @see {@link initCanvas()}
	 * @throws NullPointerException if initCanvas() is not called first
	 */
	public static Display getDisplay() {

		Assert.assertNotNull(gDisplay);

		return gDisplay;
	}

	/**
	 * Get current showing displayable on display.
	 * 
	 * @return current showing displayable
	 * @throws NullPointerException if initCanvas() is not called first
	 */
	public static Displayable getCurrDisplayable() {

		Displayable dis = getDisplay().getCurrent();

		synchronized (gDisplayables) {

			if (dis != null) {

				// something wrong happen, client do not to use WindowManager
				// API
				// to manage Displayables
				Assert.assertFalse(gDisplayables.empty(), Assert.ARG);
				Assert.assertSame(dis, gDisplayables.peek(), Assert.STATE);
			}
		}

		return dis;
	}

	/**
	 * Get the count of displayables in the stack.
	 * 
	 * @return count of displayables
	 */
	public static int getDisplayableCount() {

		return gDisplayables.size();
	}

	/**
	 * Show the displayable on display, and push it into the stack.
	 * 
	 * @param aDisplayable displayable will be shown
	 * @throws NullPointerException if initCanvas() is not called first, or
	 *             aDisplayable is null
	 */
	public static void showDisplayable(Displayable aDisplayable) {

		Assert.assertNotNull(aDisplayable);

		if (aDisplayable != gCanvas) {
			processEvents();// force to process remain events first
		}

		synchronized (gDisplayables) {

			// if already in the stack, pop out the objects in front of it
			// if not in the stack, then push it
			if (!CollectionUtils.popUntil(gDisplayables, aDisplayable)) {
				gDisplayables.push(aDisplayable);
			}
			gDisplay.setCurrent(aDisplayable);
		}

		Log.d(W_TAG, "Show displayable ", aDisplayable);
	}

	/**
	 * Restore the current displayable, maybe useful when cover from application
	 * stop, force to refresh the screen.
	 * 
	 * @throws NullPointerException if initCanvas() is not called first
	 */
	public static void restoreCurrDisplayable() {

		synchronized (gDisplayables) {

			if (!gDisplayables.empty()) {
				getDisplay().setCurrent((Displayable) gDisplayables.peek());
			}
		}
	}

	/**
	 * Restore the previous displayable in the stack, will pop out the current.
	 * 
	 * @throws NullPointerException if initCanvas() is not called first
	 */
	public static void restorePrevDisplayable() {

		Display display = getDisplay();

		synchronized (gDisplayables) {
			if (gDisplayables.size() > 1) {
				gDisplayables.pop();
				display.setCurrent((Displayable) gDisplayables.peek());
			} else if (gDisplayables.size() == 1) {
				gDisplayables.pop();
				display.setCurrent(null);
			}
		}
	}

	/**
	 * Get the current showing window on canvas.
	 * 
	 * @return current showing window, null when not windows in the stack
	 */
	public static Window getCurrWindow() {

		synchronized (gWindows) {
			return gWindows.empty() ? null : (Window) gWindows.peek();
		}
	}

	public static Window getPrevWindow() {

		synchronized (gWindows) {
			int idx = gWindows.size() - 2;
			return idx >= 0 ? (Window) gWindows.elementAt(idx) : null;
		}
	}

	/**
	 * Get the count of windows in the stack.
	 * 
	 * @return count of windows in the stack
	 */
	public static int getWindowCount() {

		return gWindows.size();
	}

	/**
	 * Is window the current showing window on canvas.
	 * 
	 * @param aWin window
	 * @return true if aWin is the current showing window
	 * @throws NullPointerException if aWin is null
	 */
	public static boolean isCurrWindow(Window aWin) {

		Assert.assertNotNull(aWin);

		synchronized (gWindows) {
			return !gWindows.empty() && gWindows.peek() == aWin;
		}
	}

	/**
	 * Show window on the canvas and push it into stack.
	 * 
	 * @param aWin window will be shown
	 * @throws NullPointerException if aWin is null
	 */
	public static void showWindow(Window aWin) {

		Assert.assertNotNull(aWin);

		synchronized (gWindows) {

			// if in the stack, pop out the objects in front of it
			// if not in the stack, then push it
			if (!CollectionUtils.popUntil(gWindows, aWin)) {
				gWindows.push(aWin);
			}
			restoreCanvas();
		}
	}

	/**
	 * Close the window and remove it out of the stack.
	 * 
	 * @param aWin window will be hidden
	 * @throws NullPointerException if aWin is null
	 */
	public static void closeWindow(Window aWin) {

		Assert.assertNotNull(aWin);

		synchronized (gWindows) {

			if (isCurrWindow(aWin)) {

				restorePrevWindow();
				if (gWindows.isEmpty()) {
					exit();
				}
			} else {
				gWindows.removeElement(aWin);
			}
		}
	}

	/**
	 * Compare two windows order inside the windows' queue.
	 * 
	 * @param aWin1 window no.1
	 * @param aWin2 window no.2
	 * @return when the return value large than zero, aWin1 is upon aWin2 or
	 *         aWin1 do not inside the queue yet, when the return value less
	 *         than zero, aWin2 is upon aWin1 or aWin2 do not inside the queue
	 *         yet, when the return value equals to zero, aWin1 and aWin2 both
	 *         do not inside the queue
	 */
	public static int compareOrder(Window aWin1, Window aWin2) {

		synchronized (gWindows) {

			int idx1 = gWindows.indexOf(aWin1);
			int idx2 = gWindows.indexOf(aWin2);

			if (idx1 < 0 && idx2 >= 0) {
				return 1;
			} else if (idx1 >= 0 && idx2 < 0) {
				return -1;
			} else {
				return idx1 - idx2;
			}
		}
	}

	/**
	 * Restore the previous window in the stack, will pop out the current.
	 */
	public static void restorePrevWindow() {

		synchronized (gWindows) {

			if (!gWindows.isEmpty()) {
				gWindows.pop();
			}
			restoreCanvas();
		}
	}

	/****************************************************************/
	/*                                                              */
	/* WindowManager End */
	/*                                                              */
	/****************************************************************/
}
