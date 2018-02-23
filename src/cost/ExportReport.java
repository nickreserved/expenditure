package cost;

import javax.swing.*;
import java.util.*;
import common.*;

public class ExportReport {
	static public final String[][] x = {{ "Rich Text", "rtf" }/*, { "OpenOffice", "odt" }*/ };
	
	static public void exportReport(String file) { exportReport(file, null); }
	static public void exportReport(String file, Map<String, String> env) {
		String ext = (String) ((HashObject) MainFrame.data.get("Ρυθμίσεις")).get("Εξαγωγή");
		String ext_d = null;
		if (ext == null) { ext = x[0][1]; ext_d = x[0][0]; }
		else
			for(int z = 0; z < x.length; z++)
				if (ext.equals(x[z][1])) { ext_d = x[z][0]; break; }

		PhpScriptRunner php = new PhpScriptRunner(MainFrame.rootPath + "php/", "templates/" + ext + "/" + file, null);
		if (env != null) php.getEnvironment().putAll(env);
		try {
			int a = php.exec(((Cost) MainFrame.costs.get()).serialize(), php, php, false);
			String err = php.getStderr();
			if (a != 0) err += "<html><font color=red><b>Το php script τερμάτισε με \"γερό\" σφάλμα";
			if (err != null && !err.equals("")) throw new Exception(err);
			JFileChooser fc = new JFileChooser(MainFrame.costs.getPos());
			fc.setFileFilter(new ExtensionFileFilter(ext, ext_d));
			int returnVal = fc.showSaveDialog(MainFrame.ths);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			file = fc.getSelectedFile().getPath();
			if (!file.endsWith("." + ext)) file += "." + ext;
			LoadSaveFile.saveStringFile(file, php.getStdout() + "}");
		} catch (Exception e) {
			showError(e.getMessage());
			return;
		}

		String editor = (String) ((HashObject) MainFrame.data.get("Ρυθμίσεις")).get("Επεξεργαστής");
		if (editor != null)
			try {
				Runtime.getRuntime().exec(editor + " \"" + file + '"');
			} catch(Exception e) {}
	}
	
	static private void showError(String err) {
		JDialog dlg = new JDialog(MainFrame.ths, "Εμφάνιση σφαλμάτων εκτέλεσης του PHP Script", true);
		JList list = new JList(err.split("\n"));
		JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dlg.add(scroll);
		dlg.pack();
		dlg.setLocation((int) (MainFrame.screenSize.getWidth() - dlg.getWidth()) / 2,
				(int) (MainFrame.screenSize.getHeight() - dlg.getHeight()) / 2);
		dlg.setVisible(true);
	}
}