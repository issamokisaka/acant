package br.org.acant.visao;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public final class JTextFieldModificado extends JTextField {
	private static final long serialVersionUID = 1L;

	private int maximoCaracteres = -1;
	private String conjuntoCaracteres = "";

	public JTextFieldModificado(int maximoCaracteres) {
		super();
		setMaximoCaracteres(maximoCaracteres);
		setConjuntoCaracteres("abcdefghijlmnopqrstuvxzkywABCDEFGHIJLMNOPQRSTUVXZKYW");
		addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jTextFieldKeyTyped(evt);
			}
		});
	}
	
	public JTextFieldModificado(int maximoCaracteres, String conjuntoCaracteres) {
		super();
		setMaximoCaracteres(maximoCaracteres);
		setConjuntoCaracteres(conjuntoCaracteres);
		addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jTextFieldKeyTyped(evt);
			}
		});
	}

	public JTextFieldModificado(int comprimentoCampo, int maximoCaracteres, String conjuntoCaracteres) {
		super();
		int ALTURA_DO_CAMPO = 24;
		setPreferredSize(new Dimension(comprimentoCampo, ALTURA_DO_CAMPO));

		setMaximoCaracteres(maximoCaracteres);
		setConjuntoCaracteres(conjuntoCaracteres);
		addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jTextFieldKeyTyped(evt);
			}
		});
	}

	private void jTextFieldKeyTyped(KeyEvent evt) {
		if (!getConjuntoCaracteres().contains(evt.getKeyChar()+"")) {
			evt.consume();
		}
		if ((getText().length() >= getMaximoCaracteres()) && (getMaximoCaracteres() != -1)) {
			evt.consume();
			setText(getText().substring(0,getMaximoCaracteres()));
		}
	}

	public int getMaximoCaracteres() {
		return maximoCaracteres;
	}
	public void setMaximoCaracteres(int maximoCaracteres) {
		this.maximoCaracteres = maximoCaracteres;
	}

	public String getConjuntoCaracteres() {
		return conjuntoCaracteres;
	}
	public void setConjuntoCaracteres(String conjuntoCaracteres) {
		this.conjuntoCaracteres = conjuntoCaracteres;
	}
}
