package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Role;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {
    private final PasswordEncoder encoder;

    @Autowired
    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User userInputMapping(UserDtoInput userDtoInput) {
        return User.builder().username(userDtoInput.getNick())
                .password(encoder.encode(userDtoInput.getPassword()))
                .email(userDtoInput.getMail())
                .role(Role.USER)
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .mail(user.getEmail())
                .build();
    }

    public UserDtoOutput loginOutputMapping(UserDetails userDetails, String token) {
        return UserDtoOutput.builder()
                .mail(userDetails.getUsername())
                .token(token)
                .build();
    }
}
