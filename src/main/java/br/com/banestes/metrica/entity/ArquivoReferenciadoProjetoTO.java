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

import br.com.sgpf.metrica.core.entity.EntityModel;

@Entity
@Table(name = "TB_ARP_ARQ_REFERENCIADO_PRO")
@SequenceGenerator(name = "SQ_ARP_ARQ_REFERENCIADO_PRO", sequenceName = "SQ_ARP_ARQ_REFERENCIADO_PRO")
@NamedQueries({ @NamedQuery(name = "ArquivoReferenciadoProjetoTO.deleteByFuncaoTransacao", query = "delete from ArquivoReferenciadoProjetoTO arp where arp.funcaoTransacaoProjetoTO = ?1") })
public class ArquivoReferenciadoProjetoTO extends EntityModel<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ARP_ID_ARQ_REFERENCIADO_PRO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ARP_ARQ_REFERENCIADO_PRO")
	private Long idArquivoReferenciadoProjeto;

	@ManyToOne
	@JoinColumn(name = "FTP_ID_FUNCAO_TRANSACAO_PRO")
	private FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FDP_ID_FUNCAO_DADOS_PROJETO")
	private FuncaoDadosProjetoTO funcaoDadosProjetoTO;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FDB_ID_FUNCAO_DADOS_BASELINE")
	private FuncaoDadosBaselineTO funcaoDadosBaselineTO;

	@OneToMany(mappedBy = "arquivoReferenciadoProjetoTO", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<TipoDadosFuncaoTransacaoProjetoTO> tipoDadosFuncaoTransacaoProjetos;

	public String getNomeArquivoLogico() {

		if (isFuncaoDadosProjeto()) {
			return this.funcaoDadosProjetoTO.getNome();
		} else {
			return this.funcaoDadosBaselineTO.getNome();
		}

	}

	public String getDescricaoArquivoLogico() {

		if (isFuncaoDadosProjeto()) {
			return this.funcaoDadosProjetoTO.getDescricao();
		} else {
			return this.funcaoDadosBaselineTO.getDescricao();
		}

	}

	public boolean isFuncaoDadosProjeto() {
		if (this.funcaoDadosProjetoTO != null) {
			return true;
		} else if (this.funcaoDadosBaselineTO != null) {
			return false;
		} else {
			throw new IllegalStateException("Arquivo referenciado não possui Funcao!");
		}
	}

	public FuncaoDadosProjetoTO getFuncaoDadosProjetoTO() {
		return funcaoDadosProjetoTO;
	}

	public void setFuncaoDadosProjetoTO(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {
		this.funcaoDadosProjetoTO = funcaoDadosProjetoTO;
	}

	public Long getIdArquivoReferenciadoProjeto() {
		return idArquivoReferenciadoProjeto;
	}

	public void setIdArquivoReferenciadoProjeto(Long idArquivoReferenciadoProjeto) {
		this.idArquivoReferenciadoProjeto = idArquivoReferenciadoProjeto;
	}

	public FuncaoTransacaoProjetoTO getFuncaoTransacaoProjetoTO() {
		return funcaoTransacaoProjetoTO;
	}

	public void setFuncaoTransacaoProjetoTO(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {
		this.funcaoTransacaoProjetoTO = funcaoTransacaoProjetoTO;
	}

	@Override
	public Long getEntityId() {
		return this.idArquivoReferenciadoProjeto;
	}

	public List<TipoDadosFuncaoTransacaoProjetoTO> getTipoDadosFuncaoTransacaoProjetos() {
		return tipoDadosFuncaoTransacaoProjetos;
	}

	public void setTipoDadosFuncaoTransacaoProjetos(
			List<TipoDadosFuncaoTransacaoProjetoTO> tipoDadosFuncaoTransacaoProjetos) {
		this.tipoDadosFuncaoTransacaoProjetos = tipoDadosFuncaoTransacaoProjetos;
	}

	public FuncaoDadosBaselineTO getFuncaoDadosBaselineTO() {
		return funcaoDadosBaselineTO;
	}

	public void setFuncaoDadosBaselineTO(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {
		this.funcaoDadosBaselineTO = funcaoDadosBaselineTO;
	}

}