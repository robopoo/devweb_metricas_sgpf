package br.com.sgpf.metrica.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.vo.MensagemErro;
import br.com.sgpf.metrica.core.util.JSFUtil;
import br.com.sgpf.metrica.core.util.StringUtil;
import br.com.sgpf.metrica.dao.ArquivoReferenciadoProjetoDAO;
import br.com.sgpf.metrica.dao.ElementoContagemDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoBaselineDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.entity.ArquivoReferenciadoBaselineTO;
import br.com.sgpf.metrica.entity.ArquivoReferenciadoProjetoTO;
import br.com.sgpf.metrica.entity.ElementoContagemTO;
import br.com.sgpf.metrica.entity.FuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoDadosTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.TipoDadosDERFuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.TipoDadosDERFuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoDadosBaselineTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoTransacaoBaselineTO;
import br.com.sgpf.metrica.entity.TipoDadosFuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.TipoRegistroBaseLineTO;
import br.com.sgpf.metrica.entity.TipoRegistroProjetoTO;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;
import br.com.sgpf.metrica.enumeration.TipoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;

@Name("funcaoTransacaoController")
@Scope(ScopeType.CONVERSATION)
public class FuncaoTransacaoController extends AbstractWebBean implements Serializable {

	private static final long serialVersionUID = -8165476663215223546L;

	protected String OUTCOME_FORM = "form";

	protected String OUTCOME_LISTA = "list";

	@DataModel
	private List<FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoTODataModel;

	@DataModelSelection("funcaoTransacaoProjetoTODataModel")
	private FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTOSelect;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@In(create = true)
	private FuncaoTransacaoBaselineDAO funcaoTransacaoBaselineDAO;

	@Out(required = false)
	private List<FuncaoTransacaoBaselineTO> funcaoTransacaoBaseLinePorSistema;

	@In(create = true)
	private ElementoContagemDAO elementoContagemDAO;

	@Out(required = false)
	private List<ElementoContagemTO> elementoContagemTOList;

	@DataModel
	private List<ArquivoReferenciadoProjetoTO> arquivoReferenciadoProjetoTOListDataModel;

	@DataModel
	private List<TipoDadosFuncaoTransacaoProjetoTO> tipoDadosFuncaoTransacaoProjetoTOListDataModel;

	private List<TipoDadosFuncaoTransacaoProjetoTO> tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp;

	@DataModelSelection("arquivoReferenciadoProjetoTOListDataModel")
	private ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTOSelect;

	@DataModel
	private List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs;

	@DataModelSelection("tipoDadosDERFuncaoTransacaoProjetoTOs")
	private TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERFuncaoTransacaoProjetoTO;

	@In(create = true)
	private ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO;

	@Out(required = false)
	private List<FuncaoDadosTO> funcaoDadosTOList;

	private FuncaoDadosTO funcaoDadosTO;

	@Out(required = false)
	private FuncaoDadosProjetoTO funcaoDadoProjetoTOIncluir;

	private FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO;

	private TipoContagemEnum tipoContagem;

	private ProjetoTO projetoTO;

	@Out(required = false)
	private FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	private OperacoesEnum operacao = OperacoesEnum.NENHUM;

	private ArquivoReferenciadoProjetoTO arquivoReferenciadoIncluir;

	private TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERInclusao;

	//	private TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERAlterado;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@In
	private FuncaoBean funcaoBean;

	boolean selecionarTodos;

	private String dsAtributoFiltro;

	private boolean elementoContagemTipoQuantidade = false;

	public void preRenderForm(ProjetoTO projetoTO, FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO,
			OperacoesEnum operacao) {

		this.projetoTO = projetoTO;

		if (funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs() == null) {
			funcaoTransacaoProjetoTO
					.setTipoDadosDERFuncaoTransacaoProjetoTOs(new ArrayList<TipoDadosDERFuncaoTransacaoProjetoTO>());
		}

		if (!OperacoesEnum.INSERCAO.equals(operacao)) {
			funcaoBean.atualizarTotaisProjeto(true, funcaoTransacaoProjetoTO, projetoTO);
		} else {
			TipoDadosDERFuncaoTransacaoProjetoTO td = new TipoDadosDERFuncaoTransacaoProjetoTO();
			td.setCalculadoDerivado(false);
			td.setDsCampo("Comando");
			td.setNmCampo("Comando");
			td.setFuncaoTransacaoProjetoTO(funcaoTransacaoProjetoTO);
			funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs().add(td);

			td = new TipoDadosDERFuncaoTransacaoProjetoTO();
			td.setCalculadoDerivado(false);
			td.setDsCampo("Mensagem");
			td.setNmCampo("Mensagem");
			td.setFuncaoTransacaoProjetoTO(funcaoTransacaoProjetoTO);
			funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs().add(td);
		}

		this.tipoDadosDERFuncaoTransacaoProjetoTOs = funcaoTransacaoProjetoTO
				.getTipoDadosDERFuncaoTransacaoProjetoTOs();

		this.tipoDadosDERInclusao = new TipoDadosDERFuncaoTransacaoProjetoTO();

		this.setOperacao(operacao);
		this.funcaoTransacaoProjetoTO = funcaoTransacaoProjetoTO;

		this.funcaoTransacaoBaselineTO = funcaoTransacaoProjetoTO.getFuncaoTransacaoBaselineTO();
		setTipoContagem(this.funcaoTransacaoBaselineTO == null ? TipoContagemEnum.ARQUIVO_LOGICO
				: TipoContagemEnum.BASELINE);

		arquivoReferenciadoProjetoTOListDataModel = new ArrayList<ArquivoReferenciadoProjetoTO>();
		tipoDadosFuncaoTransacaoProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>();

		getArquivoReferenciadoProjetoTOListDataModel().addAll(
				funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs());
		for (ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO : getArquivoReferenciadoProjetoTOListDataModel()) {
			getTipoDadosFuncaoTransacaoProjetoTOListDataModel().addAll(
					arquivoReferenciadoProjetoTO.getTipoDadosFuncaoTransacaoProjetos());
		}

		this.elementoContagemTOList = this.elementoContagemDAO.findAllElementoContagemByContrato(this.projetoTO
				.getContratoTO());

		setTipoContagem(this.funcaoTransacaoBaselineTO == null ? TipoContagemEnum.ARQUIVO_LOGICO
				: TipoContagemEnum.BASELINE);

		this.funcaoDadosTOList = new ArrayList<FuncaoDadosTO>();

		this.funcaoDadosTOList.addAll(this.projetoTO.getFuncaoDadosProjetoTOList());

		if (this.funcaoDadosTOList != null) {
			Collections.sort(this.funcaoDadosTOList, new Comparator<FuncaoDadosTO>() {

				@Override
				public int compare(FuncaoDadosTO o1, FuncaoDadosTO o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});
		}

		List<FuncaoDadosBaselineTO> funcoesExistentesProjeto = new ArrayList<FuncaoDadosBaselineTO>();

		for (FuncaoDadosProjetoTO func : this.projetoTO.getFuncaoDadosProjetoTOList()) {
			if (func.getFuncaoDadosBaselineTO() != null) {
				funcoesExistentesProjeto.add(func.getFuncaoDadosBaselineTO());
			}
		}

		if (this.projetoTO.getSistemaTO().getBaselineTO() != null) {
			for (FuncaoDadosBaselineTO func : this.projetoTO.getSistemaTO().getBaselineTO().getFuncaoDadosBaselineTOs()) {
				if (!funcoesExistentesProjeto.contains(func)) {
					this.funcaoDadosTOList.add(func);
				}
			}

		}

		tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp = null;
		dsAtributoFiltro = "";

		elementoContagemTipoQuantidade = false;

		if (funcaoTransacaoProjetoTO.getElementoContagemTO() == null)
			return;

		if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			elementoContagemTipoQuantidade = true;
		}

	}

	@TransactionExceptionInterceptor
	public String salvarFuncaoTransacao() {

		if (tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp != null) {
			tipoDadosFuncaoTransacaoProjetoTOListDataModel = tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp;
		}

		List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

		for (FuncaoTransacaoProjetoTO func : this.projetoTO.getFuncaoTransacaoProjetoTOList()) {
			if (!func.equals(this.funcaoTransacaoProjetoTO)

					&& StringUtil.limparNome(this.funcaoTransacaoProjetoTO.getNome()).equalsIgnoreCase(
							StringUtil.limparNome(func.getNome()))) {

				mensagens.add(new MensagemErro(getMessageFromResource("msg.funcao.nome.repetido.projeto")));
			}
		}

		if (this.projetoTO.getSistemaTO().getBaselineTO() != null) {
			Integer count = funcaoTransacaoBaselineDAO.countFuncoesMesmoNome(this.projetoTO.getSistemaTO()
					.getBaselineTO(), this.funcaoTransacaoProjetoTO);
			if (count > 0) {
				mensagens.add(new MensagemErro(getMessageFromResource("msg.funcao.nome.repetido.baseline")));
			}
		}

		if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() != TipoElementoContagemEnum.QUANTIDADE
				&& (getArquivoReferenciadoProjetoTOListDataModel().size() < 1 || (getTipoDadosFuncaoTransacaoProjetoTOListDataModel()
						.size() + getTipoDadosDERFuncaoTransacaoProjetoTOs().size()) < 1)) {
			mensagens.add(new MensagemErro("msg.requerido.funcao.transacao.ar.td"));
		}
		
		if (mensagens.size() > 0) {
			JSFUtil.addErrorMessageFromResource(mensagens);
			return null;
		} else {

			boolean achou = this.getTipoDadosDERFuncaoTransacaoProjetoTOs().size() > 0;

			if (!achou) {
				for (TipoDadosFuncaoTransacaoProjetoTO item : getTipoDadosFuncaoTransacaoProjetoTOListDataModel()) {
					if (item != null && item.getAtravessaFronteira()) {
						achou = true;
						break;
					}
				}
			}

			if (!achou
					&& funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() != TipoElementoContagemEnum.QUANTIDADE) {
				mensagens.add(new MensagemErro("msg.requerido.funcao.dados.tr.td"));
			}
		}

		if (!TipoItemMensuraveisEnum.INCLUSAO.equals(this.funcaoTransacaoProjetoTO.getElementoContagemTO()
				.getIndItemMensuravel())) {

			if (this.funcaoTransacaoProjetoTO.getDescricaoManutencao() == null
					|| StringUtil.isEmpty(this.funcaoTransacaoProjetoTO.getDescricaoManutencao())) {
				mensagens.add(new MensagemErro("msg.campo.obrigatorio.elemento.contagem.dif.inclusao",
						this.funcaoTransacaoProjetoTO.getElementoContagemTO().getIndItemMensuravel().getDescricao()));
			}
		}
		
		//Justificativa nao pode ser nula caso o tipo da transacao seja saida externa 
		if(funcaoTransacaoProjetoTO.getFuncaoTransacaoTP().equals(TipoFuncaoTransacaoEnum.SE))
		{
			
			if(StringUtils.isBlank(funcaoTransacaoProjetoTO.getJustificativaSe()))
			{
				mensagens.add(new MensagemErro("msg.erro.campo.obrigatorio.funcao.transacao.justificativa"));
			}
		}
		
		for (TipoDadosDERFuncaoTransacaoProjetoTO tdDER : this.funcaoTransacaoProjetoTO
				.getTipoDadosDERFuncaoTransacaoProjetoTOs()) {
			if (this.tipoDadosDERInclusao.getNmCampo() != null
					&& this.tipoDadosDERInclusao.getNmCampo().equalsIgnoreCase(tdDER.getNmCampo())) {
				mensagens.add(new MensagemErro("msg.alerta.tipo.dados.der.existe"));
			}
		}

		if (!mensagens.isEmpty()) {
			JSFUtil.addErrorMessageFromResource(mensagens);
			return null;
		}

		if (tipoDadosDERInclusao != null
				&& (!StringUtil.isEmpty(tipoDadosDERInclusao.getDsCampo()) || !StringUtil.isEmpty(tipoDadosDERInclusao
						.getNmCampo())) && !tipoDadosDERFuncaoTransacaoProjetoTOs.contains(tipoDadosDERInclusao))
			incluirTipoDadosDER();

		this.atualizarTotalARs();
		this.atualizarTotalTDs();

		this.funcaoTransacaoProjetoTO.setComplexidadeTP(funcaoTransacaoProjetoTO.obterComplexidade());
		this.funcaoTransacaoProjetoTO.setValorPontoFuncao(funcaoTransacaoProjetoTO.obterValorPontoFuncao());

		this.funcaoBean.atualizarTotaisProjeto(false, this.funcaoTransacaoProjetoTO,
				this.funcaoTransacaoProjetoTO.getProjetoTO());

		if (getOperacao() == OperacoesEnum.INSERCAO) {
			funcaoTransacaoProjetoTO.setFuncaoTransacaoBaselineTO(this.funcaoTransacaoBaselineTO);
			if (!projetoTO.getFuncaoTransacaoProjetoTOList().contains(funcaoTransacaoProjetoTO)) {
				projetoTO.getFuncaoTransacaoProjetoTOList().add(funcaoTransacaoProjetoTO);
			}

			if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
				funcaoTransacaoProjetoTO
						.setArquivoReferenciadoProjetoTOs(new ArrayList<ArquivoReferenciadoProjetoTO>());
				funcaoTransacaoProjetoTO
						.setTipoDadosDERFuncaoTransacaoProjetoTOs(new ArrayList<TipoDadosDERFuncaoTransacaoProjetoTO>());
			} else {
				funcaoTransacaoProjetoTO.setQtItem(0);
			}

			this.funcaoTransacaoProjetoDAO.insert(funcaoTransacaoProjetoTO);
		} else {

			if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() != TipoElementoContagemEnum.QUANTIDADE)
				funcaoTransacaoProjetoTO.setQtItem(0);

			this.funcaoTransacaoProjetoDAO.update(funcaoTransacaoProjetoTO);

			if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
				funcaoTransacaoProjetoTO
						.setTipoDadosDERFuncaoTransacaoProjetoTOs(new ArrayList<TipoDadosDERFuncaoTransacaoProjetoTO>());
				funcaoTransacaoProjetoTO
						.setArquivoReferenciadoProjetoTOs(new ArrayList<ArquivoReferenciadoProjetoTO>());
				this.funcaoTransacaoProjetoDAO.deleteArquivosRefAndOutrosTD(funcaoTransacaoProjetoTO);
			}

		}

		if (projetoTO.getStatus() != StatusProjetoEnum.DIGITADO) {
			projetoTO.setStatus(StatusProjetoEnum.DIGITADO);
			projetoDAO.update(projetoTO);
		}

		
		
		addInfoMessageFromResource("operacao.realizada.sucesso");

		return OUTCOME_LISTA;

	}

	public void selecionarTodosTipoDados(ActionEvent e) {
		if (this.tipoDadosFuncaoTransacaoProjetoTOListDataModel != null) {
			for (TipoDadosFuncaoTransacaoProjetoTO item : this.tipoDadosFuncaoTransacaoProjetoTOListDataModel) {
				if (!item.getAtravessaFronteira())
					item.setAtravessaFronteira(true);
			}
		}
	}

	@TransactionExceptionInterceptor
	public void incluirArquivoLogico() {
		List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

		if (this.funcaoDadosTO == null) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED", JSFUtil
					.getMessageFromResource("label.funcao.dados")));
		}

		if (funcaoDadosJaIncluida(this.funcaoDadosTO)) {
			mensagens.add(new MensagemErro("msg.alerta.funcao.dados.existe"));
		}

		if (!mensagens.isEmpty()) {
			throw new BusinessException(mensagens);
		}

		ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO = new ArquivoReferenciadoProjetoTO();
		arquivoReferenciadoProjetoTO
				.setTipoDadosFuncaoTransacaoProjetos(new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>());

		if (this.funcaoDadosTO instanceof FuncaoDadosProjetoTO) {
			FuncaoDadosProjetoTO funcaoDadosProjetoTO = (FuncaoDadosProjetoTO) this.funcaoDadosTO;

			arquivoReferenciadoProjetoTO.setFuncaoDadosProjetoTO((FuncaoDadosProjetoTO) funcaoDadosTO);
			arquivoReferenciadoProjetoTO.setFuncaoDadosBaselineTO(null);

			TipoDadosFuncaoTransacaoProjetoTO tipoDadosFuncaoTransacaoProjetoTO;
			for (TipoRegistroProjetoTO tipoRegistroProjetoTO : funcaoDadosProjetoTO.getTipoRegistroProjetoTOs()) {

				for (TipoDadosFuncaoDadosProjetoTO tipoDadosFuncaoDadosProjetoTO : tipoRegistroProjetoTO
						.getTipoDadosFuncaoDadosProjetoTOs()) {

					//O TD só deve aparever se for referenciado pela aplicação no cadastro do ALI
					if (!tipoDadosFuncaoDadosProjetoTO.getReferenciaAplicacao())
						continue;
					tipoDadosFuncaoTransacaoProjetoTO = new TipoDadosFuncaoTransacaoProjetoTO();
					tipoDadosFuncaoTransacaoProjetoTO.setArquivoReferenciadoProjetoTO(arquivoReferenciadoProjetoTO);
					tipoDadosFuncaoTransacaoProjetoTO.setAtravessaFronteira(false);
					tipoDadosFuncaoTransacaoProjetoTO.setAtributoTO(tipoDadosFuncaoDadosProjetoTO.getAtributoTO());
					arquivoReferenciadoProjetoTO.getTipoDadosFuncaoTransacaoProjetos().add(
							tipoDadosFuncaoTransacaoProjetoTO);
					this.tipoDadosFuncaoTransacaoProjetoTOListDataModel.add(tipoDadosFuncaoTransacaoProjetoTO);

					if (this.tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp != null) {

						this.tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp.add(tipoDadosFuncaoTransacaoProjetoTO);
					}
				}

			}

		} else {
			FuncaoDadosBaselineTO funcaoDadosBaselineTO = (FuncaoDadosBaselineTO) this.funcaoDadosTO;

			arquivoReferenciadoProjetoTO.setFuncaoDadosBaselineTO((FuncaoDadosBaselineTO) funcaoDadosTO);
			arquivoReferenciadoProjetoTO.setFuncaoDadosProjetoTO(null);

			TipoDadosFuncaoTransacaoProjetoTO tipoDadosFuncaoTransacaoProjetoTO;
			for (TipoRegistroBaseLineTO tipoRegistroBaselineTO : funcaoDadosBaselineTO.getTipoRegistroList()) {

				for (TipoDadosFuncaoDadosBaselineTO tipoDadosFuncaoDadosBaselineTO : tipoRegistroBaselineTO
						.getTipoDadosBaselineTOs()) {
					tipoDadosFuncaoTransacaoProjetoTO = new TipoDadosFuncaoTransacaoProjetoTO();
					tipoDadosFuncaoTransacaoProjetoTO.setArquivoReferenciadoProjetoTO(arquivoReferenciadoProjetoTO);
					tipoDadosFuncaoTransacaoProjetoTO.setAtravessaFronteira(false);
					tipoDadosFuncaoTransacaoProjetoTO.setAtributoTO(tipoDadosFuncaoDadosBaselineTO.getAtributoTO());
					arquivoReferenciadoProjetoTO.getTipoDadosFuncaoTransacaoProjetos().add(
							tipoDadosFuncaoTransacaoProjetoTO);
					this.tipoDadosFuncaoTransacaoProjetoTOListDataModel.add(tipoDadosFuncaoTransacaoProjetoTO);

					if (this.tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp != null) {

						this.tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp.add(tipoDadosFuncaoTransacaoProjetoTO);
					}
				}

			}
		}

		arquivoReferenciadoProjetoTO.setFuncaoTransacaoProjetoTO(funcaoTransacaoProjetoTO);

		this.funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs().add(arquivoReferenciadoProjetoTO);

		this.arquivoReferenciadoProjetoTOListDataModel.add(arquivoReferenciadoProjetoTO);

	}

	@TransactionExceptionInterceptor
	public void incluirTipoDadosDER() {
		List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

		if (this.tipoDadosDERInclusao == null) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED", JSFUtil
					.getMessageFromResource("label.tipo.dado.der")));
		}

		if (this.tipoDadosDERInclusao.getNmCampo() == null || "".equals(this.tipoDadosDERInclusao.getNmCampo().trim())) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED", JSFUtil
					.getMessageFromResource("label.campo")));
		}

		if (this.tipoDadosDERInclusao.getDsCampo() == null || "".equals(this.tipoDadosDERInclusao.getDsCampo().trim())) {
			mensagens.add(new MensagemErro("javax.faces.component.UIInput.REQUIRED", JSFUtil
					.getMessageFromResource("label.descricao")));
		}

		for (TipoDadosDERFuncaoTransacaoProjetoTO td : this.tipoDadosDERFuncaoTransacaoProjetoTOs) {
			if (td.getNmCampo().equals(this.tipoDadosDERInclusao.getNmCampo())) {
				mensagens.add(new MensagemErro("msg.alerta.tipo.dados.der.existe"));
				break;
			}
		}

		if (!mensagens.isEmpty()) {
			throw new BusinessException(mensagens);
		}

		this.tipoDadosDERFuncaoTransacaoProjetoTOs.add(this.tipoDadosDERInclusao);
		this.tipoDadosDERInclusao.setFuncaoTransacaoProjetoTO(this.funcaoTransacaoProjetoTO);

		this.tipoDadosDERInclusao = new TipoDadosDERFuncaoTransacaoProjetoTO();

	}

	@TransactionExceptionInterceptor
	public void excluirTipoDadosDER() {

		this.tipoDadosDERFuncaoTransacaoProjetoTOs.remove(this.tipoDadosDERFuncaoTransacaoProjetoTO);
		this.funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs().remove(
				this.tipoDadosDERFuncaoTransacaoProjetoTO);
		this.tipoDadosDERFuncaoTransacaoProjetoTO.setFuncaoTransacaoProjetoTO(null);

	}

	public void editarTipoDadosDER() {

		tipoDadosDERInclusao = this.tipoDadosDERFuncaoTransacaoProjetoTO;
		tipoDadosDERFuncaoTransacaoProjetoTOs.remove(this.tipoDadosDERFuncaoTransacaoProjetoTO);

	}

	private boolean funcaoDadosJaIncluida(FuncaoDadosTO funcaoDadosTO) {
		for (ArquivoReferenciadoProjetoTO arquivoreferenciadoTO : arquivoReferenciadoProjetoTOListDataModel) {
			if (arquivoreferenciadoTO.isFuncaoDadosProjeto()) {
				return arquivoreferenciadoTO.getFuncaoDadosProjetoTO().equals(funcaoDadosTO);
			} else {
				return arquivoreferenciadoTO.getFuncaoDadosBaselineTO().equals(funcaoDadosTO);
			}
		}
		return false;
	}

	public void atualizarTotalARs() {

		this.funcaoTransacaoProjetoTO.setQtArquivoReferenciado(this.funcaoTransacaoProjetoTO
				.getArquivoReferenciadoProjetoTOs().size());
		if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			this.funcaoTransacaoProjetoTO.setQtArquivoReferenciado(0);
		}

	}

	public void atualizarTotalTDs() {
		Integer qtTipoDados = new Integer(0);
		for (TipoDadosFuncaoTransacaoProjetoTO tipoDadosFuncaoTransacaoProjetoTO : getTipoDadosFuncaoTransacaoProjetoTOListDataModel()) {
			if (tipoDadosFuncaoTransacaoProjetoTO.getAtravessaFronteira()) {
				++qtTipoDados;
			}
		}

		qtTipoDados += this.funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs().size();

		this.funcaoTransacaoProjetoTO.setQtTipoDados(qtTipoDados);

		if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {

			this.funcaoTransacaoProjetoTO.setQtTipoDados(0);
		}
	}

	public void actionAlteraTipoContagem(ActionEvent event) {

		this.funcaoTransacaoProjetoTO = new FuncaoTransacaoProjetoTO();
		this.funcaoTransacaoProjetoTO.setProjetoTO(this.projetoTO);
		this.funcaoTransacaoProjetoTO.setArquivoReferenciadoProjetoTOs(new ArrayList<ArquivoReferenciadoProjetoTO>());
		arquivoReferenciadoProjetoTOListDataModel = new ArrayList<ArquivoReferenciadoProjetoTO>();
		tipoDadosFuncaoTransacaoProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>();
		this.funcaoTransacaoBaselineTO = null;

		if (TipoContagemEnum.BASELINE.equals(this.tipoContagem)) {
			this.funcaoTransacaoBaseLinePorSistema = this.projetoTO.getSistemaTO().getBaselineTO() != null ? this.projetoTO
					.getSistemaTO().getBaselineTO().getFuncaoTransacaoBaselineTOs()
					: null;

			for (FuncaoTransacaoProjetoTO func : this.projetoTO.getFuncaoTransacaoProjetoTOList()) {

				if (func.getFuncaoTransacaoBaselineTO() != null) {
					this.funcaoTransacaoBaseLinePorSistema.remove(func.getFuncaoTransacaoBaselineTO());
				}

			}
		}

	}

	public void actionAlteraTransacao(ActionEvent event) {

		if (funcaoTransacaoBaselineTO != null) {

			this.arquivoReferenciadoProjetoTOListDataModel = new ArrayList<ArquivoReferenciadoProjetoTO>();
			this.tipoDadosFuncaoTransacaoProjetoTOListDataModel = new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>();

			List<ArquivoReferenciadoBaselineTO> arqsReferenciados = this.funcaoTransacaoBaselineTO
					.getArquivoReferenciadoBaselineTOs();

			ArquivoReferenciadoProjetoTO arqProjeto;
			for (ArquivoReferenciadoBaselineTO arqBse : arqsReferenciados) {

				arqProjeto = new ArquivoReferenciadoProjetoTO();
				arqProjeto.setFuncaoDadosBaselineTO(arqBse.getFuncaoDadosBaselineTO());
				arqProjeto.setFuncaoDadosProjetoTO(null);
				arqProjeto.setFuncaoTransacaoProjetoTO(this.funcaoTransacaoProjetoTO);
				arqProjeto.setTipoDadosFuncaoTransacaoProjetos(new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>());

				TipoDadosFuncaoTransacaoProjetoTO tdTransPro;
				for (TipoDadosFuncaoTransacaoBaselineTO tdBse : arqBse.getTipoDadosFuncaoTransacaoBaselineTOs()) {

					tdTransPro = new TipoDadosFuncaoTransacaoProjetoTO();
					tdTransPro.setArquivoReferenciadoProjetoTO(arqProjeto);
					tdTransPro.setFlAtravessaFronteira(tdBse.getFlAtravessaFronteira());
					tdTransPro.setAtributoTO(tdBse.getAtributoTO());

					arqProjeto.getTipoDadosFuncaoTransacaoProjetos().add(tdTransPro);

					this.tipoDadosFuncaoTransacaoProjetoTOListDataModel.add(tdTransPro);
				}

				this.funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs().add(arqProjeto);

				this.arquivoReferenciadoProjetoTOListDataModel.add(arqProjeto);
			}

			this.funcaoTransacaoProjetoTO
					.setTipoDadosDERFuncaoTransacaoProjetoTOs(new ArrayList<TipoDadosDERFuncaoTransacaoProjetoTO>());
			TipoDadosDERFuncaoTransacaoProjetoTO tdDERftp;
			for (TipoDadosDERFuncaoTransacaoBaselineTO tdDERftb : this.funcaoTransacaoBaselineTO
					.getTipoDadosDERFuncaoTransacaoBaselineTOs()) {

				tdDERftp = new TipoDadosDERFuncaoTransacaoProjetoTO();
				tdDERftp.setNmCampo(tdDERftb.getNmCampo());
				tdDERftp.setFlCalculadoDerivado(tdDERftb.getFlCalculadoDerivado());
				tdDERftp.setDsCampo(tdDERftb.getDsCampo());
				tdDERftp.setFuncaoTransacaoProjetoTO(this.funcaoTransacaoProjetoTO);

				this.funcaoTransacaoProjetoTO.getTipoDadosDERFuncaoTransacaoProjetoTOs().add(tdDERftp);
			}

			this.tipoDadosDERFuncaoTransacaoProjetoTOs = this.funcaoTransacaoProjetoTO
					.getTipoDadosDERFuncaoTransacaoProjetoTOs();

			this.funcaoTransacaoProjetoTO.setAprovado(false);//Para inspeção de qualidade
			this.funcaoTransacaoProjetoTO.setComentarioRejeicao(null);//Para inspeção de qualidade
			this.funcaoTransacaoProjetoTO.setDescricao(this.funcaoTransacaoBaselineTO.getDescricao());
			this.funcaoTransacaoProjetoTO.setElementoContagemTO(null);//O usuário deve selecionar o elemento de contagem
			this.funcaoTransacaoProjetoTO.setFuncaoTransacaoBaselineTO(this.funcaoTransacaoBaselineTO);
			this.funcaoTransacaoProjetoTO.setFuncaoTransacaoTP(funcaoTransacaoBaselineTO.getFuncaoTransacaoTP());
			this.funcaoTransacaoProjetoTO.setNome(this.funcaoTransacaoBaselineTO.getNome());
			this.funcaoTransacaoProjetoTO.setProjetoTO(this.projetoTO);
			this.funcaoTransacaoProjetoTO.setSituacaoContagem(null);//Para inspeção de qualidade
			this.funcaoTransacaoProjetoTO.setQtArquivoReferenciado(null);//Será calculado ao salvar a função
			this.funcaoTransacaoProjetoTO.setQtTipoDados(null);//Será calculado ao salvar a função
			this.funcaoTransacaoProjetoTO.setValorPontoFuncao(null);//Será calculado ao salvar a função

		}
	}

	public void valueChangedArquivoLogico(ValueChangeEvent event) {
		ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTO = (ArquivoReferenciadoProjetoTO) event.getNewValue();
		if (arquivoReferenciadoProjetoTO != null) {
			this.arquivoReferenciadoProjetoTOListDataModel.add(arquivoReferenciadoProjetoTO);
		}
	}

	public void selecionarTodos() {

		this.selecionarTodos = !this.selecionarTodos;

		for (TipoDadosFuncaoTransacaoProjetoTO td : this.tipoDadosFuncaoTransacaoProjetoTOListDataModel) {
			td.setAtravessaFronteira(this.selecionarTodos);
		}
	}

	@TransactionExceptionInterceptor
	public void excluirArquivoReferenciado() {

		this.arquivoReferenciadoProjetoTOListDataModel.remove(this.arquivoReferenciadoProjetoTOSelect);

		Iterator<TipoDadosFuncaoTransacaoProjetoTO> iterator = this.tipoDadosFuncaoTransacaoProjetoTOListDataModel
				.iterator();
		while (iterator.hasNext()) {
			TipoDadosFuncaoTransacaoProjetoTO item = iterator.next();
			if (item.getArquivoReferenciadoProjetoTO().equals(arquivoReferenciadoProjetoTOSelect)) {
				iterator.remove();
			}
		}

		this.arquivoReferenciadoProjetoTOSelect.setFuncaoTransacaoProjetoTO(null);
		this.funcaoTransacaoProjetoTO.getArquivoReferenciadoProjetoTOs()
				.remove(this.arquivoReferenciadoProjetoTOSelect);

	}

	//	@TransactionExceptionInterceptor
	//	public void excluirTipoDadosDER() {
	//		
	//		this.tipoDadosDERFuncaoTransacaoProjetoTOs.remove(this.tipoDadosDERInclusao);
	//		
	//		
	//	}

	public List<FuncaoDadosProjetoTO> getFuncoeDadosPelosARs() {
		List<FuncaoDadosProjetoTO> funcs;

		if (this.arquivoReferenciadoProjetoTOListDataModel == null
				|| this.arquivoReferenciadoProjetoTOListDataModel.isEmpty()) {
			return null;
		}

		funcs = new ArrayList<FuncaoDadosProjetoTO>();

		for (ArquivoReferenciadoProjetoTO arq : this.arquivoReferenciadoProjetoTOListDataModel) {
			funcs.add(arq.getFuncaoDadosProjetoTO());
		}

		return funcs;
	}

	public List<FuncaoTransacaoProjetoTO> getFuncaoTransacaoProjetoTODataModel() {
		return funcaoTransacaoProjetoTODataModel;
	}

	public void setFuncaoTransacaoProjetoTODataModel(List<FuncaoTransacaoProjetoTO> funcaoTransacaoProjetoTODataModel) {
		this.funcaoTransacaoProjetoTODataModel = funcaoTransacaoProjetoTODataModel;
	}

	public FuncaoTransacaoProjetoTO getFuncaoTransacaoProjetoTOSelect() {
		return funcaoTransacaoProjetoTOSelect;
	}

	public void setFuncaoTransacaoProjetoTOSelect(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTOSelect) {
		this.funcaoTransacaoProjetoTOSelect = funcaoTransacaoProjetoTOSelect;
	}

	public FuncaoTransacaoProjetoDAO getFuncaoTransacaoProjetoDAO() {
		return funcaoTransacaoProjetoDAO;
	}

	public void setFuncaoTransacaoProjetoDAO(FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO) {
		this.funcaoTransacaoProjetoDAO = funcaoTransacaoProjetoDAO;
	}

	public FuncaoTransacaoBaselineDAO getFuncaoTransacaoBaselineDAO() {
		return funcaoTransacaoBaselineDAO;
	}

	public void setFuncaoTransacaoBaselineDAO(FuncaoTransacaoBaselineDAO funcaoTransacaoBaselineDAO) {
		this.funcaoTransacaoBaselineDAO = funcaoTransacaoBaselineDAO;
	}

	public List<FuncaoTransacaoBaselineTO> getFuncaoTransacaoBaseLinePorSistema() {
		return funcaoTransacaoBaseLinePorSistema;
	}

	public void setFuncaoTransacaoBaseLinePorSistema(List<FuncaoTransacaoBaselineTO> funcaoTransacaoBaseLinePorSistema) {
		this.funcaoTransacaoBaseLinePorSistema = funcaoTransacaoBaseLinePorSistema;
	}

	public ElementoContagemDAO getElementoContagemDAO() {
		return elementoContagemDAO;
	}

	public void setElementoContagemDAO(ElementoContagemDAO elementoContagemDAO) {
		this.elementoContagemDAO = elementoContagemDAO;
	}

	public List<ElementoContagemTO> getElementoContagemTOList() {
		return elementoContagemTOList;
	}

	public void setElementoContagemTOList(List<ElementoContagemTO> elementoContagemTOList) {
		this.elementoContagemTOList = elementoContagemTOList;
	}

	public List<ArquivoReferenciadoProjetoTO> getArquivoReferenciadoProjetoTOListDataModel() {
		return arquivoReferenciadoProjetoTOListDataModel;
	}

	public void setArquivoReferenciadoProjetoTOListDataModel(
			List<ArquivoReferenciadoProjetoTO> arquivoReferenciadoProjetoTOListDataModel) {
		this.arquivoReferenciadoProjetoTOListDataModel = arquivoReferenciadoProjetoTOListDataModel;
	}

	public ArquivoReferenciadoProjetoTO getArquivoReferenciadoProjetoTOSelect() {
		return arquivoReferenciadoProjetoTOSelect;
	}

	public void setArquivoReferenciadoProjetoTOSelect(ArquivoReferenciadoProjetoTO arquivoReferenciadoProjetoTOSelect) {
		this.arquivoReferenciadoProjetoTOSelect = arquivoReferenciadoProjetoTOSelect;
	}

	public List<TipoDadosFuncaoTransacaoProjetoTO> getTipoDadosFuncaoTransacaoProjetoTOListDataModel() {
		return tipoDadosFuncaoTransacaoProjetoTOListDataModel;
	}

	public void setTipoDadosFuncaoTransacaoProjetoTOListDataModel(
			List<TipoDadosFuncaoTransacaoProjetoTO> tipoDadosFuncaoTransacaoProjetoTOListDataModel) {
		this.tipoDadosFuncaoTransacaoProjetoTOListDataModel = tipoDadosFuncaoTransacaoProjetoTOListDataModel;
	}

	public ArquivoReferenciadoProjetoDAO getArquivoReferenciadoProjetoDAO() {
		return arquivoReferenciadoProjetoDAO;
	}

	public void setArquivoReferenciadoProjetoDAO(ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO) {
		this.arquivoReferenciadoProjetoDAO = arquivoReferenciadoProjetoDAO;
	}

	public FuncaoDadosProjetoTO getFuncaoDadoProjetoTOIncluir() {
		return funcaoDadoProjetoTOIncluir;
	}

	public void setFuncaoDadoProjetoTOIncluir(FuncaoDadosProjetoTO funcaoDadoProjetoTOIncluir) {
		this.funcaoDadoProjetoTOIncluir = funcaoDadoProjetoTOIncluir;
	}

	public TipoContagemEnum getTipoContagem() {
		return tipoContagem;
	}

	public void setTipoContagem(TipoContagemEnum tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public ProjetoTO getProjetoTO() {
		return projetoTO;
	}

	public void setProjetoTO(ProjetoTO projetoTO) {
		this.projetoTO = projetoTO;
	}

	public FuncaoTransacaoBaselineTO getFuncaoTransacaoBaselineTO() {
		return funcaoTransacaoBaselineTO;
	}

	public void setFuncaoTransacaoBaselineTO(FuncaoTransacaoBaselineTO funcaoTransacaoBaselineTO) {
		this.funcaoTransacaoBaselineTO = funcaoTransacaoBaselineTO;
	}

	public SistemaDAO getSistemaDAO() {
		return sistemaDAO;
	}

	public void setSistemaDAO(SistemaDAO sistemaDAO) {
		this.sistemaDAO = sistemaDAO;
	}

	public FuncaoDadosProjetoDAO getFuncaoDadosProjetoDAO() {
		return funcaoDadosProjetoDAO;
	}

	public void setFuncaoDadosProjetoDAO(FuncaoDadosProjetoDAO funcaoDadosProjetoDAO) {
		this.funcaoDadosProjetoDAO = funcaoDadosProjetoDAO;
	}

	public List<FuncaoDadosTO> getFuncaoDadosProjetoTOList() {
		return funcaoDadosTOList;
	}

	public void setFuncaoDadosProjetoTOList(List<FuncaoDadosTO> funcaoDadosProjetoTOList) {
		this.funcaoDadosTOList = funcaoDadosProjetoTOList;
	}

	public OperacoesEnum getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacoesEnum operacao) {
		this.operacao = operacao;
	}

	public FuncaoTransacaoProjetoTO getFuncaoTransacaoProjetoTO() {
		return funcaoTransacaoProjetoTO;
	}

	public void setFuncaoTransacaoProjetoTO(FuncaoTransacaoProjetoTO funcaoTransacaoProjetoTO) {
		this.funcaoTransacaoProjetoTO = funcaoTransacaoProjetoTO;
	}

	public ArquivoReferenciadoProjetoTO getArquivoReferenciadoIncluir() {
		return arquivoReferenciadoIncluir;
	}

	public void setArquivoReferenciadoIncluir(ArquivoReferenciadoProjetoTO arquivoReferenciadoIncluir) {
		this.arquivoReferenciadoIncluir = arquivoReferenciadoIncluir;
	}

	public FuncaoDadosTO getFuncaoDadosTO() {
		return funcaoDadosTO;
	}

	public void setFuncaoDadosTO(FuncaoDadosTO funcaoDadosTO) {
		this.funcaoDadosTO = funcaoDadosTO;
	}

	public List<FuncaoDadosTO> getFuncaoDadosTOList() {
		if (funcaoDadosTOList != null) {
			Collections.sort(funcaoDadosTOList, new Comparator<FuncaoDadosTO>() {

				@Override
				public int compare(FuncaoDadosTO o1, FuncaoDadosTO o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});
		}
		return funcaoDadosTOList;
	}

	public void setFuncaoDadosTOList(List<FuncaoDadosTO> funcaoDadosTOList) {
		this.funcaoDadosTOList = funcaoDadosTOList;
	}

	public TipoDadosDERFuncaoTransacaoProjetoTO getTipoDadosDERInclusao() {
		return tipoDadosDERInclusao;
	}

	public void setTipoDadosDERInclusao(TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERInclusao) {
		this.tipoDadosDERInclusao = tipoDadosDERInclusao;
	}

	public List<TipoDadosDERFuncaoTransacaoProjetoTO> getTipoDadosDERFuncaoTransacaoProjetoTOs() {
		return tipoDadosDERFuncaoTransacaoProjetoTOs;
	}

	public void setTipoDadosDERFuncaoTransacaoProjetoTOs(
			List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs) {
		this.tipoDadosDERFuncaoTransacaoProjetoTOs = tipoDadosDERFuncaoTransacaoProjetoTOs;
	}

	public TipoDadosDERFuncaoTransacaoProjetoTO getTipoDadosDERFuncaoTransacaoProjetoTO() {
		return tipoDadosDERFuncaoTransacaoProjetoTO;
	}

	public void setTipoDadosDERFuncaoTransacaoProjetoTO(
			TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERFuncaoTransacaoProjetoTO) {
		this.tipoDadosDERFuncaoTransacaoProjetoTO = tipoDadosDERFuncaoTransacaoProjetoTO;
	}

	//	public List<TipoDadosDERFuncaoTransacaoProjetoTO> getTipoDadosDERFuncaoTransacaoProjetoTOs() {
	//		return tipoDadosDERFuncaoTransacaoProjetoTOs;
	//	}
	//
	//	public void setTipoDadosDERFuncaoTransacaoProjetoTOs(
	//			List<TipoDadosDERFuncaoTransacaoProjetoTO> tipoDadosDERFuncaoTransacaoProjetoTOs) {
	//		this.tipoDadosDERFuncaoTransacaoProjetoTOs = tipoDadosDERFuncaoTransacaoProjetoTOs;
	//	}
	//
	//	public TipoDadosDERFuncaoTransacaoProjetoTO getTipoDadosDERFuncaoTransacaoProjetoTO() {
	//		return tipoDadosDERFuncaoTransacaoProjetoTO;
	//	}
	//
	//	public void setTipoDadosDERFuncaoTransacaoProjetoTO(
	//			TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERFuncaoTransacaoProjetoTO) {
	//		this.tipoDadosDERFuncaoTransacaoProjetoTO = tipoDadosDERFuncaoTransacaoProjetoTO;
	//	}
	//
	//	public TipoDadosDERFuncaoTransacaoProjetoTO getTipoDadosDERInclusao() {
	//		return tipoDadosDERInclusao;
	//	}
	//
	//	public void setTipoDadosDERInclusao(
	//			TipoDadosDERFuncaoTransacaoProjetoTO tipoDadosDERInclusao) {
	//		this.tipoDadosDERInclusao = tipoDadosDERInclusao;
	//	}

	public void buscarAtributos() {

		//		if (tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp != null)
		//			tipoDadosFuncaoTransacaoProjetoTOListDataModel = tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp;

		if (this.dsAtributoFiltro == null || this.dsAtributoFiltro.trim().length() == 0) {
			if (tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp != null)
				tipoDadosFuncaoTransacaoProjetoTOListDataModel = tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp;
		} else {

			if (tipoDadosFuncaoTransacaoProjetoTOListDataModel != null) {
				List<TipoDadosFuncaoTransacaoProjetoTO> temp = new ArrayList<TipoDadosFuncaoTransacaoProjetoTO>();
				for (TipoDadosFuncaoTransacaoProjetoTO item : tipoDadosFuncaoTransacaoProjetoTOListDataModel) {
					if (item.getAtributoTO().getDsAtributo() != null
							&& (item.getAtributoTO().getDsAtributo().toUpperCase()
									.startsWith(this.dsAtributoFiltro.toUpperCase()) || item.getAtributoTO()
									.getDsAtributo().toUpperCase().contains(this.dsAtributoFiltro.toUpperCase())))
						temp.add(item);
				}
				if (!temp.isEmpty()) {
					if (tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp == null)
						tipoDadosFuncaoTransacaoProjetoTOListDataModelBkp = tipoDadosFuncaoTransacaoProjetoTOListDataModel;

					tipoDadosFuncaoTransacaoProjetoTOListDataModel = temp;
				}
			}
		}
	}

	public String getDsAtributoFiltro() {
		return dsAtributoFiltro;
	}

	public void setDsAtributoFiltro(String dsAtributoFiltro) {
		this.dsAtributoFiltro = dsAtributoFiltro;
	}

	public boolean isElementoContagemTipoQuantidade() {
		return elementoContagemTipoQuantidade;
	}

	public void changeElementoContagem(ActionEvent event) {

		if (funcaoTransacaoProjetoTO.getElementoContagemTO() == null)
			return;

		elementoContagemTipoQuantidade = false;
		if (funcaoTransacaoProjetoTO.getElementoContagemTO().getTpElementoContagem() == TipoElementoContagemEnum.QUANTIDADE) {
			elementoContagemTipoQuantidade = true;
		}
	}

}