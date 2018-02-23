<?
require_once('engine/init.php');
require_once('header.php');

// Για να βγεί η Έκθεση "Γενομένης" και όχι "Απαιτουμένης" Δαπάνης
$prereport = true;

require('Διαβιβαστικό Δαπάνης.php');
require('Έκθεση Γενόμενης Δαπάνης.php');
require('Βεβαίωση Ανεφοδιαστικού Οργάνου.php');
require('Βεβαίωση Επισκευαστικού Οργάνου.php');
?>