package expenditure;

import static expenditure.ContentItem.fixContents;
import static expenditure.ContentItem.ΑπόφασηΑνάληψηςΥποχρέωσης;
import static expenditure.ContentItem.ΑπόφασηΑπευθείαςΑνάθεσης;
import static expenditure.ContentItem.ΔιαβιβαστικόΔαπάνης;
import static expenditure.MainFrame.NOYES;
import static expenditure.MainFrame.window;
import java.io.File;
import java.util.ArrayList;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import static util.ResizableTableModel.getLong;
import static util.ResizableTableModel.getString;

/** Μια δαπάνη. */
final class Expenditure implements VariableSerializable, TableRecord {
	/** Η διαταγή της διάθεσης της πίστωσης. */
	private String orderDispensation;
	/** Η διαταγή της διάθεσης είναι αναρτητέα στο διαδίκτυο. */
	private boolean advertise;
	/** Η δαπάνη είναι δαπάνη έργου. */
	private boolean construction;
	/** Ειδικός Φορέας ΕΦ. */
	private long acb = 10112020000000L;	// Administrative Classification of Badget
	/** Αναλυτικός Λογαριασμός Εξόδων/Εσόδων ΑΛΕ. */
	private long aae;					// Analytical Account of Expenses/Income
	/** Ο τύπος της χρηματοδότησης της δαπάνης. */
	private Financing financing;
	/** Τίτλος δαπάνης. */
	private String title;
	/** Η απόφαση απευθείας ανάθεσης. */
	private String orderDirectAssignment;
	/** Το ΑΔΑ της απόφασης απευθείας ανάθεσης. */
	private String orderDirectAssignmentId;
	/** Διαβιβαστικό της δαπάνης. */
	private String orderTransport;
	/** Κρατήσεις, ΦΕ και ΦΠΑ υπολογίζονται αυτόματα. */
	private boolean smart;
	/** Στοιχεία της Μονάδας που εκτελεί τη δαπάνη. */
	final UnitInfo unitInfo;
	/** Το φύλλο καταχώρησης της δαπάνης. */
	final ArrayList<ContentItem> contents = new ArrayList<>(70);
	/** Τα τιμολόγια της δαπάνης. */
	final ArrayList<Invoice> invoices = new ArrayList<>(30);
	/** Οι εργασίες της δαπάνης. Αφορά μόνο δαπάνες έργων. */
	final ArrayList<Work> works = new ArrayList<>(100);
	/** Οι συμβάσεις της δαπάνης. */
	final ArrayList<Contract> contracts = new ArrayList<>(7);

	/** Το αρχείο στο οποίο είναι αποθηκευμένη ή πρόκειται να αποθηκευτεί η δαπάνη.
	 * Δεν είναι απαραίτητο η δαπάνη να είναι αποθηκευμένη σε αυτό το αρχείο. Μπορεί η δαπάνη μόλις
	 * να έχει δημιουργηθεί, οπότε της δώθηκε ένα όνομα αρχείου π.χ. "Αρχείο Δαπάνης - 1.expenditure".
	 * <p>Το πεδίο δεν αποθηκεύεται μαζί με τη δαπάνη στο αρχείο. Χρησιμοποιείται μόνο κατά την
	 * εκτέλεση του προγράμματος, προκειμένου να γνωρίζει το πρόγραμμα που είναι αποθηκευμένη η
	 * δαπάνη. */
	File file;

	/** Οι αξίες σε € όλων των τιμολογίων της δαπάνης.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
	 * @param f Το αρχείο στο οποίο είναι αποθηκευμένη ή πρόκειται να αποθηκευτεί η δαπάνη.
	 * @param u Τα στοιχεία της Μονάδας, τα οποία, αν και ίδια για όλες τις δαπάνες, αντιγράφονται
	 * ξανά μέσα στη δαπάνη, ώστε αν ανοίξουμε τη δαπάνη από πρόγραμμα άλλης Μονάδας ή μετά από
	 * χρόνια, να διατηρεί τα στοιχεία της Μονάδας στην οποία συντάχθηκε, στο χρόνο που συντάχθηκε. */
	Expenditure(File f, UnitInfo u) {
		file = f; unitInfo = u; financing = Financing.ARMY_BUDGET;
		fixContents(this);
	}

	/** Αρχικοποιεί μια δαπάνη από έναν node δεδομένων του unserialize().
	 * @param file Το αρχείο στο οποίο αποθηκεύεται ή θα αποθηκευτεί η δαπάνη
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Expenditure(File file, Node node) throws Exception {
		this.file = file;
		if (!node.isObject()) throw new Exception("Για δαπάνη, αναμένονταν αντικείμενο");
		unitInfo                = new UnitInfo(node);
		orderDispensation       = node.getField(H[0]).getString();
		advertise               = node.getField(H[1]).getBoolean();
		construction            = node.getField(H[2]).getBoolean();
		acb                     = node.getField(H[3]).getInteger();
		aae                     = node.getField(H[4]).getInteger();
		financing               = Financing.valueOf(node.getField(H[5]).getString());
		title                   = node.getField(H[6]).getString();
		orderDirectAssignment   = node.getField(H[7]).getString();
		orderDirectAssignmentId = node.getField(H[8]).getString();
		orderTransport          = node.getField(H[9]).getString();
		smart                   = node.getField(H[10]).getBoolean();
		// Ανάγνωση εργασιών
		Node n                  = node.getField(H[11]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα εργασιών");
			for (Node i : n.getArray())
				works.add(new Work(i));
		}
		// Ανάγνωση συμβάσεων (πρώτα οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		n                       = node.getField(H[12]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα συμβάσεων");
			for (Node i : n.getArray())
				contracts.add(new Contract(i));
		}
		// Ανάγνωση τιμολογίων (πρώτα οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		n                       = node.getField(H[13]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα τιμολογίων");
			for (Node i : n.getArray())
				invoices.add(new Invoice(this, i));
		}
		// Ανάγνωση φύλλου καταχώρησης (πρώτα οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		ContentItem.unserialize(node.getField(H[14]).getArray(), this);
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { ΑπόφασηΑνάληψηςΥποχρέωσης.toString(), "Αναρτητέα στο διαδίκτυο",
		"Έργο", "ΕΦ", "ΑΛΕ", "Τύπος Χρηματοδότησης", "Τίτλος",
		ΑπόφασηΑπευθείαςΑνάθεσης.toString(), "ΑΔΑ Απόφασης Απευθείας Ανάθεσης",
		ΔιαβιβαστικόΔαπάνης.toString(), "Αυτόματοι Υπολογισμοί",
		"Εργασίες", "Συμβάσεις", "Τιμολόγια", "Φύλλο Καταχώρησης"
	};

	/** Ο φορέας διάθεσης της δαπάνης είναι το ΓΕΣ/ΔΥΠΠΕ. */
	boolean isConstruction() { return construction; }
	/** Η πηγή χρηματοδότησης της δαπάνης. */
	Financing getFinancing() { return financing; }
	/** Οι αυτόματοι υπολογισμοί είναι ενεργοί. */
	boolean isSmart() { return smart; }

	/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format για επεξεργασία από PHP script.
	 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
	 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
	@Override public void serialize(VariableFields fields) {
		serializeC(fields);
		fields.addV(H[14], contents);
	}
	/** Μετατρέπει τη δαπάνη σε php serialize string format, για αποθήκευση σε αρχείο.
	 * @return Μετατροπέας του αντικειμένου java σε php serialize string format. */
	VariableSerializable save() {
		return (VariableFields fields) -> {
			serializeC(fields);
			fields.add(H[14], ContentItem.save(contents));
		};
	}
	/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format.
	 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
	 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
	private void serializeC(VariableFields fields) {
		unitInfo.serialize(fields);	// Συγχώνευση των 2 αντικειμένων σε ένα
		if (orderDispensation != null)       fields.add (H[0],  orderDispensation);
		                                     fields.add (H[1],  advertise);
		                                     fields.add (H[2],  construction);
		if (acb != 0)                        fields.add (H[3],  acb);
		if (aae != 0)                        fields.add (H[4],  aae);
		                                     fields.add (H[5],  financing.toString());
		if (title != null)                   fields.add (H[6],  title);
		if (orderDirectAssignment != null)   fields.add (H[7],  orderDirectAssignment);
		if (orderDirectAssignmentId != null) fields.add (H[8],  orderDirectAssignmentId);
		if (orderTransport != null)          fields.add (H[9],  orderTransport);
		                                     fields.add (H[10], smart);
		if (!works.isEmpty())                fields.addV(H[11], works);
		if (!contracts.isEmpty())            fields.addV(H[12], contracts);
		if (!invoices.isEmpty())             fields.addV(H[13], invoices);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return null;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 1: return orderDispensation;
			case 2: return advertise ? NOYES[1] : NOYES[0];
			case 3: return construction ? NOYES[1] : NOYES[0];
			case 4: return a(acb);
			case 5: return a(aae);
			case 6: return financing;
			case 7: return title;
			case 8: return orderDirectAssignment;
			case 9: return orderDirectAssignmentId;
			case 10: return orderTransport;
			case 11: return null;	// Επικεφαλίδα «Αυτοματισμοί»
			case 12: return smart ? NOYES[1] : NOYES[0];
			default: return unitInfo.getCell(index - 13);
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: break;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 1: orderDispensation    = getString(value); break;
			case 2: advertise = value == NOYES[1];
			case 3: {		// Θέτει την πηγή χρηματοδότησης της δαπάνης
				boolean s = value == NOYES[1];
				if (s != construction && OK_OPTION == showConfirmDialog(window,
						"Επιλέξατε να αλλάξετε τύπο δαπάνης.\n"
						+ "Το φύλλο καταχώρησης της δαπάνης θα τροποποιηθεί.\n"
						+ "Η τροποποίηση που θα γίνει είναι αδύνατο να αναιρεθεί.\n"
						+ "Θέλετε να συνεχίσετε;\n"
						, "Αλλαγή τύπου δαπάνης", OK_CANCEL_OPTION, WARNING_MESSAGE)) {
					construction = s;
					//TODO: τροποποίηση φύλλου καταχώρησης δαπάνης
					if (smart) invoices.forEach(i -> i.recalcFromConstruction());
				}
				break;
			}
			case 4: acb                     = getLong(value); break;
			case 5: aae                     = getLong(value); break;
			case 6:		// Θέτει την πηγή χρηματοδότησης της δαπάνης
				if (value != financing) {
					financing = (Financing) value;
					if (smart) invoices.forEach(i -> i.recalcFromFinancing());
				}
				break;
			case 7: title                   = getString(value); break;
			case 8: orderDirectAssignment   = getString(value); break;
			case 9: orderDirectAssignmentId = getString(value); break;
			case 10: orderTransport         = getString(value); break;
			case 11: break;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 12: {		// Απενεργοποιεί/ενεργοποιεί τον έλεγχο δεδομένων στα τιμολόγια
				boolean s = value == NOYES[1];
				if (s != smart && s) {
					if (CANCEL_OPTION == showConfirmDialog(window,
							"Επιλέξατε να ενεργοποιήσετε τον αυτόματο υπολογισμό.\n"
							+ "Ο αυτόματος υπολογισμός υπολογίζει αυτόματα το ΦΠΑ, τις κρατήσεις και το ΦΕ.\n"
							+ "Τυχόν τροποποιήσεις που θα γίνουν, θα είναι αδύνατο να αναιρεθούν.\n"
							+ "Θέλετε να συνεχίσετε;\n"
							, "Αυτοματισμός", OK_CANCEL_OPTION, WARNING_MESSAGE)) return;
					smart = s;
					invoices.forEach(i -> i.recalcFromSmart());
				} else smart = s;
				break;
			}
			default: unitInfo.setCell(index - 13, value); break;
		}
	}

	/** Αντικαθιστά τα άσχημα "0" στα κελιά των πινάκων, με κενό.
	 * @param d Ένας αριθμός.
	 * @return Αν ο a είναι 0 επιστρέφει null, αλλιώς επιστρέφει τον a. */
	static Long a(long d) { return d != 0 ? d : null; }

	/** Αντικαθιστά τα άσχημα "0.0" στα κελιά των πινάκων, με κενό.
	 * @param d Ένας αριθμός double.
	 * @return Αν ο d είναι 0 επιστρέφει null, αλλιώς επιστρέφει τον d. */
	static Double a(double d) { return d != 0 ? d : null; }


	/** Ο τύπος του δικαιούχου. */
	static final class Financing {
		/** Ιδιωτική αρχικοποίηση του enum. */
		private Financing(String s) { a = s; }
		/** Ο τύπος του δικαιούχου με κείμενο. */
		final private String a;
		@Override public String toString() { return a; }
		/** Λαμβάνει τον τύπο του δικαιούχου από το κείμενο περιγραφής του.
		 * Αν το κείμενο είναι εσφαλμένο ή null επιστρέφει ARMY_BUDGET.
		 * @param s Ο τύπος του δικαιούχου σε κείμενο
		 * @return Ο τύπος του δικαιούχου */
		static Financing valueOf(String s) {
			if (OWN_PROFITS.a.equals(s)) return OWN_PROFITS;
			if (PUBLIC_INVESTMENT.a.equals(s)) return PUBLIC_INVESTMENT;
			return ARMY_BUDGET;
		}
		/** Τακτικός προϋπολογισμός ΓΕΣ. */
		static final Financing ARMY_BUDGET = new Financing("Π/Υ ΓΕΣ");
		/** Ίδιοι πόροι από κέρδη λεσχών, πρατηρίων κτλ. */
		static final Financing OWN_PROFITS = new Financing("Ιδίων πόρων");
		/** Προϋπολογισμός Προγράμματος Δημοσίων Επενδύσεων. */
		static final Financing PUBLIC_INVESTMENT = new Financing("Π/Υ ΠΔΕ");
		/** Επιστρέφει λίστα με όλους τους τύπους χρηματοδότησης.
		 * @return Λίστα με όλους τους τύπους χρηματοδότησης */
		static Financing[] values() { return new Financing[] { ARMY_BUDGET, OWN_PROFITS, PUBLIC_INVESTMENT }; }
	}
}