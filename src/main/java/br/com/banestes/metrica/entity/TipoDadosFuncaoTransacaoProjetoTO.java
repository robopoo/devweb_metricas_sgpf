package br.com.sgpf.metrica.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;

@Entity
@Table(name="TB_TDP_TIPO_DADOS_FT_PROJETO")
@SequenceGenerator(name = "SQ_TDP_TIPO_DADOS_FT_PROJETO", sequenceName = "SQ_TDP_TIPO_DADOS_FT_PROJETO")
@NamedQueries({ @NamedQuery(name = "TipoDadosFuncaoTransacaoProjetoTO.deleteByFuncaoTransacao", query = "delete from TipoDadosFuncaoTransacaoProjetoTO tdf where tdf.idTipoDadosFuncaoTransacao in "
		+ "(select obj.idTipoDadosFuncaoTransacao from TipoDadosFuncaoTransacaoProjetoTO obj join obj.arquivoReferenciadoProjetoTO arp where arp.funcaoTransacaoProjetoTO = ?1)") })
public class TipoDadosFuncaoTransacaoProjetoTO extends EntityModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TDP_ID_TIPO_DADOS_FT_PROJETO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TDP_TIPO_DADOS_FT_PROJETO")
	private Long idTipoDadosFuncaoTransacao;

	@ManyToOne
	@JoinColumn(name="ARP_ID_ARQ_REFERENCIADO_PRO")
	private ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO;

	@Type(type = "simNaoEnumUserType")
	@Column(name = "TDP_FL_ATRAVESSA_FRONTEIRA")
	private SimNaoEnum flAtravessaFronteira;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ATR_ID_ATRIBUTO")
	private AtributoTO atributoTO;
	
	public Long getIdTipoDadosFuncaoTransacao() {
		return idTipoDadosFuncaoTransacao;
	}

	public void setIdTipoDadosFuncaoTransacao(Long idTipoDadosFuncaoTransacao) {
		this.idTipoDadosFuncaoTransacao = idTipoDadosFuncaoTransacao;
	}

	public ArquivoReferenciadoProjetoTO getArquivoReferenciadoProjetoTO() {
		return arquivoReferenciadoProjetoTO;
	}

	public void setArquivoReferenciadoProjetoTO(
			ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO) {
		this.arquivoReferenciadoProjetoTO = arquivoReferenciadoProjetoTO;
	}

	public SimNaoEnum getFlAtravessaFronteira() {
		return flAtravessaFronteira;
	}
	public Boolean getAtravessaFronteira(){
		return getFlAtravessaFronteira() == SimNaoEnum.SIM ? true : false;
	}

	public void setFlAtravessaFronteira(SimNaoEnum flAtravessaFronteira) {
		
		this.flAtravessaFronteira = flAtravessaFronteira;
	}

	public void setAtravessaFronteira(Boolean value){
		if(value == null){
			setFlAtravessaFronteira(null);
		}else{
			setFlAtravessaFronteira(value ? SimNaoEnum.SIM : SimNaoEnum.NAO);
		}
	}
	public AtributoTO getAtributoTO() {
		return atributoTO;
	}

	public void setAtributoTO(AtributoTO atributoTO) {
		this.atributoTO = atributoTO;
	}

	@Override
	public Long getEntityId() {
		return this.idTipoDadosFuncaoTransacao;
	}


	
}