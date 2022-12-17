package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;

@Builder
@Data
public class IngredientDtoOutput {
    private @NonNull List<Product> product;
    private @NonNull Double weight;
    private @NonNull Instant dtCreate;
    private @NonNull Instant dtUpdate;
}
