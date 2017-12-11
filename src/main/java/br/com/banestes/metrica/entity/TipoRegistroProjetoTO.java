package br.com.sgpf.metrica.entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;

@Entity
@Table(name = "TB_TRP_TIPO_REGISTRO_PROJETO")
@SequenceGenerator(name = "SQ_TRP_TIPO_REGISTRO_PROJETO", sequenceName = "SQ_TRP_TIPO_REGISTRO_PROJETO")
@NamedQueries({
	@NamedQuery(name = "TipoRegistroProjetoTO.deleteByFuncaoDados", query = "delete from TipoRegistroProjetoTO trp where trp.funcaoDadosProjetoTO = ?1") })
public class TipoRegistroProjetoTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRP_ID_TIPO_REGISTRO_PROJETO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TRP_TIPO_REGISTRO_PROJETO")
	private Long idTipoRegistroFuncaoDadosProjeto;

	@ManyToOne
	@JoinColumn(name = "FDP_ID_FUNCAO_DADOS_PROJETO")
	private FuncaoDadosProjetoTO funcaoDadosProjetoTO;

	@ManyToOne
	@JoinColumn(name = "ENT_ID_ENTIDADE")
	private EntidadeTO entidadeTO;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TRP_FL_SUBGRUPO_DADOS")
	private SimNaoEnum indicadorSubgrupoDados;

	@OneToMany(mappedBy = "tipoRegistroFuncaoDadosProjetoTO", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.REMOVE })
	private List<TipoDadosFuncaoDadosProjetoTO> tipoDadosFuncaoDadosProjetoTOs;

	@Transient
	private boolean podeMarcarSubGrupoDados;

	public Long getIdTipoRegistroFuncaoDadosProjeto() {
		return idTipoRegistroFuncaoDadosProjeto;
	}

	public void setIdTipoRegistroFuncaoDadosProjeto(Long idTipoRegistroFuncaoDadosProjeto) {
		this.idTipoRegistroFuncaoDadosProjeto = idTipoRegistroFuncaoDadosProjeto;
	}

	public FuncaoDadosProjetoTO getFuncaoDadosProjetoTO() {
		return funcaoDadosProjetoTO;
	}

	public void setFuncaoDadosProjetoTO(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {
		this.funcaoDadosProjetoTO = funcaoDadosProjetoTO;
	}

	public EntidadeTO getEntidadeTO() {
		return entidadeTO;
	}

	public void setEntidadeTO(EntidadeTO entidadeTO) {
		this.entidadeTO = entidadeTO;
	}

	public SimNaoEnum getIndicadorSubgrupoDados() {
		return indicadorSubgrupoDados;
	}

	public void setIndicadorSubgrupoDados(SimNaoEnum indicadorSubgrupoDados) {
		this.indicadorSubgrupoDados = indicadorSubgrupoDados;
	}

	public List<TipoDadosFuncaoDadosProjetoTO> getTipoDadosFuncaoDadosProjetoTOs() {
		return tipoDadosFuncaoDadosProjetoTOs;
	}

	public void setTipoDadosFuncaoDadosProjetoTOs(List<TipoDadosFuncaoDadosProjetoTO> tipoDadosFuncaoDadosProjetoTOs) {
		this.tipoDadosFuncaoDadosProjetoTOs = tipoDadosFuncaoDadosProjetoTOs;
	}

	@Override
	public Long getEntityId() {
		return this.idTipoRegistroFuncaoDadosProjeto;
	}

	public void setSubgrupoDados(Boolean value) {
		setIndicadorSubgrupoDados(value ? SimNaoEnum.SIM : SimNaoEnum.NAO);
	}

	public Boolean getSubgrupoDados() {
		return getIndicadorSubgrupoDados() == SimNaoEnum.SIM ? true : false;
	}

	public boolean isPodeMarcarSubGrupoDados() {
		return podeMarcarSubGrupoDados;
	}

	public void setPodeMarcarSubGrupoDados(boolean podeMarcarSubGrupoDados) {
		this.podeMarcarSubGrupoDados = podeMarcarSubGrupoDados;
	}

}