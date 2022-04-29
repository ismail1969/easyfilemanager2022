package eu.ismailozer.easyfilemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FileCopyHelper {
	
	public static boolean copyFileTo(File pSource, File pTarget, boolean pLoggingCommandLine) {
		boolean ret_code = false;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can
		// change this).
		int bytesRead;
		try {
			// open the files for input and output
			fin = new FileInputStream(pSource);
			fout = new FileOutputStream(pTarget);
			// while bytesRead indicates a successful read, lets write...
			while ((bytesRead = fin.read(buffer)) >= 0) {
				fout.write(buffer, 0, bytesRead);
			}
			if (pLoggingCommandLine) {
				FileUtilityHelper.log(
						"Copy file successfull : " + pSource.getAbsolutePath() + " -> " + pTarget.getAbsolutePath(),
						pLoggingCommandLine);
			}
			ret_code = true;

		} catch (IOException e) { // Error copying file...
			IOException wrapper = new IOException("copyFiles: Unable to copy file: " + pSource.getAbsolutePath() + "to"
					+ pTarget.getAbsolutePath() + ".");
			wrapper.initCause(e);
			wrapper.setStackTrace(e.getStackTrace());
			return false;

		} finally { // Ensure that the files are closed (if they were open).
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					//
					e.printStackTrace();
				}
			}
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					//
					e.printStackTrace();
				}
			}
		}
		return ret_code;
	}	

	private void copySelectedFilesToDir(java.awt.Component pComponent, boolean pLoggingCommandLine) {
		// copyFileTo(File pSouce, File pTarget);
		// This was not a directory, so lets just copy the file
		// TODO
//		int counter = 0;
//		int[] intselected = tableSearchResult.getSelectedRows();
//		if (tableSearchResult.getRowCount() == 0) {
//			JOptionPane.showMessageDialog(pComponent, "No file(s) seleted.");
//			return;
//		} else if (intselected.length == 0) {
//			JOptionPane.showMessageDialog(pComponent, "For copy file(s) select one or more files.");
//			return;
//		}
//
//		if (JOptionPane.showConfirmDialog(null, "Do you realy copy the files to " + " another directory?",
//				"Delete files?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
//			String lCopyDir = "C:\\Temp\\_clones_" + getTimestamp();
//			String answer = (JOptionPane.showInputDialog(null, "To splitt in many files enter your value.", lCopyDir));
//			File lSelDir = null;
//			if (answer != null) {
//				lSelDir = new File(answer);
//			} else {
//				lSelDir = new File(lCopyDir);
//			}
//			if (!lSelDir.isDirectory()) {
//				lSelDir.mkdirs();
//			}
//			boolean bRecursive = true;
//			if (lSelDir != null && lSelDir.isDirectory()) {
//				for (int i = intselected.length - 1; i >= 0; i--) {
//					String sourceFile = (String) tableSearchResult.getValueAt(i, 1);
//					File source = new File(sourceFile);
//					String lOnlyFilename = FileUtilityHelper.getOnlyFilenameFromPath(sourceFile);
//
//					File target = null;
//					if (bRecursive) {
//						int start_Ind = sourceFile.indexOf(":");
//						int end_Ind = sourceFile.lastIndexOf(File.separator);
//						String targetPath = sourceFile.substring(start_Ind + 1, end_Ind);
//						targetPath = answer + targetPath;
//						if (!new File(targetPath).isDirectory()) {
//							new File(targetPath).mkdirs();
//						}
//						target = new File(targetPath, lOnlyFilename);
//					} else {
//						target = new File(lSelDir, lOnlyFilename);
//					}
//
//					if (target != null && target.exists()) {
//						target = new File(lSelDir, "Copy_Of_" + i + "_" + lOnlyFilename);
//					}
//					if (target != null && source != null) {
//						if (FileCopyHelper.copyFileTo(source, target, pLoggingCommandLine)) {
//							counter++;
//						} else {
//							JOptionPane.showMessageDialog(pComponent, "Error copy file " + source + " --> " + target);
//							break;
//						}
//					}
//				}
//			}
//			
//			JOptionPane.showMessageDialog(pComponent, "Total copied files: " + counter);
//		}
	}	
	
}
