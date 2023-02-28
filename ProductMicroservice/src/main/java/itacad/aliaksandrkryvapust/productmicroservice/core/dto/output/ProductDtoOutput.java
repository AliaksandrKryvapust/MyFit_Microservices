package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class ProductDtoOutput {
    private final @NonNull String id;
    private final @NonNull String title;
    private final @NonNull Integer calories;
    private final @NonNull Double proteins;
    private final @NonNull Double fats;
    private final @NonNull Double carbohydrates;
    private final @NonNull Integer weight;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
