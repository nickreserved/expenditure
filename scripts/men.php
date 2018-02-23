#!/bin/php
<?

function usage() { ?>
This script inserts from Excel or OpenOffice CSV files, military personel,
in "main.ini".

php men.php
	Shows this screen.

php men.php input.csv "c:\Program files\Cost"
	You must have "main.ini" inside folder above.
	Then inserts data from "input.csv" to "main.ini".

For more information, use program documentation in section General->Scripts.
<?
	exit;
}

if ($argc != 3) usage();
is_file($argv[1]) or die("File `{$argv[1]}` not exists.");
is_file($ini = "{$argv[2]}/main.ini") or die("File `$ini` not exists.");


$delimiter = "\t";
$out = "\tcommon.VectorObject Προσωπικό = {\n";

$file = file($argv[1]) or die("Problem on reading file `{$argv[1]}`");
if (isset($file[0]) && count(split(',', $file[0])) > 1) $delimiter = ',';
foreach($file as $v) {
	list($rank, $name, $unit) = split($delimiter, $v);
	$rank = trim($rank, "\" \t\r\n',"); if ($rank == '') unset($rank);
	$name = trim($name, "\" \t\r\n',"); if ($name == '') unset($name);
	$unit = trim($unit, "\" \t\r\n',"); if ($unit == '') unset($unit);
	if (!isset($rank, $name)) continue;
	$out .= "\t\tcost.Man {\n\t\t\tjava.lang.String Βαθμός = \"$rank\";\n" .
													"\t\t\tjava.lang.String Ονοματεπώνυμο = \"$name\";\n";
	if (isset($unit)) $out .= "\t\t\tjava.lang.String Μονάδα = \"$unit\";\n";
	$out .= "\t\t};\n";
}
$out .= "\t};\n";


$ini_c = file_get_contents($ini) or die ("Problem on reading file `$ini`");
$reg = '/[\t ]*common\.VectorObject\s+Προσωπικό\s*=\s*\{(\s*cost\.Man\s*\{(\s*java.lang.String\s*\w+\s*=\s*\"[^\"]*\";\s*)+\};\s*)+\};/';
$ini_c = preg_match($reg, $ini_c) ? preg_replace($reg, $out, $ini_c, 1) : preg_replace('/\};\s*$/', "$out};", $ini_c, 1);

file_put_contents($ini, $ini_c) or die ("Problem on writting file `$ini`");

echo 'SUCCESS!!!';

?>