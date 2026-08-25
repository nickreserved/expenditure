<?php

/** Φόρτωση ενός csv αρχείου.
 * @param filename Η διαδρομή του αρχείου csv.
 * @return array Array 2 διαστάσεων. Μια για τις γραμμές και μια για τις στήλες. */
function load_file($filename) {
	if (!file_exists($filename)) die("File not found: $filename\n");
	$fp = fopen($filename, 'r');
	if (!$fp) die("Cannot open file: $filename\n");
	$data = array();
	while ($row = fgetcsv($fp, 0, ";")) $data[] = $row;
	fclose($fp);
	return $data;
}

/** Εύρεση της στήλης που έχει $find σε μια επικεφαλίδα csv.
 * @param header Το array της επικεφαλίδας του csv.
 * @param find Το όνομα της στήλης που αναζητείται.
 * @return Το index της στήλης αν βρεθεί. */
function find_column($header, $find) {
	$total = count($header);
	for ($column = 0; $column < $total; ++$column)
		if ($header[$column] == $find) break;
	if ($column == $total) die("There is no '$find' column\n");
	return $column;
}

function export_cpv_list($data, $column) { ?>
	return array(
<?php 
	$total = count($data);
	for ($i = 1; $i < $total; ++$i)
	{
		$row = $data[$i];
		echo"\t\t'{$row[0]}' => '" . addcslashes(iconv('UTF-8', 'ISO-8859-7', $row[$column]), "'") . "',". PHP_EOL;
	}
?>	);
<?php
}

if ($argc < 3) die("Usage: php {$argv[0]} cpv_input.csv supplementary_cpv_input.csv output.php\n");

// Φόρτωση του αρχείου csv με τους CPV και τους συμπληρωματικούς CPV
$cpv = load_file($argv[1]);
$sup_cpv = load_file($argv[2]);

// Εύρεση της στήλης που έχει "EL" για επικεφαλίδα .
$column_cpv = find_column($cpv[0], 'EL');
$column_sup_cpv = find_column($sup_cpv[0], 'EL');

// Έναρξη εξόδου στο stdin αλλά στην πραγματικότητα συγκράτησή του και απόδοση σε μεταβλητή.
ob_start(); ?>
/// Επιστρέφει ένα array με όλους τους CPV σαν κλειδιά και τις επεξηγήσεις σαν τιμές.
function cpv_list() {
<?php export_cpv_list($cpv, $column_cpv); ?>
}

/// Επιστρέφει ένα array με όλους τους CPV σαν κλειδιά και τις επεξηγήσεις σαν τιμές.
function supplementary_cpv_list() {
<?php export_cpv_list($sup_cpv, $column_sup_cpv); ?>
}

<?php
file_put_contents($argv[3], ob_get_clean());
?>