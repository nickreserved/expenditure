<?php

/** Κλείνει το RTF αρχείο, αν απαιτείται, με εξαγωγή του κατάλληλου κειμένου.
 * Το RTF αρχείο κλείνει με το χαρακτήρα '}', αλλά ένα script μπορεί να ενσωματώνει μεγάλο αριθμό
 * δικαιολογητικών τα οποία δεν πρέπει να κλείσουν το RTF γιατί δεν γνωρίζουν ότι ακολουθούν και άλλα
 * δικαιολογητικά.
 * <p>Π.χ. η δαπάνη εξάγει και δικαιολογητικό σύμβασης, αλλά η σύμβαση εξάγεται και μόνη της. Οπότε
 * μόνο αν βρισκόμαστε στο PHP script που κλήθηκε (και όχι σε κάποιο που εισήχθηκε με require) κλείνει
 * το RTF αρχείο.
 * @param string $file Το όνομα αρχείου του script που αιτείται κλείσιμο του RTF αρχείου */
function rtf_close($file) {
	$a = get_required_files();
	if ($file == $a[0]) echo "\n\n\n}";
}

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

/** Ενσωματώνει στα τιμολόγια τις αξίες του τιμολογίου.
 * Σε κάθε τιμολόγιο, στο πεδίο 'Τιμές', αποθηκεύεται ένα array με στοιχεία που έχουν κλειδιά
 * 'Καθαρή Αξία', 'ΦΠΑ', 'Καταλογιστέο', 'Κρατήσεις', 'Πληρωτέο', 'Καθαρή Αξία για ΦΕ', 'ΦΕ',
 * 'Υπόλοιπο Πληρωτέο', με την αντίστοιχη τιμή για το καθένα.
 * <p>Σε κάθε είδος τιμολογίου, στο πεδίο 'Συνολική Τιμή', αποθηκεύεται το γινόμενο ποσότητας είδους
 * επί της καθαρής αξίας του ενός.
 * <p>Σε κάθε κράτηση τιμολογίου, στο πεδίο 'Σύνολο', αποθηκεύεται το σύνολο των επιμέρους κρατήσεων
 * της κράτησης.
 * <p>Πρέπει να κληθεί μετά από την init_deduction_names(). */
function init_invoices() {
	global $data;
	foreach($data['Τιμολόγια'] as & $invoice) {
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
		$wePayDeductions = $contractor_type == 'PUBLIC_SERVICES' || $contractor_type == 'ARMY';
		if ($wePayDeductions) $mixed += $deductions;
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
	$a = get_newer_invoice_date($data['Τιμολόγια']);
	if ($a) $data['Ημερομηνία Τελευταίου Τιμολογίου'] = $a;
}

/** Ενσωματώνει στα δεδομένα ένα array με τις λίστες τιμολογίων του κάθε δικαιούχου.
 * Στο πεδίο $data['Τιμολόγια ανά Δικαιούχο'], αποθηκεύονται στοιχεία για κάθε δικαιούχο. Κάθε
 * στοιχείο δικαιούχου, περιέχει 2 στοιχεία: Με κλειδί 'Τιμολόγια' array με τα τιμολόγια του
 * δικαιούχου. Με κλειδί 'Τιμές' τα αθροίσματα των αντίστοιχων αξιών των τιμολογίων του δικαιούχου.
 * <p>Πρέπει να κληθεί μετά από την init_invoices(). */
function init_invoices_by_contractor() {
	global $data;
	$data['Τιμολόγια ανά Δικαιούχο'] = get_invoices_by_contractor($data['Τιμολόγια']);
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as & $v)
		$v = array('Τιμολόγια' => $v, 'Τιμές' => calc_sum_of_invoices_prices($v));
}

/** Συμπληρώνει όσα πεδία της δαπάνης αφέθηκαν κενά και μπορούν να λάβουν τιμή από τα συμφραζόμενα.
 * Πρέπει να κληθεί μετά την init_invoices() και init_newer_invoice_date(). */
function init_empty_fields() {}//TODO: implement or erase

/** Ελέγχει αν τα τιμολόγια του ίδιου δικαιούχου ανήκουν στην ίδια σύμβαση.
 * Αν ένας δικαιούχος έχει υπογράψει σύμβαση μαζί μας, όλα τα τιμολόγια που έχει εκδόσει πρέπει να
 * ανήκουν σε αυτή τη σύμβαση. Αν δεν έχει υπογράψει, δεν πρέπει κανένα τιμολόγιο να ανήκει σε αυτή
 * τη σύμβαση.
 * <p>Πρέπει να κληθεί μετά από την init_invoices_by_contractor(). */
function check_invoices_contracts_contractors() {
	global $data;
	foreach($data['Τιμολόγια ανά Δικαιούχο'] as $v) {
		$invoices = $v['Τιμολόγια'];
		$index = -1;
		foreach($invoices as $invoice) {
			$a = get_contract($invoice);
			if ($index == -1) $index = $a;
			else if ($a != $index)
				trigger_error("Δεν ανήκουν όλα τα τιμολόγια του «{$invoice['Δικαιούχος']['Επωνυμία']}» στην ίδια σύμβαση", E_USER_ERROR);
		}
	}
}


/** Υπολογίζει τα αθροίσματα των αξιών λίστας τιμολογίων.
 * @param array $invoices Η λίστα τιμολογίων. Πρέπει να περιέχει τουλάχιστον ένα τιμολόγιο.
 * @param array $keys Τα κλειδιά των αξιών για τις οποίες θα υπολογιστούν τα αθροίσματά τους, π.χ.
 * 'Καθαρή Αξία'. Αν είναι null, υπολογίζονται τα αθροίσματα όλων των αξιών.
 * @return array Τα αθροίσματα των τιμών, όλων των τιμολογίων της δοσμένης λίστας */
function calc_sum_of_invoices_prices($invoices, $keys = null) {
	if (!$keys) $keys = array_keys($invoices[0]['Τιμές']);	// Λήψη όλων των κλειδιών των αξιών από το πρώτο τιμολόγιο
	$b = array_fill_keys($keys, 0);		// Αρχικοποίηση των αθροισμάτων με τιμή 0
	foreach($invoices as $invoice) {
		$price = $invoice['Τιμές'];
		foreach($keys as $key)
			$b[$key] += $price[$key];
	}
	return $b;
}

/** Υπολογίζει αθροιστικά, τις επιμέρους κρατήσεις, μιας ομάδας τιμολογίων.
 * @param array $invoices Μια λίστα τιμολογίων
 * @return array Έχει κλειδιά τα ονόματα των επιμέρους κρατήσεων και το 'Σύνολο' και τιμές τις
 * αντίστοιχες τιμές σε ευρώ, κάνοντας τη γνωστή αναγωγή για να μην υπάρχουν διαφορές δεκαδικών στο
 * άθροισμα */
function calc_partial_deductions($invoices) {
	$a = array();
	$priceSum = 0;
	// Αθροίσματα επιμέρους κρατήσεων
	foreach ($invoices as $invoice) {
		$deduction = $invoice['Κρατήσεις'];
		$sum = $deduction['Σύνολο'];
		$price = $invoice['Τιμές']['Κρατήσεις'];	// Όχι επί της καθαρής αξίας γιατί οι κρατήσεις έχουν στρογγυλοποιηθεί
		$priceSum += $price;
		foreach($deduction as $name => $term) {
			if ($name != 'Σύνολο') {
				$val = $price * $term / $sum;
				if (!isset($a[$name])) $a[$name] = $val; else $a[$name] += $val;
			}
		}
	}
	// Σφάλματα στρογγυλοποίησης
	$a = adjust_partials($a, $priceSum);
	$a['Σύνολο'] = $priceSum;
	return $a;
}

/** Ρυθμίζει μια ομάδα τιμών ώστε να έχουν ένα συγκεκριμένο άθροισμα.
 * Αν ένα ποσοστό μεταφράζεται σε μία αξία που στρογγυλοποιείται σε 2 δεκαδικά ψηφία, και ταυτόχρονα
 * αυτό το ποσοστό αναλύεται σε επιμέρους ποσοστά, τότε αν οι αντίστοιχες αξίες των επιμέρους ποσοστών
 * στρογγυλοποιηθούν στα 2 δεκαδικά ψηφία και προστεθούν, η συνολική τους αξία, ενδέχεται λόγω
 * σφαλμάτων στρογγυλοποίησης να μην είναι ίδια με την αξία που αντιστοιχούσε στο αρχικό ποσοστό.
 * Για το λόγο αυτό, οι επιμέρους αξίες τροποποιούνται ελαφρώς κατά 0.01 προκειμένου το άθροισμά τους
 * να είναι ίδιο με την αξία του αρχικού ποσοστού.
 * @param array $in Οι αξίες των επιμέρους ποσοστών χωρίς στρογγυλοποίηση
 * @param float $desirableSum Το επιθυμητό άθροισμα των παραπάνω αξιών, μετά τη στρογγυλοποίηση
 * @return array Οι αξίες των επιμέρους ποσοστών μετά τη στρογγυλοποίηση */
function adjust_partials($in, $desirableSum) {
	$sum = 0;
	$remainders = array(); $out = array();
	foreach($in as $key => $term) {
		$term_new = round($term, 2);
		$remainders[] = array($key, $term_new - $term);
		$out[$key] = $term_new;
		$sum += $term;
	}
	$remainder = round(($sum - $desirableSum) * 100);
	if ($remainder) {
		$comparator = function($v1, $v2) {
			if ($v1[1] < $v2[1]) return -1;
			if ($v1[1] > $v2[1]) return 1;
			return 0;
		};
		usort($remainders, $comparator);
		if ($remainder > 0)
			for ($z = 0; $z < $remainder; ++$z)
				$out[$remainders[count($remainders) - 1 - $z][0]] -= 0.01;
		else
			for ($z = 0; $z < -$remainder; ++$z)
				$out[$remainders[$z][0]] += 0.01;
	}
	return $out;
}

function calc_per_deduction_incometax_vat($invoices) {
	$deductions = array(); $vat = array(); $incometax = array();
	foreach($invoices as $invoice) {
		if ($invoice['ΦΕ']) {			// Αθροιση του ΦΕ για κάθε τιμολόγιο
			$key = $invoice['ΦΕ']; $value = $invoice['Τιμές']['ΦΕ'];
			if (isset($incometax[$key])) $incometax[$key] += $value; else $incometax[$key] = $value;
		}
		if ($invoice['Τιμές']['Κρατήσεις']) {	// Αθροιση των κρατήσεων για κάθε τιμολόγιο
			$key = (string) $invoice['Κρατήσεις']['Σύνολο']; $value = $invoice['Τιμές']['Κρατήσεις'];
			if (isset($deductions[$key])) $deductions[$key] += $value; else $deductions[$key] = $value;
		}
		foreach($invoice['Κατηγορίες ΦΠΑ'] as $key => $value)	// Αθροιση των αξιών ΦΠΑ για κάθε τιμολόγιο
			if (isset($vat[$key])) $vat[$key] += $value; else $vat[$key] = $value;
	}
	return array('Κρατήσεις' => $deductions, 'ΦΕ' => $incometax, 'ΦΠΑ' => $vat);
}

/** Παίρνει μια ημερομηνία σε κείμενο και επιστρέφει array με την ημερομηνία.
 * @param string $a Ημερομηνία της μορφής '31 Δεκ 19'
 * @return array Ημερομηνία της μορφής (31, 'Δεκ', 19) */
function get_date_components($a) {
	$m = null; // Να μη διαμαρτύρεται το netbeans
	if (!preg_match('/(\d{1,2}) (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2})/', $a, $m))
		trigger_error(($a ? "Το '<b>$a</b>' δεν είναι σωστή ημερομηνία" : 'Οι ημερομηνίες πρέπει να δίνονται') . " στη μορφή π.χ. '20 Νοε 19'");
	else {
		array_shift($m);
		return $m;
	}
}

/** Παίρνει μια ημερομηνία σε array και επιστρέφει την ημερομηνία σε array αριθμών.
 * @param array $a Ημερομηνία της μορφής (31, 'Δεκ', 19)
 * @return array Ημερομηνία της μορφής (31, 12, 2019) */
function get_date_components_int($a) {
	$a[1] = get_month($a[1]);
	$a[2] += 2000;
	return $a;
}

/** Παίρνει μια ημερομηνία σε κείμενο και επιστρέφει μήνα και έτος.
 * @param string $a Ημερομηνία της μορφής '26 Νοε 19'
 * @return string Ημερομηνία της μορφής 'Νοέμβριος 2019' */
function get_full_month_year($a) {
	$a = get_date_components_int(get_date_components($a));
	return strftime('%B %Y', mktime(0, 0, 0, $a[1], $a[0], $a[2]));
}

/** Παίρνει μια ημερομηνία σε κείμενο και επιστρέφει έτος.
 * @param string $a Ημερομηνία της μορφής '26 Νοε 19'
 * @return int Το έτος στη μορφή 2019 */
function get_year($a) {
	$a = get_date_components_int(get_date_components($a));
	return $a[2];
}

/** Επιστρέφει τον αριθμό του μήνα από το σύντομο όνομα του μήνα.
 * @param string $month Σύντμηση του μήνα π.χ. 'Νοε'
 * @return int Ο αριθμός του μήνα, π.χ. 11 */
function get_month($month) {
	$months = array(
		'Ιαν' => 1, 'Φεβ' => 2, 'Μαρ' => 3, 'Απρ' => 4, 'Μαι' => 5, 'Ιουν' => 6,
		'Ιουλ' => 7, 'Αυγ' => 8, 'Σεπ' => 9, 'Οκτ' => 10, 'Νοε' => 11, 'Δεκ' => 12);
	return $months[$month];
}

/** Επιστρέφει ένα array με τις λίστες τιμολογίων του κάθε δικαιούχου.
 * @param array $invoices Λίστα με τιμολόγια
 * @return array Ένα στοιχείο για κάθε δικαιούχο, που περιέχει ένα array με όλα του τα τιμολόγια */
function get_invoices_by_contractor($invoices) {
	$b = array();
	foreach($invoices as $invoice)
		$b[$invoice['Δικαιούχος']['Επωνυμία']][] = $invoice;
	return array_values($b);
}

/** Επιστρέφει ένα array με λίστες τιμολογίων χωριστά για προμήθειες και για υπηρεσίες.
 * @param array $invoices Λίστα με τιμολόγια
 * @return array Το στοιχείο με κλειδί 'Προμήθειες' είναι array με όλα τα τιμολόγια που αφορούν
 *  προμήθειες. Το στοιχείο με κλειδί 'Υπηρεσίες' είναι array με όλα τα τιμολόγια που αφορούν
 *  υπηρεσίες. Αν δεν υπάρχουν αντίστοιχα τιμολόγια, δεν υπάρχει και το αντίστοιχο στοιχείο. */
function get_invoices_by_category($invoices) {
	$b = array();
	foreach($invoices as $invoice)
		$b[get_invoice_category($invoice['Κατηγορία'])][] = $invoice;
	return $b;
}

/** Επιστρέφει το γενικό όνομα μιας κατηγορίας τιμολογίου.
 * @param string $category Η κατηγορία του τιμολογίου σε μη φιλική μορφή
 * @return string Η κατηγορία του τιμολογίου σε γενική και φιλική μορφή */
function get_invoice_category($category) {
	return is_supply($category) ? 'Προμήθεια Υλικών' : 'Παροχή Υπηρεσιών';
}

/** Επιστρέφει true αν η κατηγορία του τιμολογίου είναι προμήθειας.
 * @param string $category Η κατηγορία του τιμολογίου
 * @return boolean Η κατηγορία του τιμολογίου είναι προμήθεια */
function is_supply($category) { return $category == 'SUPPLIES' || $category == 'LIQUID_FUEL'; }

/** Επιστρέφει φιλτραρισμένα τα τιμολόγια με συγκεκριμένο τύπο.
 * @param array $invoices Λίστα τιμολογίων
 * @param string $type Ο τύπος του τιμολογίου (π.χ. 'Προμήθεια Υλικών')
 * @param bool $invert Επιστρέφει τα τιμολόγια που δεν έχουν το συγκεκριμένο τύπο (αντιστροφή)
 * @return array Λίστα τιμολογίων με το δοσμένο τύπο */
function get_invoices_with_category($invoices, $type, $invert = false) {
	$b = array();
	foreach($invoices as $invoice)
		if (($invoice['Κατηγορία'] == $type) != $invert) $b[] = $invoice;
	return $b;
}

/** Επιστρέφει φιλτραρισμένα τα τιμολόγια με συγκεκριμένο τύπο δικαιούχου.
 * @param array $invoices Λίστα τιμολογίων
 * @param string $type Ο τύπος του δικαιούχου, όπως δίνεται στο πρόγραμμα (π.χ. 'PRIVATE_SECTOR')
 * @return array Λίστα τιμολογίων με το δοσμένο τύπο δικαιούχου */
function get_invoices_with_contractor_type($invoices, $type) {
	$b = array();
	foreach($invoices as $invoice)
		if ($invoice['Προμηθευτής']['Τύπος'] == $type) $b[] = $invoice;
	return $b;
}

/** Επιστρέφει την πιο πρόσφατη ημερομηνία τιμολογίου.
 * @param array $invoices Λίστα τιμολογίων
 * @return string|null Η ημερομηνία του πιο πρόσφατου τιμολογίου στη μορφή '31 Δεκ 2019' ή null αν
 * δεν υπάρχουν τιμολόγια */
function get_newer_invoice_date($invoices) {
	$a = null;
	foreach($invoices as $invoice)
		if (isset($invoice['Τιμολόγιο'])) {
			$b = null;
			invoice($invoice['Τιμολόγιο'], $b);
			$b = mktime(0, 0, 0, $b[1], $b[0], $b[2]);
			if ($a < $b) $a = $b;
		}
	if ($a) return strftime('%d %b %y', $a);
}

/** Επιστρέφει σε κείμενο, όλα τα ονόματα ενός array με στοιχεία.
 * @param array $a Η λίστα με τα στοιχεία. Δεν μπορεί να είναι άδεια.
 * @param string $key Το κλειδί, για κάθε στοιχείο, με το όνομα του στοιχείου
 * @return string Τα ονόματα των στοιχείων του array χωρισμένα με ',' εκτός από τα τελευταία 2 που
 * είναι χωρισμένα με 'και' */
function get_names($a, $key) {
	$n = count($a);
	$r = '';
	for ($z = 0; $z < $n - 2; ++$z)
		$r .= $a[$z][$key] . ', ';
	if ($n > 1) $r .= $a[$n - 2][$key] . ' και ';
	$r .= $a[$n - 1][$key];
	return $r;
}

/** Επιστρέφει τις συμβάσεις των τιμολογίων.
 * @param array $invoices Λίστα τιμολογίων
 * @return array Λίστα συμβάσεων των δοσμένων τιμολογίων */
function get_contracts($invoices) {
	global $data;
	$b = array();
	foreach($invoices as $invoice)
		if (isset($invoice['Σύμβαση'])) $b[] = $data['Συμβάσεις'][$invoice['Σύμβαση']];
	return array_unique($b);
}

/** Επιστρέφει τις συμβάσεις των τιμολογίων.
 * @param array $invoice Τιμολόγιο
 * @return array Σύμβαση του δοσμένου τιμολογίου ή κενό array */
function get_contract($invoice) {
	global $data;
	return isset($invoice['Σύμβαση']) ? $data['Συμβάσεις'][$invoice['Σύμβαση']] : array();
}

// Επιστρέφει ένα array με τιμολόγια που έχουν ΦΕ.
function bills_with_fe($a) {
	$b = array();
	foreach($a as $v)
		if ($v['ΠοσοστόΦΕ']) $b[] = $v;
	return $b;
}

// Επιστρέφει ένα array με keys τις κρατήσεις σε ποσοστό και
// values array με όλα τα τιμολόγια που έχουν τέτοια κράτηση.
// Αν $zero == false δεν επιστρέφει τα τιμολόγια που έχουν
// κρατήσεις 0 (σπάνιο να έχουν 0 αλλά όχι απίθανο)
function bills_by_hold($a, $zero = false) {
	$b = array();
	foreach($a as $v) {
		$n = $v['ΑνάλυσηΚρατήσεωνΣεΠοσοστά']['Σύνολο'];
		if ($n || $zero) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// Επιστρέφει ενα array με keys το ΦΕ σε ποσοστό και
// values array με όλα τα τιμολόγια που έχουν τέτοιο ΦΕ.
// Δεν επιστρέφει τιμολόγια δίχως ΦΕ.
function bills_by_fe($a) {
	$b = array();
	foreach($a as $v) {
		$n = $v['ΠοσοστόΦΕ'];
		if ($n) $b[str_replace(',', '.', $n)][] = $v;
	}
	return $b;
}

// Επιστρέφει ενα array με keys τον "μήνα έτος" π.χ. "Νοε 2005" και
// values array με όλα τα τιμολόγια του μηνός.
function bills_by_month($a) {
	$b = array();
	foreach($a as $v)
		if (preg_match('/\d{1,2}-(\d{1,2})-(\d{4})/', $v['Τιμολόγιο'], $m)) {
			$b[mktime(0, 0, 0, $m[1], 1, $m[2])][] = $v;
		} else chk_bill($v['Τιμολόγιο']);
	return $b;
}

/** Ελέγχει την ορθότητα της ταυτότητας ενός στρατιωτικού εγγράφου.
 * Αν η ταυτότητα του εγγράφου δεν είναι ορθή, το πρόγραμμα τερματίζει με ένα μήνυμα σφάλματος.
 * @param string $order Η ταυτότητα του εγγράφου
 * @param array $parts Επιστρέφει 6 string στοιχεία τα οποία είναι ο αριθμός φακέλου, ο αριθμός
 * υποφακέλου, ο αριθμός πρωτοκόλλου, ο αριθμός σχεδίου, η ημερομηνία του εγγράφου και ο εκδότης
 * @return string Η ταυτότητα του εγγράφου αν είναι ορθή */
function order($order, & $parts = null) {
	if (preg_match('/(Φ\.?\d{3}(\.\d+)?)\/(\d+)\/(\d+)\/(Σ\.?\d+)\/(\d{1,2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2}))\/(.+)/', $order, $parts)) {
		$parts = array($parts[1], $parts[3], $parts[4], $parts[5], $parts[6], $parts[9]);
		return $order;
	}
	trigger_error(($order ? "Το '<b>$order</b>' δεν είναι ταυτότητα στρατιωτικού εγγράφου"
		: 'Η ταυτότητα στρατιωτικού εγγράφου πρέπει να δίνεται')
		. ' στη μορφή Φ.800.12/23/1234/Σ.123/31 Μαρ 19/3 ΛΜΧ/4ο Γρ.', E_USER_ERROR);
}

/** Ελέγχει την ταυτότητα ενός τιμολογίου
 * @param string $invoice_name Η ταυτότητα του τιμολογίου που πρέπει να έχει τη μορφή 1234/31-12-2019
 * @param array $date Αν η ταυτότητα του τιμολογίου είναι έγκυρη, επιστρέφει ένα array με 3 στοιχεία:
 * την ημέρα, το μήνα και το έτος έκδοσης του τιμολογίου, όλα αριθμητικά. Π.χ. 31, 12, 2019.
 * @return string Η ταυτότητα του τιμολογίου, όπως δόθηκε */
function invoice($invoice_name, & $date = null) {
	if (preg_match('/\d+\/(\d{1,2})-(\d{1,2})-(\d{4})/', $invoice_name, $date)) {
		array_shift($date);
		return $invoice_name;
	}
	trigger_error(($invoice_name ? "Το '<b>$invoice_name</b>' δεν είναι ταυτότητα τιμολογίου"
		: 'Η ταυτότητα τιμολογίου πρέπει να δίνεται') . ' στη μορφή 1324/31-12-2006', E_USER_ERROR);
}

/** Μαρκάρει ειδικούς χαρακτήρες.
 * Τυχόν χαρακτήρες στο κείμενο που παίζουν ειδικό ρόλο στο πρότυπο (specification) του RTF τους
 * τους κάνει escape με το χαρακτήρα backslash.
 * @param type $a Ένα κείμενο
 * @return type Έλεγχος του $a για χαρακτήρες ελέγχου στο RTF Spec και αντικατάστασή τους */
function rtf($a) { return preg_replace('/([\\\{\}])/', '$0$0', $a); }

/** Ελέγχει αν η ημερομηνία δόθηκε σωστά και την επιστρέφει, αλλιώς πυροδοτεί σφάλμα.
 * @param string $a Ημερομηνία στη μορφή '31 Δεκ 19'
 * @return string Το $a αν η ημερομηνία είναι στη σωστή μορφή */
function chk_date($a) {
	if (preg_match('/\d{1,2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{2})/', $a))
		return $a;
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι ημερομηνία" : 'Οι ημερομηνίες πρέπει να δίνονται') . " στη μορφή π.χ. '20 Νοε 19'");
}

// Παίρνει ένα string της μορφής "26 21:10 Νοε 2006"
function chk_datetime($a) {
	if (preg_match('/\d{1,2} \d{1,2}\:\d{2} (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) \d{4}/', $a))
		return rtf($a);
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι χρονική στιγμή" : 'Οι χρονικές στιγμές πρέπει να δίνονται') . " στη μορφή π.χ. '20 21:34 Νοε 2005'");
}

// Παίρνει ένα string της μορφής "21:10 26 Νοε 2006" και επιστρέφει την ώρα και την ημερομηνία
function get_datetime($a) {
	if (!preg_match('/(\d{1,2}) (\d{1,2}\:\d{2}) (Ιαν|Φεβ|Μαρ|Απρ|Μαι|Ιουν|Ιουλ|Αυγ|Σεπ|Οκτ|Νοε|Δεκ) (\d{4})/', $a, $m))
		trigger_error(($a ? "Το '<b>$a</b>' δεν είναι χρονική στιγμή" : 'Οι χρονικές στιγμές πρέπει να δίνονται') . " στη μορφή π.χ. '20 21:34 Νοε 2005'");
	else {
		$b[] = $m[2]; $b[] = "{$m[1]} {$m[3]} {$m[4]}";
		return $b;
	}
}

/** Επιστρέφει ένα string με το ονοματεπώνυμο ενός Στρατιωτικού.
 * @param a To array με τα στοιχεία του Στρατιωτικού
 * @return String με τη σύντμηση του βαθμού και το ονοματεπώνυμο του Στρατιωτικού */
function person($a) { return rtf($a['Βαθμός'] . ' ' . $a['Ονοματεπώνυμο']); }

/** Επιστρέφει ένα string με το ονοματεπώνυμο ενός Στρατιωτικού.
 * @param a To array με τα στοιχεία του Στρατιωτικού
 * @param c Αν είναι 0, ο Στρατιωτικός είναι στην ονομαστική κλίση. Αν είναι 1 είναι στη γενική. Αν
 * είναι 2 είναι στην αιτιατική. Αν είναι 3 είναι στην κλιτική.
 * @return String με τη σύντμηση του βαθμού, το ονοματεπώνυμο και -αν υπάρχει- τη Μονάδα του
 * Στρατιωτικού */
function person_ext($a, $c) {
	return inflection(person($a), $c) . (isset($a['Μονάδα']) ? " {$a['Μονάδα']}" : null);
}

// Επιστρέφει array με τις εφημερίδες
function getNewspapers($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	foreach($a as & $v)
		$v = 'Εφημερίδα «' . toUppercase($v) . '»';
	return $a;
}

function getDates($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	if (count($a) == 1) return chk_date($a[0]);
	$b = null;
	for ($z = 0; $z < count($a) - 1; $z++)
		$b .= ', ' . chk_date($a[$z]);
	$b .= ' και ' . chk_date($a[count($a) - 1]);
	return substr($b, 2);
}

/** Επιστρέφει την παράμετρο σε ευρώ, αν είναι αριθμός, ειδάλλως πυροδοτεί σφάλμα.
 * @param int|float $a Ο αριθμός
 * @param bool $zero Αν είναι true, ο αριθμός 0 επιστρέφει '0,00 €', ειδάλλως επιστρέφει null
 * @return string Ο αριθμός στη μορφή '1.234,10 €' */
function euro($a, $zero = false) {
	$a = num($a);
	if (!$a) return $zero ? '0,00 ' : null;
	else return sprintf('%01.2f ', $a);
}

/** Επιστρέφει την παράμετρο σε ποσοστό, αν είναι αριθμός, ειδάλλως πυροδοτεί σφάλμα.
 * @param int|float $a Ο αριθμός
 * @return float Ο αριθμός ακολουθούμενος από το '%', ή αν είναι 0, το 0 */
function percent($a) {
	$a = num($a);
	return !$a ? '0' : "$a%";
}

/** Επιστρέφει την παράμετρο αν είναι αριθμός, ειδάλλως πυροδοτεί σφάλμα.
 * @param int|float $a Ο αριθμός
 * @return float Ο αριθμός */
function num($a) {
	if (is_numeric($a)) return (float) $a;
	if ($a) trigger_error("Η τιμή '<b>$a</b>' θα έπρεπε να είναι αριθμός");
}

/** Ελέγχει αν ένας τραπεζικός λογαριασμός ΙΒΑΝ είναι έγκυρος.
 * Υποστηρίζονται μόνο ελληνικοί λογαριασμοί.
 * <p>Ένας λογαριασμός IBAN αποτελείται:
 * <ul><li>Από 2 κεφαλαία λατινικά γράμματα (Α-Ζ) της χώρας. Στην Ελλάδα GR.
 * <li>Από 2 αριθμούς (0-9) ελέγχου εγκυρότητας.
 * <li>Μέχρι 30 χαρακτήρες, αριθμούς ή κεφαλαία λατινικά γράμματα (0-9 Α-Ζ) που καθορίζονται
 * διαφορετικά σε κάθε χώρα και αποτελούν τον Basic Bank Account Number (BBAN). Στην Ελλάδα
 *  συγκεκριμένα είναι 23 αριθμοί (0-9):
 * <ul><li>Από 3 αριθμούς (0-9) που δηλώνουν την τράπεζα.
 * <li>Από 4 αριθμούς (0-9) που δηλώνουν το υποκατάστημα της τράπεζας.
 * <li>Από 16 αριθμούς (0-9) που δηλώνουν το λογαριασμό στην τράπεζα.</ul></ul>
 * <p>Ο αριθμός ελέγχου εγκυρότητας προκύπτει ως εξής:
 * <ul><li>Δημιουργούμε έναν κείμενο που αποτελείται από τον BBAN, τον κωδικό της χώρας και το '00'
 * που αντικαθιστά τους αριθμούς ελέγχου εγκυρότητας που δεν έχουμε υπολογίζει ακόμα. Δηλαδή πάμε
 * τους πρώτους 4 χαρακτήρες του ΙΒΑΝ στο τέλος και για αριθμούς ελέγχου εγκυρότητας βάζουμε '00'.
 * <li>Μετατρέπουμε κάθε γράμμα του κειμένου σε διψήφιο αριθμό, αφαιρώντας από τον ASCII χαρακτήρα
 * το 55. Έτσι κάθε χαρακτήρας ξεκινάει από το 10.
 * <li>Υπολογίζουμε το υπόλοιπο της διαίρεσης του παραπάνω κειμένου, που πλέον είναι αριθμός, από το
 * 97.
 * <li>Αφαιρούμε το υπόλοιπο της διαίρεσης που βρήκαμε παραπάνω από το 98.
 * <li>Το αποτέλεσμα είναι ο αριθμός ελέγχου εγκυρότητας.</ul>
 * <p>Ο έλεγχος εγκυρότητας πραγματοποιείται ως εξής:
 * <ul><li>Δημιουργούμε έναν κείμενο που αποτελείται από τον BBAN, τον κωδικό της χώρας και τους
 * αριθμούς ελέγχου εγκυρότητας. Δηλαδή πάμε τους πρώτους 4 χαρακτήρες του ΙΒΑΝ στο τέλος.
 * <li>Μετατρέπουμε κάθε γράμμα του κειμένου σε διψήφιο αριθμό, αφαιρώντας από τον ASCII χαρακτήρα
 * το 55. Έτσι κάθε χαρακτήρας ξεκινάει από το 10.
 * <li>Υπολογίζουμε το υπόλοιπο της διαίρεσης του παραπάνω κειμένου, που πλέον είναι αριθμός, από το
 * 97.
 * <li>Αν το αποτέλεσμα είναι 1, ο ΙΒΑΝ είναι έγκυρος.</ul>
 * @param string $iban Ο λογαριασμός ΙΒΑΝ
 * @param array $m Εξάγει τα επιμέρους στοιχεία του ΙΒΑΝ λογαριασμού
 * @return string Ο λογαριασμός IBAN αν είναι έγκυρος, αλλιώς πυροδοτεί σφάλμα */
function iban($iban, $m = null) {
	$iban = str_replace(' ', '', $iban);
	if (preg_match('/(GR)(\d{2})(\d{3})(\d{4})(\d{16})/', $iban, $m)) {
		array_shift($m);
		$trg = '';
		foreach(str_split(substr($iban, 4) . substr($iban, 0, 4)) as $c)
			if (ord($c) > 64) $trg .= ord($c) - 55; else $trg .= $c;
		if (bcmod($trg, 97) == 1) return $iban;
	}
	trigger_error($iban ? "Το IBAN '<b>$iban</b>' δεν είναι έγκυρο" : 'Δεν δώθηκε έγκυρο ΙΒΑΝ', E_USER_ERROR);
}

/** Επιστρέφει την τράπεζα στην οποία ανήκει ένας λογαριασμός IBAN.
 * Η τράπεζα πρέπει να δραστηριοποιείται στην Ελλάδα.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ, ο οποίος πρέπει να είναι έγκυρος
 * @return string Η επώνυμία της τράπεζας στην οποία ανήκει ο λογαριασμός */
function bank($iban) {
	$iban = (int) substr($iban, 4, 3);
	switch($iban) {
		case  10: return 'ΤΡΑΠΕΖΑ ΤΗΣ ΕΛΛΑΔΟΣ Α.Ε.';
		case  11: return 'ΕΘΝΙΚΗ ΤΡΑΠΕΖΑ ΤΗΣ ΕΛΛΑΔΟΣ Α.Ε.';
		case  14: return 'ALPHA BANK';
		case  16: return 'ATTICA BANK ΑΝΩΝΥΜΗ ΤΡΑΠΕΖΙΚΗ ΕΤΑΙΡΕΙΑ';
		case  17: return 'ΤΡΑΠΕΖΑ ΠΕΙΡΑΙΩΣ Α.Ε.';
		case  26: return 'ΤΡΑΠΕΖΑ EUROBANK ERGASIAS A.E.';
		case  34: return 'ΕΠΕΝΔΥΤΙΚΗ ΤΡΑΠΕΖΑ ΕΛΛΑΔΟΣ Α.Ε.';
		case  39: return 'ΜΠΕ ΕΝ ΠΕ ΠΑΡΙΜΠΑ ΣΕΚΙΟΥΡΙΤΙΣ ΣΕΡΒΙΣΙΣ';
		case  40: return 'FCA BANK GmbH';
		case  50: return 'ΤΡΑΠΕΖΑ ΣΑΝΤΕΡΑΤ ΙΡΑΝ';
		case  56: return 'AEGEAN BALTIC BANK Α.Τ.Ε.';
		case  57: return 'CREDICOM CONSUMER FINANCE ΤΡΑΠΕΖΑ Α.Ε.';
		case  58: return 'UNION DE CREDITOS INMOBILIARIOS S.A. ESTABLECIMIENTO FINANCIER';
		case  59: return 'OPEL BANK GmbH';
		case  61: return 'FCE BANK PLC';
		case  64: return 'THE ROYAL BANK OF SCOTLAND PLC';
		case  69: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΧΑΝΙΩΝ Συν. Π.Ε.';
		case  71: return 'HSBC BANK PLC';
		case  72: return 'UNICREDIT BANK AG';
		case  73: return 'ΤΡΑΠΕΖΑ ΚΥΠΡΟΥ ΔΗΜΟΣΙΑ ΕΤΑΙΡΕΙΑ ΛΤΔ';
		case  75: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΗΠΕΙΡΟΥ Συν. Π.Ε.';
		case  81: return 'ΜΠΑΝΚ ΟΦ ΑΜΕΡΙΚΑ ΝΑ';
		case  84: return 'CITIBANK EUROPE PLC';
		case  87: return 'ΠΑΓΚΡΗΤΙΑ ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ Συν. Π.Ε.';
		case  88: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ Ν. ΕΒΡΟΥ Συν. Π.Ε.';
		case  89: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΚΑΡΔΙΤΣΑΣ Συν. Π.Ε.';
		case  91: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΘΕΣΣΑΛΙΑΣ Συν. Π.Ε.';
		case  92: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΠΕΛΟΠΟΝΝΗΣΟΥ Συν. Π.Ε.';
		case  94: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΠΙΕΡΙΑΣ "ΟΛΥΜΠΙΑΚΗ ΠΙΣΤΗ" Συν. Π.Ε.';
		case  95: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ ΔΡΑΜΑΣ Συν. Π.Ε.';
		case  97: return 'ΤΑΜΕΙΟ ΠΑΡΑΚΑΤΑΘΗΚΩΝ & ΔΑΝΕΙΩΝ';
		case  99: return 'ΣΥΝΕΤΑΙΡΙΣΤΙΚΗ ΤΡΑΠΕΖΑ Ν. ΣΕΡΡΩΝ Συν. Π.Ε.';
		case 102: return 'VOLKSWAGEN BANK GMBH';
		case 105: return 'BMW AUSTRIA BANK GmbH';
		case 106: return 'MERCEDES-BENZ BANK POLSKA S.A.';
		case 107: return 'GREEK BRANCH OF KEDR OPEN JOINTSTOCK COMPANY COMMERCIAL B';
		case 109: return 'T.C ZIRAAT BANKASI A.S';
		case 111: return 'DEUTSCHE BANK AG';
		case 113: return 'CREDIT SUISSE (LUXEMBOURG) S.A.';
		case 114: return 'FIMBANK PLC.';
		case 115: return 'HSH NORDBANK AG';
		case 116: return 'PROCREDIT BANK (BULGARIA) EAD';
		default: trigger_error("Ο IBAN '<b>$iban</b>' αντιστοιχεί σε μη καταχωρημένη τράπεζα. Επικοινωνήστε με τον προγραμματιστή.", E_USER_ERROR);
	}
}

/** Επιστρέφει έναν τίτλο για έναν δικαιούχο της μορφής 'προμηθευτής'.
 * @param array $invoices Μια λίστα τιμολογίων από την οποία θα αποφασιστεί αν είναι προμηθευτής,
 * εργολάβος ή υπηρεσία
 * @param int $inflection 0 για ονομαστική, 1 για γενική, 2 για αιτιατική και 3 για κλιτική
 * @param int $article 0 χωρίς άρθρο, 1 για άρθρο και 2 για 'στου', 'στον', 'στης', 'στην'
 * @return string Ο τίτλος του δικαιούχου, με τη μορφή που ζητήθηκε */
function get_contractor_title($invoices, $inflection, $article) {
	$c = 0;
	foreach($invoices as $invoice)
		if (!is_supply($invoice['Κατηγορία'])) {
			$c = $invoice['Δικαιούχος']['Τύπος'] == 'PRIVATE_SECTOR' ? 1 : 2;
			break;
		}
	switch($article) {
		case 1:
			switch($inflection) {
				case 0: $article = array('ο ', 'η '); break;
				case 1: $article = array('του ', 'της '); break;
				case 2: $article = array('του ', 'την '); break;
				default: goto def;
			}
			break;
		case 2:
			switch($inflection) {
				case 1: $article = array('στου ', 'στης '); break;
				case 2: $article = array('στον ', 'στην '); break;
				default: goto def;
			}
			break;
		default:
def:		$article = array(null, null);
	}
	switch($c) {
		case 0: return $article[0] . wordInflection('προμηθευτής', $inflection);
		case 1: return $article[0] . wordInflection('ανάδοχος', $inflection);
		case 2: return $article[1] . wordInflection('υπηρεσία', $inflection);
	}
}

// convert a string to uppercase (in Greek capital letters has no tone)
// if we have e.g. "1ος Λόχος" it returns "1ΟΣ ΛΟΧΟΣ"
function toUppercaseFull($s) {
	static $pre = array('/’/', '/Έ/', '/Ή/', '/Ί/', '/Ό/', '/Ύ/', '/Ώ/', '/ς/');
	static $aft = array('Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω', 'Σ');
	return preg_replace($pre, $aft, strtoupper($s));
}

// convert a string to uppercase (in Greek capital letters has no tone)
// if we have e.g. "1ος Λόχος" it returns "1ος ΛΟΧΟΣ" and not "1ΟΣ ΛΌΧΟΣ"
function toUppercase($s) {
	return preg_replace_callback('/(\W|^)\D\w+/', create_function('$m', 'return toUppercaseFull($m[0]);'), $s);
}

// όπως η wordInflection αλλά κλίνει όλες τις λέξεις της πρότασης
// αρκεί να είναι μεγαλύτερες από 2 γράμματα
function inflection($a, $w) {
	return !$w ? $a : preg_replace_callback('/[Α-Ωα-ω’-Ώά-ώΐΰΪΫ]{3,}|\d+(ος|η|ο)/',
		create_function('$m', "return wordInflection(\$m[0], $w);"), $a);
}

// μετατρέπει την ονομαστική ενός αρσενικού ή θηλυκού σε
// (1) γενική, (2) αιτιατική, (3) κλιτική μόνο του ενικού
// δεν υποστηρίζει τρελά πράματα
// η λέξη πρέπει να είναι με μικρά γράμματα (η κατάληξη)
function wordInflection($o, $w) {
	$b = substr($o, -2);
	if ($b == 'ος' && $w == 1) return substr($o, 0, -1) . 'υ';
	elseif ($b == 'ός' && $w == 1) return substr($o, 0, -2) . 'ού';
	elseif ($w >= 2 && $w <= 3 && ($b == 'ος' || $b == 'ός') ||
					$w >= 1 && $w <= 3 && strpos('ης ής ας άς ες ές υς ύς ', "$b ") !== false)
		return substr($o, 0, -1);
  elseif ($w == 1 && strpos('ηήαάωώ', substr($o, -1)) !== false)
		return $o . 'ς';
	return $o;
}


// επιστρέφει το χρόνο σε μορφή π.χ. '05 Ιουν 19'
function now($a = null) { return strftime('%d %b %y', $a == null ? time() : $a); }


/** Επιστρέφει την ελληνική αναπαράσταση ενός ακέραιου αριθμού.
 * @param int $n Ακέραιος αριθμός από το 1 μέχρι το 999
 * @return string Η ελληνική αναπαράσταση του αριθμού (π.χ. στ για το 6) */
function countGreek($n) {
	if ($n < 1 || $n > 999) trigger_error("Για να μεταφραστεί το '<b>$n</b>' σε ελληνική αρίθμηση πρέπει να είναι ακέραιος και να ανήκει στο [1, 999]");
	static $m = array(null, 'α', 'β', 'γ', 'δ', 'ε', 'στ', 'ζ', 'η', 'θ');
	static $d = array(null, 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π', '\u991  ');
	static $e = array(null, 'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', '\u993  ');
	return $e[floor($n / 100)] . $d[floor($n / 10) % 10] . $m[$n % 10];
}


// Αν δώσετε 100.91 θα πάρετε "εκατό ευρώ και ενενήντα ένα λεπτά"
function euro2str($d) {
	$a = int2str((int) floor($d), 2);
	$b = int2str((int) (($d - floor($d)) * 100 + 0.4), 2);
	if ($b != 'μηδέν') $b .= ' λεπτά'; else $b = null;
	if ($a == 'μηδέν')
		return ($b == null) ? 'μηδέν ευρώ' : $b;
	else
		return $a . ' ευρώ' . ($b == null ? '' : ' και ' . $b);
}

// Επιστρέφει το string του φυσικού αριθμού $n
// για αριθμούς από 0 ως 999.999.999.999.999.999.999.999.999.999.999
// Δυστηχώς το php δεν υποστηρίζει integers μεγαλύτερους από 2^31-1 (για 32 bit μηχανήματα)
// Το $genos αναφέρεται στο γένος (αρσενικό = 0, θυληκό = 1, ουδέτερο = 2)
function int2str($n, $genos) {
	if (!is_int($n) || $n < 0) return;
	if (!$n) return 'μηδέν';
	static $xilia = array('χίλιοι', 'χίλιες', 'χίλια');
	static $polla = array('', 'δισ', 'τρισ', 'τετράκις ', 'πεντάκις ', 'εξάκις ', 'επτάκις ', 'οκτάκις ', 'εννιάκις ');

	$out = null;
	$b = $n % 1000;
	$n = floor($n / 1000);
	if ($b) $out = int2str_1_999($b, $genos);
	if (!$n) return $out;

	$b = $n % 1000;
	$n = floor($n / 1000);
	if ($b) {
		if ($out) $out = ' ' . $out;
		if ($b == 1) $out = $xilia[$genos] . $out;
		else $out = int2str_1_999($b, 1) . ' χιλιάδες' . $out;
	}

	$c = 0;
	while($n) {
		$b = $n % 1000;
		$a = floor($n / 1000);
		if ($b) {
			if ($out) $out = ' ' . $out;
			if ($b == 1) $out = 'ένα εκατομμύριο' . $out;
			else $out = int2str_1_999($b, 2) . ' ' . $polla[$c] . 'εκατομμύρια' . $out;
		}
		if (!$n) return $out;
		$c++;
	}
	return $out;
}

// Επιστρέφει το string του φυσικού αριθμού $n για αριθμούς από 1 ως 999.
// Το $genos αναφέρεται στο γένος (αρσενικό = 0, θυληκό = 1, ουδέτερο = 2)
function int2str_1_999($n, $genos) {
	if (!is_int($n) || $n < 1 || $n > 999) return;

	static $ekatodades = array(
		array('εκατό', 'εκατόν', 'διακόσιοι', 'τριακόσιοι', 'τετρακόσιοι', 'πεντακόσιοι', 'εξακόσιοι', 'εφτακόσιοι', 'οκτακόσιοι', 'εννιακόσιοι'),
		array('εκατό', 'εκατόν', 'διακόσιες', 'τριακόσιες', 'τετρακόσιες', 'πεντακόσιες', 'εξακόσιες', 'εφτακόσιες', 'οκτακόσιες', 'εννιακόσιες'),
		array('εκατό', 'εκατόν', 'διακόσια', 'τριακόσια', 'τετρακόσια', 'πεντακόσια', 'εξακόσια', 'εφτακόσια', 'οκτακόσια', 'εννιακόσια')
	);
  static $dekades = array('είκοσι', 'τριάντα', 'σαράντα', 'πενήντα', 'εξήντα', 'εβδομήντα', 'ογδόντα', 'ενενήντα');
	static $monades = array(
		array('ένας', 'δύο', 'τρείς', 'τέσσερεις', 'πέντε', 'έξι', 'επτά', 'οκτώ', 'εννέα', 'δέκα', 'έντεκα', 'δώδεκα', 'δεκατρείς', 'δεκατέσσερεις', 'δεκαπέντε', 'δεκαέξι', 'δεκαεπτά', 'δεκαοκτώ', 'δεκαεννιά'),
		array('ένα', 'δύο', 'τρία', 'τέσσερα', 'πέντε', 'έξι', 'επτά', 'οκτώ', 'εννέα', 'δέκα', 'έντεκα', 'δώδεκα', 'δεκατρία', 'δεκατέσσερα', 'δεκαπέντε', 'δεκαέξι', 'δεκαεπτά', 'δεκαοκτώ', 'δεκαεννιά')
	);

	$out = null;
	$a = floor($n / 100);
	$b = $n % 100;
	if ($a) {
		if (!$b && $a == 1) $a = 0;
		$out = $ekatodades[$genos][$a];
	}
	if (!$b) return $out;
	if ($out) $out .= ' ';
	if ($b < 20) {
		if ($genos == 1 && $b == 1) return $out . 'μία';
		return $out . $monades[$genos == 2 ? 1 : 0][$b - 1];
	}
	$out .= $dekades[floor($b / 10) - 2];
	$b = $n % 10;
	if (!$b) return $out;
	if ($genos == 1 && $b == 1) return $out . ' μία';
	return $out . ' ' . $monades[$genos == 2 ? 1 : 0][$b - 1];
}

/** Επιστρέφει το στοιχείο ενός array αν υπάρχει.
 * @param array $ar Το array
 * @param string|int $key Το κλειδί του στοιχείου του array
 * @return mixed|null Η τιμή του στοιχείου με το δοσμένο κλειδί ή null */
function ifexist($ar, $key) { if (isset($ar[$key])) return $ar[$key]; }
/** Επιστρέφει το στοιχείο ενός array αν υπάρχει ή αν ισχύει μια δευτερεύουσα σχέση.
 * @param bool $exp Μια δευτερεύουσα λογική σχέση
 * @param array $ar Το array
 * @param string|int $key Το κλειδί του στοιχείου του array
 * @return mixed|null Η τιμή του στοιχείου με το δοσμένο κλειδί ή null */
function ifexist2($exp, $ar, $key) { if ($exp || isset($ar[$key])) return $ar[$key]; }
/** Επιστρέφει το στοιχείο ενός array αν υπάρχει, αλλιώς επιστρέφει κάτι άλλο.
 * @param array $ar Το array
 * @param string|int $key Το κλειδί του στοιχείου του array
 * @param mixed $else Μια εναλλακτική τιμή αν δεν είναι να επιστραφεί η κανονική
 * @return mixed|null Η τιμή του στοιχείου με το δοσμένο κλειδί ή null */
function orelse($ar, $key, $else) { return isset($ar[$key]) ? $ar[$key] : $else; }
/** Επιστρέφει το στοιχείο ενός array αν υπάρχει ή αν ισχύει μια δευτερεύουσα σχέση, αλλιώς επιστρέφει κάτι άλλο.
 * @param bool $exp Μια δευτερεύουσα λογική σχέση
 * @param array $ar Το array
 * @param string|int $key Το κλειδί του στοιχείου του array
 * @param mixed $else Μια εναλλακτική τιμή αν δεν είναι να επιστραφεί η κανονική
 * @return mixed Η τιμή του στοιχείου με το δοσμένο κλειδί ή $else */
function orelse2($exp, $ar, $key, $else) { return $exp || isset($ar[$key]) ? $ar[$key] : $else; }


// -------------- Κατάσταση Πληρωμής -----------------------

/** Υπολογίζει και επιστρέφει τα στοιχεία που χρειάζονται για την κατάσταση πληρωμής.
 * @return array Ένα array με δύο στοιχεία. Το πρώτο στοιχείο, είναι ένα array για κάθε δικαιούχο,
 * με τα στοιχεία του δικαιούχου και τα αθροίσματα των αξιών όλων των τιμολογίων του δικαιούχου στη
 * δαπάνη συμπεριλαμβανομένων Καθαρής Αξίας, Καταλογιστέου, Πληρωτέου, ΦΕ και όλων των επιμέρους
 * κρατήσεων. Το δεύτερο στοιχείο είναι ένα array με τα αθροίσματα των επιμέρους κρατήσεων όλων των
 * τιμολογίων όλων των προμηθευτών. */
function calc_pay_report() {
	// Υπολογισμοί για κάθε δικαιούχο
	global $data;
	$a = get_invoices_by_contractor($data['Τιμολόγια']);
	$b = array();
	$keys = array('Καθαρή Αξία', 'Καταλογιστέο', 'ΦΕ', 'Πληρωτέο');
	foreach($a as $invoices) {
		$c = calc_sum_of_invoices_prices($invoices, $keys);
		$c['Κρατήσεις'] = calc_partial_deductions($invoices);
		$b[] = array_merge($invoices[0]['Δικαιούχος'], $c);
	}
	// Υπολογισμοί συνόλων επιμέρους κρατήσεων για όλους τους δικαιούχους
	// Τα υπόλοιπα αθροίσματα δεν υπολογίζονται γιατί υπάρχουν στη δαπάνη
	$c = array_fill_keys($data['Κρατήσεις'], 0);	// Αρχικοποίηση των αθροισμάτων με τιμή 0
	foreach($b as $v) {
		$deductions = $v['Κρατήσεις'];
		foreach($data['Κρατήσεις'] as $key)
			if (isset($deductions[$key])) $c[$key] += $deductions[$key];
	}
	return array ($b, $c);
}