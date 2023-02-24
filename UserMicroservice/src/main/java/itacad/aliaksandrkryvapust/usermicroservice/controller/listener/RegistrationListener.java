package itacad.aliaksandrkryvapust.usermicroservice.controller.listener;

import itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions.EmailSendException;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.*;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<EmailVerificationEvent> {
    private final ITokenService tokenService;
    private final JavaMailSenderImpl javaMailSender;

    @Override
    public void onApplicationEvent(@NonNull EmailVerificationEvent event) {
        try {
            User user = event.getUser();
            String token = UUID.randomUUID().toString();
            tokenService.saveToken(user, token);
            SimpleMailMessage email = createEmail(user, token);
            javaMailSender.send(email);
        } catch (Exception e) {
            throw new EmailSendException(e.getMessage(), e);
        }

    }

    private SimpleMailMessage createEmail(User user, String token) {
        String addressToSend = user.getEmail();
        String url = TOKEN_URI + token;
        String message = "To continue registration use link below:";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(addressToSend);
        email.setSubject(MAIL_SUBJECT);
        email.setText(message + "\r\n" + IP_URI +PORT_URI + url);
        return email;
    }
}
