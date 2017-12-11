package br.com.sgpf.metrica.core.enumeration;

public enum TipoRelatorioEnum {
	PDF("PDF"),
	XLS("XLS"),
	DOC("DOC");
	
	private String descricao;

	private TipoRelatorioEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}