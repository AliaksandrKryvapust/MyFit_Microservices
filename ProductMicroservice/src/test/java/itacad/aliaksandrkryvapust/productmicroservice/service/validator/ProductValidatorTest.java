package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
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
class ProductValidatorTest {
    @InjectMocks
    private ProductValidator productValidator;

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
        final Product product = getPreparedProductOutput();
        final String messageExpected = "Product id should be empty for product: " + product;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyWeight() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(null)
                .build();
        final String messageExpected = "weight is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeWeight() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(-1)
                .build();
        final String messageExpected = "weight is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyCarbohydrates() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(null)
                .weight(weight)
                .build();
        final String messageExpected = "carbohydrates is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeCarbohydrates() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(-1d)
                .weight(weight)
                .build();
        final String messageExpected = "carbohydrates is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyFats() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(null)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "fats is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeFats() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(-1d)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "fats is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyProteins() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(null)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "proteins is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeProteins() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(calories)
                .proteins(-1d)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "proteins is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyCalories() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(null)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "calories is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeCalories() {
        // preconditions
        final Product product = Product.builder()
                .title(title)
                .calories(-1)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "calories is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyTitle() {
        // preconditions
        final Product product = Product.builder()
                .title("  ")
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "title is not valid for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateShortTitle() {
        // preconditions
        final Product product = Product.builder()
                .title("a")
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String messageExpected = "title should contain from 2 to 50 letters for product:" + product;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> productValidator.validateEntity(product));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void optimisticLockCheck() {
        // preconditions
        final Product product = getPreparedProductOutput();
        final Long version = dtUpdate.toEpochMilli() - 1000;
        final String messageExpected = "product table update failed, version does not match update denied";

        //test
        Exception actualException = assertThrows(OptimisticLockException.class, () ->
                productValidator.optimisticLockCheck(version, product));

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
}