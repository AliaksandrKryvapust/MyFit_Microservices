package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IEmailTokenRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import itacad.aliaksandrkryvapust.myfitapp.service.api.ITokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService implements ITokenService {
    private final IEmailTokenRepository tokenRepository;

    public TokenService(IEmailTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void save(EmailToken token) {
        this.tokenRepository.save(token);
    }

    @Override
    public EmailToken getToken(String token) {
        return this.tokenRepository.getEmailTokenByToken(token);
    }
}
