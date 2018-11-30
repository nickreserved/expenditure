package common;

import static common.TreeFileLoader.GREEK;
import static common.TreeFileLoader.readAllBytes;
import cost.MainFrame;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PhpScriptRunner {
	private final ProcessBuilder pb;
	/** Η διαδρομή προς το εκτελέσιμο php.
	 * Σε linux είναι απλά 'php' ενώ σε Windows είναι η διαδρομή προς το php που
	 * εγκαθιστά ο installer του προγράμματος. */
	final static private String PHP = System.getProperty("os.name").contains("Windows")
			? MainFrame.rootPath + "php5/php.exe" : "php";

	public PhpScriptRunner(String directory, String script, String[] argv) {
		ArrayList<String> v = new ArrayList<>();
		v.add(PHP);
		v.add("-n");
		v.add("-d");
		v.add("log_errors=On");
		v.add("-d");
		v.add("display_errors=Off");
		if (script != null) v.add(script);
		if (argv != null) {
			v.add("--");
			v.addAll(Arrays.asList(argv));
		}

		pb = new ProcessBuilder(v);
		if (directory != null) pb.directory(new File(directory));
	}

	public Map<String, String> getEnvironment() { return pb.environment(); }
	
	static public final int NONE = 0;
	static public final int STDOUT = 1;
	static public final int STDERR = 2;
	static public final int STDOUT_STDERR = 3;
	static public final int STDOUT_STDERR_REDIRECT = 4;

	public static final class Result {
		public int errCode;
		public String stdout, stderr;
	}
	
	/** Execute PHP script.
	 * @param in stdin. If null, no stdin.
	 * @param flags one of NONE, STDOUT, STDOUT_STDERR_REDIRECT, STDOUT_STDERR
	 * @return error code, stdout and stderr
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException */
	@SuppressWarnings("null")
	public Result exec(String in, int flags) throws IOException, InterruptedException, ExecutionException {
		pb.redirectErrorStream(flags == STDOUT_STDERR_REDIRECT);

		Process p = pb.start();
		Thread tout = null, terr = null;
		StdOut rout = null, rerr = null;
		Result r = new Result();

		if (in != null) p.getOutputStream().write(in.getBytes(GREEK));
		p.getOutputStream().close();
		if (flags == NONE || flags == STDERR) p.getInputStream().close();
		else {
			rout = new StdOut(p.getInputStream());
			tout = new Thread(rout);
			tout.start();
		}
		if (flags != STDERR && flags != STDOUT_STDERR) p.getErrorStream().close();
		else {
			rerr = new StdOut(p.getErrorStream());
			terr = new Thread(rerr);
			terr.start();
		}

		r.errCode = p.waitFor();
		if (tout != null) {
			tout.join();
			if (rout.error != null) throw new ExecutionException("Πρόβλημα στο stdout του php script", rout.error);
			r.stdout = rout.out;
		}
		if (terr != null) {
			terr.join();
			if (rerr.error != null) throw new ExecutionException("Πρόβλημα στο stderr του php script", rerr.error);
			r.stderr = rerr.out;
		}
		return r;
	}
	
	private static class StdOut implements Runnable {
		StdOut(InputStream stdout) { this.stdout = stdout; }
		private final InputStream stdout;
		Exception error;
		String out;
		@Override public void run() {
			try { out = new String(readAllBytes(stdout), GREEK); }
			catch (IOException e) { error = e; }
		}
	}

	static public void init() throws ExecutionException {
		try {
			PhpScriptRunner p = new PhpScriptRunner(null, null, null);
			Result r = p.exec("<?echo 5+5;exit(51);?>", STDOUT_STDERR_REDIRECT);
			if (r.errCode != 51 || !"10".equals(r.stdout))
				throw new ExecutionException("Μη αναμενόμενα αποτελέσματα από την εκτέλεση του php script", null);
		} catch(IOException | InterruptedException | ExecutionException e) {
			throw new ExecutionException("Πρόβλημα στην αρχικοποίηση της php μηχανής", e);
		}
	}
}