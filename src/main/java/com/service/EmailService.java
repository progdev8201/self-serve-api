package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fabMoeEmail;


    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(to);
        message.setTo(fabMoeEmail);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        System.out.println("EMAIL WAS SENT SUCCESSFULLY!");
    }
}
