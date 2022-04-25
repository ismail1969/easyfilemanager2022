package eu.ismailozer.easyfilemanager;

import java.io.*;
//import java.lang.reflect.InvocationTargetException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.text.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
//import java.util.Map.Entry;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import eu.ismailozer.easyfilemanager.not_used.ProcessTimeController;

//import eu.ismailozer.easyfilemanager.not_used.CompareBinaries;

//import MyEventQueue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainGUI extends javax.swing.JFrame
		implements ItemListener, ActionListener, KeyListener, WindowListener, MouseListener, MouseMotionListener,
		InputMethodListener, FocusListener, ChangeListener, TableModelListener, PropertyChangeListener {

	private static String DEFAULT_COMPARE_DIR = "defaultCompareDir";

	private static String DEFAULT_SEARCH_DIR = "defaultSearchDir";

	private static final Dimension DIMENSION_100_22 = new java.awt.Dimension(100, 22);

	private static final Dimension DIMENSION_500_22 = new java.awt.Dimension(500, 22);
	private static final double HUNDERD = 100;
//	private static int BUFFSIZE = 0x2000; // 8192
//	private static int BUFFSIZE_MULTIPLE = 1;
	private static final java.util.Locale LOCALE_TR = new java.util.Locale("tr");
	private static double NUMBER_ENTRIES_PER_PORCENT;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7272088068571396768L;
	private static final double TEN = 10;
	private static int TOTAL_FOUND_ENTRIES;

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase(LOCALE_TR).toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				// chars[i] = Character.toUpperCase(chars[i]);
				// tr I dont work otherwise
				chars[i] = Character.toString(chars[i]).toUpperCase(LOCALE_TR).charAt(0);
				// chars[i] = lStr.charAt(0);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\''
					|| !Character.isLetter(chars[i])) {
				found = false;
			}
		}
		return String.valueOf(chars);
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

	public static String getTimestamp() {
		return new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date()).toString();
	}

	public static void main(String[] args) { // throws InterruptedException, InvocationTargetException {
		// try {

		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// // FileManagerGUI inst = new FileManagerGUI(
		// // "filemanager.properties");
		// new MainGUI("filemanager.properties");
		// }
		// });

		// java.awt.EventQueue.invokeLater(new Runnable() {
		// MyEventQueue.invokeLater(new Runnable() {
		// public void run() {
		// new MainGUI("filemanager.properties").setVisible(true);
		// }
		// });
		//
		// } catch (Exception e) {
		//
		// System.out.println("Exception: >>> " + e.getMessage() + "<<<");
		// }

		// Toolkit.getDefaultToolkit().getSystemEventQueue().push(new
		// MyEventQueue());

		// 2017.02.16

		// EventQueue eventQueue =
		// Toolkit.getDefaultToolkit().getSystemEventQueue();
		// MyEventQueue myEventQueue = new MyEventQueue();
		// eventQueue.push(myEventQueue);
		//
		try {
			Toolkit.getDefaultToolkit().getSystemEventQueue().push(new MyEventQueue());

			EventQueue.invokeLater(new Runnable() {
				// myEventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					// System.out.println("Run");

					MainGUI mainGUI = new MainGUI("filemanager.properties");
					mainGUI.setVisible(true);
				}
			});
		} catch (Exception e) {
			System.out.println("Ausnahme: " + e.getMessage());
		}
	}

	public static String replaceFirst(String s, String sub, String with) {
		int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	public static void showInfoMsg(String pMsg, String pTitle) {
		JOptionPane.showMessageDialog(null, pMsg, pTitle, JOptionPane.INFORMATION_MESSAGE);
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

	private JButton btnChooseCompareDir;
	private JButton btnChooseDir;
	private JButton btnClearAll;
	private JButton btnConvertImage;
	private JButton btnCopyFilesTo;
	private JButton btnCreateCSV;
	private JButton btnCreateXML;
	private JButton btnCreationFrom;
	private JButton btnCreationTo;
	private JButton btnDeleteDuplicateFilesLeft;
	private JButton btnDeleteDuplicateFilesRight;
	private JButton btnDeleteFiles;
	private JButton btnEditTextFile;
	private JButton btnExportTable;
	private ButtonGroup btnGrpFileSize;
	private ButtonGroup btnGrpTransform;
	private JButton btnOpenfile;
	private JButton btnOpenFileFolder;
	private JButton btnOpenSelectedFile;
	private JButton btnRemoveFomList;
	private JButton btnRenameSelectedFiles;
	private JButton btnResetRenameOpt;
	private JButton btnResetSearchOpt;
	private JButton btnsearchDuplicateFiles;
	private JButton btnSearchFiles;
	private JButton btnSelectAll;
	private JButton btnStopRunner;
	private JButton btnViewLogFiles;
	private JButton btnZoomoutPicture;
	private final String bufferSizeFactorList[] = { "1", "2", "4", "8", "16", "32" };
	private final String bufferSizeList[] = { "512", "1024", "2048", "4096", "8192", "16384" };
	JCheckBoxMenuItem cbMenuItem;
	private JComboBox<String> cbxBufferSize;
	private JComboBox<String> cbxBufferSizeFactor;
	private JComboBox<String> cbxFileExtentions;
	private JCheckBox cbxIncludeDirectories;
	private JCheckBox cbxIncludeFiles;
	private JComboBox<String> cbxItemsLeft;
	private JComboBox<String> cbxItemsRight;
	private JComboBox<String> cbxLeadingZeros;
	private JComboBox<String> cbxLefOffset;
	private JCheckBox cbxLoggingCommandLine;
	private JCheckBox cbxLoggingInFile;
	private JCheckBox cbxRecursiveMode;
	private JCheckBox cbxResetCounter;
	private JComboBox<String> cbxRightOffset;
	private JComboBox<String> cbxSearchLimits;
	private JCheckBox cbxSuccessiveUppercaseChars;
	private final String compareLimitList[] = { "1", "10", "50", "100", "200", "500", "1000", "2000", "5000", "99999" };
	protected int counter = 0;
	private Date dateCreationFrom;
	private Date dateCreationTo;
	private String DATEFORMATPATTERN = "dd.MM.yyyy";
	private String defaultCompareDir;
	private String defaultSearchDir = "C:\\Temp";
	Dimension DIMENSION_140_22 = new Dimension(140, 22);
	// private int LOOP_COUNTER = 1000;
	// private int LOOP_COUNTER = 2000;
	private Dimension dimensionPreview = new Dimension(400, 330);
	private int DIR_COUNTER;
	private DuplicateTableModel dublicateTableModel;
	TreeSet<String> extTreeSet = new TreeSet<String>();
	private int FILE_COUNTER;
	String[] fileExtentionList = { "", "mp3", "jpg", "jpeg", "gif", "png", "bmp", "csv", "java", "htm", "html", "php",
			"txt", "log", "ini", "xml", "sql", "avi", "mpeg", "mp4", "mov", "zip", "pdf", "exe", "jar" };
	private String FILEMANAGER_COPYRIGHT = "(c) www.ismailozer.eu";
	private String FILEMANAGER_LABEL = "Easy File Manager";
	private String FILEMANAGER_RELEASE = "2022.4";
	private String FILEMANAGER_RELEASE_DATE = "24.04.2022";
	FileRenamer filerenamer;
	FileSearchParameters fileSearchParameters;
	private int HEIGHT_FRM;
	private int HEIGHT_SCR;
	private String HYPHEN = " - ";
	boolean isSearchOptionEmtyp;
	boolean isSearchOptionFilled;
	private JTabbedPane jTabbedPanelForTables;
	private JTabbedPane jTabbedPaneOptionsTop;
	private JLabel lblBufferSize;
	private JLabel lblBufferSizeFactor;
	private JLabel lblCompareDir;
	private JLabel lblContainStr;
	private JLabel lblEndsWith;
	private JLabel lblFileInfo;
	private JLabel lblFileSize;
	private JLabel lblItemsLeft;
	private JLabel lblItemsRight;
	private JLabel lblLeftOffset;
	private JLabel lblPrefix;
	private JLabel lblRegExp;
	private JLabel lblRegexpReplaceNew;
	private JLabel lblRegexpReplaceOld;
	private JLabel lblReplace;
	private JLabel lblReplaceAll;
	private JLabel lblReplWith;
	private JLabel lblRightStart;
	private JLabel lblSequence;
	private JLabel lblSourceDir;
	private JLabel lblStartsWith;
	private JLabel lblSuffix;
	private String LOG_DIR = "log_";
	private String LOG_FILE = "log_";
	protected int maxValue = 100;
	JMenu menu, submenu;
	// Where the GUI is created:
	JMenuBar menuBar;
	JMenuItem menuItem;
	private JMenuItem menuItemCopyFilesTo;
	private JMenuItem menuItemEditTextFile;
	JMenuItem menuItemImportToCompareTbl;
	JMenuItem menuItemMoreLessItems;
	private JMenuItem menuItemOpenFileFolder;
	protected int minValue = 0;
	private JMenuItem openFileFolder;
	/**
	 * Auto-generated main method to display this JFrame
	 */

	// EasyProgressViewer progress;
	JPanel pnlBottomCmdButton;
	private JPanel pnlCompareDir;
	private JPanel pnlCompareFileOptions;
	private JPanel pnlDirectory;
	private JPanel pnlFilePreview;
	JPanel pnlInfo;
	private JPanel pnlNoPreview;
	private JPanel pnlOptions;
	private JPanel pnlRenameOptions;
	private JPanel pnlSearchOptions;
	private JPanel pnlTableForCompare;
	private JPanel pnlTableForRenameFiles;
	private JPanel pnlTransformOpt;
	private JPopupMenu popupMenu;
	private int preview_height = 320;
	private int preview_width = 400;

	protected JProgressBar progressBar;
	protected int progressBarValue;
	// private ProgressMonitor progressMonitor;
	private Properties prop = new Properties();
	private String PROPS_FILE = "filemanager.properties";
	JRadioButtonMenuItem rbMenuItem;
	private JRadioButton rbtAllSize;
	private JRadioButton rbtCapitalize;
	private JRadioButton rbtFileSizeEqual;
	private JRadioButton rbtFileSizeGreater;
	private JRadioButton rbtFileSizeLower;
	private JRadioButton rbtLowerCase;
	private JRadioButton rbtNormal;

	private JRadioButton rbtParentAsFilename;
	private JRadioButton rbtUpperCase;

	private JScrollPane scrollPaneForCompare;

	private JScrollPane scrollPaneForRenameFiles;
	private SimpleDateFormat sDateFormt = new SimpleDateFormat(DATEFORMATPATTERN);

	Thread searchDublicateThread;

	private final String searchLimitList[] = { "All", "10", "50", "100", "200", "500", "1000", "2000", "5000", "10000",
			"9999999" };

	private SearcherTableModel searchTableModel;
	Thread searchThread;
	private final String sequences[] = { "None", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
	private StringBuffer STR_BUFFER;
	private JTable tableCompareResult;
	private JTable tableSearchResult;

	private JTextField txtBufferSize;
	private JTextField txtBufferSizeFactor;

	private JTextField txtCompareDir;

	private JTextField txtContainStr;

	private ObservingTextField txtCreationDateFrom = null;
	private ObservingTextField txtCreationDateTo = null;
	private JTextField txtEndsWith;
	private JTextField txtFileSize;

	// private JPanel pnlSearchOptionAdditional;
	// private JRadioButton rbtSuccessiveUppercaseChars;

	private JTextField txtItemsLeft;

	private JTextField txtItemsRight;

	private JTextField txtLeftOffset;
	private JTextField txtPrefix;

	private JTextField txtRegexpReplaceNew;

	private JTextField txtRegexpReplaceOld;

	// private JLabel lblEntries;
	//
	// private JTextField txtEntries;
	// 01.01.2018

	private JTextField txtReplaceAll;

	private JTextField txtReplaceNew;

	private JTextField txtReplaceOld;

	private JTextField txtRightOffset;

	private JTextField txtSearchDir;

	private JTextField txtSearchPattern;

	private JTextField txtSequence;

	private JTextField txtStartsWith;

	private JTextField txtSuffix;

	private int WIDTH_FRM;

	private int WIDTH_SCR;

	private int ZOOM_FACTOR = 100;

	public MainGUI() {
		super();
	}

	public MainGUI(String pPropFile) {
		super();
		// loadPropertiesFile(pPropFile);
		// setPropsParameter();
		initGUI();
		// GUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnSearchFiles) {
			// searchFiles();
			// searchNew();
			searchFiles();
		} else if (source == btnSelectAll) {
			// tableSearchResult.selectAll();
			selectAll();
		} else if (source == btnRemoveFomList) {
			// tableSearchResult.selectAll();
			removeFomList();
			// testThread();
		} else if (source == btnClearAll) {
			clearSelection();
		} else if (source == btnDeleteFiles) {
			deleteSelectedSearchFiles();
		} else if (source == btnDeleteDuplicateFilesLeft) {
			deleteDuplicateFiles(1);
		} else if (source == btnDeleteDuplicateFilesRight) {
			deleteDuplicateFiles(5);
		} else if (source == btnOpenfile) {
			// openLogFile();
		} else if (source == btnChooseDir) {
			// chooseDirectory(event);
			setPath(txtSearchDir);
			// defaultDir = txtDirectory.getText();
		} else if (source == btnChooseCompareDir) {
			setPath(txtCompareDir);
		} else if (source == btnViewLogFiles) {
			viewLogFiles();
		} else if (source == txtContainStr) {
			txtSearchPattern.setEditable(false);
		} else if (source == btnOpenSelectedFile) {
			openSelectedFile(tableSearchResult, searchTableModel);
		} else if (source == btnCreateCSV) {
			createCSVFile(tableSearchResult);
		} else if (source == btnCreateXML) {
			createXMLFile(tableSearchResult);
		} else if (source == btnExportTable) {
			exportTableToFile();
		} else if (source == btnCreationFrom) {
			// setCreationDateFrom();
			setDateField(txtCreationDateFrom, dateCreationFrom);
		} else if (source == btnCreationTo) {
			setDateField(txtCreationDateTo, dateCreationTo);
		} else if (source == btnRenameSelectedFiles) {
			renameSelectedFiles();
		} else if (source == cbxLeadingZeros) {
			String s = (String) cbxLeadingZeros.getSelectedItem();
			if (s.equals("None")) {
				txtSequence.setEditable(false);
				txtSequence.setText("");
			} else {
				int i = Integer.parseInt(s);
				txtSequence.setEditable(true);
				txtSequence.setColumns(i);
				txtSequence.setDocument(new JTextFieldLimit(i));
			}
		} else if (source == btnZoomoutPicture) {
			showFile(getSelectedFilename());
		} else if (source == btnsearchDuplicateFiles) {
			// searchDublicateFiles(txtSearchDir.getText(),
			// txtCompareDir.getText(), cbxRecursiveMode.isSelected(),
			// getSearchLimit());
			// startComparingDuplicateFiles();
			searchDublicateFiles();
		} else if (source == btnOpenFileFolder) {
			openFileFolder();
		} else if (source == btnEditTextFile) {
			showTextFile(getSelectedFilename());
		} else if (source == btnCopyFilesTo) {
			copySelectedFilesToDir();
		} else if (source == btnConvertImage) {
			convertSelectedImage();
		}
		// for menuitems from jtable context menu
		// JMenuItem menu = (JMenuItem) event.getSource();
		if (source == menuItemEditTextFile) {
			showTextFile(getSelectedFilename());
		} else if (source == menuItemCopyFilesTo) {
			copySelectedFilesToDir();
		} else if (source == menuItemOpenFileFolder) {
			openFileFolder();
		}
		if (source == btnResetSearchOpt) {
			resetSearchOption(pnlSearchOptions);
		}

		if (source == btnResetRenameOpt) {
			resetSearchOption(pnlRenameOptions);
		}
		if (source == menuItemMoreLessItems) {
			searchMoreLessItemsForDir();
		}
		// if (source == menuItemOpenFileFolder) {
		// openFileFolder();
		// }
		if (source == menuItemImportToCompareTbl) {
			importToCompareTbl();
		}
		if (source == btnStopRunner) {
			if (searchThread != null && searchThread.isAlive()) {
				searchThread.interrupt();
				// searchThread.stop();
			}

			if (searchDublicateThread != null && searchDublicateThread.isAlive()) {
				searchDublicateThread.interrupt();
			}
		}
	}

	public void addDataToCompareTable(DuplicateTableModel pDublicateTblModel, File pSourceFile, File pDublicateFile) {

		if (pSourceFile == null) {
			return;
		}
		try {
			pDublicateTblModel.addRow(new Object[] { Integer.valueOf(pDublicateTblModel.getRowCount() + 1),
					pSourceFile.getCanonicalPath(), getFileExtension(pSourceFile),
					getDateFromat().format(new Date((pSourceFile.lastModified()))), getFileSize(pSourceFile),
					pDublicateFile.getCanonicalPath(), getFileExtension(pDublicateFile),
					getDateFromat().format(new Date((pDublicateFile.lastModified()))), getFileSize(pDublicateFile),
					pSourceFile.isDirectory(), "Switch"

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addItemToDuplicateTable(DuplicateTableModel pDublicateTblModel, File pLeftFile, File pRightFile)
			throws java.lang.ArrayIndexOutOfBoundsException {
		try {
			if (pLeftFile.getCanonicalPath().length() == 0) {
				return;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// String leftFileExtension = getFileExtension(pLeftFile);
		// String rightFileExtension = getFileExtension(pRightFile);

		// System.out.println("addItemToDuplicateTable ");

		try {
			pDublicateTblModel.addRow(
					new Object[] { Integer.valueOf(pDublicateTblModel.getRowCount() + 1), pLeftFile.getCanonicalPath(),
							getFileExtension(pLeftFile), getDateFromat().format(pLeftFile.lastModified()),
							getFileSize(pLeftFile), pRightFile.getCanonicalPath(), getFileExtension(pRightFile),
							getDateFromat().format(pRightFile.lastModified()), getFileSize(pRightFile),
							pLeftFile.isDirectory(), "Switch"

					});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addItemToRenameTable(SearcherTableModel pTblModel, File pFile, int pIndex)
			throws java.lang.ArrayIndexOutOfBoundsException {
		if (pTblModel == null || pFile == null) {
			return;
		}
		try {
			if (pFile.getCanonicalPath().length() == 0) {
				return;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String lTargetFilename = getFileRenamer().getRenamedFilename(pFile, txtRegexpReplaceOld.getText(),
				txtRegexpReplaceNew.getText(), txtReplaceAll.getText(), txtReplaceOld.getText(),
				txtReplaceNew.getText(), txtPrefix.getText(), txtSuffix.getText(), rbtLowerCase.isSelected(),
				rbtUpperCase.isSelected(), rbtCapitalize.isSelected(), rbtParentAsFilename.isSelected(),
				txtSequence.getText(), (String) cbxLeadingZeros.getSelectedItem(), pIndex);

		boolean pCanRename = false;
		try {
			if (pFile.getCanonicalPath().compareTo(lTargetFilename) != 0) {
				pCanRename = true;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String fileExtension = getFileExtension(pFile);
		try {
			pTblModel.addRow(new Object[] { Integer.valueOf(pTblModel.getRowCount() + 1), pFile.getCanonicalPath(),
					lTargetFilename,
					// pFile.getParent() + getFileSeparator() + lTargetFilename,
					fileExtension, getDateFromat().format(pFile.lastModified()), getFileSize(pFile), pCanRename,
					pFile.isDirectory() });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addItemToRenameTable(SearcherTableModel pTblModel, String pSourceFilename, String pTargetFilename,
			int pSequenceCounter) throws java.lang.ArrayIndexOutOfBoundsException {
		if (pSourceFilename.length() == 0 || pTargetFilename.length() == 0) {
			return;
		}
		// TODO Auto-generated method stub

		boolean pCanRename = false;
		if (pSourceFilename.compareTo(pTargetFilename) != 0) {
			pCanRename = true;
		}

		pTblModel.addRow(new Object[] { Integer.valueOf(pTblModel.getRowCount() + 1), pSourceFilename, pTargetFilename,
				getFileExtension(pSourceFilename),
				getDateFromat().format(new Date((new File(pSourceFilename).lastModified()))),
				getFileSize(pSourceFilename), pCanRename, new File(pSourceFilename).isDirectory() });
	}

	private void addMyKeyListener(JTextField pTextField) {
		//

		pTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				/* Restrict input to only integers */
				if (key < 96 && key > 105) {
					e.setKeyChar('1');
					pTextField.setText("1");
				}
			}
		});

	}

	private void addToLogBuffer(String pInfoText) {
		STR_BUFFER.append(pInfoText).append(System.getProperty("line.separator").toString());
	}

	@Override
	public void caretPositionChanged(InputMethodEvent arg0) {

	}

	private void changeFileInfoText() {
		lblFileInfo.setText(getSelectedFilename());
	}

	private void changeFrameTitle() {
		if (getSelectedFilename().isEmpty()) {
			this.setTitle(getReleaseInfo());
			return;
		}
		this.setTitle(getReleaseInfo() + HYPHEN + getSelectedFilename());
	}

	private void checkToWriteLogFile() {
		if (cbxLoggingInFile.isSelected()) {
			if (STR_BUFFER.length() > 0 && STR_BUFFER != null) {
				writeToLogFile(getLogFilename(LOG_FILE, "log"), STR_BUFFER, "UTF-8");
			}
		}
	}

	private void clearRows(javax.swing.table.DefaultTableModel pTabModel) {
		if (pTabModel == null || pTabModel.getTableModelListeners().length == 0) {
			return;
		}
		int numrows = pTabModel.getRowCount();
		for (int i = numrows - 1; i >= 0; i--) {
			pTabModel.removeRow(i);
		}
		repaint();
	}

	private void clearSelection() {
		// TODO Auto-generated method stub
		getActiveTable().clearSelection();

	}

	private void cmdLineLogging(Object pObject) {
		if (pObject != null && cbxLoggingCommandLine.isSelected()) {
			System.out.println(pObject.toString());
		}
	}

	// private double getNumberFilesPerProcent(int pAllFiles) {
	// return (double) pAllFiles / (double) 100;
	// }

	private void convertSelectedImage() {
		if (!isImageFile(getSelectedFilename())) {
			JOptionPane.showMessageDialog(null, "No image file selected: " + getSelectedFilename());
			return;
		}

		if (JOptionPane.showConfirmDialog(null, "Convert selected image?", "Convert image?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {

			if (tableSearchResult.getRowCount() > 1) {
				if (JOptionPane.showConfirmDialog(null, "Convert all selected images?", "Batch convert of images?",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {

					String lTargetFormat = "jpg";
					lTargetFormat = JOptionPane.showInputDialog(null, "Give your target image format.", "jpg");
					int[] intselected = tableSearchResult.getSelectedRows();
					java.util.List<String> img_filelist = new ArrayList<String>();

					for (int i = intselected.length - 1; i >= 0; i--) {
						img_filelist.add((String) tableSearchResult.getValueAt(i, 1));
					}
					new BatchImageConverterGUI(img_filelist, lTargetFormat, false);
				}
			} else {
				new ImageConverterGUI(getSelectedFilename(), getSelectedFilename(), "jpg");
			}
		}
	}

	private boolean copyFileTo(File pSource, File pTarget) {
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
			if (cbxLoggingCommandLine.isSelected()) {
				log("Copy file successfull : " + pSource.getAbsolutePath() + " -> " + pTarget.getAbsolutePath());
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

	private void copySelectedFilesToDir() {
		// copyFileTo(File pSouce, File pTarget);
		// This was not a directory, so lets just copy the file
		int counter = 0;
		int[] intselected = tableSearchResult.getSelectedRows();
		if (tableSearchResult.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "No file(s) seleted.");
			return;
		} else if (intselected.length == 0) {
			JOptionPane.showMessageDialog(this, "For copy file(s) select one or more files.");
			return;
		}

		if (JOptionPane.showConfirmDialog(null, "Do you realy copy the files to " + " another directory?",
				"Delete files?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			String lCopyDir = "C:\\Temp\\_clones_" + getTimestamp();
			String answer = (JOptionPane.showInputDialog(null, "To splitt in many files enter your value.", lCopyDir));
			File lSelDir = null;
			if (answer != null) {
				lSelDir = new File(answer);
			} else {
				lSelDir = new File(lCopyDir);
			}
			if (!lSelDir.isDirectory()) {
				lSelDir.mkdirs();
			}
			boolean bRecursive = true;
			if (lSelDir != null && lSelDir.isDirectory()) {
				for (int i = intselected.length - 1; i >= 0; i--) {
					String sourceFile = (String) tableSearchResult.getValueAt(i, 1);
					File source = new File(sourceFile);
					String lOnlyFilename = getOnlyFilenameFromPath(sourceFile);

					File target = null;
					if (bRecursive) {
						int start_Ind = sourceFile.indexOf(":");
						int end_Ind = sourceFile.lastIndexOf(File.separator);
						String targetPath = sourceFile.substring(start_Ind + 1, end_Ind);
						targetPath = answer + targetPath;
						if (!new File(targetPath).isDirectory()) {
							new File(targetPath).mkdirs();
						}
						target = new File(targetPath, lOnlyFilename);
					} else {
						target = new File(lSelDir, lOnlyFilename);
					}

					if (target != null && target.exists()) {
						target = new File(lSelDir, "Copy_Of_" + i + "_" + lOnlyFilename);
					}
					if (target != null && source != null) {
						if (copyFileTo(source, target)) {
							counter++;
						} else {
							JOptionPane.showMessageDialog(this, "Error copy file " + source + " --> " + target);
							break;
						}
					}
				}
			}
			JOptionPane.showMessageDialog(this, "Total copied files: " + counter);
		}
	}

	private void createCSVFile(JTable pTable) {
		int anzahl_files = 1;
		String answer = (JOptionPane.showInputDialog(null, "To splitt in many files enter your value.", "1"));

		if (answer != null && Integer.parseInt(answer) > 1) {
			anzahl_files = Integer.parseInt(answer);
		}
		int total_rows = pTable.getRowCount();
		int rows_in_file = (int) java.lang.Math.ceil((float) total_rows / anzahl_files);

		log("anzahl_files = " + anzahl_files);

		log("rows_in_file = " + rows_in_file);

		String out_file = getLogFilename("turku_listesi_", "");

		for (int i = 1; i <= anzahl_files; i++) {
			writeCSVFile(out_file + "_" + (i * rows_in_file) + ".csv", tableSearchResult,
					i * rows_in_file - rows_in_file, i * rows_in_file);
		}
	}

	private void createXMLFile(JTable pTable) {
		int anzahl_files = 1;
		String answer = (JOptionPane.showInputDialog(null, "To splitt in many files enter your value.", "1"));

		if (answer != null && Integer.parseInt(answer) > 1) {
			anzahl_files = Integer.parseInt(answer);
		}
		int total_rows = pTable.getRowCount();
		int rows_in_file = (int) java.lang.Math.ceil((float) total_rows / anzahl_files);

		log("anzahl_files = " + anzahl_files);

		log("rows_in_file = " + rows_in_file);

		String out_file = getLogFilename("turku_listesi_", "");

		// create xml file
		WriteAlbumListToXMLFile(tableSearchResult, out_file + "rows." + total_rows + ".xml");
	}

	// private void startComparingDuplicateFiles() {
	// searchDublicateFilesWithParameterThread(txtSearchDir.getText(),
	// txtCompareDir.getText(),
	// cbxRecursiveMode.isSelected(), cbxIncludeFiles.isSelected(),
	// cbxIncludeDirectories.isSelected(),
	// getSearchLimit(), cbxResetCounter.isSelected(), true,
	// txtStartsWith.getText(), txtEndsWith.getText(),
	// txtContainStr.getText(), txtSearchPattern.getText(), txtFileSize.getText(),
	// rbtAllSize.isSelected(),
	// rbtFileSizeGreater.isSelected(), rbtFileSizeLower.isSelected(),
	// rbtFileSizeEqual.isSelected(),
	// txtCreationDateFrom.getText(), txtCreationDateTo.getText(),
	// cbxSuccessiveUppercaseChars.isSelected());
	// }

	private void deleteDuplicateFiles(int pTableIndex) {
		// TODO Auto-generated method stub
		if (pTableIndex == 0) {
			pTableIndex = 5;
		}
		if (jTabbedPanelForTables.getSelectedIndex() == 1 && tableCompareResult.getRowCount() > 0) {
			int coloumn_index = tableCompareResult.getColumn(DuplicateTableModel.TABLE_COLUMN_COMPARISON[pTableIndex])
					.getModelIndex();
			deleteSelectedFiles(tableCompareResult, coloumn_index);
			// lContext = "Compare";
		}

		// repaint();
		try {
			if (tableCompareResult.getRowCount() > 0) {
				selectFirstEntry();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteFile(File pFile, boolean pMoveToTrash) {
		return pFile.delete();
	}

	// private void searchFiles() {
	// searchFilesWithParameterThread(txtSearchDir.getText(),
	// cbxRecursiveMode.isSelected(),
	// cbxIncludeFiles.isSelected(), cbxIncludeDirectories.isSelected(),
	// getSearchLimit(),
	// cbxResetCounter.isSelected(), true, txtStartsWith.getText(),
	// txtEndsWith.getText(),
	// txtContainStr.getText(), txtSearchPattern.getText(), txtFileSize.getText(),
	// rbtAllSize.isSelected(),
	// rbtFileSizeGreater.isSelected(), rbtFileSizeLower.isSelected(),
	// rbtFileSizeEqual.isSelected(),
	// txtCreationDateFrom.getText(), txtCreationDateTo.getText(),
	// cbxResetCounter.isSelected(),
	// cbxSuccessiveUppercaseChars.isSelected());
	// }

	public boolean deleteFile(String pFilename) throws IOException {
		File f = new File(pFilename);
		// Make sure the file or directory exists and isn't write protected
		if (!f.exists()) {
			String lInfo = "Delete: no such file or directory: " + pFilename;
			log(lInfo);
			// EasyUtility.showInfoMsg(lInfo, "Delete Files");
			return false;
			// If it is a directory, make sure it is empty
		} else if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0) {
				String lInfo = "Delete: directory not empty: " + pFilename;
				log(lInfo);
				showInfoMsg(lInfo, "Directory not empty");
				return false;
			} else {

				if (!this.deleteFile(f, false)/* !f.delete() */) {
					log("Delete=>: deletion failed for : " + pFilename);
					return false;
				} else {
					if (cbxLoggingCommandLine.isSelected()) {
						log("Delete=>: deletion succesful : " + pFilename);
					}
					return true;
				}
			}
		} else {

			if (!this.deleteFile(f, true)/* !f.delete() */) {
				log("Delete=>: deletion failed for : " + pFilename);
				return false;
			} else {
				log("Delete=>: deletion succesful : " + pFilename);
				return true;
			}
		}
	}

	private void deleteSelectedFiles(JTable pTable, int pColumnIndex) {
		int[] intselected = pTable.getSelectedRows();
		if (pTable.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "There is no file(s) to delete in list.");
			return;
		} else if (intselected.length == 0) {
			JOptionPane.showMessageDialog(this, "For deletion select one or more files.");
			return;
		}

		if (JOptionPane.showConfirmDialog(null,
				"Do you realy want to delete " + pTable.getSelectedRowCount() + " selected file(s)?", "Delete files?",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			int counter = 0;

			ProcessTimeController.start();
			DefaultTableModel defModel = (DefaultTableModel) pTable.getModel();
			for (int i = intselected.length - 1; i >= 0; i--) {
				try {
					if (deleteFile((String) pTable.getValueAt(intselected[i], pColumnIndex))) {
						defModel.removeRow(intselected[i]);
						// repaint(); // 2017.02.10??
						counter++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ListSelectionModel model = pTable.getSelectionModel();
			if (pTable.getRowCount() > 1) {
				model.setSelectionInterval(0, 0);
			}

			ProcessTimeController.end();
			// progress.stop();
			JOptionPane.showMessageDialog(this, "Total deleted files: " + Integer.toString(counter) + "/ Process time: "
					+ ProcessTimeController.getProzessTime() + " Sec.");
		}
	}

	private void deleteSelectedSearchFiles() {
		// String lContext = "Search";
		if (jTabbedPanelForTables.getSelectedIndex() == 0 && tableSearchResult.getRowCount() > 0) {
			int coloumn_index = tableSearchResult.getColumn(SearcherTableModel.TABLE_COLUMN_SEARCH[1]).getModelIndex();
			deleteSelectedFiles(tableSearchResult, coloumn_index);
		}

		repaint();
		try {
			if (tableSearchResult.getRowCount() > 0) {
				selectFirstEntry();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception: >>> " + "called by " + "("
					+ Locale.class.getEnclosingMethod().getName().toString() + ")" + e.getMessage() + "<<<");
			e.printStackTrace();

		}
	}

	private void determinePreferedSizes() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH_SCR = d.width;
		HEIGHT_SCR = d.height;

		WIDTH_FRM = (WIDTH_SCR * ZOOM_FACTOR / 100);
		WIDTH_FRM = WIDTH_FRM > WIDTH_SCR ? WIDTH_SCR : WIDTH_FRM;

		HEIGHT_FRM = (HEIGHT_SCR * ZOOM_FACTOR / 100);
		HEIGHT_FRM = HEIGHT_FRM > HEIGHT_SCR ? HEIGHT_SCR : HEIGHT_FRM;
	}

	private boolean dirExist(String pDirectory) {
		// TODO Auto-generated method stub
		if (new File(pDirectory).exists() == false) {
			showMessage("No directory found: [" + pDirectory + "]!");
			return false;
		}
		return true;
	}

	public void exportJTable(JTable table, File file, String pSeparator) {
		try {
			TableModel model = table.getModel();
			BufferedWriter excel = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

			for (int i = 0; i < model.getColumnCount(); i++) {
				excel.write(model.getColumnName(i) + pSeparator);
			}

			excel.write("\n");

			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < model.getColumnCount(); j++) {
					excel.write(model.getValueAt(i, j).toString() + pSeparator);
				}
				excel.write("\n");
			}
			excel.close();
			showMessage("Table exported successfully to " + file.getAbsolutePath().toString());

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void exportTableToFile() {
		JTable lTable = new JTable();
		String lExportSuffix = "file_";
		if (jTabbedPanelForTables.getSelectedIndex() == 0) {
			lTable = tableSearchResult;
			lExportSuffix = "search_";
		}

		if (jTabbedPanelForTables.getSelectedIndex() == 1) {
			lTable = tableCompareResult;
			lExportSuffix = "compare_";
		}

		if (lTable.getRowCount() > 0) {
			exportJTable(lTable, new File(getLogFilename(lExportSuffix, "csv")), ";");
		}
	}

	@Override
	public void focusGained(FocusEvent event) {
	}

	@Override
	public void focusLost(FocusEvent event) {
		Object source = event.getSource();
		if (source == tableSearchResult) {
			changeFileInfoText();
			changeFrameTitle();
			repaint();
		}
	}

	// private void searchDublicateFilesWithParameterThread(String pPathLeft, String
	// pPathRight, boolean pRecursiveMode,
	// boolean pIncludeFiles, boolean pIncludeDirectories, int pSearchLimit, boolean
	// pResetCounterValue,
	// boolean pIsSearchOptionFilled, String pStartsWith, String pEndsWith, String
	// pContainStr,
	// String pSearchPattern, String pFileSize, boolean pAllFileSize, boolean
	// pFileSizeGreater,
	// boolean pFileSizeLower, boolean pFileSizeEqual, String pCreationDateFrom,
	// String pCreationDateTo,
	// boolean pSuccessiveUppercaseChars) {
	// jTabbedPanelForTables.setSelectedIndex(1);
	// initProgressBar(progressBar);
	// compareThread = new Thread() {
	// public void run() {
	//
	// log(getTimeStamp());
	// initStringBuffer();
	// BUFFSIZE = getTextFieldValueAsInteger(txtBufferSize);
	// BUFFSIZE_MULTIPLE = getTextFieldValueAsInteger(txtBufferSizeFactor);
	// log("TOTAL BUFFER SIZE = " + new Integer((BUFFSIZE *
	// BUFFSIZE_MULTIPLE)).toString());
	// if (!dirExist(pPathLeft)) {
	// return;
	// }
	// if (pPathRight.length() > 0 && !dirExist(pPathRight)) {
	// return;
	// }
	//
	// boolean isSamePath = false;
	// // String lFoundDuplicateInfo;
	// String statText = ">>>> Compare file content";
	// String waitingText = "Please waiting ....";
	// log(statText);
	// log(waitingText);
	// cmdLineLogging(statText);
	// cmdLineLogging(waitingText);
	// String lPathLeft = pPathLeft;
	// String lPathRight = pPathRight;
	//
	// // if (progress == null) {
	// // progress = new EasyProgressViewer("Please waiting ....");
	// // }
	// //
	// // progress.setIntermediate(true);
	// // progress.setValues();
	// // progress.start();
	// // progress.setVisible(true);
	// ProcessTimeController.start();
	// getLeftItems();
	// getRightItems();
	// getLeftOffset();
	// getRightOffset();
	//
	// clearRows(tableModelCompare);
	// clearRows(tableModelSearch);
	//
	// if (lPathLeft.trim().length() < 1) {
	// lPathLeft = lPathRight;
	// }
	//
	// if (lPathRight.trim().length() < 1) {
	// lPathRight = lPathLeft;
	// }
	//
	// if (lPathRight.compareToIgnoreCase(lPathLeft) == 0) {
	// isSamePath = true;
	// }
	//
	// java.util.List<String> filelistLeft = new ArrayList<String>();
	// java.util.List<String> filelistRight = new ArrayList<String>();
	// // java.util.List<String> filesTemp = new ArrayList<String>();
	// ArrayList<String> rightListFound = new ArrayList<String>();
	// rightListFound.add("");
	// String lInfoTxt = "";
	//
	// ArrayList<String> leftListFound = new ArrayList<String>();
	// leftListFound.add("");
	//
	// try {
	// // filelistLeft = getFileListForDirectory(new File(lPathLeft),
	// pRecursiveMode,
	// // pIncludeFiles,
	// // pIncludeDirectories, pSearchLimit);
	// filelistLeft = getFileListForDirectory_2017(new File(lPathLeft),
	// pRecursiveMode, pIncludeFiles,
	// pIncludeDirectories, pSearchLimit);
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// int counter_dublicate = 1;
	// List<FileSizeCollector> fileSizeCollectorLeft = new
	// ArrayList<FileSizeCollector>();
	// List<FileSizeCollector> fileSizeCollectorRight = new
	// ArrayList<FileSizeCollector>();
	//
	// if (filelistLeft != null) {
	// fileSizeCollectorLeft = getMatchedFileList_NEW(filelistLeft,
	// pResetCounterValue, pStartsWith,
	// pEndsWith, pContainStr, pSearchPattern, pFileSize, pAllFileSize,
	// pFileSizeGreater,
	// pFileSizeLower, pFileSizeEqual, pCreationDateFrom, pCreationDateTo,
	// pSuccessiveUppercaseChars);
	//
	// if (!isSamePath) {
	// try {
	// filelistRight = getFileListForDirectory_2017(new File(lPathRight),
	// pRecursiveMode,
	// pIncludeFiles, pIncludeDirectories, pSearchLimit);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// fileSizeCollectorRight = getMatchedFileList_NEW(filelistRight,
	// pResetCounterValue, pStartsWith,
	// pEndsWith, pContainStr, pSearchPattern, pFileSize, pAllFileSize,
	// pFileSizeGreater,
	// pFileSizeLower, pFileSizeEqual, pCreationDateFrom, pCreationDateTo,
	// pSuccessiveUppercaseChars);
	// } else {
	// fileSizeCollectorRight = fileSizeCollectorLeft;
	// }
	// }
	//
	// if (fileSizeCollectorLeft != null && fileSizeCollectorRight != null) {
	// lInfoTxt = "Found files left: " + fileSizeCollectorLeft.size() + " / right: "
	// + fileSizeCollectorRight.size() + "\n";
	// log(lInfoTxt);
	// addToLogBuffer(lInfoTxt);
	// }
	//
	// FileSizeCollector leftCollector;
	// FileSizeCollector rightCollector;
	// ArrayList<String> foundInRightList = new ArrayList<String>();
	// int left_total_files = fileSizeCollectorLeft.size();
	// int j = 0;
	// int offset = 0;
	// int duplicate_counter = 0;
	// // long lengthOfAllFiles = 0;
	//
	// // for (int i = 0; i < fileSizeCollectorLeft.size(); i++) {
	// // lengthOfAllFiles += new
	// // File(fileSizeCollectorLeft.get(i).getFilename()).length();
	// // }
	// double numberFilesPerProcent =
	// getNumberFilesPerProcent(fileSizeCollectorLeft.size());
	// // long currentTotalFilesLength = 0;
	// for (int i = 0; i < fileSizeCollectorLeft.size(); i++) {
	// // currentTotalFilesLength += new
	// // File(fileSizeCollectorLeft.get(i).getFilename()).length();
	//
	// double quota = (i + 1) / numberFilesPerProcent;
	//
	// if ((int) quota % TEN == 0) {
	// progressBarValue = (int) quota;
	// }
	//
	// // float div = (float) (100 * currentTotalFilesLength / lengthOfAllFiles);
	// // counter = (int) div;
	// // if (counter < 1) {
	// // counter = 1;
	// // }
	//
	// // progressBar.setValue(counter);
	//
	// // Thread thread4 = new Thread() {
	// // public void run() {
	// // progressBar.setValue(counter);
	// // // progressBar.setToolTipText(fileName);
	// // }
	// // };
	// // thread4.start();
	// lblFileInfo.setText("Process: " + (i + 1) + " / " +
	// fileSizeCollectorLeft.size());
	//
	// Runnable runme4 = new Runnable() {
	// public void run() {
	// // progressBar.setValue(counter);
	// progressBar.setValue(progressBarValue);
	// // progressBar.setToolTipText(fileName);
	// }
	// };
	// SwingUtilities.invokeLater(runme4);
	//
	// // if (counter >= 1) {
	// // counter++;
	// // }
	//
	// // counter++;
	// // try {
	// // Thread.sleep(100);
	// // } catch (Exception ex) {
	// // }
	//
	// if (i % LOOP_COUNTER == 0) {
	// log(getTimeStamp());
	// String lInfo = i + " from " + left_total_files + " ---> Current processed
	// file / size: "
	// + fileSizeCollectorLeft.get(i).getFilename() + " /"
	// + fileSizeCollectorLeft.get(i).getFilelength();
	// log(lInfo);
	// logOnGUI(lInfo);
	// logOnTitle(lInfo);
	// }
	// for (j = offset; j < fileSizeCollectorRight.size(); j++) {
	// leftCollector = fileSizeCollectorLeft.get(i);
	// rightCollector = fileSizeCollectorRight.get(j);
	// if (leftCollector.getFilename().compareTo(rightCollector.getFilename()) == 0)
	// {
	// continue;
	// }
	//
	// if (foundInRightList.contains(leftCollector.getFilename())) {
	// continue; // for actuall file we have a entry
	// }
	//
	// if (leftCollector.getFilelength() > rightCollector.getFilelength()) {
	// continue;
	// }
	// // if (rightCollector.getFilelength() == leftCollector.getFilelength()) {
	// // if (CompareBinaries.fileContentsEquals(leftCollector.getFilename(),
	// // rightCollector.getFilename(), BUFFSIZE * BUFFSIZE_MULTIPLE)) {
	// // offset = j;
	// // counter_dublicate++;
	// // String lInfo = counter_dublicate + ".) Duplicate file " +
	// // leftCollector.getFilename()
	// // + " (i =" + i + ") /size : " + leftCollector.getFilelength() + " ---> "
	// // + rightCollector.getFilename() + " (j =" + i + ") /size : "
	// // + rightCollector.getFilelength();
	// // log(lInfo);
	// // // logOnGUI(lInfo);
	// // logOnTitle(lInfo);
	// // addToLogBuffer(lInfo);
	// // addDataToCompareTable(tableModelCompare, new
	// // File(leftCollector.getFilename()),
	// // new File(rightCollector.getFilename()));
	// // foundInRightList.add(rightCollector.getFilename());
	// // duplicate_counter++;
	// // }
	// // }
	//
	// if (rightCollector.getFilelength() == leftCollector.getFilelength()) {
	// // Path pathLeft = Paths.get(leftCollector.getFilename());
	// // Path pathRight = Paths.get(rightCollector.getFilename());
	// if (CompareBinaries.sameContentFiles(new File(leftCollector.getFilename()),
	// new File(rightCollector.getFilename()))) {
	// offset = j;
	// counter_dublicate++;
	// String lInfo = counter_dublicate + ".) Duplicate file " +
	// leftCollector.getFilename()
	// + " (i =" + i + ") /size : " + leftCollector.getFilelength() + " ---> "
	// + rightCollector.getFilename() + " (j =" + i + ") /size : "
	// + rightCollector.getFilelength();
	// log(lInfo);
	// // logOnGUI(lInfo);
	// logOnTitle(lInfo);
	// addToLogBuffer(lInfo);
	// addDataToCompareTable(tableModelCompare, new
	// File(leftCollector.getFilename()),
	// new File(rightCollector.getFilename()));
	// foundInRightList.add(rightCollector.getFilename());
	// duplicate_counter++;
	// }
	// }
	// }
	// }
	//
	// log("duplicate_counter = " + duplicate_counter);
	// jTabbedPanelForTables.setSelectedIndex(1);
	// ProcessTimeController.end();
	// int found_duplicate_entry = tableCompareResult.getRowCount();
	// if (found_duplicate_entry == 0) {
	// lInfoTxt += "No dublicate files found! ! Processtime: " +
	// ProcessTimeController.getProzessTime()
	// + " [s]";
	// } else if (tableCompareResult.getRowCount() > 0) {
	// try {
	// selectFirstEntry();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// lInfoTxt += "Found total dublicate files: " + found_duplicate_entry + "!
	// Processtime: "
	// + ProcessTimeController.getProzessTime() + " [s]";
	// log(lInfoTxt);
	// addToLogBuffer(lInfoTxt);
	// }
	// log(lInfoTxt);
	// // lblFileInfo.setText(lInfoTxt);
	// setFileInfoText(lblFileInfo, lInfoTxt);
	// logOnTitle(lInfoTxt);
	// logTimeStamp();
	// log("<<< Compare file content");
	// // lblFileInfo.setBackground(Color.GREEN);
	// // progress.stop();
	// showMessage(lInfoTxt);
	// checkToWriteLogFile();
	// // repaint();
	// // checkButtonStatus();
	// }
	// };
	// compareThread.start();
	//
	// }

	private JTable getActiveTable() {
		// TODO Auto-generated method stub
		if (jTabbedPanelForTables.getSelectedIndex() == 0) {
			return tableSearchResult;
		}

		if (jTabbedPanelForTables.getSelectedIndex() == 1) {
			return tableCompareResult;
		}
		return null;
	}

	// private FileListContainer getMatchedFileList(List<String> pFilelist, boolean
	// pResetCounterValue, String pStartsWith,
	// String pEndsWith, String pContainStr, String pSearchPattern, String
	// pFileSize, boolean pAllFileSize,
	// boolean pFileSizeGreater, boolean pFileSizeLower, boolean pFileSizeEqual,
	// String pCreationDateFrom,
	// String pCreationDateTo, boolean pSuccessiveUppercaseChars) {
	// // TODO Auto-generated method stub
	// FileListContainer fileContainer = new FileListContainer();
	// for (int i = 0; i < pFilelist.size(); i++) {
	//
	// try {
	// File lSearchedFile = new File(pFilelist.get(i));
	// if (matchFileSearchOptionsNew(lSearchedFile, pStartsWith, pEndsWith,
	// pContainStr, pSearchPattern,
	// pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower, pFileSizeEqual,
	// pCreationDateFrom,
	// pCreationDateTo, pSuccessiveUppercaseChars)) {
	//
	// fileContainer.addToList(pFilelist.get(i));
	// if (lSearchedFile.isFile()) {
	// fileContainer.incrementFileSize();
	// }
	// if (lSearchedFile.isDirectory()) {
	// fileContainer.incrementDirectorySize();
	// }
	// }
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return fileContainer;
	// }

	// private FileListContainer getMatchedFileList2018(List<String> pFilelist,
	// boolean pResetCounterValue,
	// String pStartsWith, String pEndsWith, String pContainStr, String
	// pSearchPattern, String pFileSize,
	// boolean pAllFileSize, boolean pFileSizeGreater, boolean pFileSizeLower,
	// boolean pFileSizeEqual,
	// String pCreationDateFrom, String pCreationDateTo, boolean
	// pSuccessiveUppercaseChars) throws ParseException {
	// // TODO Auto-generated method stub
	// FileMatcher fileMatcher = new FileMatcher();
	// FileListContainer fileContainer = new FileListContainer();
	// for (int i = 0; i < pFilelist.size(); i++) {
	//
	// File lSearchedFile = new File(pFilelist.get(i));
	// if (fileMatcher.isSearchedFileMatch(pFilelist.get(i), pResetCounterValue,
	// pStartsWith, pEndsWith,
	// pContainStr, pSearchPattern, pFileSize, pAllFileSize, pFileSizeGreater,
	// pFileSizeLower,
	// pFileSizeEqual, pCreationDateFrom, pCreationDateTo,
	// pSuccessiveUppercaseChars)) {
	//
	// // pFilelist.get(i).toString(), pStartsWith, pEndsWith, pContainStr,
	// // pSearchPattern,
	// // pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower, pFileSizeEqual,
	// // pCreationDateFrom,
	// // pCreationDateTo, pSuccessiveUppercaseChars)) {
	//
	// fileContainer.addToList(pFilelist.get(i));
	// if (lSearchedFile.isFile()) {
	// fileContainer.incrementFileSize();
	// }
	// if (lSearchedFile.isDirectory()) {
	// fileContainer.incrementDirectorySize();
	// }
	// }
	// }
	// return fileContainer;
	// }

	// private FileListContainer getMatchedFileList_2018(List<String> pFilelist,
	// boolean pResetCounterValue,
	// String pStartsWith, String pEndsWith, String pContainStr, String
	// pSearchPattern, String pFileSize,
	// boolean pAllFileSize, boolean pFileSizeGreater, boolean pFileSizeLower,
	// boolean pFileSizeEqual,
	// String pCreationDateFrom, String pCreationDateTo, boolean
	// pSuccessiveUppercaseChars) {
	// // TODO Auto-generated method stub
	// FileListContainer fileContainer = new FileListContainer();
	// for (int i = 0; i < pFilelist.size(); i++) {
	//
	// try {
	// File lSearchedFile = new File(pFilelist.get(i));
	// if (matchFileSearchOptionsNew(lSearchedFile, pStartsWith, pEndsWith,
	// pContainStr, pSearchPattern,
	// pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower, pFileSizeEqual,
	// pCreationDateFrom,
	// pCreationDateTo, pSuccessiveUppercaseChars)) {
	//
	// fileContainer.addToList(pFilelist.get(i));
	// if (lSearchedFile.isFile()) {
	// fileContainer.incrementFileSize();
	// }
	// if (lSearchedFile.isDirectory()) {
	// fileContainer.incrementDirectorySize();
	// }
	// }
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return fileContainer;
	// }

	// private List<FileSizeCollector> getMatchedFileList_NEW(List<String>
	// pFilelist, boolean pResetCounterValue,
	// String pStartsWith, String pEndsWith, String pContainStr, String
	// pSearchPattern, String pFileSize,
	// boolean pAllFileSize, boolean pFileSizeGreater, boolean pFileSizeLower,
	// boolean pFileSizeEqual,
	// String pCreationDateFrom, String pCreationDateTo, boolean
	// pSuccessiveUppercaseChars) {
	// // TODO Auto-generated method stub
	// // FileListContainer fileContainer = new FileListContainer();
	// List<FileSizeCollector> fileContainer = new ArrayList<FileSizeCollector>();
	// for (int i = 0; i < pFilelist.size(); i++) {
	//
	// try {
	// File lSearchedFile = new File(pFilelist.get(i));
	// if (matchFileSearchOptionsNew(lSearchedFile, pStartsWith, pEndsWith,
	// pContainStr, pSearchPattern,
	// pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower, pFileSizeEqual,
	// pCreationDateFrom,
	// pCreationDateTo, pSuccessiveUppercaseChars)) {
	//
	// try {
	// fileContainer.add(new FileSizeCollector(lSearchedFile.length(),
	// pFilelist.get(i),
	// lSearchedFile.getCanonicalPath().length()));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (lSearchedFile.isFile()) {
	// // fileContainer.incrementFileSize();
	// }
	// if (lSearchedFile.isDirectory()) {
	// // fileContainer.incrementDirectorySize();
	// }
	// }
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// Collections.sort(fileContainer);
	// return fileContainer;
	// }

	private int getComboBoxValueAsInteger(JComboBox<String> pComboBox) {
		try {
			return Integer.parseInt((String) pComboBox.getSelectedItem());
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}

	public int getCurrentProcessedProcent(int currentItemIndex) {
		double quota = (currentItemIndex / NUMBER_ENTRIES_PER_PORCENT);
		// System.out.println("quota ---" +quota);
		return (int) (quota / TEN) * ((int) TEN);
	}

	public DateFormat getDateFromat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
	}

	public Date getDateFromString(String pDateText, SimpleDateFormat pDateFormater) {
		Date date;
		try {
			date = pDateFormater.parse(pDateText);
			pDateFormater.format(date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<Integer, DublicateFiles> getDublicateFileList(String pLeftPath, String pRightPath) {
		HashMap<Integer, DublicateFiles> map = new HashMap<Integer, DublicateFiles>();

		ArrayList<File> foundFileAsDublicate = new ArrayList<File>();
		String leftPath = pLeftPath;
		String rightPath = pRightPath;

		ProcessTimeController.start();

		ArrayList<EasyFileInfo> rightFileArrayList;
		ArrayList<EasyFileInfo> leftFileArrayList = getFileArrayList(leftPath, true, true, true, false, 0, null);

		if (!rightPath.isEmpty()) {
			rightFileArrayList = getFileArrayList(rightPath, true, true, true, false, 0, null);
		} else {
			rightFileArrayList = leftFileArrayList;
		}

		setTotalFoundEntries(leftFileArrayList.size());
		setNumberEntriesPerProcent();

		EasyFileInfo leftFileCollector;
		EasyFileInfo rightFileCollector;

		File leftFile = new File("");
		File rightFile = new File("");

		DublicateFiles dublicateFiles;
		String lInfo;

		int lcl_counter = 0;

		for (int i = 0; i < leftFileArrayList.size(); i++) {

			int currentProcentValue = getCurrentProcessedProcent(i + 1);
			if (currentProcentValue > currentProcentValue && currentProcentValue % TEN == 0) {
				lInfo = "[" + currentProcentValue + "] % / " + "Process: " + (i + 1) + " / " + TOTAL_FOUND_ENTRIES
						+ " elapsed time " + ProcessTimeController.getElapsedTime() + " [s]";
				// progressBarValue = (int) currentProcentValue;
				System.out.println(lInfo);
			}

			// Runnable runme3 = new Runnable() {
			// public void run() {
			// progressBar.setValue(progressBarValue);
			// }
			// };
			// SwingUtilities.invokeLater(runme3);

			leftFileCollector = leftFileArrayList.get(i); // leftIterator.next();
			leftFile = leftFileCollector.getFile();

			if (foundFileAsDublicate.contains(leftFile)) {
				continue; // for actuall file we have a entry
			}

			for (int j = 0; j < rightFileArrayList.size(); j++) {

				rightFileCollector = rightFileArrayList.get(j); // rightIterator.next();
				rightFile = rightFileCollector.getFile();
				// rightFile = rightFileArrayList.get(j).getFile();

				if (rightFileCollector.getFilelength() < leftFileCollector.getFilelength()) {
					continue;
				}

				if (rightFileCollector.getFilelength() > leftFileCollector.getFilelength()) {
					break;
				}

				if (foundFileAsDublicate.contains(rightFile)) {
					continue; // for actuall file we have a entry
				}

				if (leftFileCollector.getFilelength() == rightFileCollector.getFilelength()) {

					try {
						if (leftFile.getCanonicalPath().equals(rightFile.getCanonicalPath())) {
							continue;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if (CompareBinaries.sameContentFiles(leftFile, rightFile)) {
						dublicateFiles = new DublicateFiles(leftFile, rightFile);
						map.put(lcl_counter++, dublicateFiles);
						foundFileAsDublicate.add(leftFile);
						foundFileAsDublicate.add(rightFile);
						break;
					}
				}
			}
		}
		return map;
	}

	public ArrayList<EasyFileInfo> getFileArrayList(String pPath, boolean pSortFileSize) {
		EasyFileWalker fw = new EasyFileWalker(pPath, cbxRecursiveMode.isSelected(), cbxIncludeFiles.isSelected(),
				cbxIncludeDirectories.isSelected(), getSearchLimit(), getFileSearchParameters());
		return fw.getFileList(pSortFileSize);
	}

	// private static String getNameSequence(int i, int s) {
	// String s1 = (new
	// StringBuilder()).append("%0").append(s).append("d").toString();
	// return String.format(s1, i);
	// }

	public ArrayList<EasyFileInfo> getFileArrayList(String pPath, boolean pSortFileSize, boolean pRecursiveMode,
			boolean pIncludeFiles, boolean pIncludeDirectories, int pSearchLimit,
			FileSearchParameters pParameterObject) {
		EasyFileWalker fw = new EasyFileWalker(pPath, pRecursiveMode, pIncludeFiles, pIncludeDirectories, pSearchLimit,
				pParameterObject);
		return fw.getFileList(pSortFileSize);
	}

	public String getFileFromFileChooser(String pParentPath, int pFileSelectionMode) {
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setFileSelectionMode(pFileSelectionMode);
		jfilechooser.setCurrentDirectory(new File(pParentPath));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(this);
		if (i == 0) {
			return jfilechooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

	public List<String> getFileListForDirectory(File pDirectory, boolean pRecursiveMode, boolean pIncludeFiles,
			boolean pIncludeDirectories, int pSearchLimit) throws IOException {
		if (pDirectory == null) {
			return null;
		}
		if (!pIncludeFiles && !pIncludeDirectories) {
			return null;
		}

		List<String> fileList = new ArrayList<String>();
		List<String> fileList_SubDirectories = new ArrayList<String>();

		// int sequence_counter = 1;
		if (pDirectory != null) {
			File files[] = pDirectory.listFiles();
			if (files != null /* || files.length > 0 */) {
				for (int i = 0; i < files.length; i++) {
					if (files != null) {
						if (isSearchLimitReached(pSearchLimit)) {
							break;
						}

						if (files[i].isFile()) {
							if (pIncludeFiles) {
								fileList.add(files[i].getCanonicalPath());
							}
						} else {
							if (pIncludeDirectories) {
								fileList.add(files[i].getCanonicalPath());
							}
							if (pRecursiveMode) {
								fileList_SubDirectories = getFileListForDirectory(files[i], pRecursiveMode,
										pIncludeFiles, pIncludeDirectories, pSearchLimit);
								fileList.addAll(fileList_SubDirectories);
							}
						}
					}
				}
			}
		}
		return fileList;
	}

	public List<String> getFileListForDirectory_2017(File pDirectory, boolean pRecursiveMode, boolean pIncludeFiles,
			boolean pIncludeDirectories, int pSearchLimit) throws IOException {
		if (pDirectory == null) {
			return null;
		}
		if (!pIncludeFiles && !pIncludeDirectories) {
			return null;
		}

		List<String> fileList = new ArrayList<String>();

		fileList = getFileLists(pDirectory.getCanonicalPath(), pRecursiveMode, pIncludeFiles, pIncludeDirectories,
				pSearchLimit);
		return fileList;
	}

	public List<String> getFileLists(String pPath, boolean pRecursiveMode, boolean pIncludeFiles,
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

	public int getFileNumberInDir(File pPath) {
		int fileSize = 0;
		File[] files = pPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileSize++;
			}
		}
		return fileSize;
	}

	public FileRenamer getFileRenamer() {
		if (filerenamer == null) {
			filerenamer = new FileRenamer();
		}
		return filerenamer;
	}

	public FileSearchParameters getFileSearchParameters() {

		Date date_from = null;
		if (!txtCreationDateFrom.getText().isEmpty()) {
			date_from = getDateFromString(txtCreationDateFrom.getText(), getsDateFormt());
		}

		Date date_to = null;

		if (!txtCreationDateTo.getText().isEmpty()) {
			date_to = getDateFromString(txtCreationDateTo.getText(), getsDateFormt());
		}

//		if (fileSearchParameters == null) {
//			fileSearchParameters = new FileSearchParameters(txtStartsWith.getText(), txtEndsWith.getText(),
//					txtContainStr.getText(), txtSearchPattern.getText(),
//					(int) getSearchedFileSize(txtFileSize.getText()), rbtFileSizeGreater.isSelected(),
//					rbtFileSizeLower.isSelected(), rbtFileSizeEqual.isSelected(), date_from, date_to,
//					cbxSuccessiveUppercaseChars.isSelected());
//		} else {
//			fileSearchParameters.initialize(txtStartsWith.getText(), txtEndsWith.getText(), txtContainStr.getText(),
//					txtSearchPattern.getText(), (int) getSearchedFileSize(txtFileSize.getText()),
//					rbtFileSizeGreater.isSelected(), rbtFileSizeLower.isSelected(), rbtFileSizeEqual.isSelected(),
//					date_from, date_to, cbxSuccessiveUppercaseChars.isSelected());
//		}

		return FileSearchParameters.getFileSearchParameters(txtStartsWith.getText(), txtEndsWith.getText(),
				txtContainStr.getText(), txtSearchPattern.getText(), (int) getSearchedFileSize(txtFileSize.getText()),
				rbtFileSizeGreater.isSelected(), rbtFileSizeLower.isSelected(), rbtFileSizeEqual.isSelected(),
				date_from, date_to, cbxSuccessiveUppercaseChars.isSelected());

		// return fileSearchParameters;
	}

	private String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	public int getFileSize(File pFile) {
		return (int) Math.ceil(pFile.length() / (float) 1024);
	}

	public int getFileSize(String pFile) {
		return getFileSize(new File(pFile));
	}

	private JTabbedPane getJTabbbedPaneOptionsTop() {
		// Start With
		jTabbedPaneOptionsTop = new JTabbedPane();
		// jTabbedPane1.setBounds(60, 120, 720, 200);

		// The following line enables to use scrolling tabs.
		// jTabbedPaneOptionsTop.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		jTabbedPaneOptionsTop.setBounds(20, 110, 760, 230);

		pnlSearchOptions = new JPanel();

		// pnlSearchOptions.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlSearchOptions.setLayout(new FlowLayout(FlowLayout.LEFT));
		// pnlSearchOptions.setBounds(60, 12, 400, 280);

		jTabbedPaneOptionsTop.addTab("Search Options", null, pnlSearchOptions, null);

		lblStartsWith = new JLabel("Starts With");
		// lblStartsWith.setBounds(10, 10, 80, 22);
		pnlSearchOptions.add(lblStartsWith);

		txtStartsWith = new JTextField("", 10);
		pnlSearchOptions.add(txtStartsWith);
		// txtStartsWith.setBounds(90, 10, 200, 22);
		txtStartsWith.addKeyListener(this);

		// Ends With
		lblEndsWith = new JLabel("Ends With");
		pnlSearchOptions.add(lblEndsWith);
		// lblEndsWith.setBounds(10, 31, 200, 22);

		txtEndsWith = new JTextField("", 10);
		pnlSearchOptions.add(txtEndsWith);
		// txtEndsWith.setBounds(90, 34, 200, 22);

		// Reset Search Values
		btnResetSearchOpt = new JButton("Reset Values");
		// btnResetSearchOpt.setBounds(380, 10, 120, 22);
		pnlSearchOptions.add(btnResetSearchOpt);
		btnResetSearchOpt.addActionListener(this);

		// File extentions
		cbxFileExtentions = getMyComboBox(fileExtentionList, "");
		// cbxFileExtentions.setBounds(380, 34, 120, 22);
		// cbxFileExtentions.setSelectedIndex(fileExtentionList[0]);
		pnlSearchOptions.add(cbxFileExtentions);

		// Contain String
		lblContainStr = new JLabel("Contain Str.");
		pnlSearchOptions.add(lblContainStr);
		// lblContainStr.setBounds(10, 58, 80, 22);

		txtContainStr = new JTextField("", 10);
		pnlSearchOptions.add(txtContainStr);
		// txtContainStr.setBounds(90, 58, 200, 22);

		// Search Include Files

		// Regular Expression
		lblRegExp = new JLabel("Regul. Expr.");
		lblRegExp.setBounds(10, 82, 80, 22);
		pnlSearchOptions.add(lblRegExp);

		txtSearchPattern = new JTextField("", 10);
		// txtSearchPattern.setBounds(90, 82, 200, 22);
		pnlSearchOptions.add(txtSearchPattern);

		// File size Panel
		JPanel pnlFileSize = new JPanel();
		FlowLayout pnlFileSizeLayout = new FlowLayout();
		pnlFileSizeLayout.setAlignment(FlowLayout.LEFT);
		pnlFileSize.setLayout(pnlFileSizeLayout);
		pnlFileSize.setBounds(80, 106, 400, 30);

		lblFileSize = new JLabel("File size [KB]");
		lblFileSize.setBounds(10, 106, 80, 22);
		pnlSearchOptions.add(lblFileSize);

		rbtAllSize = new JRadioButton("All", true);
		rbtFileSizeGreater = new JRadioButton(">> (GT)");
		rbtFileSizeLower = new JRadioButton("<< (LT)");
		rbtFileSizeEqual = new JRadioButton("= (EQ)");
		btnGrpFileSize = new ButtonGroup();

		btnGrpFileSize.add(rbtAllSize);
		btnGrpFileSize.add(rbtFileSizeGreater);
		btnGrpFileSize.add(rbtFileSizeLower);
		btnGrpFileSize.add(rbtFileSizeEqual);

		rbtAllSize.addItemListener(this);
		rbtFileSizeGreater.addItemListener(this);
		rbtFileSizeLower.addItemListener(this);
		rbtFileSizeEqual.addItemListener(this);

		pnlFileSize.add(rbtAllSize);
		pnlFileSize.add(rbtFileSizeGreater);
		pnlFileSize.add(rbtFileSizeLower);
		pnlFileSize.add(rbtFileSizeEqual);

		txtFileSize = new JTextField("", 10);
		// txtFileSize.setPreferredSize(DIMENSION_100_22);
		// txtFileSize.setPreferredSize(new java.awt.Dimension(100, 22));

		pnlFileSize.add(txtFileSize);

		JLabel lblKByte = new JLabel("KByte");
		pnlFileSize.add(lblKByte);

		pnlSearchOptions.add(pnlFileSize);

		// Date interval
		JLabel lblCreationFrom = new JLabel("Date From");
		lblCreationFrom.setBounds(10, 138, 60, 22);
		pnlSearchOptions.add(lblCreationFrom);

		txtCreationDateFrom = new ObservingTextField(getsDateFormt());
		txtCreationDateFrom.setBounds(90, 138, 100, 22);
		pnlSearchOptions.add(txtCreationDateFrom);

		ImageIcon icon = new ImageIcon(getClass().getResource("resources/JDateChooserIcon.gif"));
		btnCreationFrom = new JButton(icon);
		// btnCreationFrom = new JButton("Select..");
		btnCreationFrom.setBounds(200, 138, 50, 22);
		pnlSearchOptions.add(btnCreationFrom);
		btnCreationFrom.addActionListener(this);

		JLabel lblCreationTo = new JLabel("To");
		lblCreationTo.setBounds(250, 138, 30, 22);
		pnlSearchOptions.add(lblCreationTo);

		txtCreationDateTo = new ObservingTextField(getsDateFormt());
		txtCreationDateTo.setBounds(300, 138, 100, 22);
		pnlSearchOptions.add(txtCreationDateTo);

		// btnCreationTo = new JButton(icon);
		btnCreationTo = new JButton(icon);
		btnCreationTo.setBounds(410, 138, 50, 22);
		pnlSearchOptions.add(btnCreationTo);
		btnCreationTo.addActionListener(this);

		// --> SearchOpion Additionala
		//
		JPanel pnlSearchOptionAdditional = new JPanel();
		FlowLayout pnlSearchOptionAdditionalLayout = new FlowLayout();
		pnlSearchOptionAdditionalLayout.setAlignment(FlowLayout.LEFT);
		pnlSearchOptionAdditional.setLayout(pnlSearchOptionAdditionalLayout);
		pnlSearchOptionAdditional.setBounds(80, 160, 400, 30);

		cbxSuccessiveUppercaseChars = new JCheckBox("Successive Uppercase Chars");
		cbxSuccessiveUppercaseChars.setSelected(false);
		pnlSearchOptionAdditional.add(cbxSuccessiveUppercaseChars);

		pnlSearchOptions.add(pnlSearchOptionAdditional);

		// pnlSearchOptionAdditional.add(rbtSuccessiveUppercaseChars);
		// pnlSearchOptions.add(pnlSearchOptionAdditional);

		// <-- SearchOpion Additionala

		pnlRenameOptions = new JPanel();
		jTabbedPaneOptionsTop.addTab("Rename Options", null, pnlRenameOptions, null);
		pnlRenameOptions.setLayout(null);
		pnlRenameOptions.setRequestFocusEnabled(false);
		pnlRenameOptions.setPreferredSize(new java.awt.Dimension(660, 230));

		lblPrefix = new JLabel("Prefix");
		pnlRenameOptions.add(lblPrefix);
		lblPrefix.setBounds(10, 10, 60, 22);

		txtPrefix = new JTextField();
		pnlRenameOptions.add(txtPrefix);
		txtPrefix.setBounds(90, 10, 200, 22);

		btnResetRenameOpt = new JButton("Reset Values");
		pnlRenameOptions.add(btnResetRenameOpt);
		btnResetRenameOpt.setBounds(380, 10, 120, 22);
		btnResetRenameOpt.addActionListener(this);

		lblReplaceAll = new JLabel("Replace All");
		pnlRenameOptions.add(lblReplaceAll);
		lblReplaceAll.setBounds(10, 34, 80, 22);

		txtReplaceAll = new JTextField();
		pnlRenameOptions.add(txtReplaceAll);
		txtReplaceAll.setBounds(90, 34, 200, 22);

		lblReplace = new JLabel("Replace");
		pnlRenameOptions.add(lblReplace);
		lblReplace.setBounds(10, 58, 80, 22);

		txtReplaceOld = new JTextField();
		pnlRenameOptions.add(txtReplaceOld);
		txtReplaceOld.setBounds(90, 58, 200, 22);

		lblReplWith = new JLabel("With");
		pnlRenameOptions.add(lblReplWith);
		lblReplWith.setBounds(310, 58, 60, 22);

		txtReplaceNew = new JTextField();
		pnlRenameOptions.add(txtReplaceNew);
		txtReplaceNew.setBounds(380, 58, 200, 22);

		lblSuffix = new JLabel("Suffix");
		pnlRenameOptions.add(lblSuffix);
		lblSuffix.setBounds(10, 82, 60, 22);

		txtSuffix = new JTextField();
		pnlRenameOptions.add(txtSuffix);
		txtSuffix.setBounds(90, 82, 200, 22);

		// Replace Regexp
		lblRegexpReplaceOld = new JLabel("Regexp Repl.");
		pnlRenameOptions.add(lblRegexpReplaceOld);
		lblRegexpReplaceOld.setBounds(10, 106, 80, 22);

		txtRegexpReplaceOld = new JTextField();
		pnlRenameOptions.add(txtRegexpReplaceOld);
		txtRegexpReplaceOld.setBounds(90, 106, 200, 22);

		lblRegexpReplaceNew = new JLabel("With");
		pnlRenameOptions.add(lblRegexpReplaceNew);
		lblRegexpReplaceNew.setBounds(310, 106, 60, 22);

		txtRegexpReplaceNew = new JTextField();
		pnlRenameOptions.add(txtRegexpReplaceNew);
		txtRegexpReplaceNew.setBounds(380, 106, 200, 22);

		// Replace Regexp

		// Sequence
		lblSequence = new JLabel("Sequence");
		pnlRenameOptions.add(lblSequence);
		lblSequence.setBounds(10, 130, 60, 22);

		txtSequence = new JTextField();
		pnlRenameOptions.add(txtSequence);
		txtSequence.setBounds(90, 130, 80, 22);
		txtSequence.setEditable(false);
		// Sequence

		cbxLeadingZeros = getMyComboBox(sequences, "");
		cbxLeadingZeros.addActionListener(this);

		cbxLeadingZeros.setBounds(190, 130, 80, 22);

		pnlRenameOptions.add(cbxLeadingZeros);

		cbxResetCounter = new JCheckBox("Reset sequence each directory");
		cbxResetCounter.setSelected(true);
		pnlRenameOptions.add(cbxResetCounter);
		cbxResetCounter.setBounds(330, 130, 240, 22);

		pnlTransformOpt = new JPanel();
		FlowLayout pnlOptLayout = new FlowLayout();
		pnlOptLayout.setAlignment(FlowLayout.LEFT);
		pnlTransformOpt.setLayout(pnlOptLayout);
		pnlRenameOptions.add(pnlTransformOpt);

		pnlTransformOpt.setBounds(80, 150, 600, 30);
		rbtNormal = new JRadioButton("Normal", true);
		rbtLowerCase = new JRadioButton("To Lowercase");
		rbtUpperCase = new JRadioButton("To Uppercase");
		rbtCapitalize = new JRadioButton("To Capitalize");
		rbtParentAsFilename = new JRadioButton("Filename from parent dir");

		btnGrpTransform = new ButtonGroup();

		btnGrpTransform.add(rbtNormal);
		btnGrpTransform.add(rbtLowerCase);
		btnGrpTransform.add(rbtUpperCase);
		btnGrpTransform.add(rbtCapitalize);
		btnGrpTransform.add(rbtParentAsFilename);

		rbtNormal.addItemListener(this);
		rbtLowerCase.addItemListener(this);
		rbtUpperCase.addItemListener(this);
		rbtCapitalize.addItemListener(this);

		rbtParentAsFilename.addItemListener(this);

		pnlTransformOpt.add(rbtNormal);
		pnlTransformOpt.add(rbtLowerCase);
		pnlTransformOpt.add(rbtUpperCase);
		pnlTransformOpt.add(rbtCapitalize);
		pnlTransformOpt.add(rbtParentAsFilename);

		// >> New Tab for CompareOptions
		pnlCompareFileOptions = new JPanel();
		jTabbedPaneOptionsTop.addTab("Compare Options", null, pnlCompareFileOptions, null);
		pnlCompareFileOptions.setLayout(null);
		pnlCompareFileOptions.setRequestFocusEnabled(false);
		pnlCompareFileOptions.setPreferredSize(new java.awt.Dimension(660, 230));

		lblLeftOffset = new JLabel("Left offset");
		pnlCompareFileOptions.add(lblLeftOffset);
		lblLeftOffset.setBounds(10, 10, 100, 22);

		txtLeftOffset = new JTextField("1");
		pnlCompareFileOptions.add(txtLeftOffset);
		txtLeftOffset.setBounds(90, 10, 100, 22);
		// txtLeftStart.addKeyListener(this);
		addMyKeyListener(txtLeftOffset);

		cbxLefOffset = getMyComboBox(this.compareLimitList, "1");
		cbxLefOffset.setBounds(220, 10, 60, 22);
		pnlCompareFileOptions.add(cbxLefOffset);

		lblItemsLeft = new JLabel("Items left");
		pnlCompareFileOptions.add(lblItemsLeft);
		lblItemsLeft.setBounds(300, 10, 80, 22);

		txtItemsLeft = new JTextField("99999");
		pnlCompareFileOptions.add(txtItemsLeft);
		txtItemsLeft.setBounds(400, 10, 100, 22);
		addMyKeyListener(txtItemsLeft);

		cbxItemsLeft = getMyComboBox(this.compareLimitList, "1");
		cbxItemsLeft.setBounds(520, 10, 60, 22);
		pnlCompareFileOptions.add(cbxItemsLeft);
		// Rights >>
		lblRightStart = new JLabel("Right offset");
		pnlCompareFileOptions.add(lblRightStart);
		lblRightStart.setBounds(10, 40, 100, 22);

		txtRightOffset = new JTextField("1");
		pnlCompareFileOptions.add(txtRightOffset);
		txtRightOffset.setBounds(90, 40, 100, 22);
		// txtRightStart.addKeyListener(this);
		addMyKeyListener(txtRightOffset);

		cbxRightOffset = getMyComboBox(this.compareLimitList, "1");
		cbxRightOffset.setBounds(220, 40, 60, 22);
		pnlCompareFileOptions.add(cbxRightOffset);

		lblItemsRight = new JLabel("Items right");
		pnlCompareFileOptions.add(lblItemsRight);
		lblItemsRight.setBounds(300, 40, 80, 22);

		txtItemsRight = new JTextField("99999");
		pnlCompareFileOptions.add(txtItemsRight);
		txtItemsRight.setBounds(400, 40, 100, 22);
		addMyKeyListener(txtItemsRight);

		cbxItemsRight = getMyComboBox(this.compareLimitList, "1");
		cbxItemsRight.setBounds(520, 40, 60, 22);
		pnlCompareFileOptions.add(cbxItemsRight);
		// Rights <<

		// blocksize

		lblBufferSize = new JLabel("Buffer size");
		pnlCompareFileOptions.add(lblBufferSize);
		lblBufferSize.setBounds(10, 70, 100, 22);

		txtBufferSize = new JTextField(bufferSizeList[4]);
		pnlCompareFileOptions.add(txtBufferSize);
		txtBufferSize.setBounds(90, 70, 100, 22);
		// txtRightStart.addKeyListener(this);
		addMyKeyListener(txtBufferSize);

		cbxBufferSize = getMyComboBox(this.bufferSizeList, "1");
		cbxBufferSize.setBounds(220, 70, 60, 22);
		cbxBufferSize.setSelectedIndex(4);
		pnlCompareFileOptions.add(cbxBufferSize);

		lblBufferSizeFactor = new JLabel("Buffer factor");
		pnlCompareFileOptions.add(lblBufferSizeFactor);
		lblBufferSizeFactor.setBounds(300, 70, 100, 22);

		txtBufferSizeFactor = new JTextField(bufferSizeFactorList[5]);
		pnlCompareFileOptions.add(txtBufferSizeFactor);
		txtBufferSizeFactor.setBounds(400, 70, 100, 22);
		addMyKeyListener(txtBufferSizeFactor);

		cbxBufferSizeFactor = getMyComboBox(this.bufferSizeFactorList, "1");
		cbxBufferSizeFactor.setBounds(520, 70, 60, 22);
		pnlCompareFileOptions.add(cbxBufferSizeFactor);

		return jTabbedPaneOptionsTop;
	}

	public int getLeftItems() {
		return getTextFieldValueAsInteger(txtItemsLeft);
	}

	// public boolean isSearchOptionFilled() {
	// if (!txtStartsWith.getText().isEmpty()
	// || !txtEndsWith.getText().isEmpty()
	// || !txtContainStr.getText().isEmpty()
	// || !txtSearchPattern.getText().isEmpty()
	// || (!txtFileSize.getText().isEmpty() && !rbtAllSize
	// .isSelected())
	// || (!txtCreationDateFrom.getText().isEmpty() || !txtCreationDateTo
	// .getText().isEmpty())) {
	// return true;
	// }
	// return false;
	// }

	public int getLeftOffset() {
		return getTextFieldValueAsInteger(txtLeftOffset);
		// return leftOffset;
	}

	public long getLengthOfAllFiles(List<String> pList) {
		long totalLength = 0;
		for (int i = 0; i < pList.size(); i++) {
			totalLength += new File(pList.get(i)).length();
		}
		return totalLength;
	}

	private String getLogDir(String pSubDir) {
		return System.getProperty("user.dir") + System.getProperty("file.separator") + pSubDir
				+ new java.text.SimpleDateFormat("yyyy_MM_dd").format(new Date()).toString();
	}

	public String getLogFilename(String pSubDir, String pFileExt) {
		File log_dir = new File(getLogDir(pSubDir));
		if (!log_dir.exists()) {
			log_dir.mkdir();
		}
		return log_dir.getAbsolutePath() + System.getProperty("file.separator")
				+ new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new java.util.Date()).toString()
				+ "." + pFileExt;
	}

	public String getModificationDate(File pFile, String pDateFormat) {
		return new SimpleDateFormat(pDateFormat).format(new Date(pFile.lastModified()));
	}

	private int getMoreLessItems() {
		// TODO Auto-generated method stub
		String s = "Search items";
		String input = (String) JOptionPane.showInputDialog(this, "Give item size (with > = <):", s,
				JOptionPane.QUESTION_MESSAGE, null, null, "20");

		// Object[] possibleValues = { "First", "Second", "Third" };
		//
		// Object selectedValue = JOptionPane.showInputDialog(null,
		// "Choose one", "Input",
		// JOptionPane.INFORMATION_MESSAGE, null,
		// possibleValues, possibleValues[0]);

		if (input != null && input.length() > 0) {
			return Integer.parseInt(input);
		}
		return 0;
	}

	private JComboBox<String> getMyComboBox(String[] pListValues, Object pSelectedItem) {
		ComboBoxModel<String> myModel = new DefaultComboBoxModel<String>(pListValues);
		JComboBox<String> myComboBox = new JComboBox<>(pListValues);
		myComboBox.setModel(myModel);
		myComboBox.setSelectedItem(pSelectedItem);
		myComboBox.addItemListener(this);
		return myComboBox;
	}

	public JMenuBar getMyMenuBar() {
		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("Shwo directories more/less items");

		menuBar.add(menu);

		// a group of JMenuItems
		menuItemMoreLessItems = new JMenuItem("Shwo directories more/less items", KeyEvent.VK_T);
		menuItemMoreLessItems.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItemMoreLessItems.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menuItemMoreLessItems.addActionListener(this);

		menu.add(menuItemMoreLessItems);

		menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menu.add(menuItem);

		// a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		// a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		// a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		submenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		submenu.add(menuItem);
		menu.add(submenu);

		// Build second menu in the menu bar.
		menu = new JMenu("Tools");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
		menuBar.add(menu);

		JMenuItem menuImportToTable = new JMenu("Import to Table");
		// menuItemImportToTable.setAccelerator(KeyStroke.getKeyStroke(
		// KeyEvent.VK_1, ActionEvent.ALT_MASK));
		// menuItemImportToTable.getAccessibleContext().setAccessibleDescription(
		// "This doesn't really do anything");
		menuImportToTable.addActionListener(this);

		menu.add(menuImportToTable);
		menuItemImportToCompareTbl = new JMenuItem("Import to Comare table");
		menuItemImportToCompareTbl.addActionListener(this);
		menuImportToTable.add(menuItemImportToCompareTbl);
		return menuBar;

	}

	public String getNewFilenameFromParent(String pParent, String lNewFile, String pOldFilename) {
		// C:\Temp\subdir1\subdir2\myfile3.txt -> filename =C subdir2
		int ind = pParent.lastIndexOf(getFileSeparator());
		if (ind == -1) {
			return lNewFile;
		}
		String lParentName = pParent.substring(ind + 1);
		return replaceFirst(lNewFile, pOldFilename, lParentName);
	}

	public String getNewFilenameFromRegExpReplacement(String pSearchedRegExpress, String pReplaceRegExpress,
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

	private JPanel getPanelCompareDir() {
		// begin: compare path
		pnlCompareDir = new JPanel();
		FlowLayout pnlCompareDirLayout = new FlowLayout();
		pnlCompareDirLayout.setAlignment(FlowLayout.LEFT);
		pnlCompareDir.setLayout(pnlCompareDirLayout);

		pnlCompareDir.setBounds(20, 40, 760, 30);

		String lComparePath = "";
		if (defaultCompareDir != null && !defaultCompareDir.isEmpty()) {
			lComparePath = defaultCompareDir;
		}

		lblCompareDir = new JLabel("Compare Path:");
		// lblCompareDir.setPreferredSize(DIMENSION_100_22);
		lblCompareDir.setPreferredSize(new java.awt.Dimension(100, 22));

		pnlCompareDir.add(lblCompareDir);

		txtCompareDir = new JTextField(lComparePath);
		// txtCompareDir.setPreferredSize(DIMENSION_500_22);
		txtCompareDir.setPreferredSize(new java.awt.Dimension(500, 22));

		pnlCompareDir.add(txtCompareDir);

		btnChooseCompareDir = new JButton("Open Compare Dir");
		// btnChooseCompareDir.setPreferredSize(DIMENSION_140_22);
		btnChooseCompareDir.setPreferredSize(new java.awt.Dimension(120, 22));

		pnlCompareDir.add(btnChooseCompareDir);
		btnChooseCompareDir.addActionListener(this);
		return pnlCompareDir;
		// end: compare path
	}

	private JPanel getPanelNoPreview() {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(1, 1)); // 3 rows, 1 column
		JLabel label1 = new JLabel("No Preview", SwingConstants.CENTER);
		label1.setVerticalTextPosition(SwingConstants.BOTTOM);
		label1.setHorizontalTextPosition(SwingConstants.CENTER);
		label1.setFont(new Font("Arial", Font.BOLD, 14));
		myPanel.add(label1);
		myPanel.setBackground(Color.gray);
		label1.setForeground(Color.white);
		myPanel.setPreferredSize(dimensionPreview);
		myPanel.setVisible(true);
		return myPanel;
	}

	private JPanel getPanelOptions() {
		// begin: panel options
		pnlOptions = new JPanel();
		FlowLayout pnlOptionsLayout = new FlowLayout();
		pnlOptionsLayout.setAlignment(FlowLayout.LEFT);
		pnlOptions.setLayout(pnlOptionsLayout);
		pnlOptions.setBounds(100, 80, 730, 30);
		getContentPane().add(pnlOptions);

		cbxIncludeFiles = new JCheckBox("Include Files");
		cbxIncludeFiles.setSelected(true);
		pnlOptions.add(cbxIncludeFiles);

		cbxIncludeDirectories = new JCheckBox("Include Directories");
		// cbxIncludeDirectories.setSelected(true);
		pnlOptions.add(cbxIncludeDirectories);

		cbxLoggingInFile = new JCheckBox("Logging in file");
		cbxLoggingInFile.setSelected(false);
		pnlOptions.add(cbxLoggingInFile);

		cbxLoggingCommandLine = new JCheckBox("Commandline logging");
		cbxLoggingCommandLine.setSelected(false);
		pnlOptions.add(cbxLoggingCommandLine);

		cbxRecursiveMode = new JCheckBox("Include Subfolders");
		cbxRecursiveMode.setSelected(true);
		pnlOptions.add(cbxRecursiveMode);

		cbxSearchLimits = getMyComboBox(searchLimitList, "");

		pnlOptions.add(cbxSearchLimits);

		return pnlOptions;

		// end: panel options
	}

	@SuppressWarnings("unused")
	private JPanel getPanelSearchArea() {
		// search area
		pnlDirectory = new JPanel();
		FlowLayout pnlDirectoryLayout = new FlowLayout(FlowLayout.LEFT);
		// pnlDirectoryLayout.setAlignment(FlowLayout.LEFT);
		pnlDirectory.setLayout(pnlDirectoryLayout);
		pnlDirectory.setBounds(20, 10, 760, 30);

		lblSourceDir = new JLabel("Search Path:");
		// lblSourceDir.setPreferredSize(DIMENSION_100_22);
		lblSourceDir.setPreferredSize(new java.awt.Dimension(100, 22));

		pnlDirectory.add(lblSourceDir);

		txtSearchDir = new JTextField(defaultSearchDir);
		// txtSearchDir.setPreferredSize(DIMENSION_500_22);
		txtSearchDir.setPreferredSize(new Dimension(500, 22));
		txtSearchDir.addFocusListener(this);
		pnlDirectory.add(txtSearchDir);

		btnChooseDir = new JButton("Open directory");
		// btnChooseDir.setPreferredSize(DIMENSION_140_22);
		btnChooseDir.setPreferredSize(new Dimension(120, 22));
		pnlDirectory.add(btnChooseDir);
		btnChooseDir.addActionListener(this);
		return pnlDirectory;
	}

	private JPanel getPanelTextFilePreview(String pEditFile) {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(1, 1)); // 3 rows, 1 column
		// JLabel label1 = new JLabel("No Preview", SwingConstants.CENTER);
		// label1.setVerticalTextPosition(SwingConstants.BOTTOM);
		// label1.setHorizontalTextPosition(SwingConstants.CENTER);
		// label1.setFont(new Font("Arial", Font.BOLD, 14));
		SimpleFileEditor editorPanel = new SimpleFileEditor(pEditFile);
		myPanel.add(editorPanel);
		myPanel.setBackground(Color.gray);
		// label1.setForeground(Color.white);
		myPanel.setPreferredSize(dimensionPreview);
		myPanel.setVisible(true);
		return myPanel;
	}

	public String getReleaseInfo() {
		StringBuilder stringBuilder = new StringBuilder(100);
		stringBuilder.append(FILEMANAGER_LABEL);
		stringBuilder.append(" ");
		stringBuilder.append(FILEMANAGER_RELEASE);
		stringBuilder.append(HYPHEN);
		stringBuilder.append(FILEMANAGER_RELEASE_DATE);
		stringBuilder.append(" ");
		stringBuilder.append(FILEMANAGER_COPYRIGHT);
		return stringBuilder.toString();

//List<String> alphabets = Arrays.asList(
//		FILEMANAGER_LABEL, 
//		FILEMANAGER_RELEASE,
//		FILEMANAGER_RELEASE_DATE,
//		FILEMANAGER_COPYRIGHT);
//
//		return String.join(HYPHEN, alphabets);
	}

	public int getRightItems() {
		return getTextFieldValueAsInteger(txtItemsRight);
	}

	public int getRightOffset() {
		return getTextFieldValueAsInteger(txtRightOffset);
		// return rightOffset;
	}

	public SimpleDateFormat getsDateFormt() {
		return sDateFormt;
	}

	public List<String> getSearchedFileListNew(File pDirectory, boolean pRecursiveMode, boolean pIncludeFiles,
			boolean pIncludeDirectories, int pSearchLimit, boolean pResetCounterValue, boolean pIsSearchOptionFilled,
			String pStartsWith, String pEndsWith, String pContainStr, String pSearchPattern, String pFileSize,
			boolean pAllFileSize, boolean pFileSizeGreater, boolean pFileSizeLower, boolean pFileSizeEqual,
			String pCreationDateFrom, String pCreationDateTo, boolean pSuccessiveUppercaseChars) throws IOException {
		if (pDirectory == null) {
			return null;
		}
		if (!pIncludeFiles && !pIncludeDirectories) {
			return null;
		}

		List<String> fileList = new ArrayList<String>();
		List<String> fileList_SubDirectories = new ArrayList<String>();

		// int sequence_counter = 1;
		if (pDirectory != null) {
			File files[] = pDirectory.listFiles();
			if (files != null /* || files.length > 0 */) {
				for (int i = 0; i < files.length; i++) {
					if (files != null) {
						if (isSearchLimitReached(pSearchLimit)) {
							break;
						}

						if (files[i].isFile()) {
							if (pIncludeFiles) {
								try {
									// if (matchSearchOptions(files[i])) {
									if (matchFileSearchOptionsNew(files[i], pStartsWith, pEndsWith, pContainStr,
											pSearchPattern, pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower,
											pFileSizeEqual, pCreationDateFrom, pCreationDateTo,
											pSuccessiveUppercaseChars)) {
										fileList.add(files[i].getCanonicalPath());
										// addRenameDataToTable(tableModelSearch,
										// files[i], sequence_counter);
										// sequence_counter++;
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							if (pIncludeDirectories) {
								try {
									if (matchFileSearchOptionsNew(files[i], pStartsWith, pEndsWith, pContainStr,
											pSearchPattern, pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower,
											pFileSizeEqual, pCreationDateFrom, pCreationDateTo,
											pSuccessiveUppercaseChars)) {
										fileList.add(files[i].getCanonicalPath());
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (pRecursiveMode) {
								fileList_SubDirectories = getSearchedFileListNew(files[i], pRecursiveMode,
										pIncludeFiles, pIncludeDirectories, pSearchLimit, pResetCounterValue,
										pIsSearchOptionFilled, pStartsWith, pEndsWith, pContainStr, pSearchPattern,
										pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower, pFileSizeEqual,
										pCreationDateFrom, pCreationDateTo, pSuccessiveUppercaseChars);
								fileList.addAll(fileList_SubDirectories);
							}
						}
					}
				}
			}
		}
		return fileList;
	}

	public float getSearchedFileSize(String pFileSize) {
		if (pFileSize.isEmpty()) {
			return 0;
		}
		return Float.parseFloat(pFileSize.toString());
	}

	public int getSearchLimit() {
		if ((String) cbxSearchLimits.getSelectedItem() != "All") {
			return getComboBoxValueAsInteger(cbxSearchLimits);
		}
		return Integer.MAX_VALUE;
	}

	private String getSelectedFilename() {
		String filename = "";
		JTable tablename = null;
		int selectedTabIndex = 0;
		if (jTabbedPanelForTables != null && jTabbedPanelForTables.getSelectedIndex() >= 0) {
			jTabbedPanelForTables.getSelectedIndex();
		}

		if (selectedTabIndex == 0) {
			tablename = tableSearchResult;
		}

		if (selectedTabIndex == 1) {
			tablename = tableCompareResult;
		}

		if (tablename != null && tablename.getRowCount() > 0) {
			filename = (String) tablename.getValueAt(tablename.getSelectedRow(), 1);
		}
		return filename;
	}

	private int getTextFieldValueAsInteger(JTextField pTextField) {
		try {
			return Integer.parseInt(pTextField.getText());
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

	public void importToCompareTbl() {
		// TODO Auto-generated method stub
		importToJTable(this.tableCompareResult);
		jTabbedPanelForTables.setSelectedIndex(1);
		repaint();
	}

	public void importToJTable(JTable pTablename) {
		String lFilename = getFileFromFileChooser(txtSearchDir.getText(), JFileChooser.FILES_ONLY);
		DefaultTableModel model = (DuplicateTableModel) pTablename.getModel();
		pTablename.removeAll();
		try {
			// FileReader fr = new FileReader(lFilename);

			FileInputStream fis = new FileInputStream(lFilename);
			InputStreamReader fr = new InputStreamReader(fis, "UTF8");

			BufferedReader br = new BufferedReader(fr);
			String zeile;
			// String[] teile;
			// int columnsize = model.getColumnCount();
			String[] teile;// = new String[columnsize-1];
			zeile = br.readLine(); // dont need first Line
			// int columnsize = pTablename.getModel().getColumnCount();
			// Object[] table_Row_Data = new Object[columnsize];
			while ((zeile = br.readLine()) != null) {
				teile = zeile.split(";");
				model.addRow(new Object[] { Integer.parseInt(teile[0]), teile[1], teile[2], teile[3],
						Integer.parseInt(teile[4]), teile[5], teile[6], teile[7], Integer.parseInt(teile[8]),
						Boolean.parseBoolean(teile[9]), teile[10] });
			}

			fr.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehlermeldung",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Create the frame.
	 */
	private void initGUI() {
		try {

			// Toolkit.getDefaultToolkit().getSystemEventQueue().push(new
			// MyEventQueue());

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			determinePreferedSizes();
			loadPropertiesFile(PROPS_FILE);
			this.setPreferredSize(new java.awt.Dimension(WIDTH_FRM, HEIGHT_FRM));
			if (OSValidator.isWindows()) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}

			SwingUtilities.updateComponentTreeUI(this);
			this.setTitle(this.getReleaseInfo());

//			pnlDirectory = new JPanel();
//			FlowLayout pnlDirectoryLayout = new FlowLayout();
//			pnlDirectoryLayout.setAlignment(FlowLayout.LEFT);
//			getPanelDirectory().setLayout(pnlDirectoryLayout);
//			getPanelDirectory().setBounds(20, 10, 760, 30);
//
//			lblSourceDir = new JLabel("Search Path:");
//			lblSourceDir.setPreferredSize(DIMENSION_100_22);
//			getPanelDirectory().add(lblSourceDir);
//
//			txtSearchDir = new JTextField(defaultSearchDir);
//			txtSearchDir.setPreferredSize(DIMENSION_500_22);
//			txtSearchDir.addFocusListener(this);
//			getPanelDirectory().add(txtSearchDir);
//
//			btnChooseDir = new JButton("Open directory");
//			btnChooseDir.setPreferredSize(DIMENSION_140_22);
//			getPanelDirectory().add(btnChooseDir);
//			btnChooseDir.addActionListener(this);

			getContentPane().add(getPanelDirectory());

			// begin: compare path
			// pnlCompareDir
//			pnlCompareDir = new JPanel();
//			FlowLayout pnlCompareDirLayout = new FlowLayout();
//			pnlCompareDirLayout.setAlignment(FlowLayout.LEFT);
//			pnlCompareDir.setLayout(pnlCompareDirLayout);
//			pnlCompareDir.setBounds(20, 40, 760, 30);
//			String lComparePath = "";
//			if (defaultCompareDir != null && !defaultCompareDir.isEmpty()) {
//				lComparePath = defaultCompareDir;
//			}
//			lblCompareDir = new JLabel("Compare Path:");
//			lblCompareDir.setPreferredSize(DIMENSION_100_22);
//			pnlCompareDir.add(lblCompareDir);
//			txtCompareDir = new JTextField(lComparePath);
//			txtCompareDir.setPreferredSize(DIMENSION_500_22);
//			pnlCompareDir.add(txtCompareDir);
//			btnChooseCompareDir = new JButton("Open Compare Dir");
//			btnChooseCompareDir.setPreferredSize(DIMENSION_140_22);
//			pnlCompareDir.add(btnChooseCompareDir);
//			btnChooseCompareDir.addActionListener(this);
			getContentPane().add(getPanelCompareDir());
			
			// pnlCompareDir

			getContentPane().add(getPanelOptions());
			// begin: panel options
//			pnlOptions = new JPanel();
//			FlowLayout pnlOptionsLayout = new FlowLayout();
//			pnlOptionsLayout.setAlignment(FlowLayout.LEFT);
//			pnlOptions.setLayout(pnlOptionsLayout);
//			pnlOptions.setBounds(100, 80, 730, 30);
//			getContentPane().add(pnlOptions);
//			cbxIncludeFiles = new JCheckBox("Include Files");
//			cbxIncludeFiles.setSelected(true);
//			pnlOptions.add(cbxIncludeFiles);
//			cbxIncludeDirectories = new JCheckBox("Include Directories");
//			// cbxIncludeDirectories.setSelected(true);
//			pnlOptions.add(cbxIncludeDirectories);
//			cbxLoggingInFile = new JCheckBox("Logging in file");
//			cbxLoggingInFile.setSelected(false);
//			pnlOptions.add(cbxLoggingInFile);
//			cbxLoggingCommandLine = new JCheckBox("Commandline logging");
//			cbxLoggingCommandLine.setSelected(false);
//			pnlOptions.add(cbxLoggingCommandLine);
//			cbxRecursiveMode = new JCheckBox("Include Subfolders");
//			cbxRecursiveMode.setSelected(true);
//			pnlOptions.add(cbxRecursiveMode);
//			cbxSearchLimits = getMyComboBox(searchLimitList, "");
//			pnlOptions.add(cbxSearchLimits);
			// end: panel options
			
			// Start With

			
			pnlSearchOptions = new JPanel();
			pnlSearchOptions.setLayout(null);
			pnlSearchOptions.setBounds(60, 12, 400, 280);
			

			
			lblStartsWith = new JLabel("Starts With");
			lblStartsWith.setBounds(10, 10, 80, 22);
			pnlSearchOptions.add(lblStartsWith);
			txtStartsWith = new JTextField();
			pnlSearchOptions.add(txtStartsWith);
			txtStartsWith.setBounds(90, 10, 200, 22);
			txtStartsWith.addKeyListener(this);
			// Reset Search Values
			btnResetSearchOpt = new JButton("Reset Values");
			btnResetSearchOpt.setBounds(380, 10, 120, 22);
			pnlSearchOptions.add(btnResetSearchOpt);
			btnResetSearchOpt.addActionListener(this);
			// Ends With
			lblEndsWith = new JLabel("Ends With");
			pnlSearchOptions.add(lblEndsWith);
			lblEndsWith.setBounds(10, 31, 200, 22);
			txtEndsWith = new JTextField();
			pnlSearchOptions.add(txtEndsWith);
			txtEndsWith.setBounds(90, 34, 200, 22);
			// File extentions
			cbxFileExtentions = getMyComboBox(fileExtentionList, "");
			cbxFileExtentions.setBounds(380, 34, 120, 22);
			// cbxFileExtentions.setSelectedIndex(fileExtentionList[0]);
			pnlSearchOptions.add(cbxFileExtentions);
			// Contain String
			lblContainStr = new JLabel("Contain Str.");
			pnlSearchOptions.add(lblContainStr);
			lblContainStr.setBounds(10, 58, 80, 22);
			txtContainStr = new JTextField();
			pnlSearchOptions.add(txtContainStr);
			txtContainStr.setBounds(90, 58, 200, 22);
			// Search Include Files
			// Regular Expression
			lblRegExp = new JLabel("Regul. Expr.");
			lblRegExp.setBounds(10, 82, 80, 22);
			pnlSearchOptions.add(lblRegExp);
			txtSearchPattern = new JTextField();
			txtSearchPattern.setBounds(90, 82, 200, 22);
			pnlSearchOptions.add(txtSearchPattern);
			// File size Panel
			JPanel pnlFileSize = new JPanel();
			FlowLayout pnlFileSizeLayout = new FlowLayout();
			pnlFileSizeLayout.setAlignment(FlowLayout.LEFT);
			pnlFileSize.setLayout(pnlFileSizeLayout);
			pnlFileSize.setBounds(80, 106, 400, 30);
			lblFileSize = new JLabel("File size [KB]");
			lblFileSize.setBounds(10, 106, 80, 22);
			pnlSearchOptions.add(lblFileSize);
			rbtAllSize = new JRadioButton("All", true);
			rbtFileSizeGreater = new JRadioButton(">> (GT)");
			rbtFileSizeLower = new JRadioButton("<< (LT)");
			rbtFileSizeEqual = new JRadioButton("= (EQ)");
			btnGrpFileSize = new ButtonGroup();
			btnGrpFileSize.add(rbtAllSize);
			btnGrpFileSize.add(rbtFileSizeGreater);
			btnGrpFileSize.add(rbtFileSizeLower);
			btnGrpFileSize.add(rbtFileSizeEqual);
			rbtAllSize.addItemListener(this);
			rbtFileSizeGreater.addItemListener(this);
			rbtFileSizeLower.addItemListener(this);
			rbtFileSizeEqual.addItemListener(this);
			pnlFileSize.add(rbtAllSize);
			pnlFileSize.add(rbtFileSizeGreater);
			pnlFileSize.add(rbtFileSizeLower);
			pnlFileSize.add(rbtFileSizeEqual);
			txtFileSize = new JTextField();
			txtFileSize.setPreferredSize(DIMENSION_100_22);
			pnlFileSize.add(txtFileSize);
			JLabel lblKByte = new JLabel("KByte");
			pnlFileSize.add(lblKByte);
			pnlSearchOptions.add(pnlFileSize);
			// Date interval
			JLabel lblCreationFrom = new JLabel("Date From");
			lblCreationFrom.setBounds(10, 138, 60, 22);
			pnlSearchOptions.add(lblCreationFrom);
			txtCreationDateFrom = new ObservingTextField(getsDateFormt());
			txtCreationDateFrom.setBounds(90, 138, 100, 22);
			pnlSearchOptions.add(txtCreationDateFrom);
			ImageIcon icon = new ImageIcon(getClass().getResource("resources/JDateChooserIcon.gif"));
			btnCreationFrom = new JButton(icon);
			// btnCreationFrom = new JButton("Select..");
			btnCreationFrom.setBounds(200, 138, 50, 22);
			pnlSearchOptions.add(btnCreationFrom);
			btnCreationFrom.addActionListener(this);
			JLabel lblCreationTo = new JLabel("To");
			lblCreationTo.setBounds(250, 138, 30, 22);
			pnlSearchOptions.add(lblCreationTo);
			txtCreationDateTo = new ObservingTextField(getsDateFormt());
			txtCreationDateTo.setBounds(300, 138, 100, 22);
			pnlSearchOptions.add(txtCreationDateTo);
			// btnCreationTo = new JButton(icon);
			btnCreationTo = new JButton(icon);
			btnCreationTo.setBounds(410, 138, 50, 22);
			pnlSearchOptions.add(btnCreationTo);
			btnCreationTo.addActionListener(this);

			// --> SearchOpion Additionala
			//
			JPanel pnlSearchOptionAdditional = new JPanel();
			FlowLayout pnlSearchOptionAdditionalLayout = new FlowLayout();
			pnlSearchOptionAdditionalLayout.setAlignment(FlowLayout.LEFT);
			pnlSearchOptionAdditional.setLayout(pnlSearchOptionAdditionalLayout);
			pnlSearchOptionAdditional.setBounds(80, 160, 400, 30);
			cbxSuccessiveUppercaseChars = new JCheckBox("Successive Uppercase Chars");
			cbxSuccessiveUppercaseChars.setSelected(false);
			pnlSearchOptionAdditional.add(cbxSuccessiveUppercaseChars);
			pnlSearchOptions.add(pnlSearchOptionAdditional);
			// pnlSearchOptionAdditional.add(rbtSuccessiveUppercaseChars);
			// pnlSearchOptions.add(pnlSearchOptionAdditional);
			// <-- SearchOpion Additionala
			pnlRenameOptions = new JPanel();

			pnlRenameOptions.setLayout(null);
			pnlRenameOptions.setRequestFocusEnabled(false);
			pnlRenameOptions.setPreferredSize(new java.awt.Dimension(660, 230));
			lblPrefix = new JLabel("Prefix");
			pnlRenameOptions.add(lblPrefix);
			lblPrefix.setBounds(10, 10, 60, 22);
			txtPrefix = new JTextField();
			pnlRenameOptions.add(txtPrefix);
			txtPrefix.setBounds(90, 10, 200, 22);
			btnResetRenameOpt = new JButton("Reset Values");
			pnlRenameOptions.add(btnResetRenameOpt);
			btnResetRenameOpt.setBounds(380, 10, 120, 22);
			btnResetRenameOpt.addActionListener(this);
			lblReplaceAll = new JLabel("Replace All");
			pnlRenameOptions.add(lblReplaceAll);
			lblReplaceAll.setBounds(10, 34, 80, 22);
			txtReplaceAll = new JTextField();
			pnlRenameOptions.add(txtReplaceAll);
			txtReplaceAll.setBounds(90, 34, 200, 22);
			lblReplace = new JLabel("Replace");
			pnlRenameOptions.add(lblReplace);
			lblReplace.setBounds(10, 58, 80, 22);
			txtReplaceOld = new JTextField();
			pnlRenameOptions.add(txtReplaceOld);
			txtReplaceOld.setBounds(90, 58, 200, 22);
			lblReplWith = new JLabel("With");
			pnlRenameOptions.add(lblReplWith);
			lblReplWith.setBounds(310, 58, 60, 22);
			txtReplaceNew = new JTextField();
			pnlRenameOptions.add(txtReplaceNew);
			txtReplaceNew.setBounds(380, 58, 200, 22);
			lblSuffix = new JLabel("Suffix");
			pnlRenameOptions.add(lblSuffix);
			lblSuffix.setBounds(10, 82, 60, 22);
			txtSuffix = new JTextField();
			pnlRenameOptions.add(txtSuffix);
			txtSuffix.setBounds(90, 82, 200, 22);
			// Replace Regexp
			lblRegexpReplaceOld = new JLabel("Regexp Repl.");
			pnlRenameOptions.add(lblRegexpReplaceOld);
			lblRegexpReplaceOld.setBounds(10, 106, 80, 22);
			txtRegexpReplaceOld = new JTextField();
			pnlRenameOptions.add(txtRegexpReplaceOld);
			txtRegexpReplaceOld.setBounds(90, 106, 200, 22);
			lblRegexpReplaceNew = new JLabel("With");
			pnlRenameOptions.add(lblRegexpReplaceNew);
			lblRegexpReplaceNew.setBounds(310, 106, 60, 22);
			txtRegexpReplaceNew = new JTextField();
			pnlRenameOptions.add(txtRegexpReplaceNew);
			txtRegexpReplaceNew.setBounds(380, 106, 200, 22);
			// Replace Regexp
			// Sequence
			lblSequence = new JLabel("Sequence");
			pnlRenameOptions.add(lblSequence);
			lblSequence.setBounds(10, 130, 60, 22);
			txtSequence = new JTextField();
			pnlRenameOptions.add(txtSequence);
			txtSequence.setBounds(90, 130, 80, 22);
			txtSequence.setEditable(false);
			// Sequence
			cbxLeadingZeros = getMyComboBox(sequences, "");
			cbxLeadingZeros.addActionListener(this);
			cbxLeadingZeros.setBounds(190, 130, 80, 22);
			pnlRenameOptions.add(cbxLeadingZeros);
			cbxResetCounter = new JCheckBox("Reset sequence each directory");
			cbxResetCounter.setSelected(true);
			pnlRenameOptions.add(cbxResetCounter);
			cbxResetCounter.setBounds(330, 130, 240, 22);
			pnlTransformOpt = new JPanel();
			FlowLayout pnlOptLayout = new FlowLayout();
			pnlOptLayout.setAlignment(FlowLayout.LEFT);
			pnlTransformOpt.setLayout(pnlOptLayout);
			pnlRenameOptions.add(pnlTransformOpt);
			pnlTransformOpt.setBounds(80, 150, 600, 30);
			rbtNormal = new JRadioButton("Normal", true);
			rbtLowerCase = new JRadioButton("To Lowercase");
			rbtUpperCase = new JRadioButton("To Uppercase");
			rbtCapitalize = new JRadioButton("To Capitalize");
			rbtParentAsFilename = new JRadioButton("Filename from parent dir");
			btnGrpTransform = new ButtonGroup();
			btnGrpTransform.add(rbtNormal);
			btnGrpTransform.add(rbtLowerCase);
			btnGrpTransform.add(rbtUpperCase);
			btnGrpTransform.add(rbtCapitalize);
			btnGrpTransform.add(rbtParentAsFilename);
			rbtNormal.addItemListener(this);
			rbtLowerCase.addItemListener(this);
			rbtUpperCase.addItemListener(this);
			rbtCapitalize.addItemListener(this);
			rbtParentAsFilename.addItemListener(this);
			pnlTransformOpt.add(rbtNormal);
			pnlTransformOpt.add(rbtLowerCase);
			pnlTransformOpt.add(rbtUpperCase);
			pnlTransformOpt.add(rbtCapitalize);
			pnlTransformOpt.add(rbtParentAsFilename);
			// >> New Tab for CompareOptions
			pnlCompareFileOptions = new JPanel();

			pnlCompareFileOptions.setLayout(null);
			pnlCompareFileOptions.setRequestFocusEnabled(false);
			pnlCompareFileOptions.setPreferredSize(new java.awt.Dimension(660, 230));
			lblLeftOffset = new JLabel("Left offset");
			pnlCompareFileOptions.add(lblLeftOffset);
			lblLeftOffset.setBounds(10, 10, 100, 22);
			txtLeftOffset = new JTextField("1");
			pnlCompareFileOptions.add(txtLeftOffset);
			txtLeftOffset.setBounds(90, 10, 100, 22);
			// txtLeftStart.addKeyListener(this);
			addMyKeyListener(txtLeftOffset);
			cbxLefOffset = getMyComboBox(this.compareLimitList, "1");
			cbxLefOffset.setBounds(220, 10, 60, 22);
			pnlCompareFileOptions.add(cbxLefOffset);
			lblItemsLeft = new JLabel("Items left");
			pnlCompareFileOptions.add(lblItemsLeft);
			lblItemsLeft.setBounds(300, 10, 80, 22);
			txtItemsLeft = new JTextField("99999");
			pnlCompareFileOptions.add(txtItemsLeft);
			txtItemsLeft.setBounds(400, 10, 100, 22);
			addMyKeyListener(txtItemsLeft);
			cbxItemsLeft = getMyComboBox(this.compareLimitList, "1");
			cbxItemsLeft.setBounds(520, 10, 60, 22);
			pnlCompareFileOptions.add(cbxItemsLeft);
			// Rights >>
			lblRightStart = new JLabel("Right offset");
			pnlCompareFileOptions.add(lblRightStart);
			lblRightStart.setBounds(10, 40, 100, 22);
			txtRightOffset = new JTextField("1");
			pnlCompareFileOptions.add(txtRightOffset);
			txtRightOffset.setBounds(90, 40, 100, 22);
			// txtRightStart.addKeyListener(this);
			addMyKeyListener(txtRightOffset);
			cbxRightOffset = getMyComboBox(this.compareLimitList, "1");
			cbxRightOffset.setBounds(220, 40, 60, 22);
			pnlCompareFileOptions.add(cbxRightOffset);
			lblItemsRight = new JLabel("Items right");
			pnlCompareFileOptions.add(lblItemsRight);
			lblItemsRight.setBounds(300, 40, 80, 22);
			txtItemsRight = new JTextField("99999");
			pnlCompareFileOptions.add(txtItemsRight);
			txtItemsRight.setBounds(400, 40, 100, 22);
			addMyKeyListener(txtItemsRight);
			cbxItemsRight = getMyComboBox(this.compareLimitList, "1");
			cbxItemsRight.setBounds(520, 40, 60, 22);
			pnlCompareFileOptions.add(cbxItemsRight);
			// Rights <<
			// blocksize
			lblBufferSize = new JLabel("Buffer size");
			pnlCompareFileOptions.add(lblBufferSize);
			lblBufferSize.setBounds(10, 70, 100, 22);
			txtBufferSize = new JTextField(bufferSizeList[4]);
			pnlCompareFileOptions.add(txtBufferSize);
			txtBufferSize.setBounds(90, 70, 100, 22);
			// txtRightStart.addKeyListener(this);
			addMyKeyListener(txtBufferSize);
			cbxBufferSize = getMyComboBox(this.bufferSizeList, "1");
			cbxBufferSize.setBounds(220, 70, 60, 22);
			cbxBufferSize.setSelectedIndex(4);
			pnlCompareFileOptions.add(cbxBufferSize);
			lblBufferSizeFactor = new JLabel("Buffer factor");
			pnlCompareFileOptions.add(lblBufferSizeFactor);
			lblBufferSizeFactor.setBounds(300, 70, 100, 22);
			txtBufferSizeFactor = new JTextField(bufferSizeFactorList[5]);
			pnlCompareFileOptions.add(txtBufferSizeFactor);
			txtBufferSizeFactor.setBounds(400, 70, 100, 22);
			addMyKeyListener(txtBufferSizeFactor);
			cbxBufferSizeFactor = getMyComboBox(this.bufferSizeFactorList, "1");
			cbxBufferSizeFactor.setBounds(520, 70, 60, 22);
			pnlCompareFileOptions.add(cbxBufferSizeFactor);
			
			jTabbedPaneOptionsTop = new JTabbedPane();
			getContentPane().add(jTabbedPaneOptionsTop);
			// jTabbedPane1.setBounds(60, 120, 720, 200);
			jTabbedPaneOptionsTop.setBounds(20, 110, 760, 230);
			jTabbedPaneOptionsTop.addTab("Search Options", null, pnlSearchOptions, null);
			jTabbedPaneOptionsTop.addTab("Rename Options", null, pnlRenameOptions, null);
			jTabbedPaneOptionsTop.addTab("Compare Options", null, pnlCompareFileOptions, null);

			// << blocksize

			// <<< new Tab for Compare options

			// Bottom button for table
			JPanel pnlTopCmdButtons = new JPanel();

			FlowLayout pnlTopCmdButtonsLayout = new FlowLayout();
			// pnlTopCmdButtonsLayout.setAlignment(FlowLayout.RIGHT);
			pnlTopCmdButtonsLayout.setAlignment(FlowLayout.LEFT);
			pnlTopCmdButtons.setLayout(pnlTopCmdButtonsLayout);
			pnlTopCmdButtons.setBounds(10, 340, WIDTH_FRM - 10, 30);
			// pnlTopCmdButtons.setBounds(60, 330, 720 , 30);
			getContentPane().add(pnlTopCmdButtons);

			btnRemoveFomList = new JButton("Remove from List");
			pnlTopCmdButtons.add(btnRemoveFomList);
			btnRemoveFomList.addActionListener(this);

			btnSelectAll = new JButton("Select All");
			pnlTopCmdButtons.add(btnSelectAll);
			btnSelectAll.addActionListener(this);

			btnClearAll = new JButton("Clear All");
			pnlTopCmdButtons.add(btnClearAll);
			btnClearAll.addActionListener(this);

			btnDeleteFiles = new JButton("Delete Selected Files");
			pnlTopCmdButtons.add(btnDeleteFiles);
			btnDeleteFiles.addActionListener(this);

			btnDeleteDuplicateFilesLeft = new JButton("Delete Duplicate Files (Left)");
			pnlTopCmdButtons.add(btnDeleteDuplicateFilesLeft);
			btnDeleteDuplicateFilesLeft.addActionListener(this);

			btnDeleteDuplicateFilesRight = new JButton("Delete Duplicate Files (Rihgt)");
			pnlTopCmdButtons.add(btnDeleteDuplicateFilesRight);
			btnDeleteDuplicateFilesRight.addActionListener(this);

			btnRenameSelectedFiles = new JButton("Rename Selected Files");
			pnlTopCmdButtons.add(btnRenameSelectedFiles);
			btnRenameSelectedFiles.addActionListener(this);

//			btnOpenSelectedFile = new JButton("Open File");
//			btnOpenSelectedFile.addActionListener(this);
//			pnlTopCmdButtons.add(btnOpenSelectedFile);

			btnViewLogFiles = new JButton("View Log Files");
			pnlTopCmdButtons.add(btnViewLogFiles);
			btnViewLogFiles.addActionListener(this);

			btnsearchDuplicateFiles = new JButton("Search Duplicate");
			pnlTopCmdButtons.add(btnsearchDuplicateFiles);
			btnsearchDuplicateFiles.addActionListener(this);

			btnSearchFiles = new JButton("Search");
			pnlTopCmdButtons.add(btnSearchFiles);
			btnSearchFiles.addActionListener(this);

			// Panel for Table Scroll
			jTabbedPanelForTables = new JTabbedPane();
			// jTabbedPanelForTables.setBounds(10, 370, WIDTH_FRM - 60, 220);
			jTabbedPanelForTables.setBounds(10, 370, WIDTH_FRM - 30, HEIGHT_FRM - 550 - 30);
			getContentPane().add(jTabbedPanelForTables);

			pnlTableForRenameFiles = new JPanel();
			pnlTableForRenameFiles.setBounds(10, 390, WIDTH_FRM - 80, 200);

			scrollPaneForRenameFiles = new JScrollPane();
			scrollPaneForRenameFiles.setPreferredSize(new java.awt.Dimension(WIDTH_FRM - 80, 200));

			searchTableModel = new SearcherTableModel(null, SearcherTableModel.TABLE_COLUMN_SEARCH);

			tableSearchResult = new JTable(searchTableModel);
			tableSearchResult.addFocusListener(this);
			tableSearchResult.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tableSearchResult.setFillsViewportHeight(true);
			RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(searchTableModel);
			tableSearchResult.setRowSorter(sorter);

			tableSearchResult.addMouseListener(this);
			tableSearchResult.getTableHeader().addMouseListener(this);

			tableSearchResult.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					JTable t = (JTable) e.getSource();
					JViewport vport = scrollPaneForRenameFiles.getViewport();
					Point vp = vport.getViewPosition();
					vp.translate(0, e.getWheelRotation() * t.getRowHeight());
					t.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
				}
			});

			final TableCellRenderer r = tableSearchResult.getTableHeader().getDefaultRenderer();
			tableSearchResult.getTableHeader().setDefaultRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JLabel l = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					l.setBorder(BorderFactory.createCompoundBorder(l.getBorder(),
							BorderFactory.createEmptyBorder(0, 5, 0, 0)));
					l.setHorizontalAlignment(SwingConstants.LEFT);
					return l;
				}
			});

			tableSearchResult.setModel(searchTableModel);
			tableSearchResult.addMouseListener(this);
			tableSearchResult.addKeyListener(this);
			tableSearchResult.getSelectionModel()
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						@Override
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
							}
						}
					});

			ToolTipManager.sharedInstance().unregisterComponent(tableSearchResult);
			ToolTipManager.sharedInstance().unregisterComponent(tableSearchResult.getTableHeader());
			tableSearchResult.setComponentPopupMenu(popupMenu);
			openFileFolder = new JMenuItem("Open folder");
			openFileFolder.addActionListener(this);
			scrollPaneForRenameFiles.setViewportView(tableSearchResult);
			// pnlTableForRenameFiles.add(scrollPaneForRenameFiles);
			scrollPaneForRenameFiles.add(pnlTableForRenameFiles);
			add(scrollPaneForRenameFiles);

			// table for Comparison

			pnlTableForCompare = new JPanel();
			pnlTableForCompare.setBounds(10, 390, WIDTH_FRM - 80, 210);

			scrollPaneForCompare = new JScrollPane();
			scrollPaneForCompare.setPreferredSize(new java.awt.Dimension(WIDTH_FRM - 80, 210));

			scrollPaneForCompare.add(pnlTableForCompare);

			dublicateTableModel = new DuplicateTableModel(null, DuplicateTableModel.TABLE_COLUMN_COMPARISON);

			tableCompareResult = new JTable(dublicateTableModel);
			tableCompareResult.addFocusListener(this);
			tableCompareResult.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tableCompareResult.setFillsViewportHeight(true);
			RowSorter<TableModel> sorterCompare = new TableRowSorter<TableModel>(dublicateTableModel);
			tableCompareResult.setRowSorter(sorterCompare);

			tableCompareResult.addMouseListener(this);
			tableCompareResult.setModel(dublicateTableModel);

			tableCompareResult.getTableHeader().addMouseListener(this);

			tableCompareResult.getColumn(tableCompareResult.getColumnName(10)).setCellRenderer(new JButtonRenderer());
			tableCompareResult.getColumn(tableCompareResult.getColumnName(10)).setCellEditor(new JButtonEditor());

			tableCompareResult.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					JTable t = (JTable) e.getSource();
					JViewport vport = scrollPaneForCompare.getViewport();
					Point vp = vport.getViewPosition();
					vp.translate(0, e.getWheelRotation() * t.getRowHeight());
					t.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
				}
			});

			tableCompareResult.setModel(dublicateTableModel);
			tableCompareResult.addMouseListener(this);
			tableCompareResult.addKeyListener(this);
			tableCompareResult.getSelectionModel()
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						@Override
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
							}
						}
					});

			jTabbedPanelForTables.addTab("Search Result", null, scrollPaneForRenameFiles, null);

			jTabbedPanelForTables.addTab("Compare", null, scrollPaneForCompare, null);

			scrollPaneForCompare.setViewportView(tableCompareResult);

			// table for Comparison

			pnlFilePreview = new JPanel();
			pnlFilePreview.setLocation(830, 10);
			pnlFilePreview.setSize(dimensionPreview);

			pnlBottomCmdButton = new JPanel();
			pnlBottomCmdButton.setBounds(60, jTabbedPanelForTables.getY() + jTabbedPanelForTables.getHeight() + 20,
					WIDTH_FRM - 60, 30);
			add(pnlBottomCmdButton);

			btnConvertImage = new JButton("Convert Image");
			pnlBottomCmdButton.add(btnConvertImage);
			btnConvertImage.addActionListener(this);

			btnCopyFilesTo = new JButton("Copy Files To");
			pnlBottomCmdButton.add(btnCopyFilesTo);
			btnCopyFilesTo.addActionListener(this);

			btnEditTextFile = new JButton("Edit Text File");
			pnlBottomCmdButton.add(btnEditTextFile);
			btnEditTextFile.addActionListener(this);

			btnOpenFileFolder = new JButton("Open File Folder");
			pnlBottomCmdButton.add(btnOpenFileFolder);
			btnOpenFileFolder.addActionListener(this);

			btnOpenSelectedFile = new JButton("Open File");
			pnlBottomCmdButton.add(btnOpenSelectedFile);
			btnOpenSelectedFile.addActionListener(this);

			btnCreateCSV = new JButton("Create CSV File f. Albums");
			pnlBottomCmdButton.add(btnCreateCSV);
			btnCreateCSV.addActionListener(this);

			btnCreateXML = new JButton("Create XML for Albums");
			pnlBottomCmdButton.add(btnCreateXML);
			btnCreateXML.addActionListener(this);

			btnExportTable = new JButton("Export Tablecontent");
			pnlBottomCmdButton.add(btnExportTable);
			btnExportTable.addActionListener(this);

			pnlInfo = new JPanel();
			pnlInfo.setBackground(Color.GREEN);
			lblFileInfo = new JLabel("File: ");
			lblFileInfo.setBackground(Color.GREEN);
			pnlInfo.add(lblFileInfo);
			// pnlInfo.setBounds(60, 590, WIDTH_FRM - 60, 30);
			pnlInfo.setBounds(60, pnlBottomCmdButton.getY() + pnlBottomCmdButton.getHeight() + 10, WIDTH_FRM - 10, 20);

			add(pnlInfo);

			JPanel pnlProgressBar = new JPanel();
			pnlProgressBar.setLayout(new FlowLayout(FlowLayout.CENTER));
			pnlProgressBar.setBounds(60, pnlInfo.getY() + pnlInfo.getHeight() + 10, WIDTH_FRM - 60, 30);
			add(pnlProgressBar);

			UIManager.put("ProgressBar.foreground", new Color(8, 32, 128));

			// lblEntries = new JLabel(":");
			// pnlProgressBar.add(lblEntries);
			// lblEntries.setBounds(10, 10, 60, 22);
			//
			// txtEntries = new JTextField();
			// pnlProgressBar.add(txtEntries);
			// //txtEntries.setBounds(120, 10, 60, 22);
			// txtEntries.setBounds(new Rectangle(DIMENSION_140_22));

			progressBar = new JProgressBar();
			progressBar.setMinimum(minValue);
			progressBar.setMaximum(maxValue);
			progressBar.setStringPainted(true);
			progressBar.setPreferredSize(new Dimension(400, 22));
			// progressBar.setBounds(new Rectangle(DIMENSION_140_22));
			pnlProgressBar.add(progressBar);

			btnStopRunner = new JButton("Stop");
			pnlProgressBar.add(btnStopRunner);
			btnStopRunner.addActionListener(this);

			// create popupmenu for JTable
			popupMenu = new JPopupMenu();
			menuItemEditTextFile = new JMenuItem("Edit textfile");
			menuItemCopyFilesTo = new JMenuItem("Copy files to");
			menuItemOpenFileFolder = new JMenuItem("Open file folder");

			menuItemEditTextFile.addActionListener(this);
			menuItemCopyFilesTo.addActionListener(this);
			menuItemOpenFileFolder.addActionListener(this);

			popupMenu.add(menuItemEditTextFile);
			popupMenu.add(menuItemCopyFilesTo);
			popupMenu.add(menuItemOpenFileFolder);
			tableSearchResult.setComponentPopupMenu(popupMenu);
			tableCompareResult.setComponentPopupMenu(popupMenu);

			this.setFocusCycleRoot(false);
			this.setLocation(new java.awt.Point(10, 10));
			this.addWindowListener(this);

			// progressMonitor = new ProgressMonitor(this, "Running a Long
			// Task",
			// "", 0, 100);
			// progressMonitor.setProgress(0);

			addKeyListener(this);
			// addPropertyChangeListener(this);
			menuBar = getMyMenuBar();
			this.setJMenuBar(menuBar);

			tableCompareResult.getModel().addTableModelListener(this);
			tableSearchResult.getModel().addTableModelListener(this);

			setVisible(true);

			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel getPanelDirectory() {
		pnlDirectory = new JPanel();
		FlowLayout pnlDirectoryLayout = new FlowLayout();
		pnlDirectoryLayout.setAlignment(FlowLayout.LEFT);
		pnlDirectory.setLayout(pnlDirectoryLayout);
		pnlDirectory.setBounds(20, 10, 760, 30);

		lblSourceDir = new JLabel("Search Path:");
		lblSourceDir.setPreferredSize(DIMENSION_100_22);
		pnlDirectory.add(lblSourceDir);

		txtSearchDir = new JTextField(defaultSearchDir);
		txtSearchDir.setPreferredSize(DIMENSION_500_22);
		txtSearchDir.addFocusListener(this);
		pnlDirectory.add(txtSearchDir);

		btnChooseDir = new JButton("Open directory");
		btnChooseDir.setPreferredSize(DIMENSION_140_22);
		pnlDirectory.add(btnChooseDir);
		btnChooseDir.addActionListener(this);		
		return pnlDirectory;
	}

	public void initProgressBar(JProgressBar pProgressBar) {
		pProgressBar.setValue(0);
	}

	private void initStringBuffer() {
		if (STR_BUFFER == null) {
			STR_BUFFER = new StringBuffer();
		}
		STR_BUFFER.setLength(0);
	}

	// private boolean isSearchLimitReached(javax.swing.table.DefaultTableModel
	// pTableModell, int pSearchLimit) {
	// // TODO Auto-generated method stub
	// return (pSearchLimit > 0 && (pTableModell.getRowCount() >= pSearchLimit));
	// }

	// private String getRenameFilenameNew(File pFile, String pRegexpReplaceOld,
	// String pRegexpReplaceNew,
	// String pReplaceAll, String pReplaceOld, String pReplaceNew, String pPrefix,
	// String pSuffix,
	// boolean pRenameLowerCase, boolean pRenameUpperCase, boolean
	// pRenameCapitalize,
	// boolean pRenameParentAsFilename, String pSequence, String pLeadZero, int
	// pIndex) {
	// String lNewFile = "";
	// String lParent = pFile.getParent();
	//
	// String lFileNameWithoutExtension = pFile.getName();
	// String lFileExtension = "";
	// // if we have point at end of file
	// int indx = pFile.getName().lastIndexOf('.');
	// if (indx > 0) {
	// lFileNameWithoutExtension = pFile.getName().substring(0, indx);
	// lFileExtension = pFile.getName().substring(indx + 1);
	// }
	//
	// if (!pReplaceAll.isEmpty()) {
	// lNewFile = pReplaceAll;
	// } else if (!pReplaceOld.isEmpty()
	// /* && !txtReplaceOld.getText().isEmpty() */) {
	// if (pReplaceNew.isEmpty()) {
	// pReplaceNew = ""; //
	// }
	// lNewFile = replace(lFileNameWithoutExtension, pReplaceOld, pReplaceNew);
	// lFileExtension = replace(lFileExtension, pReplaceOld, pReplaceNew);
	// // if (pReplaceNew.isEmpty()) {
	// // pReplaceNew = ""; // TODO???
	// // } else {
	// // lNewFile = replace(lFileNameWithoutExtension, pReplaceOld,
	// // pReplaceNew);
	// // lFileExtension = replace(lFileExtension, pReplaceOld,
	// // pReplaceNew);
	// //
	// // }
	//
	// } else if (!pRegexpReplaceNew.isEmpty()) {
	// lNewFile = getNewFilenameFromRegExpReplacement(pRegexpReplaceOld,
	// pRegexpReplaceNew,
	// lFileNameWithoutExtension);
	// } else {
	// lNewFile = lFileNameWithoutExtension;
	// }
	//
	// if (!pPrefix.isEmpty()) {
	// lNewFile = pPrefix + lNewFile;
	// }
	// if (!pSuffix.isEmpty()) {
	// lNewFile = lNewFile + pSuffix;
	// }
	// if (pRenameLowerCase) {
	// lNewFile = lNewFile.toLowerCase(LOCALE_TR);
	// if (lFileExtension.length() > 0) {
	// lFileExtension.toLowerCase(LOCALE_TR);
	// }
	// }
	// if (pRenameUpperCase) {
	// lNewFile = lNewFile.toUpperCase(LOCALE_TR);
	// if (lFileExtension.length() > 0) {
	// lFileExtension.toUpperCase(LOCALE_TR);
	// }
	// }
	// if (pRenameCapitalize) {
	// // lNewFile = EasyUtility.capitalize(lNewFile);
	// lNewFile = capitalizeString(lNewFile);
	// if (lFileExtension.length() > 0) {
	// // lFileExtension = capitalizeString(lFileExtension);
	// }
	// }
	// if (pRenameParentAsFilename) {
	// lNewFile = getNewFilenameFromParent(pFile.getParent(), lNewFile,
	// lFileNameWithoutExtension);
	// }
	// String lSequence = "";
	// if (!pLeadZero.equals("None")) {
	// int lSeqNo = 0;
	// if (pSequence.length() > 0) {
	// lSeqNo = Integer.parseInt(pSequence);
	// }
	// int lZeros = Integer.parseInt(pLeadZero);
	// lSequence = getNameSequence(lSeqNo + pIndex, lZeros);
	// lNewFile = lNewFile + lSequence;
	// }
	// lNewFile = lParent + getFileSeparator() + lNewFile;
	// if (lFileExtension != null && lFileExtension.length() > 0) {
	// lNewFile = lNewFile + "." + lFileExtension;
	// }
	// return lNewFile;
	// }

	@Override
	public void inputMethodTextChanged(InputMethodEvent event) {
	}

	private boolean isImageFile(String pImage) {
		if (pImage.length() == 0) {
			return false;
		}
		return new File(pImage).isFile()
				&& (pImage.toLowerCase(LOCALE_TR).endsWith("jpeg") || pImage.toLowerCase(LOCALE_TR).endsWith("jpg")
						|| pImage.endsWith("gif") || pImage.toLowerCase(LOCALE_TR).endsWith("tif")
						|| pImage.endsWith("bmp") || pImage.toLowerCase(LOCALE_TR).endsWith("png"));
	}

	private boolean isSearchLimitReached(int pSearchLimit) {
		// TODO Auto-generated method stub
		return (pSearchLimit > 0 && (searchTableModel.getRowCount() >= pSearchLimit));
	}

	public boolean isSuccessiveUppercaseChars(String pText) {
		if (pText.length() <= 1) {
			return false;
		}

		int preview_value = 0;
		int current_value = 0;
		for (int i = 0; i < pText.length(); i++) {
			// char c = pText.charAt(i);
			current_value = pText.codePointAt(i);
			if (preview_value > 0) {
				if (Character.isUpperCase(preview_value) && Character.isUpperCase(current_value)) {
					return true;
				}
			}
			preview_value = current_value;
			// if(c >= 97 && c <= 122) {
			// return false;
			// }
		}
		// str.charAt(index)
		return false;
	}

	private boolean isTextFile(String pFilename) {
		if (pFilename.length() == 0) {
			return false;
		}

		// pFilename.length() >= Runtime.getRuntime().maxMemory()
		if (new File(pFilename).length() >= Runtime.getRuntime().maxMemory()) {
			return false;
		}
		return pFilename.toLowerCase(LOCALE_TR).endsWith("txt") || pFilename.toLowerCase(LOCALE_TR).endsWith("log")
				|| pFilename.endsWith("xml") || pFilename.toLowerCase(LOCALE_TR).endsWith("html")
				|| pFilename.toLowerCase(LOCALE_TR).endsWith("ini") || pFilename.toLowerCase(LOCALE_TR).endsWith("csv")
				|| pFilename.endsWith("java") || pFilename.endsWith("css")
				|| pFilename.toLowerCase(LOCALE_TR).endsWith("php");
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		Object source = event.getSource();
		if (source == cbxFileExtentions) {
			txtEndsWith.setText((String) cbxFileExtentions.getSelectedItem());
		}

		if (source == cbxLefOffset) {
			txtLeftOffset.setText((String) cbxLefOffset.getSelectedItem());
		}

		if (source == cbxItemsLeft) {
			txtItemsLeft.setText((String) cbxItemsLeft.getSelectedItem());
		}

		if (source == cbxRightOffset) {
			txtRightOffset.setText((String) cbxRightOffset.getSelectedItem());
		}

		if (source == cbxItemsRight) {
			txtItemsRight.setText((String) cbxItemsRight.getSelectedItem());
		}

		if (source == cbxBufferSize) {
			txtBufferSize.setText((String) cbxBufferSize.getSelectedItem());
		}

		if (source == cbxBufferSizeFactor) {
			txtBufferSizeFactor.setText((String) cbxBufferSizeFactor.getSelectedItem());
		}

		// if (source == cbxSearchLimits) {
		// cbxSearchLimits.setText((String) cbxItemsRight.getSelectedItem());
		// }
		//
		txtSearchPattern.setEditable(txtContainStr.getText().isEmpty() && txtStartsWith.getText().isEmpty()
				&& txtEndsWith.getText().isEmpty());

		txtContainStr.setEditable(txtSearchPattern.getText().isEmpty());
		txtStartsWith.setEditable(txtSearchPattern.getText().isEmpty());
		txtEndsWith.setEditable(txtSearchPattern.getText().isEmpty());
	}

	// private String replace(String str, String pattern, String replace) {
	// int s = 0;
	// int e = 0;
	// StringBuffer result = new StringBuffer();
	//
	// while ((e = str.indexOf(pattern, s)) >= 0) {
	// result.append(str.substring(s, e));
	// result.append(replace);
	// s = e + pattern.length();
	// }
	// result.append(str.substring(s));
	// return result.toString();
	// }

	@Override
	public void keyPressed(KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_UP
				|| event.getKeyCode() == KeyEvent.VK_DOWN)) {
			repaint();
			try {
				showPreview(getSelectedFilename());
				changeFrameTitle();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_RIGHT
				|| event.getKeyCode() == KeyEvent.VK_LEFT) {
			showFile(getSelectedFilename());
		}
		if (event.getKeyCode() == KeyEvent.VK_DELETE) {
			deleteSelectedSearchFiles();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		Object source = event.getSource();
		if (source != tableSearchResult && source != cbxFileExtentions) {
			txtSearchPattern.setEditable(txtContainStr.getText().isEmpty() && txtStartsWith.getText().isEmpty()
					&& txtEndsWith.getText().isEmpty());

			txtContainStr.setEditable(txtSearchPattern.getText().isEmpty());
			txtStartsWith.setEditable(txtSearchPattern.getText().isEmpty());
			txtEndsWith.setEditable(txtSearchPattern.getText().isEmpty());
			// process();
		}
		try {
			showPreview(getSelectedFilename());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	public void loadPropertiesFile(String pPropFile) {
		PROPS_FILE = pPropFile;
		java.io.FileInputStream fis = null;
		FileOutputStream fos = null;
		File myFile = new File(pPropFile);
		if (myFile.isFile() && myFile.exists()) {
			try {

				fis = new java.io.FileInputStream(PROPS_FILE);
				// fis = new java.io.FileInputStream(getClass().getResource(
				// "resources/"+PROPS_FILE).getPath());
				this.prop.load(fis);
				defaultSearchDir = prop.getProperty(DEFAULT_SEARCH_DIR);
				if (defaultSearchDir == null && defaultSearchDir.length() == 0) {
					defaultSearchDir = System.getProperty("user.dir");
				}
				defaultCompareDir = prop.getProperty(DEFAULT_COMPARE_DIR);
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			defaultSearchDir = System.getProperty("user.dir");
			try {
				fos = new FileOutputStream(new File(PROPS_FILE));
				prop.store(fos, "my interesting comment");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void log(Object pObject) {
		if (pObject != null && cbxLoggingCommandLine.isSelected()) {
			cmdLineLogging(pObject.toString());
			// System.out.println(pObject.toString());
		}
	}

	public void logOnGUI(Object pObject) {
		if (pObject != null && cbxLoggingCommandLine.isSelected()) {
			lblFileInfo.setText((String) pObject);
		}
	}

	public void logOnTitle(Object pObject) {
		if (pObject != null && cbxLoggingCommandLine.isSelected()) {
			setTitle((String) pObject);
		}
	}

	private void logTimeStamp() {
		if (cbxLoggingCommandLine.isSelected()) {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			// Date now = new Date();
			// String strDate = sdfDate.format(new Date());
			System.out.println(">>> Time: " + sdfDate.format(new Date()));
		}
	}

	private boolean matchCreationDate(File pSearchedFile, String pCreationDateFrom, String pCreationDateTo) {

		if (!pCreationDateFrom.isEmpty() || !pCreationDateTo.isEmpty()) {
			Date from_date = null;
			Date to_date = null;
			Date file_date = null;
			file_date = new Date(pSearchedFile.lastModified());
			getsDateFormt().format(file_date);

			if (!pCreationDateFrom.isEmpty()) {
				from_date = getDateFromString(pCreationDateFrom, getsDateFormt());
			}
			if (!pCreationDateTo.isEmpty()) {
				to_date = getDateFromString(pCreationDateTo, getsDateFormt());
				Calendar cal = Calendar.getInstance();
				cal.setTime(to_date);
				// cal.set(Calendar.HOUR_OF_DAY, 23);
				// cal.set(Calendar.MINUTE, 59);
				// cal.set(Calendar.SECOND, 59);
				// cal.set(Calendar.MILLISECOND, 999); // credit to f1sh
				cal.add(Calendar.DATE, 1); // next day, check below < for
											// 23:59:59
				to_date = cal.getTime();
			}

			if (from_date != null && to_date != null) {
				return from_date.getTime() <= file_date.getTime() && file_date.getTime() < to_date.getTime();
			} else if (from_date != null && to_date == null) {
				return file_date.getTime() >= from_date.getTime();
			} else if (from_date == null && to_date != null) {
				return file_date.getTime() < to_date.getTime();
			}
		}
		return true;
	}

	private boolean matchEndsWith(String pEndsWith, String searchedFileName) {
		if (!pEndsWith.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).endsWith(pEndsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	private boolean matchFileSearchOptionsNew(File pSearchedFile, String pStartsWith, String pEndsWith,
			String pContainStr, String pSearchPattern, String pFileSize, boolean pAllFileSize, boolean pFileSizeGreater,
			boolean pFileSizeLower, boolean pFileSizeEqual, String pCreationDateFrom, String pCreationDateTo,
			boolean pSuccessiveUppercaseChars) throws ParseException {
		// no checks if fields are empty
		String searchedFileName = pSearchedFile.getName();
		if (!matchStartsWith(pStartsWith, searchedFileName)) {
			return false;
		}

		if (!matchEndsWith(pEndsWith, searchedFileName)) {
			return false;
		}

		if (!pContainStr.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).contains(pContainStr.toLowerCase(LOCALE_TR))) {
			return false;
		}
		if (!matchPattern(pSearchPattern, searchedFileName)) {
			return false;
		}

		// check SuccessiveUppercaseChars

		if (pSuccessiveUppercaseChars && !isSuccessiveUppercaseChars(searchedFileName)) {
			return false;
		}

		// check Attributes
		if (!matchFileSizeAttribute(pSearchedFile, pFileSize, pAllFileSize, pFileSizeGreater, pFileSizeLower,
				pFileSizeEqual)) {
			return false;
		}

		if (!matchCreationDate(pSearchedFile, pCreationDateFrom, pCreationDateTo)) {
			return false;
		}
		return true;
	}

	private boolean matchFileSizeAttribute(File pSearchedFile, String pFileSize, boolean pAllFileSize,
			boolean pFileSizeGreater, boolean pFileSizeLower, boolean pFileSizeEqual) {
		if (!pFileSize.isEmpty() && !pAllFileSize) {
			float searched_filesize = Float.parseFloat(pFileSize.toString());

			// float lfloat_1024 = 1024;
			float filesize = getFileSize(pSearchedFile);
			if (pFileSizeGreater) {
				if (filesize < searched_filesize) {
					return false;
				}

			} else if (pFileSizeLower) {
				if (filesize > searched_filesize) {
					return false;
				}
				// return !(filesize < searched_filesize);
			} else if (pFileSizeEqual) {
				return (filesize == searched_filesize);

			} else {
				return false;
			}
		}
		return true;
	}

	private boolean matchPattern(String pPattern, String pInputStr) {
		if (pPattern.isEmpty() || pInputStr.isEmpty()) {
			return true;
		}
		Pattern pattern = Pattern.compile(pPattern);
		Matcher matcher = pattern.matcher(pInputStr);
		return matcher.find();
	}

	private boolean matchStartsWith(String pStartsWith, String searchedFileName) {
		if (!pStartsWith.isEmpty()
				&& !searchedFileName.toLowerCase(LOCALE_TR).startsWith(pStartsWith.toLowerCase(LOCALE_TR))) {
			return false;
		}
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		changeFileInfoText();
		changeFrameTitle();
		repaint();
		if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1) {
			try {
				showPreview(getSelectedFilename());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	// public void windowGainedFocus(WindowEvent e) {
	// System.out.println("Window Gained Focus Event");
	// }

	private void openFileFolder() {
		String openPath = getSelectedFilename();
		File file = new File(openPath);
		if (file.isFile()) {
			openPath = file.getParent().toString();
		}

		try {
			Runtime.getRuntime().exec("explorer.exe " + openPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openSelectedFile(JTable pTable, DefaultTableModel pMyTblModel) {
		try {
			log("Open file...." + pMyTblModel.getValueAt(pTable.getSelectedRow(), 1).toString());
			Runtime.getRuntime().exec("cmd.exe /c " + pMyTblModel.getValueAt(pTable.getSelectedRow(), 1).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		// Object obj = evt.getOldValue();
		// if (obj instanceof JTable){
		// if(obj == tableModelSearch ){
		//
		// }
		// }
	}

	private void removeFomList() {
		// TODO Auto-generated method stub
		// getActiveTable().getSelectedRows();
		int[] intselected = getActiveTable().getSelectedRows();
		for (int i = intselected.length - 1; i >= 0; i--) {
			// getActiveTable().remove(i);
			((DefaultTableModel) getActiveTable().getModel()).removeRow(intselected[i]);
			// TableRowSorter sorter = new
			// TableSorter(getActiveTable().getModel());
			// getActiveTable().getTableHeader().gett
			// ((DefaultTableModel)((TableSorter)getActiveTable().getModel()).removeRow(((TableSorter)getActiveTable().getModel()).modelIndex(intselected[i]));
		}

		getActiveTable().addNotify();
		// repaint();
	}

	private void renameSelectedFiles() {
		String lLogText = "";
		int renameCountr = 0;
		int[] intselected = tableSearchResult.getSelectedRows();
		if (tableSearchResult.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "You have no file(s) to rename.");
			return;
		} else if (intselected.length == 0) {
			JOptionPane.showMessageDialog(this, "For rename select one or more files.");
			return;
		}

		if (JOptionPane.showConfirmDialog(null, "Do you realy want to rename selected file(s)?", "Rename files?",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			for (int i = intselected.length - 1; i >= 0; i--) {
				String oldfile = (String) tableSearchResult.getValueAt(intselected[i], 1);
				String newfile = (String) tableSearchResult.getValueAt(intselected[i], 2);

				if (!oldfile.equals(newfile)) {
					try {
						new File(oldfile).renameTo(new File(newfile));
						lLogText = "Rename " + oldfile + " --> " + newfile + " succesful!";
						renameCountr++;
					} catch (SecurityException exp) {
						exp.printStackTrace();
						lLogText = "Can't rename " + oldfile + " --> " + newfile + " !";
					}
					log(lLogText);
				}
			}

			if (renameCountr > 0) {
				JOptionPane.showMessageDialog(this, "Total renamed files: " + String.valueOf(renameCountr));
			}
		}
	}

	private void resetSearchOption(JPanel pPanelname) {
		// TODO Auto-generated method stub

		Component[] components = getContentPane().getComponents();

		for (int i = 0; i < components.length; ++i) {
			if ((components[i] instanceof JTabbedPane)) {
				JTabbedPane jTabbedPane = (JTabbedPane) components[i];
				if (jTabbedPane == jTabbedPaneOptionsTop) {
					// jTabbedPane.getSelectedComponent();
					// Component tabComp = jTabbedPane
					// .getTabComponentAt(jTabbedPane.getSelectedIndex());
					// Container cont3 = tabComp.getFocusCycleRootAncestor();
					Component[] components2 = jTabbedPane.getComponents();
					for (int j = 0; j < components2.length; ++j) {
						if (components2[j] instanceof JPanel) {
							JPanel jPanel = (JPanel) components2[j];
							if (jPanel == pPanelname) {
								Component[] components3 = jPanel.getComponents();
								for (int z = 0; z < components3.length; ++z) {
									if (components3[z] instanceof JTextField) {
										JTextField jTextField = (JTextField) components3[z];
										jTextField.setText("");
										;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void searchDublicateFiles() {

		jTabbedPanelForTables.setSelectedIndex(1);
		initProgressBar(progressBar);
		searchDublicateThread = new Thread() {
			@Override
			public void run() {

				ArrayList<File> foundFileAsDublicate = new ArrayList<File>();

				log(getTimeStamp());
				initStringBuffer();
				// BUFFSIZE = getTextFieldValueAsInteger(txtBufferSize);
				// BUFFSIZE_MULTIPLE = getTextFieldValueAsInteger(txtBufferSizeFactor);
				// log("TOTAL BUFFER SIZE = " + new Integer((BUFFSIZE *
				// BUFFSIZE_MULTIPLE)).toString());

				String leftPath = txtSearchDir.getText();
				String rightPath = txtCompareDir.getText();
				if (!dirExist(leftPath)) {
					return;
				}
				if (rightPath.length() > 0 && !dirExist(rightPath)) {
					return;
				}

				String statText = ">>>> Compare file content";
				String waitingText = "Please waiting ....";
				log(statText);
				log(waitingText);
				cmdLineLogging(statText);
				cmdLineLogging(waitingText);

				ProcessTimeController.start();
				getLeftItems();
				getRightItems();
				getLeftOffset();
				getRightOffset();

				clearRows(dublicateTableModel);
				clearRows(searchTableModel);

				ArrayList<EasyFileInfo> rightFileArrayList;
				ArrayList<EasyFileInfo> leftFileArrayList = getFileArrayList(leftPath, true);

				if (!rightPath.isEmpty()) {
					rightFileArrayList = getFileArrayList(rightPath, true);
				} else {
					rightFileArrayList = leftFileArrayList;
				}

				setTotalFoundEntries(leftFileArrayList.size());
				setNumberEntriesPerProcent();

				progressBarValue = 0;

				EasyFileInfo leftFileCollector;
				EasyFileInfo rightFileCollector;

				File leftFile = new File("");
				File rightFile = new File("");

				for (int i = 0; i < leftFileArrayList.size(); i++) {

					int currentProcentValue = getCurrentProcessedProcent(i + 1);
					if (currentProcentValue > progressBarValue && currentProcentValue % TEN == 0) {
						lblFileInfo.setText("Process: " + (i + 1) + " / " + TOTAL_FOUND_ENTRIES + " elapsed time "
								+ ProcessTimeController.getElapsedTime() + " [s]");
						progressBarValue = currentProcentValue;
					}

					Runnable progressBarThread = new Runnable() {
						@Override
						public void run() {
							progressBar.setValue(progressBarValue);
						}
					};
					SwingUtilities.invokeLater(progressBarThread);

					leftFileCollector = leftFileArrayList.get(i); // leftIterator.next();
					leftFile = leftFileCollector.getFile();

					String lText = "Process file:..." + leftFileCollector.getFile().getAbsoluteFile();
					// logOnGUI(lText);
					// lblFileInfo.setText(lText);

					setFileInfoText(lblFileInfo, lText);
					logOnTitle(lText);

					if (foundFileAsDublicate.contains(leftFile)) {
						continue; // for actuall file we have a entry
					}

					for (int j = 0; j < rightFileArrayList.size(); j++) {

						rightFileCollector = rightFileArrayList.get(j); // rightIterator.next();
						rightFile = rightFileCollector.getFile();
						// rightFile = rightFileArrayList.get(j).getFile();

						if (rightFileCollector.getFilelength() < leftFileCollector.getFilelength()) {
							continue;
						}

						if (rightFileCollector.getFilelength() > leftFileCollector.getFilelength()) {
							break;
						}

						if (foundFileAsDublicate.contains(rightFile)) {
							continue; // for actuall file we have a entry
						}

						if (leftFileCollector.getFilelength() == rightFileCollector.getFilelength()) {

							try {
								if (leftFile.getCanonicalPath().equals(rightFile.getCanonicalPath())) {
									continue;
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}

							if (CompareBinaries.sameContentFiles(leftFile, rightFile)) {
								try {
									addItemToDuplicateTable(dublicateTableModel, leftFileCollector.getFile(),
											rightFileCollector.getFile());
								} catch (ArrayIndexOutOfBoundsException e) {
									e.printStackTrace();
								}

								foundFileAsDublicate.add(leftFile);
								foundFileAsDublicate.add(rightFile);
								break;
							}
						}
					}
				}

				String lInfoTxt = "";
				progressBarValue = (int) HUNDERD;
				progressBar.setValue(progressBarValue);
				if (jTabbedPanelForTables != null) {
					jTabbedPanelForTables.setSelectedIndex(1);
				}

				ProcessTimeController.end();
				if (tableCompareResult != null) {
					int found_duplicate_entry = tableCompareResult.getRowCount();
					if (found_duplicate_entry == 0) {
						lInfoTxt += "No dublicate files found! ! Processtime: " + ProcessTimeController.getProzessTime()
								+ " [s]";
					} else if (tableCompareResult.getRowCount() > 0) {
						try {
							selectFirstEntry();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						lInfoTxt += "Found total dublicate files: " + found_duplicate_entry + "! Processtime: "
								+ ProcessTimeController.getProzessTime() + " [s]";
						log(lInfoTxt);
						addToLogBuffer(lInfoTxt);
					}
				}

				log(lInfoTxt);
				setFileInfoText(lblFileInfo, lInfoTxt);
				logOnTitle(lInfoTxt);
				logTimeStamp();
				log("<<< Compare file content");
				showMessage(lInfoTxt);
				checkToWriteLogFile();
			}
		};
		searchDublicateThread.start();
	}

	public void searchFiles() {
		try {
			initProgressBar(progressBar);
			if (jTabbedPanelForTables != null) {
				jTabbedPanelForTables.setSelectedIndex(0);
			}

			DIR_COUNTER = 0;
			FILE_COUNTER = 0;
			searchThread = new Thread() {
				@Override
				public void run() {
					log(getTimeStamp());
					initStringBuffer();
					if (!dirExist(txtSearchDir.getText())) {
						return;
					}

					log(">>>> Search files");
					log("Please waiting ....");
					logOnGUI("Please waiting ....");

					ProcessTimeController.start();
					clearRows(searchTableModel);
					clearRows(dublicateTableModel);

					// String lInfoTxt = "";

					ArrayList<EasyFileInfo> searchedFileArrayList = getFileArrayList(txtSearchDir.getText(), false);
					// Collections.sort(searchedFileArrayList, new FileSizeArraySorter());

					setTotalFoundEntries(searchedFileArrayList.size());
					// System.out.println("totalFileSize = " +totalFileSize);

					setNumberEntriesPerProcent();

					int lIndex = 1;
					File lPreviewDir = null;
					// File lCurrentDir;

					// int laufendNr = 1;

					progressBarValue = 0;
					EasyFileInfo f;
					// for (EasyFileInfo f : searchedFileArrayList) {
					for (int i = 0; i < searchedFileArrayList.size(); i++) {
						f = searchedFileArrayList.get(i);
						if (cbxResetCounter.isSelected()) {
							if (lPreviewDir != null) {
								if (f.getFile().getPath().compareTo(lPreviewDir.getPath()) != 0) {
									if (f.getFile().isDirectory()) {
										lIndex = 1;
									}
								}
							}
							if (f.getFile().isDirectory()) {
								lIndex = 1;
							}
						}

						lIndex++;

						if (f.getFile().isDirectory()) {
							DIR_COUNTER++;
						} else {
							// File
							FILE_COUNTER++;
						}

						lPreviewDir = f.getFile();

						String lText = "Process file:..." + f.getFile().getAbsoluteFile();

						setFileInfoText(lblFileInfo, lText);
						logOnTitle(lText);

						int currentProcentValue = getCurrentProcessedProcent((i + 1));
						if (currentProcentValue > progressBarValue && currentProcentValue % TEN == 0) {
							lblFileInfo.setText("Process: " + (i + 1) + " / " + TOTAL_FOUND_ENTRIES + " elapsed time "
									+ ProcessTimeController.getElapsedTime() + " [s]");
							progressBarValue = currentProcentValue;
						}
						// laufendNr++;

						try {
							addItemToRenameTable(searchTableModel, f.getFile(), lIndex);
						} catch (ArrayIndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Runnable runme3 = new Runnable() {
							@Override
							public void run() {
								// progressBar.setValue(counter);
								progressBar.setValue(progressBarValue);
								// progressBar.setToolTipText(fileName);
							}
						};
						SwingUtilities.invokeLater(runme3);
						// addItemToRenameTable(tableModelSearch, f.getAbsoluteFile(), lTargetFilename,
						// lIndex);

					}
					progressBarValue = (int) HUNDERD;
					progressBar.setValue(progressBarValue);

					// jTabbedPanelForTables.setSelectedIndex(0);
					if (jTabbedPanelForTables != null) {
						jTabbedPanelForTables.setSelectedIndex(0);
					}

					ProcessTimeController.end();
					// progress.stop();
					logTimeStamp();
					// checkButtonStatus();
					try {
						showResult();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			searchThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			Thread.currentThread().getStackTrace();
		}
	}

	private void searchMoreLessItemsForDir() {
		// TODO Auto-generated method stub
		int file_items = 20;
		int file_items_dialog = 20;
		if ((file_items_dialog = getMoreLessItems()) > 0) {
			file_items = file_items_dialog;
		}

		clearRows(searchTableModel);
		java.util.List<String> filelistSearch = new ArrayList<String>();

		try {
			filelistSearch = getFileListForDirectory(new File(txtSearchDir.getText()), cbxRecursiveMode.isSelected(),
					cbxIncludeFiles.isSelected(), cbxIncludeDirectories.isSelected(), getSearchLimit());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// list is some List of Strings, to remove duplicate entries
		Set<String> setList = new HashSet<String>(filelistSearch);
		List<String> list = new ArrayList<String>(setList);
		Collections.sort(list);
		int found_items = 0;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String curr_Filename = list.get(i);
				setTitle("Please waiting... Process files: " + curr_Filename);
				if (new File(curr_Filename).isDirectory()) {
					int findFiles = getFileNumberInDir(new File(curr_Filename));
					if (findFiles >= file_items) {
						File files[] = new File(curr_Filename).listFiles();
						String lInfo = "Current Dir: " + curr_Filename;
						log(lInfo);
						try {
							addItemToRenameTable(searchTableModel, new File(curr_Filename).getCanonicalPath(),
									new File(curr_Filename).getCanonicalPath(), 1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						for (int idx = 0; idx < files.length; idx++) {
							String lFilename = "";
							try {
								lFilename = files[idx].getCanonicalPath();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (files[idx].isFile()) {
								addItemToRenameTable(searchTableModel, lFilename, lFilename, 1);
								found_items++;
							}

						}
					}
				}
			}
		}
		setTitle("Process finished! - Found items: " + found_items);
		try {
			showResult();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selectAll() {
		// TODO Auto-generated method stub
		if (jTabbedPanelForTables.getSelectedIndex() == 0) {
			tableSearchResult.selectAll();
		}

		if (jTabbedPanelForTables.getSelectedIndex() == 1) {
			tableCompareResult.selectAll();
		}
	}

	private void selectFirstEntry() throws InterruptedException {
		ListSelectionModel selectionModel = null;

		int selTabIndx = jTabbedPanelForTables.getSelectedIndex();
		if (selTabIndx == 0 && tableSearchResult.getRowCount() > 0) {
			selectionModel = tableSearchResult.getSelectionModel();
		}

		if (selTabIndx == 1 && tableCompareResult.getRowCount() > 0) {
			selectionModel = tableCompareResult.getSelectionModel();
		}
		if (selectionModel != null) {
			selectionModel.setSelectionInterval(0, 0);
			showPreview(getSelectedFilename());
		}
	}

	private void setDateField(ObservingTextField pTextField, Date pDate) {
		DatePicker dp = new DatePicker(pTextField);
		Date selectedDate = null;
		dp.formatDate(selectedDate, DATEFORMATPATTERN);
		dp.setSelectedDate(selectedDate);
		pDate = selectedDate;
		dp.formatDate(pDate, DATEFORMATPATTERN);
		dp.start(pTextField);
	}

	protected void setFileInfoText(JLabel pLabel, String pText) {
		// TODO Auto-generated method stub
		if (cbxLoggingCommandLine.isSelected()) {
			pLabel.setText(pText);
			// System.out.println(pObject.toString());
		}
	}

	public void setLeftItems(int pLeftItems) {
	}

	public void setLeftOffset(int pLeftOffset) {
	}

	private void setMyIconImage() {
		// TODO Auto-generated method stub
		this.setIconImage(new ImageIcon(getClass().getResource("resources/logo_icon.png")).getImage());
	}

	public void setNumberEntriesPerProcent() {
		NUMBER_ENTRIES_PER_PORCENT = (double) TOTAL_FOUND_ENTRIES / (double) 100;
	}

	private void setPath(JTextField pTextField) {
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setFileSelectionMode(1);
		jfilechooser.setCurrentDirectory(new File(pTextField.getText()));
		jfilechooser.setAcceptAllFileFilterUsed(false);
		int i = jfilechooser.showOpenDialog(this);
		if (i == 0) {
			pTextField.setText(jfilechooser.getSelectedFile().getPath());
		}
	}

	public void setRightItems(int pRightItems) {
	}

	public void setRightOffset(int pRightOffset) {
	}

	public void setsDateFormt(SimpleDateFormat sDateFormt) {
		this.sDateFormt = sDateFormt;
	}

	public void setTotalFoundEntries(int size) {
		TOTAL_FOUND_ENTRIES = size;
	}

	private void showFile(String pImagefile) {
		if (isImageFile(pImagefile)) {
			try {
				new FullImageViewer(pImagefile, true);
				this.toBack();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else if (pImagefile.toLowerCase(LOCALE_TR).endsWith("mp3")) {
			// new EasyMP3Player(pImagefile, true);
		}
	}

	private void showMessage(String lInfoTxt) {
		JOptionPane.showMessageDialog(this, lInfoTxt);
	}

	private void showPreview(String pImageFile) throws InterruptedException {

		if (pImageFile.length() < 0 || new File(pImageFile) == null || !new File(pImageFile).exists()
				|| !(new File(pImageFile).canRead())) {
			return;
		}

		pnlFilePreview.removeAll();

		// ImageViewerPanel preview = null;
		pnlNoPreview = null;
		changeFileInfoText();
		if (isImageFile(pImageFile)) {
			// preview = new ImageViewerPanel(pImageFile, preview_width, preview_height);
			// pnlFilePreview.add(preview);
			ImageViewerPanel preview_new = null;
			preview_new = new ImageViewerPanel(new String[] { pImageFile, "C:\\Temp\\IMG_2726.JPG" }, preview_width,
					preview_height);
			pnlFilePreview.add(preview_new);
			getContentPane().add(pnlFilePreview);
		} else if (isTextFile(pImageFile)) {
			pnlNoPreview = getPanelTextFilePreview(pImageFile);
			pnlFilePreview.add(pnlNoPreview, java.awt.BorderLayout.CENTER);
			getContentPane().add(pnlFilePreview);

		} else {
			pnlNoPreview = getPanelNoPreview();
			pnlFilePreview.add(pnlNoPreview, java.awt.BorderLayout.CENTER);
			getContentPane().add(pnlFilePreview);
		}
		pnlFilePreview.validate();
		pnlFilePreview.repaint();
	}

	public void showProgressBar(JFrame pParentFrame, int pCounter) {
		final JDialog dlg = new JDialog(pParentFrame, "Progress Dialog", true);
		JProgressBar dpb = new JProgressBar(0, pCounter);
		dlg.add(BorderLayout.CENTER, dpb);
		dlg.add(BorderLayout.NORTH, new JLabel("Progress..."));
		dlg.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dlg.setSize(300, 75);
		dlg.setLocationRelativeTo(this);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				dlg.setVisible(true);
			}
		});
		t.start();
		for (int i = 0; i <= pCounter; i++) {
			lblFileInfo.setText("Count : " + i);
			dpb.setValue(i);
			if (dpb.getValue() == pCounter) {
				dlg.setVisible(false);
				// System.exit(0);
				dlg.dispose();
			}
			// TEST
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		dlg.setVisible(true);
	}

	private void showResult() throws InterruptedException {
		String lInfoTxt = "";

		if (searchTableModel == null) {
			return;
		}

		int foundentry = searchTableModel.getRowCount();

		if (foundentry == 0) {
			// clearRows();
			lInfoTxt = "No matched files found in directory " + txtSearchDir.getText() + "! Process time "
					+ ProcessTimeController.getProzessTime() + " [s]";
			// checkButtonStatus();
			pnlFilePreview.removeAll();
			pnlFilePreview.repaint();
		} else {
			selectFirstEntry();
			lInfoTxt = "Found " + FILE_COUNTER + " files and " + DIR_COUNTER + " directories. Process time "
					+ ProcessTimeController.getProzessTime() + " [s]";

			checkToWriteLogFile();
		}
		lblFileInfo.setText(lInfoTxt);
		logOnTitle(lInfoTxt);
		lblFileInfo.setBackground(Color.GREEN);
		log(lInfoTxt);
		showMessage(lInfoTxt);
		// fillFileExtentions();
		repaint();
	}

	public void showTextFile(String pTextFile) {
		new TextEditor(pTextFile);
		toBack();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		// AbstractButton aButton = (AbstractButton) event.getSource();
		// Object source = e.getSource();
		//
		// if(source == tableCompareResult){
		// if(tableCompareResult.getRowCount() > 0){
		// btnDeleteFiles.setEnabled(tableCompareResult.getRowCount() > 0);
		// }
		// }
	}

	private void storeProperties() {
		try {
			FileOutputStream fos;
			prop.setProperty(DEFAULT_SEARCH_DIR, txtSearchDir.getText());
			prop.setProperty(DEFAULT_COMPARE_DIR, txtCompareDir.getText());
			prop.store((fos = new FileOutputStream(PROPS_FILE)),
					DEFAULT_SEARCH_DIR + ":" + prop.getProperty(DEFAULT_SEARCH_DIR) + "");
			fos.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		// Object source = e.getSource();

		// if(source == tableCompareResult.getModel()){
		// if(tableCompareResult.getRowCount() > 0){
		// btnDeleteFiles.setEnabled(tableSearchResult.getRowCount() > 0);
		// }
		// }
		//
		// if(source == tableSearchResult.getModel()){
		// if(tableSearchResult.getRowCount() > 0){
		// btnDeleteDuplicateFilesLeft.setEnabled(tableCompareResult.getRowCount()
		// > 0);
		// btnDeleteDuplicateFilesRight.setEnabled(tableCompareResult.getRowCount()
		// > 0);
		// }
		// }

	}

	private void viewLogFiles() {
		new FileDeleterGUI(getLogDir(LOG_DIR), new String[] { "No", "Logfiles", "Modified", "Size [KB]" });
	}

	public void walkDirectory(String pPath, boolean pRecursiveMode, boolean pIncludeFiles, boolean pIncludeDirectories,
			int pSearchLimit, List<String> pFileLists) throws IOException {

		File root = new File(pPath);
		File[] list = root.listFiles();

		if (isSearchLimitReached(pSearchLimit)) {
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

				if (isSearchLimitReached(pSearchLimit)) {
					return;
				}

			} else {
				// System.out.println( "File:" + f.getAbsoluteFile() );
				if (pIncludeFiles) {
					pFileLists.add(f.getCanonicalPath());
				}
				if (isSearchLimitReached(pSearchLimit)) {
					return;
				}
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// loadPropertiesFile(PROPS_FILE);
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// saveProps();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		storeProperties();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// log("windowOpened");
		determinePreferedSizes();
		// loadPropertiesFile(PROPS_FILE);
		btnSearchFiles.requestFocus();
		setMyIconImage();
	}

	private boolean WriteAlbumListToXMLFile(JTable pTable, String pXMLFile) {
		// TODO Auto-generated method stub
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
			String artist_value;
			String album_value;
			String title_value;

			String artist_value_tmp = "";
			String album_value_tmp = "";
			String title_value_tmp = "";
			Element artist = null;
			Element album = null;
			Element title = null;

			// /EASY
			int total_Tbl_Items = pTable.getRowCount();
			for (int i = 0; i < total_Tbl_Items; i++) {
				String filename = (String) pTable.getValueAt(i, 1);
				String[] piece = filename.trim().split("[\\\\]");
				artist_value = piece[piece.length - 3];
				album_value = piece[piece.length - 2];
				title_value = piece[piece.length - 1];

				if (artist_value.compareToIgnoreCase(artist_value_tmp) != 0) {
					artist = doc.createElement("artist");
					artist.appendChild(doc.createTextNode(artist_value));
					staff.appendChild(artist);
				}
				if (album_value.compareToIgnoreCase(album_value_tmp) != 0) {
					album = doc.createElement("album");
					album.appendChild(doc.createTextNode(album_value));
					artist.appendChild(album);
				}
				if (title_value.compareToIgnoreCase(title_value_tmp) != 0) {
					title = doc.createElement("title");
					title.appendChild(doc.createTextNode(title_value));
					album.appendChild(title);
				}
				artist_value_tmp = artist_value;
				album_value_tmp = album_value;
				title_value_tmp = title_value;
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(pXMLFile));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			transformer.transform(source, result);

			log("File saved in path: " + pXMLFile);
			return true;

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
			return false;
		}
	}

	private void writeCSVFile(String pCSVFile, JTable pTable, int pStartInd, int pEndInd) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pCSVFile), "UTF-8"));
			int total_Tbl_Items = pTable.getRowCount();
			for (int i = pStartInd; i < pEndInd; i++) {
				if (i < total_Tbl_Items) {
					String filename = (String) tableSearchResult.getValueAt(i, 1);
					String[] piece = filename.trim().split("[\\\\]");
					out.write(piece[piece.length - 3] + ";" + piece[piece.length - 2] + ";" + piece[piece.length - 1]
							+ System.getProperty("line.separator"));
					log("GetValueAt : " + (String) tableSearchResult.getValueAt(i, 1));
				} else {
					break;
				}
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}