/**
 * 
 */
package br.com.sgpf.metrica.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.security.Identity;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.util.JSFUtil;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.dao.ArquivoReferenciadoProjetoDAO;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.dao.EntidadeDAO;
import br.com.sgpf.metrica.dao.FuncaoDadosProjetoDAO;
import br.com.sgpf.metrica.dao.FuncaoTransacaoProjetoDAO;
import br.com.sgpf.metrica.dao.ParametroSistemaDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.dao.TipoRegistroProjetoDAO;
import br.com.sgpf.metrica.entity.AnalistaTO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.FuncaoDadosProjetoTO;
import br.com.sgpf.metrica.entity.FuncaoTO;
import br.com.sgpf.metrica.entity.FuncaoTransacaoProjetoTO;
import br.com.sgpf.metrica.entity.ParametroSistemaTO;
import br.com.sgpf.metrica.entity.ProjetoTO;
import br.com.sgpf.metrica.entity.ServicoNaoMensuravelTO;
import br.com.sgpf.metrica.entity.SistemaTO;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;
import br.com.sgpf.metrica.mail.IMailImplements;
import br.com.sgpf.metrica.mail.MailInspecaoQA;
import br.com.sgpf.metrica.mail.MailUtil;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 26/02/2014
 */

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.1
 * @since 10/03/2014
 * @time 16:37:25
 */

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * 
 * @author Fernando Padrão Silveira <mailto:fernando.silveira@pdcase.com.br>
 * @version 1.0.2
 * @since 18/05/2014
 * @time 16:37:25
 */
@Name("projetoBean")
@Scope(ScopeType.CONVERSATION)
public class ProjetoBean extends AbstractCrudWebBean<ProjetoTO, Long, ProjetoDAO, ProjetoTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3194842241363633177L;

	private static final String SESSION_ATT_NAME = "idProjetoFiltroSession";

	@Out(required = false)
	private EmpresaTO empresaFiltro;

	@Out(required = false)
	private ProjetoTO projetoFiltro;

	@In(create = true)
	@Out(required = false)
	private IDataModel<ProjetoTO, Long, ProjetoTO> projetoDataModel;

	@Out(required = false)
	private ProjetoTO projetoTO;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	@Out(required = false)
	private EmpresaTO empresaTO;

	@In(create = true)
	private ContratoDAO contratoDAO;

	@In(create = true)
	private AnalistaDAO analistaDAO;

	@Out(required = false)
	private List<AnalistaTO> analistaTOEmpresaList;

	@Out(required = false)
	private List<AnalistaTO> gestorTOEmpresaList;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	@Out(required = false)
	private SistemaTO sistemaFuncaoDados;

	@In(create = true)
	private EntidadeDAO entidadeDAO;

	private OperacoesEnum operacaoInclusaoFuncao;

	@In(create = true)
	private FuncaoDadosProjetoDAO funcaoDadosProjetoDAO;

	@In(create = true)
	private FuncaoTransacaoProjetoDAO funcaoTransacaoProjetoDAO;

	@Out(required = false)
	private List<SistemaTO> sistemasDisponiveisParaEnvolver;

	@DataModel
	private List<SistemaTO> sistemasEnvolvidos;

	@DataModelSelection("sistemasEnvolvidos")
	private SistemaTO sistemasEnvolvidoSelecionado;

	@Out(required = false)
	private SistemaTO sistemaTO;

	@In(create = true)
	@Out(required = true)
	private FuncaoDadosController funcaoDadosController;

	@In(create = true)
	@Out(required = true)
	private FuncaoTransacaoController funcaoTransacaoController;

	@In(create = true)
	private TipoRegistroProjetoDAO tipoRegistroProjetoDAO;

	@In(create = true)
	private ArquivoReferenciadoProjetoDAO arquivoReferenciadoProjetoDAO;

	@In
	private Identity identity;

	@In(create = true)
	private MailUtil mailUtil;

	@In(create = true)
	private ParametroSistemaDAO parametroSistemaDAO;

	@Create()
	public void atualizarFiltro() {

		Long idProjeto = (Long) JSFUtil.getSessionAttribute(SESSION_ATT_NAME);

		if (idProjeto != null) {
			ProjetoTO projetoTO = projetoDAO.findById(idProjeto);
			this.setEmpresaFiltro(projetoTO.getSistemaTO().getEmpresaTO());
			setProjetoFiltro(new ProjetoTO());
			getProjetoFiltro().setContratoTO(projetoTO.getContratoTO());
			getProjetoFiltro().setSistemaTO(projetoTO.getSistemaTO());
		} else {
			setProjetoFiltro(new ProjetoTO());
			setEmpresaFiltro(new EmpresaTO());
		}

	}

	@Override
	public void preRender() {
		// setProjetoFiltro(new ProjetoTO());
		// setEmpresaFiltro(new EmpresaTO());
		atualizarFiltro();

		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	public String editar() {
		String outcome = super.editar();
		this.setAnalistaTOEmpresaList(this.buscarAnalistas(this.projetoTO.getSistemaTO().getEmpresaTO()));
		this.setGestorTOEmpresaList(this.buscarGestores(this.projetoTO.getSistemaTO().getEmpresaTO()));
		this.setSistemasEnvolvidos(this.getVo().getSistemasEnvolvidos());
		return outcome;
	}

	@Override
	public String visualizar() {
		String outcome = super.visualizar();
		this.setAnalistaTOEmpresaList(this.buscarAnalistas(this.projetoTO.getSistemaTO().getEmpresaTO()));
		this.setGestorTOEmpresaList(this.buscarGestores(this.projetoTO.getSistemaTO().getEmpresaTO()));
		this.setSistemasEnvolvidos(this.getVo().getSistemasEnvolvidos());
		return outcome;
	}

	public List<ContratoTO> filtrarContratosAtivos() {

		List<ContratoTO> contratosAtivos = new ArrayList<ContratoTO>();
		if (this.empresaFiltro != null) {
			for (ContratoTO contratoTO : this.empresaFiltro.getContratoTOlist()) {
				if (StatusEnum.ATIVADO.equals(contratoTO.getStContrato())) {
					contratosAtivos.add(contratoTO);
				}
			}
		}

		return contratosAtivos;

	}

	public void calcularTotais(ProjetoTO projetoTO) {

		BigDecimal qtPontoFuncaoDados = BigDecimal.ZERO;

		BigDecimal qtPontoFuncaoTransacao = BigDecimal.ZERO;

		BigDecimal qtPontoFuncaoLocal = BigDecimal.ZERO;

		BigDecimal qtPontoFuncaoServicoNaoMensuravel = BigDecimal.ZERO;

		BigDecimal qtPontoFuncaoMensuravel;

		if (this.projetoTO.getFuncaoDadosProjetoTOList() != null) {
			for (FuncaoTO funcao : projetoTO.getFuncaoDadosProjetoTOList()) {
				qtPontoFuncaoDados = qtPontoFuncaoDados.add(funcao.getValorPontoFuncao());
				qtPontoFuncaoLocal = qtPontoFuncaoLocal.add(funcao.getValorPontoFuncaoLocal());
			}
		}

		if (this.projetoTO.getFuncaoTransacaoProjetoTOList() != null) {
			for (FuncaoTO funcao : projetoTO.getFuncaoTransacaoProjetoTOList()) {
				qtPontoFuncaoTransacao = qtPontoFuncaoTransacao.add(funcao.getValorPontoFuncao());
				qtPontoFuncaoLocal = qtPontoFuncaoLocal.add(funcao.getValorPontoFuncaoLocal());
			}
		}

		if (this.projetoTO.getServicoNaoMensuravelTOs() != null)
			for (ServicoNaoMensuravelTO serv : projetoTO.getServicoNaoMensuravelTOs()) {
				qtPontoFuncaoServicoNaoMensuravel = qtPontoFuncaoServicoNaoMensuravel.add(serv.getQtPontoFuncaoFinal());
			}

		qtPontoFuncaoMensuravel = qtPontoFuncaoDados.add(qtPontoFuncaoTransacao);

		this.projetoTO.setQtPontoFuncaoDados(qtPontoFuncaoDados);
		this.projetoTO.setQtPontoFuncaoLocal(qtPontoFuncaoLocal);
		this.projetoTO.setQtPontoFuncaoMensuravel(qtPontoFuncaoMensuravel);
		this.projetoTO.setQtPontoFuncaoServicoNaoMensuravel(qtPontoFuncaoServicoNaoMensuravel);
		this.projetoTO.setQtPontoFuncaoTransacao(qtPontoFuncaoTransacao);

	}

	// TODO Validar se possui Servico não mensurável
	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((ProjetoTO) getSelecao().clone());

		FuncaoDadosProjetoTO filterFdp = new FuncaoDadosProjetoTO();
		filterFdp.setProjetoTO(this.projetoTO);

		int qtFuncsDdProjeto = this.funcaoDadosProjetoDAO.getCount(filterFdp);

		FuncaoTransacaoProjetoTO filterPro = new FuncaoTransacaoProjetoTO();
		filterPro.setProjetoTO(this.projetoTO);

		int qtFuncsTrProjeto = this.funcaoDadosProjetoDAO.getCount(filterFdp);

		if (qtFuncsDdProjeto > 0 || qtFuncsTrProjeto > 0) {
			throw new BusinessException("msg.projeto.exclusao.nao.permitida");
		}

		setOperacao(OperacoesEnum.EXCLUSAO);
		super.gravar();
	}

	/**
	 * Limpar pesquisa
	 */
	public void limparPesquisa() {
		preRender();
		setConsultaRealizada(false);
		getListagem().limpar();
	}

	public void valueChangedEmpresa(ValueChangeEvent event) {
		if ((EmpresaTO) event.getNewValue() != null) {
			setAnalistaTOEmpresaList(buscarAnalistas((EmpresaTO) event.getNewValue()));
			this.setGestorTOEmpresaList(this.buscarGestores((EmpresaTO) event.getNewValue()));

		}
	}

	private List<AnalistaTO> buscarAnalistas(EmpresaTO empresaTO) {
		return this.analistaDAO.findAllAnalistaByEmpresa(empresaTO);
	}

	private List<AnalistaTO> buscarGestores(EmpresaTO empresaTO) {
		return this.analistaDAO.findAllGestorByEmpresa(empresaTO);
	}

	public void valueChangedSistema(ValueChangeEvent event) {
		SistemaTO sistemaTO = (SistemaTO) event.getNewValue();
		ProjetoTO projeto = new ProjetoTO();
		projeto.setEmpresaTransient(this.projetoTO.getEmpresaTransient());
		projeto.setSistemaTO(sistemaTO);
		this.sistemasDisponiveisParaEnvolver = sistemaDAO.findAllSistemaByProjetoEmpresaSemSistemaAtual(projeto);
	}

	public void selecionaAba(ValueChangeEvent event) {
		String tabSelected = (String) event.getNewValue();
		Contexts.getConversationContext().set("contagemFormAbaSelecionada", tabSelected);
	}

	protected void executarPreEventos() {
		switch (getOperacao()) {
		case INSERCAO:
			if (projetoDAO.ExisteProjetosByNmProjetoNmSubProjeto(this.getVo().getProjeto(), this.getVo()
					.getSubProjeto(), this.getVo().getSistemaTO()) > 0) {
				throw new BusinessException("Já existe um projeto com esta combinação (Sistema/Projeto/Subprojeto).");
			}
			this.getVo().setQtPontoFuncaoDados(BigDecimal.ZERO);
			this.getVo().setQtPontoFuncaoLocal(BigDecimal.ZERO);
			this.getVo().setQtPontoFuncaoMensuravel(BigDecimal.ZERO);
			this.getVo().setQtPontoFuncaoServicoNaoMensuravel(BigDecimal.ZERO);
			this.getVo().setQtPontoFuncaoTransacao(BigDecimal.ZERO);
			this.getVo().setStatus(StatusProjetoEnum.DIGITADO);
			break;
		case ALTERACAO:
			if (projetoDAO.ExisteProjetosByNmProjetoNmSubProjeto(this.getVo().getProjeto(), this.getVo()
					.getSubProjeto(), this.getVo().getSistemaTO()) > 1) {
				throw new BusinessException("Já existe um projeto com esta combinação (Sistema/Projeto/Subprojeto).");
			}
		}
	}

	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	/**
	 * @return the empresaFiltro
	 */
	public EmpresaTO getEmpresaFiltro() {
		return empresaFiltro;
	}

	/**
	 * @param empresaFiltro
	 *            the empresaFiltro to set
	 */
	public void setEmpresaFiltro(EmpresaTO empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}

	/**
	 * @return the projetoFiltro
	 */
	public ProjetoTO getProjetoFiltro() {
		return projetoFiltro;
	}

	/**
	 * @param projetoFiltro
	 *            the projetoFiltro to set
	 */
	public void setProjetoFiltro(ProjetoTO projetoFiltro) {
		this.projetoFiltro = projetoFiltro;
	}

	@Override
	protected ProjetoDAO getDao() {
		return this.projetoDAO;
	}

	@Override
	protected ProjetoTO getVo() {
		return this.projetoTO;
	}

	@Override
	protected ProjetoTO getFiltro() {
		return this.projetoFiltro;
	}

	@Override
	protected void setVo(ProjetoTO vo) {
		this.projetoTO = vo;
	}

	@Override
	protected IDataModel<ProjetoTO, Long, ProjetoTO> getListagem() {
		return this.projetoDataModel;
	}

	@Override
	protected void setListagem(IDataModel<ProjetoTO, Long, ProjetoTO> listagem) {
		this.projetoDataModel = listagem;
	}

	/**
	 * @return the analistaTOEmpresaList
	 */
	public List<AnalistaTO> getAnalistaTOEmpresaList() {
		return analistaTOEmpresaList;
	}

	/**
	 * @param analistaTOEmpresaList
	 *            the analistaTOEmpresaList to set
	 */
	public void setAnalistaTOEmpresaList(List<AnalistaTO> analistaTOEmpresaList) {
		this.analistaTOEmpresaList = analistaTOEmpresaList;
	}

	/**
	 * @return the sistemaFuncaoDados
	 */
	public SistemaTO getSistemaFuncaoDados() {
		return sistemaFuncaoDados;
	}

	/**
	 * @param sistemaFuncaoDados
	 *            the sistemaFuncaoDados to set
	 */
	public void setSistemaFuncaoDados(SistemaTO sistemaFuncaoDados) {
		this.sistemaFuncaoDados = sistemaFuncaoDados;
	}

	public String avaliarQualidade() {
		this.preRender();
		this.empresaFiltro = null;
		this.projetoFiltro = this.getSelecao();
		this.projetoDataModel.limpar();
		return "inspQualidade";
	}

	/**
	 * @return the operacaoInclusaoFuncao
	 */
	public OperacoesEnum getOperacaoInclusaoFuncao() {
		return operacaoInclusaoFuncao;
	}

	/**
	 * @param operacaoInclusaoFuncao
	 *            the operacaoInclusaoFuncao to set
	 */
	public void setOperacaoInclusaoFuncao(OperacoesEnum operacaoInclusaoFuncao) {
		this.operacaoInclusaoFuncao = operacaoInclusaoFuncao;
	}

	public List<SistemaTO> getSistemasDisponiveisParaEnvolver() {
		return sistemasDisponiveisParaEnvolver;
	}

	public SistemaTO getSistemasEnvolvidoSelecionado() {
		return sistemasEnvolvidoSelecionado;
	}

	public void setSistemasEnvolvidoSelecionado(SistemaTO sistemasEnvolvidoSelecionado) {
		this.sistemasEnvolvidoSelecionado = sistemasEnvolvidoSelecionado;
	}

	public void excluirSistema() {
		this.sistemasEnvolvidos.remove(sistemasEnvolvidoSelecionado);
		this.projetoTO.getSistemasEnvolvidos().remove(sistemasEnvolvidoSelecionado);
	}

	@TransactionExceptionInterceptor
	public void incluirSistema() {
		if (this.sistemaTO == null) {
			throw new BusinessException("É necessário informar o Sistema Associado");
		}

		if (this.projetoTO != null && this.projetoTO.getSistemaTO() != null
				&& this.sistemaTO.equals(this.projetoTO.getSistemaTO())) {
			throw new BusinessException("O Sistema Associado deve ser diferente do Sistema deste Projeto");
		}

		if (this.projetoTO.getSistemasEnvolvidos() == null)
			this.projetoTO.setSistemasEnvolvidos(new ArrayList<SistemaTO>());

		this.sistemasEnvolvidos = this.projetoTO.getSistemasEnvolvidos();

		if (!this.sistemasEnvolvidos.contains(sistemaTO))
			this.sistemasEnvolvidos.add(sistemaTO);

		this.sistemaTO = null;
	}

	public String novo() {
		String novo = super.novo();
		getVo().setDtCriacao(new Date());

		ContratoTO cnt = new ContratoTO();
		cnt.setEmpresaTO(new EmpresaTO());

		this.projetoTO.setContratoTO(cnt);

		return novo;
	}

	public SistemaTO getSistemaTO() {
		return sistemaTO;
	}

	public void setSistemaTO(SistemaTO sistemaTO) {
		this.sistemaTO = sistemaTO;
	}

	public List<SistemaTO> getSistemasEnvolvidos() {
		return sistemasEnvolvidos;
	}

	public void setSistemasEnvolvidos(List<SistemaTO> sistemasEnvolvidos) {
		this.sistemasEnvolvidos = sistemasEnvolvidos;
	}

	public FuncaoDadosController getFuncaoDadosController() {
		return funcaoDadosController;
	}

	public FuncaoTransacaoController getFuncaoTransacaoController() {
		return funcaoTransacaoController;
	}

	public List<AnalistaTO> getGestorTOEmpresaList() {
		return gestorTOEmpresaList;
	}

	public void setGestorTOEmpresaList(List<AnalistaTO> gestorTOEmpresaList) {
		this.gestorTOEmpresaList = gestorTOEmpresaList;
	}

	public void concluirProjeto() {

		super.editar();

		this.projetoTO.setStatus(StatusProjetoEnum.CONCLUIDO);
		this.projetoDAO.update(this.projetoTO);

		addInfoMessageFromResource("Projeto concluído enviado com sucesso");

	}

	public void enviarEmail() {
		try {

			super.editar();

			ParametroSistemaTO param = parametroSistemaDAO.find();

			if (param == null)
				throw new BusinessException("Parâmetro não cadastrado");

			IMailImplements mailTO = new MailInspecaoQA(projetoTO).getMailTO();
			mailTO.setDestino(param.getDsEmailQA());
			mailUtil.send(mailTO);

			this.projetoTO.setStatus(StatusProjetoEnum.ENVIADO_INSPECAO);
			this.projetoDAO.update(this.projetoTO);

			addInfoMessageFromResource("Email enviado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessageFromResource("Ocorreu um erro ao enviar o email. Tente novamente ou comunique ao suporte");
		}
	}
}