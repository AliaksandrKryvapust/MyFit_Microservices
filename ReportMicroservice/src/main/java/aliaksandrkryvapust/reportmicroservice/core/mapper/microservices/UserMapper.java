package aliaksandrkryvapust.reportmicroservice.core.mapper.microservices;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.TokenValidationDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.UserDto;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.core.security.UserPrincipal;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EUserRole;
import aliaksandrkryvapust.reportmicroservice.repository.entity.User;
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
    public User inputMapping(MyUserDetails userDetails) {
        return User.builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .role(EUserRole.valueOf(userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority()))
                .build();
    }

    public UserDto outputAuditMapping(MyUserDetails userDetails) {
        return UserDto.builder()
                .uuid(userDetails.getId())
                .mail(userDetails.getUsername())
                .role(EUserRole.valueOf(userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority()))
                .build();
    }
}
