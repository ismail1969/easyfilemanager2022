package eu.ismailozer.easyfilemanager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FileUtilityHelper {

	public static int getFileNumberInDir(File pPath) {
		int fileSize = 0;
		File[] files = pPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileSize++;
			}
		}
		return fileSize;
	}

	public static String getFileFromFileChooser(String pParentPath, int pFileSelectionMode,
			java.awt.Component pComponent) {
		JFileChooser jfilechooser = new javax.swing.JFileChooser();
		jfilechooser.setFileSelectionMode(pFileSelectionMode);
		jfilechooser.setCurrentDirectory(new File(pParentPath));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(pComponent);
		if (i == 0) {
			return jfilechooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

	public static void setPath(JTextField pTextField, java.awt.Component pComponent) {
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(pTextField.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(pComponent);
		if (i == 0) {
			pTextField.setText(jfilechooser.getSelectedFile().getPath());
		}
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

	public static String getOnlyFilenameFromPath(String f) {
//		String filename = null;
//		File file = new File(f);
//		String s = file.getName();
//		// int i = s.lastIndexOf(File.separator);
//		//
//		if (file.isDirectory()) {
//			filename = null;
//		} else {
//			filename = s;
//		}

//		String filename = null;
//		File file = new File(f);
//		String s = file.getName();
		// int i = s.lastIndexOf(File.separator);
		//
		if (new File(f).isFile()) {
			return new File(f).getName();
		}

		return null;

	}

	public static boolean dirExist(String pDirectory) {
		// TODO Auto-generated method stub
		return new File(pDirectory).exists();
	}

	public static boolean dirExistWithMessage(String pDirectory, java.awt.Component pComponent) {
		// TODO Auto-generated method stub
		if (!dirExist(pDirectory)) {
			javax.swing.JOptionPane.showMessageDialog(pComponent, "No directory found: [" + pDirectory + "]!");
			return false;
		}
		return true;

	}

	public boolean deleteFile(String pFilename, boolean pLoggingCommandLine) throws IOException {
		File f = new File(pFilename);
		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) {
			String lInfo = "Delete: no such file or directory: " + pFilename;
			FileUtilityHelper.log(lInfo, pLoggingCommandLine);
			// EasyUtility.showInfoMsg(lInfo, "Delete Files");
			return false;
			// If it is a directory, make sure it is empty
		} else if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0) {
				String lInfo = "Delete: directory not empty: " + pFilename;
				FileUtilityHelper.log(lInfo, pLoggingCommandLine);
				// showInfoMsg(lInfo, "Directory not empty");
				JOptionPane.showMessageDialog(null, lInfo, "Directory not empty", JOptionPane.INFORMATION_MESSAGE);
				return false;
			} else {

				if (!f.delete()) {
					FileUtilityHelper.log("Delete=>: deletion failed for : " + pFilename, pLoggingCommandLine);
					return false;
				} else {
					if (pLoggingCommandLine) {
						FileUtilityHelper.log("Delete=>: deletion succesful : " + pFilename, pLoggingCommandLine);
					}
					return true;

				}
			}

		} else {

			if (!f.delete()) {
				FileUtilityHelper.log("Delete=>: deletion failed for : " + pFilename, pLoggingCommandLine);
				return false;
			} else {
				FileUtilityHelper.log("Delete=>: deletion succesful : " + pFilename, pLoggingCommandLine);
				return true;
			}
		}
	}

	public static String getNewFilenameFromRegExpReplacement(String pSearchedRegExpress, String pReplaceRegExpress,
			String pStringToChange) {
		if (pSearchedRegExpress == null || pStringToChange == null) {
			return null;
		}
		Pattern pattern = Pattern.compile(pSearchedRegExpress);
		Matcher matcher = pattern.matcher(pStringToChange);
		return matcher.replaceAll(pReplaceRegExpress);
	}

	public java.util.List<File> getOnlyDirsFromPath(File pRootDir) {

		if (pRootDir == null) {
			return null;
		}
		java.util.List<File> returnDirs = new ArrayList<File>();

		java.util.List<File> subDirs = new ArrayList<File>();

		File[] list = pRootDir.listFiles();

		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory()) {
				returnDirs.add(list[i]);
				subDirs = getOnlyDirsFromPath(list[i]);
				returnDirs.addAll(subDirs);
			}
		}
		return returnDirs;
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	// LOGGING
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

	public static void cmdLineLogging(Object pObject, boolean pLoggingCommandLine) {
		if (pObject != null && pLoggingCommandLine) {
			System.out.println(pObject.toString());
		}
	}

	public static void log(Object pObject, boolean pLoggingCommandLine) {
		if (pObject != null && pLoggingCommandLine) {
			FileUtilityHelper.cmdLineLogging(pObject.toString(), pLoggingCommandLine);
			// System.out.println(pObject.toString());
		}
	}

	public static void logOnGUI(Object pObject, boolean pLoggingCommandLine, javax.swing.JLabel pJLabel) {
		if (pObject != null && pLoggingCommandLine) {
			pJLabel.setText((String) pObject);
		}
	}

	public static void logOnTitle(Object pObject, boolean pLoggingCommandLine, javax.swing.JFrame pFrame) {
		if (pObject != null && pLoggingCommandLine) {
			pFrame.setTitle((String) pObject);
		}
	}

	public static void logTimeStamp(boolean pLoggingCommandLine) {
		if (pLoggingCommandLine) {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			// Date now = new Date();
			// String strDate = sdfDate.format(new Date());
			System.out.println(">>> Time: " + sdfDate.format(new Date()));
		}
	}

}
