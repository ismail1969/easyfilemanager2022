package eu.ismailozer.easyfilemanager;

//import ismail.EasyFileEditorOld;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class FileDeleterGUI extends JFrame implements ActionListener {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FileDeleterGUI inst = new FileDeleterGUI(System.getProperty("user.dir"),
						new String[] { "No", "Logfiles", "Modified", "Size" });
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	private static final long serialVersionUID = -7281628845224208887L;
	DefaultTableModel tabModel;
	JTable table;
	JTextField txtDirectory;

	JButton btnSelectAll;
	JButton btnClearAll;
	JButton btnDeleteFiles;
	JButton btnShowFiles;
	JButton btnOpenFileDlg;
	JButton btnOpenSelectedFile;
	JPanel pnlDirectory;
	JLabel lblDirectory;
	boolean lCmdLineLogging;

	public boolean deleteFile(String pFilename, boolean pCmdLineLogging) {
		lCmdLineLogging = pCmdLineLogging;
		File f = new File(pFilename);
		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) {
			String lInfo = "Delete: no such file or directory: " + pFilename;
			System.out.println(lInfo);
			showInfoMsg(lInfo, "Delete Files");
			return false;
			// If it is a directory, make sure it is empty
		} else if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0) {
				String lInfo = "Delete: directory not empty: " + pFilename;
				System.out.println(lInfo);
				showInfoMsg(lInfo, "Directory not empty");
				return false;
			} else {
				String lInfo = "Delete: directory not allowed: " + pFilename;
				System.out.println(lInfo);
				showInfoMsg(lInfo, "Directory deletion not allowed");
				return false;
			}
		} else {

			if (!f.delete()) {
				System.out.println("Delete: deletion failed for : " + pFilename);
				return false;
			} else {
				if (lCmdLineLogging) {
					System.out.println("Delete: deletion succesful : " + pFilename);
					return true;
				}
			}
		}
		return true;
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnSelectAll) {
			table.selectAll();
		} else if (source == btnClearAll) {
			clearSelections(table, tabModel);
		} else if (source == btnDeleteFiles) {
			removeSelectedFiles();
		} else if (source == btnShowFiles) {
			fillTableRows(tabModel, getFiles(txtDirectory.getText()));
		} else if (source == btnOpenFileDlg) {
			chooseLogDirectory(event);
			fillTableRows(tabModel, getFiles(txtDirectory.getText()));
		} else if (source == btnOpenSelectedFile) {
			openSelectedFile(table, tabModel);
		}
	}

	private void openSelectedFile(JTable pTable, DefaultTableModel pMyTblModel) {

		if (!(pTable.getSelectedRows().length == 1)) {
			showInfoMsg("Please select only one file to edit!", "Open file");
			return;
		} else {
			try {
				Runtime.getRuntime()
						.exec("cmd.exe /c " + pMyTblModel.getValueAt(pTable.getSelectedRow(), 1).toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// new
			// EasyFileEditorOld(pMyTblModel.getValueAt(pTable.getSelectedRow(),
			// 1).toString());
		}
	}

	private void showInfoMsg(String pMsg, String pTitle) {
		JOptionPane.showMessageDialog(null, pMsg, pTitle, JOptionPane.INFORMATION_MESSAGE);

	}

	private void removeSelectedFiles() {
		int[] intselected = table.getSelectedRows();
		if (table.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "You have no files to delete.");
			return;
		} else if (intselected.length == 0) {
			JOptionPane.showMessageDialog(this, "For deletion select one or more files.");
			return;
		}
		int result = JOptionPane.showConfirmDialog(null, "Do you realy want to delete selected files?", "Delete files?",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.NO_OPTION) {
			return;
		}

		for (int i = intselected.length - 1; i >= 0; i--) {
			if (deleteFile((String) table.getValueAt(intselected[i], 1), lCmdLineLogging)) {
				tabModel.removeRow(intselected[i]);
			}
		}
	}

	private final String formatFileSize(long l) {
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

	private void clearRows(DefaultTableModel pMyTblModel) {
		int numrows = pMyTblModel.getRowCount();
		for (int i = numrows - 1; i >= 0; i--) {
			pMyTblModel.removeRow(i);
		}
	}

	private void chooseLogDirectory(ActionEvent evt) {
		JFileChooser jfilechooser = new JFileChooser(new File(txtDirectory.getText()));
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(txtDirectory.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(this);
		if (i == 0) {
			txtDirectory.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	public void clearSelections(JTable pTbl, DefaultTableModel pTblModel) {
		if (pTbl.getSelectedRow() != -1) {
			int numrows = pTblModel.getRowCount();
			for (int i = numrows - 1; i >= 0; i--) {
				pTbl.clearSelection();
			}
		}
	}

	private java.io.File[] getFiles(String pDirectory) {
		if (pDirectory.isEmpty()) {
			pDirectory = "C:\\Temp";
		}
		if (!new File(pDirectory).exists()) {
			if (!new File(pDirectory).mkdir()) {
				return null;
			}
		}
		return (new File(pDirectory)).listFiles();
	}

	private void fillTableRows(DefaultTableModel pTblModel, File[] pFiles) {
		clearRows(pTblModel);
		for (int i = 0; i < pFiles.length; i++) {
			pTblModel.addRow(new String[] { Integer.toString(i + 1), pFiles[i].getAbsolutePath(),
					formateDate(pFiles[i].lastModified()), formatFileSize(pFiles[i].length()) });
		}
	}

	private final String formateDate(long l) {
		return (new java.text.SimpleDateFormat("yyyy-MM-dd : HH-mm-ss")).format(new Date(l));
	}

	public FileDeleterGUI(String pDirectory, String[] pColumnNames) {
		this.setPreferredSize(new java.awt.Dimension(800, 700));
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(this);

		tabModel = new DefaultTableModel(null, pColumnNames);
		table = new JTable(tabModel);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		// table.setBackground(Color.yellow);

		fillTableRows(tabModel, getFiles(pDirectory));

		JScrollPane scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel();

		pnlDirectory = new JPanel();
		lblDirectory = new JLabel();
		pnlDirectory.add(lblDirectory);
		lblDirectory.setText("Directory");
		lblDirectory.setPreferredSize(new java.awt.Dimension(59, 14));
		txtDirectory = new JTextField();
		pnlDirectory.add(txtDirectory);
		txtDirectory.setText(pDirectory);
		txtDirectory.setPreferredSize(new java.awt.Dimension(444, 21));
		btnOpenFileDlg = new JButton();
		pnlDirectory.add(btnOpenFileDlg);
		btnOpenFileDlg.setText("Open...");
		btnOpenFileDlg.setPreferredSize(new java.awt.Dimension(107, 21));
		btnOpenFileDlg.setSize(114, 21);
		btnOpenFileDlg.addActionListener(this);

		btnSelectAll = new JButton("Select All");
		btnClearAll = new JButton("Clear All");
		btnDeleteFiles = new JButton("Delete Selected Files");
		btnShowFiles = new JButton("Refresh Table");
		btnOpenSelectedFile = new JButton("Open File");

		buttonPanel.add(btnSelectAll);
		buttonPanel.add(btnClearAll);
		buttonPanel.add(btnDeleteFiles);
		buttonPanel.add(btnOpenSelectedFile);
		buttonPanel.add(btnShowFiles);

		btnSelectAll.addActionListener(this);
		btnClearAll.addActionListener(this);
		btnDeleteFiles.addActionListener(this);
		btnShowFiles.addActionListener(this);
		btnOpenSelectedFile.addActionListener(this);

		this.setTitle("Deleting files");
		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(pnlDirectory, "North");

		mainPanel.add(scrollPane, "Center");

		mainPanel.add(buttonPanel, "South");

		this.getContentPane().add(mainPanel);

		setBounds(40, 40, 400, 800);

		this.pack();

		this.setVisible(true);
	}
}
