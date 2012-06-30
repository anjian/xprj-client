/**
 * Tiny.cn.uc.io.File.java, 2011-1-14
 * 
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.uc.util.ArrayUtils;
import cn.uc.util.Platform;
import cn.uc.util.StringUtils;
import cn.uc.util.debug.Log;

/**
 * Wrapper Class of MIDP's FileConnection.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
public abstract class File {

	public static final int READ = 1;
	public static final int WRITE = 2;
	public static final int READ_WRITE = 3;

	public static final String FILE_SEPARATOR = "/";

	public static final int UNKNOWN_FILE_IMPL = -1;
	public static final int JSR75_FILE_IMPL = 0;
	public static final int MOTO_FILE_IMPL = 1;
	public static final int SIEMENS_FILE_IMPL = 2;

	public static final int gFileImplFlag;

	public static String[] gRootPaths;

	static {

		if (Platform.isClassExists("javax.microedition.io.file.FileConnection")) {
			gFileImplFlag = JSR75_FILE_IMPL;
		} else if (Platform.isClassExists("com.motorola.io.FileConnection")) {
			gFileImplFlag = MOTO_FILE_IMPL;
		} else if (Platform
			.isClassExists("com.siemens.mp.io.file.FileConnection")) {
			gFileImplFlag = SIEMENS_FILE_IMPL;
		} else {
			gFileImplFlag = UNKNOWN_FILE_IMPL;
		}
	}

	public static boolean hasFileApi() {

		return gFileImplFlag != UNKNOWN_FILE_IMPL;
	}

	public static String getRootPath() {

		if (gRootPaths == null) {

			try {
				switch (gFileImplFlag) {

				case JSR75_FILE_IMPL:
					gRootPaths = (new Jsr75FileImpl()).getRootPaths();
					break;

				case MOTO_FILE_IMPL:
					gRootPaths = (new MotoFileImpl()).getRootPaths();
					break;

				case SIEMENS_FILE_IMPL:
					gRootPaths = (new SiemensFileImpl()).getRootPaths();
					break;
				}
			} catch (Exception e) {
				Log.e(e);
			}

			if (gRootPaths == null) {
				gRootPaths = ArrayUtils.EMPTY_STRING_ARRAY;
			}
		}
		
		return gRootPaths.length > 0 ? gRootPaths[0] : StringUtils.EMPTY;
	}

	public static void mkdir(String aLevel1Dir, String aLevel2Dir,
		String aLevel3Dir) throws SecurityException, IOException {

		String root = getRootPath();
		StringBuffer sb = new StringBuffer("file:///").append(root)
			.append(aLevel1Dir).append(FILE_SEPARATOR);

		File file = create(sb.toString(), READ_WRITE);
		if (!file.exists()) {
			file.mkdir();
		}
		Platform.closeFile(file);

		sb.append(aLevel2Dir).append(FILE_SEPARATOR);
		file = create(sb.toString(), READ_WRITE);

		if (!file.exists()) {
			file.mkdir();
		}
		Platform.closeFile(file);

		sb.append(aLevel3Dir).append(FILE_SEPARATOR);
		file = create(sb.toString(), READ_WRITE);
		if (!file.exists()) {
			file.mkdir();
		}
		Platform.closeFile(file);
	}

	public static File create(String aPath) throws IOException,
		SecurityException {

		switch (gFileImplFlag) {

		case JSR75_FILE_IMPL:
			return new Jsr75FileImpl(aPath);

		case MOTO_FILE_IMPL:
			return new MotoFileImpl(aPath);

		case SIEMENS_FILE_IMPL:
			return new SiemensFileImpl(aPath);

		default:
			throw new IOException();
		}
	}

	public static File create(String aPath, int aMode) throws IOException,
		SecurityException {

		switch (gFileImplFlag) {

		case JSR75_FILE_IMPL:
			return new Jsr75FileImpl(aPath);

		case MOTO_FILE_IMPL:
			return new MotoFileImpl(aPath);

		case SIEMENS_FILE_IMPL:
			return new SiemensFileImpl(aPath);

		default:
			throw new IOException();
		}
	}

	public abstract String[] getRootPaths();

	public abstract void close() throws IOException;

	public abstract long availableSize();

	public abstract boolean canRead();

	public abstract boolean canWrite();

	public abstract void create() throws IOException;

	public abstract void delete() throws IOException;

	public abstract long directorySize(boolean arg0) throws IOException;

	public abstract boolean exists();

	public abstract long fileSize() throws IOException;

	public abstract String getName();

	public abstract String getPath();

	public abstract String getURL();

	public abstract boolean isDirectory();

	public abstract boolean isHidden();

	public abstract boolean isOpen();

	public abstract long lastModified();

	public abstract String[] list() throws IOException;

	public abstract String[] list(String arg0, boolean arg1) throws IOException;

	public abstract void mkdir() throws IOException;

	public abstract InputStream openInputStream() throws IOException;

	public abstract OutputStream openOutputStream() throws IOException;

	public abstract OutputStream openOutputStream(long aArg) throws IOException;

	public abstract DataInputStream openDataInputStream() throws IOException;

	public abstract DataOutputStream openDataOutputStream() throws IOException;

	public abstract void rename(String arg0) throws IOException;

	public abstract void setFileConnection(String arg0) throws IOException;

	public abstract void setHidden(boolean arg0) throws IOException;

	public abstract void setReadable(boolean arg0) throws IOException;

	public abstract void setWritable(boolean arg0) throws IOException;

	public abstract long totalSize();

	public abstract void truncate(long arg0) throws IOException;

	public abstract long usedSize();
}
