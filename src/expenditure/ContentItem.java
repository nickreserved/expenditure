package expenditure;

import static expenditure.MainFrame.NOYES;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import util.PhpSerializer.Fields;
import util.PhpSerializer.Node;
import util.PhpSerializer.Serializable;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.ResizableTableModel.TableRecord;
import static util.ResizableTableModel.getString;

/** Ένα δικαιολογητικό. */
final class ContentItem implements VariableSerializable, TableRecord {
	/** Όνομα δικαιολογητικού. */
	private String name;
	/** Ο εκδότης του δικαιολογητικού, για οριζόμενα από το χρήστη δικαιολογητικά.
	 * Αν είναι null, είναι η επωνυμία της Μονάδας. */
	private String issuer;
	/** Πλεγμένη πληροφορία για το δικαιολογητικό στα bits του byte.
	 * Όταν είσαι παλιά καραβάνα στον προγραμματισμό, έχεις φάει Assembly με το κουτάλι και θέλεις
	 * να αποφύγεις άπειρα πεδία ή άπειρες κληρονομικότητες και πολυμορφισμούς, χρησιμοποιείς ένα
	 * byte με packed δεδομένα. Δεον να αποφεύγεται :-)
	 * Η ανάλυση των bit του πεδίου, βρίσκεται σε όλες τις byte σταθερές στατικές μεταβλητές της
	 * κλάσης. */
	private byte type;
	/** Πλήθος δικαιολογητικών που θα εξαχθούν. */
	final private byte total;

	/** Δικαιολογητικό που καταχωρείται στο φύλλο καταχώρησης της δαπάνης.
	 * Εξαίρεση αποτελούν τα οριζόμενα από το χρήστη δικαιολογητικά, που καταχωρούνται στο φύλλο
	 * καταχώρησης της δαπάνης, αλλά το type τους είναι 0. */
	static final private byte TYPE_LISTED = 1;
	/** Δικαιολογητικό για το οποίο το πρόγραμμα έχει εξαγόμενο έντυπο. */
	static final private byte TYPE_EXPORTED = 2;
	/** Δικαιολογητικό που καταχωρείται στο φύλλο καταχώρησης της δαπάνης και εξάγει και έντυπο. */
	static final private byte TYPE_LISTED_EXPORTED = TYPE_LISTED | TYPE_EXPORTED;
	/** Μάσκα για απομόνωση του τύπου του δικαιολογητικού. */
	static final private byte TYPE_MASK = TYPE_LISTED_EXPORTED;

	/** Το δικαιολογητικό εξάγεται πλήρως και ο χρήστης δεν έχει τη δυνατότητα να το αλλάξει αυτό. */
	static final private byte CHOICES_YES_FIXED = 0;
	/** Ο χρήστης μπορεί να επιλέξει αν το δικαιολογητικό θα εξαχθεί πλήρως ή καθόλου. */
	static final private byte CHOICES_YES_NO = 4;
	/** Ο χρήστης μπορεί να επιλέξει αν το δικαιολογητικό θα εξαχθεί πλήρως ή θα καταχωρηθεί μόνο στο φύλλο καταχώρησης.
	 * Αφορά μόνο τα δικαιολογητικά για τα οποία το πρόγραμμα μπορεί να εξάγει έντυπο και μπορούν να
	 * καταχωρηθούν στο φύλλο καταχώρησης. */
	static final private byte CHOICES_YES_LIST = 8;
	/** Ο χρήστης μπορεί να επιλέξει αν το δικαιολογητικό θα εξαχθεί πλήρως, θα καταχωρηθεί μόνο στο φύλλο καταχώρησης, ή δε θα εξαχθεί καθόλου.
	 * Αφορά μόνο τα δικαιολογητικά για τα οποία το πρόγραμμα μπορεί να εξάγει έντυπο και μπορούν να
	 * καταχωρηθούν στο φύλλο καταχώρησης. */
	static final private byte CHOICES_YES_NO_LIST = 4 | 8;
	/** Μάσκα για απομόνωση των επιλογών εξαγωγής του δικαιολογητικού. */
	static final private byte CHOICES_MASK = 4 | 8;

	/** Η προκαθορισμένη επιλογή για το δικαιολογητικό είναι να μην εξαχθεί καθόλου. */
	static final private byte DEFAULT_NO = 0;
	/** Η προκαθορισμένη επιλογή για το δικαιολογητικό είναι να εξαχθεί πλήρως. */
	static final private byte DEFAULT_YES = 16;
	/** Η προκαθορισμένη επιλογή για το δικαιολογητικό είναι να κατχωρηθεί μόνο στο φύλλο καταχώρησης.
	 * Αφορά μόνο τα δικαιολογητικά για τα οποία το πρόγραμμα μπορεί να εξάγει έντυπο και μπορούν να
	 * καταχωρηθούν στο φύλλο καταχώρησης. */
	static final private byte DEFAULT_LIST = 32;
	/** Μάσκα για απομόνωση της προκαθορισμένης επιλογής εξαγωγής του δικαιολογητικού. */
	static final private byte DEFAULT_MASK = 16 | 32;

	/** Το δικαιολογητικό δε θα εξαχθεί καθόλου. */
	static final private byte VALUE_NO = 0;
	/** Το δικαιολογητικό θα εξαχθεί πλήρως. */
	static final private byte VALUE_YES = 64;
	/** Το δικαιολογητικό θα καταχωρηθεί στο φύλλο καταχώρησης.
	 * Αφορά μόνο τα δικαιολογητικά για τα οποία το πρόγραμμα μπορεί να εξάγει έντυπο και μπορούν να
	 * καταχωρηθούν στο φύλλο καταχώρησης. */
	static final private byte VALUE_LIST = -128;
	/** Μάσκα για την επιλογή εξαγωγής του δικαιολογητικού. */
	static final private byte VALUE_MASK = 64 | -128;
	/** Μάσκα για όλες τις άλλες τιμές του type εκτός από το αν και πως θα εξαχθεί το δικαιολογητικό. */
	static final private byte VALUE_MASK_INVERT = 1 | 2 | 4 | 8 | 16 | 32;

	static final private byte INIT_YES_FIXED = CHOICES_YES_FIXED | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNO_NO = CHOICES_YES_NO | DEFAULT_NO | VALUE_NO;
	static final private byte INIT_YESNO_YES = CHOICES_YES_NO | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNOLIST_NO = CHOICES_YES_NO_LIST | DEFAULT_NO | VALUE_NO;
	static final private byte INIT_YESNOLIST_YES = CHOICES_YES_NO_LIST | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESNOLIST_LIST = CHOICES_YES_NO_LIST | DEFAULT_LIST | VALUE_LIST;
	static final private byte INIT_YESLIST_YES = CHOICES_YES_LIST | DEFAULT_YES | VALUE_YES;
	static final private byte INIT_YESLIST_LIST = CHOICES_YES_LIST | DEFAULT_LIST | VALUE_LIST;

	/** Το δικαιολογητικό το δημιούργησε ο χρήστης. */
	boolean isUserDefined() { return type == 0; }
	/** Το δικαιολογητικό δύναται και να εξάγει έγγραφο και να καταχωρηθεί στο φύλλο καταχώρησης.
	 * Μπορεί με βάση τις ρυθμίσεις να εξάγει. Αλλά εδώ γίνεται αναφορά στη δυνατότητα και όχι στο
	 * τι θα κάνει τελικά. */
	private boolean isAbleListedExported() { return (type & TYPE_MASK) == TYPE_MASK; }
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
	private int getActionInt() { return (type >> 6) & 3; }
	/** Επιστρέφει την προκαθορισμένη ενέργεια εξαγωγής του δικαιολογητικού.
	 * @return 0: Ούτε εξάγεται έντυπο, ούτε καταχωρείται στο φύλλο καταχώρησης. 1: Εξάγεται έντυπο
	 * και καταχωρείται στο φύλλο καταχώρησης (αν το δικαιολογητικό υποστηρίζει μόνο το ένα από τα
	 * δύο, τότε αφορά αυτό που υποστηρίζει). 2: Καταχωρείται μόνο στο φύλλο καταχώρησης (αφορά μόνο
	 * τα δικαιολογητικά που και εξάγονται και καταχωρούνται στο φύλλο καταχώρησης). */
	private int getDefaultActionInt() { return (type >> 4) & 3; }
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
	ContentItem() { this(null, null); }

	/** Αρχικοποίηση δικαιολογητικού με copy constructor. */
	private ContentItem(ContentItem i) { name = i.name; issuer = i.issuer; type = i.type; total = i.total; }

	/** Αρχικοποίηση καθορισμένου από το χρήστη δικαιολογητικού.
	 * @param name Όνομα δικαιολογητικού
	 * @param issuer Ο εκδότης του δικαιολογητικού, για οριζόμενα από το χρήστη δικαιολογητικά */
	private ContentItem(String name, String issuer) { this.name = name; this.issuer = issuer; total = 1; }

	/** Αρχικοποίηση προκαθορισμένου δικαιολογητικού.
	 * @param name Όνομα δικαιολογητικού
	 * @param type Ο τύπος εξαγωγής του δικαιολογητικού */
	private ContentItem(String name, int type) { this.name = name; this.type = (byte) type; total = 1; }

	/** Αρχικοποίηση του δικαιολογητικού.
	 * @param name Όνομα δικαιολογητικού
	 * @param type Ο τύπος εξαγωγής του δικαιολογητικού
	 * @param total Ο αριθμός των δικαιολογητικών που απαιτούνται στη δαπάνη */
	private ContentItem(String name, int type, int total) {
		this.name = name; this.type = (byte) type; this.total = (byte) total;
	}

	/** Επιστρέφει το όνομα του δικαιολογητικού.
	 * @return Το όνομα του δικαιολογητικού */
	@Override public String toString() { return name; }

	/** Ονόματα πεδίων αποθήκευσης για κάθε εγγραφή του φύλλου καταχώρησης. */
	static final String[] H = { "Δικαιολογητικό", "Εκδότης", "Μετά από", "Εξαγωγή" };
	/** Επιλογή εμφάνισης για δικαιολογητικό που δύναται και να εξαχθεί οστόσο μόνο καταχωρείται. */
	static final String ONLY_LISTED = "Μόνο καταχώρηση";

	/** Καθορίζει τα πεδία του αντικειμένου που θα εξαχθούν σε php serialize string format για επεξεργασία από PHP script.
	 * @param fields Διαχειριστής των πεδίων του αντικεμένου για εξαγωγή. Όταν το αντικείμενο
	 * θέλει να επιλέξει ένα πεδίο για εξαγωγή, χρησιμοποιεί την VariableFields.add(). */
	@Override public void serialize(VariableFields fields) {
		boolean export = (type & TYPE_EXPORTED) == TYPE_EXPORTED && (type & VALUE_MASK) == VALUE_YES;
		boolean enlist = isUserDefined()
				|| (type & TYPE_LISTED) == TYPE_LISTED && (type & VALUE_MASK) != VALUE_NO;
		if (name != null)   fields.add(H[0],         name);
		if (issuer != null) fields.add(H[1],         issuer);
							fields.add("Καταχώρηση", enlist);
							fields.add(H[3],         export);
							fields.add("Πλήθος",     total);
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
				save.add(export ->
						new Fields(export, 3)
								.write(H[0], i.name)
								.write(H[1], i.issuer)
								.write(H[2], prv));
			} else {
				int action = i.getActionInt();
				if (i.hasChoice() && action != i.getDefaultActionInt())
					save.add(export ->
							new Fields(export, 2)
									.write(H[0], i.name)
									.write(H[3], action));
			}
			prev = i.name;
		}
		return save;
	}

	@Override public Object getCell(int index) {
		if (index == 0) return name;
		else {
			if (isUserDefined()) return issuer;
			if (hasChoice()) {
				int a = getActionInt();
				return a > 1 ? ONLY_LISTED : NOYES[a];
			}
			return null;
		}
	}

	@Override public void setCell(int index, Object value) {
		switch(index) {
			case 0: name = getString(value); break;
			case 1:
				if (isUserDefined()) issuer = getString(value);
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

	@Override public boolean isEmpty() { return name == null && issuer == null; }

	private boolean equals(ContentItem ci) {
		return this == ci || ci != null && (type & VALUE_MASK_INVERT) == (ci.type & VALUE_MASK_INVERT)
				&& name == ci.name;
	}

	static final private ContentItem ΑπόσπασμαΠοινικούΜητρώου
			= new ContentItem("Απόσπασμα Ποινικού Μητρώου", TYPE_LISTED | INIT_YESNO_YES);
	static final private ContentItem ΦορολογικήΑσφαλιστικήΕνημερότητα
			= new ContentItem("Φορολογική και Ασφαλιστική Ενημερότητα", TYPE_LISTED | INIT_YES_FIXED);
	static final private ContentItem ΥπεύθυνηΔήλωσηΕισφορώνΑσφάλισης
			= new ContentItem("Υπεύθυνη Δήλωση Σχετικά με τους Οργανισμούς Κύριας και Επικουρικής Ασφάλισης που Καταβάλλονται Εισφορές", TYPE_LISTED | INIT_YES_FIXED);
	static final private ContentItem ΥπεύθυνηΔήλωσηΔικαστικήςΑπόφασης
			= new ContentItem("Υπεύθυνη Δήλωση Περί μη Έκδοσης Δικαστικής ή Διοικητικής Απόφασης με Τελεσίδικη και Δεσμευτική Ισχύ, για την Αθέτηση των Υποχρεώσεων, όσον Αφορά στην Καταβολή Φόρων ή Εισφορών Κοινωνικής Ασφάλισης", TYPE_LISTED | INIT_YES_FIXED);
	static final private ContentItem ΠρόσκλησηΥποβολήςΠροσφορών
			= new ContentItem("Πρόσκληση Υποβολής Προσφορών", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES);
	static final private ContentItem ΑπόφασηΑπευθείαςΑνάθεσης
			= new ContentItem("Απόφαση Απευθείας Ανάθεσης", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES);
	static final private ContentItem Σύμβαση
			= new ContentItem("Σύμβαση", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST);
//	static final private ContentItem ΒεβαίωσηΑπόδοσηςΦΕ = listed("Βεβαίωση Απόδοσης ΦΕ", INIT_YESNO_NO);

	/** Έντυπα δαπάνης, προ των υποφακέλων. */
	static final private ContentItem[] FOLDER_PREREQUISITES = {
		new ContentItem("Διαβιβαστικό Δαπάνης", TYPE_EXPORTED | INIT_YESNO_YES),
		new ContentItem("Εξώφυλλο Δαπάνης", TYPE_EXPORTED | INIT_YES_FIXED),
		new ContentItem("Φύλλο Καταχώρησης", TYPE_EXPORTED | INIT_YES_FIXED)
	};

	/** Τα έντυπα του Υποφακέλου Α, Δικαιολογητικά Προμηθειών - Υπηρεσιών. */
	static final private ContentItem[] FOLDER_A_SUPPLIES_SERVICES = {
		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Α»: ΔΙΚΑΙΟΛΟΓΗΤΙΚΑ ΠΡΟΜΗΘΕΙΩΝ - ΥΠΗΡΕΣΙΩΝ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		new ContentItem("Απόφαση Ανάληψης Υποχρέωσης", TYPE_LISTED | INIT_YES_FIXED),
		new ContentItem("Κατάσταση Πληρωμής", TYPE_LISTED_EXPORTED | INIT_YES_FIXED, 2),
		new ContentItem("Δγη Συγκρότησης Επιτροπών", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES),
		new ContentItem("Τιμολόγια", TYPE_LISTED | INIT_YES_FIXED),
		new ContentItem("Αποδεικτικό Πληρωμής ΤΠΕΔΕ, Π.Ο.ΕΜΔΥΔΑΣ, ΤΜΕΔΕ", TYPE_LISTED | INIT_YESNO_YES),	// Έργο
		new ContentItem("Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),	// καθαρή αξία > 2500
		new ContentItem("Βεβαίωση Παραλαβής", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),	// καθαρή αξία <= 2500
		new ContentItem("Βεβαίωση μη Χρέωσης Υλικών", TYPE_LISTED_EXPORTED | INIT_YESNO_YES),
		new ContentItem("Πρωτόκολλο Προσωρινής και Οριστικής Παραλαβής", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES),	// Έργο
		new ContentItem("Πρωτόκολλο Παραλαβής Αφανών Εργασιών", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES),	// Έργο
		new ContentItem("Οριστική και Αναλυτική Επιμέτρηση", TYPE_LISTED_EXPORTED | INIT_YESLIST_YES),	// Έργο
		new ContentItem("Βεβαίωση Εκτέλεσης του Έργου από Οπλίτες", TYPE_LISTED_EXPORTED | INIT_YESNO_YES),	// Έργο
		new ContentItem("Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ", TYPE_LISTED_EXPORTED | INIT_YESNOLIST_LIST),
		new ContentItem("Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού", TYPE_LISTED_EXPORTED | INIT_YESNOLIST_LIST)
	};

	/** Τα έντυπα του Υποφακέλου Β, Δικαιολογητικά Απευθείας Ανάθεσης. */
	static private final ContentItem[] FOLDER_B_DIRECT_ASSIGNMENT = {
		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Β»: ΔΙΚΑΙΟΛΟΓΗΤΙΚΑ ΑΠΕΥΘΕΙΑΣ ΑΝΑΘΕΣΗΣ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		ΠρόσκλησηΥποβολήςΠροσφορών,
		ΑπόφασηΑπευθείαςΑνάθεσης,
		ΑπόσπασμαΠοινικούΜητρώου,			// καθαρή αξία > 2500
		ΦορολογικήΑσφαλιστικήΕνημερότητα,	// καταλογιστέο > 1500 και καταλογιστέο > 3000 && καθαρή αξία > 2500
		ΥπεύθυνηΔήλωσηΕισφορώνΑσφάλισης,	// καθαρή αξία > 2500
		ΥπεύθυνηΔήλωσηΔικαστικήςΑπόφασης,	// καθαρή αξία > 2500
		Σύμβαση,							// καθαρή αξία > 2500
	};

	/** Τα έντυπα των Υποφακέλων Β, Γ, Δ και Ε, του Διαγωνισμού (Συνοπτικού ή Ανοικτής Διαδικασίας). */
	static private final ContentItem[] FOLDER_BCDE_TENDER = {
		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Β»: ΥΠΟΒΟΛΗ ΠΡΟΣΦΟΡΩΝ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		new ContentItem("Διακήρυξη Διαγωνισμού", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST),
		new ContentItem("Δικαιολογητικά Συμμετοχής Οικονομικών Φορέων", TYPE_LISTED | INIT_YES_FIXED),
		new ContentItem("Πρακτικά Αποσφράγισης Δικαιολογητικών Συμμετοχής", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST),
		new ContentItem("Απόφαση Ανάδειξης Προσωρινού Αναδόχου", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST),

		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Γ»: ΑΠΟΔΕΙΚΤΙΚΑ ΜΕΣΑ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		new ContentItem("Πρακτικό Ελέγχου Δικαιολογητικών Κατακύρωσης", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST),
		ΑπόσπασμαΠοινικούΜητρώου,
		ΦορολογικήΑσφαλιστικήΕνημερότητα,
		ΥπεύθυνηΔήλωσηΕισφορώνΑσφάλισης,
		ΥπεύθυνηΔήλωσηΔικαστικήςΑπόφασης,
		new ContentItem("Δικαιολογητικά Κατακύρωσης Προσωρινού Αναδόχου", TYPE_LISTED | INIT_YES_FIXED),

		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Δ»: ΚΑΤΑΚΥΡΩΣΗ ΔΙΑΓΩΝΙΣΜΟΥ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		ΠρόσκλησηΥποβολήςΠροσφορών,	// Σε περίπτωση που έχουμε συνοπτικό διαγωνισμό και απευθείας ανάθεση μαζί
		ΑπόφασηΑπευθείαςΑνάθεσης,	// Σε περίπτωση που έχουμε συνοπτικό διαγωνισμό και απευθείας ανάθεση μαζί
		new ContentItem("Κατακύρωση Διαγωνισμού", TYPE_LISTED_EXPORTED | INIT_YESLIST_LIST),

		new ContentItem("ΥΠΟΦΑΚΕΛΟΣ «Ε»: ΥΠΟΓΡΑΦΗ ΣΥΜΦΩΝΗΤΙΚΟΥ", TYPE_LISTED_EXPORTED | INIT_YES_FIXED),
		Σύμβαση
	};

	/** Κατασκευάζει το φύλλο καταχώρησης από τις αποθηκευμένες αλλαγές του.
	 * Από τη δαπάνη προκύπτει το προκαθορισμένο φύλλο καταχώρησης της δαπάνης. Σε αυτό
	 * πραγματοποιούνται οι αλλαγές που ήταν αποθηκευμένες στο αρχείο της δαπάνης.
	 * @param loaded Οι αποθηκευμένες στο αρχείο της δαπάνης, αλλαγές του προκαθορισμένου φύλλου
	 * καταχώρησης
	 * @param cfg Ο τύπος του φύλλου καταχώρησης. false για απευθείας ανάθεση, true για διαγωνισμό.
	 * @param contents Το τελικό φύλλο καταχώρησης της δαπάνης μετά τις αλλαγές */
	static void unserialize(List<Node> loaded, boolean cfg, List<ContentItem> contents) throws Exception {
		List<ContentItem> defContents = createAutoContents(cfg);
		int from = 0;
		for (Node node : loaded) {
			String name = node.getField(H[0]).getString();
			Node n = node.getField(H[2]);
			if (n.isExist()) {		// Δικαιολογητικό κατασκευασμένο από το χρήστη
				String prev = n.getString();
				// Το δικαιολογητικό
				ContentItem userDef = new ContentItem(name, node.getField(ContentItem.H[1]).getString());
				// Έλεγχος αν η αναφορά προς το προηγούμενο δικαιολογητικό, είναι το τελευταίο
				// δικαιολογητικό της λίστας εξόδου, ειδάλλως εισάγουμε ένα-ένα τα δικαιολογητικά του
				// προκαθορισμένου φύλλου μέχρι να βρούμε σε ποιο αναφέρεται η αναφορά. Αν δε βρεθεί
				// αναφορά το δικαιολογητικό προστίθεται στο τέλος.
				if (contents.isEmpty() || !Objects.equals(contents.get(contents.size() - 1).name, prev))
					while(from < defContents.size()) {	// ...καταχώρησης μέχρι να βρούμε την αναφορά
						ContentItem ci = defContents.get(from++);
						contents.add(ci.cloneMutable());
						if (ci.name.equals(prev)) break;
					}
				contents.add(userDef);
			} else {					// Δικαιολογητικό προκαθορισμένο από το πρόγραμμα
				int to = from;
				while(to < defContents.size()) {
					ContentItem ci = defContents.get(to++);
					// Αν το δικαιολογητικό δεν επιδέχεται τροποποιήσεων χρήστη το αγνοούμε
					// επίσης δεν εισάγουμε ένα-ένα τα στοιχεία του προκαθορισμένου φύλλου καταχώρησης
					// αλλά όλα μαζί μόνο όταν όλες οι απαιτήσεις ικανοποιηθούν
					if (ci.name.equals(name) && ci.hasChoice()) {
						do
							contents.add(defContents.get(from++).cloneMutable());
						while(from < to);
						ci = contents.get(contents.size() - 1);	// Του τελευταίου εισαχθέντος δικαιολογητικού...
						switch((int) node.getField(H[3]).getInteger()) {	// ...του θέτουμε την ενέργεια εξαγωγής
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
		while(from < defContents.size())
			contents.add(defContents.get(from++).cloneMutable());
	}

	/** Τροποποιεί το φύλλο καταχώρησης με βάση τα δεδομένα της δαπάνης.
	 * Το πρόγραμμα προσπαθεί να διατηρήσει, στο μέτρο του δυνατού, τυχόν αλλαγές του χρήστη στις
	 * επιλογές των εγγραφών του φύλλου καταχώρησης, καθώς και τα οριζόμενα από το χρήστη
	 * δικαιολογητικά.
	 * @param contents Το ισχύον φύλλο καταχώρησης. Μετά την κλήση, θα έχει τροποποιηθεί.
	 * @param cfg Ο τύπος του φύλλου καταχώρησης. false για απευθείας ανάθεση, true για διαγωνισμό. */
	static void convertContents(List<ContentItem> contents, boolean cfg) {
		List<ContentItem> defContents = createAutoContents(cfg);
		int idxDef = 0;	// To item του defContents στο οποίο αναφερόμαστε
		int idxCur = 0;		// To item του contents στο οποίο αναφερόμαστε
		while(idxCur < contents.size()) {
			ContentItem ci = contents.get(idxCur);
			if (ci.isUserDefined()) ++idxCur;	// Δικαιολογητικό του χρήστη δεν το πειράζουμε
			else {
				int to = idxDef;	// Ψάχνουμε στο τρέχον φύλλο, στοιχείο ίδιο με το προκαθορισμένο φύλλο
				while(to < defContents.size() && !defContents.get(to).equals(ci)) ++to;
				// ...αν δε βρούμε, αφαιρούμε το στοιχείο από το τρέχον φύλλο και δεν προχωράμε
				// στο defContents γιατί το τρέχον φύλλο μπορεί να είχε παραπανίσιο δικαιολογητικό
				// που στο προκαθορισμένο φύλλο δεν υπάρχει...
				if (to == defContents.size()) contents.remove(idxCur);
				else { // ...αν βρούμε, προσθέτουμε τα προηγούμενα του προκαθορισμένου φύλλου στο τρέχον
					while(idxDef < to)
						contents.add(idxCur++, defContents.get(idxDef++).cloneMutable());
					// ...εκτός από αυτό στο οποίο βρήκαμε ομοιότητα γιατί μπορεί να του έχει
					// τροποποιήσει την τιμή ο χρήστης (το κρατάμε ως έχει)
					++idxCur; ++idxDef;
				}
			}
		}
		while(idxDef < defContents.size())	// Αντιγραφή των υπολοίπων στοιχείων του προκαθορισμένου φύλλου
			contents.add(defContents.get(idxDef++).cloneMutable());
	}

	/** Αν η εγγραφή επιδέχεται αλλαγές από το χρήστη την κλωνοποιεί, ειδάλλως επιστρέφει την ίδια.
	 * Η κλήση εξασφαλίζει ότι η τρέχουσα εγγραφή του φύλλου καταχώρησης δε θα τροποποιηθεί. Αυτό το
	 * πετυχαίνει κλωνοποιώντας την, αν επιδέχεται τροποποίησης από το χρήστη. */
	private ContentItem cloneMutable() { return hasChoice() ? new ContentItem(this) : this; }

	/** Δημιουργεί τα προκαθορισμένα περιεχόμενα της δαπάνης με βάση τον τύπο του διαγωνισμού.
	 * @param cfg Ο τύπος του φύλλου καταχώρησης. false για απευθείας ανάθεση, true για διαγωνισμό.
	 * @return Η λίστα του φύλλου καταχώρησης, μόνο με προκαθορισμένα δικαιολογητικά */
	static private List<ContentItem> createAutoContents(boolean cfg) {
		ArrayList<ContentItem> contents = new ArrayList<>(70);
		createAutoContents(cfg, contents);
		return contents;
	}
	/** Δημιουργεί τα προκαθορισμένα περιεχόμενα της δαπάνης με βάση τον τύπο του διαγωνισμού.
	 * @param cfg Ο τύπος του φύλλου καταχώρησης. false για απευθείας ανάθεση, true για διαγωνισμό.
	 * @param contents Η λίστα του φύλλου καταχώρησης, μόνο με προκαθορισμένα δικαιολογητικά */
	static void createAutoContents(boolean cfg, List<ContentItem> contents) {
		contents.addAll(Arrays.asList(FOLDER_PREREQUISITES));
		contents.addAll(Arrays.asList(FOLDER_A_SUPPLIES_SERVICES));
		contents.addAll(Arrays.asList(cfg ? FOLDER_BCDE_TENDER : FOLDER_B_DIRECT_ASSIGNMENT));
	}
}