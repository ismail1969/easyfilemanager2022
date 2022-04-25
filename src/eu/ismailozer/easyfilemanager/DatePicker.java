package eu.ismailozer.easyfilemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

@SuppressWarnings({ "unused", "deprecation" })
public class DatePicker extends Observable implements Runnable, WindowFocusListener {
	public static class DayLabel extends JLabel implements MouseInputListener, MouseMotionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3223641721847494457L;
		private DatePicker parent;

		private Border oldBorder;

		public DayLabel(DatePicker parent, int day) {
			super(Integer.toString(day));
			this.parent = parent;
			setHorizontalAlignment(SwingConstants.CENTER);
			setFont(plain);
			this.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// JOptionPane.showMessageDialog(this,getText());
			parent.dayPicked(Integer.parseInt(getText()));
		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			oldBorder = this.getBorder();
			Border b = BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED);
			b = BorderFactory.createEtchedBorder();
			this.setBorder(b);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			this.setBorder(oldBorder);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		public void setCurrentDayStyle() {
			setFont(bold);
			setForeground(Color.RED);
		}

		public void setSelectedDayStyle() {
			setFont(plain);
			setForeground(Color.BLUE);
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}

		public void setWeekendStyle() {
			setFont(plain);
			setForeground(Color.GRAY);
		}

	}

	public static class MonthPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3868565515839397932L;
		private DatePicker parent;

		public MonthPanel(DatePicker parent, Calendar c) {
			this.parent = parent;
			GridLayout g = new GridLayout();
			g.setColumns(7);
			g.setRows(0);
			this.setLayout(g);

			for (int i = 0; i < 7; i++) {
				JLabel wd = new JLabel(parent.getString("week." + i, ""));
				wd.setHorizontalAlignment(SwingConstants.CENTER);
				if (i == 0)
					wd.setForeground(Color.RED);
				else if (i == 6)
					wd.setForeground(Color.gray);
				this.add(wd);
			}

			setDaysOfMonth(c);
			// this.setPreferredSize(new Dimension(200, 120));
			this.setPreferredSize(new Dimension(250, 140));

		}

		private void setDaysOfMonth(Calendar c) {
			Calendar curr = new GregorianCalendar();
			int currdate = curr.get(Calendar.DAY_OF_MONTH);
			int currmon = curr.get(Calendar.MONTH);
			int curryear = curr.get(Calendar.YEAR);

			int seldate = -1;
			int selmon = -1;
			int selyear = -1;
			if (parent.selectedDate != null) {
				seldate = parent.selectedDate.get(Calendar.DAY_OF_MONTH);
				selmon = parent.selectedDate.get(Calendar.MONTH);
				selyear = parent.selectedDate.get(Calendar.YEAR);
			}

			int date = c.get(Calendar.DAY_OF_MONTH);
			int mon = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);
			int day = c.get(Calendar.DAY_OF_WEEK);
			int start = (7 - (date - day) % 7) % 7;
			int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

			for (int i = 0; i < start; i++) {
				JLabel lbl = new JLabel("");
				add(lbl);
			}
			int pos = start;
			for (int i = 1; i <= days; i++) {
				pos++;
				DayLabel lbl = new DayLabel(parent, i);
				if (seldate == i && selmon == mon && selyear == year)
					lbl.setSelectedDayStyle();
				if (currdate == i && currmon == mon && curryear == year)
					lbl.setCurrentDayStyle();
				if (pos % 7 == 0 || pos % 7 == 1)
					lbl.setWeekendStyle();
				add(lbl);

			}
		}
	}

	public static class NavigatePanel extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4180438899094449476L;

		private DatePicker parent;

		private JButton premon;

		private JButton preyear;

		private JButton nextmon;

		private JButton nextyear;

		private JLabel lbl;

		// private byte[] getImage(String fileName) {
		// InputStream is = null;
		//
		// try {
		// is = new BufferedInputStream(DatePicker.class.getClassLoader()
		// .getResourceAsStream(fileName));
		// byte[] b = new byte[is.available()];
		// is.read(b);
		// return b;
		// } catch (IOException e) {
		// e.printStackTrace();
		// return null;
		// } finally {
		// try {
		// is.close();
		// } catch (IOException e) {
		// }
		// }
		// }

		private JComboBox<String> monthBox;

		private JComboBox<Integer> yearBox;
		private String[] months;
		private Integer[] years;
		private Box box;
		// final int height=10;
		final int height = 10;

		public NavigatePanel(DatePicker parent) {
			this.parent = parent;
			setLayout(new BorderLayout());
			// Dimension d = new Dimension(50, 50);
			Dimension d = new Dimension(40, 40);
			Box box = new Box(BoxLayout.X_AXIS);
			preyear = new JButton("<<");
			// preyear.setToolTipText(parent.getString("pre.year",
			// "Previous year."));
			// ImageIcon icon = new ImageIcon(getImage("preyear.gif"), "<<");
			// preyear.setIcon(icon);
			preyear.addActionListener(this);
			preyear.setPreferredSize(d);
			box.add(preyear);

			// box.add(Box.createHorizontalStrut(3));
			// box.add(Box.createHorizontalStrut(2));

			premon = new JButton(" < ");
			// premon.setBounds(new Rectangle(10,10));
			// premon
			// .setToolTipText(parent.getString("pre.mon",
			// "Previous Month"));
			// icon = new ImageIcon(getImage("premon.gif"), "<");
			// premon.setIcon(icon);
			premon.addActionListener(this);
			premon.setPreferredSize(d);
			box.add(premon);

			add(box, BorderLayout.WEST);

			box = new Box(BoxLayout.X_AXIS);
			nextmon = new JButton(">");
			// nextmon.setSize(60, 22);
			// nextmon.setToolTipText(parent.getString("next.mon",
			// "Next month."));
			// icon = new ImageIcon(getImage("nextmon.gif"), ">");
			// nextmon.setIcon(icon);
			nextmon.setPreferredSize(d);
			nextmon.addActionListener(this);
			box.add(nextmon);

			// box.add(Box.createHorizontalStrut(3));
			// box.add(Box.createHorizontalStrut(5));

			nextyear = new JButton(">>");
			// nextyear
			// .setToolTipText(parent.getString("next.year", "Next year."));
			// icon = new ImageIcon(getImage("nextyear.gif"), ">>");
			// nextyear.setIcon(icon);
			nextyear.setPreferredSize(d);
			nextyear.addActionListener(this);
			box.add(nextyear);

			add(box, BorderLayout.EAST);
			setCurrentMonth(parent.calendar);
			// setLabel(parent.calendar);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			Calendar c = new GregorianCalendar();
			c.setTime(parent.getCalendar().getTime());
			if (src instanceof JButton) {
				if (e.getSource() == premon)
					c.add(Calendar.MONTH, -1);
				else if (e.getSource() == nextmon)
					c.add(Calendar.MONTH, 1);
				else if (e.getSource() == nextyear)
					c.add(Calendar.YEAR, 1);
				if (e.getSource() == preyear)
					c.add(Calendar.YEAR, -1);
				// System.out.println(c.getTime());
				parent.updateScreen(c);
			} else if (src instanceof JComboBox) {
				@SuppressWarnings("unchecked")
				JComboBox<Object> jcb = (JComboBox<Object>) src;
				if (src == monthBox) {
					c.set(Calendar.MONTH, jcb.getSelectedIndex());
				} else if (e.getSource() == yearBox) {
					c.set(Calendar.YEAR, years[jcb.getSelectedIndex()].intValue());
					setYearComboBox(c);
				}
				parent.setMonthPanel(c);
				parent.screen.pack();
			}
		}

		public void setCurrentMonth(Calendar c) {
			setMonthComboBox(c);
			setYearComboBox(c);

			if (box == null) {
				box = new Box(BoxLayout.X_AXIS);
				box.add(monthBox);
				box.add(yearBox);
				add(box, BorderLayout.CENTER);
			}

		}

		public void setLabel(Calendar c) {
			if (lbl != null)
				remove(lbl);
			lbl = new JLabel(parent.getString("month." + c.get(Calendar.MONTH), "") + ", " + c.get(Calendar.YEAR));
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			add(lbl, BorderLayout.CENTER);

		}

		private void setMonthComboBox(Calendar c) {
			if (months == null) {
				months = new String[12];
				// for(int i=0;i<12;i++)
				// {
				// String m=parent.getString("month."+i,"");
				// months[i]=m;
				// }
				months[0] = "January";
				months[1] = "February";
				months[2] = "March";
				months[3] = "April";
				months[4] = "May";
				months[5] = "June";
				months[6] = "July";
				months[7] = "August";
				months[8] = "September";
				months[9] = "October";
				months[10] = "November";
				months[11] = "December";
			}
			if (monthBox == null) {
				monthBox = new JComboBox<>(months);
				monthBox.addActionListener(this);
				monthBox.setFont(DatePicker.plain);
				monthBox.setSize(monthBox.getWidth(), height);
				monthBox.setPreferredSize(new Dimension(monthBox.getWidth(), height));
			}
			monthBox.setModel(new DefaultComboBoxModel<>(months));
			monthBox.setSelectedIndex(c.get(Calendar.MONTH));
		}

		private void setYearComboBox(Calendar c) {
			int y = c.get(Calendar.YEAR);
			years = new Integer[7];
			for (int i = y - 3, j = 0; i <= y + 3; i++, j++) {
				years[j] = Integer.valueOf(i);
			}
			if (yearBox == null) {
				yearBox = new JComboBox<>(years);
				yearBox.addActionListener(this);
				yearBox.setFont(DatePicker.plain);
				yearBox.setSize(yearBox.getWidth(), height);
				yearBox.setPreferredSize(new Dimension(yearBox.getWidth(), height));
			}
			yearBox.setModel(new DefaultComboBoxModel<>(years));
			yearBox.setSelectedItem(years[3]);
		}

	}

	protected static Font plain = new Font("Arial", Font.PLAIN, 10);

	protected static Font bold = new Font("Arial", Font.BOLD, 10);

	public static void main(String[] argv) {
		DatePicker dp = new DatePicker(null);

		dp.start(null);
	}

	private MonthPanel monthPanel;

	private NavigatePanel navPanel;

	protected Calendar calendar;

	private Calendar selectedDate;

	private boolean closeOnSelect = true;

	// private Locale locale = Locale.US;
	private Locale locale = Locale.getDefault();
	// private Locale locale = Locale.DE;
	// private Locale locale = new Locale("de", "DE");

	private DateFormat sdf;

	private JDialog screen;

	private ResourceBundle i18n;

	public DatePicker(Observer observer) {
		this(observer, new Date());
	}

	public DatePicker(Observer observer, Date selecteddate) {
		// this(observer, selecteddate, Locale.US);
		// this(observer, selecteddate, Locale.getDefault());
		// this(observer, selecteddate, Locale.getDefault());
		this(observer, selecteddate, Locale.getDefault());
	}

	public DatePicker(Observer observer, Date selecteddate, Locale locale) {
		super();
		this.locale = locale;
		register(observer);
		screen = new JDialog();
		screen.addWindowFocusListener(this);
		// screen.setSize(200, 200);
		screen.setSize(400, 400);
		screen.setResizable(true);
		screen.setModal(true);
		screen.setUndecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		screen.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		screen.getContentPane().setLayout(new BorderLayout());
		//
		calendar = new GregorianCalendar();

		setSelectedDate(selecteddate);
		Calendar c = calendar;
		if (selectedDate != null)
			c = selectedDate;
		updateScreen(c);
		screen.getContentPane().add(navPanel, BorderLayout.NORTH);

		screen.setTitle(getString("program.title", "Date Picker"));
	}

	public DatePicker(Observer observer, Locale locale) {
		this(observer, new Date(), locale);
	}

	protected void dayPicked(int day) {
		// this.day = day;
		calendar.set(Calendar.DAY_OF_MONTH, day);
		setSelectedDate(calendar.getTime());
		this.setChanged();
		this.notifyObservers(selectedDate);
		// System.out.println("cnt="+observable.countObservers()+", day
		// picked="+calendar.getTime());
		if (closeOnSelect) {
			screen.dispose();
			screen.setVisible(false);
		}
	}

	public String formatDate(Calendar date) {
		if (date == null)
			return "";
		return formatDate(date.getTime());
	}

	public String formatDate(Calendar date, String pattern) {
		if (date == null)
			return "";
		return new SimpleDateFormat(pattern).format(date.getTime());
	}

	public String formatDate(Date date) {
		if (date == null)
			return "";
		if (sdf == null) {
			// sdf = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,
			// locale);
			//
			sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

			sdf = new SimpleDateFormat("dd.MM.yyyy");

		}
		return sdf.format(date);
	}

	public String formatDate(Date date, String pattern) {
		if (date == null)
			return "";
		return new SimpleDateFormat(pattern).format(date);
	}

	public String formatDateNew(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf_new = new SimpleDateFormat("dd.MM.yyyy");
		return sdf_new.format(date);
	}

	private Calendar getCalendar() {
		return calendar;
	}

	public Locale getLocale() {
		return this.locale == null ? Locale.getDefault() : locale;
		// return this.locale == null ? Locale.getDefault() : locale;
		// return Locale.getDefault();
	}

	public JDialog getScreen() {
		return this.screen;
	}

	public String getString(String key, String dv) {
		// String baseName = "resources.i18n";
		//
		// if (i18n == null || getLocale().getDefault() != i18n.getLocale()) {
		// try {
		// i18n = ResourceBundle.getBundle(baseName, getLocale());
		// } catch (java.util.MissingResourceException e) {
		// // TODO: handle exception
		// }
		// }
		// String val = i18n.getString(key);
		// if (val == null)
		// return dv;
		// else
		// return val;
		return dv;
	}

	public boolean isCloseOnSelect() {
		return closeOnSelect;
	}

	public Date parseDate(String date) {
		if (sdf == null)
			// EASY
			sdf = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public void register(Observer observer) {
		if (observer != null)
			this.addObserver(observer);
	}

	@Override
	public void run() {
		screen.pack();

		screen.setVisible(true);

	}

	private void setCalendar(Calendar c) {
		this.calendar = c;
	}

	public void setCloseOnSelect(boolean closeOnSelect) {
		this.closeOnSelect = closeOnSelect;
	}

	public void setLocale(Locale l) {
		this.locale = l;
	}

	protected void setMonthPanel(Calendar calendar) {
		if (calendar != null)
			this.calendar.setTime(calendar.getTime());
		if (monthPanel != null)
			screen.getContentPane().remove(monthPanel);
		monthPanel = new MonthPanel(this, calendar);
		screen.getContentPane().add(monthPanel, BorderLayout.CENTER);
	}

	public void setSelectedDate(Date d) {
		if (d != null) {
			if (selectedDate == null)
				selectedDate = new GregorianCalendar();
			this.selectedDate.setTime(d);
			updateScreen(selectedDate);
		}
	}

	public void start(Component c) {
		if (c != null) {
			Component p = c.getParent();
			int x = c.getX() + c.getWidth(), y = c.getY() + c.getHeight();
			while (p != null) {
				x += p.getX();
				y += p.getY();
				p = p.getParent();
			}
			// System.out.println("x="+x+ " y="+y);
			screen.setLocation(x + 50, y + 50);
		} else {
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			screen.setLocation((int) (dim.getWidth() - screen.getWidth()) / 2,
					(int) (dim.getHeight() - screen.getHeight()) / 2);
		}
		SwingUtilities.invokeLater(this);
	}

	public void unregister(Observer observer) {
		if (observer != null)
			this.deleteObserver(observer);
	}

	protected void updateScreen(Calendar c) {
		if (navPanel == null)
			navPanel = new NavigatePanel(this);
		// navPanel.setLabel(c);
		navPanel.setCurrentMonth(c);
		setMonthPanel(c);
		screen.pack();
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		screen.toFront();
	}

}
