package br.org.acant.visao;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.MaskFormatter;
import br.org.acant.util.Utilitario;

public class JanelaInternaPeriodoPesquisa extends JanelaInternaMae
									  	  implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel labelPeriodo;
	private JLabel labelA;

	private JTextField dataInicial;
	private JTextField dataFinal;

	private JButton bOK;
	private JButton bCancelar;

	public JanelaInternaPeriodoPesquisa(JanelaPrincipal acant) {
		super("Período de pesquisa", acant);

		setBounds(50, 50, 430, 100);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEFT));

		labelPeriodo = new JLabel("Digite o período de pesquisa: ");
		add(labelPeriodo);

		try {
			dataInicial = new JFormattedTextField(new MaskFormatter("##/##/####"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		dataInicial.setText(Utilitario.obterDataAtual());
		dataInicial.requestFocus();
		add(dataInicial);

		labelA = new JLabel("a");
		add(labelA);

		try {
			dataFinal = new JFormattedTextField(new MaskFormatter("##/##/####"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		dataFinal.setText(Utilitario.obterDataAtual());
		add(dataFinal);

		bOK = new JButton("OK");
		bOK.setMnemonic('O');
		bOK.addActionListener(this);
		add(bOK);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		add(bCancelar);
		
		final JanelaInternaMae janela = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				janela.setVisible(false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == bOK) {
			chamarJanelaFluxoCaixa(dataInicial.getText(), dataFinal.getText());
		} else if (evt.getSource() == bCancelar) {
			setVisible(false);
		}
	}

	private void chamarJanelaFluxoCaixa(String dataInicial, String dataFinal) {
		setVisible(false);

		JanelaInternaFluxoCaixa janelaInternaFluxoCaixa =
				new JanelaInternaFluxoCaixa(acant, dataInicial, dataFinal);
		acant.getPainelCentral().add(janelaInternaFluxoCaixa);
		janelaInternaFluxoCaixa.setVisible(true);
	}
}
