package br.com.sgpf.metrica.enumeration;

import br.com.sgpf.metrica.core.enumeration.EnumModel;

public enum OrigemEnum implements EnumModel {
	INTERNO("I", "Interno"),
	EXTERNO("E", "Externo");

	private String value;

	private String descricao;

	private OrigemEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	public static OrigemEnum parse(String value) {
		if (value != null) {
			OrigemEnum[] origens = OrigemEnum.values();
			for (OrigemEnum origemEnum : origens) {
				if (origemEnum.getValue().equals(value))
					return origemEnum;
			}
		}
		return null;
	}

}
