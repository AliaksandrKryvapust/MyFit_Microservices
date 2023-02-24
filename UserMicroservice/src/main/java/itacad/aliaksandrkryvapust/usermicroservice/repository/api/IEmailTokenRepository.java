package itacad.aliaksandrkryvapust.usermicroservice.repository.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IEmailTokenRepository extends JpaRepository<EmailToken, UUID> {
    @Transactional(readOnly = true)
    Optional<EmailToken> getEmailTokenByToken(String token);
}
