<?php
system('chcp 65001 > nul');
$filename = __DIR__ . '/../src/expenditure.ini';
$data = unserialize(file_get_contents($filename));
switch($argv[1]) {
	case 'dump': var_dump($data); break;
	case 'clean-all':
		unset($data['Έκδοση']);
		$keys = array_keys($data['Δαπάνες']);
		$data['Δαπάνες'] = array('Παράδειγμα' => $data['Δαπάνες'][$keys[$data['Τρέχουσα Δαπάνη']]]);
		unset($data['Κρατήσεις']);
		unset($data['Υπεύθυνη Δήλωση']);
		unset($data['Κέλυφος']);
		unset($data['Ένα Αντίγραφο']);
		unset($data['Τρέχουσα Δαπάνη']);
		file_put_contents($filename, serialize($data));
		break;
	case 'clean': break;
	case 'help':
	default: ?>
Usage:
php <?=$argv[0]?> [option]
where option is:
dump       var_dump the data.
clean-all  cleans all the useless keys.
clean      nothing yet
help       this screen
<?php
} ?>