package br.com.sgpf.metrica.entity;

import java.util.List;

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

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.enumeration.TipoAnalistaEnum;

 
@Entity
@Table(name = "TB_ANL_ANALISTA")
@SequenceGenerator(name = "SQ_ANL_ID_ANALISTA", sequenceName = "SQ_ANL_ANALISTA")
public class AnalistaTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ANL_ID_ANALISTA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ANL_ID_ANALISTA")
	private Long idAnalista;

	@Column(name = "ANL_NM_ANALISTA")
	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	private String nomeAnalista;

	@Column(name = "ANL_DS_MAIL_ANALISTA")
	private String dsMail;

	@Type(type = "tipoAnalistaEnumUserType")
	@Column(name = "ANL_TP_ANALISTA", nullable = true)
	private TipoAnalistaEnum tpAnalista;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID_EMPRESA")
	private EmpresaTO empresaTO;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "responsavelPDCaseTO")
	private List<ProjetoTO> responsavelPDCaseTOList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "responsavelPrincipalClienteTO")
	private List<ProjetoTO> responsavelPrincipalClienteTOList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "responsavelEnvolvidoClientTO")
	private List<ProjetoTO> responsavelEnvolvidoClientTOList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gestorPrincipalTO")
	private List<ProjetoTO> gestorPrincipalTOList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gestorEnvolvidoTO")
	private List<ProjetoTO> gestorEnvolvidoTOList;

	public Long getIdAnalista() {
		return idAnalista;
	}

	public void setIdAnalista(Long idAnalista) {
		this.idAnalista = idAnalista;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sgpf.metrica.core.entity.EntityModel#getEntityId()
	 */
	@Override
	public Long getEntityId() {
		return this.idAnalista;
	}

	/**
	 * @return the nomeAnalista
	 */
	public String getNomeAnalista() {
		return nomeAnalista;
	}

	/**
	 * @param nomeAnalista
	 *            the nomeAnalista to set
	 */
	public void setNomeAnalista(String nomeAnalista) {
		this.nomeAnalista = nomeAnalista;
	}

	public List<ProjetoTO> getResponsavelPDCaseTOList() {
		return responsavelPDCaseTOList;
	}

	public void setResponsavelPDCaseTOList(List<ProjetoTO> responsavelPDCaseTOList) {
		this.responsavelPDCaseTOList = responsavelPDCaseTOList;
	}

	public List<ProjetoTO> getResponsavelPrincipalClienteTOList() {
		return responsavelPrincipalClienteTOList;
	}

	public void setResponsavelPrincipalClienteTOList(List<ProjetoTO> responsavelPrincipalClienteTOList) {
		this.responsavelPrincipalClienteTOList = responsavelPrincipalClienteTOList;
	}

	public List<ProjetoTO> getResponsavelEnvolvidoClientTOList() {
		return responsavelEnvolvidoClientTOList;
	}

	public void setResponsavelEnvolvidoClientTOList(List<ProjetoTO> responsavelEnvolvidoClientTOList) {
		this.responsavelEnvolvidoClientTOList = responsavelEnvolvidoClientTOList;
	}

	public List<ProjetoTO> getGestorPrincipalTOList() {
		return gestorPrincipalTOList;
	}

	public void setGestorPrincipalTOList(List<ProjetoTO> gestorPrincipalTOList) {
		this.gestorPrincipalTOList = gestorPrincipalTOList;
	}

	public List<ProjetoTO> getGestorEnvolvidoTOList() {
		return gestorEnvolvidoTOList;
	}

	public void setGestorEnvolvidoTOList(List<ProjetoTO> gestorEnvolvidoTOList) {
		this.gestorEnvolvidoTOList = gestorEnvolvidoTOList;
	}

	public TipoAnalistaEnum getTpAnalista() {
		return tpAnalista;
	}

	public void setTpAnalista(TipoAnalistaEnum tpAnalista) {
		this.tpAnalista = tpAnalista;
	}

	/**
	 * @return the empresaTO
	 */
	public EmpresaTO getEmpresaTO() {
		return empresaTO;
	}

	/**
	 * @param empresaTO
	 *            the empresaTO to set
	 */
	public void setEmpresaTO(EmpresaTO empresaTO) {
		this.empresaTO = empresaTO;
	}

	public String getDsMail() {
		return dsMail;
	}

	public void setDsMail(String dsMail) {
		this.dsMail = dsMail;
	}
	
	

}
