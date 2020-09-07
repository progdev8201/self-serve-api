package com.controller;

import com.model.dto.ContactForm;
import com.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
public class SupportController {

    @Autowired
    SupportService supportService;

    @PostMapping("/{email}")
    public void sendEmail(@RequestBody ContactForm contactForm, @PathVariable String email){
        supportService.SendEmail(contactForm,email);
    }
}
