package expenditure;

import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Τα στοιχεία ενός προσώπου της Μονάδας / Υπηρεσίας. */
final class Person implements VariableSerializable, TableRecord {
	/** Στρατιωτικός βαθμός σε σύντμηση. Π.χ. Τχης (ΜΧ). */
	private String rank;
	/** Ονοματεπώνυμο. */
	private String name;
	/** Μονάδα στην οποία ανήκει, σε γενική μορφή με άρθρο. Π.χ. του 3 ΛΜΧ. */
	private String unit;

	/** Αρχικοποίηση του αντικειμένου. */
	Person() {}

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Person(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για πρόσωπο, αναμένονταν αντικείμενο");
		rank = node.getField(H[0]).getString();
		name = node.getField(H[1]).getString();
		unit = node.getField(H[2]).getString();
	}

	/** Ονόματα πεδίων αποθήκευσης.
	 * Αν αλλάξει οτιδήποτε, πρέπει να αναπροσαρμοστεί η κλήση MainFrame.createPersonnelPanel() */
	static final String[] H = { "Βαθμός", "Ονοματεπώνυμο", "Μονάδα" };

	@Override public String toString() { return rank + " " + name; }

	@Override public boolean equals(Object o) {
		if (o == this) return true;
		else if (o instanceof Person) {
			Person person = (Person) o;
			return Objects.equals(rank, person.rank) && Objects.equals(name, person.name);
		} else return false;
	}

	@Override public int hashCode() {	// Δημιουργήθηκε αυτόματα από το netbeans
		int hash = 7;
		hash = 83 * hash + Objects.hashCode(rank);
		hash = 83 * hash + Objects.hashCode(name);
		return hash;
	}

	@Override public void serialize(VariableFields fields) {
		if (rank != null) fields.add (H[0], rank);
		if (name != null) fields.add (H[1], name);
		if (unit != null) fields.add (H[2], unit);
	}

	@Override public String getCell(int index) {
		switch(index) {
			case 0: return rank;
			case 1: return name;
			default: return unit;
		}
	}

	@Override public void setCell(int index, Object value) {
		String s = getString(value);
		switch(index) {
			case 0: rank = s; break;
			case 1: name = s; break;
			case 2: unit = s; break;
		}
	}

	@Override public boolean isEmpty() { return rank == null && name == null && unit == null; }
}
