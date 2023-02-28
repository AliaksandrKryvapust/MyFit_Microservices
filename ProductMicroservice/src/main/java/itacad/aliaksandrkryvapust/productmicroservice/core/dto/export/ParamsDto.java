package itacad.aliaksandrkryvapust.productmicroservice.core.dto.export;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Builder
@Data
public class ParamsDto {
    private final @NonNull Instant from;
    private final @NonNull Instant to;
    private final @NonNull String userId;
}
