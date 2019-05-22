package expenditure;

import static expenditure.MainFrame.NOYES;
import java.util.ArrayList;
import java.util.List;
import util.PhpSerializer;
import util.PhpSerializer.Fields;
import util.PhpSerializer.Node;
import util.PhpSerializer.Serializable;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getByte;
import static util.ResizableTableModel.getString;

/** Ένα δικαιολογητικό. */
final class ContentItem implements VariableSerializable, TableRecord {
	/** Όνομα δικαιολογητικού. */
	private String name;
	/** Όνομα του PHP script του δικαιολογητικού.
	 * Αν είναι null, δεν εξάγεται κάποιο έντυπο, αλλά καταχωρείται στο φύλλο καταχώρησης.
	 */
	private String script;
	/** Αριθμός αντιτύπων του δικαιολογητικού. */
	private byte copies;
	/** Πλεγμένη πληροφορία για το δικαιολογητικό στα bits του byte.
	 * Όταν είσαι παλιά καραβάνα στον προγραμματισμό και θέλεις να αποφύγεις άπειρα πεδία ή άπειρες
	 * κληρονομικότητες και πολυμορφισμούς, χρησιμοποιείς ένα byte με packed δεδομένα.
	 * <ul><li><strong>bit 0:</strong>
	 * <ul><li><strong>!script, !bit0:</strong> Το δικαιολογητικό δεν είναι από τα προκαθορισμένα
	 * του προγράμματος - έχει προστεθεί από το χρήστη. Δεν έχει κάποιο εξαγώμενο έντυπο, απλά
	 * καταχωρείται στο φύλλο καταχώρησης.
	 * <li><strong>!script, bit0:</strong> Το δικαιολογητικό δεν έχει κάποιο εξαγώμενο έντυπο, απλά
	 * καταχωρείται στο φύλλο καταχώρησης.
	 * <li><strong>script, !bit0:</strong> Το δικαιολογητικό έχει εξαγώμενο έντυπο και καταχωρείται
	 * και στο φύλλο καταχώρησης.
	 * <li><strong>script, bit0:</strong> Το δικαιολογητικό έχει εξαγώμενο έντυπο, αλλά δεν
	 * καταχωρείται στο φύλλο καταχώρησης.</ul>
	 * <li><strong>bit 2-1:</strong> Αν το δικαιολογητικό έχει προστεθεί από το χρήστη, είναι 11.
	 * <ul><li><strong>00:</strong> Η προκαθορισμένη ενέργεια για το δικαιολογητικό, είναι να μην
	 * εξάγεται το έντυπο, ούτε να καταχωρείται στο φύλλο καταχώρησης. Ο χρήστης μπορεί να
	 * τροποποιήσει αυτή την ενέργεια.
	 * <li><strong>01:</strong> Η προκαθορισμένη ενέργεια για το δικαιολογητικό, είναι να εξάγεται το
	 * το έντυπο και να καταχωρείται και στο φύλλο καταχώρησης. Αν το δικαιολογητικό υποστηρίζει μόνο
	 * κάτι από αυτά τα δύο, τότε αναφέρεται μόνο σε αυτό. Ο χρήστης μπορεί να τροποποιήσει αυτή την
	 * ενέργεια.
	 * <li><strong>10:</strong> Αφορά μόνο την περίπτωση που το δικαιολογητικό έχει και εξαγώμενο
	 * έντυπο και καταχωρείται στο φύλλο καταχώρησης. Στην περίπτωση αυτή η προκαθορισμένη ενέργεια
	 * είναι να μην εξάγεται το έντυπο αλλά να καταχωρείται στο φύλλο καταχώρησης. Ο χρήστης μπορεί
	 * να τροποποιήσει αυτή την ενέργεια.
	 * <li><strong>11:</strong> Η προκαθορισμένη ενέργεια για το δικαιολογητικό, είναι να εξάγεται το
	 * το έντυπο και να καταχωρείται και στο φύλλο καταχώρησης. Αν το δικαιολογητικό υποστηρίζει μόνο
	 * κάτι από αυτά τα δύο, τότε αναφέρεται μόνο σε αυτό. Ο χρήστης δεν μπορεί να τροποποιήσει αυτή
	 * την ενέργεια.</ul>
	 * <li><strong>bit 4-3:</strong> Αν ο χρήστης δεν μπορεί να τροποποιήσει την προκαθορισμένη
	 * ενέργεια ή το δικαιολογητικό έχει προστεθεί από το χρήστη, είναι 01.
	 * <ul><li><strong>00:</strong> Η ορισμένη από το χρήστη ενέργεια για το δικαιολογητικό, είναι
	 * να μην εξάγεται το έντυπο, ούτε να καταχωρείται στο φύλλο καταχώρησης.
	 * <li><strong>01:</strong> Η ορισμένη από το χρήστη ενέργεια για το δικαιολογητικό, είναι να
	 * εξάγεται το έντυπο και να καταχωρείται και στο φύλλο καταχώρησης. Αν το δικαιολογητικό
	 * υποστηρίζει μόνο κάτι από αυτά τα δύο, τότε αναφέρεται μόνο σε αυτό.
	 * <li><strong>10:</strong> Αφορά μόνο την περίπτωση που το δικαιολογητικό έχει και εξαγώμενο
	 * έντυπο και καταχωρείται στο φύλλο καταχώρησης. Στην περίπτωση αυτή η ορισμένη από το χρήστη
	 * ενέργεια είναι να μην εξάγεται το έντυπο αλλά να καταχωρείται στο φύλλο καταχώρησης.</ul></ul> */
	private byte type;

	/** Το δικαιολογητικό θα εξαχθεί.
	 * Αν δεν μπορεί να εξάγει έντυπο απλά καταχωρείται στο φύλλο καταχώρησης. Αν μπορεί να εξάγει
	 * έντυπο, το εξάγει. Αν μπορεί να κάνει και τα δύο, κάνει και τα δύο. */
	static final private byte VALUE_YES = 32;
	/** Το δικαιολογητικό δε θα εξαχθεί, ούτε θα καταχωρηθεί στο φύλλο καταχώρησης. */
	static final private byte VALUE_NO = 0;
	/** Το δικαιολογητικό θα καταχωρηθεί στο φύλλο καταχώρησης.
	 * Αφορά μόνο δικαιολογητικά που μπορούν και να εξαχθούν και να καταχωρηθούν στο φύλλο καταχώρησης. */
	static final private byte VALUE_LIST = 64;
	/** Μάσκα για το αν θα εξαχθεί το δικαιολογητικό και πως. */
	static final private byte VALUE_MASK = 32 | 64;
	/** Μάσκα για όλες τις άλλες τιμές του type εκτός από το αν και πως θα εξαχθεί το δικαιολογητικό. */
	static final private byte VALUE_MASK_INVERT = 1 | 2 | 4 | 8 | 16;

	static final private byte DEFAULT_NO = 0;
	static final private byte DEFAULT_YES = 8;
	static final private byte DEFAULT_LIST = 16;
	static final private byte DEFAULT_MASK = 8 | 16;

	static final private byte CHOICES_YES_FIXED = 0;
	static final private byte CHOICES_YES_NO = 2;
	static final private byte CHOICES_YES_LIST = 4;
	static final private byte CHOICES_YES_NO_LIST = 2 | 4;
	static final private byte CHOICES_MASK = 2 | 4;
	static final private byte LISTED_XOR_EXPORTED = 1;

	static final private byte INIT_YES_FIXED = CHOICES_YES_FIXED | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNO_NO = CHOICES_YES_NO | DEFAULT_NO | VALUE_NO;
	static final private byte INIT_YESNO_YES = CHOICES_YES_NO | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNOLIST_NO = CHOICES_YES_NO_LIST | DEFAULT_NO | VALUE_NO;
	static final private byte INIT_YESNOLIST_YES = CHOICES_YES_NO_LIST | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNOLIST_LIST = CHOICES_YES_NO_LIST | DEFAULT_LIST | VALUE_LIST;
	static final private byte INIT_YESLIST_YES = CHOICES_YES_LIST | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESLIST_LIST = CHOICES_YES_LIST | DEFAULT_LIST | VALUE_LIST;

	/** Το δικαιολογητικό το δημιούργησε ο χρήστης. */
	boolean isUserDefined() { return script == null && (type & LISTED_XOR_EXPORTED) == 0; }
	/** Το δικαιολογητικό δύναται και να εξάγει έγγραφο και να καταχωρηθεί στο φύλλο καταχώρησης.
	 * Μπορεί με βάση τις ρυθμίσεις να εξάγει. Αλλά εδώ γίνεται αναφορά στη δυνατότητα και όχι στο
	 * τι θα κάνει τελικά. */
	private boolean isAbleListedExported() { return script != null && (type & LISTED_XOR_EXPORTED) == 0; }
	/** Το δικαιολογητικό επιδέχεται αλλαγές από το χρήστη στο τι θα εξάγει. */
	boolean hasChoice() { return (type & CHOICES_MASK) != CHOICES_YES_FIXED; }
	/** Το δικαιολογητικό λαμβάνει τιμές εξαγωγής Όχι και Ναι.
	 * Το Ναι σημαίνει ότι ότι το δικαιολογητικό εξάγεται στο μέγιστο δυνατό βαθμό. Δηλαδή αν μπορεί
	 * να καταχωρηθεί στο φύλλο καταχώρησης το κάνει και αν μπορεί να εξάγει έντυπο το κάνει. */
	boolean hasChoiceNoYes() { return (type & CHOICES_MASK) == CHOICES_YES_NO; }
	/** Το δικαιολογητικό λαμβάνει τιμές εξαγωγής Όχι, Ναι και Μόνο καταχώρηση.
	 * Υπονοείται ότι το δικαιολογητικό και μπορεί να καταχωρηθεί στο φύλλο καταχώρησης και μπορεί
	 * να εξάγει έντυπο. Με το Ναι τα κάνει και τα δύο. */
	boolean hasChoiceNoYesList() { return (type & CHOICES_MASK) == CHOICES_YES_NO_LIST; }
	/** Το δικαιολογητικό λαμβάνει τιμές εξαγωγής Ναι και Μόνο καταχώρηση.
	 * Υπονοείται ότι το δικαιολογητικό και μπορεί να καταχωρηθεί στο φύλλο καταχώρησης και μπορεί
	 * να εξάγει έντυπο. Με το Ναι τα κάνει και τα δύο. */
	private boolean hasChoiceYesList() { return (type & CHOICES_MASK) == CHOICES_YES_LIST; }
	/** Επιστρέφει την ενέργεια εξαγωγής του δικαιολογητικού.
	 * @return 0: Ούτε εξάγεται έντυπο, ούτε καταχωρείται στο φύλλο καταχώρησης. 1: Εξάγεται έντυπο
	 * και καταχωρείται στο φύλλο καταχώρησης (αν το δικαιολογητικό υποστηρίζει μόνο το ένα από τα
	 * δύο, τότε αφορά αυτό που υποστηρίζει). 2: Καταχωρείται μόνο στο φύλλο καταχώρησης (αφορά μόνο
	 * τα δικαιολογητικά που και εξάγονται και καταχωρούνται στο φύλλο καταχώρησης). */
	private int getActionInt() { return (type >> 5) & 3; }
	/** Επιστρέφει την προκαθορισμένη ενέργεια εξαγωγής του δικαιολογητικού.
	 * @return 0: Ούτε εξάγεται έντυπο, ούτε καταχωρείται στο φύλλο καταχώρησης. 1: Εξάγεται έντυπο
	 * και καταχωρείται στο φύλλο καταχώρησης (αν το δικαιολογητικό υποστηρίζει μόνο το ένα από τα
	 * δύο, τότε αφορά αυτό που υποστηρίζει). 2: Καταχωρείται μόνο στο φύλλο καταχώρησης (αφορά μόνο
	 * τα δικαιολογητικά που και εξάγονται και καταχωρούνται στο φύλλο καταχώρησης). */
	private int getDefaultActionInt() { return (type >> 3) & 3; }
	/** Θέτει την ενέργεια ενός δικαιολογητικού σε μη εξαγωγή εντύπου, ούτε καταχώρηση στο φύλλο καταχώρησης.
	 * Βεβαιωθείτε ότι το δικαιολογητικό είναι προκαθορισμένο και επιτρέπεται η αλλαγή της ενέργειας
	 * εξαγωγής. */
	private void setNo() { type &= VALUE_MASK_INVERT; type |= VALUE_NO; }
	/** Θέτει την ενέργεια ενός δικαιολογητικού σε εξαγωγή εντύπου και καταχώρηση στο φύλλο καταχώρησης.
	 * Αν το δικαιολογητικό υποστηρίζει μόνο ένα από τα παραπάνω, θα πραγματοποιηθεί μόνο αυτό το ένα.
	 * <p>Βεβαιωθείτε ότι το δικαιολογητικό είναι προκαθορισμένο και επιτρέπεται η αλλαγή της
	 * ενέργειας εξαγωγής. */
	private void setYes() { type &= VALUE_MASK_INVERT; type |= VALUE_YES; }
	/** Θέτει την ενέργεια ενός δικαιολογητικού σε μη εξαγωγή εντύπου, αλλά καταχώρηση στο φύλλο καταχώρησης.
	 * Βεβαιωθείτε ότι το δικαιολογητικό είναι προκαθορισμένο, επιτρέπεται η αλλαγή της ενέργειας
	 * εξαγωγής και έχει τη δυνατότητα να εξάγει και έντυπο και να καταχωρηθεί σε φύλλο καταχώρησης. */
	private void setListedOnly() { type &= VALUE_MASK_INVERT; type |= VALUE_LIST; }

	/** Αρχικοποίηση καθορισμένου από το χρήστη δικαιολογητικού. */
	ContentItem() { this(null, (byte) 1); }

	/** Αρχικοποίηση δικαιολογητικού με copy constructor. */
	ContentItem(ContentItem i) { name = i.name; script = i.script; copies = i.copies; type = i.type; }

	/** Αρχικοποίηση καθορισμένου από το χρήστη δικαιολογητικού.
	 * @param name Όνομα δικαιολογητικού
	 * @param copies Ο αριθμός αντιτύπων */
	private ContentItem(String name, byte copies) { this(name, null, INIT_YES_FIXED, copies); }

	/** Αρχικοποίηση του δικαιολογητικού.
	 * @param name Όνομα δικαιολογητικού
	 * @param script Όνομα του PHP script του δικαιολογητικού. Αν είναι null, δεν εξάγεται κάποιο
	 * έντυπο, αλλά καταχωρείται στο φύλλο καταχώρησης.
	 * @param type Ο τύπος εξαγωγής του δικαιολογητικού
	 * @param copies Ο αριθμός αντιτύπων για μη προκαθορισμένα δικαιολογητικά, ή για προκαθορισμένα
	 * τι θα εξαχθεί και αν θα εξαχθεί */
	private ContentItem(String name, String script, byte type, byte copies) {
		this.name = name; this.script = script; this.type = type; this.copies = copies;
	}

	/** Επιστρέφει το όνομα του δικαιολογητικού.
	 * @return Το όνομα του δικαιολογητικού */
	@Override public String toString() { return name; }

	/** Επικεφαλίδες του πίνακα και ονόματα πεδίων αποθήκευσης για κάθε εγγραφή του φύλλου καταχώρησης. */
	static final String[] H = { "Δικαιολογητικό", "Πλήθος" };
	/** Επιλογή εμφάνισης για δικαιολογητικό που δύναται και να εξαχθεί οστόσο μόνο καταχωρείται. */
	static final String ONLY_LISTED = "Μόνο καταχώρηση";
	/** Όνομα πεδίου αποθήκευσης για εγγραφή του φύλλου καταχώρησης κατασκευασμένη από το χρήστη. */
	static final private String AFTER = "Μετά από";
	/** Όνομα πεδίου αποθήκευσης για τον τύπο εξαγωγής προκαθορισμένης εγγραφής του φύλλου καταχώρησης. */
	static final private String EXPORT = "Εξαγωγή";

	/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format για επεξεργασία από PHP script.
	 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
	 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
	@Override public void serialize(VariableFields fields) {
		boolean export = script != null && (type & VALUE_MASK) == VALUE_YES;
		boolean enlist = copies > 0 && (type & VALUE_MASK) != VALUE_NO &&
				(script == null || (type & LISTED_XOR_EXPORTED) == 0);
		if (name != null) fields.add(H[0],         name);
		if (copies != 0)  fields.add(H[1],         copies);
		if (export)       fields.add("Αρχείο",     script);
		                  fields.add("Καταχώρηση", enlist);
	 }

	/** Προετοιμάζει μια λίστα φύλλου καταχώρησης για αποθήκευση σε αρχείο.
	 * Όταν αποθηκεύεται η δαπάνη σε αρχείο, δεν αποθηκεύονται μαζί της όλες οι εγγραφές του φύλλου
	 * καταχώρησης. Αποθηκεύονται μόνο όσες δεν είναι προκαθορισμένες (δημιουργήθηκαν δηλαδή από το
	 * χρήστη), καθώς και οι προκαθορισμένες που έχουν ρύθμιση εξαγωγής διαφορετική από την
	 * προκαθορισμένη.
	 * @param contents Η λίστα εγγραφών του φύλλου καταχώρησης για προετοιμασία
	 * @return Λίστα με τις τροποποιήσεις της προκαθορισμένης λίστας εγγραφών του φύλλου καταχώρησης */
	static ArrayList<Serializable> save(List<ContentItem> contents) {
		ArrayList<Serializable> save = new ArrayList<>(contents.size());
		String prev = null;
		for (ContentItem i : contents) {
			if (i.isUserDefined()) {
				String prv = prev;
				save.add((PhpSerializer export) ->
						new Fields(export, 3)
								.write(H[0], i.name)
								.write(H[1], i.copies)
								.write(AFTER, prv));
			} else {
				int action = i.getActionInt();
				if (i.hasChoice() && action != i.getDefaultActionInt())
					save.add((PhpSerializer export) ->
							new Fields(export, 2)
									.write(H[0], i.name)
									.write(EXPORT, action));
			}
			prev = i.name;
		}
		return save;
	}

	@Override public Object getCell(int index) {
		if (index == 0) return name;
		else {
			if (isUserDefined()) return copies;
			if (hasChoice()) {
				int a = getActionInt();
				return a > 1 ? ONLY_LISTED : NOYES[a];
			}
			return null;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0:
				if (isUserDefined()) name = getString(value);
				break;
			case 1:
				if (isUserDefined()) copies = getByte(value);
				else if (hasChoice()) {
					if ((hasChoiceNoYesList() || hasChoiceNoYes()) && value == NOYES[0])
						setNo();
					else if ((hasChoiceNoYesList() || hasChoiceYesList()) && value == ONLY_LISTED)
						setListedOnly();
					else setYes();
				}
				break;
		}
	}

	@Override public boolean isEmpty() { return name == null && copies == 0; }

	private boolean equals(ContentItem ci) {
		return this == ci || ci != null && (type & VALUE_MASK_INVERT) == (ci.type & VALUE_MASK_INVERT)
				&& name == ci.name;
	}

	/** Κατασκευάζει το φύλλο καταχώρησης από τις αποθηκευμένες αλλαγές της.
	 * Από τη δαπάνη προκύπτει το προκαθορισμένο φύλλο καταχώρησης της δαπάνης. Σε αυτό
	 * πραγματοποιούνται οι αλλαγές που ήταν αποθηκευμένες στο αρχείο της δαπάνης.
	 * @param loaded Οι αποθηκευμένες στο αρχείο της δαπάνης, αλλαγές του προκαθορισμένου φύλλου
	 * καταχώρησης
	 * @param e Η δαπάνη */
	static void unserialize(List<Node> loaded, Expenditure e) throws Exception {
		ContentItem[] defContents = createDefaultContents(e);
		e.contents.ensureCapacity(defContents.length + loaded.size());
		int from = 0;
		for (Node node : loaded) {
			String name = node.getField(H[0]).getString();
			Node n = node.getField(AFTER);
			if (n.isExist()) {		// Δικαιολογητικό κατασκευασμένο από το χρήστη
				String prev = n.getString();
				// Το δικαιολογητικό
				ContentItem userDef = new ContentItem(name, (byte) node.getField(H[1]).getInteger());
				// Έλεγχος αν η αναφορά προς το προηγούμενο δικαιολογητικό, είναι το τελευταίο
				// δικαιολογητικό της λίστας εξόδου, ειδάλλως εισάγουμε ένα-ένα τα δικαιολογητικά του
				// προκαθορισμένου φύλλου μέχρι να βρούμε σε ποιο αναφέρεται η αναφορά. Αν δε βρεθεί
				// αναφορά το δικαιολογητικό προστίθεται στο τέλος.
				if (e.contents.isEmpty() || !e.contents.get(e.contents.size() - 1).name.equals(prev))
					while(from < defContents.length) {	// ...καταχώρησης μέχρι να βρούμε την αναφορά
						ContentItem ci = defContents[from++];
						e.contents.add(ci.cloneMutable());
						if (ci.name.equals(prev)) break;
					}
				e.contents.add(userDef);
			} else {					// Δικαιολογητικό προκαθορισμένο από το πρόγραμμα
				int to = from;
				while(to < defContents.length) {
					ContentItem ci = defContents[to++];
					// Αν το δικαιολογητικό δεν επιδέχεται τροποποιήσεων χρήστη το αγνοούμε
					// επίσης δεν εισάγουμε ένα-ένα τα στοιχεία του προκαθορισμένου φύλλου καταχώρησης
					// αλλά όλα μαζί μόνο όταν όλες οι απαιτήσεις ικανοποιηθούν
					if (ci.name.equals(name) && ci.hasChoice()) {
						do
							e.contents.add(defContents[from++].cloneMutable());
						while(from < to);
						ci = e.contents.get(e.contents.size() - 1);	// Του τελευταίου εισαχθέντος δικαιολογητικού...
						switch((int) node.getField(EXPORT).getInteger()) {	// ...του θέτουμε την ενέργεια εξαγωγής
							case 0: ci.setNo(); break;
							case 2: if (ci.isAbleListedExported()) { ci.setListedOnly(); break; }
							default: ci.setYes(); break;
						}
						break;
					}
				}
			}
		}
		// Προσθήκη και των υπολοίπων δικαιολητικών
		while(from < defContents.length)
			e.contents.add(defContents[from++].cloneMutable());
	}

	static private ContentItem listed(String name, byte type) {
		return new ContentItem(name, null, (byte) (type | LISTED_XOR_EXPORTED), (byte) 1);
	}
	static private ContentItem exported(String name, byte type) {
		return new ContentItem(name, name + ".php", (byte) (type | LISTED_XOR_EXPORTED), (byte) 1);
	}
	static private ContentItem both(String name, byte type, int copies) {
		return new ContentItem(name, name + ".php", type, (byte) copies);
	}


	static final ContentItem ΔιαβιβαστικόΔαπάνης = exported("Διαβιβαστικό Δαπάνης", INIT_YESNO_YES);
	static final private ContentItem ΕξώφυλλοΔαπάνης = exported("Εξώφυλλο Δαπάνης", INIT_YES_FIXED);
	static final private ContentItem ΦύλλοΚαταχώρησης = exported("Φύλλο Καταχώρησης", INIT_YES_FIXED);
	static final private ContentItem Υποφάκελος = both("Υποφάκελος", INIT_YES_FIXED, 1);
	static final ContentItem ΑπόφασηΑνάληψηςΥποχρέωσης = listed("Απόφαση Ανάληψης Υποχρέωσης", INIT_YES_FIXED);
	static final private ContentItem ΚατάστασηΠληρωμής = both("Κατάσταση Πληρωμής", INIT_YES_FIXED, 3);
	static final ContentItem ΔγηΣυγκρότησηςΕπιτροπών = both("Δγη Συγκρότησης Επιτροπών", INIT_YESLIST_YES, 1);
	static final private ContentItem Τιμολόγια = listed("Τιμολόγια", INIT_YES_FIXED);
	static final private ContentItem ΔελτίαΑποστολής = listed("Δελτία Αποστολής", INIT_YESNO_NO);
	static final private ContentItem ΠρωτόκολλοΟριστικήςΠοιοτικήςΠοσοτικήςΠαραλαβής = both("Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής", INIT_YES_FIXED, 1);
	static final private ContentItem ΑΔΔΥ = listed("ΑΔΔΥ", INIT_YESNO_YES);
	static final private ContentItem ΒεβαίωσηΠαραλαβής = both("Βεβαίωση Παραλαβής", INIT_YES_FIXED, 1);
	static final private ContentItem ΒεβαίωσηΜηΧρέωσηςΥλικών = both("Βεβαίωση μη Χρέωσης Υλικών", INIT_YESNO_YES, 1);
	static final ContentItem ΑπόφασηΑπευθείαςΑνάθεσης = both("Απόφαση Απευθείας Ανάθεσης", INIT_YESLIST_YES, 1);
	static final private ContentItem ΥπεύθυνηΔήλωσηMηΧρησιμοποίησηςΑντιπροσώπουΕταιρίαςΑξκουΕΔ = both("Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ", INIT_YESNOLIST_LIST, 1);
	static final private ContentItem ΥπεύθυνηΔήλωσηΓνωστοποίησηςΤραπεζικούΛογαριασμού = both("Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού", INIT_YESNOLIST_LIST, 1);
	static final private ContentItem ΑπόσπασμαΠοινικούΜητρώου = listed("Απόσπασμα Ποινικού Μητρώου", INIT_YESNO_YES);
	static final private ContentItem ΦορολογικήΑσφαλιστικήΕνημερότητα = listed("Φορολογική και Ασφαλιστική Ενημερότητα", INIT_YES_FIXED);
	static final private ContentItem ΕπάρκειαΜέσωνΑυτοκάθαρσης = listed("Αποδεικτικά μέσα για τη διαπίστωση της επάρκειας των μέτρων αυτοκάθαρσης", INIT_YESNO_NO);
	static final private ContentItem ΜηΎπαρξηΔυνητικώνΛόγωνΑποκλεισμού = listed("Αποδεικτικά μέσα για τη μη ύπαρξη δυνητικών λόγων αποκλεισμού", INIT_YESNO_NO);
	static final private ContentItem Σύμβαση = both("Σύμβαση", INIT_YESLIST_LIST, 1);
	static final private ContentItem ΒεβαίωσηΑπόδοσηςΦΕ = listed("Βεβαίωση Απόδοσης ΦΕ", INIT_YESNO_NO);

	/** Τροποποιεί το φύλλο καταχώρησης με βάση τα δεδομένα της δαπάνης. */
	static void fixContents(Expenditure e) {
		ContentItem[] defContents = createDefaultContents(e);
		int from = 0;	// To item του defContents στο οποίο αναφερόμαστε
		int z = 0;		// To item του e.contents στο οποίο αναφερόμαστε
		while(z < e.contents.size()) {
			ContentItem ci = e.contents.get(z);
			if (ci.isUserDefined()) ++z;	// Δικαιολογητικό του χρήστη δεν το πειράζουμε
			else {
				int to = from;	// Ψάχνουμε στο τρέχον φύλλο, στοιχείο ίδιο με το προκαθορισμένο φύλλο
				while(to < defContents.length && !defContents[to].equals(ci)) ++to;
				// ...αν δε βρούμε, αφαιρούμε το στοιχείο από το τρέχον φύλλο και δεν προχωράμε
				// στο defContents γιατί το τρέχον φύλλο μπορεί να είχε παραπανίσιο δικαιολογητικό
				// που στο προκαθορισμένο φύλλο δεν υπάρχει...
				if (to == defContents.length) e.contents.remove(z);
				else { // ...αν βρούμε, προσθέτουμε τα προηγούμενα του προκαθορισμένου φύλλου στο τρέχον
					while(from < to)
						e.contents.add(z++, defContents[from++].cloneMutable());
					// ...εκτός από αυτό στο οποίο βρήκαμε ομοιότητα γιατί μπορεί να του έχει
					// τροποποιήσει την τιμή ο χρήστης (το κρατάμε ως έχει)
					++z; ++from;
				}
			}
		}
		while(from < defContents.length)	// Αντιγραφή των υπολοίπων στοιχείων του προκαθορισμένου φύλλου
			e.contents.add(defContents[from++].cloneMutable());
	}

	/** Αν η εγγραφή επιδέχεται αλλαγές από το χρήστη την κλωνοποιεί, ειδάλλως επιστρέφει την ίδια.
	 * Η κλήση εξασφαλίζει ότι η τρέχουσα εγγραφή του φύλλου καταχώρησης δε θα τροποποιηθεί. Αυτό το
	 * πετυχαίνει κλωνοποιώντας την, αν επιδέχεται τροποποίησης από το χρήστη. */
	private ContentItem cloneMutable() { return hasChoice() ? new ContentItem(this) : this; }

	static private ContentItem[] createDefaultContents(Expenditure e) {
		//TODO: Απαιτεί πολύ δουλειά όταν υλοποιηθούν συμβάσεις, διαγωνισμοί, έργα
		return ΑπευθείαςΑνάθεση;
	}

	static private final ContentItem[] ΑπευθείαςΑνάθεση = {
		ΔιαβιβαστικόΔαπάνης,
		ΕξώφυλλοΔαπάνης,
		ΦύλλοΚαταχώρησης,
		Υποφάκελος,
		ΑπόφασηΑνάληψηςΥποχρέωσης,
		ΚατάστασηΠληρωμής,
		ΔγηΣυγκρότησηςΕπιτροπών,
		Τιμολόγια,
		ΔελτίαΑποστολής,
		ΠρωτόκολλοΟριστικήςΠοιοτικήςΠοσοτικήςΠαραλαβής,	// καθαρή αξία > 2500
		ΒεβαίωσηΠαραλαβής,
		ΑΔΔΥ,
		ΒεβαίωσηΜηΧρέωσηςΥλικών,
		Υποφάκελος,
		ΑπόφασηΑπευθείαςΑνάθεσης,
		ΥπεύθυνηΔήλωσηMηΧρησιμοποίησηςΑντιπροσώπουΕταιρίαςΑξκουΕΔ,
		ΥπεύθυνηΔήλωσηΓνωστοποίησηςΤραπεζικούΛογαριασμού,
		ΑπόσπασμαΠοινικούΜητρώου,	// καθαρή αξία > 2500
		ΦορολογικήΑσφαλιστικήΕνημερότητα,	// καταλογιστέο > 1500 και καταλογιστέο > 3000 && καθαρή αξία > 2500
		ΕπάρκειαΜέσωνΑυτοκάθαρσης,	// καθαρή αξία > 2500
		ΜηΎπαρξηΔυνητικώνΛόγωνΑποκλεισμού,	// καθαρή αξία > 2500
		Σύμβαση,	// καθαρή αξία > 2500
//			s("Απόφαση Παρεκκλίσεων Όρων Σύμβασης"),	// καθαρή αξία > 2500
		Υποφάκελος,
		ΒεβαίωσηΑπόδοσηςΦΕ
	};

//		static private final Paper[] papers = {		// Συνοπτικός Διαγωνισμός
	//			ΔιαβιβαστικόΔαπάνης,
	//			Εξώφυλλο,
	//			ΦύλλοΚαταχώρησης,
	//			Υποφάκελος,
	//			ΔιάθεσηΠίστωσης,
	//			ΚατάστασηΠληρωμής,
	//			ΔγηΣυγκρότησηςΕπιτροπών,
	//			Τιμολόγια,
	//			ΔελτίαΑποστολής,
	//			ΠρωτόκολλοΟριστικήςΠοιοτικήςΠοσοτικήςΠαραλαβής,	// καθαρή αξία > 2500
	//			ΑΔΔΥ,
	//			ΒεβαίωσηΜηΧρέωσηςΥλικών,
	//			Υποφάκελος,
//			s("Διακήρυξη με ΑΔΑΜ στο ΚΥΜΔΗΣ και ΑΔΑ στο ΔΙΑΥΓΕΙΑ"),
//			s("Έγγραφη Πρόσκληση Συμμετοχής στο Συνοπτικό Διαγωνισμό 3 τουλάχιστον Οικονομικών Φορέων"),
//			s("ΤΕΥΔ Υποψηφίων Οικονομικών Φορέων"),
//			s("Πρωτότυπες Τεχνικές Προσφορές"),
//			s("Πρωτότυπες Οικονομικές Προσφορές"),
//			s("Πρακτικά - Εισηγήσεις Αποσφράγισης και Αξιολόγησης Δικαιολογητικών Συμμετοχής - Τεχνικών Προσφορών και Οικονομικών Προσφορών"),
//			s("Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ"),
//			p("Απόσπασμα Ποινικού Μητρώου"),	// καθαρή αξία > 2500
//			ΦορολογικήΕνημερότητα,	// καθαρή αξία > 2500
//			ΑσφαλιστικήΕνημερότητα,	// καθαρή αξία > 2500
//			p("Αποδεικτικά Μέσα για τη Διαπίστωση της Επάρκειας ή Μη, των Μέτρων Αυτοκάθαρσης"),	// καθαρή αξία > 2500
//			p("Αποδεικτικά Μέσα για τη Μη Ύπαρξη Δυνητικών Λόγων Αποκλεισμού"),	// καθαρή αξία > 2500
//			s("Σύμβαση"),	// καθαρή αξία > 2500
//			s("Απόφαση Παρεκκλίσεων Όρων Σύμβασης"),	// καθαρή αξία > 2500
//		};


	// ΑΔΑΜ = Αριθμό Διαδικτυακής Ανάρτησης Μητρώου
	// ΑΔΑ = Αριθμό Διαδικτυακής Ανάρτησης
}