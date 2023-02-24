package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {
    private final PasswordEncoder encoder;

    public User userInputMapping(UserDtoRegistration userDtoRegistration) {
        return User.builder()
                .username(userDtoRegistration.getUsername())
                .password(encoder.encode(userDtoRegistration.getPassword()))
                .email(userDtoRegistration.getEmail())
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
        return UserRegistrationDtoOutput.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public UserLoginDtoOutput loginOutputMapping(UserDetails userDetails, String token) {
        return UserLoginDtoOutput.builder()
                .email(userDetails.getUsername())
                .token(token)
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .id(user.getId().toString())
                .dtCreate(user.getDtCreate())
                .dtUpdate(user.getDtUpdate())
                .dtLogin(user.getDtLogin())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .status(user.getStatus().name())
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

    public void updateEntityFields(User user, User currentEntity) {
        currentEntity.setUsername(user.getUsername());
        currentEntity.setPassword(user.getPassword());
        currentEntity.setEmail(user.getEmail());
        currentEntity.setStatus(user.getStatus());
    }
}
