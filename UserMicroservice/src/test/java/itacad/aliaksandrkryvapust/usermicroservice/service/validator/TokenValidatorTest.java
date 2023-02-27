package itacad.aliaksandrkryvapust.usermicroservice.service.validator;

import itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions.ExpiredEmailTokenException;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TokenValidatorTest {
    @InjectMocks
    TokenValidator tokenValidator;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final String username = "someone";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String password = "kdrL556D";
    final int time = 24 * 60 * 60;
    final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";


    @Test
    void validateEmptyToken() {
        // preconditions
        final String messageExpected = "Token is not exist " + token;

        //test
        Exception actualException = assertThrows(NoSuchElementException.class,
                () -> tokenValidator.validate(token, null, time));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateExpiredToken() {
        // preconditions
        final EmailToken emailToken = getPreparedEmailToken();
        final String messageExpected = "Token has been expired";

        //test
        Exception actualException = assertThrows(ExpiredEmailTokenException.class,
                () -> tokenValidator.validate(token, emailToken, time));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }


    @Test
    void validateEmailToken() {
        // preconditions
        final String messageExpected = "Token is not exist";

        //test
        Exception actualException = assertThrows(NoSuchElementException.class,
                () -> tokenValidator.validateEmailToken(null));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    EmailToken getPreparedEmailToken() {
        return EmailToken.builder()
                .token(token)
                .user(getPreparedUserOutput())
                .dtUpdate(dtUpdate)
                .build();
    }

    User getPreparedUserOutput() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .username(username)
                .role(EUserRole.USER)
                .status(EUserStatus.ACTIVATED)
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .build();
    }
}