package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.MaskFormatter;

import br.org.acant.modelo.Lancamento;

public class JanelaInternaLancamentoCadastro extends JanelaInternaMae
										   	 implements ActionListener, FocusListener {
	private static final long serialVersionUID = 1L;

	private JLabel descricao;
	private JTextField campoDescricao;
	private JLabel data;
	private JFormattedTextField campoData;
	private JLabel unidade;
	private JTextField campoValorTotal01;
	private JTextField campoValorTotalParteDecimal01;

	private JCheckBox checkBox;

	private JTextField campoUnidade;
	private JLabel quantidade;
	private JTextField campoQuantidade;
	private JLabel valorUnitario;
	private JTextField campoValorUnitario;
	private JTextField campoValorUnitarioParteDecimal;
	private JLabel valorTotal;
	private JTextField campoValorTotal02;
	private JTextField campoValorTotalParteDecimal02;
	private JPanel painelCentral;

	private JButton bCalcular;
	private JButton bIncluirAlterar;
	private JButton bCancelar;
	private JPanel painelInferior;

	protected int codPlanoConta;
	private Lancamento lanc;

	public JanelaInternaLancamentoCadastro(JanelaPrincipal acant,
			JanelaInternaMae janelaMae,
			int codPlanoConta) {
		super("Incluir Lançamento", acant);

		this.janelaInternaMae = janelaMae;
		this.codPlanoConta = codPlanoConta;

		this.lanc = new Lancamento();

		initComponents("Incluir", 'I');
	}

	public JanelaInternaLancamentoCadastro(JanelaPrincipal acant,
			JanelaInternaMae janelaMae,
			int codPlanoConta,
			Lancamento lanc) {
		super("Alterar Lançamento", acant);

		this.janelaInternaMae = janelaMae;
		this.codPlanoConta = codPlanoConta;

		this.lanc = lanc;

		initComponents("Alterar", 'A');
	}


	private void initComponents(String labelBotaoIncluirAlterar,
			char mnemonicBotaoIncluirAlterar) {
		setBounds(50, 50, 500, 320);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		GridLayout layoutCentral = new GridLayout(8, 2);
		layoutCentral.setHgap(3);
		layoutCentral.setVgap(3);
		painelCentral = new JPanel(layoutCentral);

		boolean habilitarDetalhamento = false;

		descricao = new JLabel("Descrição do lançamento:");
		painelCentral.add(descricao);
		campoDescricao = new JTextField(lanc.getDescricao());
		painelCentral.add(campoDescricao);
		if (!lanc.getDescricao().trim().equals("") &&
				!lanc.getUnidadeMedida().trim().equals("") &&
						!lanc.getQuantidadeItens().trim().equals("")) {

			habilitarDetalhamento = true;

		}


		data = new JLabel("Data do lançamento:");
		painelCentral.add(data);
		try {
			campoData = new JFormattedTextField(new MaskFormatter("##/##/####"));
			campoData.setText(lanc.getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		painelCentral.add(campoData);


		valorTotal = new JLabel("Valor total do lançamento:");
		painelCentral.add(valorTotal);
		painelCentral.add(obterPainelCampoValorTotal01());
		if (habilitarDetalhamento) {
			campoValorTotal01.setEnabled(false);
			campoValorTotalParteDecimal01.setEnabled(false);
		}


		painelCentral.add(new JLabel(""));
		checkBox = new JCheckBox("Detalhamento");
		checkBox.addActionListener(this);
		checkBox.setSelected(habilitarDetalhamento);
		painelCentral.add(checkBox);


		unidade = new JLabel("Unidade de medida:");
		painelCentral.add(unidade);
		campoUnidade = new JTextFieldModificado(8);
		campoUnidade.setText(lanc.getUnidadeMedida());
		campoUnidade.setEnabled(habilitarDetalhamento);
		painelCentral.add(campoUnidade);


		quantidade = new JLabel("Quantidade de itens:");
		painelCentral.add(quantidade);
		campoQuantidade = new JTextFieldModificado(15, "0123456789");
		campoQuantidade.addFocusListener(this);
		campoQuantidade.setEnabled(habilitarDetalhamento);
		campoQuantidade.setHorizontalAlignment(JTextField.RIGHT);
		if (lanc.getQuantidadeItens().trim().equals("")) {
			campoQuantidade.setText("0");
		} else {
			campoQuantidade.setText(lanc.getQuantidadeItens());
		}
		painelCentral.add(campoQuantidade);


		valorUnitario = new JLabel("Valor unitário do item:");
		painelCentral.add(valorUnitario);
		painelCentral.add(obterPainelCampoValorUnitario());
		campoValorUnitario.setEnabled(habilitarDetalhamento);
		campoValorUnitarioParteDecimal.setEnabled(habilitarDetalhamento);


		valorTotal = new JLabel("Valor total do lançamento:");
		painelCentral.add(valorTotal);
		painelCentral.add(obterPainelCampoValorTotal02());
		add(painelCentral, BorderLayout.CENTER);


		painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		bCalcular = new JButton("Calcular");
		bCalcular.setMnemonic('u');
		bCalcular.addActionListener(this);
		bCalcular.setEnabled(habilitarDetalhamento);
		painelInferior.add(bCalcular);

		bIncluirAlterar = new JButton(labelBotaoIncluirAlterar);
		bIncluirAlterar.setMnemonic(mnemonicBotaoIncluirAlterar);
		bIncluirAlterar.addActionListener(this);
		if (habilitarDetalhamento) {
			bIncluirAlterar.setEnabled(false);
		}
		painelInferior.add(bIncluirAlterar);

		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic('C');
		bCancelar.addActionListener(this);
		painelInferior.add(bCancelar);

		add(painelInferior, BorderLayout.SOUTH);

		final JanelaInternaMae janela = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				janela.setVisible(false);
				limparCampos();
			}
		});
	}

	private String[] separarInteiroDeDecimal(String campo) {
		StringTokenizer st = new StringTokenizer(campo, ",");
		String[] partes = new String[]{st.nextToken(),st.nextToken()};
		return partes;
	}

	private JPanel obterPainelCampoValorTotal01() {
		JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		String[] partesValorTotal = null;
		campoValorTotal01 = new JTextFieldModificado(170, 20, "0123456789");
		campoValorTotal01.setHorizontalAlignment(JTextField.RIGHT);
		if (!lanc.getValorTotal().trim().equals("")) {
			partesValorTotal = separarInteiroDeDecimal(lanc.getValorTotal());
			campoValorTotal01.setText(partesValorTotal[0]);
		} else {
			campoValorTotal01.setText("0");
		}
		painel.add(campoValorTotal01);

		painel.add(new JLabel(","));

		campoValorTotalParteDecimal01 = new JTextFieldModificado(25, 2, "0123456789");
		campoValorTotalParteDecimal01.setHorizontalAlignment(JTextField.RIGHT);
		if (!lanc.getValorTotal().trim().equals("")) {
			campoValorTotalParteDecimal01.setText(partesValorTotal[1]);
		} else {
			campoValorTotalParteDecimal01.setText("00");
		}
		painel.add(campoValorTotalParteDecimal01);
		return painel;
	}

	private JPanel obterPainelCampoValorUnitario() {
		JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		String[] partesValorUnitario = null;
		campoValorUnitario = new JTextFieldModificado(170, 20, "0123456789");
		campoValorUnitario.setHorizontalAlignment(JTextField.RIGHT);
		campoValorUnitario.addFocusListener(this);
		campoValorUnitario.setEnabled(false);
		if (!lanc.getValorUnitarioItem().trim().equals("")) {
			partesValorUnitario = separarInteiroDeDecimal(lanc.getValorUnitarioItem());
			campoValorUnitario.setText(partesValorUnitario[0]);
		} else {
			campoValorUnitario.setText("0");
		}
		painel.add(campoValorUnitario);

		painel.add(new JLabel(","));

		campoValorUnitarioParteDecimal = new JTextFieldModificado(25, 2, "0123456789");
		campoValorUnitarioParteDecimal.addFocusListener(this);
		campoValorUnitarioParteDecimal.setEnabled(false);
		if (!lanc.getValorUnitarioItem().trim().equals("")) {
			campoValorUnitarioParteDecimal.setText(partesValorUnitario[1]);
		} else {
			campoValorUnitarioParteDecimal.setText("00");
		}
		painel.add(campoValorUnitarioParteDecimal);
		return painel;
	}

	private JPanel obterPainelCampoValorTotal02() {
		JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		String[] partesValorTotal = null;
		campoValorTotal02 = new JTextFieldModificado(170, 20, "0123456789");
		campoValorTotal02.setHorizontalAlignment(JTextField.RIGHT);
		campoValorTotal02.setEditable(false);
		if (!lanc.getValorTotal().trim().equals("")) {
			partesValorTotal = separarInteiroDeDecimal(lanc.getValorTotal());
			campoValorTotal02.setText(partesValorTotal[0]);
		} else {
			campoValorTotal02.setText("0");
		}
		painel.add(campoValorTotal02);

		painel.add(new JLabel(","));

		campoValorTotalParteDecimal02 = new JTextFieldModificado(25, 2, "0123456789");
		campoValorTotalParteDecimal02.setEditable(false);
		if (!lanc.getValorTotal().trim().equals("")) {
			campoValorTotalParteDecimal02.setText(partesValorTotal[1]);
		} else {
			campoValorTotalParteDecimal02.setText("00");
		}
		painel.add(campoValorTotalParteDecimal02);
		return painel;
	}

	private boolean verificarCampos() {
		if (campoDescricao.getText().trim().equals("")) {
			imprimirMensagem(campoDescricao, "descrição do lançamento");
			return false;
		}

//      Se checkBox Detalhamento estiver marcado!!!
		if (checkBox.isSelected()) {
			if (campoUnidade.getText().trim().equals("")) {
				imprimirMensagem(campoUnidade, "unidade de medida");
				return false;
			}
			if (!verificarCamposValor()) {
				return false;
			}
		} else {
			if (campoValorTotal01.getText().trim().equals("") ||
					Integer.parseInt(campoValorTotal01.getText()) == 0) {
				imprimirMensagem(campoValorTotal01, "valor total do lançamento");
				return false;
			}	
		}

		return true;
	}

	private boolean verificarCamposValor() {
		if (campoQuantidade.getText().trim().equals("") ||
				Integer.parseInt(campoQuantidade.getText()) == 0) {
			imprimirMensagem(campoQuantidade, "quantidade de itens");
			return false;
		} else if (campoValorUnitario.getText().trim().equals("") ||
				Integer.parseInt(campoValorUnitario.getText()) == 0) {
			imprimirMensagem(campoValorUnitario, "valor unitário do item");
			return false;
		}
		return true;
	}

	private void imprimirMensagem(JTextField campo, String nomeCampo) {
		JOptionPane.showInternalMessageDialog(this,
				"Campo \""+nomeCampo+"\" é obrigatório.",
				"Atenção",
				JOptionPane.ERROR_MESSAGE);
		campo.requestFocus();
	}

	private void alterarEstadoCamposDetalhamento(boolean flag) {
		campoUnidade.setEnabled(flag);
		campoQuantidade.setEnabled(flag);
		campoValorUnitario.setEnabled(flag);
		campoValorUnitarioParteDecimal.setEnabled(flag);
		
		bCalcular.setEnabled(flag);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == checkBox) {
			if (checkBox.isSelected()) {
				alterarEstadoCamposDetalhamento(true);

				campoValorTotal01.setEnabled(false);
				campoValorTotalParteDecimal01.setEnabled(false);

				bIncluirAlterar.setEnabled(false);
			} else {
				alterarEstadoCamposDetalhamento(false);

				campoValorTotal01.setEnabled(true);
				campoValorTotalParteDecimal01.setEnabled(true);

				bIncluirAlterar.setEnabled(true);
			}
		} else if (ae.getSource() == bCalcular) {
			if (verificarCamposValor()) {
				int quantidade = Integer.parseInt(campoQuantidade.getText().trim());
				float valorUnit = Float.parseFloat(campoValorUnitario.getText().trim() + "." +
								  		campoValorUnitarioParteDecimal.getText().trim());
				double totalDouble = (quantidade * valorUnit);
				String totalFormatadoString =
						new DecimalFormat("000,000,000.00").format(totalDouble);
//				System.out.println("totalFormat = " + totalFormatadoString);

				String total = String.valueOf(Double.parseDouble(
						totalFormatadoString.replace(".","").replace(",",".")));
//				System.out.println("total = " + total);

				StringTokenizer st = new StringTokenizer(total,".");
				campoValorTotal02.setText(st.nextToken());
				campoValorTotalParteDecimal02.setText(st.nextToken());

				bIncluirAlterar.setEnabled(true);
			}
		} else if (ae.getSource() == bIncluirAlterar) {
			try {
				if (verificarCampos()) {
					JanelaInternaLancamento janelaMae =
							(JanelaInternaLancamento)this.janelaInternaMae;
					Lancamento lanc = new Lancamento();
					lanc.setDescricao(campoDescricao.getText());
					lanc.setData(campoData.getText());
					lanc.setUnidadeMedida(campoUnidade.getText());
					lanc.setQuantidadeItens(campoQuantidade.getText());


					if (campoValorUnitarioParteDecimal.getText().trim().equals("")) {
						campoValorUnitarioParteDecimal.setText("00");
					}
					String valorUnitario = campoValorUnitario.getText().trim() + "," +
							campoValorUnitarioParteDecimal.getText().trim();
					lanc.setValorUnitarioItem(valorUnitario);


					String valorTotal;
					if (!checkBox.isSelected()) {
						if (campoValorTotalParteDecimal01.getText().trim().equals("")) {
							campoValorTotalParteDecimal01.setText("00");
						}
						valorTotal = campoValorTotal01.getText().trim() + "," + 
								campoValorTotalParteDecimal01.getText().trim();
						lanc.setUnidadeMedida("");
						lanc.setQuantidadeItens("0");
						lanc.setValorUnitarioItem("0,00");
						lanc.setValorTotal("0,00");
					} else {
						valorTotal = campoValorTotal02.getText().trim() + "," + 
								campoValorTotalParteDecimal02.getText().trim();
					}
					lanc.setValorTotal(valorTotal);

					lanc.setCodPlanoConta(String.valueOf(this.codPlanoConta));

					int cod_lanc = 0;
					if (janelaMae.tabela.getSelectedRow() != -1) {
						cod_lanc = janelaMae.arrayCodLancamento.get(
								janelaMae.tabela.getSelectedRow());
						lanc.setCod(String.valueOf(cod_lanc));
						Lancamento.atualizar(lanc);

						int row = janelaMae.tabela.getSelectedRow();
						janelaMae.tableModel.setValueAt(campoDescricao.getText(), row, 0);
						janelaMae.tableModel.setValueAt(campoData.getText(), row, 1);

						if (checkBox.isSelected()) {
							janelaMae.tableModel.setValueAt(campoUnidade.getText(), row, 2);
							janelaMae.tableModel.setValueAt(campoQuantidade.getText(), row, 3);
							janelaMae.tableModel.setValueAt(valorUnitario, row, 4);
						} else {
							janelaMae.tableModel.setValueAt("", row, 2);
							janelaMae.tableModel.setValueAt("0", row, 3);
							janelaMae.tableModel.setValueAt("0,00", row, 4);
						}
						janelaMae.tableModel.setValueAt(valorTotal, row, 5);
					} else {
						cod_lanc = Lancamento.incluir(lanc);
						janelaMae.arrayCodLancamento.add(cod_lanc);

//						Se campo Detalhamento estiver com check, no formulario
						if (checkBox.isSelected()) {
							janelaMae.tableModel.addRow(new Object[]{campoDescricao.getText(),
									campoData.getText(), campoUnidade.getText(),
									campoQuantidade.getText(), valorUnitario,
									valorTotal});
						} else {
							janelaMae.tableModel.addRow(new Object[]{campoDescricao.getText(),
									campoData.getText(), "", "0", "0,00", valorTotal});
						}
					}
					setVisible(false);
					limparCampos();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (ae.getSource() == bCancelar) {
			setVisible(false);
			limparCampos();
		}
	}

	private void limparCampos() {
		campoDescricao.setText("");
		campoUnidade.setText("");
		campoQuantidade.setText("");
		campoValorTotal01.setText("0");
		campoValorTotalParteDecimal01.setText("00");
		campoValorUnitario.setText("0");
		campoValorUnitarioParteDecimal.setText("00");
		campoValorTotal02.setText("0");
		campoValorTotalParteDecimal02.setText("00");
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		if (arg0.getSource() == campoQuantidade ||
				arg0.getSource() == campoValorUnitarioParteDecimal ||
				arg0.getSource() == campoValorUnitario) {
			bIncluirAlterar.setEnabled(false);
		}
	}
	@Override
	public void focusLost(FocusEvent arg0) {}
}
