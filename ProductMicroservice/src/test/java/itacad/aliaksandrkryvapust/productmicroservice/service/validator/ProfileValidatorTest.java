package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileValidatorTest {
    @InjectMocks
    private ProfileValidator profileValidator;
    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New profile was created";
    final Double weight = 100d;
    final Integer height = 173;
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final LocalDate dtBirthday = LocalDate.parse("1993-07-01", df);
    final Double target = 85.9;
    final EActivityType activity_type = EActivityType.ACTIVE;
    final EProfileSex sex = EProfileSex.MALE;

    @Test
    void validateNonEmptyId() {
        // preconditions
        final Profile profile = getPreparedProfileOutput();
        final String messageExpected = "profile id should be empty for profile: " + profile;

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptySex() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(null)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "sex is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyActivityType() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(null)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "activity type is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyTarget() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(null)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "target is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeTarget() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(-1d)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "target is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyBirthdayDate() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(null)
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "birthday date is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateFutureBirthdayDate() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(LocalDate.parse("2233-07-01", df))
                .weight(weight)
                .height(height)
                .build();
        final String messageExpected = "birthday date is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyWeight() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(null)
                .height(height)
                .build();
        final String messageExpected = "weight is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeWeight() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(-1d)
                .height(height)
                .build();
        final String messageExpected = "weight is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateEmptyHeight() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(null)
                .build();
        final String messageExpected = "height is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void validateNegativeHeight() {
        // preconditions
        final Profile profile = Profile.builder()
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(-1)
                .build();
        final String messageExpected = "height is not valid for profile:" + profile;

        //test
        Exception actualException = assertThrows(IllegalArgumentException.class, () -> profileValidator.validateEntity(profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    @Test
    void optimisticLockCheck() {
        // preconditions
        final Profile profile = getPreparedProfileOutput();
        final Long version = dtUpdate.toEpochMilli() - 1000;
        final String messageExpected = "profile table update failed, version does not match update denied";

        //test
        Exception actualException = assertThrows(OptimisticLockException.class, () ->
                profileValidator.optimisticLockCheck(version, profile));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    private User getPreparedUserOutput() {
        return User.builder()
                .version(dtUpdate)
                .username(email)
                .userId(id)
                .build();
    }

    private Profile getPreparedProfileOutput() {
        return Profile.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .user(getPreparedUserOutput())
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .id(id)
                .build();
    }
}