package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IIngredientService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MealService implements IMealService {
    private final IMealRepository mealRepository;
    private final IProductService productService;
    private final IIngredientService ingredientService;

    @Autowired
    public MealService(IMealRepository mealRepository, IProductService productService, IIngredientService ingredientService) {
        this.mealRepository = mealRepository;
        this.productService = productService;
        this.ingredientService = ingredientService;
    }

    @Override
    @Transactional
    public Meal save(Meal meal) {
        validateInput(meal);
        setProductsFromDatabase(meal);
        this.mealRepository.save(meal);
        return meal;
    }

    @Override
    public Page<Meal> get(Pageable pageable, UUID userId) {
        return this.mealRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Meal get(UUID id, UUID userId) {
        return this.mealRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Meal currentEntity = this.mealRepository.findById(id).orElseThrow();
        this.checkCredentials(currentEntity);
        this.ingredientService.deleteAllByMealId(id);
        this.mealRepository.deleteById(id);
    }

    private void checkCredentials(Meal currentEntity) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentEntity.getUserId().equals(userDetails.getId())){
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }

    @Override
    @Transactional
    public Meal update(Meal meal, UUID id, Long version) {
        this.validateInput(meal);
        Meal currentEntity = this.mealRepository.findById(id).orElseThrow();
        this.checkOptimisticLock(version, currentEntity);
        this.checkCredentials(currentEntity);
        this.delete(id);
        return this.save(meal);
    }

    private void checkOptimisticLock(Long version, Meal currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("meal table update failed, version does not match update denied");
        }
    }

    private void setProductsFromDatabase(Meal meal) {
        List<UUID> productIds = meal.getIngredients().stream().map(Ingredient::getProductId)
                .collect(Collectors.toList());
        List<Product> currentProducts = this.productService.getByIds(productIds);
        for (Ingredient ingredient : meal.getIngredients()) {
            Product product = currentProducts.stream().filter((i) -> i.getId().equals(ingredient.getProductId()))
                    .findFirst().orElseThrow();
            ingredient.setProduct(product);
        }
    }

    private void validateInput(Meal meal) {
        if (meal.getId() != null || meal.getDtUpdate() != null) {
            throw new IllegalStateException("Meal id & version should be empty");
        }
    }
}
