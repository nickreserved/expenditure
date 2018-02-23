<?
require_once('engine/basic.php');

if (isset($data['Ποσό']) || !count($data['Τιμολόγια'])) $data['Προκαταβολή'] = $data['Ποσό'];
else {
	$a = calc_bills($data['Τιμολόγια']);
	$data['Προκαταβολή'] = $a['Πληρωτέο'];
	foreach($data['ΦύλλοΚαταχώρησης'] as $v)
		if ($v['Δικαιολογητικό'] == 'Βεβαίωση Απόδοσης Κρατήσεων') {
			$data['Προκαταβολή'] = $a['Καταλογιστέο'];
			break;
		}
}

require_once('engine/init.php');
require_once('header.php');
?>


\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs28\b\ul\qc ΑΠΟΔΕΙΞΗ <?=euro($data['Προκαταβολή'])?>\par\par
\pard\plain\qj Ο υπογεγραμένος <?=man_ext($data['ΑξκοςΈργου'], 0)?> έλαβα <?=euro2str($data['Προκαταβολή'])?> (<?=euro($data['Προκαταβολή'])?>), για «<?=chk($data['Τίτλος'])?>» κατόπιν της δγης <?=chk_order($data['ΔγηΔιάθεσης'])?> και <?=chk_order($data['ΔγηΑξκουΈργου'])?> οι οποίες επισυνάπτονται.\par
\qr <?=now()?>\par

\pard\plain\fs23\par
\trowd\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx1587\clftsWidth1\clNoWrap\cellx2381\clftsWidth1\clNoWrap\cellx3515\clftsWidth1\clNoWrap\cellx6860\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΝΤΗΣ\cell\line - Ο -\line ΕΟΥ\cell\line - Ο -\line ΤΑΜΙΑΣ\cell
\line - Ο -\line ΛΑΒΩΝ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell
\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell\row



\pard\plain\par\par\par\par\brdrb\brdrs\par\pard\plain\par\par\par\par\par



\fs28\b\ul\qc ΕΞΟΥΣΙΟΔΟΤΗΣΗ\par\par
\pard\plain\qj Ο υπογεγραμένος <?=man_ext($data['ΑξκοςΈργου'], 0)?>, εξουσιοδοτώ τον . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . να παραλάβει <?=euro2str($data['Προκαταβολή'])?> (<?=euro($data['Προκαταβολή'])?>), για «<?=chk($data['Τίτλος'])?>».\par
\qr <?=now()?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line ΓΙΑ ΤΟ ΓΝΗΣΙΟ ΤΗΣ ΥΠΟΓΡΑΦΗΣ\cell
- Ο -\line ΕΞΟΥΣΙΟΔΟΤΩΝ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell
- Ο -\line ΕΞΟΥΣΙΟΔΟΤΟΥΜΕΝΟΣ\cell\row

\sect

<?
// Για να βγεί ακριβές αντίγραφο των διαταγών και όχι σχέδιο
$draft = false;
if (strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση')) require('order_work_officer.php');
else require('order.php');
?>