package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenMapper {

    public EmailToken inputMapping(User user, String token){
        return EmailToken.builder()
                .token(token)
                .user(user).build();
    }
}
