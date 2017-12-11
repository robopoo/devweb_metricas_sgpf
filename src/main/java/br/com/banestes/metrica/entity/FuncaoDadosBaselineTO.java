package br.com.sgpf.metrica.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

@Entity
@Table(name = "TB_FDB_FUNCAO_DADOS_BASELINE")
@AttributeOverrides({

@AttributeOverride(name = "nome", column = @Column(name = "FDB_NM_ARQUIVO_LOGICO")),
		@AttributeOverride(name = "situacaoContagem", column = @Column(name = "FDB_ST_CONTAGEM")),
		@AttributeOverride(name = "descricao", column = @Column(name = "FDB_DS_ARQUIVO_LOGICO")),
		@AttributeOverride(name = "comentarioRejeicao", column = @Column(name = "FDB_DS_COMENTARIO_REJEICAO")),
		@AttributeOverride(name = "qtTipoDados", column = @Column(name = "FDB_QT_TIPO_DADOS")),
		@AttributeOverride(name = "qtTipoRegistro", column = @Column(name = "FDB_QT_TIPO_REGISTRO")),
		@AttributeOverride(name = "valorPontoFuncao", column = @Column(name = "FDB_VL_VALOR_PF")),
		@AttributeOverride(name = "complexidadeTP", column = @Column(name = "FDB_TP_COMPLEXIDADE")),
		@AttributeOverride(name = "tipoFuncaoDadosProjeto", column = @Column(name = "FDB_TP_FUNCAO")) })
@NamedQueries({
		@NamedQuery(name = "FuncaoDadosBaselineTO.findDescricaoTRs", query = "select ent.nmEntidade from TipoRegistroBaseLineTO trb join trb.entidadeTO ent where trb.funcaoDadosBaselineTO = ?1 order by ent.nmEntidade"),
		@NamedQuery(name = "FuncaoDadosBaselineTO.findDescricaoTDs", query = "select tdb.atributoTO.nmAtributo from TipoRegistroBaseLineTO trb join trb.tipoDadosBaselineTOs tdb where trb.funcaoDadosBaselineTO = ?1 and tdb.flReferenciaAplicacao = ?2 order by tdb.atributoTO.nmAtributo"),
		@NamedQuery(name = "FuncaoDadosBaselineTO.deleteFilhos", query = "delete from TipoRegistroBaseLineTO trb where trb.funcaoDadosBaselineTO = ?1"),
		@NamedQuery(name = "FuncaoDadosBaselineTO.findBySistemaAndTipo", query = "select obj from FuncaoDadosBaselineTO obj where obj.baselineTO.sistemaTO = :sistema and not exists ( select 1 from FuncaoDadosProjetoTO fdp where fdp.projetoTO = :projeto and fdp.funcaoDadosBaselineTO = obj ) order by obj.nome") })

@SequenceGenerator(name = "SQ_FDB_FUNCAO_DADOS_BASELINE", sequenceName = "SQ_FDB_FUNCAO_DADOS_BASELINE")
public class FuncaoDadosBaselineTO extends FuncaoDadosTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FDB_ID_FUNCAO_DADOS_BASELINE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FDB_FUNCAO_DADOS_BASELINE")
	private Long idFuncaoDadosBaseline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BSE_ID_BASELINE")
	private BaselineTO baselineTO;

	@OneToMany(mappedBy = "funcaoDadosBaselineTO", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	private List<TipoRegistroBaseLineTO> tipoRegistroList;

	public void updateByProjeto(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {
		updateByProjeto(funcaoDadosProjetoTO, this);
	}

	public static FuncaoDadosBaselineTO valueOf(FuncaoDadosProjetoTO funcaoDadosProjetoTO, BaselineTO baselineTO) {

		FuncaoDadosBaselineTO funcaoDadosBaselineTO = new FuncaoDadosBaselineTO();

		updateByProjeto(funcaoDadosProjetoTO, funcaoDadosBaselineTO);

		funcaoDadosBaselineTO.setBaselineTO(baselineTO);

		return funcaoDadosBaselineTO;
	}

	private static void updateByProjeto(FuncaoDadosProjetoTO funcaoDadosProjetoTO,
			FuncaoDadosBaselineTO funcaoDadosBaselineTO) {

		funcaoDadosBaselineTO.setNome(funcaoDadosProjetoTO.getNome());
		funcaoDadosBaselineTO.setSituacaoContagem(funcaoDadosProjetoTO.getSituacaoContagem());
		funcaoDadosBaselineTO.setDescricao(funcaoDadosProjetoTO.getDescricao());
		funcaoDadosBaselineTO.setComentarioRejeicao(funcaoDadosProjetoTO.getDescricao());
//		funcaoDadosBaselineTO.setElementoContagemTO(funcaoDadosProjetoTO.getElementoContagemTO());
		funcaoDadosBaselineTO.setQtTipoDados(funcaoDadosProjetoTO.getQtTipoDados());
		funcaoDadosBaselineTO.setValorPontoFuncao(funcaoDadosProjetoTO.getValorPontoFuncao());
		funcaoDadosBaselineTO.setComplexidadeTP(funcaoDadosProjetoTO.getComplexidadeTP());

		funcaoDadosBaselineTO.setFuncaoDadosTP(funcaoDadosProjetoTO.getFuncaoDadosTP());
		funcaoDadosBaselineTO.setQtTipoRegistro(funcaoDadosProjetoTO.getQtTipoRegistro());

		List<TipoRegistroBaseLineTO> tipoRegistroBaseLineTOs = new ArrayList<TipoRegistroBaseLineTO>(
				funcaoDadosProjetoTO.getTipoRegistroProjetoTOs().size());
		TipoRegistroBaseLineTO tipoRegistroBaseLineTO;
		for (TipoRegistroProjetoTO tipoRegistroProjetoTO : funcaoDadosProjetoTO.getTipoRegistroProjetoTOs()) {
			tipoRegistroBaseLineTO = TipoRegistroBaseLineTO.valueOf(tipoRegistroProjetoTO);
			tipoRegistroBaseLineTO.setFuncaoDadosBaselineTO(funcaoDadosBaselineTO);
			tipoRegistroBaseLineTOs.add(tipoRegistroBaseLineTO);
		}
		funcaoDadosBaselineTO.setTipoRegistroList(tipoRegistroBaseLineTOs);
	}

	public List<TipoRegistroBaseLineTO> getTipoRegistroList() {
		return tipoRegistroList;
	}

	public void setTipoRegistroList(List<TipoRegistroBaseLineTO> tipoRegistroList) {
		this.tipoRegistroList = tipoRegistroList;
	}

	public Long getIdFuncaoDadosBaseline() {
		return idFuncaoDadosBaseline;
	}

	public BaselineTO getBaselineTO() {
		return baselineTO;
	}

	public void setBaselineTO(BaselineTO baselineTO) {
		this.baselineTO = baselineTO;
	}

	public void setIdFuncaoDadosBaseline(Long idFuncaoDadosBaseline) {
		this.idFuncaoDadosBaseline = idFuncaoDadosBaseline;
	}

	@Override
	public Long getEntityId() {
		return this.idFuncaoDadosBaseline;
	}
}