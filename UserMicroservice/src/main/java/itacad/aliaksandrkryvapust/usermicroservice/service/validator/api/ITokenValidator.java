package itacad.aliaksandrkryvapust.usermicroservice.service.validator.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;

public interface ITokenValidator {
    void validate(String token, EmailToken emailToken, int expirationTime);
    void validateEmailToken(EmailToken emailToken);
}
