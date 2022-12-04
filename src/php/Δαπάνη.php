<?php
require_once('contract.php');
require_once('tender.php');
require_once('statement.php');

/* Εξάγει υποφάκελο δαπάνης.
 * @param string $name Το κείμενο του υποφακέλου μαζί με τον αριθμό του, στη μορφή που δίνεται από
 * το γραφικό περιβάλλον */
function export_subfolder($name) {
	$pos = strpos($name, ':', 13);
	$num = substr($name, 0, $pos);
	$name = substr($name, $pos + 2);
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1133\margtsxn6850\margbsxn5669

\pard\plain\sa283\qc\fs72 <?=rtf($num)?>\par\hyphpar0\fs46 <?=rtf($name)?>\par

\sect

<?php

}

// Για να βγεί ακριβές αντίγραφο των διαταγών (true) και όχι σχέδιο (false)
// Παράλληλα με το 'δαπάνη', πετυχαίνουμε το true και ταυτόχρονα ότι εξάγεται δαπάνη
$output = 'δαπάνη';
// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';

init(8);

foreach($data['Φύλλο Καταχώρησης'] as $paper_v)
	if ($paper_v['Εξαγωγή']) {
		ob_start();

		$name = $paper_v['Δικαιολογητικό'];
		switch($name) {
			case 'Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού':
				statement_common('statement_IBAN', $data['Μονάδα Πλήρες']); break;
			case 'Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ':
				statement_common('statement_representative', $data['Μονάδα Πλήρες']); break;
			case 'Σύμβαση': export_contracts(); break;
			case 'Πρακτικά Αποσφράγισης Δικαιολογητικών Συμμετοχής': offer_unseal_reports(); break;
			default:
				if (substr($name, 0, 12) == 'ΥΠΟΦΑΚΕΛΟΣ «') {
					// Εδώ κάνουμε μια χακιά γιατί στις δαπάνες ήσσονος σημασίας δεν πρέπει να
					// εμφανίζεται Υποφάκελος «Β».
					// Από τη μια όμως τυχόν Φορολογική Ενημερότητα στις Απευθείας Αναθέσεις πάει
					// στον Υποφάκελο «Β» και από την άλλη, αν δεν υπάρχει Φορολογική Ενημερότητα,
					// εμφανίζεται ένας Υποφάκελος «Β» χωρίς καθόλου περιεχόμενα.
					// Η αντιμετώπιση είναι, αν έχουμε δαπάνη ήσσονος σημασίας, να ανήκουν όλα στον
					// Υποφάκελο «Α» και να μην υπάρχει Υποφάκελος «Β».
					if (isset($data['Συμβάσεις']) || substr($name, 12, 1) != 'Β')
						export_subfolder($name);
				} else require($name . '.php');
		}

		echo str_repeat(ob_get_clean(), $onlyone ? 1 : $paper_v['Πλήθος']);
	}

unset($output, $onlyone, $paper_v, $name);

rtf_close(__FILE__);