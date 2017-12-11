package br.com.sgpf.metrica.security.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.seam.annotations.In;
import org.jboss.seam.security.Identity;

import br.com.sgpf.metrica.core.util.JSFUtil;
import br.com.sgpf.metrica.core.util.StringUtil;

public class SecurityPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 6135354138637278046L; 

	@In
	private Identity identity;
	
	public void afterPhase(PhaseEvent event) {
		try {

			if (identity == null || !identity.isLoggedIn()) {
				String paginaAtual = JSFUtil.getFacesContext().getViewRoot().getViewId();
				if (StringUtil.isEmpty(paginaAtual)) {
					return;
				}
				if (!paginaAtual.endsWith(".xhtml")) {
					return;
				}
				if (paginaAtual.contains("login.xhtml") || paginaAtual.contains("home.xhtml")) {
					return;
				}
				String ultimaPaginaAcessada = (String) JSFUtil.getSessionAttribute("cs_ultimaPaginaAcessada");
				if (!StringUtil.isEmpty(ultimaPaginaAcessada) && ultimaPaginaAcessada.equals(paginaAtual)) {
					return;
				}
				if (JSFUtil.getRequestParameter("modo") != null && JSFUtil.getRequestParameter("modo").toString().equals("LOV")) {
					JSFUtil.setSessionAttribute("cs_ultimaPaginaAcessada", paginaAtual);
					return;
				}
//				boolean permiteAcesso = false;
				
//				for (OperacaoMenuEnum operacaoMenu : OperacaoMenuEnum.values()) {
//					for (TipoPerfilEnum tipoPerfil : operacaoMenu.getTipoPerfils()) {
//						if (usuario.getTipoPerfil() == tipoPerfil) {
//							if (paginaAtual.contains(operacaoMenu.getDiretorio())) {
//								permiteAcesso = true;
//								break;
//							}
//						}
//					}
//				}
				boolean permiteAcesso = true;
				if (!permiteAcesso) {
					JSFUtil.addErrorMessageFromResource("msg.erro.acesso.negado.pagina", paginaAtual.replaceAll("xhtml", "seam"));
					JSFUtil.getExternalContext().redirect(JSFUtil.getRequest().getContextPath() + "/view/home.seam");
				} else {
					JSFUtil.setSessionAttribute("cs_ultimaPaginaAcessada", paginaAtual);
				}
			}
		} catch (Exception e) {

		}
	}

	public void beforePhase(PhaseEvent event) {

	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
