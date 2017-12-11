package br.com.sgpf.metrica.jdbc;

public class ConexaoSqlServer extends ConexaoSimples{

	
	public ConexaoSqlServer(ConfiguracaoConexaoBD conexaoBanco) {
		super(conexaoBanco);
	}

	public static final String CLASS_NAME_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	
	@Override
	public String getURLJDBC(){
		return String.format("jdbc:jtds:sqlserver://%s:%d/%s", super.conexaoBanco.getHost(), super.conexaoBanco.getPorta(), this.conexaoBanco.getSid());
	}
	
	@Override
	public String getClassNameDriver() {
		return CLASS_NAME_DRIVER;
	}
	
	@Override
	public String getSchema(){
		return null;
	}

	@Override
	protected String getValidationQuery() {
		return conexaoBanco.getSchema();
	}

	
}
