<?php
require_once('init.php');
require_once('header.php');

$c = count($data['Κρατήσεις']) + 3.6;	// 1.2 για Καθαρή Αξία, Καταλογιστέο, Πληρωτέο
if ($data['Τιμές']['ΦΕ'] > 0) $c++;
$c = floor(8901 / $c);
$d = 6236;
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=rtf($data['Μονάδα'])?>\line <?=$data['Ημερομηνία Τελευταίου Τιμολογίου']?>\par\par
\ul\qc ΚΑΤΑΣΤΑΣΗ ΠΛΗΡΩΜΗΣ\par\par

\pard\plain\fs20
\trowd\trhdr\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\trqc
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx1701
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx3402
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx4819
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

\qc\b Δικαιούχος\cell Διεύθυνση\cell e-mail\cell ΔΟΥ\cell Καθαρή Αξία\cell <?php
foreach($data['Κρατήσεις'] as $v)
	echo "$v\cell ";
if ($data['Τιμές']['ΦΕ'] > 0) echo 'ΦΕ\cell ';
echo 'Καταλογιστέο\cell Πληρωτέο\b0\cell\row' . PHP_EOL;

$a = calc_pay_report();

foreach($a[0] as $v) {
	echo '\ql ' . rtf($v['Επωνυμία']) . '\cell ';
	if (isset($v['Πόλη'])) {
		echo rtf($v['Πόλη']);
		if (isset($v['Διεύθυνση'])) echo ', ' . rtf($v['Διεύθυνση']);
	} ?>\cell <?
	echo rtf(ifexist($v, 'e-mail')) . '\cell ' . rtf($v['ΔΟΥ']) . '\cell\qr ' . euro($v['Καθαρή Αξία']) . '\cell ';
	$i = $v['Κρατήσεις'];
	foreach($data['Κρατήσεις'] as $t)
		echo euro(ifexist($i, $t)) . '\cell ';
	if ($data['Τιμές']['ΦΕ'] > 0)
		echo euro(ifexist($v, 'ΦΕ')) . '\cell ';
	echo euro($v['Καταλογιστέο']) . '\cell ';
	echo euro($v['Πληρωτέο']) . '\cell\row' . PHP_EOL;
}

echo '\qr\b\cell\cell\cell Σύνολο\cell ';
echo euro($data['Τιμές']['Καθαρή Αξία']) . '\cell ';
foreach($data['Κρατήσεις'] as $t)
	echo euro(ifexist($a[1], $t)) . '\cell ';
if ($data['Τιμές']['ΦΕ'] > 0) echo euro($data['Τιμές']['ΦΕ']) . '\cell ';
echo euro($data['Τιμές']['Καταλογιστέο']) . '\cell ';
echo euro($data['Τιμές']['Πληρωτέο']) . '\b0\cell\row' . PHP_EOL . PHP_EOL;
?>
\pard\plain\li10204\qc\par
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\par

\sect

<?php rtf_close(__FILE__); ?>