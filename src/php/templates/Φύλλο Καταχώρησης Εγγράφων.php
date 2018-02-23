<?
require_once('engine/init.php');
require_once('header.php');
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs24\ul\qc ΦΥΛΛΟ\line ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par\par

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
		elseif ($d == 'Φορολογική και Ασφαλιστική Ενημερότητα') {
			$bills_provider = bills_by_provider(getBillsType($bills, '*Ιδιώτης*'));
			$b = null;
			foreach($bills_provider as $k => $i)
				$b[$k] = calc_bills($i);
			foreach($b as $k => $i) {
				if ($i['ΚαθαρήΑξία'] >= 1500) echo ' ' . ++$count . '.\tab Φορολογική Ενημερότητα: «' . $k . '»\cell\row';
				if ($i['ΚαθαρήΑξία'] >= 3000) echo ' ' . ++$count . '.\tab Ασφαλιστική Ενημερότητα: «' . $k . '»\cell\row';
			}
		}
		elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && !count($data['Εργασίες']));
		elseif ($d == 'Βεβαίωση Απόδοσης ΦΕ' && !$bills_info['ΦΕΣεΕυρώ']);
		elseif ($d == 'Πρωτόκολλο Εκτελεσθέντων Εργασιών' && (!$bills_buy || empty($data['Εργασίες'])));
		elseif (!$bills_buy && (
						$d == 'Πρωτόκολλο Αγοράς και Διάθεσης' ||
						$d == 'Πρωτόκολλο Ποιοτικής και Ποσοτικής Παραλαβής' && $data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή' ||
						$d == 'Βεβαίωση Ανεφοδιαστικού Οργάνου' ||
						$d == 'Βεβαίωση Μη Χρέωσης Υλικών'));
		elseif (!$bills_contract && (
						$d == 'Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας' ||
						$d == 'Βεβαίωση Επισκευαστικού Οργάνου'));
		elseif ($d == 'Σύμβαση' && !isset($bills_info['ΑνάλυσηΚρατήσεωνΣεΕυρώ']['ΕΑΑΔΗΣΥ']));
		else {
			$a = isset($v['Δγη']) ? '\line\tab (\i ' . chk_order($data[$v['Δγη']]) . '\i0 )' : '';
			$c = $v['Πλήθος'] > 1 ? "\\tab (x{$v['Πλήθος']})" : '';
			echo ' ' . ++$count . '.\tab ' . $d . $a . $c . '\cell\row';
		}
	}

?>

\sect

