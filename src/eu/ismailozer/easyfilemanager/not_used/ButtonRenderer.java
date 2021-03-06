package eu.ismailozer.easyfilemanager.not_used;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @version 1.0 11/09/98
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5369675441137283238L;

	public ButtonRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}