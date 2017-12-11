package br.com.sgpf.metrica.core.exception;


public class FormatterException extends Exception {

	private static final long serialVersionUID = -615356748337749224L;

	private String valor;
	private String tipo;

	
	public FormatterException(String valor, String tipo) {
		this.valor = valor;
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}