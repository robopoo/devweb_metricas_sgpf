package br.com.sgpf.metrica.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConexaoBD {

	
	public Connection getConnection() throws SQLException;

	public void closeConnection();

}
