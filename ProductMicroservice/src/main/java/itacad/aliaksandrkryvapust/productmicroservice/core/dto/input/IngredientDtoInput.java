package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.ProductReferenceDtoInput;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@Data
@Jacksonized
public class IngredientDtoInput {
    @Valid
    @NotNull(message = "product reference cannot be null")
    private final ProductReferenceDtoInput product;
    @NotNull(message = "weight cannot be null")
    @Positive(message = "weight should be positive")
    private final Integer weight;
}
