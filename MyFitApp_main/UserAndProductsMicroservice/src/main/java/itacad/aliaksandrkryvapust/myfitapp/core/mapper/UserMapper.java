package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {
    private final PasswordEncoder encoder;

    @Autowired
    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User userInputMapping(UserDtoRegistration userDtoRegistration) {
        return User.builder().username(userDtoRegistration.getNick())
                .password(encoder.encode(userDtoRegistration.getPassword()))
                .email(userDtoRegistration.getMail())
                .role(UserRole.USER)
                .status(UserStatus.WAITING_ACTIVATION)
                .build();
    }

    public User inputMapping(UserDtoInput userDtoInput) {
        return User.builder()
                .username(userDtoInput.getNick())
                .password(encoder.encode(userDtoInput.getPassword()))
                .email(userDtoInput.getMail())
                .role(UserRole.valueOf(userDtoInput.getRole()))
                .status(UserStatus.valueOf(userDtoInput.getStatus()))
                .build();
    }

    public UserLoginDtoOutput registerOutputMapping(User user) {
        return UserLoginDtoOutput.builder()
                .mail(user.getEmail())
                .build();
    }

    public UserLoginDtoOutput loginOutputMapping(UserDetails userDetails, String token) {
        return UserLoginDtoOutput.builder()
                .mail(userDetails.getUsername())
                .token(token)
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .uuid(user.getId())
                .dtCreate(user.getDtCreate())
                .dtUpdate(user.getDtUpdate())
                .mail(user.getEmail())
                .nick(user.getUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public PageDtoOutput<UserDtoOutput> outputPageMapping(Page<User> record) {
        List<UserDtoOutput> outputs = record.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<UserDtoOutput>builder()
                .number(record.getNumber() + 1)
                .size(record.getSize())
                .totalPages(record.getTotalPages())
                .totalElements(record.getTotalElements())
                .first(record.isFirst())
                .numberOfElements(record.getNumberOfElements())
                .last(record.isLast())
                .content(outputs)
                .build();
    }
}
