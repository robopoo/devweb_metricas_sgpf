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
import br.com.sgpf.metrica.core.enumeration.StatusEnum;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.util.AES;
import br.com.sgpf.metrica.dao.UsuarioDAO;
import br.com.sgpf.metrica.entity.UsuarioTO;

@Deprecated
@Name("usuarioBean")
@Scope(ScopeType.CONVERSATION)
public class UsuarioBean extends AbstractCrudWebBean<UsuarioTO, Long, UsuarioDAO, UsuarioTO> {

	private static final long serialVersionUID = -5765280323103479509L;

	@Out(required = false)
	private UsuarioTO usuarioFiltro;

	@In(create = true)
	@Out(required = false)
	private IDataModel<UsuarioTO, Long, UsuarioTO> usuarioDataModel;

	@Out(required = false)
	private UsuarioTO usuario;

	@In(create = true)
	private UsuarioDAO usuarioDAO;

	private boolean resetaSenha = false;

	public void preRender() {
		usuarioFiltro = new UsuarioTO();
		setVo(null);
		setOperacao(OperacoesEnum.FILTRAGEM);
//		getListagem().limpar();
		this.resetaSenha = false;
	}

	@Override
	protected boolean atualizarListagemPosOperacao() {
		return true;
	}

	public void limparPesquisa() {
		setConsultaRealizada(false);
		this.usuarioFiltro = new UsuarioTO();
		getListagem().limpar();
	}

	@Override
	public String editar() {
		super.editar();
		this.resetaSenha = false;
		return getAlteracaoOutcome();
	}

	public String resetarSenha() {
		super.editar();
		getVo().setSenha(AES.decrypt(getVo().getSenha()));
		this.resetaSenha = true;
		return getAlteracaoOutcome();
	}

	@Override
	protected UsuarioDAO getDao() {
		return usuarioDAO;
	}

	@Override
	protected UsuarioTO getVo() {
		return usuario;
	}

	@Override
	protected UsuarioTO getFiltro() {
		return usuarioFiltro;
	}

	@Override
	protected void setVo(UsuarioTO vo) {
		this.usuario = vo;
	}

	@Override
	protected IDataModel<UsuarioTO, Long, UsuarioTO> getListagem() {
		return usuarioDataModel;
	}

	@Override
	protected void setListagem(IDataModel<UsuarioTO, Long, UsuarioTO> listagem) {
		this.usuarioDataModel = listagem;
	}

	@Override
	protected void executarPreEventos() {
		if (getDao().existeByLogin(usuario.getId(), usuario.getLogin())) {
			throw new BusinessException("msg.erro.registro.duplicado");
		}
		if (getOperacao() == OperacoesEnum.ALTERACAO && isResetaSenha()) {
			getVo().setSenha(AES.encrypt(getVo().getSenha()));
		}
	}

	@TransactionExceptionInterceptor
	public String ativar() {
		setVo((UsuarioTO) getSelecao().clone());
		setOperacao(OperacoesEnum.ALTERACAO);
		getVo().setStatus(StatusEnum.ATIVADO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
		return null;
	}

	@TransactionExceptionInterceptor
	public String desativar() {
		setVo((UsuarioTO) getSelecao().clone());
		setOperacao(OperacoesEnum.ALTERACAO);
		getVo().setStatus(StatusEnum.DESATIVADO);
		gravar();
		buscar();
		setOperacao(OperacoesEnum.FILTRAGEM);
		return null;
	}

//	public void exportar() {
//		this.tipoRelatorio = null;
//		this.fileRelatorio = null;
//		ScrollableResults results = null;
//		try {
//			results = getDao().findByCriteria(getListagem().getFiltro(), getListagem().getOrders());
//			this.fileRelatorio = new UsuarioExcelModel(UsuarioExcelModel.USUARIO, results).export();
//			this.tipoRelatorio = TipoRelatorioEnum.XLS;
//		} finally {
//			if (results != null) {
//				results.close();
//				results = null;
//			}
//		}
//	}

//	public void obterImpressao() {
//		if (this.fileRelatorio != null) {
//			downloadReport(UsuarioExcelModel.USUARIO, this.fileRelatorio, this.tipoRelatorio);
//		}
//		this.tipoRelatorio = null;
//		this.fileRelatorio = null;
//	}

	public boolean isResetaSenha() {
		return resetaSenha;
	}

	public void setResetaSenha(boolean resetarSenha) {
		this.resetaSenha = resetarSenha;
	}

}
