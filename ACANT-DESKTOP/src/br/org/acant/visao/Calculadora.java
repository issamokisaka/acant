package br.org.acant.visao;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


public class Calculadora extends JanelaInternaMae
	implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel[] row;
	private JButton[] button;
	private String[] buttonString;

	private int[] dimW;
	private int[] dimH;	

	private Dimension displayDimension;
	private Dimension regularDimension;
	private Dimension rColumnDimension;
	private Dimension zeroButDimension;

	private boolean[] function;
	private double[] temporary;
	private JTextArea display;
	private Font font;

	public Calculadora(JanelaPrincipal acant) {
		super("Calculadora",acant);
		setSize(360,250);
		setResizable(false);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		GridLayout grid = new GridLayout(5,5);
		setLayout(grid);

		row = new JPanel[5];
		button = new JButton[19];
		buttonString = new String[]{"7", "8", "9", "+",
							        "4", "5", "6", "-",
							        "1", "2", "3", "*",
							        ",", "/", "C", "",
							        "", "=", "0"};
		dimW = new int[]{300,45,100,90};
		dimH = new int[]{35,40};

		displayDimension = new Dimension(dimW[0], dimH[0]);
		regularDimension = new Dimension(dimW[1], dimH[1]);
		rColumnDimension = new Dimension(dimW[2], dimH[1]);
		zeroButDimension = new Dimension(dimW[3], dimH[1]);
		function = new boolean[4];
		temporary = new double[]{0,0};
		display = new JTextArea(1, 20);
		font = new Font("Times new Roman", Font.BOLD, 14);
		
		for(int i = 0; i < 4; i++)
			function[i] = false;

		FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
		FlowLayout f2 = new FlowLayout(FlowLayout.CENTER,1,1);
		
		for(int i = 0; i < 5; i++)
			row[i] = new JPanel();

		row[0].setLayout(f1);

		for(int i = 1; i < 5; i++)
			row[i].setLayout(f2);
		
		for(int i = 0; i < 19; i++) {
			button[i] = new JButton();
			button[i].setText(buttonString[i]);
			button[i].setFont(font);
			button[i].addActionListener(this);
		}
		
		display.setFont(font);
		display.setEditable(false);
		display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		display.setPreferredSize(displayDimension);
		
		for(int i = 0; i < 14; i++)
			button[i].setPreferredSize(regularDimension);

		for(int i = 14; i < 18; i++)
			button[i].setPreferredSize(rColumnDimension);
		
		button[18].setPreferredSize(zeroButDimension);
		row[0].add(display);
		add(row[0]);
		
		for(int i = 0; i < 4; i++)
			row[1].add(button[i]);
		row[1].add(button[14]);
		add(row[1]);
		
		for(int i = 4; i < 8; i++)
			row[2].add(button[i]);
		row[2].add(button[15]);
		add(row[2]);

		for(int i = 8; i < 12; i++)
			row[3].add(button[i]);

		row[3].add(button[16]);
		add(row[3]);
		
		row[4].add(button[18]);
		for(int i = 12; i < 14; i++)
			row[4].add(button[i]);

		row[4].add(button[17]);
		add(row[4]);

		setVisible(true);

		final JanelaInternaMae jim = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);

				jim.setVisible(false);

// ########################################################
			}
		});
	}

    private void clear() {
        try {
            display.setText("");
            for(int i = 0; i < 4; i++)
                function[i] = false;

            for(int i = 0; i < 2; i++)
                temporary[i] = 0;
        } catch(NullPointerException e) { 
        	e.printStackTrace();
        }
    }

    private void getResult() {
        double result = 0;
        temporary[1] = Double.parseDouble(display.getText());
        String temp0 = Double.toString(temporary[0]);
        String temp1 = Double.toString(temporary[1]);

        try {
            if(temp0.contains("-")) {
                String[] temp00 = temp0.split("-", 2);
                temporary[0] = (Double.parseDouble(temp00[1]) * -1);
            }

            if(temp1.contains("-")) {
                String[] temp11 = temp1.split("-", 2);
                temporary[1] = (Double.parseDouble(temp11[1]) * -1);
            }
        } catch(ArrayIndexOutOfBoundsException e) {
        	e.printStackTrace();
        }

        try {
            if(function[2] == true)
                result = temporary[0] * temporary[1];
            else if(function[3] == true)
                result = temporary[0] / temporary[1];
            else if(function[0] == true)
                result = temporary[0] + temporary[1];
            else if(function[1] == true)
                result = temporary[0] - temporary[1];

            display.setText(Double.toString(result));
            for(int i = 0; i < 4; i++)
                function[i] = false;
        } catch(NumberFormatException e) {
        	e.printStackTrace();
        }
    }

	@Override
	public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == button[0])
            display.append("7");

        if(ae.getSource() == button[1])
            display.append("8");

        if(ae.getSource() == button[2])
            display.append("9");

//      When press button +
        if(ae.getSource() == button[3]) {
            //add function[0]
            temporary[0] = Double.parseDouble(display.getText());
            function[0] = true;
            display.setText("");
        }

        if(ae.getSource() == button[4])
            display.append("4");

        if(ae.getSource() == button[5])
            display.append("5");

        if(ae.getSource() == button[6])
            display.append("6");

//      When press button -
        if(ae.getSource() == button[7]) {
            //subtract function[1]
            temporary[0] = Double.parseDouble(display.getText());
            function[1] = true;
            display.setText("");
        }

        if(ae.getSource() == button[8])
            display.append("1");

        if(ae.getSource() == button[9])
            display.append("2");

        if(ae.getSource() == button[10])
            display.append("3");

//      When press button *
        if(ae.getSource() == button[11]) {
            //multiply function[2]
            temporary[0] = Double.parseDouble(display.getText());
            function[2] = true;
            display.setText("");
        }

        if(ae.getSource() == button[12])
            display.append(".");

//      When press button /
        if(ae.getSource() == button[13]) {
            //divide function[3]
            temporary[0] = Double.parseDouble(display.getText());
            function[3] = true;
            display.setText("");
        }

        if(ae.getSource() == button[14])
        	clear();
        
        if(ae.getSource() == button[17])
            getResult();

        if(ae.getSource() == button[18])
            display.append("0");
	}
}
