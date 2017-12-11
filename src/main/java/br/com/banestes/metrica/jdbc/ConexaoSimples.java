package br.com.sgpf.metrica.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbcp.BasicDataSource;

public abstract class ConexaoSimples implements ConexaoBD{

	private BasicDataSource basicDataSource;

	protected ConfiguracaoConexaoBD conexaoBanco;

	public abstract String getClassNameDriver();

	public abstract String getURLJDBC();
	
	public Map<String, String> getPropriedades(){
		return null;
	}

	public ConexaoSimples(ConfiguracaoConexaoBD conexaoBanco){
		this.conexaoBanco = conexaoBanco;

		String jdbcUrl = getURLJDBC();
		String user = conexaoBanco.getUsuario();
		String pass = conexaoBanco.getSenha();

		this.basicDataSource = new BasicDataSource();
		this.basicDataSource.setDriverClassName(getClassNameDriver());
		this.basicDataSource.setUsername(user);
		this.basicDataSource.setPassword(pass);
		
		Map<String, String> props = getPropriedades();
		
		if(props != null){
			for(Entry<String, String> prop : props.entrySet()){
				this.basicDataSource.addConnectionProperty(prop.getKey(), prop.getValue());
			}
		}

		this.basicDataSource.setUrl(jdbcUrl);
		if(getValidationQuery() != null && !"".equals(getValidationQuery())){
			this.basicDataSource.setValidationQuery(getValidationQuery());
		}
		this.basicDataSource.setTestOnBorrow(true);
	}
	
	protected abstract String getValidationQuery();

	public String getSchema(){
		return this.conexaoBanco.getSchema();
	}

	public ConfiguracaoConexaoBD getConexaoBanco() {
		return conexaoBanco;
	}

	public Connection getConnection() throws SQLException
	{
		return this.basicDataSource.getConnection();
	}

	public void closeConnection()
	{
		if (this.basicDataSource != null)
		{
			try
			{
				this.basicDataSource.close();
			}
			catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
	}


}
