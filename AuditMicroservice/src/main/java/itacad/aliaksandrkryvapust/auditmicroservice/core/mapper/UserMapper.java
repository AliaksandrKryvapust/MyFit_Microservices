package itacad.aliaksandrkryvapust.auditmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.TokenValidationDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.UserDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {

    public UserPrincipal inputValidationMapping(TokenValidationDto dto) {
        return UserPrincipal.builder()
                .id(UUID.fromString(dto.getId()))
                .username(dto.getUsername())
                .authenticated(dto.getAuthenticated())
                .role(EUserRole.valueOf(dto.getRole()))
                .build();
    }

    public User inputMapping(UserDto userDto) {
        if (userDto.getStatus() != null) {
            return User.builder()
                    .id(UUID.fromString(userDto.getId()))
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .role(EUserRole.valueOf(userDto.getRole()))
                    .status(EUserStatus.valueOf(userDto.getStatus()))
                    .dtCreate(userDto.getDtCreate())
                    .dtUpdate(userDto.getDtUpdate())
                    .build();
        } else {
            return User.builder()
                    .id(UUID.fromString(userDto.getId()))
                    .email(userDto.getEmail())
                    .role(EUserRole.valueOf(userDto.getRole()))
                    .build();
        }
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .id(user.getId().toString())
                .dtCreate(user.getDtCreate())
                .dtUpdate(user.getDtUpdate())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }
}
