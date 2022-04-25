package eu.ismailozer.easyfilemanager;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

//import com.sun.jmx.snmp.Timestamp;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BatchImageConverterGUI extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> files;
	private String targetFormat;
	private boolean showSuccessInfo = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String lDir = "C:\\Temp\\batch-converter\\bmp";
				new BatchImageConverterGUI(lDir, "bmp", "jpg", false);
			}
		});
	}

	public BatchImageConverterGUI(String[] pFiles, String pTargetFormat, boolean pSuccessInfo) {
		super();
		targetFormat = pTargetFormat;
		showSuccessInfo = pSuccessInfo;
		startConvertImage();
	}

	public BatchImageConverterGUI(String pDir, String pSrcExtention, String pTargetFormat, boolean pSuccessInfo) {
		super();
		files = getFileList(pDir, pSrcExtention);
		targetFormat = pTargetFormat;
		showSuccessInfo = pSuccessInfo;
		startConvertImage();
	}

	public BatchImageConverterGUI(List<String> pFileList, String pTargetFormat, boolean pSuccessInfo) {
		super();
		files = pFileList;
		targetFormat = pTargetFormat;
		showSuccessInfo = pSuccessInfo;
		startConvertImage();
	}

	private static List<String> getFileList(String pDirectory, String pSrcExtention) {
		List<String> fileList = new ArrayList<String>();
		File file = new File(pDirectory);
		File[] files = file.listFiles(new ImageFileFilter(pSrcExtention));
		for (int fileInList = 0; fileInList < files.length; fileInList++) {
			if (files[fileInList].isFile()) {
				fileList.add(files[fileInList].toString());
			}
		}
		return fileList;
	}

	private String getTargetImageName(String pFileSource, String pTargetFormat) {
		return pFileSource.substring(0, pFileSource.lastIndexOf('.')) + "." + pTargetFormat;
	}

	private void startConvertImage() {
		/*
		 * if (JOptionPane.showConfirmDialog( null,
		 * "Do you realy want convert the images \n", // + txtSourceDir.getText() +
		 * " to \n" // + txtTargetDir.getText() + "?", "Convert image file?",
		 * "Convert image file?", JOptionPane.YES_NO_OPTION,
		 * JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
		 * convertFiles(this.files, this.targetFormat); }
		 */
		if (convertFiles(this.files, this.targetFormat)) {
			JOptionPane.showMessageDialog(this, "Image conversion successfully finished!");
		} else {
			JOptionPane.showMessageDialog(this, "There was some errors on image conversion!");
		}
	}

	private boolean convertFiles(List<String> pFileList, String pTargetFormat) {
		Iterator<String> it = pFileList.iterator();
		boolean success = true;
		while (it.hasNext()) {
			String file = (String) it.next();
			success = success & convertFile(file, pTargetFormat);
		}
		return success;
	}

	private boolean convertFile(String pFile, String pTargetFormat) {
		boolean success = false;
		String lInfo = "";
		String lTargetImage = getTargetImageName(pFile, pTargetFormat);
		try {
			BufferedImage image = ImageIO.read(new File(pFile));

			if (image == null) {
				lInfo = "Decode Error: Das Bild " + pFile + " konnte nicht dekodiert werden!";
				System.out.println(lInfo);
				if (showSuccessInfo) {
					JOptionPane.showMessageDialog(null, lInfo);
				}
			} else if (ImageIO.write(image, pTargetFormat, new File(lTargetImage)) == false) {
				lInfo = "Format Error: Unbekanntes Zielformat: " + pTargetFormat;
				System.out.println(lInfo);
				// JOptionPane.showMessageDialog(null, lInfo);
				if (showSuccessInfo) {
					JOptionPane.showMessageDialog(null, lInfo);
				}
			} else {
				lInfo = "Success: New Image was converted successful: " + lTargetImage;
				if (showSuccessInfo) {
					JOptionPane.showMessageDialog(null, lInfo);
				}
				System.out.println(lInfo);
				success = true;
			}
		} catch (IOException e) {
			lInfo = "Load Error: Fehler beim Laden oder Speichern!";
			System.out.println(lInfo);
			if (showSuccessInfo) {
				JOptionPane.showMessageDialog(null, lInfo);
			}
		}
		return success;
	}
}
