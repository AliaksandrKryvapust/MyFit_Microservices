package itacad.aliaksandrkryvapust.usermicroservice.repository.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);
}

