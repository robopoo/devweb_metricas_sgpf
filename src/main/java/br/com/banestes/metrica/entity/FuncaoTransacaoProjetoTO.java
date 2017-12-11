package br.com.sgpf.metrica.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.sgpf.metrica.bean.InspecaoFuncao;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;

@Entity
@Table(name = "TB_FTP_FUNCAO_TRANSACAO_PRO")
@AttributeOverrides({

@AttributeOverride(name = "nome", column = @Column(name = "FTP_NM_TRANSACAO")),
		@AttributeOverride(name = "situacaoContagem", column = @Column(name = "FTP_ST_CONTAGEM")),
		@AttributeOverride(name = "descricao", column = @Column(name = "FTP_DS_TRANSACAO")),
		@AttributeOverride(name = "comentarioRejeicao", column = @Column(name = "FTP_DS_COMENTARIO_REJEICAO")),
		@AttributeOverride(name = "qtTipoDados", column = @Column(name = "FTP_QT_TIPO_DADOS")),
		@AttributeOverride(name = "qtArquivoReferenciado", column = @Column(name = "FTP_QT_ARQUIVO_REFERENCIADO")),
		@AttributeOverride(name = "valorPontoFuncao", column = @Column(name = "FTP_VL_PF")),
		@AttributeOverride(name = "complexidadeTP", column = @Column(name = "FTP_TP_COMPLEXIDADE")),
		@AttributeOverride(name = "funcaoTransacaoTP", column = @Column(name = "FTP_TP_FUNCAO")) })
@NamedQueries({ @NamedQuery(name = "FuncaoTransacaoProjetoTO.findDescricaoARs", query = "select new br.com.sgpf.metrica.entity.DescricaoARsProjeto(fdp.nome, fdb.nome) from ArquivoReferenciadoProjetoTO arp left join arp.funcaoDadosProjetoTO fdp left join arp.funcaoDadosBaselineTO fdb where arp.funcaoTransacaoProjetoTO = ?1") })
@NamedNativeQueries({

		@NamedNativeQuery(name = "FuncaoTransacaoProjetoTO.findDescricaoTDs", query = " SELECT atr.atr_nm_atributo as nome "
				+ " FROM tb_arp_arq_referenciado_pro arp, tb_tdp_tipo_dados_ft_projeto tdp, tb_atr_atributo atr "
				+ " where arp.arp_id_arq_referenciado_pro = tdp.arp_id_arq_referenciado_pro "
				+ " and atr.atr_id_atributo = tdp.atr_id_atributo "
				+ " and arp.ftp_id_funcao_transacao_pro = ?1 "
				+ " and tdp.tdp_fl_atravessa_fronteira = ?2 "
				+ " UNION ALL "
				+ " SELECT tde_nm_campo as nome "
				+ " FROM tb_tde_tipo_dados_der_projeto" + " where ftp_id_funcao_transacao_pro = ?3 " 
				+ " order by nome ", resultSetMapping = "FuncaoTransacaoProjetoTO.descTDs"),

		@NamedNativeQuery(name = "FuncaoTransacaoProjetoTO.findAtributosTDs", query = " SELECT (case when atr.ATR_DS_ATRIBUTO is null then atr.atr_nm_atributo else atr.ATR_DS_ATRIBUTO end) as nome, "
				+ " (case when atr.ATR_DS_ATRIBUTO is null then 0 else 1 end) as hasNomeLogico "
				+ " FROM tb_arp_arq_referenciado_pro arp, tb_tdp_tipo_dados_ft_projeto tdp, tb_atr_atributo atr  "
				+ " where arp.arp_id_arq_referenciado_pro = tdp.arp_id_arq_referenciado_pro  "
				+ " and atr.atr_id_atributo = tdp.atr_id_atributo "
				+ " and arp.ftp_id_funcao_transacao_pro = ?1 "
				+ " and tdp.tdp_fl_atravessa_fronteira = ?2 "
				+ " UNION ALL "
				+ " SELECT tde_nm_campo as nome "
				+ " ,1 as hasNomeLogico "
				+ " FROM tb_tde_tipo_dados_der_projeto"
				+ " where ftp_id_funcao_transacao_pro = ?3"
				+ " order by nome ", resultSetMapping = "FuncaoTransacaoProjetoTO.atributosTDs")

})
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "FuncaoTransacaoProjetoTO.descTDs", columns = { @ColumnResult(name = "nome") }),
		@SqlResultSetMapping(name = "FuncaoTransacaoProjetoTO.atributosTDs", columns = { @ColumnResult(name = "nome"),
				@ColumnResult(name = "hasNomeLogico") }) })
@SequenceGenerator(name = "SQ_FTP_FUNCAO_TRANSACAO_PRO", sequenceName = "SQ_FTP_FUNCAO_TRANSACAO_PRO")
public class FuncaoTransacaoProjetoTO extends FuncaoTransacaoTO implements Serializable, Aprovacao {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FTP_ID_FUNCAO_TRANSACAO_PRO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FTP_FUNCAO_TRANSACAO_PRO")
	private Long idFuncaoTransacaoProjeto;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "PRO_ID_PROJETO")
	private ProjetoTO projetoTO;

	@Column(name = "FTP_DS_MANUTENCAO")
	private String descricaoManutencao;
	
	@Column(name = "FTP_SE_JUSTIFICATIVA")
	private String justificativaSe;

	@OneToMany(mappedBy = "funcaoTransacaoProjetoTO", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ArquivoReferenciadoProjetoTO> arquivoReferenciadoProjetoTOs;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "FTB_ID_FUNCAO_TRANSACAO_BSE")
	private FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO;

	@OneToMany(mappedBy = "funcaoTransacaoProjetoTO", cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs;

	@Transient
	private InspecaoFuncao inspecaoARs;

	@Transient
	private InspecaoFuncao inspecaoTDs;

	@Column(name = "FTB_QT_ITEM")
	private Integer qtItem;

	public String getDescricaoARs() {
		StringBuffer s = new StringBuffer("");

		String nome;
		if (this.arquivoReferenciadoProjetoTOs != null) {
			for (ArquivoReferenciadoProjetoTO arp : this.arquivoReferenciadoProjetoTOs) {

				if (arp.isFuncaoDadosProjeto()) {
					nome = arp.getFuncaoDadosProjetoTO().getNome();
				} else {
					nome = arp.getFuncaoDadosBaselineTO().getNome();
				}

				s.append(nome).append(", ");
			}
		}

		return s.lastIndexOf(",") > -1 ? s.replace(s.lastIndexOf(", "), s.length(), "").toString() : s.toString();
	}

	public Long getIdFuncaoTransacaoProjeto() {
		return idFuncaoTransacaoProjeto;
	}

	public void setIdFuncaoTransacaoProjeto(Long idFuncaoTransacaoProjeto) {
		this.idFuncaoTransacaoProjeto = idFuncaoTransacaoProjeto;
	}

	public String getDescricaoManutencao() {
		return descricaoManutencao;
	}

	public void setDescricaoManutencao(String descricaoManutencao) {
		this.descricaoManutencao = descricaoManutencao;
	}

	public ProjetoTO getProjetoTO() {
		return projetoTO;
	}

	public void setProjetoTO(ProjetoTO projetoTO) {
		this.projetoTO = projetoTO;
	}

	@Override
	public Long getEntityId() {
		return this.idFuncaoTransacaoProjeto;
	}

	public List<ArquivoReferenciadoProjetoTO> getArquivoReferenciadoProjetoTOs() {
		return arquivoReferenciadoProjetoTOs;
	}

	public void setArquivoReferenciadoProjetoTOs(List<ArquivoReferenciadoProjetoTO> arquivoReferenciadoProjetoTOs) {
		this.arquivoReferenciadoProjetoTOs = arquivoReferenciadoProjetoTOs;
	}

	public FuncaoTransacaoBaselineTO getFuncaoTransacaoBaselineTO() {
		return funcaoTransacaoBaselineTO;
	}

	public void setFuncaoTransacaoBaselineTO(FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {
		this.funcaoTransacaoBaselineTO = funcaoTransacaoBaselineTO;
	}

	public InspecaoFuncao getInspecaoARs() {
		return inspecaoARs;
	}

	public void setInspecaoARs(InspecaoFuncao inspecaoARs) {
		this.inspecaoARs = inspecaoARs;
	}

	public InspecaoFuncao getInspecaoTDs() {
		return inspecaoTDs;
	}

	public void setInspecaoTDs(InspecaoFuncao inspecaoTDs) {
		this.inspecaoTDs = inspecaoTDs;
	}

	public List<TipoDadosDERFuncaoTransacaoProjetoTO> getTipoDadosDERFuncaoTransacaoProjetoTOs() {
		return tipoDadosDERFuncaoTransacaoProjetoTOs;
	}

	public void setTipoDadosDERFuncaoTransacaoProjetoTOs(
			List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs) {
		this.tipoDadosDERFuncaoTransacaoProjetoTOs = tipoDadosDERFuncaoTransacaoProjetoTOs;
	}

	public Integer getQtItem() {
		return qtItem;
	}

	public void setQtItem(Integer qtItem) {
		this.qtItem = qtItem;
	}
	
	public void setJustificativaSe(String justificativa)
	{
	
		justificativaSe = justificativa;
		
	}
	
	public String getJustificativaSe()
	{
		
		return justificativaSe;
	}

	public BigDecimal getValorPontoFuncaoLocal() {
		BigDecimal local = BigDecimal.ZERO;

		if (this.getElementoContagemTO() == null
				|| this.getElementoContagemTO().getTpElementoContagem() != TipoElementoContagemEnum.QUANTIDADE) {
			local = super.getValorPontoFuncaoLocal();
		} else {
			if (this.getElementoContagemTO() != null)
				local = this.getElementoContagemTO().getVlrFatorEquivalencia().multiply(new BigDecimal(getQtItem()));
		}

		return local;
	}

	

	
}