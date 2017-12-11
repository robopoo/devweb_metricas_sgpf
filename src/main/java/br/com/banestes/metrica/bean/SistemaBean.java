package br.com.sgpf.metrica.bean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractCrudWebBean;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.dao.SistemaDAO;
import br.com.sgpf.metrica.entity.SistemaTO;

@Name("sistemaBean")
@Scope(ScopeType.CONVERSATION)
public class SistemaBean extends
		AbstractCrudWebBean<SistemaTO, Long, SistemaDAO, SistemaTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 612662533136152125L;

	@Out(required = false)
	private SistemaTO sistemaFiltro;

	@In(create = true)
	@Out(required = false)
	private IDataModel<SistemaTO, Long, SistemaTO> sistemaDataModel;

	@Out(required = false)
	private SistemaTO sistemaTO;

	@In(create = true)
	private SistemaDAO sistemaDAO;

	public void preRender() {
		sistemaFiltro = new SistemaTO();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
	}

	@Override
	protected void executarPreEventos() {

		if(OperacoesEnum.INSERCAO.equals(getOperacao()) 
				|| OperacoesEnum.ALTERACAO.equals(getOperacao())){
			
			Integer qtSistemas = sistemaDAO.countSistemasByCodigo(this.sistemaTO.getCdSistema(), this.sistemaTO.getEmpresaTO(), this.sistemaTO.getIdSistema());
			
			if(qtSistemas > 0){
				throw new BusinessException("msg.erro.codigo.sistema.duplicado");
			}
		}
		
		
	}
	
	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.sistemaFiltro = new SistemaTO();
		getListagem().limpar();
	}

	@Override
	protected SistemaDAO getDao() {
		return sistemaDAO;
	}

	@Override
	protected SistemaTO getVo() {
		return sistemaTO;
	}

	@Override
	protected SistemaTO getFiltro() {
		return sistemaFiltro;
	}

	@Override
	protected void setVo(SistemaTO vo) {
		this.sistemaTO = vo;

	}

	@Override
	protected IDataModel<SistemaTO, Long, SistemaTO> getListagem() {
		return sistemaDataModel;
	}

	@Override
	protected void setListagem(IDataModel<SistemaTO, Long, SistemaTO> listagem) {
		this.sistemaDataModel = listagem;
	}

	@TransactionExceptionInterceptor
	public void excluir() {
		setVo((SistemaTO) getSelecao().clone());
		setOperacao(OperacoesEnum.EXCLUSAO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
	}
	
	public boolean existeProjetos(SistemaTO sistema) {

		if (sistema != null && sistema.getProjetoTOList() != null
				&& !sistema.getProjetoTOList().isEmpty()) {
			return true;
		}

		return false;

	}

}
