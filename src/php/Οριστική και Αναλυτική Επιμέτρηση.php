<?
require_once('functions.php');
init(4);

if ($data['Έργο']) {
	if (!isset($data['Εργασίες'])) trigger_error('Δεν υπάρχουν εργασίες', E_USER_ERROR);

	start_35_20();
?>

\pard\plain\sa240\qr\b <?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\par
\fs24\ul\qc ΑΝΑΛΥΤΙΚΗ ΚΑΙ ΟΡΙΣΤΙΚΗ ΕΠΙΜΕΤΡΗΣΗ\par
\pard\plain\sb120\sa120\fi567\tx1134\tx1701\qj
Σήμερα την <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>, ο επιβλέπων του έργου «<?=rtf($data['Τίτλος'])?>», <?=personi($data['Αξκος Έργου'], 0)?>, που ορίστηκε με την <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?>, πραγματοποίησε την αναλυτική και οριστική επιμέτρηση όλων των εργασιών που εκτελέστηκαν, απολογιστικά, όπως παρακάτω:\par

\pard\trowd\trhdr\fs23<?php ob_start(); ?>\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx6179
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7596
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
<?php $c = ob_get_flush(); ?>
\qc\b A/A\cell ΕΡΓΑΣΙΑ ΚΑΙ ΥΛΙΚΑ\cell ΠΟΣΟΤΗΤΑ\cell ΜΟΝΑΔΑ\b0\cell\row
\pard\trowd<?=$c?>
<?php
foreach($data['Εργασίες'] as $c => $work) {
	echo '\b\qj ' . ($c + 1) . '\cell\qj ' . rtf($work['Εργασία']) . '\cell\qc ' . num($work['Ποσότητα']) . '\cell ' . rtf($work['Μονάδα Mέτρησης']) . '\b0\cell\row' . PHP_EOL;
	if (isset($work['Υλικά']))
		foreach($work['Υλικά'] as $c => $item)
			echo '\qr ' . greeknum($c + 1) . '\cell\qj ' . rtf($item['Υλικό']) . '\cell\qc ' . num($item['Ποσότητα']) . '\cell ' . rtf($item['Μονάδα Mέτρησης']) . '\cell\row' . PHP_EOL;
}
?>

\pard\plain\fs23\par\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx4394\clftsWidth1\clNoWrap\cellx8787\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=rtf($data['Αξκος Έργου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Έργου']['Βαθμός'])?>\cell\row

\sect

<?php

}	// if

unset($c, $item, $work);

rtf_close(__FILE__);