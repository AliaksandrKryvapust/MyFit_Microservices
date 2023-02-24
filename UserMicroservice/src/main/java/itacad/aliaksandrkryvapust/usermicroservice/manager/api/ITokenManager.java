package itacad.aliaksandrkryvapust.usermicroservice.manager.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

public interface ITokenManager {
    void saveToken(User user,String token);
    void validateToken(String token);
    void resendToken(String token);
}
