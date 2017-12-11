package br.com.sgpf.metrica.mail;

import br.com.sgpf.metrica.entity.ProjetoTO;

public class MailInspecaoQA implements Constants {

	private static final long serialVersionUID = -8365993614492280443L;

	private final IMailImplements mailTO;

	public MailInspecaoQA(ProjetoTO projetoTO) {

		StringBuilder body = new StringBuilder();
		body.append("Serviço de Alerta - PD Métricas");
		body.append(_QUEBRAR_LINHA);
		body.append(_QUEBRAR_LINHA);

		body.append("Projeto: " + projetoTO.getProjeto() + " / " + projetoTO.getSubProjeto());
		body.append(_QUEBRAR_LINHA);

		body.append("Sistema: " + projetoTO.getSistema());
		body.append(_QUEBRAR_LINHA);

		body.append("Tarefa: Garantir Qualidade");
		body.append(_QUEBRAR_LINHA);

		body.append("Artefatos: DVS e FPA");
		body.append(_QUEBRAR_LINHA);
		body.append(_QUEBRAR_LINHA);

		body.append("Responsável Execução: ");
		body.append(projetoTO.getResponsavelPDCaseTO().getNomeAnalista());
		body.append(_QUEBRAR_LINHA);
		body.append(_QUEBRAR_LINHA);

		body.append("Responsável Aprovação: QA");

		String subject = projetoTO.getProjeto() + " / " + projetoTO.getSubProjeto() + " - DVS e FPA - Inspeção de QA";

		this.mailTO = new IMailImplements(subject, body.toString());
	}

	public IMailImplements getMailTO() {
		return mailTO;
	}

}