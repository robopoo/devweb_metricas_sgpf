package br.com.sgpf.metrica.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("decimalConverter")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Converter()
public class DecimalConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {

	if (value == null || value.isEmpty()) {
	    return null;
	}
	String nvalue = "";
	char[] array = value.toCharArray();
	for (int i = 0; i < array.length; i++) {
	    if (array[i] != '.' && array[i] == ',')
		nvalue += ".";
	    else if (array[i] != '.' && array[i] != ',')
		nvalue += array[i];
	}

	BigDecimal result = new BigDecimal(nvalue);
	return result;
    }

    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
	BigDecimal valor = (BigDecimal) value;
	Integer precision = valor.scale();
	precision = ((precision % 2) == 0 ? precision : precision + 1);
	String pattern = "#,##0.00";
	if (precision.intValue() > 0) {
	    pattern = "#,##0.";
	    for (byte i = 0; i < precision; i++)
		pattern += "0";
	}

	final DecimalFormat df = (DecimalFormat) java.text.DecimalFormat
		.getNumberInstance(ctx.getViewRoot().getLocale());
	df.setParseBigDecimal(true);
	df.applyPattern(pattern);
	df.setMinimumFractionDigits(1);
	df.setMaximumFractionDigits(9);
	final String strValue = df.format(valor.doubleValue());
	return strValue;
    }
}
