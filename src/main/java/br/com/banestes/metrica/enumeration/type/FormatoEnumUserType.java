package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.FormatoEnum;

public class FormatoEnumUserType extends GenericEnumUserType<Integer, FormatoEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public FormatoEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(FormatoEnum.class, FormatoEnum.values(), "getValue", Types.NUMERIC);
	}
}