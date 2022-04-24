package eu.ismailozer.easyfilemanager;

import java.awt.event.*;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class FullImageViewer extends JFrame implements MouseListener, KeyListener, FocusListener, WindowFocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6637347323459632612L;
	Image resizedImage;
	int offset_w = 30;
	int offset_h = 20;
	String imageFile;
	int image_w;
	int image_h;

	public static void main(String[] args) {
		String s = (String) javax.swing.JOptionPane.showInputDialog(new JFrame("Directory"), "Open the file:\n",
				"Customized Dialog", javax.swing.JOptionPane.PLAIN_MESSAGE);

		String filename = "C:\\Temp\\testpics\\testpics01.jpg";
		// If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			filename = s;
		}

		try {
			new FullImageViewer(filename, true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public FullImageViewer(String pImageFile, boolean pHideWinFrame) throws InterruptedException {
		super();
		// setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		imageFile = pImageFile;
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		getContentPane().setLayout(null);

		// remove window frame
		if (pHideWinFrame) {
			setUndecorated(true);
		}

		Image source_img = null;
		try {
			source_img = new ImageIcon(ImageIO.read(new File(pImageFile))).getImage();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		int display_w = d.width;
		int display_h = d.height;

		image_w = source_img.getWidth(null);
		image_h = source_img.getHeight(null);

		double width_rate = (double) image_w / display_w;
		double height_rate = (double) image_h / display_h;

		double max_rate = Math.max(width_rate, height_rate);

		int scaled_width;
		int scaled_height;

		if (max_rate > 1) {
			scaled_width = (int) ((double) image_w / max_rate);
			scaled_height = (int) ((double) image_h / max_rate);
			resizedImage = source_img.getScaledInstance(scaled_width, scaled_height, Image.SCALE_SMOOTH);

		} else {
			resizedImage = source_img;
			scaled_width = image_w;
			scaled_height = image_h;
		}

		scaled_width = scaled_width - offset_w;
		scaled_height = scaled_height - offset_h;

		if (scaled_width <= display_w || scaled_height <= display_h) {
			setBounds((display_w - scaled_width) / 2, (display_h - scaled_height) / 2, scaled_width, scaled_height);
		}

		setPreferredSize(new Dimension(scaled_width, scaled_height));

		if (scaled_width < display_w || scaled_height < display_h) {
			setBounds((display_w - scaled_width) / 2, (display_h - scaled_height) / 2, scaled_width, scaled_height);
		}

		setPreferredSize(new Dimension(scaled_width, scaled_height));
		setLocationRelativeTo(null);
		setTitle("ImageViewer - " + pImageFile);
		addMouseListener(this);
		addKeyListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
		pack();
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawImage(resizedImage, 0, 0, null);

		String dim_Text = imageFile + "(" + image_w + " x " + image_h + ")";
		g.setFont(new Font("Serif", Font.BOLD, 16));
		// g.setColor(Color.black);
		// g.drawString(dim_Text, 10, 15);
		g.setColor(Color.white);
		g.drawString(dim_Text, 10, 20);

		// g.drawString(imageFile, image_h - 50 ,image_w- 50);
	}

	public void mouseClicked(MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1) {
			dispose();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.dispose();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// Object source = event.getSource();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		// Object source = event.getSource();
		if (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_UP
				|| event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ESCAPE
				|| event.getKeyCode() == KeyEvent.VK_DOWN) {
			this.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void focusGained(FocusEvent e) {
		// System.out.println("focus Gained");
	}

	@Override
	public void focusLost(FocusEvent e) {
		// this.dispose();
		// System.exit(NORMAL);
		// System.out.println("focus Lost");
		dispose();
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {

	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {

	}
}