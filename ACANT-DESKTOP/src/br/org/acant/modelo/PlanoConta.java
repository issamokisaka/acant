package br.org.acant.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import br.org.acant.persistencia.Conexao;

public class PlanoConta {
	private int cod;
	private String tipoLancamentoAvo = "";
	private String tipoLancamentoMae = "";
	private String tipoLancamentoFilha = "";
	private String nomePlanoConta;
	private String data;
	private int codPessoa;

	private Vector<Lancamento> lancamentos;

	public static final String A = "ENTRADAS";
	public static final String B = "SAIDAS";

	public PlanoConta(int cod, String tipoLancamentoAvo,
					  String tipoLancamentoMae, String tipoLancamentoFilha,
					  String nomePlanoConta, String data, int codPessoa) {
		this.cod = cod;
		this.tipoLancamentoAvo = tipoLancamentoAvo;
		this.tipoLancamentoMae = tipoLancamentoMae;
		this.tipoLancamentoFilha = tipoLancamentoFilha;
		this.nomePlanoConta = nomePlanoConta;
		this.data = data;
		this.codPessoa = codPessoa;
	}

	public PlanoConta() {
		this.nomePlanoConta = "          ";
	}

	@Override
	public String toString() {
		return nomePlanoConta;
	}

	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getTipoLancamentoAvo() {
		return tipoLancamentoAvo;
	}
	public void setTipoLancamentoAvo(String tipoLancamentoAvo) {
		this.tipoLancamentoAvo = tipoLancamentoAvo;
	}

	public String getTipoLancamentoMae() {
		return tipoLancamentoMae;
	}
	public void setTipoLancamentoMae(String tipoLancamentoMae) {
		this.tipoLancamentoMae = tipoLancamentoMae;
	}

	public String getTipoLancamentoFilha() {
		return tipoLancamentoFilha;
	}
	public void setTipoLancamentoFilha(String tipoLancamentoFilha) {
		this.tipoLancamentoFilha = tipoLancamentoFilha;
	}

	public String getNomePlanoConta() {
		return nomePlanoConta;
	}
	public void setNomePlanoConta(String nomePlanoConta) {
		this.nomePlanoConta = nomePlanoConta;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public int getCodPessoa() {
		return codPessoa;
	}
	public void setCodPessoa(int codPessoa) {
		this.codPessoa = codPessoa;
	}
	
	public Vector<Lancamento> getLancamentos() {
		return lancamentos;
	}
	public void setLancamentos(Vector<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public int incluir() throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		String data = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());

		int retorno = conn.incluirObjeto("INSERT INTO "+ getClass().getSimpleName() +" VALUES " +
										 "(null,'"+ this.tipoLancamentoAvo +"'," +
										 "'"+ this.tipoLancamentoMae +"',"+
										 "'"+ this.tipoLancamentoFilha +"'," +
										 "'"+ this.nomePlanoConta +"',"+
										 "'"+ data +"',"+ this.getCodPessoa() +")");
		conn.fecharConexao();
		return retorno;
	}

	public static int incluir(PlanoConta planoConta) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		String data = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
		
		int retorno = conn.incluirObjeto("INSERT INTO "+ planoConta.getClass().getSimpleName() +" "+
				                         "VALUES (null,'"+ planoConta.tipoLancamentoAvo +"',"+
										 "'"+ planoConta.tipoLancamentoMae +"',"+
				                         "'"+ planoConta.tipoLancamentoFilha +"',"+
										 "'"+ planoConta.nomePlanoConta +"','"+ data +"',"+
				                         planoConta.getCodPessoa()+")");
		conn.fecharConexao();
		return retorno;
	}
	
	public static Vector<PlanoConta> obterPlanoContas(int codPessoa) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		String sql = "SELECT * FROM "+ PlanoConta.class.getSimpleName() +" "+
					 "WHERE cod_pessoa = "+ codPessoa +" "+
				     "ORDER BY tipo_lancamento_avo ASC, tipo_lancamento_mae ASC, "+
				     "tipo_lancamento_filha ASC";

		ResultSet rs = conn.obterRegistro(sql);
		PlanoConta planoConta = null;
		Vector<PlanoConta> vetorPlanoConta = new Vector<PlanoConta>();
		while(rs.next()) {
			planoConta = new PlanoConta(rs.getInt("cod_plano_conta"),
					                    rs.getString("tipo_lancamento_avo"),
					                    rs.getString("tipo_lancamento_mae"),
					                    rs.getString("tipo_lancamento_filha"),
					                    rs.getString("nome_plano_conta"),
					                    rs.getString("data"),
					                    rs.getInt("cod_pessoa"));
			vetorPlanoConta.add(planoConta);
		}

		conn.fecharConexao();
		return vetorPlanoConta;
	}

	public static int remover(int codPessoa, String[] codPlanoConta) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		String sql = "DELETE FROM " + PlanoConta.class.getSimpleName() + " " +
					 "WHERE tipo_lancamento_avo = '" + codPlanoConta[0] + "' " +
					 "AND tipo_lancamento_mae = '" + codPlanoConta[1] + "' " +
					 "AND tipo_lancamento_filha = '" + codPlanoConta[2] + "' " +
					 "AND cod_pessoa = " + codPessoa;

		int ret = conn.excluirRegistro(sql);		
		conn.fecharConexao();
		return ret;
	}
	
	public static int removerPlanoConta(int codPC) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		String sql = "DELETE FROM " + PlanoConta.class.getSimpleName() + " " +
					 "WHERE cod_plano_conta = " + codPC;

		int ret = conn.excluirRegistro(sql);		
		conn.fecharConexao();
		return ret;
	}

	public static int atualizar(int cod,
			String tipoLancamentoAvo,
			String nomePlano) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		String sql = "UPDATE " + PlanoConta.class.getSimpleName() + " " +
					 "SET tipo_lancamento_avo = '" + tipoLancamentoAvo + "', " +
					 "nome_plano_conta = '" + nomePlano + "' " +
					 "WHERE cod_plano_conta = " + cod;

		int ret = conn.atualizar(sql);
		conn.fecharConexao();
		return ret;
	}

	public static int atualizar(PlanoConta pc) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

//		String sql = "UPDATE " + PlanoConta.class.getSimpleName() + " " +
//					 "SET tipo_lancamento_avo = '" + pc.getTipoLancamentoAvo() + "', " +
//					 "nome_plano_conta = '" + pc.getNomePlanoConta() + "' " +
//					 "WHERE cod_plano_conta = " + pc.getCod();

		String sql = "UPDATE " + PlanoConta.class.getSimpleName() + " " +
				 	 "SET nome_plano_conta = '" + pc.getNomePlanoConta() + "' " +
					 "WHERE cod_plano_conta = " + pc.getCod();
		
		int ret = conn.atualizar(sql);
		conn.fecharConexao();
		return ret;
	}
}
