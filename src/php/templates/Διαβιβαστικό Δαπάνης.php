<?
require_once('engine/init.php');
require_once('header.php');

if (!isset($prereport)) $prereport = false;

$attached = $prereport ? 'Δύο (2)' : '1 Φάκελος';
$connect = $prereport ? null : array($data['ΔγηΔιάθεσης']);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134

<?=preOrder(!$draft || isset($data['Διαβιβαστικό']) ? $data['Διαβιβαστικό'] : null, array($data['ΕλέγχουσαΑρχή']), array(null), $draft, $attached)?>
<?=subjectOrder('Δαπάνες', $connect)?>
\pard\plain\fs24\tx567\tx1134\tx1701\tx2268\qj
<? if ($prereport) { ?>
\tab 1.\tab Σας υποβάλλουμε συνημμένα έκθεση απαιτούμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?>» ύψους <?=euro($data['Ποσό'])?>.\par\par
<? } else { ?>
\tab 1.\tab Σας υποβάλλουμε συνημμένα φάκελο γενόμενης δαπάνης που αφορά «<?=chk($data['Τίτλος'])?>» ύψους <?=euro($data['Ποσό'])?>, για τεχνοοικονομικό έλεγχο.\par\par
<? } ?>
\tab 2.\tab Παρακαλούμε για τις ενέργειές σας.\par\par

<? if ($draft) draftOrder(); else bottomOrder(); ?>

\sect

