package eu.ismailozer.easyfilemanager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

class JButtonEditor extends AbstractCellEditor implements TableCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7756384489136658497L;
	JButton button;
	String txt;

	public JButtonEditor() {
		super();
		button = new JButton("TEST");
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Buttontext: " + button.getText());
			}
		});
	}

	public Object getCellEditorValue() {
		return null;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	public void cancelCellEditing() {
	}

	public void addCellEditorListener(CellEditorListener l) {
	}

	public void removeCellEditorListener(CellEditorListener l) {
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		txt = (value == null) ? "xxx" : value.toString();
		button.setText(txt);
		return button;
	}

//	@Override
//	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}