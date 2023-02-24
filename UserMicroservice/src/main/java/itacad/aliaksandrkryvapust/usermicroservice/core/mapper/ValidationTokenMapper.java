package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ValidationTokenMapper {

    public TokenValidationDto outputMapping(User user) {
        boolean authenticated = user.getStatus().equals(EUserStatus.ACTIVATED);
        return TokenValidationDto.builder()
                .id(user.getId().toString())
                .authenticated(authenticated)
                .username(user.getEmail())
                .role(user.getRole().name())
                .dtUpdate(user.getDtUpdate())
                .build();
    }
}
