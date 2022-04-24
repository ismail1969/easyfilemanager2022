package eu.ismailozer.easyfilemanager;

public class SearcherTableModel extends javax.swing.table.DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Object[] TABLE_COLUMN_SEARCH = { "No", "Filename", "Rename File", "File Extention",
			"Last Modified", "Size [KB]", "Can Rename", "is Folder" };

	public static final Object[] TABLE_COLUMN_COMPARISON = { "No", "Left File", "Left File Extention",
			"Left Last Modified", "Left Size [KB]", "Right File", "Right File Extention", "Right Last Modified",
			"Right Size [KB]" };

	public SearcherTableModel(java.lang.Object[][] data, java.lang.Object[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		// return super.getColumnClass(columnIndex);

		switch (columnIndex) {
		case 0:
			return Integer.class; // NO
		case 4:
			return Integer.class; // Date
		case 5:
			return Integer.class; // Size
		case 6:
			return Boolean.class; // Can Rename
		case 7:
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
		case 2:
			return true;
		default:
			return false;
		}
	}
}