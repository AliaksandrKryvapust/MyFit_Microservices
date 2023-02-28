package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProductMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProductRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService, IProductManager {
    private final static String PRODUCT_POST = "New product was created";
    private final static String PRODUCT_PUT = "Product was updated";
    private final static String PRODUCT_DELETE = "Product was deleted";
    private final IProductRepository productRepository;
    private final IProductValidator productValidator;
    private final ProductMapper productMapper;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product, UUID id, Long version) {
        Product currentEntity = getEntity(id);
        productValidator.optimisticLockCheck(version, currentEntity);
        productValidator.checkCredentials(currentEntity);
        productMapper.updateEntityFields(product, currentEntity);
        return save(currentEntity);
    }

    @Override
    public Page<Product> get(Pageable pageable, UUID userId) {
        return productRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Product get(UUID id, UUID userId) {
        return productRepository.findByIdAndUserId(id, userId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void delete(UUID id) {
        Product currentEntity = getEntity(id);
        productValidator.checkCredentials(currentEntity);
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getByIds(List<UUID> uuids) {
        return productRepository.findAllById(uuids);
    }

    private Product getEntity(UUID id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ProductDtoOutput saveDto(ProductDtoInput menuItemDtoInput) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product entityToSave = productMapper.inputMapping(menuItemDtoInput, userDetails);
        productValidator.validateEntity(entityToSave);
        Product product = save(entityToSave);
        prepareAudit(product, userDetails, PRODUCT_POST);
        return productMapper.outputMapping(product);
    }

    @Override
    public PageDtoOutput<ProductDtoOutput> getDto(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Product> page = get(pageable, userDetails.getId());
        return productMapper.outputPageMapping(page);
    }

    @Override
    public ProductDtoOutput getDto(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = get(id, userDetails.getId());
        return productMapper.outputMapping(product);
    }

    @Override
    public ProductDtoOutput updateDto(ProductDtoInput productDtoInput, UUID id, Long version) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product entityToSave = productMapper.inputMapping(productDtoInput, userDetails);
        productValidator.validateEntity(entityToSave);
        Product product = update(entityToSave, id, version);
        prepareAudit(product, userDetails, PRODUCT_PUT);
        return productMapper.outputMapping(product);
    }

    @Override
    public void deleteDto(UUID id) {
        delete(id);
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = Product.builder().id(id).build();
        prepareAudit(product, userDetails, PRODUCT_DELETE);
    }


    private void prepareAudit(Product product, MyUserDetails userDetails, String method) {
        AuditDto auditDto = auditMapper.productOutputMapping(product, userDetails, method);
        auditManager.audit(auditDto);
    }
}
