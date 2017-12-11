package br.com.sgpf.metrica.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import br.com.sgpf.metrica.core.annotation.LikeCriteria;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.MatchModeEnum;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;
import br.com.sgpf.metrica.excel.fpa.CabecalhoPlanilhaFPA;

@Entity
@Table( name = "TB_PRO_PROJETO" )
@SequenceGenerator( name = "SQ_PRO_ID_PROJETO", sequenceName = "SQ_PRO_ID_PROJETO" )
public class ProjetoTO extends EntityModel<Long> implements
		CabecalhoPlanilhaFPA
{

	private static final long serialVersionUID = 1L;

	@Id
	@Column( name = "PRO_ID_PROJETO" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SQ_PRO_ID_PROJETO" )
	private Long idProjeto;

	@Transient
	private EmpresaTO empresaTransient;

	@ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
	@JoinColumn( name = "SIS_ID_SISTEMA" )
	private SistemaTO sistemaTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "CTR_ID_CONTRATO" )
	private ContratoTO contratoTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "ANL_ID_RESP_PDCASE" )
	private AnalistaTO responsavelPDCaseTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "ANL_ID_RESP_PRINCIPAL_CLIENTE" )
	private AnalistaTO responsavelPrincipalClienteTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "ANL_ID_RESP_ENVOLVIDO_CLIENTE" )
	private AnalistaTO responsavelEnvolvidoClientTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "ANL_ID_GESTOR_PRINCIPAL" )
	private AnalistaTO gestorPrincipalTO;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "ANL_ID_GESTOR_ENVOLVIDO" )
	private AnalistaTO gestorEnvolvidoTO;

	@Transient
	private Long idSistema;

	@Transient
	private AnalistaTO analistaTO;

	@Transient
	private Long idAnalistaEmpresa;

	@LikeCriteria( caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE )
	@Column( name = "PRO_DS_PROJETO" )
	private String projeto;

	@LikeCriteria( caseSensitive = false, matchMode = MatchModeEnum.ANYWHERE )
	@Column( name = "PRO_DS_SUBPROJETO" )
	private String subProjeto;

	@Column( name = "PRO_DS_TITULO" )
	private String titulo;

	@Column( name = "PRO_QT_PF_FUNCAO_DADOS" )
	private BigDecimal qtPontoFuncaoDados;

	@Column( name = "PRO_QT_PF_FUNCAO_TRANSACAO" )
	private BigDecimal qtPontoFuncaoTransacao;

	@Column( name = "PRO_QT_PF_LOCAL" )
	private BigDecimal qtPontoFuncaoLocal;

	@Column( name = "PRO_QT_PF_FUNCAO_MENSURAVEL" )
	private BigDecimal qtPontoFuncaoMensuravel;

	@Column( name = "PRO_QT_PF_SERV_NAO_MENSURAVEL" )
	private BigDecimal qtPontoFuncaoServicoNaoMensuravel;

	@Column( name = "PRO_VS_VISAO_GERAL_SOLUCAO", length = 500 )
	private String visaoGeralSolucao;

	@Column( name = "PRO_CT_CONTAGEM" )
	private String contagem;

	@Column( name = "PRO_DT_DATA_CONTAGEM" )
	private Date dtContagem;

	@Column( name = "PRO_DT_CRIACAO" )
	@Temporal( TemporalType.TIMESTAMP )
	private Date dtCriacao;

	@Type( type = "statusProjetoEnumUserType" )
	@Column( name = "PRO_ST_STATUS" )
	private StatusProjetoEnum status;

	@OneToMany( mappedBy = "projetoTO", fetch = FetchType.LAZY )
	private List<FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoTOList;

	@OneToMany( mappedBy = "projetoTO", fetch = FetchType.LAZY )
	private List<FuncaoDadosProjetoTO> funcaoDadosProjetoTOList;

	@OneToMany( mappedBy = "projetoTO", fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE } )
	private List<ServicoNaoMensuravelTO> servicoNaoMensuravelTOs;

	@Transient
	private FuncaoDadosProjetoTO funcadoDadosProjetoTOTransient;

	@Transient
	private FuncaoTransacaoProjetoTO funcadoTrasacaoProjetoTOTransient;

	// @Transient
	@ManyToMany
	@JoinTable( name = "TB_PRS_PROJETO_SISTEMA", joinColumns = { @JoinColumn( name = "PRO_ID_PROJETO" ) }, inverseJoinColumns = { @JoinColumn( name = "SIS_ID_SISTEMA" ) } )
	private List<SistemaTO> sistemasEnvolvidos;

	@Transient
	private String dsSistemasEnvolvidos;

	@Version
	private Long version;

	public BigDecimal getPfTotal()
	{
		
		BigDecimal vlPFMens = BigDecimal.ZERO;
		BigDecimal vlPFNaoMens = BigDecimal.ZERO;

		if ( this.getQtPontoFuncaoMensuravel() != null )
		{
			vlPFMens = this.getQtPontoFuncaoMensuravel();
		}

		if ( this.getQtPontoFuncaoServicoNaoMensuravel() != null )
		{
			vlPFNaoMens = this.getQtPontoFuncaoServicoNaoMensuravel();
		}

		return vlPFMens.add(vlPFNaoMens);
	}


	public BigDecimal getQtPontoFuncaoDados()
	{
		// TODO: TESTE
		// qtPontoFuncaoDados = new BigDecimal((new
		// Random()).nextInt(8)).multiply(new BigDecimal(100));
		return qtPontoFuncaoDados;
	}

	public void setQtPontoFuncaoDados( BigDecimal qtPontoFuncaoDados )
	{
		this.qtPontoFuncaoDados = qtPontoFuncaoDados;
	}

	public void addQtPontoFuncaoDados( BigDecimal qtPontoFuncaoDados )
	{
		if ( this.qtPontoFuncaoDados == null )
		{
			this.qtPontoFuncaoDados = BigDecimal.ZERO;
		}

		if ( qtPontoFuncaoDados != null )
		{
			this.qtPontoFuncaoDados = this.qtPontoFuncaoDados
					.add(qtPontoFuncaoDados);
		}

	}

	public BigDecimal getQtPontoFuncaoTransacao()
	{
		// TODO: TESTE
		// qtPontoFuncaoTransacao = new BigDecimal((new
		// Random()).nextInt(8)).multiply(new BigDecimal(100));
		return qtPontoFuncaoTransacao;
	}

	public void setQtPontoFuncaoTransacao( BigDecimal qtPontoFuncaoTransacao )
	{
		this.qtPontoFuncaoTransacao = qtPontoFuncaoTransacao;
	}

	public void addQtPontoFuncaoTransacao( BigDecimal qtPontoFuncaoTransacao )
	{
		if ( this.qtPontoFuncaoTransacao == null )
		{
			this.qtPontoFuncaoTransacao = BigDecimal.ZERO;
		}

		if ( qtPontoFuncaoTransacao != null )
		{
			this.qtPontoFuncaoTransacao = qtPontoFuncaoTransacao
					.add(qtPontoFuncaoTransacao);
		}

	}

	public BigDecimal getQtPontoFuncaoMensuravel()
	{

		return qtPontoFuncaoMensuravel;
	}

	public void setQtPontoFuncaoMensuravel( BigDecimal qtPontoFuncaoMensuravel )
	{
		this.qtPontoFuncaoMensuravel = qtPontoFuncaoMensuravel;
	}

	public void addQtPontoFuncaoMensuravel( BigDecimal qtPontoFuncaoMensuravel )
	{
		if ( this.qtPontoFuncaoMensuravel == null )
		{
			this.qtPontoFuncaoMensuravel = BigDecimal.ZERO;
		}

		if ( qtPontoFuncaoMensuravel != null )
		{
			this.qtPontoFuncaoMensuravel = this.qtPontoFuncaoMensuravel
					.add(qtPontoFuncaoMensuravel);
		}

	}

	public BigDecimal getQtPontoFuncaoServicoNaoMensuravel()
	{

		return qtPontoFuncaoServicoNaoMensuravel;
	}

	public void setQtPontoFuncaoServicoNaoMensuravel(
			BigDecimal qtPontoFuncaoServicoNaoMensuravel )
	{
		this.qtPontoFuncaoServicoNaoMensuravel = qtPontoFuncaoServicoNaoMensuravel;
	}

	public void addQtPontoFuncaoServicoNaoMensuravel(
			BigDecimal qtPontoFuncaoServicoNaoMensuravel )
	{
		if ( this.qtPontoFuncaoServicoNaoMensuravel == null )
		{
			this.qtPontoFuncaoServicoNaoMensuravel = BigDecimal.ZERO;
		}

		if ( qtPontoFuncaoServicoNaoMensuravel != null )
		{
			this.qtPontoFuncaoServicoNaoMensuravel = this.qtPontoFuncaoServicoNaoMensuravel
					.add(qtPontoFuncaoServicoNaoMensuravel);
		}
	}

	public Long getIdSistema()
	{
		return idSistema;
	}

	public void setIdSistema( Long idSistema )
	{
		this.idSistema = idSistema;
	}

	public SistemaTO getSistemaTO()
	{
		return sistemaTO;
	}

	public void setSistemaTO( SistemaTO sistemaTO )
	{
		this.sistemaTO = sistemaTO;
	}

	public List<ServicoNaoMensuravelTO> getServicoNaoMensuravelTOs()
	{
		return servicoNaoMensuravelTOs;
	}

	public void setServicoNaoMensuravelTOs(
			List<ServicoNaoMensuravelTO> servicoNaoMensuravelTOs )
	{
		this.servicoNaoMensuravelTOs = servicoNaoMensuravelTOs;
	}

	public AnalistaTO getAnalistaTO()
	{
		return analistaTO;
	}

	public void setAnalistaTO( AnalistaTO analistaTO )
	{
		this.analistaTO = analistaTO;
	}

	public Long getIdAnalistaEmpresa()
	{
		return idAnalistaEmpresa;
	}

	public void setIdAnalistaEmpresa( Long idAnalistaEmpresa )
	{
		this.idAnalistaEmpresa = idAnalistaEmpresa;
	}

	public String getProjeto()
	{
		return projeto;
	}

	public void setProjeto( String projeto )
	{
		this.projeto = projeto;
	}

	public String getSubProjeto()
	{
		return subProjeto;
	}

	public Date getDtCriacao()
	{
		return dtCriacao;
	}

	public void setDtCriacao( Date dtCriacao )
	{
		this.dtCriacao = dtCriacao;
	}

	public void setSubProjeto( String subProjeto )
	{
		this.subProjeto = subProjeto;
	}

	public String getTitulo()
	{
		return titulo;
	}

	public void setTitulo( String titulo )
	{
		this.titulo = titulo;
	}

	public BigDecimal getQtPontoFuncaoLocal()
	{
		// TODO: TESTE
		// qtPontoFuncaoLocal = (new BigDecimal(new
		// Random().nextInt(8))).multiply(new BigDecimal(100));

		return qtPontoFuncaoLocal;
	}

	public void setQtPontoFuncaoLocal( BigDecimal qtPontoFuncaoLocal )
	{
		this.qtPontoFuncaoLocal = qtPontoFuncaoLocal;
	}

	public void addQtPontoFuncaoLocal( BigDecimal qtPontoFuncaoLocal )
	{
		if ( this.qtPontoFuncaoLocal == null )
		{
			this.qtPontoFuncaoLocal = BigDecimal.ZERO;
		}

		if ( qtPontoFuncaoLocal != null )
		{
			this.qtPontoFuncaoLocal = this.qtPontoFuncaoLocal
					.add(qtPontoFuncaoLocal);
		}

	}

	public String getContagem()
	{
		return contagem;
	}

	public void setContagem( String contagem )
	{
		this.contagem = contagem;
	}

	public Date getDtContagem()
	{
		return dtContagem;
	}

	public void setDtContagem( Date dtContagem )
	{
		this.dtContagem = dtContagem;
	}

	public StatusProjetoEnum getStatus()
	{
		return status;
	}

	public void setStatus( StatusProjetoEnum status )
	{
		this.status = status;
	}

	public List<FuncaoDadosProjetoTO> getFuncaoDadosProjetoTOList()
	{
		return funcaoDadosProjetoTOList;
	}

	public void setFuncaoDadosProjetoTOList(
			List<FuncaoDadosProjetoTO> funcaoDadosProjetoTOList )
	{
		this.funcaoDadosProjetoTOList = funcaoDadosProjetoTOList;
	}

	/**
	 * @return the contratoTO
	 */
	public ContratoTO getContratoTO()
	{
		return contratoTO;
	}

	/**
	 * @param contratoTO
	 *            the contratoTO to set
	 */
	public void setContratoTO( ContratoTO contratoTO )
	{
		this.contratoTO = contratoTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.sgpf.metrica.core.entity.EntityModel#getEntityId()
	 */
	@Override
	public Long getEntityId()
	{
		return getIdProjeto();
	}

	/**
	 * @return the idProjeto
	 */
	public Long getIdProjeto()
	{
		return idProjeto;
	}

	/**
	 * @param idProjeto
	 *            the idProjeto to set
	 */
	public void setIdProjeto( Long idProjeto )
	{
		this.idProjeto = idProjeto;
	}

	/**
	 * @return the responsavelPDCaseTO
	 */
	public AnalistaTO getResponsavelPDCaseTO()
	{
		return responsavelPDCaseTO;
	}

	/**
	 * @param responsavelPDCaseTO
	 *            the responsavelPDCaseTO to set
	 */
	public void setResponsavelPDCaseTO( AnalistaTO responsavelPDCaseTO )
	{
		this.responsavelPDCaseTO = responsavelPDCaseTO;
	}

	/**
	 * @return the responsavelPrincipalClienteTO
	 */
	public AnalistaTO getResponsavelPrincipalClienteTO()
	{
		return responsavelPrincipalClienteTO;
	}

	/**
	 * @param responsavelPrincipalClienteTO
	 *            the responsavelPrincipalClienteTO to set
	 */
	public void setResponsavelPrincipalClienteTO(
			AnalistaTO responsavelPrincipalClienteTO )
	{
		this.responsavelPrincipalClienteTO = responsavelPrincipalClienteTO;
	}

	/**
	 * @return the responsavelEnvolvidoClientTO
	 */
	public AnalistaTO getResponsavelEnvolvidoClientTO()
	{
		return responsavelEnvolvidoClientTO;
	}

	/**
	 * @param responsavelEnvolvidoClientTO
	 *            the responsavelEnvolvidoClientTO to set
	 */
	public void setResponsavelEnvolvidoClientTO(
			AnalistaTO responsavelEnvolvidoClientTO )
	{
		this.responsavelEnvolvidoClientTO = responsavelEnvolvidoClientTO;
	}

	/**
	 * @return the gestorPrincipalTO
	 */
	public AnalistaTO getGestorPrincipalTO()
	{
		return gestorPrincipalTO;
	}

	/**
	 * @param gestorPrincipalTO
	 *            the gestorPrincipalTO to set
	 */
	public void setGestorPrincipalTO( AnalistaTO gestorPrincipalTO )
	{
		this.gestorPrincipalTO = gestorPrincipalTO;
	}

	/**
	 * @return the gestorEnvolvidoTO
	 */
	public AnalistaTO getGestorEnvolvidoTO()
	{
		return gestorEnvolvidoTO;
	}

	/**
	 * @param gestorEnvolvidoTO
	 *            the gestorEnvolvidoTO to set
	 */
	public void setGestorEnvolvidoTO( AnalistaTO gestorEnvolvidoTO )
	{
		this.gestorEnvolvidoTO = gestorEnvolvidoTO;
	}

	/**
	 * @return the empresaTransient
	 */
	public EmpresaTO getEmpresaTransient()
	{
		return empresaTransient;
	}

	/**
	 * @param empresaTransient
	 *            the empresaTransient to set
	 */
	public void setEmpresaTransient( EmpresaTO empresaTransient )
	{
		this.empresaTransient = empresaTransient;
	}

	public List<FuncaoTransacaoProjetoTO> getFuncaoTransacaoProjetoTOList()
	{
		return funcaoTransacaoProjetoTOList;
	}

	public void setFuncaoTransacaoProjetoTOList(
			List<FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoTOList )
	{
		this.funcaoTransacaoProjetoTOList = funcaoTransacaoProjetoTOList;
	}

	@Override
	public Date getDataRevisao()
	{
		return new Date();
	}

	@Override
	public String getFornecedor()
	{
		return "PD CASE Informática LTDA";
	}

	@Override
	public String getSistema()
	{
		return this.getSistemaTO() != null ? this.getSistemaTO().getCdSistema()
				+ " - " + this.getSistemaTO().getDsSistema() : "";
	}

	@Override
	public String getAnalistaFornecedor()
	{
		return this.responsavelPDCaseTO != null ? this.responsavelPDCaseTO
				.getNomeAnalista() : "";
	}

	@Override
	public String getAnalistaCliente()
	{
		return this.responsavelPrincipalClienteTO != null ? this.responsavelPrincipalClienteTO
				.getNomeAnalista() : "";
	}

	/**
	 * @return the visaoGeralSolucao
	 */
	public String getVisaoGeralSolucao()
	{
		return visaoGeralSolucao;
	}

	/**
	 * @param visaoGeralSolucao
	 *            the visaoGeralSolucao to set
	 */
	public void setVisaoGeralSolucao( String visaoGeralSolucao )
	{
		this.visaoGeralSolucao = visaoGeralSolucao;
	}

	/**
	 * @return the funcadoDadosProjetoTOTransient
	 */
	public FuncaoDadosProjetoTO getFuncadoDadosProjetoTOTransient()
	{
		return funcadoDadosProjetoTOTransient;
	}

	/**
	 * @param funcadoDadosProjetoTOTransient
	 *            the funcadoDadosProjetoTOTransient to set
	 */
	public void setFuncadoDadosProjetoTOTransient(
			FuncaoDadosProjetoTO funcadoDadosProjetoTOTransient )
	{
		this.funcadoDadosProjetoTOTransient = funcadoDadosProjetoTOTransient;
	}

	public List<SistemaTO> getSistemasEnvolvidos()
	{
		return sistemasEnvolvidos;
	}

	public void setSistemasEnvolvidos( List<SistemaTO> sistemasEnvolvidos )
	{
		this.sistemasEnvolvidos = sistemasEnvolvidos;
	}

	public FuncaoTransacaoProjetoTO getFuncadoTrasacaoProjetoTOTransient()
	{
		return funcadoTrasacaoProjetoTOTransient;
	}

	public void setFuncadoTrasacaoProjetoTOTransient(
			FuncaoTransacaoProjetoTO funcadoTrasacaoProjetoTOTransient )
	{
		this.funcadoTrasacaoProjetoTOTransient = funcadoTrasacaoProjetoTOTransient;
	}

	public String getDsSistemasEnvolvidos()
	{
		StringBuilder sistemas = new StringBuilder(" ");
		if ( sistemasEnvolvidos != null && !sistemasEnvolvidos.isEmpty() )
		{
			int nrSis = 0;
			for ( SistemaTO sistemaTO : sistemasEnvolvidos )
			{
				if ( nrSis++ > 0 )
				{
					sistemas.append(", ");
				}
				sistemas.append(sistemaTO.getCdSistema() + " - "
						+ sistemaTO.getSistema());
			}
			dsSistemasEnvolvidos = sistemas.toString();
		} else
		{
			dsSistemasEnvolvidos = "Não se aplica";
		}
		return dsSistemasEnvolvidos;
	}

	@Override
	public String getTituloPlanilha()
	{
		return String.format("%s/%s - %s", this.getProjeto(),
				this.getSubProjeto(), this.getTitulo());
	}

	
	
	
	
	public ProjetoTO updateValorPf()
	{

		qtPontoFuncaoLocal = BigDecimal.ZERO;
		qtPontoFuncaoDados = BigDecimal.ZERO;
		qtPontoFuncaoTransacao = BigDecimal.ZERO;
		qtPontoFuncaoServicoNaoMensuravel = BigDecimal.ZERO;
		
		for ( FuncaoDadosProjetoTO fdo : funcaoDadosProjetoTOList )
		{
			qtPontoFuncaoLocal = qtPontoFuncaoLocal.add(fdo
					.getValorPontoFuncaoLocal());
			qtPontoFuncaoDados = qtPontoFuncaoDados.add(fdo
					.getValorPontoFuncao());
		}

		for ( FuncaoTransacaoProjetoTO fto : funcaoTransacaoProjetoTOList )
		{
			qtPontoFuncaoLocal = qtPontoFuncaoLocal.add(fto
					.getValorPontoFuncaoLocal());
			qtPontoFuncaoTransacao = qtPontoFuncaoTransacao.add(fto
					.getValorPontoFuncao());

		}
		
		qtPontoFuncaoMensuravel = qtPontoFuncaoLocal;
		

		// Servico Nao Mensuravel

		for ( ServicoNaoMensuravelTO snm : servicoNaoMensuravelTOs )
		{
			qtPontoFuncaoServicoNaoMensuravel = qtPontoFuncaoServicoNaoMensuravel
					.add(snm.getQtPontoFuncaoFinal());
			qtPontoFuncaoLocal = qtPontoFuncaoLocal.add(snm.getQtPontoFuncaoFinal());
		}
		
		return this;

	}

}
