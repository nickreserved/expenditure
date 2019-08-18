<?php
require_once('functions.php');
init(6);

if (!$data['Τιμές']['ΦΕ']) trigger_error('Δεν υπάρχουν τιμολόγια με ΦΕ', E_USER_ERROR);
// Δεν χρησιμοποιείται σε κανένα άλλο δικαιολογητικό αυτός ο κώδικας, προκειμένου να μεταφερθεί στο functions.php
// Διαχωρισμός τιμολογίων κατά μήνα.
$per_months = array();
foreach($data['Τιμολόγια'] as $invoice)
	if ($invoice['ΦΕ']) $per_months[strftime('%B %Y', $invoice['Ημερομηνία'])][] = $invoice;
// Υπολογισμός αξιών σε € για τις μηνιαίες ομάδες τιμολογίων.
foreach($per_months as & $per_month)
	$per_month = array(
		'Τιμολόγια' => $per_month,
		'Τιμές' => calc_sum_of_invoices_prices($per_month)
	);
// Επανάληψη για κάθε μήνα
foreach ($per_months as $date => $per_month) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs21\trowd\cellx7370\cellx10206
Προς τη ΔΟΥ\line Περίοδος:  <?=$date?>\cell
Δήλωση:\line Αριθμός Φακέλου:\line Φορ. Μητρώο:\cell\row

\pard\plain\qc{\b Δ Η Λ Ω Σ Η}\par\par
{\fs20 Απόδοσης παρακρατουμένου φόρου από προμήθειες κάθε είδους αγαθών ή παροχής υπηρεσιών από τις δημόσιες υπηρεσίες, Ο.Τ.Α., Ν.Π.Δ.Δ. κ.λ.π. (περιπτώσεις στ' παραγράφου 1 άρθρου 37α Ν.Δ. 3323/1955)}\par\par

\pard\plain\trowd
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx5670
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc <?=rtf($data['Μονάδα Πλήρες'])?>\line\b Επωνυμία\b0\line\line <?=rtf(get_unit_address())?>\line\b Διεύθυνση (Πόλη - Οδός - Τ.Κ.)\b0\line\line 090153025\line\b Α.Φ.Μ.\b0\cell
Στρατιωτική Μονάδα\line\b Νομική Μορφή\b0\line\line <?=rtf($data['Τηλέφωνο']) ?>\line\b Αρ. Τηλεφώνου\b0\line\line ΔΟΥ Ν. Ψυχικού\line\b Δ.Ο.Υ.\b0\cell\row

\pard\plain\par\par


\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx3118
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx5386
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx8220
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc Είδος τιμολογίου\cell Καθαρή αξία\cell Συντελεστής φόρου\cell Ποσό φόρου\cell\row
<?php
	foreach($per_month['Τιμολόγια'] as $invoice)
		echo '\qc ' . $invoice['Κατηγορία'] . '\cell\qr ' . euro($invoice['Τιμές']['Καθαρή Αξία για ΦΕ'])
			. '\cell\qc ' . percent($invoice['ΦΕ']) . '\cell\qr ' . euro($invoice['Τιμές']['ΦΕ'])
			. '\cell\row' . PHP_EOL
?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx3118
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\cellx5386
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qr ΣΥΝΟΛΟ\cell <?=euro($per_month['Τιμές']['Καθαρή Αξία για ΦΕ'])?>\cell <?=euro($per_month['Τιμές']['ΦΕ'])?>\cell\row
\ql Φόρος λόγω εκπρόθεσμου\cell\cell\cell\row
\qr ΓΕΝΙΚΟ ΣΥΝΟΛΟ\cell\cell\cell\row

\pard\plain\par\qc{\fs24\b\ul ΥΠΕΥΘΥΝΗ ΔΗΛΩΣΗ}\par\par
\ql{\fs20 Βεβαιώνω υπεύθυνα και με επίγνωση των συνεπειών του νόμου την ακρίβεια της παρούσας δήλωσης.}\par\par
\qc {\ul Ο Δηλών}\line\line\line\line <?=rtf($data['Αξκος Γραφείου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Γραφείου']['Βαθμός'])?>\par\par

\trowd\cellx3402\cellx6804\cellx10206\qc\fs20
ΠΑΡΑΛΗΦΘΗΚΕ - ΘΕΩΡΗΘΗΚΕ\cell ΚΑΤΕΒΛΗΘΗΚΑΝ\cell ΒΕΒΑΙΩΘΗΚΑΝ\cell\row
\trowd\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\cellx1701\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx3402
\cellx5103\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx6804
\cellx8505\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206\qr
Εμπρόθεσμα\cell\cell Ευρώ\cell\cell Ευρώ\cell\cell\row
Εκπρόθεσμα\cell\cell Αρ. Διπλοτύπου\cell\cell Αρ. Διπλοτύπου\cell\cell\row
Ημερομηνία\cell\cell Ημερομηνία\cell\cell Ημερομηνία\cell\cell\row
\pard\plain\par
\trowd\cellx3402\cellx6804\cellx10206\qc
Ο Παραλαβών - Θεωρήσας\cell Ο Εκδότης\cell Ο Ενεργήσας τη βεβαίωση\cell\row
\pard\plain

\sect

\sectd\lndscpsxn\pgwsxn16838\pghsxn11906\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qc{\fs24\b\ul ΠΙΝΑΚΑΣ ΕΠΙΧΕΙΡΗΣΕΩΝ}\par\par
{\fs20 Από τις οποίες προμηθεύτηκαν κάθε είδους αγαθά ή υπηρεσίες οι υπόχρεοι παρακράτησης\line φόρου εισοδήματος της περιπτ. στ' της παραγρ. 1 του άρθρου 37α του Ν.Δ. 3323/1955.}\par\par

\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\fs20
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx3175
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx4252
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx7262
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx9468
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx12416
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx13946
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15137
Επωνυμία\cell Α.Φ.Μ.\cell Διεύθυνση\cell Είδος τιμολογίου\cell Τιμολόγιο\cell\ Καθαρή αξία\cell Ποσοστό φόρου\cell Ποσό φόρου\cell\row
<?php
	foreach($per_month['Τιμολόγια'] as $invoice) {
		$contractor = $invoice['Δικαιούχος'];
		echo '\ql ' . rtf($contractor['Επωνυμία']) . '\cell\qc ' . rtf($contractor['ΑΦΜ']) . '\cell\ql '
				. rtf($contractor['Διεύθυνση']) . '\cell\qc ' . $invoice['Κατηγορία'] . '\cell '
				. $invoice['Τιμολόγιο'] . '\cell\qr ' . euro($invoice['Τιμές']['Καθαρή Αξία για ΦΕ'])
				. '\cell\qc ' . percent($invoice['ΦΕ']) . '\cell\qr ' . euro($invoice['Τιμές']['ΦΕ'])
				. '\cell\row' . PHP_EOL;
	}
?>
\trowd\trpaddfl3\trpaddl28\trpaddfr3\trpaddr28\fs20
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx11169
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\cellx12416
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx15137
\qr ΣΥΝΟΛΟ:\cell <?=euro($per_month['Τιμές']['Καθαρή Αξία για ΦΕ'])?>\cell <?=euro($per_month['Τιμές']['ΦΕ'])?>\cell\row
\pard\plain\qr <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', time())?>\par

\pard\plain\fs23\par
\trowd\trkeep\cellx7568\cellx15136\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=rtf($data['Αξκος Γραφείου']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Αξκος Γραφείου']['Βαθμός'])?>\cell\row

\sect

<?php
}

unset($contractor, $date, $invoice, $per_month, $per_months);

rtf_close(__FILE__);