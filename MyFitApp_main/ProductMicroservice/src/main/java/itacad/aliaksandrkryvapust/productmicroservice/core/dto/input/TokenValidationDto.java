package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class TokenValidationDto {
    private final @NonNull UUID id;
    private final @NonNull Boolean authenticated;
    private final @NonNull String username;
    private final @NonNull EUserRole role;
    private final @NonNull Instant dtUpdate;
}

