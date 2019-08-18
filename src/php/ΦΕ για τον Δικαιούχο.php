<?php
require_once('functions.php');
init(6);

if (!$data['Τιμές']['ΦΕ']) trigger_error('Δεν υπάρχουν τιμολόγια με ΦΕ', E_USER_ERROR);
foreach ($data['Δικαιούχοι'] as $per_contractor) {
	if (!$per_contractor['Τιμές']['ΦΕ']) continue;
	$contractor = $per_contractor['Δικαιούχος'];
?>

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain I. ΣΤΟΙΧΕΙΑ ΤΟΥ ΥΠΟΧΡΕΟΥ\par

\pard\plain\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx6178
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx11227
\cellx15136
\qc <?=rtf($data['Μονάδα Πλήρες'])?>\line\b Επωνυμία\b0\line\line<?=rtf(get_unit_address())?>\line\b Διεύθυνση (Πόλη - Οδός - Τ.Κ.)\b0\line\line 090153025\line\b Α.Φ.Μ.\b0\cell
Στρατιωτική Μονάδα\line\b Νομική Μορφή\b0\line\line <?=rtf($data['Τηλέφωνο'])?>\line\b Αρ. Τηλεφώνου\b0\line\line ΔΟΥ Ν. Ψυχικού\line\b Δ.Ο.Υ.\b0\cell
\b ΒΕΒΑΙΩΣΗ\b0\line\line Παρακρατούμενου φόρου από προμήθειες κάθε είδους αγαθών ή παροχής υπηρεσιών από τις δημόσιες ΟΤΑ, ΝΠΔΔ κ.λ.π. (περιπτ. στ' παραγ. 1 άρθρου 37α του Ν.Δ. 3323/1955)\cell\row

\pard\plain\line II. ΣΤΟΙΧΕΙΑ ΕΠΙΧΕΙΡΗΣΗΣ\par
\trowd\trautofit1\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx6178
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11227
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qc <?=rtf($contractor['Επωνυμία'])?>\line\b Επωνυμία\b0\line\line <?=rtf($contractor['Διεύθυνση'])?>\line\b Διεύθυνση (Πόλη - Οδός - Τ.Κ.)\b0\cell
<?php if (isset($contractor['Τηλέφωνο'])) echo rtf($contractor['Τηλέφωνο']); ?>
\line\b Τηλέφωνο\b0\cell
<?=rtf($contractor['ΑΦΜ'])?>\line\b Α.Φ.Μ.\b0\line\line <?=rtf($contractor['ΔΟΥ'])?>\line\b Δ.Ο.Υ.\b0\cell\row

\pard\plain\line III. ΣΤΟΙΧΕΙΑ ΣΥΝΑΛΛΑΓΗΣ\par

\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx2835
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qc Είδος τιμολογίου\cell Αριθμός τιμολογίου\cell Καθαρό ποσό συναλλαγής\cell Ποσό φόρου που παρακρατήθηκε\cell\row
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx2835
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11961
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
<?php
	foreach ($per_contractor['Τιμολόγια'] as $invoice)
		echo '\qc ' . rtf($invoice['Κατηγορία']) . '\cell ' . $invoice['Τιμολόγιο'] . '\cell\qr '
			. euro($invoice['Τιμές']['Καθαρή Αξία για ΦΕ']) . '\cell\qc ' . percent($invoice['ΦΕ'])
			. '\cell\qr ' . euro($invoice['Τιμές']['ΦΕ']) . '\cell\row' . PHP_EOL
?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5669
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx8787
\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15136
\qr ΣΥΝΟΛΟ\cell \qr <?=euro($per_contractor['Τιμές']['Καθαρή Αξία για ΦΕ'])?>\cell\qr <?=euro($per_contractor['Τιμές']['ΦΕ'])?>\cell\row

\pard\plain\qr <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', time())?>\par


\pard\plain\fs23\trowd\trkeep\cellx7568\cellx15136\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=rtf($data['Αξκος Γραφείου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Γραφείου']['Βαθμός'])?>\cell\row

\sect

<?php
}

unset($contractor, $invoice, $per_contractor);

rtf_close(__FILE__);