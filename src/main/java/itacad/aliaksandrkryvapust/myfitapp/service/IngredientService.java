package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IIngredientRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IIngredientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IngredientService implements IIngredientService {

    private final IIngredientRepository ingredientRepository;

    public IngredientService(IIngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public Ingredient save(Ingredient ingredient) {
        if (ingredient.getId() != null) {
            throw new IllegalStateException("Ingredient id should be empty");
        }
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> get() {
        return this.ingredientRepository.findAll();
    }

    @Override
    public Ingredient get(UUID id) {
        return this.ingredientRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
    this.ingredientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ingredient update(Ingredient ingredient, UUID id, Instant version) {
        if (ingredient.getId() != null) {
            throw new IllegalStateException("Ingredient id should be empty");
        }
        Ingredient currentEntity = this.ingredientRepository.findById(id).orElseThrow();
        if (!currentEntity.getDtUpdate().equals(version)) {
            throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
        }
        return this.ingredientRepository.save(ingredient);
    }
}
