package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class ProductDtoInput {
    @NotNull(message = "title cannot be null")
    @Size(min = 2, max = 100, message = "title should contain from 4 to 100 letters")
    private final String title;
    @NotNull(message = "calories cannot be null")
    @Positive(message = "calories should be positive")
    private final Integer calories;
    @NotNull(message = "proteins cannot be null")
    @DecimalMin(value = "0", message = "proteins should be zero or greater")
    private final Double proteins;
    @NotNull(message = "fats cannot be null")
    @DecimalMin(value = "0", message = "fats should  be zero or greater")
    private final Double fats;
    @NotNull(message = "carbohydrates cannot be null")
    @DecimalMin(value = "0", message = "carbohydrates should  be zero or greater")
    private final Double carbohydrates;
    @NotNull(message = "weight cannot be null")
    @Positive(message = "weight should be positive")
    private final Integer weight;
}
