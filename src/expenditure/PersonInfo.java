package expenditure;

import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Τα προσωπικά στοιχεία ενός ατόμου που απαιτούνται στην Υπεύθυνη Δήλωση */
final class PersonInfo implements VariableSerializable, TableRecord {
	/** Ονοματεπώνυμο. */
	String name;
	/** Ονοματεπώνυμο πατέρα. */
	String fathername;
	/** Ονοματεπώνυμο μητέρας. */
	String mothername;
	/** Ημερομηνία γέννησης στη μορφή '31 Δεκ 80'. */
	String birthdate;
	/** Τόπος γέννησης. */
	String birthplace;
	/** Αριθμός ταυτότητας. */
	String id;
	/** Αριθμός τηλεφώνου. */
	String phone;
	/** Διεύθυνση κατοικίας. Περιλαμβάνει πόλη ή χωριό, διεύθυνση, Τ.Κ. */
	String homeaddress;
	/** e-mail. */
	String email;

	/** Αρχικοποίηση με όλα τα στοιχεία κενά. */
	PersonInfo() {}

	/** Αρχικοποιεί ένα πρόσωπο από έναν node δεδομένων του unserialize().
	 * @param node Ο node δεδομένων */
	PersonInfo(Node node) {
		name        = node.getField(H[0]).getString();
		fathername  = node.getField(H[1]).getString();
		mothername  = node.getField(H[2]).getString();
		birthdate   = node.getField(H[3]).getString();
		birthplace  = node.getField(H[4]).getString();
		id          = node.getField(H[5]).getString();
		phone       = node.getField(H[6]).getString();
		homeaddress = node.getField(H[7]).getString();
		email       = node.getField(H[8]).getString();
	}

	/** Ονόματα πεδίων αποθήκευσης. */
	static final String[] H = { "Ονοματεπώνυμο", "Ονοματεπώνυμο Πατέρα", "Ονοματεπώνυμο Μητέρας",
		"Ημερομηνία Γέννησης", "Τόπος Γέννησης", "Αριθμός Ταυτότητας", "Τηλέφωνο",
		"Διεύθυνση Κατοικίας", "e-mail",
	};

	@Override public void serialize(VariableFields fields) {
		if (name != null)        fields.add(H[0], name);
		if (fathername != null)  fields.add(H[1], fathername);
		if (mothername != null)  fields.add(H[2], mothername);
		if (birthdate != null)   fields.add(H[3], birthdate);
		if (birthplace != null)  fields.add(H[4], birthplace);
		if (id != null)          fields.add(H[5], id);
		if (phone != null)       fields.add(H[6], phone);
		if (homeaddress != null) fields.add(H[7], homeaddress);
		if (email != null)       fields.add(H[8], email);
	}

	@Override public Object getCell(int index) {
		switch(index) {
			case 0: return name;
			case 1: return fathername;
			case 2: return mothername;
			case 3: return birthdate;
			case 4: return birthplace;
			case 5: return id;
			case 6: return phone;
			case 7: return homeaddress;
			default: return email;
		}
	}

	@Override public void setCell(int index, Object value) {
		String s = getString(value);
		switch(index) {
			case 0: name        = s; break;
			case 1: fathername  = s; break;
			case 2: mothername  = s; break;
			case 3: birthdate   = s; break;
			case 4: birthplace  = s; break;
			case 5: id          = s; break;
			case 6: phone       = s; break;
			case 7: homeaddress = s; break;
			case 8: email       = s; break;
		}
	}
}