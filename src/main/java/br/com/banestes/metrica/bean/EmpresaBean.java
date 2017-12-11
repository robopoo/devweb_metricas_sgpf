package br.com.sgpf.metrica.bean;

import java.util.ArrayList;
import java.util.List;

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
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.dao.EmpresaDAO;
import br.com.sgpf.metrica.entity.ContratoTO;
import br.com.sgpf.metrica.entity.EmpresaTO;

@Name("empresaBean")
@Scope(ScopeType.CONVERSATION)
public class EmpresaBean extends
		AbstractCrudWebBean<EmpresaTO, Long, EmpresaDAO, EmpresaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 612662533136152125L;

	@Out(required = false)
	private EmpresaTO empresaFiltro;

	@In(create = true)
	@Out(required = false)
	private IDataModel<EmpresaTO, Long, EmpresaTO> empresaDataModel;

	@Out(required = false)
	private EmpresaTO empresaTO;

	@In(create = true)
	private EmpresaDAO empresaDAO;

	/*
	 * Variaveis de detalhe Contrato
	 */

	@DataModel
	List<ContratoTO> contratoTolist;

	@DataModelSelection(value = "contratoTolist")
	ContratoTO contratoTOSelect;

	@Out(required = false)
	ContratoTO contratoTO;
	
	@Out(required = false)
	ContratoTO contratoTOEditado;

	public void preRender() {
		empresaFiltro = new EmpresaTO();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.empresaFiltro = new EmpresaTO();
		this.contratoTO = new ContratoTO();
		getListagem().limpar();
	}

	@Override
	protected EmpresaDAO getDao() {
		return empresaDAO;
	}

	@Override
	protected EmpresaTO getVo() {
		return empresaTO;
	}

	@Override
	protected EmpresaTO getFiltro() {
		return empresaFiltro;
	}

	@Override
	protected void setVo(EmpresaTO vo) {
		this.empresaTO = vo;

	}

	@Override
	protected IDataModel<EmpresaTO, Long, EmpresaTO> getListagem() {
		return empresaDataModel;
	}

	@Override
	protected void setListagem(IDataModel<EmpresaTO, Long, EmpresaTO> listagem) {
		this.empresaDataModel = listagem;
	}

	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((EmpresaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.EXCLUSAO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	public String novo() {
		super.novo();
		empresaTO.setStEmpresa(StatusEnum.ATIVADO);
		empresaTO.setFlFornecedor(SimNaoEnum.NAO);
		contratoTolist = new ArrayList<ContratoTO>();
		contratoTO = new ContratoTO();
		return getInclusaoOutcome();

	}

	@Override
	public String visualizar() {
		super.visualizar();
		contratoTolist = empresaTO.getContratoTOlist();
		if (contratoTolist == null)
			contratoTolist = new ArrayList<ContratoTO>();
		return getVisualizacaoOutcome();
	}

	@Override
	public String gravar() {
		boolean erro = false;

		if (getOperacao() != OperacoesEnum.EXCLUSAO) {

			for (EmpresaTO e : empresaDAO.findByCriteria(new EmpresaTO(), null,
					null)) {

				if (e.getNmEmpresa().equals(empresaTO.getNmEmpresa())
						&& empresaTO.getIdEmpresa() != null
						&& empresaTO.getIdEmpresa().intValue() != e
								.getIdEmpresa().intValue()) {

					addErrorMessageFromResource("Nome de da empresa já existente");
					erro = true;
					break;
				}
			}
			if (!erro) {

				if (contratoTolist != null && contratoTolist.size() > 0) {

					for (ContratoTO c : contratoTolist) {
						c.setEmpresaTO(empresaTO);
					}

					empresaTO.setContratoTOlist(contratoTolist);

					super.gravar();
					contratoTolist = new ArrayList<ContratoTO>();
					return getSucessOutcome();
				} else {
					if (empresaTO.getFlFornecedor() != null
							&& empresaTO.getFlFornecedor() == SimNaoEnum.NAO) {

						addErrorMessageFromResource("A empresa deve possuir pelo menos um contrato associado.");
						return null;
					} else {
						return super.gravar();

					}
				}
			}
			return null;
		}

		return super.gravar();
	}

	@Override
	public String editar() {
		super.editar();
		if (empresaTO.getContratoTOlist() != null) {
			contratoTolist = empresaTO.getContratoTOlist();

		} else {
			contratoTolist = new ArrayList<ContratoTO>();
		}

		contratoTO = new ContratoTO();

		return getAlteracaoOutcome();
	}

	public void editarContrato() {
		contratoTO = (ContratoTO) contratoTOSelect.clone();
		contratoTOEditado = contratoTOSelect;
	}

	public void removerContrato() {
		boolean erro = contratoTolist.remove(contratoTOSelect);
		
		contratoTOSelect.setEmpresaTO(null);
		
		if (!erro) {
			addErrorMessageFromResource("Contrato não localizado");
			erro = true;

		}

	}

	public void desativarContrato() {
		contratoTOSelect.setStContrato(StatusEnum.DESATIVADO);
	}

	public void ativarContrato() {
		contratoTOSelect.setStContrato(StatusEnum.ATIVADO);
	}

	public void addContrato() {

		boolean erro = false;
		if (contratoTO == null || contratoTO.getCodigoContrato().isEmpty()) {
			addErrorMessageFromResource("Campo Nome deve ser preenchido.");
			erro = true;
		}
		if (contratoTO == null || contratoTO.getDescricaoContrato().isEmpty()) {

			addErrorMessageFromResource("Campo Descrição deve ser preenchido.");
			erro = true;
		}

		for (ContratoTO c : contratoTolist) {
			if ((contratoTOEditado == null || contratoTOEditado != c) 
					&& c.getCodigoContrato().equals(contratoTO.getCodigoContrato())) {
				addErrorMessageFromResource("Número de contrato já existente");
				erro = true;
				break;
			}
		}

		if (!erro) {

			if (contratoTOEditado == null) {//Inclusão
				contratoTO.setStContrato(StatusEnum.ATIVADO);
				contratoTolist.add(contratoTO);
			}else{//Alteração
				contratoTolist.set(contratoTolist.indexOf(contratoTOEditado), contratoTO);
			}
			
			contratoTOEditado = null;
			contratoTO = new ContratoTO();
		}
	}

	public List<ContratoTO> getContratoTolist() {
		return contratoTolist;
	}

	public void setContratoTolist(List<ContratoTO> contratoTolist) {
		this.contratoTolist = contratoTolist;
	}

	public EmpresaTO getEmpresaFiltro() {
		return empresaFiltro;
	}

	public void setEmpresaFiltro(EmpresaTO empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}

	public IDataModel<EmpresaTO, Long, EmpresaTO> getEmpresaDataModel() {
		return empresaDataModel;
	}

	public void setEmpresaDataModel(
			IDataModel<EmpresaTO, Long, EmpresaTO> empresaDataModel) {
		this.empresaDataModel = empresaDataModel;
	}

	public EmpresaTO getEmpresaTO() {
		return empresaTO;
	}

	public void setEmpresaTO(EmpresaTO empresaTO) {
		this.empresaTO = empresaTO;
	}

	public EmpresaDAO getEmpresaDAO() {
		return empresaDAO;
	}

	public void setEmpresaDAO(EmpresaDAO empresaDAO) {
		this.empresaDAO = empresaDAO;
	}

	public ContratoTO getContratoTOSelect() {
		return contratoTOSelect;
	}

	public void setContratoTOSelect(ContratoTO contratoTOSelect) {
		this.contratoTOSelect = contratoTOSelect;
	}

	public ContratoTO getContratoTO() {
		return contratoTO;
	}

	public void setContratoTO(ContratoTO contratoTO) {
		this.contratoTO = contratoTO;
	}

	@TransactionExceptionInterceptor
	public String ativar() {
		setVo((EmpresaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.ALTERACAO);
		getVo().setStEmpresa(StatusEnum.ATIVADO);
		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
		return null;
	}

	@TransactionExceptionInterceptor
	public String desativar() {
		setVo((EmpresaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.ALTERACAO);
		getVo().setStEmpresa(StatusEnum.DESATIVADO);
		super.gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
		return null;
	}

	public boolean existeContagem(ContratoTO contrato) {

		if (contrato!= null && contrato.getProjetoTOList() != null
				&& !contrato.getProjetoTOList().isEmpty()) {
			return true;
		}

		return false;

	}

	public boolean existeContagemEmpresa(EmpresaTO empresa) {

		if (empresa != null && empresa.getContratoTOlist() != null
				&& !empresa.getContratoTOlist().isEmpty()) {

			for (ContratoTO contrato : empresa.getContratoTOlist()) {

				if (contrato.getProjetoTOList() != null
						&& !contrato.getProjetoTOList().isEmpty()) {
					return true;
				}
			}
		}

		return false;

	}
	
	public boolean existeFatorEmpresa(EmpresaTO empresa) {

		if (empresa != null && empresa.getContratoTOlist() != null
				&& !empresa.getContratoTOlist().isEmpty()) {

			for (ContratoTO contrato : empresa.getContratoTOlist()) {

				if (contrato.getFatorEquivalenciaTOList() != null
						&& !contrato.getProjetoTOList().isEmpty()) {
					return true;
				}
			}
		}

		return false;

	}

}
