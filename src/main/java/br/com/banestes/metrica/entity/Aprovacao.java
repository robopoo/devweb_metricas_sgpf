package br.com.sgpf.metrica.entity;

import br.com.sgpf.metrica.enumeration.StatusAvaliacaoEnum;

public interface Aprovacao {

	public String getComentarioRejeicao();
	
	public void setComentarioRejeicao(String comentarioRejeicao);
	
	public boolean isAprovado();
	
	public StatusAvaliacaoEnum getSituacaoContagem();

	public void setSituacaoContagem(StatusAvaliacaoEnum situacaoContagem);
	
}
