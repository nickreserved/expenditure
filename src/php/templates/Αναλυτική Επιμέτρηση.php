<?
require_once('engine/init.php');
require_once('header.php');

if (!count($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);
if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Η Αναλυτική Επιμέτρηση αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΑΝΑΛΥΤΙΚΗ ΕΠΙΜΕΤΡΗΣΗ\par\par
\pard\plain\tx567\tx1134\tx1701\qj <?=chk(ucfirst($data['ΠεριοχήΈργου']))?> σήμερα τις <?=$data['ΗμερομηνίαΑφανώνΕργασιών']?> ο επιβλέπων του έργου «<?=$data['Έργο']?>», <?=man_ext($data['ΑξκοςΈργου'], 0)?> πραγματοποίησε την αναλυτική επιμέτρηση όλων των εργασιών που εκτελέστηκαν απολογιστικά όπως παρακάτω:\par
<?
$count = 0;
foreach($data['Εργασίες'] as $v) {
	echo '\par\tab\b ' . ++$count . '.\tab\ul ' . chk($v['Εργασία']) . '\ul0\b0  (' . num($v['Ποσότητα']) . ' ' . chk_measure($v['ΜονάδαMέτρησης']) . ')\par';
	if (count($v['Υλικά'])) echo '\tab\tab Για την εργασία αυτή καταναλώθηκαν τα παρακάτω υλικά:\par';
	$count1 = 0;
	foreach($v['Υλικά'] as $i)
		echo '\tab\tab ' . countGreek(++$count1) . '.\tab ' . num($i['Ποσότητα']) . ' ' . chk_measure($i['ΜονάδαMέτρησης']) . ' ' . chk($i['Υλικό']) . '\par';
} ?>

\pard\plain\fs23\par\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

