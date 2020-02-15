package com.springboot.restboilerplate.demo.utils;

import com.springboot.restboilerplate.demo.configs.constants.ApplicationContstants;
import com.springboot.restboilerplate.demo.configs.constants.MessagesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Objects;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private SimpleMailMessage mailTemplate(String mailType, Locale locale) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setSubject(ApplicationContstants.MAIL_TITLE +
                messageSource.getMessage(MessagesConstants.MAIL + "subject", null, locale));
        simpleMailMessage.setText(
                messageSource.getMessage(MessagesConstants.MAIL + "mail." + mailType, null, locale) +
                        messageSource.getMessage(MessagesConstants.MAIL + "message." + mailType, null, locale)
        );

        return simpleMailMessage;
    }

    private MimeMessage mailSender(SimpleMailMessage mailTemplate, String mailAddress, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setTo(mailAddress);
            mimeMessage.setSubject(mailTemplate.getSubject());

        } catch (MessagingException ex) {
            logger.error(ex.getMessage());
        }


        return mimeMessage;
    }
    public void sendPasswordResetMail(String mailAddress, String passwordResetToken, Locale locale) {
        SimpleMailMessage mailTemplate = mailTemplate("password.reset", locale);
        MimeMessage mimeMessage = mailSender(
                mailTemplate,
                mailAddress,
                String.format(Objects.requireNonNull(mailTemplate.getText()),
                        ApplicationContstants.FRONTEND_ADDRESS+"forgot-password?token="+passwordResetToken));
        javaMailSender.send(mimeMessage);
    }

    public void sendActivationMail(String mailAddress, String activationToken, Locale locale) {
        SimpleMailMessage mailTemplate = mailTemplate("activation", locale);
        MimeMessage mimeMessage = mailSender(
                mailTemplate,
                mailAddress,
                String.format(Objects.requireNonNull(mailTemplate.getText()),
                        ApplicationContstants.FRONTEND_ADDRESS+"activation?token="+activationToken));
        javaMailSender.send(mimeMessage);
    }
}
