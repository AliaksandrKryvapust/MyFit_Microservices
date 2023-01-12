package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class MealDtoOutput {
    private final @NonNull UUID uuid;
    private final @NonNull List<IngredientDtoOutput> composition;
    private final @NonNull String title;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
