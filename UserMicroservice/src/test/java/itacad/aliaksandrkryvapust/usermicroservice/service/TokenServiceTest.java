package itacad.aliaksandrkryvapust.usermicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IEmailTokenRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.ITokenValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks
    TokenService tokenService;
    @Mock
    IEmailTokenRepository tokenRepository;
    @Mock
    TokenMapper tokenMapper;
    @Mock
    ITokenValidator tokenValidator;
    @Mock
    UserMapper userMapper;
    @Mock
    AuditMapper auditMapper;
    @Mock
    AuditService auditService;
    @Mock
    UserService userService;
    @Mock
    ApplicationEventPublisher eventPublisher;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final LocalDate dtLogin = LocalDate.parse("04/04/2023", df);
    final String email = "admin@myfit.com";
    final String username = "someone";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String password = "kdrL556D";
    final String text = "User was activated";
    final int time = 24 * 60 * 60;
    final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";

    @Test
    void saveToken() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final EmailToken emailToken = getPreparedEmailToken();
        Mockito.when(tokenMapper.inputMapping(userOutput, token)).thenReturn(emailToken);
        ArgumentCaptor<EmailToken> actualEmailToken = ArgumentCaptor.forClass(EmailToken.class);

        //test
        tokenService.saveToken(userOutput, token);
        Mockito.verify(tokenRepository, Mockito.times(1)).save(actualEmailToken.capture());

        // assert
        assertEquals(emailToken, actualEmailToken.getValue());
    }

    @Test
    void getToken() {
        // preconditions
        final EmailToken emailToken = getPreparedEmailToken();
        Mockito.when(tokenRepository.getEmailTokenByToken(token)).thenReturn(Optional.of(emailToken));

        //test
        EmailToken actual = tokenService.getToken(token);

        // assert
        assertNotNull(actual.getUser());
        assertEquals(token, actual.getToken());
    }

    @Test
    void activateUser() throws URISyntaxException, JsonProcessingException {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final EmailToken emailToken = getPreparedEmailToken();
        final AuditDto auditDto = getPreparedAuditDto();
        Mockito.when(tokenRepository.getEmailTokenByToken(token)).thenReturn(Optional.of(emailToken));
        Mockito.when(userMapper.activationMapping(any(User.class))).thenReturn(userOutput);
        Mockito.when(auditMapper.userOutputMapping(any(User.class), any(String.class))).thenReturn(auditDto);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        tokenService.activateUser(token);
        Mockito.verify(tokenValidator, Mockito.times(1)).validate(token, emailToken, time);
        Mockito.verify(userService, Mockito.times(1)).update(userOutput, id, dtUpdate.toEpochMilli());
        Mockito.verify(auditService, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(auditDto, actualAudit.getValue());
    }

    @Test
    void resendToken() {
        // preconditions
        final EmailToken emailToken = getPreparedEmailToken();
        Mockito.when(tokenRepository.getEmailTokenByToken(token)).thenReturn(Optional.of(emailToken));
        ArgumentCaptor<EmailToken> actualEmailToken = ArgumentCaptor.forClass(EmailToken.class);

        //test
        tokenService.resendToken(token);
        Mockito.verify(tokenValidator, Mockito.times(1)).validateEmailToken(actualEmailToken.capture());
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(any(ApplicationEvent.class));

        // assert
        assertEquals(emailToken, actualEmailToken.getValue());
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

    EmailToken getPreparedEmailToken() {
        return EmailToken.builder()
                .token(token)
                .user(getPreparedUserOutput())
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
}