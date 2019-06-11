package expenditure;

import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import util.ResizableHeaderTableModel;

/** Το μοντέλο του πίνακα υλικών εργασιών. */
final class MaterialsTableModel extends ResizableHeaderTableModel<Material> implements ListSelectionListener {
	/** Συντόμευση στον πίνακα εργασιών. */
	final private JTable tblWorks;
	/** Συντόμευση στη λίστα υλικών της ενεργής εργασίας. */
	private List<Material> list;

	/** Δημιουργία του μοντέλου του πίνακα. */
	@SuppressWarnings("LeakingThisInConstructor")
	MaterialsTableModel(JTable tblWorks) {
		super(Material.H);
		this.tblWorks = tblWorks;
		tblWorks.getSelectionModel().addListSelectionListener(this);
	}

	@Override protected List<Material> get() { return list; }
	@Override protected Material createNew() { return new Material(); }

	// Όταν αλλάζει η επιλογή εργασίας στον πίνακα εργασιών, αλλάζουν τα περιεχόμενα του πίνακα υλικών
	@Override public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		int selection = tblWorks.getSelectedRow();
		if (selection == -1) list = null;
		else {
			List<Work> works = ((WorksTableModel) tblWorks.getModel()).get();
			list = selection < works.size() ? works.get(selection).materials : null;
		}
		fireTableDataChanged();
	}
}