package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class IngredientDto {
    private final @NonNull ProductDto product;
    private final @NonNull Integer weight;
}
