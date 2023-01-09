package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull UUID uuid;
    private final @NonNull String nick;
    private final @NonNull String mail;
    private final @NonNull UserRole role;
    private final @NonNull UserStatus status;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
