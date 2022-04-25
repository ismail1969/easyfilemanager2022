package eu.ismailozer.easyfilemanager.not_used;

//import ProcessTimeController;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.*;
//import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Date;
import java.util.List;
//import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.ismailozer.easyfilemanager.ProcessTimeController;

public class Utility {

	/**
	 * How big buffer to use to process files.
	 */
	public static final int BUFFER_SIZE = 65536;

	public static List<String> getFileLists(String pPath, boolean pRecursiveMode, boolean pIncludeFiles,
			boolean pIncludeDirectories, int pSearchLimit) {

		List<String> pFileLists = new ArrayList<String>();
		try {
			walkDirectory(pPath, pRecursiveMode, pIncludeFiles, pIncludeDirectories, pSearchLimit, pFileLists);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pFileLists;
	}

	public static void walkDirectory(String pPath, boolean pRecursiveMode, boolean pIncludeFiles,
			boolean pIncludeDirectories, int pSearchLimit, List<String> pFileLists) throws IOException {

		File root = new File(pPath);
		File[] list = root.listFiles();

		if (pFileLists.size() >= pSearchLimit) {
			return;
		}

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walkDirectory(f.getAbsolutePath(), pRecursiveMode, pIncludeFiles, pIncludeDirectories, pSearchLimit,
						pFileLists);
				// System.out.println( "Dir:" + f.getAbsoluteFile() );
				if (pIncludeDirectories) {
					pFileLists.add(f.getCanonicalPath());
				}

				if (pFileLists.size() >= pSearchLimit) {
					return;
				}

			} else {
				// System.out.println( "File:" + f.getAbsoluteFile() );
				if (pIncludeFiles) {
					pFileLists.add(f.getCanonicalPath());
				}
				if (pFileLists.size() >= pSearchLimit) {
					return;
				}
			}
		}
	}

	public static void chooseDirectory(javax.swing.text.JTextComponent pTextComp, Component pParent) {
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(pTextComp.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(pParent);
		if (i == 0) {
			pTextComp.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	public static String replaceFirst(String s, String sub, String with) {
		int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	public static String getNewFilenameFromParent(String pParent, String lNewFile, String pOldFilename) {
		// C:\Temp\subdir1\subdir2\myfile3.txt -> filename =C subdir2
		int ind = pParent.lastIndexOf(System.getProperty("file.separator"));
		if (ind == -1) {
			return lNewFile;
		}
		String lParentName = pParent.substring(ind + 1);
		return replaceFirst(lNewFile, pOldFilename, lParentName);
	}

	public static String getOnlyFilenameFromPath(String f) {
		String filename = null;
		File file = new File(f);
		String s = file.getName();
		// int i = s.lastIndexOf(File.separator);
		//
		if (file.isDirectory()) {
			filename = null;
		} else {
			filename = s;
		}
		return filename;
	}

	public static String getFileExtension(File f) {
		if (f == null || f.isDirectory()) {
			return "";
		}
		String ext = "";
		int i = f.getName().lastIndexOf('.');

		if (i > 0) {
			ext = f.getName().substring(i + 1);
		}
		return ext;
	}

	public static String getFileExtension(String file) {
		return getFileExtension(new File(file));
	}

	public static String getFileNameWithoutExtension(String pFilename) {
		// String fileName="C:\\anu\\My\\Hello.txt";
		String lFilenameWithoutExtension = "";
		File file = new File(pFilename);

		int index = file.getName().lastIndexOf('.');

		if (index > 0 /* && index <= file.getName().length() - 2 */) {
			lFilenameWithoutExtension = file.getName().substring(0, index);
		}

		return lFilenameWithoutExtension;
	}

	public static String getFilenameWithoutExtentionTest(File f) {
		if (f == null) {
			return null;
		}
		String lFilenameWihtoutExt = "";
		// String lFilenameWihtoutExt = "";
		int i = f.getName().lastIndexOf('.');

		if (i > 0) {
			lFilenameWihtoutExt = f.getName().substring(0, i);
		} else {
			lFilenameWihtoutExt = f.getName();
		}

		return lFilenameWihtoutExt;
	}

	public static void showInfoMsg(String pMsg, String pTitle) {
		JOptionPane.showMessageDialog(null, pMsg, pTitle, JOptionPane.INFORMATION_MESSAGE);

	}

	public static void executeRuntime(String pFilename) {
		try {
			Runtime.getRuntime().exec("cmd.exe /c " + pFilename);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public static String capitalize(String s) {

		final java.util.StringTokenizer st = new java.util.StringTokenizer(s, " ", true);
		final StringBuilder sb = new StringBuilder();

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			token = String.format("%s%s", Character.toUpperCase(token.charAt(0)), token.substring(1).toLowerCase());
			sb.append(token);
		}

		return sb.toString();

	}

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\''
					|| !Character.isLetter(chars[i])) {
				found = false;
			}
		}
		return String.valueOf(chars);
	}

	public static String getTimestamp() {
		return new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date()).toString();
	}

	public static void writeToLogFile(String pLogFilename, StringBuffer pStrBuffer, String pCarSet) {
		if (pCarSet.isEmpty())
			pCarSet = "UTF-8";
		try {
			java.io.BufferedWriter out = new java.io.BufferedWriter(
					new java.io.OutputStreamWriter(new java.io.FileOutputStream(pLogFilename), pCarSet));
			out.write(pStrBuffer.toString());
			out.close();
			if (pStrBuffer.length() > 0) {
				System.out.println("File created succesful ... ");
				System.out.println("File = " + pLogFilename);
			} else {
				System.out.println("No files found !");
			}
		} catch (java.io.IOException exp) {
			exp.printStackTrace();
		}
	}

	public static int showConfirmDialog(String pQuestion, String pTitle) {
		return javax.swing.JOptionPane.showConfirmDialog(null, pQuestion, pTitle, javax.swing.JOptionPane.YES_NO_OPTION,
				javax.swing.JOptionPane.WARNING_MESSAGE);
	}

	public static final String formatFileSize(long l) {
		String s = String.valueOf(l);
		int digits = 0;
		while (s.length() > 3) {
			s = s.substring(0, s.length() - 3);
			digits++;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(s);
		if ((s.length() == 1) && (String.valueOf(l).length() >= 3)) {
			buffer.append(".");
			buffer.append(String.valueOf(l).substring(1, 3));
		} else if ((s.length() == 2) && (String.valueOf(l).length() >= 3)) {
			buffer.append(".");
			buffer.append(String.valueOf(l).substring(2, 3));
		}
		if (digits == 0) {
			buffer.append(" B");
		} else if (digits == 1) {
			buffer.append(" KB");
		} else if (digits == 2) {
			buffer.append(" MB");
		} else if (digits == 3) {
			buffer.append(" GB");
		} else if (digits == 4) {
			buffer.append(" TB");
		}
		return buffer.toString();
	}

	public static final String formateDate(long l) {
		return (new java.text.SimpleDateFormat("yyyy-MM-dd : HH-mm-ss")).format(new java.util.Date(l));
	}

	@SuppressWarnings("unused")
	private static void chooseDirectory(JTextField pTextField) {
		JFileChooser jfilechooser = new JFileChooser(new File(pTextField.getText()));
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(pTextField.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(new Frame());
		if (i == 0) {
			pTextField.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	public static void copyFiles(File src, File dest) throws IOException {
		if (!src.exists()) {
			throw new IOException("copyFiles: Can not find source: " + src.getAbsolutePath() + ".");
		} else if (!src.canRead()) { // check to ensure we have rights to the
			// source...
			throw new IOException("copyFiles: No right to source: " + src.getAbsolutePath() + ".");
		}

		// is this a directory copy?
		if (src.isDirectory()) {
			if (!dest.exists()) { // does the destination already exist?
				// if not we need to make it exist if possible (note this is
				// mkdirs not mkdir)
				if (!dest.mkdirs()) {
					throw new IOException("copyFiles: Could not create direcotry: " + dest.getAbsolutePath() + ".");
				} else {
					System.out.println("Directory created: " + dest.getAbsolutePath());
				}
			}

			// get a listing of files...
			String list[] = src.list();
			// copy all the files in the list.
			for (int i = 0; i < list.length; i++) {
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copyFiles(src1, dest1);
			}

		} else {
			// This was not a directory, so lets just copy the file
			FileInputStream fin = null;
			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can
			// change this).
			int bytesRead;
			try {
				// open the files for input and output
				fin = new FileInputStream(src);
				fout = new FileOutputStream(dest);
				// while bytesRead indicates a successful read, lets write...
				while ((bytesRead = fin.read(buffer)) >= 0) {
					fout.write(buffer, 0, bytesRead);
				}
				System.out
						.println("Copy file successfull : " + src.getAbsolutePath() + " -> " + dest.getAbsolutePath());

			} catch (IOException e) { // Error copying file...
				IOException wrapper = new IOException("copyFiles: Unable to copy file: " + src.getAbsolutePath() + "to"
						+ dest.getAbsolutePath() + ".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} finally { // Ensure that the files are closed (if they were open).
				if (fin != null) {
					fin.close();
				}
				if (fout != null) {
					fout.close();
				}
			}
		}
	}

	// from 2014

	public static boolean WriteAlbumListToXMLFile(JTable pTable) {

		// example XML
		// <?xml version="1.0" encoding="UTF-8" standalone="no" ?>
		// <company>
		// <staff id="1">
		// <firstname>yong</firstname>
		// <lastname>mook kim</lastname>
		// <nickname>mkyong</nickname>
		// <salary>100000</salary>
		// </staff>
		// </company>

		String xmlfilename = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ Utility.getTimestamp() + "." + "xml";

		// int total_rows = pTable.getRowCount();

		//
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Root");
			doc.appendChild(rootElement);

			// staff elements
			Element staff = doc.createElement("Ezgiler");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// // firstname elements
			// Element firstname = doc.createElement("firstname");
			// firstname.appendChild(doc.createTextNode("yong"));
			// staff.appendChild(firstname);
			//
			// // lastname elements
			// Element lastname = doc.createElement("lastname");
			// lastname.appendChild(doc.createTextNode("mook kim"));
			// staff.appendChild(lastname);
			//
			// // nickname elements
			// Element nickname = doc.createElement("nickname");
			// nickname.appendChild(doc.createTextNode("mkyong"));
			// staff.appendChild(nickname);
			//
			// // salary elements
			// Element salary = doc.createElement("salary");
			// salary.appendChild(doc.createTextNode("100000"));
			// staff.appendChild(salary);

			// /EASY
			int total_Tbl_Items = pTable.getRowCount();
			for (int i = 0; i < total_Tbl_Items; i++) {
				String filename = (String) pTable.getValueAt(i, 1);
				String[] piece = filename.trim().split("[\\\\]");
				// out.write(piece[piece.length - 3] + ";"
				// + piece[piece.length - 2] + ";"
				// + piece[piece.length - 1]
				// + System.getProperty("line.separator"));
				// System.out.println("GetValueAt : "
				// + (String) pTable.getValueAt(i, 1));

				Element artist = doc.createElement("artist");
				artist.appendChild(doc.createTextNode(piece[piece.length - 3]));
				staff.appendChild(artist);

				Element album = doc.createElement("album");
				album.appendChild(doc.createTextNode(piece[piece.length - 2]));
				staff.appendChild(album);

				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(piece[piece.length - 1]));
				staff.appendChild(title);

			}
			// /

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlfilename));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(source, result);

			System.out.println("File saved in path: " + xmlfilename);
			return true;

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
			return false;
		}

	}

	public static List<String[]> getFileListWithMD5FromPath(String pPath, boolean pRecursiv, int pSearchLimit) {
		List<String[]> mylist = new ArrayList<String[]>();
		List<String[]> mylist_dir = new ArrayList<String[]>();
		File root = new File(pPath);
		File[] list = root.listFiles();

		if (list != null) {
			for (File f : list) {
				if (pSearchLimit > 0 && mylist.size() >= pSearchLimit) {
					break;
				}
				if (f.isDirectory()) {
					// mylist_dir = null;
					if (pRecursiv) {
						mylist_dir = getFileListWithMD5FromPath(f.getAbsolutePath(), pRecursiv, pSearchLimit);
						if (mylist_dir != null) {
							mylist.addAll(mylist_dir);
						}
					}

				} else {
					String lPath = f.getAbsolutePath();
					String lMD5 = Utility.getMD5FromFile(f);

					mylist.add(new String[] { lPath, lMD5 });

					if (pSearchLimit > 0 && mylist.size() >= pSearchLimit) {
						break;
					}
				}
			}
		}
		return mylist;
	}

	public static List<String> getFileListFromPath(String pPath, boolean pRecursiv, int pSearchLimit) {
		List<String> mylist = new ArrayList<String>();
		List<String> mylist_dir = new ArrayList<String>();
		File root = new File(pPath);
		File[] list = root.listFiles();

		if (list != null) {
			for (File f : list) {
				if (pSearchLimit > 0 && mylist.size() >= pSearchLimit) {
					break;
				}
				if (f.isDirectory()) {
					// mylist_dir = null;
					if (pRecursiv) {
						try {
							mylist_dir = getFileListFromPath(f.getCanonicalPath(), pRecursiv, pSearchLimit);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (mylist_dir != null) {
							mylist.addAll(mylist_dir);
						}
					}

				} else {
					// String lPath = f.getAbsolutePath();
					// String lMD5 = EasyUtility.getMD5FromFile(f);

					// mylist.add(new String [] {lPath, lMD5});

					try {
						mylist.add(f.getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (pSearchLimit > 0 && mylist.size() >= pSearchLimit) {
						break;
					}
				}
			}
		}
		return mylist;
	}

	public static List<String> getFileList(File pFile, boolean pRecursive, boolean pIncludeFiles, boolean pIncludeDirs,
			int pMaxHits) {
		if (pFile == null || pFile.isFile()) {
			return null;
		}
		List<String> fileList = new ArrayList<String>();
		List<String> subDir_fileList = new ArrayList<String>();
		File[] rootFiles = pFile.listFiles();
		for (int i = 0; i < rootFiles.length; i++) {
			if (pMaxHits > 0 && fileList.size() >= pMaxHits) {
				break;
			}
			if (rootFiles[i].isDirectory()) {
				if (pIncludeDirs) {
					try {
						fileList.add(rootFiles[i].getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (pRecursive) {
					subDir_fileList = getFileList(rootFiles[i], pRecursive, pIncludeFiles, pIncludeDirs, pMaxHits);
					fileList.addAll(subDir_fileList);
				}

			} else {
				if (pIncludeFiles) {
					try {
						fileList.add(rootFiles[i].getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return fileList;
	}

	public static void showFileMD5(File pFile) {
		ProcessTimeController.start();
		MessageDigest digest = null;
		InputStream is = null;
		;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// File f = new File("D:\\_download\\netbeans-8.0.1-windows.exe");

		try {
			is = new FileInputStream(pFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[8192];
		int read = 0;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			ProcessTimeController.end();
			System.out.println("MD5: " + output);
			ProcessTimeController.printProzessTime();
			System.out.println(" >>>> END: >>> " + pFile.getCanonicalPath());
		} catch (IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
	}

	public static String getMD5FromFile(File pFile) {

		MessageDigest digest = null;
		InputStream is = null;
		;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			is = new FileInputStream(pFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// byte[] buffer = new byte[8192];
		// int BUFFER = 0x100;
		// int BUFFER = 0x1000; // 512
		// byte[] buffer = new byte[4096];

		// int BUFFER = 0x10000;
		byte[] buffer = new byte[8192];
		int read = 0;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			return bigInt.toString(16);
		} catch (IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}
	}

	/**
	 * Compare binary files. Both files must be files (not directories) and exist.
	 * 
	 * @param first  - first file
	 * @param second - second file
	 * @return boolean - true if files are binery equal
	 * @throws IOException - error in function
	 */
	public static boolean isFileBinaryEqual(File first, File second, int FIRST_BUFFER, int BUFFER_SIZE)
			throws IOException {
		// TODO: Test: Missing test
		if (first == null || second == null) {
			return false;
		}
		// int BUFFER = 0x1000; // 4096
		// int BUFFER = 0x100; // 512

		if (first.isDirectory() || second.isDirectory()) {
			return false;
		}

		// if file size are different, then the files are different

		if (first.length() != second.length()) {
			return false;
		}

		boolean retval = false;
		if (BUFFER_SIZE < 1024) {
			BUFFER_SIZE = 65536;
		}
		if ((first.exists()) && (second.exists()) && (first.isFile()) && (second.isFile())) {
			if (first.getCanonicalPath().equals(second.getCanonicalPath())) {
				return false;
			} else {
				FileInputStream firstInput = null;
				FileInputStream secondInput = null;
				BufferedInputStream bufFirstInput = null;
				BufferedInputStream bufSecondInput = null;

				try {
					firstInput = new FileInputStream(first);
					secondInput = new FileInputStream(second);
					// check first file content only little portion of bytes
					byte[] firstContents = new byte[FIRST_BUFFER];
					byte[] secondContents = new byte[FIRST_BUFFER];

					firstInput.read(firstContents);
					secondInput.read(secondContents);

					if (Arrays.equals(firstContents, secondContents)) {
						// System.out
						// .println("Arrays.equals(contents, contents2) true:");
						return true;
					}
					// // check portion

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

						if (firstInput != null) {
							firstInput.close();
						}

						if (secondInput != null) {
							secondInput.close();
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

	public static boolean isFileMD5Equal(File first, File second) throws IOException {
		// TODO: Test: Missing test
		if (first == null || second == null) {
			return false;
		}
		// int BUFFER = 0x1000; // 4096
		// int BUFFER = 0x100; // 512

		if (first.isDirectory() || second.isDirectory()) {
			return false;
		}

		// if file size are different, then the files are different

		if (first.length() != second.length()) {
			return false;
		}

		if (getMD5FromFile(first).compareTo(getMD5FromFile(second)) == 0) {
			return true;
		}
		// return retval;
		return false;
	}

}
