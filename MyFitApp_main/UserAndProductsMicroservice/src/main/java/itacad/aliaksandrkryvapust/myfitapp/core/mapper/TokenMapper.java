package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;

public class TokenMapper {

    public EmailToken inputMapping(User user, String token){
        return EmailToken.builder()
                .token(token)
                .user(user).build();
    }
}
