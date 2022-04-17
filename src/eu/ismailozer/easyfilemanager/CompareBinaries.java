package eu.ismailozer.easyfilemanager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.commons.io.FileUtils;

import org.apache.commons.io.FileUtils;

public class CompareBinaries {
	// public static final int BUFFER_SIZE = 65536;

	// private final static int BUFFSIZE = 1024;

	public static final int BUFFER_SIZE = 65536;

	public static boolean inputStreamEquals(InputStream is1, InputStream is2) throws IOException {
		return inputStreamEquals(is1, is2, 0x200);
	}

	public static boolean inputStreamEquals(InputStream is1, InputStream is2, int pBufferSize) throws IOException {

		if (is1 == is2)
			return true;

		if (is1 == null && is2 == null) {
			System.out.println("both input streams are null");
			return true;
		}

		if (is1 == null || is2 == null)
			return false;
		if (pBufferSize < 1) {
			pBufferSize = 1024;
		}
		// pBufferSize = 8 * pBufferSize;
		byte buff1[] = new byte[pBufferSize];
		byte buff2[] = new byte[pBufferSize];

		// int numByte = new BufferedInputStream(is1).available();
		// BUFFSIZE = numByte;
		try {
			int read1 = -1;
			int read2 = -1;

			do {
				int offset1 = 0;
				while (offset1 < pBufferSize && (read1 = is1.read(buff1, offset1, pBufferSize - offset1)) >= 0) {
					offset1 += read1;
				}

				int offset2 = 0;
				while (offset2 < pBufferSize && (read2 = is2.read(buff2, offset2, pBufferSize - offset2)) >= 0) {
					offset2 += read2;
				}
				if (offset1 != offset2)
					return false;
				if (offset1 != pBufferSize) {
					Arrays.fill(buff1, offset1, pBufferSize, (byte) 0);
					Arrays.fill(buff2, offset2, pBufferSize, (byte) 0);
				}
				if (!Arrays.equals(buff1, buff2)) {
					return false;
				}
			} while (read1 >= 0 && read2 >= 0);
			if (read1 < 0 && read2 < 0)
				return true; // both at EOF
			return false;

		} catch (Exception ei) {
			return false;
		} finally {
			try {
				if (is1 != null) {
					is1.close();
				}
				if (is2 != null) {
					is2.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
	}

	public static boolean fileContentsEquals(File file1, File file2, int pBufferSize) {
		// InputStream is1 = null;
		// InputStream is2 = null;

		// DataInputStream is1 = null;
		// DataInputStream is2 = null;

		BufferedInputStream is1 = null;
		BufferedInputStream is2 = null;

		if (file1.length() != file2.length())
			return false;

		try {
			// is1 = new FileInputStream(file1);
			// is2 = new FileInputStream(file2);

			is1 = new BufferedInputStream(new FileInputStream(file1), pBufferSize);
			is2 = new BufferedInputStream(new FileInputStream(file2), pBufferSize);

			// is1 = new DataInputStream(new BufferedInputStream(new
			// FileInputStream(file1), 10*pBufferSize));
			// is2= new DataInputStream(new BufferedInputStream(new
			// FileInputStream(file2), 10*pBufferSize));
			// OK: return inputStreamEquals(is1, is2, pBufferSize);
			return inputStreamEquals(is1, is2, file1.length(), file1.length(), pBufferSize);

		} catch (Exception ei) {
			return false;
		} finally {
			try {
				if (is1 != null)
					is1.close();
				if (is2 != null)
					is2.close();
			} catch (Exception ei2) {
			}
		}
	}

	public static boolean fileContentsEquals(String fn1, String fn2, int pBufferSize) {
		return fileContentsEquals(new File(fn1), new File(fn2), pBufferSize);
	}

	public static boolean isFileBinaryEqual(File first, File second, int pBufferSize) throws IOException {
		// TODO: Test: Missing test
		// public static final int BUFFER_SIZE = 65536;
		boolean retval = false;

		if ((first.exists()) && (second.exists()) && (first.isFile()) && (second.isFile())) {
			if (first.getCanonicalPath().equals(second.getCanonicalPath())) {
				retval = true;
			} else {
				FileInputStream firstInput = null;
				FileInputStream secondInput = null;
				BufferedInputStream bufFirstInput = null;
				BufferedInputStream bufSecondInput = null;

				try {
					firstInput = new FileInputStream(first);
					secondInput = new FileInputStream(second);
					bufFirstInput = new BufferedInputStream(firstInput, pBufferSize);
					bufSecondInput = new BufferedInputStream(secondInput, pBufferSize);

					int firstByte;
					int secondByte;

					while (true) {
						firstByte = bufFirstInput.read();
						secondByte = bufSecondInput.read();
						if (firstByte != secondByte) {
							break;
						}
						if ((firstByte < 0) && (secondByte < 0)) {
							retval = true;
							break;
						}
					}
				} finally {
					try {
						if (bufFirstInput != null) {
							bufFirstInput.close();
						}
					} finally {
						if (bufSecondInput != null) {
							bufSecondInput.close();
						}
					}
				}
			}
		}

		return retval;
	}

	// ismail

	public static boolean inputStreamEquals(InputStream is1, InputStream is2, long is1_length, long is2_length,
			int pBufferSize) throws IOException {

		if (is1 == is2)
			return true;

		if (is1 == null && is2 == null) {
			System.out.println("both input streams are null");
			return true;
		}

		if (is1 == null || is2 == null)
			return false;
		if (pBufferSize < 1) {
			pBufferSize = 1024;
		}
		// pBufferSize = 8 * pBufferSize;
		byte buff1[] = new byte[pBufferSize];
		byte buff2[] = new byte[pBufferSize];

		try {
			int read1 = -1;
			int read2 = -1;

			do {
				int offset1 = 0;
				while (offset1 < pBufferSize && (read1 = is1.read(buff1, offset1, pBufferSize - offset1)) >= 0) {
					offset1 += read1;
				}

				int offset2 = 0;
				while (offset2 < pBufferSize && (read2 = is2.read(buff2, offset2, pBufferSize - offset2)) >= 0) {
					offset2 += read2;
				}
				if (offset1 != offset2) {
					return false;
				}
				if (offset1 != pBufferSize) {
					Arrays.fill(buff1, offset1, pBufferSize, (byte) 0);
					Arrays.fill(buff2, offset2, pBufferSize, (byte) 0);
				}
				if (!Arrays.equals(buff1, buff2)) {
					return false;
				}
			} while (read1 >= 0 && read2 >= 0);
			if (read1 < 0 && read2 < 0)
				return true; // both at EOF
			return false;

		} catch (Exception ei) {
			return false;
		} finally {
			try {
				if (is1 != null) {
					is1.close();
				}
				if (is2 != null) {
					is2.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
	}

	public static String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

	public static boolean sameContentFiles(File pFirstFile, File pSecondFile) {
		try {
			return FileUtils.contentEquals(pFirstFile, pSecondFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		// compareResult = FileUtils.contentEquals(file1, file2);
		// System.out.println("Are the files are same? " + compareResult);
	}

	public static boolean sameContent(Path file1, Path file2) throws IOException {
		final long size = Files.size(file1);
		if (size != Files.size(file2))
			return false;

		if (size < 4096)
			return Arrays.equals(Files.readAllBytes(file1), Files.readAllBytes(file2));

		try (InputStream is1 = Files.newInputStream(file1); InputStream is2 = Files.newInputStream(file2)) {
			// Compare byte-by-byte.
			// Note that this can be sped up drastically by reading large chunks
			// (e.g. 16 KBs) but care must be taken as InputStream.read(byte[])
			// does not neccessarily read a whole array!
			int data;
			while ((data = is1.read()) != -1)
				if (data != is2.read())
					return false;
		}

		return true;
	}

	/**
	 * Compare binary files. Both files must be files (not directories) and exist.
	 * 
	 * @param first  - first file
	 * @param second - second file
	 * @return boolean - true if files are binery equal
	 * @throws IOException - error in function
	 */
	public boolean isFileBinaryEqual(File first, File second) throws IOException {
		// TODO: Test: Missing test
		boolean retval = false;

		if ((first.exists()) && (second.exists()) && (first.isFile()) && (second.isFile())) {
			if (first.getCanonicalPath().equals(second.getCanonicalPath())) {
				retval = true;
			} else {
				FileInputStream firstInput = null;
				FileInputStream secondInput = null;
				BufferedInputStream bufFirstInput = null;
				BufferedInputStream bufSecondInput = null;

				try {
					firstInput = new FileInputStream(first);
					secondInput = new FileInputStream(second);
					bufFirstInput = new BufferedInputStream(firstInput, BUFFER_SIZE);
					bufSecondInput = new BufferedInputStream(secondInput, BUFFER_SIZE);

					int firstByte;
					int secondByte;

					while (true) {
						firstByte = bufFirstInput.read();
						secondByte = bufSecondInput.read();
						if (firstByte != secondByte) {
							break;
						}
						if ((firstByte < 0) && (secondByte < 0)) {
							retval = true;
							break;
						}
					}
				} finally {
					try {
						if (bufFirstInput != null) {
							bufFirstInput.close();
						}
					} finally {
						if (bufSecondInput != null) {
							bufSecondInput.close();
						}
					}
				}
			}
		}

		return retval;
	}

}