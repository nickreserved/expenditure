<?
require_once('engine/init.php');
require_once('header.php');

if (!$bills_hold_all) trigger_error('Δεν υπάρχουν τιμολόγια', E_USER_ERROR);

$c = count($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']) - 1;
if (($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) $c++;
if (($bills_info['ΦΕΣεΕυρώ']) > 0) $c++;
$c = floor(8335 / $c);
$d = 3188;
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par
\ul\qc ΣΥΓΚΕΝΤΡΩΤΙΚΗ ΚΑΤΑΣΤΑΣΗ ΚΡΑΤΗΣΕΩΝ ΥΠΕΡ ΤΡΙΤΩΝ\par\par

\pard\plain\fs20
\trowd\trhdr\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\trqc
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx436
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx2042
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx3188
<? if (($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>
<? } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=1436?>
<? if ($bills_fe) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>
<? }
for($z = 1; $z < count($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']); $z++) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx<?=$d+=$c?>
<? } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx14030
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx15136

\qc\b A/A\cell Τιμολόγιο\cell Καθαρή Αξία\cell<? if (($bills_info ['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) echo ' ΦΠΑ\cell' ?> Καταλογιστέο\cell
<?
if ($bills_fe) echo 'ΦΕ' . (count($bills_fe) == 1 ? '\line ' . percent(key($bills_fe)) : '') . '\cell';
foreach($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'] as $k => $v)
	if ($k != 'Σύνολο')
		echo " $k" . (count($bills_hold_all) == 1 ? '\line ' . percent($bills[0]['ΑνάλυσηΚρατήσεωνΣεΠοσοστά'][$k]): '') . '\cell';
?>
 Κρατήσεις<? if (count($bills_hold_all) == 1) echo '\line ' . percent($bills[0]['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο'])?>\cell Πληρωτέο\b0\cell\row

<? $count = 0;
foreach($bills_hold_all as $v) {
	if (count($bills_hold_all) > 1) {
		?>\qc\b\cell\cell\cell<?
		if (($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) echo '\cell';
		?>\cell<?
		if ($bills_fe) echo '\cell ';
		foreach($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'] as $k => $i)
			if ($k != 'Σύνολο') echo (isset($v[0]['ΑνάλυσηΚρατήσεωνΣεΠοσοστά'][$k]) ? percent($v[0]['ΑνάλυσηΚρατήσεωνΣεΠοσοστά'][$k]) : '') . '\cell ';
		echo percent($v[0]['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο']) . '\cell\cell\row\b0';
	}

	foreach($v as $i) {
		?>\qr <?=++$count?>\cell\qc <?=chk_bill($i['Τιμολόγιο'])?>\cell\qr <?=euro($i['ΚαθαρήΑξία'])?>\cell <?
		if (($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) echo euro($i['ΚατηγορίεςΦΠΑ']['Σύνολο']) . '\cell ';
		echo euro($i['Καταλογιστέο']) . '\cell ';
		if ($bills_fe) echo euro($i['ΦΕΣεΕυρώ']) . '\cell ';
		foreach($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'] as $k => $t)
			if ($k != 'Σύνολο') echo (isset($i['ΑνάλυσηΚρατήσεωνΣεΕυρώ'][$k]) ? euro($i['ΑνάλυσηΚρατήσεωνΣεΕυρώ'][$k]) : '') . '\cell ';
		echo euro($i['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο']) . '\cell ' . euro($i['Πληρωτέο']) . '\cell\row';
	}
} ?>

\qr\b\cell Σύνολο\cell <?=euro($bills_info['ΚαθαρήΑξία'])?>\cell
<? if (($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) > 0) echo euro($bills_info['ΚατηγορίεςΦΠΑ']['Σύνολο']) . '\cell ' ?>
<?=euro($bills_info['Καταλογιστέο'])?>\b0\cell<?
if ($bills_fe) echo ' ' . euro($bills_info['ΦΕΣεΕυρώ']) . '\cell';
foreach($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'] as $k => $v)
	if ($k != 'Σύνολο') echo ' ' . euro($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ'][$k]) . '\cell';
?>\b <?=euro($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\cell
<?=euro($bills_info['Πληρωτέο'])?>\cell\row

\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΤελευταίουΤιμολογίου']?>\par


\pard\plain\fs23
\trowd\trkeep\cellx7568\cellx15136\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

