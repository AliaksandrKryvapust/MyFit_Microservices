package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
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
    private final @NonNull EUserRole role;
    private final @NonNull EUserStatus status;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
