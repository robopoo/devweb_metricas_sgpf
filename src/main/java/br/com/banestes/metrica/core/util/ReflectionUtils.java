package br.com.sgpf.metrica.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Classe utilitária de serviços baseados em Java Reflection API
 */
public class ReflectionUtils {

	public static Method getMethod(Class<?> classe, String nomeCampo) {
		String getter = "get" + nomeCampo.replaceFirst(nomeCampo.substring(0, 1), nomeCampo.substring(0, 1).toUpperCase());
		try {
			Method m = classe.getMethod(getter);
			return m;
		} catch (SecurityException e) {
			throw new RuntimeException("Falha ao recuperar metodo [" + getter + "] da classe [" + classe.getName() + "]", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Falha ao recuperar metodo [" + getter + "] da classe [" + classe.getName() + "]", e);
		}
	}

	/**
	 * Metodo auxiliar para recuperacao do valor de um atributo de um determinado objeto.
	 * 
	 * @param classe
	 *            - A classe do objeto.
	 * @param objeto
	 *            - A instancia que contem o valor.
	 * @param nomeCampo
	 *            - O nome do atributo.
	 * @return O valor do atributo ou null.
	 */
	public static Object recuperarValorAtributo(Class<?> classe, Object objeto, String nomeCampo) {
		Method m = getMethod(classe, nomeCampo);
		try {
			return m.invoke(objeto);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Falha ao recuperar valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Falha ao recuperar valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Falha ao recuperar valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		}
	}

	/**
	 * Metodo auxiliar para preencher do valor de um atributo de um determinado objeto.
	 * 
	 * @param classe
	 *            - A classe do objeto.
	 * @param objeto
	 *            - A instancia que contem o valor.
	 * @param nomeCampo
	 *            - O nome do atributo.
	 * @param valor
	 *            do atributo.
	 */
	public static void preencherValorAtributo(Class<?> classe, Object objeto, String nomeCampo, Object valor) {
		String setter = "set" + nomeCampo.replaceFirst(nomeCampo.substring(0, 1), nomeCampo.substring(0, 1).toUpperCase());
		Method m = getMethod(classe, nomeCampo);
		try {
			Method metodoSET = classe.getMethod(setter, m.getReturnType());
			metodoSET.invoke(objeto, valor);
		} catch (SecurityException e) {
			throw new RuntimeException("Falha ao preencher valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Falha ao preencher valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Falha ao preencher valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Falha ao preencher valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Falha ao preencher valor de atributo [" + m.getName() + "] de objeto [" + objeto + "]", e);
		}
	}

	/**
	 * Realiza o merge dos valores do objeto origem para o objeto destino. Os metodos que sao capturados pelo algoritmo sao metodos GET e que nao possuem parametro. Os valores sao capturados a partir dos metodos GET existentes na classe origem. Isso quer dizer que caso o destino seja uma subclasse de origem, os seus metodos nao serao reconhecidos. Somente valores nao nulos sao passados de origem para destino. Campos string vazios e numericos zerados sao permitidos.
	 * 
	 * @param <C>
	 * @param origem
	 *            : Objeto cujas propriedades serao transferidas para o destino
	 * @param destino
	 *            : Recebera os valores de origem
	 */
	public static <C> void mergeProperties(C origem, C destino) {
		Class<?> classe = origem.getClass();
		while (!classe.equals(Object.class)) {
			for (Field f : classe.getDeclaredFields()) {
				if (f.isAnnotationPresent(Transient.class)) {
					continue;
				}
				if (f.isAnnotationPresent(Id.class) || (f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Version.class))
						|| f.isAnnotationPresent(JoinColumn.class)) {
					Object valor = recuperarValorAtributo(origem.getClass(), origem, f.getName());
					preencherValorAtributo(destino.getClass(), destino, f.getName(), valor);
				}
			}
			classe = classe.getSuperclass();
		}
	}
}
