package br.com.sgpf.metrica.core.enumeration.types;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.StatusEnum;

public class StatusEnumUserType extends GenericEnumUserType<String, StatusEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public StatusEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(br.com.sgpf.metrica.core.enumeration.StatusEnum.class, br.com.sgpf.metrica.core.enumeration.StatusEnum.values(), "getValue", Types.CHAR);
	}
}