package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class MealDtoOutput {
    private @NonNull UUID id;
    private @NonNull List<IngredientDtoOutput> ingredients;
    private @NonNull String title;
    private @NonNull Instant dtCreate;
    private @NonNull Instant dtUpdate;
}
