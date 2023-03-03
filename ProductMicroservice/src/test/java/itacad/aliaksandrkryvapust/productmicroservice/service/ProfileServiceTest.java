package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.EType;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProfileMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProfileRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.*;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProfileValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;
    @Mock
    private IProfileRepository profileRepository;
    @Mock
    private IProfileValidator profileValidator;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New profile was created";
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Double weight = 100d;
    final String dt_Supply = "1993-07-01";
    final Integer height = 173;
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final LocalDate dtBirthday = LocalDate.parse("1993-07-01", df);
    final String dt_Birthday = "1993-07-01";
    final Double target = 85.9;
    final EActivityType activity_type = EActivityType.ACTIVE;
    final EProfileSex sex = EProfileSex.MALE;
    final String username = "someone";

    @Test
    void save() {
        // preconditions
        final Profile profileInput = getPreparedProfileInput();
        final Profile profileOutput = getPreparedProfileOutput();
        Mockito.when(profileRepository.save(profileInput)).thenReturn(profileOutput);

        //test
        Profile actual = profileService.save(profileInput);

        // assert
        assertNotNull(actual);
        checkProfileOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final Profile profileOutput = getPreparedProfileOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Profile> page = new PageImpl<>(Collections.singletonList(profileOutput), pageable, 1);
        Mockito.when(profileRepository.findAllByUser_UserId(pageable, id)).thenReturn(page);

        //test
        Page<Profile> actual = profileService.get(pageable, id);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Profile profile : actual.getContent()) {
            checkProfileOutputFields(profile);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final Profile profileOutput = getPreparedProfileOutput();
        Mockito.when(profileRepository.findByIdAndUser_UserId(id, id)).thenReturn(Optional.of(profileOutput));

        //test
        Profile actual = profileService.get(id, id);

        // assert
        assertNotNull(actual);
        checkProfileOutputFields(actual);
    }

    @Test
    void saveDto() {
        // preconditions
        final ProfileDtoInput dtoInput = getPreparedProfileDtoInput();
        final ProfileDtoOutput dtoOutput = getPreparedProfileDtoOutput();
        final Profile profileInput = getPreparedProfileInput();
        final Profile profileOutput = getPreparedProfileOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        Mockito.when(profileMapper.inputMapping(dtoInput, userDetails)).thenReturn(profileInput);
        Mockito.when(profileRepository.save(profileInput)).thenReturn(profileOutput);
        Mockito.when(auditMapper.profileOutputMapping(profileOutput, userDetails, text)).thenReturn(auditDto);
        Mockito.when(profileMapper.outputMapping(profileOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Profile> actualProfile = ArgumentCaptor.forClass(Profile.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        ProfileDtoOutput actual = profileService.saveDto(dtoInput);
        Mockito.verify(profileValidator, Mockito.times(1)).validateEntity(actualProfile.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(profileInput, actualProfile.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkProfileDtoOutputFields(actual);
    }

    @Test
    void getDto() {
        // preconditions
        final Profile profileOutput = getPreparedProfileOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Profile> page = new PageImpl<>(Collections.singletonList(profileOutput), pageable, 1);
        final PageDtoOutput<ProfileDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        setSecurityContext();
        Mockito.when(profileRepository.findAllByUser_UserId(pageable, id)).thenReturn(page);
        Mockito.when(profileMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<ProfileDtoOutput> actual = profileService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkProfileDtoOutputFields);
    }

    @Test
    void testGetDto() {
        // preconditions
        final Profile profileOutput = getPreparedProfileOutput();
        final ProfileDtoOutput profileDtoOutput = getPreparedProfileDtoOutput();
        setSecurityContext();
        Mockito.when(profileRepository.findByIdAndUser_UserId(id, id)).thenReturn(Optional.of(profileOutput));
        Mockito.when(profileMapper.outputMapping(profileOutput)).thenReturn(profileDtoOutput);

        //test
        ProfileDtoOutput actual = profileService.getDto(id);

        // assert
        assertNotNull(actual);
        checkProfileDtoOutputFields(actual);
    }

    private MyUserDetails setSecurityContext() {
        final MyUserDetails userDetails = getPreparedUserDetails();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        return userDetails;
    }

    private Profile getPreparedProfileInput() {
        return Profile.builder()
                .user(getPreparedUserOutput())
                .sex(sex)
                .activityType(activity_type)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
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

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .version(dtUpdate)
                .username(email)
                .user_id(id.toString())
                .build();
    }

    private ProfileDtoOutput getPreparedProfileDtoOutput() {
        return ProfileDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .user(getPreparedUserDtoOutput())
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .id(id.toString())
                .build();
    }

    private ProfileDtoInput getPreparedProfileDtoInput() {
        return ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
    }

    private UserDto getPreparedUserDto() {
        return UserDto.builder()
                .role(EUserRole.USER.name())
                .email(email)
                .id(id.toString())
                .build();
    }

    private AuditDto getPreparedAuditDto() {
        return AuditDto.builder()
                .id(String.valueOf(id))
                .user(getPreparedUserDto())
                .text(text)
                .type(EType.PRODUCT.name())
                .build();
    }

    private MyUserDetails getPreparedUserDetails() {
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(id)
                .username(email)
                .authenticated(true)
                .role(EUserRole.USER)
                .dtUpdate(dtUpdate)
                .build();
        return new MyUserDetails(userPrincipal);
    }

    PageDtoOutput<ProfileDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<ProfileDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedProfileDtoOutput()))
                .build();
    }

    private void checkProfileOutputFields(Profile actual) {
        assertEquals(id, actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(sex, actual.getSex());
        assertEquals(activity_type, actual.getActivityType());
        assertEquals(target, actual.getTarget());
        assertEquals(dtBirthday, actual.getDtBirthday());
        assertEquals(weight, actual.getWeight());
        assertEquals(height, actual.getHeight());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
        assertEquals(id, actual.getUser().getUserId());
        assertEquals(email, actual.getUser().getUsername());
        assertEquals(dtUpdate, actual.getUser().getVersion());
    }

    private void checkProfileDtoOutputFields(ProfileDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(sex.name(), actual.getSex());
        assertEquals(activity_type.name(), actual.getActivityType());
        assertEquals(target, actual.getTarget());
        assertEquals(dtBirthday, actual.getDtBirthday());
        assertEquals(weight, actual.getWeight());
        assertEquals(height, actual.getHeight());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
        assertEquals(id.toString(), actual.getUser().getUser_id());
        assertEquals(email, actual.getUser().getUsername());
        assertEquals(dtUpdate, actual.getUser().getVersion());
    }
    private void checkPageDtoOutputFields(PageDtoOutput<ProfileDtoOutput> actual) {
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.getFirst());
        Assertions.assertTrue(actual.getLast());
        assertEquals(2, actual.getNumber());
        assertEquals(1, actual.getNumberOfElements());
        assertEquals(1, actual.getSize());
        assertEquals(1, actual.getTotalPages());
        assertEquals(1, actual.getTotalElements());
    }

}