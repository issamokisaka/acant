package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import br.org.acant.modelo.PlanoConta;

public class JanelaInternaPlanoContaCadastro extends JanelaInternaMae
											 implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JPanel  painelSuperior;
	private JComboBox<String> combo01;
	private JComboBox<Object> combo02;
	private JComboBox<Object> combo03;

	private JPanel  painelCentral;
	private JLabel labelNomeConta;
	private JTextField campoNomeConta;

	private JPanel  painelInferior;
	private JButton bIncluir;
	private JButton bCancelar;

	protected Vector<PlanoConta> vetorEntradas;
	protected Vector<PlanoConta> vetorSaidas;
	
	public JanelaInternaPlanoContaCadastro(JanelaPrincipal acant, Vector<PlanoConta> entradas, Vector<PlanoConta> saidas) {
		super("Incluir conta/detalhamento", acant);

		this.vetorEntradas = entradas;
		this.vetorSaidas = saidas;

		setBounds(50, 50, 500, 130);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		painelSuperior = new JPanel();
		painelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

		String[] contCombo01 = {"ENTRADA", "SAIDA"};
		combo01 = new JComboBox<String>(contCombo01);
		combo01.requestFocus();
		combo01.addActionListener(this);
		painelSuperior.add(combo01);


		combo02 = new JComboBox<Object>(new Object[]{new PlanoConta()});
		combo02.addActionListener(this);
		carregarCombo02();
		painelSuperior.add(combo02);

		combo03 = new JComboBox<Object>(new Object[]{new PlanoConta()});
		painelSuperior.add(combo03);

		add(painelSuperior, BorderLayout.NORTH);

		painelCentral = new JPanel();
		painelCentral.setLayout(new FlowLayout(FlowLayout.LEFT));
		labelNomeConta = new JLabel("Nome da conta/detalhamento:");
		painelCentral.add(labelNomeConta);
		campoNomeConta = new JTextField("",20);
		painelCentral.add(campoNomeConta);
		add(painelCentral, BorderLayout.CENTER);

		painelInferior = new JPanel();
		painelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT));

		bIncluir = new JButton("Incluir");
		bIncluir.setMnemonic('I');
		bIncluir.addActionListener(this);
		painelInferior.add(bIncluir);
		getRootPane().setDefaultButton(bIncluir);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		painelInferior.add(bCancelar);

		add(painelInferior, BorderLayout.SOUTH);

		final JanelaInternaPlanoContaCadastro janela = this;
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
		if (evt.getSource() == bIncluir) {
			
			incluirPlanoConta();
			
		} else if (evt.getSource() == bCancelar) {
			setVisible(false);

			campoNomeConta.setText("");
		} else if (evt.getSource() == combo01) {
			carregarCombo02();
		} else if (evt.getSource() == combo02) {
			carregarCombo03();
		}
	}

	private void incluirPlanoConta() {
		PlanoConta comboMae = (PlanoConta)combo02.getSelectedItem();
		String comboAvo = (String)combo01.getSelectedItem();
		if (comboMae.toString().trim().isEmpty()) {
			String proximoCodigoMae = "";
			if (comboAvo.trim().equals("ENTRADA")) {
				proximoCodigoMae = obterProximoCodigoMae(vetorEntradas.iterator());
			} else {
				proximoCodigoMae = obterProximoCodigoMae(vetorSaidas.iterator());
			}

			try {
				PlanoConta.incluir(new PlanoConta(0, comboAvo,
						                         proximoCodigoMae,
						      					 "",campoNomeConta.getText(), "",
						      					 acant.getPessoa().getCod()));

				campoNomeConta.setText("");
				setVisible(false);
				acant.getPlanoConta().setVisible(false);

				acant.getPessoa().setPlanoConta(PlanoConta.obterPlanoContas(acant.getPessoa().getCod()));
				acant.setPessoa(acant.getPessoa());

				acant.setPlanoConta(new JanelaInternaPlanoConta(acant));
				acant.getPainelCentral().add(acant.getPlanoConta());
				acant.getPlanoConta().setVisible(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			String proximoCodigoFilha = "";
			if (comboAvo.trim().equals("ENTRADA")) {
				proximoCodigoFilha = obterProximoCodigoFilha(vetorEntradas.iterator());
			} else {
				proximoCodigoFilha = obterProximoCodigoFilha(vetorSaidas.iterator());
			}
			
			try {
				PlanoConta.incluir(new PlanoConta(0, comboAvo,
						                         comboMae.getTipoLancamentoMae(),
						      					 proximoCodigoFilha,
						      					 campoNomeConta.getText(), "",
						      					 acant.getPessoa().getCod()));

				campoNomeConta.setText("");
				setVisible(false);
				acant.getPlanoConta().setVisible(false);

				acant.getPessoa().setPlanoConta(PlanoConta.obterPlanoContas(acant.getPessoa().getCod()));
				acant.setPessoa(acant.getPessoa());

				acant.setPlanoConta(new JanelaInternaPlanoConta(acant));
				acant.getPainelCentral().add(acant.getPlanoConta());
				acant.getPlanoConta().setVisible(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String obterProximoCodigoMae(Iterator<PlanoConta> iterPlanoConta) {
		int maior = 0;
		while (iterPlanoConta.hasNext()) {
			PlanoConta pc = iterPlanoConta.next();
			if (!pc.getTipoLancamentoMae().trim().isEmpty()) {
				int num = Integer.parseInt(pc.getTipoLancamentoMae());
				if (num >= maior) {
					maior = num;
				}
			}
		}
		maior += 1;
		if (maior < 10)
			return "0"+maior;
		return ""+maior;
	}

	private String obterProximoCodigoFilha(Iterator<PlanoConta> iterPlanoConta) {
		int maior = 0;
		while (iterPlanoConta.hasNext()) {
			PlanoConta pc = iterPlanoConta.next();
			if (pc.getTipoLancamentoMae().trim().equals(((PlanoConta)combo02.getSelectedItem()).getTipoLancamentoMae())) {
				if (!pc.getTipoLancamentoFilha().trim().isEmpty()) {
					int num = Integer.parseInt(pc.getTipoLancamentoFilha());
					if (num >= maior) {
						maior = num;
					}
				}
			}
		}
		maior += 1;
		if (maior < 10)
			return "0"+maior;
		return ""+maior;
	}

	private void carregarCombo02() {
		Iterator<PlanoConta> iterPlanoConta = acant.getPessoa().getPlanoConta().iterator();
		String itemSelecionado = (String)combo01.getSelectedItem();
		combo02.removeAllItems();
		combo02.addItem(new PlanoConta());
		while (iterPlanoConta.hasNext()) {
			PlanoConta pc = iterPlanoConta.next();
			if (itemSelecionado.trim().equals(pc.getTipoLancamentoAvo().trim()) &&
					pc.getTipoLancamentoFilha().trim().equals("")) {
				combo02.addItem(pc);
			}
		}
	}

	private void carregarCombo03() {
		if (combo02.getSelectedItem() != null) {
			PlanoConta pcs = (PlanoConta)combo02.getSelectedItem();
			if (combo03 != null) {
				combo03.removeAllItems();
				Iterator<PlanoConta> iterPlanoConta = acant.getPessoa().getPlanoConta().iterator();
				while (iterPlanoConta.hasNext()) {
					PlanoConta pc = iterPlanoConta.next();
					if (pc.getTipoLancamentoAvo().trim().equals(pcs.getTipoLancamentoAvo().trim()) &&
							pc.getTipoLancamentoMae().trim().equals(pcs.getTipoLancamentoMae().trim()) &&
							!pc.getTipoLancamentoFilha().trim().isEmpty()) {
						combo03.addItem(new PlanoConta(pc.getCod(),
													   pc.getTipoLancamentoAvo(),
													   pc.getTipoLancamentoMae(),
													   pc.getTipoLancamentoFilha(),
													   pc.getNomePlanoConta().trim(),
													   pc.getData(),
													   pc.getCodPessoa()));
					}
				}
			}
		}
	}
}
