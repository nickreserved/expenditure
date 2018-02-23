<?
$fp = fopen('providers.ini', 'r');
while(!feof($fp)) {
	list($provider, $a, $afm, $doy, $phone, $tk, $city, $address) = split("\t", fgets($fp));
	$address = trim($address);
	echo "cost.Provider {\r\n";
	if ($provider != '') echo "\tjava.lang.String Επωνυμία = \"$provider}\";\r\n";
	if ($afm != '') echo "\tjava.lang.String ΑΦΜ = \"$afm\";\r\n";
	if ($doy != '') echo "\tjava.lang.String ΔΟΥ = \"$doy\";\r\n";
	if ($phone != '') echo "\tjava.lang.String Τηλέφωνο = \"$phone\";\r\n";
	if ($tk != '') echo "\tjava.lang.String Τ.Κ. = \"$tk\";\r\n";
	if ($city != '') echo "\tjava.lang.String Πόλη = \"$city\";\r\n";
	if ($address != '') echo "\tjava.lang.String Διεύθυνση = \"$address\";\r\n";
	echo "};\r\n";
}
fclose($fp);
?>