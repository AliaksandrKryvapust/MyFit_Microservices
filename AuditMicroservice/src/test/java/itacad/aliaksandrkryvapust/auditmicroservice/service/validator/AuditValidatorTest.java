package itacad.aliaksandrkryvapust.auditmicroservice.service.validator;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IUserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuditValidatorTest {
    // preconditions
    final String text = "product was added";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final EUserRole role = EUserRole.USER;
    final EType type = EType.PRODUCT;
    final String email = "admin@myfit.com";
    @InjectMocks
    private AuditValidator auditValidator;
    @Mock
    private IUserValidator userValidator;

    @Test
    void validateNonEmptyId() {
        // preconditions
        final Audit audit = getPreparedAuditInput();
        audit.setId(id);
        final String messageExpected = "Audit id should be empty for audit: " + audit;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyActionId() {
        // preconditions
        final Audit audit = Audit.builder()
                .type(type)
                .text(text)
                .user(getPreparedUser())
                .actionId(null)
                .build();
        final String messageExpected = "action id is not valid for audit:" + audit;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyUser() {
        // preconditions
        final Audit audit = Audit.builder()
                .type(type)
                .text(text)
                .user(null)
                .actionId(id)
                .build();
        final String messageExpected = "user is not valid for audit:" + audit;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyText() {
        // preconditions
        final Audit audit = Audit.builder()
                .type(type)
                .text(null)
                .user(getPreparedUser())
                .actionId(id)
                .build();
        final String messageExpected = "text is not valid for audit:" + audit;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateShortText() {
        // preconditions
        final Audit audit = Audit.builder()
                .type(type)
                .text("a")
                .user(getPreparedUser())
                .actionId(id)
                .build();
        final String messageExpected = "text should contain from 2 to 50 letters for audit:" + audit;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyType() {
        // preconditions
        final Audit audit = Audit.builder()
                .type(null)
                .text(text)
                .user(getPreparedUser())
                .actionId(id)
                .build();
        final String messageExpected = "type is not valid for audit:" + audit;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> auditValidator.validateEntity(audit));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    private User getPreparedUser() {
        return User.builder()
                .role(role)
                .email(email)
                .id(id)
                .build();
    }

    private Audit getPreparedAuditInput() {
        return Audit.builder()
                .type(type)
                .text(text)
                .user(getPreparedUser())
                .actionId(id)
                .build();
    }
}