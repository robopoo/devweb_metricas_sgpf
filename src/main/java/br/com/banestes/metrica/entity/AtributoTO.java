package br.com.sgpf.metrica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.enumeration.FormatoEnum;

@Entity
@Table(name = "TB_ATR_ATRIBUTO")
@SequenceGenerator(name = "SQ_ATR_ID_ATRIBUTO", sequenceName = "SQ_ATR_ID_ATRIBUTO")
public class AtributoTO extends EntityModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6570142972607715027L;

	@Id
	@Column(name = "ATR_ID_ATRIBUTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ATR_ID_ATRIBUTO")
	private Long idAtributo;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENT_ID_ENTIDADE")
	private EntidadeTO entidadeTO;

	@NotNull
	@Column(name = "ATR_NM_ATRIBUTO", nullable = false)
	private String nmAtributo;

	@Column(name = "ATR_DS_ATRIBUTO")
	private String dsAtributo;

	@Type(type = "formatoEnumUserType")
	@Column(name = "ATR_TP_FORMATO", nullable = true)
	private FormatoEnum tpFormato;

	@Column(name = "ATR_NR_TAMANHO", nullable = true)
	private Integer nrTamanho;

	@Column(name = "ATR_NR_PRECISAO", nullable = true)
	private Integer precisao;

	@Column(name = "ATR_DS_VALIDADE", nullable = true)
	private String dsValidade;

	@Type(type = "statusEnumUserType")
	@Column(name = "ATR_ST_ATRIBUTO", nullable = true)
	private StatusEnum status;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "ATR_FL_RECONHECIDO_USUARIO", nullable = true)
	private SimNaoEnum flReconhecido;

	public Long getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}

	public EntidadeTO getEntidadeTO() {
		return entidadeTO;
	}

	public void setEntidadeTO(EntidadeTO entidadeTO) {
		this.entidadeTO = entidadeTO;
	}

	public String getNmAtributo() {
		return nmAtributo;
	}

	public void setNmAtributo(String nmAtributo) {
		this.nmAtributo = nmAtributo;
	}

	public String getDsAtributo() {
		return dsAtributo;
	}

	public void setDsAtributo(String dsAtributo) {
		this.dsAtributo = dsAtributo;
	}

	public FormatoEnum getTpFormato() {
		return tpFormato;
	}

	public void setTpFormato(FormatoEnum tpFormato) {
		this.tpFormato = tpFormato;
	}

	public Integer getPrecisao() {
		return precisao;
	}

	public void setPrecisao(Integer precisao) {
		this.precisao = precisao;
	}

	public void setNrTamanho(Integer nrTamanho) {
		this.nrTamanho = nrTamanho;
	}

	public Integer getNrTamanho() {
		return nrTamanho;
	}

	public String getDsValidade() {
		return dsValidade;
	}

	public void setDsValidade(String dsValidade) {
		this.dsValidade = dsValidade;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public SimNaoEnum getFlReconhecido() {
		return flReconhecido;
	}

	public void setFlReconhecido(SimNaoEnum flReconhecido) {
		this.flReconhecido = flReconhecido;
	}

	@Override
	public Long getEntityId() {
		return idAtributo;
	}

	public boolean isHasDescricao() {
		return this.dsAtributo != null && this.dsAtributo.length() > 0;
	}

	public boolean isHasNmLogico() {
		return this.dsAtributo != null && this.dsAtributo.length() > 0;
	}

}