package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ValidationTokenMapper {

    public TokenValidationDto outputMapping(String username, UserRole role) {
        return TokenValidationDto.builder()
                .authenticated(true)
                .username(username)
                .role(role).build();
    }
}
