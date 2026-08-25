<?php
if ($argc < 2) die("Usage: php {$argv[0]} input.csv output.php\n");
if (!file_exists($argv[1])) die("File not found: {$argv[1]}\n");
$fp = fopen($argv[1], 'r');
if (!$fp) die("Cannot open file: {$argv[1]}\n");
$data = array();
while ($row = fgetcsv($fp, 0, ";")) $data[] = $row;
fclose($fp);
ob_start();
?>/** Επιστρέφει στοιχεία για την τράπεζα στην οποία ανήκει ένας λογαριασμός IBAN.
 * Η τράπεζα πρέπει να δραστηριοποιείται στην Ελλάδα.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ, ο οποίος πρέπει να είναι ελληνικός
 * @return array|null Στοιχεία της τράπεζας στην οποία ανήκει ο λογαριασμός */
function bank_full_info($iban) {
	switch(substr($iban, 4, 3)) {
<?php
$total = count($data);
for ($i = 2; $i < $total; ++$i)
{
	$row = $data[$i];
	echo         "\t\tcase {$row[0]}: return array('code'    => {$row[0]}" .
							  ",\n\t\t\t\t\t\t\t\t 'name'    => '{$row[1]}'";
	if (strlen($row[2])) echo ",\n\t\t\t\t\t\t\t\t 'address' => '{$row[2]}'";
	if (strlen($row[3])) echo ",\n\t\t\t\t\t\t\t\t 'phone'   => '{$row[3]}'";
	if (strlen($row[4])) echo ",\n\t\t\t\t\t\t\t\t 'fax'     => '{$row[4]}'";
	if (strlen($row[5])) echo ",\n\t\t\t\t\t\t\t\t 'url'     => '{$row[5]}'";
	echo ");\n";
}
?>		default: if ($trigger) trigger_error("Ο IBAN '<b>$iban</b>' αντιστοιχεί σε μη καταχωρημένη τράπεζα.");
	}
}<?php
file_put_contents($argv[2], ob_get_clean());
?>