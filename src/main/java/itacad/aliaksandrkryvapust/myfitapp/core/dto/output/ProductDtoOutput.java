package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class ProductDtoOutput {
    private @NonNull UUID id;
    private @NonNull String title;
    private @NonNull Integer calories;
    private @NonNull Integer proteins;
    private @NonNull Integer fats;
    private @NonNull Integer carbohydrates;
    private @NonNull Instant dtCreate;
    private @NonNull Instant dtUpdate;
}
