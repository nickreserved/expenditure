<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills_buy)) $bills_buy = getBillsCategory($data['Τιμολόγια'], 'Παροχή υπηρεσιών', false);

if ($bills_buy) {
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b\fs28\ul\qc ΒΕΒΑΙΩΣΗ\line ΑΝΕΦΟΔΙΑΣΤΙΚΟΥ ΟΡΓΑΝΟΥ\par\par
\pard\plain\qj Ο υπογεγραμένος ................................... βεβαιώνει οτι το Ανεφοδιαστικό Όργανο δεν διαθέτει τα παρακάτω υλικά σε απόθεμα:\par

\pard\trowd\trhdr\fs23\trautofit1\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx567
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx7540
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx8787
\clbrdrt\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\clftsWidth1\clNoWrap\cellx10206
\qc\b A/A\cell ΕΙΔΟΣ\cell ΜΟΝΑΔΑ\cell ΠΟΣΟΤΗΤΑ\b0\cell\row
<?
$count = 0;
foreach($bills_buy as $v) {
	$items = $v['Είδη'];
	foreach($items as $i) { ?>
\qr <?=++$count?>\cell\qj <?=chk($i['Είδος'])?>\cell\qc <?=chk($i['ΜονάδαMέτρησης'])?>\cell <?=num($i['Ποσότητα'])?>\cell\row
<? } } ?>

\pard\plain\qr <?=chk($data['Πόλη']) . ', ' . now()?>\par

\pard\plain\fs23\ri5102\qc\b - Ο -\line ΒΕΒΑΙΩΝ\par


\sect

}

<? } ?>