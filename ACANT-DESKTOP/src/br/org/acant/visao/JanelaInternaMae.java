package br.org.acant.visao;

import javax.swing.JInternalFrame;

public abstract class JanelaInternaMae extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	protected JanelaPrincipal acant;
	protected JanelaInternaMae janelaInternaMae;
	
	public JanelaInternaMae(String titulo, JanelaPrincipal acant) {
		super(titulo);
		this.acant = acant;
	}

	public JanelaPrincipal getAcant() {
		return acant;
	}
	public void setAcant(JanelaPrincipal acant) {
		this.acant = acant;
	}
}
