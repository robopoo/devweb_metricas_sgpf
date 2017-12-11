package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoComplexidadeEnum;

public class TipoComplexidadeEnumUserType extends GenericEnumUserType<String, TipoComplexidadeEnum> {

	private static final long serialVersionUID = 1;

	public TipoComplexidadeEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(TipoComplexidadeEnum.class, TipoComplexidadeEnum.values(), "getValue", Types.CHAR);
	}
	
}
