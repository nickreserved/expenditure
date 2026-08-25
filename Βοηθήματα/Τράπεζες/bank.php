<?php
if ($argc < 2) die("Usage: php {$argv[0]} input.csv output.php\n");
if (!file_exists($argv[1])) die("File not found: {$argv[1]}\n");
$fp = fopen($argv[1], 'r');
if (!$fp) die("Cannot open file: {$argv[1]}\n");
$data = array();
while ($row = fgetcsv($fp, 0, ";")) $data[] = $row;
fclose($fp);
ob_start();
?>/** Επιστρέφει την τράπεζα στην οποία ανήκει ένας λογαριασμός IBAN.
 * Η τράπεζα πρέπει να δραστηριοποιείται στην Ελλάδα.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ, ο οποίος πρέπει να είναι ελληνικός
 * @param bool $trigger Πυροδοτεί σφάλμα αν ο ΙΒΑΝ δεν αντιστοιχεί σε καμία τράπεζα
 * @return string|null Η επώνυμία της τράπεζας στην οποία ανήκει ο λογαριασμός */
function bank($iban, $trigger = true) {
	switch(substr($iban, 4, 3)) {
<?php
$total = count($data);
for ($i = 2; $i < $total; ++$i)
{
	$row = $data[$i];
	echo "\t\tcase {$row[0]}: return '{$row[1]}';\n";
}
?>		default: if ($trigger) trigger_error("Ο IBAN '<b>$iban</b>' αντιστοιχεί σε μη καταχωρημένη τράπεζα.");
	}
}<?php
file_put_contents($argv[2], ob_get_clean());
?>