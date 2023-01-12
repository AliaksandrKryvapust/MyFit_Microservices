package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class AuditDto {
    private final @NonNull String id;
    private final @NonNull UserDto user;
    private final @NonNull String text;
    private final @NonNull EType type;
}
