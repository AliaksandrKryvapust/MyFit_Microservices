package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class IngredientDtoOutput {
    private final @NonNull ProductDtoOutput product;
    private final @NonNull Integer weight;
}
