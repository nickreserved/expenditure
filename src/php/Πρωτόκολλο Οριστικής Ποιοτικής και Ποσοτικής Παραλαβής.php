<?php
require_once('functions.php');
init(7);

if (isset($data['Συμβάσεις']))
	foreach($data['Συμβάσεις'] as $per_contract) {
		$invoices = $per_contract['Τιμολόγια'];
		foreach(get_invoices_by_category($invoices) as $a => $invoices) {
			$b = $a ? 'Προμηθειών' : 'Υπηρεσιών';
			// Κατάληξη πληθυντικού - ενικού σχετικά με τον αριθμό τιμολογίων
			$c = count($invoices) > 1 ? 'α' : 'ο';
?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\qr <?=rtf($data['Σχηματισμός'])?>\line <?=rtf($data['Μονάδα'])?>\line <?=rtf($data['Έδρα']) . ', ' . strftime('%d %b %y', get_newer_timestamp($invoices))?>\par\par
\fs24\qc\b ΠΡΩΤΟΚΟΛΛΟ ΟΡΙΣΤΙΚΗΣ ΠΟΣΟΤΙΚΗΣ ΚΑΙ ΠΟΙΟΤΙΚΗΣ ΠΑΡΑΛΑΒΗΣ <?=$a[0] ? 'ΠΡΟΜΗΘΕΙΩΝ' : 'ΥΠΗΡΕΣΙΩΝ'?>\par\par
\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
\tab Σήμερα την <?=strftime('%d %b %y', get_newer_timestamp($invoices))?>, η Επιτροπή η οποία αποτελείται από τους:\par
\tab\tab α.\tab <?=personi($data["Πρόεδρος Παραλαβής $b"], 0)?>, Πρόεδρος της Επιτροπής.\par
\tab\tab β.\tab <?=personi($data["Α Μέλος Παραλαβής $b"], 0)?>, και\par
\tab\tab γ.\tab <?=personi($data["Β Μέλος Παραλαβής $b"], 0)?>, Μέλη της Επιτροπής.\par
βάσει της διοικητικής πράξης <?=$data['Δγη Συγκρότησης Επιτροπών']['Ταυτότητα']?>, βεβαιώνει την ποιοτική και ποσοτική παραλαβή<?=$a ? ' και παράδοση στο διαχειριστή υλικού' : ''?>, των ακόλουθων ειδών τα οποία παρείχε ο οικονομικός φορέας <?=rtf(get_contractor_id($per_contract['Δικαιούχος']))?>, σύμφωνα με τους όρους της υπ' αριθμόν <?=$per_contract['Σύμβαση']['Σύμβαση']?> σύμβασης, τις συμφωνηθείσες προδιαγραφές και τ<?=$c?> υπ' αριθμόν <?=get_names_key($invoices, 'Τιμολόγιο');?> τιμολόγι<?=$c?>.\par

<?php report($invoices); ?>

\pard\plain\sb120\sa120\tx567\tx1134\tx1701\qj
Αφού συντάχθηκε το παρόν σε τριπλότυπο, υπογράφεται όπως παρακάτω:

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\cellx3402\clftsWidth1\cellx6804\clftsWidth1\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=rtf($data["Πρόεδρος Παραλαβής $b"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Πρόεδρος Παραλαβής $b"]['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=rtf($data["Α Μέλος Παραλαβής $b"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Α Μέλος Παραλαβής $b"]['Βαθμός'])?>
\line\line\line <?=rtf($data["Β Μέλος Παραλαβής $b"]['Ονοματεπώνυμο'])?>\line <?=rtf($data["Β Μέλος Παραλαβής $b"]['Βαθμός'])?>\cell
- Ο -\line ΠΑΡΑΛΑΒΩΝ <?php
if ($data['Έργο']) echo 'ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line ' . rtf($data['Αξκος Έργου']['Ονοματεπώνυμο']) . '\line' . rtf($data['Αξκος Έργου']['Βαθμός']);
elseif ($a) echo 'ΔΧΣΤΗΣ\line\line\line ' . rtf($data['Δχστης']['Ονοματεπώνυμο']) . '\line' . rtf($data['Δχστης']['Βαθμός']);
elseif (isset($data['Αξκος Έργου'])) echo 'ΤΜΗΜΑΤΑΡΧΗΣ\line\line\line ' . rtf($data['Αξκος Έργου']['Ονοματεπώνυμο']) . '\line' . rtf($data['Αξκος Έργου']['Βαθμός']);
else echo 'ΑΞΚΟΣ 4ου ΓΡΑΦΕΙΟΥ\line\line\line ' . rtf($data['Αξκος Γραφείου']['Ονοματεπώνυμο']) . '\line' . rtf($data['Αξκος Γραφείου']['Βαθμός']);
?>\line\line\line ΘΕΩΡΗΘΗΚΕ\line - Ο -\line ΔΙΟΙΚΗΤΗΣ\line\line\line <?=rtf($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=rtf($data['Δκτης']['Βαθμός'])?>\cell\row

\sect

<?		}
	}

unset($a, $b, $c, $invoices, $per_contract);

rtf_close(__FILE__); ?>