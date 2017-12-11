package br.com.sgpf.metrica.enumeration;


public enum TipoElementoContagemEnum {
	DEFLATOR("D", "Deflator"),
	FIXO("F", "Fixo"),
	QUANTIDADE("Q", "Quantidade");

	private String value;

	private String descricao;

	private TipoElementoContagemEnum(String value, String descricao) {
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