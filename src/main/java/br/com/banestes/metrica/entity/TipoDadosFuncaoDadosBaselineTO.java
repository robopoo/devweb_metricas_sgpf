package br.com.sgpf.metrica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;

@Entity
@Table(name="TB_TDB_TIPO_DADOS_FD_BASELINE")
@SequenceGenerator(name = "SQ_TDB_TIPO_DADOS_BASELINE", sequenceName = "SQ_TDB_TIPO_DADOS_BASELINE")
public class TipoDadosFuncaoDadosBaselineTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDB_ID_TIPO_DADOS_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDB_TIPO_DADOS_BASELINE")
	private Long idTipoDadoFuncaoDadosBaseline;

	@ManyToOne
	@JoinColumn(name="TRB_ID_TIPO_REGISTRO_BASELINE")
	private TipoRegistroBaseLineTO tipoRegistroBaseLineTO;

	@ManyToOne
	@JoinColumn(name="ATR_ID_ATRIBUTO")
	private AtributoTO atributoTO;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDB_FL_REFERENCIA_APLICACAO")
	private SimNaoEnum flReferenciaAplicacao;

	public static TipoDadosFuncaoDadosBaselineTO valueOf(TipoDadosFuncaoDadosProjetoTO tipoDadosFuncaoDadosProjetoTO){
		
		TipoDadosFuncaoDadosBaselineTO td = new TipoDadosFuncaoDadosBaselineTO();
		
		td.setAtributoTO(tipoDadosFuncaoDadosProjetoTO.getAtributoTO());
		td.setFlReferenciaAplicacao(tipoDadosFuncaoDadosProjetoTO.getFlReferenciaAplicacao());
		
		return td;
	}
	
	public Long getIdTipoDadoFuncaoDadosBaseline() {
		return idTipoDadoFuncaoDadosBaseline;
	}

	public TipoRegistroBaseLineTO getTipoRegistroBaseLineTO() {
		return tipoRegistroBaseLineTO;
	}

	public void setTipoRegistroBaseLineTO(
			TipoRegistroBaseLineTO tipoRegistroBaseLineTO) {
		this.tipoRegistroBaseLineTO = tipoRegistroBaseLineTO;
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

	@Override
	public Long getEntityId() {
		return this.idTipoDadoFuncaoDadosBaseline;
	}

}