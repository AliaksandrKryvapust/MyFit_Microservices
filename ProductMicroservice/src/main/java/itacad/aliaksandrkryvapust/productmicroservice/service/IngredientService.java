package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IIngredientRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.UUID;

@Service
public class IngredientService implements IIngredientService {

    private final IIngredientRepository ingredientRepository;
    @Autowired
    public IngredientService(IIngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public Ingredient save(Ingredient ingredient) {
        validateInput(ingredient);
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public Page<Ingredient> get(Pageable pageable, UUID userId) {
        return this.ingredientRepository.findAll(pageable);
    }

    @Override
    public Ingredient get(UUID id, UUID userId) {
        return this.ingredientRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.ingredientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ingredient update(Ingredient ingredient, UUID id, Long version) {
        validateInput(ingredient);
        Ingredient currentEntity = this.ingredientRepository.findById(id).orElseThrow();
        checkOptimisticLock(version, currentEntity);
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteAllByMealId(UUID uuid) {
        this.ingredientRepository.deleteAllByMealId(uuid);
    }

    private void checkOptimisticLock(Long version, Ingredient currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
        }
    }

    private void validateInput(Ingredient ingredient) {
        if (ingredient.getProductId() != null) {
            throw new IllegalStateException("Ingredient id should be empty");
        }
    }
}
