package com.graduate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final static String SUBJECT = "Password recovery";
    private final static String MESSAGE = "You link to recovery password: ";
    private final static String PATH = "/login/change-password/";

    private JavaMailSender mailSender;

    public MailService(@Autowired JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String receiver, String hash) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(SUBJECT);
        msg.setText(MESSAGE + PATH + hash);
        mailSender.send(msg);
    }
}
