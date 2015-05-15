package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import br.org.acant.modelo.Pessoa;
import br.org.acant.modelo.PlanoConta;

public class JanelaInternaPlanoConta extends JanelaInternaMae
	implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton bIncluir;
	private JButton bEditar;
	private JButton bRemover;
	private JButton bImprimir;

	private JPanel painelBotoes;
	private JToolBar barraFerramentas;

	protected JTable tabela;
	protected DefaultTableModel tableModel;

	private Vector<PlanoConta> vetorEntradas;
	private Vector<PlanoConta> vetorSaidas;

	public JanelaInternaPlanoConta(JanelaPrincipal acant) {
		super("Plano de Contas",acant);

		acant.getLabelBarraStatus().setText("Agricultor: " + acant.getPessoa().getNome());

		setSize(acant.getPainelCentral().getSize());
		setLocation(0, 0);
		setMaximizable(false);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		painelBotoes = new JPanel();
		painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));

		bIncluir = new JButton();
		bIncluir.setMargin(new Insets(0,0,0,0));
		bIncluir.setToolTipText("Incluir nova conta");
		bIncluir.setIcon(new ImageIcon(getClass().getResource("/img/adicionar.png")));
		bIncluir.addActionListener(this);
		painelBotoes.add(bIncluir);

		bEditar = new JButton();
		bEditar.setMargin(new Insets(0,0,0,0));
		bEditar.setToolTipText("Editar conta");
		bEditar.setIcon(new ImageIcon(getClass().getResource("/img/editar.png")));
		bEditar.addActionListener(this);
		painelBotoes.add(bEditar);

		bRemover = new JButton();
		bRemover.setMargin(new Insets(0,0,0,0));
		bRemover.setToolTipText("Excluir conta");
		bRemover.setIcon(new ImageIcon(getClass().getResource("/img/remover.png")));
		bRemover.addActionListener(this);
		painelBotoes.add(bRemover);

		bImprimir = new JButton();
		bImprimir.setMargin(new Insets(0,0,0,0));
		bImprimir.setToolTipText("Exibir fluxo de caixa");
		bImprimir.setIcon(new ImageIcon(getClass().getResource("/img/imprimir.png")));
		bImprimir.addActionListener(this);
		painelBotoes.add(bImprimir);

		barraFerramentas = new JToolBar();
		barraFerramentas.setFloatable(false);
		barraFerramentas.add(painelBotoes);
		add(barraFerramentas, BorderLayout.SOUTH);

		tableModel = new DefaultTableModel();
		tabela = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (column == 0) {
					c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
					c.setForeground(Color.BLUE);
				} else {
					c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
					c.setForeground(Color.RED);
				}
				return c;
			}
		};
		tabela.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		Pessoa pessoa = acant.getPessoa();
		Iterator<PlanoConta> iterPlanoConta = pessoa.getPlanoConta().iterator();

		vetorEntradas = new Vector<PlanoConta>();
		vetorSaidas = new Vector<PlanoConta>();

		while(iterPlanoConta.hasNext()) {
			PlanoConta pc = iterPlanoConta.next();

			if (pc.getTipoLancamentoAvo().trim().equals("ENTRADA")) {
				if (pc.getTipoLancamentoMae().trim().equals("")) {
					continue;
				} else if (pc.getTipoLancamentoFilha().trim().equals("")) {
					vetorEntradas.add(pc);
				} else {
					pc.setNomePlanoConta("     "+pc.getNomePlanoConta());
					vetorEntradas.add(pc);
				}
			} else {
				if (pc.getTipoLancamentoMae().trim().equals("")) {
					continue;
				} else if (pc.getTipoLancamentoFilha().trim().equals("")) {
					vetorSaidas.add(pc);
				} else {
					pc.setNomePlanoConta("     "+pc.getNomePlanoConta());
					vetorSaidas.add(pc);
				}
			}
		}

		tableModel.addColumn("ENTRADAS", vetorEntradas);
		tableModel.addColumn("SAIDAS", vetorSaidas);

		tabela.getTableHeader().setResizingAllowed(false);
//		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tabela.setColumnSelectionAllowed(true);
		tabela.setRowSelectionAllowed(true);

		tabela.getColumnModel().getColumn(0).setPreferredWidth(250);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(250);

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setAutoscrolls(true);
		add(scrollPane, BorderLayout.CENTER);

		tabela.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					abrirJanelaLancamento();
				}
			}
		});
		tabela.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					abrirJanelaLancamento();
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
				jif.acant.getLabelBarraStatus().setText(" ");
				jif.setVisible(false);

				jif.acant.botaoCadastrar.setEnabled(true);
				jif.acant.botaoLogar.setEnabled(true);
			}
		});
	}

	private void abrirJanelaLancamento() {
		Object objeto = null;

		if (tabela.getSelectedRow() != -1 && tabela.getSelectedColumn() != -1) {
			objeto = tableModel.getValueAt(tabela.getSelectedRow(),
					tabela.getSelectedColumn());
		}

		if (objeto != null) {
			PlanoConta pcs = (PlanoConta)objeto;

			int retorno = 0;
			if (pcs.getTipoLancamentoAvo().trim().equals("ENTRADA")) {
				retorno = existePCFilhas(pcs, vetorEntradas.iterator());
			} else {
				retorno = existePCFilhas(pcs, vetorSaidas.iterator());
			}

			if (retorno == 0) {
				JOptionPane.showInternalMessageDialog(this,
						"Não é possível fazer lançamentos nesta conta.");
			} else {
				JanelaInternaLancamento janelaInternaLancamento =
						new JanelaInternaLancamento(acant,
												    pcs.getNomePlanoConta(),
												    pcs.getCod());
				acant.getPainelCentral().add(janelaInternaLancamento);
				janelaInternaLancamento.setVisible(true);

				setVisible(false);
			}
		}
	}

	private int existePCFilhas(PlanoConta pcs, Iterator<PlanoConta> iterPC) {
		while (iterPC.hasNext()) {
			PlanoConta pcIter = iterPC.next();
			if (pcs.getTipoLancamentoMae().trim().equals(pcIter.getTipoLancamentoMae().trim()) &&
					pcs.getTipoLancamentoFilha().trim().equals("") &&
					pcs.getCod() != pcIter.getCod()) {
				return 0;
			}
		}
		return pcs.getCod();
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == bIncluir) {
			JanelaInternaPlanoContaCadastro janelaInternaPlanoContaCadastro =
					new JanelaInternaPlanoContaCadastro(acant, vetorEntradas, vetorSaidas);
			acant.getPainelCentral().add(janelaInternaPlanoContaCadastro);
			janelaInternaPlanoContaCadastro.setVisible(true);
		} else if (evt.getSource() == bEditar) {
			if (tabela.getSelectedRow() != -1) {
				Object obj = tableModel.getValueAt(tabela.getSelectedRow(),
						tabela.getSelectedColumn());
				if (obj != null) {
					PlanoConta pcs = (PlanoConta)obj;
					JanelaInternaPlanoContaAtualizacao janelaInternaPlanoContaAtualizacao =
							new JanelaInternaPlanoContaAtualizacao(acant, pcs, tabela);
					janelaInternaPlanoContaAtualizacao.janelaInternaMae = this;
					acant.getPainelCentral().add(janelaInternaPlanoContaAtualizacao);
					janelaInternaPlanoContaAtualizacao.setVisible(true);
				}
			} else {
				JOptionPane.showInternalMessageDialog(this,
						"Nenhuma conta foi selecionada para edição.");
			}
		} else if (evt.getSource() == bRemover) {
			if (tabela.getSelectedRow() != -1) {
				Object objeto = tableModel.getValueAt(tabela.getSelectedRow(),
						tabela.getSelectedColumn());
				if (objeto != null) { 		
					PlanoConta pcs = (PlanoConta)objeto;

					int retorno = 0;
					if (pcs.getTipoLancamentoAvo().trim().equals("ENTRADA")) {
						retorno = existePCFilhas(pcs, vetorEntradas.iterator());
					} else {
						retorno = existePCFilhas(pcs, vetorSaidas.iterator());
					}

					if (retorno == 0) {
						JOptionPane.showInternalMessageDialog(this,
								"Esta conta não pode ser removida, " +
								"pois existem contas filhas desta");
					} else {
						int ret = JOptionPane.showInternalConfirmDialog(this,
								"Remover esta conta?", "Acant", JOptionPane.YES_NO_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							acant.getPlanoConta().setVisible(false);

							try {
								PlanoConta.removerPlanoConta(retorno);
								acant.getPessoa().setPlanoConta(
										PlanoConta.obterPlanoContas(
												acant.getPessoa().getCod()));
							} catch (SQLException e) {
								e.printStackTrace();
							}

							acant.setPessoa(acant.getPessoa());
							acant.setPlanoConta(new JanelaInternaPlanoConta(acant));
							acant.getPainelCentral().add(acant.getPlanoConta());
							acant.getPlanoConta().setVisible(true);
						}
					}
				}
			} else {
				JOptionPane.showInternalMessageDialog(this,
						"Nenhuma conta foi selecionada para remoção.");
			}
		} else if (evt.getSource() == bImprimir) {
			JanelaInternaPeriodoPesquisa janelaInternaPeriodoPesquisa =
					new JanelaInternaPeriodoPesquisa(acant);
			acant.getPainelCentral().add(janelaInternaPeriodoPesquisa);
			janelaInternaPeriodoPesquisa.setVisible(true);
		}
	}
}
