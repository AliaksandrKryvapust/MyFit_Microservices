package itacad.aliaksandrkryvapust.productmicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProductMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IProductManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class ProductManager implements IProductManager {
    private final static String productPost = "New product was created";
    private final static String productPut = "Product was updated";
    private final static String productDelete = "Product was deleted";
    private final ProductMapper productMapper;
    private final IProductService productService;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Autowired
    public ProductManager(ProductMapper productMapper, IProductService productService,
                          AuditMapper auditMapper, AuditManager auditManager) {
        this.productMapper = productMapper;
        this.productService = productService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public ProductDtoOutput save(ProductDtoInput menuItemDtoInput) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Product product = this.productService.save(productMapper.inputMapping(menuItemDtoInput, userDetails));
            this.prepareAuditData(product, productPost, userDetails);
            return productMapper.outputMapping(product);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput<ProductDtoOutput> get(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return productMapper.outputPageMapping(this.productService.get(pageable, userDetails.getId()));
    }

    @Override
    public ProductDtoOutput get(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return productMapper.outputMapping(this.productService.get(id, userDetails.getId()));
    }

    @Override
    public void delete(UUID id) {
        try {
            this.productService.delete(id);
            this.prepareAuditToSend(id);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public ProductDtoOutput update(ProductDtoInput productDtoInput, UUID id, Long version) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Product product = this.productService.update(productMapper.inputMapping(productDtoInput, userDetails), id, version);
            this.prepareAuditData(product, productPut, userDetails);
            return productMapper.outputMapping(product);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private void prepareAuditData(Product product, String productMethod, MyUserDetails userDetails)
            throws JsonProcessingException, URISyntaxException {
        AuditDto auditDto = this.auditMapper.productOutputMapping(product, userDetails, productMethod);
        this.auditManager.audit(auditDto);
    }

    private void prepareAuditToSend(UUID id) throws JsonProcessingException, URISyntaxException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = Product.builder().id(id).build();
        AuditDto auditDto = this.auditMapper.productOutputMapping(product, userDetails, productDelete);
        this.auditManager.audit(auditDto);
    }
}
