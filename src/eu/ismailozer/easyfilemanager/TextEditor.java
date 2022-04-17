package eu.ismailozer.easyfilemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;

/**
 * A class illustrating running line number count on JTextPane. Nothing is
 * painted on the pane itself, but a separate JPanel handles painting the line
 * numbers.<br>
 * 
 * @author Daniel Sj�blom<br>
 *         Created on Mar 3, 2004<br>
 *         Copyright (c) 2004<br>
 * @version 1.0<br>
 */
public class TextEditor extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// for this simple experiment, we keep the pane + scrollpane as members.
	JTextPane pane;
	JScrollPane scrollPane;
	private JButton btnClose;

	public TextEditor(String pFilename) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("EasyEditor : " + pFilename);
		getContentPane().setLayout(new BorderLayout());
		EasyTextEditorPanel linePanel = new EasyTextEditorPanel(pFilename);
		getContentPane().add(linePanel, BorderLayout.WEST);
		getContentPane().add(linePanel.scrollPane, BorderLayout.CENTER);
		btnClose = new JButton();
		add(btnClose, BorderLayout.SOUTH);
		btnClose.setText("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		setPreferredSize(new Dimension(800, 600));
		setLocation(50, 50);
		pack();
		setSize(new Dimension(800, 600));
		setVisible(true);
	}

	// test main
	public static void main(String[] args) {
		new TextEditor("C:\\Temp\\my.log");
	}
}

class EasyTextEditorPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextPane pane;
	JScrollPane scrollPane;

	public EasyTextEditorPanel(String pFilename) {
		super();
		Font font = new Font("Helvetica", Font.BOLD, 12);
		setMinimumSize(new Dimension(30, 30));
		setPreferredSize(new Dimension(30, 30));
		setMinimumSize(new Dimension(30, 30));
		setBackground(Color.decode("#00008b"));
		setForeground(Color.white);
		setFont(font);

		pane = new JTextPane() {
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				super.paint(g);
				EasyTextEditorPanel.this.repaint();
			}
		};
		pane.setBackground(Color.black);
		pane.setForeground(Color.white);

		// pane.setBackground(Color.decode("#fffff0"));

		pane.setFont(font);
		scrollPane = new JScrollPane(pane);

		add(scrollPane);
		try {
			pane.read(new FileReader(pFilename), null);
		} catch (java.io.IOException ioe) {

		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		@SuppressWarnings("deprecation")
		int start = pane.viewToModel(scrollPane.getViewport().getViewPosition());
		@SuppressWarnings("deprecation")
		int end = pane.viewToModel(new Point(scrollPane.getViewport().getViewPosition().x + pane.getWidth(),
				scrollPane.getViewport().getViewPosition().y + pane.getHeight()));
		// translate offsets to lines
		Document doc = pane.getDocument();
		int startline = doc.getDefaultRootElement().getElementIndex(start);
		int endline = doc.getDefaultRootElement().getElementIndex(end);

		int fontHeight = g.getFontMetrics(pane.getFont()).getHeight();

		for (int line = startline, y = 0; line <= endline; line++, y += fontHeight) {
			g.drawString(Integer.toString(line), 0, y);
		}
	}
}