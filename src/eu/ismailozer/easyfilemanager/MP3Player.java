package eu.ismailozer.easyfilemanager;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.advanced.AdvancedPlayer;

public class MP3Player extends JFrame implements KeyListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private AdvancedPlayer player;
	private JFileChooser fc = new JFileChooser();
	private JTextField fileField;

	public MP3Player(String pMP3File, boolean pAutoStart) {
		super("Easy Simple MP3 Player" + " - " + pMP3File);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLocationByPlatform(true);

		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".mp3");
			}

			@Override
			public String getDescription() {
				return "MP3";
			}
		});

		// JButton btnPlay, btnStop, btnOpen;

		this.setLayout(new FlowLayout());
		// this.add(btnOpen = new JButton(new OpenAction()));
		this.add(fileField = new JTextField(20));
		fileField.setText(pMP3File);
		// this.add(btnPlay = new JButton(new PlayAction()));
		// this.add(btnStop = new JButton(new StopAction()));
		// btnStop.addKeyListener(this);
		addKeyListener(this);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// if (!(player == null)) {
				// player.close();
				// }
				dispose();
			}
		});

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// dispose();
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});

		this.pack();
		this.setVisible(true);

		if (pMP3File.length() > 0 && pAutoStart) {
			new Thread() {
				public void run() {
					try {
						initializePlayer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// try {
					// if (player == null) {
					// initializePlayer();
					// }
					// player.play();
					// } catch (JavaLayerException e) {
					// e.printStackTrace();
					// }
				}
			}.start();
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		// EasyMP3Player mp3player;
		new MP3Player("C:\\Temp\\test.mp3", true);
	}

	@SuppressWarnings("finally")
	protected boolean initializePlayer() throws Exception {
		// boolean fileExists = false;

		try {
			if (fileField.getText() == null || fileField.getText() == "")
				throw new Exception();
			// Play player;
			// FileInputStream fin = new FileInputStream(fileField.getText());
			// if(player == null){
			// player = new AdvancedPlayer(new BufferedInputStream(fin));
			// player.play();
			// }
			// player.play();
			return true;
			// fileExists = true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			return false;
		}
	}

	class PlayAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.NAME, "Play");
			putValue(Action.LARGE_ICON_KEY, large_play);
			putValue(Action.ACCELERATOR_KEY, KeyEvent.VK_P);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				if (initializePlayer() == false) {
					JOptionPane.showMessageDialog(MP3Player.this, "Bitte wählen Sie eine MP3 Datei aus!",
							"Fehlermeldung", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			new Thread() {
				public void run() {
					// try {
					// player.play();
					// } catch (JarException e) {
					// e.printStackTrace();
					// }
				}
			}.start();
		}
	}

	class StopAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.NAME, "Stop");
			putValue(Action.LARGE_ICON_KEY, large_stop);
		}

		public void actionPerformed(ActionEvent e) {
			// player.close();
		}
	}

	class OpenAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.NAME, "Open");
			putValue(Action.LARGE_ICON_KEY, large_open);
		}

		public void actionPerformed(ActionEvent e) {

			int returnVal = fc.showOpenDialog(MP3Player.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				fileField.setText(file.getAbsolutePath());
			}

		}
	}

	Icon large_play = createImageIcon("resources/player_play.png");
	Icon large_stop = createImageIcon("resources/player_stop.png");
	Icon large_open = createImageIcon("resources/player_eject.png");

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Datei konnte nicht gefunden werden: " + path);
			return null;
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_ESCAPE
				|| event.getKeyCode() == KeyEvent.VK_SPACE) {
			// if (!(player == null)) {
			// player.close();
			// }
			dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		// Object source = event.getSource();
		// if (event.getKeyCode() == KeyEvent.VK_P) {
		// // player.stop();
		// try {
		// player.play();
		// } catch (JavaLayerException e) {
		//
		// e.printStackTrace();
		// }
		// }
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (event.getKeyCode() == 80) {
			// try {
			// player.play();
			// } catch (JavaLayerException e) {
			// e.printStackTrace();
			// }
		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		System.out.println("actionPerformed source = " + source.toString());
	}
}