package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.MealReferenceDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.ProductReferenceDtoInput;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.Instant;

@Builder
@Data
@Jacksonized
public class RecordDtoInput {
    @NotNull(message = "supply date cannot be null")
    @Past(message = "supply date should refer to moment in the past")
    private final Instant dtSupply;
    @Valid
    private final ProductReferenceDtoInput product;
    @Valid
    private final MealReferenceDtoInput recipe;
    @NotNull(message = "proteins cannot be null")
    @Positive(message = "proteins should be positive")
    private final Integer weight;
}
