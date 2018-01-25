package com.noworking;


import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;  
  
public class ActivationMail5  
{  
 public static void sendMail(String name,String domainName,String toMail) {
	
	   String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	   // Get a Properties object
	      Properties props = System.getProperties();
	      props.setProperty("mail.smtp.host", "smtp.gmail.com");
	      props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	      props.setProperty("mail.smtp.socketFactory.fallback", "false");
	      props.setProperty("mail.smtp.port", "465");
	      props.setProperty("mail.smtp.socketFactory.port", "465");
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.debug", "true");
	      props.put("mail.store.protocol", "pop3");
	      props.put("mail.transport.protocol", "smtp");
	//      String fromMailusername = fromMail;//
	//      String fromMailPassword =password;
	      try{
	      Session session = Session.getDefaultInstance(props, 
	                           new Authenticator(){
	                              protected PasswordAuthentication getPasswordAuthentication() {
	                                 return new PasswordAuthentication("techfloppy5@gmail.com", "techfloppy@5");
	                              }});

	    // -- Create a new message --
	      Message msg = new MimeMessage(session);

	   // -- Set the FROM and TO fields --
	      msg.setFrom(new InternetAddress("techfloppy5@gmail.com"));
	      msg.setRecipients(Message.RecipientType.TO, 
	                       InternetAddress.parse(toMail,false));
	      
	      String content ="Hi "+name+", \r\n" + 
	      		"\r\n" + 
	      		"\r\n" + 
	      		"For Reasonable cost  Includes all below services:-\r\n" + 
	      		"\r\n" + 
	      		"1. Hosting to maintain your website at low cost ($50)\r\n" + 
	      		"2. Website designing with mobile responsive, 2 forms integration's with payment gateway.\n"+
	      		"3. Enhencement of your existing website (php, java, .net and wordpress )\r\n" + 
	      		"4. Logo Design (3 logo samples will be provided)\r\n" + 
	      		"5. Business Card Design(2 design samples will be provided)\r\n" + 
	      		"6. Single side Flyer Design (2 design samples will be provided)\r\n\n\n\n"	+
	      		"Thanks & Regards,\r\n" + 
	      		"Gopinadh Randhi\r\n" + 
	      		"Techfloppy IT Solutions\r\n" + 
	      		"call :: 0-955 300 3180\r\n" + 
	      		"mail:: techfloppy@gmail.com\r\n" + 
	      		"skype:: gopinadh.r";
	      
	      msg.setSubject("Regarding to "+domainName);
	      msg.setText(content);
	      msg.setSentDate(new Date());
	      System.out.println("**********************************************************"+"techfloppy5@gmail.com");
	      Transport.send(msg);
	      System.out.println("Message sent.");
	   }catch (MessagingException e){ System.out.println("Erreur d'envoi, cause: " + e);}
 }	 
	 
	 
	 
	 
	 
	 
	 
 
 }  
