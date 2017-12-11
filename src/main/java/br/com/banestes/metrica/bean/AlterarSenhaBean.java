package br.com.sgpf.metrica.bean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.annotation.TransactionExceptionInterceptor;
import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.util.AES;
import br.com.sgpf.metrica.dao.UsuarioDAO;
import br.com.sgpf.metrica.entity.UsuarioTO;

@Name("alterarSenhaBean")
@Scope(ScopeType.CONVERSATION)
@Deprecated
public class AlterarSenhaBean extends AbstractWebBean {

	private static final long serialVersionUID = 8641676470073483962L;

	@In(create = true)
	private UsuarioDAO usuarioDAO;

	private String login;

	private String senhaAntiga;

	private String novaSenha;

	private String repetirNovaSenha;

	@Override
	public void preRender() {
		this.login = null;
		this.senhaAntiga = null;
		this.novaSenha = null;
		this.repetirNovaSenha = null;
	}

	@TransactionExceptionInterceptor
	public void salvar() {
		String senhaAntigaEncrypt = AES.encrypt(this.senhaAntiga);
		UsuarioTO usuario = usuarioDAO.findByLoginSenha(this.login,
				senhaAntigaEncrypt);
		if (usuario == null) {
			throw new BusinessException("msg.erro.usuario.senha.invalidos");
		}
		if (!this.novaSenha.equals(this.repetirNovaSenha)) {
			throw new BusinessException("msg.erro.senhaRepetir");
		}
		String novaSenhaEncrypt = AES.encrypt(this.novaSenha);
		if (senhaAntigaEncrypt.equals(novaSenhaEncrypt)) {
			throw new BusinessException(
					"msg.erro.nova.senha.igual.senha.antiga");
		}
		usuario.setSenha(novaSenhaEncrypt);
		usuarioDAO.update(usuario);
	}

	public void finalizarAlteracaoSenha() {
		addInfoMessageFromResource("senha.alterada.sucesso");
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getRepetirNovaSenha() {
		return repetirNovaSenha;
	}

	public void setRepetirNovaSenha(String repetirNovaSenha) {
		this.repetirNovaSenha = repetirNovaSenha;
	}

}