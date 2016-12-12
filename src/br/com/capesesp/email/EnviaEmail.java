package br.com.capesesp.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.logging.Logger;
import org.quartz.JobExecutionContext;



public class EnviaEmail {
	
	public static final Logger logger = Logger.getLogger("br.com.capesesp.email");
	
	private static final String EMAIL_FUNCIONAL = "email.relatorio.funcional";
	@Resource(lookup = "java:jboss/mail/Default")
	Session session;
	
	public EnviaEmail(){
	}
	
	public void enviaEmail(JobExecutionContext context, String nomeJob, String nomeTrigger){
		
		String emailFrom = "naoresponder@capesesp.com.br";
		String mensagemTexto = "Segue em anexo relatório de envio dos dados cadastrais do associado para a Funcional";

		Message mensagem = new MimeMessage(session);
	
		try {
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String filename = "log/funcional.log";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
			
			mensagem.setSubject("Relatório de Envio do Cadastro - Funcional");
			mensagem.setText(mensagemTexto);
			mensagem.addFrom(InternetAddress.parse(emailFrom));
			mensagem.setContent(multipart);
			
			Transport.send(mensagem,  InternetAddress.parse(System.getProperty(EMAIL_FUNCIONAL)));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

}
