package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class ProductDtoInput {
    @NotNull(message = "title cannot be null")
    @Size(min = 2, max = 100, message = "title should contain from 4 to 100 letters")
    private String title;
    @NotNull(message = "calories cannot be null")
    @Positive(message = "calories should be positive")
    private Integer calories;
    @NotNull(message = "proteins cannot be null")
    @Positive(message = "proteins should be positive")
    private Integer proteins;
    @NotNull(message = "fats cannot be null")
    @Positive(message = "fats should be positive")
    private Integer fats;
    @NotNull(message = "carbohydrates cannot be null")
    @Positive(message = "carbohydrates should be positive")
    private Integer carbohydrates;
}
