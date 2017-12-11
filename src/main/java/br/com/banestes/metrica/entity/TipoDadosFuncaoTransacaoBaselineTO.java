package br.com.sgpf.metrica.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="TB_TDT_TIPO_DADOS_FT_BASELINE")
@SequenceGenerator(name = "SQ_TDT_TIPO_DADOS_FT_BASELINE", sequenceName = "SQ_TDT_TIPO_DADOS_FT_BASELINE")
public class TipoDadosFuncaoTransacaoBaselineTO extends EntityModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDT_ID_TIPO_DADOS_FT_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDT_TIPO_DADOS_FT_BASELINE")
	private Long idTipoDadosFuncaoTransacao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ARB_ID_ARQ_REFERENCIADO_BSE")
	private ArquivoReferenciadoBaselineTO arquivoReferenciadoBaselineTO;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDT_FL_ATRAVESSA_FRONTEIRA")
	private SimNaoEnum flAtravessaFronteira;
	
	@Column(name="TDT_DS_TIPO_DADOS")
	private String descricao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ATR_ID_ATRIBUTO")
	private AtributoTO atributoTO;

	
	public static TipoDadosFuncaoTransacaoBaselineTO valueOf(TipoDadosFuncaoTransacaoProjetoTO tipoDadosFuncaoTransacaoProjeto){
		
		TipoDadosFuncaoTransacaoBaselineTO tipoDadosFuncaoTransacaoBaselineTO = new TipoDadosFuncaoTransacaoBaselineTO();
		
		tipoDadosFuncaoTransacaoBaselineTO.setAtributoTO(tipoDadosFuncaoTransacaoProjeto.getAtributoTO());
		tipoDadosFuncaoTransacaoBaselineTO.setFlAtravessaFronteira(tipoDadosFuncaoTransacaoProjeto.getFlAtravessaFronteira());
		
		return tipoDadosFuncaoTransacaoBaselineTO;
	}
	
	public AtributoTO getAtributoTO() {
		return atributoTO;
	}

	public void setAtributoTO(AtributoTO atributoTO) {
		this.atributoTO = atributoTO;
	}

	public Long getIdTipoDadosFuncaoTransacao() {
		return idTipoDadosFuncaoTransacao;
	}

	public void setIdTipoDadosFuncaoTransacao(Long idTipoDadosFuncaoTransacao) {
		this.idTipoDadosFuncaoTransacao = idTipoDadosFuncaoTransacao;
	}

	public ArquivoReferenciadoBaselineTO getArquivoReferenciadoBaselineTO() {
		return arquivoReferenciadoBaselineTO;
	}

	public void setArquivoReferenciadoBaselineTO(
			ArquivoReferenciadoBaselineTO arquivoReferenciadoBaselineTO) {
		this.arquivoReferenciadoBaselineTO = arquivoReferenciadoBaselineTO;
	}

	public SimNaoEnum getFlAtravessaFronteira() {
		return flAtravessaFronteira;
	}

	public void setFlAtravessaFronteira(SimNaoEnum flAtravessaFronteira) {
		this.flAtravessaFronteira = flAtravessaFronteira;
	}

	@Override
	public Long getEntityId() {
		return this.idTipoDadosFuncaoTransacao;
	}
	
}