package com.binarfinalproject.rajawali.util;

import com.binarfinalproject.rajawali.config.MailConfiguration;
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

    @Autowired
    private MailConfiguration mailConfiguration;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    @Value("${spring.mail.sender.mail}")
    private String senderEmail;

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;



    public void  createEmail(String email, String subject, String message) {

        try {
//            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//            Properties props = mailSender.getJavaMailProperties();
//            mailSender.setUsername("nkhairiyah016@gmail.com");
//            mailSender.setPassword("mukaqzpuwfvauxul");
//            props.put("mail.transport.protocol", "smtp");
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            mailSender.setHost("smtp.gmail.com");
//            mailSender.setPort(587);
            MimeMessage mime = mailSender.createMimeMessage();
            log.info("Sending email to: " + email);
            log.info("Sending email from: " + senderEmail);
            log.info("Sending email with subject: " + subject);

            MimeMessageHelper helper = new MimeMessageHelper(mime, true,  "UTF-8");
            helper.setFrom(senderEmail, senderName);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mime);
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
        }

    }

}



