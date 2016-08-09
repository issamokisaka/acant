package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import br.org.acant.modelo.PlanoConta;

public class JanelaInternaPlanoContaAtualizacao extends JanelaInternaMae
implements ActionListener {
	private static final long serialVersionUID = 1L;

//	private JComboBox<String> combo01;
	private JTextField campoNomeConta;
	private JPanel  painelSuperior;

	private JButton bAtualizar;
	private JButton bCancelar;
	private JPanel  painelInferior;

	private PlanoConta pcs;
//	private JTable tabela;

	public JanelaInternaPlanoContaAtualizacao(JanelaPrincipal acant,
			PlanoConta pcs, JTable tabela) {
		super("Atualizar Plano de Conta", acant);

		this.pcs = pcs;
//		this.tabela = tabela;
		initComponents();
	}

	private void initComponents() {
		setBounds(50, 50, 500, 100);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		painelSuperior = new JPanel();
		painelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

//		String[] contCombo01 = new String[]{"ENTRADA", "SAIDA"};
//		combo01 = new JComboBox<String>(contCombo01);
//		combo01.setSelectedItem(pcs.getTipoLancamentoAvo().trim());
//		combo01.requestFocus();
//		combo01.addActionListener(this);
//		painelSuperior.add(combo01);

		campoNomeConta = new JTextField(pcs.getNomePlanoConta().trim(), 40);
		painelSuperior.add(campoNomeConta);

		add(painelSuperior, BorderLayout.NORTH);


		painelInferior = new JPanel();
		painelInferior.setLayout(new FlowLayout(FlowLayout.LEFT));

		bAtualizar = new JButton("Atualizar");
		bAtualizar.setMnemonic('A');
		bAtualizar.addActionListener(this);
		painelInferior.add(bAtualizar);
		getRootPane().setDefaultButton(bAtualizar);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		painelInferior.add(bCancelar);

		add(painelInferior, BorderLayout.SOUTH);


		final JanelaInternaPlanoContaAtualizacao janela = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				janela.setVisible(false);

				janela.campoNomeConta.setText("");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == bAtualizar) {
			atualizarPlanoConta();
		} else if (evt.getSource() == bCancelar) {
			setVisible(false);
		}
	}

	private void atualizarPlanoConta() {
//		pcs.setTipoLancamentoAvo((String)combo01.getSelectedItem());
		pcs.setNomePlanoConta(campoNomeConta.getText());
		try {
			PlanoConta.atualizar(pcs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		setVisible(false);
		acant.getPlanoConta().setVisible(false);

		try {
			acant.getPessoa().setPlanoConta(PlanoConta.obterPlanoContas(acant.getPessoa().getCod()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		acant.setPessoa(acant.getPessoa());

		acant.setPlanoConta(new JanelaInternaPlanoConta(acant));
		acant.getPainelCentral().add(acant.getPlanoConta());
		acant.getPlanoConta().setVisible(true);
	}
}
