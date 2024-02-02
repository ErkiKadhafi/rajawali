package com.binarfinalproject.rajawali.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    @Value("${spring.mail.sender.mail}")
    private String senderEmail;

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    public boolean createEmail(String email, String subject, String message) {
        MimeMessage mime = mailSender.createMimeMessage();

        boolean success = false;
        try {
            log.info("Sending email to: " + email);
            log.info("Sending email from: " + senderEmail);
            log.info("Sending email with subject: " + subject);

            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom(senderEmail, senderName);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mime);
            success = true;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
        }

        return success;
    }

    public void sendEmail(final String to, final String subject, final String message) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                createEmail(to, subject, message);
            }
        });
    }

}
