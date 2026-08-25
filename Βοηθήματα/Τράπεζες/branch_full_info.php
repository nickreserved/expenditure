<?php
if ($argc < 2) die("Usage: php {$argv[0]} input.csv output.php\n");
if (!file_exists($argv[1])) die("File not found: {$argv[1]}\n");
$fp = fopen($argv[1], 'r');
if (!$fp) die("Cannot open file: {$argv[1]}\n");
$data = array();
while ($row = fgetcsv($fp, 0, ";")) $data[] = $row;
fclose($fp);
ob_start();
?>/** Επιστρέφει στοιχεία για το υποκατάστημα της τράπεζας στην οποία ανήκει ένας λογαριασμός IBAN.
 * Η τράπεζα πρέπει να δραστηριοποιείται στην Ελλάδα.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ, ο οποίος πρέπει να είναι ελληνικός
 * @return array|null Στοιχεία του υποκαταστήματος της τράπεζας στην οποία ανήκει ο λογαριασμός */
function branch_full_info($iban) {
	switch(substr($iban, 4, 7)) {
<?php
$total = count($data);
for ($i = 2; $i < $total; ++$i)
{
	$row = $data[$i];
	if (!ctype_digit(substr($row[0], 1, -1))) continue;
	if (strlen($row[0]) != 7 + 2) continue;
	echo "\t\tcase {$row[0]}: return array('name' => '" . addslashes($row[2]) . "'";
	if (strlen($row[3 ])) echo ", 'address' => '" . addslashes($row[3]) . "'";
	if (strlen($row[4 ])) echo ", 'zip' => '{$row[4]}'";
	if (strlen($row[5 ])) echo ", 'phone' => '{$row[5]}'";
	if (strlen($row[6 ]) && strpbrk($row[6 ], '0123456789'))
						  echo ", 'fax' => '{$row[6]}'";
	if (strlen($row[7 ])) echo ", 'municipality' => '" . addslashes($row[7]) . "'";
	if (strlen($row[8 ])) echo ", 'perfecture' => '" . addslashes($row[8]) . "'";
	if (strlen($row[9 ])) echo ", 'branch' => " . ($row[9] == 'Yes' ? 'true' : 'false');
	if (strlen($row[10])) echo ", 'atm' => " . ($row[10] == 'Yes' ? 'true' : 'false');
	if (strlen($row[11])) echo ", 'aps' => " . ($row[11] == 'Yes' ? 'true' : 'false');
	echo ");\n";
}
?>		default: if ($trigger) trigger_error("Ο IBAN '<b>$iban</b>' αντιστοιχεί σε μη καταχωρημένη τράπεζα.");
	}
}<?php
file_put_contents($argv[2], ob_get_clean());
?>