package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class RecordDto {
    private final @NonNull UUID uuid;
    private final @NonNull Integer weight;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtSupply;
    private ProductDto product;
    private MealDto recipe;
}
