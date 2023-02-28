package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull String user_id;
    private final @NonNull String username;
    private final @NonNull Instant version;
}
