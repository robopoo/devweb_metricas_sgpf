package br.com.sgpf.metrica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;

@Entity
@Table(name = "TB_TDD_TIPO_DADOS_FD_PROJETO")
@SequenceGenerator(name = "SQ_TDD_TIPO_DADOS_FD_PROJETO", sequenceName = "SQ_TDD_TIPO_DADOS_FD_PROJETO")
@NamedQueries({ @NamedQuery(name = "TipoRegistroProjetoTO.deleteTipoRegistroByFuncaoDados", query = "delete from TipoDadosFuncaoDadosProjetoTO obj where obj.idTipoDadosProjeto in "
		+ "(select tfd.idTipoDadosProjeto from TipoDadosFuncaoDadosProjetoTO tfd join tfd.tipoRegistroFuncaoDadosProjetoTO trp join trp.funcaoDadosProjetoTO fd where trp.funcaoDadosProjetoTO = ?1)") })
public class TipoDadosFuncaoDadosProjetoTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDD_ID_TIPO_DADOS_FD_PROJETO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDD_TIPO_DADOS_FD_PROJETO")
	private Long idTipoDadosProjeto;

	@ManyToOne
	@JoinColumn(name = "TRP_ID_TIPO_REGISTRO_PROJETO")
	private TipoRegistroProjetoTO tipoRegistroFuncaoDadosProjetoTO;

	@ManyToOne
	@JoinColumn(name = "ATR_ID_ATRIBUTO")
	private AtributoTO atributoTO;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDD_FL_REFERENCIA_APLICACAO", nullable = true)
	private SimNaoEnum flReferenciaAplicacao;

	public Long getIdTipoDadosProjeto() {
		return idTipoDadosProjeto;
	}

	public void setIdTipoDadosProjeto(Long idTipoDadosProjeto) {
		this.idTipoDadosProjeto = idTipoDadosProjeto;
	}

	public TipoRegistroProjetoTO getTipoRegistroFuncaoDadosProjetoTO() {
		return tipoRegistroFuncaoDadosProjetoTO;
	}

	public void setTipoRegistroFuncaoDadosProjetoTO(TipoRegistroProjetoTO tipoRegistroFuncaoDadosProjetoTO) {
		this.tipoRegistroFuncaoDadosProjetoTO = tipoRegistroFuncaoDadosProjetoTO;
	}

	public AtributoTO getAtributoTO() {
		return atributoTO;
	}

	public void setAtributoTO(AtributoTO atributoTO) {
		this.atributoTO = atributoTO;
	}

	public SimNaoEnum getFlReferenciaAplicacao() {
		return flReferenciaAplicacao;
	}

	public void setFlReferenciaAplicacao(SimNaoEnum flReferenciaAplicacao) {
		this.flReferenciaAplicacao = flReferenciaAplicacao;
	}

	public void setReferenciaAplicacao(Boolean value) {
		setFlReferenciaAplicacao(value ? SimNaoEnum.SIM : SimNaoEnum.NAO);
	}

	public Boolean getReferenciaAplicacao() {
		return getFlReferenciaAplicacao() == SimNaoEnum.SIM ? true : false;
	}

	/* (non-Javadoc)
	 * @see br.com.sgpf.metrica.core.entity.EntityModel#getEntityId()
	 */
	@Override
	public Long getEntityId() {
		return this.idTipoDadosProjeto;
	}
}