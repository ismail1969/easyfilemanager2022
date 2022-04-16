package eu.ismailozer.easyfilemanager;

import java.io.File;

public class EasyFileInfo {

	public EasyFileInfo(long filelength, File file) {
		super();
		this.filelength = filelength;
		this.setFile(file);
	}

	private long filelength;
	private File file;

	public long getFilelength() {
		return filelength;
	}

	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
