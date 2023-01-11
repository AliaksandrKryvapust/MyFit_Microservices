package itacad.aliaksandrkryvapust.auditmicroservice.core.dto;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserStatus;
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
    private final UUID uuid;
    private final String nick;
    private final @NonNull String mail;
    private final @NonNull EUserRole role;
    private final EUserStatus status;
    private final Instant dtCreate;
    private final Instant dtUpdate;
}
