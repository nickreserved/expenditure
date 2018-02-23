<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills_contract)) $bills_contract = getBillsCategory($data['Τιμολόγια'], 'Παροχή υπηρεσιών');

if ($bills_contract) {
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΠΑΡΑΛΑΒΗΣ ΓΕΝΟΜΕΝΗΣ ΕΡΓΑΣΙΑΣ\par\par
\pard\plain\tx567\tx1134\qj
\tab Σήμερα την <?=now()?> η υπογεγραμμένη επιτροπή αποτελούμενη από τους:\par
\tab\tab α. <?=inflection(man($data['ΠρόεδροςΑγοράςΔιάθεσης']), 2)?> ως πρόεδρο\par
\tab\tab β. <?=inflection(man($data['ΜέλοςΑγοράςΔιάθεσηςΑ']), 2)?> και\par
\tab\tab γ. <?=inflection(man($data['ΜέλοςΑγοράςΔιάθεσηςΒ']), 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑνάθεσης'])?>\ul0  προέβη στην παραλαβή των παρακάτω εργασιών οι οποίες έγιναν καλώς και σύμφωνα με τους κανόνες της τέχνης.\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΙΔΟΣ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($bills_contract as $v) {
	$items = $v['Είδη'];
	foreach($items as $i) { ?>
\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=chk($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\row
<? } } ?>

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell\row

\sect

}

<? } ?>