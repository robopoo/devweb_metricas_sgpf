package br.com.sgpf.metrica.core.faces;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.faces.FacesMessages;

@Scope(ScopeType.CONVERSATION)
@Name(CustomFacesMessages.COMPONENT_NAME)
@Install(precedence = Install.APPLICATION)
@BypassInterceptors
public class CustomFacesMessages extends FacesMessages {

	private static final long serialVersionUID = 3615425609540261955L;

	public void removerMensagensDuplicadas() {
		int size = getMessages() != null ? getMessages().size() : 0;
		for (int i = 0; i < size - 1; i++) {
			getMessages().remove(i);
		}
	}
}
