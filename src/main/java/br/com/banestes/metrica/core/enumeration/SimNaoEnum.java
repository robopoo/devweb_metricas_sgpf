package br.com.sgpf.metrica.core.enumeration;


public enum SimNaoEnum implements EnumModel {
	NAO("N", "Não"),
	SIM("S", "Sim");

	private String value;

	private String descricao;

	private SimNaoEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getValue() {
		return value;
	}
}