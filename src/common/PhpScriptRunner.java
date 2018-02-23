package common;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class PhpScriptRunner implements PipeHandler {
	private static boolean runScript = false;
	private static String interpreter = "php";
	
	private OutputStream stdin;
	private InputStream stdout;
	private InputStream stderr;
	
	private ProcessBuilder pb;
	private Map<String, String> env;
	
	private int what_run;
	private Object cin;
	private PipeHandler cout, cerr;
	private boolean throwthread = false;
	private String sout, serr;
	
	
	public PhpScriptRunner(String directory, String script, String[] argv) {
		Vector<String> v = new Vector<String>();
		if (!runScript || script == null) {
			v.add(interpreter);
			v.add("-n");
			v.add("-d");
			v.add("log_errors=On");
			v.add("-d");
			v.add("display_errors=Off");
		}
		if (script != null) v.add(script);
		if (argv != null) {
			if (!runScript) v.add("--");
			v.addAll(Arrays.asList(argv));
		}
		
		pb = new ProcessBuilder(v);
		pb.directory(directory == null ? null : new File(directory));
		env = pb.environment();
	}
	
	public Map<String, String> getEnvironment() { return env; }
	public String getStdout() { return sout; }
	public String getStderr() { return serr; }
	
	public int exec(Object in, PipeHandler out, PipeHandler err, boolean redirect) throws Exception {
		
		if (err == null && redirect) pb.redirectErrorStream(true);
		
		Process p = pb.start();
		stdin = p.getOutputStream();
		stdout = p.getInputStream();
		stderr = p.getErrorStream();
		
		cin = in; cout = out; cerr = err;
		
		if (out == null) stdout.close(); else new Thread(new RunPipe(1)).start();
		if (in == null) stdin.close(); else new Thread(new RunPipe(0)).start();
		if (err == null) stderr.close(); else new Thread(new RunPipe(2)).start();
		
		int r = p.waitFor();
		if (throwthread) throw new Exception("Πρόβλημα στα stdin, stdout, stderr του php script");
		return r;
		
	}
	
	private class RunPipe implements Runnable {
		private int what;
		public RunPipe(int what_run) { what = what_run; }
		public void run() {
			try {
				switch(what) {
					case 0:
						if (cin instanceof PipeHandler)
							((PipeHandler) cin).processOutputStream(0, stdin);
						else processOutputStream(0, stdin);
						break;
					case 1:
						cout.processInputStream(0, stdout);
						break;
					case 2:
						cerr.processInputStream(1, stderr);
				}
			} catch(Exception e) {
				throwthread = true;
			}
		}
	}
	
	public void processOutputStream(int id, OutputStream os) {
		try {
			int size = cin instanceof byte[] ? ((byte[]) cin).length : cin.toString().length();
			for(int z = 0; size > 0; z++, size -= 16384) {
				os.write(cin instanceof byte[] ? (byte[]) cin : cin.toString().getBytes(), z *16384, size < 16384 ? size : 16384);
				os.flush();
			}
			os.close();
		} catch(Exception e) {}
	}
	
	public void processInputStream(int id, InputStream is) throws Exception {
		byte[] b = new byte[16384];
		if (id == 0) sout = ""; else serr = "";
		int c = 0;
		while((c = is.read(b)) > 0)
			if (id == 0) sout += new String(b, 0, c); else serr += new String(b, 0, c);
	}
	
	
	static public void init(String directory) {
		PhpScriptRunner p = new PhpScriptRunner(null, null, null);
		try {
			if (p.exec("<?echo 5+5;exit(51);?>", p, null, true) != 51)
				throw new ExecutionException("Πρόβλημα στην τιμή εξόδου του php script", null);
			String f = p.getStdout();
			if (f == null || !f.equals("10"))
				throw new ExecutionException("Πρόβλημα στο stdout του php script", null);
		} catch(Exception e) {
			Functions.showExceptionMessage(e, "Πρόβλημα στην αρχικοποίηση της php μηχανής");
		}
	}
}