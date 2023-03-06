package itacad.aliaksandrkryvapust.auditmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.TokenValidationDto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenMapper {

    public TokenValidationDto createTokenValidationDto() {
        return TokenValidationDto.builder()
                .id("6639d1e8-7e73-4888-a489-9ca9247d2826")
                .username("audit@email")
                .role("AUDIT")
                .authenticated(true)
                .build();
    }
}
