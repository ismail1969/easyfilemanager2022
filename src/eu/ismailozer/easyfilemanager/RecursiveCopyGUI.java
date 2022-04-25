package eu.ismailozer.easyfilemanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

//import com.sun.jmx.snmp.Timestamp;

import java.io.*;

public class RecursiveCopyGUI extends javax.swing.JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new RecursiveCopyGUI("Copy Recursive Directories");
			}
		});
	}

	private JTextField txtSourceDir;
	private JTextField txtTargetDir;
	private JButton btnOpenSourceDir;
	private JButton btnOpenTargetDir;

	private JButton btnCopyFiles;

	private JLabel lblInfo;

	public RecursiveCopyGUI(String pTitle) {
		super(pTitle);
		initGUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnOpenSourceDir) {
			chooseLogDirectory(txtSourceDir);
		} else if (source == btnOpenTargetDir) {
			chooseLogDirectory(txtTargetDir);
		} else if (source == btnCopyFiles) {
			startCopy();
		}
	}

	private void chooseLogDirectory(JTextField pTextField) {
		JFileChooser jfilechooser = new JFileChooser(new File(pTextField.getText()));
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(pTextField.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(this);
		if (i == 0) {
			pTextField.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	public long getTimeStampTime() {
		return System.currentTimeMillis();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setLayout(null);
			setTitle("Recursive Copy");
			{
				txtSourceDir = new JTextField("C:\\Temp\\kizlar");
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
				txtTargetDir = new JTextField("C:\\Temp\\kizlar_tekrar");
				getContentPane().add(txtTargetDir);
				txtTargetDir.setBounds(10, 40, 400, 25);
			}
			{
				btnOpenTargetDir = new JButton("Select Target");
				getContentPane().add(btnOpenTargetDir);
				btnOpenTargetDir.addActionListener(this);
				btnOpenTargetDir.setBounds(440, 40, 150, 25);

			}

			lblInfo = new JLabel("Info:");
			getContentPane().add(lblInfo);
			lblInfo.setBounds(10, 70, 400, 25);
			{
				btnCopyFiles = new JButton();
				getContentPane().add(btnCopyFiles);
				btnCopyFiles.setText("Copy Recursive");
				btnCopyFiles.addActionListener(this);
			}

			btnCopyFiles.setBounds(440, 70, 150, 25);

			setPreferredSize(new java.awt.Dimension(600, 180));
			setLocationRelativeTo(null);
			setVisible(true);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startCopy() {
		if (JOptionPane.showConfirmDialog(null,
				"Do you realy want copy the files from " + txtSourceDir.getText() + " to " + txtTargetDir.getText()
						+ "?",
				"Delete files?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			try {
				long start = getTimeStampTime();
				copyFiles(new File(txtSourceDir.getText()), new File(txtTargetDir.getText()));
				long end = getTimeStampTime();
				String lInfo = "Process time " + (end - start) / 1000 + " [s]";
				System.out.println(lInfo);
				lblInfo.setText(lInfo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
