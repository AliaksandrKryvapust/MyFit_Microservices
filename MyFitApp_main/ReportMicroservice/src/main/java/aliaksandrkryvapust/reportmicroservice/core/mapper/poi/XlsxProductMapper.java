package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.ProductDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredientProduct;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxProduct;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxProductMapper {
    public XlsxProduct inputMapping(ProductDto productDto) {
        return XlsxProduct.builder().title(productDto.getTitle())
                .calories(productDto.getCalories())
                .proteins(productDto.getProteins())
                .fats(productDto.getFats())
                .carbohydrates(productDto.getCarbohydrates())
                .weight(productDto.getWeight()).build();
    }

    public XlsxIngredientProduct ingredientInputMapping(ProductDto productDto) {
        return XlsxIngredientProduct.builder().title(productDto.getTitle())
                .calories(productDto.getCalories())
                .proteins(productDto.getProteins())
                .fats(productDto.getFats())
                .carbohydrates(productDto.getCarbohydrates())
                .weight(productDto.getWeight()).build();
    }
}
