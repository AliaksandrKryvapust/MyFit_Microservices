package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.controller.exceptions.ExpiredEmailTokenException;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserStatus;
import itacad.aliaksandrkryvapust.myfitapp.service.api.ITokenService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.NoSuchElementException;

@Component
public class TokenManager implements ITokenManager {
    private static final int EXPIRATION_TIME = 24 * 60 * 60;
    private final ITokenService tokenService;
    private final IUserService userService;
    private final TokenMapper tokenMapper;

    public TokenManager(ITokenService tokenService, IUserService userService, TokenMapper tokenMapper) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenMapper = tokenMapper;
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
        User user = emailToken.getUser();
        user.setStatus(UserStatus.ACTIVATED);
        userService.save(user);
    }

    private void validate(String token, EmailToken emailToken) {
        if (emailToken == null) {
            throw new NoSuchElementException("Token is not exist " + token);
        } else if ((Instant.now().getEpochSecond() - emailToken.getDtUpdate().getEpochSecond()) >= EXPIRATION_TIME) {
            throw new ExpiredEmailTokenException("Token has been expired");
        }
    }
}
