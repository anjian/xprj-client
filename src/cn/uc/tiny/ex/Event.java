/**
 * Tiny.cn.uc.ui.ex.Event.java, 2010-11-19
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import cn.uc.build.Config;
import cn.uc.tiny.Component;
import cn.uc.util.BitUtils;
import cn.uc.util.Platform;
import cn.uc.util.debug.Assert;
import cn.uc.util.debug.Log;

/**
 * Event represents an event happens during the running of application.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public class Event {

	/**
	 * {Enum} Type of {@link Event}
	 */
	public static final class EventType {

		public static final int KEY_PRESSED = 0;
		public static final int KEY_RELEASED = 1;
		public static final int KEY_REPEATED = 2;
		public static final int KEY_LONG_PRESSED = 3;

		public static final int POINTER_PRESSED = 4;
		public static final int POINTER_RELEASED = 5;
		public static final int POINTER_DRAGGED = 6;
		public static final int POINTER_LONG_PRESSED = 7;

		public static final int ACTION = 8;
		public static final int TIMER = 9;
		public static final int PROGRESS = 10;
		public static final int ERROR = 11;

		public static final int USER_DEFINED = 100;
	}

	// constants for better readable code
	public static final boolean SPONTANEOUS = true;
	public static final boolean INTERNAL = false;
	public static final EventHandler NO_TARGET = null;
	public static final int NO_ARG = -1;
	public static final Object NO_PARAM = null;
	public static final int UNDEFINED_ERROR_ID = -1;

	private static final int IS_ACCEPTED = 0;
	private static final int IS_SPONTANEOUS = 1;
	private static final int IS_FILTERED = 2;
	private static final int IS_WAIT_TO_SEND = 3;
	private static final int IS_SENT = 4;

	/**
	 * The time stamp when event happens, actually it is the delta time after
	 * system startup.
	 * 
	 * @see {@link Platform.deltaTimeAfterStart}
	 */
	public final int time;

	/**
	 * The type of event, i.e. KEY_PRESSED, KEY_RELEASED, etc...
	 * 
	 * @see {@link Event.EventType}
	 */
	public final int type;

	/**
	 * The target of this event, if an event has a target, it will be dispatch
	 * to the target directly.
	 */
	public final EventHandler target;

	/**
	 * 
	 * The argument 1 of event.
	 * 
	 * <ul>
	 * <li><strong>ActionEvent</strong>, is the command id when the action
	 * triggered by command.
	 * <li><strong>KeyEvent</strong>, is the key code.
	 * <li><strong>PointerEvent</strong>, is the x coordinate.
	 * <li><strong>TimerEvent</strong>, is the ID of Timer.
	 * <li><strong>ProgressEvent</strong>, is the progress, in the range of 0 ~
	 * 100.
	 * <li><strong>ErrorEvent</strong>, is the ID of Error if it has one.
	 * <ul>
	 */
	public final int arg1;

	/**
	 * 
	 * The argument 2 of event.
	 * 
	 * <ul>
	 * <li><strong>KeyEvent</strong>, is the game action.
	 * <li><strong>PointerEvent</strong>, is the y coordinate.
	 * <ul>
	 */
	public final int arg2;

	/**
	 * Additional parameter, its type and data will depend on Event's type.
	 * 
	 * <ul>
	 * <li><strong>ActionEvent</strong> parameter is the source of the action,
	 * it can be a component ({@link Component}) or a command (
	 * {@link CommandEx}).
	 * <li><strong>ProgressEvent</strong> parameter is a String of progress
	 * information.
	 * <li><strong>ErrorEvent</strong> parameter is a String or a Throwable for
	 * the cause of error. it can be <code>null</code> when there are no focus
	 * before changed.
	 * <ul>
	 */
	public final Object param;

	/**
	 * The flags of event, include the handled result and other attributes
	 * 
	 * @see {@link Event.EventFlag}
	 */
	private int flags;

	/**
	 * Create a event with type, target, arguments, parameter and spontaneous
	 * flag.
	 * 
	 * @param aType {@link EventType} of Event
	 * @param aTarget target of Event
	 * @param aArg1 argument 1 of Event
	 * @param aArg2 argument 2 of Event
	 * @param aParam parameter object of Event
	 * @param aSpontaneous whether the Event is {@link #SPONTANEOUS} or
	 *            {@link #INTERNAL}
	 */
	public Event(int aType, EventHandler aTarget, int aArg1, int aArg2,
		Object aParam, boolean aSpontaneous) {

		time = Platform.currentTimeAfterStart();
		type = aType;
		target = aTarget;

		arg1 = aArg1;
		arg2 = aArg2;
		param = aParam;

		flags = BitUtils.set(flags, IS_SPONTANEOUS, aSpontaneous);
	}

	public final boolean isKeyEvent() {

		return type >= EventType.KEY_PRESSED
			&& type <= EventType.KEY_LONG_PRESSED;
	}

	/**
	 * Is repeated key event or not.
	 * 
	 * @return true when the event is KEY_REPEATED
	 */
	public final boolean isKeyRepeated() {

		return type == EventType.KEY_REPEATED;
	}

	public final boolean isPointerEvent() {

		return type >= EventType.POINTER_PRESSED
			&& type <= EventType.POINTER_LONG_PRESSED;
	}

	public final boolean hasTarget() {

		return target != null;
	}

	public final void accept() {

		flags = BitUtils.set(flags, IS_ACCEPTED);
	}

	public final void ignore() {

		flags = BitUtils.clear(flags, IS_ACCEPTED);
	}

	public final void filter() {

		flags = BitUtils.set(flags, IS_FILTERED);
	}

	public final boolean isAccepted() {

		return BitUtils.get(flags, IS_ACCEPTED);
	}

	public final boolean isSpontaneous() {

		return BitUtils.get(flags, IS_SPONTANEOUS);
	}

	public final boolean isFiltered() {

		return BitUtils.get(flags, IS_FILTERED);
	}

	final void setWaitToSend() {

		flags = BitUtils.set(flags, IS_WAIT_TO_SEND);
		flags = BitUtils.clear(flags, IS_SENT);
	}

	final boolean isWaitToSend() {

		return BitUtils.get(flags, IS_WAIT_TO_SEND);
	}
	
	final void setSent() {
		
		flags = BitUtils.set(flags, IS_SENT);
	}
	
	final boolean isSent() {
		
		return BitUtils.get(flags, IS_SENT);
	}

	public final boolean isComponentAction() {

		return type == EventType.ACTION && param != null
			&& param instanceof Component;
	}

	public final Component getActionComponent() {

		Assert.assertTrue(isComponentAction(), Assert.STATE);

		return (Component) param;
	}

	public final boolean isCommandAction() {

		return type == EventType.ACTION && !isComponentAction();
	}

	public final int getCommandId() {

		return arg1;
	}

	public final int getTimerId() {

		return arg1;
	}

	public final int getErrorId() {

		return arg1;
	}

	public final Object getErrorCause() {

		return param;
	}

	public final int getProgress() {

		return arg1;
	}

	public final Object getProgressInfo() {

		return param;
	}

	/**
	 * Get the x coordinate of PointerEvent
	 * 
	 * @return the x
	 */
	public int getX() {

		return arg1;
	}

	/**
	 * Get the y coordinate of PointerEvent
	 * 
	 * @return the y
	 */
	public int getY() {

		return arg2;
	}

	/**
	 * Get the keycode of KeyEvent.
	 * 
	 * @return the keyCode
	 */
	public int getKeyCode() {

		return arg1;
	}

	/**
	 * Get the game action of KeyEvent.
	 * 
	 * @return the gameAction
	 */
	public int getGameAction() {

		return arg2;
	}

	/**
	 * Get key name of KeyEvent.
	 * 
	 * @return key name
	 */
	public String getKeyName() {

		return CanvasEx.gCanvas.getKeyName(arg1);
	}

	public char getChar() {

		return (char) (arg1 > 0 ? arg1 : 0);
	}

	/**
	 * Is number or not, i.e. KEY_NUM0 ~ KEY_NUM9.
	 * 
	 * @return true when the key is number key
	 */
	public boolean isNumberKey() {

		return CanvasEx.isNumberKey(arg1);
	}

	/**
	 * Is key "star" (*) or not.
	 * 
	 * @return true when the key is key "star" (*)
	 */
	public boolean isStarKey() {

		return CanvasEx.isStarKey(arg1);
	}

	/**
	 * Is key "pound" (#) or not.
	 * 
	 * @return true when the key is key "pound" (#)
	 */
	public boolean isPoundKey() {

		return CanvasEx.isPoundKey(arg1);
	}

	/**
	 * Is ITU-T standard telephone keypad's key or not, i.e. isNumber or isStar
	 * or isPound.
	 * 
	 * @return true then the key is ITU-T standard telephone keypad's key
	 */
	public boolean isStdTelephoneKey() {

		return CanvasEx.isStdTelephoneKey(arg1);
	}

	/**
	 * Is character key or not, i.e. ASCII character.
	 * 
	 * @return true when the key is character key
	 */
	public boolean isCharacterKey() {

		return CanvasEx.isCharacterKey(arg1);
	}

	public boolean isNavigationKey() {

		return CanvasEx.isNavigationKey(arg1, arg2);
	}

	public boolean isSelectKey() {

		return CanvasEx.isSelectKey(arg1, arg2);
	}

	public boolean isSendKey() {

		return CanvasEx.isSendKey(arg1);
	}

	public boolean isClearKey() {

		return CanvasEx.isClearKey(arg1);
	}

	public boolean isSoftKey() {

		return CanvasEx.isSoftKey(arg1);
	}

	public boolean isLeftSK() {

		return CanvasEx.isLeftSK(arg1);
	}

	public boolean isRightSK() {

		return CanvasEx.isRightSK(arg1);
	}

	public String toString() {

		if (!Config.DEBUG)
			return super.toString();

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(type());
		sBuffer.append(" Event [arg1=");
		sBuffer.append(arg1);
		sBuffer.append(", arg2=");
		sBuffer.append(arg2);
		sBuffer.append(", param=");
		sBuffer.append(Log.toString(param));
		sBuffer.append(", flag=");
		sBuffer.append(flag());
		sBuffer.append(", target=");
		sBuffer.append(Log.toString(target));
		sBuffer.append(", time=");
		sBuffer.append(time);
		sBuffer.append("]");
		return sBuffer.toString();
	}

	private final String type() {
	
		switch (type) {
		case EventType.KEY_PRESSED:
			return "KEY_PRESSED";
	
		case EventType.KEY_RELEASED:
			return "KEY_RELEASED";
	
		case EventType.KEY_REPEATED:
			return "KEY_REPEATED";
	
		case EventType.KEY_LONG_PRESSED:
			return "KEY_LONG_PRESSED";
	
		case EventType.POINTER_PRESSED:
			return "POINTER_PRESSED";
	
		case EventType.POINTER_RELEASED:
			return "POINTER_RELEASED";
	
		case EventType.POINTER_DRAGGED:
			return "POINTER_DRAGGED";
	
		case EventType.POINTER_LONG_PRESSED:
			return "POINTER_LONG_PRESSED";
	
		case EventType.ACTION:
			return "ACTION";
	
		case EventType.TIMER:
			return "TIMER";
	
		case EventType.PROGRESS:
			return "PROGRESS";
	
		case EventType.ERROR:
			return "ERROR";
	
		default:
			return "UNKNOWN";
		}
	}

	private final String flag() {
	
		StringBuffer sb = new StringBuffer();
		sb.append(isAccepted() ? "ACCEPTED" : "IGNORED");
		sb.append(" | ");
		sb.append(isSpontaneous() ? "SPONTANEOUS" : "INTERNAL");
		sb.append(isFiltered() ? " | FILTERED" : "");
		sb.append(isWaitToSend() ? " | WAITING" : "");
		sb.append(isSent() ? " | SENT" : "");
		return sb.toString();
	}
}
