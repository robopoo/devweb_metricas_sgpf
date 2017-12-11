package br.com.sgpf.metrica.core.exception.vo;

import java.io.Serializable;

public class MensagemErro implements Serializable {

	private static final long serialVersionUID = -5140406871475359749L;

	private String key;

	private String[] params;

	public MensagemErro(String key) {
		this.key = key;
	}

	public MensagemErro(String key, String... params) {
		this.key = key;
		this.params = params;
	}

	public String getKey() {
		return key;
	}

	public String[] getParams() {
		return params;
	}

}
