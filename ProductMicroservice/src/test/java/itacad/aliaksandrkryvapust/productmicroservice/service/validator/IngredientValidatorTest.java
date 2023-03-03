package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class IngredientValidatorTest {
    @InjectMocks
    private IngredientValidator ingredientValidator;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final Integer weight = 100;

    @Test
    void validateNonEmptyId() {
        // preconditions
        final Ingredient ingredient = getPreparedIngredientOutput();
        final String messageExpected = "Ingredient id should be empty for ingredient: " + ingredient;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> ingredientValidator.validateEntity(ingredient));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyWeight() {
        // preconditions
        final Ingredient ingredient = Ingredient.builder()
                .weight(null)
                .productId(id)
                .build();
        final String messageExpected = "weight is not valid for ingredient:" + ingredient;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> ingredientValidator.validateEntity(ingredient));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeWeight() {
        // preconditions
        final Ingredient ingredient = Ingredient.builder()
                .weight(-1)
                .productId(id)
                .build();
        final String messageExpected = "weight is not valid for ingredient:" + ingredient;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> ingredientValidator.validateEntity(ingredient));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyProductId() {
        // preconditions
        final Ingredient ingredient = Ingredient.builder()
                .weight(weight)
                .productId(null)
                .build();
        final String messageExpected = "product id is not valid for ingredient:" + ingredient;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> ingredientValidator.validateEntity(ingredient));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void optimisticLockCheck() {
        // preconditions
        final Ingredient ingredient = getPreparedIngredientOutput();
        final Long version = dtUpdate.toEpochMilli() - 1000;
        final String messageExpected = "ingredient table update failed, version does not match update denied";

        //test
        Exception actualException = assertThrows(OptimisticLockException.class, () ->
                ingredientValidator.optimisticLockCheck(version, ingredient));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    private Ingredient getPreparedIngredientOutput() {
        return Ingredient.builder()
                .id(id)
                .weight(weight)
                .productId(id)
                .dtUpdate(dtUpdate)
                .dtCreate(dtCreate)
                .build();
    }
}