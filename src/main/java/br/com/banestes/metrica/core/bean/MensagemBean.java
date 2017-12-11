package br.com.sgpf.metrica.core.bean;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.com.sgpf.metrica.core.faces.CustomFacesMessages;
import br.com.sgpf.metrica.core.util.JSFUtil;



@Name("mensagemBean")
@Scope(ScopeType.SESSION)
public class MensagemBean extends AbstractWebBean {

	private static final long serialVersionUID = 4023506947072981295L;

	public String getMessagemErroPadrao() {
		try {
			((CustomFacesMessages) Component.getInstance(CustomFacesMessages.COMPONENT_NAME)).removerMensagensDuplicadas();
		} catch (Exception e) {
		}
		return JSFUtil.getMessageFromResource("org.jboss.seam.TransactionFailed");
	}

}