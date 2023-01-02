package aliaksandrkryvapust.reportmicroservice.core.dto;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Params;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class ReportDtoOutput {
    private final @NonNull UUID id;
    private final @NonNull Status status;
    private final @NonNull Type type;
    private final @NonNull String description;
    private final @NonNull Params params;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
