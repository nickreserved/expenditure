package util;

import java.io.File;
import java.util.stream.Stream;
import javax.swing.filechooser.FileFilter;

/** Ένα φίλτρο αρχείων με βάση την επέκτασή τους. */
public class ExtensionFileFilter extends FileFilter {
	/** Μια λίστα με όλες τις αποδεκτές επεκτάσεις αρχείων. */
	final private String[] extensions;
	/** Η περιγραφή του τύπου αρχείων. */
	final private String description;

	/** Αρχικοποίηση του φίλτρου αρχείων.
	 * Υπάρχει διάκριση πεζών-κεφαλαίων στις επεκτάσεις.
	 * @param extension Η αποδεκτή επέκταση αρχείου
	 * @param description Η περιγραφή του τύπου αρχείων */
	public ExtensionFileFilter(String extension, String description) {
		this(new String[] { extension }, description);
	}

	/** Αρχικοποίηση του φίλτρου αρχείων.
	 * Υπάρχει διάκριση πεζών-κεφαλαίων στις επεκτάσεις.
	 * @param extensions Μια λίστα με όλες τις αποδεκτές επεκτάσεις αρχείων
	 * @param description Η περιγραφή του τύπου αρχείων */
	public ExtensionFileFilter(String[] extensions, String description) {
		this.extensions = extensions; this.description = description;
	}

	@Override public String getDescription() { return description; }

	@Override public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String file = f.getName();
		return Stream.of(extensions).anyMatch(i -> file.endsWith("." + i));
	}
}