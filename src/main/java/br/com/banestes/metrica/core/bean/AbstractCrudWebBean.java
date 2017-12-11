package br.com.sgpf.metrica.core.bean;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.jboss.seam.core.Events;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.dao.AbstractDao;
import br.com.sgpf.metrica.core.datamodel.api.IDataModel;
import br.com.sgpf.metrica.core.entity.EntityModel;
import br.com.sgpf.metrica.core.enumeration.OperacoesEnum;

@SuppressWarnings("serial")
public abstract class AbstractCrudWebBean<T extends EntityModel<PK>, PK extends Serializable, D extends AbstractDao<T, PK, Filtro>, Filtro extends T> extends
		AbstractWebBean {

	private boolean consultaRealizada;

	protected abstract D getDao();

	protected abstract T getVo();

	protected abstract T getFiltro();

	protected abstract void setVo(T vo);

	protected abstract IDataModel<T, PK, T> getListagem();

	protected abstract void setListagem(IDataModel<T, PK, T> listagem);

	private String target;

	protected T getSelecao() {
		return getListagem().getSelecao();
	}

	protected String getInclusaoOutcome() {
		return OUTCOME_FORM;
	}

	protected String getAlteracaoOutcome() {
		return OUTCOME_FORM;
	}

	protected String getSucessOutcome() {
		return OUTCOME_LISTA;
	}

	protected String getCancelOutcome() {
		return OUTCOME_LISTA;
	}

	protected String getVisualizacaoOutcome() {
		return OUTCOME_FORM;
	}

	protected boolean limparListagem() {
		return true;
	}

	protected boolean resetForm() {
		return true;
	}

	protected boolean atualizarListagemPosOperacao() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public String novo() {
		try {
			Class<?> clazz = (Class<?>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			setVo((T) clazz.newInstance());
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			return null;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		setOperacao(OperacoesEnum.INSERCAO);
		if (limparListagem()) {
			getListagem().limpar();
			setConsultaRealizada(false);
		}
		return getInclusaoOutcome();
	}

	protected boolean editarVOSelecao() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public String editar() {
		if (editarVOSelecao()) {
			setVo((T) getSelecao().clone());
		} else {
			setVo((T) getVo().clone());
		}
		setOperacao(OperacoesEnum.ALTERACAO);
		return getAlteracaoOutcome();
	}

	public void excluir(ActionEvent e) {
		setOperacao(OperacoesEnum.EXCLUSAO);
	}

	public String visualizar() {
		setVo(getSelecao());
		if (limparListagem()) {
			getListagem().limpar();
			setConsultaRealizada(false);
		}
		setOperacao(OperacoesEnum.VISUALIZACAO);
		return getVisualizacaoOutcome();
	}

	public void buscar() {
		getListagem().iniciar(getFiltro());
		this.setConsultaRealizada(true);
	}

	@SuppressWarnings("unchecked")
	public String cancelar() {
		if (resetForm()) {
			try {
				Class<?> clazz = (Class<?>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
				setVo((T) clazz.newInstance());
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
				return null;
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
			setOperacao(OperacoesEnum.NENHUM);
		}
		if (limparListagem()) {
			getDao().clear();
			getListagem().limpar();
			setConsultaRealizada(false);
		}
		return getCancelOutcome();
	}

	protected void executarPreEventos() {

	}

	protected void executarPosEventos() {

	}

	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	@TransactionExceptionInterceptor
	public String gravar() {
		if (getOperacao() != OperacoesEnum.INSERCAO && getOperacao() != OperacoesEnum.ALTERACAO && getOperacao() != OperacoesEnum.EXCLUSAO
				&& getOperacao() != OperacoesEnum.ATIVACAO && getOperacao() != OperacoesEnum.DESATIVACAO && getOperacao() != OperacoesEnum.PUBLICAR)
			throw new IllegalStateException("Operação Ilegal");
		executarPreEventos();
		switch (getOperacao()) {
			case INSERCAO : {
				getDao().insert(getVo());
				break;
			}
			case ALTERACAO : {
				getDao().update(getVo());
				break;
			}
			case EXCLUSAO : {
				getDao().delete(getVo());
				break;
			}
			case ATIVACAO : {
				getDao().update(getVo());
				break;
			}
			case DESATIVACAO : {
				getDao().update(getVo());
				break;
			}
			case PUBLICAR : {
				getDao().update(getVo());
				break;
			}
		}
		executarPosEventos();

		if (resetForm()) {
			try {
				Class<?> clazz = (Class<?>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
				setVo((T) clazz.newInstance());
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
				return null;
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
			setOperacao(OperacoesEnum.NENHUM);
		}

		addInfoMessageFromResource("operacao.realizada.sucesso");
		if (atualizarListagemPosOperacao())
			buscar();
		return getSucessOutcome();
	}

	public void setConsultaRealizada(boolean consultaRealizada) {
		this.consultaRealizada = consultaRealizada;
	}

	public boolean isConsultaRealizada() {
		return consultaRealizada;
	}

	public void prepararLov(String target) {
		this.target = target;
		preRender();
	}

	public void selecionar() {
		Events.instance().raiseEvent(target, getListagem().getSelecao());
	}
}