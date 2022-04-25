package eu.ismailozer.easyfilemanager;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

class CutAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4181506557099680096L;
	JTextComponent comp;

	public CutAction(JTextComponent comp) {
		super("Cut");
		this.comp = comp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		comp.cut();
	}

	@Override
	public boolean isEnabled() {
		return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
	}
}