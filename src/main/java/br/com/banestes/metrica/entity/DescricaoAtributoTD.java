package br.com.sgpf.metrica.entity;

public class DescricaoAtributoTD {

	private String nome;

	private boolean hasNmLogico;

	public DescricaoAtributoTD(String nome, int hasNmLogico) {
		this.nome = nome;
		this.hasNmLogico = 0 != hasNmLogico;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isHasNmLogico() {
		return hasNmLogico;
	}

	public void setHasNmLogico(boolean hasNmLogico) {
		this.hasNmLogico = hasNmLogico;
	}

	public String toString() {
		return nome;
	}

}
