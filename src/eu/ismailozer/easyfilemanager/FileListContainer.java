package eu.ismailozer.easyfilemanager;

import java.util.ArrayList;
import java.util.List;

public class FileListContainer {

	public FileListContainer() {
		super();
		// TODO Auto-generated constructor stub
		fileList = new ArrayList<String>();
		fileSize = 0;
		directorySize = 0;
	}

	private List<String> fileList;
	private int fileSize;
	private int directorySize;
	// private long lastModified;
	// private long length;
	// private boolean isFile;
	// private boolean isDirectory;

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getDirectorySize() {
		return directorySize;
	}

	public void setDirectorySize(int directorySize) {
		this.directorySize = directorySize;
	}

	public void addToList(String pFile) {
		if (fileList != null) {
			fileList.add(pFile);
		}
	}

	public void incrementFileSize() {
		fileSize++;
	}

	public void incrementDirectorySize() {
		directorySize++;
	}
}