package expenditure;

import java.util.ArrayList;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getDouble;
import static util.ResizableTableModel.getString;

/** Μια εργασία που πραγματοποίησε ο εργολάβος σε δαπάνη έργου. */
final class Work implements VariableSerializable, TableRecord {
	/** Η περιγραφή μιας εργασίας. */
	private String name;
	/** Η ποσότητα μιας εργασίας. Μπορεί να είναι και 0, όταν π.χ. δεν έχει κάποια γνωστή μονάδα μέτρησης. */
	private double quantity;
	/** Η μονάδα μέτρησης μιας εργασίας. Π.χ. ώρες ή τεμάχια. */
	private String unit;
	/** Μια λίστα με τα υλικά και τις ποσότητες τους, που χρησιμοποιήθηκαν στην εργασία. */
	final ArrayList<Material> materials = new ArrayList<>();

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
	public Work() { quantity = 1; unit = "τεμάχια"; }

	/** Αρχικοποιεί μια εργασία από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	public Work(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για εργασία, αναμένονταν αντικείμενο");
		name     = node.getField(H[0]).getString();
		quantity = node.getField(H[1]).getDecimal();
		unit     = node.getField(H[2]).getString();
		// Ανάγνωση υλικών
		Node n = node.getField(H[3]);
		if (n.isExist()) {
			if (!n.isArray()) throw new Exception("Για εργασία, αναμένονταν λίστα υλικών");
			for (Node i : n.getArray())
				materials.add(new Material(i));
		}
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Εργασία", "Ποσότητα", "Μονάδα Mέτρησης", "Υλικά" };

	@Override public String toString() { return name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)         fields.add (H[0], name);
		if (quantity != 0)        fields.add (H[1], quantity);
		if (unit != null)         fields.add (H[2], unit);
		if (!materials.isEmpty()) fields.addV(H[3], materials);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return quantity;
			default: return unit;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name     = getString(value); break;
			case 1: quantity = getDouble(value); break;
			case 2: unit     = getString(value); break;
		}
	}

	@Override public boolean isEmpty() { return name == null && quantity == 0 && materials.isEmpty(); }
}