package eu.ismailozer.easyfilemanager;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

class DeleteAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5453378664517370192L;
	JTextComponent comp;

	public DeleteAction(JTextComponent comp) {
		super("Delete");
		this.comp = comp;
	}

	public void actionPerformed(ActionEvent e) {
		comp.replaceSelection(null);
	}

	public boolean isEnabled() {
		return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
	}
}