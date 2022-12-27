package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.TokenValidationDto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenMapper {
    public TokenValidationDto outputMapping(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<GrantedAuthority> authorityList = (List<GrantedAuthority>) request.getAttribute("authorities");
        return TokenValidationDto.builder()
                .isAuthenticated(true)
                .username(username)
                .authorities(authorityList).build();
    }
}
