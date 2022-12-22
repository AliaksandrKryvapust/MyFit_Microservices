package itacad.aliaksandrkryvapust.myfitapp.repository.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}

