package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Data
public class TokenValidationDto {
    private final @NonNull Boolean isAuthenticated;
    private final @NonNull String username;
    private final @NonNull List<GrantedAuthority> authorities;
}
