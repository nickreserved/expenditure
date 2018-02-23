<?
require_once('engine/init.php');
require_once('header.php');

if (!isset($prereport)) $prereport = false;

if ($prereport) $to = $data['ΓραφείοΣχηματισμού'];
else {
	$to = get_order($data['ΔγηΔιάθεσης']);
	$to = $to['Εκδότης'];
}
$attached = $prereport ? 'Δύο (2)' : 'Ένας (1) Φάκελος';
$connect = $prereport ? null : array($data['ΔγηΔιάθεσης']);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?
echo preOrder(!$draft || isset($data['Διαβιβαστικό']) ? $data['Διαβιβαστικό'] : null, array($to), array(null), $draft, $attached);
echo '\pard\plain\par\par\par';
echo subjectOrder('Δαπάνες', $connect);
?>
\pard\plain\fs24\tx567\tx1134\tx1701\tx2268\qj
<? if ($prereport) { ?>
\tab 1.\tab Υποβάλλεται συνημμένα έκθεση απαιτούμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?> <?=euro($data['Ποσό'])?>».\par\par
<? } else { ?>
\tab 1.\tab Υποβάλλεται συνημμένα φάκελος γενόμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?> <?=euro($data['Ποσό'])?>», για τεχνοοικονομικό έλεγχο.\par\par
<? } ?>
\tab 2.\tab Παρακαλούμε για τις ενέργειές σας.\par\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

