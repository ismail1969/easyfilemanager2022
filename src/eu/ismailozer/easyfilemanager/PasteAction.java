package eu.ismailozer.easyfilemanager;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

class PasteAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8110028717382776237L;
	JTextComponent comp;

	public PasteAction(JTextComponent comp) {
		super("Paste");
		this.comp = comp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		comp.paste();
	}

	@Override
	public boolean isEnabled() {
		if (comp.isEditable() && comp.isEnabled()) {
			Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
			return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		} else
			return false;
	}
}