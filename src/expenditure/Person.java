package expenditure;

import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getLong;
import static util.ResizableTableModel.getString;

/** Τα στοιχεία ενός προσώπου της Μονάδας / Υπηρεσίας. */
final class Person implements VariableSerializable, TableRecord {
	/** Στρατιωτικός βαθμός σε σύντμηση. Π.χ. Τχης (ΜΧ). */
	private String rank;
	/** Ονοματεπώνυμο. */
	private String name;
	/** ΑΜ. */
	private long id;

	/** Αρχικοποίηση του αντικειμένου. */
	Person() {}

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Person(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για πρόσωπο, αναμένονταν αντικείμενο");
		rank = node.getField(H[0]).getString();
		name = node.getField(H[1]).getString();
		id = node.getField(H[2]).getInteger();
	}

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @return Το πρόσωπο ή null αν ο node είναι null
	 * @throws Exception Αν ο node δεν είναι αντικείμενο, ούτε null */
	static Person create(Node node) throws Exception {
		return node.isExist() && !node.isNull() ? new Person(node) : null;
	}

	/** Ονόματα πεδίων αποθήκευσης.
	 * Αν αλλάξει οτιδήποτε, πρέπει να αναπροσαρμοστεί η κλήση MainFrame.createPersonnelPanel() */
	static final String[] H = { "Βαθμός", "Ονοματεπώνυμο", "ΑΜ" };

	@Override public String toString() { return rank + " " + name; }

	@Override public boolean equals(Object o) {
		if (o == this) return true;
		else if (o instanceof Person) {
			Person person = (Person) o;
			return Objects.equals(rank, person.rank) && Objects.equals(name, person.name) && id == person.id;
		} else return false;
	}

	@Override public int hashCode() {	// Δημιουργήθηκε αυτόματα από το netbeans
		int hash = 7;
		hash = 83 * hash + Objects.hashCode(rank);
		hash = 83 * hash + Objects.hashCode(name);
		hash = 83 * hash + (int) id;
		return hash;
	}

	@Override public void serialize(VariableFields fields) {
		if (rank != null) fields.add(H[0], rank);
		if (name != null) fields.add(H[1], name);
		if (id != 0)      fields.add(H[2], id);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return rank;
			case 1: return name;
			default: return id == 0 ? "" : id;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: rank = getString(value); break;
			case 1: name = getString(value); break;
			case 2: id = getLong(value); break;
		}
	}

	@Override public boolean isEmpty() { return rank == null && name == null && id == 0; }
}
