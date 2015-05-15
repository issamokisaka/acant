package br.org.acant.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
	private static final String USUARIO = "sa";
	private static final String SENHA = "";

	private static final String BASE = "acant.db";
//	private static final String BASE = "/home/"+System.getenv("USER")+"/Development/acant/acant.db";

	private static final String URL = "jdbc:sqlite:";
	private static final String DRIVER = "org.sqlite.JDBC";

	private Connection conn;
	private Statement stmt;

	/**
	 * @throws SQLException
	 */
	public void abrirConexao() throws SQLException {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        conn = DriverManager.getConnection(URL + BASE, USUARIO, SENHA);
        stmt = conn.createStatement();
	}

	/**
	 * @throws SQLException
	 */
	public void fecharConexao() throws SQLException {
		stmt.close();
		conn.close();
	}

	/**
	 * @param tabela
	 * @return
	 * @throws SQLException
	 */
	public ResultSet consultar(String tabela) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabela);
		return rs;
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean consultarRegistro(String sql) throws SQLException {
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
			return true;
		return false;
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean existemRegistros(String sql) throws SQLException {
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
			return true;
		return false;
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet obterRegistro(String sql) throws SQLException {
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet obterRegistros(String sql) throws SQLException {
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int incluirObjeto(String sql) throws SQLException {
		return stmt.executeUpdate(sql);
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int excluirRegistro(String sql) throws SQLException {
		return stmt.executeUpdate(sql);
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int atualizar(String sql) throws SQLException {
		return stmt.executeUpdate(sql);
	}
}
