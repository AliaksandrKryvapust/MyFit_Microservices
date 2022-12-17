package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Builder
@Data
public class IngredientDtoOutput {
    private @NonNull ProductDtoOutput product;
    private @NonNull Double weight;
    private @NonNull Instant dtCreate;
    private @NonNull Instant dtUpdate;
}
