package itacad.aliaksandrkryvapust.usermicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.controller.exceptions.ExpiredEmailTokenException;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.NoSuchElementException;

@Component
public class TokenManager implements ITokenManager {
    private static final int EXPIRATION_TIME = 24 * 60 * 60;
    private final static String userPut = "User was activated";
    private final ITokenService tokenService;
    private final IUserService userService;
    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;
    private final ApplicationEventPublisher eventPublisher;

    public TokenManager(ITokenService tokenService, IUserService userService, TokenMapper tokenMapper,
                        UserMapper userMapper, AuditMapper auditMapper, AuditManager auditManager,
                        ApplicationEventPublisher eventPublisher) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenMapper = tokenMapper;
        this.userMapper = userMapper;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void saveToken(User user, String token) {
        EmailToken emailToken = this.tokenMapper.inputMapping(user, token);
        this.tokenService.save(emailToken);
    }

    @Override
    public void validateToken(String token) {
        EmailToken emailToken = this.tokenService.getToken(token);
        this.validate(token, emailToken);
        this.activateUser(emailToken);
    }

    @Override
    public void resendToken(String token, HttpServletRequest request) {
        EmailToken emailToken = this.tokenService.getToken(token);
        if (emailToken == null) {
            throw new NoSuchElementException("Token is not exist " + token);
        }
        String appUrl = request.getContextPath();
        this.eventPublisher.publishEvent(new EmailVerificationEvent(appUrl, request.getLocale(), emailToken.getUser()));
    }

    private void activateUser(EmailToken emailToken) {
        try {
            User user = emailToken.getUser();
            user.setStatus(UserStatus.ACTIVATED);
            User userToSave = this.userMapper.activationMapping(user);
            userService.update(userToSave, user.getId(), user.getDtUpdate().toEpochMilli());
            AuditDto auditDto = this.auditMapper.userOutputMapping(user, userPut);
            this.auditManager.audit(auditDto);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private void validate(String token, EmailToken emailToken) {
        if (emailToken == null) {
            throw new NoSuchElementException("Token is not exist " + token);
        } else if ((Instant.now().getEpochSecond() - emailToken.getDtUpdate().getEpochSecond()) >= EXPIRATION_TIME) {
            throw new ExpiredEmailTokenException("Token has been expired");
        }
    }
}
