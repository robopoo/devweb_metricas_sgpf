package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.enumeration.StatusAvaliacaoEnum;
import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;
import br.com.sgpf.metrica.excel.fpa.FuncaoPlanilhaFPA;

@MappedSuperclass
public abstract class FuncaoTO extends EntityModel<Long> implements FuncaoPlanilhaFPA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;

	@Type(type = "statusAvaliacaoEnumUserType")
	private StatusAvaliacaoEnum situacaoContagem;

	private String descricao;

	private String comentarioRejeicao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELC_ID_ELEMENTO_CONTAGEM")
	private ElementoContagemTO elementoContagemTO;

	private Integer qtTipoDados;

	private BigDecimal valorPontoFuncao;

	@Type(type = "tipoComplexidadeEnumUserType")
	private TipoComplexidadeEnum complexidadeTP;

	@Transient
	private String descricaoTDs;

	@Transient
	private String descricaoARsTRs;

	public BigDecimal getValorPontoFuncaoLocal() {
		BigDecimal local;

		if (this.valorPontoFuncao == null) {
			return BigDecimal.ZERO;
		}

		local = this.valorPontoFuncao;
		if (this.elementoContagemTO != null && this.elementoContagemTO.getVlrFatorEquivalencia() != null) {
			local = this.valorPontoFuncao.multiply(this.elementoContagemTO.getVlrFatorEquivalencia());
		}

		return local;
	}

	public Integer getQtTipoDados() {
		return qtTipoDados;
	}

	public void setQtTipoDados(Integer qtTipoDados) {
		this.qtTipoDados = qtTipoDados;
	}

	public BigDecimal getValorPontoFuncao() {
		return valorPontoFuncao;
	}

	public void setValorPontoFuncao(BigDecimal valorPontoFuncao) {
		this.valorPontoFuncao = valorPontoFuncao;
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

	public String getComentarioRejeicao() {
		return comentarioRejeicao;
	}

	public void setComentarioRejeicao(String comentarioRejeicao) {
		this.comentarioRejeicao = comentarioRejeicao;
	}

	public ElementoContagemTO getElementoContagemTO() {
		return elementoContagemTO;
	}

	public void setElementoContagemTO(ElementoContagemTO elementoContagemTO) {
		this.elementoContagemTO = elementoContagemTO;
	}

	public TipoComplexidadeEnum getComplexidadeTP() {
		return complexidadeTP;
	}

	public void setComplexidadeTP(TipoComplexidadeEnum complexidadeTP) {
		this.complexidadeTP = complexidadeTP;
	}

	public StatusAvaliacaoEnum getSituacaoContagem() {
		return situacaoContagem;
	}

	public void setSituacaoContagem(StatusAvaliacaoEnum situacaoContagem) {
		this.situacaoContagem = situacaoContagem;
	}

	public boolean isAprovado() {
		return StatusAvaliacaoEnum.APROVADO.equals(this.situacaoContagem);
	}

	public void setAprovado(boolean aprovado) {
		this.situacaoContagem = aprovado ? StatusAvaliacaoEnum.APROVADO : StatusAvaliacaoEnum.REJEITADO;
	}

	@Override
	public String getNome() {
		return this.nome;
	}

	@Override
	public String getCodElementoContagem() {
		if (this.elementoContagemTO == null) {
			return "";
		}

		return this.elementoContagemTO.getCodElementoContagem();

	}

	public String getDescricaoTDs() {
		return descricaoTDs;
	}

	public void setDescricaoTDs(String descricaoTDs) {
		this.descricaoTDs = descricaoTDs;
	}

	public String getDescricaoARsTRs() {
		return descricaoARsTRs;
	}

	public void setDescricaoARsTRs(String descricaoARsTRs) {
		this.descricaoARsTRs = descricaoARsTRs;
	}

}
