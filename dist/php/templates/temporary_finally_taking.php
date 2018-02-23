<?
require_once('engine/init.php');
require_once('header.php');

if (!count($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);
if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Το Πρωτόκολλο Προσωρινής και Οριστικής Παραλαβής αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΠΡΟΣΩΡΙΝΗΣ<?=!strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση') ? 'ΚΑΙ ΟΡΙΣΤΙΚΗΣ' : ''?> ΠΑΡΑΛΑΒΗΣ\par\par
\pard\plain\tx567\tx1134\qj
\tab Οι υπογεγραμένοι:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής'], 0)?> ως πρόεδρος\par
\tab\tab β. <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ'], 0)?> και\par
\tab\tab γ. <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ'], 0)?> ως μέλη\par
της επιτροπής προσωρινής και οριστικής παραλαβής του Έργου «<?=$data['Έργο']?>», που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΠροσωρινήςΟριστικήςΠαραλαβής'])?>\ul0 , μεταβόντες σήμερα τις <?=$data['ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής']?> επί τόπου του έργου (<?=chk($data['ΠεριοχήΈργου'])?>) προς παραλαβή των εκτελεσθέντων εργασιών και έχοντες υπ’ όψιν την από <?=$data['ΗμερομηνίαΟριστικήςΕπιμέτρησης']?> οριστική επιμέτρηση, θεωρημένη από τον Αξκο Έργου, εξετάσαμε και καταμετρήσαμε όλες τις εκτελεσθείσες ποσότητες αυτών παρουσία του παραδίδοντος και εκτελέσαντος την εργασία Αξκού Έργου <?=man_ext($data['ΑξκοςΈργου'], 2)?>, και βρήκαμε αυτές όπως παρακάτω:\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx5754
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx6973
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8447
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΡΓΑΣΙΑ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\cell ΜΟΝΟΓΡΑΦΗ\line ΠΡΟΕΔΡΟΥ\b0\cell\row
<?
$count = 0;
foreach($data['Εργασίες'] as $v) {
	?>\qr <?=++$count?>\cell\qj <?=chk($v['Εργασία'])?>\cell\qc <?=chk_measure($v['ΜονάδαMέτρησης'])?>\cell <?=num($v['Ποσότητα'])?>\cell\line\cell\row<?
} ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής']?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2552\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx7655\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

