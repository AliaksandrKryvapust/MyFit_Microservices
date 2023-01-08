package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.myfitapp.controller.exceptions.ExpiredEmailTokenException;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.myfitapp.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserStatus;
import itacad.aliaksandrkryvapust.myfitapp.service.api.ITokenService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import org.springframework.stereotype.Component;

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
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    public TokenManager(ITokenService tokenService, IUserService userService, TokenMapper tokenMapper,
                        AuditMapper auditMapper, AuditManager auditManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenMapper = tokenMapper;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
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

    private void activateUser(EmailToken emailToken) {
        try {
            User user = emailToken.getUser();
            user.setStatus(UserStatus.ACTIVATED);
            userService.save(user);
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
