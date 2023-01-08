package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Data
public class TokenValidationDto {
    private final @NonNull Boolean authenticated;
    private final @NonNull String username;
    private final @NonNull UserRole role;
}

