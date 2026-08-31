<?php
// Μπορούμε να παρακάμψουμε αυτή τη διαδικασία
// δημιουργώντας ένα φάκελο '.phar' μέσα στο φάκελο 'php'
// και τοποθετώντας ένα αρχείο 'stub.php' με περιεχόμενο '<?php __HALT_COMPILER();'.
// Επειτα συμπιέζουμε σε 'zip' ή 'phar.zip' ή 'jar'.


// Δημιουργούμε το αρχείο PHAR με την εντολή
// php.exe -d phar.readonly=0 build-phar.php

// Το αρχείο phar που θα δημιουργηθεί.
$phar = new Phar(__DIR__ . '/bundle.phar');
// Ο φάκελος από τον οποίο θα δημιουργηθεί.
$phar->buildFromDirectory(__DIR__ . '/php/');
// Το ελάχιστο stub για το PHAR.
// Δεν γίνεται να αγνοηθεί γιατί μπαίνει το προεπιλεγμένο που είναι περίπου 7KB.
// Δεν γίνεται να είναι '' ή 'die;'.
$phar->setStub('<?php __HALT_COMPILER();');
// Κανονικά παράγεται φυσικό PHAR που είναι ασυμπίεστο κείμενο.
// Με αυτή την εντολή παράγεται ΕΠΙΠΛΕΟΝ PHAR.ZIP
$phar->convertToExecutable(Phar::TAR, Phar::GZ);
$phar->convertToExecutable(Phar::ZIP);
?>