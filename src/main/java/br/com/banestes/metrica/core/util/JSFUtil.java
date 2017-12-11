package br.com.sgpf.metrica.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.faces.FacesMessages;

import br.com.sgpf.metrica.core.exception.BusinessException;
import br.com.sgpf.metrica.core.exception.vo.MensagemErro;

public abstract class JSFUtil {

	private static ResourceBundle resourceBundle;

	private JSFUtil() {

	}

	public static final FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static final ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	public static final HttpServletRequest getRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	public static final Object getRequestParameter(String name) {
		return getRequest().getParameter(name);
	}

	public static final Object getSessionAttribute(String name) {
		HttpSession session = (HttpSession) getExternalContext().getSession(false);
		return session.getAttribute(name);
	}

	public static final void setSessionAttribute(String name, String value) {
		HttpSession session = (HttpSession) getExternalContext().getSession(false);
		session.setAttribute(name, value);
	}
	
	public static final void setSessionAttribute(String name, Object value) {
		HttpSession session = (HttpSession) getExternalContext().getSession(false);
		session.setAttribute(name, value);
	}

	private static final ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			Locale locale = null;
			if (getFacesContext() != null) {
				locale = getFacesContext().getViewRoot().getLocale();
			}
			if (locale == null) {
				locale = new Locale("pt", "BR");
			}
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			resourceBundle = ResourceBundle.getBundle("messages", locale, classLoader);
		}
		return resourceBundle;
	}

	public static final boolean containsMessage(String key) {
		return getResourceBundle().containsKey(key);
	}

	public static final String getMessageFromResource(String key) {
		return getResourceBundle().getString(key);
	}

	public static final void addInfoMessageFromResource(String key) {
		FacesMessages.instance().addFromResourceBundle(org.jboss.seam.international.StatusMessage.Severity.INFO, key, (Object[]) null);
	}

	public static final void addErrorMessageFromResource(String key) {
		FacesMessages.instance().addFromResourceBundle(org.jboss.seam.international.StatusMessage.Severity.ERROR, key, (Object[]) null);
	}

	public static final void addErrorMessageFromResource(String key, String... params) {
		FacesMessages.instance().addFromResourceBundle(org.jboss.seam.international.StatusMessage.Severity.ERROR, key, (Object[]) params);
	}

	public static final void addErrorMessageFromResource(BusinessException businessException) {
		if (businessException.getMensagens() != null && businessException.getMensagens().size() != 0) {
				addErrorMessageFromResource(businessException.getMensagens());		
		} else {
			if (containsMessage(businessException.getMessage())) {
				addErrorMessageFromResource(businessException.getMessage());
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, businessException.getMessage(), null));
			}
		}
	}
	
	public static final void addErrorMessageFromResource(MensagemErro ...mensagensErro) {
		if(mensagensErro != null){
			addErrorMessageFromResource(Arrays.asList(mensagensErro));
		}
	}
	
	public static final void addErrorMessageFromResource(List<MensagemErro> mensagensErro) {
		if(mensagensErro != null){
			for (MensagemErro mensagemErro : mensagensErro) {
				addErrorMessageFromResource(mensagemErro.getKey(), mensagemErro.getParams());
			}
		}
	}

//	public static final UsuarioTO getUsuarioLogado() {
//		return (UsuarioTO) Contexts.getSessionContext().get("usuarioAutenticado");
//	}

}
