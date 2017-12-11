package br.com.sgpf.metrica.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

@Entity
@Table( name = "TB_FTB_FUNCAO_TRANSACAO_BSE" )
@AttributeOverrides( {

		@AttributeOverride( name = "nome" , column = @Column( name = "FTB_NM_TRANSACAO" ) ),
		@AttributeOverride( name = "situacaoContagem" , column = @Column( name = "FTB_ST_CONTAGEM" ) ),
		@AttributeOverride( name = "descricao" , column = @Column( name = "FTB_DS_TRANSACAO" ) ),
		@AttributeOverride( name = "comentarioRejeicao" , column = @Column( name = "FTB_DS_COMENTARIO_REJEICAO" ) ),
		@AttributeOverride( name = "qtTipoDados" , column = @Column( name = "FTB_QT_TIPO_DADOS" ) ),
		@AttributeOverride( name = "qtArquivoReferenciado" , column = @Column( name = "FTB_QT_ARQUIVO_REFERENCIADO" ) ),
		@AttributeOverride( name = "valorPontoFuncao" , column = @Column( name = "FTB_VL_PF" ) ),
		@AttributeOverride( name = "complexidadeTP" , column = @Column( name = "FTB_TP_COMPLEXIDADE" ) ),
		@AttributeOverride( name = "funcaoTransacaoTP" , column = @Column( name = "FTB_TP_FUNCAO" ) ) } )
@NamedQueries( {
		@NamedQuery( name = "FuncaoTransacaoBaselineTO.findDescricaoARs" , query = "select fdb.nome from ArquivoReferenciadoBaselineTO arb join arb.funcaoDadosBaselineTO fdb where arb.funcaoTransacaoBaselineTO = ?1 order by fdb.nome" ),
		@NamedQuery( name = "FuncaoTransacaoBaselineTO.deleteFilhos" , query = "delete from ArquivoReferenciadoBaselineTO arb where arb.funcaoTransacaoBaselineTO = ?1" ) } )
@NamedNativeQuery( name = "FuncaoTransacaoBaselineTO.findDescricaoTDs" , query = " SELECT atr.atr_nm_atributo as nome "
		+ " FROM tb_arb_arq_referenciado_bse arb, tb_tdt_tipo_dados_ft_baseline tdt, tb_atr_atributo atr "
		+ " where arb.arb_id_arq_referenciado_bse = tdt.arb_id_arq_referenciado_bse "
		+ " and atr.atr_id_atributo = tdt.atr_id_atributo "
		+ " and arb.ftb_id_funcao_transacao_bse = ?1 "
		+ " and tdt.tdt_fl_atravessa_fronteira = ?2 "
		+ " UNION ALL "
		+ " SELECT tdr_nm_campo as nome "
		+ " FROM tb_tdr_tipo_dados_der_baseline"
		+ " where ftb_id_funcao_transacao_bse = ?3  " , resultSetMapping = "FuncaoTransacaoBaselineTO.descTDs" )
@SqlResultSetMapping( name = "FuncaoTransacaoBaselineTO.descTDs" , columns = { @ColumnResult( name = "nome" ) } )
@NamedQuery( name = "FuncaoTransacaoBaselineTO.findBySistemaAndTipo" , query = "select obj from FuncaoTransacaoBaselineTO obj where obj.baselineTO.sistemaTO.idSistema = :idSistema order by obj.nome" )
@SequenceGenerator( name = "SQ_FTB_FUNCAO_TRANSACAO_BSE" , sequenceName = "SQ_FTB_FUNCAO_TRANSACAO_BSE" )
public class FuncaoTransacaoBaselineTO extends FuncaoTransacaoTO
{

	private static final long serialVersionUID = 1L;

	@Id
	@Column( name = "FTB_ID_FUNCAO_TRANSACAO_BSE" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE , generator = "SQ_FTB_FUNCAO_TRANSACAO_BSE" )
	private Long idFuncaoTransacaoBaseline;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "BSE_ID_BASELINE" )
	private BaselineTO baselineTO;

	@Column( name = "FTB_SE_JUSTIFICATIVA" )
	private String justificativaSe;

	@OneToMany( mappedBy = "funcaoTransacaoBaselineTO" , fetch = FetchType.LAZY , cascade = {
			CascadeType.PERSIST, CascadeType.MERGE } )
	private List<ArquivoReferenciadoBaselineTO> arquivoReferenciadoBaselineTOs;

	@OneToMany( mappedBy = "funcaoTransacaoBaselineTO" , cascade = CascadeType.ALL )
	private List<TipoDadosDERFuncaoTransacaoBaselineTO> tipoDadosDERFuncaoTransacaoBaselineTOs;

	public void updateByProjeto (
			FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO )
	{
		updateByProjeto(funcaoTransacaoProjetoTO, this);
	}

	public static FuncaoTransacaoBaselineTO valueOf (
			FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO ,
			BaselineTO baselineTO )
	{

		FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO = new FuncaoTransacaoBaselineTO();
		funcaoTransacaoBaselineTO.setBaselineTO(baselineTO);

		updateByProjeto(funcaoTransacaoProjetoTO, funcaoTransacaoBaselineTO);

		return funcaoTransacaoBaselineTO;
	}

	private static void updateByProjeto (
			FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO ,
			FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO )
	{

		funcaoTransacaoBaselineTO.setNome(funcaoTransacaoProjetoTO.getNome());
		funcaoTransacaoBaselineTO.setSituacaoContagem(funcaoTransacaoProjetoTO
				.getSituacaoContagem());
		funcaoTransacaoBaselineTO.setDescricao(funcaoTransacaoProjetoTO
				.getDescricao());
		funcaoTransacaoBaselineTO
				.setComentarioRejeicao(funcaoTransacaoProjetoTO.getDescricao());
		// funcaoTransacaoBaselineTO.setElementoContagemTO(funcaoTransacaoProjetoTO.getElementoContagemTO());
		funcaoTransacaoBaselineTO.setQtTipoDados(funcaoTransacaoProjetoTO
				.getQtTipoDados());
		funcaoTransacaoBaselineTO.setValorPontoFuncao(funcaoTransacaoProjetoTO
				.getValorPontoFuncao());
		funcaoTransacaoBaselineTO.setComplexidadeTP(funcaoTransacaoProjetoTO
				.getComplexidadeTP());

		// TODO:
		// funcaoTransacaoBaselinoTO.setJustificativaSe(funcaoTransacaoProjetoTO.getJustificativaSe());

		funcaoTransacaoBaselineTO.setFuncaoTransacaoTP(funcaoTransacaoProjetoTO
				.getFuncaoTransacaoTP());
		funcaoTransacaoBaselineTO
				.setQtArquivoReferenciado(funcaoTransacaoProjetoTO
						.getQtArquivoReferenciado());

		List<ArquivoReferenciadoBaselineTO> arquivoReferenciadoBaselineTOs = new ArrayList<ArquivoReferenciadoBaselineTO>(
				funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs()
						.size());
		ArquivoReferenciadoBaselineTO arquivoReferenciadoBaselineTO;
		for ( ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO : funcaoTransacaoProjetoTO
				.getArquivoReferenciadoProjetoTOs() )
		{
			arquivoReferenciadoBaselineTO = ArquivoReferenciadoBaselineTO
					.valueOf(arquivoReferenciadoProjetoTO);
			arquivoReferenciadoBaselineTO
					.setFuncaoTransacaoBaselineTO(funcaoTransacaoBaselineTO);
			arquivoReferenciadoBaselineTOs.add(arquivoReferenciadoBaselineTO);
		}
		funcaoTransacaoBaselineTO
				.setArquivoReferenciadoBaselineTOs(arquivoReferenciadoBaselineTOs);

		List<TipoDadosDERFuncaoTransacaoBaselineTO> tdsDER = new ArrayList<TipoDadosDERFuncaoTransacaoBaselineTO>();
		TipoDadosDERFuncaoTransacaoBaselineTO tdDERbse;
		for ( TipoDadosDERFuncaoTransacaoProjetoTO tdDERpro : funcaoTransacaoProjetoTO
				.getTipoDadosDERFuncaoTransacaoProjetoTOs() )
		{
			tdDERbse = new TipoDadosDERFuncaoTransacaoBaselineTO();
			tdDERbse.setDsCampo(tdDERpro.getDsCampo());
			tdDERbse.setFlCalculadoDerivado(tdDERpro.getFlCalculadoDerivado());
			tdDERbse.setFuncaoTransacaoBaselineTO(funcaoTransacaoBaselineTO);
			tdDERbse.setNmCampo(tdDERpro.getNmCampo());

			tdsDER.add(tdDERbse);
		}

		funcaoTransacaoBaselineTO
				.setTipoDadosDERFuncaoTransacaoBaselineTOs(tdsDER);

	}

	public Long getIdFuncaoTransacaoBaseline ()
	{
		return idFuncaoTransacaoBaseline;
	}

	public void setIdFuncaoTransacaoBaseline ( Long idFuncaoTransacaoBaseline )
	{
		this.idFuncaoTransacaoBaseline = idFuncaoTransacaoBaseline;
	}

	public String getJustificativaSe ()
	{
		return justificativaSe;
	}

	public void setJustificativaSe ( String justificativaSe )
	{
		this.justificativaSe = justificativaSe;
	}

	public List<ArquivoReferenciadoBaselineTO> getArquivoReferenciadoBaselineTOs ()
	{
		return arquivoReferenciadoBaselineTOs;
	}

	public void setArquivoReferenciadoBaselineTOs (
			List<ArquivoReferenciadoBaselineTO> arquivoReferenciadoBaselineTOs )
	{
		this.arquivoReferenciadoBaselineTOs = arquivoReferenciadoBaselineTOs;
	}

	public BaselineTO getBaselineTO ()
	{
		return baselineTO;
	}

	public void setBaselineTO ( BaselineTO baselineTO )
	{
		this.baselineTO = baselineTO;
	}

	@Override
	public Long getEntityId ()
	{
		return this.idFuncaoTransacaoBaseline;
	}

	public List<TipoDadosDERFuncaoTransacaoBaselineTO> getTipoDadosDERFuncaoTransacaoBaselineTOs ()
	{
		return tipoDadosDERFuncaoTransacaoBaselineTOs;
	}

	public void setTipoDadosDERFuncaoTransacaoBaselineTOs (
			List<TipoDadosDERFuncaoTransacaoBaselineTO> tipoDadosDERFuncaoTransacaoBaselineTOs )
	{
		this.tipoDadosDERFuncaoTransacaoBaselineTOs = tipoDadosDERFuncaoTransacaoBaselineTOs;
	}

}