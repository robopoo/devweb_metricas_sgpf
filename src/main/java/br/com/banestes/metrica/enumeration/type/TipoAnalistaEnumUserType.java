package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoAnalistaEnum;

public class TipoAnalistaEnumUserType extends GenericEnumUserType<String, TipoAnalistaEnum> {

	private static final long serialVersionUID = 1;

	public TipoAnalistaEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(br.com.sgpf.metrica.enumeration.TipoAnalistaEnum.class, br.com.sgpf.metrica.enumeration.TipoAnalistaEnum.values(), "getValue", Types.CHAR);
	}
	
}
