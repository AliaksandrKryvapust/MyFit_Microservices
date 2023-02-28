package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IIngredientValidator;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Component
@RequiredArgsConstructor
public class IngredientValidator implements IIngredientValidator {
    private final IProductValidator productValidator;

    @Override
    public void validateEntity(Ingredient ingredient) {
        checkAuxiliaryFields(ingredient);
        checkProduct(ingredient);
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

    private void checkProduct(Ingredient ingredient) {
        if (ingredient.getProduct() == null) {
            throw new IllegalArgumentException("product is not valid for ingredient:" + ingredient);
        }
        productValidator.validateEntity(ingredient.getProduct());
    }
}
