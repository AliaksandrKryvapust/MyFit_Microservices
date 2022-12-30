package itacad.aliaksandrkryvapust.auditmicroservice.core.dto;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class TokenValidationDto {
    private final @NonNull Boolean authenticated;
    private final @NonNull String username;
    private final @NonNull UserRole role;
}
