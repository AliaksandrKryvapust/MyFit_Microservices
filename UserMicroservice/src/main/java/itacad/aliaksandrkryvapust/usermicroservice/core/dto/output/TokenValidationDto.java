package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class TokenValidationDto {
    private final @NonNull UUID id;
    private final @NonNull Boolean authenticated;
    private final @NonNull String username;
    private final @NonNull UserRole role;
    private final @NonNull Instant dtUpdate;
}

