package br.com.sgpf.metrica.core.exception;

import java.util.ArrayList;
import java.util.List;

import br.com.sgpf.metrica.core.exception.vo.MensagemErro;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 7693110056567458488L;

	private List<MensagemErro> mensagens = new ArrayList<MensagemErro>();

	public BusinessException(String mensagem) {
		super(mensagem);
	}

	public BusinessException(MensagemErro mensagemErro) {
		mensagens.add(mensagemErro);
	}

	public BusinessException(List<MensagemErro> mensagens) {
		super();
		this.mensagens = mensagens;
	}

	public List<MensagemErro> getMensagens() {
		return mensagens;
	}

}