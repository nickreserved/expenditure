<?

// Για να βγεί η Έκθεση "Γενομένης" και όχι "Απαιτουμένης" Δαπάνης
$prereport = false;

// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;

require_once('engine/functions.php');
require_once('engine/order.php');
require_once('header.php');


foreach($data['ΦύλλοΚαταχώρησης'] as $cost_item)
	if (isset($cost_item['Αρχείο'])) {
		// Αν μπει ρύθμιση για να μην εκτυπώνει πάνω από 1, εδώ.
		// Να μπει με include_once γιατί μπορεί να υπάρχουν διάσπαρτα τα αρχεία.
		ob_start();
		require("{$cost_item['Αρχείο']}.php");
		$a = ob_get_clean();
		for($z = 0; $z < $cost_item['Πλήθος']; $z++) echo $a;
	}
?>