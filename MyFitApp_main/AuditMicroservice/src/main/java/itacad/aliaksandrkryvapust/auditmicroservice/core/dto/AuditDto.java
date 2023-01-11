package itacad.aliaksandrkryvapust.auditmicroservice.core.dto;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class AuditDto {
    private final @NonNull String id; // TODO validation
    private final @NonNull UserDto user;
    private final @NonNull String text;
    private final @NonNull EType type;
}
