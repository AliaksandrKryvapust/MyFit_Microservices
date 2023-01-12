package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Data
@Jacksonized
public class MealDto {
    private final @NonNull List<IngredientDto> composition;
    private final @NonNull String title;
}
