package itacad.aliaksandrkryvapust.myfitapp.repository.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEmailTokenRepository extends JpaRepository<EmailToken, UUID> {
}
