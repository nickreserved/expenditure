<?php
require_once('statement.php');

statement_common(
	/** Εξάγει μια υπεύθυνη δήλωση δικαιούχου που γνωστοποιεί το IBAN.
	 * @param array $data Τα δεδομένα της υπεύθυνης δήλωσης */
	function($data) {
		$data['Προς'] = 'ΔΟΥ';
		$data['Δήλωση'] = 'Το χρηματικό ποσό που δικαιούμαι να εισπράξω σαν «{\b ' . rtf($data['Επωνυμία']) . '}», ΑΦΜ: ' . rtf($data['ΑΦΜ']) . ', τηλέφωνο: ' . rtf($data['Τηλέφωνο']) . ', να κατατεθεί στο λογαριασμό IBAN {\b ' . iban($data['ΙΒΑΝ']) . '} ({\b ' . bank($data['ΙΒΑΝ']) . '}).\par Επισυνάπτεται φωτοτυπία του βιβλιαρίου καταθέσεων.';
		statement_contractor($data);
	});

rtf_close(__FILE__);