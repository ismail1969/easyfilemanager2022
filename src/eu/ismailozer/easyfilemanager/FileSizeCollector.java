package eu.ismailozer.easyfilemanager;

public class FileSizeCollector implements Comparable<FileSizeCollector> {

	private long filelength;
	private String filename;
	private int filenamelength;

	public FileSizeCollector(long filelength, String filename, int filenamelength) {
		this.filelength = filelength;
		this.filename = filename;
		this.filenamelength = filenamelength;
	}

	public long getFilelength() {
		return filelength;
	}

	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String toString() {
		return "Filelength " + filelength + " -> Filename " + filename + "\n";
	}

	// @Override
	// public int compareTo(Object o1) {
	// // TODO Auto-generated method stub
	// if (this.filelength == ((FileSizeCollector) o1).filelength)
	// return 0;
	// else if ((this.filelength) > ((FileSizeCollector) o1).filelength)
	// return 1;
	// else
	// return -1;
	// }

	// public int compareTo(Person other) {
	// int i = firstName.compareTo(other.firstName);
	// if (i != 0) return i;
	//
	// i = lastName.compareTo(other.lastName);
	// if (i != 0) return i;
	//
	// return Integer.valueOf(age).compareTo(Integer.valueOf(other.age));
	// }

	@Override
	public int compareTo(FileSizeCollector other) {
		// TODO Auto-generated method stub
		int retval;
		if (this.filelength == ((FileSizeCollector) other).filelength) {
			retval = 0;// return 0;
		} else if ((this.filelength) > ((FileSizeCollector) other).filelength) {
			retval = 1; // return 1;
		} else {
			retval = -1; // return -1;
		}
		if (retval != 0) {
			return retval;
		}

		// compare filenamelength

		if (this.filenamelength == ((FileSizeCollector) other).filenamelength) {
			retval = 0;// return 0;
		} else if ((this.filenamelength) > ((FileSizeCollector) other).filenamelength) {
			retval = 1; // return 1;
		} else {
			retval = -1; // return -1;
		}
		if (retval != 0) {
			return retval;
		}

		// compare filenames
		retval = filename.compareTo(other.filename);
		if (retval != 0)
			return retval;
		return retval;
	}

	public int getFilenamelength() {
		return filenamelength;
	}

	public void setFilenamelength(int filenamelength) {
		this.filenamelength = filenamelength;
	}
}