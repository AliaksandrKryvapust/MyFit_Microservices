package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.ProductMapper;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IProductManager;
import itacad.aliaksandrkryvapust.myfitapp.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.UserService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class ProductManager implements IProductManager {
    private final static String productPost = "New product was created";
    private final static String productPut = "Product was updated";
    private final static String productDelete = "Product was deleted";
    private final ProductMapper productMapper;
    private final IProductService productService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    @Autowired
    public ProductManager(ProductMapper productMapper, IProductService productService, JwtTokenUtil jwtTokenUtil,
                          UserService userService, AuditMapper auditMapper, AuditManager auditManager) {
        this.productMapper = productMapper;
        this.productService = productService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public ProductDtoOutput save(ProductDtoInput menuItemDtoInput, HttpServletRequest request) {
        try {
            Product product = this.productService.save(productMapper.inputMapping(menuItemDtoInput));
            User user = getUser(request);
            AuditDto auditDto = this.auditMapper.productOutputMapping(product, user, productPost);
            this.auditManager.audit(auditDto);
            return productMapper.outputMapping(product);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtTokenUtil.getUsername(token);
        return this.userService.getUser(username);
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
        try {
            this.productService.delete(id);
            User user = getUser(request);
            Product product = Product.builder().id(id).build();
            AuditDto auditDto = this.auditMapper.productOutputMapping(product, user, productDelete);
            this.auditManager.audit(auditDto);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public ProductDtoOutput update(ProductDtoInput productDtoInput, UUID id, Long version, HttpServletRequest request) {
        try {
            Product product = this.productService.update(productMapper.inputMapping(productDtoInput), id, version);
            User user = getUser(request);
            AuditDto auditDto = this.auditMapper.productOutputMapping(product, user, productPut);
            this.auditManager.audit(auditDto);
            return productMapper.outputMapping(product);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }
}
