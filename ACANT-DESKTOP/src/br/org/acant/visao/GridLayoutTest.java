package br.org.acant.visao;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GridLayoutTest {
   public static void main(String[] a) {
      JFrame myFrame = new JFrame("GridLayout Test");
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Container myPane = myFrame.getContentPane();
      myPane.setLayout(new GridLayout(2,1));
      myPane.add(getFieldPanel());
      myPane.add(getButtonPanel());
      myFrame.pack();
      myFrame.setVisible(true);
   }
   private static JPanel getFieldPanel() {
      JPanel p = new JPanel(new GridLayout(4,2));
      p.setBorder(BorderFactory.createTitledBorder("Details"));
	  p.add(new JLabel("Name:",SwingConstants.RIGHT));
	  p.add(new JTextField(16));
	  p.add(new JLabel("System:",SwingConstants.RIGHT));
	  p.add(getSystemPanel());
	  p.add(new JLabel("Language:",SwingConstants.RIGHT));
	  p.add(getLanguagePanel());
	  p.add(new JLabel("Year:",SwingConstants.RIGHT));
	  p.add(new JComboBox<String>(
     new String[] {"2021","2022","2023"}));
      return p;
   }
   private static JPanel getButtonPanel() {
      JPanel p = new JPanel(new GridLayout(1,2));
      p.add(new JButton("OK"));
      p.add(new JButton("Cancel"));
      return p;
   }
   private static JPanel getSystemPanel() {
      JRadioButton unixButton = new JRadioButton("Unix",true);
      JRadioButton winButton = new JRadioButton("Window",false);
      ButtonGroup systemGroup = new ButtonGroup();
      systemGroup.add(unixButton);
      systemGroup.add(winButton);
      JPanel p = new JPanel(new GridLayout(1,2));
      p.add(unixButton);
      p.add(winButton);
      return p;
   }
   private static JPanel getLanguagePanel() {
      JPanel p = new JPanel(new GridLayout(1,3));
      p.add(new JCheckBox("Java",true));
	  p.add(new JCheckBox("C++",true));
	  p.add(new JCheckBox("Perl",false));
      return p;
   }
}
