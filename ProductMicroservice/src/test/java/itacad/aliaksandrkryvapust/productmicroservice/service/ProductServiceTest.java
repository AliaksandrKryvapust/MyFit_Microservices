package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.EType;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProductMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProductRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProductValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private IProductRepository productRepository;
    @Mock
    private IProductValidator productValidator;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New product was created";
    final String textUpdate = "Product was updated";
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Integer weight = 100;

    @Test
    void save() {
        // preconditions
        final Product productInput = getPreparedProductInput();
        final Product productOutput = getPreparedProductOutput();
        Mockito.when(productRepository.save(productInput)).thenReturn(productOutput);

        //test
        Product actual = productService.save(productInput);

        // assert
        assertNotNull(actual);
        checkProductOutputFields(actual);
    }

    @Test
    void update() {
        // preconditions
        final Product userInput = getPreparedProductInput();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(userInput));
        Mockito.when(productRepository.save(userInput)).thenReturn(userInput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> actualProduct = ArgumentCaptor.forClass(Product.class);

        //test
        Product actual = productService.update(userInput, id, dtUpdate.toEpochMilli(), id);
        Mockito.verify(productValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualProduct.capture());
        Mockito.verify(productMapper, Mockito.times(1)).updateEntityFields(actualProduct.capture(),
                actualProduct.capture());

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(userInput, actualProduct.getValue());
        assertNotNull(actual);
        checkProductOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final Product userOutput = getPreparedProductOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Product> page = new PageImpl<>(Collections.singletonList(userOutput), pageable, 1);
        Mockito.when(productRepository.findAllByUserId(pageable, id)).thenReturn(page);

        //test
        Page<Product> actual = productService.get(pageable, id);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Product product : actual.getContent()) {
            checkProductOutputFields(product);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final Product productOutput = getPreparedProductOutput();
        Mockito.when(productRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(productOutput));

        //test
        Product actual = productService.get(id, id);

        // assert
        assertNotNull(actual);
        checkProductOutputFields(actual);
    }

    @Test
    void delete() {
        // preconditions

        //test
        productService.delete(id, id);
        Mockito.verify(productRepository, Mockito.times(1)).deleteByIdAndUserId(id, id);

        // assert
    }

    @Test
    void getByIds() {
        // preconditions
        final Product productOutput = getPreparedProductOutput();
        Mockito.when(productRepository.findAllById(Collections.singletonList(id))).thenReturn(Collections.singletonList(productOutput));

        //test
        List<Product> actual = productService.getByIds(Collections.singletonList(id));

        // assert
        assertNotNull(actual);
        actual.forEach(this::checkProductOutputFields);
    }

    @Test
    void saveDto() {
        // preconditions
        final ProductDtoInput dtoInput = getPreparedProductDtoInput();
        final ProductDtoOutput dtoOutput = getPreparedProductDtoOutput();
        final Product productInput = getPreparedProductInput();
        final Product productOutput = getPreparedProductOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        Mockito.when(productMapper.inputMapping(dtoInput, userDetails)).thenReturn(productInput);
        Mockito.when(productRepository.save(productInput)).thenReturn(productOutput);
        Mockito.when(auditMapper.productOutputMapping(productOutput, userDetails, text)).thenReturn(auditDto);
        Mockito.when(productMapper.outputMapping(productOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Product> actualProduct = ArgumentCaptor.forClass(Product.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        ProductDtoOutput actual = productService.saveDto(dtoInput);
        Mockito.verify(productValidator, Mockito.times(1)).validateEntity(actualProduct.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(productInput, actualProduct.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkProductDtoOutputFields(actual);
    }

    @Test
    void getDto() {
        // preconditions
        final Product productOutput = getPreparedProductOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Product> page = new PageImpl<>(Collections.singletonList(productOutput), pageable, 1);
        final PageDtoOutput<ProductDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        setSecurityContext();
        Mockito.when(productRepository.findAllByUserId(pageable, id)).thenReturn(page);
        Mockito.when(productMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<ProductDtoOutput> actual = productService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkProductDtoOutputFields);
    }

    @Test
    void testGetDto() {
        // preconditions
        final Product productOutput = getPreparedProductOutput();
        final ProductDtoOutput productDtoOutput = getPreparedProductDtoOutput();
        setSecurityContext();
        Mockito.when(productRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(productOutput));
        Mockito.when(productMapper.outputMapping(productOutput)).thenReturn(productDtoOutput);

        //test
        ProductDtoOutput actual = productService.getDto(id);

        // assert
        assertNotNull(actual);
        checkProductDtoOutputFields(actual);
    }


    @Test
    void updateDto() {
        // preconditions
        final Product productInput = getPreparedProductInput();
        final Product productOutput = getPreparedProductOutput();
        final ProductDtoInput dtoInput = getPreparedProductDtoInput();
        final ProductDtoOutput dtoOutput = getPreparedProductDtoOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        Mockito.when(productMapper.inputMapping(dtoInput, userDetails)).thenReturn(productInput);
        Mockito.when(productRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(productInput));
        Mockito.when(productRepository.save(productInput)).thenReturn(productOutput);
        Mockito.when(auditMapper.productOutputMapping(productOutput, userDetails, textUpdate)).thenReturn(auditDto);
        Mockito.when(productMapper.outputMapping(productOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> actualProduct = ArgumentCaptor.forClass(Product.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        ProductDtoOutput actual = productService.updateDto(dtoInput, id, dtUpdate.toEpochMilli());
        Mockito.verify(productValidator, Mockito.times(1)).validateEntity(actualProduct.capture());
        Mockito.verify(productValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualProduct.capture());
        Mockito.verify(productMapper, Mockito.times(1)).updateEntityFields(actualProduct.capture(),
                actualProduct.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(productInput, actualProduct.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkProductDtoOutputFields(actual);
    }

    @Test
    void deleteDto() {
        // preconditions
        setSecurityContext();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(auditMapper.productOutputMapping(any(Product.class), any(MyUserDetails.class), any(String.class)))
                .thenReturn(auditDto);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        productService.deleteDto(id);
        Mockito.verify(productRepository, Mockito.times(1)).deleteByIdAndUserId(id, id);
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
    }

    private MyUserDetails setSecurityContext() {
        final MyUserDetails userDetails = getPreparedUserDetails();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        return userDetails;
    }

    private Product getPreparedProductInput() {
        return Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
    }

    private Product getPreparedProductOutput() {
        return Product.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .id(id)
                .build();
    }

    PageDtoOutput<ProductDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<ProductDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedProductDtoOutput()))
                .build();
    }

    private ProductDtoInput getPreparedProductDtoInput() {
        return ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
    }

    private UserDto getPreparedUserDto() {
        return UserDto.builder()
                .role(EUserRole.USER.name())
                .email(email)
                .id(id.toString())
                .build();
    }

    private AuditDto getPreparedAuditDto() {
        return AuditDto.builder()
                .id(String.valueOf(id))
                .user(getPreparedUserDto())
                .text(text)
                .type(EType.PRODUCT.name())
                .build();
    }

    private MyUserDetails getPreparedUserDetails() {
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(id)
                .username(email)
                .authenticated(true)
                .role(EUserRole.USER)
                .dtUpdate(dtUpdate)
                .build();
        return new MyUserDetails(userPrincipal);
    }

    private ProductDtoOutput getPreparedProductDtoOutput() {
        return ProductDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .id(id.toString())
                .build();
    }

    private void checkProductOutputFields(Product actual) {
        assertEquals(id, actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(carbohydrates, actual.getCarbohydrates());
        assertEquals(fats, actual.getFats());
        assertEquals(proteins, actual.getProteins());
        assertEquals(calories, actual.getCalories());
        assertEquals(title, actual.getTitle());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }

    private void checkProductDtoOutputFields(ProductDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(carbohydrates, actual.getCarbohydrates());
        assertEquals(fats, actual.getFats());
        assertEquals(proteins, actual.getProteins());
        assertEquals(calories, actual.getCalories());
        assertEquals(title, actual.getTitle());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }

    private void checkPageDtoOutputFields(PageDtoOutput<ProductDtoOutput> actual) {
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.getFirst());
        Assertions.assertTrue(actual.getLast());
        assertEquals(2, actual.getNumber());
        assertEquals(1, actual.getNumberOfElements());
        assertEquals(1, actual.getSize());
        assertEquals(1, actual.getTotalPages());
        assertEquals(1, actual.getTotalElements());
    }
}