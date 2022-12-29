package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.ProductMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IProductManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class ProductManager implements IProductManager {

    private final ProductMapper productMapper;
    private final IProductService productService;

    @Autowired
    public ProductManager(ProductMapper productMapper, IProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @Override
    public ProductDtoOutput save(ProductDtoInput menuItemDtoInput, HttpServletRequest request) {
        Product product = this.productService.save(productMapper.inputMapping(menuItemDtoInput));
        return productMapper.outputMapping(product);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return productMapper.outputPageMapping(this.productService.get(pageable));
    }

    @Override
    public ProductDtoOutput get(UUID id) {
        return productMapper.outputMapping(this.productService.get(id));
    }

    @Override
    public void delete(UUID id, HttpServletRequest request) {
        this.productService.delete(id);
    }

    @Override
    public ProductDtoOutput update(ProductDtoInput productDtoInput, UUID id, Long version, HttpServletRequest request) {
        Product product = this.productService.update(productMapper.inputMapping(productDtoInput), id, version);
        return productMapper.outputMapping(product);
    }
}
