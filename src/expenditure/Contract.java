package expenditure;

import java.io.IOException;
import util.PhpSerializer;
import util.PhpSerializer.Node;
import util.PhpSerializer.Serializable;

/** Μια σύμβαση. */
public class Contract implements Serializable {


	/** Οι αξίες σε € των τιμολογίων που ανήκουν στην σύμβαση.
	 * Η καθαρή αξία, το ΦΠΑ, το καταλογιστέο, οι κρατήσεις, το πληρωτέο, το ΦΕ και το υπόλοιπο
	 * πληρωτέο, όλα στογγυλοποιημένα στο δεύτερο δεκαδικό ψηφίο και με την αυτή σειρά.
	 * <p>Τα δεδομένα αυτά δεν αποθηκεύονται. Υπολογίζονται προκειμένου να χρησιμοποιηθούν από τον
	 * πίνακα που προβάλει τα αθροίσματα των τιμολογίων (στην καρτέλα «Τιμολόγια», ο τρίτος πίνακας
	 * κάτω-κάτω). */
	final double[] prices = new double[7];


	/** Αρχικοποίηση του αντικειμένου με τις προκαθορισμένες τιμές. */
	Contract() {}

	/** Αρχικοποιεί μια σύμβαση από έναν node δεδομένων του unserialize().
	 * @param n Ο node δεδομένων
	 * @throws Exception Αν ο node δεν είναι αντικείμενο */
	Contract(Node node) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override public void serialize(PhpSerializer export) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
