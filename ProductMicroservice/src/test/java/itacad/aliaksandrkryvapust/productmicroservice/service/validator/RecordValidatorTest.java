package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecordValidatorTest {
    @InjectMocks
    private RecordValidator recordValidator;

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
        final Record record = getPreparedRecordOutput();
        final String messageExpected = "record id should be empty for record: " + record;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> recordValidator.validateEntity(record));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyProductId() {
        // preconditions
        final Record record = Record.builder()
                .weight(weight)
                .dtSupply(dtUpdate)
                .productId(null)
                .build();
        final String messageExpected = "record should contain product or meal: " + record;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> recordValidator.validateEntity(record));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyWeight() {
        // preconditions
        final Record record = Record.builder()
                .weight(null)
                .dtSupply(dtUpdate)
                .productId(id)
                .build();
        final String messageExpected = "weight is not valid for record:" + record;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> recordValidator.validateEntity(record));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeWeight() {
        // preconditions
        final Record record = Record.builder()
                .weight(-1)
                .dtSupply(dtUpdate)
                .productId(id)
                .build();
        final String messageExpected = "weight is not valid for record:" + record;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> recordValidator.validateEntity(record));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptySupplyDate() {
        // preconditions
        final Record record = Record.builder()
                .weight(weight)
                .dtSupply(null)
                .productId(id)
                .build();
        final String messageExpected = "supply date is not valid for record:" + record;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> recordValidator.validateEntity(record));

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

    private Record getPreparedRecordOutput() {
        return Record.builder()
                .dtCreate(dtCreate)
                .dtSupply(dtUpdate)
                .product(getPreparedProductOutput())
                .weight(weight)
                .id(id)
                .build();
    }
}