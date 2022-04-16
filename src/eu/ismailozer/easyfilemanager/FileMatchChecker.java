package eu.ismailozer.easyfilemanager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMatchChecker {

	// private String startsWith;
	// private String endsWith;
	// private String containStr;
	// private String searchPattern;
	// private int fileSize;
	// //private boolean allFileSize;
	// private boolean fileSizeGreater;
	// private boolean fileSizeLower;
	// private boolean fileSizeEqual;
	// private Date creationDateFrom;
	// private Date creationDateTo;
	// private boolean successiveUppercaseChars;

	private static final java.util.Locale LOCALE_TR = new java.util.Locale("tr");
	private SimpleDateFormat sDateFormt = new SimpleDateFormat("dd.MM.yyyy");

	private FileSearchParameters parameterObject;

	public FileMatchChecker(FileSearchParameters parameterObject) {
		this.parameterObject = parameterObject;
	}

	public boolean matchAllParameter(File pFilename) {
		if (this.parameterObject == null || pFilename == null) {
			return true;
		}

		// no checks if fields are empty
		// String searchedFileName = pFilename;// pSearchedFile.getName();

		if (!matchStartsWith(pFilename)) {
			return false;
		}

		if (!matchEndsWith(pFilename)) {
			return false;
		}

		if (!containString(pFilename)) {
			return false;
		}

		if (!matchPattern(pFilename)) {
			return false;
		}

		// check SuccessiveUppercaseChars

		if (!isSuccessiveUppercaseChars(pFilename)) {
			return false;
		}

		// check Attributes
		if (!matchFileSizeAttribute(pFilename)) {
			return false;
		}

		if (!matchCreationDate(pFilename)) {
			return false;
		}

		return true;
	}

	public boolean matchEndsWith(File pFilename) {
		if (pFilename == null) {
			return true;
		}
		if (parameterObject != null && !parameterObject.endsWith.isEmpty() && !pFilename.getName()
				.toLowerCase(LOCALE_TR).endsWith(parameterObject.endsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	public boolean matchStartsWith(File pFilename) {
		if (pFilename == null) {
			return true;
		}
		if (parameterObject != null && !parameterObject.startsWith.isEmpty() && !pFilename.getName()
				.toLowerCase(LOCALE_TR).startsWith(parameterObject.startsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	public boolean containString(File pFilename) {
		if (pFilename == null) {
			return true;
		}
		if (parameterObject != null && !parameterObject.containStr.isEmpty() && !pFilename.getName()
				.toLowerCase(LOCALE_TR).contains(parameterObject.containStr.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	public boolean matchPattern(File pFilename) {
		if (pFilename == null) {
			return true;
		}
		if (parameterObject != null && !parameterObject.searchPattern.isEmpty()) {
			Pattern pattern = Pattern.compile(parameterObject.searchPattern);
			Matcher matcher = pattern.matcher(pFilename.getName());
			return matcher.find();
		}
		return true;
	}

	public boolean isSuccessiveUppercaseChars(File pFilename) {
		if (pFilename == null) {
			return true;
		}
		if (!parameterObject.successiveUppercaseChars) {
			return true; // no check
		}
		if (pFilename.getName().length() <= 1) {
			return false;
		}

		int preview_value = 0;
		int current_value = 0;
		for (int i = 0; i < pFilename.getName().length(); i++) {
			// char c = pText.charAt(i);
			current_value = pFilename.getName().codePointAt(i);
			if (preview_value > 0) {
				if (Character.isUpperCase(preview_value) && Character.isUpperCase(current_value)) {
					return true;
				}
			}
			preview_value = current_value;
			// if(c >= 97 && c <= 122) {
			// return false;
			// }
		}
		// str.charAt(index)
		return false;
	}

	public boolean matchFileSizeAttribute(File pFilename) {
		if (pFilename == null || parameterObject.fileSize <= 0) {
			return true; // no check
		}
		if (parameterObject.fileSize >= 0) {
			float searched_filesize = parameterObject.fileSize; // Float.parseFloat(pFileSize.toString());

			// float lfloat_1024 = 1024;
			float filesize = getFileSize((pFilename));
			if (parameterObject.fileSizeGreater) {
				if (filesize < searched_filesize) {
					return false;
				}

			} else if (parameterObject.fileSizeLower) {
				if (filesize > searched_filesize) {
					return false;
				}
				// return !(filesize < searched_filesize);
			} else if (parameterObject.fileSizeEqual) {
				return (filesize == searched_filesize);

			} else {
				return false;
			}
		}
		return true;
	}

	public int getFileSize(File pFile) {
		return (int) Math.ceil(pFile.length() / (float) 1024);
	}

	public boolean matchCreationDate(File pFilename) {
		if (pFilename == null) {
			return true; // no check
		}

		if (parameterObject.creationDateFrom == null && parameterObject.creationDateTo == null) {
			return true; // no check
		}

		if (parameterObject.creationDateFrom != null || parameterObject.creationDateTo != null) {
			Date from_date = null;
			Date to_date = null;
			Date file_date = null;
			file_date = new Date(pFilename.lastModified());
			getsDateFormt().format(file_date);

			if (parameterObject.creationDateFrom != null) {
				from_date = parameterObject.creationDateFrom; // getDateFromString(pCreationDateFrom, getsDateFormt());
				getsDateFormt().format(from_date);
			}
			if (parameterObject.creationDateTo != null) {
				to_date = parameterObject.creationDateTo; // getDateFromString(pCreationDateTo, getsDateFormt());
				getsDateFormt().format(to_date);
				Calendar cal = Calendar.getInstance();
				cal.setTime(to_date);
				// cal.set(Calendar.HOUR_OF_DAY, 23);
				// cal.set(Calendar.MINUTE, 59);
				// cal.set(Calendar.SECOND, 59);
				// cal.set(Calendar.MILLISECOND, 999); // credit to f1sh
				cal.add(Calendar.DATE, 1); // next day, check below < for
											// 23:59:59
				to_date = cal.getTime();
			}

			if (from_date != null && to_date != null) {
				return from_date.getTime() <= file_date.getTime() && file_date.getTime() < to_date.getTime();
			} else if (from_date != null && to_date == null) {
				return file_date.getTime() >= from_date.getTime();
			} else if (from_date == null && to_date != null) {
				return file_date.getTime() < to_date.getTime();
			}
		}
		return true;
	}

	public SimpleDateFormat getsDateFormt() {
		return sDateFormt;
	}
}
