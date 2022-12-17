package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class RecordDtoOutput {
    private @NonNull UUID id;
    private UUID productId;
    private UUID mealId;
    private @NonNull Double weight;
    private @NonNull Instant dtCreate;
}
