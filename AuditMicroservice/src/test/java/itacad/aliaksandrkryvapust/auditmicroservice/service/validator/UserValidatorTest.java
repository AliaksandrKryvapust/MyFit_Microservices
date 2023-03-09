package itacad.aliaksandrkryvapust.auditmicroservice.service.validator;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    @InjectMocks
    private UserValidator userValidator;

    // preconditions
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final EUserRole role = EUserRole.USER;
    final String email = "admin@myfit.com";

    @Test
    void validateEmptyId() {
        // preconditions
        final User user = User.builder()
                .role(role)
                .email(email)
                .id(null)
                .build();
        final String messageExpected = "User id is empty for user: " + user;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> userValidator.validateEntity(user));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyRole() {
        // preconditions
        final User user = User.builder()
                .role(null)
                .email(email)
                .id(id)
                .build();
        final String messageExpected = "user role is not valid for user:" + user;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> userValidator.validateEntity(user));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyEmail() {
        // preconditions
        final User user = User.builder()
                .role(role)
                .email(null)
                .id(id)
                .build();
        final String messageExpected = "email is not valid for user:" + user;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> userValidator.validateEntity(user));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateShortEmail() {
        // preconditions
        final User user = User.builder()
                .role(role)
                .email("a")
                .id(id)
                .build();
        final String messageExpected = "email should contain from 2 to 50 letters for user:" + user;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> userValidator.validateEntity(user));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }
}