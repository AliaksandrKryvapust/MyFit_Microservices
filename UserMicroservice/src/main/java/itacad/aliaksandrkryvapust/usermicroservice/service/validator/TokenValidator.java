package itacad.aliaksandrkryvapust.usermicroservice.service.validator;

import itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions.ExpiredEmailTokenException;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.ITokenValidator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.NoSuchElementException;

@Component
public class TokenValidator implements ITokenValidator {

    public void validate(String token, EmailToken emailToken, int expirationTime) {
        if (emailToken == null) {
            throw new NoSuchElementException("Token is not exist " + token);
        }
        if ((Instant.now().getEpochSecond() - emailToken.getDtUpdate().getEpochSecond()) >= expirationTime) {
            throw new ExpiredEmailTokenException("Token has been expired");
        }
    }

    @Override
    public void validateEmailToken(EmailToken emailToken) {
        if (emailToken == null) {
            throw new NoSuchElementException("Token is not exist");
        }
    }
}
