package br.com.sgpf.metrica.mail;

public class IMailImplements implements IMail {

	private String destino;

	private String subject;

	private String body;

	public IMailImplements(String subject, String body) {
		this(null, subject, body);
	}

	public IMailImplements(String destino, String subject, String body) {
		super();
		this.destino = destino;
		this.subject = subject;
		this.body = body;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

}