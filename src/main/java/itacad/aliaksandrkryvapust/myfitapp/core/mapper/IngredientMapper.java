package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.IngredientDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IngredientMapper {
    private final ProductMapper productMapper;

    @Autowired
    public IngredientMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Ingredient inputMapping(IngredientDtoInput ingredientDtoInput) {
        return Ingredient.builder()
                .productId(ingredientDtoInput.getProductId())
                .weight(ingredientDtoInput.getWeight())
                .build();
    }

    public IngredientDtoOutput outputMapping(Ingredient ingredient) {
        ProductDtoOutput dtoOutput = this.productMapper.outputMapping(ingredient.getProduct());
        return IngredientDtoOutput.builder()
                .product(dtoOutput)
                .weight(ingredient.getWeight())
                .build();
    }
}
