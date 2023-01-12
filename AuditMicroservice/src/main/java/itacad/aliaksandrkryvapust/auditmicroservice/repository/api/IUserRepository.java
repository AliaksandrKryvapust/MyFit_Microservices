package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
}
