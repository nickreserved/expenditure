<?php
require_once('functions.php');

$draft = isset($_ENV['draft']) && $_ENV['draft'] == 'true';

/** Εξάγει το αν η διαταγή είναι αναρτητέα στο διαδίκτυο ή όχι, καθώς και το ΑΔΑ της.
 * @param string adaKey Το κλειδί του $data με το ΑΔΑ της διαταγής, εφόσον κοινοποιείται στο διαδίκτυο
 * @param bool draft Το έγγραφο είναι σχέδιο */
function order_publish($adaKey, $draft) {
	global $data;
	order_publish_plain($data['Αναρτητέα στο διαδίκτυο'], $adaKey, $draft);
}

/** Εξάγει το αν η διαταγή είναι αναρτητέα στο διαδίκτυο ή όχι, καθώς και το ΑΔΑ της.
 * @param bool $public Είναι αναρτητέα στο διαδίκτυο
 * @param string adaKey Το κλειδί του $data με το ΑΔΑ της διαταγής, εφόσον κοινοποιείται στο διαδίκτυο
 * @param bool draft Το έγγραφο είναι σχέδιο */
function order_publish_plain($public, $adaKey, $draft) {
	global $data;
	if ($public) { ?>

\pard\plain\qr ΑΔΑ: <?=orelse2(!$draft, $data, $adaKey, '........')?>\par
\pard\plain\qc{\ul\b ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}\par\par

<?php } else { ?>

\pard\plain\qc{\ul\b ΜΗ ΑΝΑΡΤΗΤΕΑ ΣΤΟ ΔΙΑΔΙΚΤΥΟ}\par\par

<?php }
}


/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * Υπολογίζει αυτόματα αν πρέπει να εξαχθούν οι αποδέκτες ή πρέπει να μπει 'Πίνακας Αποδεκτών'.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function order_header_autorecipients($order, $to, $info, $draft, $attached, $subject, $references) {
	if (need_recipient_table($to, $info))
		order_header_recipients($order, $draft, $attached, $subject, $references);
	else order_header($order, $to, $info, $draft, $attached, $subject, $references);
}

/** Ελέγχει αν απαιτείται πίνακας αποδεκτών σε ένα στρατιωτικό έγγραφο.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @return bool Οι αποδέκτες είναι αρκετοί άρα απαιτείται πίνακας αποδεκτών */
function need_recipient_table($to, $info) { return (count($to) + is_array($info) ? count($info) : 0) > 5; }

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου, με πίνακα αποδεκτών.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function order_header_recipients($order, $draft, $attached, $subject, $references) {
	$r = <<<'EOT'
{\b ΠΡΟΣ:}\line Πίνακας Αποδεκτών\line\par
{\b ΚΟΙΝ.:}
EOT;
	order_header_common($order, $r, $draft, $attached, $subject, $references);
}

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function order_header($order, $to, $info, $draft, $attached, $subject, $references) {
	$r = '{\b ΠΡΟΣ:}\tab ';
	foreach($to as $v) $r .= rtf($v) . '\line';
	$r .= '\par' . PHP_EOL;
	$r .= '{\b ΚΟΙΝ.:}\tab ';
	if (is_array($info))
		foreach($info as $v) $r .= rtf($v) . '\line';
	order_header_common($order, $r, $draft, $attached, $subject, $references);
}

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param string $recipients Οι αποδέκτες του εγγράφου
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function order_header_common($order, $recipients, $draft, $attached, $subject, $references) {
	global $data;
	if (!$draft || isset($order)) { $ord = null; order($order, $ord); }
?>

\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\pard\plain\fi-1134\li1134\tx1134\intbl
<?=$recipients?>\cell
\pard\plain\intbl
<?=wordwrap(rtf(strtouppergn($data['Μονάδα Πλήρες'])), 25, '\line ')?>\line <?=rtf(strtouppergn($data['Γραφείο']))?>\line Τηλ. <?=rtf($data['Τηλέφωνο'])?>\line <?php
	if (!$draft || isset($ord))
		echo rtf($ord[0]) . '/' . rtf($ord[1]) . '/' . rtf($ord[2]) . '\line ' . rtf($ord[3]) . '\line ' . rtf($data['Έδρα']) . ', ' . rtf($ord[4]);
	else echo 'Φ. \u8230_ / \u8230_ / \u8230_\line Σ. \u8230_\line ' . rtf($data['Έδρα']) . ', \u8230_ ' . strftime('%b %y');
	if ($attached) echo '\line Συνημμένα: ' . $attached;
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
			echo '\pard\plain\sa120\fi-1644\li1644\tx1134\tx1644\tab ' . greeknum($a) . '.';
		} else echo '\pard\plain\sa120\fi-1134\li1134\tx1134\qj{\b ΣΧΕΤ.:}';
		echo '\tab{\ul ' . rtf($references[$a - 1]) . '}\par' . PHP_EOL . PHP_EOL;
	} ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<?php }

/** Εξάγει το μετά του κειμένου μέρος, ενός στρατιωτικού εγγράφου.
 * @param bool $draft Το έγγραφο εκδίδεται σαν σχέδιο, ειδάλλως σαν ακριβές αντίγραφο */
function order_footer($draft) { if ($draft) order_footer_draft(); else order_footer_copy(); }

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

<?php }



/** Εξάγει τον πίνακα αποδεκτών ενός στρατιωτικού εγγράφου.
 * Υπολογίζει αυτόματα αν πρέπει να μπει 'Πίνακας Αποδεκτών'.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση */
function order_recipient_table_auto($to, $info) {
	if (need_recipient_table($to, $info)) order_recipient_table($to, $info);
}

/** Εξάγει τον πίνακα αποδεκτών ενός στρατιωτικού εγγράφου.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση. */
function order_recipient_table($to, $info) { ?>

\pard\plain\sb567\sa57\ul ΠΙΝΑΚΑΣ ΑΠΟΔΕΚΤΩΝ\par
\sb0\sa0 Αποδέκτες για Ενέργεια\ul0\par
<?php
	foreach($to as $v) echo rtf($v) . '\par'. PHP_EOL;
	if (is_array($info)) {
?>\sb57{\ul Αποδέκτες για Πληροφορία}\par\sb0
<?php
		foreach($info as $v) echo rtf($v) . '\par'. PHP_EOL;
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
 * @param string $order Η ταυτότητα του στρατιωτικού εγγράφου, ή null αν είναι σχέδιο
 * @param string $appendix Ο αριθμός του παραρτήματος, π.χ. 'Α' ή 'ΣΤ'
 * @param string $title Ο τίτλος του παραρτήματος */
function appendix_header($order, $appendix, $title) {
	global $data;
?>
\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\line\ul ΠΑΡΑΡΤΗΜΑ «<?=$appendix?>» ΣΤΗ\line
<?php
	if (isset($order)) {
		order($order, $order);
		echo "{$order[0]}/{$order[1]}/{$order[2]}/{$order[3]}";
	} else echo 'Φ. \u8230_ / \u8230_ / \u8230_ / \u8230_';
?>\ul0\cell
<?=wordwrap(rtf(strtouppergn($data['Μονάδα Πλήρες'])), 25, '\line ')?>\line <?=rtf(strtouppergn($data['Γραφείο']))?>\line <?=isset($order) ? $order[4] : ('\u8230_ ' . strftime('%b %y'))?>\cell\row

\pard\plain\sb227\sa113\qc{\b\ul <?=rtf($title)?>}\par

<?php }

/** Εξάγει το μετά του κειμένου μέρος, ενός παραρτήματος στρατιωτικού εγγράφου.
 * @param bool $draft Το έγγραφο εκδίδεται σαν σχέδιο, ειδάλλως σαν ακριβές αντίγραφο */
function appendix_footer($draft) { if ($draft) appendix_footer_draft(); else appendix_footer_copy(); }

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