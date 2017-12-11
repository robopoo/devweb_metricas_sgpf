package br.com.sgpf.metrica.entity;

import java.util.ArrayList;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sgpf.metrica.core.entity.EntityModel;

@Entity
@Table(name="TB_ARB_ARQ_REFERENCIADO_BSE")
@SequenceGenerator(name = "SQ_ARB_ARQ_REFERENCIADO_BSE", sequenceName = "SQ_ARB_ARQ_REFERENCIADO_BSE")
public class ArquivoReferenciadoBaselineTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ARB_ID_ARQ_REFERENCIADO_BSE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ARB_ARQ_REFERENCIADO_BSE")
	private Long idArquivoReferenciadoProjeto;

	@ManyToOne
	@JoinColumn(name="FTB_ID_FUNCAO_TRANSACAO_BSE")
	private FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO;
	
	@ManyToOne
	@JoinColumn(name="FDB_ID_FUNCAO_DADOS_BASELINE")
	private FuncaoDadosBaselineTO funcaoDadosBaselineTO;
	
	@OneToMany(mappedBy="arquivoReferenciadoBaselineTO", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<TipoDadosFuncaoTransacaoBaselineTO> tipoDadosFuncaoTransacaoBaselineTOs; 
	
	public static ArquivoReferenciadoBaselineTO valueOf(ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO){
		
		ArquivoReferenciadoBaselineTO arquivoReferenciadoBaselineTO = new ArquivoReferenciadoBaselineTO();
		
		if(arquivoReferenciadoProjetoTO.isFuncaoDadosProjeto()){
			arquivoReferenciadoBaselineTO.setFuncaoDadosBaselineTO(arquivoReferenciadoProjetoTO.getFuncaoDadosProjetoTO().getFuncaoDadosBaselineTO());
		}else{
			//Arquivo referenciado criado a partir de uma função de dados do Baseline
			arquivoReferenciadoBaselineTO.setFuncaoDadosBaselineTO(arquivoReferenciadoProjetoTO.getFuncaoDadosBaselineTO());
		}
		
		List<TipoDadosFuncaoTransacaoBaselineTO> tds = new ArrayList<TipoDadosFuncaoTransacaoBaselineTO>(arquivoReferenciadoProjetoTO.getTipoDadosFuncaoTransacaoProjetos().size());
		TipoDadosFuncaoTransacaoBaselineTO td;
		for (TipoDadosFuncaoTransacaoProjetoTO funcao : arquivoReferenciadoProjetoTO.getTipoDadosFuncaoTransacaoProjetos()) {
			td = TipoDadosFuncaoTransacaoBaselineTO.valueOf(funcao);
			td.setArquivoReferenciadoBaselineTO(arquivoReferenciadoBaselineTO);
			tds.add(td);
		}
		
		arquivoReferenciadoBaselineTO.setTipoDadosFuncaoTransacaoBaselineTOs(tds);
		
		return arquivoReferenciadoBaselineTO;
	}
	
	public Long getIdArquivoReferenciadoProjeto() {
		return idArquivoReferenciadoProjeto;
	}

	public void setIdArquivoReferenciadoProjeto(Long idArquivoReferenciadoProjeto) {
		this.idArquivoReferenciadoProjeto = idArquivoReferenciadoProjeto;
	}

	public FuncaoTransacaoBaselineTO getFuncaoTransacaoBaselineTO() {
		return funcaoTransacaoBaselineTO;
	}

	public FuncaoDadosBaselineTO getFuncaoDadosBaselineTO() {
		return funcaoDadosBaselineTO;
	}

	public void setFuncaoDadosBaselineTO(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {
		this.funcaoDadosBaselineTO = funcaoDadosBaselineTO;
	}

	public void setFuncaoTransacaoBaselineTO(
			FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {
		this.funcaoTransacaoBaselineTO = funcaoTransacaoBaselineTO;
	}

	@Override
	public Long getEntityId() {
		return this.idArquivoReferenciadoProjeto;
	}
	
	public List<TipoDadosFuncaoTransacaoBaselineTO> getTipoDadosFuncaoTransacaoBaselineTOs() {
		return tipoDadosFuncaoTransacaoBaselineTOs;
	}

	public void setTipoDadosFuncaoTransacaoBaselineTOs(
			List<TipoDadosFuncaoTransacaoBaselineTO> tipoDadosFuncaoTransacaoBaselineTOs) {
		this.tipoDadosFuncaoTransacaoBaselineTOs = tipoDadosFuncaoTransacaoBaselineTOs;
	}

}