package br.com.sgpf.metrica.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.dao.ContratoDAO;
import br.com.sgpf.metrica.dao.FatorEquivalenciaDAO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.ElementoContagemTO;
import br.com.sgpf.metrica.entity.EmpresaTO;
import br.com.sgpf.metrica.entity.FatorEquivalenciaTO;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;

@Name("fatorEquivalenciaBean")
@Scope(ScopeType.CONVERSATION)
public class FatorEquivalenciaBean extends
		AbstractCrudWebBean<FatorEquivalenciaTO, Long, FatorEquivalenciaDAO, FatorEquivalenciaTO> {
	private static final long serialVersionUID = 612662533136152125L;
	@Out(required = false)
	private FatorEquivalenciaTO filtro;
	@Out(required = false)
	private EmpresaTO filtroEmpresa;
	@Out(required = false)
	private ContratoTO filtroContrato;

	//
	// @DataModel
	// private List<FatorEquivalenciaTO> fatorEquivalenciaTOList;
	//
	// @DataModelSelection(value = "fatorEquivalenciaTOList")
	// private FatorEquivalenciaTO fatorEquivalenciaTOSelect;

	@In(create = true)
	@Out(required = false)
	private IDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> fatorEquivalenciaDataModel;
	@Out(required = false)
	private FatorEquivalenciaTO fatorEquivalenciaTO;
	@In(create = true)
	private FatorEquivalenciaDAO fatorEquivalenciaDAO;
	@In(create = true)
	private ContratoDAO contratoDAO;
	@Out(required = false)
	private List<ContratoTO> contratosTOlistFiltro;
	@Out(required = false)
	private List<ContratoTO> contratosTOlist;
	private boolean itemMensuravel;

	/*
	 * Variaveis de detalhe Elemento Contagem
	 */
	@DataModel
	private List<ElementoContagemTO> elementoContagemTOList;
	@DataModelSelection(value = "elementoContagemTOList")
	private ElementoContagemTO elementoContagemTOSelect;

	@Out(required = false)
	private ElementoContagemTO elementoContagemTO;

	@Out(required = false)
	private ElementoContagemTO elementoContagemTOEditado;

	private String ajudaTipoFatorEquivalencia = "Selecione um tipo de Elemento.";

	public EmpresaTO getFiltroEmpresa() {
		return filtroEmpresa;
	}

	public void setFiltroEmpresa(EmpresaTO filtroEmpresa) {
		this.filtroEmpresa = filtroEmpresa;
	}

	public ContratoTO getFiltroContrato() {
		return filtroContrato;
	}

	public void setFiltroContrato(ContratoTO filtroContrato) {
		this.filtroContrato = filtroContrato;
	}

	public void valueChangedEmpresa(ValueChangeEvent event) {
		filtroEmpresa = ((EmpresaTO) event.getNewValue());

		if (filtroEmpresa != null && filtroEmpresa.getContratoTOlist() != null) {
			if (this.getOperacao() == OperacoesEnum.ALTERACAO || this.getOperacao() == OperacoesEnum.INSERCAO) {

				List<ContratoTO> listc = new ArrayList<ContratoTO>();

				for (ContratoTO c : filtroEmpresa.getContratoTOlist()) {
					if (c.getStContrato() == StatusEnum.ATIVADO) {
						listc.add(c);
					}
				}

				contratosTOlist = listc;

			} else {
				contratosTOlist = filtroEmpresa.getContratoTOlist();

			}
			contratosTOlistFiltro = filtroEmpresa.getContratoTOlist();
		} else {
			contratosTOlist = new ArrayList<ContratoTO>();
		}
	}

	public void valueChangedFator(ValueChangeEvent event) {

		if (event.getNewValue() == null) {
			ajudaTipoFatorEquivalencia = "Selecione um tipo de Elemento.";
		} else {
			atualizaAjuda((TipoElementoContagemEnum) event.getNewValue());
		}
	}

	public void valueChangedContrato(ValueChangeEvent event) {
		filtroContrato = ((ContratoTO) event.getNewValue());

		if (fatorEquivalenciaTO != null) {
			fatorEquivalenciaTO.setContratoTO(filtroContrato);

			if (filtroEmpresa == null) {
				filtroContrato = new ContratoTO();
			}
		}
	}

	public List<ContratoTO> getContratosTOlist() {
		return contratosTOlist;
	}

	public void setContratosTOlist(List<ContratoTO> contratosTOlist) {
		this.contratosTOlist = contratosTOlist;
	}

	public void preRender() {
		filtroContrato = null;
		filtroEmpresa = null;
		filtro = new FatorEquivalenciaTO();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		filtro = new FatorEquivalenciaTO();
		filtroContrato = null;
		filtroEmpresa = null;
		contratosTOlist = new ArrayList<ContratoTO>();
		this.fatorEquivalenciaTO = new FatorEquivalenciaTO();
		getListagem().limpar();
	}

	@Override
	public void buscar() {
		if (filtroContrato == null) {
			filtroContrato = new ContratoTO();
		}
		filtroContrato.setEmpresaTO(filtroEmpresa);
		filtro.setContratoTO(filtroContrato);
		super.buscar();
	}

	@Override
	protected FatorEquivalenciaDAO getDao() {
		return fatorEquivalenciaDAO;
	}

	@Override
	protected FatorEquivalenciaTO getVo() {
		return fatorEquivalenciaTO;
	}

	@Override
	protected FatorEquivalenciaTO getFiltro() {
		return filtro;
	}

	@Override
	protected void setVo(FatorEquivalenciaTO vo) {
		this.fatorEquivalenciaTO = vo;
	}

	@Override
	protected IDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> getListagem() {
		return fatorEquivalenciaDataModel;
	}

	@Override
	protected void setListagem(IDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> listagem) {
		this.fatorEquivalenciaDataModel = listagem;
	}

	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((FatorEquivalenciaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.EXCLUSAO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	public String novo() {
		super.novo();
		elementoContagemTOList = new ArrayList<ElementoContagemTO>();
		elementoContagemTO = new ElementoContagemTO();
		filtroEmpresa = new EmpresaTO();

		return getInclusaoOutcome();
	}

	@Override
	public String visualizar() {
		super.visualizar();

		if (fatorEquivalenciaTO != null) {
			elementoContagemTOList = fatorEquivalenciaTO.getElementoContagemTOList();
			filtroContrato = fatorEquivalenciaTO.getContratoTO();
		}

		if (elementoContagemTOList == null) {
			elementoContagemTOList = new ArrayList<ElementoContagemTO>();
		}

		filtroEmpresa = fatorEquivalenciaTO.getContratoTO().getEmpresaTO();
		filtroContrato = fatorEquivalenciaTO.getContratoTO();
		contratosTOlist = filtroEmpresa.getContratoTOlist();
		contratosTOlistFiltro = filtroEmpresa.getContratoTOlist();

		return getVisualizacaoOutcome();
	}

	@Override
	public String editar() {
		super.editar();

		if (fatorEquivalenciaTO.getElementoContagemTOList() != null) {
			elementoContagemTOList = fatorEquivalenciaTO.getElementoContagemTOList();
		} else {
			elementoContagemTOList = new ArrayList<ElementoContagemTO>();
		}

		elementoContagemTO = new ElementoContagemTO();
		filtroEmpresa = fatorEquivalenciaTO.getContratoTO().getEmpresaTO();
		filtroContrato = fatorEquivalenciaTO.getContratoTO();

		//contratosTOlist = filtroEmpresa.getContratoTOlist();
		List<ContratoTO> listc = new ArrayList<ContratoTO>();

		for (ContratoTO c : filtroEmpresa.getContratoTOlist()) {
			if (c.getStContrato() == StatusEnum.ATIVADO) {
				listc.add(c);
			}
		}

		contratosTOlist = listc;

		contratosTOlistFiltro = filtroEmpresa.getContratoTOlist();

		return getAlteracaoOutcome();
	}

	@Override
	public String gravar() {
		if ((getOperacao() != OperacoesEnum.EXCLUSAO) && (elementoContagemTOList != null)
				&& (elementoContagemTOList.size() > 0)) {
			for (ElementoContagemTO e : elementoContagemTOList) {
				e.setFatorEquivalenciaTO(fatorEquivalenciaTO);
			}

			fatorEquivalenciaTO.setElementoContagemTOList(elementoContagemTOList);

			elementoContagemTOList = new ArrayList<ElementoContagemTO>();
			filtroContrato = new ContratoTO();
			filtroEmpresa = new EmpresaTO();
			contratosTOlist = new ArrayList<ContratoTO>();
			filtro = new FatorEquivalenciaTO();

			return super.gravar();
		} else {
			if ((getOperacao() != OperacoesEnum.EXCLUSAO) && (getOperacao() != OperacoesEnum.PUBLICAR)) {
				addErrorMessageFromResource("O Fator deve possuir pelo menos um elemento de contagem associado.");

				return null;
			} else {
				filtroContrato = new ContratoTO();
				filtroEmpresa = new EmpresaTO();
				contratosTOlist = new ArrayList<ContratoTO>();
				filtro = new FatorEquivalenciaTO();

				return super.gravar();
			}
		}
	}

	public void editarElemento() {
		elementoContagemTO = (ElementoContagemTO) elementoContagemTOSelect.clone();
		elementoContagemTOEditado = elementoContagemTOSelect;
	}

	public void removerElemento() {
		boolean erro = elementoContagemTOList.remove(elementoContagemTOSelect);

		elementoContagemTOSelect.setFatorEquivalenciaTO(null);

		if (!erro) {
			addErrorMessageFromResource("Elemento não localizado");
			erro = true;
		}
	}

	public void addElemento() {
		boolean erro = false;

		if ((elementoContagemTO == null) || elementoContagemTO.getCodElementoContagem().isEmpty()) {
			addErrorMessageFromResource("Campo Código de Elemento deve ser preenchido.");
			erro = true;
		}

		if ((elementoContagemTO == null) || (elementoContagemTO.getTpElementoContagem() == null)) {
			addErrorMessageFromResource("Campo Tipo de Elemento deve ser preenchido.");
			erro = true;
		}

		if ((elementoContagemTO == null) || elementoContagemTO.getDescResumida().isEmpty()) {
			addErrorMessageFromResource("Campo Descrição Resumida deve ser preenchido.");
			erro = true;
		}

		if ((elementoContagemTO == null) || elementoContagemTO.getDescDetalhada().isEmpty()) {
			addErrorMessageFromResource("Campo Descrição Detalhada deve ser preenchido.");
			erro = true;
		}

		if ((elementoContagemTO == null) || (elementoContagemTO.getVlrFatorEquivalencia() == null)) {
			addErrorMessageFromResource("Campo Fator de Equivalência deve ser preenchido.");
			erro = true;
		}

		if ((elementoContagemTO == null) || (elementoContagemTO.getIndItemMensuravel() == null)) {
			addErrorMessageFromResource("Campo Item Mensurável deve ser preenchido.");
			erro = true;
		}

		for (ElementoContagemTO e : elementoContagemTOList) {
			if ((elementoContagemTOEditado == null || elementoContagemTOEditado != e)
					&& e.getCodElementoContagem().trim().equals(elementoContagemTO.getCodElementoContagem().trim())) {
				addErrorMessageFromResource("Código de elemento já existente ");
				erro = true;

				break;
			}
		}

		if (!erro) {

			if (this.elementoContagemTOEditado == null) {
				elementoContagemTOList.add(elementoContagemTO);
			} else {
				elementoContagemTOList.set(elementoContagemTOList.indexOf(this.elementoContagemTOEditado),
						this.elementoContagemTO);
			}

			this.elementoContagemTOEditado = null;
			elementoContagemTO = new ElementoContagemTO();
		}
	}

	public IDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> getFatorEquivalenciaDataModel() {
		return fatorEquivalenciaDataModel;
	}

	public void setFatorEquivalenciaDataModel(
			IDataModel<FatorEquivalenciaTO, Long, FatorEquivalenciaTO> fatorEquivalenciaDataModel) {
		this.fatorEquivalenciaDataModel = fatorEquivalenciaDataModel;
	}

	public FatorEquivalenciaTO getFatorEquivalenciaTO() {
		return fatorEquivalenciaTO;
	}

	public void setFatorEquivalenciaTO(FatorEquivalenciaTO fatorEquivalenciaTO) {
		this.fatorEquivalenciaTO = fatorEquivalenciaTO;
	}

	public FatorEquivalenciaDAO getFatorEquivalenciaDAO() {
		return fatorEquivalenciaDAO;
	}

	public void setFatorEquivalenciaDAO(FatorEquivalenciaDAO fatorEquivalenciaDAO) {
		this.fatorEquivalenciaDAO = fatorEquivalenciaDAO;
	}

	public List<ElementoContagemTO> getElementoContagemTOList() {
		return elementoContagemTOList;
	}

	public void setElementoContagemTOList(List<ElementoContagemTO> elementoContagemTOList) {
		this.elementoContagemTOList = elementoContagemTOList;
	}

	public ElementoContagemTO getElementoContagemTOSelect() {
		return elementoContagemTOSelect;
	}

	public void setElementoContagemTOSelect(ElementoContagemTO elementoContagemTOSelect) {
		this.elementoContagemTOSelect = elementoContagemTOSelect;
	}

	public ElementoContagemTO getElementoContagemTO() {
		return elementoContagemTO;
	}

	public void setElementoContagemTO(ElementoContagemTO elementoContagemTO) {
		this.elementoContagemTO = elementoContagemTO;
	}

	public void setFiltro(FatorEquivalenciaTO filtro) {
		this.filtro = filtro;
	}

	public boolean isItemMensuravel() {
		return itemMensuravel;
	}

	public void setItemMensuravel(boolean itemMensuravel) {
		this.itemMensuravel = itemMensuravel;
	}

	public List<ContratoTO> getContratosTOlistFiltro() {
		return contratosTOlistFiltro;
	}

	public void setContratosTOlistFiltro(List<ContratoTO> contratosTOlistFiltro) {
		this.contratosTOlistFiltro = contratosTOlistFiltro;
	}

	@TransactionExceptionInterceptor
	public String publicar() {
		setVo((FatorEquivalenciaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.PUBLICAR);
		fatorEquivalenciaDAO.publicar(getVo());

		getVo().setDtVigenciaInicio(new Date());

		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);

		return null;
	}

	public void atualizaAjuda(TipoElementoContagemEnum tipoElementoContagemEnum) {
		if (tipoElementoContagemEnum == null) {
			ajudaTipoFatorEquivalencia = "Selecione um tipo de Elemento.";
		}
		switch (tipoElementoContagemEnum) {
		case DEFLATOR:
			ajudaTipoFatorEquivalencia = " Define um percentual ao qual será aplicado no valor de contribuição do PF.\n"
					+ "PF Local = PF * Fator de Equivalência";
			break;
		case FIXO:
			ajudaTipoFatorEquivalencia = "Define um valor fixo para remuneração de uma manutenção "
					+ "classificada como não-mensurável (requisito não-funcional).\n"
					+ "PF Local = Fator de Equivalência";
			break;
		case QUANTIDADE:
			ajudaTipoFatorEquivalencia = " Define um valor unitário para o(s) ítem(ns) em manutenção.\n"
					+ "PF Local = Qtde Itens * Fator de Equivalência";
			break;
		default:
			ajudaTipoFatorEquivalencia = "Ajuda não encontrada";
			break;
		}
	}

	public String getAjudaTipoFatorEquivalencia() {
		return ajudaTipoFatorEquivalencia;
	}

	public void setAjudaTipoFatorEquivalencia(String ajudaTipoFatorEquivalencia) {
		this.ajudaTipoFatorEquivalencia = ajudaTipoFatorEquivalencia;
	}

	//
	// @TransactionExceptionInterceptor
	// public String desativar() {
	// setVo((EmpresaTO) getSelecao().clone());
	// setOperacao(OperacoesEnum.ALTERACAO);
	// getVo().setStEmpresa(StatusEnum.DESATIVADO);
	// super.gravar();
	// buscar();
	// setOperacao(OperacoesEnum.FILTRAGEM);
	// return null;
	// }

	public boolean existeContagem(ContratoTO contrato) {

		if (contrato != null && contrato.getProjetoTOList() != null && !contrato.getProjetoTOList().isEmpty()) {
			return true;
		}

		return false;

	}
}
