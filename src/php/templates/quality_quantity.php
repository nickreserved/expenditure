<?
require_once('engine/functions.php');
require_once('header.php');


//if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή')
	$bills_rr = $data['Τιμολόγια'];
//else {
//	if (!isset($bills_buy)) $bills_buy = getBillsCategory($data['Τιμολόγια'], 'Προμήθεια υλικών');
//	$bills_rr = $bills_buy;
//}
//if ($bills_rr) {
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b\ul ΜΟΝΑΔΑ:\ul0\b0  <?=chk($data['ΣύντμησηΜονάδας'])?>\line
\b\ul ΕΡΓΟ:\ul0\b0  <?=chk($data['Τίτλος'])?>\par\par
\fs28\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΠΟΙΟΤΙΚΗΣ ΚΑΙ ΠΟΣΟΤΙΚΗΣ ΠΑΡΑΛΛΑΒΗΣ\par\par
\pard\plain\tx567\tx1134\qj
\tab Σήμερα την <?=now()?> η υπογεγραμμένη επιτροπή αποτελούμενη από τους:\par
\tab\tab α. <?=inflection(man($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής']), 2)?> ως πρόεδρο\par
\tab\tab β. <?=inflection(man($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ']), 2)?> και\par
\tab\tab γ. <?=inflection(man($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ']), 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑνάθεσης'])?>\ul0  προέβη στην ποιοτική και ποσοτική παραλαβή των παρακάτω υλικών και/ή εργασιών τα οποία βρήκε σε άριστη κατάσταση και τα οποία παρέδωσε στον Αξκο Έργου <?=inflection(man($data['ΑξκοςΈργου']), 2)?>.\par\par


\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΙΔΟΣ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($bills_rr as $v) {
	$items = $v['Είδη'];
	foreach($items as $i) { ?>
\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=chk($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\row
<? } } ?>


\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΠοιοτικήςΠοσοτικήςΠαραλαβής']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠοιοτικήςΠοσοτικήςΠαραλαβήςΒ']['Βαθμός'])?>\cell\row

\sect

}

