package eu.ismailozer.easyfilemanager.not_used;

import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

public class FileInfos implements Comparable<FileInfos> {

	// private List<String> fileList;
	// private int fileSize;
	// private int directorySize;
	private String canonicalPath;
	private long lastModified;
	private long length;
	private boolean isFile;
	private boolean isDirectory;

	public FileInfos(String pFilename) {
		super();
		// TODO Auto-generated constructor stub
		File file = new File(pFilename);
		try {
			setCanonicalPath(file.getCanonicalPath());
			setLastModified(file.lastModified());
			setLength(file.length());
			setFile(file.isFile());
			setDirectory(file.isDirectory());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCanonicalPath() {
		return canonicalPath;
	}

	public void setCanonicalPath(String canonicalPath) {
		this.canonicalPath = canonicalPath;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@Override
	public int compareTo(FileInfos other) {
		// TODO Auto-generated method stub
		// return 0;

		// TODO Auto-generated method stub
		int retval;
		if (this.length == other.length) {
			retval = 0;// return 0;
		} else if ((this.length) > other.length) {
			retval = 1; // return 1;
		} else {
			retval = -1; // return -1;
		}
		if (retval != 0) {
			return retval;
		}

		// compare filenamelength

		if (this.length == other.length) {
			retval = 0;// return 0;
		} else if ((this.length) > other.length) {
			retval = 1; // return 1;
		} else {
			retval = -1; // return -1;
		}
		if (retval != 0) {
			return retval;
		}

		// compare filenames
		retval = canonicalPath.compareTo(other.canonicalPath);
		if (retval != 0)
			return retval;
		return retval;
	}

	// public List<String> getFileList() {
	// return fileList;
	// }
	//
	// public void setFileList(List<String> fileList) {
	// this.fileList = fileList;
	// }
	//
	// public int getFileSize() {
	// return fileSize;
	// }
	//
	// public void setFileSize(int fileSize) {
	// this.fileSize = fileSize;
	// }
	//
	// public int getDirectorySize() {
	// return directorySize;
	// }
	//
	// public void setDirectorySize(int directorySize) {
	// this.directorySize = directorySize;
	// }
	//
	// public void addToList(String pFile) {
	// if (fileList != null) {
	// fileList.add(pFile);
	// }
	// }
	//
	// public void incrementFileSize() {
	// fileSize++;
	// }
	//
	// public void incrementDirectorySize() {
	// directorySize++;
	// }
}