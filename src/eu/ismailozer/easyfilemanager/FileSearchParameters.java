package eu.ismailozer.easyfilemanager;

import java.util.Date;

public class FileSearchParameters {
	public String startsWith;
	public String endsWith;
	public String containStr;
	public String searchPattern;
	public int fileSize;
	public boolean fileSizeGreater;
	public boolean fileSizeLower;
	public boolean fileSizeEqual;
	public Date creationDateFrom;
	public Date creationDateTo;
	public boolean successiveUppercaseChars;

	public FileSearchParameters() {

	}

//	public FileSearchParameters(String startsWith, String endsWith, String containStr, String searchPattern,
//			int fileSize, boolean fileSizeGreater, boolean fileSizeLower, boolean fileSizeEqual, Date creationDateFrom,
//			Date creationDateTo, boolean successiveUppercaseChars) {
//		this.startsWith = startsWith;
//		this.endsWith = endsWith;
//		this.containStr = containStr;
//		this.searchPattern = searchPattern;
//		this.fileSize = fileSize;
//		this.fileSizeGreater = fileSizeGreater;
//		this.fileSizeLower = fileSizeLower;
//		this.fileSizeEqual = fileSizeEqual;
//		this.creationDateFrom = creationDateFrom;
//		this.creationDateTo = creationDateTo;
//		this.successiveUppercaseChars = successiveUppercaseChars;
//	}

	public void setParameters(String startsWith, String endsWith, String containStr, String searchPattern, int fileSize,
			boolean fileSizeGreater, boolean fileSizeLower, boolean fileSizeEqual, Date creationDateFrom,
			Date creationDateTo, boolean successiveUppercaseChars) {
		this.startsWith = startsWith;
		this.endsWith = endsWith;
		this.containStr = containStr;
		this.searchPattern = searchPattern;
		this.fileSize = fileSize;
		this.fileSizeGreater = fileSizeGreater;
		this.fileSizeLower = fileSizeLower;
		this.fileSizeEqual = fileSizeEqual;
		this.creationDateFrom = creationDateFrom;
		this.creationDateTo = creationDateTo;
		this.successiveUppercaseChars = successiveUppercaseChars;
	}

	public static FileSearchParameters getFileSearchParameters(String startsWith, String endsWith, String containStr,
			String searchPattern, int fileSize, boolean fileSizeGreater, boolean fileSizeLower, boolean fileSizeEqual,
			Date creationDateFrom, Date creationDateTo, boolean successiveUppercaseChars) {
		FileSearchParameters searchObject = new FileSearchParameters();
		searchObject.setParameters(startsWith, endsWith, containStr, searchPattern, fileSize, fileSizeGreater,
				fileSizeLower, fileSizeEqual, creationDateFrom, creationDateTo, successiveUppercaseChars);
		return searchObject;
	}
}