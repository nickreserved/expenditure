package expenditure;

import java.text.Normalizer;
import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/// Ο πάροχος δεδομένων στο JTree με την CPV ιεραρχία.
public class CpvTreeModel implements TreeModel {
	public CpvTreeModel(TreeNode rootNode) { filtered_cpv_hierarchy = full_cpv_hierarchy = rootNode; }

	@Override
	public Object getRoot() { return filtered_cpv_hierarchy; }

	@Override
	public Object getChild(Object parent, int index) { return ((TreeNode) parent).children[index]; }

	@Override
	public int getChildCount(Object parent) { return ((TreeNode) parent).children.length; }

	@Override
	public boolean isLeaf(Object node) {
		TreeNode[] children = ((TreeNode) node).children;
		return children == null || children.length == 0;
	}

	/** Αν αλλάξει κάποιος κόμβος ενημερώνεται η συνάρτηση, οστόσο δεν αλλάζει κανένας κόμβος. */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		TreeNode[] children = ((TreeNode) parent).children;
		for (int i = 0; i < children.length; ++i)
			if (children[i] == child) return i;
		return 0;
	}

	/// Οι listeners του μοντέλου.
	ArrayList<TreeModelListener> listeners = new ArrayList<>();

	@Override
	public void addTreeModelListener(TreeModelListener l) { listeners.add(l); }

	@Override
	public void removeTreeModelListener(TreeModelListener l) { listeners.remove(l); }


	/// Ο κόμβος CPV του δέντρου.
	static public class TreeNode {
		/** Constructor που χρησιμοποιείται μόνο από τον ριζικό κόμβο
		 * @param info Ο τιτλος του κόμβου */
		public TreeNode(String info) { cpv = null; this.info = info; }
		/** Constructor που χρησιμοποιείται από κόμβο CPV χωρίς όμως να οριστούν τα παιδιά του.
		 * @param str Ένα array 2 στοιχείων με το πρώτο να είναι ο cpv και το δεύτερο η επεξήγηση. */
		public TreeNode(String[] str) { cpv = str[0]; info = str[1]; }
		/** Constructor που χρησιμοποιείται από κόμβο CPV χωρίς όμως να οριστούν τα παιδιά του.
		 * @param cpv Ο κωδικός CPV του κόμβου
		 * @param info Ο τίτλος του κόμβου */
		public TreeNode(String cpv, String info) { this.cpv = cpv; this.info = info; }
		/** Constructor που χρησιμοποιείται από κόμβο CPV και ορίζει και τα παιδιά του.
		 * @param cpv Ο κωδικός CPV του κόμβου
		 * @param info Ο τίτλος του κόμβου
		 * @param children Τα παιδιά του κόμβου */
		public TreeNode(String cpv, String info, TreeNode[] children) {
			this.cpv = cpv; this.info = info; this.children = children;
		}

		/// Ο κωδικός CPV μαζί με το ψηφίο ελέγχου.
		public final String cpv;
		/// Η περιγραφή του CPV με ελληνικά
		public final String info;
		/// Οι CPV απόγονοι.
		public TreeNode[] children = null;

		@Override
		public String toString() {
			if (cpv == null) return info;
			else return cpv + ": " + info;
		}
	}

	/// Η πλήρης ιεραρχία CPV.
	private final TreeNode full_cpv_hierarchy;

	/// Η ιεραρχία CPV μετά το φιλτράρισμα της αναζήτησης.
	private TreeNode filtered_cpv_hierarchy;


	void search(String find) {
		if (find.trim().isEmpty())
			if (filtered_cpv_hierarchy == full_cpv_hierarchy) return;
			else filtered_cpv_hierarchy = full_cpv_hierarchy;
		else {
			String[] keywords = Normalizer.normalize(find, Normalizer.Form.NFD)
					.replaceAll("\\p{M}", "").toUpperCase().split("\\s+");
			TreeNode root = search(keywords, full_cpv_hierarchy);
			if (root == null) root = new TreeNode("Δεν βρέθηκε εγγραφή");
			filtered_cpv_hierarchy = root;
		}
		fireTreeStructureChanged();
	}

	private TreeNode search(String[] keywords, TreeNode node) {
		ArrayList<TreeNode> children = new ArrayList<>();

		// Έλεγχος παιδιών. Αν υπάρχει έστω και ένα παιδί που να ικανοποιεί την αναζήτηση, υπάρχει
		// και ο γονιός.
		if (node.children != null)
			for (int i = 0; i < node.children.length; ++i) {
				TreeNode child = search(keywords, node.children[i]);
				if (child != null) children.add(child);
			}

		// Έλεγχος του γονιού μόνο αν δεν υπάρχουν παιδιά που ικανοποιούν την αναζήτηση.
		boolean pass = !children.isEmpty();
		if (!pass) {
			for (int i = 0; i < keywords.length; ++i)
				if (node.cpv != null && node.cpv.contains(keywords[i]) || node.info != null
						&& Normalizer.normalize(node.info, Normalizer.Form.NFD)
								.replaceAll("\\p{M}", "").toUpperCase()
								.contains(keywords[i])) pass = true;
			return pass ? new TreeNode(node.cpv, node.info) : null;
		} else return new TreeNode(node.cpv, node.info, children.toArray(new TreeNode[0]));
	}

	/// Ενημερώνει τους listeners ότι ολόκληρη η δομή του δέντρου άλλαξε.
	private void fireTreeStructureChanged() {
		TreeModelEvent e = new TreeModelEvent(this, new Object[] { getRoot() });
		for (TreeModelListener listener : listeners)
			listener.treeStructureChanged(e);
	}
}
