package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.sgpf.metrica.core.entity.EntityModel;

@Entity
@Table(name = "TB_FTR_FATOR_EQUIVALENCIA", uniqueConstraints = @UniqueConstraint(columnNames = { "CTR_ID_CONTRATO",
		"FTR_VRS_FATOR_EQUIVALENTE" }))
@SequenceGenerator(name = "SQ_FTR_ID_FATOR_EQUIVALENCIA", sequenceName = "SQ_FTR_ID_FATOR_EQUIVALENCIA")
public class FatorEquivalenciaTO extends EntityModel<Long> {

	private static final long serialVersionUID = -4995855642132156784L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CTR_ID_CONTRATO")
	// @LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	private ContratoTO contratoTO;

	@Id
	@Column(name = "FTR_ID_FATOR_EQUIVALENCIA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FTR_ID_FATOR_EQUIVALENCIA")
	private Long idFatorEquivalencia;

	@Column(name = "FTR_VRS_FATOR_EQUIVALENTE", nullable = false)
	private BigDecimal versaoFatorEquivalencia;

	@Column(name = "FTR_NR_FATOR_EQUIVALENCIA")
	private Long fatorEquivalencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FTR_DT_VIGENCIA_INICIAL")
	private Date dtVigenciaInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FTR_DT_VIGENCIA_FINAL")
	private Date dtVigenciaFim;

	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fatorEquivalenciaTO", cascade = { CascadeType.ALL })
	private List<ElementoContagemTO> elementoContagemTOList;

	@Override
	public Long getEntityId() {

		return idFatorEquivalencia;
	}

	public ContratoTO getContratoTO() {
		return contratoTO;
	}

	public void setContratoTO(ContratoTO contratoTO) {
		this.contratoTO = contratoTO;
	}

	public Long getIdFatorEquivalencia() {
		return idFatorEquivalencia;
	}

	public void setIdFatorEquivalencia(Long idFatorEquivalencia) {
		this.idFatorEquivalencia = idFatorEquivalencia;
	}

	public BigDecimal getVersaoFatorEquivalencia() {
		return versaoFatorEquivalencia;
	}

	public void setVersaoFatorEquivalencia(BigDecimal versaoFatorEquivalencia) {
		this.versaoFatorEquivalencia = versaoFatorEquivalencia;
	}

	public Long getFatorEquivalencia() {
		return fatorEquivalencia;
	}

	public void setFatorEquivalencia(Long fatorEquivalencia) {
		this.fatorEquivalencia = fatorEquivalencia;
	}

	public Date getDtVigenciaInicio() {
		return dtVigenciaInicio;
	}

	public void setDtVigenciaInicio(Date dtVigenciaInicio) {
		this.dtVigenciaInicio = dtVigenciaInicio;
	}

	public Date getDtVigenciaFim() {
		return dtVigenciaFim;
	}

	public void setDtVigenciaFim(Date dtVigenciaFim) {
		this.dtVigenciaFim = dtVigenciaFim;
	}

	public List<ElementoContagemTO> getElementoContagemTOList() {
		if (elementoContagemTOList != null) {
			Collections.sort(elementoContagemTOList, new Comparator<ElementoContagemTO>() {

				@Override
				public int compare(ElementoContagemTO o1, ElementoContagemTO o2) {
					return o1.getCodElementoContagem().compareTo(o2.getCodElementoContagem());
				}
			});
		}
		return elementoContagemTOList;
	}

	public void setElementoContagemTOList(List<ElementoContagemTO> elementoContagemTOList) {
		this.elementoContagemTOList = elementoContagemTOList;
	}

}
