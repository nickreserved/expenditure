package expenditure;

import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel;
import util.ResizableTableModel;
import static util.ResizableTableModel.getString;

/** Ένας δικαιούχος / προμηθευτής / εργολάβος / μειοδότης. */
public class Contractor implements VariableSerializable, ResizableTableModel.TableRecord,
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

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contractor(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για δικαιούχο, αναμένονταν αντικείμενο");
		name        = node.getField(H[0]).getString();
		try { type  = Type.valueOf(node.getField(H[1]).getString()); }
		catch(RuntimeException e) { type = Type.PRIVATE_SECTOR; }
		trn         = node.getField(H[2]).getString();
		taxOffice   = node.getField(H[3]).getString();
		address     = node.getField(H[4]).getString();
		iban        = node.getField(H[5]).getString();
		person      = new PersonInfo(node);
	}

	/** Ο τύπος του δικαιούχου. Π.χ. Δημόσιο, Ιδιώτης, κτλ
	 * @return Ο τύπος του δικαιούχου */
	Type getType() { return type; }

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Επωνυμία", "Τύπος", "ΑΦΜ", "ΔΟΥ", "Διεύθυνση", "ΙΒΑΝ" };

	/** Ο τύπος του δικαιούχου. */
	enum Type {
		/** Ιδιωτικός τομέας: Ιδιωτικές επιχειρήσεις, ελεύθεροι επαγγελματίες κ.τ.λ.
		 * Έχουν ΦΠΑ, κρατήσεις και ΦΕ. */
		PRIVATE_SECTOR,
		/** Δημόσιος τομέας: Δημόσιες επιχειρήσεις και ΕΚΕΜΣ.
		 * Έχουν ΦΠΑ, δεν έχουν ΦΕ και τις κρατήσεις τις πληρώνουμε εμείς. */
		PUBLIC_SERVICES,
		/** Στρατιωτικά Πρατήρια, πλην ΕΚΕΜΣ.
		 * Δεν έχουν ΦΠΑ, ΦΕ και τις κρατήσεις τις πληρώνουμε εμείς. */
		ARMY;
		/** Ο τύπος του δικαιούχου με κείμενο. */
		static final private String[] TYPE = {
			"Ιδιωτικός Τομέας", "Δημόσιος Τομέας και ΕΚΕΜΣ", "Στρατιωτικά Πρατήρια, πλην ΕΚΕΜΣ"
		};
		@Override public String toString() { return TYPE[ordinal()]; }
	}

	@Override public String toString() { return name; }

	@Override public boolean equals(Object o) {	// Ίδια επωνυμία, ΑΦΜ και ΙΒΑΝ -> ίδιος δικαιούχος
		if (o == this) return true;
		else if (o instanceof Contractor) {
			Contractor contractor = (Contractor) o;
			return Objects.equals(trn, contractor.trn) && Objects.equals(name, contractor.name) &&
					Objects.equals(iban, contractor.iban);
		} else return false;
	}

	@Override public int hashCode() {	// Δημιουργήθηκε αυτόματα από το netbeans
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(name);
		hash = 47 * hash + Objects.hashCode(trn);
		hash = 47 * hash + Objects.hashCode(iban);
		return hash;
	}

	@Override public void serialize(VariableFields fields) {
		if (name != null)        fields.add (H[0],  name);
		                         fields.add (H[1],  type.name());	// Δεν είναι ποτέ null
		if (trn != null)         fields.add (H[2],  trn);
		if (taxOffice != null)   fields.add (H[3],  taxOffice);
		if (address != null)     fields.add (H[4],  address);
		if (iban != null)        fields.add (H[5],  iban);
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
			case 0: name         = getString(value); break;
			case 1: type         = (Type) value; break;
			case 2: trn          = getString(value); break;
			case 3: taxOffice    = getString(value); break;
			case 4: address      = getString(value); break;
			// case 5: break;
			case 6: iban         = getString(value); break;
			default: person.setCell(index - 7, value);
		}
	}

	@Override public boolean isEmpty() {
		return name == null &&
				trn == null &&
				taxOffice == null &&
				address == null &&
				iban == null &&
				person.birthdate == null &&
				person.birthplace == null &&
				person.email == null &&
				person.fathername == null &&
				person.homeaddress == null &&
				person.id == null &&
				person.mothername == null &&
				person.name == null &&
				person.phone == null;
	}
}