<?php
require_once('init.php');
require_once('header.php');

if (!function_exists('contents_subfolder')) {

/** Εξάγει την εγγραφή ενός υποφακέλου.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @return Ο αριθμός υποφακέλου στη μορφή 'Α2' */
function contents_subfolder($content_item) {
	$name = $content_item['Δικαιολογητικό'];
	$pos = strpos($name, '»', 13);
	$countA = substr($name, 12, $pos - 12);
	$name = substr($name, $pos + 3);
	echo '\cell\cell\b\line ' . $countA . '\cell\line ' . $name . '\b0\cell\cell\row' . PHP_EOL;
	return $countA;
}

/** Εξάγει τις εγγραφές των τιμολογίων. */
function contents_invoices() {
	global $data, $count, $countA;
	foreach($data['Τιμολόγια'] as $invoice)
		echo ++$count . '\cell ' . rtf($invoice['Δικαιούχος']['Επωνυμία']) . '\cell ' . $countA
				. '\cell Τιμολόγιο ' . invoice($invoice['Τιμολόγιο']) . '\cell\cell\row' . PHP_EOL;
}

/** Εξάγει τις εγγραφές των πρωτοκόλλων οριστικής ποιοτικής και ποσοτικής παραλαβής. */
function contents_acceptance_protocol() {
	global $data;
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor)
		if (isset($per_contractor['Σύμβαση'])) {
			$flags = 0;	// FLAGS: 1: Προμήθεια Υλικών, 2: Παροχή Υπηρεσιών
			foreach($per_contractor['Τιμολόγια'] as $invoice) {
				$flags |= is_supply($invoice['Κατηγορία']) ? 1 : 2;
				if ($flags == 3) break;
			}
			if ($flags) {
				$f = function($a) use($per_contractor) {
					global $count, $unit, $countA;
					echo ++$count . '\cell ' . $unit . '\cell ' . $countA
							. '\cell Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής '
							. $a . ' «' . rtf($per_contractor['Δικαιούχος']['Επωνυμία'])
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
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor)
		if (!isset($per_contractor['Σύμβαση']))
			foreach($per_contractor['Τιμολόγια'] as $invoice) {
				$flags |= is_supply($invoice['Κατηγορία']) ? 1 : 2;
				if ($flags == 3) break 2;
			}
	if ($flags) {
		$f = function($a) {
			global $count, $unit, $countA;
			echo ++$count . '\cell ' . $unit . '\cell ' . $countA . '\cell Βεβαίωση Παραλαβής '
					. $a . '\cell\cell\row' . PHP_EOL;
		};
		if ($flags & 1) $f('Προμηθειών');
		if ($flags & 2) $f('Υπηρεσιών');
	}
}

/** Εξάγει τις εγγραφές των αποσπασμάτων ποινικών μητρώων. */
function contents_criminal_record() {
	global $data, $count, $countA;
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
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
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
		$contractor = $per_contractor['Δικαιούχος'];
		if ($contractor['Τύπος'] == 'Ιδιωτικός Τομέας')
			echo ++$count . '\cell ' . rtf($contractor['Επωνυμία']) . '\cell ' . $countA
				. '\cell ' . rtf($content_item['Δικαιολογητικό']) . '\cell\cell\row' . PHP_EOL;
	}
}

/** Εξάγει τις εγγραφές των φορολογικών και ασφαλιστικών ενημεροτήτων. */
function contents_tax_insurrance_currency() {
	global $data, $count, $countA;
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
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
	global $data, $count, $countA, $unit;
	if (isset($data['Συμβάσεις']))
		foreach($data['Συμβάσεις'] as $contract)
			echo ++$count . '\cell ' . $unit . '\cell ' . $countA . '\cell Σύμβαση '
					. contract($contract['Σύμβαση']) . ' με «' . rtf($contract['Ανάδοχος']['Επωνυμία'])
					. '»\cell\cell\row' . PHP_EOL;
}

/** Εξάγει τις εγγραφές διαταγών.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @param string $order Η ταυτότητα της διαταγής */
function contents_order($content_item, $order) {
	$b = null;
	$line2 = '\par ' . order($order, $b);
	contents_default_low($content_item, $b[5], $line2);
}

/** Εξάγει μια γενική εγγραφή.
 * @param array $content_item Η καταχώρηση περιεχομένων */
function contents_default($content_item) {
	global $unit;
	$issuer = orelse($content_item, 'Εκδότης', $unit);
	contents_default_low($content_item, $issuer, null);
}

/** Εξάγει μια γενική εγγραφή.
 * @param array $content_item Η καταχώρηση περιεχομένων
 * @param string $issuer Ο εκδότης της διαταγής
 * @param string $line2 μια δεύτερη γραμμή κάτω από την βασική περιγραφή της καταχώρησης */
function contents_default_low($content_item, $issuer, $line2) {
	global $count, $countA;
	$c = $content_item['Πλήθος'] > 1 ? " (x{$content_item['Πλήθος']})" : '';
	echo ++$count . '\cell ' . $issuer . '\cell ' . $countA . '\cell ' . $content_item['Δικαιολογητικό']
			. $c . $line2 . '\cell\cell\row' . PHP_EOL;
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



// Συντομεύσεις
$unit = rtf($data['Μονάδα']);
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
$count = 0;		// Αρίθμηση δικαιολογητικών
$countA = null;	// Αρίθμηση υποφακέλων

foreach($data['Φύλλο Καταχώρησης'] as $content_item) {
	if (!$content_item['Καταχώρηση']) continue;
	$name = $content_item['Δικαιολογητικό'];
	if (substr($content_item['Δικαιολογητικό'], 0, 12) == 'ΥΠΟΦΑΚΕΛΟΣ «') $countA = contents_subfolder($content_item);
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
			case 'Απόφαση Απευθείας Ανάθεσης': if (!has_direct_assignment()) break; // else continue
			case 'Απόφαση Ανάληψης Υποχρέωσης':
			case 'Δγη Συγκρότησης Επιτροπών': contents_order($content_item, $data[$content_item['Δικαιολογητικό']]); break;
			default: contents_default($content_item); break;
		}
}

/*			case 'Βεβαίωση Απόδοσης ΦΕ':
				if ($data['Τιμές']['ΦΕ']) goto def;
				break;
	/*elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && !count($data['Εργασίες']));
	elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && (!$bills_buy || empty($data['Εργασίες'])));
	elseif (!$bills_buy && (
					$d == 'Πρωτόκολλο Αγοράς και Διάθεσης' ||
					$d == 'Πρωτόκολλο Ποιοτικής και Ποσοτικής Παραλαβής' && $data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή' ||
					$d == 'Βεβαίωση Ανεφοδιαστικού Οργάνου' ||
					$d == 'Βεβαίωση Μη Χρέωσης Υλικών'));
	elseif (!$bills_contract && (
					$d == 'Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας' ||
					$d == 'Βεβαίωση Επισκευαστικού Οργάνου'));
	elseif ($d == 'Σύμβαση' && !isset($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['ΕΑΑΔΗΣΥ']));*/

unset($c2, $content_item, $count, $countA, $name, $unit);
?>

\sect

<?php

rtf_close(__FILE__);