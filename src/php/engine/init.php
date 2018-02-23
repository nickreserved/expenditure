<?
require_once('basic.php');

// συντομεύσεις
$bills = $data['Τιμολόγια'];
$bills_info = calc_bills($bills);
$bills_buy = getBillsCategory($bills, 'Παροχή υπηρεσιών', false);
$bills_contract = getBillsCategory($bills, 'Παροχή υπηρεσιών');
$bills_warrant = getBillsType($bills, '*ΣΠ/ΚΨΜ*Δημόσιο*');
$bills_hold = bills_by_hold($bills);
$bills_hold_all = bills_by_hold($bills, true);
$bills_fe = bills_by_fe($bills);


// συμπληρώνει όσα κελιά αφέθηκαν κενά
if (!isset($data['Ποσό']) && isset($bills_info['Καταλογιστέο'])) $data['Ποσό'] = $bills_info['Καταλογιστέο'];
if (!isset($data['Έργο']) && isset($data['Τίτλος'])) $data['Έργο'] = $data['Τίτλος'];
if (!isset($data['ΠεριοχήΈργου'])) $data['ΠεριοχήΈργου'] = $data['Πόλη'];
if (!isset($data['ΜέλοςΑφανώνΕργασιών']) && isset($data['ΑξκοςΈργου'])) $data['ΜέλοςΑφανώνΕργασιών'] = $data['ΑξκοςΈργου'];

// Λαμβάνει και ελέγχει την ημερομηνία του πιο νέου τιμολογίου
$a = null;
foreach($bills as $v) {
	if (isset($v['Τιμολόγιο']) && preg_match('/(\d{1,2})-(\d{1,2})-(\d{4})/', $v['Τιμολόγιο'], $b)) {
		$b = mktime(0, 0, 0, $b[2], $b[1], $b[3], -1);
		if ($a < $b) $a = $b;
	}
}
if ($a) $a = $data['ΗμερομηνίαΤελευταίουΤιμολογίου'] = strftime('%d %b %Y', $a);

// συμπληρώνει όσες ημερομηνίες αφέθηκαν κενές
$b = array('ΗμερομηνίαΑφανώνΕργασιών', 'ΗμερομηνίαΟριστικήςΕπιμέτρησης', 'ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής', 'ΗμερομηνίαΔιοικητικήςΠαράδοσης');
foreach($b as $v)
	if (isset($data[$v])) $a = chk_date($data[$v]);
	elseif ($a) $data[$v] = $a;

// Συμπληρώνει τις τμηματικές Δγες αν δεν έχουμε Εργολαβία
if (isset($data['ΤύποςΔαπάνης'], $data['ΔγηΑνάθεσης']) && !strpos($data['ΤύποςΔαπάνης'], 'Εργοληπτική Επιχείρηση'))
	$data['ΔγηΠροσωρινήςΟριστικήςΠαραλαβής'] = $data['ΔγηΠοιοτικήςΠοσοτικήςΠαραλαβής'] =
		$data['ΔγηΑγοράςΔιάθεσης'] = $data['ΔγηΑφανώνΕργασιών'] = $data['ΔγηΑξκουΈργου'] = $data['ΔγηΑνάθεσης'];

// Συμπληρώνει τα κενά στο Φύλλο Καταχώρησης
if (isset($data['ΦύλλοΚαταχώρησης'])) {
	foreach($data['ΦύλλοΚαταχώρησης'] as & $v) {
		$a = get_contents($v['Δικαιολογητικό']);
		if ($a) $v = array_merge($v, $a);
	}
	unset($v);
}

?>