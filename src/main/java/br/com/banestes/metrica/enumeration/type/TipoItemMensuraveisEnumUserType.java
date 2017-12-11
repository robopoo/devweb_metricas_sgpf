package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoItemMensuraveisEnum;

public class TipoItemMensuraveisEnumUserType extends GenericEnumUserType<String, TipoItemMensuraveisEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public TipoItemMensuraveisEnumUserType() throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		super(TipoItemMensuraveisEnum.class, TipoItemMensuraveisEnum.values(), "getValue", Types.CHAR);
	}
}