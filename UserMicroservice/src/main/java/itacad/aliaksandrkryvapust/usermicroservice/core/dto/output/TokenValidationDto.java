package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Builder
@Data
public class TokenValidationDto {
    private final @NonNull String id;
    private final @NonNull Boolean authenticated;
    private final @NonNull String username;
    private final @NonNull String role;
    private final @NonNull Instant dtUpdate;
}

