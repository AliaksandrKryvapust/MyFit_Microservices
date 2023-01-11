package aliaksandrkryvapust.reportmicroservice.core.mapper;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.TokenValidationDto;
import aliaksandrkryvapust.reportmicroservice.core.security.UserPrincipal;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EUserRole;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {
    public UserPrincipal inputValidationMapping(TokenValidationDto dto) {
        return UserPrincipal.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .authenticated(dto.getAuthenticated())
                .role(EUserRole.valueOf(dto.getRole()))
                .build();
    }
}
