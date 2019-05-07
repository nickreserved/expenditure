<?php
require_once('functions.php');

$draft = isset($_ENV['draft']) && $_ENV['draft'] == 'true';

/** Εξάγει τις ιδιότητες της ενότητας κειμένου ενός στρατιωτικού εγγράφου. */
function startOrder() { ?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

<?php }

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * Υπολογίζει αυτόματα αν πρέπει να εξαχθούν οι αποδέκτες ή πρέπει να μπει 'Πίνακας Αποδεκτών'.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function preOrderS($order, $to, $info, $draft, $attached, $subject, $references) {
	if (need_recipient_table($to, $info))
		preOrderN($order, $draft, $attached, $subject, $references);
	else preOrder($order, $to, $info, $draft, $attached, $subject, $references);
}

/** Ελέγχει αν απαιτείται πίνακας αποδεκτών σε ένα στρατιωτικό έγγραφο.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @return bool Οι αποδέκτες είναι αρκετοί άρα απαιτείται πίνακας αποδεκτών */
function need_recipient_table($to, $info) { return (count($to) + is_array($info) ? count($info) : 0) > 5; }


/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function preOrderN($order, $draft, $attached, $subject, $references) {
	$r = <<<'EOT'
{\b ΠΡΟΣ:}\line Πίνακας Αποδεκτών\line\par
{\b ΚΟΙΝ.:}
EOT;
	preOrderR($order, $r, $draft, $attached, $subject, $references);
}

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function preOrder($order, $to, $info, $draft, $attached, $subject, $references) {
	$r = '{\b ΠΡΟΣ:}\tab ';
	foreach($to as $v) $r .= rtf($v) . '\line';
	$r .= '\par' . PHP_EOL;
	$r .= '{\b ΚΟΙΝ.:}\tab ';
	if (is_array($info))
		foreach($info as $v) $r .= rtf($v) . '\line';
	preOrderR($order, $r, $draft, $attached, $subject, $references);
}

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param string $recipients Οι αποδέκτες του εγγράφου
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function preOrderR($order, $recipients, $draft, $attached, $subject, $references) {
	global $data;
	if (!$draft || isset($order)) { $ord = null; order($order, $ord); }
?>

\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\pard\plain\fi-1134\li1134\tx1134
<?=$recipients?>\cell
\pard\plain
<?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\line <?=rtf(strtouppergn($data['Γραφείο']))?>\line Τηλ. <?=rtf($data['Τηλέφωνο'])?>\line <?php
	if (!$draft || isset($ord))
		echo rtf($ord[0]) . '/' . rtf($ord[1]) . '/' . rtf($ord[2]) . '\line ' . rtf($ord[3]) . '\line ' . rtf($data['Έδρα']) . ', ' . rtf($ord[4]);
	else echo 'Φ.\line Σ.\line ' . rtf($data['Έδρα']);
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
function postOrder($draft) { if ($draft) postOrderDraft(); else postOrderCopy(); }


/** Εξάγει το μετά του κειμένου μέρος, ενός σχεδίου στρατιωτικού εγγράφου. */
function postOrderDraft() {
	global $data; ?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2929\clftsWidth1\clNoWrap\cellx5858\clftsWidth1\clNoWrap\cellx8788\qc
- Το -\line\ul <?=rtf($data['Γραφείο'])?>\ul0\line\line\line <?=rtf($data['Αξκος Γραφείου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Γραφείου']['Πλήρης Βαθμός'])?>\cell
- Ο -\line\ul Υδκτης\ul0\line\line\line <?=rtf($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=rtf($data['ΕΟΥ']['Πλήρης Βαθμός'])?>\cell
- Ο -\line\ul Δκτης\ul0\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Πλήρης Βαθμός'])?>\cell\row
<?php }

/** Εξάγει το μετά του κειμένου μέρος, ενός ακριβούς αντίγραφου στρατιωτικού εγγράφου. */
function postOrderCopy() {
	global $data; ?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
Ακριβές Αντίγραφο\line\line\line\line <?=person($data['Αξκος Γραφείου'])?>\line <?=rtf($data['Ιδιότητα Αξκου'])?>\cell
<?=person($data['Δκτης'])?>\line Διοικητής\cell\row
<?php }

/** Εξάγει τον πίνακα αποδεκτών ενός στρατιωτικού εγγράφου.
 * Υπολογίζει αυτόματα αν πρέπει να μπει 'Πίνακας Αποδεκτών'.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση */
function recipientTableOrderS($to, $info) {
	if (need_recipient_table($to, $info)) recipientTableOrder($to, $info);
}

/** Εξάγει τον πίνακα αποδεκτών ενός στρατιωτικού εγγράφου.
 * @param array $to Οι αποδέκτες προς ενέργεια
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση. */
function recipientTableOrder($to, $info) { ?>

\pard\plain\sb567\sa57\ul ΠΙΝΑΚΑΣ ΑΠΟΔΕΚΤΩΝ\par
\sb0\sa0 Αποδέκτες για Ενέργεια\ul0\par\sb0
<?php
	foreach($to as $v) echo rtf($v) . '\par'. PHP_EOL;
	if (is_array($info)) {
?>\sb57{\ul Αποδέκτες για Πληροφορία}\par\sb0
<?php
		foreach($info as $v) echo rtf($v) . '\par'. PHP_EOL;
	}
}