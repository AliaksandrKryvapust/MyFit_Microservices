package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.transactional.api.IMealTransactionalService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IMealValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService implements IMealService {
    private final IMealRepository mealRepository;
    private final IProductService productService;
    private final IMealValidator mealValidator;
    private final IMealTransactionalService mealTransactionalService;

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
}
