package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AuditDtoOutput {
    private final @NonNull String id;
    private final @NonNull UserDtoOutput user;
    private final @NonNull String text;
    private final @NonNull EType type;
}
