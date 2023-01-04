package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class ProductDto {
    private final @NonNull String title;
    private final @NonNull Integer calories;
    private final @NonNull Double proteins;
    private final @NonNull Double fats;
    private final @NonNull Double carbohydrates;
    private final @NonNull Integer weight;
}
