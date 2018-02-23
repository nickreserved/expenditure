<?
require_once('engine/init.php');
require_once('header.php');

// Για να βγεί η Έκθεση "Γενομένης" και όχι "Απαιτουμένης" Δαπάνης
$prereport = false;

// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;

// Αν η ρύθμιση "Μόνο μια φορά" είναι ενεργή
$onlyone = getEnvironment('one', 'true');

if (!isset($data['ΦύλλοΚαταχώρησης']))
	trigger_error('Ορίστε <b>Τύπο Δαπάνης</b> και <b>Τύπο Διαγωνισμού</b> για να δημιουργηθεί το Φύλλο Καταχώρησης', E_USER_ERROR);

foreach($data['ΦύλλοΚαταχώρησης'] as $cost_v)
	if (isset($cost_v['Αρχείο'])) {
		ob_start();
		require("{$cost_v['Δικαιολογητικό']}.php");
		$a = ob_get_clean();
		$b = $onlyone ? 1 : $cost_v['Πλήθος'];
		for($z = 0; $z < $b; $z++) echo $a;
	}

?>