package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class AuditDto {
    private final @NonNull String id;
    private final @NonNull UserDto user;
    private final @NonNull String text;
    private final @NonNull String type;
}
