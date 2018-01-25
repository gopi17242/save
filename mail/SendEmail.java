package com.mail;
/**
 * @author Pinki.Roy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

 import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 /*
  * This is mail component .
  * The class is used to send mail to the users.
  * */
public class SendEmail {
	private String smtpserver = "";
	private String SMTP_AUTH_USER = "";
	private String SMTP_AUTH_PWD = "";

	public SendEmail(String smtpserver, String authUser, String authPassword) {
		this.smtpserver = smtpserver;
		this.SMTP_AUTH_USER = authUser;
		this.SMTP_AUTH_PWD = authPassword;

	}

	public void postMail(String recipients, String subject, String message, String from, String recCc, String recBcc)
			throws MessagingException {
		
		
		boolean debug = false;
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.auth", "true");
	//	props.put("mail.smtp.startssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port","25");
		
		//props.put("mail.smtp.name","Meetings at ACN infotech");
		

		// Get the authentication
		Authenticator auth = new SMTPAuthenticator();

		Session session = Session.getInstance(props, auth);
		debug = true;
		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		ArrayList aRcptList = new ArrayList();
		ArrayList aCcList = new ArrayList();
		ArrayList aBccList = new ArrayList();
		
		if (recipients != null && !recipients.equalsIgnoreCase("")) {
			StringTokenizer aToSt = new StringTokenizer(recipients, ",");
			while (aToSt.hasMoreElements()) {
				aRcptList.add(aToSt.nextElement().toString());
			}
		}
		if (recCc != null && !recCc.equalsIgnoreCase("")) {
			StringTokenizer aToSt = new StringTokenizer(recCc, ",");
			while (aToSt.hasMoreElements()) {
				aCcList.add(aToSt.nextElement().toString());
			}
		}
		if (recBcc != null && !recBcc.equalsIgnoreCase("")) {
			StringTokenizer aToSt = new StringTokenizer(recBcc, ",");
			while (aToSt.hasMoreElements()) {
				aBccList.add(aToSt.nextElement().toString());
			}
		}
		if (aRcptList.size() >0 ) {
			int aRcptSize = aRcptList.size();
			InternetAddress[] addressTo = new InternetAddress[aRcptSize];
			for (int i=0; i< aRcptSize; i++){
				addressTo[i] = new InternetAddress(aRcptList.get(i).toString());
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);
		}
		if (aCcList.size() >0 ) {
			int aRcptSize = aCcList.size();
			InternetAddress[] addressTo = new InternetAddress[aRcptSize];
			for (int i=0; i< aRcptSize; i++){
				addressTo[i] = new InternetAddress(aCcList.get(i).toString());
			}
			msg.setRecipients(Message.RecipientType.CC, addressTo);
		}
		if (aBccList.size() >0 ) {
			int aRcptSize = aBccList.size();
			InternetAddress[] addressTo = new InternetAddress[aRcptSize];
			for (int i=0; i< aRcptSize; i++){
				addressTo[i] = new InternetAddress(aRcptList.get(i).toString());
			}
			msg.setRecipients(Message.RecipientType.BCC, addressTo);
		}


		// Optional : You can also set your custom headers in the Email if you Want
		msg.addHeader("MyHeaderName", "myHeaderValue");

		// Setting the Subject and Content Type
		if (subject != null) {
			msg.setSubject(subject);
		}

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();
		// Fill the message
		messageBodyPart.setContent(message, "text/html");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		// Part two is attachment

		msg.setContent(multipart);
		msg.setSentDate(new Date());

		Transport.send(msg);
	}

	public void postMailAttach(String recipients, String subject, String message, String from, String recCc, String recBcc, String localfile)
			throws MessagingException {
		boolean debug = false;
		// Set the host smtp ddress
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpserver);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// Get the authentication
		Authenticator auth = new SMTPAuthenticator();

		Session session = Session.getInstance(props, auth);
		debug = true;
		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = { new InternetAddress(recipients) };
		InternetAddress[] addressCc = { new InternetAddress(recCc) };
		InternetAddress[] addressBcc = { new InternetAddress(recBcc) };
		System.out.println("***** SendMail.java: localfile: " + localfile);

		addressTo[0] = new InternetAddress(recipients);
		addressCc[0] = new InternetAddress(recCc);
		addressBcc[0] = new InternetAddress(recBcc);
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setRecipients(Message.RecipientType.CC, addressCc);
		msg.setRecipients(Message.RecipientType.BCC, addressBcc);

		// Optional : You can also set your custom headers in the Email if you Want
		msg.addHeader("MyHeaderName", "myHeaderValue");

		// Setting the Subject and Content Type
		if (subject != null) {
			msg.setSubject(subject);
		}

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();
		// Fill the message
		messageBodyPart.setContent(message, "text/html");
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		// Part two is attachment
		/*
		 * messageBodyPart = new MimeBodyPart(); DataSource source = new FileDataSource( localfile ); messageBodyPart.setDataHandler(new
		 * DataHandler(source)); messageBodyPart.setFileName( localfile ); multipart.addBodyPart(messageBodyPart);
		 */
		MimeBodyPart mbp = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(localfile);
		mbp.setDataHandler(new DataHandler(fds));
		mbp.setFileName(fds.getName());
		multipart.addBodyPart(mbp);

		msg.setContent(multipart);
		msg.setSentDate(new Date());

		Transport.send(msg);
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			System.out.println("-----------*********----------");

			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

}

