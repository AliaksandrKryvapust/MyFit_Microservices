package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IIngredientValidator;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IMealValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Component
@RequiredArgsConstructor
public class MealValidator implements IMealValidator {
    private final IIngredientValidator ingredientValidator;

    @Override
    public void validateEntity(Meal meal) {
        checkAuxiliaryFields(meal);
        checkIngredients(meal);
        checkTitle(meal);
    }

    @Override
    public void optimisticLockCheck(Long version, Meal currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("meal table update failed, version does not match update denied");
        }
    }

    @Override
    public void checkCredentials(Meal currentEntity) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentEntity.getUserId().equals(userDetails.getId())) {
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }


    private void checkAuxiliaryFields(Meal meal) {
        if (meal.getId() != null) {
            throw new IllegalStateException("Meal id should be empty for meal: " + meal);
        }
        if (meal.getDtUpdate() != null) {
            throw new IllegalStateException("Meal version should be empty for meal: " + meal);
        }
        if (meal.getDtCreate() != null) {
            throw new IllegalStateException("Meal creation date should be empty for meal: " + meal);
        }
    }

    private void checkTitle(Meal meal) {
        if (meal.getTitle() == null || meal.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is not valid for meal:" + meal);
        }
        char[] chars = meal.getTitle().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("title should contain from 2 to 50 letters for meal:" + meal);
        }
    }

    private void checkIngredients(Meal meal) {
        if (meal.getIngredients() == null) {
            throw new IllegalArgumentException("Ingredients is not valid for meal:" + meal);
        }
        meal.getIngredients().forEach(ingredientValidator::validateEntity);
    }
}
