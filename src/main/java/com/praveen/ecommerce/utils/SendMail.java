package com.praveen.ecommerce.utils;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.praveen.ecommerce.models.Order;

@Component
public class SendMail {

	public boolean sendEmail(String to, String from, String subject, String body) {

		final String username = "psoni10001@gmail.com";
		final String password = "***";
		boolean sent = false;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from==null?username:from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(body, "text/html; charset=utf-8");

			Transport.send(message);

			sent = true;

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return sent;
	}
	public boolean sendEmail(String to, String subject, String body) {
		return sendEmail(to,null, subject, body);
	}
	
	@Async
	public CompletableFuture<Boolean> orderConfirmation(String email,Order order){
		boolean sent = false;
		String subject = "Order "+order.getDispOrderId()+" confirmed";
		String body = htmlStore.getOrderConfirmationHtml(order);
		sent = sendEmail(email, subject, body);
		return CompletableFuture.completedFuture(sent);
	}
}