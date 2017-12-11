package br.com.sgpf.metrica.jdbc;

import org.hibernate.validator.NotEmpty;
import org.jboss.seam.annotations.Name;

@Name("conexaoBanco")
public class ConfiguracaoConexaoBD {
	
	@NotEmpty
	private String usuario;
	
	@NotEmpty
	private String senha;
	
	@NotEmpty
	private String host;
	
	@NotEmpty
	private Integer porta;
	
	@NotEmpty
	private String sid;

	private String schema;

	public ConfiguracaoConexaoBD()
	{
		
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPorta() {
		return porta;
	}

	public void setPorta(Integer porta) {
		this.porta = porta;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
}
