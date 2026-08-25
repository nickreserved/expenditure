<?php
require_once('functions.php');
init(6);

if (!function_exists('payment_report_table_row_properties')) {


/** Εξάγει τις ιδιότητες των κελιών με σωστή συγχώνευση κελιών κάθετα.
 * @param int $cur_invoice Τρέχον τιμολόγιο δικαιούχου.
 * @param array|null $invoices Λίστα τιμολογίων δικαιούχου. Αν είναι null, τότε πρόκειται για τις
 * ιδιότητες κελιών της επικεφαλίδας και όλες οι άλλες παράμετροι αγνοούνται.
 * @param int $cur_deduction Τρέχουσα κράτηση του τρέχοντος τιμολογίου δικαιούχου. */
function payment_report_table_row_properties($cur_invoice, $cur_deduction, $invoices = null) {
	if (!isset($invoices)) { $a = $b = ''; $h = '\trhdr'; } // ιδιότητες επικεφαλίδας
	else {
		$num_invoices = count($invoices);
		$invoice = $invoices[$cur_invoice];
		// Ίδια διάταξη κελιών
		if ($cur_deduction > 1) return;
		if (count($invoice['Κρατήσεις']) <= 2) { // Δεν προσμετράμε το 'Σύνολο'
			$b = $h = '';
			$a = $num_invoices == 1 ? '' :
					!$cur_invoice ? '\clvmgf' : '\clvmrg';
			// Αν το τρέχον τιμολόγιο έχει μόνο μια κράτηση και το προηγούμενο τιμολόγιο είχε μόνο
			// μια κράτηση, δεν υπάρχει λόγος να επαναληφθεί η διάταξη των κελιών γιατί δεν αλλάζει.
			// Αυτό ξεκινάει από την τρίτη φορά γιατί οι προηγούμενες απαιτούν '\clvmgf', '\clvmrg'.
			if ($cur_invoice > 1 && count($invoices[$cur_invoice - 1]['Κρατήσεις']) <= 2) return;
		} else {
			$a = !$cur_invoice && !$cur_deduction ? '\clvmgf' : '\clvmrg';
			$b = !$cur_deduction ? '\clvmgf' : '\clvmrg';
			$h = '';
		}
	}
?>
\trowd<?=$h?>
\trqc\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc<?=$a?>\cellx3911
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc<?=$b?>\cellx5669
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc<?=$b?>\cellx7030
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clvertalc\cellx8561
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc\cellx9808
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc<?=$b?>\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc<?=$b?>\cellx12529
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc<?=$b?>\cellx13776
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\clvertalc<?=$b?>\cellx15137
<?php
}


}	// endif function_exists



foreach($data['Δικαιούχοι'] as $per_contractor) {
	$contractor = $per_contractor['Δικαιούχος'];
	$prices = $per_contractor['Τιμές'];
?>

\sectd\sbkodd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf(strtouppergn($data['Μονάδα Πλήρες']))?>\line Τηλέφωνο <?=rtf($data['Τηλέφωνο'])?>\line <?=rtf($data['Έδρα'])?>, <?=strftime('%d %b %y', $data['Ημερομηνία Τελευταίου Τιμολογίου'])?>\par
\pard\plain\sb240\sa240\qc\b\ul ΚΑΤΑΣΤΑΣΗ ΠΛΗΡΩΜΗΣ\b0\ul0\par

\pard\plain\sa120\ql ΓΙΑ ΤΟΝ ΔΙΚΑΙΟΥΧΟ: <?=rtf($contractor['Επωνυμία'])?>, ΑΦΜ: <?=rtf($contractor['ΑΦΜ'])?>\line
<?php
if ($contractor['Τύπος'] == 'Ιδιωτικός Τομέας') { ?>
IBAN: <?=rtf($contractor['ΙΒΑΝ'])?>\line
<?php } ?>
ΠΟΣΟΥ: <?=euro2str($prices['Υπόλοιπο Πληρωτέο'])?> (<?=euro($prices['Υπόλοιπο Πληρωτέο'])?>)\line
ΛΟΓΩ: <?=rtf(inflectPhrase(isset($per_contractor['Σύμβαση']) ? $per_contractor['Σύμβαση']['Τίτλος'] : $data['Τίτλος'], 1))?>\line
ΕΙΔΙΚΟΣ ΦΟΡΕΑΣ: <?=rtf($data['ΕΦ'])?>, ΑΛΕ: <?=rtf($data['ΑΛΕ'])?>\par

\pard\plain\fs22<?=payment_report_table_row_properties(0, 0, null) ?>\qc\b Δικαιούχος\cell Παραστατικό\cell Καθαρή\line Αξία\cell Κράτηση\cell Ποσό\line Κράτησης\cell Σύνολο\line Κρατήσεων\cell Καθαρό\line Ποσό\cell ΦΕ\cell Καθαρό\line Πληρωτέο\b0\cell\row

<?php

	$invoices = $per_contractor['Τιμολόγια'];
	foreach($invoices as $i => $invoice) {
		$iprices = $invoice['Τιμές'];
		$j = 0;
		$deductions = $invoice['Ανάλυση Κρατήσεων'];
		if (!count($deductions)) $deductions = array('' => 0);
		foreach($deductions as $name => $price) {
			payment_report_table_row_properties($i, $j, $invoices);
			if (!$j) {
				if (!$i) echo '\ql ' . rtf(get_contractor_full_info($contractor));
				echo '\cell\qc ' . rtf($invoice['Τιμολόγιο']) . '\cell\qr '
					. euro($iprices['Καθαρή Αξία']) . '\cell\qc ';
				if (strlen($name)) echo rtf($name) . ' ' . percent($invoice['Κρατήσεις'][$name]);
				echo '\cell\qr ' . euro($price) . '\cell ' . euro($iprices['Κρατήσεις'])
					. '\cell\qr ' . euro($iprices['Καθαρή Αξία για ΦΕ'])
					. '\cell ' . euro($iprices['ΦΕ']) . '\cell ' . euro($iprices['Υπόλοιπο Πληρωτέο'])
					. '\cell\row' . PHP_EOL;
			} else {
				echo '\cell\cell\cell\qc ';
				if (strlen($name)) echo rtf($name) . ' ' . percent($invoice['Κρατήσεις'][$name]);
				echo '\cell\qr ' . euro($price) . '\cell\cell\cell\cell\cell\row' . PHP_EOL;
			}
			++$j;
		}
	}
	echo PHP_EOL;
?>
\trowd
\trqc\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clftsWidth1\cellx3911
\clftsWidth1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7030
\clftsWidth1\cellx8561
\clftsWidth1\cellx9808
\clftsWidth1\cellx11169
\clftsWidth1\cellx12529
\clftsWidth1\cellx13776
\clftsWidth1\cellx15137
\ql ΚΑΘΑΡΗ ΑΞΙΑ\cell\cell\qr <?=euro($prices['Καθαρή Αξία'])?>\cell\cell\cell\cell\cell\cell\cell\row
<?php
if ($prices['ΦΠΑ']) {
?>\ql ΦΠΑ\cell\cell\qr <?=euro($prices['ΦΠΑ'])?>\cell\cell\cell\cell\cell\cell\cell\row
<?php } ?>
\trowd
\trqc\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clftsWidth1\cellx3911
\clftsWidth1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx7030
\clftsWidth1\cellx8561
\clftsWidth1\cellx9808
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx12529
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx13776
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx15137
\ql ΓΕΝΙΚΟ ΣΥΝΟΛΟ\cell\cell\qr <?=euro($prices['Καταλογιστέο'])?>\cell\cell\cell <?=euro($prices['Κρατήσεις'])?>\cell <?=euro($prices['Καθαρή Αξία για ΦΕ'])?>\cell <?=euro($prices['ΦΕ'])?>\cell <?=euro($prices['Υπόλοιπο Πληρωτέο'])?>\cell\row


\pard\plain\li10204\qc\par
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\par

\sect

<?php

}
unset($contractor, $deductions, $i, $invoice, $invoices, $j, $name, $per_contractor, $price, $prices);

rtf_close(__FILE__);