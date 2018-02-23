<?
require_once('engine/init.php');
require_once('header.php');
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs28\ul\qc ΦΥΛΛΟ\line ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par\par

\pard\plain\tx397\tqr\tx10050
\trowd\fs23\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206

<?
$count = $count1 = 0;
foreach($data['ΦύλλοΚαταχώρησης'] as $v)
	if (!isset($v['Ακαταχώρητο'])) {
		$d = $v['Δικαιολογητικό'];
		if ($d == 'ΥΠΟΦΑΚΕΛΟΣ') {
			?>\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ «<?=strtoupper(countGreek(++$count1))?>»\b0}\cell\row\ql<?
		}
		elseif ($d == 'Τιμολόγια')
			foreach($bills as $i) {
				echo ' ' . ++$count; ?>.\tab Τιμολόγιο υπ αριθμόν <?=chk_bill($i['Τιμολόγιο'])?>\tab (<?=chk($i['Κατηγορία'])?>)\cell\row<?
			}
		else {
			if (!count($data['Εργασίες']) && $d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' ||
					!$bills_info['ΦΕΣεΕυρώ'] && $d == 'Βεβαίωση Απόδοσης ΦΕ' ||
					!$bills_buy && (
						$d == 'Πρωτόκολλο Αγοράς και Διάθεσης' ||
						$d == 'Πρωτόκολλο Ποιοτικής και Ποσοτικής Παραλαβής' && $data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή' ||
						$d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' ||
						$d == 'Βεβαίωση Ανεφοδιαστικού Οργάνου' ||
						$d == 'Βεβαίωση Μη Χρέωσης Υλικών') ||
					!$bills_contract && (
						$d == 'Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας' ||
						$d == 'Βεβαίωση Επισκευαστικού Οργάνου')) continue;
			$a = isset($v['Δγη']) ? '\line\tab (\i ' . chk_order($data[$v['Δγη']]) . '\i0 )' : '';
			$c = $v['Πλήθος'] > 1 ? "\\tab (x{$v['Πλήθος']})" : '';
			echo ' ' . ++$count . '.\tab ' . $d . $a . $c . '\cell\row';
		}
	}

?>

\sect

