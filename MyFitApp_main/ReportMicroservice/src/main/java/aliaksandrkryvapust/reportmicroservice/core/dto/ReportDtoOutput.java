package aliaksandrkryvapust.reportmicroservice.core.dto;

import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Data
public class ReportDtoOutput {
    private final @NonNull UUID uuid;
    private final @NonNull EStatus status;
    private final @NonNull EType type;
    private final @NonNull String description;
    private final @NonNull ParamsDto params;
    private final @NonNull Long dtCreate;
    private final @NonNull Long dtUpdate;
}
