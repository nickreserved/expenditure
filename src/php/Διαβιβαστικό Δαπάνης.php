<?php
require_once('functions.php');
require_once('order.php');
init(5);

start_35_20();
order_header(
		ifexist2($output, $data, 'Διαβιβαστικό Δαπάνης'),
		array($data['Ελέγχουσα Αρχή']), array($data['Μονάδα']), $output, '1 Φάκελος',
		'Δαπάνες', array($data['Απόφαση Ανάληψης Υποχρέωσης']['Ταυτότητα']));
?>
1.\tab Σας υποβάλλουμε συνημμένα φάκελο γενόμενης δαπάνης που αφορά «<?=rtf($data['Τίτλος'])?>» ύψους <?=euro($data['Τιμές']['Καταλογιστέο'])?>, για τεχνοοικονομικό έλεγχο.\par
2.\tab Παρακαλούμε για τις ενέργειές σας.\par
3.\tab Χειριστής θέματος: <?=personi($data['Αξκος Γραφείου'], 0)?>, τηλ. <?=rtf($data['Τηλέφωνο'])?>.\par

<?php order_footer($output); ?>

\sect

<?php

rtf_close(__FILE__);