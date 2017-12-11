package br.com.sgpf.metrica.word.dvs;

public class ValueStyle {

	public enum STYLE {
		NORMAL, BOLD, RED
	};

	String value;

	STYLE style;

	public ValueStyle(String value) {
		this(value, STYLE.NORMAL);
	}

	public ValueStyle(String value, STYLE style) {
		super();
		this.value = value;
		this.style = style;
	}

}