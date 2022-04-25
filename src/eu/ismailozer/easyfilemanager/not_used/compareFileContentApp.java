package eu.ismailozer.easyfilemanager.not_used;

import java.io.File;
import org.apache.commons.io.FileUtils;

public class compareFileContentApp {
	public static void main(String[] args) throws Exception {
		/* Get the files to be compared first */
		File file1 = new File(args[0]);
		File file2 = new File(args[1]);

		boolean compareResult = FileUtils.contentEquals(file1, file2);
		System.out.println("Are the files are same? " + compareResult);

	}
}