package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IIngredientValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MealValidatorTest {
    @InjectMocks
    private MealValidator mealValidator;
    @Mock
    private IIngredientValidator ingredientValidator;
    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Integer weight = 100;

    @Test
    void validateNonEmptyId() {
        // preconditions
        final Meal meal = getPreparedMealOutput();
        final String messageExpected = "Meal id should be empty for meal: " + meal;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> mealValidator.validateEntity(meal));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyTitle() {
        // preconditions
        final Meal meal = Meal.builder()
                .title("   ")
                .ingredients(Collections.singletonList(getPreparedIngredientOutput()))
                .build();
        final String messageExpected = "title is not valid for meal:" + meal;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> mealValidator.validateEntity(meal));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateShortTitle() {
        // preconditions
        final Meal meal = Meal.builder()
                .title("a")
                .ingredients(Collections.singletonList(getPreparedIngredientOutput()))
                .build();
        final String messageExpected = "title should contain from 2 to 50 letters for meal:" + meal;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> mealValidator.validateEntity(meal));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyIngredients() {
        // preconditions
        final Meal meal = Meal.builder()
                .title(title)
                .ingredients(null)
                .build();
        final String messageExpected = "Ingredients is not valid for meal:" + meal;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> mealValidator.validateEntity(meal));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void optimisticLockCheck() {
        // preconditions
        final Meal meal = getPreparedMealOutput();
        final Long version = dtUpdate.toEpochMilli() - 1000;
        final String messageExpected = "meal table update failed, version does not match update denied";

        //test
        Exception actualException = assertThrows(OptimisticLockException.class, () ->
                mealValidator.optimisticLockCheck(version, meal));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
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
}