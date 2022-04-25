package eu.ismailozer.easyfilemanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

//import com.sun.jmx.snmp.Timestamp;

import java.io.*;

//import java.security.Timestamp;

public class ImageConverterGUI extends javax.swing.JFrame implements ActionListener, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ImageConverterGUI("C:\\Temp\\kizlar\\camilla-belle\\camilla04.jpg",
						"C:\\Temp\\kizlar\\camilla-belle\\camilla04.png", "png");
			}
		});
	}

	private JTextField txtSourceDir;
	private JTextField txtTargetDir;
	private JButton btnOpenSourceDir;
	private JButton btnOpenTargetDir;
	private JButton btnCopyFiles;

	private JLabel lblInfo;
	private final String extentions[] = { "jpg", "jpeg", "gif", "png", "bmp", "tiff" };
	private JComboBox<String> cbxTargetFileFormat;
	private String format;
	private String targetImage;

	private String sourceImage;

	public ImageConverterGUI(String pSourceImage, String pTargetImage, String pFormat) {
		super();
		sourceImage = pSourceImage;
		targetImage = pTargetImage;
		format = pFormat;
		initGUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnOpenSourceDir) {
			chooseLogDirectory(txtSourceDir);
			txtTargetDir.setText(
					getTargetImageName(txtSourceDir.getText(), (String) cbxTargetFileFormat.getSelectedItem()));
		} else if (source == btnOpenTargetDir) {
			chooseLogDirectory(txtTargetDir);
		} else if (source == btnCopyFiles) {
			startConvertImage();
		}
	}

	private void chooseLogDirectory(JTextField pTextField) {
		JFileChooser jfilechooser = new JFileChooser(new File(pTextField.getText()));
		jfilechooser.setFileSelectionMode(0);
		jfilechooser.setCurrentDirectory(new File(pTextField.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(this);
		if (i == 0) {
			pTextField.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	private void convertFile(String pSourceImage, String pTargetImage, String pFormat) {
		String lInfo = "";
		try {
			BufferedImage image = ImageIO.read(new File(pSourceImage));

			if (image == null) {
				lInfo = "Das Bild " + pSourceImage + " konnte nicht dekodiert werden!";
				System.out.println(lInfo);
				JOptionPane.showMessageDialog(null, lInfo);
			} else if (ImageIO.write(image, pFormat, new File(pTargetImage)) == false) {
				lInfo = "Unbekanntes Zielformat: " + pFormat;
				System.out.println(lInfo);
				JOptionPane.showMessageDialog(null, lInfo);
			} else {
				lInfo = "New Image was converted successful: " + pTargetImage;
				System.out.println(lInfo);
				JOptionPane.showMessageDialog(null, lInfo);
			}
		} catch (IOException e) {
			lInfo = "Fehler beim Laden oder Speichern!";
			System.out.println(lInfo);
			JOptionPane.showMessageDialog(null, lInfo);
		}
	}

	private String getTargetImageName(String pFileSource, String pTargetFormat) {
		return pFileSource.substring(0, pFileSource.lastIndexOf('.')) + "."
				+ (String) cbxTargetFileFormat.getSelectedItem();
	}

	public long getTimeStampTime() {
		return System.currentTimeMillis();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setLayout(null);
			setTitle("Image Converter GUI");
			{
				txtSourceDir = new JTextField(sourceImage);
				getContentPane().add(txtSourceDir);
				txtSourceDir.setBounds(10, 10, 400, 25);
			}
			{
				btnOpenSourceDir = new JButton("Select Source");
				getContentPane().add(btnOpenSourceDir);
				btnOpenSourceDir.addActionListener(this);
				btnOpenSourceDir.setBounds(440, 10, 150, 25);
			}
			{
				txtTargetDir = new JTextField(targetImage);
				getContentPane().add(txtTargetDir);
				txtTargetDir.setBounds(10, 40, 400, 25);
			}
			{
				btnOpenTargetDir = new JButton("Select Target");
				getContentPane().add(btnOpenTargetDir);
				btnOpenTargetDir.addActionListener(this);
				btnOpenTargetDir.setBounds(440, 40, 150, 25);
			}

			{
				ComboBoxModel<String> cbxFileExtentionsModel = new DefaultComboBoxModel<>(extentions);
				cbxTargetFileFormat = new JComboBox<>();
				cbxTargetFileFormat.setModel(cbxFileExtentionsModel);
				cbxTargetFileFormat.setBounds(10, 70, 400, 25);
				cbxTargetFileFormat.setSelectedItem(format);
				cbxTargetFileFormat.addItemListener(this);
				getContentPane().add(cbxTargetFileFormat);
			}

			{
				btnCopyFiles = new JButton();
				getContentPane().add(btnCopyFiles);
				btnCopyFiles.setText("Convert Image");
				btnCopyFiles.addActionListener(this);
				btnCopyFiles.setBounds(440, 70, 150, 25);
			}

			{
				lblInfo = new JLabel("Info:");
				getContentPane().add(lblInfo);
				lblInfo.setBounds(10, 100, 400, 25);
			}

			setPreferredSize(new java.awt.Dimension(600, 180));
			setLocationRelativeTo(null);
			setVisible(true);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		Object source = event.getSource();
		if (source == cbxTargetFileFormat) {
			txtTargetDir.setText(
					getTargetImageName(txtTargetDir.getText(), (String) cbxTargetFileFormat.getSelectedItem()));
		}
	}

	private void startConvertImage() {
		if (JOptionPane.showConfirmDialog(null,
				"Do you realy want convert the image \n" + txtSourceDir.getText() + " to \n" + txtTargetDir.getText()
						+ "?",
				"Convert image file?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			// try {
			// long start = getTimeStampTime();
			// copyFiles(new File(txtSourceDir.getText()), new
			// File(txtTargetDir.getText()));
			// long end = getTimeStampTime();
			// String lInfo = "Process time " + (end-start)/1000 + " [s]";
			// System.out.println(lInfo);
			// lblInfo.setText(lInfo);
			// } catch (IOException e) {
			// // Auto-generated catch block
			// e.printStackTrace();
			// }
			convertFile(txtSourceDir.getText(), txtTargetDir.getText(), (String) cbxTargetFileFormat.getSelectedItem());
		}
	}
}
