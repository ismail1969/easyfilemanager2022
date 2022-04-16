package eu.ismailozer.easyfilemanager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRenamer {

	private static final java.util.Locale LOCALE_TR = new java.util.Locale("tr");

	public String getRenamedFilename(File pFilename, String pRegexpReplaceOld, String pRegexpReplaceNew,
			String pReplaceAll, String pReplaceOld, String pReplaceNew, String pPrefix, String pSuffix,
			boolean pRenameLowerCase, boolean pRenameUpperCase, boolean pRenameCapitalize,
			boolean pRenameParentAsFilename, String pSequence, String pLeadZero, int pSequenceIndex) {
		String lNewFile = "";
		// File filename = new File(pFilename);
		String lParent = pFilename.getParent();

		// String lFileNameWithoutExtension = pFile.getName();
		String lFileNameWithoutExtension = pFilename.getName();
		String lFileExtension = "";
		// if we have point at end of file
		int indx = pFilename.getName().lastIndexOf('.');
		if (indx > 0) {
			lFileNameWithoutExtension = pFilename.getName().substring(0, indx);
			lFileExtension = pFilename.getName().substring(indx + 1);
		}

		if (!pReplaceAll.isEmpty()) {
			lNewFile = pReplaceAll;
		} else if (!pReplaceOld.isEmpty()
		/* && !txtReplaceOld.getText().isEmpty() */) {
			if (pReplaceNew.isEmpty()) {
				pReplaceNew = ""; // TODO???
			}
			lNewFile = replace(lFileNameWithoutExtension, pReplaceOld, pReplaceNew);
			lFileExtension = replace(lFileExtension, pReplaceOld, pReplaceNew);

		} else if (!pRegexpReplaceNew.isEmpty()) {
			lNewFile = getNewFilenameFromRegExpReplacement(pRegexpReplaceOld, pRegexpReplaceNew,
					lFileNameWithoutExtension);
		} else {
			lNewFile = lFileNameWithoutExtension;
		}

		if (!pPrefix.isEmpty()) {
			lNewFile = pPrefix + lNewFile;
		}
		if (!pSuffix.isEmpty()) {
			lNewFile = lNewFile + pSuffix;
		}
		if (pRenameLowerCase) {
			lNewFile = lNewFile.toLowerCase(LOCALE_TR);
			if (lFileExtension.length() > 0) {
				lFileExtension.toLowerCase(LOCALE_TR);
			}
		}
		if (pRenameUpperCase) {
			lNewFile = lNewFile.toUpperCase(LOCALE_TR);
			if (lFileExtension.length() > 0) {
				lFileExtension.toUpperCase(LOCALE_TR);
			}
		}
		if (pRenameCapitalize) {
			// lNewFile = EasyUtility.capitalize(lNewFile);
			lNewFile = capitalizeString(lNewFile);
			if (lFileExtension.length() > 0) {
				// lFileExtension = capitalizeString(lFileExtension);
			}
		}
		if (pRenameParentAsFilename) {
			lNewFile = getNewFilenameFromParent(lParent, lNewFile, lFileNameWithoutExtension);
		}
		String lSequence = "";
		if (!pLeadZero.equals("None")) {
			int lSeqNo = 0;
			if (pSequence.length() > 0) {
				lSeqNo = Integer.parseInt(pSequence);
			}
			int lZeros = Integer.parseInt(pLeadZero);
			lSequence = getNameSequence(lSeqNo + pSequenceIndex, lZeros);
			lNewFile = lNewFile + lSequence;
		}
		lNewFile = lParent + getFileSeparator() + lNewFile;
		if (lFileExtension != null && lFileExtension.length() > 0) {
			lNewFile = lNewFile + "." + lFileExtension;
		}
		return lNewFile;
	}

	public String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	public String getNewFilenameFromRegExpReplacement(String pSearchedRegExpress, String pReplaceRegExpress,
			String pStringToChange) {
		if (pSearchedRegExpress == null || pStringToChange == null) {
			return null;
		}
		Pattern pattern = Pattern.compile(pSearchedRegExpress);
		Matcher matcher = pattern.matcher(pStringToChange);
		return matcher.replaceAll(pReplaceRegExpress);
	}

	public String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	private static String getNameSequence(int i, int s) {
		String s1 = (new StringBuilder()).append("%0").append(s).append("d").toString();
		return String.format(s1, i);
	}

	public String getNewFilenameFromParent(String pParent, String lNewFile, String pOldFilename) {
		// C:\Temp\subdir1\subdir2\myfile3.txt -> filename =C subdir2
		int ind = pParent.lastIndexOf(getFileSeparator());
		if (ind == -1) {
			return lNewFile;
		}
		String lParentName = pParent.substring(ind + 1);
		return replaceFirst(lNewFile, pOldFilename, lParentName);
	}

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase(LOCALE_TR).toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				// chars[i] = Character.toUpperCase(chars[i]);
				// tr I dont work otherwise
				chars[i] = Character.toString(chars[i]).toUpperCase(LOCALE_TR).charAt(0);
				// chars[i] = lStr.charAt(0);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\''
					|| !Character.isLetter(chars[i])) {
				found = false;
			}
		}
		return String.valueOf(chars);
	}

	public static String replaceFirst(String s, String sub, String with) {
		int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

}
