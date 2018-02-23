<?
require_once('engine/functions.php');
require_once('header.php');

if (!isset($bills)) $bills = $data['Τιμολόγια'];
if (!isset($bills_info)) $bills_info = calc_bills($bills);
if (!isset($bills_contract)) $bills_contract = getBillsCategory($bills, 'Παροχή υπηρεσιών');
if (!isset($bills_buy)) $bills_buy = getBillsCategory($bills, 'Παροχή υπηρεσιών', false);
?>

{

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\fs28\ul\qc ΦΥΛΛΟ\line ΚΑΤΑΧΩΡΗΣΗΣ ΕΓΓΡΑΦΩΝ\par\par

\pard\plain\tx397\tx9638
\trowd\fs23\trpaddfl3\trpaddl57\trpaddfr3\trpaddr57
\clbrdrt\brdrs\brdrw1\clbrdrl\brdrs\brdrw1\clbrdrb\brdrs\brdrw1\clbrdrr\brdrs\brdrw1\cellx10206

<?
$f = $c = 0;
foreach($data['ΦύλλοΚαταχώρησης'] as $v)
	if (!isset($v['Ακαταχώρητο'])) {
		if ($v['Δικαιολογητικό'] == 'ΥΠΟΦΑΚΕΛΟΣ') {
			?>\qc{\line\b ΥΠΟΦΑΚΕΛΟΣ «<?=strtoupper(countGreek(++$f))?>»\b0}\cell\row\ql<?
		}
		elseif ($v['Δικαιολογητικό'] == 'Τιμολόγια') {
			foreach($bills as $b) {
				echo ' ' . ++$c; ?>.\tab Τιμολόγιο υπ' αριθμόν <?=chk_bill($b['Τιμολόγιο'])?>\cell\row<?
			}
		}
		else {
			if ($v['Δικαιολογητικό'] == 'Βεβαίωση Απόδοσης ΦΕ' && !$bills_info['ΦΕΣεΕυρώ'] ||
			($v['Δικαιολογητικό'] == 'Πρωτόκολλο Αγοράς και Διάθεσης' || $v['Δικαιολογητικό'] == 'Βεβαίωση Ανεφοδιαστικού Οργάνου') && !$bills_buy ||
			($v['Δικαιολογητικό'] == 'Πρωτόκολλο Παραλαβής Γενόμενης Εργασίας' || $v['Δικαιολογητικό'] == 'Βεβαίωση Επισκευαστικού Οργάνου') && !$bills_contract) continue;
			$a = isset($v['Δγη']) ? ' ' . chk($data[$v['Δγη']]) : '';
			$d = $v['Πλήθος'] > 1 ? "\\tab (x{$v['Πλήθος']})" : '';
			echo ' ' . ++$c . '.\tab ' . $v['Δικαιολογητικό'] . $a . $d . '\cell\row';
		}
	}
?>

\sect

}

