<?php
require_once('init.php');
require_once('header.php');

// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;
// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';

foreach($data['Φύλλο Καταχώρησης'] as $paper_v)
	if (isset($paper_v['Αρχείο'])) {
		ob_start();
		require($paper_v['Αρχείο']);
		echo str_repeat(ob_get_clean(), $onlyone ? 1 : $paper_v['Πλήθος']);
	}

unset($draft, $onlyone, $paper_v);

rtf_close(__FILE__);