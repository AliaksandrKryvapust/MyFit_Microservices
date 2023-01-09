package itacad.aliaksandrkryvapust.usermicroservice.repository.api;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}

