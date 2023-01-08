package itacad.aliaksandrkryvapust.myfitapp.service.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;

public interface ITokenService {
    void save(EmailToken token);

    EmailToken getToken(String token);
}
