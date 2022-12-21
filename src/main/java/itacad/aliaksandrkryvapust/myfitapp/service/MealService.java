package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IIngredientService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IMealService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Meal> get(Pageable pageable) {
        return this.mealRepository.findAll(pageable);
    }

    @Override
    public Meal get(UUID id) {
        return this.mealRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.ingredientService.deleteAllByMealId(id);
        this.mealRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Meal update(Meal meal, UUID id, Long version) {
        Meal currentEntity = this.mealRepository.findById(id).orElseThrow();
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("meal table update failed, version does not match update denied");
        }
        this.delete(id);
        return this.save(meal);
    }
}
