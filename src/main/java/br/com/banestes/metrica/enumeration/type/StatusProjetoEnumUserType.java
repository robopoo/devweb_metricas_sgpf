package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.StatusProjetoEnum;

public class StatusProjetoEnumUserType extends GenericEnumUserType<String, StatusProjetoEnum> {

	private static final long serialVersionUID = 1;

	public StatusProjetoEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(StatusProjetoEnum.class, StatusProjetoEnum.values(), "getValue", Types.CHAR);
	}
	
}
