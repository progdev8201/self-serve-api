package com.controller;

import com.model.dto.ContactForm;
import com.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/{email}")
    public void sendEmail(@RequestBody ContactForm contactForm, @PathVariable String email){
        supportService.SendEmail(contactForm,email);
    }
}
