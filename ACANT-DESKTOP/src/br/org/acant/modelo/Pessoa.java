package br.org.acant.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import br.org.acant.persistencia.Conexao;

public class Pessoa {
	private int cod;
	private String nome;
	private String senha;
	private String tipoPessoa;

	private Vector<PlanoConta> planoConta;

	public static final String PROPRIETARIO = "PROPRIETARIO";
	public static final String CONTADOR     = "CONTADOR";
	public static final String GERENTE      = "GERENTE";

	public Pessoa() {
		super();
	}

	/**
	 * @param cod
	 * @param nome
	 * @param senha
	 * @param tipoPessoa
	 */
	public Pessoa(int cod, String nome, String senha, String tipoPessoa) {
		this.cod = cod;
		this.nome = nome;
		this.senha = senha;
		this.tipoPessoa = tipoPessoa;
	}

	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	
	public Vector<PlanoConta> getPlanoConta() {
		return planoConta;
	}
	public void setPlanoConta(Vector<PlanoConta> planoConta) {
		this.planoConta = planoConta;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public static Vector<Pessoa> obterPessoas() throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		ResultSet rs = conn.consultar(Pessoa.class.getSimpleName());
		Vector<Pessoa> pessoas = new Vector<Pessoa>();
		while(rs.next()) {
			pessoas.add(new Pessoa(rs.getInt("cod_pessoa"),
					rs.getString("nome"),
					rs.getString("senha"),
					rs.getString("tipo_pessoa")));
		}
		conn.fecharConexao();
		return pessoas;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int incluir() throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();
		int retorno = conn.incluirObjeto("INSERT INTO "+ getClass().getSimpleName() +" VALUES " +
										 "(null,'"+ getNome().toUpperCase() +"','"+ getSenha() +"'," +
										 "'"+getTipoPessoa()+"')");
		conn.fecharConexao();
		return retorno;
	}

	/**
	 * @param nomePessoa
	 * @return
	 * @throws SQLException
	 */
	public static boolean consultarPessoa(String nomePessoa) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		boolean retorno = conn.consultarRegistro("SELECT nome FROM "+ Pessoa.class.getSimpleName() +" "+
												 "WHERE nome LIKE '"+ nomePessoa.toUpperCase() +"'");

		conn.fecharConexao();
		return retorno;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean obterCodPessoa() throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		ResultSet rs = conn.obterRegistro("SELECT cod_pessoa FROM "+ Pessoa.class.getSimpleName() +" "+
										  "WHERE nome LIKE '"+ this.nome.toUpperCase() +"'");
		
		while(rs.next()) {
			this.cod = rs.getInt("cod_pessoa");
			conn.fecharConexao();
			return true;
		}
		conn.fecharConexao();
		return false;
	}

	/**
	 * @param nomePessoa
	 * @return
	 * @throws SQLException
	 */
	public static Pessoa obterPessoa(String nomePessoa) throws SQLException {
		Conexao conn = new Conexao();
		conn.abrirConexao();

		ResultSet rs = conn.obterRegistro("SELECT * FROM "+ Pessoa.class.getSimpleName() +" "+
										  "WHERE nome LIKE '"+ nomePessoa.toUpperCase() +"'");
		Pessoa pessoa = null;
		while(rs.next()) {
			pessoa = new Pessoa(rs.getInt("cod_pessoa"), rs.getString("nome"),
					            rs.getString("senha"), rs.getString("tipo_pessoa"));
			pessoa.setPlanoConta(PlanoConta.obterPlanoContas(pessoa.getCod()));
		}
		conn.fecharConexao();
		return pessoa;
	}

	/**
	 * @param codPessoa
	 */
	public static void excluirPessoa(int codPessoa) {}

}
