package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductMapper {
    public Product inputMapping(ProductDtoInput productDtoInput, MyUserDetails userDetails) {
        return Product.builder().title(productDtoInput.getTitle())
                .calories(productDtoInput.getCalories())
                .proteins(productDtoInput.getProteins())
                .fats(productDtoInput.getFats())
                .carbohydrates(productDtoInput.getCarbohydrates())
                .weight(productDtoInput.getWeight())
                .userId(userDetails.getId()).build();
    }

    public ProductDtoOutput outputMapping(Product product) {
        return ProductDtoOutput.builder()
                .uuid(product.getId())
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

    public PageDtoOutput<ProductDtoOutput> outputPageMapping(Page<Product> products) {
        List<ProductDtoOutput> outputs = products.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<ProductDtoOutput>builder()
                .number(products.getNumber()+1)
                .size(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .first(products.isFirst())
                .numberOfElements(products.getNumberOfElements())
                .last(products.isLast())
                .content(outputs)
                .build();
    }
}
