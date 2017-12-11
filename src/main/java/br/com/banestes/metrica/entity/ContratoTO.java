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

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;

@Entity
@Table(name = "TB_CTR_CONTRATO")
@SequenceGenerator(name = "SQ__CTR_ID_CONTRATO", sequenceName = "SQ__CTR_ID_CONTRATO")
public class ContratoTO extends EntityModel<Long> {

	private static final long serialVersionUID = 3908496986620236479L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID_EMPRESA")
	private EmpresaTO empresaTO;

	@Id
	@Column(name = "CTR_ID_CONTRATO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ__CTR_ID_CONTRATO")
	private Long idContrato;

	@Column(name = "CTR_CD_CODIGO", unique = true, nullable = false)
	private String codigoContrato;

	@Column(name = "CTR_DS_CONTRATO")
	private String descricaoContrato;

	@Type(type = "statusEnumUserType")
	@Column(name = "CTR_ST_STATUS", nullable = false)
	private StatusEnum stContrato;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contratoTO")
	private List<FatorEquivalenciaTO> fatorEquivalenciaTOList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contratoTO")
	private List<ProjetoTO> projetoTOList;

	public EmpresaTO getEmpresaTO() {
		return empresaTO;
	}

	public void setEmpresaTO(EmpresaTO empresaTO) {
		this.empresaTO = empresaTO;
	}

	public Long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}

	public String getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(String codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public String getDescricaoContrato() {
		return descricaoContrato;
	}

	public void setDescricaoContrato(String descricaoContrato) {
		this.descricaoContrato = descricaoContrato;
	}

	@Override
	public Long getEntityId() {
		return this.idContrato;
	}

	public StatusEnum getStContrato() {
		return stContrato;
	}

	public void setStContrato(StatusEnum stContrato) {
		this.stContrato = stContrato;
	}

	public List<FatorEquivalenciaTO> getFatorEquivalenciaTOList() {
		return fatorEquivalenciaTOList;
	}

	public void setFatorEquivalenciaTOList(
			List<FatorEquivalenciaTO> fatorEquivalenciaTOList) {
		this.fatorEquivalenciaTOList = fatorEquivalenciaTOList;
	}

	
	
	
	public List<ProjetoTO> getProjetoTOList() {
		return projetoTOList;
	}

	public void setProjetoTOList(List<ProjetoTO> projetoTOList) {
		this.projetoTOList = projetoTOList;
	}

	public String impressaoContrato() {
		String retorno = null;
		if (this.getCodigoContrato() != null
				&& !this.getCodigoContrato().isEmpty()) {
			retorno = this.getCodigoContrato() + " - ";
		}
		if (this.getDescricaoContrato() != null
				&& !this.getDescricaoContrato().isEmpty()) {
			retorno += this.getDescricaoContrato();
		}
		
		return retorno;

	}

}
