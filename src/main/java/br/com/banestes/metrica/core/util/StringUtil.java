package br.com.sgpf.metrica.core.util;

import java.text.Normalizer;

public abstract class StringUtil {

	private StringUtil() {

	}

	public static final boolean isEmpty(String valor) {
		if (valor == null || (valor.length()) == 0) {
			return true;
		}
		return "".equals(valor.trim());
	}

	/**
	 * Retorna verdadeiro caso o valor passado como parametro seja apenas
	 * numeros, caso contrario retorna falso
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isOnlyNumber(final String str) {
		if (isEmpty(str))
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (!str.substring(i, i + 1).matches("[0-9]")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Retorna uma string apenas com caracteres numericos
	 * 
	 * @param value
	 * @return String
	 */
	public static String getOnlyNumber(String value) {
		if (isEmpty(value)) {
			return null;
		}
		String newValue = "";
		for (int i = 0; i < value.length(); i++) {
			if (isOnlyNumber(value.substring(i, i + 1))) {
				newValue += value.substring(i, i + 1);
			}
		}
		return newValue;

	}

	public static String limparNome(String nome) {
		if (nome != null) {
			nome = nome.trim();
			nome = nome.replaceAll("_", " ");
			nome = nome.replaceAll("-", " ");
			nome = Normalizer.normalize(nome, Normalizer.Form.NFD);
			nome = nome.replaceAll("[^\\p{ASCII}]", "");
			nome = nome.toUpperCase();
		}
		return nome;
	}

}
