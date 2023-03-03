package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.IngredientDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.ProductReferenceDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.EType;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.MealMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.transactional.api.IMealTransactionalService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IMealValidator;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {
    @InjectMocks
    private MealService mealService;
    @Mock
    private IProductService productService;
    @Mock
    private IMealRepository mealRepository;
    @Mock
    private IMealValidator mealValidator;
    @Mock
    private MealMapper mealMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private IMealTransactionalService transactionalService;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New meal was created";
    final String textUpdate = "Meal was updated";
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Integer weight = 100;

    @Test
    void save() {
        // preconditions
        final Meal mealInput = getPreparedMealInput();
        final Meal mealOutput = getPreparedMealOutput();
        final Product product = getPreparedProductOutput();
        Mockito.when(productService.getByIds(Collections.singletonList(id))).thenReturn(Collections.singletonList(product));
        Mockito.when(mealRepository.save(mealInput)).thenReturn(mealOutput);

        //test
        Meal actual = mealService.save(mealInput);

        // assert
        assertNotNull(actual);
        checkMealOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final Meal mealOutput = getPreparedMealOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Meal> page = new PageImpl<>(Collections.singletonList(mealOutput), pageable, 1);
        Mockito.when(mealRepository.findAllByUserId(pageable, id)).thenReturn(page);

        //test
        Page<Meal> actual = mealService.get(pageable, id);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Meal meal : actual.getContent()) {
            checkMealOutputFields(meal);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final Meal mealOutput = getPreparedMealOutput();
        Mockito.when(mealRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(mealOutput));

        //test
        Meal actual = mealService.get(id, id);

        // assert
        assertNotNull(actual);
        checkMealOutputFields(actual);
    }

    @Test
    void update() {
        // preconditions
        final Meal mealInput = getPreparedMealInput();
        final Meal mealOutput = getPreparedMealOutput();
        final Product product = getPreparedProductOutput();
        Mockito.when(mealRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(mealInput));
        Mockito.when(productService.getByIds(Collections.singletonList(id))).thenReturn(Collections.singletonList(product));
        Mockito.when(mealRepository.save(mealInput)).thenReturn(mealOutput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Meal> actualMeal = ArgumentCaptor.forClass(Meal.class);

        //test
        Meal actual = mealService.update(mealInput, id, dtUpdate.toEpochMilli(), id);
        Mockito.verify(mealValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualMeal.capture());
        Mockito.verify(transactionalService, Mockito.times(1)).deleteTransactional(id, id);

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(mealInput, actualMeal.getValue());
        assertNotNull(actual);
        checkMealOutputFields(actual);
    }

    @Test
    void delete() {
        // preconditions

        //test
        mealService.delete(id, id);
        Mockito.verify(transactionalService, Mockito.times(1)).deleteTransactional(id, id);

        // assert
    }

    @Test
    void saveDto() {
        // preconditions
        final MealDtoInput dtoInput = getPreparedMealDtoInput();
        final MealDtoOutput dtoOutput = getPreparedMealDtoOutput();
        final Meal mealInput = getPreparedMealInput();
        final Meal mealOutput = getPreparedMealOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        final Product product = getPreparedProductOutput();
        Mockito.when(mealMapper.inputMapping(dtoInput, userDetails)).thenReturn(mealInput);
        Mockito.when(productService.getByIds(Collections.singletonList(id))).thenReturn(Collections.singletonList(product));
        Mockito.when(mealRepository.save(mealInput)).thenReturn(mealOutput);
        Mockito.when(auditMapper.mealOutputMapping(mealOutput, userDetails, text)).thenReturn(auditDto);
        Mockito.when(mealMapper.outputMapping(mealOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Meal> actualMeal = ArgumentCaptor.forClass(Meal.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        MealDtoOutput actual = mealService.saveDto(dtoInput);
        Mockito.verify(mealValidator, Mockito.times(1)).validateEntity(actualMeal.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(mealInput, actualMeal.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkMealDtoOutputFields(actual);
    }

    @Test
    void getDto() {
        // preconditions
        final Meal mealOutput = getPreparedMealOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Meal> page = new PageImpl<>(Collections.singletonList(mealOutput), pageable, 1);
        final PageDtoOutput<MealDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        setSecurityContext();
        Mockito.when(mealRepository.findAllByUserId(pageable, id)).thenReturn(page);
        Mockito.when(mealMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<MealDtoOutput> actual = mealService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkMealDtoOutputFields);
    }

    @Test
    void testGetDto() {
        // preconditions
        final Meal mealOutput = getPreparedMealOutput();
        final MealDtoOutput mealDtoOutput = getPreparedMealDtoOutput();
        setSecurityContext();
        Mockito.when(mealRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(mealOutput));
        Mockito.when(mealMapper.outputMapping(mealOutput)).thenReturn(mealDtoOutput);

        //test
        MealDtoOutput actual = mealService.getDto(id);

        // assert
        assertNotNull(actual);
        checkMealDtoOutputFields(actual);
    }

    @Test
    void updateDto() {
        // preconditions
        final Meal mealInput = getPreparedMealInput();
        final Meal mealOutput = getPreparedMealOutput();
        final MealDtoInput dtoInput = getPreparedMealDtoInput();
        final MealDtoOutput dtoOutput = getPreparedMealDtoOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        final Product product = getPreparedProductOutput();
        Mockito.when(mealMapper.inputMapping(dtoInput, userDetails)).thenReturn(mealInput);
        Mockito.when(mealRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(mealInput));
        Mockito.when(productService.getByIds(Collections.singletonList(id))).thenReturn(Collections.singletonList(product));
        Mockito.when(mealRepository.save(mealInput)).thenReturn(mealOutput);
        Mockito.when(auditMapper.mealOutputMapping(mealOutput, userDetails, textUpdate)).thenReturn(auditDto);
        Mockito.when(mealMapper.outputMapping(mealOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Meal> actualMeal = ArgumentCaptor.forClass(Meal.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        MealDtoOutput actual = mealService.updateDto(dtoInput, id, dtUpdate.toEpochMilli());
        Mockito.verify(mealValidator, Mockito.times(1)).validateEntity(actualMeal.capture());
        Mockito.verify(mealValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualMeal.capture());
        Mockito.verify(transactionalService, Mockito.times(1)).deleteTransactional(id, id);
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(mealInput, actualMeal.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkMealDtoOutputFields(actual);
    }

    @Test
    void deleteDto() {
        // preconditions
        setSecurityContext();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(auditMapper.mealOutputMapping(any(Meal.class), any(MyUserDetails.class), any(String.class)))
                .thenReturn(auditDto);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        mealService.deleteDto(id);
        Mockito.verify(transactionalService, Mockito.times(1)).deleteTransactional(id, id);
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

    private Ingredient getPreparedIngredientOutput() {
        Product dtoOutput = getPreparedProductOutput();
        return Ingredient.builder()
                .product(dtoOutput)
                .weight(weight)
                .productId(id)
                .build();
    }

    private Meal getPreparedMealOutput() {
        List<Ingredient> ingredients = Collections.singletonList(getPreparedIngredientOutput());
        return Meal.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .ingredients(ingredients)
                .id(id)
                .build();
    }

    private Meal getPreparedMealInput() {
        List<Ingredient> ingredients = Collections.singletonList(getPreparedIngredientOutput());
        return Meal.builder()
                .title(title)
                .ingredients(ingredients)
                .build();
    }

    private ProductReferenceDtoInput getPreparedProductReferenceDtoInput() {
        return ProductReferenceDtoInput.builder()
                .id(id.toString())
                .build();
    }

    private IngredientDtoInput getPreparedIngredientDtoInput() {
        ProductReferenceDtoInput dtoInput = getPreparedProductReferenceDtoInput();
        return IngredientDtoInput.builder()
                .product(dtoInput)
                .weight(weight)
                .build();
    }

    private MealDtoInput getPreparedMealDtoInput() {
        Set<IngredientDtoInput> ingredients = Collections.singleton(getPreparedIngredientDtoInput());
        return MealDtoInput.builder()
                .title(title)
                .composition(ingredients)
                .build();
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

    private IngredientDtoOutput getPreparedIngredientDtoOutput() {
        ProductDtoOutput dtoOutput = getPreparedProductDtoOutput();
        return IngredientDtoOutput.builder()
                .product(dtoOutput)
                .weight(weight)
                .build();
    }

    private MealDtoOutput getPreparedMealDtoOutput() {
        List<IngredientDtoOutput> ingredients = Collections.singletonList(getPreparedIngredientDtoOutput());
        return MealDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .composition(ingredients)
                .id(id.toString())
                .build();
    }

    PageDtoOutput<MealDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<MealDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedMealDtoOutput()))
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

    private void checkMealOutputFields(Meal actual) {
        assertEquals(id, actual.getId());
        assertEquals(title, actual.getTitle());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
        for (Ingredient ingredient : actual.getIngredients()) {
            assertEquals(weight, ingredient.getWeight());
            assertEquals(id, ingredient.getProduct().getId());
            assertEquals(weight, ingredient.getProduct().getWeight());
            assertEquals(carbohydrates, ingredient.getProduct().getCarbohydrates());
            assertEquals(fats, ingredient.getProduct().getFats());
            assertEquals(proteins, ingredient.getProduct().getProteins());
            assertEquals(calories, ingredient.getProduct().getCalories());
            assertEquals(title, ingredient.getProduct().getTitle());
            assertEquals(dtCreate, ingredient.getProduct().getDtCreate());
            assertEquals(dtUpdate, ingredient.getProduct().getDtUpdate());
        }
    }

    private void checkMealDtoOutputFields(MealDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(title, actual.getTitle());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
        for (IngredientDtoOutput ingredient : actual.getComposition()) {
            assertEquals(weight, ingredient.getWeight());
            assertEquals(id.toString(), ingredient.getProduct().getId());
            assertEquals(weight, ingredient.getProduct().getWeight());
            assertEquals(carbohydrates, ingredient.getProduct().getCarbohydrates());
            assertEquals(fats, ingredient.getProduct().getFats());
            assertEquals(proteins, ingredient.getProduct().getProteins());
            assertEquals(calories, ingredient.getProduct().getCalories());
            assertEquals(title, ingredient.getProduct().getTitle());
            assertEquals(dtCreate, ingredient.getProduct().getDtCreate());
            assertEquals(dtUpdate, ingredient.getProduct().getDtUpdate());
        }
    }

    private void checkPageDtoOutputFields(PageDtoOutput<MealDtoOutput> actual) {
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