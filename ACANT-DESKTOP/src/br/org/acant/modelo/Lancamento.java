package br.org.acant.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import br.org.acant.persistencia.Conexao;
import br.org.acant.util.Utilitario;

public class Lancamento {
	private String cod;
	private String descricao;
	private String data;
	private String unidadeMedida;
	private String quantidadeItens;
	private String valorUnitarioItem;
	private String valorTotal;
	private String codPlanoConta;

	public Lancamento() {
		this.cod = "";
		this.descricao = "";
		this.data = Utilitario.obterDataAtual();
		this.unidadeMedida = "";
		this.quantidadeItens = "";
		this.valorUnitarioItem = "";
		this.valorTotal = "";
		this.codPlanoConta = "";
	}

	public Lancamento(String cod, String descricao, String data,
					  String unidadeMedida, String quantidadeItens,
					  String valorUnitarioItem, String valorTotal,
					  String codPlanoConta) {
		this.cod = cod;
		this.descricao = descricao;
		this.data = data;
		this.unidadeMedida = unidadeMedida;
		this.quantidadeItens = quantidadeItens;
		this.valorUnitarioItem = valorUnitarioItem;
		this.valorTotal = valorTotal;
		this.codPlanoConta = codPlanoConta;
	}
	
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getQuantidadeItens() {
		return quantidadeItens;
	}
	public void setQuantidadeItens(String quantidadeItens) {
		this.quantidadeItens = quantidadeItens;
	}

	public String getValorUnitarioItem() {
		return valorUnitarioItem;
	}
	public void setValorUnitarioItem(String valorUnitarioItem) {
		this.valorUnitarioItem = valorUnitarioItem;
	}

	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getCodPlanoConta() {
		return codPlanoConta;
	}
	public void setCodPlanoConta(String codPlanoConta) {
		this.codPlanoConta = codPlanoConta;
	}

	public static int incluir(Lancamento lancamento) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		int retorno = conn.incluirObjeto("INSERT INTO " + lancamento.getClass().getSimpleName() + " " +
						   "VALUES (null," +
						   "'" + lancamento.getDescricao() + "'," +
				 		   Utilitario.obterDataFormatoInvertido(lancamento.getData()) + "," +
				 		   "'" + lancamento.getUnidadeMedida() + "'," +
				 		   lancamento.getQuantidadeItens() + "," +
				 		   "'" + lancamento.getValorUnitarioItem().trim() + "'," +
				 		   "'" + lancamento.getValorTotal().trim() + "'," +
				 		   lancamento.getCodPlanoConta() + ")");
		int cod_lanc = 0;
		if (retorno == 1) {
			ResultSet rs = conn.obterRegistro(
					"SELECT cod_lancamento FROM "+ Lancamento.class.getSimpleName() +" "+
					"WHERE descricao_do_lancamento = '"+ lancamento.getDescricao() +"' AND "+
					"data_do_lancamento = "+ Utilitario.obterDataFormatoInvertido(lancamento.getData()) +" AND "+
					"unidade_de_medida = '"+ lancamento.getUnidadeMedida() +"' AND "+
					"quantidade_de_itens = '"+ lancamento.getQuantidadeItens() +"' AND "+
					"valor_unitario_do_item = '"+ lancamento.getValorUnitarioItem() +"' AND "+
					"valor_total_do_lancamento = '"+ lancamento.getValorTotal() +"' AND "+
					"cod_plano_conta = "+ lancamento.getCodPlanoConta());
			while (rs.next()) {
				cod_lanc = rs.getInt("cod_lancamento");
			}
		}
		conn.fecharConexao();
		return cod_lanc;
	}

	public static int remover(int codigoLancamento) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		int retorno = conn.excluirRegistro("DELETE FROM Lancamento WHERE " +
										   "cod_lancamento = " + codigoLancamento);
		conn.fecharConexao();
		return retorno;
	}

	public static Vector<Lancamento> obterLancamentos(int codPlanoConta) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		ResultSet rs = conn.obterRegistro("SELECT * FROM " + Lancamento.class.getSimpleName() + " " +
										  "WHERE cod_plano_conta = " + codPlanoConta);
		Vector<Lancamento> vetorLancamentos = new Vector<Lancamento>();
		while(rs.next()) {
			vetorLancamentos.add(new Lancamento(rs.getString("cod_lancamento"),
												rs.getString("descricao_do_lancamento"),
												Utilitario.obterDataFormatoPadrao(
														rs.getInt("data_do_lancamento")),
												rs.getString("unidade_de_medida"),
												rs.getString("quantidade_de_itens"),
												rs.getString("valor_unitario_do_item"),
												rs.getString("valor_total_do_lancamento"),
												rs.getString("cod_plano_conta")));
		}
		conn.fecharConexao();
		return vetorLancamentos;
	}

	public static Vector<Lancamento> obterLancamentosPorDatas(int codPlanoConta,
													  String dataInicial,
													  String dataFinal) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		int di = Utilitario.obterDataFormatoInvertido(dataInicial);
		int df = Utilitario.obterDataFormatoInvertido(dataFinal);
		ResultSet rs = conn.obterRegistro("SELECT * FROM " + Lancamento.class.getSimpleName() + " " +
										  "WHERE cod_plano_conta = " + codPlanoConta + " " +
										  "AND data_do_lancamento BETWEEN "+di+" AND "+df);
		Vector<Lancamento> vetorLancamentos = new Vector<Lancamento>();
		while(rs.next()) {
			vetorLancamentos.add(new Lancamento(rs.getString("cod_lancamento"),
												rs.getString("descricao_do_lancamento"),
												Utilitario.obterDataFormatoPadrao(
														rs.getInt("data_do_lancamento")),
												rs.getString("unidade_de_medida"),
												rs.getString("quantidade_de_itens"),
												rs.getString("valor_unitario_do_item"),
												rs.getString("valor_total_do_lancamento"),
												rs.getString("cod_plano_conta")));
		}
		conn.fecharConexao();
		return vetorLancamentos;
	}

	public static boolean existemLancamentos(int codPlanoConta) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		boolean retorno = conn.existemRegistros("SELECT * FROM " + Lancamento.class.getSimpleName() + " " +
												"WHERE cod_plano_conta = " + codPlanoConta);
		conn.fecharConexao();
		if (retorno)
			return true;
		return false;
	}

	public static int atualizar(Lancamento lanc) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		int retorno = conn.atualizar("UPDATE "+ Lancamento.class.getSimpleName() +" "+
									 "SET descricao_do_lancamento = '"+ lanc.getDescricao() +"',"+
									 "data_do_lancamento = "+ Utilitario.obterDataFormatoInvertido(lanc.getData()) +","+
									 "unidade_de_medida = '"+ lanc.getUnidadeMedida() +"',"+
									 "quantidade_de_itens = "+ lanc.getQuantidadeItens() +","+
									 "valor_unitario_do_item = '"+ lanc.getValorUnitarioItem() +"',"+
									 "valor_total_do_lancamento = '"+ lanc.getValorTotal() +"',"+
									 "cod_plano_conta = "+ lanc.getCodPlanoConta() +" "+
									 "WHERE cod_lancamento = "+ lanc.getCod());
		conn.fecharConexao();
		return retorno;
	}
}
