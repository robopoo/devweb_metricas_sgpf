package br.com.sgpf.metrica.enumeration;


public enum CategoriaEnum {
	NEGOCIO(0, "Dados de Negócio"),
	REFERENCIA(1, "Dados de Referência"),
	DADOS(2,"Dados de Código");

	private Integer value;

	private String descricao;

	private CategoriaEnum(Integer value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public Integer getValue() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}
}