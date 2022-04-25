package eu.ismailozer.easyfilemanager.not_used;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMatcher {

	private static final java.util.Locale LOCALE_TR = new java.util.Locale("tr");
	private SimpleDateFormat sDateFormt = new SimpleDateFormat("dd.MM.yyyy");

	// private String filename;
	// private String regexpReplaceOld;
	// private String regexpReplaceNew;
	// private String replaceAll;
	// private String replaceOld;
	// private String replaceNew;
	// private String prefix;
	// private String suffix;
	// private boolean renameLowerCase;
	// private boolean renameUpperCase;
	// private boolean renameCapitalize;
	// private boolean renameParentAsFilename;
	// private String sequence;
	// private String leadZero;
	// private int sequenceIndex;

	public boolean isSearchedFileMatch(String pFilename, boolean pResetCounterValue, String pStartsWith,
			String pEndsWith, String pContainStr, String pSearchPattern, String pFileSize, boolean pAllFileSize,
			boolean pFileSizeGreater, boolean pFileSizeLower, boolean pFileSizeEqual, String pCreationDateFrom,
			String pCreationDateTo, boolean pSuccessiveUppercaseChars) {

		// no checks if fields are empty
		String searchedFileName = pFilename;// pSearchedFile.getName();

		if (!matchStartsWith(pStartsWith, searchedFileName)) {
			return false;
		}

		if (!matchEndsWith(pEndsWith, searchedFileName)) {
			return false;
		}

		if (!containString(pContainStr, searchedFileName)) {
			return false;
		}

		if (!matchPattern(pSearchPattern, searchedFileName)) {
			return false;
		}

		// check SuccessiveUppercaseChars

		if (pSuccessiveUppercaseChars && !isSuccessiveUppercaseChars(searchedFileName)) {
			return false;
		}

		// check Attributes
		if (!matchFileSizeAttribute(searchedFileName, pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower,
				pFileSizeEqual)) {
			return false;
		}

		if (!matchCreationDate(searchedFileName, pCreationDateFrom, pCreationDateTo)) {
			return false;
		}

		return true;
	}

	private boolean containString(String pContainStr, String searchedFileName) {
		if (!pContainStr.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).contains(pContainStr.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	private boolean matchFileSizeAttribute(String pSearchedFile, String pFileSize, boolean pAllFileSize,
			boolean pFileSizeGreater, boolean pFileSizeLower, boolean pFileSizeEqual) {
		if (!pFileSize.isEmpty() && !pAllFileSize) {
			float searched_filesize = Float.parseFloat(pFileSize.toString());

			// float lfloat_1024 = 1024;
			float filesize = getFileSize((new File(pSearchedFile)));
			if (pFileSizeGreater) {
				if (filesize < searched_filesize) {
					return false;
				}

			} else if (pFileSizeLower) {
				if (filesize > searched_filesize) {
					return false;
				}
				// return !(filesize < searched_filesize);
			} else if (pFileSizeEqual) {
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

	public float getSearchedFileSize(String pFileSize) {
		return Float.parseFloat(pFileSize.toString());
	}

	private boolean matchEndsWith(String pEndsWith, String searchedFileName) {
		if (!pEndsWith.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).endsWith(pEndsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	private boolean matchStartsWith(String pStartsWith, String searchedFileName) {
		if (!pStartsWith.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).startsWith(pStartsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	private boolean matchCreationDate(String pSearchedFile, String pCreationDateFrom, String pCreationDateTo) {

		if (!pCreationDateFrom.isEmpty() || !pCreationDateTo.isEmpty()) {
			Date from_date = null;
			Date to_date = null;
			Date file_date = null;
			file_date = new Date((new File(pSearchedFile)).lastModified());
			getsDateFormt().format(file_date);

			if (!pCreationDateFrom.isEmpty()) {
				from_date = getDateFromString(pCreationDateFrom, getsDateFormt());
			}
			if (!pCreationDateTo.isEmpty()) {
				to_date = getDateFromString(pCreationDateTo, getsDateFormt());
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

	public boolean isSuccessiveUppercaseChars(String pText) {
		if (pText.length() <= 1) {
			return false;
		}

		int preview_value = 0;
		int current_value = 0;
		for (int i = 0; i < pText.length(); i++) {
			// char c = pText.charAt(i);
			current_value = pText.codePointAt(i);
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

	public Date getDateFromString(String pDateText, SimpleDateFormat pDateFormater) {
		Date date;
		try {
			date = pDateFormater.parse(pDateText);
			pDateFormater.format(date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SimpleDateFormat getsDateFormt() {
		return sDateFormt;
	}

	private boolean matchPattern(String pPattern, String pInputStr) {
		if (pPattern.isEmpty() || pInputStr.isEmpty()) {
			return true;
		}
		Pattern pattern = Pattern.compile(pPattern);
		Matcher matcher = pattern.matcher(pInputStr);
		return matcher.find();
	}

}
