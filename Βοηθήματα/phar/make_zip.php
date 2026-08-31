<?php
$src = __DIR__ .'/php';
$trg = __DIR__ .'/bundle.jar';
try {
	// 1. Διαγραφή παλιού zip αν υπάρχει (για να μην προστεθούν αρχεία πάνω του)
	if (file_exists($trg)) unlink($trg);
	// 2. Δημιουργία ενός απλού αντικειμένου PharData με κατάληξη .zip
	// Αυτό λέει στην PHP να δημιουργήσει ένα standard ZIP archive (χωρίς stubs)
	$zip = new PharData($trg, 0, null, Phar::ZIP);
	// 3. Αυτόματη προσθήκη όλων των αρχείων του φακέλου (αναδρομικά)
	// Δυστυχώς δεν περιλαμβάνει καν το φάκελο '.phar'
	$zip->buildFromDirectory($iterator, $src);
	
} catch (Exception $e) { echo "Σφάλμα κατά τη συμπίεση: " . $e->getMessage() . "\n"; }
?>