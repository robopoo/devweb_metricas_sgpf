package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;

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
import br.com.sgpf.metrica.enumeration.StatusAvaliacaoEnum;
import br.com.sgpf.metrica.excel.fpa.ServicoNaoMensuravelPlanilhaFPA;

@Entity
@Table(name = "TB_SNM_SERVICO_NAO_MENSURAVEL")
@SequenceGenerator(name = "SQ_SNM_SERVICO_NAO_MENSURAVEL", sequenceName = "SQ_SNM_SERVICO_NAO_MENSURAVEL")
public class ServicoNaoMensuravelTO extends EntityModel<Long> implements ServicoNaoMensuravelPlanilhaFPA, Aprovacao{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SNM_ID_SERVICO_NAO_MENSURAVEL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SNM_SERVICO_NAO_MENSURAVEL")
	private Long idFuncaoNaoMensuravel;
	
	@Column(name="SNM_NM_SERVICO")
	private String nome;
	
	@Column(name="SNM_DS_SERVICO")
	private String descricao;
	
	@Column(name="SNM_DS_MANUTENCAO_SERVICO")
	private String descricaoManutencao;
	
	@Column(name="SNM_DS_COMENTARIO_REJEICAO")
	private String comentarioRejeicao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRO_ID_PROJETO")
	private ProjetoTO projetoTO;
	
	@Column(name="SNM_QT_ITENS")
	private Integer qtItens;
	
	@Type(type="statusAvaliacaoEnumUserType")
	@Column(name="SNM_ST_CONTAGEM")
	private StatusAvaliacaoEnum situacaoContagem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ELC_ID_ELEMENTO_CONTAGEM")
	private ElementoContagemTO elementoContagemTO;
	
	@Column(name="SNM_VL_PF_REFERENCIA")
	private BigDecimal qtPontoFuncaoReferencia;
	
	@Column(name="SNM_QT_PF_FINAL")
	private BigDecimal qtPontoFuncaoFinal;
	
	public Long getIdFuncaoNaoMensuravel() {
		return idFuncaoNaoMensuravel;
	}

	public void setIdFuncaoNaoMensuravel(Long idFuncaoNaoMensuravel) {
		this.idFuncaoNaoMensuravel = idFuncaoNaoMensuravel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoManutencao() {
		return descricaoManutencao;
	}

	public void setDescricaoManutencao(String descricaoManutencao) {
		this.descricaoManutencao = descricaoManutencao;
	}

	public String getComentarioRejeicao() {
		return comentarioRejeicao;
	}

	public void setComentarioRejeicao(String comentarioRejeicao) {
		this.comentarioRejeicao = comentarioRejeicao;
	}

	public ProjetoTO getProjetoTO() {
		return projetoTO;
	}

	public void setProjetoTO(ProjetoTO projetoTO) {
		this.projetoTO = projetoTO;
	}

	public Integer getQtItens() {
		return qtItens;
	}

	public void setQtItens(Integer qtItens) {
		this.qtItens = qtItens;
	}

	public StatusAvaliacaoEnum getSituacaoContagem() {
		return situacaoContagem;
	}

	public void setSituacaoContagem(StatusAvaliacaoEnum situacaoContagem) {
		this.situacaoContagem = situacaoContagem;
	}

	public ElementoContagemTO getElementoContagemTO() {
		return elementoContagemTO;
	}

	public void setElementoContagemTO(ElementoContagemTO elementoContagemTO) {
		this.elementoContagemTO = elementoContagemTO;
	}

	public BigDecimal getQtPontoFuncaoReferencia() {
		return qtPontoFuncaoReferencia;
	}

	public void setQtPontoFuncaoReferencia(BigDecimal qtPontoFuncaoReferencia) {
		this.qtPontoFuncaoReferencia = qtPontoFuncaoReferencia;
	}
	

	public BigDecimal getQtPontoFuncaoFinal() {
		return qtPontoFuncaoFinal;
	}

	public void setQtPontoFuncaoFinal(BigDecimal qtPontoFuncaoFinal) {
		this.qtPontoFuncaoFinal = qtPontoFuncaoFinal;
	}

	@Override
	public Long getEntityId() {
		return this.idFuncaoNaoMensuravel;
	}

	@Override
	public String getCdElementoContagem(){
		if(this.elementoContagemTO == null 
				|| this.elementoContagemTO.getCodElementoContagem() == null){
			return "";
		}
		
		return this.getElementoContagemTO().getCodElementoContagem();
	}
	
	@Override
	public String getQuantidadeItensPlanilha(){
		if(this.getQtItens() == null){
			return "";
		}
		
		return this.getQtItens().toString();
	}
	
	public boolean isAprovado(){
		return StatusAvaliacaoEnum.APROVADO.equals(this.situacaoContagem);
	}
	
	public void setAprovado(boolean aprovado){
		this.situacaoContagem = aprovado ? StatusAvaliacaoEnum.APROVADO : StatusAvaliacaoEnum.REJEITADO; 
	}

	
}
