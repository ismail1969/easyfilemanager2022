package eu.ismailozer.easyfilemanager;

import java.util.Comparator;

public class FileSizeArraySorter implements Comparator<EasyFileInfo> {

	public FileSizeArraySorter() {
	}

	@Override
	public int compare(EasyFileInfo obj1, EasyFileInfo obj2) {
		long aSize = obj1.getFilelength();
		long bSize = obj2.getFilelength();
		if (aSize < bSize) {
			return -1;
		} else if (aSize > bSize) {
			return 1;
		} else {
			return 0;
		}
	}
}