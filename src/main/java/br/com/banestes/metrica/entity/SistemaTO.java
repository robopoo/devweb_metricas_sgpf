package br.com.sgpf.metrica.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.excel.fpa.CabecalhoPlanilhaFPA;

@Entity
@Table(name = "TB_SIS_SISTEMA")
@SequenceGenerator(name = "SQ_SIS_ID_SISTEMA", sequenceName = "SQ_SIS_ID_SISTEMA")
public class SistemaTO extends EntityModel<Long> implements CabecalhoPlanilhaFPA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060620755869388999L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID_EMPRESA")
	private EmpresaTO empresaTO;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sistemaTO")
	private List<ProjetoTO> projetoTOList;

	@Id
	@Column(name = "SIS_ID_SISTEMA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SIS_ID_SISTEMA")
	private Long idSistema;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "SIS_DS_SISTEMA")
	private String dsSistema;

	@LikeCriteria(caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE)
	@Column(name = "SIS_CD_SISTEMA")
	private String cdSistema;

	//TODO Após mapear a Entidade Projeto, descomentar o Código abaixo!
	//	@OneToMany(fetch = FetchType.LAZY, mappedBy="sistemaTO")
	@Transient
	private List<ProjetoTO> projetos;

	//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sistemaTO")
	//	private List<EntidadeTO> entidadeTOlist;

	@OneToOne(mappedBy = "sistemaTO", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private BaselineTO baselineTO;

	public BaselineTO getBaselineTO() {
		return baselineTO;
	}

	public void setBaselineTO(BaselineTO baselineTO) {
		this.baselineTO = baselineTO;
	}

	public List<ProjetoTO> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<ProjetoTO> projetos) {
		this.projetos = projetos;
	}

	public Long getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(Long idSistema) {
		this.idSistema = idSistema;
	}

	public String getDsSistema() {
		return dsSistema;
	}

	public void setDsSistema(String dsSistema) {
		this.dsSistema = dsSistema;
	}

	public String getCdSistema() {
		return cdSistema;
	}

	public void setCdSistema(String cdSistema) {
		this.cdSistema = cdSistema;
	}

	public EmpresaTO getEmpresaTO() {
		return empresaTO;
	}

	public void setEmpresaTO(EmpresaTO empresaTO) {
		this.empresaTO = empresaTO;
	}

	@Override
	public Long getEntityId() {
		return this.idSistema;
	}

	@Override
	public Date getDataRevisao() {
		return new Date();
	}

	@Override
	public String getFornecedor() {
		return "PD CASE Informática LTDA";
	}

	@Override
	public String getSistema() {
		return this.cdSistema + " - " + this.dsSistema;
	}

	@Override
	public String getAnalistaFornecedor() {
		return "Não se aplica";
	}

	@Override
	public String getAnalistaCliente() {
		return "Não se aplica";
	}

	/**
	 * Retorna descricao visual para apresentação
	 * @return String
	 */
	public String getDescricaoVisual() {
		return getCdSistema().concat(" - ").concat(getDsSistema());
	}

	public List<ProjetoTO> getProjetoTOList() {
		return projetoTOList;
	}

	public void setProjetoTOList(List<ProjetoTO> projetoTOList) {
		this.projetoTOList = projetoTOList;
	}

	@Override
	public String getTituloPlanilha() {
		return "Não se aplica.";
	}

	@Override
	public Date getDtCriacao() {
		return null;
	}

}
