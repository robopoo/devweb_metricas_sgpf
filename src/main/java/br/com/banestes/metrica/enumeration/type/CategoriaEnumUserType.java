package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.CategoriaEnum;

public class CategoriaEnumUserType extends GenericEnumUserType<Integer, CategoriaEnum> {

	private static final long serialVersionUID = -4740075906657019834L;

	public CategoriaEnumUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		super(CategoriaEnum.class, CategoriaEnum.values(), "getValue", Types.NUMERIC);
	}
}