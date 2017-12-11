/**
 * 
 */
package br.com.sgpf.metrica.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 13/03/2014
 * @time 10:37:54
 */

@Name("toUpperCaseConverter")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Converter()
public class ToUpperCaseConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		 return (arg2 != null) ? arg2.toUpperCase() : null;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return (arg2 != null) ? arg2.toString().toUpperCase() : null;
	}

}
