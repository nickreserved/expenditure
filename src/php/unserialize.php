<?php
require_once('basic.php');

// Μετατροπή του κειμένου στο STDIN στην κύρια δομή δεδομένων
$data = unserialize(stream_get_contents(STDIN));
if (!$data) trigger_error('Τα εισερχόμενα δεδομένα είναι λάθος', E_USER_ERROR);