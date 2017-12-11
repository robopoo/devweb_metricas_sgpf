/**
 * 
 */
package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoFuncaoDadosProjetoEnum;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * @author Glauber Monteiro <mailto:glauber.monteiro@pdcase.com.br>
 * @version 1.0.0
 * @since 20/03/2014
 * @time 15:28:14
 */

public class TipoFuncaoDadosProjetoUserType extends GenericEnumUserType<Integer, TipoFuncaoDadosProjetoEnum> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2821973577553696764L;

	/**
	 * @param clazz
	 * @param enumValues
	 * @param method
	 * @param sqlType
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public TipoFuncaoDadosProjetoUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		super(TipoFuncaoDadosProjetoEnum.class, TipoFuncaoDadosProjetoEnum.values(), "getValue", Types.INTEGER);
	}
}
