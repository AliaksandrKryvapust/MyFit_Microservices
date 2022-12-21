package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class IngredientDtoInput {
    @NotNull(message = "productId cannot be empty")
    private final UUID productId;
    @NotNull(message = "weight cannot be null")
    @Positive(message = "weight should be positive")
    private final Double weight;
}
