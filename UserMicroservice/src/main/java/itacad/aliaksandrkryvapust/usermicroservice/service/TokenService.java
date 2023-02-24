package itacad.aliaksandrkryvapust.usermicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IEmailTokenRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenService;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.ITokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int EXPIRATION_TIME = 24 * 60 * 60;
    private final static String userPut = "User was activated";
    private final IEmailTokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final ITokenValidator tokenValidator;
    private final UserMapper userMapper;
    private final AuditMapper auditMapper;
    private final AuditService auditService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void saveToken(User user, String token) {
        EmailToken emailToken = tokenMapper.inputMapping(user, token);
        tokenRepository.save(emailToken);
    }

    @Override
    public EmailToken getToken(String token) {
        return tokenRepository.getEmailTokenByToken(token).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void activateUser(String token) {
        EmailToken emailToken = getToken(token);
        tokenValidator.validate(token, emailToken, EXPIRATION_TIME);
        activate(emailToken);
    }

    @Override
    public void resendToken(String token) {
        EmailToken emailToken = getToken(token);
        tokenValidator.validateEmailToken(emailToken);
        eventPublisher.publishEvent(new EmailVerificationEvent(emailToken.getUser()));
    }

    private void activate(EmailToken emailToken) {
        try {
            User user = emailToken.getUser();
            user.setStatus(EUserStatus.ACTIVATED);
            User userToSave = userMapper.activationMapping(user);
            userService.update(userToSave, user.getId(), user.getDtUpdate().toEpochMilli());
            AuditDto auditDto = auditMapper.userOutputMapping(user, userPut);
            auditService.audit(auditDto);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }
}
