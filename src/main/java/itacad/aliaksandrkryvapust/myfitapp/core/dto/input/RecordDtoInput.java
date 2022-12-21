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
public class RecordDtoInput {
    private final UUID productId;
    private final UUID mealId;
    @NotNull(message = "proteins cannot be null")
    @Positive(message = "proteins should be positive")
    private final Double weight;
}
