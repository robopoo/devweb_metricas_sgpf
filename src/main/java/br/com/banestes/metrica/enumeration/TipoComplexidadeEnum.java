package br.com.sgpf.metrica.enumeration;

public enum TipoComplexidadeEnum {

	ALTA("A", "Alta"), 
	BAIXA("B", "Baixa"),
	MEDIA("M", "Media");

	private String value;

	private String descricao;

	private TipoComplexidadeEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public String getValue() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}

}
