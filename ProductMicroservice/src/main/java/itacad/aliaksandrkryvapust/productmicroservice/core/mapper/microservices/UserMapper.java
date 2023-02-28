package itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.TokenValidationDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.User;
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
                .dtUpdate(dto.getDtUpdate())
                .build();
    }

    public User inputMapping(MyUserDetails userDetails) {
        return User.builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .version(userDetails.getVersion())
                .build();
    }

    public UserDto outputAuditMapping(MyUserDetails userDetails) {
        return UserDto.builder()
                .id(userDetails.getId().toString())
                .email(userDetails.getUsername())
                .role(userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority())
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .user_id(user.getUserId().toString())
                .username(user.getUsername())
                .version(user.getVersion())
                .build();
    }
}
