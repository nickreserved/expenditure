<?php
require_once('init.php');
require_once('header.php');

// Πλάτος κελιών πίνακα
$c = count($data['Κρατήσεις']) + 3.6;	// 1.2 για Καθαρή Αξία, Καταλογιστέο, Πληρωτέο
if ($data['Τιμές']['ΦΕ'] > 0) $c++;
$c = floor(8901 / $c);
$d = 6236;
?>

\sectd\sbkodd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Μονάδα'])?>\line <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>\par\par
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

<?php foreach($data['Κρατήσεις'] as $deduction_name) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>

<?php }
if ($data['Τιμές']['ΦΕ'] > 0) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>

<?php } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=round(1.2 * $c)?>

\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=round(1.2 * $c)?>

<? $c = ob_get_flush(); ?>
\qc\b Δικαιούχος\cell Διεύθυνση\cell e-mail\cell ΔΟΥ\cell Καθαρή\line Αξία\cell <?php
foreach($data['Κρατήσεις'] as $deduction_name)
	echo "$deduction_name\cell ";
if ($data['Τιμές']['ΦΕ'] > 0) echo 'ΦΕ\cell ';
echo 'Καταλογιστέο\cell Υπόλοιπο\line Πληρωτέο\b0\cell\row' . PHP_EOL;
echo '\trowd' . $c . PHP_EOL;

foreach($data['Δικαιούχοι'] as $per_contractor) {
	$contractor = $per_contractor['Δικαιούχος'];
	$prices = $per_contractor['Τιμές'];
	echo '\ql ' . rtf($contractor['Επωνυμία']) . '\cell ' . rtf(ifexist($contractor, 'Διεύθυνση'))
			. '\cell ' . str_replace('@', '\zwbo @', rtf(ifexist($contractor, 'e-mail'))) . '\cell '
			. rtf($contractor['ΔΟΥ']) . '\cell\qr ' . euro($prices['Καθαρή Αξία']) . '\cell ';
	$deductions = calc_partial_deductions($per_contractor['Τιμολόγια']);
	foreach($data['Κρατήσεις'] as $deduction_name)
		echo euro(ifexist($deductions, $deduction_name)) . '\cell ';
	if ($data['Τιμές']['ΦΕ'] > 0)
		echo euro(ifexist($prices, 'ΦΕ')) . '\cell ';
	echo euro($prices['Καταλογιστέο']) . '\cell ';
	echo euro($prices['Υπόλοιπο Πληρωτέο']) . '\cell\row' . PHP_EOL;
}

$prices = $data['Τιμές'];
$deductions = calc_partial_deductions($data['Τιμολόγια']);
echo '\qr\b\cell\cell\cell Σύνολο\cell ';
echo euro($prices['Καθαρή Αξία']) . '\cell ';
foreach($data['Κρατήσεις'] as $deduction_name)
	echo euro(ifexist($deductions, $deduction_name)) . '\cell ';
if ($prices['ΦΕ'] > 0) echo euro($prices['ΦΕ']) . '\cell ';
echo euro($prices['Καταλογιστέο']) . '\cell ';
echo euro($prices['Υπόλοιπο Πληρωτέο']) . '\b0\cell\row' . PHP_EOL . PHP_EOL;
?>
\pard\plain\li10204\qc\par
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\par

\sect

<?php
unset($c, $contractor, $d, $deduction_name, $deductions, $per_contractor, $prices);

rtf_close(__FILE__);