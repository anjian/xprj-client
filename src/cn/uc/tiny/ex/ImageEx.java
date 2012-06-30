/**
 * Tiny.cn.uc.ui.ex.ImageEx.java, 2011-1-20
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.tiny.ex;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.lcdui.Image;

import cn.uc.util.BitUtils;
import cn.uc.util.Platform;
import cn.uc.util.io.File;

/**
 * Encapsulation of MIDP's {@link Image}.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public final class ImageEx {

	private final Image img;

	/**
	 * @see {@link Image#createImage(int, int)}
	 */
	public static ImageEx createImage(int width, int height) {

		return new ImageEx(Image.createImage(width, height));
	}

	public static ImageEx createImage(ImageEx source) {

		return new ImageEx(Image.createImage(source.img));
	}

	public static ImageEx createImage(String name) throws IOException {

		return new ImageEx(Image.createImage(name));
	}

	public static ImageEx createImage(byte[] imageData, int imageOffset,
		int imageLength) {

		return new ImageEx(Image.createImage(imageData, imageOffset,
			imageLength));
	}

	public static ImageEx createImage(ImageEx image, int x, int y, int width,
		int height, int transform) {

		return new ImageEx(Image.createImage(image.img, x, y, width, height,
			transform));
	}

	public static ImageEx createImage(InputStream stream) throws IOException {

		return new ImageEx(Image.createImage(stream));
	}

	public static ImageEx createRGBImage(int rgb[], int width, int height,
		boolean processAlpha) {

		return new ImageEx(Image.createRGBImage(rgb, width, height,
			processAlpha));
	}

	private ImageEx(Image aImage) {

		img = aImage;
	}

	public GraphicsEx getGraphics() {

		return new GraphicsEx(img.getGraphics());
	}

	public int getWidth() {

		return img.getWidth();
	}

	public int getHeight() {

		return img.getHeight();
	}

	public boolean isMutable() {

		return img.isMutable();
	}

	public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y,
		int width, int height) {

		img.getRGB(rgbData, offset, scanlength, x, y, width, height);
	}

	public void getMargins(int aBgColor, int aFgColor, int[] aOutMargins) {

		int w = getWidth();
		int h = getHeight();
		int x = 0, y = 0;

		int[] rgb = new int[Math.max(w, h)];
		int color;
		boolean found = false;

		aOutMargins[0] = -1;
		aOutMargins[1] = -1;
		aOutMargins[2] = -1;
		aOutMargins[3] = -1;

		// find left margin
		found = false;
		for (x = 0; x < w && !found; ++x) {

			getRGB(rgb, 0, 1, x, 0, 1, h);
			for (y = 0; y < h; ++y) {

				color = rgb[y];
				if (Math.abs(color - aBgColor) > Math.abs(color - aFgColor)) {
					aOutMargins[0] = x;
					found = true;
					break;
				}
			}
		}

		// find right margin
		found = false;
		for (x = w - 1; x != 0 && !found; --x) {

			getRGB(rgb, 0, 1, x, 0, 1, h);
			for (y = 0; y < h; ++y) {

				color = rgb[y];
				if (Math.abs(color - aBgColor) > Math.abs(color - aFgColor)) {
					aOutMargins[2] = w - 1 - x;
					found = true;
					break;
				}
			}
		}

		// find top margin
		found = false;
		for (y = 0; y < h && !found; ++y) {

			getRGB(rgb, 0, w, 0, y, w, 1);
			for (x = 0; x < w; ++x) {

				color = rgb[x];
				if (Math.abs(color - aBgColor) > Math.abs(color - aFgColor)) {
					aOutMargins[1] = y;
					found = true;
					break;
				}
			}
		}

		// find bottom margin
		found = false;
		for (y = h - 1; y != 0 && !found; --y) {

			getRGB(rgb, 0, w, 0, y, w, 1);
			for (x = 0; x < w; ++x) {

				color = rgb[x];
				if (Math.abs(color - aBgColor) > Math.abs(color - aFgColor)) {
					aOutMargins[3] = h - 1 - y;
					found = true;
					break;
				}
			}
		}
	}

	public void save(String aPath) throws IOException {

		File file = File.create(aPath, File.READ_WRITE);
		this.save(file);
	}

	public void save(File aFile) throws IOException {

		try {

			if (aFile != null) {
				if (!aFile.exists()) {
					aFile.create();
				}
				this.save(aFile.openDataOutputStream());
			}
		} finally {
			Platform.closeFile(aFile);
		}
	}

	public void save(OutputStream aOut) throws IOException {

		this.save(new DataOutputStream(aOut));
	}

	public void save(DataOutputStream aDos) throws IOException {

		int width = getWidth();
		int height = getHeight();
		int scanlength = (width * 3 + 3) / 4 * 4;

		final int BITMAPFILEHEADER_SIZE = 14;
		final int BITMAPINFOHEADER_SIZE = 40;

		// --- Bitmap file header
		// byte[] bitmapFileHeader = new byte[14];
		byte[] bfType = { (byte) 'B', (byte) 'M' };
		int bfSize = scanlength * height + BITMAPFILEHEADER_SIZE
			+ BITMAPINFOHEADER_SIZE;
		short bfReserved1 = 0;
		short bfReserved2 = 0;
		int bfOffBits = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;

		// --- Bitmap info header
		// byte[] bitmapInfoHeader = new byte[40];
		int biSize = BITMAPINFOHEADER_SIZE;
		int biWidth = width;
		int biHeight = -height;
		short biPlanes = 1;
		short biBitCount = 24;
		int biCompression = 0;
		int biSizeImage = 0;
		int biXPelsPerMeter = 3780;
		int biYPelsPerMeter = 3780;
		int biClrUsed = 0;
		int biClrImportant = 0;

		int[] rgbData = new int[width];
		byte[] data = new byte[scanlength];

		try {

			// write file header
			aDos.write(bfType);
			aDos.writeInt(BitUtils.reverseBytes(bfSize));
			aDos.writeShort(BitUtils.reverseBytes(bfReserved1));
			aDos.writeShort(BitUtils.reverseBytes(bfReserved2));
			aDos.writeInt(BitUtils.reverseBytes(bfOffBits));

			// write info header
			aDos.writeInt(BitUtils.reverseBytes(biSize));
			aDos.writeInt(BitUtils.reverseBytes(biWidth));
			aDos.writeInt(BitUtils.reverseBytes(biHeight));
			aDos.writeShort(BitUtils.reverseBytes(biPlanes));
			aDos.writeShort(BitUtils.reverseBytes(biBitCount));
			aDos.writeInt(BitUtils.reverseBytes(biCompression));
			aDos.writeInt(BitUtils.reverseBytes(biSizeImage));
			aDos.writeInt(BitUtils.reverseBytes(biXPelsPerMeter));
			aDos.writeInt(BitUtils.reverseBytes(biYPelsPerMeter));
			aDos.writeInt(BitUtils.reverseBytes(biClrUsed));
			aDos.writeInt(BitUtils.reverseBytes(biClrImportant));

			// write data
			for (int y = 0; y < height; ++y) {

				getRGB(rgbData, 0, width, 0, y, width, 1);

				for (int x = 0; x < width; ++x) {

					BitUtils.reverseTo3Bytes(rgbData[x], data, x * 3);
				}

				aDos.write(data);
			}
		} finally {
			Platform.closeStream(aDos);
		}
	}
}
