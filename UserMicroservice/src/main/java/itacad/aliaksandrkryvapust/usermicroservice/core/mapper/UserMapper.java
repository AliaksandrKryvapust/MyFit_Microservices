package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
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
                .role(EUserRole.USER)
                .status(EUserStatus.WAITING_ACTIVATION)
                .build();
    }

    public User inputMapping(UserDtoInput userDtoInput) {
        return User.builder()
                .username(userDtoInput.getUsername())
                .password(encoder.encode(userDtoInput.getPassword()))
                .email(userDtoInput.getEmail())
                .role(EUserRole.valueOf(userDtoInput.getRole()))
                .status(EUserStatus.valueOf(userDtoInput.getStatus()))
                .build();
    }

    public User activationMapping(User user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public UserRegistrationDtoOutput registerOutputMapping(User user) {
        String role = user.getRole().name();
        return UserRegistrationDtoOutput.builder()
                .email(user.getEmail())
                .role(role)
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
