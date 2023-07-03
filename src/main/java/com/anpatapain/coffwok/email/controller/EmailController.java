package com.anpatapain.coffwok.email.controller;

import com.anpatapain.coffwok.email.model.Email;
import com.anpatapain.coffwok.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    // Sending a simple Email
    @PostMapping("/api/sendMail")
    @PreAuthorize("hasRole('USER')")
    public String sendMail(@RequestBody Email details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }
}
