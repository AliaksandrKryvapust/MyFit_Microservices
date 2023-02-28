package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.TokenValidationDto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TokenMapper {

    public TokenValidationDto createTokenValidationDto() {
        return TokenValidationDto.builder()
                .id(UUID.fromString("6639d1e8-7e73-4888-a489-9ca9247d2826"))
                .username("report@email")
                .role("REPORT")
                .authenticated(true)
                .dtUpdate(Instant.now())
                .build();
    }
}
