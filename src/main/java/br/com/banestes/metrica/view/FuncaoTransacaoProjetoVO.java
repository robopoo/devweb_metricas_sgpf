package br.com.sgpf.metrica.view;

import java.io.Serializable;
import java.util.List;

import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.DescricaoAtributoTD;
import br.com.sgpf.metrica.entity.TipoDadosDERFuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;

public class FuncaoTransacaoProjetoVO implements Serializable, Cloneable, Comparable<FuncaoTransacaoProjetoVO> {

	private static final long serialVersionUID = 1L;

	// Inclusão - alteração
	private String tipo;

	private String nome;

	private String descricao;

	private String arquivosReferenciados;

	private String tipoDados;

	private String justificativaSe;
	
	public String dsManutencao;

	public List<DescricaoAtributoTD> tds;

	public List<ArquivoReferenciadoProjetoTO> arqReferenciados;

	public List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs;

	public TipoComplexidadeEnum complexidadeTP;

	public TipoFuncaoTransacaoEnum funcaoTransacaoTP;

	public int qtAr;

	public int qtTD;

	private boolean isElementoQuantidade;

	public int qtItem;

	public static class Builder {

		private FuncaoTransacaoProjetoVO instancia;

		public Builder() {
			instancia = new FuncaoTransacaoProjetoVO();
		}

		public Builder doTipo(String tipo) {
			instancia.tipo = tipo;
			return this;
		}

		public Builder comNome(String nome) {
			this.instancia.nome = nome;
			return this;
		}

		public Builder comJustificativaSe(String justificativa)
		{
			this.instancia.justificativaSe = justificativa;
			return this;
		}
		
		public Builder comDescricao(String descricao) {
			this.instancia.descricao = descricao;
			return this;
		}

		public Builder referenciaOsArquivos(String arquivos) {
			this.instancia.arquivosReferenciados = arquivos;
			return this;
		}

		public Builder comTipoDadosTemp(List<DescricaoAtributoTD> tds) {
			this.instancia.tds = tds;
			return this;
		}

		public Builder comTipoDados(String tipoDados) {
			this.instancia.tipoDados = tipoDados;
			return this;
		}

		public FuncaoTransacaoProjetoVO instanciar() {
			return instancia;
		}

		public Builder comManutencao(String descricaoManutencao) {
			this.instancia.dsManutencao = descricaoManutencao;
			return this;
		}

		public Builder comArquivosReferenciados(List<ArquivoReferenciadoProjetoTO> arquivoReferenciadoProjetoTOs) {
			this.instancia.arqReferenciados = arquivoReferenciadoProjetoTOs;
			return this;
		}

		public Builder comDadosDerivados(
				List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs) {
			this.instancia.tipoDadosDERFuncaoTransacaoProjetoTOs = tipoDadosDERFuncaoTransacaoProjetoTOs;
			return this;
		}

		public Builder comComplexidade(TipoComplexidadeEnum complexidadeTP) {
			this.instancia.complexidadeTP = complexidadeTP;
			return this;
		}

		public Builder doTipo(TipoFuncaoTransacaoEnum funcaoTransacaoTP) {
			this.instancia.funcaoTransacaoTP = funcaoTransacaoTP;
			return this;
		}

		public Builder comAR(Integer qtAR) {
			this.instancia.qtAr = qtAR;
			return this;
		}

		public Builder comTD(Integer qtTD) {
			this.instancia.qtTD = qtTD;
			return this;
		}

		public Builder quantidade(Integer qtItem) {
			this.instancia.qtItem = qtItem;
			return this;
		}

		public Builder setElementoQtdade(boolean b) {
			this.instancia.isElementoQuantidade = b;
			return this;
		}

	}
	
	public String getJustificativaSe()
	{
		return justificativaSe;
	}
	
	public void setJustificativaSe(String justificativa)
	{
		justificativaSe = justificativa;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getArquivosReferenciados() {
		return arquivosReferenciados;
	}

	public void setArquivosReferenciados(String arquivosReferenciados) {
		this.arquivosReferenciados = arquivosReferenciados;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoDados() {
		return tipoDados;
	}

	public void setTipoDados(String tipoDados) {
		this.tipoDados = tipoDados;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getDsManutencao() {
		return dsManutencao;
	}

	public List<DescricaoAtributoTD> getTds() {
		return tds;
	}

	public Integer getQtdadeLinhas() {
		if (TipoFuncaoTransacaoEnum.EE == funcaoTransacaoTP) {

		} else {

		}
		return 0;
	}

	@Override
	public int compareTo(FuncaoTransacaoProjetoVO o) {
		return this.getNome().compareToIgnoreCase(o.getNome());
	}

	public boolean isElementoQuantidade() {
		return isElementoQuantidade;
	}

	public int getQtItem() {
		return qtItem;
	}

	public TipoFuncaoTransacaoEnum getFuncaoTransacaoTP()
	{
		
		return funcaoTransacaoTP;
	}
	
	public void setFuncaoTransacaoTP(TipoFuncaoTransacaoEnum tipo)
	{
		
		 funcaoTransacaoTP = tipo;
	}
	
}