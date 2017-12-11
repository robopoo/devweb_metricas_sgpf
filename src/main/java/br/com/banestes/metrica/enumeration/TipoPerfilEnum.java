package br.com.sgpf.metrica.enumeration;

import br.com.sgpf.metrica.core.enumeration.EnumModel;

public enum TipoPerfilEnum implements EnumModel {
	ADMINISTRADOR("1", "Administrador"),
	OPERACIONAL("2", "Operacional");

	private String value;

	private String descricao;

	private TipoPerfilEnum(String value, String descricao) {
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