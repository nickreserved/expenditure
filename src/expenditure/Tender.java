package expenditure;

import static expenditure.CompetitorTableModel.isContractorDeleteAccepted;
import static expenditure.Expenditure.a;
import static expenditure.MainFrame.NOYES;
import static expenditure.MainFrame.window;
import java.util.ArrayList;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import util.ResizableTableModel;
import static util.ResizableTableModel.getDouble;
import static util.ResizableTableModel.getString;

/** Μια σύμβαση. */
final class Tender implements VariableSerializable, TableRecord {
	/** Το θέμα του διαγωνισμού. */
	private String title;
	/** Ο τύπος του διαγωνισμού. */
	private TenderType tenderType;
	/** Η διακήρυξη του διαγωνισμού. */
	private String tenderAnnouncement;
	/** Το ΑΔΑ της διακήρυξης του διαγωνισμού. */
	private String tenderAnnouncementId;
	/** Χρόνος διεξαγωγής διαγωνισμού.
	 * Αφορά την αποσφράγιση των δικαιολογητικών συμμετοχής, των τεχνικών
	 * προσφορών και αν πρόκειται για συνοπτικό διαγωνισμό, και των οικονομικών
	 * προσφορών (αν δεν τίθεται άλλη ημερομηνία). */
	private String tenderTime;
	/** Χρόνος αποσφράγισης οικονομικών προσφορών.
	 * Σε συνοπτικό διαγωνισμό, αν είναι κενό, η αποσφράγιση γίνεται σε μια
	 * συνεδρία. */
	private String tenderTimeEconomic;
	/** Κωδικοί κοινού λεξιλογίου δημόσιων συμβάσεων. */
	private String cpv;
	/** Συμπληρωματικοί κωδικοί κοινού λεξιλογίου δημόσιων συμβάσεων. */
	private String cpvAux;
	/** Διαγωνιστική διαδικασία όχι για όλο το αντικείμενο αλλά για κάθε είδος χωριστά. */
	private boolean perItem;
	/** Απαιτούμενα δικαιολογητικά συμμετοχής, χωρισμένα με &. */
	private String tenderDocuments;
	/** Το καταλογιστέο ποσό του διαγωνισμού, επί του οποίου θα μειοδοτήσουν οι ενδιαφερόμενοι. */
	private double mixed;
	/** Διαταγή απόφασης προσωρινού αναδόχου. */
	private String tenderContractorOrder;
	/** Αποδεικτικά μέσα προσωρινού αναδόχου. */
	private String awardDocuments;
	/** Χρόνος κλήσης προσωρινού αναδόχου για να υποβάλλει τα δικαιολογητικά. */
	private String awardTime;
	/** Απαιτείται εγγυητική επιστολή καλής εκτέλεσης 5%. */
	private boolean awardWarranty;
	/** Διαταγή κατακύρωσης διαγωνισμού σε ανάδοχο. */
	private String awardContractorOrder;
	/** Οι διαγωνιζόμενοι. */
	final ArrayList<Competitor> competitors = new ArrayList<>();

	/** Οι αξίες σε € των τιμολογίων που ανήκουν στο διαγωνισμό.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Η δαπάνη στην οποία ανήκει ο διαγωνισμός. */
	final Expenditure parent;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
	 * @param parent Η δαπάνη στην οποία ανήκει ο διαγωνισμός */
	Tender(Expenditure parent) {
		this.parent = parent; tenderType = TenderType.CONCISE_TENDER;
		tenderDocuments = "ΤΕΥΔ & Τεχνική Προσφορά & Οικονομική Προσφορά";
		awardDocuments = "Αποδεικτικά Μέσα για τη Διαπίστωση της Επάρκειας ή Μη, των Μέτρων Αυτοκάθαρσης & "
				+ "Αποδεικτικά μέσα ότι δεν Συντρέχουν οι Λόγοι Αποκλεισμού των Άρθρων 73 και 74 του Ν.4412/2016 & "
				+ "Πιστοποιητικό από τη Διεύθυνση Προγραμματισμού και Συντονισμού της Επιθεώρησης Εργασιακών Σχέσεων & "
				+ "Πιστοποιητικό για την Απόδειξη Καταλληλότητας Άσκησης Επαγγελματικής Δραστηριότητας & "
				+ "Απόδειξη Οικονομικής και Χρηματοοικονομικής Επάρκειας & "
				+ "Πιστοποιητικά Τεχνικής και Επαγγελματικής Ικανότητας & "
				+ "Πιστοποιητικό Συμμόρφωσης με Πρότυπα Διασφάλισης και Περιβαλλοντικής Διαχείρισης & "
				+ "Δικαιολογητικά Νομιμοποίησης του Προσωρινού Αναδόχου & "
				+ "Δικαιολογητικά Απόδειξης Δάνειας Εμπειρίας & "
				+ "Επικυρωμένο Αντίγραφο Εγγύησης Καλής Εκτέλεσης & "
				+ "Επικυρωμένο Αντίγραφο Εγγύησης Προκαταβολής & "
				+ "Επικυρωμένο Αντίγραφο Εγγύησης Καλής Λειτουργίας";
	}

	/** Αρχικοποιεί ένα διαγωνισμό από έναν node δεδομένων του unserialize().
	 * @param parent Η δαπάνη στην οποία ανήκει ο διαγωνισμός
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Tender(Expenditure parent, Node node) throws Exception {
		this.parent = parent;
		if (!node.isObject()) throw new Exception("Για διαγωνισμό, αναμένονταν αντικείμενο");
		title                 = node.getField(H[ 0]).getString();
		tenderType            = TenderType.valueOf(node.getField(H[1]).getString());
		tenderAnnouncement    = node.getField(H[ 2]).getString();
		tenderAnnouncementId  = node.getField(H[ 3]).getString();
		tenderTime            = node.getField(H[ 4]).getString();
		tenderTimeEconomic    = node.getField(H[ 5]).getString();
		cpv                   = node.getField(H[ 6]).getString();
		cpvAux                = node.getField(H[ 7]).getString();
		perItem               = node.getField(H[ 8]).getBoolean();
		tenderDocuments       = node.getField(H[ 9]).getString();
		mixed                 = node.getField(H[10]).getDecimal();
		tenderContractorOrder = node.getField(H[11]).getString();
		awardDocuments        = node.getField(H[12]).getString();
		awardTime             = node.getField(H[13]).getString();
		awardWarranty         = node.getField(H[14]).getBoolean();
		awardContractorOrder  = node.getField(H[15]).getString();
		for (Node m           : node.getField(H[16]).getArray())
			competitors.add(new Competitor(this, m));
	}

	/** Κόστος πάνω από το οποίο έχουμε κανονικό διαγωνισμό. */
	static final private int OPEN_PRICE = 60000;

	/** Καθορίζει το είδος του διαγωνισμού ανάλογα με την καθαρή αξία των τιμολογίων. */
	void setTenderType() {
		tenderType = prices[0] > OPEN_PRICE ? TenderType.OPEN_PROC : TenderType.CONCISE_TENDER;
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Τίτλος", "Τύπος", "Διακήρυξη Διαγωνισμού", "ΑΔΑ Διακήρυξης", "Χρόνος Αποσφράγισης Προσφορών",
		"Χρόνος Αποσφράγισης Οικονομικών Προσφορών", "CPV", "Συμπληρωματικό CPV",
		"Προσφορά κατά είδος", "Δικαιολογητικά Συμμετοχής", "Καταλογιστέο",
		"Απόφαση Ανάδειξης Προσωρινού Αναδόχου", "Δικαιολογητικά Κατακύρωσης",
		"Χρόνος Κατάθεσης Δικαιολογητικών Κατακύρωσης", "Εγγυητική Επιστολή Καλής Εκτέλεσης",
		"Κατακύρωση Διαγωνισμού", "Ενδιαφερόμενοι"
	};

	/** Επιστρέφει τον τύπο του διαγωνισμού. */
	TenderType getTenderType() { return tenderType; }

	@Override public String toString() { return tenderAnnouncement == null ? "Ανώνυμος" : tenderAnnouncement; }

	@Override public void serialize(VariableFields fields) {
		if (title != null)                 fields.add(H[ 0], title);
		                                   fields.add(H[ 1], tenderType.toString());	// Δεν είναι ποτέ null
		if (tenderAnnouncement != null)    fields.add(H[ 2], tenderAnnouncement);
		if (tenderAnnouncementId != null)  fields.add(H[ 3], tenderAnnouncementId);
		if (tenderTime != null)            fields.add(H[ 4], tenderTime);
		if (tenderTimeEconomic != null)    fields.add(H[ 5], tenderTime);
		if (cpv != null)                   fields.add(H[ 6], cpv);
		if (cpvAux != null)                fields.add(H[ 7], cpvAux);
		                                   fields.add(H[ 8], perItem);
		if (tenderDocuments != null)       fields.add(H[ 9], tenderDocuments);
		if (mixed != 0)                    fields.add(H[10], mixed);
		if (tenderContractorOrder != null) fields.add(H[11], tenderContractorOrder);
		if (awardDocuments != null)        fields.add(H[12], awardDocuments);
		if (awardTime != null)             fields.add(H[13], awardTime);
		                                   fields.add(H[14], awardWarranty);
		if (awardContractorOrder != null)  fields.add(H[15], awardContractorOrder);
		if (!competitors.isEmpty())        fields.addListVariableSerializable(H[16], competitors);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case  0: return null;	// Επικεφαλίδα «Διαγωνισμός»
			case  1: return title;
			case  2: return tenderType;
			case  3: return tenderAnnouncement;
			case  4: return tenderAnnouncementId;
			case  5: return tenderTime;
			case  6: return tenderTimeEconomic;
			case  7: return cpv;
			case  8: return cpvAux;
			case  9: return NOYES[perItem ? 1 : 0];
			case 10: return tenderDocuments;
			case 11: return a(mixed);
			case 12: return tenderContractorOrder;
			case 13: return null;	// Επικεφαλίδα «Κατακύρωση»
			case 14: return awardDocuments;
			case 15: return awardTime;
			case 16: return NOYES[awardWarranty ? 1 : 0];
			default: return awardContractorOrder;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			//case  0: break;				// Επικεφαλίδα «Διαγωνισμός»
			case  1: title                 = getString(value); break;
			case  2:
				tenderType                 = (TenderType) value;
				parent.calcContents();
				break;
			case  3: tenderAnnouncement    = getString(value); break;
			case  4: tenderAnnouncementId  = getString(value); break;
			case  5: tenderTime            = getString(value); break;
			case  6: tenderTimeEconomic    = getString(value); break;
			case  7: cpv                   = getString(value); break;
			case  8: cpvAux                = getString(value); break;
			case  9: perItem               = value == NOYES[1]; break;
			case 10: tenderDocuments       = getString(value); break;
			case 11: mixed                 = getDouble(value); break;
			case 12: tenderContractorOrder = getString(value); break;
			//case  13: break;				// Επικεφαλίδα «Κατακύρωση»
			case 14: awardDocuments        = getString(value); break;
			case 15: awardTime             = getString(value); break;
			case 16: awardWarranty         = value == NOYES[1]; break;
			case 17: awardContractorOrder  = getString(value); break;
		}
	}


	/** Ο τύπος του διαγωνισμού. */
	static final class TenderType {
		/** Ιδιωτική αρχικοποίηση του enum. */
		private TenderType(String s) { a = s; }
		/** Ο τύπος του διαγωνισμού με κείμενο. */
		final private String a;
		@Override public String toString() { return a; }
		/** Λαμβάνει τον τύπο του διαγωνισμού από το κείμενο περιγραφής του.
		 * Αν το κείμενο είναι εσφαλμένο ή null επιστρέφει DIRECT_ASSIGNMENT.
		 * @param s Ο τύπος του διαγωνισμού σε κείμενο
		 * @return Ο τύπος του διαγωνισμού ή null αν πρόκειται για απευθείας ανάθεση */
		static private TenderType valueOf(String s) {
			if (CONCISE_TENDER.a.equals(s)) return CONCISE_TENDER;
			if (OPEN_PROC.a.equals(s)) return OPEN_PROC;
			if (CLOSED_PROC.a.equals(s)) return CLOSED_PROC;
			return null;
		}
		/** Συνοπτικός Διαγωνισμός. */
		static final TenderType CONCISE_TENDER = new TenderType("Συνοπτικός Διαγωνισμός");
		/** Ανοικτή Διαδικασία. */
		static final private TenderType OPEN_PROC = new TenderType("Ανοικτή Διαδικασία");
		/** Κλειστή Διαδικασία. */
		static final private TenderType CLOSED_PROC = new TenderType("Κλειστή Διαδικασία");
		/** Επιστρέφει λίστα με όλους τους τύπους διαγωνισμού.
		 * @return Λίστα με όλους τους τύπους διαγωνισμού */
		static TenderType[] values() { return new TenderType[] { CONCISE_TENDER, OPEN_PROC, CLOSED_PROC }; }
	}


	/** Ενδιαφερόμενοι οικονομικοί φορείς για το διαγωνισμό. */
	static class Competitor implements VariableSerializable, ResizableTableModel.TableRecord {
		/** Ο πατρικός διαγωνισμός. */
		final private Tender parent;
		/** Ενδιαφερόμενος */
		private Contractor contractor;
		/** Υποβληθέντα δικαιολογητικά συμμετοχής, χωρισμένα με &. */
		private String documents;
		/** Λόγοι απόρριψης, για δικαιολογητικά συμμετοχής και τεχνική προσφορά, διαχωριζόμενοι με &. */
		private String technicalRejections;
		/** Λόγοι απόρριψης, για οικονομική προσφορά, διαχωριζόμενοι με &. */
		private String economicRejections;
		/** Ύψος οικονομικής προσφοράς. */
		private double offer;

		/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
		 * @param tender Ο διαγωνισμός για τον οποίο επέδειξε ενδιαφέρον ο οικονομικός φορέας */
		Competitor(Tender tender) { parent = tender; documents = tender.tenderDocuments; }
		/** Αρχικοποίηση του αντικειμένου.
		 * @param tender Ο διαγωνισμός για τον οποίο επέδειξε ενδιαφέρον ο οικονομικός φορέας
		 * @param contractor Ο ενδιαφερόμενος οικονομικός φορέας */
		Competitor(Tender tender, Contractor contractor) { this(tender); this.contractor = contractor; }

		/** Αρχικοποιεί τον ενδιαφερόμενο οικονομικό φορέα από έναν node δεδομένων του unserialize().
		 * @param tender Ο διαγωνισμός για τον οποίο επέδειξε ενδιαφέρον ο οικονομικός φορέας
		 * @param n Ο node δεδομένων
		 * @throws Exception Αν ο node δεν είναι αντικείμενο */
		private Competitor(Tender tender, Node node) throws Exception {
			parent = tender;
			if (!node.isObject()) throw new Exception("Για ενδιαφερόμενο οικονομικό φορέα, αναμένονταν αντικείμενο");
			contractor               = Contractor.create(node.getField(H[0]));
			documents                = node.getField(H[1]).getString();
			technicalRejections      = node.getField(H[2]).getString();
			Node n                   = node.getField(H[3]);
			if (n.isDecimal()) offer = n.getDecimal();
			else economicRejections  = n.getString();
		}

		/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
		static final String[] H = {
			"Ενδιαφερόμενος", "Δικαιολογητικά Συμμετοχής", "Λόγοι Απόρριψης Συμμετοχής", "Προσφορά ή Απόρριψη"
		};

		/** Επιστρέφει τον ενδιαφερόμενο οικονομικό φορέα του διαγωνισμού. */
		Contractor getContractor() { return contractor; }

		@Override public void serialize(VariableFields fields) {
			if (contractor != null)           fields.add(H[0], contractor);
			if (documents != null)            fields.add(H[1], documents);
			if (technicalRejections != null)  fields.add(H[2], technicalRejections);
			if (economicRejections != null)   fields.add(H[3], economicRejections);
			else if (offer != 0)              fields.add(H[3], offer);
		}

		@Override public Object getCell(int index) {
			switch(index) {
				case 0: return contractor;
				case 1: return documents;
				case 2: return technicalRejections;
				default: return economicRejections != null ? economicRejections : a(offer);
			}
		}

		@Override public void setCell(int index, Object value) {
			switch(index) {
				case 0:
					if (value == contractor);
					else if (value != null
							&& parent.competitors.stream()
									.anyMatch(i -> value.equals(i.getContractor())))
						showMessageDialog(window, "Ο ενδιαφερόμενος υπάρχει ήδη.",
								"Αποτυχία εισαγωγής", ERROR_MESSAGE);
					else if (!isContractorDeleteAccepted(parent, contractor))
						showMessageDialog(window, "Δεν μπορεί να αλλάξει ενδιαφερόμενος που είναι ανάδοχος σε σύμβαση",
								"Αποτυχία αλλαγής ενδιαφερόμενου", ERROR_MESSAGE);
					else contractor = (Contractor) value;
					break;
				case 1: documents = getString(value); break;
				case 2: technicalRejections = getString(value); break;
				case 3:
					offer = getDouble(value);
					economicRejections = offer == 0 ? getString(value) : null;
					break;
			}
		}

		@Override public boolean isEmpty() {
			return contractor == null && documents == null && technicalRejections == null
					&& economicRejections == null && offer == 0;
		}
	}
}