package br.com.sgpf.metrica.enumeration;

public enum StatusAvaliacaoEnum {

	APROVADO("A", "Aprovado"),
	REJEITADO("R", "Rejeitado");
	
	private String value;

	private String descricao;

	private StatusAvaliacaoEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public String getValue() {
		return value;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public boolean isAprovado(){
		return APROVADO.getValue().equals(this.value);
	}
	
}
