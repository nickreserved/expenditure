<?php

/** Επιστρέφει το PHP script που κλήθηκε πρώτο.
 * @return string Το όνομα του PHP script που κλήθηκε πρώτο */
function get_first_script() {
	$a = get_required_files();
	return $a[0];
}

/** Κλείνει το RTF αρχείο, αν απαιτείται, με εξαγωγή του κατάλληλου κειμένου.
 * Το RTF αρχείο κλείνει με το χαρακτήρα '}', αλλά ένα script μπορεί να ενσωματώνει μεγάλο αριθμό
 * δικαιολογητικών τα οποία δεν πρέπει να κλείσουν το RTF γιατί δεν γνωρίζουν ότι ακολουθούν και άλλα
 * δικαιολογητικά.
 * <p>Π.χ. η δαπάνη εξάγει και δικαιολογητικό σύμβασης, αλλά η σύμβαση εξάγεται και μόνη της. Οπότε
 * μόνο αν βρισκόμαστε στο PHP script που κλήθηκε (και όχι σε κάποιο που εισήχθηκε με require) κλείνει
 * το RTF αρχείο.
 * @param string $file Το όνομα αρχείου του script που αιτείται κλείσιμο του RTF αρχείου */
function rtf_close($file) { if ($file == get_first_script()) echo "\n\n\n}"; }

/** Εξάγει τις ιδιότητες της ενότητας κειμένου ενός στρατιωτικού εγγράφου. */
function start_35_20() { ?>

\sectd\sbkodd\pgwsxn11906\pghsxn16838\marglsxn1984\margrsxn1134\margtsxn1134\margbsxn1134\facingp\margmirror

<?php }

/** Μαρκάρει ειδικούς χαρακτήρες.
 * Τυχόν χαρακτήρες στο κείμενο που παίζουν ειδικό ρόλο στο πρότυπο (specification) του RTF τους
 * τους κάνει escape με το χαρακτήρα backslash.
 * @param type $a Ένα κείμενο
 * @return type Έλεγχος του $a για χαρακτήρες ελέγχου στο RTF Spec και αντικατάστασή τους */
function rtf($a) { return str_replace(array('\\', '{', '}'), array('\\\\', '\{', '\}'), $a); }

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

/** Ελέγχει την ταυτότητα ενός τιμολογίου.
 * @param string $invoice Η ταυτότητα του τιμολογίου που πρέπει να έχει τη μορφή 1234/31-12-2019
 * @param array $date Αν η ταυτότητα του τιμολογίου είναι έγκυρη, επιστρέφει ένα array με 3 στοιχεία:
 * την ημέρα, το μήνα και το έτος έκδοσης του τιμολογίου, όλα αριθμητικά. Π.χ. 31, 12, 2019.
 * @return string Η ταυτότητα του τιμολογίου, όπως δόθηκε */
function invoice($invoice, & $date = null) {
	if (preg_match('/\d+\/(\d{1,2})-(\d{1,2})-(\d{4})/', $invoice, $date)) {
		array_shift($date);
		if ($date[2] >= 2000 && $date[2] < date('Y') + 3 && checkdate($date[1], $date[0], $date[2]))
			return $invoice;
	}
	trigger_error(($invoice ? "Το '<b>$invoice</b>' δεν είναι ταυτότητα τιμολογίου"
		: 'Η ταυτότητα τιμολογίου πρέπει να δίνεται') . ' στη μορφή 1324/31-12-2006');
}

/** Ελέγχει την ταυτότητα μιας σύμβασης.
 * @param string $contract Η ταυτότητα της σύμβασης που πρέπει να έχει τη μορφή 123/31-12-2019
 * @param array $date Αν η ταυτότητα της σύμβασης είναι έγκυρη, επιστρέφει ένα array με 4 στοιχεία:
 * τον αριθμό πρωτοκόλλου, την ημέρα, το μήνα και το έτος έκδοσης της σύμβασης, όλα αριθμητικά. Π.χ.
 * 123, 31, 12, 2019.
 * @return string Η ταυτότητα της σύμβασης, στη μορφή '123/2019' δηλαδή 'αρ. πρωτοκόλου'/'έτος' */
function contract($contract, & $date = null) {
	if (preg_match('/(\d+)\/(\d{1,2})-(\d{1,2})-(\d{4})/', $contract, $date)) {
		array_shift($date);
		return $date[0] . '/' . $date[3];
	}
	trigger_error(($contract ? "Το '<b>$contract</b>' δεν είναι ταυτότητα σύμβασης"
		: 'Η ταυτότητα σύμβασης πρέπει να δίνεται') . ' στη μορφή 132/31-12-2019 (αρ. πρωτοκόλλου/ημερομηνία)');
}


/** Μετατρέπει ένα κείμενο σε κεφαλαία, αφαιρώντας τον τονισμό.
 * @param string $s Το κείμενο εισόδου, π.χ. '1ος Λόχος'
 * @return string Το κείμενο εισόδου με όλα τα γράμματα κεφαλαία, χωρίς τονισμό, π.χ. '1ΟΣ ΛΟΧΟΣ' */
function strtoupperg($s) {
	static $pre = array('’', 'Έ', 'Ή', 'Ί', 'Ό', 'Ύ', 'Ώ', 'ς');
	static $aft = array('Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω', 'Σ');
	return str_replace($pre, $aft, strtoupper($s));
}
/** Μετατρέπει ένα κείμενο σε κεφαλαία, αφαιρώντας τον τονισμό και με σεβασμό σε αριθμούς.
 * @param string $s Το κείμενο εισόδου, π.χ. '1ος Λόχος'
 * @return string Το κείμενο εισόδου με όλα τα γράμματα κεφαλαία, χωρίς τονισμό, π.χ. '1ος ΛΟΧΟΣ' */
function strtouppergn($s) {
	return preg_replace_callback('/(\W|^)\D\w+/', function($m) { return strtoupperg($m[0]); }, $s);
}


/** Επιστρέφει την ελληνική αναπαράσταση ενός ακέραιου αριθμού.
 * @param int $n Ακέραιος αριθμός από το 1 μέχρι το 999
 * @return string Η ελληνική αναπαράσταση του αριθμού (π.χ. στ για το 6) */
function greeknum($n) {
	if ($n < 1 || $n > 999) trigger_error("Για να μεταφραστεί το '<b>$n</b>' σε ελληνική αρίθμηση πρέπει να είναι ακέραιος και να ανήκει στο [1, 999]");
	static $m = array(null, 'α', 'β', 'γ', 'δ', 'ε', 'στ', 'ζ', 'η', 'θ');
	static $d = array(null, 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π', '\u991  ');
	static $e = array(null, 'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', '\u993  ');
	return $e[floor($n / 100)] . $d[floor($n / 10) % 10] . $m[$n % 10];
}


/** Μετατρέπει όλες τις λέξεις της πρότασης από ονομαστική σε άλλη πτώση του ενικού.
 * Δεν υποστηρίζει τρελά πράγματα, απλά ανιχνεύει την κατάληξη μιας μιας λέξης και την αντικαθιστά.
 * Οι λέξεις πρέπει να είναι με μικρά γράμματα ή τουλάχιστον η κατάληξη των λέξεων. Επίσης πρέπει
 * να είναι τουλάχιστον 2 γράμματα.
 * @param string $a Το κείμενο με λέξεις στην ονομαστική ενικού
 * @param int $w Μετατροπή σε (0) ονομαστική, (1) γενική, (2) αιτιατική, (3) κλιτική του ενικού
 * @return string Το $a με τις λέξεις στη σωστή πτώση του ενικού */
function inflectPhrase($a, $w) {
	return !$w ? $a : preg_replace_callback('/[Α-Ωα-ω’-Ώά-ώΐΰΪΫ]{3,}|\d+(ος|η|ο)/',
		function($m) use($w) { return inflection($m[0], $w); }, $a);
}
/** Μετατρέπει την ονομαστική ενός αρσενικού ή θηλυκού σε άλλη πτώση του ενικού.
 * Δεν υποστηρίζει τρελά πράγματα, απλά ανιχνεύει την κατάληξη και την αντικαθιστά. Η λέξη πρέπει
 * να είναι με μικρά γράμματα ή τουλάχιστον η κατάληξη της λέξης. Επίσης πρέπει να είναι τουλάχιστον
 * 2 γράμματα.
 * @param string $o Η ονομαστική ενικού ενός αρσενικού ή θηλυκού αντικειμένου ή επιθέτου (μια λέξη)
 * @param int $w Μετατροπή σε (0) ονομαστική, (1) γενική, (2) αιτιατική, (3) κλιτική του ενικού
 * @return string Το $o στη σωστή πτώση του ενικού */
function inflection($o, $w) {
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


/** Επιστρέφει ένα string με το ονοματεπώνυμο ενός Στρατιωτικού.
 * @param array $a Tα στοιχεία του Στρατιωτικού
 * @return string Το ονοματεπώνυμο του Στρατιωτικού στη μορφή 'Τχης (ΜΧ) Γκέσος Παύλος' */
function person($a) { return rtf($a['Βαθμός'] . ' ' . $a['Ονοματεπώνυμο']); }
/** Επιστρέφει ένα string με το ονοματεπώνυμο ενός Στρατιωτικού.
 * @param array $a Tα στοιχεία του Στρατιωτικού
 * @param int $c Αν είναι 0, ο Στρατιωτικός είναι στην ονομαστική κλίση. Αν είναι 1 είναι στη γενική.
 * Αν είναι 2 είναι στην αιτιατική. Αν είναι 3 είναι στην κλιτική.
 * @return string Το ονοματεπώνυμο του Στρατιωτικού στη δοσμένη πτώση, έχοντας τη μορφή π.χ.
 * 'Τχη (ΜΧ) Γκέσου Παύλου του 3ου ΛΜΧ', με τη Μονάδα να εξάγεται μόνο αν υπάρχει */
function personi($a, $c) {
	return inflectPhrase(person($a), $c) . (isset($a['Μονάδα']) ? " {$a['Μονάδα']}" : null);
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


/** Ελέγχει αν ένας τραπεζικός λογαριασμός ΙΒΑΝ είναι ελληνικός και έγκυρος.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ
 * @return string Ο λογαριασμός IBAN αν είναι ελληνικός και έγκυρος, αλλιώς πυροδοτεί σφάλμα */
function iban($iban) {
	$iban = str_replace(' ', '', $iban);
	if (is_greek_iban($iban) && is_valid_iban($iban)) return $iban;
	trigger_error($iban ? "Το IBAN '<b>$iban</b>' δεν είναι ελληνικό και έγκυρο" : 'Δεν δώθηκε έγκυρο ελληνικό ΙΒΑΝ');
}

/** Ελέγχει αν ένας τραπεζικός λογαριασμός ΙΒΑΝ συμφωνεί με το ελληνικό πρότυπο.
 * Δεν κάνει έλεγχο εγκυρότητας στον IBAN. Απλά ελέγχει αν τα ψηφία του συμφωνούν με αυτά που έχουν
 * καθοριστεί για τους ελληνικούς IBAN.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ χωρίς κενά, μόνο με χαρακτήρες 0-9Α-Ζ
 * @return bool Ο λογαριασμός IBAN είναι ελληνικός αλλά πιθανόν μη έγκυρος */
function is_greek_iban($iban) { return preg_match('/GR\d{25}/', $iban); }

/** Ελέγχει αν ένας τραπεζικός λογαριασμός ΙΒΑΝ συμφωνεί με το γενικό πρότυπο.
 * Δεν κάνει έλεγχο εγκυρότητας στον IBAN. Απλά ελέγχει αν τα ψηφία του συμφωνούν με αυτά που έχουν
 * καθοριστεί για τους IBAN.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ χωρίς κενά, μόνο με χαρακτήρες 0-9Α-Ζ
 * @return bool Είναι λογαριασμός IBAN, αλλά πιθανόν μη έγκυρος */
function is_iban($iban) { return preg_match('/[A-Z]{2}\d{2}[A-Z0-9]{1,30}/', $iban); }

/** Ελέγχει αν ένας τραπεζικός λογαριασμός ΙΒΑΝ είναι έγκυρος.
 * Πριν την εκτέλεση, θα πρέπει ένας από τους is_iban(), is_greek_iban() να έχει επιστρέψει true.
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
 * @param string $iban Ο λογαριασμός ΙΒΑΝ χωρίς κενά, μόνο με χαρακτήρες 0-9Α-Ζ
 * @return bool Ο λογαριασμός IBAN είναι έγκυρος
 * @see is_iban(), is_greek_iban() */
function is_valid_iban($iban) {
	$trg = '';
	foreach(str_split(substr($iban, 4) . substr($iban, 0, 4)) as $c)
		if (ord($c) > 64) $trg .= ord($c) - 55; else $trg .= $c;
	return bcmod($trg, 97) == 1;
}

/** Επιστρέφει την τράπεζα στην οποία ανήκει ένας λογαριασμός IBAN.
 * Η τράπεζα πρέπει να δραστηριοποιείται στην Ελλάδα.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ, ο οποίος πρέπει να είναι ελληνικός
 * @param bool $trigger Πυροδοτεί σφάλμα αν ο ΙΒΑΝ δεν αντιστοιχεί σε καμία τράπεζα
 * @return string|null Η επώνυμία της τράπεζας στην οποία ανήκει ο λογαριασμός */
function bank($iban, $trigger = true) {
	switch((int) substr($iban, 4, 3)) {
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
		default: if ($trigger) trigger_error("Ο IBAN '<b>$iban</b>' αντιστοιχεί σε μη καταχωρημένη τράπεζα.");
	}
}

/** Εκτελείται από το περιβάλλον Java προκειμένου να αποκτήσει πληροφορίες για ένα λογαριασμό IBAN.
 * Εξάγει πληροφορίες οι οποίες επιστρέφουν στο περιβάλλον Java και εμφανίζονται σε ένα παράθυρο.
 * @param string $iban Ο λογαριασμός ΙΒΑΝ χωρίς κενά, μόνο με χαρακτήρες 0-9Α-Ζ */
function iban_gui($iban) {
	if (!is_iban($iban) || !is_valid_iban($iban))
		echo $iban ? "O $iban δεν είναι έγκυρος ΙΒΑΝ" : 'Μη έγκυρος IBAN';
	else {
		echo "O $iban είναι έγκυρος ΙΒΑΝ\n";
		if (!is_greek_iban($iban)) echo 'Ο ΙΒΑΝ δεν είναι ελληνικός και δεν υποστηρίζεται.';
		else {
			echo "O IBAN είναι ελληνικός (GR)\n";
			$bank = bank($iban, false);
			if (!isset($bank)) echo 'Ο IBAN αντιστοιχεί σε τράπεζα που δεν υπάρχει.';
			else {
				echo "Η τράπεζα είναι $bank (κωδικός: " . substr($iban, 4, 3) . ")\n";
				echo 'Το υποκατάστημα της τράπεζας έχει κωδικό ' . substr($iban, 7, 4) . "\n";
				echo 'Ο αριθμός λογαριασμού είναι ' . substr($iban, 11);
			}
		}
	}
}


/** Από τη σύντμηση ενός στρατιωτικού βαθμού, επιστρέφει το βαθμό ολογράφως.
 * @param string $a Η σύντμηση του βαθμού, στη μορφή 'ΕΠΟΠ Δνέας (ΜΧ)'
 * @return string Ο βαθμός ολογράφως, στη μορφή 'ΕΠΟΠ Δεκανέας (ΜΧ)' */
function fullrank($a) {
	$find = array(
		'Στρτης', 'Δνεας', 'Δνέας', 'Λχιας', 'Λχίας', 'Επχιας', 'Επχίας', 'Αλχιας', 'Αλχίας',
		'Ανθστης', 'Ανθστής', 'Ανθλγος', 'Ανθλγός', 'Ανθλχος', 'Υπλγος', 'Υπλγός', 'Υπλχος',
		'Λγος', 'Λγός', 'Ιλχος', 'Ίλχος', 'Τχης', 'Επχος', 'Ανχης', 'Σχης', 'Τξχος', 'Υπτγος',
		'Αντγος', 'Στγος', 'Στγός'
	);
	$replace = array(
		'Στρατιώτης', 'Δεκανέας', 'Δεκανέας', 'Λοχίας', 'Λοχίας', 'Επιλοχίας', 'Επιλοχίας',
		'Αρχιλοχίας', 'Αρχιλοχίας', 'Ανθυπασπιστής', 'Ανθυπασπιστής',
		'Ανθυπολοχαγός', 'Ανθυπολοχαγός', 'Ανθυπίλαρχος', 'Υπολοχαγός', 'Υπολοχαγός', 'Υπίλαρχος',
		'Λοχαγός', 'Λοχαγός', 'Ίλαρχος', 'Ίλαρχος', 'Ταγματάρχης', 'Επίλαρχος', 'Αντισυνταγματάρχης',
		'Συνταγματάρχης', 'Ταξίαρχος', 'Υποστράτηγος', 'Αντιστράτηγος', 'Στρατηγός', 'Στρατηγός'
	);
	return str_replace($find, $replace, $a);
}


/** Ελέγχει αν η ημερομηνία δόθηκε σωστά και την επιστρέφει, αλλιώς πυροδοτεί σφάλμα.
 * @param string $a Ημερομηνία στη μορφή '31 Δεκ 19'
 * @return string Το $a αν η ημερομηνία είναι στη σωστή μορφή */
function chk_date($a) { parse_date($a); return $a; }

/** Παίρνει μια ημερομηνία σε κείμενο και επιστρέφει array με την ημερομηνία.
 * @param string $a Ημερομηνία της μορφής '31 Δεκ 19'
 * @return array Ημερομηνία της μορφής ('31', '12', '2019') */
function parse_date($a) {
	$m = explode(' ', $a, 3);
	if (count($m) == 3) {
		$m[1] = get_month($m[1]);
		$m[2] = get_year($m[2]);
		if (is_int($m[2]) && checkdate($m[1], $m[0], $m[2])) return $m;
	}
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι ημερομηνία" : 'Οι ημερομηνίες πρέπει να δίνονται') . " στη μορφή π.χ. '20 Μαρ 19'");
}

/** Παίρνει μια χρονική στιγμή σε κείμενο και επιστρέφει array με τη χρονική στιγμή.
 * @param string $a Ημερομηνία της μορφής '31 23:59 Δεκ 19'
 * @return array Ημερομηνία της μορφής ('59', '23', '31', '12', '2019') */
function parse_datetime($a) {
	$m = null;
	if (preg_match('/(\d{1,2}) (\d{1,2})\:(\d\d) (.{3,4}) (\d\d|\d\d\d\d)/', $a, $m)) {
		array_shift($m);
		$m[3] = get_month($m[3]);
		$m[4] = get_year($m[4]);
		if (is_int($m[4]) && checkdate($m[3], $m[0], $m[4])
				&& $m[1] >= 0 && $m[1] < 24 && $m[2] >= 0 && $m[2] < 60) return $m;
	}
	trigger_error(($a ? "Το '<b>$a</b>' δεν είναι χρονική στιγμή" : 'Οι χρονικές στιγμές πρέπει να δίνονται') . " στη μορφή π.χ. '20 21:34 Νοε 2005'");
}

/** Ελέγχει για την ορθότητα του έτους.
 * Το τετραψήφιο έτος πρέπει να είναι από 1900 έως και 2 χρόνια μεταγενέστερα του τρέχοντος έτους.
 * <p>Αν το διψήφιο έτος είναι μεταγενέστερο του τρέχοντος + 2 χρόνια, λαμβάνεται ως 19ΧΧ, ειδάλλως
 * ως 20ΧΧ.
 * @param int $year Διψήφιος ή τετραψήφιος αριθμός του έτους
 * @return int Τετραψήφιος αριθμός του έτους */
function get_year($year) {
	$n = strlen($year);
	if ($n == 2 || $n == 4) {
		$curyear = date('Y');
		if ($year >= 0 && $year < 100 || $year >= 1900 && $year < $curyear + 3) {
			if ($year < 100)
				$year += 1997 + $year > $curyear ? 1900 : 2000;
			return (int) $year;
		}
	}
}

/** Επιστρέφει τον αριθμό του μήνα από το σύντομο όνομα του μήνα.
 * @param string $month Σύντμηση του μήνα π.χ. 'Νοε'
 * @return int|null Ο αριθμός του μήνα, π.χ. 11 ή null αν δε δόθηκε έγκυρος μήνας */
function get_month($month) {
	$months = array(
		'Ιαν' => 1, 'Φεβ' => 2, 'Μαρ' => 3, 'Απρ' => 4, 'Μαι' => 5, 'Ιουν' => 6,
		'Ιουλ' => 7, 'Αυγ' => 8, 'Σεπ' => 9, 'Οκτ' => 10, 'Νοε' => 11, 'Δεκ' => 12,
		'Μάρ' => 3, 'Μαϊ' => 5, 'Μάι' => 5, 'Ιούν' => 6, 'Ιούλ' => 7, 'Αύγ' => 8, 'Νοέ' => 11);
	if (isset($months[$month])) return $months[$month];
}

/** Επιστρέφει το χρόνο σε μορφή '05 Ιουν 19'.
 * @param int $a Ένα timestamp. Αν παραλείπεται είναι το τρέχον timestamp.
 * @return string Η ημερομηνία που αντιστοιχεί στο timestamp, σε μορφή '05 Ιουν 19'. */
function now($a = null) { return strftime('%d %b %y', $a ? $a : time()); }


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

/** Επιστέφει τις κατηγορίες κρατήσεων, ΦΕ και ΦΠΑ για μια λίστα τιμολογίων.
 * @param array $invoices Λίστα με τιμολόγια
 * @return array 3 array με τα παρακάτω κλειδιά:
 * <ul><li>'Κρατήσεις': array με κλειδιά τα ποσοστά των κρατήσεων των τιμολογίων και τιμές το
 * αντίστοιχο άθροισμα σε €. (Δεν γίνεται ανάλυση κρατήσεων, αλλά μόνο για το συνολικό ποσοστό κάθε
 * τιμολογίου)
 * <li>'ΦΕ': array με κλειδιά τα ποσοστά του ΦΕ των τιμολογίων και τιμές το αντίστοιχο άθροισμα σε €.
 * <li>'ΦΠΑ': array με κλειδιά τα ποσοστά των ΦΠΑ των τιμολογίων και τιμές το αντίστοιχο άθροισμα
 * σε €.<ul> */
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

//TODO: Χρησιμοποιείται μια φορά. Ενσωμάτωση;
/** Επιστρέφει το γενικό όνομα μιας κατηγορίας τιμολογίου.
 * @param string $category Η κατηγορία του τιμολογίου σε μη φιλική μορφή
 * @return string Η κατηγορία του τιμολογίου σε γενική και φιλική μορφή */
function get_invoice_category($category) {
	return is_supply($category) ? 'Προμήθεια Υλικών' : 'Παροχή Υπηρεσιών';
}

/** Επιστρέφει true αν η κατηγορία του τιμολογίου είναι προμήθειας.
 * @param string $category Η κατηγορία του τιμολογίου
 * @return boolean Η κατηγορία του τιμολογίου είναι προμήθεια */
function is_supply($category) { return $category == 'Προμήθεια υλικών' || $category == 'Προμήθεια υγρών καυσίμων'; }

/** Επιστρέφει τον τύπο διαγωνισμού που έγινε για το τιμολόγιο.
 * @param array $invoice Το τιμολόγιο
 * @return string Ο τύπος του διαγωνισμού ή 'Απευθείας Ανάθεση' */
function get_invoice_tender_type($invoice) {
	return isset($invoice['Σύμβαση']) ? $invoice['Σύμβαση']['Τύπος Διαγωνισμού'] : 'Απευθείας Ανάθεση';
}

/** Έλεγχος αν υπάρχει στη δαπάνη απευθείας ανάθεση.
 * @return bool Υπάρχει στη δαπάνη απευθείας ανάθεση */
function has_direct_assignment() { return has_tender_type('Απευθείας Ανάθεση'); }

/** Έλεγχος αν υπάρχει στη δαπάνη διαγωνισμός.
 * @return bool Υπάρχει στη δαπάνη διαγωνισμός */
function has_tender() {
	global $data;
	if (isset($data['Συμβάσεις']))
		foreach($data['Συμβάσεις'] as $contract)
			if ($contract['Τύπος Διαγωνισμού'] != 'Απευθείας Ανάθεση') return true;
	return false;
}

/** Έλεγχος αν υπάρχει στη δαπάνη συγκεκριμένος τύπος διαγωνισμού.
 * @param string type Ο τύπος του διαγωνισμού
 * @return bool Υπάρχει στη δαπάνη ο δοσμένος τύπος διαγωνισμού */
function has_tender_type($type) {
	global $data;
	$b = $type == 'Απευθείας Ανάθεση';
	if (!isset($data['Συμβάσεις'])) return $b;		// Δεν υπάρχουν συμβάσεις: Απευθείας Ανάθεση
	foreach($data['Συμβάσεις'] as $contract)		// Υπάρχουν συμβάσεις: έλεγχος ισότητας
		if ($contract['Τύπος Διαγωνισμού'] == $type) return true;
	if ($b)						// Μόνο για Απευθείας Ανάθεση: έλεγχος για τιμολογια δίχως σύμβαση
		foreach($data['Τιμολόγια'] as $invoice)
			if (!isset($invoice['Σύμβαση'])) return true;
	return false;
}

/** Επιστρέφει το πιο πρόσφατο timestamp τιμολογίου.
 * @param array $invoices Λίστα τιμολογίων
 * @return int|null Το timestamp του πιο πρόσφατου τιμολογίου ή null αν δεν υπάρχουν τιμολόγια ή δεν
 * έχει οριστεί η ταυτότητα κανενός από αυτά */
function get_newer_invoice_timestamp($invoices) {
	$a = null;
	foreach($invoices as $invoice)
		if (isset($invoice['Τιμολόγιο'])) {
			$b = null;
			invoice($invoice['Τιμολόγιο'], $b);
			$b = mktime(0, 0, 0, $b[1], $b[0], $b[2]);
			if ($a < $b) $a = $b;
		}
	return $a;
}

/** Επιστρέφει σε κείμενο, όλα τα ονόματα ενός array με στοιχεία.
 * @param array $a Η λίστα με τα στοιχεία. Δεν μπορεί να είναι άδεια.
 * @param string $key Το κλειδί, για κάθε στοιχείο, με το όνομα του στοιχείου
 * @return string Τα ονόματα των στοιχείων του array χωρισμένα με ',' εκτός από τα τελευταία 2 που
 * είναι χωρισμένα με 'και' */
function get_names_key($a, $key) { return get_names_func($a, function($b) use($key) { return $b[$key]; }); }

/** Επιστρέφει σε κείμενο, όλα τα ονόματα ενός array.
 * @param array $a Η λίστα με τα ονόματα. Δεν μπορεί να είναι άδεια.
 * @return string Τα ονόματα του array χωρισμένα με ',' εκτός από τα τελευταία 2 που είναι χωρισμένα
 * με 'και' */
function get_names($a) { return get_names_func($a, function($b) { return $b; }); }

/** Επιστρέφει σε κείμενο, όλα τα ονόματα ενός array με στοιχεία.
 * @param array $a Η λίστα με τα στοιχεία. Δεν μπορεί να είναι άδεια.
 * @param callable $func Συνάρτηση που επιστρέφει το όνομα για κάθε στοιχείο του array
 * @return string Τα ονόματα των στοιχείων του array χωρισμένα με ',' εκτός από τα τελευταία 2 που
 * είναι χωρισμένα με 'και' */
function get_names_func($a, $func) {
	$n = count($a);
	$r = '';
	for ($z = 0; $z < $n - 2; ++$z)
		$r .= $func($a[$z]) . ', ';
	if ($n > 1) $r .= $func($a[$n - 2]) . ' και ';
	$r .= $func($a[$n - 1]);
	return $r;
}

/** Επιστρέφει το γένος ενός ουσιαστικού ή επιθέτου.
 * Είναι πιθανό το γένος να επιστραφεί εσφαλμένο.
 * @param string $txt Κείμενο του οποίου η πρώτη λέξη διαχωρίζεται με κενό με το υπόλοιπο κείμενο
 * και για την οποία λέξη θα βρεθεί το γένος
 * @return int 0 αν η πρώτη λέξη είναι αρσενικού γένους, 1 αν είναι θυλικού, 2 αν είναι ουδέτερου
 * και -1 αν δε μπορεί να προσδιοριστεί */
function gender($txt) {
	$txt = explode(' ', $txt, 2);
	$txt = $txt[0];
	switch(strtolower(substr($txt, -1))) {
		case 'ς':
		case 'σ': return 0;
		case 'α': // Εκτός από θυλικό θα μπορούσε να είναι και ουδέτερο
		case 'ά': // π.χ. προσάναμα
		case 'η':
		case 'ή': return 1;
		case 'ο':
		case 'ό':
		case 'ι':
		case 'ί': return 2;
		default: return -1;
	}
}

/** Επιστρέφει το άρθρο ενός ουσιαστικού ή επιθέτου.
 * @param int $gender 0 για αρσενικό γένος, 1 για θυλικό, 2 για ουδέτερο και -1 για απροσδιόριστο
 * @param int $inflection 0 για ονομαστική, 1 για γενική, 2 για αιτιατική και 3 για κλιτική
 * @param bool $on false για άρθρο και true για 'στου', 'στον', 'στης', 'στην', 'στο'
 * @return string Το άρθρο */
function article($gender, $inflection, $on = false) {
	if ($on)
		switch($gender) {
			case 1:
				switch($inflection) {
					case 1: return 'στης';
					case 2: return 'στην';
				}
				break;
			case 2:
				switch($inflection) {
					case 1: return 'στου';
					case 2: return 'στο';
				}
				break;
			default:	// αρσενικά και απροσδιόριστα
				switch($inflection) {
					case 1: return 'στου';
					case 2: return 'στον';
				}
				break;
		}
	else
		switch($gender) {
			case 1:
				switch($inflection) {
					case 0: return 'η';
					case 1: return 'της';
					case 2: return 'την';
				}
				break;
			case 2:
				switch($inflection) {
					case 0: return 'το';
					case 1: return 'του';
					case 2: return 'το';
				}
				break;
			default:	// αρσενικά και απροσδιόριστα
				switch($inflection) {
					case 0: return 'ο';
					case 1: return 'του';
					case 2: return 'τον';
				}
				break;
		}
}

/** Επιστρέφει έναν τίτλο για έναν δικαιούχο της μορφής 'προμηθευτής'.
 * @param array $invoices Μια λίστα τιμολογίων από την οποία θα αποφασιστεί αν είναι προμηθευτής,
 * εργολάβος ή υπηρεσία
 * @param int $inflection 0 για ονομαστική, 1 για γενική, 2 για αιτιατική και 3 για κλιτική
 * @param int $article null χωρίς άρθρο, false για άρθρο και true για 'στου', 'στον', 'στης', 'στην'
 * @return string Ο τίτλος του δικαιούχου, με τη μορφή που ζητήθηκε */
function get_contractor_title($invoices, $inflection, $article) {
	/*TODO: Οι περιπτώσεις είναι:
	 * Προμήθεια υλικών (όλα): προμηθευτής
	 * Παροχή υπηρεσιών (όλα) και όχι Ιδιωτικός Τομέας: υπηρεσία (ΔΕΗ, ΟΤΕ)
	 * Παροχή υπηρεσιών και όχι έργο: Μηχανουργείο, Ηλεκτρολόγος
	 * Μίσθωση Ακινήτων:
	 * Παροχή υπηρεσιών και έργο: ανάδοχος (Κατασκευή έργου, Σύνταξη ή επίβλεψη μελέτης) */
	$name = 'προμηθευτής'; $gender = 0;
	foreach($invoices as $invoice)
		if (!is_supply($invoice['Κατηγορία'])) {
			if ($invoice['Δικαιούχος']['Τύπος'] == 'Ιδιωτικός Τομέας') $name = 'ανάδοχος';
			else { $name = 'υπηρεσία'; $gender = 1; }
			break;
		}
	$name = inflection($name, $inflection);
	if (isset($article)) $article = article($gender, $inflection, $article);
	return isset($article) ? "$article $name" : $name;
}

/** Υλοποίηση τελεστή spaceship (&lt;=&gt;) στο PHP 5.
 * Ισοδύναμο με $a &lt;=&gt; $b.
 * @param mixed $a Τιμή
 * @param mixed $b Τιμή
 * @return int 0 αν είναι ίσα, -1 αν $a &lt; $b και 1 αν $a &gt; $b */
function ss($a, $b) { return $a === $b ? 0 : ($a < $b ? -1 : 1); }

/** Επιστρέφει τα ταχυδρομικά στοιχεία της Μονάδας.
 * @param bool $name Συμπεριλαμβάνεται η επωνυμία της Μονάδας
 * @return string Η επωνυμία, η έδρα, η διεύθυνση, το τηλέφωνο και ο ΤΚ της Μονάδας */
function get_unit_address($name = true) {
	global $data;
	$a = $name ? $data['Μονάδα Πλήρες'] . ', ' : '';
	$a .= 'διεύθυνση: ' . $data['Έδρα'];
	if (isset($data['Διεύθυνση'])) $a .= ', ' . $data['Διεύθυνση'];
	if (isset($data['Τηλέφωνο'])) $a .= ', τηλέφωνο: ' . $data['Τηλέφωνο'];
	if (isset($data['Τ.Κ.'])) $a .= ', Τ.Κ. ' . $data['Τ.Κ.'];
	return $a;
}

//TODO: Δεν χρησιμοποιούνται


/* * Επιστρέφει φιλτραρισμένα τα τιμολόγια με συγκεκριμένο τύπο.
 * @param array $invoices Λίστα τιμολογίων
 * @param string $type Ο τύπος του τιμολογίου (π.χ. 'Προμήθεια Υλικών')
 * @param bool $invert Επιστρέφει τα τιμολόγια που δεν έχουν το συγκεκριμένο τύπο (αντιστροφή)
 * @return array Λίστα τιμολογίων με το δοσμένο τύπο * /
function get_invoices_with_category($invoices, $type, $invert = false) {
	$b = array();
	foreach($invoices as $invoice)
		if (($invoice['Κατηγορία'] == $type) != $invert) $b[] = $invoice;
	return $b;
}

/** Επιστρέφει φιλτραρισμένα τα τιμολόγια με συγκεκριμένο τύπο δικαιούχου.
 * @param array $invoices Λίστα τιμολογίων
 * @param string $type Ο τύπος του δικαιούχου, όπως δίνεται στο πρόγραμμα (π.χ. 'Ιδιωτικός Τομέας')
 * @return array Λίστα τιμολογίων με το δοσμένο τύπο δικαιούχου * /
function get_invoices_with_contractor_type($invoices, $type) {
	$b = array();
	foreach($invoices as $invoice)
		if ($invoice['Προμηθευτής']['Τύπος'] == $type) $b[] = $invoice;
	return $b;
}


/** Επιστρέφει τις συμβάσεις των τιμολογίων.
 * @param array $invoices Λίστα τιμολογίων
 * @return array Λίστα συμβάσεων των δοσμένων τιμολογίων * /
function get_contracts($invoices) {
	global $data;
	$b = array();
	foreach($invoices as $invoice)
		if (isset($invoice['Σύμβαση'])) $b[] = $data['Συμβάσεις'][$invoice['Σύμβαση']];
	return array_unique($b);
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


// Επιστρέφει array με τις εφημερίδες
function getNewspapers($a) {
	$a = preg_split("/([ ]*,[ ]*)/", $a);
	foreach($a as & $v)
		$v = 'Εφημερίδα «' . strtouppergn($v) . '»';
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
*/