package eu.ismailozer.easyfilemanager;

public class ConverterUtilityHelper {

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase(ConstantsGlobal.LOCALE_TR).toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				// chars[i] = Character.toUpperCase(chars[i]);
				// tr I dont work otherwise
				chars[i] = Character.toString(chars[i]).toUpperCase(ConstantsGlobal.LOCALE_TR).charAt(0);
				// chars[i] = lStr.charAt(0);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\''
					|| !Character.isLetter(chars[i])) {
				found = false;
			}
		}
		return String.valueOf(chars);
	}
	
	public static int getIntValue(String pString) {
		if (pString.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(pString.toString());
	}	

}
