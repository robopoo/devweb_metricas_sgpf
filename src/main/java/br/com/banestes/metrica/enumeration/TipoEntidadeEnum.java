package br.com.sgpf.metrica.enumeration;


public enum TipoEntidadeEnum {
	ASSOCIATIVA(0, "Associativa"),
	ATRIBUITIVA(1, "Atributiva"),
	SUBTIPO(2,"Subtipo");

	private Integer value;

	private String descricao;

	private TipoEntidadeEnum(Integer value, String descricao) {
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