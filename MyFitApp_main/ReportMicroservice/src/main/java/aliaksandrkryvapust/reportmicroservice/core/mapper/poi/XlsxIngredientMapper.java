package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.IngredientDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxIngredientMapper {

    public XlsxIngredient inputMapping(IngredientDto ingredientDto) {
        return XlsxIngredient.builder()
                .weight(ingredientDto.getWeight())
                .productTitle(ingredientDto.getProduct().getTitle())
                .productCalories(ingredientDto.getProduct().getCalories())
                .productProteins(ingredientDto.getProduct().getProteins())
                .productFats(ingredientDto.getProduct().getFats())
                .productCarbohydrates(ingredientDto.getProduct().getCarbohydrates())
                .productWeight(ingredientDto.getProduct().getWeight())
                .build();
    }
}
