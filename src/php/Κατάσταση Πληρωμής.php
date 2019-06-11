<?php
require_once('init.php');
require_once('header.php');

$c = count($data['Κρατήσεις']) + 3.6;	// 1.2 για Καθαρή Αξία, Καταλογιστέο, Πληρωτέο
if ($data['Τιμές']['ΦΕ'] > 0) $c++;
$c = floor(8901 / $c);
$d = 6236;
?>

\sectd\sbkodd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Μονάδα'])?>\line <?=$data['Ημερομηνία Τελευταίου Τιμολογίου']?>\par\par
\qc\b\ul ΚΑΤΑΣΤΑΣΗ ΠΛΗΡΩΜΗΣ\par\par

\pard\plain\fs20
\trowd\trhdr
<?php ob_start();	// Buffer με επικεφαλίδες πίνακα ?>
\trqc\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx1701
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx3402
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx4819
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d?>

\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=round(1.2 * $c)?>

<?php foreach($data['Κρατήσεις'] as $v) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>

<?php }
if ($data['Τιμές']['ΦΕ'] > 0) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>

<?php } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=round(1.2 * $c)?>

\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=round(1.2 * $c)?>

<? $c2 = ob_get_flush(); ?>
\qc\b Δικαιούχος\cell Διεύθυνση\cell e-mail\cell ΔΟΥ\cell Καθαρή\line Αξία\cell <?php
foreach($data['Κρατήσεις'] as $v)
	echo "$v\cell ";
if ($data['Τιμές']['ΦΕ'] > 0) echo 'ΦΕ\cell ';
echo 'Καταλογιστέο\cell Υπόλοιπο\line Πληρωτέο\b0\cell\row' . PHP_EOL;
echo '\trowd' . $c2 . PHP_EOL;



/* Μετά από το επόμενο τμήμα κώδικα, στο $a υπάρχει ενα array με δύο στοιχεία. Το πρώτο στοιχείο,
είναι ένα array για κάθε δικαιούχο, με τα στοιχεία του δικαιούχου και τα αθροίσματα των αξιών όλων
των τιμολογίων του δικαιούχου στη δαπάνη συμπεριλαμβανομένων Καθαρής Αξίας, Καταλογιστέου,
Πληρωτέου, ΦΕ και όλων των επιμέρους κρατήσεων. Το δεύτερο στοιχείο είναι ένα array με τα αθροίσματα
των επιμέρους κρατήσεων όλων των τιμολογίων όλων των προμηθευτών. */
// Υπολογισμοί για κάθε δικαιούχο
$b = array();
$keys = array('Καθαρή Αξία', 'Καταλογιστέο', 'ΦΕ', 'Υπόλοιπο Πληρωτέο');
foreach(get_invoices_by_contractor($data['Τιμολόγια']) as $invoices) {
	$c = calc_sum_of_invoices_prices($invoices, $keys);
	$c['Κρατήσεις'] = calc_partial_deductions($invoices);
	$b[] = array_merge($invoices[0]['Δικαιούχος'], $c);
}
// Υπολογισμοί συνόλων επιμέρους κρατήσεων για όλους τους δικαιούχους
// Τα υπόλοιπα αθροίσματα δεν υπολογίζονται γιατί υπάρχουν στη δαπάνη
$c = array_fill_keys($data['Κρατήσεις'], 0);	// Αρχικοποίηση των αθροισμάτων με τιμή 0
foreach($b as $v) {
	$deductions = $v['Κρατήσεις'];
	foreach($data['Κρατήσεις'] as $key)
		if (isset($deductions[$key])) $c[$key] += $deductions[$key];
}
$a = array ($b, $c);

foreach($a[0] as $v) {
	echo '\ql ' . rtf($v['Επωνυμία']) . '\cell ' . rtf(ifexist($v, 'Διεύθυνση')) . '\cell '
			. str_replace('@', '\zwbo @', rtf(ifexist($v, 'e-mail'))) . '\cell '
			. rtf($v['ΔΟΥ']) . '\cell\qr ' . euro($v['Καθαρή Αξία']) . '\cell ';
	$i = $v['Κρατήσεις'];
	foreach($data['Κρατήσεις'] as $t)
		echo euro(ifexist($i, $t)) . '\cell ';
	if ($data['Τιμές']['ΦΕ'] > 0)
		echo euro(ifexist($v, 'ΦΕ')) . '\cell ';
	echo euro($v['Καταλογιστέο']) . '\cell ';
	echo euro($v['Υπόλοιπο Πληρωτέο']) . '\cell\row' . PHP_EOL;
}

echo '\qr\b\cell\cell\cell Σύνολο\cell ';
echo euro($data['Τιμές']['Καθαρή Αξία']) . '\cell ';
foreach($data['Κρατήσεις'] as $t)
	echo euro(ifexist($a[1], $t)) . '\cell ';
if ($data['Τιμές']['ΦΕ'] > 0) echo euro($data['Τιμές']['ΦΕ']) . '\cell ';
echo euro($data['Τιμές']['Καταλογιστέο']) . '\cell ';
echo euro($data['Τιμές']['Υπόλοιπο Πληρωτέο']) . '\b0\cell\row' . PHP_EOL . PHP_EOL;
?>
\pard\plain\li10204\qc\par
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\par

\sect

<?php
unset($a, $b, $c, $d, $v, $i, $t, $c2, $keys, $deductions, $invoices, $key);

rtf_close(__FILE__);