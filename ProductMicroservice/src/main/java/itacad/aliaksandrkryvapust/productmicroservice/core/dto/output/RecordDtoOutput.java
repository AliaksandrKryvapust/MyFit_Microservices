package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class RecordDtoOutput {
    private final @NonNull String id;
    private final @NonNull Integer weight;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtSupply;
    private @Nullable ProductDtoOutput product;
    private @Nullable MealDtoOutput recipe;
}
