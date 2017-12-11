package br.com.sgpf.metrica.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.seam.annotations.Name;

@Name(value = "mailUtil")
public class MailUtil {

	private static final Authenticator _AUTHENTICATOR;

	private static final String _MAIL_REMETENTE = "admin_pd_metricas@pdcase.com.br";

	private static final Properties _PROPS;

	static {
		_AUTHENTICATOR = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(_MAIL_REMETENTE, "PD7840pd");
			}
		};

		_PROPS = new Properties();
		_PROPS.put("mail.smtp.host", "smtp.pdcase.com.br");
		_PROPS.put("mail.smtp.auth", "true");
		_PROPS.put("mail.smtp.port", "587");
		_PROPS.put("mail.mime.charset", "ISO-8859-1");
	}

	public void send(IMail mailTO) throws Exception {
		try {

			Session mailSession = Session.getDefaultInstance(_PROPS, _AUTHENTICATOR);

			Message msg = new MimeMessage(mailSession);

			msg.setFrom(new InternetAddress(_MAIL_REMETENTE));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTO.getDestino()));
			msg.setSentDate(new Date());
			msg.setSubject(mailTO.getSubject());

			msg.setText(mailTO.getBody());

			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
