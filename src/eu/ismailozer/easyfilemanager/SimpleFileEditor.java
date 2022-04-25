package eu.ismailozer.easyfilemanager;

import java.awt.BorderLayout;
import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

public class SimpleFileEditor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) throws Exception {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new SimpleFileEditor("c:\\Temp\\file.xml"), BorderLayout.CENTER);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	public SimpleFileEditor(String pEditFile /* , int pWith, int pHeight */) {
		setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setVisible(true);
		textArea.setTabSize(20);
		textArea.setRows(20);

		try {
			textArea.append(getFileContent(pEditFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.CENTER);
		// scroll.setSize(400, 400);
		setSize(400, 400);
	}

	public String getFileContent(String pEditFile) throws IOException {
		// String filename = "c:\\Temp\\file.xml";

		String line;
		String UTF8Str;
		StringBuffer buffer = new StringBuffer();
		BufferedReader in2 = new BufferedReader(new FileReader(pEditFile));
		int lineCounter = 1;
		while ((line = in2.readLine()) != null) {
			UTF8Str = new String(line.getBytes(), "UTF-8");
			buffer.append(lineCounter + ". " + UTF8Str + System.getProperty("line.separator"));
			lineCounter++;
		}
		in2.close();
		return buffer.toString();
	}
}
