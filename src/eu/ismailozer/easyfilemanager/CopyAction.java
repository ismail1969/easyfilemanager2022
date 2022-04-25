package eu.ismailozer.easyfilemanager;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

class CopyAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8187806765915745556L;
	JTextComponent comp;

	public CopyAction(JTextComponent comp) {
		super("Copy");
		this.comp = comp;
	}

	public void actionPerformed(ActionEvent e) {
		comp.copy();
	}

	public boolean isEnabled() {
		return comp.isEnabled() && comp.getSelectedText() != null;
	}
}