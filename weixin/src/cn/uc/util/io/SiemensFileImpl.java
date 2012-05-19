/**
 * Tiny.cn.uc.util.io.SiemensFileImpl.java, 2011-4-11
 *
 * Copyright (c) 2010, 2011 UC Mobile, All rights reserved.
 */
package cn.uc.util.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;

import cn.uc.util.CollectionUtils;

/**
 * Siemens FileConnection Wrapper implementation.
 * 
 * @author <a href="mailto:yixx@ucweb.com">Roger Yi</a>
 * @since 1.0
 * @version 1.0
 */
class SiemensFileImpl extends File {

	private com.siemens.mp.io.file.FileConnection fc;

	public SiemensFileImpl(String aPath) throws IOException, SecurityException {

		fc = (com.siemens.mp.io.file.FileConnection) Connector.open(aPath);
	}

	public SiemensFileImpl(String aPath, int aMode) throws IOException,
		SecurityException {

		fc = (com.siemens.mp.io.file.FileConnection) Connector.open(aPath,
			aMode);
	}
	
	SiemensFileImpl() {
		
	}

	public String[] getRootPaths() {

		return CollectionUtils
			.toStringArray(com.siemens.mp.io.file.FileSystemRegistry
				.listRoots());
	}

	public void close() throws IOException {

		fc.close();
	}

	public long availableSize() {

		return fc.availableSize();
	}

	public boolean canRead() {

		return fc.canRead();
	}

	public boolean canWrite() {

		return fc.canWrite();
	}

	public void create() throws IOException {

		fc.create();
	}

	public void delete() throws IOException {

		fc.delete();
	}

	public long directorySize(boolean arg0) throws IOException {

		return fc.directorySize(arg0);
	}

	public boolean exists() {

		return fc.exists();
	}

	public long fileSize() throws IOException {

		return fc.fileSize();
	}

	public String getName() {

		return fc.getName();
	}

	public String getPath() {

		return fc.getPath();
	}

	public String getURL() {

		return fc.getURL();
	}

	public boolean isDirectory() {

		return fc.isDirectory();
	}

	public boolean isHidden() {

		return fc.isHidden();
	}

	public boolean isOpen() {

		return fc.isOpen();
	}

	public long lastModified() {

		return fc.lastModified();
	}

	public String[] list() throws IOException {

		return CollectionUtils.toStringArray(fc.list());
	}

	public String[] list(String arg0, boolean arg1) throws IOException {

		return CollectionUtils.toStringArray(fc.list(arg0, arg1));
	}

	public void mkdir() throws IOException {

		fc.mkdir();
	}

	public InputStream openInputStream() throws IOException {

		return fc.openInputStream();
	}

	public OutputStream openOutputStream() throws IOException {

		return fc.openOutputStream();
	}

	public OutputStream openOutputStream(long aArg) throws IOException {

		return fc.openOutputStream(aArg);
	}

	public DataInputStream openDataInputStream() throws IOException {

		return fc.openDataInputStream();
	}

	public DataOutputStream openDataOutputStream() throws IOException {

		return fc.openDataOutputStream();
	}

	public void rename(String arg0) throws IOException {

		fc.rename(arg0);
	}

	public void setFileConnection(String arg0) throws IOException {

		fc.setFileConnection(arg0);
	}

	public void setHidden(boolean arg0) throws IOException {

		fc.setHidden(arg0);
	}

	public void setReadable(boolean arg0) throws IOException {

		fc.setReadable(arg0);
	}

	public void setWritable(boolean arg0) throws IOException {

		fc.setWritable(arg0);
	}

	public long totalSize() {

		return fc.totalSize();
	}

	public void truncate(long arg0) throws IOException {

		fc.truncate(arg0);
	}

	public long usedSize() {

		return fc.usedSize();
	}
}
