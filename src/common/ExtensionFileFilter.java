package common;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {
	String s = "";
	String t = "";

	public ExtensionFileFilter() {}

	public ExtensionFileFilter(String s, String title) { setExtensions(s, title); }

	public final void setExtensions(String s, String title) {
		this.s = s.toLowerCase();
		t = title;
	}

	@Override
	public String getDescription() { return t; }

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String file = f.getName().toLowerCase();
		if (s.length() == 0) return true;
		String[] d = s.split(":");
		for (String d1 : d)
			if (file.endsWith(d1)) return true;
		return false;
	}
}