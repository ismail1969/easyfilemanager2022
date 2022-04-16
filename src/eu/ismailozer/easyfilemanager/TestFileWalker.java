package eu.ismailozer.easyfilemanager;

import java.util.List;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;

public class TestFileWalker {

	// private void logTimeStamp() {
	//
	// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//
	// dd/MM/yyyy
	// // Date now = new Date();
	// // String strDate = sdfDate.format(new Date());
	// System.out.println(">>> Time: " + sdfDate.format(new Date()));
	// }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ProcessTimeController.start();

		// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//
		// dd/MM/yyyy
		// Date now = new Date();
		// String strDate = sdfDate.format(new Date());

		System.out.println("Start");
		List<String> pFileLists = new ArrayList<String>();
		pFileLists = Utility.getFileLists("C:\\Musik\\", true, true, true, 999999);
		for (int i = 0; i < pFileLists.size(); i++) {
			System.out.println(i + ": " + pFileLists.get(i));
		}
		System.out.println("Ende");

		ProcessTimeController.end();
		// progress.stop();
		// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//
		// dd/MM/yyyy
		// Date now = new Date();
		// String strDate = sdfDate.format(new Date());
		System.out.println(">>> Time: " + ProcessTimeController.getProzessTime());
	}

}
