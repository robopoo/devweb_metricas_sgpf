package br.com.sgpf.metrica.security;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import br.com.sgpf.metrica.core.bean.AbstractWebBean;
import br.com.sgpf.metrica.core.enumeration.OperacaoMenuEnum;
import br.com.sgpf.metrica.dao.UsuarioDAO;
import br.com.sgpf.metrica.entity.UsuarioTO;
import br.com.sgpf.metrica.enumeration.TipoPerfilEnum;

@Name("autenticador")
@Scope(ScopeType.SESSION)
public class Autenticador extends AbstractWebBean {

	private static final long serialVersionUID = 4023506947072981295L;

	@In
	private Credentials credentials;

	@In
	private Identity identity;

	@Out(required = false, scope = ScopeType.SESSION)
	protected UsuarioTO usuarioAutenticado;

	@In(create = true)
	private UsuarioDAO usuarioDAO;

	@Override
	public void preRender() {
		this.identity.getCredentials().setUsername(null);
		this.identity.getCredentials().setPassword(null);
	}

	public boolean permiteAcessoOperacao(OperacaoMenuEnum operacaoMenu) {
		if (operacaoMenu != null && this.usuarioAutenticado != null) {
			for (TipoPerfilEnum tipoPerfil : operacaoMenu.getTipoPerfils()) {
				if (tipoPerfil == this.usuarioAutenticado.getTipoPerfil()) {
					return true;
				}
			}
		}
		return false;
	}

}