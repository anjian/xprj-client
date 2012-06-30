/**
 * Tiny.cn.uc.ui.events.EventHandler.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

/**
 * Interface identify that target is an event handler, can dispatch or route
 * event to it for handling.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public interface EventHandler {

	public boolean event(Event aEv);

	public void keyEvent(Event aKeyEv);

	public void pointerEvent(Event aPtEv);

	public void actionEvent(Event aActEv);

	public void timerEvent(Event aTimerEv);

	public void progressEvent(Event aProgressEv);

	public void errorEvent(Event aErrorEv);

	public void sizeChanged(int aNewWidth, int aNewHeight);

	public void orentationChanged(int aNewOrentation);

	public void showNotify();

	public void hideNotify();

	public void focusGained(Event aCauseEv);

	public void focusLost(Event aCauseEv);

	public void onEventError(Throwable aErr);
}
