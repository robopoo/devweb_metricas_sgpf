package br.com.sgpf.metrica.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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
import javax.persistence.Transient;

import br.com.sgpf.metrica.bean.InspecaoFuncao;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;

@Entity
@Table(name = "TB_FDP_FUNCAO_DADOS_PROJETO")
@AttributeOverrides({

@AttributeOverride(name = "nome", column = @Column(name = "FDP_NM_ARQUIVO_LOGICO")),
		@AttributeOverride(name = "situacaoContagem", column = @Column(name = "FDP_ST_CONTAGEM")),
		@AttributeOverride(name = "descricao", column = @Column(name = "FDP_DS_ARQUIVO_LOGICO")),
		@AttributeOverride(name = "comentarioRejeicao", column = @Column(name = "FDP_DS_COMENTARIO_REJEICAO")),
		@AttributeOverride(name = "qtTipoDados", column = @Column(name = "FDP_QT_TIPO_DADOS")),
		@AttributeOverride(name = "qtTipoRegistro", column = @Column(name = "FDP_QT_TIPO_REGISTRO")),
		@AttributeOverride(name = "valorPontoFuncao", column = @Column(name = "FDP_VL_PF")),
		@AttributeOverride(name = "complexidadeTP", column = @Column(name = "FDP_TP_COMPLEXIDADE")),
		@AttributeOverride(name = "tipoFuncaoDadosProjeto", column = @Column(name = "FDP_TP_FUNCAO")) })
@NamedQueries({
		@NamedQuery(name = "FuncaoDadosProjetoTO.findDescricaoTRs", query = "select ent.nmEntidade from TipoRegistroProjetoTO trp join trp.entidadeTO ent where trp.funcaoDadosProjetoTO = ?1 order by ent.nmEntidade"),
		@NamedQuery(name = "FuncaoDadosProjetoTO.findDescricaoTDs", query = "select tdp.atributoTO.nmAtributo from TipoRegistroProjetoTO trb join trb.tipoDadosFuncaoDadosProjetoTOs tdp where trb.funcaoDadosProjetoTO = ?1 and tdp.flReferenciaAplicacao = ?2 order by tdp.atributoTO.nmAtributo"),
		@NamedQuery(name = "FuncaoDadosProjetoTO.findBySistema", query = "select obj from FuncaoDadosProjetoTO obj where obj.funcaoDadosBaselineTO.baselineTO.sistemaTO.idSistema = :idSistema order by obj.nome") })
@SequenceGenerator(name = "SQ_FDP_FUNCAO_DADOS_PROJETO", sequenceName = "SQ_FDP_FUNCAO_DADOS_PROJETO")
public class FuncaoDadosProjetoTO extends FuncaoDadosTO implements Serializable, Aprovacao {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FDP_ID_FUNCAO_DADOS_PROJETO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FDP_FUNCAO_DADOS_PROJETO")
	private Long idFuncaoDadosProjeto;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "PRO_ID_PROJETO")
	private ProjetoTO projetoTO;

	@Column(name = "FDP_DS_MANUTENCAO")
	private String descricaoManutencao;

	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OneToMany(mappedBy = "funcaoDadosProjetoTO", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<TipoRegistroProjetoTO> tipoRegistroProjetoTOs;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "FDB_ID_FUNCAO_DADOS_BASELINE")
	private FuncaoDadosBaselineTO funcaoDadosBaselineTO;

	@Transient
	private InspecaoFuncao inspecaoTRs;

	@Transient
	private InspecaoFuncao inspecaoTDs;

	@Column(name = "FDP_QT_ITEM")
	private Long qtItens;

	public String getDescricaoTRs() {
		StringBuffer s = new StringBuffer("");

		if (tipoRegistroProjetoTOs != null) {
			for (TipoRegistroProjetoTO tipoRegistroProjeto : tipoRegistroProjetoTOs) {
				s.append(tipoRegistroProjeto.getEntidadeTO().getDescricaoVisual()).append(", ");
			}
		}

		return s.lastIndexOf(",") > -1 ? s.replace(s.lastIndexOf(", "), s.length(), "").toString() : s.toString();
	}

	public InspecaoFuncao getInspecaoTRs() {
		return inspecaoTRs;
	}

	public void setInspecaoTRs(InspecaoFuncao inspecaoTRs) {
		this.inspecaoTRs = inspecaoTRs;
	}

	public InspecaoFuncao getInspecaoTDs() {
		return inspecaoTDs;
	}

	public void setInspecaoTDs(InspecaoFuncao inspecaoTDs) {
		this.inspecaoTDs = inspecaoTDs;
	}

	public Long getIdFuncaoDadosProjeto() {
		return idFuncaoDadosProjeto;
	}

	public void setIdFuncaoDadosProjeto(Long idFuncaoDadosProjeto) {
		this.idFuncaoDadosProjeto = idFuncaoDadosProjeto;
	}

	public ProjetoTO getProjetoTO() {
		return projetoTO;
	}

	public void setProjetoTO(ProjetoTO projetoTO) {
		this.projetoTO = projetoTO;
	}

	public List<TipoRegistroProjetoTO> getTipoRegistroProjetoTOs() {
		return tipoRegistroProjetoTOs;
	}

	public void setTipoRegistroProjetoTOs(List<TipoRegistroProjetoTO> tipoRegistroProjetoTOs) {
		this.tipoRegistroProjetoTOs = tipoRegistroProjetoTOs;
	}

	public String getDescricaoManutencao() {
		return descricaoManutencao;
	}

	public void setDescricaoManutencao(String descricaoManutencao) {
		this.descricaoManutencao = descricaoManutencao;
	}

	public FuncaoDadosBaselineTO getFuncaoDadosBaselineTO() {
		return funcaoDadosBaselineTO;
	}

	public void setFuncaoDadosBaselineTO(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {
		this.funcaoDadosBaselineTO = funcaoDadosBaselineTO;
	}

	@Override
	public Long getEntityId() {
		return this.idFuncaoDadosProjeto;
	}

	public Long getQtItens() {
		return qtItens;
	}

	public void setQtItens(Long qtItens) {
		this.qtItens = qtItens;
	}

	public BigDecimal getValorPontoFuncaoLocal() {
		BigDecimal local = BigDecimal.ZERO;

		if (this.getElementoContagemTO() == null
				|| this.getElementoContagemTO().getTpElementoContagem() != TipoElementoContagemEnum.QUANTIDADE) {
			local = super.getValorPontoFuncaoLocal();
		} else {
			if (this.getElementoContagemTO() != null)
				local = this.getElementoContagemTO().getVlrFatorEquivalencia().multiply(new BigDecimal(getQtItens()));
		}

		return local;
	}
}
