package itacad.aliaksandrkryvapust.auditmicroservice.core.dto;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Type;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class AuditDto {
    private final @NonNull String id;
    private final @NonNull UserDto user;
    private final @NonNull String text;
    private final @NonNull Type type;
}
