package br.com.sgpf.metrica.jdbc;

public class Coluna {

	
	private String nome;
	
	private String descricao;
	
	private String typeName;
	

	private int dataType;
	
	private int tamanho;
	
	private Integer precisao;
	
	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getPrecisao() {
		return precisao;
	}

	public void setPrecisao(Integer precisao) {
		this.precisao = precisao;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
