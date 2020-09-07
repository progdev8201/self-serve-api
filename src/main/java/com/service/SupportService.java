package com.service;

import com.model.dto.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportService {

    @Autowired
    private EmailService emailService;

    public boolean SendEmail(ContactForm contactForm, String email){
        emailService.sendSimpleMessage(email,contactForm.getContactType().toString(),contactForm.getComment());
        return true;
    }
}
