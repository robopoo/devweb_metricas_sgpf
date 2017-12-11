package br.com.sgpf.metrica.view;

import java.math.BigDecimal;
import java.util.List;

import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.ElementoContagemTO;

public class FuncaoDadosProjetoVO implements Comparable<FuncaoDadosProjetoVO> {

	private String nome;

	private String descricao;

	private String descricaoManutencao;

	private ElementoContagemTO elementoContagemTO;

	private List<AtributoTO> atributoTOs;

	public BigDecimal qtPontoFuncao;

	public static class Builder {

		private FuncaoDadosProjetoVO instancia;

		public Builder() {
			instancia = new FuncaoDadosProjetoVO();
		}

		public Builder comNome(String nome) {
			this.instancia.nome = nome;
			return this;
		}

		public Builder comManutencao(String descricaoManutencao) {
			this.instancia.descricaoManutencao = descricaoManutencao;
			return this;
		}

		public Builder comDescricao(String descricao) {
			this.instancia.descricao = descricao;
			return this;
		}

		public Builder quantidadePontoFuncao(BigDecimal qtPontoFuncao) {
			this.instancia.qtPontoFuncao = qtPontoFuncao;
			return this;
		}

		public Builder comElementoContagem(ElementoContagemTO elementoContagemTO) {
			this.instancia.elementoContagemTO = elementoContagemTO;
			return this;
		}

		public Builder possuiOsAtributos(List<AtributoTO> atributoTOs) {
			this.instancia.atributoTOs = atributoTOs;
			return this;
		}

		public FuncaoDadosProjetoVO instanciar() {
			return instancia;
		}

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<AtributoTO> getAtributoTOs() {
		return atributoTOs;
	}

	public void setAtributoTOs(List<AtributoTO> atributoTOs) {
		this.atributoTOs = atributoTOs;
	}

	public String getDescricaoManutencao() {
		return descricaoManutencao;
	}

	public void setDescricaoManutencao(String descricaoManutencao) {
		this.descricaoManutencao = descricaoManutencao;
	}

	public ElementoContagemTO getElementoContagemTO() {
		return elementoContagemTO;
	}

	public BigDecimal getQtPontoFuncao() {
		return qtPontoFuncao;
	}

	@Override
	public int compareTo(FuncaoDadosProjetoVO o) {
		return this.getNome().compareToIgnoreCase(o.getNome());
	}

}