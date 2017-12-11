package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sgpf.metrica.core.entity.EntityModel;

@Entity(name="BASELINE")
@Table(name = "TB_BSE_BASELINE")
@SequenceGenerator(name = "SQ_BSE_ID_BASELINE", sequenceName = "SQ_BSE_ID_BASELINE")
public class BaselineTO extends EntityModel<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BSE_ID_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BSE_ID_BASELINE")
	private Long idBaseLine;

	@OneToOne
	@JoinColumn(name="SIS_ID_SISTEMA")
	private SistemaTO sistemaTO;

	@Column(name="BSE_VL_PF_FUNCAO_DADOS")
	private BigDecimal valorPFDados;

	@Column(name="BSE_VL_PF_FUNCAO_TRANSACAO")
	private BigDecimal valorPFTransacao;

	@Column(name="BSE_NR_VERSAO")
	private Integer nrVersao;
	
	@OneToMany(mappedBy="baselineTO", fetch=FetchType.LAZY)
	private List<FuncaoTransacaoBaselineTO> funcaoTransacaoBaselineTOs;
	
	@OneToMany(mappedBy="baselineTO", fetch=FetchType.LAZY)
	private List<FuncaoDadosBaselineTO> funcaoDadosBaselineTOs;


	public BaselineTO(){

	}
	
	public BigDecimal getQtPFTotal(){
		BigDecimal vlTrans = BigDecimal.ZERO;
		BigDecimal vlDados = BigDecimal.ZERO;
			
		if(this.valorPFDados != null){
			vlDados = this.valorPFDados;
		}
		
		if(this.valorPFTransacao != null){
			vlTrans = this.valorPFTransacao;
		}
		
		return vlTrans.add(vlDados);
	}

	public Long getIdBaseLine() {
		return idBaseLine;
	}

	public void setIdBaseLine(Long idBaseLine) {
		this.idBaseLine = idBaseLine;
	}

	public SistemaTO getSistemaTO() {
		return sistemaTO;
	}

	public void setSistemaTO(SistemaTO sistemaTO) {
		this.sistemaTO = sistemaTO;
	}

	public BigDecimal getValorPFDados() {
		return valorPFDados;
	}

	public void setValorPFDados(BigDecimal valorPFDados) {
		this.valorPFDados = valorPFDados;
	}

	public void addValorPFTransacao(BigDecimal valor) {
		if(this.valorPFTransacao == null){
			this.valorPFTransacao = BigDecimal.ZERO;
		}
		
		this.valorPFTransacao = this.valorPFTransacao.add(valor);
	}
	
	public void addValorPFDados(BigDecimal valor) {
		if(this.valorPFDados == null){
			this.valorPFDados = BigDecimal.ZERO;
		}
		
		this.valorPFDados = this.valorPFDados.add(valor);
	}
	
	public BigDecimal getValorPFTransacao() {
		return valorPFTransacao;
	}

	public void setValorPFTransacao(BigDecimal valorPFTransacao) {
		this.valorPFTransacao = valorPFTransacao;
	}

	public Integer getNrVersao() {
		return nrVersao;
	}

	public void setNrVersao(Integer nrVersao) {
		this.nrVersao = nrVersao;
	}

	public List<FuncaoTransacaoBaselineTO> getFuncaoTransacaoBaselineTOs() {
		return funcaoTransacaoBaselineTOs;
	}

	public void setFuncaoTransacaoBaselineTOs(
			List<FuncaoTransacaoBaselineTO> funcaoTransacaoBaselineTOs) {
		this.funcaoTransacaoBaselineTOs = funcaoTransacaoBaselineTOs;
	}

	public List<FuncaoDadosBaselineTO> getFuncaoDadosBaselineTOs() {
		return funcaoDadosBaselineTOs;
	}

	public void setFuncaoDadosBaselineTOs(
			List<FuncaoDadosBaselineTO> funcaoDadosBaselineTOs) {
		this.funcaoDadosBaselineTOs = funcaoDadosBaselineTOs;
	}

	@Override
	public Long getEntityId() {
		return this.idBaseLine;
	}

	public void updateBaselineValues()
	{
		BigDecimal sum = BigDecimal.ZERO;
		for(FuncaoTransacaoBaselineTO ftb : funcaoTransacaoBaselineTOs)
			sum = sum.add(ftb.getValorPontoFuncao());
				
		valorPFTransacao = sum;
		
		sum = BigDecimal.ZERO;
		for(FuncaoDadosBaselineTO fdb : funcaoDadosBaselineTOs)
			sum = sum.add(fdb.getValorPontoFuncao());
		
		valorPFDados = sum;
		
	
			
	}
	
}

