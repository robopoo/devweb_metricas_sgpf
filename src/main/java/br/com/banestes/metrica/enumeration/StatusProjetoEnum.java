package br.com.sgpf.metrica.enumeration;

public enum StatusProjetoEnum {

	DIGITADO("D","Digitado"),
	APROVADO("A", "Aprovado"),
	REJEITADO("R", "Rejeitado"),
	ENVIADO_INSPECAO("E", "Enviado para Inspeção"),
	CONCLUIDO("C", "Concluído");
	
	private String value;

	private String descricao;

	private StatusProjetoEnum(String value, String descricao) {
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
