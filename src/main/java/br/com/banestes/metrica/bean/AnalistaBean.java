package br.com.sgpf.metrica.bean;

import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.vo.MensagemErro;
import br.com.sgpf.metrica.dao.AnalistaDAO;
import br.com.sgpf.metrica.dao.ProjetoDAO;
import br.com.sgpf.metrica.entity.AnalistaTO;
import br.com.sgpf.metrica.enumeration.TipoAnalistaEnum;

@Name("analistaBean")
@Scope(ScopeType.CONVERSATION)
public class AnalistaBean extends AbstractCrudWebBean<AnalistaTO, Long, AnalistaDAO, AnalistaTO> {

	private static final long serialVersionUID = 4589129913597782094L;

	@Out(required = false)
	private AnalistaTO analistaFiltro;

	@In(create = true)
	@Out(required = false)
	private IDataModel<AnalistaTO, Long, AnalistaTO> analistaDataModel;

	@Out(required = false)
	private AnalistaTO analistaTO;

	@In(create = true)
	private AnalistaDAO analistaDAO;

	@In(create = true)
	private ProjetoDAO projetoDAO;

	public void preRender() {

		analistaFiltro = new AnalistaTO();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void actionAlterarEmpresa(ActionEvent event) {
		if (analistaTO.getEmpresaTO() != null && SimNaoEnum.SIM.equals(analistaTO.getEmpresaTO().getFlFornecedor())) {
			analistaTO.setTpAnalista(TipoAnalistaEnum.ANALISTA);
		}
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.analistaFiltro = new AnalistaTO();
		getListagem().limpar();
	}

	public void buscar() {

		getListagem().iniciar(getFiltro());
		this.setConsultaRealizada(true);
	}

	@Override
	protected AnalistaDAO getDao() {
		return analistaDAO;
	}

	@Override
	protected AnalistaTO getVo() {
		return analistaTO;
	}

	@Override
	protected AnalistaTO getFiltro() {
		return analistaFiltro;
	}

	@Override
	protected void setVo(AnalistaTO vo) {
		this.analistaTO = vo;

	}

	@Override
	protected IDataModel<AnalistaTO, Long, AnalistaTO> getListagem() {
		return analistaDataModel;
	}

	@Override
	protected void setListagem(IDataModel<AnalistaTO, Long, AnalistaTO> listagem) {
		this.analistaDataModel = listagem;
	}

	@TransactionExceptionInterceptor
	public void excluir() {

		AnalistaTO analistaTO = (AnalistaTO) getSelecao();

		Integer qtProjetos = this.projetoDAO.countProjetosByAnalista(analistaTO);

		if (qtProjetos > 0) {
			super.addErrorMessageFromResource("msg.erro.analista.associado.projeto");
			return;
		}

		setVo((AnalistaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.EXCLUSAO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	public String novo() {
		super.novo();
		return getInclusaoOutcome();

	}

	@Override
	public String visualizar() {
		super.visualizar();
		return getVisualizacaoOutcome();
	}

	@Override
	public String gravar() {

		if (!OperacoesEnum.EXCLUSAO.equals(getOperacao())) {
			if (analistaTO.getTpAnalista().equals(TipoAnalistaEnum.ANALISTA)) {
				if (analistaTO.getDsMail() == null || analistaTO.getDsMail().trim().isEmpty()) {
					addErrorMessageFromResource(new BusinessException(
							new MensagemErro("msg.analista.email.obrigatorio")));
					return null;
				}
			}
		}
		super.gravar();
		return getSucessOutcome();

	}

	@Override
	public String editar() {
		super.editar();

		return getAlteracaoOutcome();
	}

	public boolean exibeTipoAnalista() {
		if (analistaTO.getEmpresaTO() != null && SimNaoEnum.SIM.equals(analistaTO.getEmpresaTO().getFlFornecedor())) {
			analistaTO.setTpAnalista(TipoAnalistaEnum.ANALISTA);
			System.out.println("false");
			return false;
		}
		System.out.println("true");
		return true;
		
		
	}

	

	public boolean emailRequerido() {
		if (analistaTO != null) {
			if (TipoAnalistaEnum.ANALISTA.equals(analistaTO.getTpAnalista())) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

}
