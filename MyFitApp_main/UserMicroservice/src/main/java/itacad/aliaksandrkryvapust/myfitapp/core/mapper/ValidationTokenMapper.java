package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserRole;
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
