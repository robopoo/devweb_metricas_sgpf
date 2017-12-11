package br.com.sgpf.metrica.core.enumeration;


public enum StatusEnum implements EnumModel {
	ATIVADO("A", "Ativado"),
	DESATIVADO("D", "Desativado");

	private String value;

	private String descricao;

	private StatusEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public String getValue() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}

	public static StatusEnum parse(String value) {
		if (value != null) {
			StatusEnum[] origens = StatusEnum.values();
			for (StatusEnum status : origens) {
				if (status.getValue().equals(value))
					return status;
			}
		}
		return null;
	}

}