<?
require_once('functions.php');

$draft = isset($_ENV['draft']) && $_ENV['draft'] == 'true';

/** Εξάγει τις ιδιότητες της ενότητας κειμένου ενός στρατιωτικού εγγράφου. */
function startOrder() { ?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<? }

/** Εξάγει το προ του κειμένου μέρος ενός στρατιωτικού εγγράφου.
 * @param string|null $order Η ταυτότητα του εγγράφου
 * @param array $to Οι αποδέκτες προς ενέργεια. Τουλάχιστον ένα στοιχείο.
 * @param array|null $info Οι αποδέκτες προς κοινοποίηση. Αν κάποιο στοιχείο είναι null, αντικαθίσταται
 * από τον εκδότη του εγγράφου $order ή αν αυτό είναι null, από τη Μονάδα που συντάσσει τη δαπάνη.
 * @param bool draft Το έγγραφο είναι σχέδιο
 * @param string|null $attached Ο αριθμός συνημμένων
 * @param string $subject Το θέμα του εγγράφου
 * @param array|null $references Τα σχετικά του εγγράφου */
function preOrder($order, $to, $info, $draft, $attached, $subject, $references) {
	global $data;
	if (!$draft || isset($order)) { $ord = null; order($order, $ord); }
?>
\pard\plain\tx1134
\trowd\trautofit1\trpaddl0\trpaddr0\cellx5103\clftsWidth1\clNoWrap\cellx8788
\b ΠΡΟΣ:\b0<? foreach($to as $v) echo '\tab ' . rtf($v) . '\line'; ?>\line
\b ΚΟΙΝ.:\b0<?
	if (is_array($info))
		foreach($info as $v) {
			if (!$v) $v = isset($ord) ? $ord[5] : $data['Μονάδα'];
			if ($v) echo '\tab ' . rtf($v) . '\line';
		}
	?>\cell
<?=rtf(toUppercase($data['Μονάδα Πλήρες']))?>\line <?=rtf(toUppercase($data['Γραφείο']))?>\line Τηλ. <?=rtf($data['Τηλέφωνο'])?>\line <?
	if (!$draft || isset($ord))
		echo rtf($ord[0]) . '/' . rtf($ord[1]) . '/' . rtf($ord[2]) . '\line ' . rtf($ord[3]) . '\line ' . rtf($data['Έδρα']) . ', ' . rtf($ord[4]);
	else echo 'Φ.\line Σ.\line ' . rtf($data['Έδρα']);
	if ($attached) echo '\line Συνημμένα: ' . $attached;
	?>\cell\row

\pard\plain\sb240\sa240\fi-1134\li1134\tx1134\qj
{\b ΘΕΜΑ:}\tab{\ul <?=rtf($subject)?>}\par
<?
	if ($references) {
		$a = count($references);
		if ($a > 1) {
			echo '\pard\plain\fi-1644\li1644\tx1134\tx1644\qj{\b ΣΧΕΤ.:}';
			for($z = 0; $z < $a - 1; $z++)
				echo '\tab ' . countGreek($z + 1) . '.\tab ' . rtf($references[$z]) . '\par' . PHP_EOL;
			echo '\pard\plain\sa120\fi-1644\li1644\tx1134\tx1644\tab ' . countGreek($a) . '.';
		} else echo '\pard\plain\sa120\fi-1134\li1134\tx1134\qj{\b ΣΧΕΤ.:}';
		echo '\tab{\ul ' . rtf($references[$a - 1]) . '}\par' . PHP_EOL . PHP_EOL;
	} ?>
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\tx2268\qj
<? }

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
<? }

/** Εξάγει το μετά του κειμένου μέρος, ενός ακριβούς αντίγραφου στρατιωτικού εγγράφου. */
function postOrderCopy() {
	global $data; ?>
\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8788\qc
Ακριβές Αντίγραφο\line\line\line\line <?=person($data['Αξκος Γραφείου'])?>\line <?=rtf($data['Ιδιότητα Αξκου'])?>\cell
<?=person($data['Δκτης'])?>\line Διοικητής\cell\row
<? }