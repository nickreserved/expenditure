<?

$data = '';

while(!feof(STDIN))
	$data .= fread(STDIN, 1024);

$data = unserialize($data);

if (!$data) die('Τα εισερχόμενα δεδομένα είναι λάθος');

?>