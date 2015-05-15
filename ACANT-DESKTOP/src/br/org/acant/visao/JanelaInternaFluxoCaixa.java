package br.org.acant.visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import br.org.acant.modelo.FluxoCaixa;
import br.org.acant.modelo.Lancamento;
import br.org.acant.modelo.PlanoConta;
import br.org.acant.util.Utilitario;

public class JanelaInternaFluxoCaixa extends JanelaInternaMae {
	private static final long serialVersionUID = 1L;

	private String dataInicial;
	private String dataFinal;

	private JTable tabela;
	protected DefaultTableModel tableModel;

	public JanelaInternaFluxoCaixa(JanelaPrincipal acant,
								   String dataInicial,
								   String dataFinal) {
		super("Fluxo de caixa", acant);

		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;

		setSize(acant.getPainelCentral().getSize());
		setLocation(0, 0);
		setMaximizable(true);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());

		tableModel = new DefaultTableModel();
		tableModel.addColumn("Data");
		tableModel.addColumn("Conta");
		tableModel.addColumn("Descrição");
		tableModel.addColumn("$ Recebido");
		tableModel.addColumn("$ Pago");
		tableModel.addColumn("Saldo");
		
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
					if (tableModel.getValueAt(row, column) != null) {
						String valor = (String)tableModel.getValueAt(row, column);
						if (valor.contains("/")) {
							c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
							c.setForeground(Color.BLACK);
						}
					}
				} else if (column == 4) {
					c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
					c.setForeground(Color.RED);
				} else if (column == 5) {
					if (tableModel.getValueAt(row, column) != null) {
						String valor = (String)tableModel.getValueAt(row, column);
						if (valor.contains("-")) {
							c.setFont(new Font(Font.SANS_SERIF, Font.BOLD , 12));
							c.setForeground(Color.RED);
						} else {
							c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN , 12));
							c.setForeground(Color.BLACK);
						}
					}
				} else {
					c.setFont(new Font(Font.SANS_SERIF, Font.PLAIN , 12));
					c.setForeground(Color.BLACK);
				}

				return c;
			}
		};

		tabela.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.getTableHeader().setResizingAllowed(false);
//		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(250);

		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
		tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);

		Iterator<PlanoConta> iterPlanoConta = acant.getPessoa().getPlanoConta().iterator();
		Vector<FluxoCaixa> vetorFC = new Vector<FluxoCaixa>();
		while (iterPlanoConta.hasNext()) {
			PlanoConta pc = iterPlanoConta.next();
			Iterator<Lancamento> iterTodosLancamentos = null;

			try {
				iterTodosLancamentos = Lancamento.obterLancamentos(pc.getCod()).iterator();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			while (iterTodosLancamentos.hasNext()) {
				Lancamento lanc = iterTodosLancamentos.next();
				
				if (pc.getTipoLancamentoAvo().trim().equalsIgnoreCase("ENTRADA")) {
					vetorFC.add(
							new FluxoCaixa(Utilitario.obterObjetoDataPadrao(lanc.getData()),
  										   pc.getNomePlanoConta(),
  										   lanc.getDescricao(),
  										   lanc.getValorTotal(),
  										   "0,00",
						                   ""));
				} else if (pc.getTipoLancamentoAvo().trim().equalsIgnoreCase("SAIDA")) {
					vetorFC.add(
							new FluxoCaixa(Utilitario.obterObjetoDataPadrao(lanc.getData()),
										   pc.getNomePlanoConta(),
										   lanc.getDescricao(),
										   "0,00",
										   lanc.getValorTotal(),
										   ""));
				}
			}
		}

		montarTabelaFC(vetorFC);

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);
		
		final JanelaInternaMae jim = this;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				jim.setVisible(false);
			}
		});
	}

	private void montarTabelaFC(Vector<FluxoCaixa> vetorFC) {
//		Ordena coleção por data
		Collections.sort(vetorFC);

		float valorRecebidoTotal = 0.00f;
		float valorPagoTotal = 0.00f;

		float saldo = 0.00f;

		int dataInicialInt = Utilitario.obterDataFormatoInvertido(dataInicial);
		int dataFinalInt = Utilitario.obterDataFormatoInvertido(dataFinal);

		int dataInt;

		String data = "";
		String dataAnterior = "";
		for (int i = 0; i < vetorFC.size(); i++) {
			FluxoCaixa fc = vetorFC.get(i);

			data = new SimpleDateFormat("dd/MM/yyyy").format(fc.getData()).trim();

			if (dataAnterior.trim().isEmpty()) {
				dataInt = Utilitario.obterDataFormatoInvertido(data);
				if ((dataInt >= dataInicialInt) && (dataInt <= dataFinalInt)) {
					tableModel.addRow(new Object[]{data,
							fc.getConta().trim(),
							fc.getLancamento(),
							fc.getValorRecebido(),
							fc.getValorPago(),
							fc.getSaldo()});
				}
			} else if (data.equals(dataAnterior)) {
				dataInt = Utilitario.obterDataFormatoInvertido(data);
				if ((dataInt >= dataInicialInt) && (dataInt <= dataFinalInt)) {
					tableModel.addRow(new Object[]{"",
							fc.getConta().trim(),
							fc.getLancamento(),
							fc.getValorRecebido(),
							fc.getValorPago(),
							fc.getSaldo()});
				}
			} else {
				dataInt = Utilitario.obterDataFormatoInvertido(dataAnterior);
				saldo = (saldo + valorRecebidoTotal) - valorPagoTotal;
				if ((dataInt >= dataInicialInt) && (dataInt <= dataFinalInt)) {
					imprimirLinhaTotalizacao(valorRecebidoTotal,
											 valorPagoTotal,
											 saldo);
				}

				valorRecebidoTotal = 0.00f;
				valorPagoTotal = 0.00f;

				dataInt = Utilitario.obterDataFormatoInvertido(data);
				if ((dataInt >= dataInicialInt) && (dataInt <= dataFinalInt)) {
					tableModel.addRow(new Object[]{data,
							fc.getConta().trim(),
							fc.getLancamento(),
							fc.getValorRecebido(),
							fc.getValorPago(),
							fc.getSaldo()});
				}
			}
			valorRecebidoTotal += transformarValor(fc.getValorRecebido());
			valorPagoTotal += transformarValor(fc.getValorPago());

			dataAnterior = data.trim();
		}
		dataInt = Utilitario.obterDataFormatoInvertido(dataAnterior);
		saldo = (saldo + valorRecebidoTotal) - valorPagoTotal;
		if ((dataInt >= dataInicialInt) && (dataInt <= dataFinalInt)) {
			imprimirLinhaTotalizacao(valorRecebidoTotal,
									 valorPagoTotal,
									 saldo);
		}
	}

	private void imprimirLinhaTotalizacao(float valorRecebido, float valorPago, float saldo) {
		tableModel.addRow(new Object[]{"",
									   "",
									   "",
									   Utilitario.formatarParaDecimal(valorRecebido),
									   Utilitario.formatarParaDecimal(valorPago),
									   Utilitario.formatarParaDecimal(saldo)});
		tableModel.addRow(new Object[6]);
	}

	private float transformarValor(String valor) {
		return Float.parseFloat(valor.replace(".", "").replace(",","."));
	}
}
