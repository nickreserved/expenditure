package common;

import java.io.*;

public class LoadSaveFile {

	static public String loadResource(String file) throws Exception {
		
		return loadFile(ClassLoader.getSystemResourceAsStream(file));
	}

	static public String loadFile(String file) throws Exception {
		return loadFile(new FileInputStream(file));
	}

	static public String loadFile(InputStream is) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for(;;) {
			int len = is.read(buffer);
			if (len < 0) break;
			bout.write(buffer, 0, len);
		}
		return bout.toString();
	}

	static public void save(String file, Object sv) throws Exception {
		String s = Functions.saveable(null, sv);
		String[] d = s.split("\r\n");
		s = "";
		int c = 0;
		for (String d1 : d) {
			if (d1.startsWith("}") && c > 0) c--;
			s += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t".substring(0, c) + d1 + "\r\n";
			if (d1.endsWith("{")) c++;
		}

		saveStringFile(file, s);
	}

	static public void saveStringFile(String file, String data) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
		osw.write(data);
		osw.flush();
		osw.close();
	}
}