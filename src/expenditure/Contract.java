package expenditure;

import static expenditure.Contract.TenderType.DIRECT_ASSIGNMENT;
import java.util.ArrayList;
import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import util.ResizableTableModel;
import static util.ResizableTableModel.getString;

/** Μια σύμβαση. */
final class Contract implements VariableSerializable, TableRecord {
	/** Η ταυτότητα της σύμβασης. */
	private String name;
	/** Το θέμα της σύμβασης. */
	private String title;
	/** Ο τύπος του διαγωνισμού από τον οποίο προέκυψε η σύμβαση. */
	private TenderType tenderType;
	/** Ο δικαιούχος με τον οποίο συνυπογράφεται η σύμβαση. */
	private Contractor contractor;
	/** Η διακήρυξη του διαγωνισμού. */
	private String competitionCall;
	/** Το ΑΔΑ της διακήρυξης του διαγωνισμού. */
	private String competitionCallId;
	/** Τόπος διεξαγωγής διαγωνισμού. */
	private String competitionPlace;
	/** Χρόνος διεξαγωγής διαγωνισμού. */
	private String competitionTime;
	/** Κωδικοί κοινού λεξιλογίου δημόσιων συμβάσεων. */
	private String cpv;
	/** Συμπληρωματικοί κωδικοί κοινού λεξιλογίου δημόσιων συμβάσεων. */
	private String cpvAux;
	/** Οι διαγωνιζόμενοι. */
	final ArrayList<Contractor> competitors = new ArrayList<>();
	/** Τα δικαιολογητικά του διαγωνισμού. */
	final ArrayList<Document> documents = new ArrayList<>();

	/** Οι αξίες σε € των τιμολογίων που ανήκουν στην σύμβαση.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Η δαπάνη στην οποία ανήκει η σύμβαση. */
	final Expenditure parent;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση */
	Contract(Expenditure parent) { this.parent = parent; tenderType = TenderType.DIRECT_ASSIGNMENT; }

	/** Αρχικοποιεί μια σύμβαση από έναν node δεδομένων του unserialize().
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contract(Expenditure parent, Node node) throws Exception {
		this.parent = parent;
		if (!node.isObject()) throw new Exception("Για σύμβαση, αναμένονταν αντικείμενο");
		name              = node.getField(H[0]).getString();
		title             = node.getField(H[1]).getString();
		tenderType        = TenderType.valueOf(node.getField(H[2]).getString());
		Node n            = node.getField(H[3]);
		competitionCall   = node.getField(H[4]).getString();
		competitionCallId = node.getField(H[5]).getString();
		competitionPlace  = node.getField(H[6]).getString();
		competitionTime   = node.getField(H[7]).getString();
		cpv               = node.getField(H[8]).getString();
		cpvAux            = node.getField(H[9]).getString();
		for (Node m : node.getField(H[10]).getArray())
			competitors.add(new Contractor(m));
		for (Node m : node.getField(H[11]).getArray())
			documents.add(new Document(m));
		// Σε διαγωνισμό, ο νικητής αποθηκεύεται ως index των διαγωνιζόμενων
		contractor = n.isInteger() ? competitors.get((int) n.getInteger()) : Contractor.create(n);
	}

	/** Αρχικοποιείται μια σύμβαση από το σύστημα αυτόματων υπολογισμών.
	 * @param parent Η δαπάνη στην οποία ανήκει η σύμβαση
	 * @param contractor Ο ανάδοχος της σύμβασης */
	Contract(Expenditure parent, Contractor contractor) {
		this.parent = parent; this.contractor = contractor;
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
		calcDocuments();
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Σύμβαση", "Τίτλος", "Τύπος Διαγωνισμού", "Ανάδοχος",
		"Διακήρυξη Διαγωνισμού", "ΑΔΑ Διακήρυξης Διαγωνισμού", "Τόπος Διαγωνισμού",
		"Χρόνος Διαγωνισμού", "CPV", "Συμπληρωματικό CPV",
		"Διαγωνιζόμενοι", "Δικαιολογητικά"
	};

	/** Επιστρέφει τον ανάδοχο της σύμβασης. */
	Contractor getContractor() { return contractor; }
	/** Επιστρέφει τον τύπο του διαγωνισμού. */
	TenderType getTenderType() { return tenderType; }

	@Override public String toString() { return name == null ? "Σύμβαση ανώνυμη" : "Σύμβαση " + name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)              fields.add(H[0], name);
		if (title != null)             fields.add(H[1], title);
		                               fields.add(H[2], tenderType.toString());	// Δεν είναι ποτέ null
		if (contractor != null) {	// Αν έχουμε διαγωνισμό ο νικητής αποθηκεύεται με το index των διαγωνιζόμενων
			int idx = competitors.indexOf(contractor);
			if (idx != -1)             fields.add(H[3], idx);
			else                       fields.add(H[3], contractor);
		}
		if (competitionCall != null)   fields.add(H[4], competitionCall);
		if (competitionCallId != null) fields.add(H[5], competitionCallId);
		if (competitionPlace != null)  fields.add(H[6], competitionPlace);
		if (competitionTime != null)   fields.add(H[7], competitionTime);
		if (cpv != null)               fields.add(H[8], cpv);
		if (cpvAux != null)            fields.add(H[9], cpvAux);
		if (!competitors.isEmpty())    fields.addListVariableSerializable(H[10], competitors);
		if (!documents.isEmpty())      fields.addListVariableSerializable(H[11], documents);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return title;
			case 2: return tenderType;
			case 3: return contractor;
			case 4: return null;	// Επικεφαλίδα: Διαγωνισμοί
			case 5: return competitionCall;
			case 6: return competitionCallId;
			case 7: return competitionPlace;
			case 8: return competitionTime;
			case 9: return cpv;
			default: return cpvAux;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name              = getString(value); break;
			case 1: title             = getString(value); break;
			case 2:
				tenderType            = (TenderType) value;
				calcDocuments();
				parent.calcContents();
				break;
			case 3:
				contractor            = (Contractor) value;
				calcDocuments();
				break;
			//case 4: break;	// Επικεφαλίδα: Διαγωνισμοί
			case 5: competitionCall   = getString(value); break;
			case 6: competitionCallId = getString(value); break;
			case 7: competitionPlace  = getString(value); break;
			case 8: competitionTime   = getString(value); break;
			case 9: cpv               = getString(value); break;
			case 10: cpvAux           = getString(value); break;
		}
	}

	/** Η σύμβαση έχει όλα της τα πεδία κενά. */
	boolean isEmpty() {
		return name == null && title == null && contractor == null && competitionCall == null
				&& competitionCallId == null && competitionPlace == null && competitionTime == null
				&& cpv == null && cpvAux == null
				&& competitors.isEmpty() && documents.isEmpty();
	}

	/** Προσθέτει αυτόματα δικαιολογητικά και διαγωνιζόμενους, αν υπάρχει διαγωνισμός.
	 * Αν έχει οριστεί ο ανάδοχος, προστίθεται στη λίστα διαγωνιζόμενων. */
	private void calcDocuments() {
		if (tenderType != DIRECT_ASSIGNMENT) {
			if (contractor != null && !competitors.contains(contractor))
				competitors.add(contractor);
			addDocumentIfNotExist(new Document("ΤΕΥΔ"));
			addDocumentIfNotExist(new Document("Οικονομική Προσφορά"));
			addDocumentIfNotExist(new Document("Τεχνική Προσφορά"));
			addDocumentIfNotExist(new Document("Υπεύθυνη Δήλωση μη χρησιμοποίησης απόστρατου των ΕΔ ως εκπρόσωπου"));
		}
	}

	/** Προσθέτει ένα δικαιολογητικό στη λίστα δικαιολογητικών, αν δεν υπάρχει ήδη.
	 * @param doc Το δικαιολογητικό που πιθανόν να προστεθεί στη λίστα δικαιολογητικών */
	private void addDocumentIfNotExist(Document doc) { if (!documents.contains(doc)) documents.add(doc); }


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
		static final TenderType DIRECT_ASSIGNMENT = new TenderType("Απευθείας Ανάθεση");
		/** Συνοπτικός Διαγωνισμός. */
		static final TenderType CONCISE_TENDER = new TenderType("Συνοπτικός Διαγωνισμός");
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

	/** Δικαιολογητικό διαγωνιζόμενων οικονομικών φορέων. */
	static class Document implements VariableSerializable, ResizableTableModel.TableRecord {
		/** Όνομα δικαιολογητικού. */
		private String name;
		/** Λόγος απόρριψης για κάθε οικονομικό φορέα.
		 * Μπορεί να έχει τις τιμές:
		 * <ul><li>"δεν απαιτείται": Το δικαιολογητικό δεν απαιτείται για το συγκεκριμένο οικονομικό
		 * φορέα.
		 * <li>"δεν το υπέβαλε": Ο οικονομικός φορέας παρέλειψε να υποβάλει το δικαιολογητικό.
		 * <li>null: Το υπέβαλε και έγινε αποδεκτό.
		 * <li>Οτιδήποτε άλλο κείμενο: Το δικαιολογητικό δε γίνεται δεκτό και το κείμενο είναι ο
		 * λόγος που δε γίνεται δεκτό. */
		private final ArrayList<String> rejections = new ArrayList<>(5);

		/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
		Document() {}
		/** Αρχικοποίηση του αντικειμένου.
		 * @param name Το όνομα του δικαιολογητικού */
		private Document(String name) { this.name = name; }

		/** Αρχικοποιεί το δικαιολογητικό ενός οικονομικού φορέα από έναν node δεδομένων του unserialize().
		 * @param n Ο node δεδομένων
		 * @throws Exception Αν ο node δεν είναι αντικείμενο */
		private Document(Node node) throws Exception {
			if (!node.isObject()) throw new Exception("Για δικαιολογητικό οικονομικού φορέα, αναμένονταν αντικείμενο");
			name = node.getField(H[0]).getString();
			node.getField(H[1]).getArray().forEach(i -> rejections.add(i.getString()));
		}

		/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
		static final String[] H = { "Δικαιολογητικό", "Απορρίψεις" };

		@Override public void serialize(VariableFields fields) {
			if (name != null)          fields.add(          H[0], name);
			if (!rejections.isEmpty()) fields.addListString(H[1], rejections);
		}

		@Override public Object getCell(int index) {
			if (index == 0) return name;
			else return index - 1 < rejections.size() ? rejections.get(index - 1) : null;
		}

		@Override public void setCell(int index, Object value) {
			if (index == 0) name = getString(value);
			else {
				--index;
				// Αν η τιμή δεν είναι null, λαμβάνουμε υπόψη ότι μπορεί να είναι εκτός ορίων
				// λίστας και πρέπει να προστεθούν κι άλλα στοιχεία στο τέλος της λίστας
				if (value != null) {
					for (int z = index - rejections.size(); z >= 0; --z)
						rejections.add(null);
					rejections.set(index, (String) value);
				} else if (index + 1 < rejections.size()) rejections.set(index, (String) value);
				// Αν η τιμή είναι null και είναι η τελευταία στη λίστα, αφαιρούμε το στοιχείο
				// και όλα τα υπόλοιπα null στοιχεία στο τέλος της λίστας
				else if (index + 1 == rejections.size()) {
					while(index >= 0 && rejections.get(index) == null) --index;
					rejections.subList(index, rejections.size()).clear();
				}
			}
		}

		@Override public boolean isEmpty() {
			return name == null && rejections.stream().allMatch(i -> i == null);
		}

		@Override public boolean equals(Object o) {
			return this == o || o instanceof Document && Objects.equals(name, ((Document) o).name);
		}

		@Override public int hashCode() { return Objects.hashCode(this.name); }
	}
}