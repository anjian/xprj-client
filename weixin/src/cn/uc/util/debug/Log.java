/**
 * Tiny.cn.uc.util.Log.java, 2010-11-19
 * 
 * Copyright (c) 2010 UC Mobile, All rights reserved.
 */
package cn.uc.util.debug;

import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import cn.uc.tiny.ex.CanvasEx;
import cn.uc.tiny.ex.ImageEx;
import cn.uc.util.ArrayUtils;
import cn.uc.util.Platform;
import cn.uc.util.io.File;

/**
 * Log is a helper class, it help to log DEBUG message.
 * 
 * <p>
 * <strong> This class can be removed totally along with its methods' invoking
 * statements by ProGuard when the final release of software do not need any
 * logging information, you can put the following script in ProGuard's
 * configuration file: </strong>
 * 
 * <pre>
 * -assumenosideeffects class cn.uc.util.debug.Log {
 *   &ltmethods&gt;
 * }
 * #&ltmethods&gt -> < methods >
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 * @see Stringable
 */
public final class Log {

	public static final String E_TAG = "ERROR";
	public static final String W_TAG = "WARNING";

	private static Vector gTagsFilter;
	private static int gLastTime = 0;
	private static int gCount = 0;

	public static void addTagFilter(String aTagFilter) {

		if (gTagsFilter == null) {
			gTagsFilter = new Vector();
		}

		gTagsFilter.addElement(aTagFilter);
	}

	public static void d(String aTag, String aMsg) {

		++gCount;

		if (gTagsFilter != null && gTagsFilter.contains(aTag))
			return;

		StringBuffer sb = new StringBuffer(256);

		//format #THREAD [TAG, TIME, ECLIPSE] MESSAGE
		String thread = null;
		try {

			if (CanvasEx.isEventThread()) {

				thread = "#EVT" + "-" + Thread.currentThread().getPriority();
			}
			else if (CanvasEx.isPaintThread()) {

				thread = "#PNT" + "-" + Thread.currentThread().getPriority();
			}
		}
		catch (Throwable t) {
		}
		finally {

			if (thread == null) {

				thread = Thread.currentThread().toString();
			}
		}

		int delta = Platform.currentTimeAfterStart();
		int eclipse = delta - gLastTime;
		gLastTime = delta;

		sb.append(thread).append(" [").append(aTag).append(", ").append(delta).append(
			", ").append(eclipse).append("] ").append(aMsg);

		System.out.println(sb);
	}

	public static void e(Object aObj) {

		d(E_TAG, toString(aObj));
		if (aObj instanceof Throwable) {
			((Throwable) aObj).printStackTrace();
		}
	}

	public static void w(Object aObj) {

		d(W_TAG, toString(aObj));
	}

	public static void w(Object aObj1, Object aObj2) {

		d(W_TAG, toString(aObj1) + toString(aObj2));
	}
	
	public static void d(String aTag, Object aObj) {

		d(aTag, toString(aObj));
	}

	public static void d(String aTag, Object aObj1, Object aObj2) {

		d(aTag, toString(aObj1) + toString(aObj2));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3,
		Object aObj4) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3) +
			toString(aObj4));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3,
		Object aObj4, Object aObj5) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3) +
			toString(aObj4) + toString(aObj5));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3,
		Object aObj4, Object aObj5, Object aObj6) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3) +
			toString(aObj4) + toString(aObj5) + toString(aObj6));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3,
		Object aObj4, Object aObj5, Object aObj6, Object aObj7) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3) +
			toString(aObj4) + toString(aObj5) + toString(aObj6) +
			toString(aObj7));
	}

	public static void d(String aTag, Object aObj1, Object aObj2, Object aObj3,
		Object aObj4, Object aObj5, Object aObj6, Object aObj7, Object aObj8) {

		d(aTag, toString(aObj1) + toString(aObj2) + toString(aObj3) +
			toString(aObj4) + toString(aObj5) + toString(aObj6) +
			toString(aObj7) + toString(aObj8));
	}

	public static String toString(boolean aVal) {

		return aVal ? "TRUE" : "FALSE";
	}

	public static String toString(int aVal) {

		return Integer.toString(aVal);
	}

	public static String toString(char aCh) {

		return "" + aCh;
	}

	public static String toBinaryString(int aVal) {

		return Integer.toBinaryString(aVal);
	}

	public static String toString(long aVal) {

		return Long.toString(aVal);
	}

	public static String toString(Object aObj) {

		if (aObj == null)
			return "null";

		if (aObj instanceof int[])
			return ArrayUtils.toString((int[]) aObj);

		return aObj.toString();
	}

	public static void s(String aTag, ImageEx aImg, String aName) {

		Calendar now = Calendar.getInstance();

		try {
			
			String root = File.getRootPath();
			StringBuffer name = new StringBuffer();
			name.append("file:///").append(root).append("ucweb").append(
				File.FILE_SEPARATOR).append("log").append(File.FILE_SEPARATOR).append(
				aTag).append(File.FILE_SEPARATOR).append("[").append(aName).append(
				"]").append(now.get(Calendar.YEAR)).append("-").append(
				now.get(Calendar.MONTH) + 1).append("-").append(
				now.get(Calendar.DAY_OF_MONTH)).append("-").append(
				now.get(Calendar.HOUR_OF_DAY)).append("-").append(
				now.get(Calendar.MINUTE)).append("-").append(
				now.get(Calendar.SECOND)).append("-").append(
				now.get(Calendar.MILLISECOND)).append(".bmp");
			
			File.mkdir("ucweb", "log", aTag);
			aImg.save(name.toString());
			d(aTag, "Save image to : ", name.toString());
		}
		catch (IOException e) {
			e(e);
		}
		catch (SecurityException e) {
			e(e);
		}
	}

	public static void printSystemProperties() {

		d(Platform.TAG, "$System Properties : ");

		d(Platform.TAG, "microedition.profiles : ",
			Platform.getSystemProperty("microedition.profiles"));
		d(Platform.TAG, "microedition.configuration : ",
			Platform.getSystemProperty("microedition.configuration"));
		d(Platform.TAG, "microedition.locale : ",
			Platform.getSystemProperty("microedition.locale"));
		d(Platform.TAG, "microedition.platform : ",
			Platform.getSystemProperty("microedition.platform"));
		d(Platform.TAG, "microedition.encoding : ",
			Platform.getSystemProperty("microedition.encoding"));
		d(Platform.TAG, "microedition.commports : ",
			Platform.getSystemProperty("microedition.commports"));
		d(Platform.TAG, "microedition.hostname : ",
			Platform.getSystemProperty("microedition.hostname"));
		d(Platform.TAG, "microedition.jtwi.version : ",
			Platform.getSystemProperty("microedition.jtwi.version"));

		d(Platform.TAG, "microedition.media.version : ",
			Platform.getSystemProperty("microedition.media.version"));
		d(Platform.TAG, "microedition.pim.version : ",
			Platform.getSystemProperty("microedition.pim.version"));
		d(Platform.TAG, "microedition.m3g.version : ",
			Platform.getSystemProperty("microedition.m3g.version"));
		d(Platform.TAG, "microedition.location.version : ",
			Platform.getSystemProperty("microedition.location.version"));
		d(Platform.TAG, "bluetooth.api.version : ",
			Platform.getSystemProperty("bluetooth.api.version"));
		d(
			Platform.TAG,
			"microedition.io.file.FileConnection.version : ",
			Platform.getSystemProperty("microedition.io.file.FileConnection.version"));
		d(Platform.TAG, "microedition.global.version : ",
			Platform.getSystemProperty("microedition.global.version"));
		d(Platform.TAG, "microedition.chapi.version : ",
			Platform.getSystemProperty("microedition.chapi.version"));
		d(Platform.TAG, "microedition.sip.version : ",
			Platform.getSystemProperty("microedition.sip.version"));

		d(Platform.TAG, "supports.mixing : ",
			Platform.getSystemProperty("supports.mixing"));
		d(Platform.TAG, "supports.audio.capture : ",
			Platform.getSystemProperty("supports.audio.capture"));
		d(Platform.TAG, "supports.video.capture : ",
			Platform.getSystemProperty("supports.video.capture"));
		d(Platform.TAG, "supports.recording : ",
			Platform.getSystemProperty("supports.recording"));
		d(Platform.TAG, "audio.encodings : ",
			Platform.getSystemProperty("bluetooth.api.version"));
		d(Platform.TAG, "video.encodings : ",
			Platform.getSystemProperty("video.encodings"));
		d(Platform.TAG, "video.snapshot.encodings : ",
			Platform.getSystemProperty("video.snapshot.encodings"));
		d(Platform.TAG, "streamable.contents : ",
			Platform.getSystemProperty("streamable.contents"));

		d(Platform.TAG, "wireless.messaging.sms.smsc : ",
			Platform.getSystemProperty("wireless.messaging.sms.smsc"));
		d(Platform.TAG, "wireless.messaging.mms.mmsc : ",
			Platform.getSystemProperty("wireless.messaging.mms.mmsc"));

		d(Platform.TAG, "fileconn.dir.photos : ",
			Platform.getSystemProperty("fileconn.dir.photos"));
		d(Platform.TAG, "fileconn.dir.videos : ",
			Platform.getSystemProperty("fileconn.dir.videos"));
		d(Platform.TAG, "fileconn.dir.tones : ",
			Platform.getSystemProperty("fileconn.dir.tones"));
		d(Platform.TAG, "fileconn.dir.memorycard : ",
			Platform.getSystemProperty("fileconn.dir.memorycard"));
		d(Platform.TAG, "fileconn.dir.private  : ",
			Platform.getSystemProperty("bluetooth.api.version"));
		d(Platform.TAG, "fileconn.dir.photos.name : ",
			Platform.getSystemProperty("fileconn.dir.photos.name"));
		d(Platform.TAG, "fileconn.dir.videos.name : ",
			Platform.getSystemProperty("fileconn.dir.videos.name"));
		d(Platform.TAG, "fileconn.dir.tones.name : ",
			Platform.getSystemProperty("fileconn.dir.tones.name"));
		d(Platform.TAG, "file.separator : ",
			Platform.getSystemProperty("file.separator"));
		d(Platform.TAG, "file.encoding : ",
			Platform.getSystemProperty("file.encoding"));
		d(Platform.TAG, "line.separator : ",
			Platform.getSystemProperty("line.separator"));
		d(Platform.TAG, "path.separator : ",
			Platform.getSystemProperty("path.separator"));
		d(Platform.TAG, "fileconn.dir.memorycard.name : ",
			Platform.getSystemProperty("fileconn.dir.memorycard.name"));
	}
}
