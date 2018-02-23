<?
require_once('engine/functions.php');
require_once('header.php');

// Για να βγεί η Έκθεση "Γενομένης" και όχι "Απαιτουμένης" Δαπάνης
$prereport = true;

require('route_slip.php');
require('report.php');
?>