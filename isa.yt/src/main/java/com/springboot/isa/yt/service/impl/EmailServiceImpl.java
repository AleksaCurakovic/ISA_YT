package com.springboot.isa.yt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	

	@Override
	@Async
	public void sendVerificationEmail(String recipient, String username, String token) {
		try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
	        
	        String link = "http://localhost:8080/verify?token=" + token;
	        
	        String htmlContent = String.format(
	        	    "<h2>Welcome to Jutjubić, %s!</h2>" +
	        	    	    "<p>To complete your registration, please follow the link below:</p>" +
	        	    	    "<p><a href='%s'>Verify My Account</a></p>" +
	        	    	    "<br><p>Thank you!</p>", 
	        	    	    username, link
	        	    	);
	        
	        helper.setText(htmlContent, true); 
	        helper.setTo(recipient);
	        helper.setSubject("Verify your account");
	        helper.setFrom("onlybuns.noreply60@gmail.com");

	        mailSender.send(mimeMessage);
	    } catch (MessagingException e) {
	        throw new IllegalStateException("Failed to send email");
	    }
	}

}