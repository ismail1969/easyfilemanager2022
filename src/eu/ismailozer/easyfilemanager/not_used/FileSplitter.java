package eu.ismailozer.easyfilemanager.not_used;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;

public class FileSplitter {

	public static void main(String[] args) throws IOException {
		System.out.println(Math.ceil(23.46));
		System.out.println(Math.ceil(15.01));
		System.out.println(Math.ceil(63.33));

		// File file = new File("C:\\downloads\\hakkinda.html" );
		File file = new File("C:\\temp\\del_foto_e.log");
		// int lines = ReadStringFromFileLineByLine.countLines(file);
		// double lines_for_splitts = Math.ceil(lines/3);
		// System.out.print("Nummer of lines = " + new
		// Integer(lines).toString());
		FileSplitter.splittFileContent(file, 3, true);
	}

	public static void splittFileContent(File pFile, double pSplitts, boolean pFirstLineInEachFile) {
		try {
			double pLinesForSplitts = Math.ceil(countLines(pFile) / pSplitts);
			double counter = 0;
			int filenumber = 1;
			String headerLine = "";
			// File file = pFile;//new File("C:\\downloads\\index.html");
			// FileReader fileReader = new FileReader(file);
			// BufferedReader bufferedReader = new BufferedReader(fileReader);
			InputStreamReader inputReader = new InputStreamReader(new FileInputStream(pFile));
			System.out.println("getEncoding = " + inputReader.getEncoding());

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(pFile), inputReader.getEncoding())); // "UTF-8"
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				counter++;
				if (pFirstLineInEachFile && counter == 1) {
					headerLine = line;
				}
				stringBuffer.append(line);
				stringBuffer.append("\n");
				if (counter % pLinesForSplitts == 0) {

					writeToFile(getSplittFileName(pFile, filenumber), stringBuffer, inputReader.getEncoding());
					filenumber++;
					stringBuffer.setLength(0);
					if (pFirstLineInEachFile) {
						stringBuffer.append(headerLine).append("\n");
					}
				}
			}
			writeToFile(getSplittFileName(pFile, filenumber), stringBuffer, inputReader.getEncoding());
			// fileReader.close();
			inputReader.close();
			bufferedReader.close();
			System.out.println("Contents of file:");
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSplittFileName(File pFile, int pFilenumber) {
		return getFileNameWithoutExtension(pFile) + "_" + Integer.valueOf(pFilenumber).toString()
				+ getFileExtension(pFile);
		// "C:\\downloads\\splitt_" + new Integer(pFilenumber).toString()+".csv"
		// return "";
	}

	public static int countLines(File aFile) throws IOException {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(aFile));
			while ((reader.readLine()) != null)
				;
			return reader.getLineNumber();
		} catch (Exception ex) {
			return -1;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static void writeToFile(String pFilename, StringBuffer pData, String pEncoding) throws IOException {
		// BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pFilename), pEncoding));

		// Writer out = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream(pFilename), "UTF-8"));
		out.write(pData.toString());
		out.flush();
		out.close();
	}

	public static String getFileExtension(File pFilename) {
		if (pFilename == null) {
			return null;
		}
		int index = pFilename.getAbsolutePath().lastIndexOf('.');

		if (index == -1) {
			return pFilename.getAbsolutePath();
		} else {
			return pFilename.getAbsolutePath().substring(index);
		}
	}

	public static String getFileNameWithoutExtension(File pFilename) {
		if (pFilename == null) {
			return null;
		}
		int index = pFilename.getAbsolutePath().lastIndexOf('.');

		if (index == -1) {
			return pFilename.getAbsolutePath();
		} else {
			return pFilename.getAbsolutePath().substring(0, index);
		}
	}
}