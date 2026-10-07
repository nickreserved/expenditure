<?php
require_once('functions.php');

if (!isset($output)) $output = !isset($_ENV['draft']) || $_ENV['draft'] != 'true';

/** Εξάγει το αν η διαταγή είναι αναρτητέα στο διαδίκτυο ή όχι, καθώς και το ΑΔΑ της.
 * @param array $order Το array με τα στοιχεία ταυτότητας του στρατιωτικού εγγράφου ή null */
function order_publish($order) {
	if (published()) {
		if (!isset($order['ΑΔΑ']) && !isset($order['ΑΔΑΜ'])) { ?>
\pard\plain\li5670 ΑΔΑ: \u8230.\u8230.\par
ΑΔΑΜ: \u8230.\u8230.\par
<?php	} else {
			echo '\pard\plain\li5670 ';
			if (isset($order['ΑΔΑ'])) echo 'ΑΔΑ: ' . $order['ΑΔΑ'] . '\par' . PHP_EOL;
			if (isset($order['ΑΔΑΜ'])) echo 'ΑΔΑΜ: ' . $order['ΑΔΑΜ'] . '\par' . PHP_EOL;
		} ?>
\li0\sb120\sa240\qc{\b ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}\par

<?php } else { ?>

\pard\plain\sa240\qc{\b ΜΗ ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}\par

<?php }
}

/** Ελέγχει αν απαιτείται πίνακας αποδεκτών σε ένα στρατιωτικό έγγραφο.
 * @param array|null $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @return bool Οι αποδέκτες είναι αρκετοί άρα απαιτείται πίνακας αποδεκτών */
function need_recipient_table($to, $info) {
	$f = function($x) { return strlen($x); };
	return is_array($to)   && (count($to) > 3   || max(array_map($f, $to)) > 25)
		|| is_array($info) && (count($info) > 3 || max(array_map($f, $info)) > 25)
		|| is_string($to) && strlen($to) > 25
		|| is_string($info) && strlen($info) > 25;
}

/** Παίρνει τους αποδέκτες και τους κάνει κείμενο RTF για το προ του κειμένου μέρος
 * @param array|string|null $to Οι αποδέκτες του εγγράφου για ενέργεια ή κοινοποίηση.
 * Μετά την εκτέλεση είναι string κατάλληλο για τοποθέτηση μέσα σε RTF. */
function order_header_recipients_to_rtf(&$r) {
	if (is_array($r) && count($r))
		$r = '\tab ' . implode('\line' . PHP_EOL, array_map(function($x) { return rtf($x); }, $r));
	else if (is_string($r)) $r = '\tab ' . rtf($r);
	else $r = '';
}

/** Παίρνει τους αποδέκτες και τους κάνει κείμενο RTF για τον πίνακα αποδεκτών
 * @param array|string|null $to Οι αποδέκτες του εγγράφου για ενέργεια ή κοινοποίηση.
 * Μετά την εκτέλεση είναι string κατάλληλο για τοποθέτηση μέσα σε RTF. */
function order_footer_recipients_to_rtf(&$r) {
	if (is_array($r) && count($r))
		$r = implode('\par' . PHP_EOL, array_map(function($x) { return rtf($x); }, $r));
	else if (is_string($r)) $r = rtf($r);
	else $r = '-';
}

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param array|null $order Τα στοιχεία της ταυτότητας του εγγράφου
 * @param array|string|null $to Οι αποδέκτες του εγγράφου για ενέργεια
 * @param array|string|null $info Οι αποδέκτες του εγγράφου για κοινοποίηση
 * @param mixed $output Το έγγραφο είναι ακριβές αντίγραφο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function order_header($order, $to, $info, $output, $attached, $subject, $references) {
	global $data;

	if (need_recipient_table($to, $info)) { $to = '\line Πίνακας Αποδεκτών'; $info = ''; }
	else { order_header_recipients_to_rtf($to); order_header_recipients_to_rtf($info); }
?>

\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\pard\plain\fi-1134\li1134\tx1134\intbl
{\b ΠΡΟΣ:}<?=$to?>\cell
\pard\plain\intbl
<?=wordwrap(rtf(strtouppergn($data['Μονάδα Πλήρες'])), 25, '\line ')?>\line <?=rtf(strtouppergn($data['Γραφείο']))?>\line Τηλ. <?=rtf($data['Τηλέφωνο'])?>\cell\row
\pard\plain\fi-1134\li1134\tx1134\intbl
{\b KOIN.:}<?=$info?>\cell
\pard\plain\intbl <?php
	if ($output || isset($order))
		echo $order['Φάκελος - Πρωτόκολλο'] . '\line ' . $order['Σχέδιο']  . '\line ' . rtf($data['Έδρα']) . ', ' . $order['Ημερομηνία'];
	else echo 'Φ. \u8230_ / \u8230_\line Σ. \u8230_\line ' . rtf($data['Έδρα']) . ', \u8230_ ' . strftime('%b %y');
	if ($attached) echo '\line Συν.: ' . $attached;
	?>\cell\row

\pard\plain\sb240\sa240\fi-1134\li1134\tx1134\qj
{\b ΘΕΜΑ:}\tab{\ul <?=rtf($subject)?>}\par
<?php
	if ($references) {
		$a = count($references);
		if ($a > 1) {
			echo '\pard\plain\fi-1644\li1644\tx1134\tx1644\qj{\b ΣΧΕΤ.:}';
			for($z = 0; $z < $a - 1; $z++)
				echo '\tab ' . greeknum($z + 1) . '.\tab ' . rtf($references[$z]) . '\par' . PHP_EOL;
			echo '\pard\plain\sa120\fi-1644\li1644\tx1134\tx1644\qj\tab ' . greeknum($a) . '.';
		} else echo '\pard\plain\sa120\fi-1134\li1134\tx1134\qj{\b ΣΧΕΤ.:}';
		echo '\tab{\ul ' . rtf($references[$a - 1]) . '}\par' . PHP_EOL . PHP_EOL;
	} ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<?php }

/** Εξάγει το μετά του κειμένου μέρος, ενός στρατιωτικού εγγράφου.
 * @param bool $output Το έγγραφο είναι ακριβές αντίγραφο */
function order_footer($output) { if ($output) order_footer_copy(); else order_footer_draft(); }

/** Εξάγει το μετά του κειμένου μέρος, ενός σχεδίου στρατιωτικού εγγράφου. */
function order_footer_draft() {
	global $data;
	order_footer_draft_signs(array(
		array('Το', $data['Γραφείο'], $data['Αξκος Γραφείου']),
		array('Ο', 'Υδκτης', $data['ΕΟΥ']),
		array('Ο', 'Δκτης', $data['Δκτης'])
	));
}

/** Εξάγει το μετά του κειμένου μέρος, ενός σχεδίου στρατιωτικού εγγράφου.
 * @param array $a Ένα στοιχείο για κάθε υπογράφοντα. Το στοιχείο είναι array με το άρθρο της
 * ιδιότητας του υπογράφοντος, την ιδιότητα του υπογράφοντος και τον υπογράφοντα.
 * @param int $width Το ωφέλιμο πλάτος της σελίδας (αν τυχόν η σελίδα είναι οριζόντια ή κάθετη) */
function order_footer_draft_signs($a, $width = 8788) {
	$c = count($a);
	$width /= $c;
	?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113

<?php
	for($z = 1; $z <= $c; ++$z)
		echo '\clftsWidth1\clNoWrap\cellx' . (int) ($width * $z);
	echo '\qc' . PHP_EOL;
	foreach($a as $v)
		echo '- ' . rtf($v[0]) . ' -\line{\ul ' . rtf($v[1]) . '}\line\line\line ' . rtf($v[2]['Ονοματεπώνυμο']) . '\line ' . rtf(fullrank($v[2]['Βαθμός'])) . '\cell' . PHP_EOL;
	echo '\row' . PHP_EOL . PHP_EOL;
}

/** Εξάγει το μετά του κειμένου μέρος, ενός ακριβούς αντίγραφου στρατιωτικού εγγράφου.
 * Υπογράφοντες είναι ο χειριστής και ο διοικητής. */
function order_footer_copy() {
	global $data;
	order_footer_copy_signs($data['Ιδιότητα Αξκου'], $data['Αξκος Γραφείου'], 'Διοικητής', $data['Δκτης']);
}

/** Εξάγει το μετά του κειμένου μέρος, ενός ακριβούς αντίγραφου στρατιωτικού εγγράφου. */
function order_footer_copy_signs($operatorProperty, $operator, $appovalProperty, $approvalPerson) { ?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
Ακριβές Αντίγραφο\line\line\line\line <?=person($operator)?>\line <?=rtf($operatorProperty)?>\cell
<?=person($approvalPerson)?>\line <?=rtf($appovalProperty)?>\cell\row

<?php
}


/** Εξάγει τον πίνακα αποδεκτών ενός στρατιωτικού εγγράφου.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση. */
function order_recipient_table($to, $info) {
	if (need_recipient_table($to, $info)) {
		order_footer_recipients_to_rtf($to);
		order_footer_recipients_to_rtf($info); ?>

\pard\plain\sb240\sa240\ul ΠΙΝΑΚΑΣ ΑΠΟΔΕΚΤΩΝ\par
\sb0\sa0 Αποδέκτες για Ενέργεια\ul0\par
<?=$to?>\par
{\ul Αποδέκτες για Πληροφορία}\par
<?=$info?><?php
	}
}

/** Επιστρέφει τον δικαιούχο, σαν αποδέκτη στρατιωτικού εγγράφου.
 * Η μορφή του αποδέκτη είναι 'Επωνυμία (Διευθυνση)'
 * @param array $contractor Ο δικαιούχος
 * @return string Ο δικαιούχος σαν αποδέκτης */
function get_contractor_recipient($contractor) {
	$a = $contractor['Επωνυμία'];
	if (isset($contractor['Διεύθυνση'])) $a .= " ({$contractor['Διεύθυνση']})";
	return $a;
}

/** Λίστα παραρτημάτων στρατιωτικού εγγράφου.
 * @param array $a Λίστα με τους τίτλους των παραρτημάτων */
function order_appendices($a) {
	echo '\pard\plain\sb567\sa57{\ul ΠΑΡΑΡΤΗΜΑΤΑ}\par\sb0\sa0\tx567' . PHP_EOL;
	$c = 0;
	foreach($a as $v)
		echo '«' . strtoupper(greekNum(++$c)) . '»\tab ' . rtf($v) . '\par' . PHP_EOL;
}

/** Εξάγει το προ του κειμένου μέρος ενός παραρτήματος στρατιωτικού εγγράφου.
 * @param array|null $order Τα στοιχεία της ταυτότητας του εγγράφου
 * @param string $appendix Ο αριθμός του παραρτήματος, π.χ. 'Α' ή 'ΣΤ'
 * @param string $title Ο τίτλος του παραρτήματος */
function appendix_header($order, $appendix, $title) {
	global $data;
?>
\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\line\ul ΠΑΡΑΡΤΗΜΑ «<?=$appendix?>» ΣΤΗ\line
<?=isset($order) ? "{$order['Φάκελος - Πρωτόκολλο']}/{$order['Σχέδιο']}" : 'Φ. \u8230_ / \u8230_ / \u8230_'?>\ul0\cell
<?=wordwrap(rtf(strtouppergn($data['Μονάδα Πλήρες'])), 25, '\line ')?>\line <?=rtf(strtouppergn($data['Γραφείο']))?>\line <?=isset($order) ? $order['Ημερομηνία'] : ('\u8230_ ' . strftime('%b %y'))?>\cell\row

\pard\plain\sb227\sa113\qc{\b\ul <?=rtf($title)?>}\par

<?php }

/** Εξάγει το μετά του κειμένου μέρος, ενός παραρτήματος στρατιωτικού εγγράφου.
 * @param bool $output Το έγγραφο είναι ακριβές αντίγραφο */
function appendix_footer($output) { if ($output) appendix_footer_copy(); else appendix_footer_draft(); }

/** Εξάγει το μετά του κειμένου μέρος, ενός σχεδίου παραρτήματος στρατιωτικού εγγράφου. */
function appendix_footer_draft() {
	global $data;
	order_footer_draft_signs(array(
		array('Το', $data['Γραφείο'], $data['Αξκος Γραφείου']),
		array('Ο', 'Υδκτης', $data['ΕΟΥ'])
	));
}

/** Εξάγει το μετά του κειμένου μέρος, ενός ακριβούς αντίγραφου παραρτήματος στρατιωτικού εγγράφου.
 * Υπογράφοντες είναι ο χειριστής και ο υποδιοικητής. */
function appendix_footer_copy() {
	global $data;
	order_footer_copy_signs($data['Ιδιότητα Αξκου'], $data['Αξκος Γραφείου'], 'Υποδιοικητής', $data['ΕΟΥ']);
}