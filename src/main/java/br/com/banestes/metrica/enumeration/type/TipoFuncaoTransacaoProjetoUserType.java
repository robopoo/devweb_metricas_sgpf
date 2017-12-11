/**
 * 
 */
package br.com.sgpf.metrica.enumeration.type;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import br.com.sgpf.metrica.core.enumeration.types.GenericEnumUserType;
import br.com.sgpf.metrica.enumeration.TipoFuncaoTransacaoEnum;

/**
 * PD Case Informática Ltda. www.pdcase.com.br
 * @author Fernando Padrão Silveira <mailto:fernando.silveira@pdcase.com.br>
 * @version 1.0.0
 * @since 09/06/2014
 * @time 18:28:14
 */

public class TipoFuncaoTransacaoProjetoUserType extends GenericEnumUserType<Integer, TipoFuncaoTransacaoEnum> {

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
	public TipoFuncaoTransacaoProjetoUserType() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		super(TipoFuncaoTransacaoEnum.class, TipoFuncaoTransacaoEnum.values(), "getValue", Types.INTEGER);
	}
}
