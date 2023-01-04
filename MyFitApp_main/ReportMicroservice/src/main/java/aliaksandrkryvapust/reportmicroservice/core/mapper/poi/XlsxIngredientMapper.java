package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.IngredientDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredient;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredientProduct;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxIngredientMapper {
    private final XlsxProductMapper productMapper;

    public XlsxIngredientMapper(XlsxProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public XlsxIngredient inputMapping(IngredientDto ingredientDto) {
        XlsxIngredientProduct ingredientProduct = productMapper.ingredientInputMapping(ingredientDto.getProduct());
        return XlsxIngredient.builder()
                .product(ingredientProduct)
                .weight(ingredientDto.getWeight())
                .build();
    }
}
