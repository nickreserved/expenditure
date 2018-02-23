<?
require_once('engine/init.php');
require_once('header.php');

if (!count($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);
if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Το Πρωτόκολλο Διοικητικής Παράδοσης αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΔΟΙΚΗΤΙΚΗΣ ΠΑΡΑΔΟΣΗΣ\par\par
\pard\plain\qj Σήμερα την <?=$data['ΗμερομηνίαΔιοικητικήςΠαράδοσης']?> ο Αξκός Έργου <?=man_ext($data['ΑξκοςΈργου'], 0)?> και ο <?=man_ext($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής'], 0)?>, πραγματοποίησαν ο πρώτος την παράδοση και ο δεύτερος την παραλαβή του έργου «<?=$data['Έργο']?>» (<?=chk($data['ΠεριοχήΈργου'])?>) όπως παρακάτω:\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΥΛΙΚΟ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($data['Εργασίες'] as $v)
	foreach($v['Υλικά'] as $i) {
		?>\qr <?=++$count?>\cell\qj <?=chk($i['Υλικό'])?>\cell\qc <?=chk_measure($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\row<?
	} ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΔιοικητικήςΠαράδοσης']?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΑΡΑΛΑΜΒΑΝΩΝ\line\line\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

