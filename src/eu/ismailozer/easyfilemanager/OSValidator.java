package eu.ismailozer.easyfilemanager;

/**
 * OSValidator class
 */
public class OSValidator {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isLinux() {
		return OS.contains("linux");
	}

	public static boolean isMac() {
		return (OS.contains("mac"));
	}

	public static boolean isSolaris() {
		return (OS.contains("sunos"));
	}

	public static boolean isUnix() {
		return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
	}

	public static boolean isWindows() {
		return (OS.contains("win"));
	}
}