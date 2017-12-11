package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.StatusAvaliacaoEnum;

public class StatusAvaliacaoEnumUserType extends GenericEnumUserType<String, StatusAvaliacaoEnum> {

	private static final long serialVersionUID = 1;

	public StatusAvaliacaoEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(StatusAvaliacaoEnum.class, StatusAvaliacaoEnum.values(), "getValue", Types.CHAR);
	}
	
}
