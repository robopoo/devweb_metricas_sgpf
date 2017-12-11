package br.com.sgpf.metrica.enumeration;

public enum TipoFuncaoTransacaoEnum {

	EE(0, "EE"), SE(1, "SE"), CE(2,"CE");

	private Integer value;
	private String descricao;

	private TipoFuncaoTransacaoEnum(Integer value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
