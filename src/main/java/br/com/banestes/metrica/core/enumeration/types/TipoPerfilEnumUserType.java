package br.com.sgpf.metrica.core.enumeration.types;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.enumeration.TipoPerfilEnum;

public class TipoPerfilEnumUserType extends GenericEnumUserType<String, TipoPerfilEnum> {

	private static final long serialVersionUID = -2032336844446514427L;

	public TipoPerfilEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(TipoPerfilEnum.class, TipoPerfilEnum.values(), "getValue", Types.CHAR);
	}
}
