package com.mail;


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
  
public class ActivationMail
{  
 public static void sendMail(String name,String domainName,String toMail,String fromMail,String password) {
	
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
	      try{
	      Session session = Session.getDefaultInstance(props, 
	                           new Authenticator(){
	                              protected PasswordAuthentication getPasswordAuthentication() {
	                                 return new PasswordAuthentication(fromMail, password);
	                              }});

	    // -- Create a new message --
	      Message msg = new MimeMessage(session);

	   // -- Set the FROM and TO fields --
	      msg.setFrom(new InternetAddress(fromMail));
	      msg.setRecipients(Message.RecipientType.TO, 
	                       InternetAddress.parse(toMail,false));
	      
	      String content ="Hi "+name+", \r\n" + 
	      		"\r\n" + 
	      		"\r\n" + 
	      		"Since 2013 we have been providing customized Website and software.\r\n" + 
	      		" Now, we primarily focus on serving our client's needs through amazing customized solutions for their businesses.\r\n" + 
	      		"\r\n" + 
	      		" WHAT WE DO:\r\n\n" + 
	      		"-Website- Design/Development (Basic website for 3000 rupees(5 to 8 pages), other websites based on your requirment \r\n" + 
	      		"-Website hosting+Domain for 3000 rupees  only for one year \r\n" + 
	      		"-Application - Development  \r\n" + 
	      		"-Word Press , Java\r\n" + 
	      		"-Logo Designing\r\n" + 
	      		"-WooCommerce\r\n" + 
	      		"-Shopping cart, Payment integration\r\n" + 
	      		"-iPhone Apps\r\n" + 
	      		"-Android Apps\r\n" + 
	      		"-Custom Programming\r\n" + 
	      		"-Cross platform Application\r\n" +  
	      		"\r\n" + 
	      		"\r\n" + 
	      		"We look forward to reply and how we can help to improve your business!\r\n" + 
	      		"\r\n\n\n" + 
	      		"Thanks & Regards,\r\n" + 
	      		"Techfloppy IT Solutions\r\n" + 
	      		"Prakash.M\r\n" + 
	      		"call :: 0-929 321 9394\r\n";
	      //+ 
	  //    		"mail:: techfloppy@ gmail.com\r\n" + 
	    // 		"skype:: gopinadh.r";
	      
	      
	      msg.setSubject("Regarding to your new Domain "+domainName);
	      msg.setText(content);
	      msg.setSentDate(new Date());
	      System.out.println("Hi "+name+" mail sent to "+toMail+" from "+ fromMail);
	      Transport.send(msg);
	      System.out.println("Message sent.");
	   }catch (MessagingException e){ System.out.println("Erreur d'envoi, cause: " + e);}
 }	 
	 
	 
	 
	 
	 
	 
	 
 
 }  
