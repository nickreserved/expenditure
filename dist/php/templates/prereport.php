<?
require_once('engine/init.php');
require_once('header.php');

// Για να βγεί η Έκθεση "Γενομένης" και όχι "Απαιτουμένης" Δαπάνης
$prereport = true;

require('route_slip.php');
require('report.php');
require('refit_unit_affirmation.php');
require('repair_unit_affirmation.php');
?>