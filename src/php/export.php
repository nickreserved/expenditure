<?php
require_once('basic.php');
require_once('unserialize.php');
require_once('header.php');
require_once('statement.php');
require_once('contract.php');

// Η μεταβλητή περιβάλλοντος καθορίζει τι θα εξαχθεί
$name = $_ENV['export'];
switch($name) {
	case 'statement_IBAN':
	case 'statement_representative':
		if ($_ENV['many'] == 'true') {
			require_once('init.php');
			statement_common($name);
		} else $name($data);
		break;
	default: $name(); break;
}

rtf_close(__FILE__);