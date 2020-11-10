package com.kdax.bizportal.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceUtil {

    @Value("${email.sender.name}")
    String senderName;
    @Value("${email.sender.address}")
    String senderAddress;

    private JavaMailSender javaMailSender;

    public EmailServiceUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        MimeMessagePreparator message = mimeMessage -> {
            String content = text;
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(String.format("%s <%s>",senderName,senderAddress));
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        javaMailSender.send(message);

    }

}