package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class MealDto {
    private final @NonNull UUID uuid;
    private final @NonNull List<IngredientDto> composition;
    private final @NonNull String title;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
