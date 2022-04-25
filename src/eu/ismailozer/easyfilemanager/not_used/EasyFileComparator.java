package eu.ismailozer.easyfilemanager.not_used;

import java.util.Comparator;

import java.io.File;

public class EasyFileComparator implements Comparator<java.io.File> {

	@Override
	public int compare(File file1, File file2) {
		// TODO Auto-generated method stub
		// return 0;

		// TODO Auto-generated method stub
		int retval;
		if (file1.length() == file2.length()) {
			retval = 0;// return 0;
		} else if ((file1.length()) > file2.length()) {
			retval = 1; // return 1;
		} else {
			retval = -1; // return -1;
		}
		if (retval != 0) {
			return retval;
		}

		// compare filenamelength

		// if (this.length == ((FileInfos) other).length) {
		// retval = 0;// return 0;
		// } else if ((this.length) > ((FileInfos) other).length) {
		// retval = 1; // return 1;
		// } else {
		// retval = -1; // return -1;
		// }
		// if (retval != 0) {
		// return retval;
		// }

		// compare filenames
		// try {
		// retval = file1.getCanonicalPath().compareTo(file2.getCanonicalPath());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// if (retval != 0)
		// return retval;
		return retval;

	}

}
