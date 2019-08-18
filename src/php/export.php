<?php
require_once('functions.php');

// Η μεταβλητή περιβάλλοντος καθορίζει τι θα εξαχθεί
$name = $_ENV['export'];
switch($name) {
	case 'statement_IBAN':
	case 'statement_representative':
		require_once('statement.php');
		init(3);
		if (isset($data['Έργο'])) {	// Υπάρχει ανοικτή δαπάνη
			init(6);
			statement_common($name);
		} else $name($data);
		break;
	case 'statement_gui':
		require_once('statement.php');
		init(3);
		$name();
		break;
	default:
		require_once('contract.php');
		require_once('tender.php');
		init(8);
		$name();
		break;
}

rtf_close(__FILE__);