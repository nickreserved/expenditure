package expenditure;

import static expenditure.Expenditure.a;
import java.util.ArrayList;
import java.util.Arrays;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getDouble;

/** Οι αναλυτικές κρατήσεις ενός τιμολογίου σε ποσοστά. */
final class Deduction implements VariableSerializable, TableRecord {
	/** Λίστα με όλες τις κρατήσεις.
	 * Οι δείκτες του array αντιστοιχούν στα ονόματα κρατήσεων στο DEDUCTION_HEADERS.
	 * Κατά συνέπεια, το άθροισμα των επιμέρους κρατήσεων είναι το πρώτο στοιχείο.
	 * <p>Για ταχύτητα, προτιμήθηκε αυτή η προσέγγιση, αντί για TreeMap<String, Double>. */
	private double[] term;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
	Deduction() { term = new double[TABLE_HEADER.size()]; }

	/** Αρχικοποιεί μια κράτηση από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Deduction(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για κράτηση, αναμένονταν αντικείμενο");
		// Οι επιμέρους κρατήσεις πρέπει να είναι αριθμοί
		String[] names = node.getFieldNames();
		for (String name : names)
			if (!node.getField(name).isDecimal()) throw new Exception("Για επιμέρους κράτηση, αναμένονταν δεκαδικός");
		// Νέα ονόματα κρατήσεων προστίθενται στις επικεφαλίδες του πίνακα
		for (String name : names)
			if (!TABLE_HEADER.contains(name)) TABLE_HEADER.add(name);
		// Διαβάζουμε τις επιμέρους κρατήσεις
		term = new double[TABLE_HEADER.size()];
		for (int z = 1; z < term.length; ++z)
			term[z] = node.getField(TABLE_HEADER.get(z)).getDecimal();
		recalculate();
	}

	/** Αρχικοποιεί μια κράτηση από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @return Η κράτηση ή null αν ο node είναι null
	 * @throws Exception Αν ο node δεν είναι αντικείμενο, ούτε null */
	static Deduction create(Node node) throws Exception {
		return node.isExist() && !node.isNull() ? new Deduction(node) : null;
	}

	/** Αρχικοποιεί μια κράτηση από ένα array επιμέρους κρατήσεων.
	 * @param terms Ένα array του οποίου, κάθε στοιχείο συμβαδίζει με το αντίστοιχο στοιχείο του
	 * TABLE_HEADER */
	private Deduction(double[] terms) { term = terms; }

	/** Λίστα με τα ονόματα όλων των κρατήσεων.
	 * Η λίστα χρησιμοποιείται σαν επικεφαλίδες στον πίνακα κρατήσεων.
	 * <p>Το πρώτο όνομα είναι δεσμευμένο για το σύνολο. Επιπλέον υπάρχουν κάποιες κρατήσεις που έχει
	 * παρατηρηθεί ότι δεν καταργούνται.
	 * <p>Στη λίστα πέρα από τις κρατήσεις που προϋπάρχουν, μπορούν να προστεθούν και άλλες, καθώς
	 * και να αφαιρεθούν.
	 * <p>Η λίστα χρησιμοποιείται επίσης σαν ονόματα πεδίων αποθήκευσης. */
	static final ArrayList<String> TABLE_HEADER = createTableHeader();
	/** Δημιουργεί στατικά τη λίστα με τους τίτλους των επιμέρους κρατήσεων. */
	static private ArrayList<String> createTableHeader() {
		ArrayList<String> a = new ArrayList<>(15);
		a.add("Σύνολο"); a.add("ΜΤΣ"); a.add("ΕΛΟΑΣ");
		a.add("ΕΑΔΗΣΥ"); a.add("ΒΑΜ"); a.add("ΕΚΟΕΜΣ");
		return a;
	}

	//                                                    Σύνολο,ΜΤΣ,ΕΛΟΑΣ,ΕΑΔΗΣΥ,ΒΑΜ,ΕΚΟΕΜΣ
	/** Προμήθεια με χρηματοδότηση τακτικού Π/Υ και (καθαρή αξία &lt;= 1000 ή μίσθωση ακινήτων). */
	static final Deduction D6    = new Deduction(new double[] {  6,   4, 2 });
	/** Προμήθεια με χρηματοδότηση τακτικού Π/Υ και καθαρή αξία > 1000 */
	static final Deduction D6_1  = new Deduction(new double[] {  6.1, 4, 2, 0.1 });
	/** Προμήθεια με χρηματοδότηση ιδίων πόρων και (καθαρή αξία &lt;= 1000 ή μίσθωση ακινήτων). */
	static final Deduction D16   = new Deduction(new double[] { 16,   4, 2, 0,   2, 8 });
	/** Προμήθεια με χρηματοδότηση ιδίων πόρων και καθαρή αξία > 1000. */
	static final Deduction D16_1 = new Deduction(new double[] { 16.1, 4, 2, 0.1, 2, 8 });
	/** Προμήθεια με χρηματοδότηση Π/Υ ΠΔΕ και (καθαρή αξία &lt;= 1000 ή μίσθωση ακινήτων). */
	static final Deduction D0    = new Deduction(new double[] { 0 });
	/** Προμήθεια με χρηματοδότηση Π/Υ ΠΔΕ και καθαρή αξία > 1000. */
	static final Deduction D0_1  = new Deduction(new double[] {  0.1, 0, 0, 0.1 });

	@Override public String toString() { return Double.toString(term[0]); }

	/** Υπολογισμός του αθροίσματος των κρατήσεων. */
	private void recalculate() {
		term[0] = 0;
		for (int z = 1; z < term.length; ++z)
			term[0] += term[z];
		term[0] = Math.round(term[0] * 100000) / 100000.0;
	}

	/** Το άθροισμα των επιμέρους κρατήσεων.
	 * @param Το άθροισμα των επιμέρους κρατήσεων */
	double sum() { return term[0]; }

	/** Συγκρίνει 2 κρατήσεις για ισότητα.
	 * @param ο Μια κράτηση που θα συγκριθεί με την τρέχουσα. Πρέπει να είναι τύπου
	 * Deduction οπωσδήποτε και όχι null.
	 * @returns true αν οι κρατήσεις είναι ίσες
	 * @throws ClassCastException Αν το o δεν είναι τύπου Deduction */
	@Override public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Deduction) {
			Deduction deduction = (Deduction) o;
			// Αν το σύνολο είναι ίσο, τυχόν παραπάνω στοιχεία, σε οποιαδήποτε από τις 2 κρατήσεις, θα είναι 0
			int length = Math.min(term.length, deduction.term.length);
			for (int z = 0; z < length; ++z)
				if (term[z] != deduction.term[z]) return false;
			return true;
		}
		return false;
	}
	// Πάει ζευγάρι με την equals. Χρησιμοποιείται από hash tables, κάτι που το πρόγραμμα δεν το κάνει
	// Προστέθηκε για να μη βγάζει warning το netbeans και ο κώδικας παράχθηκε αυτόματα από το netbeans
	@Override public int hashCode() { return 69 + Arrays.hashCode(term); }

	@Override public void serialize(VariableFields fields) {
		for (int z = 1; z < term.length; ++z)
			if (term[z] != 0) fields.add(TABLE_HEADER.get(z), term[z]);
	}

	@Override public Object getCell(int index) { return index < term.length ? a(term[index]) : null; }

	@Override public void setCell(int index, Object value) {
		if (index != 0) {	// Το άθροισμα δεν αλλάζει. Πρέπει να αλλαχθούν οι επιμέρους κρατήσεις.
			double v = getDouble(value);
			if (index >= term.length) {
				if (v == 0) return;
				term = Arrays.copyOf(term, index + 1);
			} else if (term[index] == v) return;
			term[index] = v;
			recalculate();
		}
	}

	@Override public boolean isEmpty() { return term[0] == 0; }
}