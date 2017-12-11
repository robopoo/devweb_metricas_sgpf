package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoEntidadeEnum;

public class TipoEntidadeEnumUserType extends GenericEnumUserType<Integer, TipoEntidadeEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public TipoEntidadeEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(TipoEntidadeEnum.class, TipoEntidadeEnum.values(), "getValue", Types.NUMERIC);
	}
}