<?php
require_once('init.php');
require_once('header.php');
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs24\ul\qc ΦΥΛΛΟ\line ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par\par

\pard\plain\tx397\tqr\tx10050
\trowd\fs23\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206

<?
$count = 0;		// Αρίθμηση δικαιολογητικών
$count1 = 0;	// Αρίθμηση υποφακέλων

foreach($data['Φύλλο Καταχώρησης'] as $content_item) {
	if (!$content_item['Καταχώρηση']) continue;
	switch($content_item['Δικαιολογητικό']) {
		case 'Υποφάκελος':
			echo '\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ «' . strtoupper(countGreek(++$count1)) . '»\b0}\cell\row' . PHP_EOL . '\ql ';
			break;
		case 'Τιμολόγια':
			foreach($data['Τιμολόγια'] as $i)
				echo ++$count . '.\tab Τιμολόγιο υπ\' αριθμόν ' . invoice($i['Τιμολόγιο']) . '\cell\row' . PHP_EOL;
			break;
		case 'Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής':
			foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor)
				if ($per_contractor['Τιμές']['Καθαρή Αξία'] > 2500) {
					$c = 0;	// FLAGS: 1: Προμήθεια Υλικών, 2: Παροχή Υπηρεσιών
					foreach($per_contractor['Τιμολόγια'] as $i) {
						$c |= is_supply($i['Κατηγορία']) ? 1 : 2;
						if ($c == 3) break;
					}
					if ($c & 1) echo ++$count . '.\tab Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής Προμηθειών «' . $i['Δικαιούχος']['Επωνυμία'] . '»\cell\row' . PHP_EOL;
					if ($c & 2) echo ++$count . '.\tab Πρωτόκολλο Οριστικής Ποιοτικής και Ποσοτικής Παραλαβής Υπηρεσιών «' . $i['Δικαιούχος']['Επωνυμία'] . '»\cell\row' . PHP_EOL;
				}
			break;
		case 'Βεβαίωση Παραλαβής':
			$c = 0;	// FLAGS: 1: Προμήθεια Υλικών, 2: Παροχή Υπηρεσιών
			foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor)
				if ($per_contractor['Τιμές']['Καθαρή Αξία'] <= 2500)
					foreach($per_contractor['Τιμολόγια'] as $i) {
						$c |= is_supply($i['Κατηγορία']) ? 1 : 2;
						if ($c == 3) break 2;
					}
			if ($c & 1) echo ++$count . '.\tab Βεβαίωση Παραλαβής Προμηθειών\cell\row' . PHP_EOL;
			if ($c & 2) echo ++$count . '.\tab Βεβαίωση Παραλαβής Υπηρεσιών\cell\row' . PHP_EOL;
			break;
		case 'ΑΔΔΥ':
		case 'Βεβαίωση μη Χρέωσης Υλικών':
			foreach($data['Τιμολόγια'] as $i)
				if (is_supply($i['Κατηγορία'])) goto def;
			break;
		case 'Απόσπασμα Ποινικού Μητρώου':
			foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
				$contractor = $per_contractor['Τιμολόγια'][0]['Δικαιούχος'];
				if ($contractor['Τύπος'] == 'PRIVATE_SECTOR')
					if ($per_contractor['Τιμές']['Καθαρή Αξία'] > 2500)
						echo ++$count . '.\tab Απόσπασμα Ποινικού Μητρώου «' . $contractor['Επωνυμία'] . '»\cell\row' . PHP_EOL;
			}
			break;
		case 'Φορολογική και Ασφαλιστική Ενημερότητα':
			foreach($data['Τιμολόγια ανά Δικαιούχο'] as $per_contractor) {
				$contractor = $per_contractor['Τιμολόγια'][0]['Δικαιούχος'];
				if ($contractor['Τύπος'] == 'PRIVATE_SECTOR') {
					$mixed = $per_contractor['Τιμές']['Καταλογιστέο'];
					$name = $contractor['Επωνυμία'];
					if ($mixed > 1500) echo ++$count . '.\tab Φορολογική Ενημερότητα: «' . $name . '»\cell\row' . PHP_EOL;
					if ($mixed > 3000) echo ++$count . '.\tab Ασφαλιστική Ενημερότητα: «' . $name . '»\cell\row' . PHP_EOL;
				}
			}
			break;
		case 'Βεβαίωση Απόδοσης ΦΕ':
			if ($data['Τιμές']['ΦΕ']) goto def;
			break;
		case 'Απόφαση Ανάληψης Υποχρέωσης':
		case 'Δγη Συγκρότησης Επιτροπών':
		case 'Απόφαση Απευθείας Ανάθεσης':
			$content_item['Δγη'] = $data[$content_item['Δικαιολογητικό']];
			goto def;
def:	default:
			$a = isset($content_item['Δγη']) ? '\line\tab (\i ' . order($content_item['Δγη']) . '\i0 )' : '';
			$c = $content_item['Πλήθος'] > 1 ? "\\tab (x{$content_item['Πλήθος']})" : '';
			echo ++$count . '.\tab ' . $content_item['Δικαιολογητικό'] . $a . $c . '\cell\row' . PHP_EOL;
	}
}

/*	/*elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && !count($data['Εργασίες']));
	elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && (!$bills_buy || empty($data['Εργασίες'])));
	elseif (!$bills_buy && (
					$d == 'Πρωτόκολλο Αγοράς και Διάθεσης' ||
					$d == 'Πρωτόκολλο Ποιοτικής και Ποσοτικής Παραλαβής' && $data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή' ||
					$d == 'Βεβαίωση Ανεφοδιαστικού Οργάνου' ||
					$d == 'Βεβαίωση Μη Χρέωσης Υλικών'));
	elseif (!$bills_contract && (
					$d == 'Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας' ||
					$d == 'Βεβαίωση Επισκευαστικού Οργάνου'));
	elseif ($d == 'Σύμβαση' && !isset($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['ΕΑΑΔΗΣΥ']));*/

unset($count, $count1, $content_item, $i, $contractor, $per_contractor, $a, $c, $mixed, $net);
?>

\sect

<?php rtf_close(__FILE__); ?>