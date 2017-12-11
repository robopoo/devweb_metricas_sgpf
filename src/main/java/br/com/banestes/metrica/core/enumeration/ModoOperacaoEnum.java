package br.com.sgpf.metrica.core.enumeration;

public enum ModoOperacaoEnum {

	NORMAL("Normal"),
	LOV("Lov");
	
	private String rotulo;
	
	private ModoOperacaoEnum(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getRotulo() {
		return rotulo;
	}
}
