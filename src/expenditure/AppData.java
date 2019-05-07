package expenditure;

import expenditure.StatementDialog.Statement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import util.PhpSerializer;
import util.PhpSerializer.Fields;
import util.PhpSerializer.Node;
import util.PhpSerializer.Serializable;

/** Όλα τα δεδομένα του προγράμματος που έχουν χρησιμότητα για το χρήστη.
 * Ρυθμίσεις, ανοικτές δαπάνες, αμετάβλητα στοιχεία κτλ.
 * <p>Τα δεδομένα αυτά αποθηκεύονται από το πρόγραμμα κατά τον τερματισμό του, αλλά και σε τακτά
 * χρονικά διαστήματα, για λόγους ασφαλείας. */
final class AppData implements Serializable {
	/** Λίστα με τις ανοικτές δαπάνες. */
	final ArrayList<Expenditure> expenditures = new ArrayList<>();
	/** Το index της τρέχουσας δαπάνης, στη λίστα με τις ανοικτές δαπάνες. */
	int activeExpenditure;
	/** Λίστα με τις κρατήσεις. */
	ArrayList<Deduction> deductions = new ArrayList<>();
	/** Λίστα με το προσωπικό της Μονάδας / Υπηρεσίας. */
	ArrayList<Person> personnel = new ArrayList<>();
	/** Λίστα με τους δικαιούχους/εργολάβους/προμηθευτές/συμβεμβλημένους. */
	ArrayList<Contractor> contractors = new ArrayList<>();
	/** Στοιχεία σχετικά με τη Μονάδα/Υπηρεσία που δε μεταβάλλονται από δαπάνη σε δαπάνη. */
	UnitInfo unitInfo = new UnitInfo();
	/** Στοιχεία Υπεύθυνης Δήλωσης */
	Statement statement = new Statement();
	/** Το κέλυφος του προγράμματος. Καθαρά για λόγους εμφάνισης. */
	String skin;
	/** Αν ένα δικαιολογητικό απαιτεί περισσότερα του ενός αντίτυπα, εξήγαγε το μόνο μια φορά.
	 * Π.χ. η κατάσταση κρατήσεων πρέπει να υπάρχει στη δαπάνη 3 φορές. Αν είναι true, στο αρχείο
	 * της δαπάνης θα εξαχθεί μόνο μια φορά. Αν είναι false θα εξαχθεί 3 φορές. */
	boolean onlyOnce;
	/** Η έκδοση του προγράμματος με την οποία αποθηκεύτηκαν οι ρυθμίσεις.
	 * Αν υπάρχει διαφορά με το τρέχον πρόγραμμα, τότε ενσωματώνονται οι κρατήσεις από το default
	 * αρχείο cost.ini μέσα στο JAR, στις τρέχουσες ρυθμίσεις.
	 * <p>Το νόημα είναι ότι κάθε φορά που βγαίνουν νέες κρατήσεις και κάνει το πρόγραμμα ανανέωση,
	 * να περνιούνται οι νέες κρατήσεις σε ήδη χρησιμοποιημένα αρχεία ρυθμίσεων από παλιότερες
	 * εκδόσεις του προγράμματος. */
	String version;

	/** Αρχικοποιεί τα δεδομένα του προγράμματος. */
	AppData() {}

	/** Αρχικοποιεί τα δεδομένα του προγράμματος από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	AppData(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Εσφαλμένη δομή στα δεδομένα");
		// Ανάγνωση έκδοσης, μόνο αν υπάρχει
		version = node.getField(H[0]).getString();
		// Ανάγνωση δαπανών
		Node n = node.getField(H[1]);
		if (n.isExist()) {
			if (!n.isObject()) throw new Exception("Εσφαλμένη δομή στη λίστα δαπανών");
			for (String name : n.getFieldNames())
				expenditures.add(new Expenditure(new File(name), n.getField(name)));
		}
		// Ανάγνωση τρέχουσας δαπάνης
		activeExpenditure = (int) node.getField(H[2]).getInteger();
		// Ανάγνωση κρατήσεων
		n = node.getField(H[3]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα κρατήσεων");
			for (Node i : n.getArray())
				deductions.add(new Deduction(i));
		}
		// Ανάγνωση προσωπικού
		n = node.getField(H[4]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα προσωπικού");
			for (Node i : n.getArray())
				personnel.add(new Person(i));
		}
		// Ανάγνωση δικαιούχων
		n = node.getField(H[5]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Εσφαλμένη δομή στη λίστα δικαιούχων");
			for (Node i : n.getArray())
				contractors.add(new Contractor(i));
		}
		// Ανάγνωση στοιχείων Μονάδας
		unitInfo  = new UnitInfo(node.getField(H[6]));
		// Ανάγνωση στοιχείων Υπεύθυνης Δήλωσης
		statement = new Statement(node.getField(H[7]));
		// Ανάγνωση ρυθμίσεων
		skin      = node.getField(H[8]).getString();
		onlyOnce  = node.getField(H[9]).getBoolean();
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static private final String[] H = {
		"Έκδοση", "Δαπάνες", "Τρέχουσα Δαπάνη", "Κρατήσεις", "Προσωπικό", "Δικαιούχοι",
		"Στοιχεία Μονάδας", "Υπεύθυνη Δήλωση", "Κέλυφος", "Ένα Αντίγραφο"
	};


	@Override public void serialize(PhpSerializer export) throws IOException {
		// Όταν το πρόγραμμα αποθηκεύσει τα δεδομένα του, πρέπει για κάθε ανοικτή δαπάνη να
		// αποθηκεύσει και το αρχείο στο οποίο αυτή σώζεται. Επειδή όμως οι δαπάνες δεν αποθηκεύουν
		// το αρχείο τους οι ίδιες, ο παρακάτω κώδικας αποθηκεύει τις ανοικτές δαπάνες σε ένα map με
		// κλειδιά τα ονόματα αρχείων τους.
		Serializable expendituresWithFilename = (PhpSerializer exp) -> {
			Fields f = new Fields(exp, expenditures.size());
			for (Expenditure i : expenditures)
				f.write(i.file.getCanonicalPath(), i.save());
		};
		new Fields(export, H.length)
				.write (H[0], version)
				.write (H[1], expendituresWithFilename)
				.write (H[2], activeExpenditure)
				.writeV(H[3], deductions)
				.writeV(H[4], personnel)
				.writeV(H[5], contractors)
				.write (H[6], unitInfo)
				.write (H[7], statement.saveWithFilename())
				.write (H[8], skin)
				.write (H[9], onlyOnce);
	}

	/** Επιστρέφει την τρέχουσα δαπάνη που εμφανίζεται στο πρόγραμμα αυτή τη στιγμή.
	 * @return Η δαπάνη
	 * @throws IndexOutOfBoundsException Αν δεν υπάρχει ανοικτή δαπάνη στο πρόγραμμα */
	Expenditure getActiveExpenditure() { return expenditures.get(activeExpenditure); }
	/** Δεν υπάρχουν ανοικτές δαπάνες στο πρόγραμμα.
	 * @return Δεν υπάρχουν ανοικτές δαπάνες στο πρόγραμμα */
	boolean isEmpty() { return expenditures.isEmpty(); }
	/** Ελέγχει αν υπάρχει ανοικτή δαπάνη, με συγκεκριμένο όνομα αρχείου.
	 * @param file Το αρχείο της δαπάνης
	 * @return Αν υπάρχει ανοικτή δαπάνη με αυτό το όνομα αρχείου */
	boolean isExpenditureExist(File file) { return expenditures.stream().anyMatch(i -> i.file.equals(file)); }
	/** Προσθέτει μια νέα δαπάνη στις ανοικτές και την κάνει τρέχουσα στο παράθυρο του προγράμματος. */
	void addActiveExpenditure(Expenditure cost) {
		activeExpenditure = expenditures.size();
		expenditures.add(cost);
	}
}
