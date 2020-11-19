package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Value("${spring.mail.username}")
    private String fabMoeEmail;


    public boolean sendSimpleMessage(String ownerEmail, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        text = "<h3>" + text + "</h3><p>From: " + ownerEmail + "<p>";

        try {
            helper.setText(text,true);
            helper.setFrom(ownerEmail);
            helper.setTo(fabMoeEmail);
            helper.setSubject(subject);
            javaMailSender.send(mimeMessage);
            LOGGER.info("EMAIL WAS SENT SUCCESSFULLY!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
