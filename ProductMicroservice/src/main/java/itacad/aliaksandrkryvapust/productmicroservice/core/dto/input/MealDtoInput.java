package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Data
@Jacksonized
public class MealDtoInput {
    @Valid
    @NotNull(message = "composition cannot be null")
    private final Set<IngredientDtoInput> composition;
    @NotNull(message = "title cannot be null")
    @Size(min = 2, max = 100, message = "title should contain from 2 to 100 letters")
    private final String title;
}
