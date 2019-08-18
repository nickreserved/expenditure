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

// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$output = 'δαπάνη';
// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';
// Ενημερώνουμε ότι πρόκειται για δαπάνη
$data['Δαπάνη'] = true;

init(8);

foreach($data['Φύλλο Καταχώρησης'] as $paper_v)
	if ($paper_v['Εξαγωγή']) {
		ob_start();

		$name = $paper_v['Δικαιολογητικό'];
		switch($name) {
			case 'Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού':
				statement_common('statement_IBAN'); break;
			case 'Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ':
				statement_common('statement_representative'); break;
			case 'Σύμβαση': export_contracts(); break;
			case 'Πρακτικά Αποσφράγισης Δικαιολογητικών Συμμετοχής': offer_unseal_reports(); break;
			default:
				if (substr($name, 0, 12) == 'ΥΠΟΦΑΚΕΛΟΣ «') export_subfolder($name);
				else require($name . '.php');
		}

		echo str_repeat(ob_get_clean(), $onlyone ? 1 : $paper_v['Πλήθος']);
	}

unset($output, $onlyone, $paper_v, $name);

rtf_close(__FILE__);