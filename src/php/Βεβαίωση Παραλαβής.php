<?php
require_once('init.php');
require_once('header.php');

$invoices = array();
foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	// Κόστος για το οποίο απαιτείται να υπογραφεί σύμβαση
	if ($per_contractor['Τιμές']['Καθαρή Αξία'] > 2500) continue;
	$invoices = array_merge($invoices, $per_contractor['Τιμολόγια']);
}
foreach(get_invoices_by_category($invoices) as $category => $invoices) {
	// Κείμενο με τα τιμολόγια, της μορφής '1/31-12-19, 9/1-12-19 και 5/31-1-19'
	$invoice_list = get_names($invoices, 'Τιμολόγιο');
	// Η ημερομηνία του νεότερου τιμολογίου από τα επιλεγμένα
	$newer_invoice_date = get_newer_invoice_date($invoices);
	// Τμήματα κειμένου που αλλάζουν μέσα στο πρωτόκολλο σχετικά με το είδος τιμολογίου
	$a = $category == 'Προμήθεια Υλικών'
			? array('προμηθειών', 'Προμηθειών', 'ΠΡΟΜΗΘΕΙΩΝ')
			: array('υπηρεσιών', 'Υπηρεσιών', 'ΥΠΗΡΕΣΙΩΝ');
	// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
	$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc{\b ΒΕΒΑΙΩΣΗ ΠΑΡΑΛΑΒΗΣ <?=$a[2]?>}\par\par

\qj Βεβαιώνεται η αγορά, παραλαβή και διάθεση για ικανοποίηση άμεσων αναγκών της Μονάδας, των αναγραφόμενων στ<?=$c?> υπ' αριθμόν <?=$invoice_list?> τιμολόγι<?=$c?> <?=$a[0]?> σύμφωνα με την Φ.092/235631/9 Αυγ 1982 Απόφαση ΥΦΕΘΑ και Φ.600.9/12/601600/Σ.129/13 Ιαν 2005/ΓΕΣ/ΔΟΙ/3γ.\par
\pard\plain\qr\par <?=rtf($data['Έδρα']) . ', ' . $newer_invoice_date?>\par

\pard\plain\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>
\line\line\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>\cell\row

\sect

<? }

unset($per_contractor, $category, $invoices, $newer_invoice_date, $invoice_list, $a, $c);

rtf_close(__FILE__); ?>