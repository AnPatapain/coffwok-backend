package com.anpatapain.coffwok.email.service;

import com.anpatapain.coffwok.email.model.Email;

public interface EmailService {
    // To send a simple email
    String sendSimpleMail(Email details);

    // To send an email with attachment
    String sendMailWithAttachment(Email details);
}
