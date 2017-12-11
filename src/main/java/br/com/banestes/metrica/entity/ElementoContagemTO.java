package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;

@Entity
@Table(name = "TB_ELC_ELEMENTO_CONTAGEM")
@SequenceGenerator(name = "SQ_ELC_ID_ELEMENTO_CONTAGEM", sequenceName = "SQ_ELC_ID_ELEMENTO_CONTAGEM")
@NamedQueries({ @NamedQuery(name = "findAllElementoContagemByContrato", query = "select obj from ElementoContagemTO obj where obj.fatorEquivalenciaTO.contratoTO = :contratoTO and  obj.fatorEquivalenciaTO.dtVigenciaFim is null order by obj.codElementoContagem")

})
public class ElementoContagemTO extends EntityModel<Long> {

	private static final long serialVersionUID = 5853847579008584515L;

	@Id
	@Column(name = "ELC_ID_ELEMENTO_CONTAGEM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ELC_ID_ELEMENTO_CONTAGEM")
	private Long idElementoContagem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FTR_ID_FATOR_EQUIVALENCIA")
	private FatorEquivalenciaTO fatorEquivalenciaTO;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "ELC_CD_ELEMENTO_CONTAGEM", nullable = false)
	private String codElementoContagem;

	@Column(name = "ELC_DS_RES_ELEMENTO_CONTAGEM", nullable = false)
	private String descResumida;

	@Column(name = "ELC_DS_DET_ELEMENTO_CONTAGEM", nullable = true)
	private String descDetalhada;

	@Column(name = "ELC_VL_FATOR_EQUIVALENCIA", nullable = false)
	private BigDecimal vlrFatorEquivalencia;

	@Type(type = "tipoItemMensuraveisEnumUserType")
	@Column(name = "ELC_IND_ITEM_MENSURAVEL", nullable = false)
	private TipoItemMensuraveisEnum indItemMensuravel;

	@Type(type = "tipoElementoContagemEnumUserType")
	@Column(name = "ELC_TP_ELEMENTO_CONTAGEM", nullable = false)
	private TipoElementoContagemEnum tpElementoContagem;

	@Override
	public Long getEntityId() {
		return idElementoContagem;
	}

	public FatorEquivalenciaTO getFatorEquivalenciaTO() {
		return fatorEquivalenciaTO;
	}

	public void setFatorEquivalenciaTO(FatorEquivalenciaTO fatorEquivalenciaTO) {
		this.fatorEquivalenciaTO = fatorEquivalenciaTO;
	}

	public Long getIdElementoContagem() {
		return idElementoContagem;
	}

	public void setIdElementoContagem(Long idElementoContagem) {
		this.idElementoContagem = idElementoContagem;
	}

	public String getCodElementoContagem() {
		return codElementoContagem;
	}

	public void setCodElementoContagem(String codElementoContagem) {
		this.codElementoContagem = codElementoContagem;
	}

	public String getDescResumida() {
		return descResumida;
	}

	public void setDescResumida(String descResumida) {
		this.descResumida = descResumida;
	}

	public String getDescDetalhada() {
		return descDetalhada;
	}

	public void setDescDetalhada(String descDetalhada) {
		this.descDetalhada = descDetalhada;
	}

	public BigDecimal getVlrFatorEquivalencia() {
		return vlrFatorEquivalencia;
	}

	public void setVlrFatorEquivalencia(BigDecimal vlrFatorEquivalencia) {
		this.vlrFatorEquivalencia = vlrFatorEquivalencia;
	}

	public TipoItemMensuraveisEnum getIndItemMensuravel() {
		return indItemMensuravel;
	}

	public void setIndItemMensuravel(TipoItemMensuraveisEnum indItemMensuravel) {
		this.indItemMensuravel = indItemMensuravel;
	}

	public TipoElementoContagemEnum getTpElementoContagem() {
		return tpElementoContagem;
	}

	public void setTpElementoContagem(
			TipoElementoContagemEnum tpElementoContagem) {
		this.tpElementoContagem = tpElementoContagem;
	}

	public String getDescricaoVisual() {
		return getCodElementoContagem().concat(" - ").concat(getDescResumida());
	}

}
