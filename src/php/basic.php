<?php

/** Θέτει το χειριστή σφαλμάτων και προειδοποιήσεων για τη μηχανή PHP.
 * Αν επιστρέψει το callback, ο χειρισμός του σφάλματος πραγματοποιείται από τον προκαθορισμένο
 * χειριστή σφάλματος του PHP.
 * <p>Ο λόγος ύπαρξης του χειριστή, είναι προκειμένου να αντικαθιστά τυχόν σφάλματα του PHP με
 * ελληνικό, πιο επεξηγηματικό κείμενο, καθώς το κείμενο αυτό θα το δει ο πελάτης και θα πρέπει να
 * καταλάβει τι έκανε λάθος ή τι παρέλειψε.
 * @param int $errno Ο τύπος του σφάλματος
 * @param string $str Το μήνυμα του σφάλματος
 * @param string $file Το αρχείο στο οποίο συνέβη το σφάλμα
 * @param int $line Η γραμμή στο αρχείο στο οποίο συνέβη το σφάλμα */
set_error_handler(function($errno, $str, $file, $line) {
	if ($errno == E_STRICT) return;

	// Εμφάνιση του μυνήματος λάθους όσο γίνεται πιο ανθρώπινα
	$str = str_replace('Undefined index:', 'Δεν ορίστηκε το πεδίο <b>', $str);
	$file = basename($file, '.php');
	fwrite(STDERR, "<html><b>$file($line):</b> $str\n");

	if ($errno == E_USER_ERROR) die();
});
set_time_limit(10);
date_default_timezone_set('Europe/Athens');
setlocale(LC_ALL, 'el_GR', 'ell_grc');