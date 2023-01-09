package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class RecordDto {
    private final @NonNull Integer weight;
    private final @NonNull String dtSupply;
    private ProductDtoOutput product;
    private MealDtoOutput recipe;
}
