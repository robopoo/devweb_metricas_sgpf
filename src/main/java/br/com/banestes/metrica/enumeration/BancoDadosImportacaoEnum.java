package br.com.sgpf.metrica.enumeration;

public enum BancoDadosImportacaoEnum {
	ORACLE("Oracle"),
	SQLSERVER("SQL Server");
	
	private String descricao;
	
	private BancoDadosImportacaoEnum(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	
	
}
