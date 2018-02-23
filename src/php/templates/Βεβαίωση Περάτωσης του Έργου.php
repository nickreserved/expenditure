<?
require_once('engine/init.php');
require_once('header.php');

if ($data['ΤύποςΔαπάνης'] == 'Προμήθεια - Συντήρηση - Επισκευή') trigger_error('Το Πρωτόκολλο Προσωρινής και Οριστικής Παραλαβής αφορά Κατασκευές Έργων', E_USER_ERROR);
?>

\sectd\pgwsxn11906\pghsxn16838\marglsxn850\margrsxn850\margtsxn1134\margbsxn1134

\pard\plain\b <?=chk(toUppercase($data['Μονάδα']))?>\par\par
\fs24\ul\qc ΒΕΒΑΙΩΣΗ\line ΠΕΡΑΤΩΣΗΣ ΤΟΥ ΕΡΓΟΥ\par\par
\pard\plain\tx567\tx1134\qj
\tab <?=chk(ucfirst($data['ΠεριοχήΈργου']))?> σήμερα την <?=$data['ΗμερομηνίαΠροσωρινήςΟριστικήςΠαραλαβής']?> η επιτροπή προσωρινής και οριστικής παραλαβής αποτελούμενοι από τους:\par
\tab\tab α. <?=man_ext($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής'], 2)?> ως πρόεδρο\par
\tab\tab β. <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ'], 2)?> και\par
\tab\tab γ. <?=man_ext($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ'], 2)?> ως μέλη\par
που συγκροτήθηκε με την \ul <?=chk_order($data['ΔγηΑνάθεσης'])?>\ul0 , μεταβόντες σήμερα επί τόπου του έργου «<?=$data['Έργο']?>», κατόπιν προσκλήσεως του επιβλέποντος Αξκου <?=man_ext($data['ΑξκοςΈργου'], 1)?> παρέλαβαν το παραπάνω έργο το οποίο βρήκαν ολοκληρωμένο.\par\par

\pard\plain\fs23\par
\trowd\trkeep\trqc\trautofit1\trpaddfl3\trpaddl113\trpaddfr3\trpaddr113
\clftsWidth1\clNoWrap\cellx2552\clftsWidth1\clNoWrap\cellx5103\clftsWidth1\clNoWrap\cellx7655\clftsWidth1\clNoWrap\cellx10206\qc
ΕΘΕΩΡΗΘΗ\line - Ο -\line ΔΚΤΗΣ\line\line\line <?=chk($data['Δκτης']['Ονοματεπώνυμο'])?>\line <?=chk($data['Δκτης']['Βαθμός'])?>\cell
\line - Ο -\line ΠΡΟΕΔΡΟΣ\line\line\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΠρόεδροςΠροσωρινήςΟριστικήςΠαραλαβής']['Βαθμός'])?>\cell
\line - ΤΑ -\line ΜΕΛΗ\line\line\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΑ']['Βαθμός'])?>
\line\line\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΜέλοςΠροσωρινήςΟριστικήςΠαραλαβήςΒ']['Βαθμός'])?>\cell
\line - Ο -\line ΑΞΚΟΣ ΕΡΓΟΥ\line\line\line <?=chk($data['ΑξκοςΈργου']['Ονοματεπώνυμο'])?>\line <?=chk($data['ΑξκοςΈργου']['Βαθμός'])?>\cell\row

\sect

