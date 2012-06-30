/**
 * Tiny.cn.uc.ui.events.BasicEventHandler.java, 2010-11-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import cn.uc.tiny.ex.Event.EventType;
import cn.uc.util.debug.Log;

/**
 * Basic event handler implementation, implement a basic event routing logic.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class BasicEventHandler implements EventHandler {

	/**
	 * Empty event handler, just use as a stub and do nothing.
	 */
	public static final BasicEventHandler EMPTY_HANDLER = new BasicEventHandler();

	/** {@inheritDoc} */
	public boolean event(Event aEv) {

		switch (aEv.type) {

		case EventType.KEY_PRESSED:
		case EventType.KEY_RELEASED:
		case EventType.KEY_REPEATED:
		case EventType.KEY_LONG_PRESSED:
			keyEvent(aEv);
			break;

		case EventType.POINTER_PRESSED:
		case EventType.POINTER_RELEASED:
		case EventType.POINTER_DRAGGED:
		case EventType.POINTER_LONG_PRESSED:
			pointerEvent(aEv);
			break;

		case EventType.ACTION:
			actionEvent(aEv);
			break;

		case EventType.TIMER:
			timerEvent(aEv);
			break;

		case EventType.PROGRESS:
			progressEvent(aEv);
			break;

		case EventType.ERROR:
			errorEvent(aEv);
			break;
		}

		return aEv.isAccepted() || aEv.isFiltered();
	}

	/** {@inheritDoc} */
	public void keyEvent(Event aKeyEv) {

		switch (aKeyEv.type) {

		case EventType.KEY_PRESSED:
			keyPressed(aKeyEv);
			break;

		case EventType.KEY_RELEASED:
			keyReleased(aKeyEv);
			break;

		case EventType.KEY_REPEATED:
			keyRepeated(aKeyEv);
			break;

		case EventType.KEY_LONG_PRESSED:
			keyLongPressed(aKeyEv);
			break;
		}
	}

	/** {@inheritDoc} */
	public void pointerEvent(Event aPtEv) {

		switch (aPtEv.type) {
		case EventType.POINTER_PRESSED:
			pointerPressed(aPtEv);
			break;

		case EventType.POINTER_RELEASED:
			pointerReleased(aPtEv);
			break;

		case EventType.POINTER_DRAGGED:
			pointerDragged(aPtEv);
			break;

		case EventType.POINTER_LONG_PRESSED:
			pointerLongPressed(aPtEv);
			break;
		}
	}

	public void keyPressed(Event aKeyEv) {

	}

	public void keyReleased(Event aKeyEv) {

	}

	public void keyRepeated(Event aKeyEv) {

	}

	public void keyLongPressed(Event aKeyEv) {

	}

	public void pointerPressed(Event aPtEv) {

	}

	public void pointerReleased(Event aPtEv) {

	}

	public void pointerDragged(Event aPtEv) {

	}

	public void pointerLongPressed(Event aPtEv) {

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

		Object cause = aErrorEv.getErrorCause();

		if (cause instanceof Throwable)
			onEventError((Throwable) cause);
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

		Log.e(aErr);
	}
}
