package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class IngredientDto {
    private final @NonNull ProductDto product;
    private final @NonNull Integer weight;
}
