package eu.ismailozer.easyfilemanager;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		comp.replaceSelection(null);
	}

	@Override
	public boolean isEnabled() {
		return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
	}
}