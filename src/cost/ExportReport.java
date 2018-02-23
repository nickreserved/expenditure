package cost;

import common.ExtensionFileFilter;
import common.PhpScriptRunner;
import static common.TreeFileLoader.GREEK;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ExportReport {
	static public final String[][] TYPES = {{ "Rich Text", "rtf" } };

	static public void exportReport(String file) { exportReport(file, null); }
	static public void exportReport(String file, Map<String, String> env) {
		PhpScriptRunner php = new PhpScriptRunner(MainFrame.rootPath + "php/", "templates/" + file, null);
		if (env != null) php.getEnvironment().putAll(env);
		try {
			PhpScriptRunner.Result r = php.exec(
					((Cost) MainFrame.costs.get()).serialize(new StringBuilder(65536)).toString(),
					PhpScriptRunner.STDOUT_STDERR);
			if (r.errCode != 0) r.stderr += "<html><font color=red><b>Το php script τερμάτισε με σοβαρό σφάλμα";
			if (r.stderr != null && !r.stderr.equals("")) throw new Exception(r.stderr);
			JFileChooser fc = new JFileChooser(MainFrame.costs.getPos());
			fc.setFileFilter(new ExtensionFileFilter("rtf", "Rich Text"));
			int returnVal = fc.showSaveDialog(MainFrame.ths);
			if(returnVal != JFileChooser.APPROVE_OPTION) return;
			file = fc.getSelectedFile().getPath();
			if (!file.endsWith(".rtf")) file += ".rtf";
			try (FileOutputStream f = new FileOutputStream(file)) {
				f.write((r.stdout + "}").getBytes(GREEK));
				f.close();
			}
			try { Desktop.getDesktop().open(new File(file)); }
			catch (IllegalArgumentException | IOException ex) {}
		} catch (Exception e) { showError(e.getMessage()); }
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