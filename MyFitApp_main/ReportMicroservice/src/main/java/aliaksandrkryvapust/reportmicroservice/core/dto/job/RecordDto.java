package aliaksandrkryvapust.reportmicroservice.core.dto.job;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Builder
@Data
@Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecordDto {
    private final @NonNull Integer weight;
    private final @NonNull Instant dtSupply;
    private ProductDto product;
    private MealDto recipe;
}
