package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.IngredientDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IngredientMapper {
    private final ProductMapper productMapper;

    public Ingredient inputMapping(IngredientDtoInput ingredientDtoInput) {
        return Ingredient.builder()
                .productId(UUID.fromString(ingredientDtoInput.getProduct().getId()))
                .weight(ingredientDtoInput.getWeight())
                .build();
    }

    public IngredientDtoOutput outputMapping(Ingredient ingredient) {
        ProductDtoOutput dtoOutput = productMapper.outputMapping(ingredient.getProduct());
        return IngredientDtoOutput.builder()
                .product(dtoOutput)
                .weight(ingredient.getWeight())
                .build();
    }
}
