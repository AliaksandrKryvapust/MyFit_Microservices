package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Params;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ParamsValidatorTest {
    @InjectMocks
    private ParamsValidator paramsValidator;

    // preconditions
    final LocalDate from = LocalDate.parse("2022-12-21");
    final LocalDate to = LocalDate.parse("2023-01-12");

    @Test
    void validateEmptyStartDate() {
        // preconditions
        final Params params = Params.builder()
                .start(null)
                .finish(to)
                .build();
        final String messageExpected = "start date can not be null for params:" + params;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> paramsValidator.validateEntity(params));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateFutureStartDate() {
        // preconditions
        final Params params = Params.builder()
                .start(LocalDate.parse("2023-01-13"))
                .finish(to)
                .build();
        final String messageExpected = "start date must refer to moment before end date for params:" + params;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> paramsValidator.validateEntity(params));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyEndDate() {
        // preconditions
        final Params params = Params.builder()
                .start(from)
                .finish(null)
                .build();
        final String messageExpected = "end date can not be null for params:" + params;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> paramsValidator.validateEntity(params));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateFutureEndDate() {
        // preconditions
        final Params params = Params.builder()
                .start(from)
                .finish(LocalDate.parse("2023-05-13"))
                .build();
        final String messageExpected = "end date must refer to moment in the past or present for params:" + params;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> paramsValidator.validateEntity(params));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }
}