package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class ProductDtoOutput {
    private final @NonNull UUID uuid;
    private final @NonNull String title;
    private final @NonNull Integer calories;
    private final @NonNull Integer proteins;
    private final @NonNull Integer fats;
    private final @NonNull Integer carbohydrates;
    private final @NonNull Double weight;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
