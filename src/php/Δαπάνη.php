<?php
require_once('init.php');
require_once('header.php');

// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;
// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = isset($_ENV['one']) && $_ENV['one'] == 'true';

foreach($data['Φύλλο Καταχώρησης'] as $cost_v) {
	if (isset($cost_v['Αρχείο'])) {
		ob_start();
		require($cost_v['Αρχείο']);
		$a = ob_get_clean();
		$b = $onlyone ? 1 : $cost_v['Πλήθος'];
		for($z = 0; $z < $b; $z++) echo $a;
	}
}

unset($draft, $onlyone, $cost_v, $a, $b, $z);

rtf_close(__FILE__);