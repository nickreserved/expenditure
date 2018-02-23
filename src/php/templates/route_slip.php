<?
require_once('engine/functions.php');
require_once('header.php');
require_once('engine/order.php');

if (!isset($bills_info)) $bills_info = calc_bills($data['Τιμολόγια']);
if (!isset($data['Ποσό'])) $data['Ποσό'] = $bills_info['Καταλογιστέο'];

if (!isset($draft)) $draft = getEnvironment('draft', 'true');
if (!isset($prereport)) $prereport = false;
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
$attached = $prereport ? 'Ένα (1)' : 'Ένας (1) Φάκελος';
$connect = $prereport ? null : array($data['ΔγηΔιάθεσης']);

echo preOrder(isset($data['Διαβιβαστικό']) ? $data['Διαβιβαστικό'] : null, array($data['ΓραφείοΣχηματισμού']), array(null), $draft, $attached);
echo '\pard\plain\par\par\par';
echo subjectOrder('Δαπάνες', isset($data['ΔγηΔιάθεσης']) ? array($data['ΔγηΔιάθεσης']) : null);
?>
\pard\plain\fs28\tx567\tx1134\tx1701\tx2268\qj
<? if ($prereport) { ?>
\tab\b 1.\b0\tab Υποβάλλεται συννημένα έκθεση απαιτούμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?> <?=euro($data['Ποσό'])?>».\par\par
<? } else { ?>
\tab\b 1.\b0\tab Υποβάλλεται συννημένα φάκελος γενόμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?> <?=euro($data['Ποσό'])?>», για τεχνοοικονομικό έλεγχο.\par\par
<? } ?>
\tab\b 2.\b0\tab Παρακαλούμε για τις ενέργειές σας.\par\par


<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

}

