<?php
require_once('init.php');
require_once('contract.php');
require_once('statement.php');
require_once('header.php');

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

/** Εξάγει ένα δικαιολογητικό της δαπάνης.
 * @param string $name Το όνομα του δικαιολογητικού όπως δίνεται από το γραφικό περιβάλλον */
function export($name) {
	switch($name) {
		case 'Υπεύθυνη Δήλωση, Γνωστοποίησης Τραπεζικού Λογαριασμού':
			statement_common('statement_IBAN'); break;
		case 'Υπεύθυνη Δήλωση, μη Χρησιμοποίησης Αντιπροσώπου Εταιρίας, Αξκου των ΕΔ':
			statement_common('statement_representative'); break;
		case 'Σύμβαση': export_contracts(); break;
		default:
			if (substr($name, 0, 12) == 'ΥΠΟΦΑΚΕΛΟΣ «') export_subfolder($name);
			else {
				global $data, $draft;
				require($name . '.php');
			}
	}
}


// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;
// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';

foreach($data['Φύλλο Καταχώρησης'] as $paper_v)
	if ($paper_v['Εξαγωγή']) {
		ob_start();
		export($paper_v['Δικαιολογητικό']);
		echo str_repeat(ob_get_clean(), $onlyone ? 1 : $paper_v['Πλήθος']);
	}

unset($draft, $onlyone, $paper_v, $name);

rtf_close(__FILE__);