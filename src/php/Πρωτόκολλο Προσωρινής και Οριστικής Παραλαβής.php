<?php
require_once('functions.php');
init(4);

if ($data['Έργο']) {
	if (!isset($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);

	start_35_20();
?>

\pard\plain\sa240\qr\b <?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\par
\fs24\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΠΡΟΣΩΡΙΝΗΣ ΚΑΙ ΟΡΙΣΤΙΚΗΣ ΠΑΡΑΛΑΒΗΣ\par
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\qj
Σήμερα την <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>, η επιτροπή προσωρινής και οριστικής παραλαβής, αποτελούμενοι από τους:\par
\tab α.\tab <?=personi($data['Πρόεδρος Προσωρινής και Οριστικής Παραλαβής'], 0)?> ως πρόεδρος\par
\tab β.\tab <?=personi($data['Α Μέλος Προσωρινής και Οριστικής Παραλαβής'], 0)?> και\par
\tab γ.\tab <?=personi($data['Β Μέλος Προσωρινής και Οριστικής Παραλαβής'], 0)?> ως μέλη\par
που συγκροτήθηκε με την <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?> , μετέβηκε στην έδρα του έργου «<?=rtf($data['Τίτλος'])?>», προς παραλαβή των εκτελεσθέντων εργασιών και έχοντας υπ' όψιν την Οριστική Επιμέτρηση, θεωρημένη από τον Αξκο Έργου, εξέτασε και καταμέτρησε όλες τις εκτελεσθείσες ποσότητες αυτών, παρουσία του παραδίδοντος και εκτελέσαντος την εργασία Αξκού Έργου <?=personi($data['Αξκος Έργου'], 2)?>, και βρήκαμε αυτές όπως παρακάτω:\par

\pard\trowd\trhdr\fs23<?php ob_start(); ?>\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clvertalc\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clvertalc\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx4535
\clvertalc\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx5952
\clvertalc\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7143
\clvertalc\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
<?php $c = ob_get_flush(); ?>
\qc\b A/A\cell ΕΡΓΑΣΙΑ\cell ΠΟΣΟΤΗΤΑ\cell ΜΟΝΑΔΑ\cell ΜΟΝΟΓΡΑΦΗ\line ΠΡΟΕΔΡΟΥ\b0\cell\row
\pard\trowd\trrh567<?=$c?>
<?php
foreach($data['Εργασίες'] as $c => $work)
	echo '\qr ' . ($c + 1) . '\cell\qj ' . rtf($work['Εργασία']) . '\cell\qc ' . num($work['Ποσότητα']) . '\cell ' . rtf($work['Μονάδα Mέτρησης']) . '\cell\cell\row' . PHP_EOL;
?>
\pard\plain\qr\par <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2197\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx6590\clftsWidth1\clNoWrap\cellx8787\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data['Πρόεδρος Προσωρινής και Οριστικής Παραλαβής']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Πρόεδρος Προσωρινής και Οριστικής Παραλαβής']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data['Α Μέλος Προσωρινής και Οριστικής Παραλαβής']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Α Μέλος Προσωρινής και Οριστικής Παραλαβής']['Βαθμός'])?>
\line\line\line <?=rtf($data['Β Μέλος Προσωρινής και Οριστικής Παραλαβής']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Β Μέλος Προσωρινής και Οριστικής Παραλαβής']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=rtf($data['Αξκος Έργου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Έργου']['Βαθμός'])?>\cell\row

\sect

<?php

}	// if

unset($c, $work);

rtf_close(__FILE__);