/**
 * ResourceManager.java, 2010-12-3
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

import cn.uc.T;
import cn.uc.util.Platform;
import cn.uc.util.debug.Log;

/**
 * Resource is used to load external text resource and can get the text through
 * its index in TxtIndex.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 7.6
 * @version 7.6
 * @see T
 */
public class Resource {

	public static final String TAG = "Res";

	/** The path of external text resource. */
	public static final String TEXT_RESOURCE = "/uc.bin";
	
	/** Use cache to cache text string or not. */
	public static final boolean TEXT_USE_CACHE = false;

	public static final byte UTF_8_ENCODING = 0;
	public static final byte UTF_16_ENCODING = 1;

	public static final String[] gImageResources = { "/l.png", "/30.png",
		"/29.png", "/32.png", "/31.png", "/33.png", "/35.png", "/56.png" };

	// image resource id
	public static final int LOGO_UCWEB = 0;
	public static final int LOGO_UCWEB_STARTUP = 1;
	public static final int LOGO_UCWEB_VERSION = 2;
	public static final int ICON_PROGRESS_ON = 3;
	public static final int ICON_PROGRESS_OFF = 4;
	public static final int ICON_DOWN_ARROW = 5;
	public static final int ICON_RIGHT_ARROW = 6;
	public static final int ICON_TIMER = 7;
	public static final int TOTAL_IMAGE_RESOURCE = 8;

	private static final Image[] gImageCache = new Image[TOTAL_IMAGE_RESOURCE];

	private static int[] gOffsets;
	private static String gText;
	private static String[] gTextCache;

	static {

		try {
			loadText();
		} catch (Exception e) {
			Log.e(e);
		}
	}

	/**
	 * Load external text resource.
	 * 
	 * @return true when load successful
	 */
	public static boolean loadText() {

		InputStream is = Resource.class.getResourceAsStream(TEXT_RESOURCE);
		return loadText(is, TEXT_USE_CACHE);
	}

	/**
	 * Load external text resource.
	 * 
	 * @param aIs the InputSteam of text resource file
	 * @param aUseCache use internal cache or not
	 * @return true when load successful
	 */
	private static boolean loadText(InputStream aIs, boolean aUseCache) {

		if (aIs != null) {

			Log.d(TAG, "Load text resource...");

			DataInputStream dis = new DataInputStream(aIs);
			try {

				int count = dis.readInt();
				if (count <= 0) {
					return false;
				}

				// read text encoding
				byte encoding = dis.readByte();
				// read text (all texts are merged into a huge text)
				if (encoding == UTF_8_ENCODING) {
					gText = dis.readUTF();
				} else {

					// read length of chars
					int len = dis.readInt();

					// read chars
					char[] buf = new char[len];
					for (int i = 0; i < len; ++i) {
						buf[i] = dis.readChar();
					}

					gText = new String(buf);
				}

				// read length table to generate start/end index to separate
				// text
				gOffsets = new int[count + 1];

				for (int i = 0, offset = 0; i < count; ++i, gOffsets[i] = offset) {
					offset += dis.readShort();
				}

				if (aUseCache) {
					gTextCache = new String[count];
				}

				Log.d(TAG, "Load text resource finished.");
				return true;
			} catch (IOException e) {
				Log.e(e);
			} finally {
				Platform.closeStream(dis);
			}
		}

		return false;
	}

	/**
	 * Get the text string by its index.
	 * 
	 * @param aIdx the index of text
	 * @return text string
	 * @see T
	 */
	public static String getText(int aIdx) {

		if (gText == null || gOffsets == null) {
			return "";
		}

		if (gTextCache == null) {
			return gText.substring(gOffsets[aIdx], gOffsets[aIdx + 1]);
		}

		if (gTextCache[aIdx] == null) {
			gTextCache[aIdx] = gText.substring(gOffsets[aIdx],
				gOffsets[aIdx + 1]);
		}

		return gTextCache[aIdx];
	}

	/**
	 * Get the resource image by its id.
	 * 
	 * @param aImgId id of image resource
	 * @param aCache choose cache the image or not
	 * @return image
	 */
	public static Image getImage(int aImgId, boolean aCache) {

		if (gImageCache[aImgId] == null) {

			String resName = gImageResources[aImgId];
			InputStream resIs = Resource.class.getResourceAsStream(resName);

			// load
			try {

				Image resImg = Image.createImage(resIs);
				if (aCache) {
					gImageCache[aImgId] = resImg;
				} else {
					return resImg;
				}
			} catch (IOException e) {
				Log.e(e);
				return Image.createImage(1, 1);// return an empty 1x1 image
			} finally {
				Platform.closeStream(resIs);
			}
		}

		return gImageCache[aImgId];
	}

	public static void removeCachedImage(int aImgId) {

		gImageCache[aImgId] = null;
	}
	
	public static void removeAllCachedImage() {
		
		for (int i = 0; i < gImageCache.length; ++i) {
			gImageCache[i] = null;
		}
	}
}
