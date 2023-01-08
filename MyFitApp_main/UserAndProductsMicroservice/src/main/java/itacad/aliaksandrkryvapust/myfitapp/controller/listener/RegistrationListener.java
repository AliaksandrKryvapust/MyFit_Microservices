package itacad.aliaksandrkryvapust.myfitapp.controller.listener;

import itacad.aliaksandrkryvapust.myfitapp.controller.exceptions.EmailSendException;
import itacad.aliaksandrkryvapust.myfitapp.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static itacad.aliaksandrkryvapust.myfitapp.core.Constants.MAIL_SUBJECT;

@Component
public class RegistrationListener implements ApplicationListener<EmailVerificationEvent> {
    private final ITokenManager tokenManager;
    private final MessageSource messageSource;
    private final JavaMailSender javaMailSender;

    public RegistrationListener(ITokenManager tokenManager, MessageSource messageSource, JavaMailSender javaMailSender) {
        this.tokenManager = tokenManager;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(@NonNull EmailVerificationEvent event) {
        try {
            User user = event.getUser();
            String token = UUID.randomUUID().toString();
            tokenManager.saveToken(user, token);
            SimpleMailMessage email = createEmail(event, user, token);
            javaMailSender.send(email);
        } catch (Exception e) {
            throw new EmailSendException(e.getMessage(), e);
        }

    }

    private SimpleMailMessage createEmail(EmailVerificationEvent event, User user, String token) {
        String addressToSend = user.getEmail();
        String url = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messageSource.getMessage("message.regSucc", null, event.getLocale());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(addressToSend);
        email.setSubject(MAIL_SUBJECT);
        email.setText(message + "\r\n" + "http://localhost:8080" + url);
        return email;
    }
}
