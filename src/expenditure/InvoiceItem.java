package expenditure;

import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getByte;
import static util.ResizableTableModel.getDouble;
import static util.ResizableTableModel.getString;

/** Το είδος ενός τιμολογίου. */
final class InvoiceItem implements VariableSerializable, TableRecord {
	/** Είδος. */
	private String name;
	/** Ποσότητα. */
	private double quantity;
	/** Καθαρή αξία της μονάδας. */
	private double price;
	/** Ποσοστό ΦΠΑ. */
	private byte vat;
	/** Μονάδα μέτρησης. */
	private String unit;

	/** Καθαρή αξία ολόκληρης της ποσότητας. Υπολογίζεται από τις υπόλοιπες τιμές. */
	private double priceTotal;
	/** Καθαρή αξία + ΦΠΑ της μονάδας. Υπολογίζεται από τις υπόλοιπες τιμές. */
	private double priceVAT;
	/** Καθαρή αξία + ΦΠΑ, ολόκληρης της ποσότητας. Υπολογίζεται από τις υπόλοιπες τιμές. */
	private double priceVATTotal;

	/** Το τιμολόγιο στο οποίο ανήκει το είδος τιμολογίου. */
	final Invoice parent;

	/** Αρχικοποίει το είδος του τιμολογίου με τις προκαθορισμένες τιμές.
	 * @param parent Το πατρικό τιμολόγιο */
	InvoiceItem(Invoice parent) {
		this.parent = parent; quantity = 1; unit = "τεμάχια";
		// Το νέο είδος τιμολογίου γίνεται η χοντροειδής παραδοχή ότι θα δημιουργηθεί στο τέλος της
		// λίστας ειδών του τιμολογίου, οπότε του δίνουμε το ΦΠΑ του τελευταίου είδους που βρίσκεται
		// στη λίστα ειδών του τιμολογίου. Αν η λίστα είναι άδεια του δίνουμε την προκαθορισμένη τιμή
		calcVAT(parent.items.isEmpty() ? DEFAULT_VAT : parent.items.get(parent.items.size() - 1).vat);
	}

	/** Αρχικοποιεί ένα είδος τιμολογίου από έναν node δεδομένων του unserialize().
	 * @param parent Το πατρικό τιμολόγιο
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	InvoiceItem(Invoice parent, Node n) throws Exception {
		this.parent = parent;
		if (!n.isObject()) throw new Exception("Για είδος τιμολογίου, αναμένονταν αντικείμενο");
		name     = n.getField(H[0]).getString();
		quantity = n.getField(H[1]).getDecimal();
		price    = n.getField(H[2]).getDecimal();
		vat      = (byte) n.getField(H[4]).getInteger();
		unit     = n.getField(H[7]).getString();
		calc();
	}

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Είδος", "Ποσότητα", "Τιμή Μονάδας", "Συνολική Τιμή", "ΦΠΑ", "Τιμή Mονάδας με ΦΠΑ",
		"Συνολική Τιμή με ΦΠΑ", "Μονάδα Mέτρησης"
	};

	/** Επιστρέφει τη μονάδα μέτρησης του είδους. */
	String getUnit() { return unit; }
	/** Επιστρέφει την ποσότητα του είδους. */
	double getQuantity() { return quantity; }
	/** Επιστρέφει την καθαρή αξία του είδους για όλη την ποσότητα. */
	double getNetPrice() { return priceTotal; }
	/** Επιστρέφει το ΦΠΑ σε € του είδους για όλη την ποσότητα. */
	double getVATPrice() { return priceTotal * vat / 100; }

	@Override public String toString() { return name; }

	@Override public void serialize(VariableFields fields) {
		if (name != null)  fields.add(H[0], name);
		if (quantity != 0) fields.add(H[1], quantity);
		if (price != 0)    fields.add(H[2], price);
		                   fields.add(H[4], vat);	// Το ΦΠΑ και μηδενικό, εξάγεται
		if (unit != null)  fields.add(H[7], unit);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return quantity;
			case 2: return price;
			case 3: return priceTotal;
			case 4: return vat;
			case 5: return priceVAT;
			case 6: return priceVATTotal;
			default: return unit;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name = getString(value); break;
			case 1: quantity = getDouble(value); recalc(); break;
			case 2: price = getDouble(value); recalc(); break;
			case 3:
				price = getDouble(value);
				if (quantity != 0) price /= quantity;
				price = Math.round(price * 10000) / 10000.0;
				recalc();
				break;
			case 4: calcVAT(getByte(value)); recalc(); break;
			case 5:
				price = Math.round(getDouble(value) * 100 / (100 + vat) * 10000) / 10000.0;
				recalc();
				break;
			case 6:
				price = getDouble(value) * 100 / (100 + vat);
				if (quantity != 0) price /= quantity;
				price = Math.round(price * 10000) / 10000.0;
				recalc();
				break;
			case 7: unit = getString(value); break;
		}
	}

	@Override public boolean isEmpty() { return name == null && quantity == 0 && price == 0; }

	/** Υπολογισμός τιμών που εξαρτώνται από άλλες. */
	private void calc() {
		priceTotal    = Math.round(price * quantity * 1000) / 1000.0;
		priceVATTotal = Math.round(priceTotal * (100 + vat) * 10) / 1000.0;
		priceVAT      = Math.round(price * (100 + vat) * 10) / 1000.0;
	}

	/** Υπολογισμός τιμών που εξαρτώνται από άλλες σε όλη την ιεραρχία δεδομένων. */
	private void recalc() { calc(); parent.recalcFromItems(); }

	/** Το πιο συχνά χρησιμοποιούμενο ΦΠΑ. */
	static final private byte DEFAULT_VAT = 24;

	/** Θέτει το ΦΠΑ του είδους σε 0.
	 * Καλείται από το τιμολόγιο.
	 * @return Πραγματοποιήθηκε αλλαγή */
	boolean setVAT0() {
		if (vat != 0) {
			vat = 0; calc();
			return true;
		} else return false;
	}
	/** Θέτει το ΦΠΑ του είδους σε κάτι διαφορετικό από το 0.
	 * Καλείται από το τιμολόγιο.
	 * @return Πραγματοποιήθηκε αλλαγή */
	boolean setVATnot0() {
		if (vat == 0) {
			vat = DEFAULT_VAT; calc();
			return true;
		} else return false;
	}

	/** Θέτει το ποσοστό ΦΠΑ του είδους.
	 * Αν έχουμε αυτόματους υπολογισμούς, το κάνει με βάση τα υπόλοιπα στοιχεία της δαπάνης.
	 * @param tax Το ΦΠΑ που προτείνεται και θα χρησιμοποιηθεί αν είναι συμβατό με τα υπόλοιπα
	 * στοιχεία της δαπάνης. */
	private void calcVAT(byte tax) {
		if (parent.parent.isSmart()) {
			if (Invoice.hasVAT(parent.getType(), parent.getContractor())) {
				if (tax != 0) vat = tax;
				else if (vat == 0) vat = DEFAULT_VAT;
			} else vat = 0;
		} else vat = tax;
	}
}