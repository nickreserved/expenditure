<?
require_once('engine/init.php');
require_once('header.php');

if (!count($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);
if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Το Πρωτόκολλο Αφανών Εργασιών αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΠΑΡΑΛΑΒΗΣ ΑΦΑΝΩΝ ΕΡΓΑΣΙΩΝ\par\par
\pard\plain\tx567\tx1134\qj
\tab <?=chk(ucfirst($data['ΠεριοχήΈργου']))?> σήμερα την <?=$data['ΗμερομηνίαΑφανώνΕργασιών']?> η επιτροπή αφανών εργασιών αποτελούμενοι από τους:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΑφανώνΕργασιών'], 2)?> ως πρόεδρο\par
\tab\tab β. <?=man_ext($data['ΜέλοςΑφανώνΕργασιών'], 2)?> ως μέλος\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑφανώνΕργασιών'])?>\ul0 , μεταβόντες σήμερα επί τόπου του έργου «<?=$data['Έργο']?>», κατόπιν προσκλήσεως του επιβλέποντος Αξκου <?=man_ext($data['ΑξκοςΈργου'], 1)?> καταμέτρησε και παρέλαβε<?=isset($data['Εργολάβος']) ? ", ενώ βρίσκονταν παρών ο εργολήπτης {$data['Εργολάβος']['Επωνυμία']}," : ''?> τις αφανείς εργασίες και βρήκε αυτές όπως παρακάτω:\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΡΓΑΣΙΑ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row<?
$count = 0;
foreach($data['Εργασίες'] as $v) {
	?>\qr <?=++$count?>\cell\qj <?=chk($v['Εργασία'])?>\cell\qc <?=chk_measure($v['ΜονάδαMέτρησης'])?>\cell <?=num($v['Ποσότητα'])?>\cell\row<?
} ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΑφανώνΕργασιών']?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑφανώνΕργασιών']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑφανώνΕργασιών']['Βαθμός'])?>\cell
\line - ΤΟ -\line ΜΕΛΟΣ\line\line\line <?=chk($data['ΜέλοςΑφανώνΕργασιών']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑφανώνΕργασιών']['Βαθμός'])?>\cell\row

\sect

