package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.web.context.request.WebRequest;

public interface ITokenManager {
    void saveToken(User user,String token);
    void validateToken(String token);
    void resendToken(String token, WebRequest request);
}
