package br.com.sgpf.metrica.core.util;

public abstract class DocumentoUtil {

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private DocumentoUtil() {

	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static final boolean isValidCPF(String cpf) {
		if ((cpf == null) || (cpf.length() != 11))
			return false;

		boolean resposta = true;
		String c = cpf.substring(0, 1);
		for (int i = 1; i < cpf.length(); i++) {
			resposta = (resposta && c.equals(cpf.substring(i, i + 1)));
			c = cpf.substring(i, i + 1);
		}
		if (resposta)
			return false;

		Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static final boolean isValidCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14))
			return false;

		Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
	}

}
