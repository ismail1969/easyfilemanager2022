package eu.ismailozer.easyfilemanager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ProgressViewer extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7847273033616107067L;
	JProgressBar aJProgressBar;
	JButton btnCancel;

	public ProgressViewer(String pTitle) {
		super(pTitle);
		// JFrame frame = new JFrame("Indeterminate");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		aJProgressBar = new JProgressBar(0, 100);
		aJProgressBar.setIgnoreRepaint(true);
		aJProgressBar.setIndeterminate(true);
		getContentPane().add(aJProgressBar, BorderLayout.NORTH);
		btnCancel = new JButton("Cancel");
		getContentPane().add(btnCancel, BorderLayout.SOUTH);
		// setSize(300, 100);
		setBounds(300, 300, 300, 200);
		setVisible(true);
	}

	public void setIntermediate(boolean pBool) {
		aJProgressBar.setIndeterminate(pBool);
		setVisible(true);
	}

	public void start() {
		// this.setVisible(true);
		// if(!aJProgressBar.isIndeterminate()){
		// aJProgressBar.setIndeterminate(true);
		// }
	}

	public void stop() {
		aJProgressBar.setIndeterminate(false);
		this.dispose();
	}

	public void setValues(/* int min, int max */) {
		aJProgressBar.setOrientation(SwingConstants.HORIZONTAL);
		aJProgressBar.setMinimum(0);
		aJProgressBar.setMaximum(100);
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		Object source = arg0.getSource();
		if (source == btnCancel) {
			this.dispose();
		}

	}

	// public void dosomething(int pMax){
	// int i=0;
	// while (i <=pMax){
	// System.out.println("Print line : "+i++);
	// }
	// }

	// public static void main(String args[]) throws Exception {
	// // EasyProgressViewer progress = new EasyProgressViewer(
	// // "Pleas waiting ....");
	// // progress.setVisible(true);
	// // progress.setIntermediate(true);
	// // progress.start();
	// // //progress.dosomething(100000000);
	// // progress.stop();
	// // //progress.setIntermediate(false);
	// // progress.dispose();
	// }

}