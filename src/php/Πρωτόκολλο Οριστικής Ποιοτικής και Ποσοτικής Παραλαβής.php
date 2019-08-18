<?php
require_once('functions.php');
init(7);

if (isset($data['Συμβάσεις']))
	foreach($data['Συμβάσεις'] as $per_contract) {
		$invoices = $per_contract['Τιμολόγια'];
		foreach(get_invoices_by_category($invoices) as $a => $invoices) {
			// Τμήματα κειμένου που αλλάζουν μέσα στο πρωτόκολλο σχετικά με το είδος τιμολογίου
			$a = $a ? array('προμηθειών', 'Προμηθειών', 'ΠΡΟΜΗΘΕΙΩΝ', 'στα είδη', 'των ειδών',
						'\tab 3.\tab Τα υπόψη είδη ανταποκρίνονται πλήρως στους όρους της σύμβασης και παραδόθηκαν στο Διαχειριστή Υλικού.\par')
					: array('υπηρεσιών', 'Υπηρεσιών', 'ΥΠΗΡΕΣΙΩΝ', 'στις υπηρεσίες', 'των υπηρεσιών', null);
			// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
			$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Μονάδα'])?>\par\par
\fs24\qc\b ΠΡΩΤΟΚΟΛΛΟ ΟΡΙΣΤΙΚΗΣ ΠΟΙΟΤΙΚΗΣ ΚΑΙ ΠΟΣΟΤΙΚΗΣ ΠΑΡΑΛΑΒΗΣ <?=$a[2]?>\par\par
\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
\tab 1.\tab Σήμερα την <?=strftime('%d %b %y', get_newer_timestamp($invoices))?> συνήλθε η Επιτροπή Παρακολούθησης και Παραλαβής <?=$a[1]?>, που συγκροτήθηκε με την <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?> για να προβεί στην παραλαβή των <?=$a[0]?> που παραδόθηκαν από την επιχείρηση «<?=$per_contract['Δικαιούχος']['Επωνυμία']?>». Στη συνεδρίαση παρόντες ήταν οι:\par
\tab\tab α.\tab <?=personi($data["Πρόεδρος Παραλαβής $a[1]"], 0)?>, Πρόεδρος της Επιτροπής.\par
\tab\tab β.\tab <?=personi($data["Α Μέλος Παραλαβής $a[1]"], 0)?>, και\par
\tab\tab γ.\tab <?=personi($data["Β Μέλος Παραλαβής $a[1]"], 0)?>, Μέλη της Επιτροπής.\par
\tab 2.\tab Η Επιτροπή αφού έλαβε υπόψη:\par
\tab\tab α.\tab Τους όρους της <?=$per_contract['Σύμβαση']['Σύμβαση']?> σύμβασης.\par
\tab\tab β.\tab Τον ποσοτικό και ποιοτικό έλεγχο που διενήργησε <?=$a[3]?> που αναφέρονταν στην παραπάνω σύμβαση.\par
\tab\tab γ.\tab Τ<?=$c?> υπ' αριθμόν <?=get_names_key($invoices, 'Τιμολόγιο');?> τιμολόγι<?=$c?>.\par
\qc{\b Β ε β α ι ώ ν ε ι}\par
\qj την παραλαβή <?=$a[4]?>, όπως αναγράφονται στ<?=$c?> παραπάνω τιμολόγι<?=$c?>.\par
<?=$a[5]?>

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\cellx3402\clftsWidth1\cellx6804\clftsWidth1\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Πρόεδρος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Α Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>
\line\line\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Β Μέλος Παραλαβής $a[1]"]['Βαθμός'])?>\cell
- Η -\line ΕΠΙΧΕΙΡΗΣΗ\line\line\line <?php if (isset($per_contract['Δικαιούχος']['Ονοματεπώνυμο'])) echo rtf($per_contract['Δικαιούχος']['Ονοματεπώνυμο']) . '\line '; ?>Εκπρόσωπος επιχείρησης\cell\row

\sect

<?
		}
	}

unset($a, $c, $invoices, $per_contract);

rtf_close(__FILE__); ?>