<?
require_once('engine/init.php');
require_once('header.php');

if (count($data['Εργασίες']) && $bills_buy) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs28\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΕΚΤΕΛΕΣΘΕΝΤΩΝ ΕΡΓΑΣΙΩΝ\par\par
\pard\plain\tx567\tx1134\tx1701\qj
\tab Σήμερα την <?=$data['ΗμερομηνίαΤελευταίουΤιμολογίου']?> η υπογεγραμμένη επιτροπή αποτελούμενη από τους:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΑγοράςΔιάθεσης'], 2)?> ως πρόεδρο\par
\tab\tab β. <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΑ'], 2)?> και\par
\tab\tab γ. <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΒ'], 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑγοράςΔιάθεσης'])?>\ul0 , βεβαιώνει οτι για την πραγματοποίηση των εργασιών, αναλώθηκαν υλικά όπως παρακάτω:\par
<?
$count = 0;
foreach($data['Εργασίες'] as $v) {
	echo '\par\tab\b ' . ++$count . '.\tab\ul ' . chk($v['Εργασία']) . '\ul0\b0  (' . num($v['Ποσότητα']) . ' ' . chk_measure($v['ΜονάδαMέτρησης']) . ')\par';
	$count1 = 0;
	foreach($v['Υλικά'] as $i)
		echo '\tab\tab ' . countGreek(++$count1) . '.\tab ' . chk($i['Υλικό']) . ' (' . num($i['Ποσότητα']) . ' ' . chk_measure($i['ΜονάδαMέτρησης']) . ')\par';
} ?>

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2552\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx7655\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

<? } ?>