package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class MealReferenceDtoInput {
    @NotNull(message = "recipe cannot be empty")
    private final UUID uuid;
}
