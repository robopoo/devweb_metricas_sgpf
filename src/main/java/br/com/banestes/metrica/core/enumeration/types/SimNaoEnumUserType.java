package br.com.sgpf.metrica.core.enumeration.types;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.SimNaoEnum;

public class SimNaoEnumUserType extends GenericEnumUserType<String, SimNaoEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public SimNaoEnumUserType() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		super(SimNaoEnum.class, SimNaoEnum.values(), "getValue", Types.CHAR);
	}
}