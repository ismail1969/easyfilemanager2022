package eu.ismailozer.easyfilemanager;

//import java.awt.Component;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class DuplicateTableModel extends javax.swing.table.DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// public static final Object [] TABLE_COLUMN_SEARCH = { "No", "Filename",
	// "Rename File", "File Extention",
	// "Last Modified", "Size [KB]", "Can Rename", "is Folder"};
	//

	public static final Object[] TABLE_COLUMN_COMPARISON = { "No", "Left File", "Left File Extention",
			"Left Last Modified", "Left Size [KB]", "Right File", "Right File Extention", "Right Last Modified",
			"Right Size [KB]", "isFolder", "Switch" };

	public DuplicateTableModel(java.lang.Object[][] data, java.lang.Object[] columnNames) {
		super(data, columnNames);
	}

	// public Class getColumnClass(int c) {
	// return getValueAt(0, c).getClass();
	// }
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		// return super.getColumnClass(columnIndex);
		switch (columnIndex) {
		case 0:
			return Integer.class; // NO
		case 4:
			return Integer.class; // NO

		case 8:
			return Integer.class; // NO
		case 9:
			return Boolean.class; // is Folder.
		default:
			return String.class; //
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		// return super.isCellEditable(row, column);

		switch (column) {
		case 1:
		case 5:
			return true;
		default:
			return false;
		}
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		return new ClientsTableRenderer();
	}
}