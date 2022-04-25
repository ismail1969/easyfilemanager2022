package eu.ismailozer.easyfilemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageViewerPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6637347323459632612L;
	Image resizedImage;
	private Image loupe_img = null;
	// String imageFile;
	int width;
	int height;

	private int src_image_w;
	private int src_image_h;

	private String[] filenames;

	private int position = 0;

	JPanel pnlImage;
	JPanel pnlNextPreview;
	JButton btnPreview;
	JButton btnNext;
	// public ImageViewerPanel_New(String pImageFile, int pFitWidth, int
	// pFitHeight)
	// throws InterruptedException {
	// super();
	// imageFile = pImageFile;
	// width = pFitWidth;
	// height = pFitHeight;
	// scaleImageSize();
	// addMouseListener(this);
	// addMouseMotionListener(this);
	// }

	public ImageViewerPanel(String[] pFilenames, int pFitWidth, int pFitHeight) throws InterruptedException {
		super();

		setLayout(new BorderLayout(0, 0));

		pnlImage = new JPanel();
		add(pnlImage, BorderLayout.CENTER);

		pnlNextPreview = new JPanel();
		add(pnlNextPreview, BorderLayout.SOUTH);
		pnlNextPreview.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnPreview = new JButton("Preview");
		btnPreview.addActionListener(this);
		pnlNextPreview.add(btnPreview);

		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		pnlNextPreview.add(btnNext);

		pnlNextPreview.setOpaque(false);

		filenames = pFilenames;
		width = pFitWidth;
		height = pFitHeight;
		scaleImageSize();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	// public ImageViewerPanel_New(String pImageFile) throws
	// InterruptedException {
	// // Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	// // int toolkit_w = d.width;
	// // int toolkit_h = d.height;
	// new ImageViewerPanel_New(pImageFile, getToolkitDimension().width,
	// getToolkitDimension().height);
	// }

	// public ImageViewerPanel_New() throws InterruptedException {
	// // Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	// // int toolkit_w = d.width;
	// // int toolkit_h = d.height;
	// new ImageViewerPanel_New(filenames[position],
	// getToolkitDimension().width,
	// getToolkitDimension().height);
	// }

	public Dimension getToolkitDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public void scaleImageSize() {
		int display_w = width;
		int display_h = height;
		Image source_img = null;

		loupe_img = new ImageIcon(getClass().getResource("resources/loupe.png")).getImage();

		// try {
		// loupe_img = ImageIO.read(new File(this.getClass()
		// .getResource("resources/loupe.png").getFile()));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		try {
			source_img = new ImageIcon(ImageIO.read(new File(filenames[position]))).getImage();
			// System.out.println("test");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print("Msg: " + e.getMessage());
			System.out.print("imageFile: " + filenames[position]);
		}
		if (source_img == null) {
			return;
		}
		// int image_w = source_img.getWidth(null);
		// int image_h = source_img.getHeight(null);

		src_image_w = source_img.getWidth(null);
		src_image_h = source_img.getHeight(null);

		double width_rate = (double) src_image_w / display_w;
		double height_rate = (double) src_image_h / display_h;

		double max_rate = Math.max(width_rate, height_rate);

		int scaled_width;
		int scaled_height;

		if (max_rate > 1) {
			scaled_width = (int) ((double) src_image_w / max_rate);
			scaled_height = (int) ((double) src_image_h / max_rate);
			resizedImage = source_img.getScaledInstance(scaled_width, scaled_height, Image.SCALE_SMOOTH);

		} else {
			resizedImage = source_img;
			scaled_width = src_image_w;
			scaled_height = src_image_h;
		}

		if (scaled_width < display_w || scaled_height < display_h) {
			setBounds((display_w - scaled_width) / 2, (display_h - scaled_height) / 2, scaled_width, scaled_height);
		}

		setPreferredSize(new Dimension(scaled_width, scaled_height));
	}

	public void paint(Graphics g) {
		g.drawImage(resizedImage, 0, 0, null);
		// draw the image dimensions
		// String dim_Text = src_image_w + " x " + src_image_h;
		String dim_Text = filenames[position] + " (" + src_image_w + " x " + src_image_h + ")";
		g.setFont(new Font("Serif", Font.BOLD, 16));
		// g.setColor(Color.black);
		// g.drawString(dim_Text, 10, 15);
		g.setColor(Color.white);
		g.drawString(dim_Text, 10, 20);

		// g.drawString(imageFile, this.height-20 ,/*this.width*/ 20);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 1) {
			try {
				new FullImageViewer(filenames[position], true).toFront();

				// new EasyImageViewer(imageFile, true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.getGraphics().drawImage(loupe_img, getWidth() / 2, getHeight() / 2, this);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == btnNext) {
			showNextPicture();
			// searchNew();
		}

		else if (source == btnPreview) {
			showPreviewPicture();
			// searchNew();
		}
		validate();
		repaint();

	}

	private void showPreviewPicture() {
		// TODO Auto-generated method stub
		position--; // Decrement the index position of the array of filenames by
		// one on buttonPressed
		if (!btnNext.isEnabled()) { // if NextButton is
			btnNext.setEnabled(true); // disabled, enable it
		}
		if (position == 0) { // If we are viewing the first Picture in
			btnPreview.setEnabled(false); // the directory, disable previous
											// button
		}
		repaint();
	}

	private void showNextPicture() {

		position++; // Increment the index position of array of filenames by one
		// on buttonPressed

		if (!btnPreview.isEnabled()) {
			// listFiles(Path);
			btnPreview.setEnabled(true);
		}
		if (position == filenames.length) {
			btnNext.setEnabled(false);
			position--;
			repaint();
			return;
		}

	}
}