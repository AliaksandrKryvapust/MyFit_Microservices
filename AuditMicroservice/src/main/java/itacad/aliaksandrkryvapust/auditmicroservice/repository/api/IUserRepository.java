package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
}
