package expenditure;

import java.util.Objects;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Ένας δικαιούχος / προμηθευτής / εργολάβος / μειοδότης. */
public class Contractor implements VariableSerializable, TableRecord {
	/** Η επωνυμία του δικαιούχου. */
	private String name;
	/** Ο τύπος του δικαιούχου. */
	private Type type;
	/** Το ΑΦΜ του δικαιούχου. */
	private String trn;			// Tax Registration Number
	/** Η ΔΟΥ του δικαιούχου. */
	private String taxOffice;
	/** Το e-mail του δικαιούχου. */
	private String email;
	/** Το τηλέφωνο του δικαιούχου. */
	private String phone;
	/** Ο Τ.Κ. του δικαιούχου. */
	private String zipCode;
	/** Η διεύθυνση του δικαιούχου. */
	private String address;
	/** Ο IBAN του δικαιούχου. */
	private String iban;		// Από εδώ και κάτω, για την υπεύθυνη δήλωση του IBAN
	/** Το ονοματεπώνυμο του δικαιούχου. */
	private String ownname;
	/** Το ονοματεπώνυμο του πατέρα του δικαιούχου. */
	private String fathername;
	/** Το ονοματεπώνυμο της μητέρας του δικαιούχου. */
	private String mothername;
	/** Η διεύθυνση διαμονής του δικαιούχου. */
	private String home_address;
	/** Η πόλη που γεννήθηκε ο δικαιούχος. */
	private String birthplace;
	/** Η ημερομηνία γέννησης του δικαιούχου. */
	private String birthdate;
	/** Ο αριθμός ταυτότητας του δικαιούχου. */
	private String policeid;

	/** Αρχικοποίηση του αντικειμένου. */
	Contractor() { type = Type.PRIVATE_SECTOR; }

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contractor(Node node) throws Exception {
		if (!node.isObject()) throw new Exception("Για δικαιούχο, αναμένονταν αντικείμενο");
		name       = node.getField(H[0]).getString();
		try { type = Type.valueOf(node.getField(H[1]).getString()); }
		catch(RuntimeException e) { type = Type.PRIVATE_SECTOR; }
		trn          = node.getField(H[2]).getString();
		taxOffice    = node.getField(H[3]).getString();
		email        = node.getField(H[4]).getString();
		phone        = node.getField(H[5]).getString();
		zipCode      = node.getField(H[6]).getString();
		address      = node.getField(H[7]).getString();
		iban         = node.getField(H[8]).getString();
		ownname      = node.getField(H[9]).getString();
		fathername   = node.getField(H[10]).getString();
		mothername   = node.getField(H[11]).getString();
		home_address = node.getField(H[12]).getString();
		birthplace   = node.getField(H[13]).getString();
		birthdate    = node.getField(H[14]).getString();
		policeid     = node.getField(H[15]).getString();
	}

	/** Ο τύπος του δικαιούχου. Π.χ. Δημόσιο, Ιδιώτης, κτλ
	 * @return Ο τύπος του δικαιούχου */
	Type getType() { return type; }

	/** Επικεφαλίδες του αντίστοιχου πίνακα, αλλά και ονόματα πεδίων αποθήκευσης. */
	static final String[] H = {
		"Επωνυμία", "Τύπος", "ΑΦΜ", "ΔΟΥ", "e-mail", "Τηλέφωνο", "Τ.Κ.", "Διεύθυνση", "ΙΒΑΝ",
		"Ονοματεπώνυμο", "Ονοματεπώνυμο Πατέρα", "Ονοματεπώνυμο Μητέρας",
		"Διεύθυνση Κατοικίας", "Τόπος Γέννησης", "Ημερομηνία Γέννησης", "ΑΔΤ"
	};

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
		if (name != null)         fields.add (H[0],  name);
		                          fields.add (H[1],  type.name());	// Δεν είναι ποτέ null
		if (trn != null)          fields.add (H[2],  trn);
		if (taxOffice != null)    fields.add (H[3],  taxOffice);
		if (email != null)        fields.add (H[4],  email);
		if (phone != null)        fields.add (H[5],  phone);
		if (zipCode != null)      fields.add (H[6],  zipCode);
		if (address != null)      fields.add (H[7],  address);
		if (iban != null)         fields.add (H[8],  iban);
		if (ownname != null)      fields.add (H[9], fathername);
		if (fathername != null)   fields.add (H[10], fathername);
		if (mothername != null)   fields.add (H[11], mothername);
		if (home_address != null) fields.add (H[12], home_address);
		if (birthplace != null)   fields.add (H[13], birthplace);
		if (birthdate != null)    fields.add (H[14], birthdate);
		if (policeid != null)     fields.add (H[15], policeid);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return type;
			case 2: return trn;
			case 3: return taxOffice;
			case 4: return email;
			case 5: return phone;
			case 6: return zipCode;
			case 7: return address;
			case 8: return iban;
			case 9: return ownname;
			case 10: return fathername;
			case 11: return mothername;
			case 12: return home_address;
			case 13: return birthplace;
			case 14: return birthdate;
			default: return policeid;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name          = getString(value); break;
			case 1: type          = (Type) value; break;
			case 2: trn           = getString(value); break;
			case 3: taxOffice     = getString(value); break;
			case 4: email         = getString(value); break;
			case 5: phone         = getString(value); break;
			case 6: zipCode       = getString(value); break;
			case 7: address       = getString(value); break;
			case 8: iban          = getString(value); break;
			case 9: ownname      = getString(value); break;
			case 10: fathername   = getString(value); break;
			case 11: mothername   = getString(value); break;
			case 12: home_address = getString(value); break;
			case 13: birthplace   = getString(value); break;
			case 14: birthdate    = getString(value); break;
			case 15: policeid     = getString(value); break;
		}
	}

	@Override public boolean isEmpty() {
		return name == null &&
				trn == null &&
				taxOffice == null &&
				email == null &&
				phone == null &&
				zipCode == null &&
				address == null &&
				iban == null &&
				ownname == null &&
				fathername == null &&
				mothername == null &&
				home_address == null &&
				birthplace == null &&
				birthdate == null &&
				policeid == null;
	}
}