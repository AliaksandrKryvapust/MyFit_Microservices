package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductMapper {
    public Product inputMapping(ProductDtoInput productDtoInput) {
        return Product.builder().title(productDtoInput.getTitle())
                .calories(productDtoInput.getCalories())
                .proteins(productDtoInput.getProteins())
                .fats(productDtoInput.getFats())
                .carbohydrates(productDtoInput.getCarbohydrates())
                .weight(productDtoInput.getWeight()).build();
    }

    public ProductDtoOutput outputMapping(Product product) {
        return ProductDtoOutput.builder()
                .id(product.getId())
                .title(product.getTitle())
                .calories(product.getCalories())
                .proteins(product.getProteins())
                .fats(product.getFats())
                .carbohydrates(product.getCarbohydrates())
                .weight(product.getWeight())
                .dtCreate(product.getDtCreate())
                .dtUpdate(product.getDtUpdate())
                .build();
    }
}
