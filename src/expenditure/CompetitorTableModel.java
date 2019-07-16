package expenditure;

import static expenditure.MainFrame.window;
import expenditure.Tender.Competitor;
import static expenditure.Tender.Competitor.H;
import static expenditure.TenderInfoTableModel.u;
import java.util.List;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import util.ResizableHeaderTableModel;

/** Το μοντέλο πίνακα με τα στοιχεία των ενδιαφερόμενων οικονομικών φορέων για το διαγωνισμό. */
class CompetitorTableModel extends ResizableHeaderTableModel {
	/** Ο διαγωνισμός. */
	private Tender tender;
	/** Δημιουργία του μοντέλου του πίνακα. */
	CompetitorTableModel() { super(new String[] { H[0], u(H[1]), u(H[2]), H[3] }); }
	@Override protected List<Competitor> get() { return tender != null ? tender.competitors : null; }
	@Override protected Competitor createNew() { return new Competitor(tender); }
	@Override protected void remove(int index) {
		Contractor contractor = tender.competitors.get(index).getContractor();
		if (isContractorDeleteAccepted(tender, contractor)) tender.competitors.remove(index);
		else showMessageDialog(window, "Δεν μπορείτε να διαγράψετε ενδιαφερόμενο που είναι ανάδοχος σε σύμβαση",
				"Αποτυχία διαγραφής ενδιαφερόμενου", ERROR_MESSAGE);
	}
	/** Θέτει τoν τρέχοντα διαγωνισμό.
	 * @param tender Ο τρέχοντας διαγωνισμός */
	void setSelectedTender(Tender tender) { this.tender = tender; fireTableDataChanged(); }
	/** Ο ενδιαφερόμενος οικονομικός φορέας μπορεί να διαγραφεί.
	 * Όταν γίνεται αίτημα διαγραφής ή αντικατάστασης ενός ενδιαφερόμενου οικονομικού φορέα σε έναν
	 * διαγωνισμό, πρέπει να εξασφαλιστεί ότι δεν είναι ανάδοχος σε καμία σύμβαση.
	 * @param tender Ο διαγωνισμός για τον οποίο ενδιαφέρεται ο οικονομικός φορέας
	 * @param contractor Ο ενδιαφερόμενος οικονομικός φορέας
	 * @return Ο ενδιαφερόμενος μπορεί να διαγραφεί */
	static boolean isContractorDeleteAccepted(Tender tender, Contractor contractor) {
		return contractor == null ||
				tender.parent.contracts.stream()
						.filter(i -> i.getTender() == tender)
						.noneMatch(i -> contractor.equals(i.getContractor()));
	}
}
