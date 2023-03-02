package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@Jacksonized
public class MealReferenceDtoInput {
    @NotBlank(message = "recipe cannot be blank")
    private final String id;
}
