package eu.ismailozer.easyfilemanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ClientsTableRenderer extends JPanel implements TableCellRenderer {
	/**
		 * 
		 */
	private static final long serialVersionUID = 3462701498200678500L;

	@Override
	public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		setBackground(Color.WHITE);
		if (column < 5 && value != null) {
			JLabel label = new JLabel(value.toString());
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 9));
			panel.setBackground(Color.WHITE);
			panel.add(label);
			this.add(panel);
		} else {

			JButton button = new JButton(value.toString());
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Clicked !");
				}
				//
				// @Override
				// public void actionPerformed(ActionEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
			});
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));
			panel.setBackground(Color.WHITE);
			panel.add(button);
			this.add(panel);
		}

		return this;
	}
}