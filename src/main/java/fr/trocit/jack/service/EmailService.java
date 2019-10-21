package fr.trocit.jack.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
    private JavaMailSender javamailSender;
    
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
    	this.javamailSender = javaMailSender;
    }
 
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("a.bensaid.olivier@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        javamailSender.send(message);
    }
}