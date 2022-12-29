package itacad.aliaksandrkryvapust.auditmicroservice.core.dto;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class UserDto {
    private final @NonNull UUID uuid;
    private final @NonNull String nick;
    private final @NonNull String mail;
    private final @NonNull UserRole role;
    private final @NonNull UserStatus status;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
