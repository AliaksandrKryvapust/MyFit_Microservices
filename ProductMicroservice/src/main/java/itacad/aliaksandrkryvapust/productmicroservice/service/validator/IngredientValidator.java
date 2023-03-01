package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IIngredientValidator;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Component
public class IngredientValidator implements IIngredientValidator {

    @Override
    public void validateEntity(Ingredient ingredient) {
        checkAuxiliaryFields(ingredient);
        checkProductId(ingredient);
        checkWeight(ingredient);
    }

    @Override
    public void optimisticLockCheck(Long version, Ingredient currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("ingredient table update failed, version does not match update denied");
        }
    }

    private void checkAuxiliaryFields(Ingredient ingredient) {
        if (ingredient.getId() != null) {
            throw new IllegalStateException("Ingredient id should be empty for ingredient: " + ingredient);
        }
        if (ingredient.getDtUpdate() != null) {
            throw new IllegalStateException("Ingredient version should be empty for ingredient: " + ingredient);
        }
        if (ingredient.getDtCreate() != null) {
            throw new IllegalStateException("Ingredient creation date should be empty for ingredient: " + ingredient);
        }
    }

    private void checkWeight(Ingredient ingredient) {
        if (ingredient.getWeight() == null || ingredient.getWeight() <= 0) {
            throw new IllegalArgumentException("weight is not valid for ingredient:" + ingredient);
        }
    }

    private void checkProductId(Ingredient ingredient) {
        if (ingredient.getProductId() == null) {
            throw new IllegalArgumentException("product id is not valid for ingredient:" + ingredient);
        }
    }
}
