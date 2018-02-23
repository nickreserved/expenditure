<?
require_once('engine/init.php');
require_once('header.php');
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

<? foreach ($bills as $v) { ?>

\pard\plain
Προμηθευτής:\b  <?=chk($v['Προμηθευτής']['Επωνυμία'])?>\b0\line
ΔΟΥ Στρατού: Ν. Ψυχικού\line
Α.Φ.Μ. Στρατού: 090153025\line\par

\pard\plain\fs21
\trowd\trhdr\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5839
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7086
<? if (count($v['ΚατηγορίεςΦΠΑ']) > 2) { ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7653
<? } ?>
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΙΔΟΣ\cell ΠΟΣΟΤΗΤΑ\cell<? if (count($v['ΚατηγορίεςΦΠΑ']) > 2) echo ' ΦΠΑ\cell'; ?> ΜΟΝΑΔΑ\cell ΣΥΝΟΛΟ\b0\cell\row
<?
$count = 0;
foreach($v['Είδη'] as $i) { ?>
\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=num($i['Ποσότητα'])?>\cell\qr <? if (count($v['ΚατηγορίεςΦΠΑ']) > 2) echo percent($i['ΦΠΑ']) . '\cell'; ?> <?=euro($i['ΤιμήΜονάδας'])?>\cell <?=euro($i['ΣυνολικήΤιμή'])?>\cell\row
<? } ?>

\pard\tx1\tqdec\tx9667<?require('report$2.php');?>
\line\b ΚΑΘΑΡΗ ΑΞΙΑ:\tab <?=euro($v['ΚαθαρήΑξία'])?>\b0\cell\row
<? if ($v['Τύπος'] == 'ΣΠ/ΚΨΜ') { ?>
Προστίθενται Κρατήσεις <?=percent($v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο'])?>:\tab <?=euro($v['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row
<? }
foreach($v['ΚατηγορίεςΦΠΑ'] as $k => $i) {
	if ($k != 'Σύνολο') { ?>
Προστίθεται ΦΠΑ <?=percent($k)?>:\tab <?=euro($i)?>\cell\row
<? } } ?>
\b ΚΑΤΑΛΟΓΙΣΤΕΟ:\tab <?=euro($v['Καταλογιστέο'])?>\b0\cell\row
Αφαιρούνται Κρατήσεις <?=percent($v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο'])?>:\tab <?=euro($v['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row
\b ΠΛΗΡΩΤΕΟ:\tab <?=euro($v['Πληρωτέο'])?>\b0\cell\row
<? if ($v['ΦΕΣεΕυρώ']) { ?>
Παρακρατήθηκε ΦΕ <?=percent($v['ΠοσοστόΦΕ'])?>:\tab <?=euro($v['ΦΕΣεΕυρώ'])?>\b0\cell\row
\b ΥΠΟΛΟΙΠΟ ΠΛΗΡΩΤΕΟ:\tab <?=euro($v['ΥπόλοιποΠληρωτέο'])?>\b0\cell\row
<? } ?>

\pard\par\par\par\par

<? } ?>