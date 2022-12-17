package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@Jacksonized
public class MealDtoInput {
    @Valid
    @NotNull(message = "ingredients cannot be null")
    private List<IngredientDtoInput> ingredients;
    @NotNull(message = "title cannot be null")
    @Size(min = 2, max = 100, message = "title should contain from 4 to 100 letters")
    private String title;
}
