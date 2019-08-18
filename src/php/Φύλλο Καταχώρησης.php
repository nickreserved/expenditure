<?php
require_once('functions.php');
init(8);

if (!function_exists('contents_subfolder')) {

/** Εξάγει την εγγραφή ενός υποφακέλου.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @return Ο αριθμός υποφακέλου στη μορφή 'Α2' */
function contents_subfolder($content_item) {
	global $countA;
	$name = $content_item['Δικαιολογητικό'];
	$pos = strpos($name, '»', 13);
	$countA = substr($name, 12, $pos - 12);
	$name = substr($name, $pos + 3);
	echo '\cell\cell\b\line ' . $countA . '\cell\line ' . $name . '\b0\cell\cell\row' . PHP_EOL;
}

/** Εξάγει τις εγγραφές των τιμολογίων. */
function contents_invoices() {
	global $data, $count, $countA;
	foreach($data['Τιμολόγια'] as $invoice)
		echo ++$count . '\cell ' . rtf($invoice['Δικαιούχος']['Επωνυμία']) . '\cell ' . $countA
				. '\cell Τιμολόγιο ' . $invoice['Τιμολόγιο'] . '\cell\cell\row' . PHP_EOL;
}

/** Εξάγει τις εγγραφές των πρωτοκόλλων οριστικής ποιοτικής και ποσοτικής παραλαβής. */
function contents_acceptance_protocol() {
	global $data;
	if (isset($data['Συμβάσεις']))
		foreach($data['Συμβάσεις'] as $per_contract) {
			$flags = 0;	// FLAGS: 1: Προμήθεια Υλικών, 2: Παροχή Υπηρεσιών
			foreach($per_contract['Τιμολόγια'] as $invoice) {
				$flags |= is_supply($invoice['Κατηγορία']) ? 1 : 2;
				if ($flags == 3) break;
			}
			if ($flags) {
				$f = function($a) use($per_contract) {
					global $data, $count, $countA;
					echo ++$count . '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA
							. '\cell Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής '
							. $a . ' «' . rtf($per_contract['Δικαιούχος']['Επωνυμία'])
							. '»\cell\cell\row' . PHP_EOL;
				};
				if ($flags & 1) $f('Προμηθειών');
				if ($flags & 2) $f('Υπηρεσιών');
			}
		}
}

/** Εξάγει τις εγγραφές των βεβαιώσεων παραλαβής. */
function contents_acceptance_affirmation() {
	global $data;
	$flags = 0;	// FLAGS: 1: Προμήθεια Υλικών, 2: Παροχή Υπηρεσιών
	foreach($data['Δικαιούχοι'] as $per_contractor)
		if (!isset($per_contractor['Σύμβαση']))
			foreach($per_contractor['Τιμολόγια'] as $invoice) {
				$flags |= is_supply($invoice['Κατηγορία']) ? 1 : 2;
				if ($flags == 3) break 2;
			}
	if ($flags) {
		$f = function($a) {
			global $data, $count, $countA;
			echo ++$count . '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA . '\cell Βεβαίωση Παραλαβής '
					. $a . '\cell\cell\row' . PHP_EOL;
		};
		if ($flags & 1) $f('Προμηθειών');
		if ($flags & 2) $f('Υπηρεσιών');
	}
}

/** Εξάγει τις εγγραφές των αποσπασμάτων ποινικών μητρώων. */
function contents_criminal_record() {
	global $data, $count, $countA;
	foreach($data['Δικαιούχοι'] as $per_contractor) {
		$contractor = $per_contractor['Δικαιούχος'];
		if ($contractor['Τύπος'] == 'Ιδιωτικός Τομέας' && isset($per_contractor['Σύμβαση']))
			echo ++$count . '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA
				. '\cell Απόσπασμα Ποινικού Μητρώου\cell\cell\row' . PHP_EOL;
	}
}

/** Εξάγει τις εγγραφές των υπεύθυνων δηλώσεων.
 * @param array $content_item Η καταχώρηση περιεχομένων */
function contents_statement($content_item) {
	global $data, $count, $countA;
	foreach($data['Δικαιούχοι'] as $per_contractor) {
		$contractor = $per_contractor['Δικαιούχος'];
		if ($contractor['Τύπος'] == 'Ιδιωτικός Τομέας')
			echo ++$count . '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA
				. '\cell ' . rtf($content_item['Δικαιολογητικό']) . '\cell\cell\row' . PHP_EOL;
	}
}

/** Εξάγει τις εγγραφές των φορολογικών και ασφαλιστικών ενημεροτήτων. */
function contents_tax_insurrance_currency() {
	global $data, $count, $countA;
	foreach($data['Δικαιούχοι'] as $per_contractor) {
		$contractor = $per_contractor['Δικαιούχος'];
		if ($contractor['Τύπος'] == 'Ιδιωτικός Τομέας') {
			$mixed = $per_contractor['Τιμές']['Καταλογιστέο'];
			if ($mixed > 1500)
				echo ++$count . '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA
					. '\cell Φορολογική Ενημερότητα\cell\cell\row' . PHP_EOL;
			if ($mixed > 3000 || $per_contractor['Τιμές']['Καθαρή Αξία'] > 2500)
				echo ++$count . '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA
					. '\cell Ασφαλιστική Ενημερότητα\cell\cell\row' . PHP_EOL;
		}
	}
}

/** Εξάγει τις εγγραφές των συμβάσεων. */
function contents_contract() {
	global $data, $count, $countA;
	if (isset($data['Συμβάσεις']))
		foreach($data['Συμβάσεις'] as $per_contract)
			echo ++$count . '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA . '\cell Σύμβαση '
					. $per_contract['Σύμβαση']['Σύμβαση'] . ' με «'
					. rtf($per_contract['Δικαιούχος']['Επωνυμία'])
					. '»\cell\cell\row' . PHP_EOL;
}

/** Απαριθμεί και εξάγει τα δικαιολογητικά που υπέβαλαν οι διαγωνιζόμενοι. */
function contents_interested_documents() {
	global $data, $count, $countA;
	foreach($data['Διαγωνισμοί'] as $per_tender)
		foreach($per_tender['Διαγωνισμός']['Ενδιαφερόμενοι'] as $per_competitor) {
			$a = '\cell ' . rtf($per_competitor['Ενδιαφερόμενος']['Επωνυμία']) . '\cell ' . $countA . '\cell ';
			foreach($per_competitor['Δικαιολογητικά Συμμετοχής'] as $document)
				echo ++$count . $a . $document . '\cell\cell\row' . PHP_EOL;
		}
}

/** Απαριθμεί και εξάγει τα δικαιολογητικά που υπέβαλαν οι προσωρινοί ανάδοχοι. */
function contents_legal_documents() {
	global $data, $count, $countA;
	foreach($data['Διαγωνισμοί'] as $per_tender) {
		foreach($per_tender['Συμβάσεις'] as $contract) {
			$contractor = $contract['Ανάδοχος'];
			$a = '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA . '\cell ';
			foreach($per_tender['Διαγωνισμός']['Δικαιολογητικά Κατακύρωσης'] as $document)
				echo ++$count . $a . $document . '\cell\cell\row' . PHP_EOL;
		}
	}
}

/** Εξάγει διαταγές διαγωνισμών μαζί με το αποδεικτικό κοινοποίησής τους.
 * @param array $content_item Τα στοιχεία μιας εγγραφής περιεχομένων */
function contents_order_tender($content_item, $proof = true) {
	global $data, $count, $countA;
	foreach($data['Διαγωνισμοί'] as $per_tender) {
		$tender = $per_tender['Διαγωνισμός'];
		contents_order($content_item, $tender[$content_item['Δικαιολογητικό']]);
		if ($proof)
			echo ++$count . '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA
					. '\cell Αποδεικτικό Κοινοποίησης της\par'
					. $tender[$content_item['Δικαιολογητικό']]['Ταυτότητα'] . '\cell\cell\row' . PHP_EOL;
	}
}

/** Εξάγει τα πρακτικά ελέγχου δικαιολογητικών κατακύρωσης.
 * @param array $content_item Τα στοιχεία μιας εγγραφής περιεχομένων */
function contents_award_check() {
	global $data, $count, $countA;
	$a = '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA . '\cell Πρακτικό Ελέγχου Δικαιολογητικών Κατακύρωσης (';
	foreach($data['Διαγωνισμοί'] as $per_tender)
		echo ++$count . $a . strftime('%d %b %y', $per_tender['Διαγωνισμός']['Χρόνος Κατάθεσης Δικαιολογητικών Κατακύρωσης']) . ')\cell\cell\row' . PHP_EOL;
}

/** Εξάγει τα πρακτικά αποσφράγισης προσφορών. */
function contents_unseal() {
	global $data, $count, $countA;
	$a = '\cell ' . rtf($data['Μονάδα']) . '\cell ' . $countA . '\cell ';
	foreach($data['Διαγωνισμοί'] as $per_tender) {
		$tender = $per_tender['Διαγωνισμός'];
		$b = strftime('%d %b %y', $tender['Χρόνος Αποσφράγισης Προσφορών']) . ')\cell\cell\row' . PHP_EOL;
		if (has_2_unseals($tender)) {
			echo ++$count . $a . 'Πρακτικό αποσφράγισης δικαιολογητικών συμμετοχής και τεχνικών προσφορών (' . $b;
			echo ++$count . $a . 'Πρακτικό αποσφράγισης οικονομικών προσφορών ('
					. strftime('%d %b %y', $tender['Χρόνος Αποσφράγισης Οικονομικών Προσφορών'])
					. ')\cell\cell\row' . PHP_EOL;
		} else echo ++$count . $a . 'Πρακτικό αποσφράγισης προσφορών (' . $b;
	}
}

/** Εξάγει τις εγγραφές διαταγών.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @param string $order Η ταυτότητα της διαταγής */
function contents_order($content_item, $order) {
	contents_default_low($content_item, $order['Εκδότης'], '\par ' . $order['Ταυτότητα']);
}

/** Εξάγει μια γενική εγγραφή.
 * @param array $content_item Η καταχώρηση περιεχομένων */
function contents_default($content_item) {
	global $data;
	$issuer = orelse($content_item, 'Εκδότης', $data['Μονάδα']);
	contents_default_low($content_item, $issuer, null);
}

/** Εξάγει μια γενική εγγραφή.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @param string $issuer Ο εκδότης της διαταγής
 * @param string $line2 μια δεύτερη γραμμή κάτω από την βασική περιγραφή της καταχώρησης */
function contents_default_low($content_item, $issuer, $line2) {
	global $count, $countA;
	$c = $content_item['Πλήθος'] > 1 ? " x{$content_item['Πλήθος']}" : null;
	echo ++$count . '\cell ' . rtf($issuer) . '\cell ' . $countA . '\cell '
			. rtf($content_item['Δικαιολογητικό']) . $line2 . '\cell' . $c . '\cell\row' . PHP_EOL;
}

/** Εξάγει μια βεβαίωση μη χρέωσης, ή ένα ΑΔΔΥ.
 * @param array $content_item Η καταχώρηση περιεχομένων */
function contents_debit_affirmation($content_item) {
	global $data;
	foreach($data['Τιμολόγια'] as $invoice)
		if (is_supply($invoice['Κατηγορία'])) {
			contents_default($content_item);
			break;
		}
}

}
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\sa113\qc\fs24\b ΦΥΛΛΟ ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par
\qj Τίτλος Δαπάνης: \b0{\ul <?=rtf($data['Τίτλος'])?>}\par

\pard\plain\fs22
\trowd\trhdr
<?php ob_start();	// Buffer με επικεφαλίδες πίνακα ?>
\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx453
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx3288
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx3741
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx9637
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx10205
<? $c2 = ob_get_flush(); ?>
\qc\b Α/Α\cell ΕΚΔΟΤΗΣ\cell Α/Υ\cell ΠΕΡΙΛΗΨΗ ΕΓΓΡΑΦΟΥ\cell ΠΑΡ.\b0\cell\row
<?php
echo '\trowd' . $c2 . '\qj ' . PHP_EOL;

foreach($data['Φύλλο Καταχώρησης'] as $content_item) {
	if (!$content_item['Καταχώρηση']) continue;
	$name = $content_item['Δικαιολογητικό'];
	if (substr($content_item['Δικαιολογητικό'], 0, 12) == 'ΥΠΟΦΑΚΕΛΟΣ «') contents_subfolder($content_item);
	else
		switch($name) {
			case 'Τιμολόγια': contents_invoices(); break;
			case 'Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής': contents_acceptance_protocol(); break;
			case 'Βεβαίωση Παραλαβής': contents_acceptance_affirmation(); break;
			case 'ΑΔΔΥ':
			case 'Βεβαίωση μη Χρέωσης Υλικών': contents_debit_affirmation($content_item); break;
			case 'Απόσπασμα Ποινικού Μητρώου': contents_criminal_record(); break;
			case 'Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού':
			case 'Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ': contents_statement($content_item); break;
			case 'Φορολογική και Ασφαλιστική Ενημερότητα': contents_tax_insurrance_currency(); break;
			case 'Σύμβαση': contents_contract(); break;
			case 'Δικαιολογητικά Κατακύρωσης Προσωρινού Αναδόχου': contents_legal_documents(); break;
			case 'Δικαιολογητικά Συμμετοχής Οικονομικών Φορέων': contents_interested_documents(); break;
			case 'Πρακτικά Αποσφράγισης Δικαιολογητικών Συμμετοχής': contents_unseal(); break;
			case 'Πρακτικό Ελέγχου Δικαιολογητικών Κατακύρωσης': contents_award_check(); break;
			case 'Κατακύρωση Διαγωνισμού':
			case 'Απόφαση Ανάδειξης Προσωρινού Αναδόχου': contents_order_tender($content_item, true); break;
			case 'Διακήρυξη Διαγωνισμού': contents_order_tender($content_item, false); break;
			case 'Απόφαση Απευθείας Ανάθεσης': if (!has_direct_assignment()) break; // else continue
			case 'Απόφαση Ανάληψης Υποχρέωσης':
			case 'Δγη Συγκρότησης Επιτροπών': contents_order($content_item, $data[$content_item['Δικαιολογητικό']]); break;
			case 'Βεβαίωση Εκτέλεσης του Έργου από Οπλίτες':
				if (has_service_category($data['Τιμολόγια'])) break; // else continue
			case 'Πρωτόκολλο Παραλαβής Αφανών Εργασιών':
			case 'Πρωτόκολλο Προσωρινής και Οριστικής Παραλαβής Παραλαβής':
			case 'Οριστική και Αναλυτική Επιμέτρηση':
				if (!$data['Έργο']) break; // else continue
			default: contents_default($content_item); break;
		}
}

unset($c2, $content_item, $count, $countA, $name);
?>

\sect

<?php

rtf_close(__FILE__);