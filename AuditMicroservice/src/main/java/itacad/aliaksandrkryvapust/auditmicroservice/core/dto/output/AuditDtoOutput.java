package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@Builder
public class AuditDtoOutput {
    private final @NonNull String id;
    private final @NonNull String actionId;
    private final @NonNull UserDtoOutput user;
    private final @NonNull String text;
    private final @NonNull String type;
    private final @NonNull Instant dtCreate;
}
