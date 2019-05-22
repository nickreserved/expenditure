package expenditure;

import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel;
import util.ResizableTableModel;
import static util.ResizableTableModel.getString;

/** Μια σύμβαση. */
public class Contract implements VariableSerializable, ResizableTableModel.TableRecord,
		PropertiesTableModel.TableRecord {
	/** Η ταυτότητα της σύμβασης. */
	private String name;
	/** Το θέμα της σύμβασης. */
	private String title;
	/** Ο τύπος του διαγωνισμού από τον οποίο προέκυψε η σύμβαση. */
	private TenderType tenderType;
	/** Ο δικαιούχος με τον οποίο συνυπογράφεται η σύμβαση. */
	private Contractor contractor;

	/** Οι αξίες σε € των τιμολογίων που ανήκουν στην σύμβαση.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
	Contract() { tenderType = TenderType.DIRECT_ASSIGNMENT; }

	/** Αρχικοποιεί μια σύμβαση από έναν node δεδομένων του unserialize().
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contract(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για σύμβαση, αναμένονταν αντικείμενο");
		name             = node.getField(H[0]).getString();
		title            = node.getField(H[1]).getString();
		tenderType       = TenderType.valueOf(node.getField(H[2]).getString());
		try { contractor = new Contractor(node.getField(H[3])); }
		catch(Exception e) { contractor = null; }
	}

	/** Αρχικοποιείται μια σύμβαση από το σύστημα αυτόματων υπολογισμών.
	 * @param contractor Ο ανάδοχος της σύμβασης */
	Contract(Contractor contractor) {
		this.contractor = contractor;
		name = contractor != null ? contractor.toString() : "ανώνυμη";
	}

	/** Κόστος πάνω από το οποίο έχουμε συνοπτικό διαγωνισμό. */
	static final private int CONCISE_PRICE = 20000;
	/** Κόστος πάνω από το οποίο έχουμε κανονικό διαγωνισμό. */
	static final private int OPEN_PRICE = 60000;

	/** Καθορίζει το είδος του διαγωνισμού στη σύμβαση ανάλογα με τη δοσμένη καθαρή αξία.
	 * @param price Η συνολική καθαρή αξία όλων των τιμολογίων του ανάδοχου */
	void setTenderType(double price) {
		if (price > OPEN_PRICE) tenderType = TenderType.OPEN_PROC;
		else if (price > CONCISE_PRICE) tenderType = TenderType.CONCISE_TENDER;
		else tenderType = TenderType.DIRECT_ASSIGNMENT;
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Σύμβαση", "Τίτλος", "Τύπος Διαγωνισμού", "Ανάδοχος" };

	/** Επιστρέφει τον ανάδοχο της σύμβασης. */
	Contractor getContractor() { return contractor; }

	@Override public String toString() { return "Σύμβαση " + name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)       fields.add(H[0], name);
		if (title != null)      fields.add(H[1], title);
		                        fields.add(H[2], tenderType.toString());	// Δεν είναι ποτέ null
		if (contractor != null) fields.add(H[3], contractor);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return title;
			case 2: return tenderType;
			default: return contractor;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name       = getString(value); break;
			case 1: title      = getString(value); break;
//			case 2: tenderType = (TenderType) value; break;
			case 3: contractor = (Contractor) value; break;
		}
	}

	@Override public boolean isEmpty() { return name == null && title == null && contractor == null; }


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
		 * @return Ο τύπος του διαγωνισμού */
		static private TenderType valueOf(String s) {
			if (CONCISE_TENDER.a.equals(s)) return CONCISE_TENDER;
			if (OPEN_PROC.a.equals(s)) return OPEN_PROC;
			if (CLOSED_PROC.a.equals(s)) return CLOSED_PROC;
			if (COMPETITIVE_PROC.a.equals(s)) return COMPETITIVE_PROC;
			if (COMPETITIVE_DIALOG.a.equals(s)) return COMPETITIVE_DIALOG;
			if (INNOVATION_PARTNERSHIP.a.equals(s)) return INNOVATION_PARTNERSHIP;
			return DIRECT_ASSIGNMENT;
		}
		/** Απευθείας Ανάθεση. */
		static final private TenderType DIRECT_ASSIGNMENT = new TenderType("Απευθείας Ανάθεση");
		/** Συνοπτικός Διαγωνισμός. */
		static final private TenderType CONCISE_TENDER = new TenderType("Συνοπτικός Διαγωνισμός");
		/** Ανοικτή Διαδικασία. */
		static final private TenderType OPEN_PROC = new TenderType("Ανοικτή Διαδικασία");
		/** Κλειστή Διαδικασία. */
		static final private TenderType CLOSED_PROC = new TenderType("Κλειστή Διαδικασία");
		/** Ανταγωνιστική Διαδικασία με Διαπραγμάτευση. */
		static final private TenderType COMPETITIVE_PROC = new TenderType("Ανταγωνιστική Διαδικασία με Διαπραγμάτευση");
		/** Ανταγωνιστικός Διάλογος. */
		static final private TenderType COMPETITIVE_DIALOG = new TenderType("Ανταγωνιστικός Διάλογος");
		/** Σύμπραξη Καινοτομίας. */
		static final private TenderType INNOVATION_PARTNERSHIP = new TenderType("Σύμπραξη Καινοτομίας");
		/** Επιστρέφει λίστα με όλους τους τύπους διαγωνισμού.
		 * @return Λίστα με όλους τους τύπους διαγωνισμού */
		static TenderType[] values() {
			return new TenderType[] {
				DIRECT_ASSIGNMENT, CONCISE_TENDER, OPEN_PROC, CLOSED_PROC, COMPETITIVE_PROC,
				COMPETITIVE_DIALOG, INNOVATION_PARTNERSHIP
			};
		}
	}
}