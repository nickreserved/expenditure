package expenditure;

import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel;
import util.ResizableTableModel;
import static util.ResizableTableModel.getString;

/** Ένας δικαιούχος / προμηθευτής / εργολάβος / μειοδότης. */
final class Contractor implements VariableSerializable, ResizableTableModel.TableRecord,
		PropertiesTableModel.TableRecord {
	/** Η επωνυμία του δικαιούχου. */
	private String name;
	/** Ο τύπος του δικαιούχου. */
	private Type type;
	/** Το ΑΦΜ του δικαιούχου. */
	private String trn;			// Tax Registration Number
	/** Η ΔΟΥ του δικαιούχου. */
	private String taxOffice;
	/** Η διεύθυνση του δικαιούχου. */
	private String address;
	/** Ο IBAN του δικαιούχου. */
	private String iban;		// Από εδώ και κάτω, για την υπεύθυνη δήλωση του IBAN
	/** Προσωπικά στοιχεία προσώπου. */
	PersonInfo person;

	/** Αρχικοποίηση του αντικειμένου. */
	Contractor() { type = Type.PRIVATE_SECTOR; person = new PersonInfo(); }

	/** Αρχικοποιεί ένα δικαιούχο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contractor(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για δικαιούχο, αναμένονταν αντικείμενο");
		name      = node.getField(H[0]).getString();
		type      = Type.valueOf(node.getField(H[1]).getString());
		trn       = node.getField(H[2]).getString();
		taxOffice = node.getField(H[3]).getString();
		address   = node.getField(H[4]).getString();
		iban      = node.getField(H[5]).getString();
		person    = new PersonInfo(node);
	}

	/** Αρχικοποιεί ένα δικαιούχο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @return Ο δικαιούχος ή null αν ο node είναι null
	 * @throws Exception Αν ο node δεν είναι αντικείμενο, ούτε null */
	static Contractor create(Node node) throws Exception {
		return node.isExist() && !node.isNull() ? new Contractor(node) : null;
	}

	/** Ο τύπος του δικαιούχου. Π.χ. Δημόσιο, Ιδιώτης, κτλ
	 * @return Ο τύπος του δικαιούχου */
	Type getType() { return type; }

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Επωνυμία", "Τύπος", "ΑΦΜ", "ΔΟΥ", "Διεύθυνση", "ΙΒΑΝ" };

	@Override public String toString() { return name; }

	@Override public boolean equals(Object o) {	// Ίδια επωνυμία, ΑΦΜ και ΙΒΑΝ -> ίδιος δικαιούχος
		if (o == this) return true;
		else if (o instanceof Contractor) {
			Contractor contractor = (Contractor) o;
			return Objects.equals(trn, contractor.trn) && Objects.equals(name, contractor.name) &&
					Objects.equals(iban, contractor.iban) && Objects.equals(type, contractor.type);
		} else return false;
	}

	@Override public int hashCode() {	// Δημιουργήθηκε αυτόματα από το netbeans
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(name);
		hash = 47 * hash + Objects.hashCode(trn);
		hash = 47 * hash + Objects.hashCode(iban);
		hash = 47 * hash + Objects.hashCode(type);
		return hash;
	}

	@Override public void serialize(VariableFields fields) {
		if (name != null)      fields.add (H[0], name);
		                       fields.add (H[1], type.toString());	// Δεν είναι ποτέ null
		if (trn != null)       fields.add (H[2], trn);
		if (taxOffice != null) fields.add (H[3], taxOffice);
		if (address != null)   fields.add (H[4], address);
		if (iban != null)      fields.add (H[5], iban);
		person.serialize(fields);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return type;
			case 2: return trn;
			case 3: return taxOffice;
			case 4: return address;
			case 5: return null;
			case 6: return iban;
			default: return person.getCell(index - 7);
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name      = getString(value); break;
			case 1: type      = (Type) value; break;
			case 2: trn       = getString(value); break;
			case 3: taxOffice = getString(value); break;
			case 4: address   = getString(value); break;
			case 5: break;
			case 6: iban      = getString(value); break;
			default: person.setCell(index - 7, value);
		}
	}

	@Override public boolean isEmpty() {
		return name == null &&
				trn == null &&
				taxOffice == null &&
				address == null &&
				iban == null &&
				person.name == null &&
				person.fathername == null &&
				person.mothername == null &&
				person.birthdate == null &&
				person.birthplace == null &&
				person.id == null &&
				person.phone == null &&
				person.homeaddress == null &&
				person.email == null;
	}


	/** Ο τύπος του δικαιούχου. */
	static final class Type {
		/** Ιδιωτική αρχικοποίηση του enum. */
		private Type(String s) { a = s; }
		/** Ο τύπος του δικαιούχου με κείμενο. */
		final private String a;
		@Override public String toString() { return a; }
		/** Λαμβάνει τον τύπο του δικαιούχου από το κείμενο περιγραφής του.
		 * Αν το κείμενο είναι εσφαλμένο ή null επιστρέφει PRIVATE_SECTOR.
		 * @param s Ο τύπος του δικαιούχου σε κείμενο
		 * @return Ο τύπος του δικαιούχου */
		static Type valueOf(String s) {
			if (PUBLIC_SERVICES.a.equals(s)) return PUBLIC_SERVICES;
			if (ARMY.a.equals(s)) return ARMY;
			return PRIVATE_SECTOR;
		}
		/** Ιδιωτικός τομέας: Ιδιωτικές επιχειρήσεις, ελεύθεροι επαγγελματίες κ.τ.λ.
		 * Έχουν ΦΠΑ, κρατήσεις και ΦΕ. */
		static final Type PRIVATE_SECTOR = new Type("Ιδιωτικός Τομέας");
		/** Δημόσιος τομέας: Δημόσιες επιχειρήσεις και ΕΚΕΜΣ.
		 * Έχουν ΦΠΑ, δεν έχουν ΦΕ και τις κρατήσεις τις πληρώνουμε εμείς. */
		static final Type PUBLIC_SERVICES = new Type("Δημόσιος Τομέας και ΕΚΕΜΣ");
		/** Ο τύπος του δικαιούχου με κείμενο. */
		static final Type ARMY = new Type("Στρατιωτικά Πρατήρια, πλην ΕΚΕΜΣ");
		/** Επιστρέφει λίστα με όλους τους τύπους δικαιούχου.
		 * @return Λίστα με όλους τους τύπους δικαιούχου */
		static Type[] values() { return new Type[] { PRIVATE_SECTOR, PUBLIC_SERVICES, ARMY }; }
	}
}