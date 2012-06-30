/**
 * 
 */
package cn.uc.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.microedition.midlet.MIDlet;

import cn.uc.util.debug.Log;
import cn.uc.util.io.File;

/**
 * Platform (Phone Device + OS + JavaVM) relative information and
 * functionalities.
 * 
 * @notice Must call the initiate method first.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class Platform {

	/**
	 * <p>
	 * The <code>file.encoding</code> System Property.
	 * </p>
	 * <p>
	 * File encoding, such as <code>Cp1252</code>.
	 * </p>
	 */
	public static final String FILE_ENCODING = getSystemProperty("file.encoding");

	/**
	 * <p>
	 * The <code>file.separator</code> System Property. File separator (
	 * <code>&quot;/&quot;</code> on UNIX).
	 * </p>
	 */
	public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

	/**
	 * <p>
	 * The <code>line.separator</code> System Property. Line separator (
	 * <code>&quot;\n&quot;</code> on UNIX).
	 * </p>
	 */
	public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

	/**
	 * <p>
	 * The <code>path.separator</code> System Property. Path separator (
	 * <code>&quot;:&quot;</code> on UNIX).
	 * </p>
	 */
	public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

	public static final Runtime gRuntime = Runtime.getRuntime();

	/** features bit-set, item's value as a index in a bit-set. */
	public static final int HAS_REPEAT_EVENTS = 0;
	public static final int HAS_POINTER_EVENTS = 1;
	public static final int HAS_POINTER_MOTION_EVENTS = 2;
	// features - end

	/** phone types enum, item's value as a phone type. */
	public static final int PHONE_TYPE_UNKNOWN = -1;
	public static final int PHONE_TYPE_NOKIA = 0;
	public static final int PHONE_TYPE_MOTOROLA = 1;
	public static final int PHONE_TYPE_SIEMENS = 2;
	public static final int PHONE_TYPE_SONY_ERICSSON = 3;
	public static final int PHONE_TYPE_SAMSUNG = 4;
	public static final int PHONE_TYPE_NOT_USE = 5;
	public static final int PHONE_TYPE_PALM = 6;
	public static final int PHONE_TYPE_RIM_WIRELESS = 7;
	public static final int PHONE_TYPE_VS = 8;
	public static final int PHONE_TYPE_LG = 9;
	// phone types end

	/** phone sub types enum, item's value as a phone sub type. */
	public static final int PHONE_SUB_TYPE_UNKNOWN = -1;
	public static final int PHONE_SUB_TYPE_SE_UIQ = 1;
	public static final int PHONE_SUB_TYPE_NOKIA_S40 = 2;
	public static final int PHONE_SUB_TYPE_NOKIA_S60 = 3;
	public static final int PHONE_SUB_TYPE_SE_JP8 = 6;
	// phone sub types end

	private static final int LOW_MEMORY_PERSENTAGE = 30;

	private static long SYSTEM_START_TIME = currentTimeMillis();

	public static final String TAG = "Plat";

	/** Features of platform, such as support key repeat or not. */
	private static int features;

	/** Phone type, a phone is full defined by 'phone' and 'subPhone'. */
	private static int phone;
	private static int subPhone;

	public static final void initializePlatform(MIDlet aMidlet) {

		Log.printSystemProperties();
	}

	/**
	 * <p>
	 * Gets a System property, defaulting to an empty string if the property
	 * cannot be read.
	 * </p>
	 * 
	 * @param aProperty the system property name
	 * @return the system property value or empty string when the property not
	 *         exists
	 * @see {@link System#getProperty(String)}
	 */
	public static String getSystemProperty(String aProperty) {

		try {
			return System.getProperty(aProperty);
		} catch (RuntimeException ex) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * @see {@link Thread#sleep(long)}
	 */
	public static void sleepThread(int aMillis) {

		try {
			Thread.sleep(aMillis);
		} catch (InterruptedException e) {
			Log.e(e);
		}
	}

	/**
	 * @see {@link Thread#Thread(Runnable, String)}
	 * @see {@link Thread#setPriority(int)}
	 * @see {@link Thread#start()}
	 */
	public static Thread startThread(Runnable aTarget, String aName,
		int aPriotiry) {

		Thread t = new Thread(aTarget, aName);
		t.setPriority(aPriotiry);
		t.start();

		return t;
	}

	/**
	 * @see {@link Object#wait(long)}
	 */
	public static void waitObject(Object aObject, long aTimeout) {

		if (aObject != null) {
			try {
				aObject.wait(aTimeout);
			} catch (InterruptedException e) {
				Log.e(e);
			}
		}
	}

	/**
	 * @see {@link InputStream#close()}
	 */
	public static void closeStream(InputStream aIs) {

		try {
			if (aIs != null) {
				aIs.close();
			}
		} catch (Throwable t) {
			Log.e(t);
		}
	}

	/**
	 * @see {@link OutputStream#close()}
	 */
	public static void closeStream(OutputStream aOs) {

		try {
			if (aOs != null) {
				aOs.close();
			}
		} catch (Throwable t) {
			Log.e(t);
		}
	}

	/**
	 * @see {@link Reader#close()}
	 */
	public static void closeReader(Reader aReader) {

		try {
			if (aReader != null) {
				aReader.close();
			}
		} catch (Throwable t) {
			Log.e(t);
		}
	}

	/**
	 * @see {@link Writer#close()}
	 */
	public static void closeWriter(Writer aWriter) {

		try {
			if (aWriter != null) {
				aWriter.close();
			}
		} catch (Throwable t) {
			Log.e(t);
		}
	}

	public static void closeFile(File aFile) {

		try {
			if (aFile != null) {
				aFile.close();
			}
		} catch (Throwable t) {
			Log.e(t);
		}
	}

	/**
	 * @see {@link Class#getResourceAsStream(String)}
	 */
	public static InputStream getResourceAsStream(String aName) {

		return Platform.class.getResourceAsStream(aName);
	}

	/**
	 * The class of given name is exists or not.
	 * 
	 * @param aClassName class name
	 * @return true if the class is exists
	 */
	public static boolean isClassExists(String aClassName) {

		try {
			Class.forName(aClassName);
			return true;
		} catch (ClassNotFoundException e) {
			Log.e(e);
			return false;
		}
	}

	public static void gc() {

		gcWhenMemLessThan(LOW_MEMORY_PERSENTAGE);
	}

	public static void gcWhenMemLessThan(int aPersentage) {

		aPersentage = Math.min(Math.max(aPersentage, 0), 50);

		long total = gRuntime.totalMemory();
		long free = gRuntime.freeMemory();

		if (free * 100 / total < aPersentage) {

			gRuntime.gc();

			Log.d(TAG, "Free memory : before ", Log.toString(free / 1000),
				"kb, after ", Log.toString(gRuntime.freeMemory() / 1000), "kb");
		}
	}

	public static long currentTimeMillis() {

		return System.currentTimeMillis();
	}

	public static int currentTimeAfterStart() {

		return currentTimeAfterStart(currentTimeMillis());
	}

	public static int currentTimeAfterStart(long aCurrTime) {

		return (int) (aCurrTime - SYSTEM_START_TIME);
	}

	public static long realTimeOfDelta(int aDeltaTime) {

		return (aDeltaTime & Long.MAX_VALUE) + SYSTEM_START_TIME;
	}

	public static int eclipse(int aDeltaTime) {

		return (int) (currentTimeMillis() - realTimeOfDelta(aDeltaTime));
	}

	public static int eclipse(long aCurrTime, int aDeltaTime) {

		return (int) (aCurrTime - realTimeOfDelta(aDeltaTime));
	}

	public static int eclipse(long aLastTime) {

		return (int) (currentTimeMillis() - aLastTime);
	}

	public static int eclipse(int aDelta1, int aDelta2) {

		return (int) ((aDelta1 & Long.MAX_VALUE) - (aDelta2 & Long.MAX_VALUE));
	}

	// public static void initiate(DisplayEx displayEx) {
	//
	// CanvasEx c = displayEx.getCanvasEx();
	//
	// features = BitSets.set(features, HAS_REPEAT_EVENTS,
	// c.hasRepeatEvents());
	//
	// features = BitSets.set(features, HAS_POINTER_EVENTS,
	// c.hasPointerEvents());
	//
	// features = BitSets.set(features, HAS_POINTER_MOTION_EVENTS,
	// c.hasPointerMotionEvents());
	// }
	//
	// public static boolean isUnknownPhone() {
	//
	// return phone == PHONE_TYPE_UNKNOWN;
	// }
	//
	// public static boolean hasRepeatEvents() {
	//
	// return BitSets.get(features, HAS_REPEAT_EVENTS);
	// }
}
