package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {

    public User inputMapping(UserDtoInput userDtoInput) {
        return User.builder().username(userDtoInput.getUsername())
                .password(userDtoInput.getPassword())
                .email(userDtoInput.getEmail())
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .username(user.getUsername())
                .build();
    }

    public UserDtoOutput loginOutputMapping(UserDetails userDetails, String token) {
        return UserDtoOutput.builder()
                .username(userDetails.getUsername())
                .token(token)
                .build();
    }
}
