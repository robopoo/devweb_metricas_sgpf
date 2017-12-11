package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoElementoContagemEnum;

public class TipoElementoContagemEnumUserType extends
	GenericEnumUserType<String, TipoElementoContagemEnum> {

    private static final long serialVersionUID = 6885660554913048339L;

    public TipoElementoContagemEnumUserType() throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	super(TipoElementoContagemEnum.class,
		TipoElementoContagemEnum.values(), "getValue", Types.VARCHAR);
    }
}