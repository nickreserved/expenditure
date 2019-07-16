<?php
require_once('init.php');
require_once('report.php');

/** Επιστρέφει την έδρα της Μονάδας. */
function at_unit_address() {
	global $data;
	return 'στην έδρα ' . article(gender($data['Μονάδα Πλήρες']), 1) . ' '
			. inflectPhrase($data['Μονάδα Πλήρες'], 1) . ', ' . get_unit_address();
}

/** Εξάγει αποδεικτικό κοινοποίησης.
 * @param array $contractor Ο οικονομικός φορέας
 * @param string $title Το αντικείμενο του διαγωνισμού
 * @param string $order Το έγγραφο που θα κοινοποιηθεί */
function sharing_proof($contractor, $title, $order) {
	global $data;
?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qc{\b\ul ΑΠΟΔΕΙΚΤΙΚΟ ΚΟΙΝΟΠΟΙΗΣΗΣ}\par
\qj Σήμερα \u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (ημερομηνία)} και ώρα \u8230.\u8230.\u8230. ο υπογεγραμένος \u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (βαθμός, ονοματεπώνυμο)} που υπηρετώ <?=rtf(article(gender($data['Μονάδα Πλήρες']), 2, true) . ' ' . inflectPhrase($data['Μονάδα Πλήρες'], 2))?> πήγα στην έδρα της επιχείρησης <?=rtf($contractor['Επωνυμία'])?> στη διεύθυνση <?=rtf($contractor['Διεύθυνση'])?>, και κοινοποίησα στο νόμιμο εκπρόσωπο της, που ονομάζεται <?=isset($contractor['Όνομα']) ? rtf($contractor['Όνομα']) : '\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230. {\fs20\i (ονοματεπώνυμο)}'?>, το έγγραφο <?=order($order)?> που αφορά <?=rtf($title)?>.\par

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
Ο Κοινοποιήσας\line\line\line\line\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\line{\fs20\i (βαθμός, ονοματεπώνυμο)}\cell
Εκπρόσωπος της Επιχείρησης\line\line\line\line <?=isset($contractor['Όνομα']) ? rtf($contractor['Όνομα']) : '\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\u8230.\line{\fs20\i (ονοματεπώνυμο)}'?>\cell\row

\sect

<?php
}

function contractor_legal_documents($tender) {
	foreach($tender['Δικαιολογητικά Κατακύρωσης'] as $par => $document)
		echo '\tab ' . greeknum($par + 1) . '.\tab ' . $document . '.\par' . PHP_EOL;
	++$par;
?>

\tab <?=greekNum(++$par)?>.\tab Απόσπασμα Ποινικού Μητρώου.\par
\tab <?=greekNum(++$par)?>.\tab Φορολογική και Ασφαλιστική Ενημερότητα.\par
\tab <?=greekNum(++$par)?>.\tab Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ.\par
\tab <?=greekNum(++$par)?>.\tab Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού.\par
<?php
	if ($tender['Εγγυητική Επιστολή Καλής Εκτέλεσης'])
		echo '\tab ' . greekNum(++$par) . '.\tab Επιπρόσθετα, για την υπογραφή της σύμβασης, απαιτείται η προσκόμιση Εγγυητικής Επιστολής Καλής Εκτέλεσης ύψους 5% της καθαρής αξίας της σύμβασης.\par' . PHP_EOL;
}

/** Ο τύπος του αντικειμένου του διαγωνισμού (Έργο, Προμήθεια, Υπηρεσία) */
function tender_category($invoices) {
	global $data;
	if ($data['Έργο']) return 'Κατασκευή Έργου';
	foreach($invoices as $invoice) {
		$category = get_invoice_category($invoice);
		if ($category == 'Παροχή Υπηρεσιών') break;
	}
	return $category;
}

/** Εξάγει έναν οικονομικό φορέα.
 * @param array $contractor Ο διαγωνιζόμενος οικονομικός φορέας που θα εξαχθεί
 * @param int $count Ο αύξων αριθμός του οικονομικού φορέα σε μια λίστα */
function tender_competitor($contractor, $count) {
	echo '\tab ' . greekNum($count + 1) . '.\tab ' . rtf(get_contractor_id($contractor)) . '.\par' . PHP_EOL;
}

/** Εξάγει μια λίστα στοιχείων ενός οικονομικού φορέα.
 * @param array $ar Η λίστα στοιχείων του οικονομικού φορέα που θα εξαχθεί
 * @param int $count Ο εναρκτήριος αύξων αριθμός της λίστας στοιχείων */
function tender_competitor_list($ar, & $count = 1) {
	foreach($ar as $i)
		echo '\tab\tab (' . $count++ . ')\tab ' . rtf($i) . '.\par' . PHP_EOL;
}

/** Εξάγει τους οικονομικούς φορείς με κάποια στοιχεία τους.
 * @param array $competitors Στοιχεία των οικονομικών φορέων
 * @param string $key Το κλειδί στο $competitors που περιέχει ένα array από στοιχεία προς εμφάνιση */
function tender_competitors_list($competitors, $key) {
	foreach($competitors as $count => $competitor) {
		tender_competitor($competitor['Ενδιαφερόμενος'], $count);
		if (isset($competitor[$key]))
			tender_competitor_list($competitor[$key]);
	}
}

/** Εξάγει τους οικονομικούς φορείς με τους λόγους απόρριψής τους.
 * @param array $competitors Στοιχεία των οικονομικών φορέων */
function tender_rejected_competitors_list($competitors) {
	foreach($competitors as $count => $competitor) {
		tender_competitor($competitor['Ενδιαφερόμενος'], $count);
		$count = 1;
		if (isset($competitor['Λόγοι Απόρριψης Συμμετοχής']))
			tender_competitor_list($competitor['Λόγοι Απόρριψης Συμμετοχής'], $count);
		if (isset($competitor['Λόγοι Απόρριψης Οικονομικής Προσφοράς']))
			tender_competitor_list($competitor['Λόγοι Απόρριψης Οικονομικής Προσφοράς'], $count);
	}
}

/** Εξαγωγή της επιτροπής διαγωνισμού του πρακτικού, παράγραφος 1. */
function unseal_intro_committee() {
	global $data;
?>

\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
1.\tab Η Επιτροπή Διενέργειας του Διαγωνισμού και Αξιολόγησης Προσφορών, που συγκροτήθηκε με την <?=order($data['Δγη Συγκρότησης Επιτροπών'])?> και αποτελείται από τους:\par
\tab α.\tab <?=personi($data['Πρόεδρος Διαγωνισμών'], 2)?> ως πρόεδρο\par
\tab β.\tab <?=personi($data['Α Μέλος Διαγωνισμών'], 2)?> και\par
\tab γ.\tab <?=personi($data['Β Μέλος Διαγωνισμών'], 2)?> ως μέλη,\par

<?php
}

/** Εξαγωγή του πρακτικού αποσφράγισης, παράγραφος 1.
 * @param array $tender Ο διαγωνισμός
 * @param array $invoices Τα τιμολόγια του διαγωνισμού */
function unseal_intro($tender, $invoices) {
	unseal_intro_committee();
?>
προέβη στην αποσφράγιση και αξιολόγηση των προσφορών που κατατέθηκαν στο πλαίσιο διενέργειας <?=inflectPhrase($tender['Τύπος'], 1)?>, που προκηρύχθηκε με την <?=order($tender['Διακήρυξη Διαγωνισμού'])?><?php if (published()) echo ' (ΑΔΑ: ' . rtf($tender['ΑΔΑ Διακήρυξης']) . ')'; ?> για <?=tender_category($invoices)?>.\par
<?php }

/** Εξαγωγή του πρακτικού αποσφράγισης, παράγραφοι 1-3.
 * @param array $tender Ο διαγωνισμός
 * @param array $invoices Τα τιμολόγια του διαγωνισμού */
function unseal_a_1_3($tender, $invoices) {
	unseal_intro($tender, $invoices); ?>

2.\tab Η επιτροπή συνήλθε την <?=strftime('%d %b %y', $tender['Χρόνος Αποσφράγισης Προσφορών'])?> και σταμάτησε την ώρα <?=strftime('%H:%M', $tender['Χρόνος Αποσφράγισης Προσφορών'])?> την παραλαβή των φακέλων προσφορών που κατέθεταν οι ενδιαφερόμενοι, παραλαμβάνοντας και αυτούς που είχαν αποσταλεί στην Υπηρεσία.\par
3.\tab Μετά τη λήξη της παραπάνω προθεσμίας παραλαβής των προσφορών, ο Πρόεδρος δήλωσε ότι δε μπορεί να γίνει δεκτή καμία άλλη προσφορά και άρχισε η διαδικασία της αποσφράγισης.\par
<?php }

/** Εξαγωγή του πρακτικού αποσφράγισης, παράγραφοι 4-3.
 * @param array $tender Ο διαγωνισμός
 * @param int $par Επόμενος αριθμός παραγράφου, ο οποίος ανανεώνεται με την επιστροφή
 * @return array Ενδιαφερόμενοι που έγιναν αποδεκτοί */
function unseal_a_4_8($tender, & $par) { ?>
<?=$par++?>.\tab Όλες οι προσφορές υποβλήθηκαν σε σφραγισμένο φάκελο, φέροντας τις ενδείξεις που απαιτούσε η διακήρυξη του διαγωνισμού.\par
<?=$par++?>.\tab Ακολούθως η επιτροπή, αφού εξέτασε τα εξωτερικά στοιχεία των προσφορών (σφραγισμένος φάκελος, κτλ), αποσφράγισε τον κυρίως φάκελο προσφοράς. Όλα τα δικαιολογητικά και η τεχνική προσφορά, ανά φύλλο, μονογραφήθηκαν και σφραγίστηκαν.\par
<?=$par++?>.\tab Οι διαγωνιζόμενοι, καθώς και τα δικαιολογητικά και η τεχνική προσφορά που υπέβαλαν, φαίνονται παρακάτω:\par
<?php tender_competitors_list($tender['Ενδιαφερόμενοι'], 'Δικαιολογητικά Συμμετοχής'); ?>
<?=$par++?>.\tab Η επιτροπή προχώρησε στην αξιολόγηση των δικαιολογητικών συμμετοχής και τις τεχνικές προσφορές και εισηγείται <?php
	// Εύρεση ενδιαφερόμενων που τα δικαιολογητικά τους απορρίπτονται σε Α' φάση
	$rejected_competitors = array_values(array_filter($tender['Ενδιαφερόμενοι'],
			function($competitor) { return isset($competitor['Λόγοι Απόρριψης Συμμετοχής']); }));
	// Μείωση των αποδεκτών ενδιαφερομένων σε Α' φάση
	$accepted_competitors = array_values(array_filter($tender['Ενδιαφερόμενοι'],
			function($competitor) { return !isset($competitor['Λόγοι Απόρριψης Συμμετοχής']); }));
	// Κείμενο σχετικό με το ποιος απορρίφθηκε.
	if (!count($accepted_competitors)) {
		echo 'την απόρριψη των δικαιολογητικών συμμετοχής όλων των συμμετεχουσών επιχειρήσεων, καθώς δεν πληρούν τις απαιτήσεις της διακήρυξης, για τους παρακάτω λόγους:\par' . PHP_EOL;
		tender_competitors_list($rejected_competitors, 'Λόγοι Απόρριψης Συμμετοχής');
	} else {
		echo 'την αποδοχή των δικαιολογητικών συμμετοχής των συμμετεχουσών επιχειρήσεων, καθώς πληρούν τις απαιτήσεις της διακήρυξης';
		if (count($rejected_competitors)) {
			echo ', με εξαίρεση τις παρακάτω επιχειρήσεις που τα δικαιολογητικά τους δε γίνονται δεκτά για τους παρακάτω λόγους:\par' . PHP_EOL;
			tender_competitors_list($rejected_competitors, 'Λόγοι Απόρριψης Συμμετοχής');
		} else echo '.\par' . PHP_EOL;
	}
	return $accepted_competitors;
}

/** Εξαγωγή του πρακτικού αποσφράγισης οικονομικής προσφοράς, με τους λόγους απόρριψης.
 * @param array $tender Ο διαγωνισμός
 * @param array $accepted_competitors Οι ενδιαφερόμενοι που έγιναν αποδεκτοί σε
 * πρώτη φάση
 * @param int $par Επόμενος αριθμός παραγράφου, ο οποίος ανανεώνεται με την επιστροφή
 * @return array Ενδιαφερόμενοι που έγιναν αποδεκτοί */
function unseal_economic($tender, $accepted_competitors, & $par) {
	// Εύρεση ενδιαφερόμενων που τα δικαιολογητικά τους απορρίπτονται σε Β' φάση
	$rejected_competitors = array_values(array_filter($accepted_competitors,
			function($competitor) { return isset($competitor['Λόγοι Απόρριψης Οικονομικής Προσφοράς']); }));
	// Μείωση των αποδεκτών ενδιαφερομένων μετά την Β' φάση
	$accepted_competitors = array_values(array_filter($accepted_competitors,
			function($competitor) { return !isset($competitor['Λόγοι Απόρριψης Οικονομικής Προσφοράς']); }));
	// Κείμενο σχετικό με το ποιος απορρίφθηκε.
	if (!count($accepted_competitors)) {
		echo 'την απόρριψη των οικονομικών προσφορών όλων των συμμετεχουσών επιχειρήσεων, καθώς δεν πληρούν τις απαιτήσεις της διακήρυξης, για τους παρακάτω λόγους:\par' . PHP_EOL;
		tender_competitors_list($rejected_competitors, 'Λόγοι Απόρριψης Οικονομικής Προσφοράς');
	} else {
		echo 'την αποδοχή των οικονομικών προσφορών των συμμετεχουσών επιχειρήσεων, καθώς πληρούν τις απαιτήσεις της διακήρυξης';
		if (count($rejected_competitors)) echo ', με εξαίρεση τις παρακάτω επιχειρήσεις που οι οικονομικές προσφορές τους δεν γίνονται δεκτές για τους παρακάτω λόγους:\par' . PHP_EOL;
		else echo '.\par' . PHP_EOL;
		tender_competitors_list($rejected_competitors, 'Λόγοι Απόρριψης Οικονομικής Προσφοράς');
		// Εμφάνιση προσφορών
		echo $par++ . '.\tab Οι προσφερόμενες τιμές (με ΦΠΑ) διαμορφώνονται ';
		if ($tender['Προσφορά κατά είδος'])
			echo 'όπως στις υποβληθείσες οικονομικές προσφορές που επισυνάπτονται.\par' . PHP_EOL;
		else {
			echo 'ως εξής:\par' . PHP_EOL;
			foreach($accepted_competitors as $count => $competitor)
				echo '\tab ' . greekNum($count + 1) . '.\tab '
						. rtf($competitor['Ενδιαφερόμενος']['Επωνυμία']) . ' (ΑΦΜ: '
						. rtf($competitor['Ενδιαφερόμενος']['ΑΦΜ']) . '): '
						. euro($competitor['Προσφορά']) . '.\par' . PHP_EOL;
		}
	}
	return $accepted_competitors;
}

/** Εξαγωγή της λέξης "εισήγησης" της επιτροπής.
 * @param int $par Ο αριθμός της παραγράφου */
function unseal_suggestion($par) {
	echo $par;
?>.\tab Κατόπιν των ανωτέρω, η Επιτροπή Διενέργειας του Διαγωνισμού και Αξιολόγησης των Προσφορών\par
\qc{\b ε ι σ η γ ε ί τ α ι}\par\qj
<?php
}

/** Εξαγωγή υπογραφών της επιτροπής διαγωνισμών. */
function tender_committee_signs() {
	global $data;
?>

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data['Πρόεδρος Διαγωνισμών']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Πρόεδρος Διαγωνισμών']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data['Α Μέλος Διαγωνισμών']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Α Μέλος Διαγωνισμών']['Βαθμός'])?>
\line\line\line <?=rtf($data['Β Μέλος Διαγωνισμών']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Β Μέλος Διαγωνισμών']['Βαθμός'])?>\cell\row

\sect

<?php }


/** Εξαγωγή υπογραφών για τα πρακτικά αποσφράγισης.
 * Τελευταία παράγραφος του πρακτικού, υπογραφές της επιτροπής και κλείσιμο της
 * ενότητας.
 * @param int $par Ο αριθμός της τελευταίας παραγράφου */
function unseal_signs($par) { ?>
<?=$par?>.\tab Για την διαπίστωση των ανωτέρω, συντάχθηκε το παρόν πρακτικό, το οποίο αφού αναγνώσθηκε και βεβαιώθηκε, υπογράφεται.\par

<?php
	tender_committee_signs();
}

/** Τα στοιχεία των συμβάσεων με τους αναδόχους σε διαγωνισμούς κατά είδος.
 * @param array $tender Ο διαγωνισμός
 * @return array Λίστα με τα στοιχεία των συμβάσεων */
function per_item_contractors($tender) {
	global $data;
	return array_values(array_filter($data['Συμβάσεις'],
			function($per_contract) use($tender) {
				return isset($per_contract['Διαγωνισμός']) && $tender == $per_contract['Διαγωνισμός'];
			}));
}

/** Εξαγωγή των αναδόχων και των ειδών που ο καθένας ανέλαβε, σε διαγωνισμούς κατά είδος.
 * @param array $tender Ο διαγωνισμός */
function unseal_per_item_contractors($tender) {
	echo 'την αποδοχή των οικονομικότερων κατά είδος προσφορών, όπως παρακάτω:\par' . PHP_EOL;
	foreach (per_item_contractors($tender) as $count => $per_contract) {
		$contractor = $per_contract['Δικαιούχος'];
		tender_competitor($contractor, $count);
		report($per_contract['Τιμολόγια'], $per_contract['Τιμές']);
		echo '\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj' . PHP_EOL;
	}
}

/** Εξαγωγή της εισήγησης πρακτικού αποσφράγισης οικονομικής προσφοράς.
 * Περιλαμβάνει τη εισήγηση της επιτροπής
 * @param array $per_tender Στοιχεία του διαγωνισμού
 * @param array $tender Ο διαγωνισμός
 * @param array $accepted_competitors Οι ενδιαφερόμενοι που έγιναν αποδεκτοί σε
 * πρώτη φάση
 * @param int $par Επόμενος αριθμός παραγράφου, ο οποίος ανανεώνεται με την επιστροφή
 * @return array Ενδιαφερόμενοι που έγιναν αποδεκτοί */
function unseal_suggestion_economic($per_tender, $tender, $accepted_competitors, $par) {
	unseal_suggestion($par++);
	if (!count($accepted_competitors)) {
		echo 'την επανάληψη του διαγωνισμού σε άλλο χρόνο, καθόσον καμία συμμετέχουσα επιχείρηση δεν πληρούσε τις απαιτήσεις της διακήρυξης.\par' . PHP_EOL;
		if (is_expenditure()) trigger_error('Ο διαγωνισμός δε μπορεί να είναι άγονος');
	} else if ($tender['Προσφορά κατά είδος']) unseal_per_item_contractors($tender);
	else {
		$accepted_competitors = unseal_equal_offers($accepted_competitors);
		if (count($accepted_competitors) != 1) {
			if (is_expenditure()) trigger_error('Ο διαγωνισμός δε μπορεί να είναι άγονος');
			echo 'την επανάληψη του διαγωνισμού σε 24 ώρες, με υποβολή κατ\' ελάχιστον νέων οικονομικών προσφορών, μόνο από τους παρακάτω οικονομικούς φορείς, οι οποίοι υπέβαλαν την οικονομικότερη προσφορά, αλλά ισόποση:\par' . PHP_EOL;
			foreach($accepted_competitors as $count => $competitor)
				tender_competitor($competitor['Ενδιαφερόμενος'], $count);
		} else {
			$contractor = $per_tender['Συμβάσεις'][0]['Ανάδοχος'];
			echo 'την αποδοχή της οικονομικότερης προσφοράς και την ανάδειξη ως προσωρινού αναδόχου, της επιχείρησης '
					. rtf($contractor['Επωνυμία']) . ' (ΑΦΜ: ' . rtf($contractor['ΑΦΜ']) .
					'), καθώς είναι σύμφωνη με τους όρους της διακήρυξης και πληροί τις τεχνικές προδιαγραφές.\par' . PHP_EOL;
		}
	}
	unseal_signs($par);
}

/** Εξαγωγή ενός πρακτικού αποσφράγισης δικαιολογητικών συμμετοχής και τεχνικών προσφορών.
 * @param array $per_tender Στοιχεία του διαγωνισμού
 * @param array $tender Ο διαγωνισμός */
function technical_offer_unseal_report($per_tender, $tender) {
	start_35_20();
?>

\pard\plain\sa120\qc\b ΠΡΑΚΤΙΚΟ - ΕΙΣΗΓΗΣΗ\line ΑΠΟΣΦΡΑΓΙΣΗΣ ΚΑΙ ΑΞΙΟΛΟΓΗΣΗΣ\line ΔΙΚΑΙΟΛΟΓΗΤΙΚΩΝ ΣΥΜΜΕΤΟΧΗΣ ΚΑΙ ΤΕΧΝΙΚΩΝ ΠΡΟΣΦΟΡΩΝ\par

<?php
	unseal_1_3($tender, $per_tender['Τιμολόγια']);
	$par = 4;
	$accepted_competitors = unseal_a_4_8($tender, $par);
	unseal_suggestion($par++);
	if (!count($accepted_competitors)) echo 'την επανάληψη του διαγωνισμού σε άλλο χρόνο, καθόσον καμία συμμετέχουσα επιχείρηση δεν πληρούσε τις απαιτήσεις της διακήρυξης.\par' . PHP_EOL;
	else {
		echo 'την αποδοχή των δικαιολογητικών συμμετοχής και των τεχνικών προσφορών των παρακάτω επιχειρήσεων, καθώς είναι σύμφωνες με τους όρους της διακήρυξης και πληρούν τις τεχνικές προδιαγραφές:\par' . PHP_EOL;
		foreach($accepted_competitors as $count => $competitor)
			tender_competitor($competitor['Ενδιαφερόμενος'], $count);
	}
	unseal_signs($par);
}

/** Εξαγωγή ενός πρακτικού αποσφράγισης οικονομικών προσφορών.
 * @param array $per_tender Στοιχεία του διαγωνισμού
 * @param array $tender Ο διαγωνισμός */
function economical_offer_unseal_report($per_tender, $tender) {
	// Μείωση των αποδεκτών ενδιαφερομένων μετά την Α' φάση
	$accepted_competitors = array_values(array_filter($tender['Ενδιαφερόμενοι'],
			function($competitor) { return !isset($competitor['Λόγοι Απόρριψης Συμμετοχής']); }));
	if (count($accepted_competitors)) {
		start_35_20();
?>

\pard\plain\sa120\qc\b ΠΡΑΚΤΙΚΟ - ΕΙΣΗΓΗΣΗ\line ΑΠΟΣΦΡΑΓΙΣΗΣ ΚΑΙ ΑΞΙΟΛΟΓΗΣΗΣ ΟΙΚΟΝΟΜΙΚΩΝ ΠΡΟΣΦΟΡΩΝ\par

<?php unseal_intro($tender, $per_tender['Τιμολόγια']); ?>

2.\tab Η επιτροπή συνήλθε την <?=strftime('%d %b %y και ώρα %H:%M', $tender['Χρόνος Αποσφράγισης Οικονομικών Προσφορών'])?>, παρουσία των οικονομικών φορέων, των οποίων τα δικαιολογητικά συμμετοχής και οι τεχνικές προσφορές είχαν γίνει δεκτές στην προηγούμενη συνεδρίαση της επιτροπής.\par
3.\tab Ακολούθησε η αποσφράγιση των οικονομικών προσφορών και αφού η επιτροπή τις αξιολόγησε, εισηγείται <?php
		$par = 4;
		$accepted_competitors = unseal_economic($tender, $accepted_competitors, $par);
		unseal_suggestion_economic($per_tender, $tender, $accepted_competitors, $par);

	}	// if
}

/** Εξαγωγή ενός πρακτικού αποσφράγισης που γίνεται σε μία φάση.
 * @param array $per_tender Στοιχεία του διαγωνισμού
 * @param array $tender Ο διαγωνισμός */
function offer_unseal_report($per_tender, $tender) {
	start_35_20(); ?>

\pard\plain\sa120\qc\b ΠΡΑΚΤΙΚΟ - ΕΙΣΗΓΗΣΗ\line ΑΠΟΣΦΡΑΓΙΣΗΣ ΚΑΙ ΑΞΙΟΛΟΓΗΣΗΣ\line ΔΙΚΑΙΟΛΟΓΗΤΙΚΩΝ ΣΥΜΜΕΤΟΧΗΣ,\line ΤΕΧΝΙΚΩΝ ΚΑΙ ΟΙΚΟΝΟΜΙΚΩΝ ΠΡΟΣΦΟΡΩΝ\par

<?php unseal_a_1_3($tender, $per_tender['Τιμολόγια']); ?>

4.\tab Η επιτροπή αποφάσισε η αποσφράγιση του φακέλου των δικαιολογητικών συμμετοχής, των τεχνικών προσφορών και των οικονομικών προσφορών, να γίνουν σε μια δημόσια συνεδρίαση.\par
<?php
	$par = 5;
	$accepted_competitors = unseal_a_4_8($tender, $par);
	if (count($accepted_competitors)) {
?>
<?=$par++?>.\tab Ακολούθησε η αποσφράγιση των οικονομικών προσφορών, των οικονομικών φορέων που τα δικαιολογητικά τους έγιναν αποδεκτά.\par
<?=$par++?>.\tab Η επιτροπή αξιολόγησε τις οικονομικές προσφορές και εισηγείται <?php
		$accepted_competitors = unseal_economic($tender, $accepted_competitors, $par);
	}
	unseal_suggestion_economic($per_tender, $tender, $accepted_competitors, $par);
}

/** Εξαγωγή των πρακτικών αποσφράγισης δικαιολογητικών συμμετοχής και τεχνικών προσφορών.
 * Καλείται απευθείας από το μενού του γραφικού περιβάλλοντος. */
function technical_offer_unseal_reports() {
	global $data;
	foreach($data['Διαγωνισμοί'] as $per_tender) {
		$tender = $per_tender['Διαγωνισμός'];
		if (unseal_2_phases($tender))
			technical_offer_unseal_report($per_tender, $tender);
	}
}

/** Εξαγωγή των πρακτικών αποσφράγισης είτε γίνονται σε μία είτε σε δύο φάσεις.
 * Καλείται απευθείας από το μενού του γραφικού περιβάλλοντος. */
function offer_unseal_reports() {
	global $data;
	foreach($data['Διαγωνισμοί'] as $per_tender) {
		$tender = $per_tender['Διαγωνισμός'];
		if (unseal_2_phases($tender)) {
			technical_offer_unseal_report($per_tender, $tender);
			economical_offer_unseal_report($per_tender, $tender);
		} else offer_unseal_report($per_tender, $tender);
	}
}