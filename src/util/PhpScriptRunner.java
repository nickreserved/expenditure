package util;

import static expenditure.MainFrame.readAllBytes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static util.PhpScriptRunner.Exec.OutputType.NONE;
import static util.PhpScriptRunner.Exec.OutputType.STDERR;
import static util.PhpScriptRunner.Exec.OutputType.STDOUT_STDERR;
import static util.PhpScriptRunner.Exec.OutputType.STDOUT_STDERR_REDIRECT;

/** Εκτέλεση PHP script καλώντας τον PHP Command Line Interpreter. */
public class PhpScriptRunner {
	/** Ρυθμίσεις και δημιουργία της διεργασίας του PHP Command Line Interpreter. */
	private final ProcessBuilder pb;
	/** Η διαδρομή προς το εκτελέσιμο php. */
	static private String php = "php";

	/** Αρχικοποίηση του περιβάλλοντος εκτέλεσης του PHP script.
	 * @param directory Ο φάκελος εργασίας του PHP Command Line Interpreter
	 * @param script Η διαδρομή αρχείου του PHP script
	 * @param argv Οι παράμετροι του PHP script, ή null αν δεν υπάρχουν */
	public PhpScriptRunner(String directory, String script, String[] argv) {
		ArrayList<String> v = new ArrayList<>();
		v.add(php);
		v.add("-n");			// Εκτέλεση του PHP cli, χωρίς να φορτώσει ρυθμίσεις από το php.ini
		v.add("-d");			// Θέτει τη ρύθμιση που ακολουθεί
		v.add("log_errors=On");
		v.add("-d");
		v.add("display_errors=Off");
		if (script != null) v.add(script);
		if (argv != null) {
			v.add("--");		// Θα ακολουθήσουν οι παράμετροι γραμμής εντολής του PHP script
			v.addAll(Arrays.asList(argv));
		}

		pb = new ProcessBuilder(v);
		if (directory != null) pb.directory(new File(directory));
	}

	/** Επιστρέφει τις μεταβλητές περιβάλλοντος του PHP Command Line Interpreter.
	 * @return Οι μεταβλητές περιβάλλοντος του PHP cli */
	public Map<String, String> getEnvironment() { return pb.environment(); }

	/** Αρχικοποιεί το PHP και κάνει έναν έλεγχο καλής λειτουργίας.
	 * Πρέπει να εκτελεστεί πριν από οποιαδήποτε εκτέλεση PHP script. Μπορεί να εκτελεστεί και
	 * επιπλέον φορές, προκειμένου π.χ. να αλλάξει η διαδρομή προς τον PHP Command Line Interpreter.
	 * Μετά από αυτή την κλήση, το PHP θεωρείται απόλυτα λειτουργικό.
	 * @param phpPath Η διαδρομή του εκτελέσιμου, του PHP cli.
	 * @throws ExecutionException Η διαδρομή είναι εσφαλμένη ή η δοκιμαστική εκτέλεση ενός απλού
	 * script απέτυχε ή είχε λανθασμένα εξαγόμενα αποτελέσματα ή άλλο απροσδιόριστο σφάλμα. Το
	 * ακριβές σφάλμα περιέχεται μέσα στο exception. */
	static public void init(String phpPath) throws ExecutionException {
		if (php != null) php = phpPath;
		try {
			Exec r = new Exec(new PhpScriptRunner(null, null, null),
					"<?echo 5+5;exit(51);?>".getBytes(),
					STDOUT_STDERR_REDIRECT);
			if (r.errCode != 51 || r.stdout == null || !"10".equals(new String(r.stdout)))
				throw new ExecutionException("Μη αναμενόμενα αποτελέσματα από την εκτέλεση του php script", null);
		} catch(IOException | InterruptedException | ExecutionException e) {
			throw new ExecutionException("Πρόβλημα στην αρχικοποίηση της php μηχανής", e);
		}
	}

	/** Αποκτά έλεγχο στο stdin του PHP script, προκειμένου να εξάγει εκεί δεδομένα. */
	public interface StdInStream {
		/** Αποκτά έλεγχο στο stdin του PHP script, προκειμένου να εξάγει εκεί δεδομένα.
		 * Όταν ολοκληρώσει την εγγραφή δεδομένων, πρέπει να κλείσει το stream, ειδάλλως το script
		 * δε θα ολοκληρωθεί ποτέ (θα περιμένει κι άλλη είσοδο).
		 * @param os Το stdin του PHP script
		 * @throws IOException */
		void run(OutputStream os) throws IOException;
	}

	/** Αποκτά έλεγχο στο stdout ή stderr του PHP script, προκειμένου να λαμβάνει από εκεί δεδομένα. */
	public interface StdOutStream {
		/** Αποκτά έλεγχο στο stdout ή stderr του PHP script, προκειμένου να λαμβάνει από εκεί δεδομένα.
		 * Μέσα στην κλήση πρέπει να δημιουργείται ξεχωριστό thread το οποίο λαμβάνει τα δεδομένα.
		 * @param is Το stdout ή stderr του PHP script */
		void run(InputStream is);
	}

	/** Αποκτά έλεγχο στο stdin του PHP script, προκειμένου να εξάγει εκεί δεδομένα. */
	public static final class StdIn implements StdInStream {
		/** Δεδομένα για εισαγωγή στο PHP script. */
		final private byte[] content;
		/** Αρχικοποίηση.
		 * @param content Δεδομένα για εισαγωγή στο PHP script */
		public StdIn(byte[] content) { this.content = content; }
		@Override public void run(OutputStream is) throws IOException {
			if (content != null) is.write(content);
			is.close();
		}
	}

	/** Αποκτά έλεγχο στο stdout ή stderr του PHP script, προκειμένου να λαμβάνει από εκεί δεδομένα. */
	public static final class StdOut implements StdOutStream {
		/** To exception που συνέβη στο thread και τερμάτησε, ειδάλλως null. */
		private IOException error;
		/** Tα δεδομένα εξόδου του script, αφού αυτό τερματίσει. */
		private byte[] out;
		/** Tο thread που είναι υπεύθυνο για να λαμβάνει τα δεδομένα από το strout ή stderr του script. */
		private Thread thread;
		@Override public void run(InputStream is) {
			thread = new Thread(() -> {
				try { out = readAllBytes(is); }
				catch (IOException e) { error = e; }
			});
			thread.start();
		}
		/** Περιμένει μέχρι να τερματίσει το script και μετά κάνει διαθέσιμα τα δεδομένα.
		 * @return Τα δεδομένα εξόδου του script
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws ExecutionException Αν το thread τερμάτισε με exception, περιέχει το exception του */
		public byte[] join() throws IOException, InterruptedException, ExecutionException {
			thread.join();
			if (error != null) throw new ExecutionException("Σφάλμα στο stream εξόδου του PHP script", error);
			return out;
		}
	}

	/** Εκτελεί ένα PHP script.
	 * @param in stdin handler ή null αν δεν υπάρχει είσοδος
	 * @param out stdout handler ή null αν δεν υπάρχει έξοδος. Στο stdout εξάγονται και τα σφάλματα
	 * του stderr.
	 * @return Ο κωδικός τερματισμού του PHP script. Τιμή διαφορετική του 0, δηλώνει πρόβλημα.
	 * @throws IOException
	 * @throws InterruptedException */
	public int exec(StdInStream in, StdOutStream out)
			throws IOException, InterruptedException {
		pb.redirectErrorStream(true);
		return exec(in, out, null);
	}

	/** Εκτελεί ένα PHP script.
	 * @param in stdin handler ή null αν δεν υπάρχει είσοδος
	 * @param out stdout handler ή null αν δεν υπάρχει έξοδος
	 * @param err stderr handler ή null αν δεν υπάρχει έξοδος σφαλμάτων
	 * @return Ο κωδικός τερματισμού του PHP script. Τιμή διαφορετική του 0, δηλώνει πρόβλημα.
	 * @throws IOException
	 * @throws InterruptedException */
	public int exec(StdInStream in, StdOutStream out, StdOutStream err)
			throws IOException, InterruptedException {
		Process p = pb.start();
		if (out != null) out.run(p.getInputStream()); else p.getInputStream().close();
		if (err != null) err.run(p.getErrorStream()); else p.getErrorStream().close();
		if (in != null) in.run(p.getOutputStream()); else p.getOutputStream().close();
		return p.waitFor();
	}

	/** Εκτελεί ένα PHP script. */
	static public final class Exec {
		/** Ποια stream εξάγονται από την διεργασία εκτέλεσης του PHP script. */
		public enum OutputType {
			/** Δεν εξάγεται ούτε stdout ούτε stderr. */
			NONE,
			/** Εξάγεται stdout άλλά δεν εξάγεται stderr. */
			STDOUT,
			/** Εξάγεται stderr άλλά δεν εξάγεται stdout. */
			STDERR,
			/** Εξάγονται και stderr και stdout. */
			STDOUT_STDERR,
			/** Συνενώνει την έξοδο του stderr στο stdout, αφήνοντας κενό το stderr. */
			STDOUT_STDERR_REDIRECT
		}
		/** Εκτελεί ένα PHP script.
		 * @param psr Ένα αντικείμενο εκτέλεσης PHP script.
		 * @param in stdin ή null αν δεν υπάρχει είσοδος.
		 * @param type Τι και πως θα εξαχθεί από το PHP script
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws ExecutionException */
		public Exec(PhpScriptRunner psr, byte[] in, OutputType type)
				throws IOException, InterruptedException, ExecutionException {
			psr.pb.redirectErrorStream(type == STDOUT_STDERR_REDIRECT);
			StdIn sin = new StdIn(in);
			StdOut sout = type != STDERR && type != NONE ? new StdOut() : null;
			StdOut serr = type == STDERR || type == STDOUT_STDERR ? new StdOut() : null;
			errCode = psr.exec(sin, sout, serr);
			stdout = sout != null ? sout.join() : null;
			stderr = serr != null ? serr.join() : null;
		}
		/** Ο κωδικός τερματισμού του PHP script. Τιμή διαφορετική του 0, δηλώνει πρόβλημα. */
		public final int errCode;
		/** stdout ή null αν δεν επιθυμούμε έξοδο. */
		public final byte[] stdout;
		/** stderr ή null αν δεν επιθυμούμε έξοδο σφαλμάτων. */
		public final byte[] stderr;
	}
}