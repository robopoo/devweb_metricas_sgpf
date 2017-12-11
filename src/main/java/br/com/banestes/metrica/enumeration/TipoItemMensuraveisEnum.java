package br.com.sgpf.metrica.enumeration;

import br.com.sgpf.metrica.core.enumeration.StatusEnum;

public enum TipoItemMensuraveisEnum {
	INCLUSAO("I", "Inclusão"),
	ALTERACAO("A", "Alteração"),
	EXCLUSAO("E","Exclusão"),
	NAO_MENSURAVEL("N","Itens não mensuráveis");

	private String value;

	private String descricao;

	private TipoItemMensuraveisEnum(String value, String descricao) {
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