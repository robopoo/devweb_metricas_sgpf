package br.com.sgpf.metrica.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.core.exception.vo.MensagemErro;
import br.com.sgpf.metrica.core.util.JSFUtil;
import br.com.sgpf.metrica.core.util.StringUtil;
import br.com.sgpf.metrica.dao.ArquivoReferenciadoProjetoDAO;
import br.com.sgpf.metrica.dao.ElementoContagemDAO;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.dao.TipoRegistroProjetoDAO;
import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.AtributoTO;
import br.com.sgpf.metrica.entity.ElementoContagemTO;
import br.com.sgpf.metrica.entity.EntidadeTO;
import br.com.sgpf.metrica.entity.FuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.TipoRegistroBaseLineTO;
import br.com.sgpf.metrica.entity.TipoRegistroProjetoTO;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;
import br.com.sgpf.metrica.enumeration.TipoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoEntidadeEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoDadosProjetoEnum;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;

@Name("funcaoDadosController")
@Scope(ScopeType.CONVERSATION)
public class FuncaoDadosController extends AbstractWebBean implements Serializable {

	private static final long serialVersionUID = -8165476663215223546L;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@In(create = true)
	private FuncaoDadosBaselineDAO funcaoDadosBaselineDAO;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	@In(create = true)
	private EntidadeDAO entidadeDAO;

	@In(create = true)
	private TipoRegistroProjetoDAO tipoRegistroProjetoDAO;

	@In(create = true)
	private ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO;

	@In(create = true)
	private ElementoContagemDAO elementoContagemDAO;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@Out(required = false)
	private List<FuncaoDadosBaselineTO> funcaoDadosBaseLinePorSistema;

	@Out(required = false)
	private List<ElementoContagemTO> elementoContagemTOList;

	@DataModel
	private List<TipoRegistroProjetoTO> tipoRegistroProjetoTOListDataModel;

	@DataModelSelection("tipoRegistroProjetoTOListDataModel")
	private TipoRegistroProjetoTO tipoResgiroProjetoTOSelect;

	@DataModel
	private List<TipoDadosFuncaoDadosProjetoTO> tipoDadosFuncaoDadosProjetoTOListDataModel;

	@Out(required = false)
	private EntidadeTO entidadeFuncaoDados;

	private TipoContagemEnum tipoContagem;

	private ProjetoTO projetoTO;

	@Out(required = false)
	private FuncaoDadosBaselineTO funcaoDadosBaselineTO;

	@Out(required = false)
	private List<EntidadeTO> entidadeSistemaTOList;

	@Out(required = false)
	private List<SistemaTO> sistemasEmpresaTOList;

	@Out(required = false)
	private SistemaTO sistemaFuncaoDados;

	@Out(required = false)
	private FuncaoDadosProjetoTO funcaoDadosProjetoTO;

	@In
	FuncaoDadosProjetoBean funcaoDadosProjetoBean;

	@In(create = true)
	private FuncaoBean funcaoBean;

	boolean selecionarTodos;

	private boolean elementoContagemTipoQuantidade = false;

	public void preRenderForm(ProjetoTO projetoTO, FuncaoDadosProjetoTO funcaoDadosProjetoTO, OperacoesEnum operacao) {

		this.projetoTO = projetoTO;

		if (!OperacoesEnum.INSERCAO.equals(operacao)) {
			funcaoBean.atualizarTotaisProjeto(true, funcaoDadosProjetoTO, projetoTO);
		}

		this.setOperacao(operacao);
		this.funcaoDadosProjetoTO = funcaoDadosProjetoTO;

		this.funcaoDadosBaselineTO = funcaoDadosProjetoTO.getFuncaoDadosBaselineTO();
		setTipoContagem(this.funcaoDadosBaselineTO == null ? TipoContagemEnum.ARQUIVO_LOGICO
				: TipoContagemEnum.BASELINE);
		atualizaSistemaEntidadeListas(funcaoDadosProjetoTO.getTipoFuncaoDadosProjeto());

		tipoRegistroProjetoTOListDataModel = new ArrayList<TipoRegistroProjetoTO>();
		tipoDadosFuncaoDadosProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoDadosProjetoTO>();

		getTipoRegistroProjetoTOListDataModel().addAll(funcaoDadosProjetoTO.getTipoRegistroProjetoTOs());
		for (TipoRegistroProjetoTO tipoRegistroProjetoTO : getTipoRegistroProjetoTOListDataModel()) {
			getTipoDadosFuncaoDadosProjetoTOListDataModel().addAll(
					tipoRegistroProjetoTO.getTipoDadosFuncaoDadosProjetoTOs());
		}

		this.elementoContagemTOList = this.elementoContagemDAO.findAllElementoContagemByContrato(this.projetoTO
				.getContratoTO());

		this.sistemaFuncaoDados = null;

		elementoContagemTipoQuantidade = false;

		if (funcaoDadosProjetoTO.getElementoContagemTO() == null)
			return;

		if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			elementoContagemTipoQuantidade = true;
		}

	}

	@TransactionExceptionInterceptor
	public String salvarFuncaoDados() {
		List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

		boolean isBaseline = getTipoContagem() == TipoContagemEnum.BASELINE;

		if (funcaoDadosProjetoTO.getNome() == null || StringUtil.isEmpty(this.funcaoDadosProjetoTO.getNome())) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED",
					getMessageFromResource("label.arquivo.logico.nome")));
		} else {
			for (FuncaoDadosProjetoTO func : this.projetoTO.getFuncaoDadosProjetoTOList()) {
				if (!func.equals(this.funcaoDadosProjetoTO)
						&& this.funcaoDadosProjetoTO.getNome().trim().equals(func.getNome())) {
					mensagens.add(new MensagemErro(getMessageFromResource("msg.funcao.nome.repetido.projeto")));
				}
			}

			if (this.projetoTO.getSistemaTO().getBaselineTO() != null) {
				Integer count = funcaoDadosBaselineDAO.countFuncoesMesmoNome(this.projetoTO.getSistemaTO()
						.getBaselineTO(), this.funcaoDadosProjetoTO);
				if (count > 0) {
					mensagens.add(new MensagemErro(getMessageFromResource("msg.funcao.nome.repetido.baseline")));
				}
			}
		}

		if (funcaoDadosProjetoTO.getTipoFuncaoDadosProjeto() == null && !isBaseline) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED",
					getMessageFromResource("label.arquivo.logico.tipo")));
		}

		if (funcaoDadosProjetoTO.getDescricao() == null || StringUtil.isEmpty(this.funcaoDadosProjetoTO.getDescricao())) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED",
					getMessageFromResource("label.descricao.detalhada")));
		}

		if (this.funcaoDadosProjetoTO.getElementoContagemTO() == null) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED",
					getMessageFromResource("label.elemento.contagem")));
		} else {

			/**
			 * O campo "Descrição da Manutenção" deve ser obrigatório somente se o Tipo de Elemento de Contagem for diferente de "Incluir"
			 */
			if (!TipoItemMensuraveisEnum.INCLUSAO.equals(this.funcaoDadosProjetoTO.getElementoContagemTO()
					.getIndItemMensuravel())) {

				if (funcaoDadosProjetoTO.getDescricaoManutencao() == null
						|| StringUtil.isEmpty(this.funcaoDadosProjetoTO.getDescricaoManutencao())) {
					mensagens.add(new MensagemErro("msg.campo.obrigatorio.elemento.contagem.dif.inclusao",
							this.funcaoDadosProjetoTO.getElementoContagemTO().getIndItemMensuravel().getDescricao()));
				}
			}
		}

		if (getTipoRegistroProjetoTOListDataModel().size() < 1
				|| getTipoDadosFuncaoDadosProjetoTOListDataModel().size() < 1) {
			mensagens.add(new MensagemErro("msg.requerido.funcao.dados.tr.td"));
		} else {

			boolean achou = false;
			for (TipoRegistroProjetoTO item : getTipoRegistroProjetoTOListDataModel()) {
				if (item.getSubgrupoDados()) {
					achou = true;
					break;
				}
			}
			if (!achou) {
				mensagens.add(new MensagemErro("msg.requerido.funcao.dados.tr.td"));
			} else {

				achou = false;
				for (TipoDadosFuncaoDadosProjetoTO item : getTipoDadosFuncaoDadosProjetoTOListDataModel()) {
					if (item.getReferenciaAplicacao()) {
						achou = true;
						break;
					}
				}
				if (!achou) {
					mensagens.add(new MensagemErro("msg.requerido.funcao.dados.tr.td"));
				}
			}
		}

		if (!mensagens.isEmpty()) {
			JSFUtil.addErrorMessageFromResource(mensagens);
			return null;
		}

		this.atualizarTotalTRs();
		this.atualizarTotalTDs();

		this.funcaoDadosProjetoTO.setComplexidadeTP(funcaoDadosProjetoTO.obterComplexidade());
		this.funcaoDadosProjetoTO.setValorPontoFuncao(funcaoDadosProjetoTO.obterValorPontoFuncao());

		this.funcaoBean.atualizarTotaisProjeto(false, this.funcaoDadosProjetoTO,
				this.funcaoDadosProjetoTO.getProjetoTO());

		if (getOperacao() == OperacoesEnum.INSERCAO) {

			if (!projetoTO.getFuncaoDadosProjetoTOList().contains(funcaoDadosProjetoTO)) {
				projetoTO.getFuncaoDadosProjetoTOList().add(funcaoDadosProjetoTO);
			}

			if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
				funcaoDadosProjetoTO.setTipoRegistroProjetoTOs(new ArrayList<TipoRegistroProjetoTO>());
			}

			funcaoDadosProjetoDAO.insert(funcaoDadosProjetoTO);

			if (getTipoContagem().equals(TipoContagemEnum.BASELINE)) {
				ArquivoReferenciadoProjetoTO filtro = new ArquivoReferenciadoProjetoTO();
				filtro.setFuncaoDadosBaselineTO(this.funcaoDadosProjetoTO.getFuncaoDadosBaselineTO());
				List<ArquivoReferenciadoProjetoTO> arqsAtualizar = this.arquivoReferenciadoProjetoDAO
						.findEntitiesByCriteria(filtro, null);
				for (ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO : arqsAtualizar) {
					arquivoReferenciadoProjetoTO.setFuncaoDadosBaselineTO(null);
					arquivoReferenciadoProjetoTO.setFuncaoDadosProjetoTO(this.funcaoDadosProjetoTO);
					this.arquivoReferenciadoProjetoDAO.update(arquivoReferenciadoProjetoTO);
				}
			}

		} else {
			funcaoDadosProjetoDAO.update(funcaoDadosProjetoTO);

			if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
				tipoRegistroProjetoDAO.deleteByFuncaoDados(funcaoDadosProjetoTO);
				funcaoDadosProjetoTO.setTipoRegistroProjetoTOs(new ArrayList<TipoRegistroProjetoTO>());
			}
		}

		if (projetoTO.getStatus() != StatusProjetoEnum.DIGITADO) {
			projetoTO.setStatus(StatusProjetoEnum.DIGITADO);
			projetoDAO.update(projetoTO);
		}

		
		
		this.addInfoMessageFromResource("operacao.realizada.sucesso");

		return OUTCOME_LISTA;
	}

	@TransactionExceptionInterceptor
	public void excluirTipoRegistro() {
		TipoRegistroProjetoTO tipoRegistroProjetoTOExclusao = tipoResgiroProjetoTOSelect;

		Iterator<TipoDadosFuncaoDadosProjetoTO> iterator = getTipoDadosFuncaoDadosProjetoTOListDataModel().iterator();
		while (iterator.hasNext()) {
			TipoDadosFuncaoDadosProjetoTO item = iterator.next();
			if (item.getTipoRegistroFuncaoDadosProjetoTO().equals(tipoRegistroProjetoTOExclusao)) {
				iterator.remove();
			}
		}

		getTipoRegistroProjetoTOListDataModel().remove(tipoRegistroProjetoTOExclusao);

		tipoRegistroProjetoTOExclusao.setFuncaoDadosProjetoTO(null);

		this.funcaoDadosProjetoTO.getTipoRegistroProjetoTOs().remove(tipoRegistroProjetoTOExclusao);

	}

	@TransactionExceptionInterceptor
	public void incluirEntidade() {
		// List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

		if (this.funcaoDadosProjetoTO.getTipoFuncaoDadosProjeto() == TipoFuncaoDadosProjetoEnum.AIE) {
			if (this.sistemaFuncaoDados == null) {

				JSFUtil.addErrorMessageFromResource("javax.faces.component.UIInput.REQUIRED",
						JSFUtil.getMessageFromResource("label.sistema"));
			}
		}

		if (this.entidadeFuncaoDados == null) {
			JSFUtil.addErrorMessageFromResource("javax.faces.component.UIInput.REQUIRED",
					JSFUtil.getMessageFromResource("label.entidade"));
			return;
		}

		if (entidadeJaIncluida(this.entidadeFuncaoDados)) {
			JSFUtil.addErrorMessageFromResource("msg.alerta.entidade.existe");
			return;
		}

		boolean isAssociativa = entidadeFuncaoDados.getTpEntidade() == TipoEntidadeEnum.ASSOCIATIVA;

		TipoRegistroProjetoTO tipoRegistroProjetoTO = new TipoRegistroProjetoTO();
		tipoRegistroProjetoTO.setEntidadeTO(entidadeFuncaoDados);
		tipoRegistroProjetoTO.setTipoDadosFuncaoDadosProjetoTOs(new ArrayList<TipoDadosFuncaoDadosProjetoTO>());
		tipoRegistroProjetoTO.setFuncaoDadosProjetoTO(funcaoDadosProjetoTO);

		boolean hasAtributoReconhecido = false;

		for (AtributoTO atributoTO : tipoRegistroProjetoTO.getEntidadeTO().getAtributoTOlist()) {

			if (atributoTO.getFlReconhecido() == SimNaoEnum.NAO)
				continue;

			if (atributoTO.getStatus() == StatusEnum.DESATIVADO)
				continue;

			hasAtributoReconhecido = true;

			TipoDadosFuncaoDadosProjetoTO tipoDadosFuncaoDadosProjeto = new TipoDadosFuncaoDadosProjetoTO();
			tipoDadosFuncaoDadosProjeto.setTipoRegistroFuncaoDadosProjetoTO(tipoRegistroProjetoTO);
			tipoDadosFuncaoDadosProjeto.setAtributoTO(atributoTO);
			tipoDadosFuncaoDadosProjeto.setFlReferenciaAplicacao(SimNaoEnum.NAO);

			// PENSAR EM REFATORAR
			tipoRegistroProjetoTO.getTipoDadosFuncaoDadosProjetoTOs().add(tipoDadosFuncaoDadosProjeto);
			getTipoDadosFuncaoDadosProjetoTOListDataModel().add(tipoDadosFuncaoDadosProjeto);
			tipoRegistroProjetoTO.setIndicadorSubgrupoDados(SimNaoEnum.NAO);
		}

		tipoRegistroProjetoTO.setPodeMarcarSubGrupoDados(isAssociativa && !hasAtributoReconhecido);

		// PENSAR EM REFATORAR
		funcaoDadosProjetoTO.getTipoRegistroProjetoTOs().add(tipoRegistroProjetoTO);
		getTipoRegistroProjetoTOListDataModel().add(tipoRegistroProjetoTO);

		entidadeFuncaoDados = null;

	}

	private boolean entidadeJaIncluida(EntidadeTO entidadeTO) {
		if (tipoRegistroProjetoTOListDataModel == null)
			return false;
		for (TipoRegistroProjetoTO tipoResgistroProjetoTO : tipoRegistroProjetoTOListDataModel) {
			if (tipoResgistroProjetoTO.getEntidadeTO().equals(entidadeTO))
				return true;
		}
		return false;
	}

	public void atualizarTotalTRs() {

		if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			funcaoDadosProjetoTO.setQtTipoRegistro(0);
			return;
		}

		funcaoDadosProjetoTO.setQtItens(0l);

		Integer qtTipoRegistro = new Integer(0);
		for (TipoRegistroProjetoTO tipoRegistroProjetoTO : tipoRegistroProjetoTOListDataModel) {
			if (tipoRegistroProjetoTO.getSubgrupoDados()) {
				++qtTipoRegistro;
			}
		}
		funcaoDadosProjetoTO.setQtTipoRegistro(qtTipoRegistro);

	}

	public void atualizarTotalTDs() {

		if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			funcaoDadosProjetoTO.setQtTipoDados(0);
			return;
		}

		Integer qtTipoDados = new Integer(0);
		for (TipoDadosFuncaoDadosProjetoTO tipoDadosFuncaoDadosProjetoTO : getTipoDadosFuncaoDadosProjetoTOListDataModel()) {
			if (tipoDadosFuncaoDadosProjetoTO.getReferenciaAplicacao()) {
				++qtTipoDados;
			}
		}
		funcaoDadosProjetoTO.setQtTipoDados(qtTipoDados);

	}

	public void atualizaSistemaEntidadeListas(TipoFuncaoDadosProjetoEnum tipoFuncaoDadosProjeto) {

		this.sistemasEmpresaTOList = null;
		this.entidadeSistemaTOList = null;

		if (tipoFuncaoDadosProjeto == TipoFuncaoDadosProjetoEnum.AIE) {
			this.sistemasEmpresaTOList = this.sistemaDAO.findAllSistemaByProjetoEmpresaSemSistemaAtual(this.projetoTO);
		} else if (tipoFuncaoDadosProjeto == TipoFuncaoDadosProjetoEnum.ALI) {
			this.sistemasEmpresaTOList = this.sistemaDAO.findAllSistemaByProjetoEmpresa(this.projetoTO);
		}

	}

	public void actionAlterarArquivoLogico(ActionEvent event) {
		if (this.entidadeSistemaTOList == null)
			this.entidadeSistemaTOList = new ArrayList<EntidadeTO>();
		else
			this.entidadeSistemaTOList.clear();

		if (funcaoDadosBaselineTO != null) {

			List<TipoRegistroProjetoTO> tipoRegistroProjetoTOs = new ArrayList<TipoRegistroProjetoTO>();
			List<TipoDadosFuncaoDadosProjetoTO> tipoDadosFuncaoDadosProjetoTOs;

			List<TipoRegistroBaseLineTO> tipoRegistroList = funcaoDadosBaselineTO.getTipoRegistroList();

			TipoRegistroProjetoTO tipoRegistroProjeto;
			TipoDadosFuncaoDadosProjetoTO tipoDadosFuncaoDadosProjeto;

			this.tipoDadosFuncaoDadosProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoDadosProjetoTO>();
			for (TipoRegistroBaseLineTO tipoRegistroBaseLineTO : tipoRegistroList) {
				if (!this.entidadeSistemaTOList.contains(tipoRegistroBaseLineTO.getEntidadeTO()))
					this.entidadeSistemaTOList.add(tipoRegistroBaseLineTO.getEntidadeTO());

				tipoRegistroProjeto = new TipoRegistroProjetoTO();

				tipoRegistroProjeto.setEntidadeTO(tipoRegistroBaseLineTO.getEntidadeTO());
				tipoRegistroProjeto.setSubgrupoDados(true);
				tipoRegistroProjeto.setFuncaoDadosProjetoTO(funcaoDadosProjetoTO);

				tipoRegistroProjetoTOs.add(tipoRegistroProjeto);

				List<TipoDadosFuncaoDadosBaselineTO> tipoDadosBaselineTO = tipoRegistroBaseLineTO
						.getTipoDadosBaselineTOs();
				tipoDadosFuncaoDadosProjetoTOs = new ArrayList<TipoDadosFuncaoDadosProjetoTO>();
				for (TipoDadosFuncaoDadosBaselineTO tipoDadosFuncaoDadosBaselineTO : tipoDadosBaselineTO) {
					tipoDadosFuncaoDadosProjeto = new TipoDadosFuncaoDadosProjetoTO();
					tipoDadosFuncaoDadosProjeto.setAtributoTO(tipoDadosFuncaoDadosBaselineTO.getAtributoTO());
					tipoDadosFuncaoDadosProjeto.setFlReferenciaAplicacao(tipoDadosFuncaoDadosBaselineTO
							.getFlReferenciaAplicacao());
					tipoDadosFuncaoDadosProjeto.setReferenciaAplicacao(tipoDadosFuncaoDadosBaselineTO
							.getFlReferenciaAplicacao() == SimNaoEnum.SIM);
					tipoDadosFuncaoDadosProjeto.setTipoRegistroFuncaoDadosProjetoTO(tipoRegistroProjeto);

					tipoDadosFuncaoDadosProjetoTOs.add(tipoDadosFuncaoDadosProjeto);
				}
				tipoRegistroProjeto.setTipoDadosFuncaoDadosProjetoTOs(tipoDadosFuncaoDadosProjetoTOs);
				this.tipoDadosFuncaoDadosProjetoTOListDataModel.addAll(tipoDadosFuncaoDadosProjetoTOs);
			}
			funcaoDadosProjetoTO.setTipoFuncaoDadosProjeto(funcaoDadosBaselineTO.getTipoFuncaoDadosProjeto());
			funcaoDadosProjetoTO.setDescricao(funcaoDadosBaselineTO.getDescricao());
			funcaoDadosProjetoTO.setTipoRegistroProjetoTOs(tipoRegistroProjetoTOs);
			funcaoDadosProjetoTO.setNome(funcaoDadosBaselineTO.getNome());
			funcaoDadosProjetoTO.setFuncaoDadosBaselineTO(funcaoDadosBaselineTO);
			atualizaSistemaEntidadeListas(funcaoDadosProjetoTO.getTipoFuncaoDadosProjeto());
			this.tipoRegistroProjetoTOListDataModel = tipoRegistroProjetoTOs;

		}
	}

	public void actionAlteraTipoContagem(ActionEvent event) {
		this.funcaoDadosBaseLinePorSistema = funcaoDadosBaselineDAO.findAllBySistemaNotExistsProjeto(this.projetoTO);
		this.funcaoDadosProjetoTO = new FuncaoDadosProjetoTO();
		this.funcaoDadosProjetoTO.setProjetoTO(this.projetoTO);
		this.funcaoDadosProjetoTO.setTipoRegistroProjetoTOs(new ArrayList<TipoRegistroProjetoTO>());
		this.tipoRegistroProjetoTOListDataModel = null;
		this.tipoDadosFuncaoDadosProjetoTOListDataModel = null;
		this.funcaoDadosBaselineTO = null;
	}

	public void selecionarTodos() {

		this.selecionarTodos = !this.selecionarTodos;

		for (TipoDadosFuncaoDadosProjetoTO td : this.tipoDadosFuncaoDadosProjetoTOListDataModel) {
			td.setReferenciaAplicacao(this.selecionarTodos);
		}
	}

	public List<FuncaoDadosBaselineTO> getFuncaoDadosBaseLinePorSistema() {
		return funcaoDadosBaseLinePorSistema;
	}

	public List<TipoRegistroProjetoTO> getTipoRegistroProjetoTOListDataModel() {
		return this.tipoRegistroProjetoTOListDataModel;
	}

	public List<TipoDadosFuncaoDadosProjetoTO> getTipoDadosFuncaoDadosProjetoTOListDataModel() {
		if (tipoDadosFuncaoDadosProjetoTOListDataModel == null)
			tipoDadosFuncaoDadosProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoDadosProjetoTO>();
		return tipoDadosFuncaoDadosProjetoTOListDataModel;
	}

	@Observer("eventResetFormInclusaoFuncao")
	public void reset() {
		this.tipoRegistroProjetoTOListDataModel = null;
		this.tipoDadosFuncaoDadosProjetoTOListDataModel = null;
	}

	public EntidadeTO getEntidadeFuncaoDados() {
		return entidadeFuncaoDados;
	}

	public void setEntidadeFuncaoDados(EntidadeTO entidadeFuncaoDados) {
		this.entidadeFuncaoDados = entidadeFuncaoDados;
	}

	public List<ElementoContagemTO> getElementoContagemTOList() {
		return elementoContagemTOList;
	}

	public TipoContagemEnum getTipoContagem() {
		return tipoContagem;
	}

	public void setTipoContagem(TipoContagemEnum tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public FuncaoDadosBaselineTO getFuncaoDadosBaselineTO() {
		return funcaoDadosBaselineTO;
	}

	public void setFuncaoDadosBaselineTO(FuncaoDadosBaselineTO funcaoDadosBaselineTO) {
		this.funcaoDadosBaselineTO = funcaoDadosBaselineTO;
	}

	public void valueChangedSistemaEntidade(ValueChangeEvent event) {
		SistemaTO sistemaTO = (SistemaTO) event.getNewValue();
		if (sistemaTO != null) {
			this.entidadeSistemaTOList = this.entidadeDAO.findAllEntidadeBySistema(sistemaTO);
		} else {
			this.entidadeSistemaTOList = null;
		}
	}

	public List<EntidadeTO> getEntidadeSistemaTOList() {
		return entidadeSistemaTOList;
	}

	public List<SistemaTO> getSistemasEmpresaTOList() {
		return sistemasEmpresaTOList;
	}

	public SistemaTO getSistemaFuncaoDados() {
		return sistemaFuncaoDados;
	}

	public void setSistemaFuncaoDados(SistemaTO sistemaFuncaoDados) {
		this.sistemaFuncaoDados = sistemaFuncaoDados;
	}

	public void cancelar() {
		this.funcaoDadosProjetoDAO.clear();
	}

	public FuncaoDadosProjetoTO getFuncaoDadosProjetoTO() {
		return funcaoDadosProjetoTO;
	}

	public void setFuncaoDadosProjetoTO(FuncaoDadosProjetoTO funcaoDadosProjetoTO) {
		this.funcaoDadosProjetoTO = funcaoDadosProjetoTO;
	}

	public void changeElementoContagem(ActionEvent event) {

		if (funcaoDadosProjetoTO.getElementoContagemTO() == null)
			return;

		elementoContagemTipoQuantidade = false;
		if (funcaoDadosProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			elementoContagemTipoQuantidade = true;
		}
	}

	public boolean isElementoContagemTipoQuantidade() {
		return elementoContagemTipoQuantidade;
	}
	
	//TESTAR! tela funcao de dados
	public List<EntidadeTO> getEntidadeDadosNegocio()
	{
		return entidadeDAO.findAllEntidadeDadosDeNegocio(sistemaFuncaoDados);
	}

}