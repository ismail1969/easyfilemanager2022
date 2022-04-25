package eu.ismailozer.easyfilemanager;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

// @author Santhosh Kumar T - santhosh@in.fiorano.com 
public class MyEventQueue extends EventQueue {

	// public void postEvent(AWTEvent theEvent) {
	// System.out.println("Event Posted");
	// super.postEvent(theEvent);
	// }

	public MyEventQueue() {
		super();
	}

	protected void dispatchEvent(AWTEvent event)
			throws NullPointerException, java.lang.IndexOutOfBoundsException, java.lang.ArrayIndexOutOfBoundsException {
		if (event == null) {
			return;
		}

		if (event != null) {
			// System.out.println("EVENT == " + event.toString());
			super.dispatchEvent(event);

			// interested only in mouseevents
			if (!(event instanceof MouseEvent))
				return;

			MouseEvent me = (MouseEvent) event;

			// interested only in popuptriggers
			if (!me.isPopupTrigger())
				return;

			// me.getComponent(...) retunrs the heavy weight component on which
			// event occured
			Component comp = SwingUtilities.getDeepestComponentAt(me.getComponent(), me.getX(), me.getY());

			// interested only in textcomponents
			if (!(comp instanceof JTextComponent))
				return;

			// no popup shown by user code
			if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0)
				return;

			// create popup menu and show
			JTextComponent tc = (JTextComponent) comp;
			JPopupMenu menu = new JPopupMenu();
			menu.add(new CutAction(tc));
			menu.add(new CopyAction(tc));
			menu.add(new PasteAction(tc));
			menu.add(new DeleteAction(tc));
			menu.addSeparator();
			menu.add(new SelectAllAction(tc));

			Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
			menu.show(tc, pt.x, pt.y);
		}
	}
}

//// The above class is self-explanatory with comments.
////
//// The implementation of actions is here:
//
//// @author Santhosh Kumar T - santhosh@in.fiorano.com
//class CutAction extends AbstractAction {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 4181506557099680096L;
//	CutActionData data = new CutActionData();
//
//	public CutAction(JTextComponent comp) {
//		super("Cut");
//		this.data.comp = comp;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		data.comp.cut();
//	}
//
//	public boolean isEnabled() {
//		return data.comp.isEditable() && data.comp.isEnabled() && data.comp.getSelectedText() != null;
//	}
//}
//
//// @author Santhosh Kumar T - santhosh@in.fiorano.com
//class PasteAction extends AbstractAction {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -8110028717382776237L;
//	PasteActionData data = new PasteActionData();
//
//	public PasteAction(JTextComponent comp) {
//		super("Paste");
//		this.data.comp = comp;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		data.comp.paste();
//	}
//
//	public boolean isEnabled() {
//		if (data.comp.isEditable() && data.comp.isEnabled()) {
//			Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
//			return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
//		} else
//			return false;
//	}
//}
//
//// @author Santhosh Kumar T - santhosh@in.fiorano.com
//class DeleteAction extends AbstractAction {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5453378664517370192L;
//	DeleteActionData data = new DeleteActionData();
//
//	public DeleteAction(JTextComponent comp) {
//		super("Delete");
//		this.data.comp = comp;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		data.comp.replaceSelection(null);
//	}
//
//	public boolean isEnabled() {
//		return data.comp.isEditable() && data.comp.isEnabled() && data.comp.getSelectedText() != null;
//	}
//}
//
//// @author Santhosh Kumar T - santhosh@in.fiorano.com
//class CopyAction extends AbstractAction {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -8187806765915745556L;
//	CopyActionData data = new CopyActionData();
//
//	public CopyAction(JTextComponent comp) {
//		super("Copy");
//		this.data.comp = comp;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		data.comp.copy();
//	}
//
//	public boolean isEnabled() {
//		return data.comp.isEnabled() && data.comp.getSelectedText() != null;
//	}
//}
//
//// @author Santhosh Kumar T - santhosh@in.fiorano.com
//class SelectAllAction extends AbstractAction {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -250748785294996286L;
//	SelectAllActionData data = new SelectAllActionData();
//
//	public SelectAllAction(JTextComponent comp) {
//		super("Select All");
//		this.data.comp = comp;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		data.comp.selectAll();
//	}
//
//	public boolean isEnabled() {
//		return data.comp.isEnabled() && data.comp.getText().length() > 0;
//	}
//}
