<?
require_once('engine/init.php');
require_once('header.php');

if ($bills_buy) {
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΠΡΩΤΟΚΟΛΛΟ\line ΑΓΟΡΑΣ ΚΑΙ ΔΙΑΘΕΣΗΣ ΥΛΙΚΩΝ\par\par
\pard\plain\tx567\tx1134\qj
\tab Σήμερα την <?=$data['ΗμερομηνίαΤελευταίουΤιμολογίου']?> η υπογεγραμμένη επιτροπή αποτελούμενη από τους:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΑγοράςΔιάθεσης'], 2)?> ως πρόεδρο\par
\tab\tab β. <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΑ'], 2)?> και\par
\tab\tab γ. <?=man_ext($data['ΜέλοςΑγοράςΔιάθεσηςΒ'], 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑγοράςΔιάθεσης'])?>\ul0  προέβη στην αγορά και διάθεση των παρακάτω υλικών τα οποία και παρέδωσε στον Αξκο Έργου <?=man_ext($data['ΑξκοςΈργου'], 2)?>.\par\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΙΔΟΣ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($bills_buy as $v)
	foreach($v['Είδη'] as $i) {
		?>\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=chk_measure($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\row<?
	} ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . $data['ΗμερομηνίαΤελευταίουΤιμολογίου']?>\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx3402\clftsWidth1\clNoWrap\cellx6804\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell\row

\sect

<? } ?>