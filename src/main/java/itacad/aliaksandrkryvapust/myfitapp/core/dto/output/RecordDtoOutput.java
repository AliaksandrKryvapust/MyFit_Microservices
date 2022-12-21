package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class RecordDtoOutput {
    private final @NonNull UUID uuid;
    private final @NonNull Double weight;
    private final @NonNull Instant dtCreate;
    private ProductDtoOutput product;
    private MealDtoOutput recipe;
}
