package br.com.sgpf.metrica.entity;

import javax.persistence.CascadeType;
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
@Table(name = "TB_TDR_TIPO_DADOS_DER_BASELINE")
@SequenceGenerator(name = "SQ_TDR_TIPO_DADOS_DER_BASELINE", sequenceName = "SQ_TDR_TIPO_DADOS_DER_BASELINE")
public class TipoDadosDERFuncaoTransacaoBaselineTO extends EntityModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDR_ID_TIPO_DADOS_DER_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDR_TIPO_DADOS_DER_BASELINE")
	private Long idTDDERFuncaoTransacaoBaseline;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="FTB_ID_FUNCAO_TRANSACAO_BSE")
	private FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO;

	@Column(name="TDR_NM_CAMPO")
	private String nmCampo;

	@Column(name="TDR_DS_CAMPO")
	private String dsCampo;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDR_FL_CALCULADO_DERIVADO")
	private SimNaoEnum flCalculadoDerivado;
	
	public static TipoDadosDERFuncaoTransacaoBaselineTO valueOf(TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERFuncaoTransacaoProjeto){
		
		TipoDadosDERFuncaoTransacaoBaselineTO td = new TipoDadosDERFuncaoTransacaoBaselineTO();
		
		td.setDsCampo(tipoDadosDERFuncaoTransacaoProjeto.getDsCampo());
		td.setFlCalculadoDerivado(tipoDadosDERFuncaoTransacaoProjeto.getFlCalculadoDerivado());
		td.setNmCampo(tipoDadosDERFuncaoTransacaoProjeto.getNmCampo());
		
		return td;
	}

	public Long getIdTDDERFuncaoTransacaoBaseline() {
		return idTDDERFuncaoTransacaoBaseline;
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

	@Override
	public Long getEntityId() {
		return this.idTDDERFuncaoTransacaoBaseline;
	}

	public FuncaoTransacaoBaselineTO getFuncaoTransacaoBaselineTO() {
		return funcaoTransacaoBaselineTO;
	}

	public void setFuncaoTransacaoBaselineTO(
			FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {
		this.funcaoTransacaoBaselineTO = funcaoTransacaoBaselineTO;
	}


}
