package expenditure;

import static expenditure.MainFrame.appendExt;
import static expenditure.MainFrame.data;
import static expenditure.MainFrame.exportReport;
import static expenditure.MainFrame.getLocationScreenCentered;
import static expenditure.MainFrame.showExceptionMessage;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import util.ExtensionFileFilter;
import util.PhpSerializer;
import util.PhpSerializer.FormatException;
import util.PhpSerializer.Node;
import util.PhpSerializer.VariableFields;
import util.PhpSerializer.VariableSerializable;
import util.PropertiesTableModel;
import util.PropertiesTableModel.TableRecord;
import static util.PropertiesTableModel.createTable;
import static util.ResizableTableModel.getString;

/** Διάλογος για εξαγωγή, αποθήκευση και φόρτωση στοιχείων υπεύθυνης δήλωσης. */
final class StatementDialog extends JDialog {
	/** Η προέκταση αρχείου των αρχείων υπεύθυνης δήλωσης. */
	static final private String EXT = ".δήλωση";
	/** Το component με το περιεχόμενο της δήλωσης. */
	private final JTextPane text = new JTextPane();
	/** Ο πίνακας με τα στοιχεία της δήλωσης. */
	private final JTable table = createTable(new PropertiesTableModel((int index) -> data.statement,
			new String[] { Statement.H[0], PersonInfo.H[0], PersonInfo.H[1], PersonInfo.H[2],
			PersonInfo.H[3], PersonInfo.H[4], PersonInfo.H[5], PersonInfo.H[6],
			PersonInfo.H[7] + " (Έδρα, Διεύθυνση, ΤΚ)", PersonInfo.H[8], Statement.H[1] }, 1, true));

	/** Αρχικοποίηση του παραθύρου της δήλωσης.
	 * @param w Το πατρικό παράθυρο ή null */
	StatementDialog(Window w) {
		super(w, "Σύνταξη Υπεύθυνης Δήλωσης", APPLICATION_MODAL);

		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		Box bv = Box.createVerticalBox();

		bv.add(table);
		bv.add(Box.createVerticalStrut(5));

		text.setText(data.statement.statement);
		text.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void removeUpdate(DocumentEvent e) { insertUpdate(e); }
			@Override public void changedUpdate(DocumentEvent e) { insertUpdate(e); }
			@Override public void insertUpdate(DocumentEvent e) {
				try {
					Document d = e.getDocument();
					data.statement.statement = d.getText(0, d.getLength());
				} catch (BadLocationException ex) {}
			}
		});
		StyleConstants.setTabSet(	// Θέτει το tab stop στα 20 pixels
				text.getLogicalStyle(),
				new TabSet(IntStream.range(1, 11).mapToObj(i -> new TabStop(20 * i)).toArray(TabStop[]::new)));
		bv.add(new JScrollPane(text));
		bv.add(Box.createVerticalStrut(5));

		Box bh = Box.createHorizontalBox();
		bh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		JButton button = new JButton("Εξαγωγή");
		button.addActionListener((ActionEvent e) ->
				exportReport("Υπεύθυνη Δήλωση.php", data.statement, null));
		bh.add(button);
		bh.add(Box.createHorizontalGlue());

		button = new JButton("Αποθήκευση");
		button.addActionListener((ActionEvent e) -> save());
		bh.add(button);
		bh.add(Box.createHorizontalStrut(5));

		button = new JButton("Άνοιγμα");
		button.addActionListener((ActionEvent e) -> open());
		bh.add(button);
		bv.add(bh);

		getContentPane().add(bv);
		setSize(600, 450);
		setLocation(getLocationScreenCentered(getWidth(), getHeight()));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/** Ανοίγει το διάλογο "Αποθήκευση ως" και αποθηκεύει την τρέχουσα δήλωση. */
	private void save() {
		try {
			Statement s = data.statement;
			s.statement = text.getText();
			if (s.statement.isEmpty()) s.statement = null;
			// Διάλογος Αποθήκευση ως...
			JFileChooser fc = new JFileChooser(s.file);
			fc.setSelectedFile(s.file);
			fc.setFileFilter(new ExtensionFileFilter("δήλωση", "Αρχείο Υπεύθυνης Δήλωσης"));
			if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			File f = appendExt(fc.getSelectedFile(), EXT);
			// Διαδικασίες αν το αρχείο αποθήκευσης είναι διαφορετικό από το ήδη υπάρχον
			boolean otherFile = !f.equals(s.file);
			if (otherFile) {
				// Αν το νέο όνομα αρχείου υπάρχει στο μέσο αποθήκευσης
				if (f.exists() && NO_OPTION == showConfirmDialog(this,
						"Το αρχείο αυτό υπάρχει και θα χαθεί.\nΘελετε να συνεχίσω;",
						"Αποθήκευση Υπεύθυνης Δήλωσης", YES_NO_OPTION, WARNING_MESSAGE))
					return;
				s.file = f;
			}
			// Αποθήκευση του αρχείου δήλωσης
			PhpSerializer.serialize(s, new FileOutputStream(s.file), UTF_8);
		} catch(IOException e) {
			showExceptionMessage(this, e, "Αποθήκευση Υπεύθυνης Δήλωσης", "Πρόβλημα κατά την αποθήκευση της υπεύθυνης δήλωσης");
		}
	}

	/** Ανοίγει μια δήλωση με το διάλογο "Άνοιγμα". */
	private void open() {
		// Ο διάλογος Άνοιγμα... θα ανοίξει στο φάκελο που είναι αποθηκευμένη η τρέχουσα δήλωση
		JFileChooser fc = new JFileChooser(data.statement.file);
		fc.setFileFilter(new ExtensionFileFilter("δήλωση", "Αρχείο Υπεύθυνης Δήλωσης"));
		int returnVal = fc.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION) return;
		try {
			File file = appendExt(fc.getSelectedFile(), EXT);
			data.statement = new Statement(PhpSerializer.unserialize(new FileInputStream(file), UTF_8));
			data.statement.file = file;
			text.setText(data.statement.statement);
			table.tableChanged(new TableModelEvent(table.getModel(), 0, table.getModel().getRowCount(), 1, UPDATE));
		} catch (FormatException e) {
			showExceptionMessage(this, e, "Άνοιγμα αρχείου",
				"Το αρχείο που προσπαθείτε να ανοίξετε δεν είναι σωστό αρχείο δήλωσης");
		} catch (IOException e) {
			showExceptionMessage(this, e, "Άνοιγμα αρχείου", "Πρόβλημα κατά το άνοιγμα της δήλωσης");
		}
	}


	/** Τα στοιχεία μιας Υπεύθυνης Δήλωσης. */
	static final class Statement implements VariableSerializable, TableRecord {
		/** Σε ποιο αρχείο είναι αποθηκευμένη. */
		File file;
		/** Σε ποιον απευθύνεται η υπεύθυνη δήλωση. */
		String to;
		/** Προσωπικά στοιχεία προσώπου. */
		PersonInfo person;
		/** Ημερομηνία έκδοσης στη μορφή '31 Δεκ 19'. */
		String exportdate;
		/** Το κείμενο της δήλωσης. */
		String statement;

		/** Αρχικοποίηση του αντικειμένου της δήλωσης. */
		Statement() { person = new PersonInfo(); }
		/** Αρχικοποιεί μια υπεύθυνη δήλωση από έναν node δεδομένων του unserialize().
		 * @param node Ο node δεδομένων */
		Statement(Node node) {
			to          = node.getField(H[0]).getString();
			exportdate  = node.getField(H[1]).getString();
			statement   = node.getField(H[2]).getString();
			Node n = node.getField(H[3]);
			if (n.isString())
				file    = new File(n.getString());
			person      = new PersonInfo(node);
		}

		/** Ονόματα πεδίων αποθήκευσης. */
		static final String[] H = { "Προς", "Ημερομηνία Έκδοσης", "Δήλωση", "Αρχείο" };

		@Override public void serialize(VariableFields fields) {
			if (to != null)         fields.add(H[0], to);
			if (exportdate != null) fields.add(H[1], exportdate);
			if (statement != null)  fields.add(H[2], statement);
			person.serialize(fields);
		}

		/** Εξαγωγή της δήλωσης μαζί με το όνομα αρχείου. */
		VariableSerializable saveWithFilename() {
			return (VariableFields fields) -> {
				serialize(fields);
				if (file != null) fields.add(H[3], file.getAbsolutePath());
			};
		}

		@Override public Object getCell(int index) {
			switch(index) {
				case 0: return to;
				case 10: return exportdate;
				default: return person.getCell(index - 1);
			}
		}

		@Override public void setCell(int index, Object value) {
			String s = getString(value);
			switch(index) {
				case 0:  to         = s; break;
				case 10: exportdate = s; break;
				default: person.setCell(index - 1, value);
			}
		}
	}
}
