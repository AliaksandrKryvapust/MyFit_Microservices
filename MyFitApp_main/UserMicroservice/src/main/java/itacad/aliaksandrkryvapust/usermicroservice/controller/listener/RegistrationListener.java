package itacad.aliaksandrkryvapust.usermicroservice.controller.listener;

import itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions.EmailSendException;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.MAIL_SUBJECT;

@Component
public class RegistrationListener implements ApplicationListener<EmailVerificationEvent> {
    private final ITokenManager tokenManager;
    private final JavaMailSenderImpl javaMailSender;

    public RegistrationListener(ITokenManager tokenManager, JavaMailSenderImpl javaMailSender) {
        this.tokenManager = tokenManager;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(@NonNull EmailVerificationEvent event) {
        try {
            User user = event.getUser();
            String token = UUID.randomUUID().toString();
            tokenManager.saveToken(user, token);
            SimpleMailMessage email = createEmail(user, token);
            javaMailSender.send(email);
        } catch (Exception e) {
            throw new EmailSendException(e.getMessage(), e);
        }

    }

    private SimpleMailMessage createEmail(User user, String token) {
        String addressToSend = user.getEmail();
        String url = "/api/v1/users/registration/confirm?token=" + token;
        String message = "To continue registration use link below:";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(addressToSend);
        email.setSubject(MAIL_SUBJECT);
        email.setText(message + "\r\n" + "http://localhost:8080" + url);
        return email;
    }
}
