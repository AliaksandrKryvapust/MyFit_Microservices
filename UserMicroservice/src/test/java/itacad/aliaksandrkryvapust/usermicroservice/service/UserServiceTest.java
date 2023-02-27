package itacad.aliaksandrkryvapust.usermicroservice.service;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.IUserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IUserValidator userValidator;
    @Mock
    private UserMapper userMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final LocalDate dtLogin = LocalDate.parse("04/04/2023", df);
    final String email = "admin@myfit.com";
    final String username = "someone";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String password = "kdrL556D";
    final String text = "New user was created";
    final String textUpdate = "User was updated";

    @Test
    void save() {
        // preconditions
        final User userInput = getPreparedUserInput();
        final User userOutput = getPreparedUserOutput();
        Mockito.when(userRepository.save(userInput)).thenReturn(userOutput);

        //test
        User actual = userService.save(userInput);

        // assert
        assertNotNull(actual);
        checkUserOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<User> page = new PageImpl<>(Collections.singletonList(userOutput), pageable, 1);
        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);

        //test
        Page<User> actual = userService.get(pageable);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (User user : actual.getContent()) {
            checkUserOutputFields(user);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userOutput));

        //test
        User actual = userService.get(id);

        // assert
        assertNotNull(actual);
        checkUserOutputFields(actual);
    }

    @Test
    void update() {
        // preconditions
        final User userInput = getPreparedUserOutput();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userInput));
        Mockito.when(userRepository.save(userInput)).thenReturn(userInput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<User> actualUser = ArgumentCaptor.forClass(User.class);

        //test
        User actual = userService.update(userInput, id, dtUpdate.toEpochMilli());
        Mockito.verify(userValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualUser.capture());
        Mockito.verify(userMapper, Mockito.times(1)).updateEntityFields(actualUser.capture(),
                actualUser.capture());

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(userInput, actualUser.getValue());
        assertNotNull(actual);
        checkUserOutputFields(actual);
    }

    @Test
    void saveUser() {
        // preconditions
        final UserDtoRegistration dtoInput = getPreparedUserDtoRegistration();
        final UserRegistrationDtoOutput dtoOutput = getPreparedUserRegistrationDtoOutput();
        final User userInput = getPreparedUserInput();
        final User userOutput = getPreparedUserOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(userMapper.userInputMapping(dtoInput)).thenReturn(userInput);
        Mockito.when(userRepository.save(userInput)).thenReturn(userOutput);
        Mockito.when(auditMapper.userOutputMapping(userOutput, text)).thenReturn(auditDto);
        Mockito.when(userMapper.registerOutputMapping(userOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<User> actualUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        UserRegistrationDtoOutput actual = userService.saveUser(dtoInput);
        Mockito.verify(userValidator, Mockito.times(1)).validateEntity(actualUser.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(userInput, actualUser.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        assertEquals(email, actual.getEmail());
        assertEquals(EUserRole.USER.name(), actual.getRole());
    }

    @Test
    void saveDto() {
        // preconditions
        final UserDtoInput dtoInput = getPreparedUserDtoInput();
        final UserDtoOutput dtoOutput = getPreparedUserDtoOutput();
        final User userInput = getPreparedUserInput();
        final User userOutput = getPreparedUserOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(userMapper.inputMapping(dtoInput)).thenReturn(userInput);
        Mockito.when(userRepository.save(userInput)).thenReturn(userOutput);
        Mockito.when(auditMapper.userOutputMapping(userOutput, text)).thenReturn(auditDto);
        Mockito.when(userMapper.outputMapping(userOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<User> actualUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        UserDtoOutput actual = userService.saveDto(dtoInput);
        Mockito.verify(userValidator, Mockito.times(1)).validateEntity(actualUser.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(userInput, actualUser.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkUserDtoOutputFields(actual);
    }

    @Test
    void getDto() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<User> page = new PageImpl<>(Collections.singletonList(userOutput), pageable, 1);
        final PageDtoOutput<UserDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(userMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<UserDtoOutput> actual = userService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        for (UserDtoOutput user : actual.getContent()) {
            checkUserDtoOutputFields(user);
        }
    }

    @Test
    void testGetDto() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final UserDtoOutput userDtoOutput = getPreparedUserDtoOutput();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userOutput));
        Mockito.when(userMapper.outputMapping(userOutput)).thenReturn(userDtoOutput);

        //test
        UserDtoOutput actual = userService.getDto(id);

        // assert
        assertNotNull(actual);
        checkUserDtoOutputFields(actual);
    }

    @Test
    void updateDto() {
        // preconditions
        final User userInput = getPreparedUserOutput();
        final User userOutput = getPreparedUserOutput();
        final UserDtoInput dtoInput = getPreparedUserDtoInput();
        final UserDtoOutput dtoOutput = getPreparedUserDtoOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(userMapper.inputMapping(dtoInput)).thenReturn(userInput);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userInput));
        Mockito.when(userRepository.save(userInput)).thenReturn(userOutput);
        Mockito.when(auditMapper.userOutputMapping(userOutput, textUpdate)).thenReturn(auditDto);
        Mockito.when(userMapper.outputMapping(userOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Long> actualVersion = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<User> actualUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        UserDtoOutput actual = userService.updateDto(dtoInput, id, dtUpdate.toEpochMilli());
        Mockito.verify(userValidator, Mockito.times(1)).validateEntity(actualUser.capture());
        Mockito.verify(userValidator, Mockito.times(1)).optimisticLockCheck(actualVersion.capture(),
                actualUser.capture());
        Mockito.verify(userMapper, Mockito.times(1)).updateEntityFields(actualUser.capture(),
                actualUser.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(dtUpdate.toEpochMilli(), actualVersion.getValue());
        assertEquals(userInput, actualUser.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkUserDtoOutputFields(actual);
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

    User getPreparedUserInput() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .role(EUserRole.USER)
                .status(EUserStatus.ACTIVATED)
                .build();
    }

    UserDtoRegistration getPreparedUserDtoRegistration() {
        return UserDtoRegistration.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }

    UserRegistrationDtoOutput getPreparedUserRegistrationDtoOutput() {
        return UserRegistrationDtoOutput.builder()
                .email(email)
                .role(EUserRole.USER.name())
                .build();
    }

    UserDtoInput getPreparedUserDtoInput() {
        return UserDtoInput.builder()
                .email(email)
                .password(password)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .build();
    }

    UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .id(id.toString())
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .dtLogin(dtLogin)
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .build();
    }

    PageDtoOutput<UserDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<UserDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedUserDtoOutput()))
                .build();
    }

    private AuditDto getPreparedAuditDto() {
        return AuditDto.builder()
                .id(String.valueOf(id))
                .user(getPreparedUserDtoOutput())
                .text(text)
                .type(Type.USER.name())
                .build();
    }

    private void checkUserOutputFields(User actual) {
        assertEquals(id, actual.getId());
        assertEquals(email, actual.getEmail());
        assertEquals(password, actual.getPassword());
        assertEquals(username, actual.getUsername());
        assertEquals(EUserRole.USER, actual.getRole());
        assertEquals(EUserStatus.ACTIVATED, actual.getStatus());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }

    private void checkUserDtoOutputFields(UserDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(email, actual.getEmail());
        assertEquals(EUserRole.USER.name(), actual.getRole());
        assertEquals(username, actual.getUsername());
        assertEquals(EUserStatus.ACTIVATED.name(), actual.getStatus());
        assertEquals(dtLogin, actual.getDtLogin());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }

    private void checkPageDtoOutputFields(PageDtoOutput<UserDtoOutput> actual) {
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