package expenditure;

import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getDouble;
import static util.ResizableTableModel.getString;

/** Το υλικό που χρησιμοποιήθηκε για μια εργασία. */
final class Material implements VariableSerializable, TableRecord {
	/** Είδος. */
	private String name;
	/** Ποσότητα. */
	private double quantity;
	/** Μονάδα μέτρησης. */
	private String unit;

	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
	public Material() { quantity = 1; unit = "τεμάχια"; }

	/** Αρχικοποίηση του αντικειμένου από το είδος ενός τιμολογίου.
	 * @param i Το είδος ενός τιμολογίου */
	public Material(InvoiceItem i) { name = i.toString(); unit = i.getUnit(); quantity = i.getQuantity(); }

	/** Αρχικοποιεί ένα υλικό από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	public Material(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για υλικό, αναμένονταν αντικείμενο");
		name     = node.getField(H[0]).getString();
		quantity = node.getField(H[1]).getDecimal();
		unit     = node.getField(H[2]).getString();
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Υλικό", "Ποσότητα", "Μονάδα Mέτρησης" };

	@Override public String toString() { return name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)  fields.add(H[0], name);
		if (quantity != 0) fields.add(H[1], quantity);
		if (unit != null)  fields.add(H[2], unit);
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

	@Override public boolean isEmpty() { return name == null && quantity == 0; }
}
