<?php
require_once('basic.php');
require_once('unserialize.php');
require_once('functions.php');

/** Ενσωματώνει στα δεδομένα, λίστα με τα ονόματα όλων των επιμέρους κρατήσεων, όλων των τιμολογίων.
 * Μια λίστα με τα ονόματα όλων των επιμέρους κρατήσεων όλων των τιμολογίων αποθηκεύεται στο πεδίο
 * $data['Κρατήσεις']. */
function init_deduction_names() {
	global $data;
	$a = array();
	foreach($data['Τιμολόγια'] as $invoice) {
		$deduction = $invoice['Κρατήσεις'];
		if ($deduction)
			foreach(array_keys($deduction) as $v)
				if (!in_array($v, $a)) $a[] = $v;
	}
	$data['Κρατήσεις'] = $a;
}

/** Προετοιμάζει τις συμβάσεις.
 * Σε διαγωνισμούς, ο νικητής ορίζεται με index στη λίστα διαγωνιζόμενων. Αυτό απαιτεί τροποποίηση. */
function init_contracts() {
	global $data;
	foreach($data['Συμβάσεις'] as & $contract) {
		// Τα κενά πεδία των συμβάσεων συμπληρώνονται
		if (!isset($contract['Τίτλος'])) $contract['Τίτλος'] = $data['Τίτλος'];
		if ($contract['Τύπος Διαγωνισμού'] != 'Απευθείας Ανάθεση') {
			if ($contract['Τύπος Διαγωνισμού'] != 'Συνοπτικός Διαγωνισμός')
				trigger_error('Υποστηρίζονται μόνο συνοπτικοί διαγωνισμοί (προς το παρόν)', E_USER_ERROR);
			// Οι διαγωνιζόμενοι τουλάχιστον 3
			if (!isset($contract['Διαγωνιζόμενοι']) || count($contract['Διαγωνιζόμενοι']) < 3)
				trigger_error('Οι διαγωνιζόμενοι' . (isset($contract['Σύμβαση']) ? " στη σύμβαση {$contract['Σύμβαση']}" : null) . ' δεν είναι τουλάχιστον 3');
			// To timestamp του διαγωνισμού
			$m = parse_datetime($contract['Χρόνος Διαγωνισμού']);
			$contract['Timestamp Διαγωνισμού'] = mktime($m[1], $m[2], 0, $m[3], $m[0], $m[4]);
		}
		// Σε διαγωνισμό, ο δικαιούχος αποθηκευέται σαν index, αλλά η κατάσταση
		// αυτή μπορεί να συνυπάρχει λανθασμένα και με Απευθείας Ανάθεση
		if (is_int($contract['Ανάδοχος']))
			$contract['Ανάδοχος'] = $contract['Διαγωνιζόμενοι'][$contract['Ανάδοχος']];
	}
}

/** Ενσωματώνει στα τιμολόγια τις αξίες του τιμολογίου.
 * Σε κάθε τιμολόγιο, στο πεδίο 'Τιμές', αποθηκεύεται ένα array με στοιχεία που έχουν κλειδιά
 * 'Καθαρή Αξία', 'ΦΠΑ', 'Καταλογιστέο', 'Κρατήσεις', 'Πληρωτέο', 'Καθαρή Αξία για ΦΕ', 'ΦΕ',
 * 'Υπόλοιπο Πληρωτέο', με την αντίστοιχη τιμή για το καθένα.
 * <p>Σε κάθε είδος τιμολογίου, στο πεδίο 'Συνολική Τιμή', αποθηκεύεται το γινόμενο ποσότητας είδους
 * επί της καθαρής αξίας του ενός.
 * <p>Σε κάθε κράτηση τιμολογίου, στο πεδίο 'Σύνολο', αποθηκεύεται το σύνολο των επιμέρους κρατήσεων
 * της κράτησης.
 * <p>Πρέπει να κληθεί μετά από την init_deduction_names() και την init_contracts(). */
function init_invoices() {
	global $data;
	foreach($data['Τιμολόγια'] as & $invoice) {
		// Αρχικοποιεί σύμβαση και δικαιούχο
		if (isset($invoice['Σύμβαση'])) {
			$invoice['Σύμβαση'] = $data['Συμβάσεις'][$invoice['Σύμβαση']];
			$invoice['Δικαιούχος'] = $invoice['Σύμβαση']['Ανάδοχος'];
		}
		// Ενσωματώνει στις κρατήσεις των τιμολογίων το άθροισμα των επιμέρους κρατήσεών τους.
		$deduction = & $invoice['Κρατήσεις'];
		if (!$deduction) $invoice['Κρατήσεις'] = array('Σύνολο' => 0);
		else {
			$sum = 0;
			foreach($deduction as $v)
				$sum += $v;
			$deduction['Σύνολο'] = round($sum, 5);
		}
		// Ενσωματώνει το πεδίο 'Συνολική Τιμή' σε κάθε είδος τιμολογίου
		// Υπολογίζει καθαρή αξία και ΦΠΑ τιμολογίου καθώς και τυχόν κατηγορίες ΦΠΑ
		$net = $vat = 0;
		$vat_categories = array();
		foreach($invoice['Είδη'] as & $item) {
			$a = round($item['Τιμή Μονάδας'] * $item['Ποσότητα'], 3);
			$item['Συνολική Τιμή'] = $a;
			$net += $a;
			$vat_p = $item['ΦΠΑ'];
			if ($vat_p) {
				$a *= $vat_p / 100;
				$vat += $a;
				if (isset($vat_categories[$vat_p])) $vat_categories[$vat_p] += $a;
				else $vat_categories[$vat_p] = $a;
			}
		}
		// Ενσωματώνει τις αξίες του τιμολογίου σε κάθε τιμολόγιο
		$net = round($net, 2);
		$vat = round($vat, 2);
		$vat_categories = adjust_partials($vat_categories, $vat);
		$mixed = $net + $vat;
		$deductions = round($net * $invoice['Κρατήσεις']['Σύνολο'] / 100, 2);
		$contractor_type = $invoice['Δικαιούχος']['Τύπος'];
		if ($contractor_type != 'Ιδιωτικός Τομέας') $mixed += $deductions;
		$mixed = round($mixed, 2);
		$payable = round($mixed - $deductions, 2);
		$netIncomeTax = $net;
		$incomeTax = $invoice['ΦΕ'];
		if ($incomeTax != 3) $netIncomeTax -= $deductions;
		$netIncomeTax = round($netIncomeTax, 2);
		$incomeTax = round($netIncomeTax * $incomeTax / 100, 2);
		$payableMinusIncomeTax = round($payable - $incomeTax, 2);
		$invoice['Τιμές'] = array(
			'Καθαρή Αξία'        => $net,
			'ΦΠΑ'                => $vat,
			'Καταλογιστέο'       => $mixed,
			'Κρατήσεις'          => $deductions,
			'Πληρωτέο'           => $payable,
			'Καθαρή Αξία για ΦΕ' => $netIncomeTax,
			'ΦΕ'                 => $incomeTax,
			'Υπόλοιπο Πληρωτέο'  => $payableMinusIncomeTax
		);
		$invoice['Κατηγορίες ΦΠΑ'] = $vat_categories;
	}
	// Υπολογισμός καταλογιστέου, κρατήσεων, ΦΕ, κτλ για το σύνολο της δαπάνης
	$data['Τιμές'] = calc_sum_of_invoices_prices($data['Τιμολόγια']);
}

/** Ενσωματώνει στα δεδομένα την πιο πρόσφατη ημερομηνία τιμολογίου.
 * Στο πεδίο $data['Ημερομηνία Τελευταίου Τιμολογίου'], αποθηκεύεται η ημερομηνία του πιο πρόσφατου
 * τιμολογίου στη μορφή '31 Δεκ 2019'. */
function init_newer_invoice_date() {
	global $data;
	$a = get_newer_invoice_timestamp($data['Τιμολόγια']);
	if ($a) {
		$data['Timestamp Τελευταίου Τιμολογίου'] = $a;
		$data['Ημερομηνία Τελευταίου Τιμολογίου'] = strftime('%d %b %y', $a);
	}
}

/** Ενσωματώνει στα δεδομένα ένα array με τις λίστες τιμολογίων του κάθε δικαιούχου.
 * Στο πεδίο $data['Τιμολόγια ανά Δικαιούχο'], αποθηκεύονται στοιχεία για κάθε δικαιούχο. Κάθε
 * στοιχείο δικαιούχου, περιέχει 2 στοιχεία: Με κλειδί 'Τιμολόγια' array με τα τιμολόγια του
 * δικαιούχου. Με κλειδί 'Τιμές' τα αθροίσματα των αντίστοιχων αξιών των τιμολογίων του δικαιούχου.
 * <p>Επίσης ελέγχει αν τα τιμολόγια του ίδιου δικαιούχου ανήκουν στην ίδια σύμβαση. Αν ένας
 * δικαιούχος έχει υπογράψει σύμβαση μαζί μας, όλα τα τιμολόγια που έχει εκδόσει πρέπει να ανήκουν
 * σε αυτή τη σύμβαση. Αν δεν έχει υπογράψει, δεν πρέπει κανένα τιμολόγιο να ανήκει σε αυτή τη σύμβαση.
 * <p>Πρέπει να κληθεί μετά από την init_invoices(). */
function init_invoices_by_contractor() {
	global $data;
	$contracts = array();
	// Τιμολόγια κατά δικαιούχο
	$data['Τιμολόγια ανά Δικαιούχο'] = get_invoices_by_contractor($data['Τιμολόγια']);
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as & $per_contractor) {
		// Υπολογισμός αξιών για όλη την ομάδα τιμολογίων και λοιπές συντομεύσεις
		$invoice = $per_contractor[0];
		$per_contractor = array(
			'Τιμολόγια' => $per_contractor,
			'Τιμές' => calc_sum_of_invoices_prices($per_contractor),
			'Δικαιούχος' => $invoice['Δικαιούχος']);
		// Δημιουργεί συντόμευση σύμβασης, αν υπάρχει και επιπλέον...
		// Τα τιμολόγια του ίδιου δικαιούχου πρέπει να έχουν την ίδια σύμβαση
		if (isset($invoice['Σύμβαση'])) {
			$contract = $invoice['Σύμβαση'];
			$contracts[] = $contract;
			$per_contractor['Σύμβαση'] = $contract;
			foreach($per_contractor['Τιμολόγια'] as $invoice)
				if (!isset($invoice['Σύμβαση']) || $contract !== $invoice['Σύμβαση'])
					trigger_error("Δεν ανήκουν όλα τα τιμολόγια του «{$invoice['Δικαιούχος']['Επωνυμία']}» στην ίδια σύμβαση", E_USER_ERROR);
		} else
			foreach($per_contractor['Τιμολόγια'] as $invoice)
				if (isset($invoice['Σύμβαση']))
					trigger_error("Δεν ανήκουν όλα τα τιμολόγια του «{$invoice['Δικαιούχος']['Επωνυμία']}» στην ίδια σύμβαση", E_USER_ERROR);
	}
	// Πρέπει όλες οι συμβάσεις να χρησιμοποιούνται απο τιμολόγια
	// Λαμβάνουμε τις τιμές προκειμένου τα κλειδιά να ξεκινάνε από 0
	if (isset($data['Συμβάσεις'])) {
		$contracts = array_values(array_udiff($data['Συμβάσεις'], $contracts, 'ss'));
		$a = count($contracts);
		if ($a) {
			$err = get_names_key($contracts, 'Σύμβαση');
			$err = $a == 1 ? "Η σύμβαση $err δεν χρησιμοποιείται" : "Οι συμβάσεις $err δεν χρησιμοποιούνται";
			trigger_error("$err από τιμολόγια");
		}
	}
}

init_deduction_names();
if (isset($data['Συμβάσεις'])) init_contracts();
init_invoices();
init_newer_invoice_date();
init_invoices_by_contractor();

/*var_dump($data);
die;
$bills_buy = getBillsCategory($bills, 'Παροχή υπηρεσιών', false);
$bills_contract = getBillsCategory($bills, 'Παροχή υπηρεσιών');
$bills_warrant = getBillsType($bills, '*Στρατός*Δημόσιο*');
$bills_hold = bills_by_hold($bills);
$bills_hold_all = bills_by_hold($bills, true);
$bills_fe = bills_by_fe($bills);


// συμπληρώνει όσα κελιά αφέθηκαν κενά
//if (!isset($data['Έργο']) && isset($data['Τίτλος'])) $data['Έργο'] = $data['Τίτλος'];
//if (!isset($data['ΠεριοχήΈργου'])) $data['ΠεριοχήΈργου'] = $data['Πόλη'];
//if (!isset($data['ΜέλοςΑφανώνΕργασιών']) && isset($data['ΑξκοςΈργου'])) $data['ΜέλοςΑφανώνΕργασιών'] = $data['ΑξκοςΈργου'];
//if (!isset($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ']) && isset($data['ΑξκοςΈργου'])) $data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ'] = $data['ΑξκοςΈργου'];
//if (!isset($data['ΘέμαΔιαγωνισμού']) && isset($data['Έργο'])) $data['ΘέμαΔιαγωνισμού'] = $data['Έργο'];

// Λαμβάνει και ελέγχει την ημερομηνία του πιο νέου τιμολογίου
$a = null;
foreach($bills as $v)
	if (isset($v['Τιμολόγιο']) && preg_match('/(\d{1,2})-(\d{1,2})-(\d{4})/', $v['Τιμολόγιο'], $b)) {
		$b = mktime(0, 0, 0, $b[2], $b[1], $b[3]);
		if ($a < $b) $a = $b;
	}
if ($a) $a = $data['ΗμερομηνίαΤελευταίουΤιμολογίου'] = strftime('%d %b %Y', $a);

// συμπληρώνει όσες ημερομηνίες αφέθηκαν κενές
$b = array('ΗμερομηνίαΑφανώνΕργασιών', 'ΗμερομηνίαΟριστικήςΕπιμέτρησης', 'ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής', 'ΗμερομηνίαΔιοικητικήςΠαράδοσης');
foreach($b as $v)
	if (isset($data[$v])) $a = chk_date($data[$v]);
	elseif ($a) $data[$v] = $a;

// Πέθανε αμα έχουμε έργο με δημόσιο διαγωνισμό
if (isset($data['ΤύποςΔαπάνης'], $data['ΤύποςΔιαγωνισμού']) && $data['ΤύποςΔαπάνης'] != 'Προμήθεια - Συντήρηση - Επισκευή' && $data['ΤύποςΔιαγωνισμού'] == 'Δημόσιος Διαγωνισμός')
	trigger_error('Τα έργα με Δημόσιο Διαγωνισμό γίνονται με ανάθεση σε εργοληπτικές επιχειρήσεις και επίβλεψη από ΔΣΕ', E_USER_ERROR);

// Συμπληρώνει τα κενά όταν έχουμε Διαγωνισμό
if (isset($data['ΤύποςΔαπάνης'], $data['ΤύποςΔιαγωνισμού']) && $data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή' && $data['ΤύποςΔιαγωνισμού'] != 'Χωρίς Διαγωνισμό') {
	if (isset($data['ΠρόεδροςΔιαγωνισμού'])) $data['ΠρόεδροςΑγοράςΔιάθεσης'] = $data['ΠρόεδροςΔιαγωνισμού'];
	if (isset($data['ΜέλοςΔιαγωνισμούΑ'])) $data['ΜέλοςΑγοράςΔιάθεσηςΑ'] = $data['ΜέλοςΔιαγωνισμούΑ'];
	if (isset($data['ΜέλοςΔιαγωνισμούΒ'])) $data['ΜέλοςΑγοράςΔιάθεσηςΒ'] = $data['ΜέλοςΔιαγωνισμούΒ'];
}*/