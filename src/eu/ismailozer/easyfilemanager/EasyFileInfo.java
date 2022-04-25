package eu.ismailozer.easyfilemanager;

import java.io.File;

public class EasyFileInfo {

	private long filelength;

	private File file;

	public EasyFileInfo(long filelength, File file) {
		super();
		this.filelength = filelength;
		this.setFile(file);
	}

	public File getFile() {
		return file;
	}

	public long getFilelength() {
		return filelength;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}
}
