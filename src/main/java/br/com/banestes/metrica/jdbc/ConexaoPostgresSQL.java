package br.com.sgpf.metrica.jdbc;

public class ConexaoPostgresSQL extends ConexaoSimples {

	
	public static final String CLASS_NAME_DRIVER = "org.postgresql.Driver";
	
	
	
	public ConexaoPostgresSQL(ConfiguracaoConexaoBD conexaoBanco) {
		
		super(conexaoBanco);
	}

	@Override
	public String getClassNameDriver() {
		
		return CLASS_NAME_DRIVER;
		
	}

	@Override
	public String getURLJDBC() {
		
		//"jdbc:postgresql://hostname:port/dbname","username", "password");
		
		return String.format("jdbc:postgresql://%s:%d/%s", super.conexaoBanco.getHost(), super.conexaoBanco.getPorta(), this.conexaoBanco.getSid());
		//return String.format("jdbc:jtds:sqlserver://%s:%d/%s", super.conexaoBanco.getHost(), super.conexaoBanco.getPorta(), this.conexaoBanco.getSid());
	}

	@Override
	protected String getValidationQuery() {
		return conexaoBanco.getSchema();
	}

}
