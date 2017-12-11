package br.com.sgpf.metrica.core.enumeration.types;

import org.hibernate.type.CharBooleanType;

public class SimNaoType extends CharBooleanType {

	private static final long serialVersionUID = -5259931036208009255L;

	@Override
	protected String getFalseString() {
		return "N";
	}

	@Override
	protected String getTrueString() {
		return "S";
	}

	public String getName() {
		return "sim_nao";
	}
}
