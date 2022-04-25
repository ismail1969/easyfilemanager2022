package eu.ismailozer.easyfilemanager;

import java.io.File;

public class DublicateFiles {

	// private int number;
	private File leftFile;
	private File rightFile;

	public DublicateFiles(File leftFile, File rightFile) {
		super();
		// this.setNumber(number);
		this.setLeftFile(leftFile);
		this.setRightFile(rightFile);
	}

	// public int getNumber() {
	// return number;
	// }

	// public void setNumber(int number) {
	// this.number = number;
	// }

	public File getLeftFile() {
		return leftFile;
	}

	public File getRightFile() {
		return rightFile;
	}

	public void setLeftFile(File leftFile) {
		this.leftFile = leftFile;
	}

	public void setRightFile(File rightFile) {
		this.rightFile = rightFile;
	}
}
