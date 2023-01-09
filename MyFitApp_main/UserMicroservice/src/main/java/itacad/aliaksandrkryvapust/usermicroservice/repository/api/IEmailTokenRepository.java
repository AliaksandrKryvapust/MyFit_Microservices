package itacad.aliaksandrkryvapust.usermicroservice.repository.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEmailTokenRepository extends JpaRepository<EmailToken, UUID> {
    EmailToken getEmailTokenByToken(String token);
}
