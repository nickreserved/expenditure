package expenditure;

import static expenditure.ContentItem.convertContents;
import static expenditure.ContentItem.createAutoContents;
import static expenditure.MainFrame.NOYES;
import static expenditure.MainFrame.window;
import java.io.File;
import java.util.ArrayList;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
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
	/** Η διαταγή διάθεσης της πίστωσης. */
	private String orderDispensation;
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
	/** Διαβιβαστικό της δαπάνης. */
	private String orderTransport;
	/** Κρατήσεις, ΦΕ και ΦΠΑ υπολογίζονται αυτόματα. */
	private boolean smart;
	/** Αξιωματικός Έργου. */
	private Person projectManager;
	/** Πρόεδρος Επιτροπής Προσωρινής και Οριστικής Παραλαβής. */
	private Person acceptChief;
	/** Α' Μέλος Επιτροπής Προσωρινής και Οριστικής Παραλαβής. */
	private Person acceptMemberA;
	/** Β' Μέλος Επιτροπής Προσωρινής και Οριστικής Παραλαβής. */
	private Person acceptMemberB;
	/** Πρόεδρος Επιτροπής Αφανών Εργασιών. */
	private Person worksChief;
	/** Α' Μέλος Επιτροπής Αφανών Εργασιών. */
	private Person worksMember;	/** Στοιχεία της Μονάδας που εκτελεί τη δαπάνη. */
	final UnitInfo unitInfo;
	/** Το φύλλο καταχώρησης της δαπάνης. */
	final ArrayList<ContentItem> contents = new ArrayList<>(70);
	/** Τα τιμολόγια της δαπάνης. */
	final ArrayList<Invoice> invoices = new ArrayList<>(30);
	/** Οι εργασίες της δαπάνης. Αφορά μόνο δαπάνες έργων. */
	final ArrayList<Work> works = new ArrayList<>(100);
	/** Οι συμβάσεις της δαπάνης. */
	final ArrayList<Contract> contracts = new ArrayList<>(7);
	/** Οι διαγωνισμοί της δαπάνης. */
	final ArrayList<Tender> tenders = new ArrayList<>(7);

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

	/** Ο τύπος των περιεχομένων.
	 * false για απευθείας ανάθεση, true για διαγωνισμό (συνοπτικό ή ανοικτή διαδικασία). */
	private boolean cfg;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές.
	 * @param f Το αρχείο στο οποίο είναι αποθηκευμένη ή πρόκειται να αποθηκευτεί η δαπάνη.
	 * @param u Τα στοιχεία της Μονάδας, τα οποία, αν και ίδια για όλες τις δαπάνες, αντιγράφονται
	 * ξανά μέσα στη δαπάνη, ώστε αν ανοίξουμε τη δαπάνη από πρόγραμμα άλλης Μονάδας ή μετά από
	 * χρόνια, να διατηρεί τα στοιχεία της Μονάδας στην οποία συντάχθηκε, στο χρόνο που συντάχθηκε. */
	Expenditure(File f, UnitInfo u) {
		file = f; unitInfo = u; financing = Financing.ARMY_BUDGET;
		createAutoContents(cfg, contents);
	}

	/** Αρχικοποιεί μια δαπάνη από έναν node δεδομένων του unserialize().
	 * @param file Το αρχείο στο οποίο αποθηκεύεται ή θα αποθηκευτεί η δαπάνη
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Expenditure(File file, Node node) throws Exception {
		this.file = file;
		if (!node.isObject()) throw new Exception("Για δαπάνη, αναμένονταν αντικείμενο");
		unitInfo              = new UnitInfo(node);
		orderDispensation     = node.getField(H[0]).getString();
		construction          = node.getField(H[1]).getBoolean();
		acb                   = node.getField(H[2]).getInteger();
		aae                   = node.getField(H[3]).getInteger();
		financing             = Financing.valueOf(node.getField(H[4]).getString());
		title                 = node.getField(H[5]).getString();
		orderDirectAssignment = node.getField(H[6]).getString();
		orderTransport        = node.getField(H[7]).getString();
		smart                 = node.getField(H[8]).getBoolean();
		projectManager        = Person.create(node.getField(H[9]));
		acceptChief           = Person.create(node.getField(H[10]));
		acceptMemberA         = Person.create(node.getField(H[11]));
		acceptMemberB         = Person.create(node.getField(H[12]));
		worksChief            = Person.create(node.getField(H[13]));
		worksMember           = Person.create(node.getField(H[14]));
		// Ανάγνωση εργασιών
		Node n                = node.getField(H[15]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα εργασιών");
			for (Node i : n.getArray())
				works.add(new Work(i));
		}
		// Ανάγνωση διαγωνισμών (πρώτα οι διαγωνισμοί, μετά οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		n                     = node.getField(H[16]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα διαγωνισμών");
			for (Node i : n.getArray())
				tenders.add(new Tender(this, i));
		}
		// Ανάγνωση συμβάσεων (πρώτα οι διαγωνισμοί, μετά οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		n                     = node.getField(H[17]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα συμβάσεων");
			for (Node i : n.getArray())
				contracts.add(new Contract(this, i));
		}
		// Ανάγνωση τιμολογίων (πρώτα οι διαγωνισμοί, μετά οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		n                     = node.getField(H[18]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα τιμολογίων");
			for (Node i : n.getArray())
				invoices.add(new Invoice(this, i));
		}
		// Ανάγνωση φύλλου καταχώρησης (πρώτα οι διαγωνισμοί, μετά οι συμβάσεις, μετά τα τιμολόγια, μετά το φύλλο καταχώρησης)
		cfg = hasTenders();
		ContentItem.unserialize(node.getField(H[19]).getArray(), cfg, contents);
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Απόφαση Ανάληψης Υποχρέωσης", "Έργο", "ΕΦ", "ΑΛΕ", "Τύπος Χρηματοδότησης", "Τίτλος",
		"Απόφαση Απευθείας Ανάθεσης", "Διαβιβαστικό Δαπάνης", "Αυτόματοι Υπολογισμοί",
		"Αξκος Έργου", "Πρόεδρος Προσωρινής και Οριστικής Παραλαβής",
		"Α Μέλος Προσωρινής και Οριστικής Παραλαβής", "Β Μέλος Προσωρινής και Οριστικής Παραλαβής",
		"Πρόεδρος Αφανών Εργασιών", "Μέλος Αφανών Εργασιών",
		"Εργασίες", "Διαγωνισμοί", "Συμβάσεις", "Τιμολόγια", "Φύλλο Καταχώρησης"
	};

	/** Επιστρέφει array με όλα τα πρόσωπα που εμπλέκονται στα στοιχεία δαπάνης.
	 * Χρησιμοποιείται στην εισαγωγή προσώπων στο πρόγραμμα, από τα στοιχεία μιας δαπάνης. Δεν
	 * συμπεριλαμβάνει τα πρόσωπα από τα στοιχεία της Μονάδας.
	 * @returns Το array με τα πρόσωπα */
	Person[] getPersonnel() {
		return new Person[] {
			projectManager,
			acceptChief, acceptMemberA, acceptMemberB,
			worksChief, worksMember
		};
	}

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
		fields.addListVariableSerializable(H[19], contents);
	}
	/** Μετατρέπει τη δαπάνη σε php serialize string format, για αποθήκευση σε αρχείο.
	 * @return Μετατροπέας του αντικειμένου java σε php serialize string format. */
	VariableSerializable save() {
		return fields -> {
			serializeC(fields);
			fields.addListSerializable(H[19], ContentItem.save(contents));
		};
	}
	/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format.
	 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
	 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
	private void serializeC(VariableFields fields) {
		unitInfo.serialize(fields);	// Συγχώνευση των 2 αντικειμένων σε ένα
		if (orderDispensation != null)     fields.add(H[ 0],  orderDispensation);
		/* Το πεδίο υπάρχει πάντα σε μια δαπάνη. Τα PHP scripts, αν το εντοπίσουν καταλαβαίνουν ότι
		τα δεδομένα που εισήχθησαν είναι δαπάνη. Αν δεν υπάρχει καταλαβαίνουν ότι δεν είναι δαπάνη */
		                                   fields.add(H[ 1], construction);
		if (acb != 0)                      fields.add(H[ 2], acb);
		if (aae != 0)                      fields.add(H[ 3], aae);
		                                   fields.add(H[ 4], financing.toString());
		if (title != null)                 fields.add(H[ 5], title);
		if (orderDirectAssignment != null) fields.add(H[ 6], orderDirectAssignment);
		if (orderTransport != null)        fields.add(H[ 7], orderTransport);
		                                   fields.add(H[ 8], smart);
		if (projectManager != null)        fields.add(H[ 9], projectManager);
		if (acceptChief != null)           fields.add(H[10], acceptChief);
		if (acceptMemberA != null)         fields.add(H[11], acceptMemberA);
		if (acceptMemberB != null)         fields.add(H[12], acceptMemberB);
		if (worksChief != null)            fields.add(H[13], worksChief);
		if (worksMember != null)           fields.add(H[14], worksMember);
		if (!works.isEmpty())              fields.addListVariableSerializable(H[15], works);
		if (!tenders.isEmpty())            fields.addListVariableSerializable(H[16], tenders);
		if (!contracts.isEmpty())          fields.addListVariableSerializable(H[17], contracts);
		if (!invoices.isEmpty())           fields.addListVariableSerializable(H[18], invoices);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return null;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 1: return orderDispensation;
			case 2: return construction ? NOYES[1] : NOYES[0];
			case 3: return a(acb);
			case 4: return a(aae);
			case 5: return financing;
			case 6: return title;
			case 7: return orderDirectAssignment;
			case 8: return orderTransport;
			case 9: return null;	// Επικεφαλίδα «Αυτοματισμοί»
			case 10: return smart ? NOYES[1] : NOYES[0];
			case 11: return null; // Επικεφαλίδα «Επιτροπές Έργων»
			case 12: return projectManager;
			case 13: return acceptChief;
			case 14: return acceptMemberA;
			case 15: return acceptMemberB;
			case 16: return worksChief;
			case 17: return worksMember;
			default: return unitInfo.getCell(index - 18);
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: break;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 1: orderDispensation     = getString(value); break;
			case 2:		// Δαπάνη έργου ή όχι
				if ((value == NOYES[1]) != construction) {
					construction = !construction;
					if (smart) invoices.forEach(i -> i.recalcFromConstruction());
					calcContents();
					window.updatePanels();
				}
				break;
			case 3: acb                   = getLong(value); break;
			case 4: aae                   = getLong(value); break;
			case 5:		// Θέτει την πηγή χρηματοδότησης της δαπάνης
				if (value != financing) {
					financing = (Financing) value;
					if (smart) invoices.forEach(i -> i.recalcFromFinancing());
				}
				break;
			case 6: title                 = getString(value); break;
			case 7: orderDirectAssignment = getString(value); break;
			case 8: orderTransport        = getString(value); break;
			case 9: break;	// Επικεφαλίδα «Στοιχεία Δαπάνης»
			case 10:		// Απενεργοποιεί/ενεργοποιεί τον έλεγχο δεδομένων στα τιμολόγια
				if ((value == NOYES[1]) != smart)
					if (!smart) {
						if (CANCEL_OPTION == showConfirmDialog(window,
								"Επιλέξατε να ενεργοποιήσετε τον αυτόματο υπολογισμό.\n"
								+ "Ο αυτόματος υπολογισμός ενδέχεται να τροποποιήσει στοιχεία\n"
								+ "τιμολογίων, συμβάσεων και διαγωνισμών καθώς και το φύλλο καταχώρησης.\n"
								+ "Τυχόν τροποποιήσεις που θα γίνουν, είναι αδύνατο να αναιρεθούν.\n"
								+ "Θέλετε να συνεχίσετε;\n"
								, "Αυτοματισμός", OK_CANCEL_OPTION, WARNING_MESSAGE)) return;
						smart = true;
						invoices.forEach(i -> i.recalcFromSmart());
					} else smart = false;
				break;
			//case 11: break;
			case 12: projectManager   = (Person) value; break;
			case 13: acceptChief      = (Person) value; break;
			case 14: acceptMemberA    = (Person) value; break;
			case 15: acceptMemberB    = (Person) value; break;
			case 16: worksChief       = (Person) value; break;
			case 17: worksMember      = (Person) value; break;
			default: unitInfo.setCell(index - 18, value); break;
		}
	}

	/** Τροποποιεί το φύλλο καταχώρησης, αν απαιτείται, από τα στοιχεία της δαπάνης.
	 * Αν π.χ. αλλάζει ο τύπος διαγωνισμού της δαπάνης, αλλάζει και το φύλλο καταχώρησης.
	 * <p>Το πρόγραμμα προσπαθεί να διατηρήσει, στο μέτρο του δυνατού, τυχόν αλλαγές του χρήστη στις
	 * επιλογές των εγγραφών του φύλλου καταχώρησης, καθώς και τα οριζόμενα από το χρήστη
	 * δικαιολογητικά. */
	void calcContents() {
		if (cfg ^ hasTenders())
			convertContents(contents, cfg = !cfg);
	}

	/** Απαιτείται στη δημιουργία των προκαθορισμένων περιεχομένων της δαπάνης με βάση τον τύπο του διαγωνισμού.
	 * @return Ο τύπος του φύλλου καταχώρησης. false για απευθείας ανάθεση, true για διαγωνισμό. */
	private boolean hasTenders() { return !tenders.isEmpty(); }


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