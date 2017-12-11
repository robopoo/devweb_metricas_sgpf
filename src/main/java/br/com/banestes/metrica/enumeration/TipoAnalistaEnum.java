package br.com.sgpf.metrica.enumeration;

import br.com.sgpf.metrica.core.enumeration.EnumModel;


public enum TipoAnalistaEnum implements EnumModel {
	
	ANALISTA("A", "Analista"),
	GESTOR("G", "Gestor");

	private String value;

	private String descricao;

	private TipoAnalistaEnum(String value, String descricao) {
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