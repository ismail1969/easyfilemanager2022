package eu.ismailozer.easyfilemanager;

public class ProcessTimeController {

	private static long startTime;
	private static long endTime;

	public ProcessTimeController() {

	}

	public static void start() {
		startTime = System.currentTimeMillis();
		endTime = 0;
	}

	public static void end() {
		endTime = System.currentTimeMillis();
	}

	public static String getProzessTime() {
		return Long.toString((endTime - startTime) / 1000);
	}

	public static void printProzessTime() {
		System.out.println("Process time: " + ((endTime - startTime) / 1000) + " [s]");
		endTime = 0;
		startTime = 0;
	}

	public static String getElapsedTime() {
		return Long.toString((System.currentTimeMillis() - startTime) / 1000);
	}
}
