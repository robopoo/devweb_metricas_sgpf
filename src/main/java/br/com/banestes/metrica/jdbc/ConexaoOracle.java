package br.com.sgpf.metrica.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ConexaoOracle extends ConexaoSimples{

	
	public static final Map<String, String> propriedades;
	
	static{
		propriedades = new HashMap<String, String>();
		
		propriedades.put("remarksReporting", "true");//Para retornar os comentários das colunas e tabelas
		
		Collections.unmodifiableMap(propriedades);
		
	}
	
	public ConexaoOracle(ConfiguracaoConexaoBD conexaoBanco) {
		super(conexaoBanco);
	}

	public static final String CLASS_NAME_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	@Override
	public String getURLJDBC(){
		return String.format("jdbc:oracle:thin:@%s:%d:%s", super.conexaoBanco.getHost(), super.conexaoBanco.getPorta(), this.conexaoBanco.getSid());
	}
	
	@Override
	public String getClassNameDriver() {
		return CLASS_NAME_DRIVER;
	}
	
	@Override
	public Map<String, String> getPropriedades(){
		return propriedades;
	}

	@Override
	protected String getValidationQuery() {
		return "select 1 from dual";
	}
	
	@Override
	public String getSchema(){
		if(this.conexaoBanco.getSchema() == null || "".equals(this.conexaoBanco.getSchema().trim())){
			return super.conexaoBanco.getUsuario();
		}

		return super.conexaoBanco.getSchema();
	}

	
}
