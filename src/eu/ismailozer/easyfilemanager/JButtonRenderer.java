package eu.ismailozer.easyfilemanager;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class JButtonRenderer implements TableCellRenderer {

	JButton button = new JButton();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		// table.setGridColor(Color.RED);
		button.setText(value.toString() + row + ": " + column);
		button.setToolTipText("Press " + value.toString());
		return button;
	}
}