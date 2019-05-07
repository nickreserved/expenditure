<?
require_once('init.php');
require_once('order.php');
require_once('header.php');

startOrder();
preOrder(
		!$draft || isset($data['Διαβιβαστικό Δαπάνης']) ? $data['Διαβιβαστικό Δαπάνης'] : null,
		array($data['Ελέγχουσα Αρχή']), array($data['Μονάδα']), $draft, '1 Φάκελος',
		'Δαπάνες', array($data['Απόφαση Ανάληψης Υποχρέωσης']));
?>
1.\tab Σας υποβάλλουμε συνημμένα φάκελο γενόμενης δαπάνης που αφορά «<?=rtf($data['Τίτλος'])?>» ύψους <?=euro($data['Τιμές']['Καταλογιστέο'])?>, για τεχνοοικονομικό έλεγχο.\par
2.\tab Παρακαλούμε για τις ενέργειές σας.\par

<? postOrder($draft); ?>

\sect

<?=rtf_close(__FILE__)?>