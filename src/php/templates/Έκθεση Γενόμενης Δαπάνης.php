<?
require_once('engine/init.php');
require_once('header.php');

if (!$bills_hold_all) trigger_error('Δεν υπάρχουν τιμολόγια', E_USER_ERROR);


//------------- Buffer κάποια μεγάλα τμήματα που χρησιμοποιούνται 2 φορές
ob_start();
?>\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206<?
$c1 = ob_get_clean();
ob_start();
?>\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx4592
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5839
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7086
<? if (count($bills_info['ΚατηγορίεςΦΠΑ']) > 2) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7653
<? } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206<?
$c2 = ob_get_clean();
//----------------------------
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b\fs24\ul\qc ΕΚΘΕΣΗ\par

\pard\plain\trowd\fs21\trqr\trautofit1
\clftsWidth1\clNoWrap\cellx2948
\b ΜΟΝΑΔΑ \ul <?=chk($data['ΣύντμησηΜονάδας'])?>\ul0\b0\line
<? $a = explode(' ', ($prereport ? now() : $data['ΗμερομηνίαΤελευταίουΤιμολογίου'])); ?>
Μήνας \ul <?=$a[1]?>\ul0\line
Χρήση \ul <?=$a[2]?>\ul0\line
Φορέας \ul <?=chk($data['ΕΦ'])?>\ul0\line
ΚΑ \ul <? if (isset($data['ΚΑ'])) echo chk($data['ΚΑ']); ?>\ul0\cell\row

\pard\plain\qj\ul <?=$prereport ? 'Απαιτουμένης' : 'Γενόμενης'?> Δαπάνης για «<?=chk($data['Τίτλος'])?>».\par\par

\pard\plain\fs21
\trowd\trhdr<?=$c2?>
\qc\b A/A\cell ΕΙΔΟΣ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\cell<? if (count($bills_info['ΚατηγορίεςΦΠΑ']) > 2) echo ' ΦΠΑ\cell'; ?> ΜΟΝΑΔΑ\cell ΣΥΝΟΛΟ\b0\cell\row

<?
$count = 0;
foreach($bills as $v) {
	if (count($bills) > 1 && !$prereport)
		echo $c1. '\qc\b Τιμολόγιο: ' . chk_bill($v['Τιμολόγιο']) .
			'\b0  (Κρατήσεις: ' . percent($v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο']) .
			' - ΦΕ: ' . percent($v['ΠοσοστόΦΕ']) . ')\cell\row\trowd' . $c2;
	foreach($v['Είδη'] as $i) {
		?>\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=chk_measure($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\qr <? if (count($bills_info['ΚατηγορίεςΦΠΑ']) > 2) echo percent($i['ΦΠΑ']) . '\cell'; ?> <?=euro($i['ΤιμήΜονάδας'])?>\cell <?=euro($i['ΣυνολικήΤιμή'])?>\cell\row<?
	}
} ?>

\pard\tx1\tqdec\tx10000<?=$c1?>
\line\b ΚΑΘΑΡΗ ΑΞΙΑ:\tab <?=euro($bills_info['ΚαθαρήΑξία'])?>\b0\cell\row<?
if ($bills_warrant) {
	$b = bills_by_hold($bills_warrant);
	if ($b) {
		foreach($b as $k => $v) {
			$a = calc_bills($v);
			?> Προστίθενται Κρατήσεις <?=percent($k)?>:\tab <?=euro($a['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row<?
		}
	}
}
foreach($bills_info['ΚατηγορίεςΦΠΑ'] as $k => $v) {
	if ($k != 'Σύνολο') {
		?> Προστίθεται ΦΠΑ <?=percent($k)?>:\tab <?=euro($v)?>\cell\row<?
	}
}
?>\b ΚΑΤΑΛΟΓΙΣΤΕΟ:\tab <?=euro($bills_info['Καταλογιστέο'])?>\b0\cell\row<?
if ($bills_hold) {
	foreach($bills_hold as $k => $v) {
		$a = calc_bills($v);
		?> Αφαιρούνται Κρατήσεις <?=percent($k)?>:\tab <?=euro($a['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row<?
	}
}
?>\b ΠΛΗΡΩΤΕΟ:\tab <?=euro($bills_info['Πληρωτέο'])?>\b0\cell\row<?
if (!$prereport && $bills_fe) {
	foreach($bills_fe as $k => $v) {
		$a = calc_bills($v);
		?> Παρακρατήθηκε ΦΕ <?=percent($k)?>:\tab <?=euro($a['ΦΕΣεΕυρώ'])?>\b0\cell\row<?
	}
	?>\b ΥΠΟΛΟΙΠΟ ΠΛΗΡΩΤΕΟ:\tab <?=euro($bills_info['ΥπόλοιποΠληρωτέο'])?>\b0\cell\row<?
}
?>\pard\plain\qr\par <?=chk($data['Πόλη']) . ', ' . ($prereport ? now() : $data['ΗμερομηνίαΤελευταίουΤιμολογίου'])?>\par\par


\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\cell
<? if ($prereport) { ?>
\line - Ο -\line <?=chk(toUppercase($data['ΙδιότηταΑξκου']))?>\line\line\line <?=chk($data['ΑξκοςΓραφείου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΓραφείου']['Βαθμός'])?>\cell
<? } else { ?>
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell
<? } ?>
\row

\sect

