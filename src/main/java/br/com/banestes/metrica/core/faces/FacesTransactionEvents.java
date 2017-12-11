package br.com.sgpf.metrica.core.faces;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;

@Name("org.jboss.seam.transaction.facesTransactionEvents")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
public class FacesTransactionEvents extends org.jboss.seam.transaction.FacesTransactionEvents {

	@Override
	public Severity getTransactionFailedMessageSeverity() {
		return Severity.ERROR;
	}

}
