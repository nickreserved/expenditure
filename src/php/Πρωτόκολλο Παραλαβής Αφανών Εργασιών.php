<?php
require_once('functions.php');
init(4);

if ($data['Έργο']) {
	if (!isset($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);

	start_35_20();
?>

\pard\plain\sa240\qr\b <?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\par
\fs24\ul\qc ΠΡΩΤΟΚΟΛΛΟ ΠΑΡΑΛΑΒΗΣ ΑΦΑΝΩΝ ΕΡΓΑΣΙΩΝ\par
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\qj
Σήμερα την <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>, η επιτροπή αφανών εργασιών αποτελούμενοι από τους:\par
\tab α.\tab <?=personi($data['Πρόεδρος Αφανών Εργασιών'], 2)?> ως πρόεδρο\par
\tab β.\tab <?=personi($data['Μέλος Αφανών Εργασιών'], 2)?> ως μέλος\par
που συγκροτήθηκε με την <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?> , μετέβηκε στην έδρα του έργου «<?=rtf($data['Τίτλος'])?>», κατόπιν προσκλήσεως του επιβλέποντος Αξκου <?=personi($data['Αξκος Έργου'], 1)?>, όπου καταμέτρησε και παρέλαβε, παρουσία του Εργολάβου, τις αφανείς εργασίες και βρήκε αυτές όπως παρακάτω:\par

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
\clftsWidth1\clNoWrap\cellx2929\clftsWidth1\clNoWrap\cellx5858\clftsWidth1\clNoWrap\cellx8787\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data['Πρόεδρος Αφανών Εργασιών']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Πρόεδρος Αφανών Εργασιών']['Βαθμός'])?>\cell
\line - ΤΟ -\line ΜΕΛΟΣ\line\line\line <?=rtf($data['Μέλος Αφανών Εργασιών']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Μέλος Αφανών Εργασιών']['Βαθμός'])?>\cell\row

\sect

<?php

}	// if

unset($c, $work);

rtf_close(__FILE__);