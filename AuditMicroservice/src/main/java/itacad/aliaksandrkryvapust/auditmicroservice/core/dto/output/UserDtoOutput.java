package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@Builder
public class UserDtoOutput {
    private final @NonNull String id;
    private final @NonNull String email;
    private final @NonNull String role;
}
