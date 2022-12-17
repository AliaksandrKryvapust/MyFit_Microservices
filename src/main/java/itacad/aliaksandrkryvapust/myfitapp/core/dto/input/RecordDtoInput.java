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
    private UUID productId;
    private UUID mealId;
    @NotNull(message = "proteins cannot be null")
    @Positive(message = "proteins should be positive")
    private Double weight;
}
