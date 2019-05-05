<?php
require_once('init.php');
require_once('header.php');

foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
	// Κόστος για το οποίο δεν απαιτείται να υπογραφεί σύμβαση
	if ($per_contractor['Τιμές']['Καθαρή Αξία'] <= 2500) continue;
	$invoices = $per_contractor['Τιμολόγια'];
	$contract = get_contract($invoices[0]);
	$contract = $contract['Σύμβαση'];
	foreach(get_invoices_by_category($invoices) as $category => $invoices) {
		// Κείμενο με τα τιμολόγια, της μορφής '1/31-12-19, 9/1-12-19 και 5/31-1-19'
		$invoice_list = get_names($invoices, 'Τιμολόγιο');
		// Η ημερομηνία του νεότερου τιμολογίου από τα επιλεγμένα
		$newer_invoice_date = get_newer_invoice_date($invoices);
		// Τμήματα κειμένου που αλλάζουν μέσα στο πρωτόκολλο σχετικά με το είδος τιμολογίου
		if ($category == 'Προμήθεια Υλικών')
			$a = array(
				'προμηθειών', 'Προμηθειών', 'ΠΡΟΜΗΘΕΙΩΝ', 'τον προμηθευτή', 'στα είδη', 'των ειδών',
				'\tab 3.\tab Τα υπόψη υλικά ανταποκρίνονται πλήρως στους όρους της σύμβασης και παραδόθηκαν στο Διαχειριστή Υλικού.\par',
				'Ο ΠΡΟΜΗΘΕΥΤΗΣ');
		else {
			$c = $invoices[0]['Δικαιούχος']['Τύπος'] == 'PRIVATE_SECTOR'
					? array('τον εργολάβο', 'Ο ΕΡΓΟΛΑΒΟΣ')
					: array('την Υπηρεσία', 'Η ΥΠΗΡΕΣΙΑ');
			$a = array('υπηρεσιών', 'Υπηρεσιών', 'ΥΠΗΡΕΣΙΩΝ', $c[0], 'στις παρεχόμενες υπηρεσίες',
				'των παρεχόμενων υπηρεσιών', null, $c[1]);
		}
		// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
		$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc\b ΠΡΩΤΟΚΟΛΛΟ ΟΡΙΣΤΙΚΗΣ ΠΟΙΟΤΙΚΗΣ ΚΑΙ ΠΟΣΟΤΙΚΗΣ ΠΑΡΑΛΑΒΗΣ <?=$a[2]?>\par\par
\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
\tab 1.\tab Σήμερα την <?=$newer_invoice_date?> συνήλθε η Επιτροπή Παρακολούθησης και Παραλαβής <?=$a[1]?>, που συγκροτήθηκε με την <?=order($data['Δγη Συγκρότησης Επιτροπών'])?> για να προβεί στην παραλαβή των <?=$a[0]?> που παραδόθηκαν από <?=$a[3]?> «<?=$invoices[0]['Δικαιούχος']['Επωνυμία']?>». Παρόντες ήταν οι:\par
\tab\tab α.\tab <?=person_ext($data["Πρόεδρος Παραλαβής $a[1]"], 0)?>, Πρόεδρος της Επιτροπής.\par
\tab\tab β.\tab <?=person_ext($data["Α Μέλος Παραλαβής $a[1]"], 0)?>, και\par
\tab\tab γ.\tab <?=person_ext($data["Β Μέλος Παραλαβής $a[1]"], 0)?>, Μέλη της Επιτροπής.\par
\tab 2.\tab Η Επιτροπή αφού έλαβε υπόψη:\par
\tab\tab α.\tab Τους όρους της <?=$contract?> σύμβασης.\par
\tab\tab β.\tab Τον ποσοτικό και ποιοτικό έλεγχο που διενήργησε <?=$a[4]?> που αναφέρονταν στην παραπάνω σύμβαση.\par
\tab\tab γ.\tab Τ<?=$c?> υπ' αριθμόν <?=$invoice_list?> τιμολόγι<?=$c?>.\par
\qc{\b Β ε β α ι ώ ν ε ι}\par
\qj την παραλαβή <?=$a[5]?>, όπως αναγράφονται στ<?=$c?> παραπάνω τιμολόγι<?=$c?>.\par
<?=$a[6]?>

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\cellx3402\clftsWidth1\cellx6804\clftsWidth1\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>
\line\line\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
- Ο -\line <?=$a[7]?>\line\line\line <?=rtf($invoices[0]['Δικαιούχος']['Επωνυμία'])?>\cell\row

\sect

<? } }

unset($per_contractor, $category, $invoices, $newer_invoice_date, $invoice_list, $contract, $a, $c);

rtf_close(__FILE__); ?>