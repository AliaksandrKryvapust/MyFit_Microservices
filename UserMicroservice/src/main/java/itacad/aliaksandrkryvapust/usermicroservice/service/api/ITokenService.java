package itacad.aliaksandrkryvapust.usermicroservice.service.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;

public interface ITokenService {
    void saveToken(User user, String token);
    EmailToken getToken(String token);
    void activateUser(String token);
    void resendToken(String token);
}
