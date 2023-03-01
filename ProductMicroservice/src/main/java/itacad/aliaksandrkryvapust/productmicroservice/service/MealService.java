package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.MealMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.transactional.api.IMealTransactionalService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IMealValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService implements IMealService, IMealManager {
    private final static String MEAL_POST = "New meal was created";
    private final static String MEAL_PUT = "Meal was updated";
    private final static String MEAL_DELETE = "Meal was deleted";
    private final IMealRepository mealRepository;
    private final IProductService productService;
    private final IMealValidator mealValidator;
    private final IMealTransactionalService mealTransactionalService;
    private final MealMapper mealMapper;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Override
    public Meal save(Meal meal) {
        setProductsFromDatabase(meal);
        return mealRepository.save(meal);
    }

    @Override
    public Page<Meal> get(Pageable pageable, UUID userId) {
        return mealRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Meal get(UUID id, UUID userId) {
        return mealRepository.findByIdAndUserId(id, userId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Meal update(Meal meal, UUID id, Long version, UUID userId) {
        Meal currentEntity = get(id, userId);
        mealValidator.optimisticLockCheck(version, currentEntity);
        delete(id, userId);
        return save(meal);
    }

    @Override
    public void delete(UUID id, UUID userId) {
        mealTransactionalService.deleteTransactional(id, userId);
    }

    private void setProductsFromDatabase(Meal meal) {
        List<UUID> productIds = meal.getIngredients().stream()
                .map(Ingredient::getProductId)
                .collect(Collectors.toList());
        List<Product> currentProducts = productService.getByIds(productIds);
        for (Ingredient ingredient : meal.getIngredients()) {
            Product product = currentProducts.stream()
                    .filter((i) -> i.getId().equals(ingredient.getProductId()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            ingredient.setProduct(product);
        }
    }

    @Override
    public MealDtoOutput saveDto(MealDtoInput dtoInput) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal entityToSave = mealMapper.inputMapping(dtoInput, userDetails);
        mealValidator.validateEntity(entityToSave);
        Meal meal = save(entityToSave);
        prepareAudit(meal, userDetails, MEAL_POST);
        return mealMapper.outputMapping(meal);
    }

    @Override
    public PageDtoOutput<MealDtoOutput> getDto(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Meal> page = get(pageable, userDetails.getId());
        return mealMapper.outputPageMapping(page);
    }

    @Override
    public MealDtoOutput getDto(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal meal = get(id, userDetails.getId());
        return mealMapper.outputMapping(meal);
    }

    @Override
    public MealDtoOutput updateDto(MealDtoInput dtoInput, UUID id, Long version) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal entityToSave = mealMapper.inputMapping(dtoInput, userDetails);
        mealValidator.validateEntity(entityToSave);
        Meal meal = update(entityToSave, id, version, userDetails.getId());
        prepareAudit(meal, userDetails, MEAL_PUT);
        return mealMapper.outputMapping(meal);
    }

    @Override
    public void deleteDto(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        delete(id, userDetails.getId());
        prepareAudit(Meal.builder().id(id).build(), userDetails, MEAL_DELETE);
    }

    private void prepareAudit(Meal meal, MyUserDetails userDetails, String mealMethod) {
        AuditDto auditDto = auditMapper.mealOutputMapping(meal, userDetails, mealMethod);
        auditManager.audit(auditDto);
    }
}
