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
@Table(name = "TB_TDE_TIPO_DADOS_DER_PROJETO")
@SequenceGenerator(name = "SQ_TDE_TIPO_DADOS_DER_PROJETO", sequenceName = "SQ_TDE_TIPO_DADOS_DER_PROJETO")
@NamedQueries({ @NamedQuery(name = "TipoDadosDERFuncaoTransacaoProjetoTO.deleteByFuncaoTransacao", query = "delete from TipoDadosDERFuncaoTransacaoProjetoTO tder where tder.funcaoTransacaoProjetoTO = ?1") })
public class TipoDadosDERFuncaoTransacaoProjetoTO extends EntityModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDE_ID_TIPO_DADOS_DER_PROJETO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDE_TIPO_DADOS_DER_PROJETO")
	private Long idTDDERFuncaoTransacaoProj;
	
	@ManyToOne
	@JoinColumn(name="FTP_ID_FUNCAO_TRANSACAO_PRO")
	private FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO;

	@Column(name="TDE_NM_CAMPO")
	private String nmCampo;

	@Column(name="TDE_DS_CAMPO")
	private String dsCampo;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDE_FL_CALCULADO_DERIVADO")
	private SimNaoEnum flCalculadoDerivado;

	public Long getIdTDDERFuncaoTransacaoProj() {
		return idTDDERFuncaoTransacaoProj;
	}

	public void setIdTDDERFuncaoTransacaoProj(Long idTDDERFuncaoTransacaoProj) {
		this.idTDDERFuncaoTransacaoProj = idTDDERFuncaoTransacaoProj;
	}

	public String getNmCampo() {
		return nmCampo;
	}

	public void setNmCampo(String nmCampo) {
		this.nmCampo = nmCampo;
	}

	public String getDsCampo() {
		return dsCampo;
	}

	public void setDsCampo(String dsCampo) {
		this.dsCampo = dsCampo;
	}

	public SimNaoEnum getFlCalculadoDerivado() {
		return flCalculadoDerivado;
	}

	public void setFlCalculadoDerivado(SimNaoEnum flCalculadoDerivado) {
		this.flCalculadoDerivado = flCalculadoDerivado;
	}
	
	public Boolean getCalculadoDerivado() {
		return flCalculadoDerivado != null ? SimNaoEnum.SIM.equals(this.flCalculadoDerivado) : null;
	}
	
	public void setCalculadoDerivado(Boolean calculadoDerivado) {
		if(calculadoDerivado == null){
			this.flCalculadoDerivado = null;
		}else{
			this.flCalculadoDerivado = calculadoDerivado ? SimNaoEnum.SIM : SimNaoEnum.NAO;
		}
	}

	@Override
	public Long getEntityId() {
		return this.idTDDERFuncaoTransacaoProj;
	}

	public FuncaoTransacaoProjetoTO getFuncaoTransacaoProjetoTO() {
		return funcaoTransacaoProjetoTO;
	}

	public void setFuncaoTransacaoProjetoTO(
			FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {
		this.funcaoTransacaoProjetoTO = funcaoTransacaoProjetoTO;
	}


	
}
