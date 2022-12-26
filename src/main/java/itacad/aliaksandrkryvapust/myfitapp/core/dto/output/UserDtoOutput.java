package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class UserDtoOutput {
    private @NonNull UUID uuid;
    private @NonNull String nick;
    private @NonNull String mail;
    private @NonNull UserRole role;
    private @NonNull UserStatus status;
    private @NonNull Instant dtCreate;
    private @NonNull Instant dtUpdate;
}
