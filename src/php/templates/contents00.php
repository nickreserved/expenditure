<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills)) $bills = $data['Τιμολόγια'];
if (!isset($bills_contract)) $bills_contract = getBillsCategory($bills, 'Παροχή υπηρεσιών');
if (!isset($bills_buy)) $bills_buy = getBillsCategory($bills, 'Παροχή υπηρεσιών', false);
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs28\ul\qc ΦΥΛΛΟ\line ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par\par

\pard\plain\tx397\tx9638
\trowd\fs23\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206
\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ "Α"\b0}\cell\row\ql
1.\tab Δγη Διάθεσης <?=chk_order($data['ΔγηΔιάθεσης'])?>\cell\row
2.\tab Δγη Ανάθεσης <?=chk_order($data['ΔγηΑνάθεσης'])?>\cell\row
\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ "Β"\b0}\cell\row\ql
1.\tab Δγη Ανάθεσης <?=chk_order($data['ΔγηΑνάθεσης'])?>\cell\row
<?
$c = 1;
if ($bills_contract) { ?>
<?=++$c?>.\tab Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας\tab (x3)\cell\row
<? }
if ($bills_buy) { ?>
<?=++$c?>.\tab Πρωτόκολλο Αγοράς και Διάθεσης\tab (x3)\cell\row
<? } ?>
<?=++$c?>.\tab Πρωτόκολλο Ποιοτικής και Ποσοτικής Παραλαβής\tab (x3)\cell\row
<?=++$c?>.\tab Έκθεση Γενόμενης Δαπάνης\tab (x3)\cell\row
<?
foreach($bills as $v) {
	echo ++$c; ?>.\tab Τιμολόγιο υπ' αριθμόν <?=$v['Τιμολόγιο']?>\cell\row
<? } ?>
<?=++$c?>.\tab Κρατήσεις Υπέρ Τρίτων\tab (x3)\cell\row
\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ "Γ"\b0}\cell\row\ql
1.\tab Αποδεικτικό Είσπραξης ΦΕ\cell\row
2.\tab Αποδεικτικά Είσπραξης Κρατήσεων\cell\row
3.\tab ΑΔΔΥ\cell\row
<?
$c = 3;
if ($bills_contract) { ?>
<?=++$c?>.\tab Βεβαίωση Επισκευαστικού Οργάνου\cell\row
<? }
if ($bills_buy) { ?>
<?=++$c?>.\tab Βεβαίωση Ανεφοδιαστικού Οργάνου\cell\row
<? } ?>

\sect

}

