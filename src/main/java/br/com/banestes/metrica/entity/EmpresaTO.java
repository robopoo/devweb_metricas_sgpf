package br.com.sgpf.metrica.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;

@Entity
@Table(name = "TB_EMP_EMPRESA")
@SequenceGenerator(name = "SQ_EMP_ID_EMPRESA", sequenceName = "SQ_EMP_ID_EMPRESA")
public class EmpresaTO extends EntityModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5492152868025837061L;

	@Id
	@Column(name = "EMP_ID_EMPRESA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_EMP_ID_EMPRESA")
	private Long idEmpresa;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "EMP_DS_RAZAO_SOCIAL", nullable = false)
	private String nmEmpresa;

	@Type(type = "statusEnumUserType")
	@Column(name = "EMP_ST_STATUS", nullable = false)
	private StatusEnum stEmpresa;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "EMP_FL_FORNECEDOR", nullable = false)
	private SimNaoEnum flFornecedor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresaTO", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ContratoTO> contratoTOlist;

	@OrderBy("cdSistema")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresaTO", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<SistemaTO> sistemaTOlist;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresaTO", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<AnalistaTO> analistaTOList;

	public List<ContratoTO> getContratosAtivos(){
		
		List<ContratoTO> contratosAtivos = new ArrayList<ContratoTO>();

		if(this.contratoTOlist != null){

			for (ContratoTO contratoTO : this.contratoTOlist) {
				if(StatusEnum.ATIVADO.equals(contratoTO.getStContrato())){
					contratosAtivos.add(contratoTO);
				}
			}
		}

		return contratosAtivos;
		
	}
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNmEmpresa() {
		return nmEmpresa;
	}

	public void setNmEmpresa(String nmEmpresa) {
		this.nmEmpresa = nmEmpresa;
	}

	@Override
	public Long getEntityId() {
		return this.idEmpresa;
	}

	public StatusEnum getStEmpresa() {
		return stEmpresa;
	}

	public void setStEmpresa(StatusEnum stEmpresa) {
		this.stEmpresa = stEmpresa;
	}

	public List<ContratoTO> getContratoTOlist() {
		return contratoTOlist;
	}

	public void setContratoTOlist(List<ContratoTO> contratoTOlist) {
		this.contratoTOlist = contratoTOlist;
	}

	public List<SistemaTO> getSistemaTOlist() {
		return sistemaTOlist;
	}

	public void setSistemaTOlist(List<SistemaTO> sistemaTOlist) {
		this.sistemaTOlist = sistemaTOlist;
	}

	public SimNaoEnum getFlFornecedor() {
		return flFornecedor;
	}

	public void setFlFornecedor(SimNaoEnum flFornecedor) {
		this.flFornecedor = flFornecedor;
	}

	public List<AnalistaTO> getAnalistaTOList() {
		return analistaTOList;
	}

	public void setAnalistaTOList(List<AnalistaTO> analistaTOList) {
		this.analistaTOList = analistaTOList;
	}

}
