<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills)) $bills = $data['Τιμολόγια'];
if (!isset($bills_info)) $bills_info = calc_bills($bills);
if (!isset($bills_warrant)) $bills_warrant = getBillsType($bills, 'ΣΠ/ΚΨΜ');
if (!isset($bills_hold)) $bills_hold = bills_by_hold($bills);
if (!isset($bills_fe)) $bills_fe = bills_by_fe($bills);
if (!isset($bills_buy)) $bills_buy = getBillsCategory($data['Τιμολόγια'], 'Παροχή υπηρεσιών', false);
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b\fs28\ul\qc ΕΚΘΕΣΗ\par

\pard\plain\trowd\fs21\trqr\trautofit1
\clftsWidth1\clNoWrap\cellx2948
\b ΣΧΗΜ. \ul <?=chk(getBrigate($data['ΓραφείοΣχηματισμού']))?>\ul0\line
ΜΟΝΑΔΑ \ul <?=chk($data['ΣύντμησηΜονάδας'])?>\ul0\b0\line
Μήνας \ul <?=strftime('%b')?>\ul0\line
Χρήση \ul <?=strftime('%Y')?>\ul0\line
Φορέας \ul <?=chk($data['ΕΦ'])?>\ul0\line
ΚΑ \ul <? if (isset($data['ΚΑ'])) echo chk($data['ΚΑ']); ?>\ul0\cell\row

\pard\plain\qj\ul <?=$prereport ? 'Απαιτουμένης' : 'Γενόμενης'?> Δαπάνης για «<?=chk($data['Τίτλος'])?>».\par\par

\pard\plain\fs21
\trowd\trhdr<?require('report$1.php');?>
\qc\b A/A\cell ΕΙΔΟΣ\cell ΠΟΣΟΤΗΤΑ\cell<? if (count($bills_info['ΚατηγορίεςΦΠΑ']) > 2) echo ' ΦΠΑ\cell'; ?> ΜΟΝΑΔΑ\cell ΣΥΝΟΛΟ\b0\cell\row

<?
$count = 0;
foreach($bills as $v) {
	if (count($bills) > 1 && !$prereport) {
		require('report$2.php');
		echo '\qc\b Τιμολόγιο: ' . chk_bill($v['Τιμολόγιο']) . '\b0  (Κρατήσεις: ' . percent($v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο']) .
			' - ΦΕ: ' . percent($v['ΠοσοστόΦΕ']) . ')\cell\row\trowd';
		require('report$1.php');
	}
	$items = $v['Είδη'];
	foreach($items as $i) { ?>
\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=num($i['Ποσότητα'])?>\cell\qr <? if (count($bills_info['ΚατηγορίεςΦΠΑ']) > 2) echo percent($i['ΦΠΑ']) . '\cell'; ?> <?=euro($i['ΤιμήΜονάδας'])?>\cell <?=euro($i['ΣυνολικήΤιμή'])?>\cell\row
<? } } ?>

\pard\tx1\tqdec\tx9667<?require('report$2.php');?>
\line\b ΚΑΘΑΡΗ ΑΞΙΑ:\tab <?=euro($bills_info['ΚαθαρήΑξία'])?>\b0\cell\row
<?
$bwh = array();
if ($bills_warrant) $bwh = bills_by_hold($bills_warrant);
foreach($bwh as $k => $v) {
	$a = calc_bills($v); ?>
Προστίθενται Κρατήσεις <?=percent($k)?>:\tab <?=euro($a['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row
<? }
foreach($bills_info['ΚατηγορίεςΦΠΑ'] as $k => $v) {
	if ($k != 'Σύνολο') { ?>
Προστίθεται ΦΠΑ <?=percent($k)?>:\tab <?=euro($v)?>\cell\row
<? } } ?>
\b ΚΑΤΑΛΟΓΙΣΤΕΟ:\tab <?=euro($bills_info['Καταλογιστέο'])?>\b0\cell\row
<?
foreach($bills_hold as $k => $v) {
	$a = calc_bills($v); ?>
Αφαιρούνται Κρατήσεις <?=percent($k)?>:\tab <?=euro($a['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['Σύνολο'])?>\b0\cell\row
<? } ?>
\b ΠΛΗΡΩΤΕΟ:\tab <?=euro($bills_info['Πληρωτέο'])?>\b0\cell\row
<?
if (!$prereport) {
	foreach($bills_fe as $k => $v) {
		$a = calc_bills($v); ?>
Παρακρατήθηκε ΦΕ <?=percent($k)?>:\tab <?=euro($a['ΦΕΣεΕυρώ'])?>\b0\cell\row
<?
	}
	if (count($bills_fe)) { ?>
\b ΥΠΟΛΟΙΠΟ ΠΛΗΡΩΤΕΟ:\tab <?=euro($bills_info['ΥπόλοιποΠληρωτέο'])?>\b0\cell\row
<? } } ?>
\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . now()?>\par


\pard\plain\fs23
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx<? if ($prereport) echo 5103; else echo '3402\clftsWidth1\clNoWrap\cellx6804'; ?>\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
<? if ($prereport) { ?>
\line - Ο -\line <?=chk(toUppercase($data['ΙδιότηταΑξκου']))?>\line\line\line <?=chk($data['ΑξκοςΓραφείου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΓραφείου']['Βαθμός'])?>\cell
<? } else { ?>
\line - Ο -\line ΕΟΥ\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell
<? } ?>
\row


<? if (isset($bills_buy) && !$prereport) { ?>

\pard\page\plain <?=chk(getBrigate($data['ΓραφείοΣχηματισμού']))?>\par <?=chk($data['ΣύντμησηΜονάδας'])?>\par
\qc{\fs28\b\ul ΒΕΒΑΙΩΣΗ}\par\par
\qj Βεβαιώνεται η αγορά, παραλαβή, χρέωση ή μη χρέωση και διάθεση των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης σύμφωνα με την Φ.092.5/49/307251/02 Οκτ 85 Απόφ. ΥΕΘΑ (ΣΔ - ΝΔΑ 35/86) ΓΕΕΘΑ και την Φ.801/27/4144342414/24 Ιαν 83/ΓΕΣ/ΔΟΙ/1β Γενική Δγη.\par\par

\pard\plain\fs23\par
\trowd\cellx5103\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΕΟΥ\line\line\line <?=chk($data['ΕΟΥ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΕΟΥ']['Βαθμός'])?>\cell\row

\pard\plain\qj\par\par Βεβαιώνουμε ότι αφού επισκεφθήκαμε τα διάφορα καταστήματα για να βρούμε τα καλύτερα ποιοτικά και συμφέροντα κατά τιμή είδη ενεργήσαμε την προμήθεια των υλικών που αναγράφονται στην έκθεση γενομένης δαπάνης.\par\par

\pard\plain\fs23\par
\trowd\cellx5103\cellx10206\qc
- Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΑγοράςΔιάθεσης']['Βαθμός'])?>\cell
- ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΑγοράςΔιάθεσηςΒ']['Βαθμός'])?>\cell\row

<? } ?>

\sect

}

