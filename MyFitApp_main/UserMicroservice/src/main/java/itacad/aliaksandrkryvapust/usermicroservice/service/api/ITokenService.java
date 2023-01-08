package itacad.aliaksandrkryvapust.usermicroservice.service.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;

public interface ITokenService {
    void save(EmailToken token);

    EmailToken getToken(String token);
}
