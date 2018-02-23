<?
require_once('engine/init.php');
require_once('header.php');

if (!count($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);
if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Η Οριστική Επιμέτρηση αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΟΡΙΣΤΙΚΗ ΕΠΙΜΕΤΡΗΣΗ\par\par
\pard\plain\qj Σήμερα την <?=$data['ΗμερομηνίαΟριστικήςΕπιμέτρησης']?> ο επιβλέπων του έργου «<?=$data['Έργο']?>» (<?=chk($data['ΠεριοχήΈργου'])?>), <?=man_ext($data['ΑξκοςΈργου'], 0)?> πραγματοποίησε την οριστική επιμέτρηση όλων των εργασιών που εκτελέσθηκαν απολογιστικά όπως παρακάτω:\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΡΓΑΣΙΑ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($data['Εργασίες'] as $v) {
	?>\qr <?=++$count?>\cell\qj <?=chk($v['Εργασία'])?>\cell\qc <?=chk_measure($v['ΜονάδαMέτρησης'])?>\cell <?=num($v['Ποσότητα'])?>\cell\row<?
} ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΟριστικήςΕπιμέτρησης']?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

