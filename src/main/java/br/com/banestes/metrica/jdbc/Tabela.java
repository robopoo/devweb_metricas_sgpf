package br.com.sgpf.metrica.jdbc;

import java.util.List;

public class Tabela {

	private String nome;
	
	private String descricao;

	private List<Coluna> colunas;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<Coluna> getColunas() {
		return colunas;
	}

	public void setColunas(List<Coluna> colunas) {
		this.colunas = colunas;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
