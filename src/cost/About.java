package cost;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;

public class About extends JPanel {
  public About() {
    JLabel image = new JLabel(new ImageIcon(cost.MainFrame.class.getResource("myself.jpg")));
    JLabel title = new JLabel("Στρατιωτικές Δαπάνες");
    title.setFont(new java.awt.Font("Dialog", 1, 16));
    JLabel programmer = new JLabel("Προγραμματίστηκε από τον Παύλο Γκέσο");
    JLabel version = new JLabel("Έκδοση 0.1.0 Alpha (unstable) 08 Ιαν 2005");

    String s = "Δεν βρέθηκε το αρχείο about.html";
    try {
      s = LoadSaveFile.loadFileToString("cost/about.html");
    } catch (Exception e) {
    }

    JEditorPane extra_info = new JEditorPane("text/html", s);
    JScrollPane scrlComment = new JScrollPane(extra_info, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                              JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    extra_info.setBackground(new Color(255, 243, 217));
    extra_info.setEditable(false);
    extra_info.setCaretPosition(0);

    Box bv1 = Box.createVerticalBox();
    bv1.add(title);
    bv1.add(version);
    bv1.add(programmer);
    Box bh = Box.createHorizontalBox();
    bh.add(image);
    bh.add(bv1);
    Box bv2 = Box.createVerticalBox();
    bv2.add(bh);
    bv2.add(scrlComment);
    add(bv2);
  }
}