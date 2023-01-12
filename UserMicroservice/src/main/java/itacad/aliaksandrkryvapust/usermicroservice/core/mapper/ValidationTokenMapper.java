package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ValidationTokenMapper {

    public TokenValidationDto outputMapping(User user) {
        boolean authenticated = user.getStatus().equals(UserStatus.ACTIVATED);
        return TokenValidationDto.builder()
                .id(user.getId())
                .authenticated(authenticated)
                .username(user.getEmail())
                .role(user.getRole())
                .dtUpdate(user.getDtUpdate()).build();
    }
}
