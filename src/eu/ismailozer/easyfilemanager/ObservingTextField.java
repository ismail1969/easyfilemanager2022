package eu.ismailozer.easyfilemanager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFormattedTextField;

@SuppressWarnings("deprecation")
class ObservingTextField extends JFormattedTextField implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ObservingTextField(DateFormat format) {
		super(format);
	}

	@Override
	public void update(Observable o, Object arg) {
		Calendar calendar = (Calendar) arg;
		DatePicker dp = (DatePicker) o;
		System.out.println("picked=" + dp.formatDate(calendar));
		setText(dp.formatDate(calendar));
	}
}