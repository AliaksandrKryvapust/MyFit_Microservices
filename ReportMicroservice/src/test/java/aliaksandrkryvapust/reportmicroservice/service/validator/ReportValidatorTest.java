package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.*;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IParamsValidator;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IUserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReportValidatorTest {
    @InjectMocks
    private ReportValidator reportValidator;
    @Mock
    private IUserValidator userValidator;
    @Mock
    private IParamsValidator paramsValidator;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final LocalDate from = LocalDate.parse("2022-12-21");
    final LocalDate to = LocalDate.parse("2023-01-12");
    final String description = "description";
    final EFileType fileType = EFileType.JOURNAL_REPORT;
    final String url = "https://www.example.com";

    @Test
    void validateNonEmptyId() {
        // preconditions
        final Report report = getPreparedReportOutput();
        final String messageExpected = "report id should be empty for report: " + report;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyStatus() {
        // preconditions
        final Report report = Report.builder()
                .status(null)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .user(getPreparedUser())
                .build();
        final String messageExpected = "status is not valid for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyType() {
        // preconditions
        final Report report = Report.builder()
                .status(EStatus.LOADED)
                .type(null)
                .description(description)
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .user(getPreparedUser())
                .build();
        final String messageExpected = "type is not valid for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyDescription() {
        // preconditions
        final Report report = Report.builder()
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(null)
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .user(getPreparedUser())
                .build();
        final String messageExpected = "description is not valid for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateShortDescription() {
        // preconditions
        final Report report = Report.builder()
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description("a")
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .user(getPreparedUser())
                .build();
        final String messageExpected = "description should contain from 2 to 200 letters for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyUser() {
        // preconditions
        final Report report = Report.builder()
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .user(null)
                .file(getPreparedFile())
                .build();
        final String messageExpected = "user is not valid for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyParams() {
        // preconditions
        final Report report = Report.builder()
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(null)
                .user(getPreparedUser())
                .file(getPreparedFile())
                .build();
        final String messageExpected = "params is not valid for report:" + report;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> reportValidator.validateEntity(report));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    private User getPreparedUser() {
        return User.builder()
                .role(EUserRole.USER)
                .username(email)
                .userId(id)
                .build();
    }

    private Params getPreparedParamsOutput() {
        return Params.builder()
                .start(from)
                .finish(to)
                .build();
    }

    private File getPreparedFile() {
        return File.builder()
                .fileType(fileType)
                .contentType(XLSX_CONTENT_TYPE)
                .fileName(id.toString())
                .url(url)
                .build();
    }

    private Report getPreparedReportOutput() {
        return Report.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .id(id)
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .build();
    }
}