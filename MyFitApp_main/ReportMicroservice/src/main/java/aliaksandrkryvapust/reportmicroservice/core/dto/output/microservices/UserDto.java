package aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices;

import aliaksandrkryvapust.reportmicroservice.repository.entity.EUserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Data
public class UserDto {
    private final @NonNull UUID uuid;
    private final @NonNull String mail;
    private final @NonNull EUserRole role;
}
