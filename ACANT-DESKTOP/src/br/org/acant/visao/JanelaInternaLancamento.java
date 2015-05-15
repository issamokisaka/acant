package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import br.org.acant.modelo.Lancamento;

public class JanelaInternaLancamento extends JanelaInternaMae
implements ActionListener {
	private static final long serialVersionUID = 1L;

	protected String codigo;
	protected String planoConta;
	protected int codPlanoConta;

	private JButton bIncluir;
	private JButton bEditar;
	private JButton bRemover;
	private JPanel painelBotoes;

	private JPanel painelVoltar;
	private JButton bVoltar;

	private JToolBar barraFerramentas;

	protected JTable tabela;
	protected DefaultTableModel tableModel;

	private int indiceLancamentoSelecionado = -1;
	protected Vector<Integer> arrayCodLancamento;

	public JanelaInternaLancamento(JanelaPrincipal acant, String planoConta, int codPlanoConta) {
		super("Lançamentos", acant);

		this.planoConta = planoConta;
		this.codPlanoConta = codPlanoConta;

		setSize(acant.getPainelCentral().getSize());
		setLocation(0, 0);
		setMaximizable(true);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel painelRotulo = new JPanel();
		painelRotulo.setLayout(new FlowLayout(FlowLayout.RIGHT));
		painelRotulo.add(new JLabel("Nome da conta: " + planoConta));
		add(painelRotulo, BorderLayout.NORTH);

		painelVoltar = new JPanel();
		painelVoltar.setLayout(new FlowLayout(FlowLayout.LEFT));

		bVoltar = new JButton();
		bVoltar.setMargin(new Insets(0,0,0,0));
		bVoltar.setToolTipText("Voltar");
		bVoltar.setIcon(new ImageIcon(getClass().getResource("/img/voltar.png")));
		bVoltar.addActionListener(this);
		painelVoltar.add(bVoltar);


		painelBotoes = new JPanel();
		painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));

		bIncluir = new JButton();
		bIncluir.setMargin(new Insets(0,0,0,0));
		bIncluir.setToolTipText("Incluir novo lançamento");
		bIncluir.setIcon(new ImageIcon(getClass().getResource("/img/adicionar.png")));
		bIncluir.addActionListener(this);
		painelBotoes.add(bIncluir);

		bEditar = new JButton();
		bEditar.setMargin(new Insets(0,0,0,0));
		bEditar.setToolTipText("Editar lançamento");
		bEditar.setIcon(new ImageIcon(getClass().getResource("/img/editar.png")));
		bEditar.addActionListener(this);
		painelBotoes.add(bEditar);

		bRemover = new JButton();
		bRemover.setMargin(new Insets(0,0,0,0));
		bRemover.setToolTipText("Excluir lançamento");
		bRemover.setIcon(new ImageIcon(getClass().getResource("/img/remover.png")));
		bRemover.addActionListener(this);
		painelBotoes.add(bRemover);

		barraFerramentas = new JToolBar();
		barraFerramentas.setFloatable(false);
		barraFerramentas.add(painelVoltar);
		barraFerramentas.add(painelBotoes);
		add(barraFerramentas, BorderLayout.SOUTH);

		tableModel = new DefaultTableModel();
		tableModel.addColumn("Descrição Lançamento");
		tableModel.addColumn("Data Lanç.");
		tableModel.addColumn("Unid.");
		tableModel.addColumn("Qtd");
		tableModel.addColumn("V. Unitário");
		tableModel.addColumn("V. Total");

		tabela = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabela.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tabela.getTableHeader().setResizingAllowed(false);

		tabela.getColumnModel().getColumn(0).setPreferredWidth(180);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(40);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(60);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(100);

		try {
			Iterator<Lancamento> iterLancamentos =
					Lancamento.obterLancamentos(this.codPlanoConta).iterator();
			arrayCodLancamento = new Vector<Integer>();
			int i = 0;
			while (iterLancamentos.hasNext()) {
				Lancamento lancamento = iterLancamentos.next();
				tableModel.addRow(new Object[]{lancamento.getDescricao(),
											   lancamento.getData(),
											   lancamento.getUnidadeMedida(),
											   lancamento.getQuantidadeItens(),
											   lancamento.getValorUnitarioItem(),
											   lancamento.getValorTotal()});
				arrayCodLancamento.add(i, new Integer(lancamento.getCod()));
				i++;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		final JTable tb = tabela;
		tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				indiceLancamentoSelecionado = tb.getSelectedRow();
			}
		});

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					openWindowLancamentoUpdate();
				}
			}
		});
		tabela.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					openWindowLancamentoUpdate();
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});

		final JanelaInternaMae jif = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				jif.setVisible(false);
				jif.acant.getPlanoConta().setVisible(true);
			}
		});
	}

	private void openWindowLancamentoUpdate() {
		String desc = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 0);
		String data = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 1);
		String unidade = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 2);
		String qtd = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 3);
		String vu = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 4);
		String vt = (String)tableModel.getValueAt(indiceLancamentoSelecionado, 5);

		JanelaInternaLancamentoCadastro janelaInternaLancamentoCadastro = null;
		if (unidade.trim().equals("")) {
			janelaInternaLancamentoCadastro = new JanelaInternaLancamentoCadastro(acant,this,this.codPlanoConta,
					new Lancamento(String.valueOf(arrayCodLancamento.get(
							tabela.getSelectedRow())), desc, data, "",
							"0", "0,00", vt, String.valueOf(codPlanoConta)));
		} else {
			janelaInternaLancamentoCadastro = new JanelaInternaLancamentoCadastro(acant,this,this.codPlanoConta,
					new Lancamento(String.valueOf(arrayCodLancamento.get(
							tabela.getSelectedRow())), desc, data, unidade,
							qtd, vu, vt, String.valueOf(codPlanoConta)));
		}
		acant.getPainelCentral().add(janelaInternaLancamentoCadastro);
		janelaInternaLancamentoCadastro.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == bVoltar) {
			setVisible(false);
			acant.getPlanoConta().setVisible(true);
		} else if (evt.getSource() == bIncluir) {
			JanelaInternaLancamentoCadastro janelaInternaLancamentoCadastro =
					new JanelaInternaLancamentoCadastro(acant,this,this.codPlanoConta);
			acant.getPainelCentral().add(janelaInternaLancamentoCadastro);
			janelaInternaLancamentoCadastro.setVisible(true);

			tabela.clearSelection();
		} else if (evt.getSource() == bEditar) {
			if (indiceLancamentoSelecionado != -1 && tabela.isRequestFocusEnabled()) {
				openWindowLancamentoUpdate();
			} else {
				JOptionPane.showInternalMessageDialog(this,
						"Nenhum lançamento foi selecionado.");
			}
		} else if (evt.getSource() == bRemover) {
			if (indiceLancamentoSelecionado != -1 && tabela.isRequestFocusEnabled()) {

				int retornoJanela = JOptionPane.showConfirmDialog(this,
								"Tem certeza que deseja excluir o lançamento selecionado?",
								"Remoção de lançamento",
								JOptionPane.YES_NO_OPTION);
				if (retornoJanela == JOptionPane.YES_OPTION) {
					int codLancamento =
							arrayCodLancamento.get(indiceLancamentoSelecionado).intValue();
					try {
						int retorno = Lancamento.remover(codLancamento);
						if (retorno == 1) {
							arrayCodLancamento.removeElementAt(indiceLancamentoSelecionado);
							tableModel.removeRow(indiceLancamentoSelecionado);
	
							indiceLancamentoSelecionado = -1;
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
						System.out.println(e1.getMessage());
					}
				}
			} else {
				JOptionPane.showInternalMessageDialog(this,
						"Nenhum lançamento foi selecionado para remoção.");
			}
		}
	}
}
