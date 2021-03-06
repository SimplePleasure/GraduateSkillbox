package com.graduate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final static String RECOVERY_PASS_SUBJECT = "Password recovery";
    private final static String RECOVERY_PASS_MESSAGE = "You link to recovery password: ";
    private final static String PATH = "/login/change-password/";

    private final static String HI_SUBJECT = "Welcome to dev pub";
    private final static String HI_MESSAGE = "Welcome to the developers public. Register successfully complete.";

    private JavaMailSender mailSender;

    public MailService(@Autowired JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPassRecoveryMessage(String receiver, String hash) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(RECOVERY_PASS_SUBJECT);
        msg.setText(RECOVERY_PASS_MESSAGE + PATH + hash);
        mailSender.send(msg);
    }

    public void sayHiMessage(String receiver) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(HI_SUBJECT);
        msg.setText(HI_MESSAGE);
        mailSender.send(msg);
    }

}
